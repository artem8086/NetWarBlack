package art.soft;

import java.awt.*;
import java.awt.event.*;

class IButton {

    public int x1, y1, w, h, key;
    public Image im;
    public boolean pressed, pressed2;
    public static int xm, ym;

    IButton(Image i, int x, int y, int w, int h, int k) {
        x1 = x;
        y1 = y;
        key = k;
        im = i;
        this.w = w;
        this.h = h;
    }

    public boolean draw(Graphics g) {
        int x = x1 < 0 ? Game.w + x1 : x1;
        int y = y1 < 0 ? Game.h + y1 : y1;
        if (im != null) {
            int d = pressed ? 4 : 0;
            g.setColor(new Color(0, 0, 0, 0x55));
            g.fillOval(x + 4, y + 4, w, h);
            g.drawImage(im, x + d, y + d, null);
        }
        postDraw(g, x, y);
        if (pressed2) {
            pressed2 = false;
            return true;
        }
        return false;
    }

    public void postDraw(Graphics g, int x, int y) {
    }

    public boolean mousePressed(MouseEvent e) {
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
            int x = x1 < 0 ? Game.w + x1 : x1;
            int y = y1 < 0 ? Game.h + y1 : y1;
            xm = e.getX();
            ym = e.getY();
            if (xm >= x && xm <= (x + w) && ym >= y && ym <= (y + h)) {
                pressed = true;
            }
        }
        return pressed;
        //if( (e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0 );
    }

    public boolean mouseReleased(MouseEvent e) {
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
            if (pressed) {
                pressed = false;
                pressed2 = true;
                return true;
            }
        }
        return false;
    }

    public void keyPressed(KeyEvent e) {

        if (key == e.getKeyCode()) {
            pressed = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (key == e.getKeyCode()) {
            pressed = false;
            pressed2 = true;
        }
    }
}
