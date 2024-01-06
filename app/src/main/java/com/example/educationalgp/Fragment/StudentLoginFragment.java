package com.example.educationalgp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.educationalgp.Activity.StudentProfileActivity;
import com.example.educationalgp.Repository.StudentRepository.onAuthenticationListener;
import com.example.educationalgp.Repository.TeacherRepository;
import com.example.educationalgp.ViewModel.StudentViewModel;
import com.example.educationalgp.ViewModel.TeacherViewModel;
import com.example.educationalgp.databinding.FragmentStudentLoginBinding;

import java.util.ArrayList;
import java.util.List;

public class StudentLoginFragment extends Fragment {
    private FragmentStudentLoginBinding binding;
    private final StudentViewModel studentViewModel;
    private final TeacherViewModel teacherViewModel;
    String username= "", code = "";
    public StudentLoginFragment() {
        studentViewModel = new StudentViewModel();
        teacherViewModel = new TeacherViewModel();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudentLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.btnStudentLogin.setOnClickListener(v -> loginStudent());

        return view;
    }


    private void loginStudent() {
        username = binding.etStudentName.getText().toString();
        code = binding.etStudentEnterCode.getText().toString();
        if (isUsernameValid() && isTeacherCodeValid()){
            studentViewModel.loginUser(username, code,new onAuthenticationListener() {
                @Override
                public void onSuccess() {
                    goToStudentProfile();
                    showToast("تم تسجيل الدخول");
                }

                @Override
                public void onFailure(String errorMessage) {
                    if (studentNotExist(errorMessage))
                        signupStudent(username);
                }
            });

            studentViewModel.getLoggedInUser().observe(getViewLifecycleOwner(), user -> {
                if (user == null) {
                    signupStudent(username);
                }
            });
        }

    }

    private void signupStudent(String username) {
        studentViewModel.signupUser(username, code,new onAuthenticationListener() {
            @Override
            public void onSuccess() {
                showToast("تم انشاء حساب جديد");
                goToStudentProfile();
            }

            @Override
            public void onFailure(String errorMessage) {
                String toastMessage = String.format("لم نستطع انشاء حساب جديد بسبب:  %s", errorMessage);
                showToast(toastMessage);
            }
        });
    }

    private void goToStudentProfile(){
        Intent intent = new Intent(getActivity(), StudentProfileActivity.class);
        code = binding.etStudentEnterCode.getText().toString();
        username = binding.etStudentName.getText().toString();
        intent.putExtra("teacherId", code);
        intent.putExtra("studentName", username);
        startActivity(intent);
    }


    private void showToast(String msg){
        Toast.makeText(requireContext(), msg , Toast.LENGTH_SHORT).show();
    }

    private boolean studentNotExist(String errorMsg){
        return errorMsg.equals("The supplied auth credential is incorrect, malformed or has expired.");
    }

    private boolean isUsernameValid(){
        if(TextUtils.isEmpty(username)){
            binding.etStudentName.setError("من فضلك اكتب اسمك هنا");
            return false;
        }
        return true;
    }

    private boolean isTeacherCodeValid(){
        final boolean[] isValid = {true};
        String c = binding.etStudentEnterCode.getText().toString().trim();
        if(TextUtils.isEmpty(c)){
            isValid[0] = false;
            binding.etStudentEnterCode.setError("من فضلك ادخل كود المعلم هنا");
        }
        isTeacherAvailable(code, isAvailable -> {
            if (!isAvailable){
                binding.etStudentEnterCode.setError("من فضلك ادخل كود صحيح");
            }
            isValid[0] = isAvailable;
        });
        return isValid[0];
    }

    private void isTeacherAvailable(String code, OnTeacherAvailabilityChecked callback) {
        teacherViewModel.getTeacherIDs(IDs -> {
            boolean isAvailable = IDs.contains(code);
            callback.onAvailabilityChecked(isAvailable);
        });

    }


    interface OnTeacherAvailabilityChecked {
        void onAvailabilityChecked(boolean isAvailable);
    }

}