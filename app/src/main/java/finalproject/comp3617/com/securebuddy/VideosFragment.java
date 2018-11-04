package finalproject.comp3617.com.securebuddy;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class VideosFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View drawer = inflater.inflate(R.layout.fragment_displayvideos, container, false);
        RecyclerView rvVideos = drawer.findViewById(R.id.rvVideos);


        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES), "SecureBuddy");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("SecureBuddy", "Failed to create directory");
                return null;
            }
        }

        ArrayList<File> videoFiles = new ArrayList<>(Arrays.asList(mediaStorageDir.listFiles()));
        Collections.reverse(videoFiles);

        VideosRecyclerViewAdapter adapter = new VideosRecyclerViewAdapter(videoFiles);
        rvVideos.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvVideos.setHasFixedSize(true);
        rvVideos.setItemViewCacheSize(20);
        rvVideos.setDrawingCacheEnabled(true);

        DividerItemDecoration divider = new DividerItemDecoration(rvVideos.getContext(), DividerItemDecoration.VERTICAL);
        rvVideos.addItemDecoration(divider);


        rvVideos.setAdapter(adapter);



        return rvVideos;

    }
}
