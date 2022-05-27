import processing.core.PApplet;
import processing.core.PImage;

public class Sketch extends PApplet {
  int width = 1600;
  int height = 800;
  
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

  int lambX = width/2;
  int lambY = 200;

  int i = 0;
  int enemy_i = 0;

  int[] xArray = new int[200];
  int[] yArray = new int[200];
  
  int[] xSpeedArray = new int[200];
  int[] ySpeedArray = new int[200];

  double[] xEnemyArray = new double[1000];
  double[] yEnemyArray = new double[1000];
  
  double[] xEnemySpeedArray = new double[1000];
  double[] yEnemySpeedArray = new double[1000];

  int[] angle_i = new int[10];

  int tearDelay = 0;

  boolean intangibility = false;
  int intangibilityTimer = 0;
  boolean wasHit = false;
  float life = 3; 

  int objectLimiter = 255;
  
  int internalFrameCount = 0;
  public void settings() {
    size(width, height);
  }
  public void setup() {
    background(255, 255, 255);
    img = loadImage("010.01.00.png");
//    background_room = loadImage("19-F08-07b-19.jpg");  
    heart = loadImage("PlayerHeart2.png");
    lamb = loadImage("273.10.00.png");
  }

  public void draw() {
    //background(background_room);
    background(255, 255, 255);

    if (intangibility == true){
      img = loadImage("010.01.00 2.png");
    } else if (intangibility == false){
      img = loadImage("010.01.00.png");
    }

    for(int displayHearts = 0; displayHearts < life; displayHearts ++){
      image(heart, (displayHearts*25) + 25, 45);
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
    if(enemy_i > objectLimiter){
      enemy_i = 0;
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

        xArray[i] = x;
        yArray[i] = y;

        xSpeedArray[i + 1] = 0;
        ySpeedArray[i + 1] = 0;
        tearDelay = 20;
      }
    }
    if (tearDelay > 0){
      tearDelay -= 1;
    }
 
    for (int f = 0; f <= 197; f ++){
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

      
      fill(190,190,255);
      ellipse(xArray[f], yArray[f], 15, 15);

    }
    
    for (int f = 0; f <= objectLimiter; f ++){
      xEnemyArray[f] = xEnemyArray[f] + xEnemySpeedArray[f];
      yEnemyArray[f] = yEnemyArray[f] + yEnemySpeedArray[f];

      fill(255,190,190);
      ellipse((float)xEnemyArray[f], (float)yEnemyArray[f], 25, 25);
    }

    //movement engine
    playerMovementEngine();  
    intangibilityEngine();

    

    //println(playerAngle((lambX), (lambY), x - 30, y - 25));
    //println(angle_i);
    //println(tearUpPressed + " " + tearDownPressed + " " + tearLeftPressed + " " + tearRightPressed);
    //println(frameCount + " " + angle_i);
    //enemyTear(lambX, lambY, (int) playerAngle((lambX), (lambY), x, y), -5);
    internalFrameCount += 1;
    if(enemyMakeTear == true){
      if(internalFrameCount >= 30 && internalFrameCount < 35){
        triAttack();
      }
      if(internalFrameCount >= 90 && internalFrameCount < 95){
        triAttack();
      }
      if(internalFrameCount >= 150 && internalFrameCount < 155){
        triAttack();
      }
      /*
      if(internalFrameCount == 350){
        sniperAttack(playerAngle(lambX, lambY, x, y));
      }
      if(internalFrameCount == 400){
        sniperAttack(playerAngle(lambX, lambY, x, y));
      }
      if(internalFrameCount == 450){
        sniperAttack(playerAngle(lambX, lambY, x, y));
      }
*/


      
      if(internalFrameCount >= 180){
        objectLimiter = 750;
        heartAttack();
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
      y_speed -= 3.5;
    }
    if (downPressed) {
      y_speed += 3.5;
    }
    if (leftPressed) {
      x_speed += 3.5;
    }
    if (rightPressed) {
      x_speed -= 3.5;
    }

    // Check for bouncing
    if ((x >= width) || (x <= 0)) {
      x_speed = x_speed * -1.2;
    }
    if ((y >= height) || (y <= 0)) {
      y_speed = y_speed * -1.2;
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

    //rect(lambX - 75, lambY - 75, 150, 150);    
    image(img, x - 30, y - 25);
    image(lamb, lambX - 75, lambY - 75, 150, 150);
  }
  public void intangibilityEngine(){
    if (intangibility == false){
      if ((x < (lambX + 50)) && (x > lambX - 50) && (y > lambY - 50) && (y < (lambY + 50))){
        wasHit = true;
      }
      for(int enemy_hitboxCheck = 0; enemy_hitboxCheck < objectLimiter; enemy_hitboxCheck += 1){
        if((x < (xEnemyArray[enemy_hitboxCheck] + 15)) && (x > (xEnemyArray[enemy_hitboxCheck] - 15)) && (y > (yEnemyArray[enemy_hitboxCheck] - 15)) && (y < (yEnemyArray[enemy_hitboxCheck] + 15))){
        //if(x == xEnemyArray[enemy_hitboxCheck] && y == yEnemyArray[enemy_hitboxCheck]){
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
      println(degree);
      //degree = Math.atan((yTarget)/(xTarget));
    }
    catch(ArithmeticException error){
    }
    return degree;
  }

  public void enemyTear(int xPos, int yPos, int angle, int magnitude){
    enemy_i += 1;
    xEnemyArray[enemy_i] = xPos;
    yEnemyArray[enemy_i] = yPos;
    xEnemySpeedArray[enemy_i] = (magnitude*(Math.cos(Math.toRadians(angle))));
    yEnemySpeedArray[enemy_i] = (magnitude*(Math.sin(Math.toRadians(angle))));
    
    //xEnemySpeedArray[enemy_i] = 0;
    //yEnemySpeedArray[enemy_i] = 0;
    //println(enemy_i + " " + xEnemyArray[enemy_i - 1] + " " + yEnemyArray[enemy_i - 1] + " " + xEnemySpeedArray[enemy_i - 1] + " " + yEnemySpeedArray[enemy_i - 1]);
  }
  public void triAttack(){
    angle_i[1] += 10;

    if(angle_i[1] > 360){
      angle_i[1] = 0;
      makeTear = false;
    }
    for(int triAngle = 0; triAngle < 360; triAngle += 120){
      enemyTear(lambX, lambY, angle_i[1] + triAngle, 4);
    }
  }
  public void heartAttack(){
    angle_i[2] += 2;

    if(angle_i[2] > 360){
      angle_i[2] = 0;
      enemyMakeTear = false;
    }

    enemyTear(lambX, lambY, angle_i[2], 5);
    enemyTear(lambX, lambY, angle_i[2] + 90, 5);
    enemyTear(lambX, lambY, angle_i[2] + 180, 5);
    enemyTear(lambX, lambY, angle_i[2] + 270, 5);
  }
  public void sniperAttack(double playerAngle){

    fill(255, 0, 0);
    line(lambX, lambY, x, y);

    angle_i[3] = (int)playerAngle;
    for(int i = -5; i > -20; i -= 1){
      enemyTear(lambX, lambY, angle_i[3], i);
    }
  }
}