package finalproject.comp3617.com.securebuddy;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import finalproject.comp3617.com.securebuddy.database.RecordingSchedule;
import finalproject.comp3617.com.securebuddy.database.RecordingScheduleViewModel;

public class RecordingScheduleRecyclerViewAdapter extends RecyclerView.Adapter<RecordingScheduleRecyclerViewAdapter.RecordingScheduleViewHolder> {

    private final Context context;
    private final LayoutInflater mInflater;
    private List<RecordingSchedule> recordingSchedules;

    public class RecordingScheduleViewHolder extends RecyclerView.ViewHolder {

        private final TextView startDateDisplay;
        private final TextView endDateDisplay;
        private final Button deleteRecordingScheduleButton;



        private RecordingScheduleViewHolder(View recordingView) {
            super(recordingView);

            startDateDisplay = recordingView.findViewById(R.id.startDateDisplay);
            endDateDisplay = recordingView.findViewById(R.id.endDateDisplay);
            deleteRecordingScheduleButton = recordingView.findViewById(R.id.deleteRecordingScheduleButton);
        }
    }

    RecordingScheduleRecyclerViewAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecordingScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View recordingView = mInflater.inflate(R.layout.row_recordingschedule, parent, false);

        return new RecordingScheduleViewHolder(recordingView);
    }

    @Override
    public void onBindViewHolder(RecordingScheduleViewHolder viewHolder, final int position) {

        if (recordingSchedules != null) {

            final RecordingSchedule current = recordingSchedules.get(position);
            final RecordingScheduleViewModel viewModel = ViewModelProviders.of((FragmentActivity) context).get(RecordingScheduleViewModel.class);

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd hh:mm a", Locale.getDefault());
            viewHolder.startDateDisplay.setText(dateFormat.format(current.getStartDate()));
            viewHolder.endDateDisplay.setText(dateFormat.format(current.getEndDate()));


            viewHolder.deleteRecordingScheduleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setCancelable(false);
                    alertDialog.setTitle(context.getString(R.string.deleteConfirmationTitle));
                    alertDialog.setMessage(context.getString(R.string.deleteConfirmationMessage));
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(recordingSchedules.size() > 0) {

                                // Remove from List
                                recordingSchedules.remove(position);
                                notifyItemRemoved(position);

                                // Remove from DB
                                viewModel.delete(current);

                                int requestCodeStart = (int) current.getStartDate().getTime();
                                int requestCodeEnd = (int) current.getEndDate().getTime();

                                // Remove Start Video Recording Alarm
                                AlarmManager startAlarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                Intent startIntent = new Intent(context, VideoRecordingBroadCastReceiver.class);
                                startIntent.setAction("START_VIDEO_RECORDING_SERVICE");
                                PendingIntent pendingStartIntent = PendingIntent.getBroadcast(context, requestCodeStart, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                startAlarm.cancel(pendingStartIntent);
                                pendingStartIntent.cancel();

                                // Remove Stop Video Recording Alarm
                                AlarmManager stopAlarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                Intent stopIntent = new Intent(context, VideoRecordingBroadCastReceiver.class);
                                stopIntent.setAction("STOP_VIDEO_RECORDING_SERVICE");
                                PendingIntent pendingStopIntent = PendingIntent.getBroadcast(context, requestCodeEnd, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                pendingStopIntent.cancel();
                                stopAlarm.cancel(pendingStopIntent);
                                pendingStopIntent.cancel();
                            }
                        }
                    });
                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                }
            });


        } else {
            viewHolder.startDateDisplay.setText("No Start Date");
            viewHolder.startDateDisplay.setText("No End Date");
        }
    }

    void setRecordingSchedules(List<RecordingSchedule> recordingSchedulesList){
        recordingSchedules = recordingSchedulesList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (recordingSchedules != null)
            return recordingSchedules.size();
        else return 0;
    }


}
