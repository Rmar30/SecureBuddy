package finalproject.comp3617.com.securebuddy;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import finalproject.comp3617.com.securebuddy.database.RecordingScheduleViewModel;


// TODO: UPDATE CLASS TO USE NEW CAMERA2 API; DUE TO API 23 NEEDED TO SUPPORT SetExactAndAllowWhileIdle method

public class VideoRecordingService extends Service {

    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int MEDIA_TYPE_VIDEO = 2;

    private static final String TAG = "Video Recorder";
    private static Camera videoCamera;
    private MediaRecorder mediaRecorder;
    private SurfaceHolder mSurfaceHolder;
    private RecordingScheduleViewModel recordingScheduleViewModel;


    @Override
    public void onCreate() {
        super.onCreate();
        recordingScheduleViewModel = MainActivity.recordingScheduleViewModel;
        mSurfaceHolder = MainActivity.mSurfaceHolder;
        startRecording();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecording();
    }


    private void startRecording(){
        try {
            videoCamera = getCameraInstance(getApplicationContext());

            try {
                Camera.Parameters p = videoCamera.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                videoCamera.setParameters(p);
            } catch (Exception e){
                Log.d(TAG, "Camera Parameter Settings: " + e.getMessage());
            }


            videoCamera.setPreviewDisplay(mSurfaceHolder);
            videoCamera.startPreview();
            videoCamera.unlock();

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setOrientationHint(90);
            mediaRecorder.setCamera(videoCamera);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
            mediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
            mediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

            try {
                mediaRecorder.prepare();
            } catch (IllegalStateException e) {
                Log.d(TAG, "IllegalStateException Preparing MediaRecorder: " + e.getMessage());
                releaseMediaRecorder();
                return;
            } catch (IOException e) {
                Log.d(TAG, "IOException Preparing MediaRecorder: " + e.getMessage());
                releaseMediaRecorder();
                return;
            }

            mediaRecorder.start();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException Starting MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
        } catch (IOException e) {
            Log.d(TAG, "IOException Previewing MediaRecorder: " + e.getMessage());
        }

    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            releaseMediaRecorder();
            videoCamera.release();

        }
        recordingScheduleViewModel.deleteExpiredRecordingSchedules();
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }

    private static Camera getCameraInstance(Context context){
        videoCamera = null;
        try {
            videoCamera = Camera.open();
        }
        catch (Exception e){
            Toast.makeText(context,"Camera is not Available",Toast.LENGTH_SHORT).show();
        }
        return videoCamera;
    }


    private void releaseMediaRecorder(){
        if (mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }


    // FILE PATHING AND SAVING LOCATION
    //TODO: include support for photo snap shots later

    private static File getOutputMediaFile(int type){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES), "SecureBuddy");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("SecureBuddy", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(new Date());
        File mediaFile;
        switch (type) {
            case MEDIA_TYPE_IMAGE:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "IMG_" + timeStamp + ".jpg");
                break;
            case MEDIA_TYPE_VIDEO:
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "VID_" + timeStamp + ".mp4");
                break;
            default:
                return null;
        }

        return mediaFile;
    }

}