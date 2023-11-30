package com.example.educationalgp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.educationalgp.Dao.QuestionDao;
import com.example.educationalgp.Dao.QuizDao;
import com.example.educationalgp.Dao.StudentDao;
import com.example.educationalgp.Dao.TeacherDao;
import com.example.educationalgp.Model.Question;
import com.example.educationalgp.Model.Quiz;
import com.example.educationalgp.Model.Teacher;
import com.example.educationalgp.Model.Student;

@Database(entities = {Student.class, Teacher.class, Quiz.class, Question.class}, version = 1)
public abstract class mDatabase extends RoomDatabase {

    private static mDatabase myDatabase;
    public abstract StudentDao studentDao();
    public abstract TeacherDao teacherDao();
    public abstract QuizDao quizDao();
    public abstract QuestionDao questionDao();



    public static synchronized mDatabase getInstance(Context context){
        if (myDatabase == null){
            myDatabase = Room.databaseBuilder(
                    context,
                    mDatabase.class,
                    "app_db"
            ).fallbackToDestructiveMigration().addCallback(myCallback).build();
        }
        return myDatabase;
    }


    public static RoomDatabase.Callback myCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new Thread(() -> {
                StudentDao studentDao = myDatabase.studentDao();
                TeacherDao teacherDao = myDatabase.teacherDao();
                QuestionDao questionDao = myDatabase.questionDao();
                QuizDao quizDao = myDatabase.quizDao();
            });
        }
    };

}
