package art.soft;

import java.awt.*;
import java.awt.event.*;

class Menu extends IButton {

    public static int xm, ym, bh, by, n_Pl, n_Bot, xs;
    public static String file_n;
    public static String[] text = {
        "Новая игра",
        "Выбор карты",
        "Сетевой файл",
        "Выберите сетевой файл:",
        "Игроков: ",
        "Ботов: ",
        "Продолжить",
        "Покинуть",
        "Выход"
    };

    private final Image menuIm = Game.loadImage("gui/menu.png");
    private final Image button = Game.loadImage("gui/button.png");
    private final Image butPress = Game.loadImage("gui/button_pressed.png");

    private final Image netWar = Game.loadImage("NetWar.png");
    private int ny;

    //public static boolean m_draw;
    //public static final int w = 250;
    public static boolean m_rel, m_tp;

    Menu() {
        super(null, -1, 40, 250, 320, 0);

        startMenu(true);
    }

    public void startMenu(boolean tp) {
        //Game.but_num = 0;
        start.menu = start.m;
        m_tp = tp;
        bh = 44;
        if (tp) {
            n_Pl = 1;
            n_Bot = start.all_m - n_Pl;
            h = 320;
        } else {
            h = 200;
        }
        x1 = -1;
        ny = 0;
    }

    public void postDraw(Graphics g, int x, int y) {
        g.drawImage(netWar, 20, Game.h - ny, null);
        //
        xs = x;
        Graphics2D gr = (Graphics2D) g;
        //gr.setTransform( AffineTransform.getTranslateInstance( x, y ) );
        //System.out.println("Menu!");
        g.drawImage(menuIm, x - 2, y - 2, null);
        //start.drawWindow(gr, x, y, w, h);
        by = 30 + y;
        gr.setStroke(new BasicStroke(2));
        Font ft = new Font("Serif", Font.BOLD, bh - 16);
        g.setFont(ft);

        if (m_tp) {
            if (button(g, text[0], ft)) {
                start.menu = null;

                if (file_n == null) {
                    if (start.f_args != null) {
                        file_n = start.f_args;
                    } else {
                        file_n = "dop.gm";
                    }
                }
                Game.start(file_n, n_Pl, n_Pl + n_Bot);
            }
            button(g, text[1], ft);

            if (button(g, text[2], ft)) {
                file_n = null;
                FileDialog fd = new file(start.game, text[3], FileDialog.LOAD, null);
                do {
                    file_n = fd.getFile();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                } while (file_n == null);
                fd.dispose();
                fd = null;
            }
            if (button(g, text[4] + n_Pl, ft)) {
                n_Pl++;
                if (n_Pl > start.all_m) {
                    n_Pl = 0;
                }
                if ((n_Pl + n_Bot) > start.all_m) {
                    n_Bot = start.all_m - n_Pl;
                }
            }
            if (button(g, text[5] + n_Bot, ft)) {
                n_Bot++;
                if ((n_Pl + n_Bot) > start.all_m) {
                    n_Bot = 0;
                }
            }

        } else {
            if (button(g, text[6], ft)) {
                //Game.addList = true;
                //Game.but_num = Game.all_but;
                start.menu = null;
            }
            if (button(g, text[7], ft)) {
                start.leave();
                n_Pl = 1;
                n_Bot = start.all_m;
                Game.start(null, 0, start.all_m);
                startMenu(true);
            }
        }
        if (button(g, text[8], ft)) {
            start.exitGame();
        }

        if (x1 >= -w - 20) {
            x1 -= 12;
        }
        if (ny < netWar.getHeight(null) + 20) {
            ny += 6;
        } else {
            ny = netWar.getHeight(null) + 20;
        }
    }

    public boolean button(Graphics g, String s, Font ft) {
        boolean ret = false;
        int pr = 0;

        //g.setColor(Color.lightGray);
        //g.fillRoundRect(xs + 24, by + 4, w - 40, bh - 8, 32, 32);

        if (xm >= 20 && xm <= (w - 40) && ym >= by && ym <= (by + bh - 8)) {
            if (m_rel) {
                xm = ym = 0;
                m_rel = false;
                ret = true;
            } else {
                pr = 2;
            }
        }
        /*g.setColor(Color.white);
        g.fillRoundRect(xs + 20 + pr, by + pr, w - 40, bh - 8, 32, 32);
        g.setColor(Color.black);
        g.drawRoundRect(xs + 20 + pr, by + pr, w - 40, bh - 8, 32, 32);*/
        g.drawImage(pr == 0 ? button : butPress, xs + 20, by, null);
        g.setColor(Color.black);
        g.drawString(s, xs + (w - g.getFontMetrics(ft).stringWidth(s) >> 1) + pr, by + bh - 14 + pr);

        by += bh;
        return ret;
    }

    @Override
    public boolean mousePressed(MouseEvent e) {
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
            xm = e.getX() - xs;
            ym = e.getY();
            m_rel = false;
        }
        //if( (e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0 );
        return true;
    }

    @Override
    public boolean mouseReleased(MouseEvent e) {
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
            m_rel = true;
        }
        return true;
    }
}
