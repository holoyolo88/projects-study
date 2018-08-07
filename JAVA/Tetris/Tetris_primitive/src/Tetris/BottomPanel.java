package Tetris;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

public class BottomPanel extends JPanel {

	private static final long serialVersionUID = -4722478954796124882L;
	/**
	 * 입력받는 게임 속도.
	 */
	public float SpeedLevel;
	/**
	 * 1p 선택을 위한 라디오 버튼.
	 */
	public static JRadioButton radiobtn1;
	/**
	 * 2p 선택을 위한 라디오 버튼.
	 */
	public static JRadioButton radiobtn2;

	/**
	 * 새로은 탑 판넬 인스턴스를 생성.
	 * 
	 * @param tetris
	 *            : Tetris 인스턴트를 사용 가능.
	 */
	public BottomPanel(Tetris tetris) {
		// 라디오 버튼 생성.
		radiobtn1 = new JRadioButton("1P", true);
		radiobtn2 = new JRadioButton("2P");
		ButtonGroup rbg = new ButtonGroup();
		rbg.add(radiobtn1);
		rbg.add(radiobtn2);
		// 레이블 생성.
		JLabel label = new JLabel("Set speed level");
		label.setBounds(56, 25, 86, 15);
		// 슬라이더 버튼 생성.
		JSlider slider = new JSlider(1, 10, 1);
		slider.setMajorTickSpacing(1);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.setPaintTrack(false);
		slider.setBounds(23, 50, 148, 62);
		// 스타트 버튼 생성.
		JButton btn = new JButton("START");
		btn.setBounds(23, 122, 148, 40);
		// 판넬에 추가..
		add(radiobtn1);
		add(radiobtn2);
		add(label);
		add(slider);
		add(btn);

		// 버튼 이벤트 발생시.
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e2) {
				// 1p 선택시.
				if (radiobtn1.isSelected()) {
					SpeedLevel = slider.getValue();
					tetris.resetGame();
					tetris.requestFocus();
					setVisible(false);					
				}
				// 2p 선택시.
				else if (radiobtn2.isSelected()) {
					SpeedLevel = slider.getValue();
					tetris.resetGame();
					tetris.resetGame2p();
					tetris.requestFocus();
					setVisible(false);
				}
			}
		});
	}
}
