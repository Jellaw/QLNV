package com.example.qlnv.Activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlnv.Activity.model.Employee;
import com.example.qlnv.R;
import com.example.qlnv.remoteAPI.JsonPlaceHolderAPI;
import com.example.qlnv.utils.Utils;

import java.util.Calendar;

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
    DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_empl);
        init();
        i = getIntent();
        idAcc.setText(i.getStringExtra("create_acc_id"));
        idAcc.setEnabled(false);
        if(i.getStringExtra("nameGG")!=null&&i.getStringExtra("emailGG")!=null){
            name.setText(i.getStringExtra("nameGG"));
            email.setText(i.getStringExtra("emailGG"));
            idAcc.setText(i.getStringExtra("idGG").substring(0,4));
        }
        backBtn.setOnClickListener(view -> {
            onBackPressed();
        });
        dob.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            // calender class's instance and get current date , month and year from calender
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            // date picker dialog
            datePickerDialog  = new DatePickerDialog(RegisterEmplActivity.this, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    dob.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });
        save.setOnClickListener(view -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Utils.IP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            JsonPlaceHolderAPI jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderAPI.class);
            getInforFromRadioButton();
            Employee empl = new Employee(
                    name.getText().toString(),
                    dob.getText().toString(),
                    Integer.parseInt(idAcc.getText().toString()),
                    genderInfor,
                    email.getText().toString(),
                    address.getText().toString(),
                    phone.getText().toString(),
                    posInfor,
                    Double.parseDouble(salary.getText().toString()));
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