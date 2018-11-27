import java.awt.*;

public class Blocks extends GameObject {

    private int x, y, height, width;
    private int blockType;

    public Blocks (Image img, int x, int y, int type)  {

        super(img, x, y);
        this.x = x;
        this.y = y;
        this.blockType = type;
        this.width = img.getWidth(null);
        this.height = img.getHeight(null);
    }

    public Rectangle getRectangle () {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D g) {
        g.drawImage(img, x, y, null);
    }
}
