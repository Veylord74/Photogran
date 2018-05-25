package thibaut.photogran;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    AlertDialog alert;
    CharSequence[] values = {" 5 ", " 10 ", " 15 ", " 20 "};
    int nb_pictures = 10;
    String search;
    public final static String NB_PIC = "thibaut.photogran.intent.NB_PIC";
    public final static String SEARCH = "thibaut.photogran.intent.SEARCH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_gallery = findViewById(R.id.btn_gallery);
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                createIntent();
            }
        });
    }

    public void createIntent() {
        final Intent i = new Intent(this, SecondeActivity.class);
        i.putExtra(NB_PIC, nb_pictures);
        i.putExtra(SEARCH, search);
        startActivity(i);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                createImageCountDialog();
                return true;
            case R.id.action_search:
                createSearchDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createImageCountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.dialog_title));
        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which)
                {
                    case 0:
                        nb_pictures = 5;
                        GetPhotosService.startActionGetPhotos(MainActivity.this, nb_pictures, search);
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_nb_pictures), Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        nb_pictures = 10;
                        GetPhotosService.startActionGetPhotos(MainActivity.this, nb_pictures, search);
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_nb_pictures), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        nb_pictures = 15;
                        GetPhotosService.startActionGetPhotos(MainActivity.this, nb_pictures, search);
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_nb_pictures), Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        nb_pictures = 20;
                        GetPhotosService.startActionGetPhotos(MainActivity.this, nb_pictures, search);
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_nb_pictures), Toast.LENGTH_SHORT).show();
                        break;
                }
                alert.dismiss();
            }
        });
        alert = builder.create();
        alert.show();
    }

    public void createSearchDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.prompt, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.search_title));
        builder.setView(promptView);

        final EditText input = promptView.findViewById(R.id.userInput);

        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                search = input.getText().toString();
                GetPhotosService.startActionGetPhotos(MainActivity.this, nb_pictures, search);
                createIntent();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
