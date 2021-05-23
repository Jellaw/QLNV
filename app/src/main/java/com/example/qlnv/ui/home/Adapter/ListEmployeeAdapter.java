package com.example.qlnv.ui.home.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.qlnv.Activity.model.Employee;
import com.example.qlnv.R;
import java.util.List;


public class ListEmployeeAdapter extends RecyclerView.Adapter<ListEmployeeAdapter.Viewholder> {
    List<Employee> listEmpl;
    Context context;

    public ListEmployeeAdapter(List<Employee> listEmpl, Context context) {
        this.listEmpl = listEmpl;
        this.context = context;
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
        int checkGender=0;
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


        final String nameEmpl = listEmpl.get(position).getName();


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return listEmpl.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView rv_emplAva;
        TextView tv_name, tv_id, tv_email, tv_address;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            rv_emplAva = itemView.findViewById(R.id.rv_empAva);
            tv_name = itemView.findViewById(R.id.emplName);
            tv_id = itemView.findViewById(R.id.empId);
            tv_email = itemView.findViewById(R.id.emplEmail);
            tv_address = itemView.findViewById(R.id.emplAdd);
        }
    }
}
