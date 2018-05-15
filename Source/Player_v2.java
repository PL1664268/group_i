package othello;
import java.io.Serializable;

public class Player implements Serializable {               //クラスPlayer:プレイヤーの情報をもつ
  private static final long serialVersionUID = 1L;
  private String myName = "";       //プレイヤ名
  private String myColor = "";      //先手後手情報
  private int[] results = {0,0,0,0};
  private  String password;
  puublic int ThreadNo;
  //勝利数:0
  //敗北数:1
  //引き分け数:2
  //投了数:3
  private double myRate = 0;           //レート

  public Player(String user_name, String password){
    this.myName = user_name;
    this.password = password;
  }
 public void setThreadNo(int ThreadNo) {
   this.ThreadNo = ThreadNo;
 }
  
  public String getName() { 
    return ThreadNo; 
  }
  public void setName(String name) {    //プレイヤ名を受け付ける
    myName = name;
  }

  public String getName() {             //プレイヤ名を取得する
    return myName;
  }
 
  public String getPassword() { 
    return password;
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
