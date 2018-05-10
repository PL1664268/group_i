
class CalRating {
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

  public static void main(String[] args) {
    CalRating calr = new CalRating();
    double myRating = 1300;
    double yourRating = 1500;
    double new_myRating = calr.calRating(myRating,yourRating,false);
    double new_yourRating = calr.calRating(yourRating,myRating,true);
    System.out.println("myRating: "+String.valueOf((int)(new_myRating)));
    System.out.println("yourRating: "+String.valueOf((int)(new_yourRating)));
  }
}
