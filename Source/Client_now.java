
package othello;

import java.awt.Color;
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
import java.util.ArrayList;

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
	private ImageIcon blackIcon, whiteIcon, boardIcon; //アイコン
	private PrintWriter out;//データ送信用オブジェクト
	private Othello game; //Othelloオブジェクト
	private Player myPlayer;  //自分のPlayerクラスのインスタンス変数
	private Player player; //Playerオブジェクト
	private String playername = "Player";
	private Player opponent;  //対局相手のインスタンス変数
	private Player[] otherPlayer;
	private int stopRecFlag = 0;  //通信時の受信のループを抜けるためのフラグ
	private String mycolor;
	ArrayList<Player> playerlist = new ArrayList<Player>();
	private int playernumber;
	private int arraysize; //オンライン人数
	
	int row = 8; //getRowメソッドによりオセロ盤の縦横マスの数を取得
	JPanel Board = new JPanel();
	JPanel matching = new JPanel();
	JButton Yes = new JButton("Yes");
	JButton No = new JButton("No");
	JLabel opponentname = new JLabel();
	JLabel opponentrate = new JLabel();
	
	 /*********通信属性*********/
	  //private Receive receive;  //入力ストリーム用内部クラスのインスタンス変数
	  private BufferedReader in;  //入力ストリーム
	  private PrintWriter out1;  //出力ストリーム
	  private ObjectInputStream ois;  //オブジェクト入力ストリーム
	  private ObjectOutputStream oos;  //オブジェクト出力ストリーム※必要ないかも？
	  private Socket soc = null;  //ソケット

	// コンストラクタ
	public Client(Othello game) {
		this.game = game; //引数のOthelloオブジェクトを渡す
	}

	// メソッド
	public void Login() { //ログイン画面
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じる場合の処理
		setTitle("Login");//ウィンドウのタイトル
		setSize(800, 550);//ウィンドウのサイズを設定
		//色
		Color back =new Color(55,113,184);
		Color text =new Color(247,247,247);
		Color textback =new Color(4,49,105);
		Color button =new Color(113,166,0);
		Color error =new Color(230,92,0);
		
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
		
				//【臨時】ホーム画面遷移用ボタン
				JButton home = new JButton("home");
				home.setForeground(text);
				home.setBackground(button);
				home.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 1));
				home.setBounds(10,10,10,10);
				home.setOpaque(true);
				home.setBorderPainted(false);
		//サーバー接続失敗
		JLabel serverfailed = new JLabel();
		serverfailed.setForeground(error);
		serverfailed.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 20));
		serverfailed.setBounds(440,395,500,50);
		//ログイン失敗
		JLabel loginfailed = new JLabel();
		loginfailed.setForeground(error);
		loginfailed.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 20));
		loginfailed.setBounds(535,395,500,50);
		
		p.add(title);
		p.add(name);
		p.add(username);
		p.add(pass1);
		p.add(password);
		p.add(login);
		p.add(newuser);
		p.add(titleimg);
		p.add(home);
		add(p);
		
		login.addActionListener(new ActionListener() { // ログインボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				 boolean connectresult = connectServer("localhost", 10027);
				 if( connectresult == false) {
					 serverfailed.setText("Could not connect to the server!");
				 }
				 if( connectresult == true) {
					 char[] pass2 = password.getPassword(); //char型配列をStringに変換
					 String pass3 = new String(pass2);
					 boolean flag = loginRequest(username.getText(),pass3);
					 if(flag==false) {
						 loginfailed.setText("Login failed");
					 }
					 if(flag==true) {
						 myPlayerRequest();
						 playername = myPlayer.getName();
						 System.out.println("自分のスレッドナンバー : " + myPlayer.ThreadNo);
						 userhome();
						 p.removeAll();
					 }
				 }
        }
		});
		newuser.addActionListener(new ActionListener() { //アカウント登録ボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				 Newuser();
				 p.removeAll();
				 }
		});
		
		home.addActionListener(new ActionListener() { //臨時ボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				 userhome();
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
	      out.println(userName);
	      out.println(password);
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
	    }
		return false;
	  }
	
	public void Newuser() { //アカウント作成画面
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じる場合の処理
		setTitle("Sign up");//ウィンドウのタイトル
		setSize(800, 550);//ウィンドウのサイズを設定
		
		//色
		Color back =new Color(55,113,184);
		Color text =new Color(247,247,247);
		Color textback =new Color(4,49,105);
		Color button =new Color(113,166,0);
		Color error =new Color(230,92,0);
		
		ImageIcon titleimage = new ImageIcon("titleimage.png");
		
		JPanel p = new JPanel();
		p.setLayout(null);
		p.setBackground(back);
		//タイトル
		JLabel title = new JLabel("SIGN UP");
		title.setForeground(text);
		title.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 36));
		title.setBounds(320,20,600,40);
		//名前
		JLabel name = new JLabel("Name");
		name.setForeground(text);
		name.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		name.setBounds(65,120,80,20);
		//名前入力
		JTextField username = new JTextField(16);
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
		JPasswordField password = new JPasswordField(16);
		password.setForeground(text);
		password.setBackground(textback);
		password.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 30));
		password.setBounds(60,290,300,70);
		//パスワード確認
		JLabel pass2 = new JLabel("Password");
		pass2.setForeground(text);
		pass2.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		pass2.setBounds(65,390,200,20);
		//パスワード入力2
		JPasswordField password2 = new JPasswordField(16);
		password2.setForeground(text);
		password2.setBackground(textback);
		password2.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 30));
		password2.setBounds(60,430,300,70);
		//登録ボタン
		JButton login = new JButton("Sign up");
		login.setForeground(text);
		login.setBackground(button);
		login.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		login.setBounds(475,350,250,50);
		login.setOpaque(true);
		login.setBorderPainted(false);
		//戻るボタン
		JButton returntologin = new JButton("Back");
		returntologin.setForeground(text);
		returntologin.setBackground(button);
		returntologin.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		returntologin.setBounds(475,440,250,50);
		returntologin.setOpaque(true);
		returntologin.setBorderPainted(false);
		//サーバー接続失敗
		JLabel serverfailed = new JLabel();
		serverfailed.setForeground(error);
		serverfailed.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 20));
		serverfailed.setBounds(440,395,500,50);
		//アカウント登録失敗
		JLabel newaccountfailed = new JLabel();
		newaccountfailed.setForeground(error);
		newaccountfailed.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 20));
		newaccountfailed.setBounds(140,120,600,20);
		//パスワード不一致
		JLabel passwordfailed = new JLabel();
		passwordfailed.setForeground(error);
		passwordfailed.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		passwordfailed.setBounds(200,250,200,20);
		//画像
		JLabel titleimg = new JLabel(titleimage);
		titleimg.setBounds(430, 70, 321, 257);
		
		p.add(title);
		p.add(name);
		p.add(username);
		p.add(pass1);
		p.add(password);
		p.add(pass2);
		p.add(password2);
		p.add(login);
		p.add(returntologin);
		p.add(titleimg);
		p.add(passwordfailed);
		p.add(serverfailed);
		p.add(newaccountfailed);
		add(p);
		
		
		
		login.addActionListener(new ActionListener() { // 登録ボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				 
				char[] pass = password.getPassword(); //char型配列をStringに変換
				String passstring1 = new String(pass);
				char[] pass3 = password2.getPassword(); //char型配列をStringに変換
				String passstring2 = new String(pass3);
					
				 if(!(passstring2.equals(passstring1))) {
					 passwordfailed.setText("Mismatch");
				 }
				 if(passstring2.equals(passstring1)) {
					 boolean connectresult = connectServer("localhost", 10026);
					 if( connectresult == false) {
						 serverfailed.setText("Could not connect to the server!");
					 }
					 if(connectresult == true) {
						 
						 boolean flag = accountRequest(username.getText(),passstring1);
						 if (flag ==false) {
							 newaccountfailed.setText("This name is unavailable");
						 }
						 if (flag ==true) {
							 System.out.println("プレイヤーオブジェクト未受信");
							 myPlayerRequest();
							 System.out.println("プレイヤーオブジェクト受信");
							 playername = myPlayer.getName();
							 System.out.println("自分のスレッドナンバー : " + myPlayer.ThreadNo);
							 userhome();
							 p.removeAll();
						 }
					 }
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
	      out.println(userName);
	      out.println(password);
	      out.flush();
	      String isPer = in.readLine();
	      if(isPer.equals("permit") == true) {  //新規作成できる
	        return true;
	      } else if(isPer.equals("notPermit") == true) {  //新規作成できる
	        return false;
	      } else {
	        System.out.println("確認とは別の文字列です。");  //別の文字列が送られてきた
	      }
	      //out.close();
	      //in.close();
	    }catch(IOException e) {
	      System.out.println(e);
	    }
		return false;
	  }
	
	public void userhome() { //ユーザーホーム画面
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じる場合の処理
		setTitle("Home");//ウィンドウのタイトル
		setSize(800, 550);//ウィンドウのサイズを設定
		
		//色
		Color back =new Color(55,113,184);
		Color text =new Color(247,247,247);
		Color textback =new Color(4,49,105);
		Color button =new Color(113,166,0);
		Color error =new Color(230,92,0);
		
		JPanel p = new JPanel();
		p.setLayout(null);
		p.setBackground(back);
		//タイトル
		JLabel title = new JLabel("HOME");
		title.setForeground(text);
		title.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 36));
		title.setBounds(340,20,600,40);
		
		//ユーザー名
		JLabel name = new JLabel(playername);
		name.setForeground(text);
		name.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 20));
		name.setBounds(700,10,600,40);
		
		//対局ボタン
		JButton playgame = new JButton("Play");
		playgame.setForeground(text);
		playgame.setBackground(button);
		playgame.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		playgame.setBounds(270,170,250,100);
		playgame.setOpaque(true);
		playgame.setBorderPainted(false);
		
		//成績確認ボタン
		JButton score = new JButton("Score");
		score.setForeground(text);
		score.setBackground(button);
		score.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		score.setBounds(270,340,250,100);
		score.setOpaque(true);
		score.setBorderPainted(false);
		
		//ログアウトボタン
		JButton logout = new JButton("Logout");
		logout.setForeground(text);
		logout.setBackground(button);
		logout.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		logout.setBounds(650,440,130,50);
		logout.setOpaque(true);
		logout.setBorderPainted(false);
		
		p.add(title);
		p.add(name);
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
				 otherPlayerRequest();
				 gamewaiting();
				 new Thread(new ReceiveInvite()).start();
				 //playothello();
				 //Board.repaint();
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
	
	public void gamewaiting() { //マッチング画面
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じる場合の処理
		setTitle("Matching");//ウィンドウのタイトル
		setSize(800, 550);//ウィンドウのサイズを設定
		
		//色
		Color back =new Color(55,113,184);
		Color text =new Color(247,247,247);
		Color textback =new Color(4,49,105);
		Color button =new Color(113,166,0);
		Color error =new Color(230,92,0);
		
		
		matching.setLayout(null);
		matching.setBackground(back);
		//タイトル
		JLabel title = new JLabel("MATCHING");
		title.setForeground(text);
		title.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 36));
		title.setBounds(300,20,600,40);
		
		//更新ボタン
		JButton update = new JButton("Update");
		update.setForeground(text);
		update.setBackground(button);
		update.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		update.setBounds(630,380,150,50);
		update.setOpaque(true);
		update.setBorderPainted(false);
		
		//戻るボタン
		JButton returntohome = new JButton("Home");
		returntohome.setForeground(text);
		returntohome.setBackground(button);
		returntohome.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		returntohome.setBounds(630,440,150,50);
		returntohome.setOpaque(true);
		returntohome.setBorderPainted(false);
		
		//対局待ちリスト＆レート&対局申し込みボタン
		JLabel player[] = new JLabel[8];
		JLabel rate[] = new JLabel[8];
		JButton play[] = new JButton[8];
		
		//本来はLabelの中身はotherPlayer[k].getName()
		player[0] = new JLabel();
		player[0].setForeground(text);
		player[0].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		player[0].setBounds(70,120,200,30);
		
		player[1] = new JLabel();
		player[1].setForeground(text);
		player[1].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		player[1].setBounds(70,170,200,30);
		
		player[2] = new JLabel();
		player[2].setForeground(text);
		player[2].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		player[2].setBounds(70,220,200,30);
		
		player[3] = new JLabel();
		player[3].setForeground(text);
		player[3].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		player[3].setBounds(70,270,200,30);
		
		player[4] = new JLabel();
		player[4].setForeground(text);
		player[4].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		player[4].setBounds(70,320,200,30);
		
		player[5] = new JLabel();
		player[5].setForeground(text);
		player[5].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		player[5].setBounds(70,370,200,30);
		
		player[6] = new JLabel();
		player[6].setForeground(text);
		player[6].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		player[6].setBounds(70,420,200,30);
		
		player[7] = new JLabel();
		player[7].setForeground(text);
		player[7].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		player[7].setBounds(70,470,200,30);
		
		//名前の設定
		for(int i = 0;i<arraysize;i++) {
			if(!(otherPlayer[i].getName().equals(myPlayer.getName()))) {
				player[i].setText(otherPlayer[i].getName());
			}
			System.out.println(i+1 + ":" + player[i].getText());
		}
		
		//本来はLabelの中身はotherPlayer[k].getRate
		rate[0] = new JLabel();
		rate[0].setForeground(text);
		rate[0].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		rate[0].setBounds(290,120,200,30);
		
		rate[1] = new JLabel();
		rate[1].setForeground(text);
		rate[1].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		rate[1].setBounds(290,170,200,30);
		
		rate[2] = new JLabel();
		rate[2].setForeground(text);
		rate[2].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		rate[2].setBounds(290,220,200,30);
		
		rate[3] = new JLabel();
		rate[3].setForeground(text);
		rate[3].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		rate[3].setBounds(290,270,200,30);
		
		rate[4] = new JLabel();
		rate[4].setForeground(text);
		rate[4].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		rate[4].setBounds(290,320,200,30);
		
		rate[5] = new JLabel();
		rate[5].setForeground(text);
		rate[5].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		rate[5].setBounds(290,370,200,30);
		
		rate[6] = new JLabel();
		rate[6].setForeground(text);
		rate[6].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		rate[6].setBounds(290,420,200,30);
		
		rate[7] = new JLabel();
		rate[7].setForeground(text);
		rate[7].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		rate[7].setBounds(290,470,200,30);
		
		//レートの設定
		for(int i = 0;i<arraysize;i++) {
			if(!(otherPlayer[i].getName().equals(myPlayer.getName()))) {
				rate[i].setText(String.valueOf(otherPlayer[i].getRate()));
			}
			System.out.println(i+1 + "のレート:" + rate[i].getText());
		}
		
		play[0] = new JButton("Apply");
		play[0].setForeground(text);
		play[0].setBackground(button);
		play[0].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		play[0].setBounds(350,120,130,30);
		play[0].setOpaque(true);
		play[0].setBorderPainted(false);
		
		play[1] = new JButton("Apply");
		play[1].setForeground(text);
		play[1].setBackground(button);
		play[1].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		play[1].setBounds(350,170,130,30);
		play[1].setOpaque(true);
		play[1].setBorderPainted(false);
		
		play[2] = new JButton("Apply");
		play[2].setForeground(text);
		play[2].setBackground(button);
		play[2].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		play[2].setBounds(350,220,130,30);
		play[2].setOpaque(true);
		play[2].setBorderPainted(false);
		
		play[3] = new JButton("Apply");
		play[3].setForeground(text);
		play[3].setBackground(button);
		play[3].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		play[3].setBounds(350,270,130,30);
		play[3].setOpaque(true);
		play[3].setBorderPainted(false);
		
		play[4] = new JButton("Apply");
		play[4].setForeground(text);
		play[4].setBackground(button);
		play[4].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		play[4].setBounds(350,320,130,30);
		play[4].setOpaque(true);
		play[4].setBorderPainted(false);
		
		play[5] = new JButton("Apply");
		play[5].setForeground(text);
		play[5].setBackground(button);
		play[5].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		play[5].setBounds(350,370,130,30);
		play[5].setOpaque(true);
		play[5].setBorderPainted(false);
		
		play[6] = new JButton("Apply");
		play[6].setForeground(text);
		play[6].setBackground(button);
		play[6].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		play[6].setBounds(350,420,130,30);
		play[6].setOpaque(true);
		play[6].setBorderPainted(false);
		
		play[7] = new JButton("Apply");
		play[7].setForeground(text);
		play[7].setBackground(button);
		play[7].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		play[7].setBounds(350,470,130,30);
		play[7].setOpaque(true);
		play[7].setBorderPainted(false);
		
		//Yesボタン
		
		Yes.setForeground(text);
		Yes.setBackground(button);
		Yes.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		Yes.setBounds(475,100,250,50);
		Yes.setOpaque(true);
		Yes.setBorderPainted(false);
		//Noボタン
		
		No .setForeground(text);
		No .setBackground(button);
		No .setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		No .setBounds(475,170,250,50);
		No .setOpaque(true);
		No .setBorderPainted(false);
		
		//申し込み相手
		opponentname.setText("PlayerUnknown");
		opponentname.setForeground(text);
		opponentname.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		opponentname.setBounds(70,120,200,30);
		
		//申し込み相手のレート
		opponentrate.setText("Unknownrate");
		opponentrate.setForeground(text);
		opponentrate.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		opponentrate.setBounds(70,120,200,30);
		
		matching.add(player[0]);
		matching.add(player[1]);
		matching.add(player[2]);
		matching.add(player[3]);
		matching.add(player[4]);
		matching.add(player[5]);
		matching.add(player[6]);
		matching.add(player[7]);
		matching.add(rate[0]);
		matching.add(rate[1]);
		matching.add(rate[2]);
		matching.add(rate[3]);
		matching.add(rate[4]);
		matching.add(rate[5]);
		matching.add(rate[6]);
		matching.add(rate[7]);
		
		//ボタンの表示
		for(int i = 0;i<arraysize;i++) {
			if(!(otherPlayer[i].getName().equals(myPlayer.getName()))) {
				matching.add(play[i]);
			}
		}
		matching.add(title);
		matching.add(update);
		matching.add(returntohome);
		add(matching);
		
		play[0].addActionListener(new ActionListener() { // player1ボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				 
				      //out = new PrintWriter(new OutputStreamWriter(soc.getOutputStream()));
				      out.println("requestGame");
				      out.println(Integer.toString(playerlist.get(0).ThreadNo)); //スレッドナンバーを送信
				      System.out.println("TheradNo : " + playerlist.get(0).ThreadNo);
				      out.println(Integer.toString(playerlist.indexOf(myPlayer)));
				      out.flush();
				      System.out.println("申し込み送信完了");
				      
				      //ストリームをクローズする
				      //out.close();
				  
				 
			 }
		});
		
		play[1].addActionListener(new ActionListener() { // player2ボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				
				      //out = new PrintWriter(new OutputStreamWriter(soc.getOutputStream()));
				      out.println("requestGame");
				      out.println(playerlist.get(1).ThreadNo);
				      out.println(playerlist.indexOf(myPlayer));
				      out.flush();
				      
				      //ストリームをクローズする
				      //out.close();
				  
				 
			 }
		});
		
		play[2].addActionListener(new ActionListener() { // player3ボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				 
				      //out = new PrintWriter(new OutputStreamWriter(soc.getOutputStream()));
				      out.println("requestGame");
				      out.println(playerlist.get(2).ThreadNo);
				      out.println(playerlist.indexOf(myPlayer));
				      out.flush();
				      
				      //ストリームをクローズする
				      //out.close();
				   
				 
			 }
		});
		
		play[3].addActionListener(new ActionListener() { // player4ボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				 
				      //out = new PrintWriter(new OutputStreamWriter(soc.getOutputStream()));
				      out.println("requestGame");
				      out.println(playerlist.get(3).ThreadNo);
				      out.println(playerlist.indexOf(myPlayer));
				      out.flush();
				      
				      //ストリームをクローズする
				      //out.close();
				    
			 }
		});
		
		play[4].addActionListener(new ActionListener() { // player5ボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
			
				      //out = new PrintWriter(new OutputStreamWriter(soc.getOutputStream()));
				      out.println("requestGame");
				      out.println(playerlist.get(4).ThreadNo);
				      out.println(playerlist.indexOf(myPlayer));
				      out.flush();
				      
				      //ストリームをクローズする
				      //out.close();
				    
				 
			 }
		});
		
		play[5].addActionListener(new ActionListener() { // player6ボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
			
				      //out = new PrintWriter(new OutputStreamWriter(soc.getOutputStream()));
				      out.println("requestGame");
				      out.println(playerlist.get(5).ThreadNo);
				      out.println(playerlist.indexOf(myPlayer));
				      out.flush();
				      
				      //ストリームをクローズする
				      //out.close();
				   
				 
			 }
		});
		
		play[6].addActionListener(new ActionListener() { // player7ボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
			
				      //out = new PrintWriter(new OutputStreamWriter(soc.getOutputStream()));
				      out.println("requestGame");
				      out.println(playerlist.get(6).ThreadNo);
				      out.println(playerlist.indexOf(myPlayer));
				      out.flush();
				      
				      //ストリームをクローズする
				      //out.close();
				    
				 
			 }
		});
		
		play[7].addActionListener(new ActionListener() { // player8ボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				 
				      //out = new PrintWriter(new OutputStreamWriter(soc.getOutputStream()));
				      out.println("requestGame");
				      out.println(playerlist.get(7).ThreadNo);
				      out.println(playerlist.indexOf(myPlayer));
				      out.flush();
				      
				      //ストリームをクローズする
				      //out.close();
				 
			 }
		});
		
		Yes.addActionListener(new ActionListener() { // Yesボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
			
				      //out = new PrintWriter(new OutputStreamWriter(soc.getOutputStream()));
				      out.println("Answer");
				      out.println("Yes");
				      out.flush();
				      System.out.println("Yesを送信しました");
				      stopRecFlag = 1;
				      //ストリームをクローズする
				      //out.close();
				      mycolor = "white";
				      playothello();
				      //stopRecFlag = 1;
				      matching.removeAll();
				      System.out.println("申し込んだ側：MatchR");
				      new Thread(new MatchReceive()).start();
				 
			 }
		});
		
		No.addActionListener(new ActionListener() { // Noボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
			
				      //out = new PrintWriter(new OutputStreamWriter(soc.getOutputStream()));
				      out.println("Answer");
				      out.println("No");
				      out.flush();
				      
				      //ストリームをクローズする
				      //out.close();
			 }
		});
		
		update.addActionListener(new ActionListener() { // 更新ボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				playerlist.clear();
				System.out.println("ArrayListが初期化されたよ");
				otherPlayerRequest();
				//名前の設定
				for(int i = 0;i<arraysize;i++) {
					if(!(otherPlayer[i].getName().equals(myPlayer.getName()))) {
						player[i].setText(otherPlayer[i].getName());
					}
					System.out.println(i+1 + ":" + player[i].getText());
				}
				//レートの設定
				for(int i = 0;i<arraysize;i++) {
					if(!(otherPlayer[i].getName().equals(myPlayer.getName()))) {
						rate[i].setText(String.valueOf(otherPlayer[i].getRate()));
					}
					System.out.println(i+1 + "のレート:" + rate[i].getText());
				}
				//ボタンの表示
				for(int i = 0;i<arraysize;i++) {
					if(!(otherPlayer[i].getName().equals(myPlayer.getName()))) {
						matching.add(play[i]);
					}
				}
				matching.repaint();
			 }
		});
		
		returntohome.addActionListener(new ActionListener() { // 戻るボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				userhome();
				matching.removeAll();
			 }
		});
		
		setVisible(true);
	}
	
	//対局待ちプレイヤー受付
	  public void otherPlayerRequest() {
	    try {
	      //out = new PrintWriter(new OutputStreamWriter(soc.getOutputStream()));
	      //ois = new ObjectInputStream(soc.getInputStream());
	      //ArrayList<Player> playerList = new ArrayList<Player>();
	      
	      out.println("otherPlayerRequest");
	      out.flush();
	      
	      playerlist = (ArrayList<Player>)(ois.readObject());
	      otherPlayer = (Player[])playerlist.toArray(new Player[0]);
	      
	      arraysize = Integer.parseInt(in.readLine());
		  System.out.println("test2");
		  System.out.println(otherPlayer[0].getName());
		  System.out.println("変換できたよ");
		  System.out.println("オンライン人数" + arraysize);
		  
		  for(int i=0;i<arraysize;i++) {
			  System.out.println("otheplayer[" + i + "]:" + otherPlayer[i].getName());
		  }

	      //out.close();
	      //ois.close();
	    }catch(Exception e) {
	      System.out.println(e);
	     // System.exit(1);
	    }finally{
	   
	  }}
	  
	 
	  /*********内部クラス*******/
	  //他プレイヤからの申し込みを受け取る or 自分の申し込みの答えを受け取る(承諾、拒否)
	  class ReceiveInvite implements Runnable {
	    //ストリームが２つになってしまう可能性がある。例えば更新をしたとき
	    //Clientクラスのクラス変数とは別のストリーム用の変数
	    //コンストラクタ
	    //ReceiveInvite() {
	      
	        //グローバル変数BufferedReader in
	        //in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
	      
	    //}

	    public void run() {
	      try {
	    	  System.out.println("InviteReceive start");
	        while(true) {
	          if(stopRecFlag == 1) {
	            stopRecFlag = 0;
	            System.out.println("InviteReceive end");
	            //スレッド終了時の処理 = 対局をやめる(ユーザーホームに戻る)
	            break;
	          }
	          String inputLine = in.readLine();
	        
	          if(inputLine != null) {
	            if(inputLine.equals("requestGame")) {
	            		System.out.println("申し込みを受信");
	            		playernumber = Integer.parseInt(in.readLine());
	            		System.out.println("playernumber : " + playernumber);
	            		matching.add(Yes);
	            		matching.add(No);
	            		matching.repaint();
	              //他プレイヤーからの申し込み
	            }  else if(inputLine.equals("Answer")) {
	            		inputLine = in.readLine();
	            		System.out.println(inputLine);
	            		if(inputLine.equals("Yes")) {
	            			System.out.println("答えがYesでした");
	            			mycolor = "black";
	            			System.out.println("スレッド起動前");
	            			
	            			
	            			
	            			playothello();
	            			matching.removeAll();
	            			System.out.println("申し込まれた側：MatchR");
	            			new Thread(new MatchReceive()).start();
	            			System.out.println("スレッド起動後");
	            		}else {
	            			System.out.println("拒否されました");
	            		}
	            }
	          }
	        }
	      }catch(IOException e) {
	        System.out.println("対局申し込みを受理するときにエラーがこきました。");
	      }
	    }
	  }
	
	//対局中の通信を行う:受信側
	  class MatchReceive implements Runnable {
	    //MatchReceive() {
	      
	        //グローバル変数 BufferedReader in
	        //in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
	    //}

	    public void run() {
	    	try {
	    		System.out.println("MatchReceive start");
		      while(true) {
		        String readLine = in.readLine();
		        if(readLine != null) {
		          receiveMessage(readLine);  //ただコマンドラインに表示するだけ
		          /*Othelloに対する処理*/
		          if(readLine.equals("forwardMessage")) {
		        	  	String color = in.readLine();
		        	  	int grid = Integer.parseInt(in.readLine());
		        	  	game.putStone(grid, color);
		        	  	updateDisp();
		        	  	Board.repaint();
		          }
		        }
		        //終了時の処理
		        if(stopRecFlag == 1) {
		          stopRecFlag = 0;
		          System.out.println("MatchReceive end");
		          break;
		        }
		      }
		    	}catch (Exception e){
					System.err.println("データ受信時にエラーが発生しました: " + e);
				}
	    }
	  }
	
	public void score() {//成績確認画面
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じる場合の処理
		setTitle("Score");//ウィンドウのタイトル
		setSize(800, 550);//ウィンドウのサイズを設定
		
		//色
		Color back =new Color(55,113,184);
		Color text =new Color(247,247,247);
		Color textback =new Color(4,49,105);
		Color button =new Color(113,166,0);
		Color error =new Color(230,92,0);
				
		JPanel p = new JPanel();
		p.setLayout(null);
		p.setBackground(back);
		
		//タイトル
		JLabel title = new JLabel("SCORE");
		title.setForeground(text);
		title.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 36));
		title.setBounds(340,20,600,40);
		
		JLabel name = new JLabel(myPlayer.getName());
		name.setForeground(text);
		name.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 36));
		name.setBounds(680,10,200,40);
		
		//勝ち
		JLabel win = new JLabel("Win ");
		win.setForeground(text);
		win.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 36));
		win.setBounds(80,130,600,40);
		
		//勝ち数
		JLabel wincount = new JLabel(String.valueOf(myPlayer.getWin()));
		System.out.println(String.valueOf(myPlayer.getWin()));
		wincount.setForeground(text);
		wincount.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 36));
		wincount.setBounds(160,130,600,40);
		
		//負け
		JLabel lose = new JLabel("Lose ");
		lose.setForeground(text);
		lose.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 36));
		lose.setBounds(80,200,600,40);
				
		//負け数
		JLabel losecount = new JLabel(String.valueOf(myPlayer.getDefeat()));
		losecount.setForeground(text);
		losecount.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 36));
		losecount.setBounds(160,200,600,40);
		
		//引き分け
		JLabel draw = new JLabel("Draw ");
		draw.setForeground(text);
		draw.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 36));
		draw.setBounds(80,270,600,40);
						
		//引き分け数
		JLabel drawcount = new JLabel(String.valueOf(myPlayer.getDraw()));
		drawcount.setForeground(text);
		drawcount.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 36));
		drawcount.setBounds(160,270,600,40);
		
		//降参
		JLabel surrender = new JLabel("Surrender ");
		surrender.setForeground(text);
		surrender.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 36));
		surrender.setBounds(80,340,600,40);
								
		//降参数
		JLabel surrendercount = new JLabel(String.valueOf(myPlayer.getSurrender()));
		surrendercount.setForeground(text);
		surrendercount.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 36));
		surrendercount.setBounds(160,340,600,40);
		
		//レート
		JLabel rate = new JLabel("Your Rate");
		rate.setForeground(text);
		rate.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 36));
		rate.setBounds(500,100,600,40);
		
		//レート
		JLabel rating = new JLabel(String.valueOf(myPlayer.getRate()));
		rating.setForeground(text);
		rating.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 40));
		rating.setBounds(500,170,600,40);
		
		//戻るボタン
		JButton returntohome = new JButton("Home");
		returntohome.setForeground(text);
		returntohome.setBackground(button);
		returntohome.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 28));
		returntohome.setBounds(650,440,130,50);
		returntohome.setOpaque(true);
		returntohome.setBorderPainted(false);
		
		p.add(title);
		p.add(name);
		p.add(win);
		p.add(wincount);
		p.add(lose);
		p.add(losecount);
		p.add(draw);
		p.add(drawcount);
		p.add(surrender);
		p.add(surrendercount);
		p.add(rate);
		p.add(returntohome);
		add(p);
		
		returntohome.addActionListener(new ActionListener() { // 戻るボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				userhome();
				p.removeAll();
			 }
		});
		
		setVisible(true);
	}
	
	
	
	//プレイヤー情報受付
	  public void myPlayerRequest() {
	    try {
	    	System.out.println("send myPR1");
	      //out = new PrintWriter(new OutputStreamWriter(soc.getOutputStream()));
	    	out.println("myPlayerRequest");
	    	out.flush();
	    	System.out.println("send myPR1 end");
	      ois = new ObjectInputStream(soc.getInputStream());
	      
	      System.out.println("send myPR2");
	      System.out.println("未受信２");
	      myPlayer = (Player)(ois.readObject());
	      //out.close();
	      //ois.close();
	    }catch(Exception e) {
	      System.out.println(e);
	      System.exit(1);
	    }
	  }
	
	public void playothello() {//オセロ
		game.clearboard();
		int[] grids = game.getGrids(); //getGridメソッドにより局面情報を取得
		
		//ウィンドウ設定
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//ウィンドウを閉じる場合の処理
		setTitle("game");//ウィンドウのタイトル
		setSize(800, 550);//ウィンドウのサイズを設定
		
		//色
		Color back =new Color(55,113,184);
		Color text =new Color(247,247,247);
		Color textback =new Color(4,49,105);
		Color button =new Color(113,166,0);
		Color error =new Color(230,92,0);
		
		
		Board.setLayout(null);
		Board.setBackground(back);
		
		//アイコン設定(画像ファイルをアイコンとして使う)
		whiteIcon = new ImageIcon("White.jpg");
		blackIcon = new ImageIcon("Black.jpg");
		boardIcon = new ImageIcon("GreenFrame.jpg");
		
		//オセロ盤の生成
		buttonArray = new JButton[row * row];//ボタンの配列を作成
		for(int i = 0 ; i < row * row ; i++){
			if(grids[i]==1){ 
				buttonArray[i] = new JButton(blackIcon);
				System.out.println("obj made1");
			}//盤面状態に応じたアイコンを設定
			if(grids[i]==2){ 
				buttonArray[i] = new JButton(whiteIcon);
				System.out.println("obj made2");
			}//盤面状態に応じたアイコンを設定
			if(grids[i]==0){
				buttonArray[i] = new JButton(boardIcon);
				System.out.println("obj made 3");
			}//盤面状態に応じたアイコンを設定
			
			// ボタンを配置する
			int x = (i % row) * 45 + 250;
			int y = (int) 10 + (i / row)*45 + 30;
			buttonArray[i].setBounds(x, y, 45, 45);//ボタンの大きさと位置を設定する．
			buttonArray[i].addMouseListener(this);//マウス操作を認識できるようにする
			buttonArray[i].setActionCommand(Integer.toString(i));//ボタンを識別するための名前(番号)を付加する
			Board.add(buttonArray[i]);//ボタンの配列をペインに貼り付け
		}
		
		//黒の数
		JLabel black = new JLabel("Black : " + game.black());
		black.setForeground(text);
		black.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		black.setBounds(65,100,200,20);
		Board.add(black);
		
		//白の数
		JLabel white = new JLabel("Black : " + game.white());
		white.setForeground(text);
		white.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		white.setBounds(65,150,200,20);
		Board.add(white);
		
		//降参ボタン
		stop = new JButton("降参");
		stop.setBounds(0, row * 45 + 30, (row * 45 + 10) / 2, 30);
		Board.add(stop); 
		stop.addActionListener(new ActionListener() { // 降参ボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				Board.removeAll();
				userhome();
			 }
		});
		
		//手番表示
		turnLabel = new JLabel(game.getTurn() + "の番です");
		turnLabel.setBounds(10, row * 45 + 100, row * 45 + 10, 30);
		Board.add(turnLabel);
		
		add(Board);
		Board.repaint();
		
		setVisible(true);
	}
	
	 public boolean connectServer(String ipAddress,int port) {
		    try {
		      soc = new Socket(ipAddress,port);
		      return true;
		    }catch(UnknownHostException e) {
		      System.out.println("ホストに接続できません。");
		      System.out.println(e);
		      return false;
		    }catch(IOException e) {
		      System.out.println("サーバー接続時にエラーが発生しました。");
		      System.out.println(e);
		      return false;
		    }
		  }

	 public void startMatch() {
		    try {
		      out = new PrintWriter(new OutputStreamWriter(soc.getOutputStream()));
		      //これによりsendMessage()メソッドが使えるようになる
		    }catch(IOException e) {
		      System.out.println("対局開始時の通信でエラーが起きました。");
		      System.out.println(e);
		      System.exit(1);
		    }
		  }

	public void sendMessage(String msg){	// サーバに操作情報を送信
		out.println(msg);//送信データをバッファに書き出す
		out.flush();//送信データを送る
		System.out.println("サーバにメッセージ " + msg + " を送信しました"); //テスト標準出力
	}

	public void receiveMessage(String msg){	// メッセージの受信
		System.out.println("サーバからメッセージ " + msg + " を受信しました"); //テスト用標準出力
		
	}
	
	public void updateDisp(){// 画面を更新する
		Board.removeAll();
		int[] grids = game.getGrids(); //getGridメソッドにより局面情報を取得
		
		//色
		Color back =new Color(55,113,184);
		Color text =new Color(247,247,247);
		Color textback =new Color(4,49,105);
		Color button =new Color(113,166,0);
		Color error =new Color(230,92,0);
				
				
		Board.setLayout(null);
		Board.setBackground(back);
		
		//オセロ盤の表示
		buttonArray = new JButton[row * row];//ボタンの配列を作成
		for(int i = 0 ; i < row * row ; i++){
			if(grids[i]==1){ buttonArray[i] = new JButton(blackIcon);}//盤面状態に応じたアイコンを設定
			if(grids[i]==2){ buttonArray[i] = new JButton(whiteIcon);}//盤面状態に応じたアイコンを設定
			if(grids[i]==0){ buttonArray[i] = new JButton(boardIcon);}//盤面状態に応じたアイコンを設定
			Board.add(buttonArray[i]);//ボタンの配列をペインに貼り付け
			// ボタンを配置する
			int x = (i % row) * 45 + 250;
			int y = (int) 10 + (i / row)*45 + 30;
			buttonArray[i].setBounds(x, y, 45, 45);//ボタンの大きさと位置を設定する．
			buttonArray[i].addMouseListener(this);//マウス操作を認識できるようにする
			buttonArray[i].setActionCommand(Integer.toString(i));//ボタンを識別するための名前(番号)を付加する
		}
		
		//黒の数
		JLabel black = new JLabel("Black : " + game.black());
		black.setForeground(text);
		black.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		black.setBounds(65,100,200,20);
		Board.add(black);
				
				//白の数
				JLabel white = new JLabel("Black : " + game.white());
				white.setForeground(text);
				white.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
				white.setBounds(65,150,200,20);
				Board.add(white);
		
		
		
		//降参ボタン
		stop = new JButton("降参");
		Board.add(stop); 
		stop.setBounds(0, row * 45 + 30, (row * 45 + 10) / 2, 30);
		stop.addActionListener(new ActionListener() { // 成績ボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				Board.removeAll();
				userhome();
			 }
		});
		
				
		//手番表示
		System.out.println("手番は" + game.getTurn());
		turnLabel.setText(game.getTurn() + "の番です");
		turnLabel.setBounds(10, row * 45 + 100, row * 45 + 10, 30);
		Board.add(turnLabel);
		
		add(Board);
		setVisible(true);
	}
	public void acceptOperation(String command){	// プレイヤの操作を受付
	}

  	//マウスクリック時の処理
	public void mouseClicked(MouseEvent e) {
		JButton theButton = (JButton)e.getComponent();//クリックしたオブジェクトを得る．キャストを忘れずに
		String command = theButton.getActionCommand();//ボタンの名前を取り出す
		//受信
		//putstone
		//updateDisp();
		System.out.println();
		boolean isgameover1 = game.isGameover(game.getTurn(),game.getGrids());
		if(isgameover1 == false) {
			boolean putresult = game.putStone(Integer.parseInt(command), game.getTurn());
			System.out.println("マウスがクリックされました。押されたボタンは " + command + "です。");//テスト用に標準出力
			//PutStoneInformatin(Integer.parseInt(command), mycolor);
			if(putresult==true) {
				game.chengeTurn();
			}
			updateDisp();
			Board.repaint();
			
		}
		if(isgameover1 == true) {
			stopRecFlag = 1;
			String winner = game.checkWinner();
			System.out.println(winner + "の勝ち");
			playresult(winner);
			Board.removeAll();
		}
	}
	
	//置いた石の情報を送信
	public void PutStoneInformatin(int i, String color) { 
	    try {
	      out = new PrintWriter(new OutputStreamWriter(soc.getOutputStream()));
	      out.println("forwardMessage");
	      out.println(color);
	      out.println( String.valueOf(i));
	      out.flush();
	      
	      //ストリームをクローズする
	      out.close();
	    } catch(IOException e) {
	      System.out.println(e);
	    }
	  }
	
	public void playresult(String winner) { //結果画面
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ウィンドウを閉じる場合の処理
		setTitle("Login");//ウィンドウのタイトル
		setSize(800, 550);//ウィンドウのサイズを設定
		
		String result;
		double myrate = myPlayer.getRate();
		boolean iswin = true;
		if(winner.equals(mycolor)) {
			result = "WIN";
			myPlayer.setWin(myPlayer.getWin()+1);
		}
		if(winner.equals("draw")) {
			result = "DRAW";
			myPlayer.setDraw(myPlayer.getDraw()+1);
		}else {
			result = "LOSE";
			iswin = false;
			myPlayer.setDefeat(myPlayer.getDefeat()+1);
		}
		
		//レート計算
		if(!(result.equals("DRAW"))){
			 myrate = calRating(myPlayer.getRate(), otherPlayer[playernumber].getRate(), iswin);
		}
		
		//色
		Color back =new Color(55,113,184);
		Color text =new Color(247,247,247);
		Color textback =new Color(4,49,105);
		Color button =new Color(113,166,0);
		Color error =new Color(230,92,0);
		
		JPanel p = new JPanel();
		p.setLayout(null);
		p.setBackground(back);
		//タイトル
		JLabel title = new JLabel("RESULT");
		title.setForeground(text);
		title.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 36));
		title.setBounds(310,20,180,40);
		//結果
		JLabel reslutt = new JLabel(result);
		reslutt.setForeground(error);
		reslutt.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 30));
		reslutt.setBounds(310,220,200,50);
		//レート
		JLabel rate = new JLabel(String.valueOf(myrate));
		rate.setForeground(error);
		rate.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 30));
		rate.setBounds(310,420,200,50);
		//戻るボタン
		JButton returntohome = new JButton("Home");
		returntohome.setForeground(text);
		returntohome.setBackground(button);
		returntohome.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 28));
		returntohome.setBounds(650,440,130,50);
		returntohome.setOpaque(true);
		returntohome.setBorderPainted(false);
		
		returntohome.addActionListener(new ActionListener() { // 戻るボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				//サーバーに自分のオブジェクトを送信
				 try {
				      out = new PrintWriter(new OutputStreamWriter(soc.getOutputStream()));
				      out.println("dataUpdate");
				      out.flush();
				      out.close();
				      
				      oos = new ObjectOutputStream(soc.getOutputStream());
				      oos.writeObject(myPlayer);
				      oos.flush();
				      ois.close();
				      
				    }catch(IOException e) {
				      System.out.println(e);
				      System.exit(1);
				    }
				userhome();
				p.removeAll();
			 }
		});
		
		p.add(title);
		p.add(reslutt);
		p.add(rate);
		p.add(returntohome);
		
	}
	
	double calRating(double yourRate, double secondRate, boolean isWin) { //レート計算
	    double gap = (yourRate>secondRate)?(yourRate-secondRate):(secondRate-yourRate);
	    double new_gap = 0;

	    if(isWin == true) {
	      if(yourRate>=secondRate) {
	        new_gap = 16 - gap*0.04;
	      } else {
	        new_gap = 16 + gap*0.04;
	      }
	      if(new_gap >= 40) {
	        new_gap = 40;
	      }
	      if(new_gap <= -40) {
	        new_gap = -40;
	      }
	      return (yourRate+new_gap);
	    } else {
	      if(yourRate>=secondRate) {
	        new_gap = 16 + gap*0.04;
	      } else {
	        new_gap = 16 - gap*0.04;
	      }
	      if(new_gap >= 40) {
	        new_gap = 40;
	      }
	      if(new_gap <= -40) {
	        new_gap = -40;
	      }
	      return (yourRate-new_gap);
	    }
	}
	
	  
	  public void mousePressed(MouseEvent e) {
	  }
	  public void mouseReleased(MouseEvent e) {
	  }
	  public void mouseEntered(MouseEvent e) {
	  }
	  public void mouseExited(MouseEvent e) {
	  }

	//main
	public static void main(String args[]){
		
		Othello game = new Othello();
		Client client = new Client(game);
		client.Login();
	}

	
}
