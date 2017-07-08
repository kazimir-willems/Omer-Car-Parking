package omer.parking.com.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import omer.parking.com.R;
import omer.parking.com.event.DecLotEvent;
import omer.parking.com.event.GetLotEvent;
import omer.parking.com.event.IncLotEvent;
import omer.parking.com.task.DecLotTask;
import omer.parking.com.task.GetRemainingLotTask;
import omer.parking.com.task.IncLotTask;
import omer.parking.com.util.SharedPrefManager;
import omer.parking.com.vo.DecLotResponseVo;
import omer.parking.com.vo.GetLotResponseVo;
import omer.parking.com.vo.IncLotResponseVo;

public class OfficeInfoActivity extends AppCompatActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_office_name)
    TextView tvOffice;
    @Bind(R.id.tv_office_address)
    TextView tvOfficeAddress;
    @Bind(R.id.tv_latitude)
    TextView tvLatitude;
    @Bind(R.id.tv_longitude)
    TextView tvLongitude;
    @Bind(R.id.tv_remaining_lot)
    TextView tvRemainingLot;
    @Bind(R.id.tv_mobile)
    TextView tvMobileNum;
    @Bind(R.id.tv_car_plate)
    TextView tvCarPlateNum;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_info);

        ButterKnife.bind(this);

        tvMobileNum.setText(SharedPrefManager.getInstance(this).getMobilePhone());
        tvCarPlateNum.setText(SharedPrefManager.getInstance(this).getCarPlateNum());
        tvOffice.setText(SharedPrefManager.getInstance(this).getOffice());
        tvOfficeAddress.setText(SharedPrefManager.getInstance(this).getOfficeAddress());
        tvLatitude.setText(SharedPrefManager.getInstance(this).getLatitude());
        tvLongitude.setText(SharedPrefManager.getInstance(this).getLongitude());

        progressDialog = new ProgressDialog(OfficeInfoActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.processing));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_office_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                Intent settingIntent = new Intent(OfficeInfoActivity.this, SettingsActivity.class);
                startActivity(settingIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
        startGetLotTask();
    }

    @Override
    public void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onGetLotEvent(GetLotEvent event) {
        hideProgressDialog();
        GetLotResponseVo responseVo = event.getResponse();
        if (responseVo != null) {
            processLot(responseVo.remain_lot);
        } else {
            networkError();
        }
    }

    private void processLot(int remainSlot) {
        tvRemainingLot.setText(String.valueOf(remainSlot));
    }

    private void startGetLotTask() {
        progressDialog.show();

        GetRemainingLotTask task = new GetRemainingLotTask();
        task.execute(SharedPrefManager.getInstance(this).getCurrentOfficeID());
    }

    private void hideProgressDialog() {
        if(progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void networkError() {
        Toast.makeText(OfficeInfoActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
    }
}
