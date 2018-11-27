import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Katch extends GameObject{

private int vx;
private int vy;
private int angle;
private final int R = 2;
private BufferedImage img;
private boolean RightPressed;
private boolean LeftPressed;

    public Katch (int x, int y, int vx, int vy, int angle, BufferedImage img) {

        super(img, x, y);
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;

        }

        void toggleRightPressed() {
        this.RightPressed = true;
        }

        void toggleLeftPressed() {
        this.LeftPressed = true;
        }

        void unToggleRightPressed() {
        this.RightPressed = false;
        }

        void unToggleLeftPressed() {
        this.LeftPressed = false;
        }

    public void update() {

        if (this.RightPressed) {
            this.moveLeft();
        }
        if (this.LeftPressed) {
            this.moveRight();
        }
    }

    private void moveRight() {

        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        checkBorder();
    }

    private void moveLeft() {

        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
    }

    private void checkBorder() {

        if (x < 40) {
        x = 40;
        }
        if (x >= SecondGame.screenWidth - 120) {
        x = SecondGame.screenWidth - 120;
        }
        if (y < 10) {
        y = 10;
        }
        if (y >= SecondGame.screenHeight - 10) {
        y = SecondGame.screenHeight - 10;
        }
    }

    void draw(Graphics g) {

        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
    }
}
