package com.example.educationalgp.Activity;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.educationalgp.Model.Question;
import com.example.educationalgp.Model.Quiz;
import com.example.educationalgp.R;
import com.example.educationalgp.ViewModel.QuizViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, RoleSelectorActivity.class);
            startActivity(intent);
        },3000);

        Question question = new Question();
        question.setQuestionText("يمكن أن تؤثر القوه علي إتجاه حركه الجسم المتحرك ");
        question.setOption_1("صح");
        question.setOption_2("خطأ");
        question.setAnswer("صح");

       Question question1 = new Question();
        question1.setQuestionText("لا بد للجسم المتحرك ان يقع تحت تأثير قوه........ ");
        question1.setOption_1("صح");
        question1.setOption_2("خطأ");
        question1.setAnswer("صح");

       Question question2 = new Question();
        question2.setQuestionText("أن الجسم عند القطب الشمالي اقل من وزنه عند خط الاستواء");
        question2.setOption_1("صح");
        question2.setOption_2("خطأ");
        question2.setAnswer("صح");

       Question question3 = new Question();
        question3.setQuestionText("يعتبر العالم كولوم هو مكتشف الجاذبيه الأرضية ");
        question3.setOption_1("صح");
        question3.setOption_2("خطأ");
        question3.setAnswer("خطأ");

        Question question4 = new Question();
        question4.setQuestionText("تزداد قيمه عجله الجاذبيه الارضيه كلما اقتربنا من مركز الأرض ");
        question4.setOption_1("صح");
        question4.setOption_2("خطأ");
        question4.setAnswer("صح");


        List<Question> questionList = new ArrayList<>();
        questionList.add(question);
        questionList.add(question1);
        questionList.add(question2);
        questionList.add(question3);
        questionList.add(question4);

        Question first = new Question("إذا أثرت قوه علي جسم متحرك في نفس إتجاه حركته فإن سرعته", "تزداد", "تقل", "تنعدم", "تظل ثابتة", "تزداد");
        Question sec = new Question("كل مما يأتي من القوي الطبيعية الأساسية عدا",
                "قوى المادة", "قوى كهرومغناطيسية", "قوى الجاذبية", "القوى النووية", "قوى المادة");
        Question third = new Question("مكتشف الجاذبيه الأرضية هو العالم",
                "نيوتن",
                "بلانك",
                "كولوم",
                "ارشميدس",
                "نيوتن");
        Question fourth = new Question("وزن الجسم على سطح الأرض يعتبر من؟",
                "قوى المادة", "قوى كهرومغناطيسية", "قوى الجاذبية", "القوى النووية", "قوى كهرومغناطيسية");

        Question fifth = new Question("يزداد الشغل المبذول في رفع الأجسام لاعلي بزيادة",
                "حجم الجسم",
                "كتلة الجسم",
                "كثافة الجسم",
                "لا توجد اجابة صحيحة",
                "كتلة الجسم");

        Question sixth = new Question("تقدر القوه بوحده",
                "الجرام",
                "نيوتن",
                "كولوم",
                "كيلو جرام",
                "نيوتن");
        Question seventh = new Question("يتغير وزن الجسم بتغيير ",
                "موضعه علي سطح الأرض",
                "حجمه",
                "كتلته",
                "طوله",
                "موضعه علي سطح الأرض");
        Question ninteth = new Question("إذا زادت كتله الجسم إلى الضعف فإن وزنة",
                "يقل للنصف",
                "يزداد لللضعف",
                "يظل ثابت",
                "يساوي كتلته",
                "يزداد لللضعف");
        Question tenth = new Question("جسم كتلته 50  كجم عند القطبين تكون كتلته............  50 كجم عند خط الاستواء",
                "اكبر من",
                "اصغر من",
                "يساوي",
                "اكبر من او يساوي",
                "يساوي");
        Question eleventh = new Question("تعتمد فكرة عمل.......... علي التأثير المغناطيس للتيار الكهربي ",
                "الجرس الكهربائي",
                "المصباح الكهربائي ",
                "الفرن الكهربائي ",
                "كل ما سبق",
                "الجرس الكهربائي");
        Question twelvth = new Question("يصنع قلب المغناطيسي الكهربي من.......... ",
                "الحديد الصلب",
                "الحديد المطاوع",
                "الحديد  الزهر",
                "الحديد المعزول",
                "الحديد المطاوع");
        Question q13 = new Question("يتم رفع الحديد الخردة في مصانع بأستخدام اوناش كهربائية من ِ....... ",
                "محرك كهربي",
                "ريموت كهربي",
                "مغناطيس كهربي",
                "الدينامو",
                "مغناطيس كهربي");

        Question q15 = new Question("الأجهزة الاتيه تعمل تأثير القوي الكهرومغناطيسيه عدا.......",
                "المغناطيس الكهربائي ",
                "الدينامو",
                "المصباح الكهربائي",
                "المحرك الكهربائي",
                "المصباح الكهربائي");
        Question q16 = new Question("نحصل على الطاقه الكهربية من.............",
                "الدينامو",
                "المغناطيس الكهربائي ",
                "المحرك الكهربائي",
                "العجله",
                "الدينامو");
        Question q17 = new Question("تستخدم............ في توليد الطاقة الكهربائية",
                "قوي الجاذبيه",
                "القوي النوويه الضعيفة",
                "القوي النوويه القويه",
                "قوي الماده",
                "القوي النوويه القويه");
        Question q18 = new Question("تعتمد فكره عمل القنبله الذرية على استخدام..........",
                "قوي الجاذبيه",
                "القوي النوويه الضعيفة",
                "القوي النوويه القويه",
                "قوي الماده",
                "القوي النوويه القويه");

        Question q19 = new Question("الإشاعات المستخدمه في علاج الأورام الخبيثه مصدرها.........",
                "قوي الجاذبيه",
                "القوي النوويه الضعيفة",
                "القوي النوويه القويه",
                "قوي الماده",
                "القوي النوويه الضعيفة");
        questionList.add(first);
        questionList.add(sec);
        questionList.add(third);
        questionList.add(fourth);
        questionList.add(fifth);
        questionList.add(seventh);
        questionList.add(sixth);
        questionList.add(ninteth);
        questionList.add(tenth);
        questionList.add(eleventh);
        questionList.add(twelvth);
        questionList.add(q13);
        questionList.add(q15);
        questionList.add(q16);
        questionList.add(q17);
        questionList.add(q18);
        questionList.add(q19);
        Quiz quiz = new Quiz(questionList.size(),questionList,"DEFAULT" );
        quiz.setId("un2less1");

        QuizViewModel quizViewModel = new QuizViewModel();
        quizViewModel.createQuiz(quiz);
    }
}