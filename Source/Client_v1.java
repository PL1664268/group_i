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
  /*********プレイヤー情報***/
  private Player myPlayer;
  private Player[] otherPlayer;

  /*********内部クラス*******/
  /*
  class Receive implements Runnable {  //コンストラクタの引数にSocketを渡す
    Socket soc;
    BufferedReader in;
    Receive(Socket soc) {
      this.soc = soc;
      in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
    }

    void run() {
      try {
        while(true) {
          String inputLine = in.readLine();
          /*
          if() {
            receiveMessage();
          }ここに処理を記述
          */
/*
        }
      }catch(IOException e) {
        System.out.println(e);
      }
    }
  }
  */

  class ReceiveInvite implements Runnable {
    //ストリームが２つになってしまう可能性がある。例えば更新をしたとき
    //Clientクラスのクラス変数とは別のストリーム用の変数
    //PrintWriter invOut;
    BufferedReader invIn;
    //コンストラクタ
    ReceiveInvite() {
      try {
        //invOut = new PrintWriter(new OutputStreamWriter(soc.getOutputStream()));
        invIn = new BufferedReader(new InputStreamReader(soc.getInputStream()));
      } catch(IOException e) {
        System.out.println(e);
        System.exit(1);
      }
    }

    void run() {
      try {
        while(true) {
          /*if() {
            スレッド終了時の処理 = 対局をやめる(ユーザーホームに戻る)
            break;
          }*/
          String inputLine = invIn.readLine();
          if(inputLine != null) {
            //申し込みがあったときの処理
          }
        }
        invIn.close();
      }catch(IOException e) {
        System.out.println("対局申し込みを受理するときにエラーがこきました。");
      }
    }
  }

  //対局待ち状態--内部クラスなら
  //この場合、Playerクラスのインスタンスを受け取る
  class MatchRequest implements Runnable {
  //PrintWriter matchOut;
    ObjectInputStream matchIn;
    MatchRequest() {
      try {
        //matchOut = new PrintWriter(new OutputStreamWriter(soc.getOutputStream()));
        matchIn = new ObjectInputStream(soc.getInputStream());
      }catch(IOException e) {
        System.out.println("対局待ち状態時、入出力ストリームでエラーが起きました。");
      }
    }

    void run() {
      while(true) {
        Player opponent = (Player)(matchIn.readObject());
        if(opponent != null) {
          /*画面処理:申し込みが来たことを通知*/
          System.out.println(opponent.getName()+"から対局申し込みがありました。");
        }
        /*対局待ち状態をやめる処理
        if() {
          break;
        }
        */
      }
    }
  }

  /*********メソッド*********/
  public void connectServer(String ipAddress,int port) {
    try {
      soc = new Socket(ipAddress,port);
      //receive = new Receive(soc);  //入力ストリーム用クラスを作成
      //new Thread(receive).start();  //スレッドをスタート
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

  public void receiveMessage() {  *//********戻り値が必要かも？？***********/
    try {

    }catch(IOException e) {
      System.out.println(e);
      System.exit(1);
    }
    /*return msg*/
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
      if(isPer.equals("permit") == true) {  //ログイン認証された
        return true;
      } else if(isPer.equals("notPermit") == true) {  //ログイン認証されなかった
        return false;
      } else {
        System.out.println("認証とは別の文字列です。");  //別の文字列が送られてきた
      }
      //ストリームをクローズする
      out.close();
      in.close();
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
      if(isPer.equals("permit") == true) {  //新規作成できる
        return true;
      } else if(isPer.equals("notPermit") == true) {  //新規作成できる
        return false;
      } else {
        System.out.println("確認とは別の文字列です。");  //別の文字列が送られてきた
      }
      out.close();
      in.close();
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
  //対局待ち状態--メソッドなら
  /*
  public void matchRequest() {
    try {

    }catch(IOException e) {
      System.out.println("対局待ち状態中にエラーが発生しました。");
    }
  }
  */
}
