package art.soft;


import java.awt.*;

class skill extends IButton {

    skill(int x, int y, int w, int h) {
        super(null, x, y, w, h, 0);
    }

    @Override
    public void postDraw(Graphics g, int x, int y) {
        Font ft = new Font("Serif", Font.BOLD, 22);
        g.setFont(ft);
        ((Graphics2D) g).setStroke(new BasicStroke(3));
        g.setColor(new Color(255, 255, 255, 48));
        g.fillRoundRect(x + 2, y + 2, w - 4, h - 4, 62, 62);
        g.setColor(Color.yellow);
        float f = (float) Game.levp.skill / (float) Game.levp.skill_pool;
        g.fillRoundRect(x + 2, y + 2, (int) ((w - 4) * f), h - 4, 62, 62);
        g.setColor(Color.black);
        g.drawRoundRect(x + 1, y + 1, w - 2, h - 2, 62, 62);
        String s = "" + Game.levp.skill + "/" + Game.levp.skill_pool;
        g.drawString(s, x + (w - g.getFontMetrics(ft).stringWidth(s) >> 1), y + 48);
    }
}
