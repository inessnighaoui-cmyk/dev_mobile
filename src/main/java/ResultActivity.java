import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle bundle = getIntent().getBundleExtra(MainActivity.EXTRA_BUNDLE);
        if (bundle == null) bundle = new Bundle();
        String nom = bundle.getString(MainActivity.KEY_NOM, "");
        double moyenne = bundle.getDouble(MainActivity.KEY_MOYENNE, 0);
        Bundle donnees = bundle;

        String texte = String.format("%.2f", moyenne);
        ((TextView) findViewById(R.id.tvMoyenne)).setText("Moyenne : " + texte + " / 20");
        ((ProgressBar) findViewById(R.id.progressBar)).setProgress((int) Math.round(moyenne));

        String mention;
        if (moyenne >= 16) mention = "Très bien";
        else if (moyenne >= 14) mention = "Bien";
        else if (moyenne >= 12) mention = "Assez bien";
        else if (moyenne >= 10) mention = "Passable";
        else mention = "Insuffisant";
        ((TextView) findViewById(R.id.tvMention)).setText("Mention : " + mention);

        findViewById(R.id.btnDetails).setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Détails")
                        .setMessage(nom + "\nMoyenne : " + texte + "\nMention : " + mention)
                        .setPositiveButton("OK", null)
                        .show());

        findViewById(R.id.btnSuivant).setOnClickListener(v -> {
            Intent intent = new Intent(this, ObjectifActivity.class);
            intent.putExtra(MainActivity.EXTRA_BUNDLE, donnees);
            startActivity(intent);
        });
    }
}