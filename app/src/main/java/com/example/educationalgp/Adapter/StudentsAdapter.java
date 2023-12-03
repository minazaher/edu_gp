package com.example.educationalgp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.educationalgp.Model.Student;
import com.example.educationalgp.R;

import java.util.List;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.viewholder> {
    List<Student> students;

    public StudentsAdapter(List<Student> students){
        this.students = students;
    }
    @NonNull
    @Override
    public StudentsAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate
                                (R.layout.student_grade_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsAdapter.viewholder holder, int position) {
        holder.imageView.setImageResource(R.drawable.student_placeholder);
        holder.name.setText(students.get(position).getUsername());
        holder.grade.setText(String.valueOf(students.get(position).getGrades().get(0).getPercentage()));
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

  class viewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, grade;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.holder_student_img);
            name = itemView.findViewById(R.id.holder_student_name);
            grade = itemView.findViewById(R.id.holder_student_grade);

        }
    }
}
