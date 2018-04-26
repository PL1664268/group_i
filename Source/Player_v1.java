package othello;

public class Player {               //クラスPlayer:プレイヤーの情報をもつ
  private String myName = "";       //プレイヤ名
  private String myColor = "";      //先手後手情報
  private int[] results = {0,0,0,0};
  //勝利数:0
  //敗北数:1
  //引き分け数:2
  //投了数:3
  private double myRate = 0;           //レート

  public void setName(String name) {    //プレイヤ名を受け付ける
    myName = name;
  }

  public String getName() {             //プレイヤ名を取得する
    return myName;
  }

  public void setColor(String color) {  //先手後手情報を受け付ける
    myColor = color;
  }

  public String getColor() {              //先手後手情報を取得する
    return myColor;
  }

  public void setWin(int win) {           //勝利数を受け付ける
    results[0] = win;
  }

  public int getWin() {                   //勝利数を取得する
    return results[0];
  }

  public void setDefeat(int defeat) {     //敗北数を受け付ける
    results[1] = defeat;
  }

  public int getDefeat() {                //敗北数を取得する
    return results[1];
  }

  public void setDraw(int draw) {         //引き分け数を受け付ける
    results[2] = draw;
  }

  public int getDraw() {                  //引き分け数を取得する
    return results[2];
  }

  public void setSurrender(int surrender) {//投了数を受け付ける
    results[3] = surrender;
  }

  public int getSurrender() {             //投了数を取得する
    return results[3];
  }

  public void setRate(double rate) {       //レートを受け付ける
    myRate = rate;
  }

  public double getRate() {                 //レートを取得する
    return myRate;
  }
}
