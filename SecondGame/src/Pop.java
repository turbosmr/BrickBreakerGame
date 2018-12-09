import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Pop extends GameObject {

    private double angle, moveXDirection, moveYDirection, speed, directionX = 1, directionY = -1;
    private double respawnX, respawnY, respawnAngle;

    public Pop (double x, double y, double speed, BufferedImage img) {

        super (img, x, y);
        this.speed = speed;
        this.angle = 270;
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

    public void incrementSpeed(){
        this.speed += .01;
    }

    public double getSpeed () {
        return speed;
    }

    public void reverseX () {
        directionX = directionX * -1;

    }

    public void reverseY () {
        directionY = directionY * -1;

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

        directionY = 1;
        directionX = 1;
        this.angle = angle;
    }

    public Rectangle2D.Double getRectangle () {
        return new Rectangle2D.Double(x, y, width, height);
    }

    public void draw(Graphics2D g) {

        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        g.drawImage(this.img, rotation, null);
    }
}
