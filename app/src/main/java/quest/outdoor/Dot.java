package quest.outdoor;

import android.location.Location;

public class Dot {

    private String description;
    private String hint;
    private Location location;
    private String code;

    public Dot(String description, String hint, Location location, String code) {
        this.description = description;
        this.hint = hint;
        this.location = location;
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public String getHint() {
        return hint;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Location getLocation() {
        return location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
