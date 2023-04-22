package com.example.pangeea.ai;

import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Test_AI {
    int num_questions;
    String lesson;
    Context context;
    Boolean positive_negative[] = {true,false};
    Random r = new Random();
    public Test_AI(Context context, int num_questions, String lesson, TextView prompt, TextView A, TextView B, TextView C){
        this.context = context;
        this.num_questions = num_questions;
        this.lesson = lesson;
    }

    public List<Object> generate_test(){
        AI_core core = new AI_core(context);
        List<Object> test = new ArrayList<>();
        for(int i = 1;i<= num_questions;i++){
            HashMap<String,String> question = new HashMap<>();
            if(i < 6){
                question.put("prompt",core.AI_Text("O intrebare legata de lectia '"+lesson+"' este:"));
                if(positive_negative[r.nextInt(1)]){
                    question.put("1",core.AI_Text("Un raspuns corect la intrebarea'"+question.get("prompt")+"' este :"));
                    question.put("1_isvalid","valid");
                }else{
                    question.put("1",core.AI_Text("Un raspuns gresit la intrebarea'"+question.get("prompt")+"' este :"));
                    question.put("1_isvalid","invalid");
                }
                if(positive_negative[r.nextInt(1)]){
                    question.put("2",core.AI_Text("Un raspuns corect la intrebarea'"+question.get("prompt")+"' este :"));
                    question.put("2_isvalid","valid");
                }else{
                    question.put("2",core.AI_Text("Un raspuns gresit la intrebarea'"+question.get("prompt")+"' este :"));
                    question.put("2_isvalid","invalid");
                }
                if(positive_negative[r.nextInt(1)]){
                    question.put("3",core.AI_Text("Un raspuns corect la intrebarea'"+question.get("prompt")+"' este :"));
                    question.put("3_isvalid","valid");
                }else{
                    question.put("3",core.AI_Text("Un raspuns gresit la intrebarea'"+question.get("prompt")+"' este :"));
                    question.put("3_isvalid","invalid");
                }
            }else{
                question.put("prompt",core.AI_Text("O intrebare legata de lectia '"+lesson+"' este:"));
                if(positive_negative[r.nextInt(1)]){
                    question.put("1",core.AI_Text("Un raspuns corect la intrebarea'"+question.get("prompt")+"' este :"));
                    question.put("1_isvalid","valid");
                }else{
                    question.put("1",core.AI_Text("Un raspuns aproape corect la intrebarea'"+question.get("prompt")+"' este :"));
                    question.put("1_isvalid","invalid");
                }
                if(positive_negative[r.nextInt(1)]){
                    question.put("2",core.AI_Text("Un raspuns corect la intrebarea'"+question.get("prompt")+"' este :"));
                    question.put("2_isvalid","valid");
                }else{
                    question.put("2",core.AI_Text("Un raspuns semi-corect la intrebarea'"+question.get("prompt")+"' este :"));
                    question.put("2_isvalid","invalid");
                }
                if(positive_negative[r.nextInt(1)]){
                    question.put("3",core.AI_Text("Un raspuns corect la intrebarea'"+question.get("prompt")+"' este :"));
                    question.put("3_isvalid","valid");
                }else{
                    question.put("3",core.AI_Text("Un raspuns gresit la intrebarea'"+question.get("prompt")+"' este :"));
                    question.put("3_isvalid","invalid");
                }
            }
            test.add(question);



        }
        return test;
    }

}
