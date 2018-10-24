package art.soft;

import java.awt.*;

class intro extends Canvas
        implements Runnable {

    private static Thread go;
    boolean start = false;
    Image intr;
    int sz, num;
    int imSz;

    intro() {
        super();

        intr = Game.loadImage("intro.png");
        //imSz = intr.getWidth(null);
        sz = Game.w < Game.h ? Game.h : Game.w;
        imSz = sz - (sz >> 2);
        num = 0;

        setVisible(true);

        go = new Thread(this);
        go.start();
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D gr = (Graphics2D) g;
        gr.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        int h = (int) ((float) intr.getHeight(this) * ((float) num / intr.getWidth(this)));
        g.drawImage(intr, (Game.w - num) >> 1, ((Game.h - (int) (h * 3)) >> 1)
                + (Game.h >> 3), num, h, this);
    }

    public void run() {

        while (!start) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }

        //g = (Graphics2D)buf.getGraphics();
        //g.setColor( Color.black );
        //g.fillRect( 0, 0, sz, sz );
        //g.setStroke( new BasicStroke( dx, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND ) );
        //rgb = new introFilter( g, dx );
        do {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
            }
            //intr = createImage(new FilteredImageSource(intr.getSource(), rgb));
            repaint();
            num += 6;
        } while (num < imSz);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        go = null;
        start = false;
    }
}
