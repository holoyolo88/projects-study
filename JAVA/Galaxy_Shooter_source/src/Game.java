import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Game { // ���� ���� ����
	public static void main(String[] args) {
		
		JFrame jf = new JFrame("Galaxy Shooter"); //jf ������ ����
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //�ݱ� ��ư Ȱ��ȭ
		
		jf.setContentPane(new GamePanel()); // GamePanel Ŭ������ ���� �г� ����
		jf.pack();
		jf.setVisible(true); // ������ �ڵ� ����
		
		
	}

}
