import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;


/**
 * Authour: Aston Cheng
 * Date: June 15, 2022 
 */

public class Sketch extends PApplet {
  int width = 1600;
  int height = 800;

  //int width = 1920;
  //int height = 1020;
  
  //int width = 1280;
  //int height = 720;

  int x = width/2;
  int y = 500;

  double x_speed = 0;
  double y_speed = 0;

  double x_friction = 0;
  double y_friction = 0;

  boolean upPressed = false;
  boolean downPressed = false;
  boolean rightPressed = false;
  boolean leftPressed = false;
 
  boolean makeTear = false;
  boolean enemyMakeTear = true;
  
  boolean tearUpPressed = false;
  boolean tearDownPressed = false;
  boolean tearRightPressed = false;
  boolean tearLeftPressed = false;

  PImage img;
  PImage background_room;
  PImage heart;
  PImage lamb;
  PImage fly;
  PImage background;
  PImage trophy;
  PImage chest;

  PImage the_lamb_spritesheet;
  PImage the_lamb_dead_sheet;
  PImage the_lamb_standing_sheet;
  PImage the_lamb_shooting_sheet;
  PImage[] the_lamb_frames;
  PImage[] the_lamb_dead_frames;
  int intlamb_frames = 4;
  int intlamb_dead_frames = 3;
  int the_lamb_frameWidth = 112;

  PImage YOU_DIED;

  int lambX = width/2;
  int lambY = 200;

  double lambSpeedX = 0;
  double lambSpeedY = 0;

  int i = 0;
  int[] enemy_i = new int[3];

  int[] xArray = new int[200];
  int[] yArray = new int[200];
  
  int[] xSpeedArray = new int[200];
  int[] ySpeedArray = new int[200];

  boolean[] tearExist = new boolean[200];
  boolean[] enemyTearExist = new boolean[1000];

  double[][] xEnemyArray = new double[1000][3];
  double[][] yEnemyArray = new double[1000][3];
  
  double[][] xEnemySpeedArray = new double[1000][3];
  double[][] yEnemySpeedArray = new double[1000][3];

  int[] angle_i = new int[10];

  int tearDelay = 0;
  int tearDamage = 3;

  boolean intangibility = false;
  int intangibilityTimer = 0;
  boolean wasHit = false;
  float life = 12; 

  int maxHealth = 1500;
  int bossHealth = maxHealth;

  int[] enemyHealth = new int[1000];

  int lambHitWall = 0;

  int[] objectLimiter = new int[3];
  
  int internalFrameCount = 0;
  int internalAttackDelay = 100;
  int attackIntensity;
  int gameState = 0;
  float bossPhase = 1;
  
  boolean eternalMode = false;

  int fadeWhite = 255;

  PFont text;
  PFont timesFont;

  public void settings() {
    size(width, height);
  }
  public void setup() {

    img = loadImage("010.01.00.png");
    background_room = loadImage("title.png");

    heart = loadImage("PlayerHeart2.png");
    lamb = loadImage("273.10.00.png");
    YOU_DIED = loadImage("YOU DIED BLACK.png");

    fly = loadImage("256.00.00.png");
    trophy = loadImage("trophy.jpg");
    chest = loadImage("chest.png");


    for(int i = 0; i < 200; i += 1){
      tearExist[i] = false;
      enemyTearExist[i] = false;
    }
    objectLimiter[0] = 900;
    objectLimiter[2] = 3;

    the_lamb_spritesheet = loadImage("the lamb.png");
    the_lamb_standing_sheet = the_lamb_spritesheet.get(0,0,the_lamb_frameWidth*intlamb_frames, 80);

    the_lamb_frames = new PImage[intlamb_frames];

    for(int frameNum = 0; frameNum < the_lamb_frames.length; frameNum++ ){
      the_lamb_frames[frameNum] = the_lamb_standing_sheet.get(the_lamb_frameWidth*frameNum, 0, the_lamb_frameWidth, 80);
    }

    text = createFont("Yu Gothic", 32);
    textFont(text);
    fill(0);

    timesFont = createFont("Times New Roman", 32);
    textFont(timesFont);
    fill(0);
  }

  public void draw() {
    background(background_room);
    
    //Title Screen
    if(gameState == 0){

    }

    //In-Game State
    if(gameState == 1 || gameState == 3){
      
      // Life and BossHealth
      for(int displayHearts = 0; displayHearts < life; displayHearts ++){
        image(heart, (displayHearts*25) + 100, 45);
      }
      if (life == 0){
        gameState = 2;
      }
      if(bossHealth > 0){
        stroke(0);
        fill(255, 0, 0);
        rect(200, 700,(float) (0.8* bossHealth), 10);
      } else if (bossHealth <= 0){
        gameState = 3;
        enemyMakeTear = false;
      }
      
      //tears array looping
      if(i > 197){
        i = 1;
        tearExist[i] = false;
      }
      if(enemy_i[0] > objectLimiter[0]){
        enemy_i[0] = 0;
      }
    
      //making player tears
      if (tearDelay == 0){
        if(makeTear == true){
          i ++;

          tearExist[i] = true;

          xArray[i] = x;
          yArray[i] = y;

          xSpeedArray[i + 1] = 0;
          ySpeedArray[i + 1] = 0;
          tearDelay = 5;
        }
      }
      if (tearDelay > 0){
        tearDelay -= 1;
      }
  
      //giving player tears attributes
      for (int f = 0; f <= 199; f ++){
        if (tearDelay > 0){
          if (tearUpPressed) {
            ySpeedArray[i] = -10;
          }
          if (tearDownPressed) {
            ySpeedArray[i] = 10;
          }
          if (tearLeftPressed) {
            xSpeedArray[i] = -10;
          }
          if (tearRightPressed) {
            xSpeedArray[i] = 10;
          }
        }

        xArray[f] = xArray[f] + xSpeedArray[f];
        yArray[f] = yArray[f] + ySpeedArray[f];

        if (tearExist[f] == true){
          stroke(0);
          fill(190,190,255);
          ellipse(xArray[f], yArray[f], 15, 15);

          if ((xArray[f] < (lambX + 50)) && (xArray[f] > lambX - 50) && (yArray[f] > lambY - 50) && (yArray[f] < (lambY + 50))){
            bossHealth -= tearDamage;
            tearExist[f] = false;
          }
          if (xArray[f] < -50 || xArray[f] > width + 50|| yArray[f] < -50 || yArray[f] > height + 50){
            tearExist[f] = false;
          }
          for(int n = 0; n < objectLimiter[2]; n += 1){
            if(enemyHealth[n] == 10){
              if ((xArray[f] < (xEnemyArray[n][2] + 20)) && (xArray[f] > xEnemyArray[n][2] - 20) && (yArray[f] > yEnemyArray[n][2] - 20) && (yArray[f] < (yEnemyArray[n][2] + 20))){
                enemyHealth[f] -= tearDamage;
                tearExist[f] = false;
              }
            }
          }
        }
      }

      //giving enemy tears attributes
      for (int f = 0; f <= objectLimiter[0]; f ++){
        if(enemyTearExist[f] == true){
          xEnemyArray[f][0] = xEnemyArray[f][0] + xEnemySpeedArray[f][0];
          yEnemyArray[f][0] = yEnemyArray[f][0] + yEnemySpeedArray[f][0];

          fill(255,190,190);
          ellipse((float)xEnemyArray[f][0], (float)yEnemyArray[f][0], 25, 25);

          
          xEnemyArray[f][1] = xEnemyArray[f][1] + xEnemySpeedArray[f][1];
          yEnemyArray[f][1] = yEnemyArray[f][1] + yEnemySpeedArray[f][1];

          fill(0,190,190);
          ellipse((float)xEnemyArray[f][1], (float)yEnemyArray[f][1], 25, 25);
          
          if (xEnemyArray[f][0] < -50 || xEnemyArray[f][0] > width + 50|| yEnemyArray[f][0] < -50 || yEnemyArray[f][0] > height + 50){
            enemyTearExist[f] = false;
          }
        }
      }
      

      playerMovementEngine();  
      intangibilityEngine();
   

      //the bosses' attack patterns
      attackIntensity = (1000/750)*bossHealth + 10;
      battleLoop();
    }
    // DEATH GAMESTATE
    if (gameState == 2){

      internalFrameCount = 0;
      
      background(fadeWhite);
      if (fadeWhite > 0){
        fadeWhite -= 1;
      }
      if (fadeWhite == 0){
        noTint();
        image(YOU_DIED, (width/2) - (384/2), (height/2) - (56/2));

        textFont(timesFont);
        fill(255);
        text("Press Space to Try Again", (width/2) - 150, 500);
      }
    }

    //spawn the ending chest
    if(gameState == 3){
      lamb.get(0,80, 1, 80);
      image(chest, 800 - 112, 400 - 91);

      if(x > 800 - 112 && x < 800 + 112 && y < 400 + 91 && y > 400 - 91){
        gameState = 4;
        fadeWhite = 0;
      } 
    }

    //victory screen
    if (gameState == 4){
      background(fadeWhite);
      if (fadeWhite < 225){
        fadeWhite += 1;
      }
      if (fadeWhite == 225){
        textFont(text);
        fill(0);
        text("You unlocked eternal mode!", 800 - 225, 300);
        text("Press space to go back to restart the fight", 800- 300, 400);
        text("Press E to activate eternal mode", 800 - 250, 500);
      }
    }
  }
/**
 * The keyPressed method built into processing. Checks if a key is pressed. No return value.
 */
  public void keyPressed(){
    if (key == 119){
      upPressed = true;
    }
    if (key == 115){
      downPressed = true; 
    }
    if (key == 100){
      leftPressed = true;
    }
    if (key == 97){
      rightPressed = true;
    }
    if(key == 101 && (gameState == 4 || gameState == 0) && eternalMode == false){
      eternalMode = true;
    } else if(key == 101 && (gameState == 4 || gameState == 0) && eternalMode == true){
      eternalMode = false;
    }
    if(key == 32 && (gameState == 4 || gameState == 2)){
      gameState = 0;
      bossHealth = 1500;
      life = 12;
      i = 0;
      enemy_i[0] = 0;
      bossPhase = 1;

      x = width/2;
      y = 500;
      
      lambX = width/2;
      lambY = 200;

      lambSpeedX = 0;
      lambSpeedY = 0;

      fadeWhite = 255;

      for(int reset_b = 0; reset_b < 200; reset_b += 1){
        tearExist[reset_b] = false;
      }
    }
    if (key == CODED){
      if(keyCode == UP){
        makeTear = true;

        tearUpPressed = true;
        
      }
      if(keyCode == DOWN){
        makeTear = true;

        tearDownPressed = true;

      }
      if(keyCode == LEFT){
        makeTear = true;

        tearLeftPressed = true;
        
      }
      if(keyCode == RIGHT){
        makeTear = true;

        tearRightPressed = true;
        
      }
    } 
    if(keyPressed == true && gameState == 0 && gameState != 4){
      background_room = loadImage("touhou background.jpg");  
      gameState = 1;
    }
  }
  /**
   * The keyReleased method built into processing. Checks if the key is released. No return method. 
   */
  public void keyReleased() {
    if (key == 119){
      upPressed = false;
    }
    if (key == 115){
      downPressed = false;
    }
    if (key == 100){
      leftPressed = false;
    }
    if (key == 97){
      rightPressed = false;
    }
    if (key == CODED){
      if(keyCode == UP){
        makeTear = false;

        tearUpPressed = false;
      }
      if(keyCode == DOWN){
        makeTear = false;

        tearDownPressed = false;
      }
      if(keyCode == LEFT){
        makeTear = false;

        tearLeftPressed = false;
      }
      if(keyCode == RIGHT){
        makeTear = false;

        tearRightPressed = false;
      }
    }
  }
  /**
   * The movement engine for both the player and the Lamb. No return method.
   */
  public void playerMovementEngine(){
    if (upPressed) {
      y_speed -= 3;
    }
    if (downPressed) {
      y_speed += 3;
    }
    if (leftPressed) {
      x_speed += 3;
    }
    if (rightPressed) {
      x_speed -= 3;
    }

    // Check for bouncing
    if ((x >= width - 36) || (x <= 36)) {
      x_speed = x_speed * -1.5;
    }
    if ((y >= height + 30) || (y <= 30)) {
      y_speed = y_speed * -1.5;
    }
    // friction
    if (x_speed > 0) {
      x_friction =  x_speed*0.2;
    }
    if (x_speed < 0) {
      x_friction =  x_speed*0.2;
    }

    if (Math.round(x_speed) == 0) {
      x_friction = 0;
    }


    if (y_speed > 0) {
      y_friction =  y_speed*0.2;
    }
    if (y_speed < 0) {
      y_friction = y_speed*0.2;
    }

    if (Math.round(y_speed)== 0) {
      y_friction = 0;
    }

    x_speed = x_speed - x_friction;
    y_speed = y_speed - y_friction;

    x = (int) (x + (int) x_speed);
    y = (int) (y + (int) y_speed);

    lambX = (int) (lambX + lambSpeedX);
    lambY = (int) (lambY + lambSpeedY);
    
    for(int b = 0; b <= 3; b += 1){
      if(enemyHealth[b] > 0){
        xEnemySpeedArray[b][2] = (-1*(Math.cos(Math.toRadians(playerAngle((int)xEnemyArray[b][2], (int)yEnemyArray[b][2], x, y)))));
        yEnemySpeedArray[b][2] = (-1*(Math.sin(Math.toRadians(playerAngle((int)xEnemyArray[b][2], (int)yEnemyArray[b][2], x, y)))));

        xEnemyArray[b][2] = xEnemyArray[b][2] + xEnemySpeedArray[b][2];
        yEnemyArray[b][2] = yEnemyArray[b][2] + yEnemySpeedArray[b][2];

      }
    }

    if (intangibility == true){
      tint(255,0,0);
      image(img, x - 30, y - 25);
    } else if (intangibility == false){
      noTint();
      image(img, x - 30, y - 25);
    }

    if(gameState == 1){
      noTint();
      image(the_lamb_frames[(frameCount/5)%intlamb_frames], lambX - 75, lambY - 75, 72 * 2, 69 * 2);
    }
    if(gameState == 3){
      noTint();
      image(lamb, lambX - 75, lambY - 75, 72 * 2, 69 * 2);
    }
    

    
    for (int f = 0; f < objectLimiter[2]; f ++){
      if(enemyHealth[f] == 10){
        image(fly, (float) xEnemyArray[f][2] - 18, (float)yEnemyArray[f][2] - 11, 36, 22);
      }
    }

  }

  /**
   * The intangibility engine for the player. Also checks for hit detection against the player. No return method.
   */
  public void intangibilityEngine(){
    if (intangibility == false){
      if ((x < (lambX + 50)) && (x > lambX - 50) && (y > lambY - 50) && (y < (lambY + 50))){
        wasHit = true;
      }
      for(int enemy_hitboxCheck = 0; enemy_hitboxCheck < objectLimiter[0]; enemy_hitboxCheck += 1){
        if((x < (xEnemyArray[enemy_hitboxCheck][0] + 20)) && (x > (xEnemyArray[enemy_hitboxCheck][0] - 20)) && (y > (yEnemyArray[enemy_hitboxCheck][0] - 20)) && (y < (yEnemyArray[enemy_hitboxCheck][0] + 20))){
          wasHit = true;
        }
      }
      for(int k = 0; k < 10; k += 1){
        if(enemyHealth[k] == 10){
          for(int enemy_hitboxCheck = 0; enemy_hitboxCheck < objectLimiter[2]; enemy_hitboxCheck += 1){
            if((x < (xEnemyArray[enemy_hitboxCheck][2] + 20)) && (x > (xEnemyArray[enemy_hitboxCheck][2] - 20)) && (y > (yEnemyArray[enemy_hitboxCheck][2] - 20)) && (y < (yEnemyArray[enemy_hitboxCheck][2] + 20))){
              wasHit = true;
            }
          }  
        }    
      }
    }
    if (intangibility == false && intangibilityTimer == 0 && wasHit == true){
      intangibilityTimer = 60;
      intangibility = true;
      life -= 1;
      wasHit = false;
    }
    if (intangibilityTimer > 0){
     intangibilityTimer -= 1;
    }
    if (intangibilityTimer == 0 && intangibility == true){
      intangibility = false;
    }
  }

  /**
   * The attack patterns that the boss does. Has phases. No Return method.
   */
  public void battleLoop(){
    if (bossHealth < maxHealth && bossHealth >= 0){
      internalFrameCount += 1;
      if(eternalMode == true && (bossPhase == 1 || bossPhase == 2)){
        for(int eternalIntensity = 0; eternalIntensity <= 1000; eternalIntensity += attackIntensity){
          if(internalFrameCount == eternalIntensity){
            octaAttack(0, 2);
            octaAttack(15, 2);
            octaAttack(30, 2);
          }
        }
      }
      if(bossHealth >= maxHealth/2 && bossPhase == 1){
        if (internalFrameCount == -1){
          lambHitWall = 0;
        }
              
        if(internalFrameCount >= 30 && internalFrameCount < 35){
          triAttack();
        }
        if(internalFrameCount >= 30 + (60 * internalAttackDelay/100) && internalFrameCount < 35 + (60 * internalAttackDelay/100)){
          triAttack();
        }
        if(internalFrameCount >= 30 + 2*(60 * internalAttackDelay/100) && internalFrameCount < 35 + 2*(60 * internalAttackDelay/100)){
          triAttack();
        }
                  
        if(internalFrameCount == 350){
          sniperAttack(playerAngle(lambX, lambY, x, y), 20);
        }
        if(internalFrameCount == 400){
          sniperAttack(playerAngle(lambX, lambY, x, y), 20);
        }
        if(internalFrameCount == 450){
          sniperAttack(playerAngle(lambX, lambY, x, y), 20);
        }

        if(internalFrameCount >= 550){
          if(internalFrameCount == 551){
            lambSpeedX = (int) (-16*(Math.cos(Math.toRadians(playerAngle(lambX, lambY, x, y)))));
            lambSpeedY = (int) (-16*(Math.sin(Math.toRadians(playerAngle(lambX, lambY, x, y)))));
          }
            
          if (lambX <= 50 || lambX >= width - 50){

            lambHitWall += 1;
            sniperAttack(playerAngle(lambX, lambY, x, y), 19);
            sniperAttack(-1*(playerAngle(lambX, lambY, x, y)), 19);
            
            lambSpeedX = lambSpeedX * -1;
          } else if(lambY <= 50 || lambY >= height -50){
            lambHitWall += 1;
            sniperAttack(playerAngle(lambX, lambY, x, y), 19);
            
            lambSpeedY = lambSpeedY * -1;
          }
        } else{
          if (lambX <= 50 || lambX >= width - 50){
            lambSpeedX = lambSpeedX * -1;
          } else if(lambY <= 50 || lambY >= height -50){
            lambSpeedY = lambSpeedY * -1;
          }
        }
        if (lambHitWall >= 3 && internalFrameCount <= 255){
          lambSpeedX = lambSpeedX * 0.90;
          lambSpeedY = lambSpeedY * 0.90;
          
          if (Math.round(lambSpeedX) == 0) {
            lambSpeedX = 0;
          }
          if (Math.round(lambSpeedY) == 0) {
            lambSpeedY = 0;
          }

        }
        if (lambHitWall >= 3 && internalFrameCount >= 0){
          internalFrameCount = -60;
        }
      }
      else if(bossHealth < maxHealth/2 && bossPhase == 1){
        lambSpeedX = 0;
        lambSpeedY = 0;

        internalFrameCount = 0;

        spawnFlies(4);
        bossPhase = 2;
      }
      if (bossPhase == 2){
        if(internalFrameCount >= 150 && internalFrameCount < 190 && internalFrameCount % 5 == 0){
          
          lambSpeedX = (int) (-2*(Math.cos(Math.toRadians(playerAngle(lambX, lambY, x, y)))));
          lambSpeedY = (int) (-2*(Math.sin(Math.toRadians(playerAngle(lambX, lambY, x, y)))));
          
          octaAttack(0, 6);
        }

        if(internalFrameCount >= 250 && internalFrameCount < 290 && internalFrameCount % 5 == 0){
          lambSpeedX = (int) (-4*(Math.cos(Math.toRadians(playerAngle(lambX, lambY, x, y)))));
          lambSpeedY = (int) (-4*(Math.sin(Math.toRadians(playerAngle(lambX, lambY, x, y)))));

          octaAttack(22.5, 8);
        }

        if(internalFrameCount >= 350 && internalFrameCount < 390 && internalFrameCount % 5 == 0){
          lambSpeedX = (int) (-6*(Math.cos(Math.toRadians(playerAngle(lambX, lambY, x, y)))));
          lambSpeedY = (int) (-6*(Math.sin(Math.toRadians(playerAngle(lambX, lambY, x, y)))));

          octaAttack(22.5, 8);
        }

        if(internalFrameCount >= 415 && internalFrameCount <= 499){
          lambSpeedX = lambSpeedX * 0.90;
          lambSpeedY = lambSpeedY * 0.90;
          
          if (Math.round(lambSpeedX) == 0) {
            lambSpeedX = 0;
          }
          if (Math.round(lambSpeedY) == 0) {
            lambSpeedY = 0;
          }
        }
        if(internalFrameCount == 499){
          enemy_i[0] = 0;
        }
        
        if(internalFrameCount >= 500 && internalFrameCount < 550 && internalFrameCount % 10 == 0){
          enemyTear(lambX, lambY, 15 + internalFrameCount % 2, 4);
          enemyTear(lambX, lambY, 165  + internalFrameCount % 2, 4);
          enemyTear(lambX, lambY, 195 + internalFrameCount % 2, 4);
          enemyTear(lambX, lambY, 345 + internalFrameCount % 2, 4);
        }

        if(internalFrameCount == 571){
          try{
            for(int l = 0; l < 20; l += 1){
              xEnemySpeedArray[enemy_i[0] - l][0] = (-24*(Math.cos(Math.toRadians(playerAngle((int)xEnemyArray[enemy_i[0] - l][0], (int)yEnemyArray[enemy_i[0] - l][0], x, y)))));
              yEnemySpeedArray[enemy_i[0] - l][0] = (-24*(Math.sin(Math.toRadians(playerAngle((int)xEnemyArray[enemy_i[0] - l][0], (int)yEnemyArray[enemy_i[0] - l][0], x, y)))));     
            }
          }            
          catch(IndexOutOfBoundsException error){

          }
        }

        if(internalFrameCount >= 572 && internalFrameCount < 622 && internalFrameCount % 10 == 0){
          enemyTear(lambX, lambY, 15 + internalFrameCount % 2, 4);
          enemyTear(lambX, lambY, 165  + internalFrameCount % 2, 4);
          enemyTear(lambX, lambY, 195 + internalFrameCount % 2, 4);
          enemyTear(lambX, lambY, 345 + internalFrameCount % 2, 4);
        }

        if(internalFrameCount == 623){
          try{
            for(int l = 0; l < 20; l += 1){
              xEnemySpeedArray[enemy_i[0] - l][0] = (-24*(Math.cos(Math.toRadians(playerAngle((int)xEnemyArray[enemy_i[0] - l][0], (int)yEnemyArray[enemy_i[0] - l][0], x, y)))));
              yEnemySpeedArray[enemy_i[0] - l][0] = (-24*(Math.sin(Math.toRadians(playerAngle((int)xEnemyArray[enemy_i[0] - l][0], (int)yEnemyArray[enemy_i[0] - l][0], x, y)))));     
            }
          }            
          catch(IndexOutOfBoundsException error){

          }
        }
        if(internalFrameCount >= 624 && internalFrameCount < 674 && internalFrameCount % 10 == 0){
          enemyTear(lambX, lambY, 15 + internalFrameCount % 2, 4);
          enemyTear(lambX, lambY, 165  + internalFrameCount % 2, 4);
          enemyTear(lambX, lambY, 195 + internalFrameCount % 2, 4);
          enemyTear(lambX, lambY, 345 + internalFrameCount % 2, 4);
        }

        if(internalFrameCount == 675){
          try{
            for(int l = 0; l < 20; l += 1){
              xEnemySpeedArray[enemy_i[0] - l][0] = (-24*(Math.cos(Math.toRadians(playerAngle((int)xEnemyArray[enemy_i[0] - l][0], (int)yEnemyArray[enemy_i[0] - l][0], x, y)))));
              yEnemySpeedArray[enemy_i[0] - l][0] = (-24*(Math.sin(Math.toRadians(playerAngle((int)xEnemyArray[enemy_i[0] - l][0], (int)yEnemyArray[enemy_i[0] - l][0], x, y)))));     
            }
          }            
          catch(IndexOutOfBoundsException error){

          }
          enemy_i[0] = 0;
        }


        if(internalFrameCount >= 725 && internalFrameCount <= 875){
          if (lambX <= 50 || lambX >= width - 50){
            lambSpeedX = lambSpeedX * -1;

          }
          if(lambY <= 50 || lambY >= height -50){
            lambSpeedY = lambSpeedY * -1;

          }
        }
        for(int d = 0; d < 75*3; d += 75){
          if(internalFrameCount == 725 + d){
            lambSpeedX = (int) (-20*(Math.cos(Math.toRadians(playerAngle(lambX, lambY, x, y)))));
            lambSpeedY = (int) (-20*(Math.sin(Math.toRadians(playerAngle(lambX, lambY, x, y)))));
          }

          if(internalFrameCount >= 725 + d && internalFrameCount <= 750 + d && internalFrameCount % 5 == 0){
            enemyTear(lambX, lambY, (int) (playerAngle(lambX, lambY, x, y) + 30), 4);
            enemyTear(lambX, lambY, (int) (playerAngle(lambX, lambY, x, y) - 30), 4);
          }

          if(internalFrameCount >= 750 + d && internalFrameCount <= 800 + d){
            lambSpeedX = lambSpeedX * 0.90;
            lambSpeedY = lambSpeedY * 0.90;
            
            if (Math.round(lambSpeedX) == 0) {
              lambSpeedX = 0;
            }
            if (Math.round(lambSpeedY) == 0) {
              lambSpeedY = 0;
            }
          }
        }
        if(internalFrameCount == 1000){
          for(int i = -20; i < 20; i += 5){
            for(int f = -20; f < 20; f += 5){
              enemyTear(lambX + i, lambY + f, (int)playerAngle(lambX, lambY, x, y), -4);
            }
          }
        }

        if(internalFrameCount == 1100){
          for(int l = 0; l < 64; l += 1){
            xEnemySpeedArray[enemy_i[0] - l][0] = (-15*(Math.cos(Math.toRadians(l*18))));
            yEnemySpeedArray[enemy_i[0] - l][0] = (-15*(Math.sin(Math.toRadians(l*18))));     
          }
        }

        if(internalFrameCount > 1150){
          internalFrameCount = 0;
        }
        
      }
      if(bossHealth < 200 && bossPhase == 2){
        lambSpeedX = 0;
        lambSpeedY = 0;


        enemy_i[0] = 0;
        internalFrameCount = 0;
        bossPhase = 3;
        enemy_i[2] = 0;
        objectLimiter[2] = 0;
      }
      if(bossPhase == 3){
        if(internalFrameCount >= 50 && internalFrameCount <= 150 && internalFrameCount % 5 == 0){
          heartAttack();
        }
        if(internalFrameCount % 5 == 0){
          enemyTear(200, 0, 270, -10);
          enemyTear(1400, 0, 270, -10);
        }
        if(internalFrameCount > 175){
          internalFrameCount = 0;
        }
      }
    }
  }

  /**
   * Finds the angle between 2 points
   * @param xShooter x Position of the shooter
   * @param yShooter y Position of the shooter
   * @param xTarger x Position of the target
   * @param yTarger y Position of the target
   * @return The angle in degrees.
   */
  public double playerAngle(int xShooter, int yShooter, int xTarget, int yTarget){
    double degree = 0;
    try{
      degree = Math.atan2((yShooter - yTarget),(xShooter - xTarget));
      degree = Math.toDegrees(degree);
    }
    catch(ArithmeticException error){
    }
    return degree;
  }

  /**
   * Makes the shooter fire a tear in a certain direction with magnitude.
   * @param xPos The x position of the shooter
   * @param yPos The y position of the shooter
   * @param angle The angle to fire towards
   * @param magnitude The magnitude of the shot i.e. how strong it is. Method usually wants negative values.
   */
  public void enemyTear(int xPos, int yPos, int angle, int magnitude){
    enemy_i[0] += 1;
    enemyTearExist[enemy_i[0]] = true;
    xEnemyArray[enemy_i[0]][0] = xPos;
    yEnemyArray[enemy_i[0]][0] = yPos;
    xEnemySpeedArray[enemy_i[0]][0] = (magnitude*(Math.cos(Math.toRadians(angle))));
    yEnemySpeedArray[enemy_i[0]][0]= (magnitude*(Math.sin(Math.toRadians(angle))));
  }

  /**
   * Makes three shots in 120, 240, and 360 degrees.
   */
  public void triAttack(){
    angle_i[1] += 10;

    for(int triAngle = 0; triAngle < 360; triAngle += 120){
      enemyTear(lambX, lambY, angle_i[1] + triAngle, 4);
    }
  }
  /**
   * Makes a trail of tears curving in small increments, as well as making a ring of tears. Not much of a heart attack. 
   */
  public void heartAttack(){
    angle_i[2] += 1;

    if(internalFrameCount % 60 == 0){
      octaAttack(0 + angle_i[2], 2);
      octaAttack(15 + angle_i[2], 2);
      octaAttack(30 + angle_i[2], 2);
    }

    enemyTear(lambX, lambY, angle_i[2], 5);
    enemyTear(lambX, lambY, angle_i[2] + 90, 5);
    enemyTear(lambX, lambY, angle_i[2] + 180, 5);
    enemyTear(lambX, lambY, angle_i[2] + 270, 5);
  }
  /**
   * Asks for a player angle and magnitude, then fires a bunch of tears with the same angle and descending magnitude.
   * @param playerAngle Asks for the angle.
   * @param magnitude Asks for the magnitude.
   */
  public void sniperAttack(double playerAngle, int magnitude){

    angle_i[3] = (int)playerAngle;
    for(int i = -5; i > -1*magnitude; i -= 1){
      enemyTear(lambX, lambY, angle_i[3], i);
    }
  }
  /**
   * Creates 8 tears in a ring.
   * @param angle Asks for the starting angle
   * @param magnitude Asks for the magnitude.
   */
  public void octaAttack(double angle, int magnitude){
    for(int triAngle = 0; triAngle < 360; triAngle += 45){
      enemyTear(lambX, lambY, (int) angle + triAngle, magnitude);
    }
  }
  /**
   * Creates flies.
   * @param fliesSpawned Asks for the number of flies you want to spawn.
   */
  public void spawnFlies(int fliesSpawned){
    for(int i = 0; i < fliesSpawned; i ++){
      objectLimiter[2] = fliesSpawned;
      enemy_i[2] = fliesSpawned;

      xEnemyArray[i][2] = (lambX - 200) + i*100;

      yEnemyArray[i][2] = lambY;

      enemyHealth[i] = 10;
      
    }
  }
}