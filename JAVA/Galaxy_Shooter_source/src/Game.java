import javax.swing.JFrame;

public class Game {

	// 게임 시작
	public static void main(String[] args) {

		// 프레임 생성
		JFrame jf = new JFrame("Galaxy Shooter");
		// 닫기 버튼 활성화
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// GamePanel 클래스 객체를 패널로 설정
		jf.setContentPane(new GamePanel());

		// 프레임 크기 자동 조절
		jf.pack();

		// 프레임 활성화
		jf.setVisible(true);
	}
}
