package com.example.educationalgp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.educationalgp.Model.Course;
import com.example.educationalgp.R;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.viewholder> {
    ArrayList<Course> courses;
    Context context;

    public CourseAdapter(ArrayList<Course> courses, Context context) {
        this.courses = courses;
        this.context = context;
    }


    @NonNull
    @Override
    public CourseAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate
                                (R.layout.lesson_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.viewholder holder, int position) {
        System.out.println(courses.get(position));
        holder.courseName.setText(courses.get(position).getName());
        Glide.with(context).asBitmap().load(courses.get(position).getImgUrl()).into(holder.courseImage);
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    static class viewholder  extends RecyclerView.ViewHolder{
        TextView courseName;
        ImageView courseImage;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            courseImage = itemView.findViewById(R.id.courseImg);
            courseName = itemView.findViewById(R.id.tv_courseName);
        }
    }
}
