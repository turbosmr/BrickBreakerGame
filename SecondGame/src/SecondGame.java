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
    private InputStream blockMap, blockMap2;
    private BufferedReader bufferReader;
    private String input;
    private Image wallImg, blockSolid, blockImg1, blockImg2, blockImg3;
    private ArrayList<Blocks> blocks = new ArrayList<>();
    private Katch katch;
    private KatchControl katchControl;
    private Pop pop;
    private Rectangle2D.Double popRec;
    private Rectangle2D.Double blocksRec;
    private Rectangle2D.Double katchRec;
    private Rectangle boarderLeft;
    private Rectangle boarderRight;
    private Rectangle boarderTop;
    private int score = 0, totalScore = 0, lives = 3, level = 3;
    private boolean endGame = false;

    public void init() {

        this.jf = new JFrame("Super Rainbow Reef");
        this.jf.setLocation(200, 200);
        this.world = new BufferedImage(SecondGame.screenWidth, SecondGame.screenHeight-20, BufferedImage.TYPE_INT_RGB);

        try{
            background = ImageIO.read(new File("Resources/Background1.bmp"));
            wallImg = ImageIO.read(new File("Resources/wall.gif"));
            blockSolid = read(new File("Resources/Block_solid.gif"));
            blockImg1 = read(new File("Resources/Block1.gif"));
            blockImg2 = read(new File("Resources/Block2.gif"));
            blockImg3 = read(new File("Resources/Block3.gif"));
            katchImg = read(new File("Resources/Katch1.gif"));
            bigleg = read(new File("Resources/Bigleg_small.gif"));
            popImg = read(new File("Resources/Pop1.gif"));
            blockMap = new FileInputStream("Resources/BlockMap.txt");
            blockMap2 = new FileInputStream("Resources/BlockMap2.txt");
            bufferReader = new BufferedReader(new InputStreamReader(blockMap));
            input = bufferReader.readLine();
            blockMaker();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        katch = new Katch(320-40, 450, 0, 0, 0, katchImg);
        katchControl = new KatchControl(katch, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);
        pop = new Pop(320-16, 250, popImg);

        this.jf.setLayout(new BorderLayout());
        this.jf.add(this);
        this.jf.addKeyListener(katchControl);
        this.jf.setSize(background.getWidth(), background.getHeight()+30);
        this.jf.setResizable(false);
        this.jf.setLocationRelativeTo(null);
        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jf.setVisible(true);
    }

    public void paintComponent(Graphics g) {

        g2 = (Graphics2D) g;
        g2.drawImage(world, 0, 0, this);
        buffer = world.createGraphics();

        g2.setColor(Color.white);
        g2.setFont(new Font("", Font.PLAIN, 20));
        g2.drawString("Score: "+ getScore(),screenWidth-580,screenHeight-40);
        g2.drawString("lives: "+ getLives(),screenWidth-150,screenHeight-40);

        drawBackGround(buffer);
        drawBlocks();

        katch.draw(buffer);
        pop.draw(buffer);
    }

    public void drawBackGround(Graphics2D buffer) {

        int Width = background.getWidth();
        int Height = background.getHeight();

        buffer.drawImage(background, 0, 0, Width, Height, this);
    }

    public void blockMaker() {

        int j = 0;
        try {
            while (input != null) {

                for (int i = 0; i < input.length(); i++) {

                    if (input.charAt(i) == '1') {
                        blocks.add(new Blocks(blockSolid, i * blockSolid.getWidth(null),
                                j * blockSolid.getHeight(null), 1));
                    }
                    if (input.charAt(i) == '2') {
                        blocks.add(new Blocks(blockImg1, i * blockSolid.getWidth(null),
                                j * blockSolid.getHeight(null), 2));
                    }
                    if (input.charAt(i) == '3') {
                        blocks.add(new Blocks(blockImg2, i * blockImg2.getWidth(null),
                                j * blockImg2.getHeight(null), 2));
                    }
                    else if (input.charAt(i) == '4') {
                        blocks.add(new Blocks(bigleg, i * bigleg.getWidth(null),
                                j * blockImg2.getHeight(null), 3));
                    }
                }
                j++;
                input = bufferReader.readLine();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void drawBlocks() {

        if (!blocks.isEmpty()) {

            for (int i = 0; i <= blocks.size() - 1; i++) {
                blocks.get(i).draw(buffer);
            }
        }
    }

    public int getScore() {

        return score + totalScore;
    }
    public int getLives () {

        if (pop.getY() > 630){

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

    public Rectangle GetBoarderLeft(){

        return new Rectangle(0, 0, 42, 500);
    }
    public Rectangle GetBoarderRight(){

        return new Rectangle(screenWidth-42, 0, 42, 500);
    }
    public Rectangle getBoarderTop() {

        return new Rectangle(0, 0, 640, 24);
    }

    public void checkCollisions() {

        popRec = pop.getRectangle();
        katchRec = katch.getRectangle();
        boarderLeft = GetBoarderLeft();
        boarderRight = GetBoarderRight();
        boarderTop = getBoarderTop();

        if (popRec.intersects(katchRec.x, katchRec.y, katchRec.width, katchRec.height)){

            if (pop.getX()+ pop.width/2 < katch.getX() + katch.width/2-24) {
                pop.handleCollisionKatch(225);
            }
            else if (pop.getX()+ pop.width/2 > katch.getX() + katch.width/2-24
                    && pop.getX()+ pop.width/2 < katch.getX() + katch.width/2-8) {
                         pop.handleCollisionKatch(255);
            }
            else if (pop.getX()+ pop.width/2 > katch.getX() + katch.width/2+8
                    && pop.getX()+ pop.width/2 < katch.getX() + katch.width/2+24) {
                         pop.handleCollisionKatch(285);
            }
            else if (pop.getX()+ pop.width/2 > katch.getX() + katch.width/2+15) {
                pop.handleCollisionKatch(305);
            }
            else
                pop.handleCollisionKatch(270);
        }

        if (popRec.intersects(boarderLeft.x, boarderLeft.y, boarderLeft.width, boarderLeft.height)||
            popRec.intersects(boarderRight.x, boarderRight.y, boarderRight.width, boarderRight.height)) {

            pop.handleCollisionX();
        }
        if (popRec.intersects(boarderTop)) {
            pop.handleCollisionY();
        }

        for (int i = 0; i <= blocks.size()-1; i++) {
            blocksRec = blocks.get(i).getRectangle();

            if (popRec.intersects(blocksRec)) {

                pop.handleCollisionY();

                //if (blocks.get(i).getType() == 2) {
                blocks.remove(i);
                score += 100;
                level--;
            }
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
            }
        }
        catch (InterruptedException ignored) {
        }
    }
}