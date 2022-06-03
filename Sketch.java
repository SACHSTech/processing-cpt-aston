import processing.core.PApplet;
import processing.core.PImage;

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

  PImage the_lamb_spritesheet;
  PImage the_lamb_standing_sheet;
  PImage the_lamb_shooting_sheet;
  PImage[] the_lamb_frames;
  int intlamb_frames = 4;
  int the_lamb_frameWidth = 112;

  PImage YOU_DIED;

  int lambX = width/2;
  int lambY = 200;

  double lambSpeedX = 0;
  double lambSpeedY = 0;

  int i = 0;
  int[] enemy_i = new int[2];

  int[] xArray = new int[200];
  int[] yArray = new int[200];
  
  int[] xSpeedArray = new int[200];
  int[] ySpeedArray = new int[200];

  boolean[] tearExist = new boolean[200];
  boolean[] enemyTearExist = new boolean[1000];

  double[][] xEnemyArray = new double[1000][2];
  double[][] yEnemyArray = new double[1000][2];
  
  double[][] xEnemySpeedArray = new double[1000][2];
  double[][] yEnemySpeedArray = new double[1000][2];

  int[] angle_i = new int[10];

  int tearDelay = 0;
  int tearDamage = 1;

  boolean intangibility = false;
  int intangibilityTimer = 0;
  boolean wasHit = false;
  float life = 12; 

  int maxHealth = 1000;
  int bossHealth = maxHealth;

  int lambHitWall = 0;

  int objectLimiter = 255;
  
  int internalFrameCount = 0;
  int internalAttackDelay = 100;
  int gameState = 1;

  float bossPhase = 1;

  int fadeWhite = 255;

  public void settings() {
    size(width, height);
  }
  public void setup() {
    background(255, 255, 255);
    img = loadImage("010.01.00.png");
//    background_room = loadImage("19-F08-07b-19.jpg");  
    heart = loadImage("PlayerHeart2.png");
    lamb = loadImage("273.10.00.png");
    YOU_DIED = loadImage("YOU DIED BLACK.png");
    for(int i = 0; i < 200; i += 1){
      tearExist[i] = false;
      enemyTearExist[i] = false;
    }

    the_lamb_spritesheet = loadImage("the lamb.png");
    the_lamb_standing_sheet = the_lamb_spritesheet.get(0,0,the_lamb_frameWidth*intlamb_frames, 80);

    the_lamb_frames = new PImage[intlamb_frames];

    for(int frameNum = 0; frameNum < the_lamb_frames.length; frameNum++ ){
      System.out.println("load frames");
      the_lamb_frames[frameNum] = the_lamb_standing_sheet.get(the_lamb_frameWidth*frameNum, 0, the_lamb_frameWidth, 80);
    }
  }

  public void draw() {
    //background(background_room);
    background(255, 255, 255);
    
    //Title Screen
    if(gameState == 0){
      
    }

    // PLAYING GAMESTATE
    if(gameState == 1 || gameState == 3){
      if (intangibility == true){
        img = loadImage("010.01.00 2.png");
      } else if (intangibility == false){
        img = loadImage("010.01.00.png");
      }

      for(int displayHearts = 0; displayHearts < life; displayHearts ++){
        image(heart, (displayHearts*25) + 25, 45);
      }
      if (life == 0){
        gameState = 2;
      }
      if(bossHealth > 0){
        stroke(0);
        fill(255, 0, 0);
        rect(200, 700, (float)(1000/maxHealth * bossHealth), 10);
      } else if (bossHealth == 0){
        gameState = 3;
        enemyMakeTear = false;
      }
      
      if(i > 197){
        i = 0;
        for (int b = 0; b < 197; b ++){
          //xArray[b] = 0;
          //yArray[b] = 0;
          //xSpeedArray[b] = 0;
          //ySpeedArray[b] = 0;
        }
      }
      if(enemy_i[0] > objectLimiter){
        enemy_i[0] = 0;
        /*for (int b = 0; b < 290; b ++){
          xEnemyArray[b] = 0;
          yEnemyArray[b] = 0;
          xEnemySpeedArray[b] = 0;
          yEnemySpeedArray[b] = 0;
        }*/
      }
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
            println(bossHealth);
            tearExist[f] = false;
          }
          if (xArray[f] < -50 || xArray[f] > width + 50|| yArray[f] < -50 || yArray[f] > height + 50){
            tearExist[f] = false;
          }
        }
      }
      for (int f = 0; f <= objectLimiter; f ++){
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

      //movement engine
      playerMovementEngine();  
      intangibilityEngine();



      

      //println(playerAngle((lambX), (lambY), x - 30, y - 25));
      //println(angle_i);
      //println(tearUpPressed + " " + tearDownPressed + " " + tearLeftPressed + " " + tearRightPressed);
      //println(internalFrameCount);
      //enemyTear(lambX, lambY, (int) playerAngle((lambX), (lambY), x, y), -5);
      //println(lambHitWall);

      internalAttackDelay = (int)(bossHealth/10 + 50);
      if (bossHealth < maxHealth && bossHealth >= 0){
        internalFrameCount += 1;
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
          //if(internalFrameCount == 155 + internalAttackDelay){
            sniperAttack(playerAngle(lambX, lambY, x, y), 15);
          }
          if(internalFrameCount == 400){
          //if(internalFrameCount == 205 + internalAttackDelay){
            sniperAttack(playerAngle(lambX, lambY, x, y), 17);
          }
          if(internalFrameCount == 450){
          //if(internalFrameCount == 255 + internalAttackDelay){
            sniperAttack(playerAngle(lambX, lambY, x, y), 19);
          }

          if(internalFrameCount >= 550){
            if(internalFrameCount == 551){
          //if(internalFrameCount >= 400 + (internalAttackDelay)){
            //if(internalFrameCount == 400 + (internalAttackDelay)){
              lambSpeedX = (int) (-16*(Math.cos(Math.toRadians(playerAngle(lambX, lambY, x, y)))));
              lambSpeedY = (int) (-16*(Math.sin(Math.toRadians(playerAngle(lambX, lambY, x, y)))));
            }
              
            //if (lambX <= 50 || lambX >= width - 50 && lambHitWall < 3){
            if (lambX <= 50 || lambX >= width - 50){

              lambHitWall += 1;
              sniperAttack(playerAngle(lambX, lambY, x, y), 19);
              //sniperAttack(10 + (playerAngle(lambX, lambY, x, y)), 15);
              //sniperAttack(10 - -1*(playerAngle(lambX, lambY, x, y)));
              sniperAttack(-1*(playerAngle(lambX, lambY, x, y)), 19);
              
              //sniperAttack(20 - (playerAngle(lambX, lambY, x, y)));

              //lambX = (width) - lambX;
              //lambY = (height) - lambY;
              //lambSpeedX = (int) (-16*(Math.cos(Math.toRadians(playerAngle(lambX, lambY, x, y)))));
              //lambSpeedY = (int) (-16*(Math.sin(Math.toRadians(playerAngle(lambX, lambY, x, y)))));
              lambSpeedX = lambSpeedX * -1;
            } //else if(lambY <= 50 || lambY >= height -50 && lambHitWall < 3){
            else if(lambY <= 50 || lambY >= height -50){
              lambHitWall += 1;
              sniperAttack(playerAngle(lambX, lambY, x, y), 19);
              //sniperAttack(10 + (playerAngle(lambX, lambY, x, y)), 15);
              //sniperAttack(10 - -1*(playerAngle(lambX, lambY, x, y)));
              sniperAttack(-1*(playerAngle(lambX, lambY, x, y)), 19);
              //sniperAttack(20 - (playerAngle(lambX, lambY, x, y)));

              //lambX = (width) - lambX;
              //lambY = (height) - lambY;
              //lambSpeedX = (int) (-16*(Math.cos(Math.toRadians(playerAngle(lambX, lambY, x, y)))));
              //lambSpeedY = (int) (-16*(Math.sin(Math.toRadians(playerAngle(lambX, lambY, x, y)))));
              lambSpeedY = lambSpeedY * -1;
            }
          } else{
            if (lambX <= 50 || lambX >= width - 50){
              lambSpeedX = lambSpeedX * -1;
            } //else if(lambY <= 50 || lambY >= height -50 && lambHitWall < 3){
            else if(lambY <= 50 || lambY >= height -50){
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

            lambX = width/2;
            lambY = 50;
            lambSpeedX = 0;
            lambSpeedY = 20;
          }
        }
        else if(bossHealth < maxHealth/2 && bossPhase == 1){
          lambSpeedX = 0;
          lambSpeedY = 0;

          lambX = 800;
          lambY = 500;

          internalFrameCount = 0;

          bossPhase = 2;
        }
        if (bossPhase == 2){
          if(internalFrameCount == 50){
            enemy_i[1] = 2;
            spawnFlies();

          }
          
          /*if(internalFrameCount >= 100 && internalFrameCount <= 125 && internalFrameCount % 5 == 0){
            gridAttack(0, 0);
          }*/
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

          if(internalFrameCount >= 415){
            lambSpeedX = lambSpeedX * 0.90;
            lambSpeedY = lambSpeedY * 0.90;
            
            if (Math.round(lambSpeedX) == 0) {
              lambSpeedX = 0;
            }
            if (Math.round(lambSpeedY) == 0) {
              lambSpeedY = 0;
            }
          }

          if(internalFrameCount >= 365 && internalFrameCount < 370){

          }

          if(internalFrameCount > 450){
            internalFrameCount = 0;
          }


          /*if(internalFrameCount >= 1000 && internalFrameCount % 5 == 0){
            if(enemyMakeTear == true){
              objectLimiter = 750;
              heartAttack();
            }
          }*/
          
        }
      }
    }
    // DEATH GAMESTATE
    if (gameState == 2){

      internalFrameCount = 0;
      
      background(fadeWhite);
      if (fadeWhite > 0){
        fadeWhite -= 1;
      }
      if (fadeWhite == 0){
        image(YOU_DIED, (width/2) - (384/2), (height/2) - (56/2));
      }
    }
  }

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
    } if(keyPressed == true && gameState == 0){
      gameState = 1;
    }
  }
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
/*
    lambSpeedX = (int) (-1*(Math.cos(Math.toRadians(playerAngle(lambX, lambY, x, y)))));
    lambSpeedY = (int) (-1*(Math.sin(Math.toRadians(playerAngle(lambX, lambY, x, y)))));
*/
    lambX = (int) (lambX + lambSpeedX);
    lambY = (int) (lambY + lambSpeedY);
    

    //rect(lambX - 75, lambY - 75, 150, 150);    
    image(img, x - 30, y - 25);
    //image(lamb, lambX - 75, lambY - 75, 150, 150);
    image(the_lamb_frames[(frameCount/5)%intlamb_frames], lambX - 75, lambY - 75, 72 * 2, 69 * 2);

  }
  public void intangibilityEngine(){
    if (intangibility == false){
      if ((x < (lambX + 50)) && (x > lambX - 50) && (y > lambY - 50) && (y < (lambY + 50))){
        wasHit = true;
      }
      for(int enemy_hitboxCheck = 0; enemy_hitboxCheck < objectLimiter; enemy_hitboxCheck += 1){
        if((x < (xEnemyArray[enemy_hitboxCheck][0] + 20)) && (x > (xEnemyArray[enemy_hitboxCheck][0] - 20)) && (y > (yEnemyArray[enemy_hitboxCheck][0] - 20)) && (y < (yEnemyArray[enemy_hitboxCheck][0] + 20))){
          wasHit = true;
        }
      }
    }
    if (intangibility == false && intangibilityTimer == 0 && wasHit == true){
      intangibilityTimer = 120;
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

  public double playerAngle(int xShooter, int yShooter, int xTarget, int yTarget){
    double degree = 0;
    try{
      degree = Math.atan2((yShooter - yTarget),(xShooter - xTarget));
      degree = Math.toDegrees(degree);
      //println(degree);
      //degree = Math.atan((yTarget)/(xTarget));
    }
    catch(ArithmeticException error){
    }
    return degree;
  }

  public void enemyTear(int xPos, int yPos, int angle, int magnitude){
    enemy_i[0] += 1;
    enemyTearExist[enemy_i[0]] = true;
    xEnemyArray[enemy_i[0]][0] = xPos;
    yEnemyArray[enemy_i[0]][0] = yPos;
    xEnemySpeedArray[enemy_i[0]][0] = (magnitude*(Math.cos(Math.toRadians(angle))));
    yEnemySpeedArray[enemy_i[0]][0]= (magnitude*(Math.sin(Math.toRadians(angle))));
    
    //xEnemySpeedArray[enemy_i] = 0;
    //yEnemySpeedArray[enemy_i] = 0;
    //println(enemy_i + " " + xEnemyArray[enemy_i - 1] + " " + yEnemyArray[enemy_i - 1] + " " + xEnemySpeedArray[enemy_i - 1] + " " + yEnemySpeedArray[enemy_i - 1]);
  }


  public void triAttack(){
    angle_i[1] += 10;

    for(int triAngle = 0; triAngle < 360; triAngle += 120){
      enemyTear(lambX, lambY, angle_i[1] + triAngle, 4);
    }
  }
  public void heartAttack(){
    angle_i[2] += 5;

    if(angle_i[2] > 360){
      angle_i[2] = 0;
      enemyMakeTear = false;
    }

    enemyTear(lambX, lambY, angle_i[2], 5);
    enemyTear(lambX, lambY, angle_i[2] + 90, 5);
    enemyTear(lambX, lambY, angle_i[2] + 180, 5);
    enemyTear(lambX, lambY, angle_i[2] + 270, 5);
  }
  public void sniperAttack(double playerAngle, int magnitude){
    indicatorAttack(playerAngle);

    angle_i[3] = (int)playerAngle;
    for(int i = -5; i > -1*magnitude; i -= 1){
      enemyTear(lambX, lambY, angle_i[3], i);
    }
  }
  public void gridAttack(int originX, int originY){
    objectLimiter = 995;
    //try{
      for(int i = originY; i < 7; i += 1){
        enemyTear(0, i*150, 0, 50);
      }
      
   /*} catch(IndexOutOfBoundsException error){

    }*/
  }
  public void indicatorAttack(double playerAngle){
    stroke(0);
    fill(255, 0, 0);
    line(lambX, lambY, x, y);
  }
  public void octaAttack(double angle, int magnitude){
    for(int triAngle = 0; triAngle < 360; triAngle += 45){
      enemyTear(lambX, lambY, (int) angle + triAngle, magnitude);
    }
  }
  public void spawnFlies(){

  }
}