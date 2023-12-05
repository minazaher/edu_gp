package com.example.educationalgp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.educationalgp.Adapter.CourseAdapter;
import com.example.educationalgp.Adapter.LessonAdapter;
import com.example.educationalgp.Model.Course;
import com.example.educationalgp.R;
import com.example.educationalgp.databinding.ActivityStudentProfileBinding;

import java.util.ArrayList;
import java.util.List;

public class StudentProfileActivity extends AppCompatActivity {
    ActivityStudentProfileBinding binding;

    String unit = "", lesson = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityStudentProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        initializeUnitsRecView();
        initializeLessonsRecView(getUnitThreeLessons());
        binding.btnGoToActivity.setOnClickListener(v -> goToActivitySelection(unit,lesson));

    }


    private ArrayList<Course> getUnits(){

        Course unit1 = new Course("التفاعلات الكيميائية", "https://firebasestorage.googleapis.com/v0/b/educationalgp.appspot.com/o/img_unit1.jpg?alt=media&token=512136c4-e710-400e-b48d-774bf4fd8fa3");
        Course unit2 = new Course("القوى والحركة", "https://firebasestorage.googleapis.com/v0/b/educationalgp.appspot.com/o/img_unit2.jpeg?alt=media&token=16729e81-e112-4779-87e0-b858771cfd2e");
        Course unit3 = new Course("الارض و الكون", "https://firebasestorage.googleapis.com/v0/b/educationalgp.appspot.com/o/img_unit3.jpg?alt=media&token=b63b00ae-1cf8-4858-bfc9-2a505c5d4541");
        ArrayList<Course> units = new ArrayList<>();
        units.add(unit1);
        units.add(unit2);
        units.add(unit3);
        return units;
    }

    private void initializeUnitsRecView(){
        ArrayList<Course> units = getUnits();
        CourseAdapter courseAdapter = new CourseAdapter(units, this, position -> {
            switch (position) {
                case 0:
                    initializeLessonsRecView(getUnitOneLessons());
                    break;
                case 1:
                    unit= "un2";
                    lesson = "less1";
                    initializeLessonsRecView(getUnitTwoLessons());
                    break;
                case 2:
                    initializeLessonsRecView(getUnitThreeLessons());
                    break;
            }
        });

        binding.unitsRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false));

        binding.unitsRecyclerView.setAdapter(courseAdapter);

    }
    private void initializeLessonsRecView(ArrayList<Course> lessons){
        LessonAdapter courseAdapter = new LessonAdapter(lessons, this);
        binding.lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false));
        binding.lessonsRecyclerView.setAdapter(courseAdapter);

    }

    private ArrayList<Course> getUnitTwoLessons() {
        Course lesson1 = new Course("القوى الاساسية في الطبيعة", "https://firebasestorage.googleapis.com/v0/b/educationalgp.appspot.com/o/img_lesson1.jpeg?alt=media&token=6df45031-08a6-4f80-90b4-80c4af73548c");
        Course lesson2 = new Course(" القوى المصاحبة للحركة", "https://firebasestorage.googleapis.com/v0/b/educationalgp.appspot.com/o/img_lesson2.jpeg?alt=media&token=37b3cfc4-1781-4a14-adfb-af082d65e0b7");
        Course lesson3 = new Course("الحركة", "https://firebasestorage.googleapis.com/v0/b/educationalgp.appspot.com/o/img_lesson3.jpeg?alt=media&token=efda813f-5a4e-4fb3-a874-ff9c075d391b");
        ArrayList<Course> lessons = new ArrayList<>();

        lessons.add(lesson1);
        lessons.add(lesson2);
        lessons.add(lesson3);

        return lessons;

    }
    private ArrayList<Course> getUnitOneLessons() {
        Course lesson1 = new Course("الاتحاد الكيميائى", "https://firebasestorage.googleapis.com/v0/b/educationalgp.appspot.com/o/img_un2less1.jpeg?alt=media&token=ae62cf00-40d3-4893-9f12-1352a47891bc");
        Course lesson2 = new Course("المركبات الكيميائية ", "https://firebasestorage.googleapis.com/v0/b/educationalgp.appspot.com/o/img_un2less2.jpeg?alt=media&token=20b7da54-6a82-4f19-b0ca-9c722f003ced");
        Course lesson3 = new Course("المعادله الكيميائيه و التفاعل الكميائي", "https://firebasestorage.googleapis.com/v0/b/educationalgp.appspot.com/o/img_un2less3.jpeg?alt=media&token=1492f422-c306-4bba-9cbe-cd8ea1e4660b");
        ArrayList<Course> lessons = new ArrayList<>();

        lessons.add(lesson1);
        lessons.add(lesson2);
        lessons.add(lesson3);

        return lessons;

    }
    private ArrayList<Course> getUnitThreeLessons() {
        Course lesson1 = new Course("الدرس الأول : الاجرام السماوية", "https://firebasestorage.googleapis.com/v0/b/educationalgp.appspot.com/o/img_un3less1.jpeg?alt=media&token=50f5deb0-d342-4600-abfa-f6136afdbe1e");
        Course lesson2 = new Course("الأرض", "https://firebasestorage.googleapis.com/v0/b/educationalgp.appspot.com/o/img_un3less2.jpeg?alt=media&token=420817d5-1c1e-4d8f-a579-2524954241d5");
        Course lesson3 = new Course("الصخور والمعادن", "https://firebasestorage.googleapis.com/v0/b/educationalgp.appspot.com/o/img_un3less3.jpeg?alt=media&token=0c2dbc66-cd9c-4351-9a4e-9d55180bdbce");
        ArrayList<Course> lessons = new ArrayList<>();

        lessons.add(lesson1);
        lessons.add(lesson2);
        lessons.add(lesson3);

        return lessons;

    }

    private void goToActivitySelection(String unit, String lesson){
        Intent intent = new Intent(StudentProfileActivity.this, QuizActivity.class);
        intent.putExtra("unit", unit);
        intent.putExtra("lesson", lesson);
        startActivity(intent);
    }



}