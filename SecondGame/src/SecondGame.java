import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import static javax.imageio.ImageIO.read;

public class SecondGame extends JPanel {

    private JFrame jf;
    public static final int screenWidth = 640, screenHeight = 500;
    private BufferedImage background, world, katchImg, popImg, bigleg;
    private Graphics2D buffer, g2;
    private InputStream blockMap;
    private BufferedReader bufferReader;
    private String input;
    private Image wallImg, blockSolid, blockImg1, blockImg2, blockImg3, blockImg4, blockImg5;
    private ArrayList<Blocks> blocks = new ArrayList<>();
    private Katch katch;
    private KatchControl katchControl;
    private Pop pop;
    private double speed = 0;
    private Rectangle2D.Double popRec;
    private Rectangle2D.Double blocksRec;
    private Rectangle2D.Double katchRec;
    private Rectangle boarderLeft;
    private Rectangle boarderRight;
    private Rectangle boarderTop;
    private int score = 0, totalScore = 0, lives = 3, level = 1, Bigleg = 0;
    private boolean endGame = false, Nextlevel = false;

    private void init () {

        try{
            background = ImageIO.read(new File("Resources/Background1.bmp"));
            wallImg = ImageIO.read(new File("Resources/wall.gif"));
            blockSolid = read(new File("Resources/Block_solid.gif"));
            blockImg1 = read(new File("Resources/Block1.gif"));
            blockImg2 = read(new File("Resources/Block2.gif"));
            blockImg3 = read(new File("Resources/Block6.gif"));
            blockImg4 = read(new File("Resources/Block4.gif"));
            blockImg5 = read(new File("Resources/Block5.gif"));
            katchImg = read(new File("Resources/Katch1.gif"));
            bigleg = read(new File("Resources/Bigleg_small.gif"));
            popImg = read(new File("Resources/Pop1.gif"));
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        this.jf = new JFrame("Super Rainbow Reef");
        this.jf.setLocation(200, 200);
        this.world = new BufferedImage(SecondGame.screenWidth, SecondGame.screenHeight - 20, BufferedImage.TYPE_INT_RGB);
        this.jf.setLayout(new BorderLayout());
        this.jf.add(this);
        this.jf.setSize(background.getWidth(), background.getHeight() + 30);
        this.jf.setResizable(false);
        this.jf.setLocationRelativeTo(null);
        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jf.setVisible(true);

        loadObjects();
    }
    /* loads all of the game objects that need to be reloaded for each level. These need to be
     * separated from the JFrame so that the JFrame doesn't get redrawn. The separation is only
     * needed because when you move the frame from the original spot, it will draw the new level
     * at where the original spot and you will have multiple JFrames displayed. The for loop
     * increases the speed for each level. */
    private void loadObjects() {

        blockMaker();
        katch = new Katch(320-40, 450, 0, 0, 0, katchImg);
        katchControl = new KatchControl(katch, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);
        this.jf.addKeyListener(katchControl);

        double increaseSpeed = 0;

        for (double i = 0; i < level ; i++){
            this.speed = 1.4 +(increaseSpeed * 0.2);
            increaseSpeed++;
        }
        pop = new Pop(320-16, 300, speed, popImg);
    }
    /* draws all of the objects in the game as well as the level, score, lives and speed at the
     * bottom of the screen */
    public void paintComponent(Graphics g) {

        g2 = (Graphics2D) g;
        g2.drawImage(world, 0, 0, this);
        buffer = world.createGraphics();

        g2.setColor(Color.white);
        g2.setFont(new Font("", Font.PLAIN, 20));
        g2.drawString("Level "+ incrementLevel(g2),screenWidth-580,screenHeight-65);
        g2.drawString("Score: "+ getScore(),screenWidth-580,screenHeight-40);
        g2.drawString("lives: "+ getLives(g2),screenWidth-130,screenHeight-40);
        g2.drawString("Speed: "+ pop.getSpeed(),screenWidth-130,screenHeight-65);

        drawBackGround(buffer);
        drawBlocks();

        katch.draw(buffer);
        pop.draw(buffer);
    }
    /* Draws the background for the game */
    public void drawBackGround(Graphics2D buffer) {

        int Width = background.getWidth();
        int Height = background.getHeight();
        buffer.drawImage(background, 0, 0, Width, Height, this);
    }
    /* Draws the coral defense on the screen*/
    public void drawBlocks() {

        if (!blocks.isEmpty()) {

            for (int i = 0; i <= blocks.size() - 1; i++) {
                blocks.get(i).draw(buffer);
            }
        }
    }
    /* loads the coral defense and the Biglegs into the Blocks class. It also loads a different
     * map for each level and keeps track of how many Biglegs are on the screen. for the last
     * level, I needed to and an extra Bigleg because of a bug that wouldn't load the "You Win"
     * screen if the Biglegs == 0. I labeled the extra Bigleg "e" in the text map. */
    public void blockMaker() {

        try{
            if (level == 1){
                blockMap = new FileInputStream("Resources/BlockMap1.txt");
            }
            else if (level == 2){
                blockMap = new FileInputStream("Resources/BlockMap2.txt");
            }
            else if (level == 3){
                blockMap = new FileInputStream("Resources/BlockMap3.txt");
            }
            else if (level == 4){
                blockMap = new FileInputStream("Resources/BlockMap4.txt");
            }
            else if (level == 5){
                blockMap = new FileInputStream("Resources/BlockMap5.txt");
            }
            bufferReader = new BufferedReader(new InputStreamReader(blockMap));
            input = bufferReader.readLine();
            int j = 0;

            while (input != null) {

                for (int i = 0; i < input.length(); i++) {

                    if (input.charAt(i) == 's') {
                        blocks.add(new Blocks(blockSolid, i * blockSolid.getWidth(null),
                                j * blockSolid.getHeight(null), 10));
                    }
                    if (input.charAt(i) == '1') {
                        blocks.add(new Blocks(blockImg1, i * blockSolid.getWidth(null),
                                j * blockSolid.getHeight(null), 1));
                    }
                    if (input.charAt(i) == '2') {
                        blocks.add(new Blocks(blockImg2, i * blockImg2.getWidth(null),
                                j * blockImg2.getHeight(null), 2));
                    }
                    if (input.charAt(i) == '3') {
                        blocks.add(new Blocks(blockImg3, i * blockImg2.getWidth(null),
                                j * blockImg2.getHeight(null), 3));
                    }
                    if (input.charAt(i) == '4') {
                        blocks.add(new Blocks(blockImg4, i * blockImg2.getWidth(null),
                                j * blockImg2.getHeight(null), 4));
                    }
                    if (input.charAt(i) == '5') {
                        blocks.add(new Blocks(blockImg5, i * blockImg2.getWidth(null),
                                j * blockImg2.getHeight(null), 5));
                    }
                    if (input.charAt(i) == 'e') {
                        blocks.add(new Blocks(blockImg5, i * blockImg2.getWidth(null),
                                j * blockImg2.getHeight(null), 5));
                        Bigleg++;
                    }
                    else if (input.charAt(i) == 'b') {
                        blocks.add(new Blocks(bigleg, i * bigleg.getWidth(null),
                                j * blockImg2.getHeight(null), 6));
                        Bigleg++;
                    }
                }
                j++;
                input = bufferReader.readLine();
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getScore() {
        return score + totalScore;
    }
    /* draws the game over screen and decreases lives each time Pop falls off the screen.*/
    public int getLives (Graphics g2) {

        if (pop.getY() > 1000){

            lives--;
            pop.Respawn();
        }
        if (lives == 0) {

            g2.setColor(Color.black);
            g2.setFont(new Font("", Font.PLAIN, 60));
            g2.drawString("Game Over", screenWidth/2-165, screenHeight/2);
            g2.setColor(Color.white);
            g2.setFont(new Font("", Font.PLAIN, 60));
            g2.drawString("Game Over", screenWidth/2-163, screenHeight/2-2);

            g2.setColor(Color.black);
            g2.setFont(new Font("", Font.PLAIN, 40));
            g2.drawString("Your Score is "+getScore(), screenWidth/2-200, screenHeight/2+60);
            g2.setColor(Color.white);
            g2.drawString("Your Score is "+getScore(), screenWidth/2-198, screenHeight/2+58);
            g2.setFont(new Font("", Font.PLAIN, 20));

            endGame = true;
        }
        return lives;
    }

    public boolean isEndGame() {
        return endGame;
    }

    public Rectangle getBoarderLeft(){

        return new Rectangle(0, 0, 43, 500);
    }

    public Rectangle getBoarderRight(){

        return new Rectangle(screenWidth-43, 0, 43, 500);
    }

    public Rectangle getBoarderTop() {

        return new Rectangle(0, 0, 640, 24);
    }
    /* checks all the collisons for the game. When Pop intersects with Katch there are 9 if
     * and if else statements that passes in different angles in which Pop will bounce back up at.
     * It also sets the score for each type of coral defense is hit as well as the Biglegs. */
    public void checkCollisions() {

        popRec = pop.getRectangle();
        katchRec = katch.getRectangle();
        boarderLeft = getBoarderLeft();
        boarderRight = getBoarderRight();
        boarderTop = getBoarderTop();

        if (popRec.intersects(katchRec)) {

            pop.incrementSpeed();

            if ((pop.getY())<(katch.getY())) {

                if (pop.getX() + pop.width/2 < katch.getX() + katch.width/2 - 31) {
                    pop.handleCollisionKatch(205);
                } else if (pop.getX() + pop.width/2 > katch.getX() + katch.width/2 - 31
                        && pop.getX() + pop.width/2 < katch.getX() + katch.width/2 - 22) {
                    pop.handleCollisionKatch(225);
                } else if (pop.getX() + pop.width/2 > katch.getX() + katch.width/2 - 22
                        && pop.getX() + pop.width/2 < katch.getX() + katch.width/2 - 13) {
                    pop.handleCollisionKatch(240);
                } else if (pop.getX() + pop.width/2 > katch.getX() + katch.width/2 - 13
                        && pop.getX() + pop.width/2 < katch.getX() + katch.width/2 - 4) {
                    pop.handleCollisionKatch(255);
                } else if (pop.getX() + pop.width/2 > katch.getX() + katch.width/2 - 4
                        && pop.getX() + pop.width/2 < katch.getX() + katch.width/2 + 4) {
                    pop.handleCollisionKatch(270);
                } else if (pop.getX() + pop.width/2 > katch.getX() + katch.width/2 + 4
                        && pop.getX() + pop.width/2 < katch.getX() + katch.width/2 + 13) {
                    pop.handleCollisionKatch(285);
                } else if (pop.getX() + pop.width/2 > katch.getX() + katch.width/2 + 13
                        && pop.getX() + pop.width/2 < katch.getX() + katch.width/2 + 22) {
                    pop.handleCollisionKatch(300);
                } else if (pop.getX() + pop.width/2 > katch.getX() + katch.width/2 + 22
                        && pop.getX() + pop.width/2 < katch.getX() + katch.width/2 + 31) {
                    pop.handleCollisionKatch(315);
                } else if (pop.getX() + pop.width/2 > katch.getX() + katch.width/2 + 31) {
                    pop.handleCollisionKatch(335);
                }
            }
            else
                pop.handleCollisionX();
        }
        if (popRec.intersects(boarderLeft) || popRec.intersects(boarderRight)) {
            pop.handleCollisionX();
        }
        if (popRec.intersects(boarderTop)) {
            pop.handleCollisionY();
        }
        for (int i = 0; i <= blocks.size() - 1; i++) {
            blocksRec = blocks.get(i).getRectangle();

            if (popRec.intersects(blocksRec)&&!((blocks.get(i).getType())==10)) {
                pop.handleCollisionY();

                if (blocks.get(i).getType() == 6) {
                    Bigleg--;
                    score += 2500;
                }
                if (blocks.get(i).getType() == 1) {
                    score += 100;
                }
                if (blocks.get(i).getType() == 2) {
                    score += 200;
                }
                if (blocks.get(i).getType() == 3) {
                    score += 500;
                }
                if (blocks.get(i).getType() == 4) {
                    score += 800;
                }
                if (blocks.get(i).getType() == 5) {
                    score += 1000;
                }
                if (!(blocks.get(i).getType() == 10)) {
                blocks.remove(i);
                }
            }
        }
    }
    /* increments the levels and draws the "You Win" screen with the score*/
    public int incrementLevel (Graphics2D g2) {

        if ( Bigleg == 0) {

            blocks.clear();
            level++;
            Nextlevel = true;
        }
        if ((level == 5||level == 6) && Bigleg == 1) {

            g2.setColor(Color.black);
            g2.setFont(new Font("", Font.PLAIN, 60));
            g2.drawString("You Win!", screenWidth/2-125, screenHeight/2);
            g2.setColor(Color.white);
            g2.setFont(new Font("", Font.PLAIN, 60));
            g2.drawString("You Win!", screenWidth/2-123, screenHeight/2-2);

            g2.setColor(Color.black);
            g2.setFont(new Font("", Font.PLAIN, 40));
            g2.drawString("Your Score is "+getScore(), screenWidth/2-200, screenHeight/2+60);
            g2.setColor(Color.white);
            g2.drawString("Your Score is "+getScore(), screenWidth/2-198, screenHeight/2+58);
            g2.setFont(new Font("", Font.PLAIN, 20));

            endGame = true;
        }
        return level;
    }
    public static void main(String[] args) {

        SecondGame SG = new SecondGame();
        SG.init();

        try {
            while (!SG.isEndGame()) {

                    SG.katch.update();
                    SG.pop.update();
                    SG.checkCollisions();
                    SG.repaint();
                    Thread.sleep(1000 / 144);

                    if (SG.Nextlevel == true) {

                        SG.loadObjects();
                        SG.Nextlevel = false;
                }
            }
        }
        catch (InterruptedException ignored) {
        }
    }
}