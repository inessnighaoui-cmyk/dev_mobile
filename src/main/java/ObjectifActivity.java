package com.ines.nouveau4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class ObjectifActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objectif);

        // Ici le Bundle reçu sert à afficher la moyenne de référence
        Bundle bundle = getIntent().getBundleExtra(MainActivity.EXTRA_BUNDLE);
        double moyenne = (bundle != null) ? bundle.getDouble(MainActivity.KEY_MOYENNE, 0) : 0;

        TextView tvReference = findViewById(R.id.tvReference);
        TextView tvObjectif = findViewById(R.id.tvObjectif);
        SeekBar seek = findViewById(R.id.seekObjectif);
        RadioButton radioDebutant = findViewById(R.id.radioDebutant);
        SwitchCompat switchRappel = findViewById(R.id.switchRappel);

        tvReference.setText(getString(R.string.reference_fmt, String.format("%.2f", moyenne)));

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar s, int progress, boolean fromUser) {
                tvObjectif.setText(getString(R.string.objectif_fmt, progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar s) { }
            @Override
            public void onStopTrackingTouch(SeekBar s) { }
        });

        // Valeur de départ : moyenne arrondie + 1 (plafonnée à 20)
        int depart = Math.min(20, (int) Math.round(moyenne) + 1);
        seek.setProgress(depart);
        tvObjectif.setText(getString(R.string.objectif_fmt, depart));

        switchRappel.setOnCheckedChangeListener((bouton, active) -> {
            if (active) {
                Toast.makeText(this, R.string.toast_rappel_on, Toast.LENGTH_SHORT).show();
            }
        });

        // RETOUR : on ne lance plus ResumeActivity, on renvoie le résultat à l'appelant
        findViewById(R.id.btnSuivant).setOnClickListener(v -> {
            Bundle retour = new Bundle();
            retour.putInt(MainActivity.KEY_OBJECTIF, seek.getProgress());
            retour.putString(MainActivity.KEY_NIVEAU,
                    getString(radioDebutant.isChecked() ? R.string.radio_debutant : R.string.radio_avance));
            retour.putBoolean(MainActivity.KEY_RAPPEL, switchRappel.isChecked());

            Intent data = new Intent();
            data.putExtras(retour);
            setResult(RESULT_OK, data);
            finish();
        });
    }
}