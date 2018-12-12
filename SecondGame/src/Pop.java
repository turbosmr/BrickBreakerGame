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
        this.speed += 0.01;
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
    /* handles the collision for when pop bounces off of the top of the screen and calls the
     * reverse method to reverse Pop's direction. */
    public void handleCollisionY() {
        reverseY();
    }
    /* handles the collision for when pop bounces off of the sides of the screen and calls the
     * reverse method to reverse Pop's direction. */
    public void handleCollisionX() {
        reverseX();
    }

    public void Respawn() {

        x = respawnX;
        y = respawnY;
        angle = respawnAngle;
    }
    /* handles the collision for Pop when he bounces off of Katch. */
    public void handleCollisionKatch(double angle){

        directionY = 1;
        directionX = 1;
        this.angle = angle;
    }
    /* Creates the rectangle for collisions. the Rectangle2D method gives a more precised rectangle by
     * using doubles */
    public Rectangle2D.Double getRectangle () {
        return new Rectangle2D.Double(x, y, width, height);
    }
    /* draws Pop on the screen. The AffineTransformation is needed to use the doubles for precision */
    public void draw(Graphics2D g) {

        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        g.drawImage(this.img, rotation, null);
    }
}
