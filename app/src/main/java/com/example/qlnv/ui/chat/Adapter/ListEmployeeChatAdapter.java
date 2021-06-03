package com.example.qlnv.ui.chat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.qlnv.Activity.ChattingActivity;
import com.example.qlnv.Activity.model.Employee;
import com.example.qlnv.R;
import com.example.qlnv.ui.home.Adapter.ListEmployeeAdapter;
import com.example.qlnv.ui.home.Dialog.EmployeeInforDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListEmployeeChatAdapter extends RecyclerView.Adapter<ListEmployeeChatAdapter.Viewholder> {
    List<Employee> listEmpl;
    List<Employee> listEmplSearch = new ArrayList<>();
    Context context;
    int checkGender=0;
    Employee employee;
    int accid;
    public ListEmployeeChatAdapter(List<Employee> listEmpl, Context context, int accid) {
        this.listEmpl = listEmpl;
        this.context = context;
        this.accid = accid;
        this.listEmplSearch.addAll(listEmpl);
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_emloyee_item, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, int position) {
        //Glide fix lag load image
        if (listEmpl.get(position).getGender().equals("Nam")){
            Glide.with(context)
                    .load(R.drawable.male)
                    .centerCrop()
                    .into(holder.rv_emplAva);
            checkGender=1;
        } else {
            Glide.with(context)
                    .load(R.drawable.female)
                    .centerCrop()
                    .into(holder.rv_emplAva);
            checkGender=0;
        }


        holder.tv_name.setText("Tên: "+listEmpl.get(position).getName());
        holder.tv_id.setText("ID: "+String.valueOf(listEmpl.get(position).getId()));
        holder.tv_email.setText("Email: "+listEmpl.get(position).getEmail());
        holder.tv_address.setText("Địa chỉ: "+listEmpl.get(position).getAddress());
        holder.tv_position.setText("Chức vụ: "+listEmpl.get(position).getPosition());


        int idAccEmpl = listEmpl.get(position).getAccid();
        String nameUser = listEmpl.get(position).getName();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChattingActivity.class);
                holder.itemView.setBackgroundColor(Color.parseColor("#80EDEAEA"));
                intent.putExtra("idAccEmpl",""+ idAccEmpl);
                intent.putExtra("nameUser", nameUser);
                intent.putExtra("accid",""+ accid);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listEmpl.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView rv_emplAva;
        TextView tv_name, tv_id, tv_email, tv_address,tv_position;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            rv_emplAva = itemView.findViewById(R.id.rv_empAva);
            tv_name = itemView.findViewById(R.id.emplName);
            tv_id = itemView.findViewById(R.id.empId);
            tv_email = itemView.findViewById(R.id.emplEmail);
            tv_address = itemView.findViewById(R.id.emplAdd);
            tv_position=itemView.findViewById(R.id.emplPosition);
        }
    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        listEmpl.clear();
        if (charText.length() == 0) {
            listEmpl.addAll(listEmplSearch);
        } else {
            for (Employee empl : listEmplSearch) {
                if (empl.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    listEmpl.add(empl);
                }
            }
        }
        notifyDataSetChanged();
    }
}
