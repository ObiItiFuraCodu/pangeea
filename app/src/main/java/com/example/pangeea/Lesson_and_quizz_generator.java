package com.example.pangeea;

import android.content.Context;

import org.checkerframework.checker.units.qual.A;

import java.util.HashMap;
import java.util.Map;

public class Lesson_and_quizz_generator {
    Context context;
    String lesson_title;
    public Lesson_and_quizz_generator(Context context,String lesson_title){
        this.context = context;
        this.lesson_title = lesson_title;
    }
    AI_core core = new AI_core(context);
    public Map<String,Object> generate_course_material(){
        Map<String,Object> course_material = new HashMap<>();
        course_material.put("courses",core.AI_text_2("generate a lesson with the title : " + lesson_title));
        course_material.put("quizzes",core.AI_text_2("generate a quizz based on the lesson : " + lesson_title));
        return course_material;
    }

}
