package com.example.pangeea.ai;

import android.content.Context;

import com.example.pangeea.ai.AI_core;

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
        //course_material.put("courses",core.AI_Text("generate a lesson with the title : " + lesson_title));
       // course_material.put("quizzes",core.AI_Text("generate a quizz based on the lesson : " + lesson_title));
        return course_material;
    }

}
