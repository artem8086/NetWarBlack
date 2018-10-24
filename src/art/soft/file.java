package art.soft;

import java.awt.*;
import java.io.*;

class file extends FileDialog { 

private static String file_n;
public static Frame f;

file(Frame f, String s, int t, String dir) { 

	super( f, s, t );
	setIconImage(Toolkit.getDefaultToolkit().getImage("data/icon.png"));
	
	if( dir!=null ) setDirectory(dir);
	setResizable( true );

	setSize(Game.w, Game.h); 
	setVisible(true);
}
}