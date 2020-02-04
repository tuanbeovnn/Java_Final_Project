package admin;

import java.io.Serializable;

public class Activity implements Serializable {
    private String date;
    private String activityName;
    private double cal;

    @Override
    public String toString() {
        return String.format("Activities: [Date=%s, Hours=%s, Activity = %s]", getDate(), getCal(), getCal());
    }

    public Activity(String date, String activityName, double cal) {
        this.date = date;
        this.activityName = activityName;
        this.cal = cal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public double getCal() {
        return cal;
    }

    public void setCal(double cal) {
        this.cal = cal;
    }
}
