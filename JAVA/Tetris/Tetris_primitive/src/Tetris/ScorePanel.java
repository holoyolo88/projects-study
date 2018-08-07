package Tetris;

import java.awt.Color;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ScorePanel extends JFrame {
	private static final long serialVersionUID = -2157812315456452312L;
	/**
	 * 파일 입출력을 위한 스캐너 함수
	 */
	private Scanner file;
	/**
	 * 랭킹 정보가 들어가는 배열
	 */
	private JLabel j[] = new JLabel[12];
	static int Max_level;

	/**
	 * id 판넬 생성.
	 * 
	 * @param name
	 *            : 전달받은 닉네임.
	 * @param lv
	 *            : 전달받은 현재 레벨.
	 * @param sc
	 *            : 전달받은 현재 점수.
	 */
	ScorePanel(String name, int lv, int sc) {
		super("Rankings");
		//rank 리스트 생성.
		ArrayList<UserList> rank = new ArrayList<UserList>();
		String id = name;
		int length = 0;
		// 파일이 없을 시 생성.
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter("score.txt", true));

		} catch (IOException e) {
			e.printStackTrace();
		}
		pw.append(id + " " + lv + " " + sc);
		pw.println();
		pw.flush();
		// 파일에 새로운 랭킹 정보 입력
		try {
			file = new Scanner(new File("score.txt"));
			String line;
			while (file.hasNext()) {
				line = file.nextLine();
				Scanner lineScan = new Scanner(line);
				String id1 = lineScan.next();
				int num = lineScan.nextInt();
				int num2 = lineScan.nextInt();
				rank.add(new UserList(id1, num, num2));
				length++;
				lineScan.close();
			}
		} catch (Exception ex) {
			System.out.println("파일을 여는데 문제가 생겼습니다");
		}
		for (int i = 0; i < length; i++) {
			for (int j = 0; i < length - j; j++) {
				Collections.sort(rank);
				Max_level = rank.get(0).getScore();
			}
		}
		// 출력 내용.
		j[0] = new JLabel("ID : " + name + ",   MY level : " + lv + ",   MY SCORE : " + sc);
		j[1] = new JLabel("-------------------------------------------");
		if (j[2] == null) {
			for (int i = 0; i < 10; i++) {
				j[i + 2] = new JLabel((i + 1) + "등!  ID : " + "null" + ", level : " + "0" + ", SCORE : " + "0");
			}
		}
		for (int i = 0; i < 10; i++) {
			if (length <= i) {
				break;
			} else if (i == 0) {
				j[i + 2] = new JLabel((i + 1) + "등!  ID : " + rank.get(i).getID() + ", level : " + rank.get(i).getlv()
						+ ", SCORE : " + rank.get(i).getScore());
				j[i + 2].setForeground(Color.red);
			} else if (i == 1) {
				j[i + 2] = new JLabel((i + 1) + "등!  ID : " + rank.get(i).getID() + ", level : " + rank.get(i).getlv()
						+ ", SCORE : " + rank.get(i).getScore());
				j[i + 2].setForeground(Color.blue);
			} else if (i == 2) {
				j[i + 2] = new JLabel((i + 1) + "등!  ID : " + rank.get(i).getID() + ", level : " + rank.get(i).getlv()
						+ ", SCORE : " + rank.get(i).getScore());
				j[i + 2].setForeground(Color.green);
			} else {
				j[i + 2] = new JLabel((i + 1) + "등!  ID : " + rank.get(i).getID() + ", level : " + rank.get(i).getlv()
						+ ", SCORE : " + rank.get(i).getScore());
			}
		}
		for (int i = 0; i < 12; i++) {
			add(j[i]);
		}
		// 스코어판넬의 설정.
		getContentPane().setBackground(Color.WHITE);
		setResizable(false);
		setBounds(50, 50, 250, 315);
		setLayout(new FlowLayout());
		setLocationRelativeTo(null);
		setVisible(true);
	}
}