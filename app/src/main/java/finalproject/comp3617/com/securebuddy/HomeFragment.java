package finalproject.comp3617.com.securebuddy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Calendar;


public class HomeFragment extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button start = view.findViewById(R.id.startserv);
        Button end = view.findViewById(R.id.endserv);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // CREATE AN ALARM TO START SERVICE
                AlarmManager startAlarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                Intent startIntent = new Intent(getActivity(), VideoRecordingBroadCastReceiver.class);
                startIntent.setAction("START_VIDEO_RECORDING_SERVICE");
                PendingIntent pendingStartIntent = PendingIntent.getBroadcast(getContext(), 0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                startAlarm.setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(),pendingStartIntent);

            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // CREATE AN ALARM TO START SERVICE
                AlarmManager stopAlarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                Intent stopIntent = new Intent(getActivity(), VideoRecordingBroadCastReceiver.class);
                stopIntent.setAction("STOP_VIDEO_RECORDING_SERVICE");
                PendingIntent pendingStartIntent = PendingIntent.getBroadcast(getContext(), 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                stopAlarm.setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(),pendingStartIntent);

            }
        });

        return view;
    }

}
