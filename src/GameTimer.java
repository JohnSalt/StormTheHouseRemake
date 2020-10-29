import processing.core.PApplet;

public class GameTimer {
    private long startTime;
    boolean isRunning;

    public GameTimer() {
        startTime = 0;
        isRunning = false;
    }

    public void startTimer(PApplet parent) {
        isRunning = true;
        startTime = parent.millis();
    }

    public void stopTimer() {
        isRunning = false;
    }


    public boolean getIsRunning() {
        return isRunning;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getElapsedTime(PApplet parent) {
        return parent.millis() - startTime;
    }
}
