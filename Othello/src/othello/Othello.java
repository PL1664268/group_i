package othello;

public class Othello {
  private int row = 8; //オセロの縦横のマスの数
  private int[] grids = new int[row*row]; //局面情報--何も置かれていない:0 黒:1 白:2
  private String turn; //手番(black,white)

  public Othello() { //コンストラクタ
    turn = "black";  //先手は黒
    for(int i=0; i < row*row; i++ ) {
      grids[i] = 0;
    }
    int center = row*row/2;  //つまり32
    grids[center - row/2 - 1] = 1;  //黒
    grids[center + row/2    ] = 1;  //黒
    grids[center - row/2    ] = 2;  //白
    grids[center + row/2 - 1] = 2;  //白
  }

  /*******************************メソッド**************************************/


  public String checkWinner() {  //勝敗結果を判断
    int black_num = 0;
    int white_num = 0;
    for(int i=0; i<row*row; i++) {
      if(grids[i] == 1) {  //局面が黒だったら
        black_num++;       //カウントしていく
      } else if (grids[i] == 2) {  //局面が白だったら
        white_num++;               //カウントしていく
      }
    }

    if(black_num < white_num) {         //黒が少ないとき
      return "white";
    } else if(black_num > white_num) {  //黒が多いとき
      return "black";
    } else {                     //黒と白が同数のとき
      return "draw";             //"draw"を返す
    }
  }

  public void clearboard() {
	  turn = "black";  //先手は黒
	    for(int i=0; i < row*row; i++ ) {
	      grids[i] = 0;
	    }
	    int center = row*row/2;  //つまり32
	    grids[center - row/2 - 1] = 1;  //黒
	    grids[center + row/2    ] = 1;  //黒
	    grids[center - row/2    ] = 2;  //白
	    grids[center + row/2 - 1] = 2;  //白
  }

  public int black() {
	  int count = 0;

	  for(int k=0;k<63;k++) {
		  if(grids[k] == 1) {
			  count++;
		  }
	  }

	  return count;
  }

  public int white() {
	  int count = 0;

	  for(int k=0;k<63;k++) {
		  if(grids[k] == 2) {
			  count++;
		  }
	  }

	  return count;
  }

  public String getTurn() {  //手番を取得--"black","white"を返す
    return turn;
  }

  public void setGrids(int grid,String color) {  //局面に操作をくわえる
    if(color.equals("black")) {
    		grids[grid] = 1;
    }
    if(color.equals("white")) {
		grids[grid] = 2;
}

  }

  public int[] getGrids() {  //局面情報を取得
    return grids;
  }

  public void chengeTurn() {  //手番を変更
    if(turn.equals("black") == true) {  //黒だったら
      turn = "white";                   //白に変更
    } else {
      turn = "black";                   //そうでなければ黒に変更
    }
  }

//color:黒 or 白, grids[] 局番
  public boolean isGameover(String color, int[] grids) {  //対局終了を判断
    int isContinue = 0;  //石が置けるとき =1
    int colorInt = 0;  //石の色をInteger型に直す
    if(color.equals("black") ==true) {
        colorInt = 1;  //黒なら1
    } else {
        colorInt = 2;  //白なら2
    }
    for(int mass_i = 0; mass_i < 64; mass_i++) {
      if(grids[mass_i] != 0) {  //局面に石があったら
        //System.out.println("すでに石が置かれている");
        continue;      //このループでは、以降の処理をとばす
      }
      int swiStone = 0;  //返した相手のいしの数

      int line = mass_i/8;  //行数
      int row = mass_i%8;  //列数

      int sFlag = 0;  //各方向について、味方の石とはさめるかどうか--はさめたらsFlag=1
      int ssFlag = 0;  //sFlag=1となる場合、ssFlag=1

      int cl = 0;  //移動先の位置
      int cr = 0;
      int dl = 0;  //はさむ味方の石の位置
      int dr = 0;
      int nStone = 0;
      if(colorInt == 1) {  //自分が黒だったら
  /********左上方向***************************************************************/
        cl = line - 1;
        cr = row - 1;
        nStone = cl*8 + cr;
        if(0<=nStone && nStone<=63) {
          if(grids[cl*8 + cr] == 2) {  //隣が相手の石だった場合のみ
            cl--;
            cr--;
            while(cl>=0 && cr>=0) {
              if(grids[cl*8 + cr] == 1) {  //自分の石で挟めたとき
                dl = cl;
                dr = cr;
                sFlag = 1;
                break;  //成功として、ループを抜ける
              }
              if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
                break;  //失敗として、ループを抜ける
              }
              cl--;
              cr--;
            }
            if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
              sFlag = 0;
              ssFlag = 1;
              cl = line - 1;
              cr = row -1;
              while(dl<cl && dr<cr) {  //はさんだ石を返すループ
                swiStone++;  //返した石をカウント
                cl--;
                cr--;
              }
            }
          }
        }

  /********右上方向***************************************************************/
        cl = line - 1;
        cr = row + 1;
        nStone = cl*8 +cr;
        if(0<=nStone && nStone<=63) {
          if(grids[cl*8 + cr] == 2) {  //隣が相手の石だった場合のみ
            cl--;
            cr++;
            while(cl>=0 && cr<=7) {
              if(grids[cl*8 + cr] == 1) {  //自分の石で挟めたとき
                dl = cl;
                dr = cr;
                sFlag = 1;
                break;  //成功として、ループを抜ける
              }
              if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
                break;  //失敗として、ループを抜ける
              }
              cl--;
              cr++;
            }
            if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
              sFlag = 0;
              ssFlag = 1;
              cl = line - 1;
              cr = row + 1;
              while(dl<cl && cr<dr) {  //はさんだ石を返すループ
                swiStone++;  //返した石をカウント
                cl--;
                cr++;
              }
            }
          }
        }
    /********左下方向***************************************************************/

        cl = line + 1;
        cr = row - 1;
        nStone = cl*8 + cr;
        if(0<=nStone && nStone<=63) {
          if(grids[cl*8 + cr] == 2) {  //隣が相手の石だった場合のみ
            cl++;
            cr--;
            while(cl<=7 && cr>=0) {
              if(grids[cl*8 + cr] == 1) {  //自分の石で挟めたとき
                dl = cl;
                dr = cr;
                sFlag = 1;
                break;  //成功として、ループを抜ける
              }
              if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
                break;  //失敗として、ループを抜ける
              }
              cl++;
              cr--;
            }
            if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
              sFlag = 0;
              ssFlag = 1;
              cl = line + 1;
              cr = row -1;
              while(cl<dl && dr<cr) {  //はさんだ石を返すループ
                swiStone++;  //返した石をカウント
                cl++;
                cr--;
              }
            }
          }
        }
  /********右下方向***************************************************************/
        cl = line + 1;
        cr = row + 1;
        nStone = cl*8 + cr;
        if(0<=nStone && nStone<=63) {
          if(grids[cl*8 + cr] == 2) {  //隣が相手の石だった場合のみ
            cl++;
            cr++;
            while(cl<=7 && cr<=7) {
              if(grids[cl*8 + cr] == 1) {  //自分の石で挟めたとき
                dl = cl;
                dr = cr;
                sFlag = 1;
                break;  //成功として、ループを抜ける
              }
              if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
                break;  //失敗として、ループを抜ける
              }
              cl++;
              cr++;
            }
            if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
              sFlag = 0;
              ssFlag = 1;
              cl = line + 1;
              cr = row + 1;
              while(cl<dl && cr<dr) {  //はさんだ石を返すループ
                swiStone++;  //返した石をカウント
                cl++;
                cr++;
              }
            }
          }
        }

  /********上方向***************************************************************/
        cl = line - 1;
        cr = row;
        nStone = cl*8 + cr;
        if(0<=nStone && nStone<=63) {
          if(grids[cl*8 + cr] == 2) {  //隣が相手の石だった場合のみ
            cl--;
            while(cl>=0) {
              if(grids[cl*8 + cr] == 1) {  //自分の石で挟めたとき
                dl = cl;
                dr = cr;
                sFlag = 1;
                break;  //成功として、ループを抜ける
              }
              if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
                break;  //失敗として、ループを抜ける
              }
              cl--;
            }
            if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
              sFlag = 0;
              ssFlag = 1;
              cl = line - 1;
              cr = row;
              while(dl<cl) {  //はさんだ石を返すループ
                swiStone++;  //返した石をカウント
                cl--;
              }
            }
          }
        }

  /********下方向***************************************************************/
        cl = line + 1;
        cr = row;
        nStone = cl*8 + cr;
        if(0<=nStone && nStone<=63) {
          if(grids[cl*8 + cr] == 2) {  //隣が相手の石だった場合のみ
            cl++;
            while(cl<=7) {
              if(grids[cl*8 + cr] == 1) {  //自分の石で挟めたとき
                dl = cl;
                dr = cr;
                sFlag = 1;
                break;  //成功として、ループを抜ける
              }
              if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
                break;  //失敗として、ループを抜ける
              }
              cl++;
            }
            if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
              sFlag = 0;
              ssFlag = 1;
              cl = line + 1;
              cr = row;
              while(cl<dl) {  //はさんだ石を返すループ
                swiStone++;  //返した石をカウント
                cl++;
              }
            }
          }
        }
  /********左方向***************************************************************/
        cl = line;
        cr = row - 1;
        nStone = cl*8 + cr;
        if(0<=nStone && nStone<=63) {
          if(grids[cl*8 + cr] == 2) {  //隣が相手の石だった場合のみ
            cr--;
            while(cr>=0) {
              if(grids[cl*8 + cr] == 1) {  //自分の石で挟めたとき
                dl = cl;
                dr = cr;
                sFlag = 1;
                break;  //成功として、ループを抜ける
              }
              if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
                break;  //失敗として、ループを抜ける
              }
              cr--;
            }
            if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
              sFlag = 0;
              ssFlag = 1;
              cl = line;
              cr = row -1;
              while(dr<cr) {  //はさんだ石を返すループ
                swiStone++;  //返した石をカウント
                cr--;
              }
            }
          }
        }
  /********右方向***************************************************************/
        cl = line;
        cr = row + 1;
        nStone = cl*8 + cr;
        if(0<=nStone && nStone<=63) {
          if(grids[cl*8 + cr] == 2) {  //隣が相手の石だった場合のみ
            cr++;
            while(cr<=7) {
              if(grids[cl*8 + cr] == 1) {  //自分の石で挟めたとき
                dl = cl;
                dr = cr;
                sFlag = 1;
                break;  //成功として、ループを抜ける
              }
              if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
                break;  //失敗として、ループを抜ける
              }
              cr++;
            }
            if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
              sFlag = 0;
              ssFlag = 1;
              cl = line;
              cr = row + 1;
              while(cr<dr) {  //はさんだ石を返すループ
                swiStone++;  //返した石をカウント
                cr++;
              }
            }
          }
        }
      } else if(colorInt == 2) {             //自分が白だったら
  /********左上方向***************************************************************/
        cl = line - 1;
        cr = row - 1;
        nStone = cl*8 + cr;
        if(0<=nStone && nStone<=63) {
          if(grids[cl*8 + cr] == 1) {  //隣が相手の石だった場合のみ
            cl--;
            cr--;
            while(cl>=0 && cr>=0) {
              if(grids[cl*8 + cr] == 2) {  //自分の石で挟めたとき
                dl = cl;
                dr = cr;
                sFlag = 1;
                break;  //成功として、ループを抜ける
              }
              if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
                break;  //失敗として、ループを抜ける
              }
              cl--;
              cr--;
            }
            if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
              sFlag = 0;
              ssFlag = 1;
              cl = line - 1;
              cr = row -1;
              while(dl<cl && dr<cr) {  //はさんだ石を返すループ
                swiStone++;  //返した石をカウント
                cl--;
                cr--;
              }
            }
          }
        }
    /********右上方向***************************************************************/
        cl = line - 1;
        cr = row + 1;
        nStone = cl*8 + cr;
        if(0<=nStone && nStone<=63) {
          if(grids[cl*8 + cr] == 1) {  //隣が相手の石だった場合のみ
            cl--;
            cr++;
            while(cl>=0 && cr<=7) {
              if(grids[cl*8 + cr] == 2) {  //自分の石で挟めたとき
                dl = cl;
                dr = cr;
                sFlag = 1;
                break;  //成功として、ループを抜ける
              }
              if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
                break;  //失敗として、ループを抜ける
              }
              cl--;
              cr++;
            }
            if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
              sFlag = 0;
              ssFlag = 1;
              cl = line - 1;
              cr = row + 1;
              while(dl<cl && cr<dr) {  //はさんだ石を返すループ
                swiStone++;  //返した石をカウント
                cl--;
                cr++;
              }
            }
          }
        }

  /********左下方向***************************************************************/

        cl = line + 1;
        cr = row - 1;
        nStone = cl*8 + cr;
        if(0<=nStone && nStone<=63) {
          if(grids[cl*8 + cr] == 1) {  //隣が相手の石だった場合のみ
            cl++;
            cr--;
            while(cl<=7 && cr>=0) {
              if(grids[cl*8 + cr] == 2) {  //自分の石で挟めたとき
                dl = cl;
                dr = cr;
                sFlag = 1;
                break;  //成功として、ループを抜ける
              }
              if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
                break;  //失敗として、ループを抜ける
              }
              cl++;
              cr--;
            }
            if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
              sFlag = 0;
              ssFlag = 1;
              cl = line + 1;
              cr = row -1;
              while(cl<dl && dr<cr) {  //はさんだ石を返すループ
                swiStone++;  //返した石をカウント
                cl++;
                cr--;
              }
            }
          }
        }

  /********右下方向***************************************************************/
        cl = line + 1;
        cr = row + 1;
        nStone = cl*8 + cr;
        if(0<=nStone && nStone<= 63) {
          if(grids[cl*8 + cr] == 1) {  //隣が相手の石だった場合のみ
            cl++;
            cr++;
            while(cl<=7 && cr<=7) {
              if(grids[cl*8 + cr] == 2) {  //自分の石で挟めたとき
                dl = cl;
                dr = cr;
                sFlag = 1;
                break;  //成功として、ループを抜ける
              }
              if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
                break;  //失敗として、ループを抜ける
              }
              cl++;
              cr++;
            }
            if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
              sFlag = 0;
              ssFlag = 1;
              cl = line + 1;
              cr = row + 1;
              while(cl<dl && cr<dr) {  //はさんだ石を返すループ
                swiStone++;  //返した石をカウント
                cl++;
                cr++;
              }
            }
          }
        }

  /********上方向***************************************************************/
        cl = line - 1;
        cr = row;
        nStone = cl*8 + cr;
        if(0<=nStone && nStone<=63) {
          if(grids[cl*8 + cr] == 1) {  //隣が相手の石だった場合のみ
            cl--;
            while(cl>=0) {
              if(grids[cl*8 + cr] == 2) {  //自分の石で挟めたとき
                dl = cl;
                dr = cr;
                sFlag = 1;
                break;  //成功として、ループを抜ける
              }
              if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
                break;  //失敗として、ループを抜ける
              }
              cl--;
            }
            if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
              sFlag = 0;
              ssFlag = 1;
              cl = line - 1;
              cr = row;
              while(dl<cl) {  //はさんだ石を返すループ
                swiStone++;  //返した石をカウント
                cl--;
              }
            }
          }
        }
  /********下方向***************************************************************/
        cl = line + 1;
        cr = row;
        nStone = cl*8 + cr;
        if(0<=nStone && nStone<=63) {
          if(grids[cl*8 + cr] == 1) {  //隣が相手の石だった場合のみ
            cl++;
            while(cl<=7) {
              if(grids[cl*8 + cr] == 2) {  //自分の石で挟めたとき
                dl = cl;
                dr = cr;
                sFlag = 1;
                break;  //成功として、ループを抜ける
              }
              if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
                break;  //失敗として、ループを抜ける
              }
              cl++;
            }
            if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
              sFlag = 0;
              ssFlag = 1;
              cl = line + 1;
              cr = row;
              while(cl<dl) {  //はさんだ石を返すループ
                swiStone++;  //返した石をカウント
                cl++;
              }
            }
          }
        }
  /********左方向***************************************************************/
        cl = line;
        cr = row - 1;
        nStone = cl*8 + cr;
        if(0<=nStone && nStone <=63) {
          if(grids[cl*8 + cr] == 1) {  //隣が相手の石だった場合のみ
            cr--;
            while(cr>=0) {
              if(grids[cl*8 + cr] == 2) {  //自分の石で挟めたとき
                dl = cl;
                dr = cr;
                sFlag = 1;
                break;  //成功として、ループを抜ける
              }
              if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
                break;  //失敗として、ループを抜ける
              }
              cr--;
            }
            if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
              sFlag = 0;
              ssFlag = 1;
              cl = line;
              cr = row -1;
              while(dr<cr) {  //はさんだ石を返すループ
                swiStone++;  //返した石をカウント
                cr--;
              }
            }
          }
        }
  /********右方向***************************************************************/
        cl = line;
        cr = row + 1;
        nStone = cl*8 + cr;
        if(0<=nStone && nStone<=63) {
          if(grids[cl*8 + cr] == 1) {  //隣が相手の石だった場合のみ
            cr++;
            while(cr<=7) {
              if(grids[cl*8 + cr] == 2) {  //自分の石で挟めたとき
                dl = cl;
                dr = cr;
                sFlag = 1;
                break;  //成功として、ループを抜ける
              }
              if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
                break;  //失敗として、ループを抜ける
              }
              cr++;
            }
            if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
              sFlag = 0;
              ssFlag = 1;
              cl = line;
              cr = row + 1;
              while(cr<dr) {  //はさんだ石を返すループ
                swiStone++;  //返した石をカウント
                cr++;
              }
            }
          }
        }

      }

      if(swiStone != 0) {  //石が置けるマスがある時、falseを返す。
        System.out.println("石を置けるマスがあります");
        return false;
      }
    }
    System.out.println("石を置けるマスがありません。" + color + "の番をとばします");
    return true;
  }


  public boolean putStone(int i, String color) {  //局面に操作を反映
    if(i<0 || 63<i) {  //局面外なら
      System.out.println("局面外");
      return false;          //falseを返す
    }
    if(grids[i] != 0) {  //局面に石があったら
      System.out.println("すでに石が置かれている");
      return false;      //falseを返す
    }

    int colorInt = 0;  //石の色をInteger型に直す
    if(color.equals("black") ==true) {
      colorInt = 1;  //黒なら1
    } else {
      colorInt = 2;  //白なら2
    }
    int swiStone = 0;  //返した相手のいしの数

    int line = i/8;  //行数
    int row = i%8;  //列数

    int sFlag = 0;  //各方向について、味方の石とはさめるかどうか--はさめたらsFlag=1
    int ssFlag = 0;  //sFlag=1となる場合、ssFlag=1

    int cl = 0;  //移動先の位置
    int cr = 0;
    int dl = 0;  //はさむ味方の石の位置
    int dr = 0;
    int nStone = 0;
    if(colorInt == 1) {  //自分が黒だったら
/********左上方向***************************************************************/
      cl = line - 1;
      cr = row - 1;
      nStone = cl*8 + cr;
      if(0<=nStone && nStone<=63) {
        if(grids[cl*8 + cr] == 2) {  //隣が相手の石だった場合のみ
          cl--;
          cr--;
          while(cl>=0 && cr>=0) {
            if(grids[cl*8 + cr] == 1) {  //自分の石で挟めたとき
              dl = cl;
              dr = cr;
              sFlag = 1;
              break;  //成功として、ループを抜ける
            }
            if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
              break;  //失敗として、ループを抜ける
            }
            cl--;
            cr--;
          }
          if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
            sFlag = 0;
            ssFlag = 1;
            cl = line - 1;
            cr = row -1;
            while(dl<cl && dr<cr) {  //はさんだ石を返すループ
              grids[cl*8 + cr] = 1;  //自分の色の石にする
              swiStone++;  //返した石をカウント
              cl--;
              cr--;
            }
          }
        }
      }

/********右上方向***************************************************************/
      cl = line - 1;
      cr = row + 1;
      nStone = cl*8 +cr;
      if(0<=nStone && nStone<=63) {
        if(grids[cl*8 + cr] == 2) {  //隣が相手の石だった場合のみ
          cl--;
          cr++;
          while(cl>=0 && cr<=7) {
            if(grids[cl*8 + cr] == 1) {  //自分の石で挟めたとき
              dl = cl;
              dr = cr;
              sFlag = 1;
              break;  //成功として、ループを抜ける
            }
            if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
              break;  //失敗として、ループを抜ける
            }
            cl--;
            cr++;
          }
          if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
            sFlag = 0;
            ssFlag = 1;
            cl = line - 1;
            cr = row + 1;
            while(dl<cl && cr<dr) {  //はさんだ石を返すループ
              grids[cl*8 + cr] = 1;  //自分の色の石にする
              swiStone++;  //返した石をカウント
              cl--;
              cr++;
            }
          }
        }
      }
  /********左下方向***************************************************************/

      cl = line + 1;
      cr = row - 1;
      nStone = cl*8 + cr;
      if(0<=nStone && nStone<=63) {
        if(grids[cl*8 + cr] == 2) {  //隣が相手の石だった場合のみ
          cl++;
          cr--;
          while(cl<=7 && cr>=0) {
            if(grids[cl*8 + cr] == 1) {  //自分の石で挟めたとき
              dl = cl;
              dr = cr;
              sFlag = 1;
              break;  //成功として、ループを抜ける
            }
            if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
              break;  //失敗として、ループを抜ける
            }
            cl++;
            cr--;
          }
          if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
            sFlag = 0;
            ssFlag = 1;
            cl = line + 1;
            cr = row -1;
            while(cl<dl && dr<cr) {  //はさんだ石を返すループ
              grids[cl*8 + cr] = 1;  //自分の色の石にする
              swiStone++;  //返した石をカウント
              cl++;
              cr--;
            }
          }
        }
      }
/********右下方向***************************************************************/
      cl = line + 1;
      cr = row + 1;
      nStone = cl*8 + cr;
      if(0<=nStone && nStone<=63) {
        if(grids[cl*8 + cr] == 2) {  //隣が相手の石だった場合のみ
          cl++;
          cr++;
          while(cl<=7 && cr<=7) {
            if(grids[cl*8 + cr] == 1) {  //自分の石で挟めたとき
              dl = cl;
              dr = cr;
              sFlag = 1;
              break;  //成功として、ループを抜ける
            }
            if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
              break;  //失敗として、ループを抜ける
            }
            cl++;
            cr++;
          }
          if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
            sFlag = 0;
            ssFlag = 1;
            cl = line + 1;
            cr = row + 1;
            while(cl<dl && cr<dr) {  //はさんだ石を返すループ
              grids[cl*8 + cr] = 1;  //自分の色の石にする
              swiStone++;  //返した石をカウント
              cl++;
              cr++;
            }
          }
        }
      }

/********上方向***************************************************************/
      cl = line - 1;
      cr = row;
      nStone = cl*8 + cr;
      if(0<=nStone && nStone<=63) {
        if(grids[cl*8 + cr] == 2) {  //隣が相手の石だった場合のみ
          cl--;
          while(cl>=0) {
            if(grids[cl*8 + cr] == 1) {  //自分の石で挟めたとき
              dl = cl;
              dr = cr;
              sFlag = 1;
              break;  //成功として、ループを抜ける
            }
            if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
              break;  //失敗として、ループを抜ける
            }
            cl--;
          }
          if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
            sFlag = 0;
            ssFlag = 1;
            cl = line - 1;
            cr = row;
            while(dl<cl) {  //はさんだ石を返すループ
              grids[cl*8 + cr] = 1;  //自分の色の石にする
              swiStone++;  //返した石をカウント
              cl--;
            }
          }
        }
      }

/********下方向***************************************************************/
      cl = line + 1;
      cr = row;
      nStone = cl*8 + cr;
      if(0<=nStone && nStone<=63) {
        if(grids[cl*8 + cr] == 2) {  //隣が相手の石だった場合のみ
          cl++;
          while(cl<=7) {
            if(grids[cl*8 + cr] == 1) {  //自分の石で挟めたとき
              dl = cl;
              dr = cr;
              sFlag = 1;
              break;  //成功として、ループを抜ける
            }
            if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
              break;  //失敗として、ループを抜ける
            }
            cl++;
          }
          if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
            sFlag = 0;
            ssFlag = 1;
            cl = line + 1;
            cr = row;
            while(cl<dl) {  //はさんだ石を返すループ
              grids[cl*8 + cr] = 1;  //自分の色の石にする
              swiStone++;  //返した石をカウント
              cl++;
            }
          }
        }
      }
/********左方向***************************************************************/
      cl = line;
      cr = row - 1;
      nStone = cl*8 + cr;
      if(0<=nStone && nStone<=63) {
        if(grids[cl*8 + cr] == 2) {  //隣が相手の石だった場合のみ
          cr--;
          while(cr>=0) {
            if(grids[cl*8 + cr] == 1) {  //自分の石で挟めたとき
              dl = cl;
              dr = cr;
              sFlag = 1;
              break;  //成功として、ループを抜ける
            }
            if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
              break;  //失敗として、ループを抜ける
            }
            cr--;
          }
          if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
            sFlag = 0;
            ssFlag = 1;
            cl = line;
            cr = row -1;
            while(dr<cr) {  //はさんだ石を返すループ
              grids[cl*8 + cr] = 1;  //自分の色の石にする
              swiStone++;  //返した石をカウント
              cr--;
            }
          }
        }
      }
/********右方向***************************************************************/
      cl = line;
      cr = row + 1;
      nStone = cl*8 + cr;
      if(0<=nStone && nStone<=63) {
        if(grids[cl*8 + cr] == 2) {  //隣が相手の石だった場合のみ
          cr++;
          while(cr<=7) {
            if(grids[cl*8 + cr] == 1) {  //自分の石で挟めたとき
              dl = cl;
              dr = cr;
              sFlag = 1;
              break;  //成功として、ループを抜ける
            }
            if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
              break;  //失敗として、ループを抜ける
            }
            cr++;
          }
          if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
            sFlag = 0;
            ssFlag = 1;
            cl = line;
            cr = row + 1;
            while(cr<dr) {  //はさんだ石を返すループ
              grids[cl*8 + cr] = 1;  //自分の色の石にする
              swiStone++;  //返した石をカウント
              cr++;
            }
          }
        }
      }
    } else if(colorInt == 2) {             //自分が白だったら
/********左上方向***************************************************************/
      cl = line - 1;
      cr = row - 1;
      nStone = cl*8 + cr;
      if(0<=nStone && nStone<=63) {
        if(grids[cl*8 + cr] == 1) {  //隣が相手の石だった場合のみ
          cl--;
          cr--;
          while(cl>=0 && cr>=0) {
            if(grids[cl*8 + cr] == 2) {  //自分の石で挟めたとき
              dl = cl;
              dr = cr;
              sFlag = 1;
              break;  //成功として、ループを抜ける
            }
            if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
              break;  //失敗として、ループを抜ける
            }
            cl--;
            cr--;
          }
          if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
            sFlag = 0;
            ssFlag = 1;
            cl = line - 1;
            cr = row -1;
            while(dl<cl && dr<cr) {  //はさんだ石を返すループ
              grids[cl*8 + cr] = 2;  //自分の色の石にする
              swiStone++;  //返した石をカウント
              cl--;
              cr--;
            }
          }
        }
      }
  /********右上方向***************************************************************/
      cl = line - 1;
      cr = row + 1;
      nStone = cl*8 + cr;
      if(0<=nStone && nStone<=63) {
        if(grids[cl*8 + cr] == 1) {  //隣が相手の石だった場合のみ
          cl--;
          cr++;
          while(cl>=0 && cr<=7) {
            if(grids[cl*8 + cr] == 2) {  //自分の石で挟めたとき
              dl = cl;
              dr = cr;
              sFlag = 1;
              break;  //成功として、ループを抜ける
            }
            if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
              break;  //失敗として、ループを抜ける
            }
            cl--;
            cr++;
          }
          if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
            sFlag = 0;
            ssFlag = 1;
            cl = line - 1;
            cr = row + 1;
            while(dl<cl && cr<dr) {  //はさんだ石を返すループ
              grids[cl*8 + cr] = 2;  //自分の色の石にする
              swiStone++;  //返した石をカウント
              cl--;
              cr++;
            }
          }
        }
      }

/********左下方向***************************************************************/

      cl = line + 1;
      cr = row - 1;
      nStone = cl*8 + cr;
      if(0<=nStone && nStone<=63) {
        if(grids[cl*8 + cr] == 1) {  //隣が相手の石だった場合のみ
          cl++;
          cr--;
          while(cl<=7 && cr>=0) {
            if(grids[cl*8 + cr] == 2) {  //自分の石で挟めたとき
              dl = cl;
              dr = cr;
              sFlag = 1;
              break;  //成功として、ループを抜ける
            }
            if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
              break;  //失敗として、ループを抜ける
            }
            cl++;
            cr--;
          }
          if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
            sFlag = 0;
            ssFlag = 1;
            cl = line + 1;
            cr = row -1;
            while(cl<dl && dr<cr) {  //はさんだ石を返すループ
              grids[cl*8 + cr] = 2;  //自分の色の石にする
              swiStone++;  //返した石をカウント
              cl++;
              cr--;
            }
          }
        }
      }

/********右下方向***************************************************************/
      cl = line + 1;
      cr = row + 1;
      nStone = cl*8 + cr;
      if(0<=nStone && nStone<= 63) {
        if(grids[cl*8 + cr] == 1) {  //隣が相手の石だった場合のみ
          cl++;
          cr++;
          while(cl<=7 && cr<=7) {
            if(grids[cl*8 + cr] == 2) {  //自分の石で挟めたとき
              dl = cl;
              dr = cr;
              sFlag = 1;
              break;  //成功として、ループを抜ける
            }
            if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
              break;  //失敗として、ループを抜ける
            }
            cl++;
            cr++;
          }
          if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
            sFlag = 0;
            ssFlag = 1;
            cl = line + 1;
            cr = row + 1;
            while(cl<dl && cr<dr) {  //はさんだ石を返すループ
              grids[cl*8 + cr] = 2;  //自分の色の石にする
              swiStone++;  //返した石をカウント
              cl++;
              cr++;
            }
          }
        }
      }

/********上方向***************************************************************/
      cl = line - 1;
      cr = row;
      nStone = cl*8 + cr;
      if(0<=nStone && nStone<=63) {
        if(grids[cl*8 + cr] == 1) {  //隣が相手の石だった場合のみ
          cl--;
          while(cl>=0) {
            if(grids[cl*8 + cr] == 2) {  //自分の石で挟めたとき
              dl = cl;
              dr = cr;
              sFlag = 1;
              break;  //成功として、ループを抜ける
            }
            if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
              break;  //失敗として、ループを抜ける
            }
            cl--;
          }
          if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
            sFlag = 0;
            ssFlag = 1;
            cl = line - 1;
            cr = row;
            while(dl<cl) {  //はさんだ石を返すループ
              grids[cl*8 + cr] = 2;  //自分の色の石にする
              swiStone++;  //返した石をカウント
              cl--;
            }
          }
        }
      }
/********下方向***************************************************************/
      cl = line + 1;
      cr = row;
      nStone = cl*8 + cr;
      if(0<=nStone && nStone<=63) {
        if(grids[cl*8 + cr] == 1) {  //隣が相手の石だった場合のみ
          cl++;
          while(cl<=7) {
            if(grids[cl*8 + cr] == 2) {  //自分の石で挟めたとき
              dl = cl;
              dr = cr;
              sFlag = 1;
              break;  //成功として、ループを抜ける
            }
            if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
              break;  //失敗として、ループを抜ける
            }
            cl++;
          }
          if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
            sFlag = 0;
            ssFlag = 1;
            cl = line + 1;
            cr = row;
            while(cl<dl) {  //はさんだ石を返すループ
              grids[cl*8 + cr] = 2;  //自分の色の石にする
              swiStone++;  //返した石をカウント
              cl++;
            }
          }
        }
      }
/********左方向***************************************************************/
      cl = line;
      cr = row - 1;
      nStone = cl*8 + cr;
      if(0<=nStone && nStone <=63) {
        if(grids[cl*8 + cr] == 1) {  //隣が相手の石だった場合のみ
          cr--;
          while(cr>=0) {
            if(grids[cl*8 + cr] == 2) {  //自分の石で挟めたとき
              dl = cl;
              dr = cr;
              sFlag = 1;
              break;  //成功として、ループを抜ける
            }
            if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
              break;  //失敗として、ループを抜ける
            }
            cr--;
          }
          if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
            sFlag = 0;
            ssFlag = 1;
            cl = line;
            cr = row -1;
            while(dr<cr) {  //はさんだ石を返すループ
              grids[cl*8 + cr] = 2;  //自分の色の石にする
              swiStone++;  //返した石をカウント
              cr--;
            }
          }
        }
      }
/********右方向***************************************************************/
      cl = line;
      cr = row + 1;
      nStone = cl*8 + cr;
      if(0<=nStone && nStone<=63) {
        if(grids[cl*8 + cr] == 1) {  //隣が相手の石だった場合のみ
          cr++;
          while(cr<=7) {
            if(grids[cl*8 + cr] == 2) {  //自分の石で挟めたとき
              dl = cl;
              dr = cr;
              sFlag = 1;
              break;  //成功として、ループを抜ける
            }
            if(grids[cl*8 + cr] == 0) {  //マスに石がなかった時
              break;  //失敗として、ループを抜ける
            }
            cr++;
          }
          if(sFlag == 1) { //成功:挟めたときsFlag=1    失敗:白しかなかった or マスに石がなかった
            sFlag = 0;
            ssFlag = 1;
            cl = line;
            cr = row + 1;
            while(cr<dr) {  //はさんだ石を返すループ
              grids[cl*8 + cr] = 2;  //自分の色の石にする
              swiStone++;  //返した石をカウント
              cr++;
            }
          }
        }
      }
    }

    if(swiStone == 0) {  //返した石の数が０のとき、falseを返す
      System.out.println("返せる石がありません");
      return false;
    }
    if(colorInt == 1) {  //黒だったら
      grids[i] = 1;
    } else if(colorInt == 2) {  //白だったら
      grids[i] = 2;
    }
    return true;
  }
}
