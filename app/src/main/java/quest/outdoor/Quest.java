package quest.outdoor;

import java.util.ArrayList;

public class Quest {

    int currentDot = 0;
    String introduction;
    ArrayList<Dot> dots = new ArrayList<>();

    public Quest() {
    }

    public Quest(String introduction, ArrayList<Dot> dots) {
        this.introduction = introduction;
        this.dots = dots;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void addDotToAQuest(Dot newDot)
    {
        dots.add(newDot);
    }

    public String getIntroduction() {
        return introduction;
    }

    public ArrayList<Dot> getDotsArray() {
        return dots;
    }
}
