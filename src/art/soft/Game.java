package art.soft;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;

public class Game extends Frame
        implements Runnable, MouseListener, MouseMotionListener, ComponentListener, KeyListener {

    public static Thread go;
    private static final Toolkit tk = Toolkit.getDefaultToolkit();
    private static final ClassLoader cl = Game.class.getClassLoader();
    private static MediaTracker md;
    public static int w = 1000, h = 600;
    public static long curTime;
    public static final String dataDir = "data/";

    public static int[][] der;
    public static int[][] file_map = { //X, Y, владелец поселка, кол-во HP, 1-ый сосед, 2-ой сосед..N-ый сосед (индекс)
        {500, 100, 1, 70, 2, 9, 10}, // :0
        {100, 500, 2, 70, 3}, // :1
        {500, 300, 0, 20, 0, 4, 3, 5}, // :2
        {300, 500, 0, 20, 1, 4, 2, 6}, // :3
        {500, 500, 0, 20, 2, 3, 5, 6}, // :4
        {700, 500, 0, 20, 4, 7, 6, 2}, // :5
        {500, 700, 0, 20, 4, 3, 5, 8}, // :6
        {900, 500, 3, 70, 5}, // :7
        {500, 900, 4, 70, 6},//,	// :8

        {200, 200, -2, 0, 0}, // :9	skill
        {800, 200, -3, 0, 0} // :10	mana
    //	{800,800,-2,0,7,8},			// :11	skill
    //	{200,800,-3,0,1,8}			// :12	mana
    };
    public static int dx, dy;
    public static DraggedArt[][] der_arts;
    public static Spell[][] spell;
    public static int STp, SX, SY, SR, SRt, Snum;
    public static Spell Spl;
    public static Image[] arts;
    public static CWindow cwin;

    public static int all_p, max_p, you_p;
    public static int[] col = {0xBFFFFFFF, 0xBF0000FF, 0xBFFF0000, 0xBF00FF00, 0xBFFFFF00, 0xBF7F007F, 0xBF005F00, 0xBFFF7F00, 0xBFBF0000, 0xBF00005F, 0xBF7F3F00};
    public static String[] col_s = {"белый", "синий", "красный", "зелёный", "желтый", "фиолетовый", "темно-зелёный", "оранжевый", "вишневый", "темно-синий", "коричневый"};

    public static String[] text = {
        "",//"Загрузка ресурсов...",
        "Ожидание подключения игроков ...",
        " игрок подключился.",
        "Боты никогда не выигрывают, но ", " смог!",//"Ха, ", " игрок пропустил ход:)",
        "Вы продули:( А ", " игрок ПОБЕДИЛ!",
        "А ", " игрок ПРОДУЛ то уже:)",
        "Наконец-то ", " бот продул!",
        "Хмм, ", " игрок вышел из игры:(",
        "Ваш цвет ",
        "",//"Вы пропустили ход!",
        "Вы ПРОДУЛИ:( Печалька...",
        "Вы ВЫИГРАЛИ!!!"
    };

    public static Image im, im1, im2, im3, bg;
    public static Image[][] flag;
    public static Image[] road;
    public static int fanim, p1a, curDer, nextDer, win, fall;
    public static int[] code;
    public static float sx;

    public static PLevel lev[], levp;

    public static boolean mbut1, mbut2, resize, inc_HP, m_move;
    public static boolean start_g = false, artDragg;
    public static InputStream fr[];
    public static OutputStream fw;

    public static Image buf;
    public static Graphics g;
    public static Graphics2D gr;
    public static int xm, ym, player, xs, ys, xsd, ysd;
    public static long step;
    public static Image[] attack;

    public static String f_name;
    static Cursor Ccur, Catt, Csel, Cmov, Csno, Cmvm, Cdra, Cspl;
//static GeneralPath gp;

    public static Attack attacks[];

    public static int but_num, all_but;
    public static final int but_kol = 12;
    public static IButton[] buts;

    public static Bot_AI[] bot;

//Данные для MSG_BOX
    public static int msg_col;
    public static String msg_msg;
    public static boolean msg_enable, msg_key, msg_end = true, msg_win;

    Game(String s) {
        super(s);
        md = new MediaTracker(this);
        setIconImage(loadImage("icon.png"));

        setBackground(Color.black);
        setResizable(true);

        setSize(w, h);
        setLocation(100, 100);
        setLayout(null);

        //enableEvents(AWTEvent.KEY_EVENT_MASK);
        //enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        //enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
        //enableEvents(AWTEvent.COMPONENT_EVENT_MASK);
        addComponentListener(this);
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        setVisible(true);

        go = new Thread(this);
        go.start();
    }

    public static Image loadImage(String im){
        //game.log("Load " + im);
        Image i = tk.getImage(cl.getResource(dataDir + im));
        md.addImage(i, 0);
        try {
            md.waitForID(0);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
        md.removeImage(i, 0);
        return i;
    }

    public void resize() {
        buf = createImage(w, h);
        g = buf.getGraphics();
        //g = getGraphics();
        gr = (Graphics2D) g;
        resize = false;
        //g.dispose();
    }

    private void load() {
        Point curf = new Point(0, 0);
        Point curc = new Point(16, 16);
        Ccur = tk.createCustomCursor(loadImage("cursor/default.png"), curf, null);
        this.setCursor(Ccur);
        Catt = tk.createCustomCursor(loadImage("cursor/attack.png"), curc, null);
        Csel = tk.createCustomCursor(loadImage("cursor/select.png"), curc, null);
        Csno = tk.createCustomCursor(loadImage("cursor/sel_no.png"), curc, null);
        Cmov = tk.createCustomCursor(loadImage("cursor/walkto.png"), curc, null);
        Cmvm = new Cursor(Cursor.MOVE_CURSOR);
        Cdra = new Cursor(Cursor.HAND_CURSOR);
        Cspl = new Cursor(Cursor.CROSSHAIR_CURSOR);

        //int i = 0;
        //try{
        im = loadImage("buldings/der.png");
        im1 = loadImage("buldings/der_sel.png");
        im2 = loadImage("buldings/skill.png");
        im3 = loadImage("buldings/mana.png");

        bg = loadImage("background/bg.png");

        flag = new Image[col.length][7];

        for (int j = col.length - 1; j >= 0; j--) {
            for (int i = 0; i < 7; i++) {
                PixelGrabber pg = new PixelGrabber(loadImage("fl/flag" + i + ".png"), 0, 0, -1, -1, true);
                try {
                    pg.grabPixels();
                } catch (InterruptedException ie) {
                }
                int ai[] = (int[]) pg.getPixels();
                for (int i1 = ai.length - 1; i1 >= 0; i1--) {
                    if (ai[i1] == 0xFFFF0000) {
                        ai[i1] = col[j];
                    }
                }

                flag[j][i] = createImage(new MemoryImageSource(pg.getWidth(), pg.getHeight(), ai, 0, pg.getWidth()));
                //RGBImageFilter rgb = new ColorFilter(j);
                //flag[j][i] = createImage(new FilteredImageSource(
                //	Toolkit.getDefaultToolkit().getImage("data/fl/flag"+i+".png").getSource(), rgb));
            }
        }

        attack = new Image[16];
        for (int i = 0; i < 16; i++) {
            attack[i] = loadImage("attack/" + i + ".png");
        }

        Meteor.spec = new Image[Meteor.spec_num];
        for (int i = Meteor.spec_num - 1; i >= 0; i--) {
            Meteor.spec[i] = loadImage("spec/meteor/im" + i + ".png");
        }

        road = new Image[1];
        int i = 0;
        //for(int i=0; i>=0; i++)
        road[i] = loadImage("road/" + i + ".png");

        //}catch(IOException ex){}
        //Загрузка артов
        arts = new Image[1];
        arts[0] = loadImage("buttons/cancel.png");

        //cwin = new CWindow( 128, 128, 1000, 1000 );
        cwin = new CWindow(128, 128, 254, 104);
        //Загрузка кнопок
        buts = new IButton[but_kol];
        buts[0] = new ImageButton(loadImage("buttons/menu.png"),
                loadImage("buttons/menu_press.png"), -84, -84, 64, 64, KeyEvent.VK_ESCAPE);
        buts[1] = new IButton(loadImage("buttons/help.png"), 20, 40, 64, 64, KeyEvent.VK_F1);
        buts[2] = new ImageButton(loadImage("buttons/zoom_p.png"),
                loadImage("buttons/zoom_p_press.png"), -168, -84, 64, 64, KeyEvent.VK_EQUALS);
        buts[3] = new ImageButton(loadImage("buttons/zoom_m.png"),
                loadImage("buttons/zoom_m_press.png"), -84, -168, 64, 64, KeyEvent.VK_MINUS);
        buts[4] = new IButton(loadImage("buttons/cancel.png"), 20, 124, 64, 64, KeyEvent.VK_SPACE);
        buts[5] = new level(loadImage("buttons/level.png"), 40, -104, 64, 64, KeyEvent.VK_BACK_QUOTE);
        buts[6] = new mana(loadImage("buttons/pool.png"), 105, -102, 256, 30);
        buts[7] = new skill(85, -102, 298, 53);
        buts[8] = new ArtPool();
        //buts[9] = new IButton( null, -84, 134, 64, 64, KeyEvent.VK_CONTROL );
        buts[11] = cwin;

        //Создание стрелки
        /*gp = new GeneralPath();
	gp.moveTo(150, 0); // начало
	gp.lineTo(50, -45);
	gp.quadTo(200, -10, 350, -10);
	gp.lineTo(350, -30);
	gp.lineTo(500, 0); // середина
	gp.lineTo(350, 30);
	gp.lineTo(350, 10);
	gp.quadTo(150, 10, 50, 45);
	//gp.lineTo(0, 100);
	gp.closePath(); */// линия в начало
    }

    public void Init() {
        resize();
        mapLoad();

        if (max_p > 1) {
            g.setColor(Color.white);
            g.fillRect(0, 0, w, h);

            msg_enable = true;
            msg_Box(text[1], 0);
            msg_win = true;
        }

        /*UnitX = new int[all_p][];
	UnitY = new int[all_p][];
	MaxUnit = new int[all_p];
	DUnit = new int[all_p];
	UDamage = new int[all_p];
	for(int n=all_p-1; n>=0; n--)
		UDamage[n] = 1;
	DDamage = new int[all_p];
	USkill = new int[all_p];
	for(int n=all_p-1; n>=0; n--)
		USkill[n] = 1;
	DSkill = new int[all_p];*/
        code = new int[all_p];
        for (int n = all_p - 1; n >= 0; n--) {
            code[n] = -1;
        }

        /*curDir = new int[all_p];
	nextDir = new int[all_p];
	for(int n=all_p-1; n>=0; n--)
		nextDir[n] = -1;*/
        sx = 1;
        artDragg = m_move = mbut1 = mbut2 = inc_HP = false;

        if (max_p == 0) {
            all_but = 4;
        } else {
            all_but = but_kol - 1;
        }

        msg_enable = true;
        but_num = all_but - 1;

        repaint();
        connect_players();

        msg_win = false;

        if (max_p > 0) {
            msg_Box(text[13] + col_s[you_p], col[you_p]);
        }

        for (int i = der.length - 1; i >= 0; i--) {
            if (der[i][2] > all_p) {
                der[i][2] = 0;
            }
        }

        fanim = 0;
        p1a = 0;
        curDer = -1;
        nextDer = -1;
        win = 0;

        xs = ys = 0;
        STp = 0;

        step = 0;
        start_g = false;

        if (max_p > 0) {
            buts[9] = new SpellBut(0, -84, 134, KeyEvent.VK_Q, spell[you_p - 1][0]);
            buts[10] = new SpellBut(1, -158, 134, KeyEvent.VK_W, spell[you_p - 1][1]);
        }
    }

    public void mapLoad() {
        der = new int[file_map.length][];
        for (int i = der.length - 1; i >= 0; i--) {
            der[i] = new int[file_map[i].length];
            for (int j = der[i].length - 1; j >= 0; j--) {
                der[i][j] = file_map[i][j];
            }
        }
        der_arts = new DraggedArt[der.length][1];
        der_arts[0] = new DraggedArt[6];
        der_arts[0][0] = new DraggedArt(0, 30, 4, 10, true);
        der_arts[0][1] = new DraggedArt(0, 60, -1, 12, true);

        lev = new PLevel[all_p];
        for (int n = all_p - 1; n >= 0; n--) {
            lev[n] = new PLevel();
            lev[n].mana = 350;
            lev[n].mana_pool = 350;
            lev[n].skill = 0;
            lev[n].level = 1;
            lev[n].skill_pool = 250;
            lev[n].max_level = 5;
        }
        spell = new Spell[all_p][];
        for (int i = all_p - 1; i >= 0; i--) {
            spell[i] = new Spell[lev[i].max_level];
            spell[i][0] = new Meteor();
            spell[i][1] = new Meteor();
            spell[i][2] = new Meteor();
            spell[i][3] = new Meteor();
            spell[i][4] = new SpellX();
        }

        attacks = new Attack[der.length];
        for (int n = der.length - 1; n >= 0; n--) {
            attacks[n] = new Attack(attack);
        }
    }

    public void update(Graphics graf) {
        //System.out.println("Update!");
        paint(graf);
    }

    public void paint(Graphics graf) {
        if (buf != null) {
            msg_run();
            //drawMenu();

            graf.drawImage(buf, 0, 0, this);
        }
        //super.paint( graf );
    }

    public void decode(int c) {
        if (c != (you_p - 1) && code[c] != -2) {
            do {
                try {

                    int p = c + 1;
                    player = p;
                    switch (code[c] = fr[c].read()) {
                        case 0:
                            int t;
                            while ((t = fr[c].read()) == -1);

                            int t1;
                            while ((t1 = fr[c].read()) == -1);

                            if (t == 255) {
                                t = -1;
                            }
                            if (t1 == 255) {
                                t1 = -1;
                            }
                            curDer = t;
                            step(t1);
                            break;
                        case 1: // код перемещения арта
                            int cder;
                            while ((cder = fr[c].read()) == -1);
                            int num;
                            while ((num = fr[c].read()) == -1);
                            int i;
                            while ((i = fr[c].read()) == -1);

                            for (int j = 0; j < der_arts[i].length; j++) {
                                if (der_arts[i][j] == null) {
                                    der_arts[i][j] = der_arts[cder][num];
                                    der_arts[cder][num] = null;
                                    break;
                                }
                            }
                            break;
                        case 2:
                            msg_Box(text[5] + col_s[p] + text[6], col[p]);
                            msg_win = true;
                            break;
                        case 3:
                            msg_Box(text[7] + col_s[p] + text[8], col[p]);
                            break;
                        case 4:
                            msg_Box(text[9] + col_s[p] + text[10], col[p]);
                            code[c] = -2;
                            return;
                        case 5:
                            msg_Box(text[3] + col_s[p] + text[4], col[p]);
                            msg_win = true;
                            break;
                        case 6:
                            int n;
                            while ((num = fr[c].read()) == -1);
                            while ((n = fr[c].read()) == -1);
                            dx = n;
                            while ((n = fr[c].read()) == -1);
                            dx |= n << 8;
                            while ((n = fr[c].read()) == -1);
                            dy = n;
                            while ((n = fr[c].read()) == -1);
                            dy |= n << 8;
                            spell[c][num].activate(dx, dy);
                            break;
                        case 7:
                            while ((n = fr[c].read()) == -1);
                            spell[c][n].activate(0, 0);
                            break;
                        case 255:
                            for (i = der.length - 1; i >= 0; i--) {
                                if (der[i][2] == p) {
                                    der[i][2] = 0;
                                }
                            }
                            msg_Box(text[11] + col_s[p] + text[12], col[p]);
                            if (c == 0) {
                                for (p = all_p; p > max_p; p--) {
                                    for (i = der.length - 1; i >= 0; i--) {
                                        if (der[i][2] == p) {
                                            der[i][2] = 0;
                                        }
                                    }
                                }
                            }
                            code[c] = -2;
                            return;
                    }
                    //if( code[c]!=-2 ) code[c] = -1;
                } catch (IOException ex) {
                }
            } while (code[c] != -1);
        }
    }

    public void run() {

        load();
        do {

            while (!start_g) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }

            Init();

            while (go == Thread.currentThread()) {
                curTime = System.currentTimeMillis();

                //mana --; if( mana<0 ) mana = mana_pool;
                //skill ++; if( skill>skill_pool ) skill = 0;
                int nextDerp = nextDer;
                int curDerp = curDer;
                if (max_p > 1) {
                    for (int c = (you_p == 1 ? max_p : all_p) - 1; c >= 0; c--) {
                        levp = lev[c];
                        decode(c);
                    }
                }
                if (you_p <= 1 && max_p < all_p) {
                    if ((step % 100) == 0) {
                        for (player = all_p; player > max_p; player--) {
                            if (code[player - 1] != -2) {//&& attacks[player-1].nextDir==-1 ){
                                levp = lev[player - 1];
                                bot[player - max_p - 1].step();
                            }
                        }
                    }
                }

                nextDer = nextDerp;
                curDer = curDerp;
                player = you_p;

                step++;
                if ((step % 250) == 0) {
                    inc_HP = true;
                }

                if (resize) {
                    resize();
                }

                //g.setColor(Color.white);
                //g.fillRect(0,0,w,h);
                int xb = xs % 56 - (xs != 0 ? 56 : 0);
                int yb = ys % 56 - (ys != 0 ? 56 : 0);
                for (int x = xb; x < w; x += 56) {
                    for (int y = yb; y < h; y += 56) {
                        g.drawImage(bg, x, y, this);
                    }
                }

                g.setColor(Color.black);
                //gr.setStroke(new BasicStroke(3));
                AffineTransform at = new AffineTransform(sx, 0.0, 0.0, sx, xs, ys);
                //at.concatenate(AffineTransform.getShearInstance (0.2, 0));
                gr.setTransform(at);
                //g.translate( xs, ys );

                for (int i = der.length - 1; i >= 0; i--) {
                    for (int j = der[i].length - 5; j >= 0; j--) {
                        if (der[i][j + 4] < i) {
                            drawRoad(der[i][0], der[i][1], der[der[i][4 + j]][0], der[der[i][4 + j]][1]);
                        }
                    }
                    int d = attacks[i].vld - 1;
                    if (d >= 0) {
                        levp = lev[d];
                    }
                    attacks[i].startUnit();
                }

                for (int j = all_p - 1; j >= 0; j--) {
                    levp = lev[j];
                    for (int i = spell[j].length - 1; i >= 0; i--) {
                        if (spell[j][i] != null) {
                            if (spell[j][i].time2 >= 0) {
                                spell[j][i].preDraw(g);
                            }
                        }
                    }
                }
                Attack.uanim++;
                if (Attack.uanim > 7) {
                    Attack.uanim = 0;
                }

                //g.setColor(Color.black);
                Font ft = new Font("Serif", Font.ITALIC | Font.BOLD, 30);
                g.setFont(ft);
                String s;

                win = -1;
                fall = 0;
                Cursor cur = curDer == -1 || nextDer != -1 ? Ccur : Csno;

                if (m_move && mbut1) {
                    cur = Cmvm;
                }

                for (int i = der.length - 1; i >= 0; i--) {
                    dx = der[i][0] - 40;
                    dy = der[i][1] - 40;
                    int vl = der[i][2];

                    if (max_p > 0 && STp != 2) {
                        if ((xm - xs) >= dx * sx && (xm - xs) <= (dx + 80) * sx && (ym - ys) >= dy * sx && (ym - ys) <= (dy + 80) * sx) {
                            if (artDragg) {
                                if (i != curDer && vl == you_p && curDer != -1) {
                                    for (int j = 0; j < der_arts[i].length; j++) {
                                        if (der_arts[i][j] == null) {
                                            der_arts[i][j] = der_arts[curDer][ArtPool.num];
                                            der_arts[curDer][ArtPool.num] = null;
                                            try {
                                                fw.write(1);
                                                fw.write(curDer);
                                                fw.write(ArtPool.num);
                                                fw.write(i);
                                                fw.flush();
                                            } catch (IOException ex) {
                                            }
                                            break;
                                        }
                                    }
                                }
                                artDragg = false;
                            } else {
                                if (nextDer == -1) {
                                    if (curDer == -1) {
                                        if (vl == you_p) {
                                            cur = Csel;
                                            if (mbut1) {
                                                curDer = i;
                                                mbut1 = m_move = false;
                                            }
                                        }
                                    } else {
                                        for (int j = der[curDer].length - 5; j >= 0; j--) {
                                            if (der[curDer][j + 4] == i) {
                                                if (vl == you_p || vl < 0) {
                                                    cur = Cmov;
                                                } else {
                                                    cur = Catt;
                                                }
                                                if (mbut1) {
                                                    nextDer = i;
                                                    mbut1 = m_move = false;
                                                }
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (vl >= 0) {
                        if (curDer == i && nextDer == -1) {
                            if (vl != you_p) {
                                curDer = -1;
                                nextDer = -1;
                            }
                            g.drawImage(im1, dx, dy, this);
                        } else {
                            g.drawImage(im, dx, dy, this);
                        }

                        g.drawImage(flag[vl][fanim >> 2], dx + 10, dy - 10, this);
                        s = "" + der[i][3];
                        g.setColor(Color.black);
                        g.drawString(s, dx + 40 - (getFontMetrics(ft).stringWidth(s) >> 1), dy + 20);

                        if (inc_HP && vl != 0) {
                            der[i][3]++;
                            p1a = 1;
                        }
                        if (p1a != 0 && vl != 0) {
                            g.setColor(new Color(255, 255, 0, 255 - p1a));
                            s = "+1";
                            g.drawString(s, dx + 40 - (getFontMetrics(ft).stringWidth(s) >> 1), dy + 20 - (p1a >> 2));
                        }

                        for (int j = der_arts[i].length - 1; j >= 0; j--) {
                            if (der_arts[i][j] != null) {
                                //if( der_arts[i][j].time2>=0 ) der[i][3] += 4;

                                if (der_arts[i][j].cooldown2 >= 0) {
                                    der_arts[i][j].cooldown2--;
                                }
                                if (der_arts[i][j].time2 >= 0) {
                                    der_arts[i][j].time2--;
                                } else if (der_arts[i][j].num == 0) {
                                    der_arts[i][j] = null;
                                }
                            }
                        }

                        if (vl > 0 && you_p != vl) {
                            win = 0;
                        }
                        if (you_p == vl) {
                            fall = -1;
                        }
                    } else if (vl == -2) {
                        g.drawImage(im2, dx, dy, this);
                    } else if (vl == -3) {
                        g.drawImage(im3, dx, dy, this);
                    }
                }
                for (int j = all_p - 1; j >= 0; j--) {
                    for (int i = spell[j].length - 1; i >= 0; i--) {
                        if (spell[j][i] != null) {
                            if (spell[j][i].time2 >= 0) {
                                spell[j][i].postDraw(g);
                            }
                            spell[j][i].timeDec();
                        }
                    }
                }

                if (max_p > 0 && code[you_p - 1] != -2) {
                    levp = lev[you_p - 1];
                    if (fall == 0) {
                        if (code[you_p - 1] != -2) {
                            try {
                                msg_Box(text[15], col[you_p]);
                                fw.write(3);
                                fw.flush();
                                msg_win = true;
                            } catch (IOException ex) {
                            }
                        }
                        code[you_p - 1] = -2;
                    }
                    if (win != 0) {
                        try {
                            if (!msg_win) {
                                msg_Box(text[16], col[you_p]);
                                fw.write(2);
                                fw.flush();
                            }
                            msg_win = true;
                        } catch (IOException ex) {
                        }
                    }

                    if (nextDer != -1) {
                        if (step(nextDer)) {
                            try {
                                fw.write(0);
                                fw.write(curDer);
                                fw.write(nextDer);
                                fw.flush();
                                curDer = -1;
                                //nextDer = -1;
                            } catch (IOException ex) {
                            }
                        }
                        //curDer = -1;
                        nextDer = -1;
                        /*else {
					int x1 = der[curDer][0]; int y1 = der[curDer][1];
					int x2 = der[nextDer][0]; int y2 = der[nextDer][1];

					double dx = (double)(x1-x2);
					double dy = (double)(y1-y2);

					double len = Math.sqrt( dx*dx +dy*dy );
					//System.out.println("dx = "+dx+" ; dy = "+dy+" ; len = "+len);
					//System.out.println("x1 = "+x1+" ; y1 = "+y1);
					if( len!=0 ){
						double lent = len / 550.0;
						at.concatenate(new AffineTransform( lent, 0.0, 0.0, lent, x1, y1 ));

						double an = 0;
						if( dy>0 ){
							an = Math.PI;
							if( dx<0 ){
								an += Math.PI / 2.0;
								dy = Math.abs( dx );
							} else
								dy = Math.abs( dy );
						} else {
							if( dx>0 ){
								an += Math.PI / 2.0;
								dy = Math.abs( dx );
							} else
								dy = Math.abs( dy );
						}
						at.concatenate(AffineTransform.getRotateInstance( Math.asin(dy/len) + an, 0, 0));

						gr.setTransform( at );
						g.setColor(Color.red);
						gr.fill(gp);
					}
				}*/
                    }
                }

                if (STp == 2) {
                    cur = Cspl;
                    SRt += 2;
                    if (SRt > SR) {
                        SRt = 0;
                    }
                    gr.setStroke(new BasicStroke(3));
                    dx = (int) ((xm - xs) / sx) - SR;
                    dy = (int) ((ym - ys) / sx) - SR;
                    g.setColor(new Color(32, 32, 255, 127));
                    int wh = SR + SR;
                    g.fillOval(dx, dy, wh, wh);
                    g.setColor(Color.blue);
                    g.drawOval(dx, dy, wh, wh);
                    dx += SRt;
                    dy += SRt;
                    wh -= SRt + SRt;
                    g.drawOval(dx, dy, wh, wh);
                    if (mbut1) {
                        mbut1 = false;
                        dx = (int) ((xm - xs) / sx);
                        dy = (int) ((ym - ys) / sx);
                        Spl.activate(dx, dy);
                        try {
                            fw.write(6);
                            fw.write(Snum);
                            fw.write(dx & 255);
                            fw.write(dx >> 8);
                            fw.write(dy & 255);
                            fw.write(dy >> 8);
                            fw.flush();
                        } catch (IOException ex) {
                        }
                        STp = 0;
                    } else if (mbut2) {
                        STp = 0;
                        mbut2 = false;
                    }
                }
                gr.setTransform(new AffineTransform());

                inc_HP = false;
                if (m_move && mbut1) {
                    xs += xsd - xm;
                    xm = xsd;
                    ys += ysd - ym;
                    ym = ysd;
                }

                fanim++;
                if (fanim == 28) {
                    fanim = 0;
                }
                if (p1a != 0) {
                    p1a++;
                    if (p1a == 128) {
                        p1a = 0;
                    }
                }
                //mbut1 = mbut2 = false;

                if (mbut2) {
                    curDer = -1;
                    nextDer = -1;
                    mbut2 = false;
                }

                msg_enable = true;
                if (start.menu != null) {
                    msg_enable = false;
                    start.menu.draw(g);
                    if (start_g) {
                        break;
                    }
                } else {
                    int j = -1;
                    for (int i = but_num; i >= 0; i--) {
                        if (buts[i].draw(g)) {
                            j = i;
                        }
                    }

                    if (j == 0) {
                        start.m.startMenu(false);
                    } else {
                        if (j == 4) {
                            mbut2 = true;
                        }
                    }
                    if (buts[2].pressed) {
                        if (sx < 2.0) {
                            sx += 0.05;
                        }
                    } else if (buts[3].pressed) {
                        if (sx > 0.2) {
                            sx -= 0.05;
                        }
                    }
                }

                if (ArtPool.dragged) {
                    cur = Cdra;
                }
                setCursor(cur);
                repaint();

                curTime = 20 + curTime - System.currentTimeMillis();
                if (curTime > 0) {
                    try {
                        Thread.sleep(curTime);
                    } catch (InterruptedException e) {
                    }
                }
            }
            //go = new Thread(this); 
            //go.start();
        } while (true);
    }

    public static boolean step(int i) {

        if (curDer != -1 || i != -1) {
            if (attacks[curDer].nextDir == -1) {
                if (der[curDer][3] > 2) {
                    nextDer = i;
                    //msg_Box(""+col_s[player]+" игрок походил!", col[player]);
                    int m = der[curDer][3] / 3;
                    m = m <= 50 ? m : 50;
                    attacks[curDer].init(i, m, player);
                    return true;
                }
            }
        }
        return false;
    }

    public static void incMana(int p, int i) {
        levp.mana += 10;
        if (levp.mana > levp.mana_pool) {
            levp.mana = levp.mana_pool;
        }
    }

    public static void incSkill(int p, int i) {
        levp.skill += i;
        if (levp.skill > levp.skill_pool) {
            if (levp.level < levp.max_level) {
                levp.skill -= levp.skill_pool;
                levp.skill_pool += 50;
                levp.mana_pool += 25;
                levp.mana += 25;
                levp.level++;
            } else {
                levp.level = levp.max_level;
                levp.skill = levp.skill_pool;
            }
        }
    }

    public static void dealDamage(int dr, boolean type, int dmg) {
        der[dr][3] -= dmg;
        if (der[dr][3] < 0) {
            der[dr][3] = 0;
        }
    }

    public static int sign(int x) {
        return (x > 0) ? 1 : (x < 0) ? -1 : 0;
        //возвращает 0, если аргумент (x) равен нулю; -1, если x < 0 и 1, если x > 0.
    }

    public void drawRoad(int x, int y, int xend, int yend) {
        int dx, dy, incx, incy, pdx, pdy, es, el, err;

        dx = xend - x;//проекция на ось икс
        dy = yend - y;//проекция на ось игрек

        incx = sign(dx);
        incy = sign(dy);

        if (dx < 0) {
            dx = -dx;//далее мы будем сравнивать: "if (dx < dy)"
        }
        if (dy < 0) {
            dy = -dy;//поэтому необходимо сделать dx = |dx|; dy = |dy|
        }
        if (dx > dy) {
            pdx = incx;
            pdy = 0;
            es = dy;
            el = dx;
        } else {
            pdx = 0;
            pdy = incy;
            es = dx;
            el = dy;//тогда в цикле будем двигаться по y
        }

        err = el >> 1;

        int m = 11 - (incx != 0 ? 2 : 0) - (incy != 0 ? 2 : 0);
        int t = 0;
        do {
            for (int j = m; j >= 0; j--) {
                if (t < el) {
                    err -= es;
                    if (err < 0) {
                        err += el;
                        x += incx;//сдвинуть прямую (сместить вверх или вниз, если цикл проходит по иксам)
                        y += incy;//или сместить влево-вправо, если цикл проходит по y
                    } else {
                        x += pdx;//продолжить тянуть прямую дальше, т.е. сдвинуть влево или вправо, если
                        y += pdy;//цикл идёт по иксу; сдвинуть вверх или вниз, если по y
                    }
                    t++;
                } else {
                    return;
                }
            }
            g.drawImage(road[0], x - 10, y - 10, this);
        } while (true);
    }

    public void connect_players() {
        //Открытие сетевых файлов
        if (max_p > 0) {
            char start;
            you_p = 1;

            fr = new FileInputStream[all_p];
            start = ' ';

            if (max_p > 1) {
                try {
                    fr[0] = new FileInputStream(f_name);
                    start = (char) fr[0].read();
                    you_p = 0;
                } catch (IOException ex) {
                }
            }

            if (you_p == 1) {

                try {
                    fw = new FileOutputStream(f_name);
                    fw.write('r');
                } catch (IOException ex) {
                }

                int i;
                for (i = 1; i < max_p; i++) {
                    do {
                        start = ' ';
                        try {
                            fr[i] = new FileInputStream(f_name + i);
                            start = (char) fr[i].read();
                        } catch (IOException ex) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                            }
                        }
                    } while (start != 'r');

                    msg_col = col[i];
                    msg_msg = col_s[i] + text[2];
                    repaint();
                }
                bot = new Bot_AI[all_p - max_p + 1];
                for (; i < all_p; i++) {
                    bot[i - max_p] = new Bot_AI(f_name + i);
                }

            } else if (start == 'r') {
                int i;
                for (i = 1; i < max_p; i++) {
                    try {
                        fr[i] = new FileInputStream(f_name + i);
                        start = (char) fr[i].read();
                    } catch (IOException ex) {
                        try {
                            fw = new FileOutputStream(f_name + i);
                            fw.write('r');
                            you_p = i + 1;
                        } catch (IOException e) {
                        }
                    }
                }
                for (; i < all_p; i++) {
                    do {
                        start = ' ';
                        try {
                            fr[i] = new FileInputStream(f_name + i);
                            start = (char) fr[i].read();
                        } catch (IOException ex) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                            }
                        }
                    } while (start != 'r');

                    msg_col = col[i];
                    msg_msg = "" + col_s[i] + text[2];
                    repaint();
                }
            }
            player = you_p;
        } else {
            you_p = -2;
            player = -1;
            bot = new Bot_AI[all_p];
            for (int i = all_p - 1; i >= 0; i--) {
                bot[i] = new Bot_AI(null);
            }
        }
    }

    public static void start(String file, int max, int all) {
        /*while( start_g )
		try{
			Thread.sleep(100); 
		}catch(InterruptedException e){}*/

        all_p = all;
        max_p = max > all ? all : max;

        f_name = file;
        start_g = true;

        /*while( start_g )
		try{
			Thread.sleep(100); 
		}catch(InterruptedException e){}*/
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {

        if (start.menu != null) {
            start.menu.mousePressed(e);
        } else {
            boolean b = false;
            for (int i = but_num; i >= 0; i--) {
                if (b = buts[i].mousePressed(e)) {
                    break;
                }
            }

            if (!b) {
                if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
                    mbut1 = true;
                }
                if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
                    mbut2 = true;
                }
                xsd = xm = e.getX();
                ysd = ym = e.getY();
            }
        }
        if (msg_key) {
            msg_end = true;
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (start.menu != null) {
            start.menu.mouseReleased(e);
        } else {
            boolean b = false;
            for (int i = but_num; i >= 0; i--) {
                if (b = buts[i].mouseReleased(e)) {
                    break;
                }
            }

            m_move = mbut1 = mbut2 = false;
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        if (!ArtPool.mouseDragged(e)) {
            if (start.menu == null) {
                xsd = e.getX();
                ysd = e.getY();
                m_move = true;
            }
        }
        //if( !cwin.mouseDragged( e ) )
    }

    public void mouseMoved(MouseEvent e) {
        if (start.menu == null) {
            xm = e.getX();
            ym = e.getY();
        }
    }

    public void keyPressed(KeyEvent e) {

        if (start.menu == null) {
            for (int i = but_num; i >= 0; i--) {
                buts[i].keyPressed(e);
            }
        }

        if (msg_key) {
            msg_end = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (start.menu == null) {
            for (int i = but_num; i >= 0; i--) {
                buts[i].keyReleased(e);
            }
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void componentResized(ComponentEvent e) {

        w = getWidth();
        h = getHeight();
        //if( start.m!=null ){
        //	start.m.setBounds(w-Menu.m_w+Menu.x, h>>2, Menu.m_w, Menu.m_h);
        //}
        resize = true;
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

    public static void msg_Box(String message, int col_msg) {
        if (!msg_win && msg_enable) {
            msg_col = col_msg;
            msg_msg = message;
            msg_end = false;
            msg_key = false;
        }
    }

    public void msg_run() {
        if (!msg_end && msg_enable) {

            Font ft = new Font("Serif", Font.BOLD, 24);
            g.setFont(ft);
            int ws = Game.g.getFontMetrics(ft).stringWidth(msg_msg);

            start.drawWindow(gr, w - (ws + 64) >> 1, (h - 128) >> 1, ws + 64, 128);

            g.setColor(new Color(msg_col));
            g.drawString(msg_msg, (w - ws) >> 1, h >> 1);
            if (!msg_win) {
                msg_key = true;
            }
        }
    }
}
