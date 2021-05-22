package com.example.qlnv.remoteAPI;
import com.example.qlnv.Activity.model.Account;
import com.example.qlnv.Activity.model.Employee;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface JsonPlaceHolderAPI {
    @GET("contact/")
    Call<List<Employee>> getEmployee();
    @GET("acc/")
    Call<List<Account>> getAcc();
    @POST("acc/")
    Call<Account> createAcc(@Body Account account);
    @POST("contact/")
    Call<Employee> createEmpl(@Body Employee employee);
}
