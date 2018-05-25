package thibaut.photogran;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GetPhotosService extends IntentService {
    private static final String ACTION_GET_PHOTOS = "thibaut.photogran.action.GET_PHOTOS";
    static int nb_pictures;
    static String search;

    public GetPhotosService() {
        super("GetPhotosService");
    }

    public static void startActionGetPhotos(Context context, int nb_pics, String keyword) {
        nb_pictures = nb_pics;
        search = keyword;
        Intent intent = new Intent(context, GetPhotosService.class);
        intent.setAction(ACTION_GET_PHOTOS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_PHOTOS.equals(action)) {
                handleActionGetPhotos();
            }
        }
    }

    private void handleActionGetPhotos() {
        Log.d("GetPhotosService", "Thread service name:" + Thread.currentThread().getName());
        URL url;
        try {
            if(search != null)
                url = new URL("https://api.unsplash.com/search/photos?per_page=" + nb_pictures + "&query=" + search + "&client_id=f81da655d58e3b1a931d10d89fa6c7a40f3b8d0efa4b5b2a4e0578c17729bdaa");
            else
                url = new URL("https://api.unsplash.com/photos/random/?count=" + nb_pictures + "&client_id=f81da655d58e3b1a931d10d89fa6c7a40f3b8d0efa4b5b2a4e0578c17729bdaa");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if(HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                copyInputStreamToFile(conn.getInputStream(),
                        new File(getCacheDir(), "photos.json"));
                Log.d("Service", "photos.json downloading !");
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(SecondeActivity.PHOTOS_UPDATE));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyInputStreamToFile(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0) {
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
