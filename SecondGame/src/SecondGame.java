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
    private Image wallImg, blockSolid, blockImg1, blockImg2, blockImg3, blockImg4;
    private ArrayList<Blocks> blocks = new ArrayList<>();
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private Katch katch;
    private KatchControl katchControl;
    private Pop pop;
    private double speed = 1.5;
    private Rectangle2D.Double popRec;
    private Rectangle2D.Double blocksRec;
    private Rectangle2D.Double katchRec;
    private Rectangle boarderLeft;
    private Rectangle boarderRight;
    private Rectangle boarderTop;
    private int score = 0, totalScore = 0, lives = 3, level = 1, Bigleg = 0;
    private boolean endGame = false, Nextlevel = false, winGame = false;

    private void init () {

        try{
            background = ImageIO.read(new File("Resources/Background1.bmp"));
            wallImg = ImageIO.read(new File("Resources/wall.gif"));
            blockSolid = read(new File("Resources/Block_solid.gif"));
            blockImg1 = read(new File("Resources/Block1.gif"));
            blockImg2 = read(new File("Resources/Block2.gif"));
            blockImg3 = read(new File("Resources/Block3.gif"));
            blockImg4 = read(new File("Resources/Block3.gif"));
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

    private void loadObjects() {

        blockMaker();
        katch = new Katch(320-40, 450, 0, 0, 0, katchImg);
        katchControl = new KatchControl(katch, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);
        this.jf.addKeyListener(katchControl);
        pop = new Pop(320-16, 300, speed, popImg);
    }

    public void paintComponent(Graphics g) {

        g2 = (Graphics2D) g;
        g2.drawImage(world, 0, 0, this);
        buffer = world.createGraphics();

        g2.setColor(Color.white);
        g2.setFont(new Font("", Font.PLAIN, 20));
        g2.drawString("Level "+ level,screenWidth-580,screenHeight-65);
        g2.drawString("Score: "+ getScore(),screenWidth-580,screenHeight-40);
        g2.drawString("lives: "+ getLives(),screenWidth-125,screenHeight-40);

        drawBackGround(buffer);
        drawBlocks();

        katch.draw(buffer);
        pop.draw(buffer);

        incrementLevel();
    }

    public void drawBackGround(Graphics2D buffer) {

        int Width = background.getWidth();
        int Height = background.getHeight();
        buffer.drawImage(background, 0, 0, Width, Height, this);
    }

    public void drawBlocks() {

        if (!blocks.isEmpty()) {

            for (int i = 0; i <= blocks.size() - 1; i++) {
                blocks.get(i).draw(buffer);
            }
        }
    }

    public void blockMaker() {

        try{
            if (level == 1){
                blockMap = new FileInputStream("Resources/BlockMap.txt");
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

                    if (input.charAt(i) == '1') {
                        blocks.add(new Blocks(blockSolid, i * blockSolid.getWidth(null),
                                j * blockSolid.getHeight(null), 2));
                    }
                    if (input.charAt(i) == '2') {
                        blocks.add(new Blocks(blockImg1, i * blockSolid.getWidth(null),
                                j * blockSolid.getHeight(null), 1));
                    }
                    if (input.charAt(i) == '3') {
                        blocks.add(new Blocks(blockImg2, i * blockImg2.getWidth(null),
                                j * blockImg2.getHeight(null), 1));
                    }
                    if (input.charAt(i) == '4') {
                        blocks.add(new Blocks(blockImg3, i * blockImg2.getWidth(null),
                                j * blockImg2.getHeight(null), 1));
                    }
                    if (input.charAt(i) == '6') {
                        blocks.add(new Blocks(blockImg3, i * blockImg2.getWidth(null),
                                j * blockImg2.getHeight(null), 1));
                        Bigleg++;
                    }
                    else if (input.charAt(i) == '5') {
                        blocks.add(new Blocks(bigleg, i * bigleg.getWidth(null),
                                j * blockImg2.getHeight(null), 3));
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

    public int getLives () {

        if (pop.getY() > 1000){

            lives--;
            pop.Respawn();
        }
        if (lives == 0) {

            g2.setColor(Color.black);
            g2.setFont(new Font("", Font.PLAIN, 60));
            g2.drawString("Game Over", screenWidth/2-150, screenHeight/2);
            g2.setColor(Color.white);
            g2.setFont(new Font("", Font.PLAIN, 60));
            g2.drawString("Game Over", screenWidth/2-148, screenHeight/2-2);
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

    public void checkCollisions() {

        popRec = pop.getRectangle();
        katchRec = katch.getRectangle();
        boarderLeft = getBoarderLeft();
        boarderRight = getBoarderRight();
        boarderTop = getBoarderTop();

        if (popRec.intersects(katchRec)) {

            if ((pop.getY())<(katch.getY())) {

                System.out.println("pop"+pop.getY());
                System.out.println("katch"+(katch.getY()));

                if (pop.getX() + pop.width / 2 < katch.getX() + katch.width / 2 - 31) {
                    pop.handleCollisionKatch(205);
                } else if (pop.getX() + pop.width / 2 > katch.getX() + katch.width / 2 - 31
                        && pop.getX() + pop.width / 2 < katch.getX() + katch.width / 2 - 22) {
                    pop.handleCollisionKatch(225);
                } else if (pop.getX() + pop.width / 2 > katch.getX() + katch.width / 2 - 22
                        && pop.getX() + pop.width / 2 < katch.getX() + katch.width / 2 - 13) {
                    pop.handleCollisionKatch(240);
                } else if (pop.getX() + pop.width / 2 > katch.getX() + katch.width / 2 - 13
                        && pop.getX() + pop.width / 2 < katch.getX() + katch.width / 2 - 4) {
                    pop.handleCollisionKatch(255);
                } else if (pop.getX() + pop.width / 2 > katch.getX() + katch.width / 2 - 4
                        && pop.getX() + pop.width / 2 < katch.getX() + katch.width / 2 + 4) {
                    pop.handleCollisionKatch(270);
                } else if (pop.getX() + pop.width / 2 > katch.getX() + katch.width / 2 + 4
                        && pop.getX() + pop.width / 2 < katch.getX() + katch.width / 2 + 13) {
                    pop.handleCollisionKatch(285);
                } else if (pop.getX() + pop.width / 2 > katch.getX() + katch.width / 2 + 13
                        && pop.getX() + pop.width / 2 < katch.getX() + katch.width / 2 + 22) {
                    pop.handleCollisionKatch(300);
                } else if (pop.getX() + pop.width / 2 > katch.getX() + katch.width / 2 + 22
                        && pop.getX() + pop.width / 2 < katch.getX() + katch.width / 2 + 31) {
                    pop.handleCollisionKatch(315);
                } else if (pop.getX() + pop.width / 2 > katch.getX() + katch.width / 2 + 31) {
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

            if (popRec.intersects(blocksRec)) {
                pop.handleCollisionY();

                if (blocks.get(i).getType() == 3) {
                    Bigleg--;
                    System.out.println(Bigleg);
                }
                if (!(blocks.get(i).getType() == 2)) {
                blocks.remove(i);
                score += 100;
                }
            }
        }
    }

    public void incrementLevel () {

        if ( Bigleg == 0) {
            level++;
            Nextlevel = true;
            this.speed += 0.3;
            blocks.clear();
        }
        if (level == 5 && Bigleg == 1) {

            g2.setColor(Color.black);
            g2.setFont(new Font("", Font.PLAIN, 60));
            g2.drawString("You Win!", screenWidth/2-125, screenHeight/2);
            g2.setColor(Color.white);
            g2.setFont(new Font("", Font.PLAIN, 60));
            g2.drawString("You Win!", screenWidth/2-123, screenHeight/2-2);
            g2.setFont(new Font("", Font.PLAIN, 20));
            endGame = true;
        }
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