package art.soft;

import java.awt.*;

class DraggedArt {

public int art, cooldown, num, time;
public int cooldown2, time2;
public Image im;
public boolean pressed, pressed2, active;
public static boolean apressed;

DraggedArt( int art, int cooldown, int num, int time, boolean active ){
	this.art = art;
	this.cooldown = cooldown;
	this.num = num;
	this.time = time;
	this.active = active;
	cooldown2 = -1;
	time2 = -1;
}
public void draw( Graphics g, int x1, int y1 ){
	int x = x1 < 0 ? Game.w+x1 : x1;
	int y = y1 < 0 ? Game.h+y1 : y1;

	int d = pressed ? 4 : 0;
	g.setColor( new Color(0,0,0,0x55) );
	g.fillOval( x+4, y+4, 64, 64 );
	g.drawImage( Game.arts[ art ], x+d, y+d, null );
	if( cooldown>0 && cooldown2!=-1 )
		g.fillArc( x+d, y+d, 64, 64, 90, (int)( 360 * ((float) cooldown2 / cooldown) ) );

	if( num!=-1 ){
		Font ft = new Font("Serif", Font.BOLD, 16);
		g.setFont(ft);
		String s = "" + num;
		int w = g.getFontMetrics(ft).stringWidth(s);
		g.setColor( Color.black );
		g.fillRect(x + 60 - w, y+46, w+4, 18);
		g.setColor( Color.yellow );
		g.drawString(s, x + 62 - w, y+60);
	}

	//postDraw( g, x, y );
	if( pressed2 ){
		pressed2 = false;
		if( active ){
			if( num>0 ) num --;
			cooldown2 = cooldown;
			time2 = time;
		}
	}
}
//public void postDraw( Graphics g, int x, int y ){}

public void pressed(){
	if( cooldown2==-1 ){
		pressed = true;
		apressed = true;
	}
}
public void released( boolean info ){
	if( !info )
		if( pressed ){
			apressed = false;
			pressed = false;
			pressed2 = true;
			//return true;
		}
}

}