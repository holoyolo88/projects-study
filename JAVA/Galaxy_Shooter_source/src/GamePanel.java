import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;


import java.lang.Thread;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class GamePanel extends JPanel implements Runnable, KeyListener {

	public static int WIDTH = 800; // ����
	public static int HEIGHT = 600; // ����

	private Thread thread; // Thread ����
	private Thread threadnew;
	private boolean running; // ���� ���� �ľ��� ���� running ����

	private BufferedImage image; // �̹��� ��������
	private Graphics2D g; // �̹��� ������, 2D �׷����� �����ϱ� ���� ����

	private int FPS = 30; // ������ ������ ��ġ ����
	private int averageFPS; // ��� FPS ��ġ

	public static Player player; // Player Ŭ���� ���

	public static ArrayList<Bullet> bullets; // �Ѿ��� �迭�� ���� �ҷ�����
	public static ArrayList<Enemy> enemies; // ���� �迭�� ���� �ҷ�����
	public static ArrayList<Item> items; // �Ŀ� ���� �迭�� ���� �ҷ�����
	public static ArrayList<Explosion> explosions; // ������ �迭�� ���� �ҷ�����
	public static ArrayList<Text> texts; // �ؽ�Ʈ�� �迭�� ���� �ҷ�����

	private long waveStartTimer; // �������� ���̺� ǥ�� �ð�
	private long waveStartTimerDiff; // ���̺� ǥ�� �⺻ �ð�
	private int waveNumber; // �������� ���̺� ��
	private boolean waveStart; // ���̺� �۾� ǥ�� ��ŸƮ
	private int waveDelay = 2000; // ���̺� ǥ�� ������

	private long slowDownTimer; // �������� ������ Ÿ�̸�
	private long slowDownTimerDiff; // �������� ������ �⺻ �ð�
	private int slowDownLength = 6000; // �������� ������ �ð� ����

	private long pauseTimer; // �Ͻ����� �ð�
	private long pauseTimerDiff; // �Ͻ����� ��� �ð�
	private long pauseLength = 3000;

	// public boolean Pause; // �Ͻ����� ����

	private File startBGM = new File("sounds/startBGM.wav"); // �������
	private AudioStream startBGMAudio;
	private InputStream startBGMInput;
	private File bonusSound = new File("sounds/bonusSound.wav"); // ������ ȿ����
	private AudioStream bonusSoundAudio;
	private InputStream bonusSoundInput;
	private File gameoverSound = new File("sounds/gameoverSound.wav"); // ���ӿ��� ����
	private AudioStream gameoverSoundAudio;
	private InputStream gameoverSoundInput;

	private boolean retry;

	public GamePanel() {
		super(); // ���� Ŭ���� ����
		setPreferredSize(new Dimension(WIDTH, HEIGHT)); // �г��� ������ ����
		setFocusable(true); // �гο� ��Ŀ�� Ȱ��ȭ
		requestFocus(); // �гο� ��Ŀ���� ��û

	}

	public void addNotify() {
		super.addNotify(); // addNotify�� �������� �����ְ� �ϴ� ����
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}

		addKeyListener(this); // Ű �̺�Ʈ Ȱ��ȭ
	}

	public void run() {

		running = true; // ���� �� ��� running�� true�� Ȱ��ȭ

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB); // image�� ����, ����, RGBŸ�� ����
		g = (Graphics2D) image.getGraphics(); // g�� image ��ǥ ����ֱ�
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		player = new Player(); // Player Ŭ���� ��ü ����
		bullets = new ArrayList<Bullet>(); // �Ѿ� Ŭ���� ��ü ����
		enemies = new ArrayList<Enemy>(); // �� Ŭ���� ��ü ����
		items = new ArrayList<Item>(); // �Ŀ� �� Ŭ���� ��ü ����
		explosions = new ArrayList<Explosion>(); // ���� Ŭ���� ��ü ����
		texts = new ArrayList<Text>(); // �ؽ�Ʈ Ŭ���� ��ü ����

		waveStartTimer = 0;
		waveStartTimerDiff = 0;
		waveStart = true;
		waveNumber = 0;

		// �� FPS (Frame Per Second)�� ���ϱ� ���� ������
		long startTime;
		long URDTimeMillis;
		long waitTime;
		long totalTime = 0;
		long targetTime = 1000 / FPS;

		// �ּ� �����Ӱ� �ִ� ������ ǥ��
		int frameCount = 0;
		int maxFrameCount = 30;

		while (running) {
			startTime = System.nanoTime(); // FPS ����� ���� ���� ����

			// game�� ���õ� ������Ʈ, ����, �׸� �ҷ�����
			gameUpdate();
			gameRender();
			gameDraw();

			URDTimeMillis = (System.nanoTime() - startTime) / 1000000; // FPS ���
			waitTime = targetTime - URDTimeMillis; // ���ð�

			// ������� �ƴ� ����� ����ó��
			try {
				Thread.sleep(waitTime); // �۵� ����
			} catch (Exception e) {

			}

			totalTime += System.nanoTime() - startTime; // ���� �ð���
			frameCount++; // ������ ī���� ����
			if (frameCount == maxFrameCount) {
				averageFPS = (int) (1000 / ((totalTime / frameCount) / 1000000)); // ��� ������ ���
				frameCount = 0;
				totalTime = 0;
			}

		}
		if (waveNumber == 10)
		// ���� �����
		{
			g.setColor(new Color(0, 0, 0));
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Century Gothic", Font.PLAIN, 16));
			String s = "Y O U   W I N";
			AudioPlayer.player.start(gameoverSoundAudio);
			int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2);

			s = "Score : " + player.getScore();
			length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2 + 30);

			s = "Press R to R E T R Y";
			length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2 + 60);

			s = "Press ESC to E X I T";
			length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2 + 75);
			gameDraw();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (retry == true)

				run();
		} else {
			g.setColor(new Color(0, 0, 0));
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Century Gothic", Font.PLAIN, 16));
			String s = "G A M E   O V E R";
			AudioPlayer.player.start(gameoverSoundAudio);
			int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2);

			s = "Score : " + player.getScore();
			length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2 + 30);

			s = "Press R to R E T R Y";
			length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2 + 60);

			s = "Press ESC to E X I T";
			length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			g.drawString(s, (WIDTH - length) / 2, HEIGHT / 2 + 80);

			gameDraw();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (retry == true)
				run();

		}
	}

	// ���ӿ��� ��������� ��ȭ �Ǵ� �κ�
	private void gameUpdate() {
		player.setPause(player.freeze);
		player.setPpause(player.Ppause);
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).setEpause(enemies.get(i).Epause);
		}
		// ���ο� ���̺�
		if (waveStartTimer == 0 && enemies.size() == 0) {
			waveNumber++;
			waveStart = false;
			waveStartTimer = System.nanoTime();
		} else {
			waveStartTimerDiff = (System.nanoTime() - waveStartTimer) / 1000000;
			if (waveStartTimerDiff > waveDelay) {
				waveStart = true;
				waveStartTimer = 0;
				waveStartTimerDiff = 0;
			}
		}

		// �� ����
		if (waveStart && enemies.size() == 0) {
			createNewEnemies();
		}
		// �÷��̾� ������Ʈ
		player.update();

		// �Ѿ� ������Ʈ
		for (int i = 0; i < bullets.size(); i++) {
			boolean remove = bullets.get(i).update();
			if (remove) {
				bullets.remove(i);
				i--;
			}
		}

		// �� ������Ʈ
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).update();

		}

		// �Ŀ� �� ������Ʈ
		for (int i = 0; i < items.size(); i++) {
			boolean remove = items.get(i).update();
			if (remove) {
				items.remove(i);
				i--;

			}
		}

		// ���� ������Ʈ
		for (int i = 0; i < explosions.size(); i++) {
			boolean remove = explosions.get(i).update();
			if (remove) {
				explosions.remove(i);
				i--;
			}
		}

		// �ؽ�Ʈ ������Ʈ
		for (int i = 0; i < texts.size(); i++) {
			boolean remove = texts.get(i).update();
			if (remove) {
				texts.remove(i);
				i--;
			}
		}

		// �Ѿ˿� ���� ��
		for (int i = 0; i < bullets.size(); i++) {
			// �Ѿ��� ��ǥ
			Bullet b = bullets.get(i);
			double bx = b.getx();
			double by = b.gety();
			double br = b.getr();
			for (int j = 0; j < enemies.size(); j++) {
				// ���� ��ǥ
				Enemy e = enemies.get(j);
				double ex = e.getx();
				double ey = e.gety();
				double er = e.getr();

				double dx = bx - ex;
				double dy = by - ey;
				double dist = Math.sqrt(dx * dx + dy * dy);

				// ���� ��ǥ�� �Ѿ��� ��ǥ�� ��ġ�� ���
				if (dist < br + er) {
					e.hit();
					bullets.remove(i);
					i--;
					break;
				}

			}
		}

		// �� ���� üũ
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).isDead()) {
				Enemy e = enemies.get(i);

				// �Ŀ� ��
				double rand = Math.random(); // �������� ������ ���
				if (rand < 0.02)
					items.add(new Item(1, e.getx(), e.gety()));
				else if (rand < 0.04)
					items.add(new Item(5, e.getx(), e.gety())); // score-100 item 추가
				else if (rand < 0.07)
					items.add(new Item(6, e.getx(), e.gety())); // item 추가
				else if (rand < 0.1)
					items.add(new Item(4, e.getx(), e.gety())); // item 생성 확률 변경
				else if (rand < 0.2)
					items.add(new Item(3, e.getx(), e.gety())); // item 생성 확률 변경
				else if (rand < 0.3)
					items.add(new Item(2, e.getx(), e.gety())); // item 생성 확률 변경

				player.addScore(e.getType() * 50 + e.getRank() * 50);
				enemies.remove(i);
				i--;

				e.explode();
				explosions.add(new Explosion(e.getx(), e.gety(), e.getr(), e.getr() + 25, e.getType())); // 폭발 크기 변경
			}
		}

		// �÷��̾� ���� üũ
		if (player.isDead()) {
			running = false;
		}

		// �÷��̾� - �� �浹
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

				if (dist < pr + er) {
					player.lostLife();
				}

			}
		}

		// �÷��̾� �Ŀ� ��
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

			// �Ŀ� �� ȿ��
			if (dist < pr + r) {
				int type = p.getType();

				if (type == 1) {
					player.gainLife();
					texts.add(new Text(player.getx(), player.gety(), 2000, "Life+"));
					AudioPlayer.player.start(bonusSoundAudio);
				}
				if (type == 2) {
					player.increasePower(1);
					texts.add(new Text(player.getx(), player.gety(), 2000, "Power Up +1"));
					AudioPlayer.player.start(bonusSoundAudio);
				}
				if (type == 3) {
					player.increasePower(2);
					texts.add(new Text(player.getx(), player.gety(), 2000, "Power Up +2"));
					AudioPlayer.player.start(bonusSoundAudio);
				}
				if (type == 4) {
					slowDownTimer = System.nanoTime();
					for (int j = 0; j < enemies.size(); j++) {
						enemies.get(j).setSlow(true);
					}
					texts.add(new Text(player.getx(), player.gety(), 2000, "Slow Down"));
					AudioPlayer.player.start(bonusSoundAudio);
				}
				if (type == 5) {
					player.addScore(-100);
					texts.add(new Text(player.getx(), player.gety(), 2000, "score -100"));
					AudioPlayer.player.start(bonusSoundAudio);
				}
				if (type == 6) {
					pauseTimer = System.nanoTime();
					player.setPause(true);
					texts.add(new Text(player.getx(), player.gety(), 2000, "Player Pause"));
					AudioPlayer.player.start(bonusSoundAudio);

				}
				items.remove(i);
				i--;
			}
		}

		// �������� ������ ������Ʈ
		if (slowDownTimer != 0) {
			slowDownTimerDiff = (System.nanoTime() - slowDownTimer) / 1000000;
			if (slowDownTimerDiff > slowDownLength) {
				slowDownTimer = 0;
				for (int j = 0; j < enemies.size(); j++) {
					enemies.get(j).setSlow(false);
				}
			}
		}

		if (pauseTimer != 0) // pause item 구현
		{
			pauseTimerDiff = (System.nanoTime() - pauseTimer) / 1000000;
			if (pauseTimerDiff > pauseLength) {
				pauseTimer = 0;
				player.setPause(false);
			}
		}

		// BGM
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

	// ȭ�� �� ǥ��
	private void gameRender() {
		// ��� �̹��� ����
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.WHITE);

		// �������� ������ ȭ�� ����
		if (slowDownTimer != 0) {
			g.setColor(new Color(255, 255, 255, 64));
			g.fillRect(0, 0, WIDTH, HEIGHT);
		}
		// �÷��̾� ��ü �̹��� ����
		player.draw(g); // ;; -> ;

		// �Ѿ� �̹��� ����
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).draw(g);
		}

		// �� �̹��� ����
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}

		// �Ŀ� �� �̹��� ����
		for (int i = 0; i < items.size(); i++) {
			items.get(i).draw(g);
		}

		// ���� �̹��� ����
		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).draw(g);
		}

		// �ؽ�Ʈ ǥ��
		for (int i = 0; i < texts.size(); i++) {
			texts.get(i).draw(g);
		}

		// ���̺� ���� ǥ��
		if (waveStartTimer != 0) {
			g.setFont(new Font("Century Gothic", Font.PLAIN, 18));
			String s = " - W A V E   " + waveNumber + "   -";
			int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
			int alpha = (int) (255 * Math.sin(3.14 * waveStartTimerDiff / waveDelay));
			if (alpha > 255)
				alpha = 255;
			g.setColor(new Color(255, 255, 255, alpha));
			g.drawString(s, WIDTH / 2 - length / 2, HEIGHT / 2);
		}

		// �÷��̾� ��� ǥ��
		for (int i = 0; i < player.getLifes(); i++) {
			g.setColor(Color.PINK); // Life up item 과 색상 동일하게 변경
			g.fillOval(20 + (20 * i), 20, player.getr() * 2, player.getr() * 2);
		}

		// ���� ǥ��
		g.setColor(Color.WHITE);
		g.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		g.drawString("Use rudder to move", 18, 80);
		g.drawString("Press space bar to attack", 18, 95);
		g.drawString("'S' to pause", 18, 110);
		g.drawString("'A' to resume", 18, 125);

		// �÷��̾� �Ŀ� ǥ��

		g.setStroke(new BasicStroke(2));
		g.setColor(Color.YELLOW);
		g.fillRect(20, 40, player.getPower() * 8, 8);
		g.setColor(Color.YELLOW);
		g.setStroke(new BasicStroke(2));
		for (int i = 0; i < player.getRequiredPower(); i++) {
			g.drawRect(20 + 8 * i, 40, 8, 8);
		}

		// ���� ǥ��
		g.setColor(Color.WHITE);
		g.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		g.drawString("Score  : " + player.getScore(), WIDTH - 100, 30);
		g.drawString("Wave : " + waveNumber, WIDTH - 100, 50);

		// �������� ������ ǥ��
		if (slowDownTimer != 0) {
			g.setColor(Color.WHITE);
			g.drawRect(20, 55, 100, 8);
			g.fillRect(20, 55, (int) (100 - 100 * slowDownTimerDiff / slowDownLength), 8);
		}
	}

	// �̹��� �׸���
	private void gameDraw() {
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}

	// �� ����
	private void createNewEnemies() {
		enemies.clear();
		Enemy e;

		// 1ź
		if (waveNumber == 1) {
			AudioPlayer.player.start(startBGMAudio);
			for (int i = 0; i < 5; i++) {
				enemies.add(new Enemy(1, 1));
			}
		}
		// 2ź
		if (waveNumber == 2) {
			for (int i = 0; i < 10; i++) {
				enemies.add(new Enemy(1, 1));
			}
		}
		// 3ź
		if (waveNumber == 3) {
			for (int i = 0; i < 5; i++) {
				enemies.add(new Enemy(1, 1));
			}
			enemies.add(new Enemy(1, 2));
			enemies.add(new Enemy(1, 2));
		}
		// 4ź
		if (waveNumber == 4) {
			enemies.add(new Enemy(1, 3));
			enemies.add(new Enemy(1, 4));
			for (int i = 0; i < 5; i++) {
				enemies.add(new Enemy(2, 1));
			}
		}
		// 5ź
		if (waveNumber == 5) {
			enemies.add(new Enemy(1, 4));
			enemies.add(new Enemy(1, 3));
			enemies.add(new Enemy(2, 3));
		}
		// 6ź
		if (waveNumber == 6) {
			enemies.add(new Enemy(1, 3));
			for (int i = 0; i < 5; i++) {
				enemies.add(new Enemy(2, 1));
				enemies.add(new Enemy(3, 1));
			}
		}
		// 7ź
		if (waveNumber == 7) {
			enemies.add(new Enemy(1, 3));
			enemies.add(new Enemy(2, 3));
			enemies.add(new Enemy(3, 3));
		}
		// 8ź
		if (waveNumber == 8) {
			enemies.add(new Enemy(1, 4));
			enemies.add(new Enemy(2, 4));
			enemies.add(new Enemy(3, 4));
		}
		// 9ź
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

	// Ű �Է� �̺�Ʈ
	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		// �� �� �� �� ��ü ������ �Է½�
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
		// ���ݹ�ư (�����̽���)�� ������ ��
		if (keyCode == KeyEvent.VK_SPACE) {
			player.setFiring(true);
		}
		// ESC ��ư ������ �� ���� ����
		if (keyCode == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
		if (keyCode == KeyEvent.VK_R) {
			retry = true;
		}
		if (keyCode == KeyEvent.VK_S) {
			for (int i = 0; i < enemies.size(); i++) {
				enemies.get(i).setEpause(true);
			}
			Player.setPpause(true);
		}
		if (keyCode == KeyEvent.VK_A) {
			Player.setPpause(false);
			for (int i = 0; i < enemies.size(); i++) {
				enemies.get(i).setEpause(false);
			}
		}

	}

	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		// �� �� �� �� ��ü ������ �Է� �����
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
		// ���ݹ�ư (�����̽���)�� �׸� ������ ��
		if (keyCode == KeyEvent.VK_SPACE) {
			player.setFiring(false);
		}
		if (keyCode == KeyEvent.VK_R) {
			retry = false;
		}
	}

}
