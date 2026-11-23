package com.ines.nouveau4;

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
        if (bundle == null) bundle = new Bundle(); // même stratégie que les autres écrans

        String nom = bundle.getString(MainActivity.KEY_NOM, "");
        double moyenne = bundle.getDouble(MainActivity.KEY_MOYENNE, 0);
        int objectif = bundle.getInt(MainActivity.KEY_OBJECTIF, 10);
        String niveau = bundle.getString(MainActivity.KEY_NIVEAU, getString(R.string.radio_debutant));
        boolean rappel = bundle.getBoolean(MainActivity.KEY_RAPPEL, false);

        String rappelTexte = getString(rappel ? R.string.rappels_on : R.string.rappels_off);

        StringBuilder sb = new StringBuilder(getString(R.string.recap_fmt,
                nom, String.format("%.2f", moyenne), objectif, niveau, rappelTexte));
        sb.append("\n\n");

        double ecart = objectif - moyenne;
        if (ecart <= 0) {
            sb.append(getString(R.string.etat_atteint));
        } else {
            sb.append(getString(R.string.etat_manque, String.format("%.2f", ecart)));

            // Le niveau agit ici : il fixe le rythme de progression réaliste
            int pas = niveau.equals(getString(R.string.radio_debutant)) ? 1 : 2;
            int periodes = (int) Math.ceil(ecart / pas);
            sb.append("\n").append(getString(R.string.conseil_fmt, niveau, pas, periodes));

            if (ecart > 4) {
                sb.append("\n").append(getString(R.string.ambitieux));
            }
        }

        ((TextView) findViewById(R.id.tvRecap)).setText(sb.toString());

        findViewById(R.id.btnQuitter).setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle(R.string.dialog_quitter)
                        .setMessage(R.string.dialog_quitter_msg)
                        .setPositiveButton(R.string.oui, (d, w) -> finishAffinity())
                        .setNegativeButton(R.string.non, null)
                        .show());
    }
}