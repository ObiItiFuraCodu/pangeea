package com.example.pangeea;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;

/*import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;*/
import com.google.firebase.firestore.DocumentSnapshot;
//import com.theokanning.openai.completion.CompletionChoice;
//import com.theokanning.openai.completion.CompletionRequest;
//import com.theokanning.openai.service.OpenAiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AI_core {
    private String apiUrl = "https://api.openai.com/v1/completions";
    private String accessToken = "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";
    Context context;
    OkHttpClient client = new OkHttpClient();

    public AI_core(Context context){
        this.context = context;
    }

  /*  public void AI_Text(String input, TextView responseview){
        JSONObject requestBody = new JSONObject();
     //   final String[] output = {""};
        try {

            requestBody.put("model", "text-davinci-003");
            requestBody.put("prompt", input);
            requestBody.put("max_tokens", 100);
            requestBody.put("temperature", 1);
            requestBody.put("top_p", 1);
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
                    responseview.setText(choiceObject.getString("text"));
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

    }*/
    public void AI_text_3(String prompt,TextView result){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        RequestBody formBody = new FormBody.Builder()
                .add("model", "text-davinci-002")
                .add("prompt", prompt)
                .add("max_tokens", "100")
                .add("temperature", "1")
                .add("top_p", "1")
                .add("frequency_penalty", "0.0")
                .add("presence_penalty", "0.0")
                .build();









        Request request = new Request.Builder()
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .url("https://api.openai.com/v1/engines/text-davinci-003/completions")
                .post(formBody)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = client.newCall(request).execute();
            Log.i("YEAH",  response.body().string());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public List<String> AI_text_2(String prompt){
       /* OpenAiService service = new OpenAiService(accessToken);
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("Somebody once told me the world is gonna roll me")
                .model("ada")
                .echo(true)
                .build();
        List<CompletionChoice> list =  service.createCompletion(completionRequest).getChoices();
        List<String> results = new ArrayList<>();
        for(CompletionChoice choice : list){
            results.add(choice.getText());
        }*/
        return new ArrayList<>();
    }
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




}
