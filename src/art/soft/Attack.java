package art.soft;

import java.awt.*;

class Attack {

private final static int anid[][]={	{12, 14, 10},			//{12,  0,  2},
									{ 0,  0,  8},			//{14,  0,  4},
									{ 2,  4,  6}};			//{10,  8,  6}};

int incx, incy, pdx, pdy, es, el, err, an, speed, dt, width;
 
public static int uanim;
public int nextDir, curDir, MaxUnit, DUnit, UDamage, DDamage, USkill, DSkill, vld;
public int xstart, ystart, xend, yend, TUnit;
public Image[] unit;
//public int[] UnitX;
//public int[] UnitY;

Attack( Image[] u ){
	unit = u;
	nextDir = -1;
	USkill = 1;
	UDamage = 1;
	speed = 1;
	width = 18;
}

public void init( int i, int m, int vl ){
	vld = vl;
	curDir = Game.curDer;
	nextDir = i;
	DUnit = MaxUnit = m;
	//UnitX = new int[m];
	//UnitY = new int[m];
	xstart = Game.der[curDir][0];
	ystart = Game.der[curDir][1];
	//int x = Game.der[curDir][0];
	//int y = Game.der[curDir][1];
	//for(int z=m-1; z>=0; z--){
	//	UnitX[z] = x;
	//	UnitY[z] = y;
	//}
	reinit();
}

public void reinit(){
	xend = Game.der[nextDir][0];
	yend = Game.der[nextDir][1];
	//uanim = 0;
	//TUnit = 0;

	
	int x = xstart;
    int y = ystart;
        
    int dx = xend - x;//проекция на ось икс
    int dy = yend - y;//проекция на ось игрек

    incx = Game.sign(dx);
    incy = Game.sign(dy);

	an = anid[incx+1][incy+1];
	dt = 0; TUnit = 0;

	if (dx < 0) dx = -dx;//далее мы будем сравнивать: "if (dx < dy)"
	if (dy < 0) dy = -dy;//поэтому необходимо сделать dx = |dx|; dy = |dy|

    if (dx > dy) {
        pdx = incx;     pdy = 0;
        es = dy;        el = dx;
    } else {
        pdx = 0;        pdy = incy;
        es = dx;        el = dy;//тогда в цикле будем двигаться по y
    }
    err = el>>1;
}

public void startUnit(){
	TUnit += speed; if( TUnit>=width ) TUnit = 0;
	if( nextDir!=-1 ){
		dt += speed;
		if( TUnit==0 && DUnit!=0 ){
			DUnit --;
			if( Game.der[curDir][3]!=0 ) Game.der[curDir][3] --;
			else{
				MaxUnit -= DUnit;
				DUnit = 0;
			}
		}
		//for(int i=MaxUnit-1; i>=DUnit; i--)
		//	if( UnitX[i]>=0 )
		if( xend!=Game.der[nextDir][0] || yend!=Game.der[nextDir][1] ) reinit();
		drawUnits();
	}
}

public void drawUnits(){
        int x = xstart;
		int y = ystart;

		int i=MaxUnit-DUnit;
		for (int t = 0; t < el; t++) {
        //if( x!=xend || y!=yend ) {
            err -= es;
            if (err < 0) {
                err += el;
                x += incx;//сдвинуть прямую (сместить вверх или вниз, если цикл проходит по иксам)
                y += incy;//или сместить влево-вправо, если цикл проходит по y
            } else {
                x += pdx;//продолжить тянуть прямую дальше, т.е. сдвинуть влево или вправо, если
                y += pdy;//цикл идёт по иксу; сдвинуть вверх или вниз, если по y
            }

			if( t<=dt ){
				if( t>(dt-i*width) ){
					if( ((t-TUnit) % width)==0 ){
						Game.g.drawImage(unit[an | (uanim>>2)], x-9, y-9, null);
						if( t>=(el-speed) ) unitAttack();
					}
				}
			}
			if( dt>=el ) dt = el-speed;
        //} else {
		//	
		//	return;
		}
		//if( dt>=el ) unitAttack();
}

public void unitAttack(){
	MaxUnit --;

	int nd = nextDir;
	int vl = Game.der[nd][2];
	if( vl==-2 ) Game.incSkill( vld-1, USkill+DSkill+9 ); else
	if( vl==-3 ) Game.incMana( vld-1, 10 ); else
	if( vl==vld ) Game.der[nd][3] ++;
	else if( Game.der[nd][3]!=0 ){
		Game.dealDamage( nd, true, UDamage+DDamage );
		if( vld==Game.you_p ) Game.incSkill( vld-1, USkill+DSkill );
	} else {
		Game.der[nd][3] = 1;
		Game.der[nd][2] = vld;
	}
	if( MaxUnit==0 ){
		//if( i==you_p ) curDer = -1;
		nextDir = -1;
	}
}

}