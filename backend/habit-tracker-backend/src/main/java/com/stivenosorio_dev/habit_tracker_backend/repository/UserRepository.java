package com.stivenosorio_dev.habit_tracker_backend.repository;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.stivenosorio_dev.habit_tracker_backend.model.User;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

@Repository
public class UserRepository {

    private static final String COLLECTION = "users";
    private final Firestore firestore;

    public UserRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public User findById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = firestore.collection(COLLECTION).document(id).get().get();
        return doc.exists() ? doc.toObject(User.class) : null;
    }

    public User save(User user) throws ExecutionException, InterruptedException {
        DocumentReference docRef;
        if (user.getId() == null) {
            docRef = firestore.collection(COLLECTION).document();
            user.setId(docRef.getId());
        } else {
            docRef = firestore.collection(COLLECTION).document(user.getId());
        }
        docRef.set(user).get();
        return user;
    }
}