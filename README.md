# Pangeea
**Despre cod**<br />
**Backend**:<br />
**Realizator:Habian Denis**<br />
Codul a fost facut in 2 limbaje de programare si anume Java si Python utilizand baza de date Firebase.Aplicatia este formata din numeroase clase impartite in 10 categorii (ai,backend,catalogue,content,custom elements,hour,main,other,task si test).Fiecare categorie se ocupa de cate un aspect al aplicatiei,clasele din folderul backend fiind baza aplicatiei.<br />
**AI:**<br />
Clasele din folderul AI se ocupa de functionalitatea aplicatiei numita “IHAI” (implementare hibrida AI).Aceasta se refera la implementarea inteligentei artificiale in aplicatie prin utilizarea API-ului OpenAI alaturi de o retea neuronala realizata in Python.
IHAI ofera aplicatiei un aspect “viu” aceasta devenind capabila sa ajute elevul si profesorul prin diverse moduri si anume:<br />
-Generare test<br />
Testele sunt generate in mod recursiv,initial fiind generate intrebarile apoi raspunsurile acestea putand fi corecte sau gresite.<br />
-Generare lectie<br />
DPDV al profesorului - generarea lectiei este bazata pe titlu alaturi de niste cuvinte cheie alese de profesor<br />
DPDV al elevului - generarea lectiei este bazata pe titlu iar in urma generarii elevul poate adresa intrebari<br />
-Explicare test<br />
Testele de tip ABC sunt corectate automat de catre aplicatie iar in urma corectarii elevul isi poate vedea nota alaturi de explicatii la fiecare intrebare.<br />
Toate functiile de mai sus au fost realizate cu ajutorul API-ului OpenAI apelat prin libraria Volley<br />
-Lectii si teste de imbunatatire (sortate si generate)<br />
Selectia lectiilor pentru imbunatatirea performantei unui elev este compusa dintr-o retea neuronala si un sistem simplu de selectie care se bazeaza pe secvente similare din titlul altor lectii cu titlul unui test sustinut de catre el la care a obtinut o nota mai mica de 5.<br />
Acestea functioneaza impreuna pentru a selecta lectiile similare dpdv al titlului nu numai din punct de vedere al familiei lexicale cat si al campului semantic<br />
Reteaua neuronala:<br />
Aceasta este formata din 3 straturi,primul format din 10 neuroni(input layer),al doilea din 5 si al treilea din 1 neuron(output layer).<br />
Aceasta preia 2 cuvinte transformate in numere de tip long printr-un cod ascii modificat si in functie de cat de asemanatoare sunt returneaza o variabila de tip float.Daca variabila este mai mare de 0.5,cuvintele sunt asemanatoare.<br />
Codul ascii modificat:<br />
Acesta este constituit din toate literele de la a la z in alfabetul englez sortate in functie de frecventa lor in cuvintele de zi cu zi <br />
Algoritmul simplu de selectie:<br />
Acesta selecteaza cuvintele in functie de numarul de secvente similare gasite in cele 2 titluri.<br />

```Java
//Exemplu cod:
 static int countDigit(int n)
    {
        if (n/10 == 0)
            return 1;
        else
            return 1 + countDigit(n / 10);
    }//numarare numere
    static int modified_ascii_value_of(char c){
        for(int i = 0;i< ascii.length;i++){
            if(ascii[i] == c){
                return i+1;
            }
        }
        return 0;
    }//valoarea in codul ASCII modificat
    static String converted_word(String a){
        a = a.toLowerCase();
        long s = 0;
        for(char c : a.toCharArray()){
            if(Character.isDigit(c) || Character.isLetter(c)){
                for(int i = 1;i<= countDigit(modified_ascii_value_of(c));i++){
                    s = s*10;
                }
      
                System.out.println(s);
                s = s+modified_ascii_value_of(c);
            }

        }
        System.out.println(s);
        return Long.toString(s);
    }//conversia cuvantului
    
    public boolean random() {
        return Math.random() < 0.5;
    }//boolean random pentru decizia corectitudinii unui raspuns generat in testele generate
    public static boolean[] generateBooleanVector() {
        boolean[] vector = new boolean[3];
        int index = new Random().nextInt(3);
        vector[index] = true;
        return vector;
    }//similar cu cel de mai sus

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
            //request body pentru API call
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
                    //Obtinerea textului din raspunsul call-ului

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
                //introducerea headerelor
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
        queue.add(request);//adaugarea call-ului

    }
//sistemul de sortare non AI
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
//sistemul de sortare AI
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
```
**Backend:**<br />
Clasele din folderul backend se ocupa de alimentarea aplicatiei cu date si sincronizarea lor cu aplicatia in timp real.<br />
Toate aceste clase trimit si primesc date din baza de date Firebase intr-un mod eficient,rapid si sigur.<br />
```Java
//Exemplu cod
  public void retrieve_lessons(String grade, LinearLayout linearl, String main_course){
        AI_core core = new AI_core(context);//Initializarea functiei IHAI
 
        store.collection("courses").document(grade).collection(grade)//Accesarea folder-ului cu lectii in functie de clasa elevului
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<DocumentSnapshot> list =  queryDocumentSnapshots.getDocuments();//Datele obtinute din baza de date
                        if(!(list == null || list.isEmpty())){
                            list = core.recommender_system(list,main_course);//Selectia lectiilor prin sistemul hibrid de sortare (AI si non AI)

                            for (DocumentSnapshot document : list) {
                                Button lesson_button = new Button(context);
                                lesson_button.setBackgroundColor(context.getResources().getColor(R.color.binaryblue));
                                lesson_button.setElevation(10f);
                                lesson_button.setText(document.getString("title"));//Adaugarea titlurilor si setarea unui OnClickListener pentru accesarea lor
                              lesson_button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(context, Lesson_viewer_nongenerated.class);
                                        i.putExtra("grade",grade);
                                        i.putExtra("title",document.getString("title"));
                                        context.startActivity(i);
                                    }
                                });
                                lesson_button.setElevation(10f);
                                linearl.addView(lesson_button);
                            }
                        }

                    }
                });
```
**Catalogue :**<br />
Clasele din folderul catalogue se ocupa de note,absente dar si de cresterea sau scaderea RP-urilor unui elev.RP-urile constituie sistemul de ranking al aplicatiei ce motiveaza fiecare elev sa invete si sa isi imbunatateasca notele.<br />
Sistemul de ranking are 5 rank-uri iar prin promovarea fiecarui rank elevul va primi cate un 10 la o materie la alegere.<br />
```Java
//Exemplu cod
 public void upload_mark(String class_marked, String pupil, String mark,String date,String test_name,boolean improvement_test,String subject){
        FirebaseUser user = auth.getCurrentUser();//Initializarea datelor

        Map<String,String> map = new HashMap<>();
        //Adaugarea detaliilor
        map.put("mark",mark);
        Map<String,String> test = new HashMap<>();
        test.put("BLBLBLBL","blblblb");
        int mark_int = Integer.parseInt(mark);
        increase_pupil_score(pupil,mark_int*2);
        HashMap<String,String> details = new HashMap<>();
        details.put("name",test_name);
        details.put("mark",mark);
        details.put("points",Integer.toString(mark_int*2));
        if(test_name != null){
            if(improvement_test){
                store.collection("users").document(pupil).collection("ranking_history").document(test_name)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Map<String, Object> test_received_details = documentSnapshot.getData();
                                HashMap<String,Object> improvement = new HashMap<>();
                                improvement.put("improvement_mark",mark);
                                improvement.put("improvement_points",Integer.toString(mark_int*2));
                                test_received_details.put("improvement",improvement);

                                increase_pupil_score(pupil,mark_int*2);//Incrementarea scorului elevului
                                store.collection("users").document(pupil).collection("ranking_history").document(test_name)
                                        .set(test_received_details);
                            }
                        });
            }else{
                store.collection("users").document(pupil).collection("ranking_history").document(test_name)
                        .set(details);
            }

        }
        store.collection("users").document(user.getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.get("user_category").equals("1")){
//Adaugarea detaliilor
                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(pupil).collection("marks").document(documentSnapshot.get("user_subject",String.class)).collection("marks").document(date)
                                    .set(map);
                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(pupil).collection("marks").document(documentSnapshot.get("user_subject",String.class))
                                    .set(test);
                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(pupil).collection("marks").document("overall").collection("marks").document(date)
                                    .set(map);
                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(pupil).collection("marks").document("overall")
                                    .set(test);
                        }else if(subject == null){
                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(user.getDisplayName()).collection("marks").document(test_name).collection("marks").document(date)
                                    .set(map);
                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(user.getDisplayName()).collection("marks").document(test_name)
                                    .set(test);
                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(user.getDisplayName()).collection("marks").document("overall").collection("marks").document(date)
                                    .set(map);
                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(user.getDisplayName()).collection("marks").document("overall")
                                    .set(test);
                        }else{

                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(user.getDisplayName()).collection("marks").document(test_name).collection("marks").document(date)
                                    .set(map);
                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(user.getDisplayName()).collection("marks").document(test_name)
                                    .set(test);
                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(user.getDisplayName()).collection("marks").document(subject).collection("marks").document(date)
                                    .set(map);
                            store.collection("highschools").document(documentSnapshot.get("user_highschool",String.class)).collection("classes").document(class_marked).collection("pupils").document(user.getDisplayName()).collection("marks").document(subject)
                                    .set(test);

                        }


                    }
                });
    }
```
**Content:**<br />
Clasele din folderul content se ocupa partial de continutul aplicatiei (dpdv al imbunatatirii performantei si al lectiilor).<br />
```Java
//Exemplu cod
public class Lesson_viewer extends AppCompatActivity {
//Generare lectii prin AI
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_viewer);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LinearLayout layout = findViewById(R.id.linearl_lesson_t);
        Bundle e = getIntent().getExtras();
        AI_core core = new AI_core(Lesson_viewer.this);
        String title = e.getString("title");
        List<String> answer_list = (List<String>) e.get("answer_list");
        core.generate_lessons(layout,answer_list,title); <----
    }
}

```
**Custom Elements:**<br />
Clasele din acest folder se ocupa de butoanele custom ce ofera aplicatiei o interfata placuta<br />
```Java
package com.example.pangeea.CustomElements;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.pangeea.R;

public class CustomPupilButton extends CardView {
    public CustomPupilButton(Context context) {
        super(context);
        init();
    }

    public CustomPupilButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomPupilButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    //Initializarea layout-ului
    private void init() {
        RelativeLayout.LayoutParams rootParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        rootParams.setMargins(16, 16, 16, 16);
        LinearLayout rootLayout = new LinearLayout(getContext());
        rootLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(rootLayout,rootParams);
        //adaugarea unui rootlayout pentru facilitarea aranjari elementelor in card

        GradientDrawable gradientDrawable2 = new GradientDrawable();
        gradientDrawable2.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable2.setColor(getResources().getColor(R.color.binaryblue));
        //setarea parametrilor

        float cornerRadiusInPixels2 = 20f;
        gradientDrawable2.setCornerRadius(cornerRadiusInPixels2);

        setBackground(gradientDrawable2);

        TextView text = new TextView(getContext());//Text cu numele elevului
        text.setText("NAME");
        text.setTextColor(getResources().getColor(R.color.white));
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(getResources().getColor(R.color.white));

        float cornerRadiusInPixels = 20f;
        gradientDrawable.setCornerRadius(cornerRadiusInPixels);

        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        textParams.setMargins(16, 16, 16, 0);

        ImageView view = new ImageView(getContext());//Imagine cu rank-ul elevului
        view.setImageResource(R.drawable.rankhigh);
        RelativeLayout.LayoutParams viewParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        RelativeLayout.LayoutParams viewParams2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        rootLayout.addView(text, textParams);
        rootLayout.addView(view, viewParams2);
    }
}
```

**Hour:**<br />
Clasele din folderul hour se ocupa de crearea orelor si participarea la acestea utilizand tehnologia IHAI si Smart Dock.<br />
Smart Dock este o tehnologie ce utilizeaza un dock-ing station fizic si capabilitatea NFC a telefonului pentru a asigura realizarea prezentei intr-un mod mult mai eficient.<br />
```Java
//Exemplu cod
public class Add_hour extends AppCompatActivity {
//Initializare
    int time;
    Calendar date;
    int dateinmillis;
    DatabaseConnector connector = new DatabaseConnector(this);
    List<Uri> list = new ArrayList<>();
    List<String> stringlist = new ArrayList<>();
    Boolean is_public = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hour);
        Bundle e = getIntent().getExtras();
        HourBackend backend1 = new HourBackend(this);
        TaskBackend backend2 = new TaskBackend(this);

        TextView addhour = findViewById(R.id.textView1);

        TextView generate_ai = findViewById(R.id.generate_ai_lesson);

        EditText title = findViewById(R.id.lesson_title);


        TextView select_date = findViewById(R.id.select_date_time);
        TextView upload_lessons = findViewById(R.id.add_lesson);
        EditText support_lesson_content = findViewById(R.id.support_lesson_content);
        Spinner support_lessons = findViewById(R.id.support_lessons);
        TextView add = findViewById(R.id.add_button);
        EditText details = findViewById(R.id.details);
        if(e.getString("lesson_content") != null){
            support_lesson_content.setText(e.getString("lesson_content"));
            title.setText(e.getString("title"));
        }



        if(e.getString("hour/task").equals("hour")){
            addhour.setText("ADD AN HOUR");
        }else{
            addhour.setText("ADD A TASK");
        }
        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();

            }
        });//Selectia datei
        upload_lessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });//adaugarea fisierelor pentru lectiile de curs
       /* add_ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        generate_ai.setOnClickListener(new View.OnClickListener() {//generarea unei lectii prin AI
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Add_hour.this, Generate_ai_lesson_teacher.class);
                i.putExtra("hour/task",e.getString("hour/task"));
                i.putExtra("class_selected",e.getString("class_selected"));
                if(title.getText().toString().equals("")){
                    Toast.makeText(v.getContext(),"you didn't enter a title",Toast.LENGTH_LONG).show();

                }else{
                    i.putExtra("title",title.getText().toString());
                    startActivity(i);
                    finish();
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {//Adaugarea lectiei
            @Override
            public void onClick(View v) {
                if(date == null){
                    Toast.makeText(Add_hour.this,"you didn't pick a time",Toast.LENGTH_LONG).show();
                }else{
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Make public")
                            .setMessage("Do you want the lesson to be public?")//Alegerea profesorului cu privire la publicitatea lectiei
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    is_public = true;
                                    if(e.getString("hour/task").equals("hour")){
                                        backend1.add_hour(date.getTimeInMillis(), (String) e.get("class_selected"),details.getText().toString(),list,title.getText().toString(),support_lesson_content.getText().toString(),is_public);
                                    }else{
                                        backend2.add_task(date.getTimeInMillis(), (String) e.get("class_selected"),details.getText().toString(),list,title.getText().toString(),support_lesson_content.getText().toString(),is_public);
                                    }
                                    startActivity(new Intent(Add_hour.this, MainActivity.class));
                                    finish();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(e.getString("hour/task").equals("hour")){

                                        backend1.add_hour(date.getTimeInMillis(), (String) e.get("class_selected"),details.getText().toString(),list,title.getText().toString(),support_lesson_content.getText().toString(),is_public);

                                    }else{
                                        backend2.add_task(date.getTimeInMillis(), (String) e.get("class_selected"),details.getText().toString(),list,title.getText().toString(),support_lesson_content.getText().toString(),is_public);
                                    }
                                    startActivity(new Intent(Add_hour.this, MainActivity.class));
                                    finish();
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }



            }
        });





    }
ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(!(getExtension(data.getData().toString()).equals("jpg") || getExtension(data.getData().toString()).equals("png") || getExtension(data.getData().toString()).equals("pdf")))
                        list.add(data.getData());
                        stringlist.add(data.getData().getLastPathSegment());
                        Spinner spinner = findViewById(R.id.support_lessons);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Add_hour.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,stringlist);
                        spinner.setAdapter(adapter);


                    }
                }
            });
    public void openFile(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent = Intent.createChooser(intent,"Choose NOW");
        activityResultLauncher.launch(intent);
    }//cod pentru selectia fisierelor
    public static String getExtension(String fullName) {
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    public void showDateTimePicker() {//cod pentru selectia datei
        final Calendar currentDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));


        new DatePickerDialog(Add_hour.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(Add_hour.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        dateinmillis = (int) date.getTimeInMillis();



                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

}
```
**Main:**<br />
Acestea sunt clasele de tip “home” ale aplicatiei de unde profesorul poate accesa capabilitatile aplicatiei<br />
```Java
//Exemplu cod
 //Initializare
    CatalogueBackend connector = new CatalogueBackend(this);
    HourBackend backend1 = new HourBackend(this);
    TaskBackend backend2 = new TaskBackend(this);
    TestBackend backend3 = new TestBackend(this);
    FirebaseFirestore store = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);








        //Crearea sidebarului si initializarea celorlalte
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;



            return super.onOptionsItemSelected(item);


    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Redirectionare in functie de butonul apasat
      
        switch (item.getItemId()) {

            case R.id.nav_logout: {
                backend1.log_out();
                finish();
                break;
            }
            case R.id.nav_add_hour: {


            }
            case R.id.see_corrected_tests:{
                startActivity(new Intent(MainActivity.this, See_corrected_tests.class));
                finish();
            }
            case R.id.pair_device:{
                Intent i = new Intent(MainActivity.this, NFC_detection.class);
                i.putExtra("Pair","ye");
                startActivity(i);
            }
            case R.id.my_class:{

                store.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                                .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                Intent i = new Intent(MainActivity.this,Class_info.class);
                                                i.putExtra("class_selected",documentSnapshot.get("user_class").toString());
                                                i.putExtra("pupil","ye");
                                                startActivity(i);
                                                finish();

                                            }
                                        });


            }
            case R.id.my_info:{
                store.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Intent i = new Intent(MainActivity.this, Pupil_info.class);
                                i.putExtra("pupil_class",documentSnapshot.get("user_class").toString());
                                i.putExtra("pupil_name",FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

                                i.putExtra("pupil","ye");
                                startActivity(i);
                                finish();

                            }
                        });

            }
        }

        return true;
    }
    @Override
    protected void onStart() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        super.onStart();

        store.collection("users").document(auth.getCurrentUser().getDisplayName())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        if(documentSnapshot.getString("user_category").equals("0")){
                            //navigationView.getMenu().findItem(R.id.nav_add_hour).setVisible(false);
                            connector.retrieve_materies((Spinner) navigationView.getMenu().findItem(R.id.nav_add_hour).getActionView());
                            navigationView.getMenu().findItem(R.id.nav_add_hour).setTitle("Materies");
                            navigationView.getMenu().findItem(R.id.pair_device).setVisible(false);
                            navigationView.getMenu().findItem(R.id.pair_device).setEnabled(false);




                        }else{
                            navigationView.getMenu().findItem(R.id.see_corrected_tests).setVisible(false);
                            navigationView.getMenu().findItem(R.id.my_info).setVisible(false);
                            navigationView.getMenu().findItem(R.id.my_info).setEnabled(false);
                            navigationView.getMenu().findItem(R.id.my_class).setVisible(false);
                            navigationView.getMenu().findItem(R.id.my_class).setEnabled(false);
                            connector.retrieveclasses((Spinner) navigationView.getMenu().findItem(R.id.nav_add_hour).getActionView());

                        }
                    }
                });


        backend1.import_hours(findViewById(R.id.liner3),findViewById(R.id.welcome_back),findViewById(R.id.scrollView1));
        backend2.import_tasks(findViewById(R.id.liner2),findViewById(R.id.scrollView2));
        backend3.import_tests(findViewById(R.id.liner1),findViewById(R.id.scrollView3));//Adaugarea datelor din backend

    }

```
**Task:**<br />
Clasele din acest folder sunt similare cu Hour diferenta fiind ca task nu dispune de Smart Dock<br />
**Test:**<br />
Aceste clase permit adaugarea unui test sau generarea lui,sustinerea unui test,corectarea lui automata sau manuala (in functie de tipul intrebarii) si explicarea lui in urma atribuirii notei.<br />
```Java
//Exemplu cod
//generarea unei intrebari cu ajutorul AI
 public void generate_question(String prompt, LinearLayout list, Boolean main, Boolean valid,TextView text_unmain){
        JSONObject requestBody = new JSONObject();
        //   final String[] output = {""};
        try {

            requestBody.put("model", "text-davinci-003");//TODO:TEST MODELS
            if(main){
                requestBody.put("prompt", context.getString(R.string.AI_QUESTION) +prompt+ context.getString(R.string.is));

            }else{
                if(valid){//generarea raspunsului in functie de validitatea ei
                    requestBody.put("prompt",context.getString(R.string.scientifically_accurate)+prompt+context.getString(R.string.is));                    //question.put("1_isvalid","valid");

                }else{
                    requestBody.put("prompt",context.getString(R.string.wrong_sci) +prompt+context.getString(R.string.is));                    //question.put("1_isvalid","valid");

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
                        if (choiceObject.getString("text").contains(context.getString(R.string.ras)) || choiceObject.getString("text").contains("Raspunsul") ||
                                choiceObject.getString("text").contains(context.getString(R.string.answe)) || choiceObject.getString("text").contains(context.getString(R.string.theans))
                                  || choiceObject.getString("text").contains("Răspuns") || choiceObject.getString("text").contains("Răspunsul") ||
                                choiceObject.getString("text").contains("răspuns") || choiceObject.getString("text").contains("răspunsul")){
                            generate_question(prompt,list,true,null,null);

                        }else{
                            Button button1 = (Button) button.getChildAt(0);
                            button1.setText(choiceObject.getString("text"));
                            boolean[] vector = generateBooleanVector();
                            custom_butt.setElevation(10f);
                            list.addView(custom_butt);
                            //aspectul recursiv al codului
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

```
**Other:**<br />
Clasele de tip other sunt clase care nu intra in nici o categorie,acestea asigurand anumite functionalitati ale aplicatiei cum ar fi Smart Dock.<br />
```Java
//Exemplu cod
 //Smart Dock
  String[] readNFC(Intent intent) {
  //citire NFC 
        String nfc_data[] = new String[3];
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            MifareClassic mfc = MifareClassic.get(tagFromIntent);
            byte[] data;

            try {
                mfc.connect();
                boolean auth = false;
                boolean auth2 = false;
                String cardData = null;




                auth = mfc.authenticateSectorWithKeyA(5, MifareClassic.KEY_DEFAULT);//citirea sectoarelor dock-ului
                auth2 = mfc.authenticateSectorWithKeyA(5,MifareClassic.KEY_DEFAULT);
                if (auth2) {




                    try{

                        byte[] bRead = mfc.readBlock(21);
                        String str = new String(bRead, StandardCharsets.US_ASCII);
                        // Log.i("hey", "read bytes : " + Arrays.toString(bRead));
                        //Log.i("hey", "read string : " + str);
                        nfc_data[0] = new String(mfc.readBlock(20),StandardCharsets.UTF_8);
                        nfc_data[1] = new String(mfc.readBlock(21),StandardCharsets.UTF_8);
                        nfc_data[2] = new String(mfc.readBlock(22),StandardCharsets.UTF_8);

                    }catch(Exception e){
                        e.printStackTrace();
                    }

                 //   nfc_data[3] = new String(mfc.readBlock(25),StandardCharsets.UTF_8);
                    Bundle e = getIntent().getExtras();
                    //Redirectionarea utilizatorului in functie de nevoie
                    if(e.getString("Pair") != null){
                        DatabaseConnector connector = new DatabaseConnector(NFC_detection.this);
                        connector.pair_device();
                        finish();

                    }
                    else if(e.getString("Test") != null){

                        Intent i = new Intent(this, Test_viewer_elev.class);
                        i.putExtra("hour_ms",e.getString("hour_ms"));
                        startActivity(i);
                        finish();
                        

                    }
```


**Interfata:**<br />
Realizator: Haiduc Darius<br />
Interfata a fost realizata in cadrul aplicatiei Android Studio, in fisiere de resursa XML. Interfata este compusa din numeroase clase, fiecare dintre acestea fiind folosite pentru a face aplicatia cat mai prietenoasa cu utilizatorul, fiind construite in mod intuitiv. Fiecare clasa a fost construita pe modelul Constraint Layout cu nested Constraint Layouts, pentru a permite adaptarea acesteia la cat mai multe tipuri de display, acestea incluzand de la telefoane, tablete la monitoare de calculator. Orice implementare de text a fost facuta pentru a scala pe fiecare tip de display, astfel pastrandu-si lizibiltatea in orice mediu. Interfata suporta Engleza si Romana, astfel aceasta aplicatie poate fi indreptata spre mediul intern (Romania) sau cel extern.f
Exemplu cod pentru o clasa de interfata average
```XML
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="8dp" //declararea unui layout tip constraint, care ne va ajuta sa asezam in layout elementele si va ajuta la scalabiltatea acestuia

    tools:context=".hour.Add_hour">


    <TextView
        android:id="@+id/add_lesson"
        android:layout_width="51dp"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:autoSizeTextType="uniform"
        android:background="@drawable/rounded"
        android:backgroundTint="#5D9CFB"
        android:fontFamily="@font/poppins_extrabold"
        android:maxLines="1"
        android:text="+"
        android:textAlignment="center"
        android:textColor="@color/white"
        app:layout_constraintBottom_toTopOf="@+id/guideline190"
        app:layout_constraintEnd_toStartOf="@+id/guideline35"
        app:layout_constraintHorizontal_bias="1.0"
        app:layout_constraintStart_toEndOf="@+id/textView5"
        app:layout_constraintTop_toTopOf="@+id/guideline188" /> //textview, element folosit pentru afisarea unui text stilizat

    <TextView
        android:id="@+id/textView1"
        android:layout_width="0dp"
        android:layout_height="0dp"

        android:text="@string/add_an_hour"
        android:textColor="@color/black"
        android:fontFamily="@font/poppins_extrabold"
        android:autoSizeTextType="uniform"
        app:layout_constraintBottom_toTopOf="@+id/guideline183"
        app:layout_constraintEnd_toStartOf="@+id/guideline35"
        app:layout_constraintHorizontal_bias="0.0"
        app:layout_constraintStart_toStartOf="@+id/guideline33"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/textView2"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:autoSizeTextType="uniform"
        android:fontFamily="@font/poppins_extrabold"
        android:text="@string/select_a_date_nand_time"
        android:textColor="@color/black"
        app:layout_constraintBottom_toTopOf="@+id/guideline184"
        app:layout_constraintEnd_toStartOf="@+id/guideline35"
        app:layout_constraintHorizontal_bias="0.0"
        app:layout_constraintStart_toStartOf="@+id/guideline33"
        app:layout_constraintTop_toBottomOf="@+id/textView1"
        app:layout_constraintVertical_bias="0.0" />

    <com.google.android.material.button.MaterialButton
        android:id="@+id/select_date_time"
        android:layout_width="0dp"
        android:layout_height="45dp"
        android:layout_weight="1"
        android:autoSizeTextType="uniform"
        app:cornerRadius="30sp"

        android:backgroundTint="#5D9CFB"
        android:fontFamily="@font/poppins_extrabold"
        android:maxLines="1"
        android:text="@string/add"
        android:textAlignment="center"
        android:textColor="@color/white"
        app:layout_constraintBottom_toTopOf="@+id/guideline185"
        app:layout_constraintEnd_toStartOf="@+id/guideline191"
        app:layout_constraintHorizontal_bias="1.0"
        app:layout_constraintStart_toStartOf="@+id/guideline33"
        app:layout_constraintTop_toBottomOf="@+id/textView2"
        app:layout_constraintVertical_bias="0.0" /> //practic este doar un buton, care poate fi stilizat mai usor.

    <TextView
        android:id="@+id/textView5"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:autoSizeTextType="none"
        android:fontFamily="@font/poppins_bold"
        android:gravity="bottom"
        android:hint="@string/add_some_details"
        android:inputType="textPersonName"
        android:text="@string/support_lessons"
        android:textColor="@color/black"
        android:textColorHint="@color/black"
        app:layout_constraintBottom_toTopOf="@+id/guideline189"
        app:layout_constraintEnd_toStartOf="@+id/guideline191"
        app:layout_constraintHorizontal_bias="1.0"
        app:layout_constraintStart_toStartOf="@+id/guideline396"
        app:layout_constraintTop_toTopOf="@+id/guideline187"
        app:layout_constraintVertical_bias="0.0" />

    <com.google.android.material.button.MaterialButton
        android:id="@+id/add_button"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:autoSizeTextType="uniform"
        android:backgroundTint="#5D9CFB"
        android:fontFamily="@font/poppins_extrabold"
        android:maxLines="1"
        android:text="@string/go"
        android:textAlignment="center"
        android:textColor="@color/white"
        app:cornerRadius="20sp"
        app:layout_constraintBottom_toTopOf="@+id/guideline274"
        app:layout_constraintEnd_toStartOf="@+id/guideline272"
        app:layout_constraintHorizontal_bias="0.559"
        app:layout_constraintStart_toStartOf="@+id/guideline191"
        app:layout_constraintTop_toTopOf="@+id/guideline273"
        app:layout_constraintVertical_bias="0.0" />

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/guideline33"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:orientation="vertical"
        app:layout_constraintGuide_percent="0.03"
        app:layout_constraintStart_toStartOf="parent" /> //elementul principal care ne ajuta sa facem layoutul sa arate la fel pe fiecare tip de display, acesta scaland                                                             //dupa guidelineurile impuse de noi, in unitatea percentage(din rezolutia ecranului).

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/guideline35"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        app:layout_constraintGuide_percent="0.97" />

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/guideline183"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        app:layout_constraintGuide_percent="0.08071136" />

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/guideline184"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        app:layout_constraintGuide_percent="0.2" />

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/guideline185"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        app:layout_constraintGuide_percent="0.3" />

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/guideline186"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        app:layout_constraintGuide_percent="0.36" />

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/guideline388"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        app:layout_constraintGuide_percent="0.37" />

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/guideline187"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        app:layout_constraintGuide_percent="0.48" />

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/guideline188"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        app:layout_constraintGuide_percent="0.64" />

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/guideline189"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        app:layout_constraintGuide_percent="0.55" />

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/guideline190"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintGuide_percent="0.69"
        app:layout_constraintStart_toStartOf="parent" />

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/guideline191"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        app:layout_constraintGuide_percent="0.36" />

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/guideline192"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:orientation="vertical"
        app:layout_constraintGuide_percent="0.84"
        app:layout_constraintTop_toTopOf="parent" />

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/guideline206"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        app:layout_constraintGuide_percent="0.13" />

    <EditText
        android:id="@+id/support_lesson_content"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:ems="10"
        android:inputType="textPersonName"
        app:layout_constraintBottom_toTopOf="@+id/guideline231"
        app:layout_constraintEnd_toStartOf="@+id/guideline35"
        app:layout_constraintHorizontal_bias="1.0"
        app:layout_constraintStart_toStartOf="@+id/guideline33"
        app:layout_constraintTop_toTopOf="@+id/guideline190"
        app:layout_constraintVertical_bias="0.0"
        android:background="@drawable/roundedmock"
        android:elevation="10dp"/> //EditText, folosit pentru a primi input de la un utilizator

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/guideline231"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        app:layout_constraintGuide_percent="0.77" />

    <TextView
        android:id="@+id/generate_ai_lesson"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:text="@string/or_generate_a_lesson"
        app:layout_constraintBottom_toTopOf="@+id/add_button"
        app:layout_constraintEnd_toStartOf="@+id/guideline35"
        app:layout_constraintStart_toStartOf="@+id/guideline272"
        app:layout_constraintTop_toTopOf="@+id/guideline231"
        app:layout_constraintVertical_bias="0.274" />

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/guideline272"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        app:layout_constraintGuide_percent="0.7007299" />

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/guideline273"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        app:layout_constraintGuide_percent="0.86868685" />

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/guideline274"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        app:layout_constraintGuide_percent="0.94276094" />

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toTopOf="@+id/guideline186"
        app:layout_constraintEnd_toStartOf="@+id/guideline35"
        app:layout_constraintStart_toStartOf="@+id/guideline33"
        app:layout_constraintTop_toTopOf="@+id/guideline185"
        android:background="@drawable/roundedmock"
        android:elevation="10dp">

        <EditText
            android:id="@+id/lesson_title"
            android:layout_width="0dp"
            android:layout_height="0dp"
            android:autoSizeTextType="uniform"
            android:backgroundTint="@android:color/transparent"

            android:ellipsize="start"
            android:ems="10"
            android:fontFamily="@font/poppins_bold"
            android:gravity="start"
            android:inputType="textPersonName"
            android:textColor="@color/black"
            android:textColorHint="@color/black"
            android:textSize="12sp"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toStartOf="@+id/guideline379"
            app:layout_constraintStart_toStartOf="@+id/guideline364"
            app:layout_constraintTop_toTopOf="parent" />

        <androidx.constraintlayout.widget.Guideline
            android:id="@+id/guideline364"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintGuide_percent="0.3638814"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <androidx.constraintlayout.widget.Guideline
            android:id="@+id/guideline379"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginEnd="16dp"
            android:orientation="vertical"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintGuide_percent="0.98921835" />

        <TextView
            android:id="@+id/textView36"
            android:layout_width="0dp"
            android:layout_height="0dp"
            android:fontFamily="@font/poppins_extrabold"
            android:text="@string/add_a_title"
            android:textColor="@color/black"
            app:layout_constraintBottom_toTopOf="@+id/guideline381"
            app:layout_constraintEnd_toStartOf="@+id/lesson_title"
            app:layout_constraintHorizontal_bias="0.0"
            app:layout_constraintStart_toStartOf="@+id/guideline382"
            app:layout_constraintTop_toTopOf="@+id/guideline380"
            app:layout_constraintVertical_bias="0.0" />

        <androidx.constraintlayout.widget.Guideline
            android:id="@+id/guideline380"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            app:layout_constraintGuide_percent="0.18867925" />

        <androidx.constraintlayout.widget.Guideline
            android:id="@+id/guideline381"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            app:layout_constraintGuide_percent="0.8490566" />

        <androidx.constraintlayout.widget.Guideline
            android:id="@+id/guideline382"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginBottom="16dp"
            android:orientation="vertical"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintGuide_percent="0.043126684" />

    </androidx.constraintlayout.widget.ConstraintLayout>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:background="@drawable/roundedmock"
        android:elevation="10dp"
        app:layout_constraintBottom_toTopOf="@+id/guideline187"
        app:layout_constraintEnd_toStartOf="@+id/guideline35"
        app:layout_constraintStart_toStartOf="@+id/guideline33"
        app:layout_constraintTop_toTopOf="@+id/guideline388">

        <EditText
            android:id="@+id/details"
            android:layout_width="0dp"
            android:layout_height="0dp"
            android:autoSizeTextType="uniform"

            android:backgroundTint="@android:color/transparent"

            android:ems="10"
            android:fontFamily="@font/poppins_bold"

            android:inputType="textPersonName"

            android:textColor="@color/black"

            android:textColorHint="@color/black"
            android:textSize="12sp"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toStartOf="@+id/guideline384"
            app:layout_constraintStart_toStartOf="@+id/guideline383"
            app:layout_constraintTop_toTopOf="parent" />

        <androidx.constraintlayout.widget.Guideline
            android:id="@+id/guideline383"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintGuide_percent="0.3638814"
            app:layout_constraintTop_toTopOf="parent" />

        <androidx.constraintlayout.widget.Guideline
            android:id="@+id/guideline384"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginEnd="16dp"
            android:orientation="vertical"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintGuide_percent="0.9838275" />

        <TextView
            android:id="@+id/textView37"
            android:layout_width="0dp"
            android:layout_height="0dp"
            android:fontFamily="@font/poppins_extrabold"
            android:text="@string/add_some_details"
            android:textColor="@color/black"
            app:layout_constraintBottom_toTopOf="@+id/guideline386"
            app:layout_constraintEnd_toStartOf="@+id/guideline383"
            app:layout_constraintHorizontal_bias="0.0"
            app:layout_constraintStart_toStartOf="@+id/guideline385"
            app:layout_constraintTop_toTopOf="@+id/guideline387"
            app:layout_constraintVertical_bias="0.0" />

        <androidx.constraintlayout.widget.Guideline
            android:id="@+id/guideline385"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="16dp"
            android:layout_marginTop="16dp"
            android:orientation="vertical"
            app:layout_constraintGuide_percent="0.045822103"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <androidx.constraintlayout.widget.Guideline
            android:id="@+id/guideline386"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            app:layout_constraintGuide_percent="0.6041667" />

        <androidx.constraintlayout.widget.Guideline
            android:id="@+id/guideline387"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            app:layout_constraintGuide_percent="0.16666667" />
    </androidx.constraintlayout.widget.ConstraintLayout>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toTopOf="@+id/guideline188"
        app:layout_constraintEnd_toStartOf="@+id/guideline35"
        app:layout_constraintHorizontal_bias="1.0"
        app:layout_constraintStart_toStartOf="@+id/guideline33"
        app:layout_constraintTop_toTopOf="@+id/guideline189"
        app:layout_constraintVertical_bias="0.0"
        android:background="@drawable/roundedmock"
        android:elevation="10dp">

        <Spinner
            android:id="@+id/support_lessons"
            android:layout_width="0dp"
            android:layout_height="0dp"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toStartOf="@+id/guideline391"
            app:layout_constraintHorizontal_bias="0.0"
            app:layout_constraintStart_toStartOf="@+id/guideline389"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintVertical_bias="0.0" /> //spinner, o lista de elemente aflate intr-un drop down box.

        <androidx.constraintlayout.widget.Guideline
            android:id="@+id/guideline389"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintGuide_percent="0.043126684"
            app:layout_constraintTop_toTopOf="parent" />

        <androidx.constraintlayout.widget.Guideline
            android:id="@+id/guideline391"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginBottom="16dp"
            android:orientation="vertical"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintGuide_percent="0.95956874" />

        <androidx.constraintlayout.widget.Guideline
            android:id="@+id/guideline394"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:orientation="horizontal"
            app:layout_constraintGuide_percent="0.12658228"
            app:layout_constraintTop_toTopOf="parent" />

        <androidx.constraintlayout.widget.Guideline
            android:id="@+id/guideline395"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:orientation="horizontal"
            app:layout_constraintGuide_percent="0.721519"
            app:layout_constraintTop_toTopOf="parent" />
    </androidx.constraintlayout.widget.ConstraintLayout>

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/guideline396"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:orientation="vertical"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintGuide_percent="0.06569343"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>

```

**Bibliografie**<br />
Tensorflow Tutorial - https://www.youtube.com/watch?v=FQ_0RBXM7_8 <br />
NFC - http://mifareclassicdetectiononandroid.blogspot.com/2011/04/reading-mifare-classic-1k-from-android.html <br />
Volley API - https://www.c-sharpcorner.com/article/how-to-implement-chatgpt-in-android-application/<br />
Tutorial Sidebar - https://www.geeksforgeeks.org/navigation-drawer-in-android/<br />
Algoritmi selectie fisiere - stackoverflow  <br />
Speech recognition - https://medium.com/voice-tech-podcast/android-speech-to-text-tutorial-8f6fa71606ac<br />
Alert dialog - https://stackoverflow.com/questions/2115758/how-do-i-display-an-alert-dialog-on-android <br />
File extension checker - stack overflow<br />
Elemente custom - Canva<br />
Algoritmii au fost modificati pentru a se potrivi cu nevoie de functionare ale aplicatiei.<br />
Bibliografie Tehnologii <br />
Android Studio - https://developer.android.com/studio<br />
OpenAi - https://openai.com/<br />
Firebase - https://firebase.google.com<br />
