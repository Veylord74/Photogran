package thibaut.photogran;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SecondeActivity extends AppCompatActivity {

    RecyclerView rv;
    int nb_pictures;
    AlertDialog alert;
    CharSequence[] values = {" 5 ", " 10 ", " 15 ", " 20 "};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seconde);

        createNotificationChannel();

        Intent i = getIntent();
        nb_pictures = i.getIntExtra(MainActivity.NB_PIC, 10);

        Button btn_download = findViewById(R.id.btn_download);
        btn_download.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                GetPhotosService.startActionGetPhotos(SecondeActivity.this, nb_pictures);
                Toast.makeText(getApplicationContext(), getString(R.string.toast), Toast.LENGTH_SHORT).show();
            }
        });
        IntentFilter intentFilter = new IntentFilter(PHOTOS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new PhotoUpdate(),intentFilter);

        rv = findViewById(R.id.rv_photos);
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rv.setAdapter(new PhotosAdapter(getPhotosFromFile()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                createAlertDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SecondeActivity.this);
        builder.setTitle(getString(R.string.dialog_title));
        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which)
                {
                    case 0:
                        nb_pictures = 5;
                        GetPhotosService.startActionGetPhotos(SecondeActivity.this, nb_pictures);
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_nb_pictures), Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        nb_pictures = 10;
                        GetPhotosService.startActionGetPhotos(SecondeActivity.this, nb_pictures);
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_nb_pictures), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        nb_pictures = 15;
                        GetPhotosService.startActionGetPhotos(SecondeActivity.this, nb_pictures);
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_nb_pictures), Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        nb_pictures = 20;
                        GetPhotosService.startActionGetPhotos(SecondeActivity.this, nb_pictures);
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_nb_pictures), Toast.LENGTH_SHORT).show();
                        break;
                }
                alert.dismiss();
            }
        });
        alert = builder.create();
        alert.show();
    }

    public static final String PHOTOS_UPDATE = "thibaut.photogran.PHOTOS_UPDATE";

    public class PhotoUpdate extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            PhotosAdapter ba = (PhotosAdapter) rv.getAdapter();
            ba.setNewPhoto(getPhotosFromFile());
            createNotification();
            Log.d("SecondActivity", "Successfuly dowloaded !");
        }
    }

    public JSONArray getPhotosFromFile(){
        try{
            InputStream is = new FileInputStream(getCacheDir() + "/" + "photos.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONArray(new String(buffer, "UTF-8"));
        } catch(IOException e){
            e.printStackTrace();
            return new JSONArray();
        } catch(JSONException e){
            e.printStackTrace();
            return new JSONArray();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            String CHANNEL_ID = "thibaut.photogran.ONE";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createNotification() {
        String CHANNEL_ID = "thibaut.photogran.ONE";

        final NotificationManager mNotification = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final Intent launchNotificationIntent = new Intent(this, SecondeActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, launchNotificationIntent,
                PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_desc))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);
        mNotification.notify(01, mBuilder.build());
    }

    class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoHolder> {

        private JSONArray photos;

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.rv_photo_element, parent, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            try {
                String url = photos.getJSONObject(position).getJSONObject("urls").getString("regular");
                holder.display(url);
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return photos.length();
        }

        public class PhotoHolder extends RecyclerView.ViewHolder {

            public ImageView iv;

            public PhotoHolder(View view) {

                super(view);
                iv = view.findViewById(R.id.iv_photo);

            }

            public void display(String url) {
                Picasso.with(getBaseContext()).load(url).into(iv);
            }
        }

        public PhotosAdapter(JSONArray photos) {
            this.photos = photos;
        }

        public void setNewPhoto(JSONArray photos) {
            this.photos = photos;
            notifyDataSetChanged();
        }
    }
}
