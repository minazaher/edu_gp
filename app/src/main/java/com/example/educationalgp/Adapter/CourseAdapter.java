package com.example.educationalgp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.educationalgp.Model.Course;
import com.example.educationalgp.R;
import com.example.educationalgp.onCourseSelected;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.viewholder> {
    ArrayList<Course> courses;
    Context context;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private OnItemClickListener clickListener;


    public CourseAdapter(ArrayList<Course> courses, Context context, OnItemClickListener clickListener) {
        this.courses = courses;
        this.context = context;
        this.clickListener = clickListener;

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
        if (selectedPosition == position) {
            holder.card.setBackgroundResource(R.drawable.selected_lesson_bg);
        } else {
            holder.card.setBackgroundResource(R.color.mainColor);
        }

        holder.card.setOnClickListener(v -> {
            int previousSelectedPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousSelectedPosition);
            notifyItemChanged(selectedPosition);
            clickListener.onItemClick(position);

        });
    }
    @Override
    public int getItemCount() {
        return courses.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    static class viewholder  extends RecyclerView.ViewHolder {
        TextView courseName;
        ImageView courseImage;
        CardView card;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            courseImage = itemView.findViewById(R.id.courseImg);
            courseName = itemView.findViewById(R.id.tv_courseName);
            card =itemView.findViewById(R.id.lesson_card);
        }

    }
}
