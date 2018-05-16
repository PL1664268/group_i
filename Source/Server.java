package othello;

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
import java.util.HashMap;

public class Server {
	private int MAX=100; //最大接続数
	private int port; //ポート番号
	private boolean [] online; //クライアント接続状態
	ArrayList<Player> game_online_list = new ArrayList<Player>(); //対戦待ち状態のArrayList
	Receiver receiveThread[] = new Receiver[MAX];  // 受信クラス配列、スレッドの配列

	//コンストラクタ
	public Server(int port) { //待ち受けポートを引数とする
		this.port = port; //待ち受けポートを渡す
		online = new boolean[MAX]; //オンライン状態管理用配列を用意
	}

	/**********************データ受信用スレッド(内部クラス)**********************/
	class Receiver extends Thread{
		private InputStreamReader sisr; //受信データ用文字ストリーム
		private BufferedReader br; //文字ストリーム用のバッファ
		private PrintWriter out; //データ送信用オブジェクト
		private ObjectInputStream ois;
		private ObjectOutputStream oos;
		private int ThreadNo; //プレイヤを識別するための番号,スレッド番号
		private Player player = new Player("dammy", "dammy"); //playerオブジェクト
		private boolean flag = false; //対局待ちリストのためのフラグ。1回目と2回目以降の要求で処理が変わる。
		private String user_name; //このスレッドを利用しているユーザ名
		Socket socket;
		HashMap<Player,Player> map = new HashMap<Player,Player>(); //対戦中の相手と紐づけるためのHashMap

		//内部クラスReceiverのコンストラクタ
		Receiver (Socket socket, Server server, int ThreadNo){
			try{
				this.socket=socket;
				this.ThreadNo = ThreadNo; //Thread番号を渡す
				sisr = new InputStreamReader(socket.getInputStream());
				br = new BufferedReader(sisr); //入力ストリームを作成
				out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream())); //出力ストリームを作成
				
				System.out.println(ThreadNo+"がせつぞくしました");
			} catch (IOException e) {
				System.err.println("データ受信時にエラーが発生しました: " + e);
			}
		}

		//runメソッド
		public void run(){
			try{
				while(true) {// データを受信し続ける
					String inputLine = br.readLine();//データを一行分読み込む
					if (inputLine != null){//データを受信したら,先頭メッセージによってメソッドを呼ぶ

						//ログイン認証のリクエストなら
						if(inputLine.equals("loginRequest")) {
							//ユーザ名とパスワードを受け取る
							user_name = br.readLine();
							String password = br.readLine();
							System.out.println(user_name);
							System.out.println(password);
							//ログインできるかを確認
							String msg = loginCheck(user_name,password,ThreadNo);
							out.println(msg);
							//できるなら、結果・スレッド番号・そのPlayerオブジェクトを送る
							if(msg.equals("permit")) {
								online[ThreadNo]=true;
							}
							out.flush();
							System.out.println("server message sent 1");
						}
						//アカウント作成のリクエストなら
						else if(inputLine.equals("accountRequest")) {
								//ユーザ名とパスワードを受け取る
								user_name = br.readLine();
								String password = br.readLine();
								System.out.println(user_name);
								System.out.println(password);
								//アカウント作成ができるか確認
								String msg = accountCreate(user_name, password,ThreadNo);
								out.println(msg);
								//できるなら、結果・スレッド番号・そのPlayerオブジェクトを送る
								if(msg.equals("permit")) {
									online[ThreadNo]=true;
								}
								out.flush();
								
								System.out.println("server message sent 2");
						}
						//対戦成績のリクエストなら
						else if(inputLine.equals("myPlayerRequest")) {
							    //ユーザ名を受け取り、探索して該当オブジェクトを返す
							    System.out.println("myPR受信");
								Player player = playerInfo(user_name);
								player.ThreadNo=ThreadNo;
								System.out.println("Playerオブジェクト作成");
								out.close();
								oos = new ObjectOutputStream(socket.getOutputStream());
								oos.writeObject(player);
								oos.flush();
								oos.close();
								out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
								System.out.println("server message sent 3");
						}
						//データ更新のリクエストなら
						else if(inputLine.equals("dataUpdate")){
							//ユーザ名と結果を受け取り、データを更新
							Player newPlayer;
							try {
								br.close();
								ois = new ObjectInputStream(socket.getInputStream()); //出力ストリームを作成
								newPlayer = (Player)ois.readObject();
								ois.close();
								br = new BufferedReader(sisr); 
								dataUpdate(newPlayer);
							} catch (ClassNotFoundException e) {
								// TODO 自動生成された catch ブロック
								e.printStackTrace();
							}

						}
						//対局待ち状態のリストのリクエストなら
						else if(inputLine.equals("otherPlayerRequest")){
							//対局待ち状態に遷移してのリクエストなら
							if(!flag){
								//自分自身を待ち状態リストに入れ、リストを送る
								game_online_list.add(player);
								out.println(game_online_list);
								out.flush();
								flag=true;
							}
							//updateによる再送信のリクエストなら
							else{
								out.println(game_online_list); //現在の待ち状態リストを送る
								out.flush();
							}
						}
						//他者への対局申し込みなら
						else if(inputLine.equals("requestGame")){
							//対局者のThreadNoと自身のlist番号を受け取り
							int opponent = br.read();
							String applier = String.valueOf(br.read());
							map.put(player, receiveThread[opponent].player);
							receiveThread[opponent].map.put(receiveThread[opponent].player, player);
							//対局を申し込む
							requestGame(opponent,applier);
						}
						//申し込に対する答え
						else if(inputLine.equals("Answear")) {
							String ans = br.readLine();
							if(ans.equals("Yes")) {
								receiveThread[map.get(player).ThreadNo].sendMessage("Yes");
							}
							else if(ans.equals("No")) {
								receiveThread[map.get(player).ThreadNo].sendMessage("No");
								receiveThread[map.get(player).ThreadNo].map.remove(receiveThread[map.get(player).ThreadNo].player);
								map.remove(player);
							}
						}
						//対局中のデータ転送のリクエストなら
						else if(inputLine.equals("forwardMessage")) {
							String color = br.readLine();
							String operation = br.readLine();
							forwardMessage(color, operation, map.get(player).ThreadNo); //もう一方に転送する
						}
					}
				}
			} catch (IOException e){ // 接続が切れたとき
				System.err.println("プレイヤ " + ThreadNo + "との接続が切れました．");
				System.out.println(e);
				game_online_list.remove(game_online_list.indexOf(player)); //対局待ち状態リストから削除する
				online[ThreadNo] = false; //プレイヤの接続状態を更新する
				flag=false;
				printStatus(); //接続状態を出力する
			}
		}
		public synchronized void sendMessage(String message) {
		    out.println(message);         // スレッドが応対するクライアントに送信
		    out.flush();             // バッファ内のデータを強制的に送信
		  }
	}
	/**********************************受信スレッド(内部クラス)の終わり*******************************/


/****************************************メソッド**************************************************/

	//クライアントの接続(サーバの起動)
	public void acceptClient(){
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
	public String loginCheck(String user_name, String password, int ThreadNo) {
		try{
			Player player;
			//FileInputStreamオブジェクトの生成
			FileInputStream inFile = new FileInputStream("players.obj");

            try{
            while(true){
            	//ObjectInputStreamオブジェクトの生成
            	ObjectInputStream inObject = new ObjectInputStream(inFile);
            	player = (Player)inObject.readObject(); //オブジェクトの読み込み

            	//もし、ユーザ名とパスワードが一致するなら
            	if(player.getName().equals(user_name) && player.getPassword().equals(password)){
            		receiveThread[ThreadNo].player=player; //そのplayerオブジェクトを取り出す
            		inObject.close();
            		System.out.println("login permit");
            		return "permit"; //ログインを許可する
            	}

              }
            }catch(EOFException e){
    		}
		}catch(Exception e){

		}
   		//一致しなかったら、許可しない
		System.out.println("No permit");
		return "notPermit";
	}

	//アカウント作成
	public String accountCreate(String user_name, String password, int ThreadNo) {
		try {
			Player player;
			//FileInputStreamオブジェクトの生成
            FileInputStream inFile = new FileInputStream("players.obj");

            try{
            while(true){
            	//ObjectInputStreamオブジェクトの生成
            	ObjectInputStream inObject = new ObjectInputStream(inFile);
            	player = (Player)inObject.readObject(); //オブジェクトの読み込み
            	System.out.println(player.getName());

            	//もし、同じ名前のユーザが既に存在しているなら
            	if(player.getName().equals(user_name)){
            		inObject.close();
            		System.out.println("false");
            		return "notPermit"; //アカウント作成を不許可とする
            	}
             }
            }catch(EOFException e){
    		}

            //名前の重複がなく、新規アカウントが作成できるなら↓↓↓

            //FileOutputStreamオブジェクトの生成
            FileOutputStream outFile = new FileOutputStream("players.obj",true);
            //ObjectOutputStreamオブジェクトの生成
            ObjectOutputStream outObject = new ObjectOutputStream(outFile);

            //クラスPlayerのオブジェクトの書き込み
            player = new Player(user_name,password);
            outObject.writeObject(player);
            receiveThread[ThreadNo].player=player;
            outObject.close();
       }
       catch(Exception e) {
    	   e.printStackTrace();
       }
		//アカウント作成の許可
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
	public void dataUpdate(Player newPlayer) {
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
            	if(player.getName().equals("user2")){
            	player = newPlayer;
		}
            	arr.add(player);
            }

         }catch(Exception e){
         }

        try{
           	boolean flag=false;
            for(Player p : arr){
            	if(flag==false){
                    FileOutputStream outFile1 = new FileOutputStream("players.obj");
                    //FileOutputStreamオブジェクトの生成
                   	ObjectOutputStream outObject1 = new ObjectOutputStream(outFile1);
            		outObject1.writeObject(p);
            		flag=true;
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
	public void requestGame(int opponent,String applier_list_num){
//		game_online_list.get(opponent);
		receiveThread[opponent].sendMessage("requestGame");
		receiveThread[opponent].sendMessage(applier_list_num);
	}

	//操作情報を転送
	public void forwardMessage(String msg1, String msg2, int ThreadNo) {
		receiveThread[ThreadNo].sendMessage(msg1);
		receiveThread[ThreadNo].sendMessage(msg2);
	}

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		Server server = new Server(10013); //待ち受けポート10000番でサーバオブジェクトを準備
		server.acceptClient(); //クライアント受け入れを開始
	}

}
