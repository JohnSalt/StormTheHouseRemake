public class Timer {
    private int savedTime, totalTime;

    public Timer() {
        this.savedTime = 0;
        this.totalTime = 0;
    }

    public Timer(int savedTime, int totalTime) {
        this.savedTime = savedTime;
        this.totalTime = totalTime;
    }

    public void setSavedTime(int savedTime) {
        this.savedTime = savedTime;
    }
    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }
    public int getSavedTime() {
        return savedTime;
    }
    public int getTotalTime() {
        return totalTime;
    }
    public void resetTimer() {
        this.savedTime = 0;
    }
}
