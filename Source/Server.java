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
	private int port; //�|�[�g�ԍ�
	private boolean [] online; //�N���C�A���g�ڑ����
	private PrintWriter [] out; //�f�[�^���M�p�I�u�W�F�N�g
	private Receiver [] receiver; //�f�[�^��M�p�I�u�W�F�N�g
	private String password; //�p�X���[�h
	private String user_name; //���[�U��
	ArrayList<Player> game_online_list = new ArrayList<Player>(); //���X�g�I�u�W�F�N�g
	  Receiver receiveThread[ ]= new Receiver[MAX];      // �`���b�g�N���X�z��
	  //Thread receiveThread[ ] = new Thread[MAX];   // �N���C�A���g�Ƃ̓��o�̓X���b�h
	 

	//�R���X�g���N�^
	public Server(int port) { //�҂��󂯃|�[�g�������Ƃ���
		this.port = port; //�҂��󂯃|�[�g��n��
		out = new PrintWriter [2]; //�f�[�^���M�p�I�u�W�F�N�g��2�N���C�A���g���p��
		receiver = new Receiver [2]; //�f�[�^��M�p�I�u�W�F�N�g��2�N���C�A���g���p��
		online = new boolean[2]; //�I�����C����ԊǗ��p�z���p��
	}

	// �f�[�^��M�p�X���b�h(�����N���X)
	class Receiver extends Thread{
		private InputStreamReader sisr; //��M�f�[�^�p�����X�g���[��
		private BufferedReader br; //�����X�g���[���p�̃o�b�t�@
		private PrintWriter out;
		private int ThreadNo; //�v���C�������ʂ��邽�߂̔ԍ�
		private Player player = new Player("dammy", "dammy");
		// �����N���XReceiver�̃R���X�g���N�^
		Receiver (Socket socket, Server server, int ThreadNo){
			try{
				this.ThreadNo = ThreadNo; //�v���C���ԍ���n��
				sisr = new InputStreamReader(socket.getInputStream());
				br = new BufferedReader(sisr);
				out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				System.out.println(ThreadNo+"�����������܂���");
			} catch (IOException e) {
				System.err.println("�f�[�^��M���ɃG���[���������܂���: " + e);
			}
		}
		// �����N���X Receiver�̃��\�b�h
		public void run(){
			try{
				while(true) {// �f�[�^����M��������
					String inputLine = br.readLine();//�f�[�^����s���ǂݍ���
					if (inputLine != null){ //�f�[�^����M������
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
							forwardMessage(inputLine, ThreadNo); //��������ɓ]������
						}
					}
				}
			} catch (IOException e){ // �ڑ����؂ꂽ�Ƃ�
				System.err.println("�v���C�� " + ThreadNo + "�Ƃ̐ڑ����؂�܂����D");
				game_online_list.remove(game_online_list.indexOf(player));
				online[ThreadNo] = false; //�v���C���̐ڑ���Ԃ��X�V����
				printStatus(); //�ڑ���Ԃ��o�͂���
			}
		}
		public synchronized void sendMessage(String message) {
		    out.println(message);         // �X���b�h�����΂���N���C�A���g�ɑ��M
		    out.flush( );             // �o�b�t�@���̃f�[�^�������I�ɑ��M
		  }
	}


	/*********************���\�b�h***********************/

	//�N���C�A���g�̐ڑ�(�T�[�o�̋N��)
	public void acceptClient(){
		int i=1;
		try {
			System.out.println("�T�[�o���N�����܂����D");
			ServerSocket ss = new ServerSocket(port); //�T�[�o�\�P�b�g��p��
			while (true) {
			       int p;
			        for (p = 0; p < MAX; p++){      // �ڑ��`�F�b�N
			          if (receiveThread[p] == null)    // �󂢂Ă���ꍇ
			            break;
			        }
			        if (p == MAX)           // �󂢂Ă��Ȃ��ꍇ
			            continue;             // �ȉ��̏��������Ȃ�

				Socket socket = ss.accept(); //�V�K�ڑ����󂯕t����
				receiveThread[p] = new Receiver(socket, this, p);      
		        receiveThread[p].start( );       // �X���b�h�X�^�[�g
		     }
		} catch (Exception e) {
			System.err.println("�\�P�b�g�쐬���ɃG���[���������܂���: " + e);
		}
	}


	//���O�C���F��
	public String loginCheck(String user_name, String password) {
		try{
			//FileInputStream�I�u�W�F�N�g�̐���
			FileInputStream inFile = new FileInputStream("players.obj");
	        //�I�u�W�F�N�g�̓ǂݍ���
            Player player;

            try{
            while(true){
            	//ObjectInputStream�I�u�W�F�N�g�̐���
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

	//�A�J�E���g�쐬
	public String accountCreate(String user_name, String password) {


		try {
			//FileInputStream�I�u�W�F�N�g�̐���
            FileInputStream inFile = new FileInputStream("players.obj");

            //�I�u�W�F�N�g�̓ǂݍ���
            Player player;

            try{
            while(true){
            	//ObjectInputStream�I�u�W�F�N�g�̐���
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
            
            //FileOutputStream�I�u�W�F�N�g�̐���
            FileOutputStream outFile = new FileOutputStream("players.obj",true);
            //ObjectOutputStream�I�u�W�F�N�g�̐���
            ObjectOutputStream outObject = new ObjectOutputStream(outFile);
            //�N���XHello�̃I�u�W�F�N�g�̏�������
            outObject.writeObject(new Player(user_name,password));

            outObject.close();
       }
       catch(Exception e) {
    	   e.printStackTrace();
       }
		System.out.println("true");
        return "permit";
	}

	//Player�̑ΐ퐬�т𑗐M
	public Player playerInfo(String user_name) {
		//Player�I�u�W�F�N�g���i�[����ϐ�
		Player player = new Player("dammy", "dammy");

        try{
        	//FileInputStream�I�u�W�F�N�g�̐���
            FileInputStream inFile = new FileInputStream("players.obj");

       //�Y������I�u�W�F�N�g��T��
       while(true){
        	//ObjectInputStream�I�u�W�F�N�g�̐���
        	ObjectInputStream inObject = new ObjectInputStream(inFile);
        	player = (Player)inObject.readObject(); //�ǂݍ���
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
       return player; //�I�u�W�F�N�g�����^�[���A�N���C�A���g�֑���B
	}

	//�N���C�A���g�ڑ���Ԃ̊m�F
	public void printStatus(){
		int i=0;
		while(i<=online.length) {
			if(online[i]==true)
				System.out.println("PlayerNo"+i+"�̓I�����C����Ԃł�");
			else if(online[i]==false)
				System.out.println("PlayerNo"+i+"�̓I�t���C����Ԃł�");
		}
	}

	//�f�[�^�X�V
	public void dataUpdate(String user_name, String result) {
		Player player;
		ObjectInputStream inObject;
		ArrayList<Player> arr = new ArrayList<Player>();
        try{
        	//FileInputStream�I�u�W�F�N�g�̐���
            FileInputStream inFile = new FileInputStream("players.obj");
            //�f�[�^�X�V������I�u�W�F�N�g��T��
            while(true){
            	//ObjectInputStream�I�u�W�F�N�g�̐���
            	inObject = new ObjectInputStream(inFile);
            	player = (Player)inObject.readObject(); //�ǂݍ���
            	
            	System.out.println(player.getName());
            	System.out.println(player.getWin());
            	if(player.getName().equals("user2")){
               		//���ʂɉ����ăf�[�^���X�V
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
        	 System.out.println("����");
        }
        
        try{
           	int i=0;
            for(Player p : arr){
            	if(i==0){
                    FileOutputStream outFile1 = new FileOutputStream("players.obj");
                    //FileOutputStream�I�u�W�F�N�g�̐���
                   	ObjectOutputStream outObject1 = new ObjectOutputStream(outFile1);
            		outObject1.writeObject(p);  
            		i++;
            	}
            	else{
                    //FileOutputStream�I�u�W�F�N�g�̐���
                    FileOutputStream outFile2 = new FileOutputStream("players.obj",true);
                    //FileOutputStream�I�u�W�F�N�g�̐���
                   	ObjectOutputStream outObject2 = new ObjectOutputStream(outFile2);
                     		outObject2.writeObject(p);  

            	}
            	
            	System.out.println(p.getName());
            }
          
                
               	
            }catch(Exception e){
        	
        }
	}

	/*//�΋Ǒ҂���Ԏ�t and �ΐ�҃��X�g�]��
	public void sendList(){

	}*/

	//�΋ǐ\�����ݓ]��
	public void requestGame(int opponent,){
		game_online_list.get(opponent);
		receiveThread[opponent].sendMessage("requestGame");
	}

	//�������]��
	public void forwardMessage(String msg, int playerNo) {

	}

	public static void main(String[] args) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		Server server = new Server(10005); //�҂��󂯃|�[�g10000�ԂŃT�[�o�I�u�W�F�N�g������
		server.acceptClient(); //�N���C�A���g�󂯓�����J�n
	}

}
