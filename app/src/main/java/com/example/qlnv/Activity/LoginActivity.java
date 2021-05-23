package com.example.qlnv.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.qlnv.Activity.Dialog.LoginAccountDialog;
import com.example.qlnv.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {
    CircleImageView appLogo;
    TextView register;
    Button fbLogin, GGLogin, Login;
    public int check=0;
    public int AccountID=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        appLogo.setImageResource(R.drawable.images);
        register.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterAccountActivity.class));
        });
        Login.setOnClickListener(view -> {
            OpenDialogClicked();
        });
    }
    private void init(){
        appLogo=findViewById(R.id.logo);
        register=findViewById(R.id.txtRegisterNow);
        fbLogin=findViewById(R.id.btnLoginFacebook);
        GGLogin=findViewById(R.id.btnLoginGoogle);
        Login=findViewById(R.id.btnSignIn);
    }
    private void OpenDialogClicked()  {
        LoginAccountDialog dialog_sucess = new LoginAccountDialog (this,LoginActivity.this) ;
        dialog_sucess.show();
        dialog_sucess.setCancelable(false);
    }

    public void gotoMainAcitivity() {
        if(check==1){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("Acc_id", AccountID);
            Log.v("id",""+AccountID);
            startActivity(intent);
            // close splash activity
            finish();
        }
    }
}