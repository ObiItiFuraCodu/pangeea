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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class NFC_detection extends AppCompatActivity {
    IntentFilter[] mFilters;
    String[][] mTechLists;
    PendingIntent mPendingIntent;
    LinearLayout linear;

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

        // Setup a tech list for all NfcF tags
        mTechLists = new String[][] { new String[] { MifareClassic.class.getName() } };


    }

    void readNFC(Intent intent) {
        // 1) Parse the intent and get the action that triggered this intent
        String action = intent.getAction();
        // 2) Check if it was triggered by a tag discovered interruption.
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            //  3) Get an instance of the TAG from the NfcAdapter
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            // 4) Get an instance of the Mifare classic card from this TAG intent
            MifareClassic mfc = MifareClassic.get(tagFromIntent);
            byte[] data;

            try {       //  5.1) Connect to card
                mfc.connect();
                boolean auth = false;
                String cardData = null;
                // 5.2) and get the number of sectors this card has..and loop thru these sectors

                Log.i(TAG,new String("TE DUC IN ASIA EXPRES"));



                auth = mfc.authenticateSectorWithKeyA(2, MifareClassic.KEY_DEFAULT);
                if (auth) {




                    // 6.2) In each sector - get the block count

                    byte[] bRead = mfc.readBlock(8);
                    String str = new String(bRead, StandardCharsets.US_ASCII);
                    Log.i("hey", "read bytes : " + Arrays.toString(bRead));
                    Log.i("hey", "read string : " + str);

                    Toast.makeText(this, "read : " + str, Toast.LENGTH_SHORT).show();

                } else { Log.i("MUIE","GHICI CE (NU MERE AYHAHAHHAHA)");

                }

            } catch (IOException e) {
                Log.e(TAG, e.getLocalizedMessage());

            }
        }// End of method
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
            readNFC(intent);}catch(Exception e){
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