package com.example.qlnv.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.text.style.AlignmentSpan;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    EditText edtSearchEmpl;
    Button search, getAll;
    RecyclerView rv_listEmpl;
    List<Employee> employeeList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
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
                initRecyclerViewUserChat();
            }
            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {

            }
        });
        getAll.setOnClickListener(view1 -> {
            call.enqueue(new Callback<List<Employee>>() {
                @Override
                public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                    employeeList= response.body();
                    initRecyclerViewUserChat();
                }
                @Override
                public void onFailure(Call<List<Employee>> call, Throwable t) {

                }
            });
        });
        search.setOnClickListener(view1 -> {

        } );
    }

    private void init(View v){
        edtSearchEmpl = v.findViewById(R.id.edtSearchEmpl);
        search = v.findViewById(R.id.SearchEmpBtn);
        getAll=v.findViewById(R.id.getAllBtn);
        rv_listEmpl=v.findViewById(R.id.rv_empl);
    }
    private void initRecyclerViewUserChat() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_listEmpl.setLayoutManager(layoutManager);
        ListEmployeeAdapter adapter = new ListEmployeeAdapter(employeeList, getContext());
        rv_listEmpl.setAdapter(adapter);
    }
}