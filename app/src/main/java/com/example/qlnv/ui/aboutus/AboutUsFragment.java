package com.example.qlnv.ui.aboutus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.qlnv.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AboutUsFragment extends Fragment {
    CircleImageView img1, img2, img3, img4;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        Glide.with(getContext())
                .load(R.drawable.mduy)
                .centerCrop()
                .into(img1);

    }
    private void init(View v){
        img1=v.findViewById(R.id.manhduy);
    }
}