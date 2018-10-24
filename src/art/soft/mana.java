package art.soft;

import java.awt.*;

class mana extends IButton {

public static Image im;

mana( Image i, int x, int y, int w, int h ){
	super( null, x, y, w, h, 0 );
	im = i;
}

public void postDraw( Graphics g, int x, int y ){
	Font ft = new Font("Serif", Font.BOLD, 22);
	g.setFont(ft);
	((Graphics2D)g).setStroke(new BasicStroke(3));
	//g.setColor( new Color(255, 255, 255, 48) );
	//g.fillRoundRect( x+2, y+2, w-4, h-4, 30, 30 );
	g.setColor( Color.blue );
	float f = (float)Game.levp.mana / (float)Game.levp.mana_pool;
	g.fillRoundRect( x+2, y+2, (int)((w-4)*f), h-4, 30, 30 );
	g.setColor( Color.black );
	g.drawRoundRect( x+1, y+1, w-2, h-2, 30, 30 );
	String s = "" + Game.levp.mana +"/"+ Game.levp.mana_pool;
	g.drawString(s, x+(w - g.getFontMetrics(ft).stringWidth(s)>>1), y+23);
	g.drawImage(im, x-81, y-18, null);
}
}