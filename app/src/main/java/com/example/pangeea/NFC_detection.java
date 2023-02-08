package com.example.pangeea;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

import java.util.Arrays;

public class NFC_detection extends AppCompatActivity {
    String datatype;
    IntentFilter text_filter;
    PendingIntent pendingintent;
    private TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_detection);


        try {
            Intent intent = new Intent(this,getClass());
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingintent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            text_filter =  new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED, "text/plain");

        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }


        readnfc(getIntent());



    }

    @Override
    protected void onStart() {
        super.onStart();
        enableRead();
    }

    @Override
    protected void onStop() {
        super.onStop();
        disableRead();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableRead();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        readnfc(getIntent());

    }
    private void enableRead(){
        NfcAdapter.getDefaultAdapter(this).enableForegroundDispatch(this,pendingintent, new IntentFilter[]{text_filter},null);
    }
    private void disableRead(){
        NfcAdapter.getDefaultAdapter(this).disableForegroundDispatch(this);
    }

    private void readnfc(Intent intent) {
        Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if(messages != null){
            for(Parcelable message : messages){
                NdefMessage ndef_message = (NdefMessage) message;
                for(NdefRecord record : ndef_message.getRecords()){
                    switch(record.getTnf()){
                        case NdefRecord.TNF_WELL_KNOWN:
                            textview.append("MERE:");
                            if(Arrays.equals(record.getType(),NdefRecord.RTD_TEXT)){
                               textview.append(new String(record.getPayload()));
                            }
                    }
                }
            }
        }
    }

}