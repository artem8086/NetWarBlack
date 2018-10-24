package art.soft;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

class start {

    public static String f_args;
    public static Game game;
    public static intro beg;
    public static Menu m, menu;
    public static final int all_m = 4;

//public void update(Graphics g){
//	paint( g );
//}
    public static void main(String[] args) {

        if (args.length != 0) {
            f_args = args[0];
        }
        //Создание окна
        game = new Game("NetWar (Black Edition)");

        game.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                exitGame();
            }
        });

        intro beg = new intro();
        beg.setBounds(0, 0, Game.w, Game.h);

        game.add(beg);
        
        beg.start = true;
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            if (Game.resize) {
                beg.setSize(game.getWidth(), game.getHeight());
            }
        } while (beg.start);
        game.remove(beg);
        beg = null;

        game.setResizable(true);

        m = new Menu();
        menu = m;

        Game.start(null, 0, all_m);
    }

    public static void drawWindow(Graphics2D gr, int x, int y, int w, int h) {
        gr.setStroke(new BasicStroke(1));
        gr.setColor(Color.white);
        gr.fillRoundRect(x, y, w, h, 32, 32);
        gr.setColor(Color.black);
        gr.drawRoundRect(x, y, w, h, 32, 32);

        gr.setColor(new Color(0x55FF55));
        gr.drawRoundRect(x + 2, y + 2, w - 4, h - 4, 32, 32);
        gr.drawRoundRect(x + 4, y + 4, w - 8, h - 8, 32, 32);
        gr.drawRoundRect(x + 6, y + 6, w - 12, h - 12, 32, 32);
    }

    public static void leave() {
        if (Game.fw != null) {
            try {
                Game.fw.write(-1);
                Game.fw.flush();
                Game.fw.close();
            } catch (IOException ex) {
            }
        }
    }

    public static void exitGame() {
        leave();
        System.exit(0);
    }
}
