package com.ines.nouveau4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NotesActivity extends AppCompatActivity {

    private EditText editMath, editInfo, editAnglais;
    private Bundle donnees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Bundle bundle = getIntent().getBundleExtra(MainActivity.EXTRA_BUNDLE);
        donnees = (bundle != null) ? bundle : new Bundle();
        String nom = donnees.getString(MainActivity.KEY_NOM, "");

        ((TextView) findViewById(R.id.tvBonjour)).setText(getString(R.string.bonjour_fmt, nom));

        editMath = findViewById(R.id.editMath);
        editInfo = findViewById(R.id.editInfo);
        editAnglais = findViewById(R.id.editAnglais);

        findViewById(R.id.btnCalculer).setOnClickListener(v -> {
            // Chaque lecture affiche son propre Toast et renvoie null en cas d'erreur
            Double n1 = lireNote(editMath, R.string.matiere_math);
            if (n1 == null) return;
            Double n2 = lireNote(editInfo, R.string.matiere_info);
            if (n2 == null) return;
            Double n3 = lireNote(editAnglais, R.string.matiere_anglais);
            if (n3 == null) return;

            double moyenne = (n1 + n2 + n3) / 3;
            donnees.putDouble(MainActivity.KEY_MOYENNE, moyenne);

            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra(MainActivity.EXTRA_BUNDLE, donnees);
            startActivity(intent);
        });
    }

    /** Lit une note valide entre 0 et 20, ou renvoie null après avoir affiché un Toast. */
    private Double lireNote(EditText champ, int matiereRes) {
        String matiere = getString(matiereRes);
        // Accepte la virgule des claviers français : "14,5" -> "14.5"
        String texte = champ.getText().toString().trim().replace(',', '.');

        if (texte.isEmpty()) {
            toast(getString(R.string.toast_note_vide, matiere));
            return null;
        }

        double note;
        try {
            note = Double.parseDouble(texte);
        } catch (NumberFormatException e) {
            toast(getString(R.string.toast_note_invalide, matiere));
            return null;
        }

        if (note < 0 || note > 20) {
            toast(getString(R.string.toast_note_hors, matiere));
            return null;
        }
        return note;
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}