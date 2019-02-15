package com.example.supera.kamil.quicktravel.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Config class containing all needed information to run firebase.
 */
public class FirebaseConfig {

    /**
     * URL to firebase database.
     */
    private static String databaseURL = "https://quicktrans-a0ac8.firebaseio.com/";

    /**
     * Path to config generated in firebase console.
     */
    private static String pathToAdminConfigFile = "path/to/serviceAccountKey.json";

    /**
     * Class to initialize Firebase Admin SDK.
     * This is needed secure database content.
     * Usage:
     *     Use this function everytime before connecting to firebase database.
     * @throws IOException - When file is not available.
     */
    public static void initializeAdminSDK() throws IOException {
        FileInputStream serviceAccount = new FileInputStream(pathToAdminConfigFile);

        FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl(databaseURL)
            .build();

        FirebaseApp.initializeApp(options);
    }
}
