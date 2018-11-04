package finalproject.comp3617.com.securebuddy.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface RecordingScheduleDAO {

    @Query("SELECT * FROM RecordingSchedule")
    LiveData<List<RecordingSchedule>> getAll();

    @Query("DELETE FROM RecordingSchedule WHERE endDate < :current")
    void deleteExpiredRecordingSchedules(Date current);

    @Query("SELECT EXISTS (SELECT * FROM RecordingSchedule WHERE (:start BETWEEN startDate AND endDate) OR (:end BETWEEN startDate AND endDate))")
    Boolean validationDateTimeOverlap(Date start, Date end);

    @Insert
    void insert(RecordingSchedule recordingSchedule);

    @Delete
    void delete(RecordingSchedule recordingSchedule);
}
