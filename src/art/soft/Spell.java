package art.soft;

import java.awt.*;

class Spell {

public int type;
public Image spell;
public int cooldown, time, manacost;
public int cooldown2, time2;
public int x, y, r;

Spell( int type, int cooldown, int time, int manacost ){
	this.cooldown = cooldown;
	this.manacost = manacost;
	this.time = time;
	this.type = type;
	if( type!=0 ) cooldown2 = time2 = -1;
}
Spell( int type, int cooldown, int time, int manacost, int r ){
	this( type, cooldown, time, manacost );
	this.r = r;
}

public void activate( int x, int y ){
	this.x = x;
	this.y = y;
	cooldown2 = cooldown;
	time2 = time;
	Game.levp.mana -= manacost;
}
public void preDraw( Graphics g ){}
public void postDraw( Graphics g ){}
public void Upgrade(){}

public void timeDec(){
	if( cooldown2>=0 ) cooldown2 --;
	if( time2>=0 ) time2 --;
}
public int length( int x1, int y1, int x2, int  y2 ){
	int dx = x1 - x2;
	int dy = y1 - y2;
	return (int)Math.sqrt( dx*dx + dy*dy );
}

}