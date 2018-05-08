import othello.Othello_02;
import java.util.Scanner;

public class Othello_p2 {
  public void printGrids(int[] grids) {
    for(int i = 0; i < 64; i++) {

      if(grids[i] == 1) {
        System.out.print("○");
      } else if(grids[i] == 2) {
        System.out.print("●");
      } else {
        System.out.print("  ");
      }
      if(i%8 == 7) {
        System.out.println();
      }
    }
  }

  public static void main(String[] args) {
    Othello_p2 p2 = new Othello_p2();
    Othello_02 game = new Othello_02();
    int[] grids = game.getGrids();
    String turn = "";
    Scanner sc = new Scanner(System.in);
    p2.printGrids(grids);
    for(int i=0; i < 64; i++) {
      if(i%2 == 0) { //先手
        turn = game.getTurn();
        System.out.print(turn+" ");
        int flag = 0;
        do {
          int mass = sc.nextInt();
          boolean isPut = game.putStone(mass,turn);
          if(isPut == true) {
            flag = 1;
          } else {
            System.out.println("置けない");
          }
        }while(flag == 0);
        game.chengeTurn();
        p2.printGrids(grids);
      } else {  //後手
        turn = game.getTurn();
        System.out.print(turn+" ");
        int flag = 0;
        do {
          int mass = sc.nextInt();
          boolean isPut = game.putStone(mass,turn);
          if(isPut == true) {
            flag = 1;
          } else {
            System.out.println("置けない");
          }
        }while(flag == 0);
        game.chengeTurn();
        p2.printGrids(grids);
      }
    }
  }
}
