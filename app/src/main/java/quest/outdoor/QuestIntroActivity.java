package quest.outdoor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


public class QuestIntroActivity extends AppCompatActivity {

    TextView questIntroTextview;
    ImageView clickableIntroImage;
    Quest homeQuest;
    private AppState appState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_intro);

        checkGooglePlayServices(QuestIntroActivity.this);


        //create new quest and fill it with data
        appState = AppState.getInstance();
        homeQuest = createNewQuest();

        questIntroTextview = findViewById(R.id.quest_intro_textview);
        questIntroTextview.setText(appState.getCurrentQuest().getIntroduction());

        clickableIntroImage = findViewById(R.id.clickable_imageview);
        clickableIntroImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFindTheDotActivity();
            }
        });

    }


    public static boolean checkGooglePlayServices(final Activity activity) {
        final int googlePlayServicesCheck = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        switch (googlePlayServicesCheck) {
            case ConnectionResult.SUCCESS:
            {
                Log.d("GooglePlayServices", "SUCCESS");
                return true;
            }
            case ConnectionResult.SERVICE_DISABLED:
            case ConnectionResult.SERVICE_INVALID:
            case ConnectionResult.SERVICE_MISSING:
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
            {

                Log.d("GooglePlayServices", "update required");
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(googlePlayServicesCheck, activity, 0);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        activity.finish();
                    }
                });
                dialog.show();
            }

        }
        return false;
    }


    //method createNewQuest should take a file or some files and add them to correct places
    private Quest createNewQuest() {
        Quest newQuest = new Quest();

        //add description
        String intro = "Hi Mila! Here is a quest for you: using my hints collect six treasures to unlock hidden chest with a secret!";

        Location dotFrogLocation = new Location(LocationManager.GPS_PROVIDER);
        dotFrogLocation.setLatitude(37.9089932);
        dotFrogLocation.setLongitude(-122.0466974);
        Dot dotFrog = new Dot("There is a lonely froggie sitting in an empty fountain",
                "look right going home from a parking lot",
                dotFrogLocation,
                "222");


        Location dotYellowFrogLocation = new Location(LocationManager.GPS_PROVIDER);
        dotYellowFrogLocation.setLatitude(37.9085879);
        dotYellowFrogLocation.setLongitude(-122.0473394);
        Dot dotYellowFrog = new Dot("Where once a lizard lived, look for the yellow froggie, who wants to get to a pool",
                "it's sitting on a rock near the entrance",
                null,
                "222");


        Location dotXmasTreeLocation = new Location(LocationManager.GPS_PROVIDER);
        dotXmasTreeLocation.setLatitude(37.9079210);
        dotXmasTreeLocation.setLongitude(-122.0472127);
        Dot dotXmasTree = new Dot("We wish you a merry Xmas, \n We wish you a merry Xmas,\nWe wish you a merry Xmas\nand a Happy New Year",
                "favorite Lily's Xmas tree",
                null,
                "222");


        newQuest.setIntroduction(intro);
        newQuest.addDotToAQuest(dotFrog);
        newQuest.addDotToAQuest(dotYellowFrog);
        newQuest.addDotToAQuest(dotXmasTree);

        appState.setCurrentQuest(newQuest);

        return newQuest;
    }

    public void openFindTheDotActivity() {
        Intent openFindTheDot = new Intent(QuestIntroActivity.this, FindTheDotActivity.class);
        startActivity(openFindTheDot);
    }


}
