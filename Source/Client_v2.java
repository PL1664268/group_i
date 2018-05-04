/*
loginRequest,newAccountRequestなど個々のメソッド内でストリームをつくるソースコード
*/
/**********GUI**********/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**********通信*********/
import java.net.*;
import java.io.*;
/**********その他*******/
import java.util.ArrayList;
import othello.*;

public class Client extends JFrame implements MouseListener {
  /*********GUI属性**********/

  /*********通信属性*********/
  //private Receive receive;  //入力ストリーム用内部クラスのインスタンス変数
  private BufferedReader in;  //入力ストリーム
  private PrintWriter out;  //出力ストリーム
  private ObjectInputStream ois;  //オブジェクト入力ストリーム
  private ObjectOutputStream oos;  //オブジェクト出力ストリーム※必要ないかも？
  private Socket soc = null;  //ソケット
  private int stopRecFlag = 0;  //通信時の受信のループを抜けるためのフラグ
  /*********プレイヤー情報***/
  private Player myPlayer;  //自分のPlayerクラスのインスタンス変数
  private Player opponent;  //対局相手のインスタンス変数
  private Player[] otherPlayer;
  private Othello game;  //対局のOthelloクラスのインスタンス変数

  /*********内部クラス*******/
  //他プレイヤからの申し込みを受け取る or 自分の申し込みの答えを受け取る(承諾、拒否)
  class ReceiveInvite implements Runnable {
    //ストリームが２つになってしまう可能性がある。例えば更新をしたとき
    //Clientクラスのクラス変数とは別のストリーム用の変数
    //コンストラクタ
    ReceiveInvite() {
      try {
        //グローバル変数BufferedReader in
        in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
      } catch(IOException e) {
        System.out.println(e);
        System.exit(1);
      }
    }

    void run() {
      try {
        while(true) {
          if(stopRecFlag == 1) {
            stopRecFlag = 0;
            //スレッド終了時の処理 = 対局をやめる(ユーザーホームに戻る)
            break;
          }
          String inputLine = invIn.readLine();
          if(inputLine != null) {
            //申し込みがあったときの処理
            if() {
              //他プレイヤーからの申し込み
            } else if() {
              //自分の申し込みに対する返信
            }
          }
        }
        invIn.close();
      }catch(IOException e) {
        System.out.println("対局申し込みを受理するときにエラーがこきました。");
      }
    }
  }

  //対局中の通信を行う:受信側
  class MatchReceive implements Runnable {
    MatchReceive() {
      try {
        //グローバル変数 BufferedReader in
        in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
      } catch(IOException e) {
        System.out.println("対局開始時の通信でエラーが起きました。");
        System.out.println(e);
        System.exit(1);
      }
    }

    void run() {
      while(true) {
        String readLine = in.readLine();
        if(readLine != null) {
          receiveMessage(readLine);  //ただコマンドラインに表示するだけ
          /*Othelloに対する処理を書く*/
        }

　　　　//終了時の処理
        if(stopRecFlag == 1) {
          stopRecFlag = 0;
          break;
        }
      }
    }
  }

  /*********メソッド*********/
  public void connectServer(String ipAddress,int port) {
    try {
      soc = new Socket(ipAddress,port);
    }catch(UnknownHostException e) {
      System.out.println("ホストに接続できません。");
      System.out.println(e);
      System.exit(1);
    }catch(IOException e) {
      System.out.println("サーバー接続時にエラーが発生しました。");
      System.out.println(e);
      System.exit(1);
    }
  }

  public void sendMessage(String msg) {
    out.println(msg);
    out.flush();
    System.out.println("サーバーに"+msg+"を送信しました。");
  }

  public void receiveMessage(String line) {
    System.out.println(line);
  }

//ログイン
  public boolean loginRequest(String userName,String password) {
    try {
      /*
      サーバー側で(ユーザーー名,パスワード)だと判別できるような処理が必要
      例えば、
      loginRequest
      userName:~~~~~~~
      password:^^^^^^^
      のようにキーをつける
      sendMessage("userName:"+userName);
      sendMessage("password:"+password);
      */
      in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
      out = new PrintWriter(new OutputStreamWriter(soc.getOutputStream()));
      out.println("loginRequest");
      out.println("userName:"+userName);
      out.println("password:"+password);
      out.flush();
      String isPer = in.readLine();
      out.close();
      in.close();
      if(isPer.equals("permit") == true) {  //ログイン認証された
        return true;
      } else if(isPer.equals("notPermit") == true) {  //ログイン認証されなかった
        return false;
      } else {
        System.out.println("認証とは別の文字列です。");  //別の文字列が送られてきた
      }
      //ストリームをクローズする
    } catch(IOException e) {
      System.out.println(e);
      System.exit(1);
    }
  }
//アカウント作成
  public boolean accountRequest(String userName,String password) {
    try {
      out = new PrintWriter(new OutputStreamWriter(soc.getOutputStream()));
      in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
      out.println("accountRequest");
      out.println("userName:"+userName);
      out.println("password:"+password);
      out.flush();
      String isPer = in.readLine();
      out.close();
      in.close();
      if(isPer.equals("permit") == true) {  //新規作成できる
        return true;
      } else if(isPer.equals("notPermit") == true) {  //新規作成できる
        return false;
      } else {
        System.out.println("確認とは別の文字列です。");  //別の文字列が送られてきた
      }
    }catch(IOException e) {
      System.out.println(e);
      System.exit(1);
    }
  }
//プレイヤー情報受付
  public void myPlayerRequest() {
    try {
      out = new PrintWriter(new OutputStreamWriter(soc.getOutputStream()));
      ois = new ObjectInputStream(soc.getInputStream());
      out.println("myPlayerRequest");
      out.flush();
      myPlayer = (Player)(ois.readObject());
      out.close();
      ois.close();
    }catch(IOException e) {
      System.out.println(e);
      System.exit(1);
    }
  }
//対局待ちプレイヤー受付
  public void otherPlayerRequest() {
    try {
      out = new PrintWriter(new OutputStreamWriter(soc.getOutputStream()));
      ois = new ObjectInputStream(soc.getInputStream());
      ArrayList<Player> playerList = new ArrayList<Player>();
      out.println("otherPlayerRequest");
      out.flush();
      playerList = (ArrayList<Player>)(ois.readObject());
      otherPlayer = playerList.toArray(new Player[0]);
      out.close();
      ois.close();
    }catch(IOException e) {
      System.out.println(e);
      System.exit(1);
    }
  }

  //対局開始時に呼ぶメソッド
  //主に対局中の通信を行う:送信側
  public void startMatch() {
    try {
      out = new PrintWriter(new OutputStreamWriter(cos.getOutputStream()));
      //これによりsendMessage()メソッドが使えるようになる
    }catch(IOException e) {
      System.out.println("対局開始時の通信でエラーが起きました。");
      System.out.println(e);
      System.exit(1);
    }
  }
  //相手プレイヤの操作を自分のOthelloクラスインスタンスに反映させる?
  //commandには局面のマス番号を受け取る
  public void acceptOpponentOperation(String command) {
    int mass = Integer.parseInt(command);
    game.putStone(mass,opponent.getColor());
  }

  //自分の操作を自分のOthelloクラスに反映させる?
  //戻り値は石を置けるかどうか--置ける:true,置けない:false
  public boolean acceptMyOperation(int mass) {
    if(game.putStone(mass,myPlayer.getColor()) == true) {
      System.out.println(mass+"番のマスに"+myPlayer.getColor()+"の石を置きました。");
      return true;
    } else {
      System.out.printl(mass+"番のマスに"+myPlayer.getColor()+"の石は置けません。");
      return false;
    }
  }
}
