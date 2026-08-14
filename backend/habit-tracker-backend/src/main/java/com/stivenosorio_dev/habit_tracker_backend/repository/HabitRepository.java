package com.stivenosorio_dev.habit_tracker_backend.repository;

import com.google.cloud.firestore.*;
import com.stivenosorio_dev.habit_tracker_backend.model.Habit;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class HabitRepository {

    private static final String COLLECTION = "habits";
    private final Firestore firestore;

    public HabitRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public Habit save(Habit habit) throws ExecutionException, InterruptedException {
        DocumentReference docRef;
        if (habit.getId() == null) {
            docRef = firestore.collection(COLLECTION).document(); // genera ID nuevo
            habit.setId(docRef.getId());
        } else {
            docRef = firestore.collection(COLLECTION).document(habit.getId());
        }
        docRef.set(habit).get(); // .get() bloquea hasta que la escritura termine
        return habit;
    }

    public Habit findById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = firestore.collection(COLLECTION).document(id).get().get();
        return doc.exists() ? doc.toObject(Habit.class) : null;
    }

    public List<Habit> findByUserId(String userId) throws ExecutionException, InterruptedException {
        List<Habit> result = new ArrayList<>();
        QuerySnapshot snapshot = firestore.collection(COLLECTION)
                .whereEqualTo("userId", userId)
                .whereEqualTo("active", true)
                .get().get();

        for (QueryDocumentSnapshot doc : snapshot.getDocuments()) {
            result.add(doc.toObject(Habit.class));
        }
        return result;
    }

    public void deleteById(String id) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION).document(id).delete().get();
    }
}
