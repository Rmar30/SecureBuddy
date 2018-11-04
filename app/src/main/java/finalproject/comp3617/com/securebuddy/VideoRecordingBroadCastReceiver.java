package finalproject.comp3617.com.securebuddy;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class VideoRecordingBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equalsIgnoreCase("START_VIDEO_RECORDING_SERVICE")) {
            if (!isVideoRecording(context, VideoRecordingService.class) && checkCameraHardware(context))  {
                Intent startIntent = new Intent(context,VideoRecordingService.class);
                context.startService(startIntent);
            }
        }

        if(intent.getAction().equalsIgnoreCase("STOP_VIDEO_RECORDING_SERVICE")) {
            if (isVideoRecording(context, VideoRecordingService.class) && checkCameraHardware(context)) {
                Intent endIntent = new Intent(context,VideoRecordingService.class);
                context.stopService(endIntent);
            }
        }
    }

    private boolean isVideoRecording(Context context,Class<?> videoServiceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        // Iterate through Serivce List to determine if Service is Running
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (videoServiceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

}
