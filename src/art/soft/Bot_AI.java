package art.soft;

import java.io.*;

class Bot_AI {

public OutputStream fw;
public Bot bot;

Bot_AI( String f ){
	if( f!=null ) try{
		fw = new FileOutputStream( f );
		fw.write('r');
	}catch(IOException ex){}

	bot = new bot_stupid();
}

public void step(){
	Bot.bot_code = 0;
	Bot.der = Game.der;
	Bot.der_arts = Game.der_arts;
	Bot.player = Game.player;
	
	boolean b_win = true;
	for( int i=Game.all_p-1; i>=0; i-- )
		if( (Game.player-1)!=i && Game.code[i]!=-2 ){
			b_win = false; break;
		}

	if( b_win ){
		Bot.bot_code = 5;
	} else
		bot.Bot_AI();

	try{
		//if( fw!=null ) fw.write( Bot.bot_code );

		switch( Bot.bot_code ){
		case 0:
			if( Game.step( Game.nextDer ) )
			if( fw!=null ){
				fw.write( 0 );
				fw.write( Game.curDer );
				fw.write( Game.nextDer );
			}
			break;
		case 1:
			int k = Bot.der_arts[Game.nextDer].length;
			for( int j=0; j<k; j++ )
				if( Bot.der_arts[Game.nextDer][j]==null ){
					Bot.der_arts[Game.nextDer][j] = Bot.der_arts[Game.curDer][Bot.num];
					Bot.der_arts[Game.curDer][Bot.num] = null;
					try{
						fw.write(1);
						fw.write(Game.curDer);
						fw.write(Bot.num);
						fw.write(Game.nextDer);
					}catch(IOException ex){}
					break;
				}
			break;
		case 4:
			if( fw!=null ) fw.write( 4 );
			Game.msg_Box(Game.text[9]+Game.col_s[Game.player]+Game.text[10],Game.col[Game.player]);
			Game.code[Game.player-1]=-2;
			break;
		case 5:
			if( fw!=null ) fw.write( 5 );
			Game.msg_Box(Game.text[3]+Game.col_s[Game.player]+Game.text[4],Game.col[Game.player]);
			Game.code[Game.player-1]=-2;
			Game.msg_win = true;
		}
		if( fw!=null ) fw.flush();
	}catch(IOException ex){}
}

}