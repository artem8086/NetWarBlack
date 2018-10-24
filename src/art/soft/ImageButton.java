package art.soft;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author Артём Святоха
 */
public class ImageButton extends IButton {
    
    public Image press;

    public ImageButton(Image i, Image p, int x, int y, int w, int h, int k) {
        super(i, x, y, w, h, k);
        press = p;
    }

    @Override
    public boolean draw(Graphics g) {
        int x = x1 < 0 ? Game.w + x1 : x1;
        int y = y1 < 0 ? Game.h + y1 : y1;
        g.drawImage(pressed ? press : im, x, y, null);
        postDraw(g, x, y);
        if (pressed2) {
            pressed2 = false;
            return true;
        }
        return false;
    }
}
