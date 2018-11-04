package finalproject.comp3617.com.securebuddy.database;

import android.app.Application;
import android.arch.persistence.room.Room;

public class RecordingScheduleApplication extends Application {

    private static RecordingScheduleApplication INSTANCE;
    private static final String DATABASE_NAME = "recordingScheduleDB";

    private RecordingScheduleDB database;

    public static RecordingScheduleApplication get() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // create database
        database = Room.databaseBuilder(getApplicationContext(), RecordingScheduleDB.class, DATABASE_NAME)
                .build();

        INSTANCE = this;
    }

    public RecordingScheduleDB getDB() {
        return database;
    }
}
