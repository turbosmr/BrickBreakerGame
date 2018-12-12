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
    /* Creates the rectangle for collisions. the Rectangle2D method gives a more precised rectangle by
     * using doubles */
    public Rectangle2D.Double getRectangle () {
        return new Rectangle2D.Double(x, y, width, height);
    }
    /* draws the coral defence and Biglegs on the screen. The AffineTransformation is needed to use the
     * doubles for precision. */
    public void draw(Graphics2D g) {

        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        g.drawImage(this.img, rotation, null);
    }
}
