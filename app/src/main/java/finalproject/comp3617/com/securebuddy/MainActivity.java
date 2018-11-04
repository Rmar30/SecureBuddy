package finalproject.comp3617.com.securebuddy;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import finalproject.comp3617.com.securebuddy.database.RecordingScheduleViewModel;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    public static SurfaceHolder mSurfaceHolder;
    private FragmentManager manager;
    public static RecordingScheduleViewModel recordingScheduleViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
        };

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        requestIgnoreBatteryOptimization();

        SurfaceView mSurfaceView = findViewById(R.id.hiddenSurfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setFixedSize(1,1);
        mSurfaceHolder.addCallback(this);

        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frame, new HomeFragment()).commit();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        recordingScheduleViewModel = ViewModelProviders.of(this).get(RecordingScheduleViewModel.class);
    }


    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.frame, new HomeFragment()).commit();
                    setTitle("Secure Buddy");
                    return true;
                case R.id.navigation_recordings:
                    manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.frame, new VideosFragment()).commit();
                    setTitle("Recordings");
                    return true;
                case R.id.navigation_alarms:
                    manager.beginTransaction().replace(R.id.frame, new RecordingScheduleFragment()).commit();
                    setTitle("Scheduler");
                    return true;
            }
            return false;
        }
    };


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void requestIgnoreBatteryOptimization() {
        Intent intent = new Intent();
        String packageName = this.getPackageName();
        PowerManager powerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
            this.startActivity(intent);

        }
    }

}
