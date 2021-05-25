package com.example.qlnv.Activity.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.qlnv.Activity.MainActivity;
import com.example.qlnv.R;

public class CreateNewEmpDialog extends Dialog {
    Context context;
    Button back_btn, ok_btn;
    MainActivity mainActivity;
    public CreateNewEmpDialog(@NonNull Context context, MainActivity mainActivity) {
        super(context);
        this.context=context;
        this.mainActivity=mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_new_emp_dialog);

        this.ok_btn = findViewById(R.id.OKBtnDialogCreate);
        this.back_btn = findViewById(R.id.backBtnDialogCreate);

        back_btn.setOnClickListener(view -> {
            buttonDoneClick();
        });

        ok_btn.setOnClickListener(view -> {
          mainActivity.gotoRegisterAcitivity();
        });
    }
    private void buttonDoneClick()  {
        this.dismiss();
    }
}
