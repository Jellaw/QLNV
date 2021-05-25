package com.example.qlnv.ui.home.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.qlnv.Activity.model.Employee;
import com.example.qlnv.R;
import com.example.qlnv.remoteAPI.JsonPlaceHolderAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditEmployeeDialog  extends Dialog {
    Context context;
    Employee employee;
    EditText edtName, edtAddress, edtEmail, edtSalary, edtPhone,edtPosition;
    Button btnBack, btnOK;
    public EditEmployeeDialog (@NonNull Context context, Employee employee) {
    super(context);
    this.context = context;
    this.employee = employee;
}
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.edit_empl_dialog);

            this.edtAddress = findViewById(R.id.edtAddress);
            this.edtEmail = findViewById(R.id.edtEmail);
            this.edtName = findViewById(R.id.edtName);
            this.edtPhone = findViewById(R.id.edtPhone);
            this.edtSalary = findViewById(R.id.edtSalary);
            this.edtPosition = findViewById(R.id.edtPosition);
            this.btnBack = findViewById(R.id.buttonBack);
            this.btnOK = findViewById(R.id.buttonEdit);


            edtSalary.setText(employee.getSalary().toString());
            edtName.setText(employee.getName().toString());
            edtPhone.setText(employee.getPhone().toString());
            edtEmail.setText(employee.getEmail().toString());
            edtAddress.setText(employee.getAddress().toString());
            edtPosition.setText(employee.getPosition());


            btnBack.setOnClickListener(view -> {
                buttonDoneClick();
            });
            btnOK.setOnClickListener(view -> {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.31.38:8080/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                JsonPlaceHolderAPI jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderAPI.class);
                Employee newEmpl = new Employee(edtName.getText().toString(),
                        edtEmail.getText().toString(),
                        edtAddress.getText().toString(),
                        edtPhone.getText().toString(),
                        edtPosition.getText().toString(),
                        Double.parseDouble(edtSalary.getText().toString()));
                Call<Employee> call = jsonPlaceHolderApi.putEmpl(employee.getId(),newEmpl);
                call.enqueue(new Callback<Employee>() {
                    @Override
                    public void onResponse(Call<Employee> call, Response<Employee> response) {
                        Toast.makeText(getContext(), "Sửa nhân viên thành công!", Toast.LENGTH_LONG).show();
                        buttonDoneClick();
                    }

                    @Override
                    public void onFailure(Call<Employee> call, Throwable t) {

                    }
                });
            });
        }
    private void buttonDoneClick()  {
        this.dismiss();
    }
}
