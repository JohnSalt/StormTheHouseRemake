import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.Random;

public class StormTheHouse extends PApplet {

    Random r = new Random();
    int day = 0, textSizeModifier = 100, power = 1;
    Window window = new Window(this);
    ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Integer> speeds;
    private boolean isMouseEnabled = true;
    Base base = new Base();
    ArrayList<PImage> images = new ArrayList<>();
    PImage img1, img2,img3,img4,img5;
    Wallet wallet = new Wallet();
    Magazine clip = new Magazine();
    Timer t = new Timer();


    public static void main(String[] args) {
        PApplet.main("StormTheHouse");
    }

    public void setup() {
        img1 = loadImage("frame1.gif");
        img2 = loadImage("frame2.gif");
        img3 = loadImage("frame3.gif");
        img4 = loadImage("frame4.gif");
        img5 = loadImage("frame5.gif");
        images.add(img1);
        images.add(img2);
        images.add(img3);
        images.add(img4);
        images.add(img5);
        window.drawBackground(this);
        frameRate(10);
        for(int i = 0; i < enemies.size(); i++) {
            enemies.get(i).setImages(images.get(i));
        }
    }

    public void settings() {
        size(window.getWindowWidth(), window.getWindowHeight());
    }

    public void draw() {

        window.drawBackground(this);
        base.drawBase(this);
        for(int i = 0; i < enemies.size(); i++) {
            loadImages(enemies.get(i).getCurrentX(),enemies.get(i).getStartingY());
        }

        textSize(30);
        text("Day: " + day, window.getWindowWidth()-textSizeModifier, 35);

        if (enemies.size() == 0) { //Round End
            System.out.println("Enemies list empty");
            day++;                 //increase day therefore number of enemies
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
        damageWall();
        text("Wall: " + base.getHealth() + "/100",window.getWindowWidth()-230,75);
        if (base.getHealth() < 0) {
            text("Wall: " + 0 + "/100",window.getWindowWidth()-230,75);
        }
        updateHealthBar();
        checkForLoss();
    }

    public void mousePressed() {
        if (isMouseEnabled) {
            if (clip.getAmmo() > 0) {
                for (int i = 0; i < enemies.size(); i++) {
                    if (mouseX >= enemies.get(i).getCurrentX() - 15 && mouseX <= enemies.get(i).getCurrentX() + 25 && mouseY >= enemies.get(i).getStartingY() + 3 && mouseY <= enemies.get(i).getStartingY() + 45) {
                        enemies.get(i).setHealth(1);
                        fill(255, 0, 0);
                        ellipseMode(CENTER);
                        ellipse(mouseX, mouseY, 5, 5);
                        if (enemies.get(i).getHealth() == 0) {
                            enemies.remove(i);
                        }
                    }
                }
                clip.decreaseAmmo();
                System.out.println(clip.getAmmo());
            } else {
                System.out.println("Out of ammo");
            }
        }
    }
    public void checkForLoss() {
        for (int i = 0; i < enemies.size(); i++) {
            if (base.getHealth() <= 0) {
                noLoop();
                isMouseEnabled = false;
                textSize(30);
                text("GAME OVER", window.getWindowWidth() / 2 - 105, window.getWindowHeight() / 2 + 5);
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
        if (key == 'k') {
            for (int i = 0; i < enemies.size(); i++) {
                enemies.remove(i); //removes enemies DEBUG
            }
        }
        if (key == ' ' && clip.getAmmo() != clip.getCapacity()) {
            t.setTotalTime(clip.getCapacity()*100+millis());
            if(t.getSavedTime()<t.getTotalTime()) {
                t.setSavedTime(millis());
                System.out.println("Reloading...");
            } else {
                clip.setAmmo(clip.getCapacity());
                System.out.println("Reloaded!");
            }
        }
    }

    public void createEnemies() {       //creates enemies based on day
        for (int i = 0; i < 2.4*Math.sqrt(day) + 1.4*day + 3; i++) { //y = 2.4\sqrt{x}+1.4x+3 defines number of enemies
            enemies.add(new Enemy());
            setSpeedModifier(day, enemies.get(i));
            enemies.get(i).drawEnemy(this, enemies.get(i));
        }
    }

    public void moveAll() {             //move all of the enemies each frame
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).move();
        }
    }
    public void checkWallCollision() {  //makes the wall a barrier, changes speed to 0
        int wallX;
        for (int i = 0; i < enemies.size(); i++) {
            wallX = (enemies.get(i).getStartingY()+1200)/base.getSlope();

            if (enemies.get(i).getCurrentX() >= wallX) {
                enemies.get(i).setSpeed(0);
            }
        }
    }

    public void damageWall() {         //checks if enemies aren't moving(if speed = 0) and damages the wall 1 for every enemy that isn't moving every 10 frames(1 second)
        for (int i = 0; i < enemies.size(); i++) {
            if (enemies.get(i).getSpeed() == 0) {
                if (frameCount%10 == 0) {
                    base.setHealth(1);
                }
            }
        }
    }

    public void updateHealthBar() {    //updates wall health visual
        float healthBarLength = (float)base.getHealth()/100*140;
        stroke(0);
        fill(207, 209, 88);
        if (healthBarLength >= 0) {
            rect(window.getWindowWidth() / 2 - 70, 10, healthBarLength, 20);
        }
    }

    public void loadImages(int x, int y) {   //sprite animation
        imageMode(CORNER);
        if (frameCount%5==0) {
            image(img1, x, y);
        } else if (frameCount%5==1){
            image(img2,x,y);
        } else if (frameCount%5==2){
            image(img3,x,y);
        } else if (frameCount%5==3){
            image(img4,x,y);
        } else if (frameCount%5==4){
            image(img5,x,y);
        }
    }

    public void setSpeedModifier(int day, Enemy enemy) {       //makes them faster dependant on the day
        int change = day/10;
        if (day%10==0) {
            enemy.setSpeedModifier(enemy.getSpeedModifier() + change);
        }
    }
}
