package thibaut.photogran;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SecondeActivity extends AppCompatActivity {

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
    }
}
