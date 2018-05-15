package ohtello;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	private int MAX=100;
	private int port; //ポート番号
	private boolean [] online; //クライアント接続状態
	private PrintWriter [] out; //データ送信用オブジェクト
	private Receiver [] receiver; //データ受信用オブジェクト
	private String password; //パスワード
	private String user_name; //ユーザ名
	ArrayList<Player> game_online_list = new ArrayList<Player>(); //リストオブジェクト
	  Receiver receiveThread[ ]= new Receiver[MAX];      // チャットクラス配列
	  //Thread receiveThread[ ] = new Thread[MAX];   // クライアントとの入出力スレッド
	 

	//コンストラクタ
	public Server(int port) { //待ち受けポートを引数とする
		this.port = port; //待ち受けポートを渡す
		out = new PrintWriter [2]; //データ送信用オブジェクトを2クライアント分用意
		receiver = new Receiver [2]; //データ受信用オブジェクトを2クライアント分用意
		online = new boolean[2]; //オンライン状態管理用配列を用意
	}

	// データ受信用スレッド(内部クラス)
	class Receiver extends Thread{
		private InputStreamReader sisr; //受信データ用文字ストリーム
		private BufferedReader br; //文字ストリーム用のバッファ
		private PrintWriter out;
		private int ThreadNo; //プレイヤを識別するための番号
		private Player player = new Player("dammy", "dammy");
		// 内部クラスReceiverのコンストラクタ
		Receiver (Socket socket, Server server, int ThreadNo){
			try{
				this.ThreadNo = ThreadNo; //プレイヤ番号を渡す
				sisr = new InputStreamReader(socket.getInputStream());
				br = new BufferedReader(sisr);
				out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				System.out.println(ThreadNo+"がせつぞくしました");
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
							System.out.println(user_name);
							System.out.println(password);
							String msg = loginCheck(user_name,password);
							out.println(msg);
							out.println(ThreadNo);
							out.flush();
							System.out.println("server message sent");
						}
						else if(inputLine.equals("accountRequest")) {
								String user_name = br.readLine();
								String password = br.readLine();
								System.out.println(user_name);
								System.out.println(password);
								String msg = accountCreate(user_name, password);
								out.println(msg);
								out.flush();
								System.out.println("server message sent");
						}
						else if(inputLine.equals("myPlayerRequest")) {
								String user_name = br.readLine();
								Player player = playerInfo(user_name);
								out.println(player);
								out.flush();
								System.out.println("server message sent");
						}
						else if(inputLine.equals("dataUpdate")){
							String user_name = br.readLine();
							String result = br.readLine();
							System.out.println(user_name);
							System.out.println(result);
							dataUpdate(user_name, result);
/*							out.println(msg);
							out.flush();
							System.out.println("server message sent");
*/						}
						else if(inputLine.equals("otherPlayerRequest")){
							boolean flag = false;
							if(!flag){
								game_online_list.add(player);
								out.println(game_online_list);
								flag=true;
							}else 	out.println(game_online_list);
						}
						else if(inputLine.equals("requestGame")){
							int opponent = br.read();
							requestGame(opponent);
						}
						else if(inputLine.equals("forwardMessage")) {
							forwardMessage(inputLine, ThreadNo); //もう一方に転送する
						}
					}
				}
			} catch (IOException e){ // 接続が切れたとき
				System.err.println("プレイヤ " + ThreadNo + "との接続が切れました．");
				game_online_list.remove(game_online_list.indexOf(player));
				online[ThreadNo] = false; //プレイヤの接続状態を更新する
				printStatus(); //接続状態を出力する
			}
		}
		public synchronized void sendMessage(String message) {
		    out.println(message);         // スレッドが応対するクライアントに送信
		    out.flush( );             // バッファ内のデータを強制的に送信
		  }
	}


	/*********************メソッド***********************/

	//クライアントの接続(サーバの起動)
	public void acceptClient(){
		int i=1;
		try {
			System.out.println("サーバが起動しました．");
			ServerSocket ss = new ServerSocket(port); //サーバソケットを用意
			while (true) {
			       int p;
			        for (p = 0; p < MAX; p++){      // 接続チェック
			          if (receiveThread[p] == null)    // 空いている場合
			            break;
			        }
			        if (p == MAX)           // 空いていない場合
			            continue;             // 以下の処理をしない

				Socket socket = ss.accept(); //新規接続を受け付ける
				receiveThread[p] = new Receiver(socket, this, p);      
		        receiveThread[p].start( );       // スレッドスタート
		     }
		} catch (Exception e) {
			System.err.println("ソケット作成時にエラーが発生しました: " + e);
		}
	}


	//ログイン認証
	public String loginCheck(String user_name, String password) {
		try{
			//FileInputStreamオブジェクトの生成
			FileInputStream inFile = new FileInputStream("players.obj");
	        //オブジェクトの読み込み
            Player player;

            try{
            while(true){
            	//ObjectInputStreamオブジェクトの生成
            	ObjectInputStream inObject = new ObjectInputStream(inFile);
            	player = (Player)inObject.readObject();
            	if(player.getName().equals(user_name) && player.getPassword().equals(password)){
            		inObject.close();
            		System.out.println("login permit");
            		return "permit";
            	}
              }
            }catch(EOFException e){
    		}
            finally{
            }


		}catch(Exception e){

		}
   		System.out.println("No permit");
		return "notPermit";
	}

	//アカウント作成
	public String accountCreate(String user_name, String password) {


		try {
			//FileInputStreamオブジェクトの生成
            FileInputStream inFile = new FileInputStream("players.obj");

            //オブジェクトの読み込み
            Player player;

            try{
            while(true){
            	//ObjectInputStreamオブジェクトの生成
            	ObjectInputStream inObject = new ObjectInputStream(inFile);
            	player = (Player)inObject.readObject();
            	System.out.println(player.getName());
            	if(player.getName().equals(user_name)){
            		inObject.close();
            		System.out.println("false");
            		return "notPermit";
            	}
             }
            }catch(EOFException e){
    		}
            
            //FileOutputStreamオブジェクトの生成
            FileOutputStream outFile = new FileOutputStream("players.obj",true);
            //ObjectOutputStreamオブジェクトの生成
            ObjectOutputStream outObject = new ObjectOutputStream(outFile);
            //クラスHelloのオブジェクトの書き込み
            outObject.writeObject(new Player(user_name,password));

            outObject.close();
       }
       catch(Exception e) {
    	   e.printStackTrace();
       }
		System.out.println("true");
        return "permit";
	}

	//Playerの対戦成績を送信
	public Player playerInfo(String user_name) {
		//Playerオブジェクトを格納する変数
		Player player = new Player("dammy", "dammy");

        try{
        	//FileInputStreamオブジェクトの生成
            FileInputStream inFile = new FileInputStream("players.obj");

       //該当するオブジェクトを探索
       while(true){
        	//ObjectInputStreamオブジェクトの生成
        	ObjectInputStream inObject = new ObjectInputStream(inFile);
        	player = (Player)inObject.readObject(); //読み込み
        	System.out.println(player.getName());
        	if(player.getName().equals(user_name)){
        		inObject.close();
        		System.out.println("sent your info");
        		break;
        	}
         }
        }
        catch(Exception e){
		}
       return player; //オブジェクトをリターン、クライアントへ送る。
	}

	//クライアント接続状態の確認
	public void printStatus(){
		int i=0;
		while(i<=online.length) {
			if(online[i]==true)
				System.out.println("PlayerNo"+i+"はオンライン状態です");
			else if(online[i]==false)
				System.out.println("PlayerNo"+i+"はオフライン状態です");
		}
	}

	//データ更新
	public void dataUpdate(String user_name, String result) {
		Player player;
		ObjectInputStream inObject;
		ArrayList<Player> arr = new ArrayList<Player>();
        try{
        	//FileInputStreamオブジェクトの生成
            FileInputStream inFile = new FileInputStream("players.obj");
            //データ更新をするオブジェクトを探す
            while(true){
            	//ObjectInputStreamオブジェクトの生成
            	inObject = new ObjectInputStream(inFile);
            	player = (Player)inObject.readObject(); //読み込み
            	
            	System.out.println(player.getName());
            	System.out.println(player.getWin());
            	if(player.getName().equals("user2")){
               		//結果に応じてデータを更新
               		if(result=="win"){
               			player.setWin(player.getWin()+1);
               		}else if(result=="lose"){
               			player.setDefeat(player.getDefeat()+1);

               		}else if(result=="draw"){
               			player.setDraw(player.getDraw()+1);

               		}else if(result=="surrender"){
               			player.setSurrender(player.getSurrender()+1);
               		}
                      	}
            	arr.add(player);
            }
   
         }catch(Exception e){ 
        } finally{
        	 System.out.println("中間");
        }
        
        try{
           	int i=0;
            for(Player p : arr){
            	if(i==0){
                    FileOutputStream outFile1 = new FileOutputStream("players.obj");
                    //FileOutputStreamオブジェクトの生成
                   	ObjectOutputStream outObject1 = new ObjectOutputStream(outFile1);
            		outObject1.writeObject(p);  
            		i++;
            	}
            	else{
                    //FileOutputStreamオブジェクトの生成
                    FileOutputStream outFile2 = new FileOutputStream("players.obj",true);
                    //FileOutputStreamオブジェクトの生成
                   	ObjectOutputStream outObject2 = new ObjectOutputStream(outFile2);
                     		outObject2.writeObject(p);  

            	}
            	
            	System.out.println(p.getName());
            }
          
                
               	
            }catch(Exception e){
        	
        }
	}

	/*//対局待ち状態受付 and 対戦者リスト転送
	public void sendList(){

	}*/

	//対局申し込み転送
	public void requestGame(int opponent,){
		game_online_list.get(opponent);
		receiveThread[opponent].sendMessage("requestGame");
	}

	//操作情報を転送
	public void forwardMessage(String msg, int playerNo) {

	}

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		Server server = new Server(10005); //待ち受けポート10000番でサーバオブジェクトを準備
		server.acceptClient(); //クライアント受け入れを開始
	}

}
