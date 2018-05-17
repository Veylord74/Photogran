package thibaut.photogran;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SecondeActivity extends AppCompatActivity {

    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seconde);

        Button btn_account = findViewById(R.id.btn_download);
        btn_account.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                GetPhotosService.startActionGetPhotos(SecondeActivity.this);
            }
        });
        IntentFilter intentFilter = new IntentFilter(PHOTOS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new PhotoUpdate(),intentFilter);

        rv = findViewById(R.id.rv_photos);
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }

    public static final String PHOTOS_UPDATE = "thibaut.photogran.PHOTOS_UPDATE";

    public class PhotoUpdate extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            /*rv = (RecyclerView) findViewById(R.id.rv_poke);
            BiersAdapter ba = (BiersAdapter) rv.getAdapter();
            ba.setNewBier(getPhotosFromFile());*/
            Log.d("SecondActivity", "Successfuly dowloaded !");
        }
    }

    public JSONArray getPhotosFromFile(){
        try{
            InputStream is = new FileInputStream(getCacheDir() + "/" + "photos.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONObject(new String(buffer, "UTF-8")).getJSONArray("objects");
        } catch(IOException e){
            e.printStackTrace();
            return new JSONArray();
        } catch(JSONException e){
            e.printStackTrace();
            return new JSONArray();
        }
    }

    class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoHolder> {

        private JSONArray photos;

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        public class PhotoHolder extends RecyclerView.ViewHolder {

            public ImageView photo;

            public PhotoHolder(View view) {

                super(view);
                photo = view.findViewById(R.id.rv_photo_element);

            }

            public void display(String url) {
                //photo.setText(url);
            }
        }

    }
}
