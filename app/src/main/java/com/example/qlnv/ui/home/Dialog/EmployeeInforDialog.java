package com.example.qlnv.ui.home.Dialog;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.qlnv.Activity.LoginActivity;
import com.example.qlnv.Activity.MainActivity;
import com.example.qlnv.Activity.RegisterEmplActivity;
import com.example.qlnv.Activity.model.Account;
import com.example.qlnv.Activity.model.Employee;
import com.example.qlnv.R;
import com.example.qlnv.remoteAPI.JsonPlaceHolderAPI;
import com.example.qlnv.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmployeeInforDialog extends Dialog {

    int checkGender;
    public Context context;
    Employee employee;
    ImageView accImg, reloadImg, editImg, delImg,avaEmpl;
    TextView dob, phone, salary,name;
    public EmployeeInforDialog(@NonNull Context context, int checkGender, Employee employee) {
        super(context);
        this.context = context;
        this.checkGender=checkGender;
        this.employee = employee;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.empl_info_dialog);

        this.accImg = findViewById(R.id.accInforImg);
        this.reloadImg = findViewById(R.id.reloadInforImg);
        this.editImg = findViewById(R.id.editInforImg);
        this.delImg = findViewById(R.id.delInforImg);
        this.avaEmpl=findViewById(R.id.empAvaInfor);
        this.dob=findViewById(R.id.dobInfor);
        this.phone=findViewById(R.id.phoneInfor);
        this.salary=findViewById(R.id.salaryInfor);
        this.name=findViewById(R.id.txtNameinfor);

        if (checkGender ==1){
            Glide.with(context)
                    .load(R.drawable.male)
                    .centerCrop()
                    .into(avaEmpl);
        } else {
            Glide.with(context)
                    .load(R.drawable.female)
                    .centerCrop()
                    .into(avaEmpl);
        }
        dob.setText(employee.getDob());
        phone.setText(employee.getPhone());
        salary.setText(employee.getSalary().toString());
        name.setText(employee.getName());

        editImg.setOnClickListener(view -> {
            OpenDialogClicked(context);
        });
        reloadImg.setOnClickListener(view -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Utils.IP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonPlaceHolderAPI jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderAPI.class);

            Call<Employee> call = jsonPlaceHolderApi.searchEmpl(employee.getId());
            call.enqueue(new Callback<Employee>() {
                @Override
                public void onResponse(Call<Employee> call, Response<Employee> response) {
                    Employee newEmployee = response.body();
                    dob.setText(newEmployee.getDob());
                    phone.setText(newEmployee.getPhone());
                    salary.setText(newEmployee.getSalary().toString());
                    name.setText(newEmployee.getName());
                }
                @Override
                public void onFailure(Call<Employee> call, Throwable t) {

                }
            });
        });
        accImg.setOnClickListener(view -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Utils.IP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonPlaceHolderAPI jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderAPI.class);
            Call<Account> call = jsonPlaceHolderApi.searchAcc(employee.getAccid());
            call.enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    Account accountInfor = response.body();
                    Toast.makeText(getContext(), "Username: "+accountInfor.getUsername()+", "
                            +"Password: "+accountInfor.getPassword(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<Account> call, Throwable t) {

                }
            });
        });
        delImg.setOnClickListener(view -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Utils.IP)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonPlaceHolderAPI jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderAPI.class);

            Call<Void> call = jsonPlaceHolderApi.deleteEmpl(employee.getId());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(getContext(), "Đã xóa nhân viên này khỏi server! Vui lòng reload lại danh sách!", Toast.LENGTH_LONG).show();
                    buttonDoneClick();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        });
    }
    private void OpenDialogClicked(Context context)  {
        EditEmployeeDialog dialog_sucess = new EditEmployeeDialog (context, employee) ;
        dialog_sucess.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        dialog_sucess.show();
        dialog_sucess.setCancelable(false);
    }
    private void buttonDoneClick()  {
        this.dismiss();
    }
}
