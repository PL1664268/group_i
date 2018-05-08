package ohtello;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server_v1 {

	private int port; //�|�[�g�ԍ�
	private boolean [] online; //�N���C�A���g�ڑ����
	private PrintWriter [] out; //�f�[�^���M�p�I�u�W�F�N�g
	private Receiver [] receiver; //�f�[�^��M�p�I�u�W�F�N�g
	private String password; //�p�X���[�h
	private String user_name; //���[�U��
	private int record; // �΋ǎҏ��
	private List list; //���X�g�I�u�W�F�N�g

	//�R���X�g���N�^
	public Server_v1(int port) { //�҂��󂯃|�[�g�������Ƃ���
		this.port = port; //�҂��󂯃|�[�g��n��
		out = new PrintWriter [2]; //�f�[�^���M�p�I�u�W�F�N�g��2�N���C�A���g���p��
		receiver = new Receiver [2]; //�f�[�^��M�p�I�u�W�F�N�g��2�N���C�A���g���p��
		online = new boolean[2]; //�I�����C����ԊǗ��p�z���p��
	}

	// �f�[�^��M�p�X���b�h(�����N���X)
	class Receiver extends Thread {
		private InputStreamReader sisr; //��M�f�[�^�p�����X�g���[��
		private BufferedReader br; //�����X�g���[���p�̃o�b�t�@
		private int playerNo; //�v���C�������ʂ��邽�߂̔ԍ�

		// �����N���XReceiver�̃R���X�g���N�^
		Receiver (Socket socket, int playerNo){
			try{
				this.playerNo = playerNo; //�v���C���ԍ���n��
				sisr = new InputStreamReader(socket.getInputStream());
				br = new BufferedReader(sisr);
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
							loginCheck(user_name,password);
						}
						else if(inputLine.equals("accountRequest")) {
								String user_name = br.readLine();
								String password = br.readLine();
								accountCreate(user_name, password);
						}
						else if(inputLine.equals("myPlayerRequest")) {
								playerInfo();
						}
						else if() {
							forwardMessage(inputLine, playerNo); //��������ɓ]������
						}
					}
				}
			} catch (IOException e){ // �ڑ����؂ꂽ�Ƃ�
				System.err.println("�v���C�� " + playerNo + "�Ƃ̐ڑ����؂�܂����D");
				online[playerNo] = false; //�v���C���̐ڑ���Ԃ��X�V����
				printStatus(); //�ڑ���Ԃ��o�͂���
			}
		}
	}


	/*********************���\�b�h***********************/

	//�N���C�A���g�̐ڑ�(�T�[�o�̋N��)
	public void acceptClient(){
		int i=0;
		try {
			System.out.println("�T�[�o���N�����܂����D");
			ServerSocket ss = new ServerSocket(port); //�T�[�o�\�P�b�g��p��
			while (true) {
				Socket socket = ss.accept(); //�V�K�ڑ����󂯕t����
				new Receiver(socket,i).start(); //�f�[�^��M�X���b�h�̃X�^�[�g
				i++;
			}
		} catch (Exception e) {
			System.err.println("�\�P�b�g�쐬���ɃG���[���������܂���: " + e);
		}
	}


	//���O�C���F��
	public boolean loginCheck(String user_name, String password) {

		return true;
	}

	//�A�J�E���g�쐬
	public boolean accountCreate(String user_name, String password) {

		try {     
			//FileInputStream�I�u�W�F�N�g�̐���
            FileInputStream inFile = new FileInputStream("players.obj");
            //ObjectInputStream�I�u�W�F�N�g�̐���
            ObjectInputStream inObject = new ObjectInputStream(inFile);
            //�I�u�W�F�N�g�̓ǂݍ���
            Player player = (Player)inObject.readObject();

            while(player!=null){
            	if(player.getName().equals(user_name)){
            		return false;
            	}
            }
            
            //FileOutputStream�I�u�W�F�N�g�̐���
            FileOutputStream outFile = new FileOutputStream("players.obj");
            //ObjectOutputStream�I�u�W�F�N�g�̐���
            ObjectOutputStream outObject = new ObjectOutputStream(outFile);
            //�N���XHello�̃I�u�W�F�N�g�̏�������
            outObject.writeObject(new Player(user_name,password));

            outObject.close();
       }
       catch(Exception e) {
    	   e.printStackTrace();
       }
           return true;

	}

	//Player�̑ΐ퐬�т𑗐M
	public void playerInfo() {

	}
	//�N���C�A���g�ڑ���Ԃ̊m�F
	public void printStatus(){

	}

	//�f�[�^�X�V
	public void dataUpdate() {

	}

	//�΋ǎҏ��̓]��
	public void sendPlayerInfo() {

	}

	//�΋Ǒ҂���Ԏ�t and �ΐ�҃��X�g�]��
	public void sendList(){

;	}

	//�΋ǐ\�����ݓ]��
	public void requestGame(){

	}

	/*//�������̑��M
	public void sendColor(int playerNo) {

	}*/

	//�������]��
	public void forwardMessage(String msg, int playerNo) {

	}

	public static void main(String[] args) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		Server_v1 server = new Server_v1(10000); //�҂��󂯃|�[�g10000�ԂŃT�[�o�I�u�W�F�N�g������
		server.acceptClient(); //�N���C�A���g�󂯓�����J�n
	}

}
