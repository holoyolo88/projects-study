import javax.swing.JFrame;

public class Game { // ���� ���� ����

	public static void main(String[] args) {
		
		JFrame jf = new JFrame("Galaxy Shooter"); //jf ������ ����
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //�ݱ� ��ư Ȱ��ȭ
		
		jf.setContentPane(new GamePanel()); // GamePanel Ŭ������ ���� �г� ����
		
		jf.pack(); // ������ �ڵ� ����
		jf.setVisible(true); // jf ������ Ȱ��ȭ
	}

}
