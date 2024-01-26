public class Time {
    int hour;
    int mins;
    int seconds;

    public Time (int hour, int mins, int seconds) {
        this.hour = hour;
        this.mins = mins;
        this.seconds = seconds;

    }

    public Boolean isValid() {
        if (0 <= hour && hour <= 23 && 0 <= mins && mins <= 59 && 0 <= seconds && seconds <= 59) {
            return true;
        }

        return false;
    }

    public boolean isEqual(Time time) {
        if (this.convertToSeconds() == time.convertToSeconds()) {
            return true;
        }

        return false;
    }

    public int convertToSeconds() {
        return this.hour * 60 * 60 + this.mins * 60 + this.seconds;
    }

    public String returnTime() {
        if (this.seconds < 10) {
            return this.hour + ":" + this.mins  + ":0" + this.seconds;
        }
        return this.hour + ":" + this.mins  + ":" + this.seconds;
    }

}