package finalproject.comp3617.com.securebuddy;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import finalproject.comp3617.com.securebuddy.database.RecordingSchedule;
import finalproject.comp3617.com.securebuddy.database.RecordingScheduleViewModel;


public class RecordingScheduleFragment extends Fragment {

    private RecordingScheduleViewModel recordingScheduleViewModel;
    private RecordingScheduleRecyclerViewAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        recordingScheduleViewModel = ViewModelProviders.of(this).get(RecordingScheduleViewModel.class);

        recordingScheduleViewModel.getAllRecordingSchedules().observe(this, new Observer<List<RecordingSchedule>>() {
            @Override
            public void onChanged(@Nullable final List<RecordingSchedule> recordingScheduleList) {
                adapter.setRecordingSchedules(recordingScheduleList);
                adapter.notifyDataSetChanged();
            }
        });

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View drawer = inflater.inflate(R.layout.fragment_recording_schedule, container, false);
        RecyclerView rvScheduledTasks = drawer.findViewById(R.id.rvScheduledTasks);

        adapter = new RecordingScheduleRecyclerViewAdapter(getActivity());
        rvScheduledTasks.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvScheduledTasks.setHasFixedSize(true);

        DividerItemDecoration divider = new DividerItemDecoration(rvScheduledTasks.getContext(), DividerItemDecoration.VERTICAL);
        rvScheduledTasks.addItemDecoration(divider);

        rvScheduledTasks.setAdapter(adapter);

        return rvScheduledTasks;

    }

    @Override
    public void onResume() {
        super.onResume();
        recordingScheduleViewModel.deleteExpiredRecordingSchedules();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.recording_schedule, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.recordingScheduleAddButton:
                showAddRecordingSchedule();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAddRecordingSchedule() {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View dialogView = inflater.inflate(R.layout.dialog_add_recording_schedule,null, false);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();

        final TextView startDate = dialogView.findViewById(R.id.startDateDialog);
        final TextView endDate = dialogView.findViewById(R.id.endDateDialog);

        final Calendar startDateCalendar = Calendar.getInstance();
        final Calendar endDateCalendar = Calendar.getInstance();


        // CONFIGURE - START DATE DIALOG PICKER
        final DatePickerDialog.OnDateSetListener startDateDialog = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                startDateCalendar.set(Calendar.YEAR, year);
                startDateCalendar.set(Calendar.MONTH, monthOfYear);
                startDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd hh:mm a", Locale.getDefault());
                startDate.setText(dateFormat.format(startDateCalendar.getTime()));
            }

        };

        final TimePickerDialog.OnTimeSetListener startTimeDialog = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startDateCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                startDateCalendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd hh:mm a", Locale.getDefault());
                startDate.setText(dateFormat.format(startDateCalendar.getTime()));
            }

        };

        // ACTION - START DATE DIALOG
        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(), startTimeDialog, startDateCalendar
                        .get(Calendar.HOUR_OF_DAY), startDateCalendar.get(Calendar.MINUTE), false).show();
                new DatePickerDialog(getContext(), startDateDialog, startDateCalendar
                        .get(Calendar.YEAR), startDateCalendar.get(Calendar.MONTH), startDateCalendar.get(Calendar.DATE)).show();
            }
        });


        // CONFIGURE - END DATE DIALOG PICKER
        final DatePickerDialog.OnDateSetListener endDateDialog = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                endDateCalendar.set(Calendar.YEAR, year);
                endDateCalendar.set(Calendar.MONTH, monthOfYear);
                endDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd hh:mm a", Locale.getDefault());
                endDate.setText(dateFormat.format(endDateCalendar.getTime()));
            }

        };

        final TimePickerDialog.OnTimeSetListener endTimeDialog = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endDateCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                endDateCalendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd hh:mm a", Locale.getDefault());
                endDate.setText(dateFormat.format(endDateCalendar.getTime()));
            }

        };

        // ACTION - END DATE DIALOG
        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(), endTimeDialog, endDateCalendar
                        .get(Calendar.HOUR_OF_DAY), endDateCalendar.get(Calendar.MINUTE), false).show();
                new DatePickerDialog(getContext(), endDateDialog, endDateCalendar
                        .get(Calendar.YEAR), endDateCalendar.get(Calendar.MONTH), endDateCalendar.get(Calendar.DATE)).show();
            }
        });


        // ACTION - SAVING AND EXIT
        dialogView.findViewById(R.id.addRecordingScheduleButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(startDateCalendar.getTime().getTime() > endDateCalendar.getTime().getTime()) {
                    Toast.makeText(getContext(),R.string.validationAlarmInterval,Toast.LENGTH_SHORT).show();
                } else if ((startDateCalendar.getTime().getTime() < Calendar.getInstance().getTime().getTime())
                    || (endDateCalendar.getTime().getTime() < Calendar.getInstance().getTime().getTime())){
                    Toast.makeText(getContext(),R.string.validationAlarmPast,Toast.LENGTH_SHORT).show();
                } else if(startDateCalendar.getTimeInMillis() == endDateCalendar.getTimeInMillis()) {
                    Toast.makeText(getContext(),R.string.validationAlarmDateTimeIdentical,Toast.LENGTH_SHORT).show();
                } else {

                    final RecordingSchedule record = new RecordingSchedule();
                    record.setStartDate(startDateCalendar.getTime());
                    record.setEndDate(endDateCalendar.getTime());

                    int requestCodeStart = (int) startDateCalendar.getTime().getTime();
                    int requestCodeEnd = (int) endDateCalendar.getTime().getTime();

                    // INSERT INTO DATABASE
                    recordingScheduleViewModel.insert(record);

                    // CREATE AN ALARM TO START SERVICE
                    AlarmManager startAlarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    Intent startIntent = new Intent(getActivity(), VideoRecordingBroadCastReceiver.class);
                    startIntent.setAction("START_VIDEO_RECORDING_SERVICE");
                    PendingIntent pendingStartIntent = PendingIntent.getBroadcast(getContext(), requestCodeStart, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    startAlarm.setExact(AlarmManager.RTC_WAKEUP, startDateCalendar.getTimeInMillis(), pendingStartIntent);

                    //CREATE ALARM TO STOP SERVICE
                    AlarmManager stopAlarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    Intent stopIntent = new Intent(getActivity(), VideoRecordingBroadCastReceiver.class);
                    stopIntent.setAction("STOP_VIDEO_RECORDING_SERVICE");
                    PendingIntent pendingStopIntent = PendingIntent.getBroadcast(getActivity(), requestCodeEnd, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    stopAlarm.setExact(AlarmManager.RTC_WAKEUP, endDateCalendar.getTimeInMillis(), pendingStopIntent);

                    dialog.dismiss();
                }
            }


        });

        dialogView.findViewById(R.id.cancelRecordingScheduleButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setView(dialogView);
        dialog.show();

    }

}
