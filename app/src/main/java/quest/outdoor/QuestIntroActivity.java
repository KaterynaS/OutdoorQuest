package quest.outdoor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class QuestIntroActivity extends AppCompatActivity {

    TextView questIntroTextview;
    ImageView clickableIntroImage;
    Quest homeQuest;
    private AppState appState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_intro);

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

    //method createNewQuest should take a file or some files and add them to correct places
    private Quest createNewQuest() {
        Quest newQuest = new Quest();

        //add description
        String intro = "Hi Mila! Here is a quest for you: using my hints collect six treasures to unlock hidden chest with a secret!";
        Dot dotFrog = new Dot("There is a lonely froggie sitting in an empty fountain",
                "look right going home from a parking lot",
                null,
                "222");
        Dot dotLizard = new Dot("Look for a lisard, who wants to get to a pool",
                "it's sitting on a rock near the entrance",
                null,
                "222");
        Dot dotXmasTree = new Dot("We wish you a merry Xmas, \n We wish you a merry Xmas,\nWe wish you a merry Xmas\nand a Happy New Year",
                "favorite Lily's Xmas tree",
                null,
                "222");


        newQuest.setIntroduction(intro);
        newQuest.addDotToAQuest(dotFrog);
        newQuest.addDotToAQuest(dotLizard);
        newQuest.addDotToAQuest(dotXmasTree);

        appState.setCurrentQuest(newQuest);

        return newQuest;
    }

    public void openFindTheDotActivity() {
        Intent openFindTheDot = new Intent(QuestIntroActivity.this, FindTheDotActivity.class);
        startActivity(openFindTheDot);
    }


}
