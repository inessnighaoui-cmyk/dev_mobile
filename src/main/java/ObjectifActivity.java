import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class ObjectifActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objectif);

        Bundle bundle = getIntent().getBundleExtra(MainActivity.EXTRA_BUNDLE);
        if (bundle == null) bundle = new Bundle();
        Bundle donnees = bundle;

        TextView tvObjectif = findViewById(R.id.tvObjectif);
        SeekBar seek = findViewById(R.id.seekObjectif);
        RadioButton radioDebutant = findViewById(R.id.radioDebutant);
        SwitchCompat switchRappel = findViewById(R.id.switchRappel);

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar s, int progress, boolean fromUser) {
                tvObjectif.setText("Objectif : " + progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar s) { }
            @Override
            public void onStopTrackingTouch(SeekBar s) { }
        });

        findViewById(R.id.btnSuivant).setOnClickListener(v -> {
            donnees.putInt(MainActivity.KEY_OBJECTIF, seek.getProgress());
            donnees.putString(MainActivity.KEY_NIVEAU, radioDebutant.isChecked() ? "Débutant" : "Avancé");
            donnees.putBoolean(MainActivity.KEY_RAPPEL, switchRappel.isChecked());

            Intent intent = new Intent(this, ResumeActivity.class);
            intent.putExtra(MainActivity.EXTRA_BUNDLE, donnees);
            startActivity(intent);
        });
    }
}