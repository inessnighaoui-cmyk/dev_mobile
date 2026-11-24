package com.ines.nouveau4; // à adapter à ton projet

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class EvolutionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evolution);

        Bundle bundle = getIntent().getBundleExtra(MainActivity.EXTRA_BUNDLE);
        if (bundle == null) bundle = new Bundle();

        // Notes actuelles (ajoutées par NotesActivity)
        double[] nouv = {
                bundle.getDouble(MainActivity.KEY_NOTE_MATH, 0),
                bundle.getDouble(MainActivity.KEY_NOTE_INFO, 0),
                bundle.getDouble(MainActivity.KEY_NOTE_ANGLAIS, 0)
        };
        // Notes précédentes (renvoyées par PrecedentActivity)
        int[] anc = {
                bundle.getInt(MainActivity.KEY_ANCIEN_MATH, 0),
                bundle.getInt(MainActivity.KEY_ANCIEN_INFO, 0),
                bundle.getInt(MainActivity.KEY_ANCIEN_ANGLAIS, 0)
        };
        boolean comparer = bundle.getBoolean(MainActivity.KEY_COMPARER, false);

        double nouvelle = bundle.getDouble(MainActivity.KEY_MOYENNE, 0);
        double ancienne = (anc[0] + anc[1] + anc[2]) / 3.0;
        double ecart = nouvelle - ancienne;

        // Moyennes en texte et en barres
        ((TextView) findViewById(R.id.tvAncienne))
                .setText(getString(R.string.ancienne_moyenne_fmt, f2(ancienne)));
        ((ProgressBar) findViewById(R.id.progressAncienne)).setProgress((int) Math.round(ancienne));
        ((TextView) findViewById(R.id.tvNouvelle))
                .setText(getString(R.string.nouvelle_moyenne_fmt, f2(nouvelle)));
        ((ProgressBar) findViewById(R.id.progressNouvelle)).setProgress((int) Math.round(nouvelle));

        // Verdict : on compare en centièmes pour éviter les erreurs d'arrondi
        long centiemes = Math.round(ecart * 100);
        String verdict;
        if (centiemes > 0) verdict = getString(R.string.verdict_progression);
        else if (centiemes < 0) verdict = getString(R.string.verdict_recul);
        else verdict = getString(R.string.verdict_stable);
        ((TextView) findViewById(R.id.tvEcart))
                .setText(getString(R.string.ecart_fmt, signe(ecart), verdict));

        // Détail par matière : seulement si le Switch « comparer » était actif
        if (comparer) {
            String[] noms = {
                    getString(R.string.matiere_math),
                    getString(R.string.matiere_info),
                    getString(R.string.matiere_anglais)
            };

            StringBuilder sb = new StringBuilder();
            int meilleur = 0;
            for (int i = 0; i < 3; i++) {
                double d = nouv[i] - anc[i];
                sb.append(getString(R.string.ligne_matiere_fmt,
                        noms[i], anc[i], f2(nouv[i]), signe(d), fleche(d))).append("\n");
                if (d > nouv[meilleur] - anc[meilleur]) meilleur = i;
            }

            double dMeilleur = nouv[meilleur] - anc[meilleur];
            if (Math.round(dMeilleur * 100) > 0) {
                sb.append("\n").append(getString(R.string.meilleure_fmt, noms[meilleur], signe(dMeilleur)));
            } else {
                sb.append("\n").append(getString(R.string.aucune_progression));
            }

            TextView tvDetail = findViewById(R.id.tvDetail);
            tvDetail.setText(sb.toString());
            tvDetail.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.btnQuitter).setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle(R.string.dialog_quitter)
                        .setMessage(R.string.dialog_quitter_msg)
                        .setPositiveButton(R.string.oui, (d, w) -> finishAffinity())
                        .setNegativeButton(R.string.non, null)
                        .show());
    }

    private String f2(double v) {
        return String.format("%.2f", v);
    }

    private String signe(double v) {
        return String.format("%+.2f", v);
    }

    private String fleche(double d) {
        long c = Math.round(d * 100);
        if (c > 0) return "↑";
        if (c < 0) return "↓";
        return "=";
    }
}