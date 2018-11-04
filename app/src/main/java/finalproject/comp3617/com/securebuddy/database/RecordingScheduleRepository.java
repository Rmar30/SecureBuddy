package finalproject.comp3617.com.securebuddy.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

class RecordingScheduleRepository {

    private final RecordingScheduleDAO recordingScheduleDAO;
    private final LiveData<List<RecordingSchedule>> recordingScheduleList;

    RecordingScheduleRepository(Application application) {
        RecordingScheduleDB db = RecordingScheduleDB.getDatabase(application);
        recordingScheduleDAO = db.recordingScheduleDAO();
        recordingScheduleList = recordingScheduleDAO.getAll();
    }

    LiveData<List<RecordingSchedule>> getAllRecordingSchedules() {
        return recordingScheduleList;
    }

    public void insert (RecordingSchedule recordingSchedule) {
        new insertAsyncTask(recordingScheduleDAO).execute(recordingSchedule);
    }

    private static class insertAsyncTask extends AsyncTask<RecordingSchedule, Void, Void> {

        private final RecordingScheduleDAO asyncTaskDao;

        insertAsyncTask(RecordingScheduleDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final RecordingSchedule... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void delete (RecordingSchedule recordingSchedule) {
        new deleteAsyncTask(recordingScheduleDAO).execute(recordingSchedule);
    }

    private static class deleteAsyncTask extends AsyncTask<RecordingSchedule, Void, Void> {

        private final RecordingScheduleDAO asyncTaskDao;

        deleteAsyncTask(RecordingScheduleDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final RecordingSchedule... params) {
            asyncTaskDao.delete(params[0]);
            return null;
        }
    }

    public void deleteExpiredRecordingSchedules() {
        new deleteExpiredAsyncTask(recordingScheduleDAO).execute();
    }

    private static class deleteExpiredAsyncTask extends AsyncTask<Date, Void, Void> {

        private final RecordingScheduleDAO asyncTaskDao;

        deleteExpiredAsyncTask(RecordingScheduleDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Date... params) {
            Date current = Calendar.getInstance().getTime();
            asyncTaskDao.deleteExpiredRecordingSchedules(current);
            return null;
        }
    }


}
