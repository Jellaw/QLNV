package com.example.qlnv.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.qlnv.Activity.model.Account;
import com.example.qlnv.R;
import com.example.qlnv.remoteAPI.JsonPlaceHolderAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterAccountActivity extends AppCompatActivity {
    ImageView backBtn;
    EditText username, password;
    List<Account> accs;
    int sizeAcc=0;
    Button saveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        Retrofit retrofitAcc = new Retrofit.Builder()
                .baseUrl("http://192.168.31.38:8080/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderAPI jsonPlaceHolderApiACC = retrofitAcc.create(JsonPlaceHolderAPI.class);
        Call<List<Account>> getSizeAcc = jsonPlaceHolderApiACC.getAcc();
        getSizeAcc.enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                accs = response.body();
                for (Account acc : accs) {
                    sizeAcc=acc.getId();
                    Log.v("size", ""+sizeAcc);
                }
            }
            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {
            }
        });
        backBtn.setOnClickListener(view -> {
            onBackPressed();
        });
        saveBtn.setOnClickListener(view -> {

            Account account = new Account(sizeAcc+1,username.getText().toString(),password.getText().toString());
            Call<Account> callAcc = jsonPlaceHolderApiACC.createAcc(account);
            callAcc.enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    Toast.makeText(getApplicationContext(), "Thêm tài khoản thành công!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<Account> call, Throwable t) {

                }
            });
            Intent intent = new Intent(RegisterAccountActivity.this, RegisterEmplActivity.class);
            intent.putExtra("create_acc_id", sizeAcc+1);
            Log.v("size", ""+sizeAcc);
            startActivity(intent);
            // close splash activity
            finish();
        });
    }
    private void init(){
        backBtn=findViewById(R.id.back_btn_register);
        username=findViewById(R.id.editTextUsername);
        password=findViewById(R.id.editTextPass);
        saveBtn=findViewById(R.id.saveBtn);
    }
}