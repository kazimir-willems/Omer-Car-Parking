package omer.parking.com.ui;

import android.Manifest;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import omer.parking.com.adapter.OfficeAdapter;
import omer.parking.com.event.GetOfficeListEvent;
import omer.parking.com.event.SetOfficeEvent;
import omer.parking.com.model.OfficeItem;
import omer.parking.com.service.GeofenceTransitionsIntentService;
import omer.parking.com.R;
import omer.parking.com.task.GetOfficeListTask;
import omer.parking.com.task.GetRemainingLotTask;
import omer.parking.com.task.SetOfficeTask;
import omer.parking.com.util.SharedPrefManager;
import omer.parking.com.vo.GetOfficeResponseVo;
import omer.parking.com.vo.SetOfficeResponseVo;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, OnCompleteListener<Void>{

    private ArrayList<Geofence> mGeofenceList = new ArrayList<>();
    private PendingIntent mGeofencePendingIntent = null;

    private GeofencingClient mGeofencingClient;
    private OfficeAdapter adapter;

    private ArrayList<OfficeItem> officeItems = new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;

    @Bind(R.id.office_list)
    RecyclerView officeList;

    private ProgressDialog progressDialog;

    private static final int RADIUS = 400;

    OfficeItem selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        getSupportActionBar().setTitle(R.string.app_name);

        mGeofencingClient = LocationServices.getGeofencingClient(this);

        adapter = new OfficeAdapter(this);

        mLinearLayoutManager = new LinearLayoutManager(MainActivity.this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        officeList.setLayoutManager(mLinearLayoutManager);

        officeList.setAdapter(adapter);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
        startGetOfficeList();
    }

    @Override
    public void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onGetOfficeListEvent(GetOfficeListEvent event) {
        hideProgressDialog();
        GetOfficeResponseVo responseVo = event.getResponse();
        if (responseVo != null) {
            parseOfficeInfo(responseVo.arrays);
        } else {
            networkError();
        }
    }

    @Subscribe
    public void onSetOfficeEvent(SetOfficeEvent event) {
        hideProgressDialog();
        SetOfficeResponseVo responseVo = event.getResponse();
        if (responseVo != null) {
            if(responseVo.success == 1) {
                setOfficeSuccess();
            } else {
                setOfficeFailed();
            }
        } else {
            networkError();
        }
    }

    private void startGetOfficeList() {
        progressDialog.show();

        GetOfficeListTask task = new GetOfficeListTask();
        task.execute();
    }

    private void createGeofenceRequest() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return;
        }

        mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    Log.v("Status", "Added Successfully");
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add geofences
                        // ...
                        Log.v("Status", "Failed");
                    }
                });
    }

    public void createGeofence(final OfficeItem item) {
        mGeofencingClient.removeGeofences(getGeofencePendingIntent()).addOnCompleteListener(this);
    }

    public void addGeofence(double latitude, double longitude) {

        String id = "SFO";
        Geofence fence = new Geofence.Builder()
                .setRequestId(id)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setCircularRegion(latitude, longitude, RADIUS) // Try changing your radius
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
        mGeofenceList.add(fence);

    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {

        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final ArrayList<OfficeItem> filteredModelList = filter(officeItems, newText);
        adapter.addItems(filteredModelList);

        adapter.notifyDataSetChanged();

        return true;
    }

    private static ArrayList<OfficeItem> filter(List<OfficeItem> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final ArrayList<OfficeItem> filteredModelList = new ArrayList<>();
        for (OfficeItem model : models) {
            final String text = model.getOfficeName().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void hideProgressDialog() {
        if(progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void networkError() {
        Toast.makeText(MainActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
    }

    private void parseOfficeInfo(String responseVo) {
        officeItems.clear();
        try {
            JSONArray jsonOfficeArray = new JSONArray(responseVo);
            for(int i = 0; i < jsonOfficeArray.length(); i++) {
                JSONObject jsonOfficeItem = jsonOfficeArray.getJSONObject(i);

                OfficeItem item = new OfficeItem();
                item.setOfficeID(jsonOfficeItem.getInt("id"));
                item.setOfficeName(jsonOfficeItem.getString("name"));
                item.setOfficeAddress(jsonOfficeItem.getString("address"));
                item.setLatitude(jsonOfficeItem.getDouble("latitude"));
                item.setLongitude(jsonOfficeItem.getDouble("longitude"));

                officeItems.add(item);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        adapter.addItems(officeItems);
        adapter.notifyDataSetChanged();
    }

    public void setOffice(OfficeItem item) {
        selectedItem = item;

        startSetOffice();
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
            mGeofenceList.clear();
            addGeofence(selectedItem.getLatitude(), selectedItem.getLongitude());
            createGeofenceRequest();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
        }
    }

    private void startSetOffice() {
        progressDialog.show();
        SetOfficeTask task = new SetOfficeTask();
        task.execute(SharedPrefManager.getInstance(this).getUserID(), selectedItem.getOfficeID());
    }

    private void setOfficeSuccess() {
        createGeofence(selectedItem);

        SharedPrefManager.getInstance(this).saveCurrentOfficeID(selectedItem.getOfficeID());
        SharedPrefManager.getInstance(this).saveOffice(selectedItem.getOfficeName());
        SharedPrefManager.getInstance(this).saveOfficeAddress(selectedItem.getOfficeAddress());
        SharedPrefManager.getInstance(this).saveLatitude(String.valueOf(selectedItem.getLatitude()));
        SharedPrefManager.getInstance(this).saveLongitude(String.valueOf(selectedItem.getLongitude()));

        SharedPrefManager.getInstance(this).saveLeaving(true);

        SharedPrefManager.getInstance(this).saveFirstRun(false);

        Intent intent = new Intent(MainActivity.this, OfficeInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setOfficeFailed() {
        Toast.makeText(MainActivity.this, R.string.already_parked, Toast.LENGTH_SHORT).show();
    }
}
