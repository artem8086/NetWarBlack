package art.soft;


class bot_stupid extends Bot{

public void Bot_AI(){	
	int n = 0;//
	int bre;
	for(int i=der.length-1; i>=0; i--)//перебор зданий
		if( der[i][2]==player ) n ++;//записать количество зданий ботов 
	
	if( n!=0 ){
		n = (int)(Math.random() * 1000) % n;// рандомный выбор номера любого здания
				
		for(int i=der.length-1; i>=0; i--){
			if( der[i][2]==player ){
				if( n==0 ){//номер последнего
					//n = 0;
					n = der[i].length-4;//последнюю дорожку в н
					Game.curDer = i;//от этого здания
					int p=0;
					bre=0;
					do{
						bre++;
						if( n!=0 ){
							n = (int)(Math.random() * 1000)%(n);
						} else {
							n = (int)(Math.random() * ((der[der[i][n+4]].length-4)>9?der[der[i][n+4]].length-4:der[der[i][n+4]].length-3));
						}
						if ((n+4)>(der[i].length-1))
							n=0;
						
						for (p=der[der[i][n+4]].length-4;p>0;p--) {
							if ((der[der[der[i][n+4]][p+3]][2]!=player) || (der[der[i][n+4]][2]!=player)) {
								p=-1;
								break;
							}
						}
						if (bre>900){
							for (p=der.length-1;p>=0;p--) {
								if (der[p][2]!=player){
									break;
								}//else if(p==0){
								//	Game.code[player-1]=-2;
								//}
							}
							p=-1;
						}
					}while (p!=-1);
					Game.nextDer = der[i][n+4];
					break;
				}
				n --;
			}
		}
	} else
		bot_code = 4;
}

}