package com.example.pangeea.ai;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.pangeea.R;
import com.google.firebase.firestore.DocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AI_core {
    private String apiUrl = "https://api.openai.com/v1/completions";
    private String accessToken = "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";
    String result = "";
    Interpreter tflite;
   // HashMap<String,Integer> ascii = new HashMap<>();

    List<String> modified_ascii =Arrays.asList(new String[]{"e","t","a","o","i","n","s","h","r","d","l","c","u","m","w","f","g","y","p","b","v","k","j","x","q","z"}) ;
    static char[] ascii = "etaoinshrdlcumwfgypbvkjxqz".toCharArray();

    static int countDigit(int n)
    {
        if (n/10 == 0)
            return 1;
        else
            return 1 + countDigit(n / 10);
    }
    static int modified_ascii_value_of(char c){
        for(int i = 0;i< ascii.length;i++){
            if(ascii[i] == c){
                return i+1;
            }
        }
        return 0;
    }
    static String converted_word(String a){
        a = a.toLowerCase();
        long s = 0;
        for(char c : a.toCharArray()){
            if(Character.isDigit(c) || Character.isLetter(c)){
                for(int i = 1;i<= countDigit(modified_ascii_value_of(c));i++){
                    s = s*10;
                }
                /// System.out.println((int)c);
                //System.out.println(countDigit(modified_ascii_value_of(c)));
                // System.out.println(modified_ascii_value_of(c));
                System.out.println(s);
                s = s+modified_ascii_value_of(c);
            }

        }
        System.out.println(s);
        return Long.toString(s);
    }
    Context context;
    public boolean getRandomBoolean() {
        return Math.random() < 0.5;
        //I tried another approaches here, still the same result
    }
    public static boolean[] generateBooleanVector() {
        boolean[] vector = new boolean[3];
        int index = new Random().nextInt(3);
        vector[index] = true;
        return vector;
    }
    public AI_core(Context context){
        this.context = context;
    }

    public void AI_Text(String input,TextView view){
        JSONObject requestBody = new JSONObject();
        //   final String[] output = {""};
        try {

            requestBody.put("model", "text-davinci-003");
            requestBody.put("prompt", input);
            requestBody.put("max_tokens", 1000);
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
        int timeoutMs = 250000; // 25 seconds timeout
        RetryPolicy policy = new DefaultRetryPolicy(timeoutMs, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }
    public void AI_Lesson(String input, TextView view, Button enter, EditText got_questions){
        JSONObject requestBody = new JSONObject();
        //   final String[] output = {""};
        try {

            requestBody.put("model", "text-davinci-003");
            requestBody.put("prompt", "Genereaza o lectie cu titlul"+input+"\n Lectie:");
            requestBody.put("max_tokens", 1000);
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
                    enter.setVisibility(View.VISIBLE);
                    got_questions.setVisibility(View.VISIBLE);
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
        int timeoutMs = 250000; // 25 seconds timeout
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
                    requestBody.put("prompt","Un raspuns corect din punct de vedere stiintific la intrebarea'"+prompt+"' este :");                    //question.put("1_isvalid","valid");

                }else{
                    requestBody.put("prompt","Un raspuns gresit din punct de vedere stiintific la intrebarea'"+prompt+"' este :");                    //question.put("1_isvalid","valid");

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
                    CustomButtonView custom_butt = new CustomButtonView(context);
                    LinearLayout button = (LinearLayout) custom_butt.getChildAt(0);
                    if(main){
                        if (choiceObject.getString("text").contains("Raspuns") || choiceObject.getString("text").contains("Raspunsul") ||
                                choiceObject.getString("text").contains("raspuns") || choiceObject.getString("text").contains("raspunsul")
                                  || choiceObject.getString("text").contains("Răspuns") || choiceObject.getString("text").contains("Răspunsul") ||
                                choiceObject.getString("text").contains("răspuns") || choiceObject.getString("text").contains("răspunsul")){
                            generate_question(prompt,list,true,null,null);

                        }else{
                            Button button1 = (Button) button.getChildAt(0);
                            button1.setText(choiceObject.getString("text"));
                            boolean[] vector = generateBooleanVector();
                            custom_butt.setElevation(10f);
                            list.addView(custom_butt);
                            generate_question(choiceObject.getString("text"),list,false,vector[0], (TextView) button.getChildAt(1));
                            generate_question(choiceObject.getString("text"),list,false,vector[1], (TextView) button.getChildAt(2));
                            generate_question(choiceObject.getString("text"),list,false,vector[2], (TextView) button.getChildAt(3));

                        }

                    }else{
                        text_unmain.setText(choiceObject.getString("text"));
                        if(valid){
                            text_unmain.setBackgroundColor(context.getResources().getColor(R.color.green));
                        }else{
                            text_unmain.setBackgroundColor(context.getResources().getColor(R.color.red));
                        }
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
                generate_question(prompt,list,main,valid,text_unmain);
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
        int timeoutMs = 250000; // 25 seconds timeout
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

    private boolean filtering_system_nonai(String course_1,String course_2){
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
    private boolean filtering_system_ai(String course_1,String course_2){
        try {
            tflite = new Interpreter(loadModelFile());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        if(course_1 != null && course_2 != null){
            String course_1_words[] = course_1.split(" ");
            String course_2_words[] = course_2.split(" ");

            for(String course1_word : course_1_words){

                for(String course2_word : course_2_words){





                    if(doInference(converted_word(course1_word),converted_word(course2_word)) > 0.5){
                        return true;
                    }

                }
            }
        }

    return false;
    }
    public  List<DocumentSnapshot> recommender_system(List<DocumentSnapshot> courses, String course_name){
        List<DocumentSnapshot> output = new ArrayList<>();

        for(DocumentSnapshot document : courses){
            if(filtering_system_nonai(document.get("title",String.class),course_name) || filtering_system_ai(document.get("title",String.class),course_name)){
                output.add(document);

            }
        }

        return output;
    }
    public void generate_lessons(LinearLayout layout,List<String> questions,String title){
        CustomButtonLesson lesson = new CustomButtonLesson(context);
        Button button = (Button) lesson.getChildAt(0);
        button.setText(title);
        AI_Text("Genereaza o lectie de 300 de cuvinte legata de " + title + "\n Lectie :", (TextView) lesson.getChildAt(1));
        layout.addView(lesson);
        for(String question : questions){
            CustomButtonLesson lesson1 = new CustomButtonLesson(context);
            Button button1 = (Button) lesson1.getChildAt(0);
            button1.setText(question);
            AI_Text("Genereaza un raspuns de 100 de cuvinte la intrebarea " + question + "\n Raspuns :", (TextView) lesson1.getChildAt(1));
            layout.addView(lesson1);

        }

    }
    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor=context.getAssets().openFd("linearRegressionModel_android.tflite");
        FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();
        long startOffset=fileDescriptor.getStartOffset();
        long declareLength=fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declareLength);
    }
    private float doInference(String inputString1,String inputString2) {
        long[] inputVal=new long[2];

        inputVal[0]=Long.parseLong(inputString1);
        inputVal[1]=Long.parseLong(inputString2);
        float[][] output=new float[1][1];
        tflite.run(inputVal,output);
        return output[0][0];
    }





}
