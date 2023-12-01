package com.example.educationalgp.Fragment;

import static com.example.educationalgp.ApplicationClass.*;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.educationalgp.Activity.SignUpActivity;
import com.example.educationalgp.Activity.TeacherProfileActivity;
import com.example.educationalgp.Repository.StudentRepository;
import com.example.educationalgp.ViewModel.TeacherViewModel;
import com.example.educationalgp.databinding.FragmentTeacherLoginBinding;

import java.util.regex.Pattern;

public class TeacherLoginFragment extends Fragment {
    private FragmentTeacherLoginBinding binding;
    final TeacherViewModel teacherViewModel;
    String email = "", password="";
    public TeacherLoginFragment() {
        teacherViewModel = new TeacherViewModel();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTeacherLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.btnTeacherLogin.setOnClickListener(v -> loginTeacher());
        binding.btnSignupNow.setOnClickListener(v -> goToSignupPage());

        return view;    }



    private void loginTeacher() {
        getData();
        if(isDataValid()){
            teacherViewModel.loginTeacher(email, password, new StudentRepository.onAuthenticationListener() {
                @Override
                public void onSuccess() {
                    goToTeacherProfile();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(requireContext(), "من فضلك تأكد من بياناتك مرة اخرى", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private boolean isDataValid() {
        return !email.isEmpty() && !password.isEmpty() && isValidEmail(email);
    }


    private void getData(){
        getEmail();
        getPassword();
    }

    private void getPassword(){
        String p = binding.etTeacherPasswordLogin.getText().toString();
        if(TextUtils.isEmpty(p)){
            binding.etTeacherPasswordLogin.setError("من فضلك ادخل كلمة المرور هنا");
        }
        else{
            password = p;
        }
    }

    private void getEmail(){
        String e = binding.etTeacherEmailLogin.getText().toString();
        if(TextUtils.isEmpty(e)){
            binding.etTeacherEmailLogin.setError("من فضلك ادخل البريد الالكتروني هنا");
        }
        else if(!isValidEmail(e)){
            binding.etTeacherEmailLogin.setError("من فضلك تأكد من صيغة البريد الالكتروني");
        }
        else
            email = e;
    }

    private void goToSignupPage() {
        Intent intent = new Intent(getActivity(), SignUpActivity.class);
        startActivity(intent);
    }

    private void goToTeacherProfile() {
        Intent intent = new Intent(getActivity(), TeacherProfileActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }
}