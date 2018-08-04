import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JPanel;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class GamePanel extends JPanel implements Runnable, KeyListener {

	// 패널 넓이
	public static int WIDTH = 800;
	public static int HEIGHT = 600;

	// Thread 클래스 참조 변수
	private Thread thread;

	// 게임 실행 여부
	private boolean running;

	// BufferedImage 클래스 참조 변수
	private BufferedImage image;
	// Graphics2D 클래스 참조 변수
	private Graphics2D g;

	// 게임 프레임 수와 평균 프레임 수
	private int FPS = 30;
	private int averageFPS;

	// Player 클래스 참조 변수
	public static Player player;

	// 제네릭 타입 배열
	public static ArrayList<Bullet> bullets;
	public static ArrayList<Enemy> enemies;
	public static ArrayList<Item> items;
	public static ArrayList<Explosion> explosions;
	public static ArrayList<Text> texts;

	// wave text 시작 시각
	private long waveStartTimer;
	// wave 시작 경과 시간
	private long susWaveTime;
	// waveNumber
	private int waveNumber;
	// wave text 시작 여부
	private boolean waveStart;
	// wave text 유지 시간
	private int susWaveTextTime = 2000;

	// slow down item 구현을 위한 변수
	// slow down item 적용 시작 시각
	private long slowDownTimer;
	// slow down item 시작 경과 시간
	private long susSlowDownTime;
	// slow down item 적용 시간
	private int slowDownLength = 6000;

	// player freeze item 구현을 위한 변수
	// player freeze item 적용 시작 시각
	private long freezeTimer;
	// player freeze item 시작 경과 시간
	private long susFreezeTime;
	// player freeze item 적용 시간
	private long freezeLength = 3000;

	// 게임 재시작 여부
	private boolean retry;

	// 배경 음악 설정
	private File startBGM = new File("sounds/startBGM.wav");
	private AudioStream startBGMAudio;
	private InputStream startBGMInput;
	// item 획득 효과음 설정
	private File bonusSound = new File("sounds/bonusSound.wav");
	private AudioStream bonusSoundAudio;
	private InputStream bonusSoundInput;
	// 게임 오버 배경 음악
	private File gameoverSound = new File("sounds/gameoverSound.wav");
	private AudioStream gameoverSoundAudio;
	private InputStream gameoverSoundInput;

	// 점수 기록 파일 입출력
	private File file = new File("ScoreBoard.txt");
	private FileReader fileReader;
	private FileWriter fileWriter;

	// 점수 보드
	private int[] scoreBoard;

	// GamePanel 클래스 생성자
	public GamePanel() {

		// 부모 클래스인 JPanel 클래스 생성자 호출
		super();
		// 패널 크기 설정
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		// 패널 포커스 활성화
		setFocusable(true);
		// 패널에 포커스 요청
		requestFocus();
	}

	public void addNotify() {
		// 프레임 보이기
		super.addNotify();
		// 스레드 생성 후 시작
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
		// 키 이벤트 활성화
		addKeyListener(this);
	}

	public void run() {

		// 게임 실행 여부 참으로 설정
		running = true;

		// 점수 보드
		scoreBoard = new int[6];

		// 점수 기록 파일 스트림 연결
		try {
			fileReader = new FileReader(file);

			char[] buffer = new char[100];
			fileReader.read(buffer);

			// 점수 문자열 생성
			String scoreString = "";
			for (char ch : buffer) {
				scoreString += ch;
			}
			System.out.print(scoreString);

			// 구분자를 이용해 점수 보드에 점수 설정
			String[] stringScoreBoard = scoreString.split("\\|");
			System.out.println(stringScoreBoard.length);
			for (int i = 0; i < 6; i++) {
				scoreBoard[i] = Integer.parseInt(stringScoreBoard[i]);
			}
		} catch (IOException e) {
		}
		// bufferedimage의 폭, 높이, RGB 타입 설정
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		// graphics2d에 bufferedimage의 좌표 설정
		g = (Graphics2D) image.getGraphics();
		// graphics2d 외곽선 처리 안티알리어싱 설정
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// Player 클래스 객체 생성
		player = new Player();
		// Bullet 클래스 객체 생성
		bullets = new ArrayList<Bullet>();
		// Enemy 클래스 객체 생성
		enemies = new ArrayList<Enemy>();
		// Item 클래스 객체 생성
		items = new ArrayList<Item>();
		// Explosion 클래스 객체 생성
		explosions = new ArrayList<Explosion>();
		// Text 클래스 객체 생성
		texts = new ArrayList<Text>();

		waveStartTimer = 0;
		susWaveTime = 0;
		waveStart = true;
		waveNumber = 0;

		// 평균 FPS 계산을 위한 변수
		long startTime;
		long URDTimeMillis;
		long waitTime;
		long totalTime = 0;
		long targetTime = 1000 / FPS;

		// 최소 프레임과 최대 프레임
		int frameCount = 0;
		int maxFrameCount = 30;

		while (running) {

			startTime = System.nanoTime();

			// 게임 정보 업데이트
			gameUpdate();
			// 게임 렌더
			gameRender();
			// 게임 그래픽 구현
			gameDraw();

			// 대기 시간 계산
			URDTimeMillis = (System.nanoTime() - startTime) / 1000000;
			waitTime = targetTime - URDTimeMillis;

			try {
				// 프레임 수에 맞춰 게임 화면을 보이기 위해 대기 시간만큼 스레드 일시 정지
				Thread.sleep(waitTime);
			} catch (Exception e) {
			}

			totalTime += System.nanoTime() - startTime;
			// 프레임 카운터 증가
			frameCount++;
			// 프레임 카운터가 최대 프레임 수인 경우 평균 프레임 계산
			if (frameCount == maxFrameCount) {
				averageFPS = (int) (1000 / ((totalTime / frameCount) / 1000000));
				frameCount = 0;
				totalTime = 0;
			}
		}

		// 점수 보드 업데이트
		scoreBoardUpdate();

		// waveNumber가 10인 경우
		if (waveNumber == 10) {
			g.setColor(new Color(0, 0, 0));
			g.fillRect(0, 0, WIDTH, HEIGHT);

			String s = "Y O U   W I N";
			g.setColor(Color.WHITE);
			g.setFont(new Font("Century Gothic", Font.PLAIN, 16));
			int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2);
			AudioPlayer.player.start(gameoverSoundAudio);

			s = "Score : " + player.getScore();
			length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2 + 30);

			s = "Press R to R E T R Y";
			length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2 + 60);

			s = "Press ESC to E X I T";
			length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2 + 75);

			try {
				fileWriter = new FileWriter(file);
				fileWriter.write(Integer.toString(scoreBoard[0]) + "|");
				for (int i = 1; i < 6; i++) {
					s = i + " place " + scoreBoard[6 - i];
					length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
					g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2 + 80 + i * 18);
					fileWriter.write(Integer.toString(scoreBoard[i]) + "|");
				}

				fileWriter.close();
				fileReader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			gameDraw();

			// 재시작
			while (true) {
				System.out.println(retry);
				if (retry == true)
					break;
			}
			run();
		}
		// waveNumber가 10이 아닌 경우
		else {

			g.setColor(new Color(0, 0, 0));
			g.fillRect(0, 0, WIDTH, HEIGHT);

			String s = "G A M E   O V E R";
			g.setColor(Color.WHITE);
			g.setFont(new Font("Century Gothic", Font.PLAIN, 16));
			int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2);
			AudioPlayer.player.start(gameoverSoundAudio);

			s = "Score : " + player.getScore();
			length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2 + 30);

			s = "Press R to R E T R Y";
			length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2 + 60);

			s = "Press ESC to E X I T";
			length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2 + 80);
			try {
				fileWriter = new FileWriter(file);
				fileWriter.write(Integer.toString(scoreBoard[0]) + "|");
				for (int i = 1; i < 6; i++) {
					s = i + " place " + scoreBoard[6 - i];
					length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
					g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2 + 80 + i * 18 + 10);
					fileWriter.write(Integer.toString(scoreBoard[i]) + "|");
				}

				fileWriter.close();
				fileReader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			gameDraw();

			// 재시작
			while (true) {
				System.out.println(retry);
				if (retry == true)
					break;
			}
			run();
		}
	}

	// 게임 정보 업데이트
	// 상시적으로 변화
	private void gameUpdate() {

		// player freeze item 적용 여부 설정
		player.setFreeze(player.freeze);
		// player 일시 정지 상태 여부 설정
		player.setPpause(player.Ppause);
		// enemy 일시 정지 상태 여부 설정
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).setEpause(enemies.get(i).Epause);
		}

		// wave 시작 시각이 0이고 enemy 수가 0인 경우 wave 정보 설정
		if (waveStartTimer == 0 && enemies.size() == 0) {
			waveNumber++;
			waveStart = false;
			waveStartTimer = System.nanoTime();
		}
		// wave 시작 시각이 0이 아니거나 enemy 수가 0이 아닌 경우
		else {
			susWaveTime = (System.nanoTime() - waveStartTimer) / 1000000;
			if (susWaveTime > susWaveTextTime) {
				waveStart = true;
				waveStartTimer = 0;
				susWaveTime = 0;
			}
		}

		// wave가 시작되고 enemy가 생성되지 않은 경우
		if (waveStart && enemies.size() == 0) {
			createNewEnemies();
		}
		// player 정보 업데이트
		player.update();

		// bullet 정보 업데이트
		for (int i = 0; i < bullets.size(); i++) {
			boolean remove = bullets.get(i).update();
			if (remove) {
				bullets.remove(i);
				i--;
			}
		}

		// enemy 정보 업데이트
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).update();
		}

		// item 정보 업데이트
		for (int i = 0; i < items.size(); i++) {
			boolean remove = items.get(i).update();
			if (remove) {
				items.remove(i);
				i--;
			}
		}

		// explosions 정보 업데이트
		for (int i = 0; i < explosions.size(); i++) {
			boolean remove = explosions.get(i).update();
			if (remove) {
				explosions.remove(i);
				i--;
			}
		}

		// text 정보 업데이트
		for (int i = 0; i < texts.size(); i++) {
			boolean remove = texts.get(i).update();
			if (remove) {
				texts.remove(i);
				i--;
			}
		}

		// bullet으로부터 타격받은 enemy 처리
		for (int i = 0; i < bullets.size(); i++) {

			Bullet b = bullets.get(i);
			double bx = b.getx();
			double by = b.gety();
			double br = b.getr();
			for (int j = 0; j < enemies.size(); j++) {

				Enemy e = enemies.get(j);
				double ex = e.getx();
				double ey = e.gety();
				double er = e.getr();

				double dx = bx - ex;
				double dy = by - ey;
				double dist = Math.sqrt(dx * dx + dy * dy);

				// bullet과 enemy 좌표 사이의 거리가 bullet과 enemy의 반지름의 길이보다 작은 경우
				if (dist < br + er) {
					e.hit();
					bullets.remove(i);
					i--;
					break;
				}
			}
		}

		// enemy가 사망한 경우 item 드롭
		for (int i = 0; i < enemies.size(); i++) {

			if (enemies.get(i).isDead()) {
				Enemy e = enemies.get(i);

				// item 드롭 확률을 난수로 설정
				double rand = Math.random();
				if (rand < 0.07)
					// item 유형 1 - life up
					items.add(new Item(1, e.getx(), e.gety()));
				else if (rand < 0.12)
					// item 유형 5 - score -100
					items.add(new Item(5, e.getx(), e.gety()));
				else if (rand < 0.17)
					// item 유형 6 - player freeze
					items.add(new Item(6, e.getx(), e.gety()));
				else if (rand < 0.25)
					// item 유형 4 - slow down
					items.add(new Item(4, e.getx(), e.gety()));
				else if (rand < 0.4)
					// item 유형 3 - power up+2
					items.add(new Item(3, e.getx(), e.gety()));
				else if (rand < 0.6)
					// item 유형 2 - power up+1
					items.add(new Item(2, e.getx(), e.gety()));

				// enemy 유형과 랭크에 비례해서 player 점수 증가
				player.addScore(e.getType() * 50 + e.getRank() * 50);
				// enemy 제거
				enemies.remove(i);
				i--;
				// 랭크가 낮은 새로운 enemy 생성
				e.explode();
				// enemy 폭발 효과 구현
				explosions.add(new Explosion(e.getx(), e.gety(), e.getr(), e.getr() + 25, e.getType()));
			}
		}

		// player 사망 여부 확인 후 게임 실행 여부 설정
		if (player.isDead()) {
			running = false;
		}

		// player가 회복 상태가 아닌 경우
		if (!player.isRecovering()) {

			int px = player.getx();
			int py = player.gety();
			int pr = player.getr();

			for (int i = 0; i < enemies.size(); i++) {

				Enemy e = enemies.get(i);

				double ex = e.getx();
				double ey = e.gety();
				double er = e.getr();

				double dx = px - ex;
				double dy = py - ey;
				double dist = Math.sqrt(dx * dx + dy * dy);

				// player와 enemy 좌표 사이의 거리가 player와 enemy의 반지름의 합보다 작은 경우
				// 충돌한 경우 life 수 감소
				if (dist < pr + er)
					player.lostLife();
			}
		}

		int px = player.getx();
		int py = player.gety();
		int pr = player.getr();

		for (int i = 0; i < items.size(); i++) {
			Item p = items.get(i);
			double x = p.getx();
			double y = p.gety();
			double r = p.getr();
			double dx = px - x;
			double dy = py - y;
			double dist = Math.sqrt(dx * dx + dy * dy);

			// player와 item 사이의 거리가 player와 enemy의 반지름의 합보다 작은 경우
			if (dist < pr + r) {
				int type = p.getType();
				// item 유형 1 - life up
				if (type == 1) {
					player.gainLife();
					texts.add(new Text(player.getx(), player.gety(), 2000, "Life+"));
					AudioPlayer.player.start(bonusSoundAudio);
				}
				// item 유형 2 - power up+1
				else if (type == 2) {
					player.increasePower(1);
					texts.add(new Text(player.getx(), player.gety(), 2000, "Power Up +1"));
					AudioPlayer.player.start(bonusSoundAudio);
				}
				// item 유형 3 - power up+2
				else if (type == 3) {
					player.increasePower(2);
					texts.add(new Text(player.getx(), player.gety(), 2000, "Power Up +2"));
					AudioPlayer.player.start(bonusSoundAudio);
				}
				// item 유형 4 - slow down
				else if (type == 4) {
					slowDownTimer = System.nanoTime();
					for (int j = 0; j < enemies.size(); j++)
						enemies.get(j).setSlow(true);
					texts.add(new Text(player.getx(), player.gety(), 2000, "Slow Down"));
					AudioPlayer.player.start(bonusSoundAudio);
				}
				// item 유형 5 - score-100
				else if (type == 5) {
					player.addScore(-100);
					texts.add(new Text(player.getx(), player.gety(), 2000, "score -100"));
					AudioPlayer.player.start(bonusSoundAudio);
				}
				// item 유형 6 - player freeze
				else if (type == 6) {
					freezeTimer = System.nanoTime();
					player.setFreeze(true);
					texts.add(new Text(player.getx(), player.gety(), 2000, "Player freeze"));
					AudioPlayer.player.start(bonusSoundAudio);
				}
				items.remove(i);
				i--;
			}
		}

		// slow down item 업데이트
		if (slowDownTimer != 0) {
			susSlowDownTime = (System.nanoTime() - slowDownTimer) / 1000000;
			if (susSlowDownTime > slowDownLength) {
				slowDownTimer = 0;
				for (int j = 0; j < enemies.size(); j++) {
					enemies.get(j).setSlow(false);
				}
			}
		}

		// player freeze item 업데이트
		if (freezeTimer != 0) {
			susFreezeTime = (System.nanoTime() - freezeTimer) / 1000000;
			if (susFreezeTime > freezeLength) {
				freezeTimer = 0;
				player.setFreeze(false);
			}
		}

		// 게임 배경음악 설정
		try {
			startBGMInput = new FileInputStream(startBGM);
			startBGMAudio = new AudioStream(startBGMInput);
			bonusSoundInput = new FileInputStream(bonusSound);
			bonusSoundAudio = new AudioStream(bonusSoundInput);
			gameoverSoundInput = new FileInputStream(gameoverSound);
			gameoverSoundAudio = new AudioStream(gameoverSoundInput);
		} catch (IOException e) {
		}
	}

	// game 그래픽 구현
	private void gameRender() {
		// game 배경 그래픽 구현
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.WHITE);

		// slow down item 적용할 경우
		if (slowDownTimer != 0) {
			g.setColor(new Color(255, 255, 255, 64));
			g.fillRect(0, 0, WIDTH, HEIGHT);
		}

		// player 그래픽 구현
		player.draw(g);

		// bullet 그래픽 구현
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).draw(g);
		}

		// enemy 그래픽 구현
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}

		// item 그래픽 구현
		for (int i = 0; i < items.size(); i++) {
			items.get(i).draw(g);
		}

		// explosion 그래픽 구현
		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).draw(g);
		}

		// text 그래픽 구현
		for (int i = 0; i < texts.size(); i++) {
			texts.get(i).draw(g);
		}

		// waveNumber 그래픽 구현
		if (waveStartTimer != 0) {

			String s;
			if (waveNumber < 9)
				s = " -  W A V E   " + waveNumber + "   - ";
			else
				s = " -  F I N A L   W A V E - ";
			int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			int alpha = (int) (255 * Math.sin(3.14 * susWaveTime / susWaveTextTime));
			if (alpha > 255)
				alpha = 255;
			g.setColor(new Color(255, 255, 255, alpha));
			g.setFont(new Font("Century Gothic", Font.PLAIN, 18));
			g.drawString(s, WIDTH / 2 - (length) / 2 - 25, HEIGHT / 2);
		}

		// player life 그래픽 구현
		for (int i = 0; i < player.getLifes(); i++) {
			g.setColor(Color.PINK);
			g.fillOval(20 + (20 * i), 20, player.getr() * 2, player.getr() * 2);
		}

		// 게임 방법 그래픽 구현
		g.setColor(Color.WHITE);
		g.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		g.drawString("Use rudder to move", 18, 80);
		g.drawString("Press space bar to attack", 18, 95);
		g.drawString("'S' to freeze", 18, 110);
		g.drawString("'A' to resume", 18, 125);

		// 파워 정보 그래픽 구현
		g.setColor(Color.YELLOW);
		g.fillRect(20, 40, player.getPower() * 8, 8);

		g.setColor(Color.YELLOW);
		g.setStroke(new BasicStroke(2));
		for (int i = 0; i < player.getRequiredPower(); i++) {
			g.drawRect(20 + 8 * i, 40, 8, 8);
		}

		// 점수와 wave 정보 그래픽 구현
		g.setColor(Color.WHITE);
		g.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		g.drawString("Score  : " + player.getScore(), WIDTH - 100, 30);
		g.drawString("Wave : " + waveNumber, WIDTH - 100, 50);

		// slow down item 적용할 경우
		if (slowDownTimer != 0) {
			g.setColor(Color.WHITE);
			g.drawRect(20, 55, 100, 8);
			g.fillRect(20, 55, (int) (100 - 100 * susSlowDownTime / slowDownLength), 8);
		}
	}

	// 게임 그래픽 구현
	private void gameDraw() {
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}

	// enemy 생성
	private void createNewEnemies() {
		enemies.clear();
		Enemy e;

		// wave 1
		if (waveNumber == 1) {
			AudioPlayer.player.start(startBGMAudio);
			for (int i = 0; i < 5; i++) {
				enemies.add(new Enemy(1, 1));
			}
		}
		// wave 2
		if (waveNumber == 2) {
			for (int i = 0; i < 10; i++) {
				enemies.add(new Enemy(1, 1));
			}
		}
		// wave 3
		if (waveNumber == 3) {
			for (int i = 0; i < 5; i++) {
				enemies.add(new Enemy(1, 1));
			}
			enemies.add(new Enemy(1, 2));
			enemies.add(new Enemy(1, 2));
		}
		// wave 4
		if (waveNumber == 4) {
			enemies.add(new Enemy(1, 3));
			enemies.add(new Enemy(1, 4));
			for (int i = 0; i < 5; i++) {
				enemies.add(new Enemy(2, 1));
			}
		}
		// wave 5
		if (waveNumber == 5) {
			enemies.add(new Enemy(1, 4));
			enemies.add(new Enemy(1, 3));
			enemies.add(new Enemy(2, 3));
		}
		// wave 6
		if (waveNumber == 6) {
			enemies.add(new Enemy(1, 3));
			for (int i = 0; i < 5; i++) {
				enemies.add(new Enemy(2, 1));
				enemies.add(new Enemy(3, 1));
			}
		}
		// wave 7
		if (waveNumber == 7) {
			enemies.add(new Enemy(1, 3));
			enemies.add(new Enemy(2, 3));
			enemies.add(new Enemy(3, 3));
		}
		// wave 8
		if (waveNumber == 8) {
			enemies.add(new Enemy(1, 4));
			enemies.add(new Enemy(2, 4));
			enemies.add(new Enemy(3, 4));
		}
		// final wave
		if (waveNumber == 9) {
			for (int i = 0; i < 10; i++) {
				enemies.add(new Enemy(1, 1));
				enemies.add(new Enemy(2, 1));
				enemies.add(new Enemy(3, 1));
			}
		}
		if (waveNumber == 10) {
			running = false;
		}
	}

	public void scoreBoardUpdate() {
		// 점수 보드의 점수가 player 점수와 같은 경우 반환
		for (int score : scoreBoard) {
			if (score == player.getScore())
				return;
		}
		// 점수 보드 0번을 player 점수로 설정
		scoreBoard[0] = player.getScore();
		// 점수 보드 점수 오름차순 정렬
		Arrays.sort(scoreBoard);
	}

	// 키 입력 이벤트
	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		// 방향 키가 눌린 경우 player 이동 방향을 참으로 설정
		if (keyCode == KeyEvent.VK_LEFT) {
			player.setLeft(true);
		}
		if (keyCode == KeyEvent.VK_RIGHT) {
			player.setRight(true);
		}
		if (keyCode == KeyEvent.VK_UP) {
			player.setUp(true);
		}
		if (keyCode == KeyEvent.VK_DOWN) {
			player.setDown(true);
		}
		// space bar가 눌린 경우 player bullet 발사 여부를 참으로 설정
		if (keyCode == KeyEvent.VK_SPACE) {
			player.setFiring(true);
		}
		// ESC 키가 눌린 경우 종료
		if (keyCode == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
		// s키가 눌린 경우 enemy와 player 일시 정지 여부를 참으로 설정
		if (keyCode == KeyEvent.VK_S) {
			for (int i = 0; i < enemies.size(); i++) {
				enemies.get(i).setEpause(true);
			}
			Player.setPpause(true);
		}
		// a키가 눌린 경우 enemy와 player 일시 정지 여부를 거짓으로 설정
		if (keyCode == KeyEvent.VK_A) {
			Player.setPpause(false);
			for (int i = 0; i < enemies.size(); i++) {
				enemies.get(i).setEpause(false);
			}
		}
		// r키카 눌린 경우 재시작 여부를 참으로 설정
		if (keyCode == KeyEvent.VK_R) {
			retry = true;
		}
	}

	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		// 방향 키 눌림이 끝난 경우 player 이동 방향을 거짓으로 설정
		if (keyCode == KeyEvent.VK_LEFT) {
			player.setLeft(false);
		}
		if (keyCode == KeyEvent.VK_RIGHT) {
			player.setRight(false);
		}
		if (keyCode == KeyEvent.VK_UP) {
			player.setUp(false);
		}
		if (keyCode == KeyEvent.VK_DOWN) {
			player.setDown(false);
		}
		// space bar키 눌림이 끝난 경우 player bullet 발사 여부를 거짓으로 설정
		if (keyCode == KeyEvent.VK_SPACE) {
			player.setFiring(false);
		}
		// r 키 눌림이 끝난 경우 재시작 여부를 거짓으로 설정
		if (keyCode == KeyEvent.VK_R) {
			retry = false;
		}
	}

}