package com.example.qlnv.ui.home.Adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.qlnv.Activity.MainActivity;
import com.example.qlnv.Activity.model.Employee;
import com.example.qlnv.R;
import com.example.qlnv.ui.home.Dialog.EmployeeInforDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ListEmployeeAdapter extends RecyclerView.Adapter<ListEmployeeAdapter.Viewholder> {
    List<Employee> listEmpl;
    List<Employee> listEmplSearch = new ArrayList<>();
    Context context;
    int checkGender=0;
    Employee employee;
    MainActivity mainActivity;
    public ListEmployeeAdapter(List<Employee> listEmpl, Context context, MainActivity mainActivity) {
        this.listEmpl = listEmpl;
        this.context = context;
        this.mainActivity = mainActivity;
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


        holder.tv_name.setText("T??n: "+listEmpl.get(position).getName());
        holder.tv_id.setText("ID: "+String.valueOf(listEmpl.get(position).getId()));
        holder.tv_email.setText("Email: "+listEmpl.get(position).getEmail());
        holder.tv_address.setText("?????a ch???: "+listEmpl.get(position).getAddress());
        holder.tv_position.setText("Ch???c v???: "+listEmpl.get(position).getPosition());


        if(mainActivity.position.equals("Nh??n vi??n")==false){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    employee = listEmpl.get(position);
                    OpenDialogClicked(context);
                }
            });
        }
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
    private void OpenDialogClicked(Context context)  {
        EmployeeInforDialog dialog_sucess = new EmployeeInforDialog (context, checkGender, employee) ;
        dialog_sucess.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog_sucess.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        dialog_sucess.show();
        dialog_sucess.setCancelable(true);
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
