import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class Blocks extends GameObject {

    private double height, width;
    private double blockType;

    public Blocks (Image img, double x, double y, double type)  {

        super(img, x, y);
        this.blockType = type;
        this.width = img.getWidth(null);
        this.height = img.getHeight(null);
    }

    public double getType() {
        return this.blockType;
    }

    public Rectangle2D.Double getRectangle () {
        return new Rectangle2D.Double(x, y, width, height);
    }

    public void draw(Graphics2D g) {

        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        g.drawImage(this.img, rotation, null);
    }
}
