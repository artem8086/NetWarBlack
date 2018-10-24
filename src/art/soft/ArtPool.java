package art.soft;

import java.awt.*;
import java.awt.event.*;

class ArtPool extends IButton {

public static EmptyArt[] buts = new EmptyArt[ 6 ];
public static int x, l, xd, yd, num;
public static boolean dragged;

ArtPool(){
	super( null, 0, 40, 0, 84, 0 );
	x = -20;
	l = 0;
	buts[0] = new EmptyArt( x-74, 50 );
	buts[1] = new EmptyArt( x-148, 50 );
	buts[2] = new EmptyArt( x-222, 50 );
	buts[3] = new EmptyArt( x-296, 50 );
	buts[4] = new EmptyArt( x-370, 50 );
	buts[5] = new EmptyArt( x-444, 50 );
}

public boolean draw( Graphics g ){
	if( Game.curDer!=-1 ){
		l = Game.der_arts[ Game.curDer ].length;
		if( l!=0 ){
			w = 10 + l * 74;
			x1 = Game.w + x - w;
			Graphics2D gr = (Graphics2D)g;
			start.drawWindow(gr, x1, y1, w, h);

			for( int i=l-1; i>=0; i-- ){
				DraggedArt b = Game.der_arts[Game.curDer][i];
				if( b!=null ){
					if( dragged && b.pressed ){
						buts[i].draw( g );
						b.draw( g, xd-32, yd-32 );
					} else
						b.draw( g, buts[i].x1, buts[i].y1 );
				} else
					buts[i].draw( g );
			}
		}
	} else {
		l = 0;
		w = 0;
		dragged = false;
		DraggedArt.apressed = false;
	}
	return false;
}
public boolean mousePressed(MouseEvent e){
	int xm = e.getX();
	int ym = e.getY();
	if( xm>=x1 && xm<=(x1+w) && ym>=y1 && ym<=(y1+h) ){
		pressed = true;
		if( Game.curDer!=-1 )
		for( int i=l-1; i>=0; i-- )
			if( buts[i].mousePressed( e ) ){
				DraggedArt b = Game.der_arts[Game.curDer][i];	
				if( b!=null ){
					b.pressed();
					num = i;
				}
				break;
			}
	}
	return pressed;
	//if( (e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0 );
}
public boolean mouseReleased(MouseEvent e){
	if( pressed ){
		for( int i=l-1; i>=0; i-- )
			if( buts[i].mouseReleased( e ) ){
				DraggedArt b = Game.der_arts[Game.curDer][i];
				if( b!=null ){ 
					if( dragged ){
						DraggedArt.apressed = false;
						b.pressed = false;
						dragged = false;
						Game.xm = xd;
						Game.ym = yd;
						Game.artDragg = true;
					} else
						b.released( false );
				}
				break;
			}
		pressed = false;
		return true;
	}
	return false;
}
public void keyPressed(KeyEvent e) {
	int i = -1;
	switch( e.getKeyCode() ){
	case KeyEvent.VK_Z: i = 0; break;
	case KeyEvent.VK_X: i = 1; break;
	case KeyEvent.VK_C: i = 2; break;
	case KeyEvent.VK_V: i = 3; break;
	case KeyEvent.VK_B: i = 4; break;
	case KeyEvent.VK_N: i = 5;
	}
	if( i!=-1 ){
		DraggedArt b = Game.der_arts[Game.curDer][i];	
		if( b!=null ){
			b.pressed();
			DraggedArt.apressed = false;
		}
	}
}
public void keyReleased(KeyEvent e){
	int i = -1;
	switch( e.getKeyCode() ){
	case KeyEvent.VK_Z: i = 0; break;
	case KeyEvent.VK_X: i = 1; break;
	case KeyEvent.VK_C: i = 2; break;
	case KeyEvent.VK_V: i = 3; break;
	case KeyEvent.VK_B: i = 4; break;
	case KeyEvent.VK_N: i = 5;
	}
	if( i!=-1 ){
		DraggedArt b = Game.der_arts[Game.curDer][i];	
		if( b!=null ) b.released( false );
	}
}
public static boolean mouseDragged( MouseEvent e ){
	if( DraggedArt.apressed && (e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0 ){
		dragged = true;
		xd = e.getX();
		yd = e.getY();
		return true;
	} else
		dragged = false;
	return false;
}

}