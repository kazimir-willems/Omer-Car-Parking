package omer.parking.com.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import omer.parking.com.R;
import omer.parking.com.event.LoginEvent;
import omer.parking.com.task.LoginTask;
import omer.parking.com.util.SharedPrefManager;
import omer.parking.com.util.StringUtil;
import omer.parking.com.vo.LoginResponseVo;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Bind(R.id.edt_user_name)
    TextInputEditText edtUserName;
    @Bind(R.id.edt_password)
    TextInputEditText edtPassword;

    private Animation shake;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);
        shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.edittext_shake);
    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onLoginEvent(LoginEvent event) {
        hideProgressDialog();
        LoginResponseVo responseVo = event.getResponse();
        if (responseVo != null) {
            if(responseVo.success == 1) {
                if(SharedPrefManager.getInstance(LoginActivity.this).getFirstRun()) {
                    Intent intent = new Intent(LoginActivity.this, SettingsActivity.class);

                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(LoginActivity.this, OfficeInfoActivity.class);

                    startActivity(intent);
                    finish();
                }
            } else {
                loginFailed();
            }
        } else {
            networkError();
        }
    }

    @OnClick(R.id.btn_signin)
    void onClickBtnSignIn() {
        username = edtUserName.getText().toString();
        password = edtPassword.getText().toString();

        if (!checkUserName()) return;
        if (!checkPassword()) return;

        startSignIn();
}

    private void startSignIn() {
        progressDialog.setMessage(getResources().getString(R.string.processing));
        progressDialog.show();

        LoginTask task = new LoginTask();
        task.execute(username, password);
    }

    private boolean checkUserName() {
        if (StringUtil.isEmpty(username)) {
            showInfoNotice(edtUserName);
            return false;
        }

        return true;
    }

    private boolean checkPassword() {
        if (StringUtil.isEmpty(username)) {
            showInfoNotice(edtUserName);
            return false;
        }

        return true;
    }

    private void showInfoNotice(TextInputEditText target) {
        target.startAnimation(shake);
        if (target.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void hideProgressDialog() {
        if(progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void networkError() {
        Toast.makeText(LoginActivity.this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
    }

    private void loginFailed() {
        Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
    }
}
