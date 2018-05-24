package othello;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class OthelloDriver {

	public static void main (String [] args) throws Exception{
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in), 1);
		Othello game = new Othello(); 
		System.out.println("テスト：Othelloクラスのオブジェクトを初期化");
		printStatus(game);
		printGrids(game);
		while(true){
			System.out.println("石を置く場所(数字またはpass)をキーボードで入力してください");
			String s = r.readLine();//文字列の入力
			System.out.println(s + " が入力されました。手番は " + game.getTurn() + " です。");
			game.putStone(Integer.parseInt(s), game.getTurn());
			printStatus(game);
			printGrids(game);
			System.out.println("手番を変更します。\n");
			game.chengeTurn();
		}
	}
	//状態を表示する
	public static void printStatus(Othello game){
		int[] grids = game.getGrids();
		System.out.println("checkWinner出力:" + game.checkWinner());
		System.out.println("isGameover出力:" + game.isGameover(game.getTurn(),grids));
		System.out.println("getTurn出力：" + game.getTurn());
	}
	//テスト用に盤面を表示する
	public static void printGrids(Othello game){
		int[] grids = game.getGrids();
		int row = 8;
		System.out.println("getRow出力：" + row);
		System.out.println("Gridsテスト出力");
		for(int i = 0 ; i < row * row ; i++){
			System.out.print(grids[i] + " ");
			if(i % row == row - 1){
				System.out.print("\n");
			}
		}
	}

}
