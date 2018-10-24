package art.soft;

import java.awt.*;

class Meteor extends Spell {

public static final int spec_num=8;
public static Image[] spec;
public int der_g;

Meteor(){
	super( 2, 20, 600, 15, 250 );
}

public void preDraw( Graphics g ){
	if( time2<=300 ){
		g.drawImage( spec[ 5 ], x-64, y-64, null );
		if( time2>=(300-r) ){
			g.setColor( new Color(255,255,0,127) );
			((Graphics2D)g).setStroke(new BasicStroke(64));
			int r1 = (300-time2);
			int x1 = x - r1 + 32;
			int y1 = y - r1 + 32;
			g.drawOval( x1, y1, r1+r1-64, r1+r1-64 );
			g.setColor( Color.yellow );
			((Graphics2D)g).setStroke(new BasicStroke(5));
			g.drawOval( x1-32, y1-32, r1+r1, r1+r1 );
			for( int i=Game.der.length-1; i>=0; i-- ){
				int l = length( Game.der[i][0], Game.der[i][1], x, y );
				if( l==r1 ){
					if( l<=32 ){
						Game.dealDamage( i, true, 25 );
						Game.dealDamage( i, false, 25 );
						der_g = i;
					} else 
						Game.dealDamage( i, false, 10 );
				}
			}
		}
	}else
	if( time2<=308 ) g.drawImage( spec[ 4 ], x-64, y-64, null );
}

public void postDraw( Graphics g ){
	//g.drawLine( 0, 0, x, y );
	if( time2>300 ){
		der_g = -1;
		g.setColor( new Color(0,0,0,127) );
		int x1 = time2 - 300;
		g.fillOval( x-x1-32, y-20, 64, 40 );
		if( time2<450 ){
			int y1 = (time2 - 300)<<2;
			g.drawImage( spec[ time2 & 3 ], x-x1-48, y-y1-120, null );
		}
	} else if( time2>=200 && der_g!=-1 ){
		g.drawImage( spec[ (time2 & 4 >> 2)+6 ], Game.der[der_g][0]-32, Game.der[der_g][1]-32, null );
		if( (time2 & 15)==0 ) Game.dealDamage( der_g, false, 1 );
	}
}

}