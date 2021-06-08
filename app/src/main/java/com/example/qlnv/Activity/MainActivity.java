package com.example.qlnv.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.qlnv.Activity.Dialog.CreateNewEmpDialog;
import com.example.qlnv.Activity.Dialog.LoginAccountDialog;
import com.example.qlnv.Activity.model.Account;
import com.example.qlnv.Activity.model.Employee;
import com.example.qlnv.R;
import com.example.qlnv.remoteAPI.JsonPlaceHolderAPI;
import com.example.qlnv.ui.chat.ChatFragment;
import com.example.qlnv.ui.home.Dialog.EmployeeInforDialog;
import com.example.qlnv.utils.Utils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private CircleImageView avaView;
    private TextView accName;
    private TextView accGmail;
    private AppBarConfiguration mAppBarConfiguration;
    private Toolbar toolbar;
    private  FloatingActionButton fab;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    Intent i;
    public int accid;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    public String position;
    public int check_login_google;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        View hView =  navigationView.getHeaderView(0);
        initHeaderNavigation(hView);
        setSupportActionBar(toolbar);
        //================================================================================================
        i = getIntent();
        accid = i.getIntExtra("Acc_id",0);
        check_login_google = i.getIntExtra("LoginGG",0);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               OpenDialogClicked();
            }
        });
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_stat, R.id.nav_about)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //================================================================================================
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.IP)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderAPI jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderAPI.class);
        Call<List<Employee>> call = jsonPlaceHolderApi.getEmployee();
        call.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                List<Employee> emps;
                emps= response.body();
                for (Employee employee : emps){
                    if (accid==employee.getAccid()){
                        accGmail.setText(employee.getEmail());
                        accName.setText(employee.getName());
                        position = employee.getPosition();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {

            }
        });
        avaView.setOnClickListener(view -> {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, 1);
        });

    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            Uri imageUri = data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .centerCrop()
                    .into(avaView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void init(){
         toolbar = findViewById(R.id.toolbar);
         fab = findViewById(R.id.fab);
         drawer = findViewById(R.id.drawer_layout);
         navigationView = findViewById(R.id.nav_view);

        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }
    private void initHeaderNavigation(View v){
        accName = (TextView)v.findViewById(R.id.accName);
        accGmail = (TextView)v.findViewById(R.id.accGmail);
        avaView = (CircleImageView) v.findViewById(R.id.avaView);
    }
    private void Logout(){
        if (check_login_google==1){
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            if (status.isSuccess()){
                                backToLoginActivity();
                            }else{
                                Toast.makeText(getApplicationContext(),"Session not close", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            backToLoginActivity();
        }
    }
    private void backToLoginActivity(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void shareApp(){
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        /*This will be the actual content you wish you share.*/
        String shareBody = "Quản lý nhân viên - Mạnh Duy";
        /*The type of the content is text, obviously.*/
        intent.setType("text/plain");
        /*Applying information Subject and Body.*/
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareBody);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        /*Fire!*/
        startActivity(Intent.createChooser(intent, shareBody));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout: Logout();
            case R.id.action_share: shareApp();
        }
        return true;
    }
    private void OpenDialogClicked()  {
        CreateNewEmpDialog dialog_sucess = new CreateNewEmpDialog (this, MainActivity.this) ;
        dialog_sucess.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        dialog_sucess.show();
        dialog_sucess.setCancelable(false);
    }
    public void gotoRegisterAcitivity() {
             Intent intent0 = new Intent(MainActivity.this, LoginActivity.class);
            Intent intent = new Intent(MainActivity.this, RegisterAccountActivity.class);
            startActivity(intent0);
            startActivity(intent);
            finish();
    }


    //login google===========================================================================================
    @Override
    public void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result=opr.get();
            handleSignInResult(result);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            accName.setText(account.getDisplayName());
            accGmail.setText(account.getEmail());
            try{
                Glide.with(this).load(account.getPhotoUrl()).centerCrop().into(avaView);
            }catch (NullPointerException e){
                Toast.makeText(this,"image not found",Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onPause() {//fix error: Already managing a GoogleApiClient with id 0
        super.onPause();
        googleApiClient.stopAutoManage(this);
        googleApiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}