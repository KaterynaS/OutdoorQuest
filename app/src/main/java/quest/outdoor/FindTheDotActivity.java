package quest.outdoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FindTheDotActivity extends AppCompatActivity {

    //view
    TextView dotDescriptionTextView;
    Button hintButton;
    Button startTrackingButton;
    Button stopTrackingButton;
    TextView targetCoordTextview;
    TextView distanceToTarget;
    TextView currentCoordTextview;
    Button verifyButton;

    //verification dialog
    EditText codeVerificationInput;

    //app state
    private AppState appState;
    private BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_the_dot);

        hintButton = findViewById(R.id.hint_button);
        verifyButton = findViewById(R.id.verify_button);
        targetCoordTextview = findViewById(R.id.target_coord_textview);
        currentCoordTextview = findViewById(R.id.current_coord_textview);
        startTrackingButton = findViewById(R.id.start_tracking_button);
        stopTrackingButton = findViewById(R.id.stop_tracking_button);

        appState = AppState.getInstance();

        updateDotDataOnScreen();

        //if we don't need permission checking => skip this part and enable buttons
        //if we do need permission checking - we'll handle users answer in onRequestPermissionResult
        if(!runtime_permissions())
        {
            enable_buttons();
        }



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


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(broadcastReceiver == null)
        {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String a = "" + intent.getExtras().get("coordinates");
                    currentCoordTextview.setText(a);
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null)
        {
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //checking if the request code equal to our previously typed code - 100
        if(requestCode == 100)
        {
            //and check if both permissions are granted
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED
            && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                //if permissions granted - enable buttons
                enable_buttons();
            }
            //if permissions not granted - ask for them again
            else
            {
                runtime_permissions();
            }
        }
    }

    private void enable_buttons() {
        startTrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent that points to our GPS service
                Intent i = new Intent(getApplicationContext(), GPS_Service.class);
                startService(i);
            }
        });

        stopTrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), GPS_Service.class);
                stopService(i);
            }
        });
    }


    private boolean runtime_permissions() {
        //check if version is above 23
        if(Build.VERSION.SDK_INT >= 23
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            //ask for the permissions, 100 - unique request code for permission checking
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION},
                                        100);

            //if we ask for permissions
            return true;
        }
        //if we DON'T ask for permissions
        return false;
    }


    private void vibrateInLocation(String locationName)
    {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(800, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(800);
        }

        Toast.makeText(FindTheDotActivity.this, "you reached " + locationName, Toast.LENGTH_SHORT).show();
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
                    updateDotDataOnScreen();
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

    private void updateDotDataOnScreen() {

        dotDescriptionTextView = findViewById(R.id.dot_description_textView);
        dotDescriptionTextView.setText(appState.getCurrentDotDescription());
        targetCoordTextview = findViewById(R.id.target_coord_textview);
        String targetCoord = "lat: " + appState.getCurrentDotLocation().getLatitude()
                + "\n" + "lon: " + appState.getCurrentDotLocation().getLongitude();
        targetCoordTextview.setText(targetCoord);
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
