package quest.outdoor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FindTheDotActivity extends AppCompatActivity {

    TextView dotDescriptionTextView;
    Button hintButton;
    ImageView compassImageView;
    Button verifyButton;

    EditText codeVerificationInput;

    private AppState appState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_the_dot);

        hintButton = findViewById(R.id.hint_button);
        compassImageView = findViewById(R.id.compassImageView);
        verifyButton = findViewById(R.id.verify_button);

        appState = AppState.getInstance();


        updateUI();


        //set hint area visibility conditions:
        //visible when closer than 5 meters to the dot for 2 minutes or longer
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHint();
            }
        });


        //active when closer than 3 meters to the dot
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyCode();
            }
        });

    }

    private void verifyCode() {
        //create dialog to verify code - with one button (ok)
        //if correct - show "yes" and lead to new dot activity
        //if wrong - show "no guessing", "do not cheat" or else and lead to current dot activity

        AlertDialog.Builder builder = new AlertDialog.Builder(FindTheDotActivity.this);
        builder.setTitle("code verification");
        builder.setIcon(R.mipmap.ic_lightbulb);
        builder.setMessage("enter your code");

        codeVerificationInput = new EditText(FindTheDotActivity.this);
        builder.setView(codeVerificationInput);

        builder.setPositiveButton("check", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //capture the input
                String userInputCode = codeVerificationInput.getText().toString().trim();
                String dotCode = appState.getCurrentDotCode();
                if(userInputCode.equalsIgnoreCase(dotCode))
                {
                    Toast.makeText(FindTheDotActivity.this, "correct", Toast.LENGTH_SHORT).show();
                    //todo open next dot finder
                    appState.goToNextDot(FindTheDotActivity.this);
                    dialog.cancel();
                    updateUI();
                }
                else
                {
                    //show cheat message and close dialog
                    Toast.makeText(FindTheDotActivity.this, "wrong", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.setTitle("Code verification");
        alert.show();


    }

    private void updateUI() {

        dotDescriptionTextView = findViewById(R.id.dot_description_textView);
        dotDescriptionTextView.setText(appState.getCurrentDotDescription());
    }

    private void showHint() {
        //create dialog with one button (ok)
        //or make button to "uncover" the hint - scratch

        String hintMessage = appState.getCurrentDotHint();

        AlertDialog.Builder a_builder = new AlertDialog.Builder(FindTheDotActivity.this);
        a_builder.setMessage(hintMessage)
                .setIcon(R.mipmap.ic_lightbulb)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Hint");
        alert.show();
    }


}
