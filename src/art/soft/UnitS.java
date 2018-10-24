package art.soft;


class UnitS {

public int hp, x, y, r, c, vl;
public boolean kill;
public Attack[] att;
public UnitS next;

public void dealsDamage(int dmg){
	hp -= dmg;
	if( hp<0 ){ hp = 0; kill = true; }
}

}