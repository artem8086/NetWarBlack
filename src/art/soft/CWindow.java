package art.soft;

import java.awt.*;

class CWindow extends IButton {

public int wc, hc, xs, ys, x2, y2;

CWindow( int x, int y, int w, int h ){
	super( null, x, y, 0, 0, 0 );
	x2 = x;
	y2 = y;
	wc = w;
	hc = h;
	xs = ys = 0;
}
public boolean draw( Graphics g ){
	x1 = x2;
	w = Game.w - x1 - x1;
	if( w>(wc+20) ){ w = wc+20; x1 = Game.w - w >> 1; }
	y1 = y2;
	h = Game.h - y1 - y1;
	if( h>(hc+20) ){ h = hc+20; y1 = Game.h - h >> 1; }

	if( pressed && Game.m_move ){
		xs += Game.xsd - xm << 2;
		xm = Game.xsd;
		if( xs>(wc-w) ) xs = wc - w;
		if( xs<0 ) xs = 0;
		ys += Game.ysd - ym << 2;
		ym = Game.ysd;
		if( ys>(hc-h) ) ys = hc - h;
		if( ys<0 ) ys = 0;
	}
	Graphics2D gr = (Graphics2D)g;
	start.drawWindow( gr, x1, y1, w, h );
	g.setClip(x1+10, y1+10, w-20, h-20);
	g.translate( 10+x1-xs, 10+y1-ys );
	/*if( im!=null ){
		int d = pressed ? 4 : 0;
		g.setColor( new Color(0,0,0,0x55) );
		g.fillOval( x+4, y+4, w, h );
		g.drawImage( im, x+d, y+d, null );
	}*/
	
	postDraw( g );

	g.translate( xs-10-x1, ys-10-y1 );
	g.setColor( Color.black );
	if( hc > (h-20) ){
		float c = (float)(h-40) / hc;
		g.fillRoundRect( x1+w-20, y1+(int)(ys*c)+20, 4, (int)(h*c), 4, 4 );
	}
	if( wc > (w-20) ){
		float c = (float)(w-40) / wc;
		g.fillRoundRect( x1+(int)(xs*c)+20, y1+h-20, (int)(w*c), 4, 4, 4 );
	}
	g.setClip(0, 0, Game.w, Game.h);
	if( pressed2 ){
		pressed2 = false;
		return true;
	}
	return false;
}

public void postDraw( Graphics g ){}

}