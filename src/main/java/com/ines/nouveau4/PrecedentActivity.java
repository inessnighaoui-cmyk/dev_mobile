package com.ines.nouveau4; // à adapter à ton projet

import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class PrecedentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precedent);

        // Le Bundle reçu sert à afficher la moyenne actuelle et à pré-remplir
        // les curseurs si l'utilisateur revient modifier sa saisie
        Bundle bundle = getIntent().getBundleExtra(MainActivity.EXTRA_BUNDLE);
        if (bundle == null) bundle = new Bundle();
        double moyenne = bundle.getDouble(MainActivity.KEY_MOYENNE, 0);

        ((TextView) findViewById(R.id.tvReference))
                .setText(getString(R.string.reference_fmt, String.format("%.2f", moyenne)));

        SeekBar seekMath = findViewById(R.id.seekMath);
        SeekBar seekInfo = findViewById(R.id.seekInfo);
        SeekBar seekAnglais = findViewById(R.id.seekAnglais);
        SwitchCompat switchComparer = findViewById(R.id.switchComparer);

        // Valeurs précédentes si elles existent, sinon 10 par défaut
        seekMath.setProgress(bundle.getInt(MainActivity.KEY_ANCIEN_MATH, 10));
        seekInfo.setProgress(bundle.getInt(MainActivity.KEY_ANCIEN_INFO, 10));
        seekAnglais.setProgress(bundle.getInt(MainActivity.KEY_ANCIEN_ANGLAIS, 10));
        switchComparer.setChecked(bundle.getBoolean(MainActivity.KEY_COMPARER, false));

        brancher(seekMath, (TextView) findViewById(R.id.tvMath), R.string.matiere_math);
        brancher(seekInfo, (TextView) findViewById(R.id.tvInfo), R.string.matiere_info);
        brancher(seekAnglais, (TextView) findViewById(R.id.tvAnglais), R.string.matiere_anglais);

        // RETOUR : on ne lance rien, on renvoie le résultat à l'appelant
        findViewById(R.id.btnValider).setOnClickListener(v -> {
            Bundle retour = new Bundle();
            retour.putInt(MainActivity.KEY_ANCIEN_MATH, seekMath.getProgress());
            retour.putInt(MainActivity.KEY_ANCIEN_INFO, seekInfo.getProgress());
            retour.putInt(MainActivity.KEY_ANCIEN_ANGLAIS, seekAnglais.getProgress());
            retour.putBoolean(MainActivity.KEY_COMPARER, switchComparer.isChecked());

            Intent data = new Intent();
            data.putExtras(retour);
            setResult(RESULT_OK, data);
            finish();
        });
    }

    /** Affiche la valeur du curseur dans le TextView, et la met à jour en direct. */
    private void brancher(SeekBar seek, TextView tv, int matiereRes) {
        String matiere = getString(matiereRes);
        tv.setText(getString(R.string.note_ancienne_fmt, matiere, seek.getProgress()));

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar s, int progress, boolean fromUser) {
                tv.setText(getString(R.string.note_ancienne_fmt, matiere, progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar s) { }
            @Override
            public void onStopTrackingTouch(SeekBar s) { }
        });
    }
}