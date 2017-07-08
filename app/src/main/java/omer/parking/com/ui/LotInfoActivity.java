package omer.parking.com.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import omer.parking.com.R;
import omer.parking.com.event.DecLotEvent;
import omer.parking.com.event.GetLotEvent;
import omer.parking.com.event.GetOfficeListEvent;
import omer.parking.com.event.IncLotEvent;
import omer.parking.com.task.DecLotTask;
import omer.parking.com.task.GetRemainingLotTask;
import omer.parking.com.task.IncLotTask;
import omer.parking.com.task.SetStatusTask;
import omer.parking.com.util.SharedPrefManager;
import omer.parking.com.vo.DecLotResponseVo;
import omer.parking.com.vo.GetLotResponseVo;
import omer.parking.com.vo.GetOfficeResponseVo;
import omer.parking.com.vo.IncLotResponseVo;

public class LotInfoActivity extends AppCompatActivity {

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

    private int enterFlag = 0;      //0:enter, 1:exit

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lot_info);

        ButterKnife.bind(this);

        enterFlag = getIntent().getIntExtra("enter_flag", 0);

        if(enterFlag == 1) {
            tvTitle.setText(getResources().getString(R.string.exiting_office));
        } else {
            tvTitle.setText(getResources().getString(R.string.entering_office));
            int remainSlot = getIntent().getIntExtra("remaining_slot", 0);
            processLot(remainSlot);
        }

        tvOffice.setText(SharedPrefManager.getInstance(this).getOffice());
        tvOfficeAddress.setText(SharedPrefManager.getInstance(this).getOfficeAddress());
        tvLatitude.setText(SharedPrefManager.getInstance(this).getLatitude());
        tvLongitude.setText(SharedPrefManager.getInstance(this).getLongitude());

        progressDialog = new ProgressDialog(LotInfoActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.processing));
    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);

        if(enterFlag == 1)
            startIncLotTask();
    }

    @Override
    public void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onDecLotEvent(DecLotEvent event) {
        hideProgressDialog();
        DecLotResponseVo responseVo = event.getResponse();
        if (responseVo != null) {
            tvRemainingLot.setText(String.valueOf(responseVo.remain_lot));
        } else {
            networkError();
        }
    }

    @Subscribe
    public void onIncLotEvent(IncLotEvent event) {
        hideProgressDialog();
        IncLotResponseVo responseVo = event.getResponse();
        if (responseVo != null) {
            tvRemainingLot.setText(String.valueOf(responseVo.remain_lot));
        } else {
            networkError();
        }
    }

    private void processLot(int remainSlot) {
        tvRemainingLot.setText(String.valueOf(remainSlot));

        if(SharedPrefManager.getInstance(this).getLeaving()) {
            SharedPrefManager.getInstance(this).saveLeaving(false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name);
            builder.setMessage(R.string.ask_came_with_car);
            builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SetStatusTask task = new SetStatusTask();
                    task.execute(SharedPrefManager.getInstance(LotInfoActivity.this).getUserID(), 1);
                    startDecLotTask();

                    SharedPrefManager.getInstance(LotInfoActivity.this).saveAction(true);
                }
            });
            builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    SetStatusTask task = new SetStatusTask();
                    task.execute(SharedPrefManager.getInstance(LotInfoActivity.this).getUserID(), 2);
                    SharedPrefManager.getInstance(LotInfoActivity.this).saveAction(true);
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
    }

    private void startDecLotTask() {
        progressDialog.show();

        DecLotTask task = new DecLotTask();
        task.execute(SharedPrefManager.getInstance(this).getUserID());
    }

    private void startIncLotTask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.ask_leaving_office);
        builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SetStatusTask statusTask = new SetStatusTask();
                statusTask.execute(SharedPrefManager.getInstance(LotInfoActivity.this).getUserID(), 3);

                progressDialog.show();
                IncLotTask task = new IncLotTask();
                task.execute(SharedPrefManager.getInstance(LotInfoActivity.this).getUserID());
                SharedPrefManager.getInstance(LotInfoActivity.this).saveAction(true);
            }
        });
        builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SharedPrefManager.getInstance(LotInfoActivity.this).saveAction(true);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void hideProgressDialog() {
        if(progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void networkError() {
        Toast.makeText(LotInfoActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
    }
}
