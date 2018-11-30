import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Pop extends GameObject {

    private double angle = 90, moveXDirection, moveYDirection, speed, directionX = 1, directionY = 1;

    public Pop (int x, int y, BufferedImage img) {

        super (img, x, y);
        speed = 1;
    }

    public void update() {

        moveXDirection = (double) Math.round(speed * Math.cos(Math.toRadians(angle)));
        moveYDirection = (double) Math.round(speed * Math.sin(Math.toRadians(angle)));

        x += directionX * moveXDirection;
        y += directionY * moveYDirection;
    }

    public Rectangle getRectangle () {

        return new Rectangle(x, y, width, height);
    }

    public void reverseX () {

        directionX = directionX * -1;

    }

    public void reverseY () {

         directionY = directionY * -1;

    }
    public double getAngle () {

        return this.angle;
    }

    public void handleCollisionY() {

        reverseY();
    }

    public void handleCollisionX() {

        reverseX();
    }

    public void handleCollisionKatch(double angle){

        Rectangle2D.Double popRec = new Rectangle2D.Double();
        directionY = 1;
        directionX = 1;
        this.angle = angle;
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(img, x, y, null);
    }
}
