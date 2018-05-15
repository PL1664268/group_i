
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
	private Receiver receiver; //データ受信用オブジェクト
	private Othello game; //Othelloオブジェクト
	private Player myPlayer;  //自分のPlayerクラスのインスタンス変数
	private Player player; //Playerオブジェクト
	private String playername = "Player";
	private Player opponent;  //対局相手のインスタンス変数
	private Player[] otherPlayer;
	
	
	int row = 8; //getRowメソッドによりオセロ盤の縦横マスの数を取得
	JPanel Board = new JPanel();
	
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
				 boolean connectresult = connectServer("localhost", 10005);
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
						 playername = username.getText();
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
					 boolean connectresult = connectServer("localhost", 10005);
					 if( connectresult == false) {
						 serverfailed.setText("Could not connect to the server!");
					 }
					 if(connectresult == true) {
						 
						 boolean flag = accountRequest(username.getText(),passstring1);
						 if (flag ==false) {
							 newaccountfailed.setText("This name is unavailable");
						 }
						 if (flag ==true) {
							 playername = username.getText();
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
	      out.close();
	      in.close();
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
				 p.removeAll();
				 //gamewaiting();
				 //otherPlayerRequest();
				 playothello();
				 p.removeAll();
			 }
		});
		
		score.addActionListener(new ActionListener() { // 成績ボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				score();
				p.removeAll();
				myPlayerRequest();
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
		
		JPanel p = new JPanel();
		p.setLayout(null);
		p.setBackground(back);
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
		player[0] = new JLabel("Player1");
		player[0].setForeground(text);
		player[0].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		player[0].setBounds(70,120,200,30);
		
		player[1] = new JLabel("Player2");
		player[1].setForeground(text);
		player[1].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		player[1].setBounds(70,170,200,30);
		
		player[2] = new JLabel("Player3");
		player[2].setForeground(text);
		player[2].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		player[2].setBounds(70,220,200,30);
		
		player[3] = new JLabel("Player4");
		player[3].setForeground(text);
		player[3].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		player[3].setBounds(70,270,200,30);
		
		player[4] = new JLabel("Player5");
		player[4].setForeground(text);
		player[4].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		player[4].setBounds(70,320,200,30);
		
		player[5] = new JLabel("Player6");
		player[5].setForeground(text);
		player[5].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		player[5].setBounds(70,370,200,30);
		
		player[6] = new JLabel("Player7");
		player[6].setForeground(text);
		player[6].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		player[6].setBounds(70,420,200,30);
		
		player[7] = new JLabel("Player8");
		player[7].setForeground(text);
		player[7].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		player[7].setBounds(70,470,200,30);
		
		//本来はLabelの中身はotherPlayer[k].getRate
		rate[0] = new JLabel("200");
		rate[0].setForeground(text);
		rate[0].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		rate[0].setBounds(290,120,200,30);
		
		rate[1] = new JLabel("200");
		rate[1].setForeground(text);
		rate[1].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		rate[1].setBounds(290,170,200,30);
		
		rate[2] = new JLabel("200");
		rate[2].setForeground(text);
		rate[2].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		rate[2].setBounds(290,220,200,30);
		
		rate[3] = new JLabel("200");
		rate[3].setForeground(text);
		rate[3].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		rate[3].setBounds(290,270,200,30);
		
		rate[4] = new JLabel("200");
		rate[4].setForeground(text);
		rate[4].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		rate[4].setBounds(290,320,200,30);
		
		rate[5] = new JLabel("200");
		rate[5].setForeground(text);
		rate[5].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		rate[5].setBounds(290,370,200,30);
		
		rate[6] = new JLabel("200");
		rate[6].setForeground(text);
		rate[6].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		rate[6].setBounds(290,420,200,30);
		
		rate[7] = new JLabel("200");
		rate[7].setForeground(text);
		rate[7].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 23));
		rate[7].setBounds(290,470,200,30);
		
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
		
		p.add(player[0]);
		p.add(player[1]);
		p.add(player[2]);
		p.add(player[3]);
		p.add(player[4]);
		p.add(player[5]);
		p.add(player[6]);
		p.add(player[7]);
		p.add(rate[0]);
		p.add(rate[1]);
		p.add(rate[2]);
		p.add(rate[3]);
		p.add(rate[4]);
		p.add(rate[5]);
		p.add(rate[6]);
		p.add(rate[7]);
		p.add(play[0]);
		p.add(play[1]);
		p.add(play[2]);
		p.add(play[3]);
		p.add(play[4]);
		p.add(play[5]);
		p.add(play[6]);
		p.add(play[7]);
		p.add(title);
		p.add(update);
		p.add(returntohome);
		add(p);
		
		update.addActionListener(new ActionListener() { // 更新ボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				 otherPlayerRequest();
				 
			 }
		});
		
		returntohome.addActionListener(new ActionListener() { // 戻るボタンを押した時の処理
			 public void actionPerformed(ActionEvent as) {
				userhome();
				p.removeAll();
			 }
		});
		
		setVisible(true);
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
	    }catch(Exception e) {
	      System.out.println(e);
	      System.exit(1);
	    }
	  }
	  
	 
	
	public void score() {//成績確認画面
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//ウィンドウを閉じる場合の処理
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
		
		
		
		
		//戻るボタン
		JButton returntohome = new JButton("Home");
		returntohome.setForeground(text);
		returntohome.setBackground(button);
		returntohome.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
		returntohome.setBounds(650,440,130,50);
		returntohome.setOpaque(true);
		returntohome.setBorderPainted(false);
		
		p.add(title);
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
	      out = new PrintWriter(new OutputStreamWriter(soc.getOutputStream()));
	      ois = new ObjectInputStream(soc.getInputStream());
	      out.println("myPlayerRequest");
	      out.flush();
	      myPlayer = (Player)(ois.readObject());
	      out.close();
	      ois.close();
	    }catch(Exception e) {
	      System.out.println(e);
	      System.exit(1);
	    }
	  }
	
	public void playothello() {//オセロ
		
		int[] grids = game.getGrids(); //getGridメソッドにより局面情報を取得
		
		//ウィンドウ設定
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//ウィンドウを閉じる場合の処理
		setTitle("game");//ウィンドウのタイトル
		setSize(800, 550);//ウィンドウのサイズを設定
		
		//色
		Color back =new Color(55,113,184);
		
		
		Board.setLayout(null);
		Board.setBackground(back);
		
		//アイコン設定(画像ファイルをアイコンとして使う)
		whiteIcon = new ImageIcon("White.jpg");
		blackIcon = new ImageIcon("Black.jpg");
		boardIcon = new ImageIcon("GreenFrame.jpg");
		
		//オセロ盤の生成
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
		turnLabel.setBounds(10, row * 45 + 120, row * 45 + 10, 30);
		Board.add(turnLabel);
		
		add(Board);
		
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
		Board.removeAll();
		int[] grids = game.getGrids(); //getGridメソッドにより局面情報を取得
		
		//色
		Color back =new Color(55,113,184);
				
				
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
		turnLabel.setBounds(10, row * 45 + 150, row * 45 + 10, 30);
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
		boolean isgameover1 = game.isGameover(game.getTurn(),game.getGrids());
		if(isgameover1 == false) {
			boolean putresult = game.putStone(Integer.parseInt(command), game.getTurn());
			if(putresult==true) {
				game.chengeTurn();
			}
			System.out.println("マウスがクリックされました。押されたボタンは " + command + "です。");//テスト用に標準出力
			//sendMessage(command); //メッセージを送信
			updateDisp();
			boolean isgameover2 = game.isGameover(game.getTurn(),game.getGrids());
			if(isgameover2 == true) {
				String winner = game.checkWinner();
				System.out.println(winner);
			}
		}
		if(isgameover1 == true) {
			String winner = game.checkWinner();
			System.out.println(winner);
		}
	}
	
	
	public void mouseEntered(MouseEvent e) {}//マウスがオブジェクトに入ったときの処理
	public void mouseExited(MouseEvent e) {}//マウスがオブジェクトから出たときの処理
	public void mousePressed(MouseEvent e) {}//マウスでオブジェクトを押したときの処理
	public void mouseReleased(MouseEvent e) {}//マウスで押していたオブジェクトを離したときの処理

	//main
	public static void main(String args[]){
		
		Othello game = new Othello();
		Client client = new Client(game);
		client.gamewaiting();
	}
}
