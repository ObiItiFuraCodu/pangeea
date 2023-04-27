package com.example.pangeea.ai;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pangeea.CustomElements.CustomButtonLesson;
import com.example.pangeea.CustomElements.CustomButtonView;
import com.google.firebase.firestore.DocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AI_core {
    private String apiUrl = "https://api.openai.com/v1/completions";
    private String accessToken = "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";
    String result = "";
    Context context;


    public AI_core(Context context){
        this.context = context;
    }

    public void AI_Text(String input,TextView view){
        JSONObject requestBody = new JSONObject();
        //   final String[] output = {""};
        try {

            requestBody.put("model", "text-davinci-003");
            requestBody.put("prompt", input);
            requestBody.put("max_tokens", 500);
            requestBody.put("temperature", 1.0);
            requestBody.put("top_p", 1.0);
            requestBody.put("stop",null);
            requestBody.put("frequency_penalty", 0.0);
            requestBody.put("presence_penalty", 0.0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, apiUrl, requestBody, new Response.Listener < JSONObject > () {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray choicesArray = response.getJSONArray("choices");
                    JSONObject choiceObject = choicesArray.getJSONObject(0);
                    view.setText(choiceObject.getString("text"));
                    Log.e("API Response", response.toString());
                    //Toast.makeText(MainActivity.this,text,Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API Error", error.toString());
            }
        }) {
            @Override
            public Map< String, String > getHeaders() throws AuthFailureError {
                Map < String, String > headers = new HashMap< >();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
            @Override
            protected Response < JSONObject > parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }
        };
        int timeoutMs = 25000; // 25 seconds timeout
        RetryPolicy policy = new DefaultRetryPolicy(timeoutMs, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }
    public void generate_question(String prompt, LinearLayout list, Boolean main, Boolean valid,TextView text_unmain){
        JSONObject requestBody = new JSONObject();
        //   final String[] output = {""};
        try {

            requestBody.put("model", "text-davinci-003");
            if(main){
                requestBody.put("prompt", "O intrebare scurta legata de lectia '"+prompt+"' este:");

            }else{
                if(valid){
                    requestBody.put("prompt","Un raspuns scurt corect la intrebarea'"+prompt+"' este :");                    //question.put("1_isvalid","valid");

                }else{
                    requestBody.put("prompt","Un raspuns scurt gresit la intrebarea'"+prompt+"' este :");                    //question.put("1_isvalid","valid");

                }
            }
            requestBody.put("max_tokens", 500);
            requestBody.put("temperature", 1.0);
            requestBody.put("top_p", 1.0);
            requestBody.put("stop",null);
            requestBody.put("frequency_penalty", 0.0);
            requestBody.put("presence_penalty", 0.0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, apiUrl, requestBody, new Response.Listener < JSONObject > () {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray choicesArray = response.getJSONArray("choices");
                    JSONObject choiceObject = choicesArray.getJSONObject(0);
                    CustomButtonView button = new CustomButtonView(context);
                    if(main){
                        Button button1 = (Button) button.getChildAt(0);
                        button1.setText(choiceObject.getString("text"));
                        list.addView(button);
                    }else{
                        text_unmain.setText(choiceObject.getString("text"));
                    }

                    if(main){
                        generate_question(choiceObject.getString("text"),list,false,true, (TextView) button.getChildAt(1));
                        generate_question(choiceObject.getString("text"),list,false,true, (TextView) button.getChildAt(2));
                        generate_question(choiceObject.getString("text"),list,false,false, (TextView) button.getChildAt(3));


                    }

                    Log.e("API Response", response.toString());
                    //Toast.makeText(MainActivity.this,text,Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API Error", error.toString());
            }
        }) {
            @Override
            public Map< String, String > getHeaders() throws AuthFailureError {
                Map < String, String > headers = new HashMap< >();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
            @Override
            protected Response < JSONObject > parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }
        };
        int timeoutMs = 25000; // 25 seconds timeout
        RetryPolicy policy = new DefaultRetryPolicy(timeoutMs, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }
   /* public void AI_text_3(String prompt,TextView result){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(mediaType,
                "{\n" +
                        "  \"model\": \"text-davinci-002\",\n" +
                        "  \"prompt\": \""+prompt+"\",\n" +
                        "  \"max_tokens\": 100,\n" +
                        "  \"temperature\": 1.0,\n" +
                        "  \"top_p\": 1.0,\n" +
                        "  \"n\": 1,\n" +
                        "  \"stop\": null,\n" +
                        "  \"frequency_penalty\": 0.0,\n" +
                        "  \"presence_penalty\": 0.0\n" +
                        "}");

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            Log.i("RESPONSE",response.body().string());
            try {

                JSONObject object = new JSONObject(String.valueOf(response.body()));
                JSONArray array = object.getJSONArray("choices");
                String result_text = array.getJSONObject(0).getString("text");
                result.setText(result_text.trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private boolean filtering_system(String course_1,String course_2){
        int nr = 0;
        if(course_1 == null || course_2 == null){
            return false;
        }
        for(int i = 0;i< course_1.length();i++){
            for(int j = i+1;j < course_1.length();j++){
                if(course_2.contains(course_1.substring(i,j))){
                    nr++;
                }
            }
        }
        if(nr >= 2){
            return true;
        }else{
            return false;
        }


    }
    public  List<DocumentSnapshot> recommender_system(List<DocumentSnapshot> courses, String course_name){
        List<DocumentSnapshot> output = new ArrayList<>();

        for(DocumentSnapshot document : courses){
            if(filtering_system(document.get("name",String.class),course_name)){
                output.add(document);

            }
        }

        return output;
    }
    public void generate_lessons(LinearLayout layout,List<String> questions,String title){
        CustomButtonLesson lesson = new CustomButtonLesson(context);
        Button button = (Button) lesson.getChildAt(0);
        button.setText(title);
        AI_Text("Genereaza o lectie de 250 de cuvinte legata de " + title + "\n Lectie :", (TextView) lesson.getChildAt(1));
        layout.addView(lesson);
        for(String question : questions){
            CustomButtonLesson lesson1 = new CustomButtonLesson(context);
            Button button1 = (Button) lesson1.getChildAt(0);
            button1.setText(question);
            AI_Text("Genereaza un raspuns de 100 de cuvinte la intrebarea " + title + "\n Raspuns :", (TextView) lesson1.getChildAt(1));
            layout.addView(lesson1);

        }

    }





}
