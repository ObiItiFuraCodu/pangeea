package com.example.pangeea;

import android.app.PendingIntent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;

public class Nfc_reader {
    public static final String Error_detected = "No nfc";
    public static final String Success = "Written successfully";
    public static final String Error = "Error during Writing";
    NfcAdapter nfc_adapter;
    PendingIntent pending_intent;
    IntentFilter writing_tag_filters;
    Tag my_tag;
    public void read(){

    }
}
