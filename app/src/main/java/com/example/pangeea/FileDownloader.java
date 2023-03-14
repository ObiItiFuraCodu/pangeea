package com.example.pangeea;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileDownloader {



    private static boolean isVirtualFile(Context context, Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!DocumentsContract.isDocumentUri(context, uri)) {
                return false;
            }
            Cursor cursor = context.getContentResolver().query(
                    uri,
                    new String[]{DocumentsContract.Document.COLUMN_FLAGS},
                    null, null, null);
            int flags = 0;
            if (cursor.moveToFirst()) {
                flags = cursor.getInt(0);
            }
            cursor.close();
            return (flags & DocumentsContract.Document.FLAG_VIRTUAL_DOCUMENT) != 0;
        } else {
            return false;
        }
    }
    private static InputStream getInputStreamForVirtualFile(Context context, Uri uri, String mimeTypeFilter)
            throws IOException {

        ContentResolver resolver = context.getContentResolver();
        String[] openableMimeTypes = resolver.getStreamTypes(uri, mimeTypeFilter);
        if (openableMimeTypes == null || openableMimeTypes.length < 1) {
            throw new FileNotFoundException();
        }
        return resolver
                .openTypedAssetFileDescriptor(uri, openableMimeTypes[0], null)
                .createInputStream();
    }
    private static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
    public static boolean saveFile(Context context, String source, String destinationDir, String destFileName) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child(source).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                InputStream input = null;
                boolean hasError = false;

                try {
                    if (isVirtualFile(context, uri)) {
                        input = getInputStreamForVirtualFile(context, uri, getMimeType(source));
                    } else {
                        input = context.getContentResolver().openInputStream(uri);
                    }

                    boolean directorySetupResult;
                    File destDir = new File(destinationDir);
                    if (!destDir.exists()) {
                        directorySetupResult = destDir.mkdirs();
                    } else if (!destDir.isDirectory()) {
                        directorySetupResult = replaceFileWithDir(destinationDir);
                    } else {
                        directorySetupResult = true;
                    }

                    if (!directorySetupResult) {
                        hasError = true;
                    } else {
                        String destination = destinationDir + File.separator + destFileName;
                        int originalsize = input.available();

                        bis = new BufferedInputStream(input);
                        bos = new BufferedOutputStream(new FileOutputStream(destination));
                        byte[] buf = new byte[originalsize];
                        bis.read(buf);
                        do {
                            bos.write(buf);
                        } while (bis.read(buf) != -1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    hasError = true;
                } finally {
                    try {
                        if (bos != null) {
                            bos.flush();
                            bos.close();
                        }
                    } catch (Exception ignored) {
                    }
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
      return true;
    }

    private static boolean replaceFileWithDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return true;
            }
        } else if (file.delete()) {
            File folder = new File(path);
            if (folder.mkdirs()) {
                return true;
            }
        }
        return false;
    }
}
