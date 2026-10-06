import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ResumeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);

        Bundle bundle = getIntent().getBundleExtra(MainActivity.EXTRA_BUNDLE);
        if (bundle != null) {
            String nom = bundle.getString(MainActivity.KEY_NOM, "");
            double moyenne = bundle.getDouble(MainActivity.KEY_MOYENNE, 0);
            int objectif = bundle.getInt(MainActivity.KEY_OBJECTIF, 10);
            String niveau = bundle.getString(MainActivity.KEY_NIVEAU, "");
            boolean rappel = bundle.getBoolean(MainActivity.KEY_RAPPEL, false);

            String etat = moyenne >= objectif
                    ? "Objectif atteint"
                    : String.format("Il manque %.2f points", objectif - moyenne);

            ((TextView) findViewById(R.id.tvRecap)).setText(
                    "Étudiant : " + nom
                    + "\nMoyenne : " + String.format("%.2f", moyenne)
                    + "\nObjectif : " + objectif
                    + "\nNiveau : " + niveau
                    + "\nRappels : " + (rappel ? "activés" : "désactivés")
                    + "\n\n" + etat);
        }

        findViewById(R.id.btnQuitter).setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Quitter")
                        .setMessage("Fermer l'application ?")
                        .setPositiveButton("Oui", (d, w) -> finishAffinity())
                        .setNegativeButton("Non", null)
                        .show());
    }
}