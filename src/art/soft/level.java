package art.soft;

import java.awt.*;

class level extends IButton {

level( Image i, int x, int y, int w, int h, int k ){
	super( i, x, y, w, h, k );
}

public void postDraw( Graphics g, int x, int y ){
	Font ft = new Font("Serif", Font.BOLD, 44);
	g.setFont(ft);
	g.setColor( Color.black );
	String s = "" + Game.levp.level;
	int d = pressed ? 4 : 0;
	g.drawString(s, x+32+d-(g.getFontMetrics(ft).stringWidth(s)>>1), y+46+d);
}
}