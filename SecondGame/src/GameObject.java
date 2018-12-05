import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class GameObject {

    protected double x;
    protected double y;
    protected double  height;
    protected double width;
    protected Image img;

    public GameObject(Image img, double x, double y) {

        this.img = img;
        this.x = x;
        this.y = y;
        this.height = img.getHeight(null);
        this.width = img.getWidth(null);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

}
