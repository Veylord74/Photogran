package thibaut.photogran;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

public class GetPhotosService extends IntentService {
    private static final String ACTION_GET_PHOTOS = "thibaut.photogran.action.FOO";

    public GetPhotosService() {
        super("GetPhotosService");
    }

    public static void startActionGetPhotos(Context context) {
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
    }
}
