package art.soft;

import java.awt.*;
import java.io.*;

class SpellBut extends IButton {

public Spell sp;
public int num;

SpellBut( int num, int x, int y, int key, Spell s ){
	super( null, x, y, 64, 64, key );
	if( s!=null ) im = s.spell;
	this.num = num;
	sp = s;
}
public boolean draw( Graphics g ){
	int x = x1 < 0 ? Game.w+x1 : x1;
	int y = y1 < 0 ? Game.h+y1 : y1;

	if( sp!=null ){
		g.setColor( new Color(0,0,0,0x55) );
		if( im!=null ){
			int d = 0;
			if( pressed && sp.manacost<=Game.levp.mana && sp.cooldown2==-1 ) d = 4;
			g.fillOval( x+4, y+4, 64, 64 );
			g.drawImage( im, x+d, y+d, null );
		}
		if( sp.cooldown2>0 )
			g.fillArc( x, y, 64, 64, 90, (int)( 360 * ((float) sp.cooldown2 / sp.cooldown) ) );
		if( sp.manacost>Game.levp.mana )
			g.fillOval( x, y, 64, 64 );

		if( sp.manacost!=0 ){
			Font ft = new Font("Serif", Font.BOLD, 16);
			g.setFont(ft);
			String s = "" + sp.manacost;
			int w = g.getFontMetrics(ft).stringWidth(s);
			g.setColor( Color.black );
			g.fillRect(x + 60 - w, y+46, w+4, 18);
			g.setColor( Color.blue );
			g.drawString(s, x + 62 - w, y+60);
		}

		if( pressed2 ){
			pressed2 = false;
			if( sp.manacost<=Game.levp.mana && sp.cooldown2==-1 ){
				if( sp.type==0 ) {} else {
					if( sp.type==1 ){
						sp.activate( 0, 0 );
						try{
							Game.fw.write(7);
							Game.fw.write(num);
							Game.fw.flush();
						}catch(IOException ex){}
					} else
					if( sp.type==2 ){
						Game.SR = sp.r;
						Game.STp = 2;
						Game.Spl = sp;
					}
					Game.Snum = num;
				}
			}
			return true;
		}
	} else {
		((Graphics2D)g).setStroke(new BasicStroke(3));
		g.setColor( Color.black );
		g.drawOval( x, y, 64, 64 );

		if( pressed2 ){
			pressed2 = false;
			return true;
		}
	}
	return false;
}

}