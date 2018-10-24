package art.soft;

import java.awt.*;

class SpellX extends Spell {

public static final int spec_num=8;
public static Image[] spec;
public Meteor m[];

SpellX(){
	super( 1, 2000, 600, 200 );
}

public void preDraw( Graphics g ){
	if( time2==time ){
		m = new Meteor[Game.der.length];
		for( int c=m.length-1; c>=0; c-- ){
			int vl = Game.der[c][2];
			if( vl>0 && vl!=Game.you_p ){
				m[c] = new Meteor();
				m[c].manacost = 0;
				m[c].activate( Game.der[c][0], Game.der[c][1] );
			}
		}
	}
	if( time2==0 ) m = null;
	else
	for( int c=m.length-1; c>=0; c-- )
		if( m[c]!=null ){
			m[c].preDraw( g );
		}
}

public void postDraw( Graphics g ){
	if( m!=null )
	for( int c=m.length-1; c>=0; c-- )
		if( m[c]!=null ){
			m[c].postDraw( g );
			m[c].timeDec();
		}
}

}