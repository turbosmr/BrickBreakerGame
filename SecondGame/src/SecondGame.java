import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
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
    private Image wallImg, blockSolid, blockImg1, blockImg2, blockImg3;
    private ArrayList<Blocks> blocks = new ArrayList<>();
    private Katch katch;
    private KatchControl katchControl;
    private Pop pop;
    private Rectangle popRec, blocksRec, katchRec, boarderLeft, boarderRight, boarderTop;
    private int score = 0, totalScore = 0;

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
            bufferReader = new BufferedReader(new InputStreamReader(blockMap));
            input = bufferReader.readLine();
            blockMaker();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        katch = new Katch(320-40, 450, 0, 0, 0, katchImg);
        katchControl = new KatchControl(katch, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);

        pop = new Pop(320-16, 300, popImg);

        this.jf.setLayout(new BorderLayout());
        this.jf.add(this);
        this.jf.addKeyListener(katchControl);
        this.jf.setSize(background.getWidth(), background.getHeight()+30);
        this.jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jf.setVisible(true);
    }

    public void paintComponent(Graphics g) {

        g2 = (Graphics2D) g;
        g2.drawImage(world, 0, 0, this);
        buffer = world.createGraphics();

        drawBackGround(buffer);
        drawBlocks();
        katch.draw(buffer);
        pop.draw(buffer);

        g2.setColor(Color.white);
        g2.setFont(new Font("", Font.PLAIN, 20));
        g2.drawString("Score: "+ getScore(),screenWidth-580,screenHeight-40);
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
                                j * blockSolid.getHeight(null), 1));
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

        if (popRec.intersects(katchRec)){

            if (pop.getX()+ pop.width/2 < katch.getX() + katch.width/2-15) {
                pop.handleCollisionKatch(240);
            }
            else if (pop.getX()+ pop.width/2 > katch.getX() + katch.width/2+15) {
                pop.handleCollisionKatch(300);
            }
            else
                pop.handleCollisionKatch(270);
        }

        if (popRec.intersects(boarderLeft)||
            popRec.intersects(boarderRight)) {

            pop.handleCollisionX();
        }
        if (popRec.intersects(boarderTop)){
            pop.handleCollisionY();
        }

        for (int i = 0; i <= blocks.size()-1; i++) {
            blocksRec = blocks.get(i).getRectangle();

            if (popRec.intersects(blocksRec)) {
                
                blocks.remove(i);
                score += 100;
                pop.handleCollisionY();
            }
        }
    }
    public static void main(String[] args) {

        SecondGame SG = new SecondGame();
        SG.init();

        try {
            while (true) {

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