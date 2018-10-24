package art.soft;

import java.awt.*;

class EmptyArt extends IButton {

EmptyArt( int x, int y ){
	super( null, x, y, 64, 64, 0 );
}
public void postDraw( Graphics g, int x, int y ){
	((Graphics2D)g).setStroke(new BasicStroke(3));
	g.setColor( Color.black );
	g.drawOval( x, y, w, h );
}

}