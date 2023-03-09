package com.example.pangeea;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pangeea.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class NFC_detection extends AppCompatActivity {
    IntentFilter[] mFilters;
    String[][] mTechLists;
    PendingIntent mPendingIntent;
    LinearLayout linear;
    DatabaseConnector connector = new DatabaseConnector(this);
    FirebaseAuth authenticator = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_detection);
        mPendingIntent =  PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);


        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }

        mFilters = new IntentFilter[]{
                ndef,
        };

        mTechLists = new String[][] { new String[] { MifareClassic.class.getName() } };


    }

    String[] readNFC(Intent intent) {
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




                auth = mfc.authenticateSectorWithKeyA(5, MifareClassic.KEY_DEFAULT);
                auth2 = mfc.authenticateSectorWithKeyA(5,MifareClassic.KEY_DEFAULT);
                if (auth2) {




                    Log.i(TAG,new String("TE DUC IN ASIA EXPRES"));
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

                    if(e.getBoolean("presence")){
                        Log.i("CEEEEEEEEEEEEEEEEE","EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
                        if(nfc_data[2].toCharArray()[0] == '1'){
                            Intent i = new Intent(NFC_detection.this,CSList.class);
                            i.putExtra("user_highschool",nfc_data[1]);

                            startActivity(i);
                        }else{
                            connector.upload_highschool_class_and_category(nfc_data[1],nfc_data[0],"0","");
                            startActivity(new Intent(NFC_detection.this,MainActivity.class));


                        }



                      //  mfc.writeBlock(25,"yes".getBytes(StandardCharsets.UTF_8));
                        TextView view = findViewById(R.id.textView);
                        view.setText(nfc_data[0]);
                    }else{
                        connector.make_presence(e.getString("user_class"),nfc_data[0],Long.parseLong(e.getString("hour_ms")),e.getString("teacher"));
                        Log.i("YYYYYYYYYYYYYEEEEEEEEE","EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");

                    }




                   // Toast.makeText(this, "read : " + str, Toast.LENGTH_SHORT).show();

                } else { Log.i("MUIE","GHICI CE (NU MERE AYHAHAHHAHA)");
                    nfc_data[1] = "auth error";

                }

            } catch (IOException e) {
                Log.e(TAG, e.getLocalizedMessage());

            }
        }
        return nfc_data;
    }



    @Override
    public void onResume() {
        super.onResume();
        try {
            NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(this);

            mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
        }catch (Exception e){e.printStackTrace();}
    }
    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);



        try{
            readNFC(intent);
        }catch(Exception e){
            e.printStackTrace();
        }

        Log.i("Foreground dispatch", "Discovered tag with intent: " + intent);

    }
    @Override
    public void onPause() {
        super.onPause();
        try {
            NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(this);

            mAdapter.disableForegroundDispatch(this);
        }catch(Exception e){e.printStackTrace();}
    }
}