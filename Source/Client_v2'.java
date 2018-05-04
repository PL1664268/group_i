//ログイン画面はとりあえず完成

package othello;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

//パッケージのインポート
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Client extends JFrame implements MouseListener {
	private JButton buttonArray[];//オセロ盤用のボタン配列
	private JButton stop, pass; //停止、スキップ用ボタン
	private JLabel colorLabel; // 色表示用ラベル
	private JLabel turnLabel; // 手番表示用ラベル
	private Container c; // コンテナ
	private ImageIcon blackIcon, whiteIcon, boardIcon; //アイコン
	private PrintWriter out;//データ送信用オブジェクト
	private Receiver receiver; //データ受信用オブジェクト
	private Othello game; //Othelloオブジェクト
	private Player player; //Playerオブジェクト
	
	 /*********通信属性*********/
	  //private Receive receive;  //入力ストリーム用内部クラスのインスタンス変数
	  private BufferedReader in;  //入力ストリーム
	  private PrintWriter out1;  //出力ストリーム
	  private ObjectInputStream ois;  //オブジェクト入力ストリーム
	  private ObjectOutputStream oos;  //オブジェクト出力ストリーム※必要ないかも？
	  private Socket soc = null;  //ソケット

	// コンストラクタ
	
	
	
	public Client(Othello game, Player player) { //OthelloオブジェクトとPlayerオブジェクトを引数とする
		this.game = game; //引数のOthelloオブジェクトを渡す
		this.player = player; //引数のPlayerオブジェクトを渡す
		game.Othello();
		int [] grids = game.getGrids(); //getGridメソッドにより局面情報を取得
		int row = game.getRow(); //getRowメソッドによりオセロ盤の縦横マスの数を取得
		/*
		//ウィンドウ設定
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じる場合の処理
		setTitle("ネットワーク対戦型オセロゲーム");//ウィンドウのタイトル
		setSize(row * 45 + 10, row * 45 + 200);//ウィンドウのサイズを設定
		c = getContentPane();//フレームのペインを取得
		//アイコン設定(画像ファイルをアイコンとして使う)
		whiteIcon = new ImageIcon("White.jpg");
		blackIcon = new ImageIcon("Black.jpg");
		boardIcon = new ImageIcon("GreenFrame.jpg");
		c.setLayout(null);//
		//オセロ盤の生成
		buttonArray = new JButton[row * row];//ボタンの配列を作成
		for(int i = 0 ; i < row * row ; i++){
			if(grids[i]==1){ buttonArray[i] = new JButton(blackIcon);}//盤面状態に応じたアイコンを設定
			if(grids[i]==2){ buttonArray[i] = new JButton(whiteIcon);}//盤面状態に応じたアイコンを設定
			if(grids[i]==0){ buttonArray[i] = new JButton(boardIcon);}//盤面状態に応じたアイコンを設定
			c.add(buttonArray[i]);//ボタンの配列をペインに貼り付け
			// ボタンを配置する
			int x = (i % row) * 45;
			int y = (int) (i / row) * 45;
			buttonArray[i].setBounds(x, y, 45, 45);//ボタンの大きさと位置を設定する．
			buttonArray[i].addMouseListener(this);//マウス操作を認識できるようにする
			buttonArray[i].setActionCommand(Integer.toString(i));//ボタンを識別するための名前(番号)を付加する
		}
		//終了ボタン
		stop = new JButton("終了");//終了ボタンを作成
		c.add(stop); //終了ボタンをペインに貼り付け
		stop.setBounds(0, row * 45 + 30, (row * 45 + 10) / 2, 30);//終了ボタンの境界を設定
		stop.addMouseListener(this);//マウス操作を認識できるようにする
		stop.setActionCommand("stop");//ボタンを識別するための名前を付加する
		//パスボタン
		pass = new JButton("パス");//パスボタンを作成
		c.add(pass); //パスボタンをペインに貼り付け
		pass.setBounds((row * 45 + 10) / 2, row * 45 + 30, (row * 45 + 10 ) / 2, 30);//パスボタンの境界を設定
		pass.addMouseListener(this);//マウス操作を認識できるようにする
		pass.setActionCommand("pass");//ボタンを識別するための名前を付加する
		//色表示用ラベル
		String myName = player.getName();
		colorLabel = new JLabel(myName + "さんの色は未定です");//色情報を表示するためのラベルを作成
		colorLabel.setBounds(10, row * 45 + 60 , row * 45 + 10, 30);//境界を設定
		c.add(colorLabel);//色表示用ラベルをペインに貼り付け
		//手番表示用ラベル
		turnLabel = new JLabel("手番は" + game.getTurn() + "です");//手番情報を表示するためのラベルを作成
		turnLabel.setBounds(10, row * 45 + 120, row * 45 + 10, 30);//境界を設定
		c.add(turnLabel);//手番情報ラベルをペインに貼り付け
		
		*/
		
	}

	// メソッド
	public void Login() { //ログイン画面(未配置)
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じる場合の処理
		setTitle("Login");//ウィンドウのタイトル
		setSize(800, 550);//ウィンドウのサイズを設定
		//色
		Color back =new Color(55,113,184);
		Color text =new Color(247,247,247);
		Color textback =new Color(4,49,105);
		Color button =new Color(113,166,0);
		
		ImageIcon titleimage = new ImageIcon("titleimage.png");
		
		
		JPanel p = new JPanel();
		p.setLayout(null);
		p.setBackground(back);
		//ゲームタイトル
		JLabel title = new JLabel("OTHELLO");
		title.setForeground(text);
		title.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 36));
		title.setBounds(310,20,180,40);
		//名前
		JLabel name = new JLabel("Name");
		name.setForeground(text);
		name.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		name.setBounds(65,120,80,20);
		//名前入力
		JTextField username = new JTextField(24);
		username.setForeground(text);
		username.setBackground(textback);
		username.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 30));
		username.setBounds(60,160,300,70);
		//パスワード
		JLabel pass1 = new JLabel("Password");
		pass1.setForeground(text);
		pass1.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		pass1.setBounds(65,250,200,20);
		//パスワード入力
		JPasswordField password = new JPasswordField(24);
		password.setForeground(text);
		password.setBackground(textback);
		password.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 30));
		password.setBounds(60,290,300,70);
		//ログインボタン
		JButton login = new JButton("Login");
		login.setForeground(text);
		login.setBackground(button);
		login.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		login.setBounds(475,350,250,50);
		login.setOpaque(true);
		login.setBorderPainted(false);
		//新規アカウント登録ボタン
		JButton newuser = new JButton("Newuser");
		newuser.setForeground(text);
		newuser.setBackground(button);
		newuser.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		newuser.setBounds(475,440,250,50);
		newuser.setOpaque(true);
		newuser.setBorderPainted(false);
		//画像
		JLabel titleimg = new JLabel(titleimage);
		titleimg.setBounds(430, 70, 321, 257);
		
		JLabel loginfailed = new JLabel("ログインできませんでした");
		
		p.add(title);
		p.add(name);
		p.add(username);
		p.add(pass1);
		p.add(password);
		p.add(login);
		p.add(newuser);
		p.add(titleimg);
		add(p);
		
		login.addActionListener(new ActionListener() { // ログインボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				 connectServer("localhost", 10000);
				 char[] pass2 = password.getPassword(); //char型配列をStringに変換
				 String pass3 = new String(pass2);
				 loginRequest(username.getText(),pass3);
				 if( loginRequest(username.getText(),pass3)==false) {
					 p.add(loginfailed);
				 }
				 if( loginRequest(username.getText(),pass3)==true) {
					 userhome();
					 p.removeAll();
				 }
				 userhome();
				 p.removeAll();
        }
		});
		newuser.addActionListener(new ActionListener() { //アカウント登録ボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				 newuser();
				 p.removeAll();
				 }
		});
		setVisible(true);	
	}
	
	  public boolean loginRequest(String userName,String password) { //ログイン要求
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
		return false;
	  }
	
	public void newuser() { //アカウント作成画面
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じる場合の処理
		setTitle("NewUser");//ウィンドウのタイトル
		setSize(370, 560);//ウィンドウのサイズを設定
		JPanel p = new JPanel();
		JLabel title = new JLabel("NewUser");
		JLabel name = new JLabel("name");
		JTextField username = new JTextField(16);
		JLabel pass1 = new JLabel("password");
		JPasswordField password = new JPasswordField(16);
		JButton login = new JButton("登録");
		JButton returntologin = new JButton("戻る");
		JLabel newaccountfailed = new JLabel("この名前はアカウント登録できません");
		
		p.add(title);
		p.add(name);
		p.add(username);
		p.add(pass1);
		p.add(password);
		p.add(login);
		p.add(returntologin);
		add(p);
		
		login.addActionListener(new ActionListener() { // 登録ボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				 char[] pass2 = password.getPassword(); //char型配列をStringに変換
				 String pass3 = new String(pass2);
				 accountRequest(username.getText(),pass3);
				 if (accountRequest(username.getText(),pass3)==false) {
					 p.add(newaccountfailed);
				 }
				 if (accountRequest(username.getText(),pass3)==true) {
					 userhome();
					 p.removeAll();
				 }
			 }
		});
		returntologin.addActionListener(new ActionListener() { // 戻るボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				Login();
				p.removeAll();
			 }
		});
		setVisible(true);	
	}
	
	  public boolean accountRequest(String userName,String password) { //アカウント作成要求
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
		return false;
	  }
	
	public void userhome() { //ユーザーホーム画面
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じる場合の処理
		setTitle("Login");//ウィンドウのタイトル
		setSize(370, 560);//ウィンドウのサイズを設定
		JLabel title = new JLabel("ユーザーホーム");
		JLabel name = new JLabel();
		JButton playgame = new JButton("対局する");
		JButton score = new JButton("成績を見る");
		JButton logout = new JButton("ログアウト");
		JPanel p = new JPanel();
		
		p.add(title);
		p.add(playgame);
		p.add(score);
		p.add(logout);
		add(p);
		
		logout.addActionListener(new ActionListener() { // ログアウトボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				Login();
				p.removeAll();
			 }
		});
		
		playgame.addActionListener(new ActionListener() { // 対局ボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				gamewaiting();
				p.removeAll();
			 }
		});
		
		score.addActionListener(new ActionListener() { // 成績ボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				score();
				p.removeAll();
			 }
		});
		
		setVisible(true);
	}
	
	public void gamewaiting() {
		
	}
	
	public void score() {
		
	}
	
	public void playothello() {
		
	}
	
	public void connectServer(String ipAddress, int port){	// サーバに接続
		Socket socket = null;
		try {
			socket = new Socket(ipAddress, port); //サーバ(ipAddress, port)に接続
			out = new PrintWriter(socket.getOutputStream(), true); //データ送信用オブジェクトの用意
			receiver = new Receiver(socket); //受信用オブジェクトの準備
			receiver.start();//受信用オブジェクト(スレッド)起動
		} catch (UnknownHostException e) {
			System.err.println("ホストのIPアドレスが判定できません: " + e);
			System.exit(-1);
		} catch (IOException e) {
			System.err.println("サーバ接続時にエラーが発生しました: " + e);
			System.exit(-1);
		}
	}

	public void sendMessage(String msg){	// サーバに操作情報を送信
		out.println(msg);//送信データをバッファに書き出す
		out.flush();//送信データを送る
		System.out.println("サーバにメッセージ " + msg + " を送信しました"); //テスト標準出力
	}

	// データ受信用スレッド(内部クラス)
	class Receiver extends Thread {
		private InputStreamReader sisr; //受信データ用文字ストリーム
		private BufferedReader br; //文字ストリーム用のバッファ

		// 内部クラスReceiverのコンストラクタ
		Receiver (Socket socket){
			try{
				sisr = new InputStreamReader(socket.getInputStream()); //受信したバイトデータを文字ストリームに
				br = new BufferedReader(sisr);//文字ストリームをバッファリングする
			} catch (IOException e) {
				System.err.println("データ受信時にエラーが発生しました: " + e);
			}
		}
		// 内部クラス Receiverのメソッド
		public void run(){
			try{
				while(true) {//データを受信し続ける
					String inputLine = br.readLine();//受信データを一行分読み込む
					if (inputLine != null){//データを受信したら
						receiveMessage(inputLine);//データ受信用メソッドを呼び出す
					}
				}
			} catch (IOException e){
				System.err.println("データ受信時にエラーが発生しました: " + e);
			}
		}
	}

	public void receiveMessage(String msg){	// メッセージの受信
		System.out.println("サーバからメッセージ " + msg + " を受信しました"); //テスト用標準出力
	}
	public void updateDisp(){// 画面を更新する
		
		
	}
	public void acceptOperation(String command){	// プレイヤの操作を受付
	}

  	//マウスクリック時の処理
	public void mouseClicked(MouseEvent e) {
		JButton theButton = (JButton)e.getComponent();//クリックしたオブジェクトを得る．キャストを忘れずに
		String command = theButton.getActionCommand();//ボタンの名前を取り出す
		game.putStone(Integer.parseInt(command), game.getTurn());
		game.chengeTurn();
		game.getGrids();
		updateDisp();
		System.out.println("マウスがクリックされました。押されたボタンは " + command + "です。");//テスト用に標準出力
		sendMessage(command); //テスト用にメッセージを送信
	}
	public void mouseEntered(MouseEvent e) {}//マウスがオブジェクトに入ったときの処理
	public void mouseExited(MouseEvent e) {}//マウスがオブジェクトから出たときの処理
	public void mousePressed(MouseEvent e) {}//マウスでオブジェクトを押したときの処理
	public void mouseReleased(MouseEvent e) {}//マウスで押していたオブジェクトを離したときの処理

	//テスト用のmain
	public static void main(String args[]){
		
		//最初にLogin()を起動させないといけないから後で順番入れ替え
		
		Player player = new Player(); //プレイヤオブジェクトの用意(ログイン)
		player.setName("a"); //名前を受付
		Othello game = new Othello(); //オセロオブジェクトを用意
		Client oclient = new Client(game, player); //引数としてオセロオブジェクトを渡す
		oclient.Login();
	}
}