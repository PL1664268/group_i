package othello;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ClientDriver {

	public static void main(String[] args) throws Exception {
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in), 1);
		Player player = new Player("PlayerA","1"); //プレイヤオブジェクトの用意
		Othello game = new Othello(); //オセロオブジェクトを用意
		Client clientdri = new Client(game); //引数としてオセロオブジェクトを渡す
		System.out.println("テスト用サーバに接続します");
		clientdri.connectServer("localhost", 10030);
		System.out.println("接続しました");
		System.out.println("オセロテストへ移行");
		clientdri.mycolor = "black";
		clientdri.playothello();
		
		while(true){
			System.out.println("石を置く場所を数字でキーボードで入力してください");
			String s = r.readLine();//文字列の入力
			System.out.println(s + " が入力されました。手番は " + game.getTurn() + " です。");
			clientdri.receiveMessage(s);
			System.out.println("テストメッセージ「" + s + "」を受信しました");
			boolean putresult = game.putStone(Integer.parseInt(s), game.getTurn());
			if(putresult == true) {
				game.chengeTurn();
			}
			clientdri.updateDisp();
			clientdri.Board.repaint();
		}
	}
}
