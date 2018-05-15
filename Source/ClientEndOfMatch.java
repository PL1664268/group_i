class ClientEndOfMatch {

  private int stopRecFlag = 0;  //受信スレッドのループを抜けるためのフラグ変数
  private BufferedReader in;  //入力ストリーム
  private PrintWriter out;  //出力ストリーム
  private ObjectInputStream ois;  //オブジェクト入力ストリーム
  private ObjectOutputStream oos;  //オブジェクト出力ストリーム※必要ないかも？
  private Socket soc = null;  //ソケット
  /*********プレイヤー情報**************/
  private Player myPlayer;  //自分のPlayerクラスのインスタンス変数
  private Player opponent;  //対局相手のインスタンス変数
  private Othello game;



  /*レートを計算するメソッド----------------------------------------------------------*/
  //yourRate: 自分のレート
  //secondRate: 相手のレート
  //isWin: 勝利-true,敗北-false
  double calRating(double yourRate, double secondRate, boolean isWin) {
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
  /*レートを計算するメソッド-終わり-------------------------------------------------*/
  /*受信スレッドを終了させるメソッド------------------------------------------------*/
  //勝敗結果が引数 win,defeat
  public void endOfMatch(String result) {
    /*レートの更新*/
    double myRating = myPlayer.getRate();
    double opponentRating = opponent.getRate();
    double newMyRating = 0;
    newMyRating = calRating(myRating,opponentRating,result);
    myPlayer.setRate(newMyRating);
    /*勝敗数の更新*/
    if(result.equals("win") == true) {
      myPlayer.setWin(myPlayer.getWin() + 1);
    } else if(resulq.equals("defeat") == true) {
      myPlayer.setDefeat(myPlayer.getDefeat() + 1);
    }
    /*サーバーに結果を送信する*/
    out.println("endOfMatch");
    out.println(result);
    out.println(String.valueOf(newMyRating));
    out.flush();
    /*ストリームをクローズする*/
    out.close();
    in.close();
  }
  /*受信スレッドを終了させるメソッド-終わり------------------------------------------*/
}
