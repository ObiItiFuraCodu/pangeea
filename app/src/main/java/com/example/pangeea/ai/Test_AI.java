package com.example.pangeea.ai;

import android.content.Context;
import android.widget.LinearLayout;

import java.util.Random;

public class Test_AI {
    int num_questions;
    String lesson;
    Context context;
    Boolean positive_negative[] = {true,false};
    LinearLayout question_list;
    Random r = new Random();
    public Test_AI(Context context, LinearLayout question_list, String lesson, int num_questions){
        this.context = context;
        this.num_questions = num_questions;
        this.lesson = lesson;
        this.question_list = question_list;
    }


    public void generate_test(){
        AI_core core = new AI_core(context);
        for(int i = 1;i<= num_questions;i++){
           /* TextView prompt = new TextView(context);
            TextView a = new TextView(context);
            TextView b = new TextView(context);
            TextView c = new TextView(context);
            if(i < 6){
                core.AI_Text("O intrebare legata de lectia '"+lesson+"' este:",prompt);
                if(positive_negative[r.nextInt(1)]){
                    core.AI_Text("Un raspuns corect la intrebarea'"+prompt.getText().toString()+"' este :",a);
                    //question.put("1_isvalid","valid");
                }else{
                    core.AI_Text("Un raspuns corect la intrebarea'"+prompt.getText().toString()+"' este :",a);                    //question.put("1_isvalid","valid");
                }
                if(positive_negative[r.nextInt(1)]){
                    core.AI_Text("Un raspuns corect la intrebarea'"+prompt.getText().toString()+"' este :",b);                    //question.put("1_isvalid","valid");
                }else{
                    core.AI_Text("Un raspuns corect la intrebarea'"+prompt.getText().toString()+"' este :",b);                    //question.put("1_isvalid","valid");
                }
                if(positive_negative[r.nextInt(1)]){
                    core.AI_Text("Un raspuns corect la intrebarea'"+prompt.getText().toString()+"' este :",c);                    //question.put("1_isvalid","valid");
                }else{
                    core.AI_Text("Un raspuns corect la intrebarea'"+prompt.getText().toString()+"' este :",c);                    //question.put("1_isvalid","valid");
                }
            }else{
                core.AI_Text("O intrebare legata de lectia '"+lesson+"' este:",prompt);
                if(positive_negative[r.nextInt(1)]){
                    core.AI_Text("Un raspuns corect la intrebarea'"+prompt.getText().toString()+"' este :",a);                    //question.put("1_isvalid","valid");
                }else{
                    core.AI_Text("Un raspuns corect la intrebarea'"+prompt.getText().toString()+"' este :",a);                    //question.put("1_isvalid","valid");
                }
                if(positive_negative[r.nextInt(1)]){
                    core.AI_Text("Un raspuns corect la intrebarea'"+prompt.getText().toString()+"' este :",b);
                    //question.put("1_isvalid","valid");
                }else{
                    core.AI_Text("Un raspuns corect la intrebarea'"+prompt.getText().toString()+"' este :",b);
                    //question.put("1_isvalid","valid");
                }
                if(positive_negative[r.nextInt(1)]){
                    core.AI_Text("Un raspuns corect la intrebarea'"+prompt.getText().toString()+"' este :",c);
                    //question.put("1_isvalid","valid");
                }else{
                    core.AI_Text("Un raspuns corect la intrebarea'"+prompt.getText().toString()+"' este :",c);
                    //question.put("1_isvalid","valid");
                }
            }



           /// test.add(question);


*/
            core.generate_question(lesson,question_list,true,false,null);
        }

    }

}
