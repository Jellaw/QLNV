package com.example.qlnv.ui.home.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.qlnv.Activity.LoginActivity;
import com.example.qlnv.Activity.model.Account;
import com.example.qlnv.Activity.model.Employee;
import com.example.qlnv.R;
import com.example.qlnv.remoteAPI.JsonPlaceHolderAPI;

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

    }
    private void buttonDoneClick()  {
        this.dismiss();
    }
}
