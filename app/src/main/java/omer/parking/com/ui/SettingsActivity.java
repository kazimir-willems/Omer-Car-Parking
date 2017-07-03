package omer.parking.com.ui;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import omer.parking.com.R;
import omer.parking.com.util.SharedPrefManager;

public class SettingsActivity extends AppCompatActivity {

    private static final int DEFAULT_TUNE = 1;
    private static final int NO_TUNE = 2;

    @Bind(R.id.tv_default_tune)
    TextView tvDefaultTune;
    @Bind(R.id.tv_no_parking_tune)
    TextView tvNoParkingTune;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);

        tvDefaultTune.setText(SharedPrefManager.getInstance(this).getDefaultTuneName());
        tvNoParkingTune.setText(SharedPrefManager.getInstance(this).getNoTuneName());
    }

    @OnClick(R.id.btn_set_office)
    void onClickBtnSetOffice() {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);

        startActivity(intent);
        finish();
    }

    @OnClick(R.id.default_tune_layout)
    void onClickDefaultTuneLayout() {
        final Uri currentTone= RingtoneManager.getActualDefaultRingtoneUri(SettingsActivity.this, RingtoneManager.TYPE_ALARM);
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tune");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentTone);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        startActivityForResult(intent, DEFAULT_TUNE);
    }

    @OnClick(R.id.no_parking_layout)
    void onClickNoTuneLayout() {
        final Uri currentTone= RingtoneManager.getActualDefaultRingtoneUri(SettingsActivity.this, RingtoneManager.TYPE_ALARM);
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tune");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentTone);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        startActivityForResult(intent, NO_TUNE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent mRingtone) {
        switch (requestCode) {
            case DEFAULT_TUNE:
                if (resultCode == RESULT_OK) {
                    Uri uri = mRingtone.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    Ringtone ringtone = RingtoneManager.getRingtone(SettingsActivity.this, uri);
                    SharedPrefManager.getInstance(SettingsActivity.this).saveDefaultTuneName(ringtone.getTitle(SettingsActivity.this));
                    SharedPrefManager.getInstance(SettingsActivity.this).saveDefaultTune(uri.toString());
                    tvDefaultTune.setText(ringtone.getTitle(SettingsActivity.this));
                }
                break;
            case NO_TUNE:
                if (resultCode == RESULT_OK) {
                    Uri uri = mRingtone.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    Ringtone ringtone = RingtoneManager.getRingtone(SettingsActivity.this, uri);
                    SharedPrefManager.getInstance(SettingsActivity.this).saveNoTuneName(ringtone.getTitle(SettingsActivity.this));
                    SharedPrefManager.getInstance(SettingsActivity.this).saveNoLotTune(uri.toString());
                    tvNoParkingTune.setText(ringtone.getTitle(SettingsActivity.this));
                }
                break;
        }
    }
}
