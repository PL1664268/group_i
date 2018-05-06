package othello;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server_v1 {

	private int port; //ポート番号
	private boolean [] online; //クライアント接続状態
	private PrintWriter [] out; //データ送信用オブジェクト
	private Receiver [] receiver; //データ受信用オブジェクト
	private String password; //パスワード
	private String user_name; //ユーザ名
	private int record; // 対局者情報
	private List list; //リストオブジェクト

	//コンストラクタ
	public Server_v1(int port) { //待ち受けポートを引数とする
		this.port = port; //待ち受けポートを渡す
		out = new PrintWriter [2]; //データ送信用オブジェクトを2クライアント分用意
		receiver = new Receiver [2]; //データ受信用オブジェクトを2クライアント分用意
		online = new boolean[2]; //オンライン状態管理用配列を用意
	}

	// データ受信用スレッド(内部クラス)
	class Receiver extends Thread {
		private InputStreamReader sisr; //受信データ用文字ストリーム
		private BufferedReader br; //文字ストリーム用のバッファ
		private int playerNo; //プレイヤを識別するための番号

		// 内部クラスReceiverのコンストラクタ
		Receiver (Socket socket, int playerNo){
			try{
				this.playerNo = playerNo; //プレイヤ番号を渡す
				sisr = new InputStreamReader(socket.getInputStream());
				br = new BufferedReader(sisr);
			} catch (IOException e) {
				System.err.println("データ受信時にエラーが発生しました: " + e);
			}
		}
		// 内部クラス Receiverのメソッド
		public void run(){
			try{
				while(true) {// データを受信し続ける
					String inputLine = br.readLine();//データを一行分読み込む
					if (inputLine != null){ //データを受信したら
						if(inputLine.equals("loginRequest")) {
							String user_name = br.readLine();
							String password = br.readLine();
							loginCheck(user_name,password);
						}
						else if(inputLine.equals("accountRequest")) {
								String user_name = br.readLine();
								String password = br.readLine();
								accountCreate(user_name, password);
						}
						else if(inputLine.equals("myPlayerRequest")) {
								playerInfo();
						}
						else if() {
							forwardMessage(inputLine, playerNo); //もう一方に転送する
						}
					}
				}
			} catch (IOException e){ // 接続が切れたとき
				System.err.println("プレイヤ " + playerNo + "との接続が切れました．");
				online[playerNo] = false; //プレイヤの接続状態を更新する
				printStatus(); //接続状態を出力する
			}
		}
	}


	/*********************メソッド***********************/

	//クライアントの接続(サーバの起動)
	public void acceptClient(){
		int i=0;
		try {
			System.out.println("サーバが起動しました．");
			ServerSocket ss = new ServerSocket(port); //サーバソケットを用意
			while (true) {
				Socket socket = ss.accept(); //新規接続を受け付ける
				new Receiver(socket,i).start(); //データ受信スレッドのスタート
				i++;
			}
		} catch (Exception e) {
			System.err.println("ソケット作成時にエラーが発生しました: " + e);
		}
	}


	//ログイン認証
	public boolean loginCheck(String user_name, String password) {

		return true;
	}

	//アカウント作成
	public boolean accountCreate(String user_name, String password) {
		Player_v2 player_obj;
       try {
    	   //FileOutputStreamオブジェクトの生成
    	   FileOutputStream outFile = new FileOutputStream("User.obj");
           //(ObjectOutputStreamオブジェクトの生成
    	   ObjectOutputStream outObject = new ObjectOutputStream(outFile);
    	   //FileInputStreamオブジェクトの生成
    	   FileInputStream inFile = new FileInputStream("User.obj");
    	   //ObjectInputStreamオブジェクトの生成
    	   ObjectInputStream inObject = new ObjectInputStream(inFile);

    	   while((Player_v2)inObject.readObject()!= null) {
    		   player_obj=(Player_v2)inObject.readObject();
    		   if(player_obj.getName().equals(user_name)) {
    	    	   outObject.close();  //オブジェクト出力ストリームのクローズ
    	      	   inObject.close();  //オブジェクト入力ストリームのクローズ
    	    	   outFile.close();  //ファイル出力ストリームのクローズ
    	    	   inFile.close();  //ファイル入力ストリームのクローズ
    			   return false;
    		   }
    	   }

    	   outObject.writeObject(new Player_v2(user_name, password));

    	   outObject.close();  //オブジェクト出力ストリームのクローズ
      	   inObject.close();  //オブジェクト入力ストリームのクローズ
    	   outFile.close();  //ファイル出力ストリームのクローズ
    	   inFile.close();  //ファイル入力ストリームのクローズ

       }
       catch(Exception e) {
    	   e.printStackTrace();
       }
      	   return true;
	}

	//Playerの対戦成績を送信
	public void playerInfo() {

	}
	//クライアント接続状態の確認
	public void printStatus(){

	}

	//データ更新
	public void dataUpdate() {

	}

	//対局者情報の転送
	public void sendPlayerInfo() {

	}

	//対局待ち状態受付 and 対戦者リスト転送
	public void sendList(){

;	}

	//対局申し込み転送
	public void requestGame(){

	}

	/*//先手後手情報の送信
	public void sendColor(int playerNo) {

	}*/

	//操作情報を転送
	public void forwardMessage(String msg, int playerNo) {

	}

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		Server_v1 server = new Server_v1(10000); //待ち受けポート10000番でサーバオブジェクトを準備
		server.acceptClient(); //クライアント受け入れを開始
	}

}