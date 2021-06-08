package com.example.qlnv.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlnv.Activity.Dialog.LoginAccountDialog;
import com.example.qlnv.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.SignInButtonImpl;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    CircleImageView appLogo;
    TextView register;
    Button fbLogin, Login;
    SignInButtonImpl GGLogin;
    public int check=0;
    public int AccountID=0;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;
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


        //signup with google=====================================================
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        GGLogin=(SignInButtonImpl) findViewById(R.id.btnLoginGoogle);
        GGLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });
    }
    private void init(){
        appLogo=findViewById(R.id.logo);
        register=findViewById(R.id.txtRegisterNow);
        fbLogin=findViewById(R.id.btnLoginFacebook);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            gotoProfile();
        }else{
            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
        }
    }

    private void gotoProfile(){
        Intent intent=new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("LoginGG", 1);
        startActivity(intent);
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}