package quest.outdoor;

import android.content.Context;
import android.content.Intent;
import android.location.Location;

import java.util.ArrayList;

public class AppState {

    private static AppState instance = null;

    private int currentDotNumber = 0;
    private Quest currentQuest;

    private AppState() {
        //currentQuest = new Quest();
    }

    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return(instance);
    }

    public String getCurrentDotDescription()
    {
        String currentDotDescription = "";
        currentDotDescription = currentQuest.getDotsArray().get(currentDotNumber).getDescription();
        return currentDotDescription;
    }


    public String getCurrentDotHint()
    {
        String currentDotHint = "";
        currentDotHint = currentQuest.getDotsArray().get(currentDotNumber).getHint();
        return currentDotHint;
    }

    public String getCurrentDotCode()
    {
        String currentDotCode = "";
        currentDotCode = currentQuest.getDotsArray().get(currentDotNumber).getCode();
        return currentDotCode;
    }

    public int getCurrentDotNumber() {
        return currentDotNumber;
    }

    public void goToNextDot(Context context)
    {
        if(currentQuest.getDotsArray().size() > currentDotNumber + 1)
        {
            currentDotNumber++;
        }
        else
        {
            //finish game - win, show final dialog or activity
            Intent finalActivity = new Intent(context, FinalActivity.class);
            context.startActivity(finalActivity);
        }
    }

    public Quest getCurrentQuest() {
        return currentQuest;
    }

    public static void setInstance(AppState instance) {
        AppState.instance = instance;
    }

    public void setCurrentDotNumber(int currentDotNumber) {
        this.currentDotNumber = currentDotNumber;
    }

    public void setCurrentQuest(Quest currentQuest) {
        this.currentQuest = currentQuest;
    }

    public Location getCurrentDotLocation()
    {
        Location currentDotCoordinates
                = currentQuest.getDotsArray().get(currentDotNumber).getLocation();
        return currentDotCoordinates;
    }
}
