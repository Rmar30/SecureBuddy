package finalproject.comp3617.com.securebuddy.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class RecordingScheduleViewModel extends AndroidViewModel{


    private final RecordingScheduleRepository recordingScheduleRepository;
    private final LiveData<List<RecordingSchedule>> recordingScheduleList;


    public RecordingScheduleViewModel (Application application) {
        super(application);
        recordingScheduleRepository = new RecordingScheduleRepository(application);
        recordingScheduleList = recordingScheduleRepository.getAllRecordingSchedules();
    }

    public LiveData<List<RecordingSchedule>> getAllRecordingSchedules() { return recordingScheduleList; }

    public void deleteExpiredRecordingSchedules() {
        recordingScheduleRepository.deleteExpiredRecordingSchedules();
    }

    public void insert(RecordingSchedule recordingSchedule) {
        recordingScheduleRepository.insert(recordingSchedule);
    }

    public void delete(RecordingSchedule recordingSchedule) {
        recordingScheduleRepository.delete(recordingSchedule);
    }


}
