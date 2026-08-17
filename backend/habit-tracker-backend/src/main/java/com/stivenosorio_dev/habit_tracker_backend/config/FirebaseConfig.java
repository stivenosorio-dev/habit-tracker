package com.stivenosorio_dev.habit_tracker_backend.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() throws IOException {
        String credencialesJson = System.getenv("FIREBASE_CREDENTIALS_JSON");

        InputStream serviceAccount;
        if (credencialesJson != null && !credencialesJson.isEmpty()) {
            // Producción: la variable de entorno contiene el JSON completo como texto
            serviceAccount = new ByteArrayInputStream(credencialesJson.getBytes(StandardCharsets.UTF_8));
        } else {
            // Desarrollo local: se usa el archivo físico
            serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase-service-account.json");
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }

    @Bean
    public Firestore firestore() {
        return FirestoreClient.getFirestore();
    }
}
