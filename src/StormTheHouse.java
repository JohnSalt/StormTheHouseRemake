import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import java.util.ArrayList;
import java.util.Random;
public class StormTheHouse extends PApplet {
    final int MAIN_MENU = 0, PLAY_GAME = 1,SHOP_MENU = 2, EXIT_GAME = 3;
    int gameState = MAIN_MENU;
    int day = 1, textSizeModifier = 100, houseUpgradeCost = 50000, wallUpgradeCost = 3000, gunmanCost = 2000, sniperCost = 75000, craftsmanCost = 8000, siloCost = 35000, gunDamage = 1, gunmen = 0, craftsman = 0, siloWorkers = 0, payroll = 0, missileRadius = 75;
    double reloadTime = 1312.5;
    boolean isMouseEnabled = true, spawnEnemies = true, missileSiloOwned = false, isMouseHeld;
    ArrayList<Enemy> enemies = new ArrayList<>();

    Random r = new Random();
    PImage img1, img2,img3,img4,img5,img6,img7,img8,img9,img10,mainMenu,startButtonLarge,startButtonSmall,clipSizeUpgrade,wallUpgrade,houseUpgrade,sniperRifle,addGunman,addCraftsman,missileSilo,repair,done,frog;
    PFont gameFont, sansSerif;
    Window window = new Window(this);
    Base base = new Base();
    ArrayList<PImage> images = new ArrayList<>();
    Wallet wallet = new Wallet();
    Magazine clip = new Magazine();
    GameTimer timer1 = new GameTimer();
    GameTimer dayTimer = new GameTimer();
    int width = window.getWindowWidth(), height = window.getWindowHeight();

    public static void main(String[] args) {
        PApplet.main("StormTheHouse");
    }

    public void setup() {
        sansSerif = createFont("SansSerif.bold",10);
        gameFont = createFont("GROBOLD.ttf",15);
        textFont(gameFont);
        mainMenu = loadImage("images/main-menu-screen.png");
        startButtonLarge = loadImage("images/start-button-larger.png");
        startButtonSmall = loadImage("images/start-button-smaller.png");
        clipSizeUpgrade = loadImage("images/clip-size-upgrade.png");
        wallUpgrade = loadImage("images/upgrade-wall.png");
        sniperRifle = loadImage("images/sniper-rifle.png");
        repair = loadImage("images/repair.png");
        missileSilo = loadImage("images/missile-silo.png");
        houseUpgrade = loadImage("images/house-upgrade.png");
        addGunman = loadImage("images/add-gunman.png");
        addCraftsman = loadImage("images/add-craftsman.png");
        done = loadImage("images/done.png");
        frog = loadImage("images/frog.png");
        img1 = loadImage("images/frame1.gif");
        img2 = loadImage("images/frame1.gif");
        img3 = loadImage("images/frame2.gif");
        img4 = loadImage("images/frame2.gif");
        img5 = loadImage("images/frame3.gif");
        img1 = loadImage("images/frame3.gif");
        img2 = loadImage("images/frame4.gif");
        img3 = loadImage("images/frame4.gif");
        img4 = loadImage("images/frame5.gif");
        img5 = loadImage("images/frame5.gif");
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

        window.background = loadImage("images/background.png");

        window.drawComponents(this);
        frameRate(20);
        for(int i = 0; i < enemies.size(); i++) {
            enemies.get(i).setImages(images.get(i));
        }
    }

    public void settings() {
        size(width, height);
    }

    public void draw() {
        if(gameState == MAIN_MENU) {
            gameStateMainMenu();
        }
        if(gameState == PLAY_GAME) {
            if (!dayTimer.getIsRunning()) {
                dayTimer.startTimer(this);
            }

            if(millis() - dayTimer.getStartTime() < 8000) {
                gameStateRunGame();
                text((float)(millis()-dayTimer.getStartTime())/1000, 0,height-40);
            } else {
                dayTimer.stopTimer();
                wallet.decreaseMoney(payroll);
                gameState = SHOP_MENU;
            }
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
                            enemies.get(i).decreaseHealth(gunDamage);
                            fill(255, 0, 0);
                            ellipseMode(CENTER);
                            ellipse(mouseX, mouseY, 5, 5);
                            if (enemies.get(i).getHealth() == 0) {
                                enemies.remove(i);
                                wallet.increaseMoney(100);
                            }
                        }
                    }
                    clip.decreaseAmmo();
                }
                ammoIncreaseButtonPressed();
            }
        }
        if (gameState == SHOP_MENU) {
            if (mouseX >= width/2-50 && mouseX <= width/2+50 && mouseY >= height - 80 && mouseY <= height - 40) {

                gameState = PLAY_GAME;
                day++;
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------\nDay: " + day);
                spawnEnemies = true;
            }
            if (mouseX >= 155 && mouseX <= 255 && mouseY >= 142 && mouseY <= 242) {  //upgrade clip
                if (wallet.getMoney()>=1000) {
                    clip.setCapacity(clip.getCapacity() + 1);
                    wallet.decreaseMoney(1000);
                }
            }
            if (mouseX >= 360 && mouseX <= 460 && mouseY >= 142 && mouseY <= 242) { //upgrade wall
                System.out.println("Upgrade Wall Pressed");
                if (wallet.getMoney()>=wallUpgradeCost && wallUpgradeCost != 13000) {
                    System.out.println(base.getHealth() + "/" + base.getMaxHealth());
                    base.increaseMaxHealth(50);
                    System.out.println(base.getHealth() + "/" + base.getMaxHealth());
                    if (base.getMaxHealth()-base.getHealth() < 50) {
                        base.setHealth(base.getMaxHealth());
                    } else {
                        base.increaseHealth(50);
                    }
                    System.out.println(base.getHealth() + "/" + base.getMaxHealth());
                    wallet.decreaseMoney(wallUpgradeCost);
                    if (wallUpgradeCost == 3000) {
                        wallUpgradeCost = 8000;
                    } else if (wallUpgradeCost == 8000) {
                        wallUpgradeCost = 12000;
                    } else if (wallUpgradeCost == 12000) {
                        wallUpgradeCost = 13000;
                    }
                }
            }
            if (mouseX >= 565 && mouseX <= 665 && mouseY >= 142 && mouseY <= 242) { //heal wall
                if (wallet.getMoney()>=800 && base.getHealth() < base.getMaxHealth()) {
                    base.increaseHealth(Math.min(base.getMaxHealth() - base.getHealth(), 20));
                    wallet.decreaseMoney(800);
                }
            }

            if (mouseX >= 770 && mouseX <= 870 && mouseY >= 142 && mouseY <= 242) {  //add craftsman
                if (wallet.getMoney() >= craftsmanCost) {
                    craftsman++;
                    wallet.decreaseMoney(craftsmanCost);
                    payroll += 800;
                }
            }

            if (mouseX >= 155 && mouseX <= 255 && mouseY >= 334 && mouseY <= 444) {  //add gunman
                if (wallet.getMoney() >= gunmanCost) {
                    gunmen++;
                    wallet.decreaseMoney(2000);
                    payroll += 150;
                }
            }

            if (mouseX >= 360 && mouseX <= 460 && mouseY >= 334 && mouseY <= 444) {
                if (wallet.getMoney() >= siloCost && missileSiloOwned) {
                    wallet.decreaseMoney(siloCost);
                    payroll += 1600;
                    siloWorkers++;
                }
                if (wallet.getMoney() >= siloCost && !missileSiloOwned) {
                    missileSiloOwned = true;
                    wallet.decreaseMoney(siloCost);
                    siloCost = 12000;
                }
            }

            if (mouseX >= 565 && mouseX <= 665 && mouseY >= 334 && mouseY <= 444) {  //upgrade house
                if (wallet.getMoney() >= houseUpgradeCost && houseUpgradeCost != 150000) {
                    base.increaseMaxHealth(375);
                    wallet.decreaseMoney(houseUpgradeCost);
                    System.out.println(houseUpgradeCost);
                    houseUpgradeCost += 50000;
                    System.out.println(houseUpgradeCost);
                }
            }
            if (mouseX >= 770 && mouseX <= 870 && mouseY >= 334 && mouseY <= 444) {  //sniper button
                if (wallet.getMoney() >= sniperCost) {
                    gunDamage = 2;
                    wallet.decreaseMoney(sniperCost);
                    sniperCost = 0;
                }
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
        for (int i = 0; i < 2*day-1; i++) { //y = 2.4*Math.sqrt(day) + 1.4*day + 3 defines number of enemies
            enemies.add(new Enemy());
            setSpeedModifier(day, enemies.get(i));
        }
    }
    public void createSingleEnemy() {
        enemies.add(new Enemy());
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
                if (frameCount % 20 == 0) {
                    base.decreaseHealth(1);
                }
            }
        }
    }

    public void updateHealthBar() {    //updates wall health visual
        float healthBarLength = (float)base.getHealth()/base.getMaxHealth()*140;
        stroke(0);
        fill(227, 123, 123);
        if (healthBarLength >= 0) {
            rect((float)width / 2+100, 5, healthBarLength, 12);
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

    public void reloadAnimation() {    //draws reloading animation
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

        if (mouseX >= width - 300 && mouseX <= width && mouseY >= height - 180 && mouseY <= height-5) {
            image(startButtonLarge, width - 305, height - 185);
        } else {
            image(startButtonSmall, width - 300, height - 180);
        }


        if (mousePressed && mouseX>=width - 300 && mouseX <= width && mouseY >= height - 180 && mouseY <= height-2) {
            gameState = PLAY_GAME;
        }
    }
    public void gameStateRunGame() {

        updateSpeedModifier();
        textSize(15);

        image(window.background,0,0);

        window.drawComponents(this);
        base.drawBase(this);
        image(frog,830,(float)height/2-75);
        if (spawnEnemies) {
            createEnemies();
        }
        spawnEnemies = false;
        if (enemies.size() < 2*day-1) {
            createSingleEnemy();
        }
        textAlign(CENTER);
        fill(	133, 187, 101);
        text("$" + wallet.getMoney(),(float)width/2,18);
        textAlign(LEFT);
        fill(0);


        for (Enemy enemy : enemies) {
            loadImages(enemy.getCurrentX(), enemy.getStartingY());  //ANIMATION
        }
        gunmenShoot();
        shootSilo();
        craftsmanRepair();
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

        text("Day: " + day, width-textSizeModifier, 18);  //draws the day number

        moveAll();
        checkWallCollision();
        damageWall();

        fill(0,204,0);
        text(base.getHealth() + "/" + base.getMaxHealth(),(float)width/2+245,18);  //base health counter
        fill(204, 200, 78);
        text(clip.getAmmo() + "/" + clip.getCapacity(),158,18);//ammo counter

        updateHealthBar();
        updateAmmoBar();
        reloadAnimation();
        checkForLoss();
    }

    public void gameStateShopMenu() {
        for (int i = 0; i < enemies.size(); i++) {
            enemies.remove(i);
        }

        textSize(15);
        image(window.background,0,0);
        base.drawBase(this);
        fill(100,100,100,225);
        rect(0,0,width,height); //background

        fill(100);
        rect(0,0,1024,25); //top bar

        stroke(0);
        fill(0);
        rect((float)width/2+100, 5,142,15); //healthBar
        stroke(0);
        rect(5, 5,142,15); //ammo bar
        stroke(0);
        fill(227, 123, 123);
        rect((float)width/2+101,6,(float)base.getHealth()/base.getMaxHealth()*140,13); //health bar colored
        fill(207, 209, 88);
        rect(6,6,(float)clip.getAmmo()/clip.getCapacity()*140,13);  //ammo bar colored
        fill(0,204,0);
        text(base.getHealth() + "/" + base.getMaxHealth(),(float)width/2+245,20);  //base health counter
        fill(255,255,153);
        text(clip.getAmmo() + "/" + clip.getCapacity(),158,20);
        fill(207, 209, 88);

        textAlign(CENTER);

        fill(200);
        text("Payroll: $" + payroll + "/day", (float)width/2,(float)height/2);
        stroke(133, 187, 101);
        fill(133, 187, 101);
        text("$" + wallet.getMoney(),(float)width/2,20);

        imageMode(CENTER);
        image(clipSizeUpgrade,205,192);
        image(wallUpgrade,410,192);
        if (base.getMaxHealth()==250) {   //displays when max health is reached
            textAlign(CENTER);
            textSize(20);
            fill(0);
            text("MAX",411,194);
            textSize(19);
            fill(249, 255, 77);
            text("MAX",410,192);
        }

        image(repair,615,192);
        image(addCraftsman,820,192);
        image(addGunman,205,384);
        image(missileSilo,410,384);
        image(houseUpgrade,615,384);
        if (houseUpgradeCost == 150000) {   //displays when max health is reached
            textAlign(CENTER);
            textSize(20);
            fill(0);
            text("MAX",616,386);
            textSize(19);
            fill(249, 255, 77);
            text("MAX",615,384);
        }
        image(sniperRifle,820,384);
        image(done,(float)width/2,height-60);
        imageMode(CORNER);
        fill(167,95,9);
        textFont(sansSerif,10);
        stroke(0);
        if (wallUpgradeCost!=13000) {
            text("$" + wallUpgradeCost, 387, 165);
        }



        textFont(gameFont,15);
        textAlign(CENTER);

        textAlign(LEFT);

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
        text("YOU DUMB ASS UWU", (float)width / 2, (float)height / 2);
    }

    public void gunmenShoot() {
        System.out.println("Millis: " + millis());
        System.out.println(dayTimer.getElapsedTime(this)%(3500*(Math.pow(gunmen, -0.6))));
        if (millis() > 2000 && gunmen > 0 && dayTimer.getIsRunning() && dayTimer.getElapsedTime(this)%(3500*(Math.pow(gunmen, -0.6))) >= 0 && dayTimer.getElapsedTime(this)%(3500*(Math.pow(gunmen, -0.6))) <= 49) {
            if (enemies.get(0).getCurrentX() > 0) {
                enemies.get(0).decreaseHealth(gunDamage);
                System.out.println("Enemy shot");
            } else {
                System.out.println("Enemy outside window");
                getReachableEnemy().decreaseHealth(gunDamage);
            }
            checkForDead();
        }
    }
    public Enemy getReachableEnemy() {
        for (Enemy enemy : enemies) {
            if (enemy.getCurrentX() > 0) {
                return enemy;
            }
        }
        return enemies.get(0);
    }
    public void checkForDead() {
        for (int i = 0; i < enemies.size(); i++) {
            if (enemies.get(i).getHealth() <= 0) {
                enemies.remove(i);
                wallet.increaseMoney(100);
            }
        }
    }

    public void craftsmanRepair() {
        if (craftsman > 0 && base.getHealth() != base.getMaxHealth() && dayTimer.getIsRunning() && dayTimer.getElapsedTime(this) % (3000 * (Math.pow(craftsman, -0.75))) >= 0 && dayTimer.getElapsedTime(this) % (3000 * (Math.pow(craftsman, -0.75))) <= 49) {
            base.increaseHealth(1);
        }
    }
    public void siloShoot() {
        if (millis() > 2000 && siloWorkers > 0 && missileSiloOwned && dayTimer.getIsRunning() && dayTimer.getElapsedTime(this)%(3500*(Math.pow(siloWorkers, -0.6))) >= 0 && dayTimer.getElapsedTime(this)%(3500*(Math.pow(siloWorkers, -0.6))) <= 49) {
            int randomX = r.nextInt(600)+20, randomY = r.nextInt(326)+192; //pick random start point for top left of explosion area
            System.out.println("Missile Shot\nArea Affected (X): " + randomX + " to: " + randomX + missileRadius + "\n(Y): " + randomY + " to " + randomY + missileRadius);
            rect(randomX,randomY,missileRadius,missileRadius);
            for (int i = 0; i < enemies.size(); i++) {
                if (enemies.get(i).getCurrentX() > randomX && enemies.get(i).getCurrentX() < randomX + missileRadius && enemies.get(i).getStartingY() > randomY && enemies.get(i).getStartingY() < randomY + missileRadius) {
                    fill(200,0,0);

                    enemies.remove(i);
                    wallet.increaseMoney(100);
                    System.out.println("Enemy " + i + " removed.");
                }
            }

        }
    }
    public void shootSilo() { //fix index fix area affected
        if (millis() > 2000 && siloWorkers > 0 && missileSiloOwned && dayTimer.getIsRunning() && dayTimer.getElapsedTime(this)%(3500*(Math.pow(siloWorkers, -0.6))) >= 0 && dayTimer.getElapsedTime(this)%(3500*(Math.pow(siloWorkers, -0.6))) <= 49) {
            int randomEnemyIndex = r.nextInt(enemies.size()-1);
            float radiusStartX = enemies.get(randomEnemyIndex).getCurrentX()-((float)missileRadius/2), radiusStartY = enemies.get(randomEnemyIndex).getStartingY()-((float)missileRadius/2);
            fill(255,0,0);
            System.out.println(enemies.get(randomEnemyIndex).getCurrentX() + " " + enemies.get(randomEnemyIndex).getStartingY());
            System.out.println("Missile Shot\nArea Affected (X): " + (radiusStartX) + " to: " + (radiusStartX+missileRadius) + "\n(Y): " + radiusStartY + " to " + (radiusStartY+missileRadius));

            rect(radiusStartX,radiusStartY,missileRadius,missileRadius);
            rect(radiusStartX,radiusStartY,missileRadius,missileRadius);
            rect(radiusStartX,radiusStartY,missileRadius,missileRadius);
            rect(radiusStartX,radiusStartY,missileRadius,missileRadius);

            for (int i = 0; i<enemies.size();i++) {
                if (enemies.get(i).getCurrentX() >= radiusStartX && enemies.get(i).getCurrentX() <= (radiusStartX+missileRadius) && enemies.get(i).getStartingY() >= radiusStartY && enemies.get(i).getStartingY() <= (radiusStartY+missileRadius)) {
                    enemies.get(i).decreaseHealth(3);
                    wallet.increaseMoney(100);
                    System.out.println("Enemy " + i + " damaged by 3.");
                }
            }
            enemies.remove(randomEnemyIndex);
            wallet.increaseMoney(100);
            checkForDead();
        }
    }
}
//947*Math.log(clip.getCapacity()) + 1059 when to stop timer
