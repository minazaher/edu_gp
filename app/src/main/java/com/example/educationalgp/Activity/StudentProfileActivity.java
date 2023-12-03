package com.example.educationalgp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.educationalgp.Adapter.CourseAdapter;
import com.example.educationalgp.Model.Course;
import com.example.educationalgp.R;
import com.example.educationalgp.databinding.ActivityStudentProfileBinding;

import java.util.ArrayList;
import java.util.List;

public class StudentProfileActivity extends AppCompatActivity {
    ActivityStudentProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityStudentProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Course unit1 = new Course("الوحدة الاولى : التفاعلات الكيميائية", "https://firebasestorage.googleapis.com/v0/b/educationalgp.appspot.com/o/img_unit1.jpg?alt=media&token=512136c4-e710-400e-b48d-774bf4fd8fa3");
        Course unit2 = new Course("الوحدة الثانية : القوى والحركة", "https://firebasestorage.googleapis.com/v0/b/educationalgp.appspot.com/o/img_unit2.jpeg?alt=media&token=16729e81-e112-4779-87e0-b858771cfd2e");
        Course unit3 = new Course("الوحدة الثالثة : الارض و الكون", "https://firebasestorage.googleapis.com/v0/b/educationalgp.appspot.com/o/img_unit3.jpg?alt=media&token=b63b00ae-1cf8-4858-bfc9-2a505c5d4541");
        ArrayList<Course> units = new ArrayList<>();
        units.add(unit1);
        units.add(unit2);
        units.add(unit3);

        binding.lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false));
        CourseAdapter courseAdapter = new CourseAdapter(units, this);
        System.out.println("units : " + units.toString());
        binding.unitsRecyclerView.setAdapter(courseAdapter);
        binding.unitsRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false));
        binding.lessonsRecyclerView.setAdapter(courseAdapter);
        binding.lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false));
    }


}