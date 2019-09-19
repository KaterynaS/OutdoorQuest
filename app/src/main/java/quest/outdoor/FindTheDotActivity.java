package quest.outdoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Locale;

public class FindTheDotActivity extends AppCompatActivity {

    TextView dotDescriptionTextView;
    Button hintButton;

    TextView targetCoordTextview;
    TextView distanceToTarget;
    TextView currentCoordTextview;

    Button verifyButton;
    EditText codeVerificationInput;

    private AppState appState;

    private FusedLocationProviderClient mFusedLocationClient;
    private int locationRequestCode = 1000;
    private double wayLatitude = 0.0, wayLongitude = 0.0;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_the_dot);

        hintButton = findViewById(R.id.hint_button);
        verifyButton = findViewById(R.id.verify_button);
        targetCoordTextview = findViewById(R.id.target_coord_textview);

        appState = AppState.getInstance();

        updateUI();

        //location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //check for permissions and getLastKnownLocation if the permission just acquired
        checkForPermissions();




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


    private void checkForPermissions() {
        // check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // request for permission

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);

        } else {
            // already permission granted
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        currentCoordTextview = FindTheDotActivity.this.findViewById(R.id.current_coord_textview);
                        String locationString = "Lat: " + String.valueOf(wayLatitude) + "\nLon: " + String.valueOf(wayLongitude);
                        currentCoordTextview.setText(locationString);

                        float a = location.distanceTo(appState.getCurrentDotLocation());
                        distanceToTarget = findViewById(R.id.distance_value_textview);
                        distanceToTarget.setText("" + a);

                    }
                }
            });
        }
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

        String currentDotLat = String.valueOf(appState.getCurrentDotLocation().getLatitude());
        String currentDotLon = String.valueOf(appState.getCurrentDotLocation().getLongitude());
        String currentDotLocation = "Lat: " + currentDotLat + "\nLon: " + currentDotLon;
        targetCoordTextview.setText(currentDotLocation);

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
