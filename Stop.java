public class Stop implements Comparable<Stop>{
    Time time;
    int stopID;
    int tripID;


    // Should I ignore stopName until end? and search file for the stopID?
    public Stop(int stopID, int tripID, String time) {
        this.stopID = stopID;
        this.tripID = tripID;

        String[] time1 = time.split(":"); 
        this.time = new Time(Integer.valueOf(time1[0]), Integer.valueOf(time1[1]), Integer.valueOf(time1[2]));

    }

    // returns true if this stop's trip ID is larger, false otherwise
    public boolean isTripIDLarger(Stop stop) {
        if (this.tripID > stop.tripID) {
            return true;
        }

        return false;
    }

    public boolean isTimeEqual(Stop stop) {
        if (this.time.isEqual(stop.time)) {
            return true;
        }

        return false;
    }

    // returns true if time is valid
    public boolean isTimeValid() {
        return time.isValid();
    }

    public void displayStopInfo() {
        System.out.println(tripID + ", " + this.time.returnTime() + ", " + stopID);

    }

    public int compareTo(Stop stop) {
        return this.tripID - stop.tripID;
    }

}
