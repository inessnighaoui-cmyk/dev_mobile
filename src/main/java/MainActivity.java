import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_BUNDLE = "data_bundle";
    public static final String KEY_NOM = "nom";
    public static final String KEY_MOYENNE = "moyenne";
    public static final String KEY_OBJECTIF = "objectif";
    public static final String KEY_NIVEAU = "niveau";
    public static final String KEY_RAPPEL = "rappel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editNom = findViewById(R.id.editNom);
        CheckBox checkAccept = findViewById(R.id.checkAccept);
        Button btnCommencer = findViewById(R.id.btnCommencer);

        btnCommencer.setOnClickListener(v -> {
            String nom = editNom.getText().toString().trim();

            if (nom.isEmpty()) {
                Toast.makeText(this, "Entrez votre nom", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!checkAccept.isChecked()) {
                Toast.makeText(this, "Acceptez les conditions", Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle b = new Bundle();
            b.putString(KEY_NOM, nom);

            Intent intent = new Intent(this, NotesActivity.class);
            intent.putExtra(EXTRA_BUNDLE, b);
            startActivity(intent);
        });
    }
}