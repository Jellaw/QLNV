package com.example.qlnv.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qlnv.Activity.Dialog.LoginAccountDialog;
import com.example.qlnv.Activity.model.Account;
import com.example.qlnv.Activity.model.Employee;
import com.example.qlnv.R;
import com.example.qlnv.remoteAPI.JsonPlaceHolderAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ImageView avaView;
    private TextView accName;
    private TextView accGmail;
    private AppBarConfiguration mAppBarConfiguration;
    private Toolbar toolbar;
    private  FloatingActionButton fab;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    Intent i;
    int accid=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        View hView =  navigationView.getHeaderView(0);
        initHeaderNavigation(hView);
        setSupportActionBar(toolbar);
        //================================================================================================
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
//        accid=i.getIntExtra("Acc_id",0);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.31.38:8080/api/")
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
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {

            }
        });
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
    }
    private void initHeaderNavigation(View v){
        accName = (TextView)v.findViewById(R.id.accName);
        accGmail = (TextView)v.findViewById(R.id.accGmail);
        avaView = (ImageView) v.findViewById(R.id.avaView);
    }
}