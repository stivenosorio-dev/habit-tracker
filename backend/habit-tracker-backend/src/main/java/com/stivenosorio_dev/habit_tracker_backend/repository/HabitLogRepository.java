package com.stivenosorio_dev.habit_tracker_backend.repository;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.stivenosorio_dev.habit_tracker_backend.model.HabitLog;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

@Repository
public class HabitLogRepository {
    private static final String COLLECTION = "habitLogs";
    private final Firestore firestore;

    public HabitLogRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public HabitLog save(HabitLog log) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION).document();
        log.setId(docRef.getId());
        docRef.set(log).get();
        return log;
    }

    public HabitLog findLastByHabitId(String habitId) throws ExecutionException, InterruptedException {
        QuerySnapshot snapshot = firestore.collection(COLLECTION)
                .whereEqualTo("habitId", habitId)
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(1)
                .get().get();

        return snapshot.isEmpty() ? null : snapshot.getDocuments().get(0).toObject(HabitLog.class);
    }
}
