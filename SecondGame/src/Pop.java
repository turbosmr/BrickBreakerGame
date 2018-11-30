import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Pop extends GameObject {

    private double angle = 90, moveXDirection, moveYDirection, speed, directionX = 1, directionY = 1;
    private double respawnX, respawnY, respawnAngle;

    public Pop (double x, double y, BufferedImage img) {

        super (img, x, y);
        speed = 1.5;
        respawnX = x;
        respawnY = y;
        respawnAngle = angle;
    }

    public void update() {

        moveXDirection = speed * Math.cos(Math.toRadians(angle));
        moveYDirection = speed * Math.sin(Math.toRadians(angle));

        x += directionX * moveXDirection;
        y += directionY * moveYDirection;
    }

    public Rectangle2D.Double getRectangle () {

        return new Rectangle2D.Double(x, y, width, height);
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

    public void Respawn() {

        x = respawnX;
        y = respawnY;
        angle = respawnAngle;
    }

    public void handleCollisionKatch(double angle){

        Rectangle2D.Double popRec = new Rectangle2D.Double();
        directionY = 1;
        directionX = 1;
        this.angle = angle;
    }

    public void draw(Graphics2D g) {

        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        g.drawImage(this.img, rotation, null);
    }
}
