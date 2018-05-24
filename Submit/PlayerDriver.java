package othello;

public class PlayerDriver {

	public static void main(String[] args) {
		Player playerdri = new Player("player", "password");
		System.out.println("getName出力:" + playerdri.getName());
		System.out.println("getPassword出力:" + playerdri.getPassword());
		System.out.println("setRateで[2000]を入力");
		playerdri.setRate(2000);
		System.out.println("getRate出力:" + playerdri.getRate());
		System.out.println("setWinで[50]を入力");
		playerdri.setWin(50);
		System.out.println("getWin出力:" + playerdri.getWin());
		System.out.println("setDefeatで[30]を入力");
		playerdri.setDefeat(30);
		System.out.println("getDefeat出力:" + playerdri.getDefeat());
		System.out.println("setDrawで[15]を入力");
		playerdri.setDraw(15);
		System.out.println("getDrawt出力:" + playerdri.getDraw());
		System.out.println("setColorで[black]を入力");
		playerdri.setColor("black");
		System.out.println("getColor出力:" + playerdri.getColor());
		
	}

}
