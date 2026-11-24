package com.ines.nouveau4; // à adapter à ton projet

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private static final int REQUEST_PRECEDENT = 101;
    private static final String STATE_DONNEES = "donnees";

    // Champ (et non variable locale) : onActivityResult en a besoin
    private Bundle donnees;
    private TextView tvPrecedent;
    private Button btnEvolution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Après une rotation d'écran, on récupère le Bundle déjà enrichi
        if (savedInstanceState != null && savedInstanceState.getBundle(STATE_DONNEES) != null) {
            donnees = savedInstanceState.getBundle(STATE_DONNEES);
        } else {
            Bundle bundle = getIntent().getBundleExtra(MainActivity.EXTRA_BUNDLE);
            donnees = (bundle != null) ? bundle : new Bundle();
        }

        String nom = donnees.getString(MainActivity.KEY_NOM, "");
        double moyenne = donnees.getDouble(MainActivity.KEY_MOYENNE, 0);

        String texte = String.format("%.2f", moyenne);
        ((TextView) findViewById(R.id.tvMoyenne)).setText(getString(R.string.moyenne_fmt, texte));
        ((ProgressBar) findViewById(R.id.progressBar)).setProgress((int) Math.round(moyenne));

        String mention;
        if (moyenne >= 16) mention = getString(R.string.mention_tres_bien);
        else if (moyenne >= 14) mention = getString(R.string.mention_bien);
        else if (moyenne >= 12) mention = getString(R.string.mention_assez_bien);
        else if (moyenne >= 10) mention = getString(R.string.mention_passable);
        else mention = getString(R.string.mention_insuffisant);
        ((TextView) findViewById(R.id.tvMention)).setText(getString(R.string.mention_fmt, mention));

        tvPrecedent = findViewById(R.id.tvPrecedent);
        btnEvolution = findViewById(R.id.btnEvolution);

        findViewById(R.id.btnDetails).setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle(R.string.dialog_details)
                        .setMessage(getString(R.string.details_fmt, nom, texte, mention))
                        .setPositiveButton(R.string.ok, null)
                        .show());

        // ALLER : on envoie le Bundle et on ATTEND une réponse (requestCode 101)
        findViewById(R.id.btnSuivant).setOnClickListener(v -> {
            Intent intent = new Intent(this, PrecedentActivity.class);
            intent.putExtra(MainActivity.EXTRA_BUNDLE, donnees);
            startActivityForResult(intent, REQUEST_PRECEDENT);
        });

        // L'évolution n'est accessible qu'une fois les notes précédentes reçues
        btnEvolution.setOnClickListener(v -> {
            Intent intent = new Intent(this, EvolutionActivity.class);
            intent.putExtra(MainActivity.EXTRA_BUNDLE, donnees);
            startActivity(intent);
        });

        afficherPrecedent();
    }

    // RETOUR : appelé quand PrecedentActivity fait setResult() + finish()
    @SuppressWarnings("deprecation")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // RESULT_CANCELED (touche Retour) -> on ne change rien
        if (requestCode == REQUEST_PRECEDENT && resultCode == RESULT_OK && data != null) {
            Bundle retour = data.getExtras();
            if (retour != null) {
                donnees.putAll(retour); // fusion : anciennes notes + choix du Switch
                afficherPrecedent();
            }
        }
    }

    private void afficherPrecedent() {
        if (donnees.containsKey(MainActivity.KEY_ANCIEN_MATH)) {
            int a1 = donnees.getInt(MainActivity.KEY_ANCIEN_MATH);
            int a2 = donnees.getInt(MainActivity.KEY_ANCIEN_INFO);
            int a3 = donnees.getInt(MainActivity.KEY_ANCIEN_ANGLAIS);
            double ancienne = (a1 + a2 + a3) / 3.0;

            tvPrecedent.setText(getString(R.string.precedent_choisi_fmt,
                    String.format("%.2f", ancienne)));
            tvPrecedent.setVisibility(View.VISIBLE);
            btnEvolution.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(STATE_DONNEES, donnees);
    }
}