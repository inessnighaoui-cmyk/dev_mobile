import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Bundle bundle = getIntent().getBundleExtra(MainActivity.EXTRA_BUNDLE);
        if (bundle == null) bundle = new Bundle();
        String nom = bundle.getString(MainActivity.KEY_NOM, "");

        ((TextView) findViewById(R.id.tvBonjour)).setText("Bonjour " + nom);

        EditText editMath = findViewById(R.id.editMath);
        EditText editInfo = findViewById(R.id.editInfo);
        EditText editAnglais = findViewById(R.id.editAnglais);
        Bundle donnees = bundle;

        findViewById(R.id.btnCalculer).setOnClickListener(v -> {
            String m = editMath.getText().toString().trim();
            String i = editInfo.getText().toString().trim();
            String a = editAnglais.getText().toString().trim();

            if (m.isEmpty() || i.isEmpty() || a.isEmpty()) {
                Toast.makeText(this, "Remplissez les 3 notes", Toast.LENGTH_SHORT).show();
                return;
            }

            double n1, n2, n3;
            try {
                n1 = Double.parseDouble(m);
                n2 = Double.parseDouble(i);
                n3 = Double.parseDouble(a);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Note invalide", Toast.LENGTH_SHORT).show();
                return;
            }

            if (n1 > 20 || n2 > 20 || n3 > 20) {
                Toast.makeText(this, "Une note doit être entre 0 et 20", Toast.LENGTH_SHORT).show();
                return;
            }

            double moyenne = (n1 + n2 + n3) / 3;
            donnees.putDouble(MainActivity.KEY_MOYENNE, moyenne);

            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra(MainActivity.EXTRA_BUNDLE, donnees);
            startActivity(intent);
        });
    }
}