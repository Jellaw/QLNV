package com.example.qlnv.Activity.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.qlnv.Activity.LoginActivity;
import com.example.qlnv.Activity.model.Account;
import com.example.qlnv.R;
import com.example.qlnv.SplashActivity;
import com.example.qlnv.remoteAPI.JsonPlaceHolderAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginAccountDialog extends Dialog {
    private Button signin_btn,back_btn;
    private TextView notitxt;
    private EditText inputUsername, inputPassword;
    LoginActivity loginActivity;
    public Context context;
    List<Account> accs;
    int acc_id;
    public LoginAccountDialog(@NonNull Context context, LoginActivity loginActivity) {
        super(context);
        this.context = context;
        this.loginActivity=loginActivity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_acc_dialog);

        this.signin_btn = findViewById(R.id.DialogSignIn);
        this.back_btn = findViewById(R.id.DialogBack);
        this.inputPassword = findViewById(R.id.inputPassword);
        this.inputUsername = findViewById(R.id.inputUsername);
        this.notitxt=findViewById(R.id.notitxt);

        signin_btn.setOnClickListener(view -> {
            try{
                Retrofit retrofitAcc = new Retrofit.Builder()
                        .baseUrl("http://192.168.31.38:8080/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                JsonPlaceHolderAPI jsonPlaceHolderApiACC = retrofitAcc.create(JsonPlaceHolderAPI.class);
                Call<List<Account>> callAcc = jsonPlaceHolderApiACC.getAcc();
                callAcc.enqueue(new Callback<List<Account>>() {

                    @Override
                    public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                        accs = response.body();
                        String username = inputUsername.getText().toString();
                        String pass = inputPassword.getText().toString();
                        for (Account acc : accs) {
                            if (username.equals(acc.getUsername())&&pass.equals(acc.getPassword())){
                                Toast.makeText(getContext(), "Login thành công!", Toast.LENGTH_LONG).show();
                                loginActivity.check=1;
                                acc_id=acc.getId();
                                loginActivity.AccountID=acc_id;
                                buttonDoneClick();
                                loginActivity.gotoMainAcitivity();
                            } else notitxt.setText("Sai tên đăng nhập hoặc mật khẩu!");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Account>> call, Throwable t) {
                    }
                });

            } catch (Exception e){
                Toast.makeText(getContext(), "Loi: "+e, Toast.LENGTH_LONG).show();
            }
        });
        back_btn.setOnClickListener(view -> {
            buttonDoneClick();
        });
    }
    private void buttonDoneClick()  {
        this.dismiss();
    }
}
