import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Pop extends GameObject {

    private int angle = 0, moveXDirection, moveYDirection, speed;
    private boolean collides;

    public Pop (int x, int y, BufferedImage img) {

        super (img, x, y);

        this.angle = angle;
        speed = 1;
        moveXDirection = (int) Math.round(speed * Math.cos(Math.toRadians(angle)));
        moveYDirection = (int) Math.round(speed * Math.sin(Math.toRadians(angle)));
    }

    public void update() {

        moveXDirection = (int) Math.round(speed * Math.cos(Math.toRadians(angle)));
        moveYDirection = (int) Math.round(speed * Math.sin(Math.toRadians(angle)));

        x += moveXDirection;
        y += moveYDirection;
    }

    public Rectangle getRectangle () {

        return new Rectangle(x, y, width, height);
    }

    public void handleCollision(){

        System.out.println("hit");
        this.angle =+ 180;
    }

    public void draw(Graphics2D g2) {

        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), 0, 0);
        g2.drawImage(this.img, rotation, null);
    }
}
