import othello.*;
import java.io.IOException;

public class Player_p1 {
  public static void main(String[] args) {
    System.out.println("Playerインスタンス作成");
    Player player = new Player();
    System.out.println("--------------------------------");

    System.out.println("プレイヤ名 セット値:gg");
    player.setName("gg");
    System.out.println("取得値:"+player.getName());
    System.out.println("--------------------------------");

    System.out.println("先手後手情報 セット値:black");
    player.setColor("black");
    System.out.println("取得値:"+player.getColor());
    System.out.println("--------------------------------");

    System.out.println("勝利数 セット値:65");
    player.setWin(65);
    System.out.println("取得値:"+player.getWin());
    System.out.println("--------------------------------");

    System.out.println("敗北数 セット値:35");
    player.setDefeat(35);
    System.out.println("取得値:"+player.getDefeat());
    System.out.println("--------------------------------");

    System.out.println("引き分け数 セット値:5");
    player.setDraw(5);
    System.out.println("取得値:"+player.getDraw());
    System.out.println("--------------------------------");

    System.out.println("投了数 セット値:1");
    player.setSurrender(1);
    System.out.println("取得値:"+player.getSurrender());
    System.out.println("--------------------------------");

    System.out.println("レート セット値:3502");
    player.setRate(3502);
    System.out.println("取得値:"+player.getRate());
    System.out.println("--------------------------------");

    System.out.println("Enterで終了・・・");
    try{
      System.in.read();
    } catch(IOException e) {
      e.printStackTrace();
    }
  }
}
