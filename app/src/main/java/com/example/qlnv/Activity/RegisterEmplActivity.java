package com.example.qlnv.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlnv.Activity.model.Employee;
import com.example.qlnv.R;
import com.example.qlnv.remoteAPI.JsonPlaceHolderAPI;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterEmplActivity extends AppCompatActivity {
    EditText name, phone, email, age, dob, address,salary,idAcc;
    RadioButton male, female, employee, manager;
    RadioGroup gender, position;
    String genderInfor,posInfor;
    TextView save;
    Intent i;
    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_empl);
        init();
        i = getIntent();
        idAcc.setText(i.getStringExtra("create_acc_id"));
        idAcc.setEnabled(false);
        backBtn.setOnClickListener(view -> {
            onBackPressed();
        });
        save.setOnClickListener(view -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.31.38:8080/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            JsonPlaceHolderAPI jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderAPI.class);
            getInforFromRadioButton();
            Employee empl = new Employee(
                    name.getText().toString(),
                    dob.getText().toString(),
                    Integer.parseInt(age.getText().toString()),
                    genderInfor,
                    email.getText().toString(),
                    address.getText().toString(),
                    phone.getText().toString(),
                    posInfor,
                    Double.parseDouble(salary.getText().toString()),
                    Integer.parseInt(idAcc.getText().toString()));
            Call<Employee> call = jsonPlaceHolderApi.createEmpl(empl);
            call.enqueue(new Callback<Employee>() {
                @Override
                public void onResponse(Call<Employee> call, Response<Employee> response) {
                    Toast.makeText(getApplicationContext(), "Thêm thông tin thành công!", Toast.LENGTH_LONG).show();
                    onBackPressed();
                    finish();
                }

                @Override
                public void onFailure(Call<Employee> call, Throwable t) {

                }
            });
        });
    }
    private void init(){
        idAcc=findViewById(R.id.editTextIDAcc);
        save=findViewById(R.id.SaveText);
        name=findViewById(R.id.editTextName);
        phone=findViewById(R.id.editTextPhone);
        email=findViewById(R.id.editTextEmail);
        age=findViewById(R.id.editTextAge);
        dob=findViewById(R.id.editDOB);
        address=findViewById(R.id.editAddress);
        male=findViewById(R.id.radioMale);
        female=findViewById(R.id.radioFemale);
        employee=findViewById(R.id.radioNV);
        manager=findViewById(R.id.radioQL);
        gender=findViewById(R.id.gender);
        position=findViewById(R.id.rg_pos);
        salary=findViewById(R.id.editSalary);
        backBtn=findViewById(R.id.back_btn_empl);
    }
    private void getInforFromRadioButton(){
        int genid=gender.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(genid);
        genderInfor=radioButton.getText().toString();

        int posid=position.getCheckedRadioButtonId();
        RadioButton radioBtn = (RadioButton) findViewById(posid);
        posInfor=radioBtn.getText().toString();
    }
}