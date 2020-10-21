import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Random;

public class StormTheHouse extends PApplet {

    Random r = new Random();
    int day = 0, textSizeModifier = 100, power = 1;
    Window window = new Window(this);
    Enemy e = new Enemy(8,50);
    ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Integer> speeds;
    private boolean isMouseEnabled = true;
    Base base = new Base();


    public static void main(String[] args) {
        PApplet.main("StormTheHouse");
    }

    public void setup() {
        window.drawBackground(this);
        frameRate(10);

    }

    public void settings() {
        size(window.getWindowWidth(), window.getWindowHeight());
    }

    public void draw() {
        window.drawBackground(this);
        base.drawBase(this);
        textSize(30);
        text("Day: " + day, window.getWindowWidth()-textSizeModifier, 35);
        if (enemies.size() == 0) {
            System.out.println("Enemies list empty");
            day++;
            if (day%power == 0) {
                textSizeModifier += 15;
                power*=10;
            }
            createEnemies();
        }
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).drawEnemy(this,enemies.get(i));
        }
        moveAll();
        checkWallCollision();
        checkForLoss();
    }
    public void mousePressed() {
        if (isMouseEnabled) {
            for (int i = 0; i < enemies.size(); i++) {
                if (mouseX >= enemies.get(i).getCurrentX() - 15 && mouseX <= enemies.get(i).getCurrentX() + 25 && mouseY >= enemies.get(i).getStartingY() && mouseY <= enemies.get(i).getStartingY() + 50) {
                    enemies.remove(i);
                }
            }
        }
    }
    public void checkForLoss() {
        for (int i = 0; i < enemies.size(); i++) {
            if (enemies.get(i).getCurrentX() >= window.getWindowWidth()) {
                noLoop();
                isMouseEnabled = false;
                text("GAME OVER", window.getWindowWidth() / 2 - 40, window.getWindowHeight() / 2 + 5);
            }
        }
    }
    public void keyPressed() {
        if (key == 'p') {
            loop();
            isMouseEnabled = true;
        }
        if (key == 'o') {
            noLoop();
            isMouseEnabled = false;
        }
        if (key == ESC) {
            exit();
        }
    }

    public void createEnemies() {
        for (int i = 0; i < day*2; i++) {
            enemies.add(new Enemy());
            enemies.get(i).drawEnemy(this, enemies.get(i));
        }
    }
    public void moveAll() {
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).move();
        }
    }
    public void checkWallCollision() {
        int wallX;
        for (int i = 0; i < enemies.size(); i++) {
            wallX = (enemies.get(i).getStartingY()+1200)/base.getSlope();

            if (enemies.get(i).getCurrentX() >= wallX) {
                enemies.get(i).setSpeed(0);
            }
        }
    }
}
