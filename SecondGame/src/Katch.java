import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Katch extends GameObject{

private double vx;
private double vy;
private double angle;
private final double R = 2;
private BufferedImage img;
private boolean RightPressed;
private boolean LeftPressed;

    public Katch (double x, double y, double vx, double vy, double angle, BufferedImage img) {

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

        vx = (R * Math.cos(Math.toRadians(angle)));
        vy = (R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        checkBorder();
    }

    private void moveLeft() {

        vx = (R * Math.cos(Math.toRadians(angle)));
        vy = (R * Math.sin(Math.toRadians(angle)));
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

    public Rectangle2D.Double getRectangle () {

        return new Rectangle2D.Double(x, y, width, height);
    }

    public void draw(Graphics2D g) {

        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        g.drawImage(this.img, rotation, null);
    }
}
