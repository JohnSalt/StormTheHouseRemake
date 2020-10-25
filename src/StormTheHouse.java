import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;


public class StormTheHouse extends PApplet {
    final int MAIN_MENU = 0, PLAY_GAME = 1,SHOP_MENU = 2, EXIT_GAME = 3;
    int gameState = MAIN_MENU;
    int day = 1, textSizeModifier = 100;
    double reloadTime = 1312.5;
    boolean isMouseEnabled = true, spawnEnemies = true, isMouseHeld;
    ArrayList<Enemy> enemies = new ArrayList<>();


    PImage img1, img2,img3,img4,img5,img6,img7,img8,img9,img10,mainMenu,startButtonLarge,startButtonSmall,clipSizeUpgrade,wallUpgrade,houseUpgrade,sniperRifle,addGunman,addCraftsman,missileSilo,repair,done;

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
        clipSizeUpgrade = loadImage("clip-size-upgrade.png");
        wallUpgrade = loadImage("upgrade-wall.png");
        sniperRifle = loadImage("sniper-rifle.png");
        repair = loadImage("repair.png");
        missileSilo = loadImage("missile-silo.png");
        houseUpgrade = loadImage("house-upgrade.png");
        addGunman = loadImage("add-gunman.png");
        addCraftsman = loadImage("add-craftsman.png");
        done = loadImage("done.png");

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
        if (gameState == EXIT_GAME) {
            gameStateExitGame();
        }
    }

    public void mousePressed() {
        isMouseHeld = true;
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
                }
                ammoIncreaseButtonPressed();
            }
        }
    }
    public void mouseReleased() {
        isMouseHeld = false;
    }
    public void checkForLoss() {
        if (base.getHealth() <= 0) {
            gameState = EXIT_GAME;
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
        if (key == 'i') {
            wallet.increaseMoney(10000);
        }
        if (key == ' ') {
            System.out.println("Space pressed");
            if (clip.getAmmo() != clip.getCapacity()) {
                System.out.println("Ammo<Capacity");
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
        for (Enemy enemy : enemies) {
            enemy.move();
        }
    }
    public void checkWallCollision() {  //makes the wall a barrier, changes speed to 0
        int wallX;
        for (Enemy enemy : enemies) {
            wallX = (enemy.getStartingY() + 1200) / base.getSlope();

            if (enemy.getCurrentX() >= wallX) {
                enemy.setSpeed(0);
            }
        }
    }

    public void damageWall() {         //checks if enemies aren't moving(if speed = 0) and damages the wall 1 for every enemy that isn't moving every 10 frames(1 second)
        for (Enemy enemy : enemies) {
            if (enemy.getSpeed() == 0) {
                if (frameCount % 10 == 0) {
                    base.setHealth(1);
                }
            }
        }
    }

    public void updateHealthBar() {    //updates wall health visual
        float healthBarLength = (float)base.getHealth()/base.getMaxHealth()*140;
        stroke(0);
        fill(227, 123, 123);
        if (healthBarLength >= 0) {
            rect((float)window.getWindowWidth() / 2+100, 5, healthBarLength, 12);
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
        for (Enemy enemy : enemies) {
            enemy.setSpeedModifier((day / 20) + 4);
        }
    }
    public void gameStateMainMenu() {

        image(mainMenu,0,0);

        if (mouseX >= window.getWindowWidth() - 300 && mouseX <= window.getWindowWidth() && mouseY >= window.getWindowHeight() - 180 && mouseY <= window.getWindowHeight()-5) {
            image(startButtonLarge, window.getWindowWidth() - 305, window.getWindowHeight() - 185);
        } else {
            image(startButtonSmall, window.getWindowWidth() - 300, window.getWindowHeight() - 180);
        }


        if (mousePressed && mouseX>=window.getWindowWidth() - 300 && mouseX <= window.getWindowWidth() && mouseY >= window.getWindowHeight() - 180 && mouseY <= window.getWindowHeight()-2) {
            gameState = PLAY_GAME;
        }
    }
    public void gameStateRunGame() {

        updateSpeedModifier();
        textSize(15);
        image(window.background,0,0);
        window.drawComponents(this);
        base.drawBase(this);

        if (spawnEnemies) {
            createEnemies();
        }
        spawnEnemies = false;



        textAlign(CENTER);
        fill(0);
        text("$" + wallet.getMoney(),(float)window.getWindowWidth()/2,16);
        textAlign(LEFT);

        if (enemies.size() == 0) { //Round End
            gameState = SHOP_MENU;
        }

        for (Enemy enemy : enemies) {
            loadImages(enemy.getCurrentX(), enemy.getStartingY());  //ANIMATION
        }

        if (clip.getAmmo() == 0 && isMouseHeld) {
            fill(252,0,0);
            textAlign(CENTER);
            text("Out Of Ammo", mouseX, mouseY-15);
            textAlign(LEFT);
            fill(0);
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


        text(base.getHealth() + "/" + base.getMaxHealth(),(float)window.getWindowWidth()/2+245,17);  //base health counter
        text(clip.getAmmo() + "/" + clip.getCapacity(),158,17);

        updateHealthBar();
        updateAmmoBar();
        reloadAnimation();
        checkForLoss();

    }

    public void gameStateShopMenu() {
        image(window.background,0,0);
        base.drawBase(this);
        fill(100,100,100,225);

        rect(0,0,window.getWindowWidth(),window.getWindowHeight());
        imageMode(CENTER);
        image(clipSizeUpgrade,205,192);
        image(wallUpgrade,410,192);
        image(repair,615,192);
        image(addCraftsman,820,192);
        image(addGunman,205,384);
        image(missileSilo,410,384);
        image(houseUpgrade,615,384);
        image(sniperRifle,820,384);
        image(done,(float)window.getWindowWidth()/2,window.getWindowHeight()-60);
        imageMode(CORNER);
        textAlign(CENTER);
        fill(170);
        text("$" + wallet.getMoney(),(float)window.getWindowWidth()/2,16);
        textAlign(LEFT);
        if (mousePressed && mouseX >= window.getWindowWidth()/2-50 && mouseX <= window.getWindowWidth()/2+50 && mouseY >= window.getWindowHeight() - 80 && mouseY <= window.getWindowHeight() - 40) {
            System.out.println("Start Game");
            gameState = PLAY_GAME;
            day++;
            spawnEnemies = true;
        }
        if (mousePressed && mouseX >= 205 && mouseX <= 305 && mouseY >= 192 && mouseY <= 292) {  //upgrade clip
            if (wallet.getMoney()>=1000) {
                clip.setCapacity(clip.getCapacity() + 1);
                wallet.decreaseMoney(1000);
            }
        }
        if (mousePressed && mouseX >= 410 && mouseX <= 510 && mouseY >= 192 && mouseY <= 292) {
            if (wallet.getMoney()>=3000 && base.getMaxHealth() < 250) {
                base.increaseMaxHealth(50);
                base.setHealth(base.getMaxHealth());
                wallet.decreaseMoney(3000);
            }
        }
        if (mousePressed && mouseX >= 615 && mouseX <= 715 && mouseY >= 192 && mouseY <= 292) {
            if (wallet.getMoney()>=800 && base.getHealth() < base.getMaxHealth()) {
                if (base.getHealth() > base.getMaxHealth()-20) {
                    base.setHealth(base.getMaxHealth());
                }
                base.increaseHealth(20);
                wallet.decreaseMoney(800);
            }
        }
        clip.setAmmo(clip.getCapacity());
    }

    public void gameStateExitGame() {
        noLoop();
        isMouseEnabled = false;
        textSize(60);
        textAlign(CENTER);
        fill(0);
        rect(0,0,1024,576);
        fill(194, 54, 54);
        text("YOU DUMB ASS UWU", (float)window.getWindowWidth() / 2, (float)window.getWindowHeight() / 2);
    }
}
//947*Math.log(clip.getCapacity()) + 1059 when to stop timer