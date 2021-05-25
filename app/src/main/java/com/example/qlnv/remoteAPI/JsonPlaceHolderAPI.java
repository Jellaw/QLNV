package com.example.qlnv.remoteAPI;
import com.example.qlnv.Activity.model.Account;
import com.example.qlnv.Activity.model.Employee;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface JsonPlaceHolderAPI {
    @GET("contact/")
    Call<List<Employee>> getEmployee();
    @GET("acc/")
    Call<List<Account>> getAcc();
    @POST("acc/")
    Call<Account> createAcc(@Body Account account);
    @POST("contact/")
    Call<Employee> createEmpl(@Body Employee employee);
    @GET("contact/{id}")
    Call<Employee> searchEmpl(@Path("id") int id);
    @GET("acc/{id}")
    Call<Account> searchAcc(@Path("id") int id);
    @PUT("contact/{id}")
    Call<Employee> putEmpl(@Path("id") int id, @Body Employee employee);
    @DELETE("contact/{id}")
    Call<Void> deleteEmpl(@Path("id") int id);
}
