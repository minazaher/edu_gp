package com.example.educationalgp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.educationalgp.databinding.ActivityEditQuizBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class EditQuizActivity extends AppCompatActivity {

    ActivityEditQuizBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializeMisc();
    }


    private void initializeMisc() {
        BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(binding.misc.getRoot());

}
}