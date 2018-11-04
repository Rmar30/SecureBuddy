package finalproject.comp3617.com.securebuddy.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@TypeConverters({DateTypeConverter.class})
@Database(entities = {RecordingSchedule.class}, version = 1, exportSchema = false)
public abstract class RecordingScheduleDB extends RoomDatabase {
    public abstract RecordingScheduleDAO recordingScheduleDAO();
    private static RecordingScheduleDB INSTANCE;

    static RecordingScheduleDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RecordingScheduleDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RecordingScheduleDB.class, "recordingScheduleDB").build();
                }
            }
        }
        return INSTANCE;
    }

}
