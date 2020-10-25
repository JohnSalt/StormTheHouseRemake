import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
import java.util.Random;

public class StormTheHouse extends PApplet {
    final int MAIN_MENU = 0, PLAY_GAME = 1,SHOP_MENU = 2, EXIT_GAME = 3;
    int gameState = MAIN_MENU;
    int day = 1, textSizeModifier = 100, power = 1;
    double reloadTime = 1312.5;
    boolean isMouseEnabled = true, gameIsRunning = true, spawnEnemies = true, increaseDay = true;
    ArrayList<Enemy> enemies = new ArrayList<>();


    PImage img1, img2,img3,img4,img5,img6,img7,img8,img9,img10,mainMenu,startButtonLarge,startButtonSmall;
    Random r = new Random();
    Window window = new Window(this);
    Base base = new Base();
    ArrayList<PImage> images = new ArrayList<>();
    Wallet wallet = new Wallet();
    Magazine clip = new Magazine();
    GameTimer timer1 = new GameTimer();
    GameTimer timer2 = new GameTimer();


    public static void main(String[] args) {
        PApplet.main("StormTheHouse");
    }

    public void setup() {
        mainMenu = loadImage("main-menu-screen.png");
        startButtonLarge = loadImage("start-button-larger.png");
        startButtonSmall = loadImage("start-button-smaller.png");
        img1 = loadImage("frame1.gif");
        img2 = loadImage("frame1.gif");
        img3 = loadImage("frame2.gif");
        img4 = loadImage("frame2.gif");
        img5 = loadImage("frame3.gif");
        img1 = loadImage("frame3.gif");
        img2 = loadImage("frame4.gif");
        img3 = loadImage("frame4.gif");
        img4 = loadImage("frame5.gif");
        img5 = loadImage("frame5.gif");
        images.add(img1);
        images.add(img2);
        images.add(img3);
        images.add(img4);
        images.add(img5);
        images.add(img6);
        images.add(img7);
        images.add(img8);
        images.add(img9);
        images.add(img10);

        window.background = loadImage("background.png");
        window.ammoButton = loadImage("clipupgrade.png");
        window.drawComponents(this);
        frameRate(20);
        for(int i = 0; i < enemies.size(); i++) {
            enemies.get(i).setImages(images.get(i));
        }
    }

    public void settings() {
        size(window.getWindowWidth(), window.getWindowHeight());
    }

    public void draw() {
        /*switch (gameState) {
            case MAIN_MENU:
                gameStateMainMenu();
                System.out.println("Main Menu");
            case PLAY_GAME:
                gameStateRunGame();
                System.out.println("Running Game");
            case SHOP_MENU:
                gameStateShopMenu();
                System.out.println("Shop Menu");
            case EXIT_GAME:

        }*/

        if(gameState == MAIN_MENU) {
            gameStateMainMenu();
            System.out.println("MAIN MENU");
        }
        if(gameState == PLAY_GAME) {
            gameStateRunGame();
            System.out.println("PLAY GAME");
        }
        if (gameState == SHOP_MENU) {
            gameStateShopMenu();
        }
    }

    public void mousePressed() {
        if (gameState == PLAY_GAME) {
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
                                updateWallet();
                            }
                        }
                    }

                    clip.decreaseAmmo();

                } else {
                    textAlign(CENTER);
                    text("Out Of Ammo", mouseX, mouseY - 15);
                }
                ammoIncreaseButtonPressed();
            }
        }
    }
    public void checkForLoss() {
        for (int i = 0; i < enemies.size(); i++) {
            if (base.getHealth() <= 0) {
                noLoop();
                isMouseEnabled = false;
                textSize(60);
                textAlign(CENTER);
                fill(0);
                rect(0,0,1024,576);
                fill(194, 54, 54);
                text("YOU DUMB ASS UWU", window.getWindowWidth() / 2, window.getWindowHeight() / 2);
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
        if (key == ' ') {
            System.out.println("Space pressed");
            if (clip.getAmmo() != clip.getCapacity()) {
                System.out.println("Ammo<Capacity");
                /*if (!isReloading) {
                    System.out.println("IsReloading= false");
                    startTimer();
                    isReloading = true;
                    System.out.println("Reload");
                    isMouseEnabled = false;
                }*/

                if (!timer1.getIsRunning()) {
                    System.out.println("Not Reloading, starting reload now.");
                    timer1.startTimer(this);
                    isMouseEnabled = false;
                }
            }
        }
    }

    public void createEnemies() {       //creates enemies based on day
        for (int i = 0; i < 2.4*Math.sqrt(day) + 1.4*day + 3; i++) { //y = 2.4\sqrt{x}+1.4x+3 defines number of enemies
            enemies.add(new Enemy());
            setSpeedModifier(day, enemies.get(i));
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
        fill(227, 123, 123);
        if (healthBarLength >= 0) {
            rect(window.getWindowWidth() / 2+100, 5, healthBarLength, 12);
        }
    }
    public void updateAmmoBar() {
        float ammoBarLength = (float)clip.getAmmo()/clip.getCapacity()*140;
        stroke(0);
        fill(207, 209, 88);
        if (ammoBarLength >= 0) {
            rect(5,5,ammoBarLength,12);
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
    public void updateWallet() {
        wallet.increaseMoney(100);
    }

    public void reloadAnimation() {
        if (timer1.isRunning) {
            if (millis()-timer1.getStartTime() >0 && millis()-timer1.getStartTime() <= reloadTime/10) {
                rect(5,5,14,12);
            } else if (millis()-timer1.getStartTime() >reloadTime/10 && millis()-timer1.getStartTime() <= reloadTime/5) {
                rect(5,5,28,12);
            } else if (millis()-timer1.getStartTime() >reloadTime/5 && millis()-timer1.getStartTime() <= reloadTime/10*3) {
                rect(5,5,42,12);
            } else if (millis()-timer1.getStartTime() >reloadTime/10*3 && millis()-timer1.getStartTime() <= reloadTime/10*4) {
                rect(5,5,56,12);
            } else if (millis()-timer1.getStartTime() >reloadTime/10*4 && millis()-timer1.getStartTime() <= reloadTime/10*5) {
                rect(5,5,70,12);
            } else if (millis()-timer1.getStartTime() >reloadTime/10*5 && millis()-timer1.getStartTime() <= reloadTime/10*6) {
                rect(5,5,84,12);
            } else if (millis()-timer1.getStartTime() >reloadTime/10*6 && millis()-timer1.getStartTime() <= reloadTime/10*7) {
                rect(5,5,98,12);
            } else if (millis()-timer1.getStartTime() >reloadTime/10*7 && millis()-timer1.getStartTime() <= reloadTime/10*8) {
                rect(5,5,112,12);
            } else if (millis()-timer1.getStartTime() >reloadTime/10*8 && millis()-timer1.getStartTime() <= reloadTime/10*9) {
                rect(5,5,126,12);
            } else if (millis()-timer1.getStartTime() >reloadTime/10*9 && millis()-timer1.getStartTime() <= reloadTime) {
                rect(5,5,140,12);
            }
        }
    }
    public void ammoIncreaseButtonPressed() {
        if (mouseX >= 2 && mouseX <= 42 && mouseY >=27 && mouseY <=67) {
            clip.setAmmo(clip.getAmmo()+1);
            if (wallet.getMoney()>=1000) {
                clip.increaseCapacity(1);
                wallet.decreaseMoney(1000);
            }
        }
    }
    public void updateSpeedModifier() {
        for (int i = 0; i< enemies.size();i++) {
            enemies.get(i).setSpeedModifier((day/20)+4);
        }
    }
    public void gameStateMainMenu() {
        System.out.println("IN GAMESTATE");
        image(mainMenu,0,0);
        if (mouseX >= window.getWindowWidth() - 300 && mouseX <= window.getWindowWidth() && mouseY >= window.getWindowHeight() - 180 && mouseY <= window.getWindowHeight()-5) {
            image(startButtonLarge, window.getWindowWidth() - 305, window.getWindowHeight() - 185);
        } else {
            image(startButtonSmall, window.getWindowWidth() - 300, window.getWindowHeight() - 180);
        }
        if (mousePressed && mouseX>=window.getWindowWidth() - 300 && mouseX <= window.getWindowWidth() && mouseY >= window.getWindowHeight() - 180 && mouseY <= window.getWindowHeight()-2) {
            System.out.println("SWITCH STATE");
            gameState = PLAY_GAME;
        }
    }
    public void gameStateRunGame() {

        updateSpeedModifier();
        textSize(15);
        image(window.background,0,0);
        window.drawComponents(this);
        base.drawBase(this);
        System.out.println("SHOULD DRAW BACKGROUND");
        if (spawnEnemies) {
            createEnemies();
        }
        spawnEnemies = false;
        image(window.ammoButton, 2,27);
        textAlign(CENTER);
        fill(0);
        text("$" + wallet.getMoney(),window.getWindowWidth()/2,16);
        textAlign(LEFT);

        if (enemies.size() == 0) { //Round End
            gameState = SHOP_MENU;
            /*System.out.println("Enemies list empty");
            day++;                 //increase day therefore number of enemies
            if (day%power == 0) {
                textSizeModifier += 8;
                power*=10;
            }
            createEnemies();*/
        }

        for (int i = 0; i < enemies.size(); i++) {
            loadImages(enemies.get(i).getCurrentX(), enemies.get(i).getStartingY());  //ANIMATION
        }



        if (timer1.getIsRunning() && millis() - timer1.getStartTime() >= reloadTime) {      //checks if reloading is done
            System.out.println("Reloading Done");
            clip.setAmmo(clip.getCapacity());
            isMouseEnabled = true;
            timer1.stopTimer();
        }



        text("Day: " + day, window.getWindowWidth()-textSizeModifier, 16);  //draws the day number



        moveAll();
        checkWallCollision();
        damageWall();


        text(base.getHealth() + "/100",window.getWindowWidth()/2+245,17);  //base health counter
        text(clip.getAmmo() + "/" + clip.getCapacity(),158,17);
        if (base.getHealth() < 0) {
            text(0 + "/100",window.getWindowWidth()-430,17);
        }
        updateHealthBar();
        updateAmmoBar();
        reloadAnimation();
        checkForLoss();

    }

    public void gameStateShopMenu() {
        image(window.background,0,0);
        base.drawBase(this);
        fill(0,0,0,50);
        rect(0,0,window.getWindowWidth(),window.getWindowHeight());
        fill(0);//Continue Button
        rectMode(CENTER);
        rect(window.getWindowWidth()/2,window.getWindowHeight()-60,100,40);
        rectMode(CORNER);
        if (mousePressed && mouseX >= window.getWindowWidth()/2-50 && mouseX <= window.getWindowWidth()/2+50 && mouseY >= window.getWindowHeight() - 60 && mouseY <= window.getWindowHeight() - 20) {
            System.out.println("Start Game");
            gameState = PLAY_GAME;
            day++;
            spawnEnemies = true;
        }
    }
}
//947*Math.log(clip.getCapacity()) + 1059 when to stop timer