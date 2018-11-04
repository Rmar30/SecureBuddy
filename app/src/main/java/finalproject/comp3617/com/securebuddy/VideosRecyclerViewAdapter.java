package finalproject.comp3617.com.securebuddy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;


public class VideosRecyclerViewAdapter extends RecyclerView.Adapter<VideosRecyclerViewAdapter.ViewHolder> {

    private final List<File> videoList;
    private Context context;

    public VideosRecyclerViewAdapter(List<File> videoFiles) {
        videoList = videoFiles;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView fileView;
        final TextView fileDate;
        final TextView fileSize;
        final Button fileDeleteButton;


        ViewHolder(View itemView) {
            super(itemView);

            fileView = itemView.findViewById(R.id.fileView);
            fileDate = itemView.findViewById(R.id.fileDate);
            fileSize = itemView.findViewById(R.id.fileSize);
            fileDeleteButton = itemView.findViewById(R.id.fileDeleteButton);

        }
    }

    @Override
    public VideosRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View itemView = inflater.inflate(R.layout.row_fragment_displayvideos, parent, false);

        // Return a new holder instance
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VideosRecyclerViewAdapter.ViewHolder viewHolder, final int position) {

        if (videoList != null) {

            final File item = videoList.get(position);

            Bitmap thumbImage = ThumbnailUtils.createVideoThumbnail(item.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);

            if(thumbImage == null) {
                viewHolder.fileView.setImageResource(R.drawable.ic_videocam_black_24dp);
            } else {
                viewHolder.fileView.setImageBitmap(thumbImage);
            }

            viewHolder.fileView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri intentURI = FileProvider.getUriForFile(context, "com.myapp.Securebuddy", item);

                    Intent intent = new Intent(Intent.ACTION_VIEW, intentURI);
                    intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(intentURI, "video/*");
                    context.startActivity(intent);
                }
            });

            String fileSize = android.text.format.Formatter.formatFileSize(context, item.length());
            viewHolder.fileDate.setText(item.getName());
            viewHolder.fileSize.setText(fileSize);


            viewHolder.fileDeleteButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setCancelable(false);
                    alertDialog.setTitle(context.getString(R.string.deleteConfirmationTitle));
                    alertDialog.setMessage(context.getString(R.string.deleteConfirmationMessage));
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            videoList.remove(position);
                            item.delete();
                            notifyDataSetChanged();
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
        }

    }

    @Override
    public int getItemCount() {
        if (videoList != null)
            return videoList.size();
        else return 0;
    }
}
