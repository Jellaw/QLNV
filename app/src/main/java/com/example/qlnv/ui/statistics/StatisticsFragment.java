package com.example.qlnv.ui.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlnv.Activity.model.Employee;
import com.example.qlnv.R;
import com.example.qlnv.remoteAPI.JsonPlaceHolderAPI;
import com.example.qlnv.ui.home.Adapter.ListEmployeeAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StatisticsFragment extends Fragment {
    EditText edtSearchEmpl;
    Button search, getAll;
    RecyclerView rv_listEmpl;
    List<Employee> employeeList;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.31.38:8080/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderAPI jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderAPI.class);
        Call<List<Employee>> call = jsonPlaceHolderApi.getEmployee();
        call.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                employeeList= response.body();
                initRecyclerView(employeeList);
            }
            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {

            }
        });
        getAll.setOnClickListener(view1 -> {
            Call<List<Employee>> call2 = jsonPlaceHolderApi.getEmployee();
            List<Employee> toRemove = new ArrayList<>();
            for (Employee employee:employeeList){
                toRemove.add(employee);
            }
            employeeList.removeAll(toRemove);
            call2.enqueue(new Callback<List<Employee>>() {
                @Override
                public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                    employeeList= response.body();
                    initRecyclerView(employeeList);
                }
                @Override
                public void onFailure(Call<List<Employee>> call, Throwable t) {

                }
            });
        });
        search.setOnClickListener(view1 -> {
            List<Employee> itemSearch = new ArrayList<>();
            for (Employee employee : employeeList){
                if (edtSearchEmpl.getText().toString().equals(""+employee.getAddress())){
                    itemSearch.add(employee);
                }
            }
            initRecyclerView(itemSearch);
        } );
    }

    private void init(View v){
        edtSearchEmpl = v.findViewById(R.id.edtSearchAddressEmpl);
        search = v.findViewById(R.id.SearchAddEmpBtn);
        getAll=v.findViewById(R.id.getAllAddBtn);
        rv_listEmpl=v.findViewById(R.id.rv_add_empl);
    }
    private void initRecyclerView(List<Employee> employees) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_listEmpl.setLayoutManager(layoutManager);
        ListEmployeeAdapter adapter = new ListEmployeeAdapter(employees, getContext());
        rv_listEmpl.setAdapter(adapter);
    }
}