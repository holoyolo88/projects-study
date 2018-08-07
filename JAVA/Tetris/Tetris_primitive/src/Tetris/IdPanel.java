package Tetris;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class IdPanel extends JFrame {
	private static final long serialVersionUID = -4864654165165465455L;

	/**
	 * id 판넬 생성.
	 * 
	 * @param lv
	 *            : 전달받은 현재 레벨.
	 * @param score
	 *            : 전달받은 현재 점수.
	 */
	IdPanel(final int lv, final int score) {
		setTitle("Plz Enter your ID(1P)");
		JLabel lb = new JLabel("ID : ", Label.RIGHT);
		JTextField tf = new JTextField(15);
		JButton jb = new JButton("enter");
		// 버튼 이벤트 발생.
		jb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tf.getText().equals("")) {
					tf.setText("user");
				}
				String str = tf.getText();
				//스코어 판넬 생성 및 인수 전달.
				ScorePanel scorepanel = new ScorePanel(str, lv, score);
				scorepanel.setVisible(true);
				dispose();
			}
		});
		add(lb);
		add(tf);
		add(jb);
		// 판넬의 설정
		this.getContentPane().setBackground(Color.WHITE);
		setBounds(110, 200, 500, 80);
		setLayout(new FlowLayout());
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		this.pack();
	}
}