import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Player {

	// player 좌표
	private int x;
	private int y;

	// player 반지름
	private int r;

	// player 이동 거리
	private int dx;
	private int dy;

	// player 속력
	private int speed;

	// player 이동 방향
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;

	// player bullet 발사 여부
	private boolean firing;

	// player bullet 발사 시작 시각
	private long firingTimer;

	// player bullet 발사 지연 시간
	private long firingDelay;

	// enemy와 충돌 후 회복 상태 여부
	private boolean recovering;

	// enemy와 충돌 후 회복 상태 시작 시각
	private long recoveryTimer;

	// player life 수
	private int life;

	// player 색상
	private Color pcolor_default;
	private Color pcolor_recovering;

	// player 점수
	private int score;

	// player 파워
	private int power;

	// player 파워 레벨
	private int powerLevel = 0;

	// player 파워 레벨 업을 위해 요구되는 파워 수
	private int[] requiredPower = { 5, 10, 15 };

	// player bullet 발사 효과음
	private File bulletSound = new File("sounds/bulletSound.wav");
	private AudioStream bulletSoundAudio;
	private InputStream bulletSoundInput;

	// player freeze 아이템 사용 여부
	public boolean freeze;

	// player 일시 정지 상태 여부
	public static boolean Ppause;

	// Player 클래스 생성자
	public Player() {

		x = GamePanel.WIDTH / 2;

		// "W A V E" text 가림 방지를 위한 y 좌표 설정
		y = GamePanel.HEIGHT / 2 + 20;
		r = 5;

		dx = 0;
		dy = 0;
		speed = 10;

		life = 3;

		pcolor_default = Color.WHITE;
		pcolor_recovering = Color.RED;

		firing = false;
		firingTimer = System.nanoTime();
		firingDelay = 250;

		recovering = false;
		recoveryTimer = 0;

		score = 0;

		freeze = false;
		Ppause = false;
	}

	public int getx() {
		return x;
	}

	public int gety() {
		return y;
	}

	public int getr() {
		return r;
	}

	public int getScore() {
		return score;
	}

	public int getLifes() {
		return life;
	}

	public boolean isDead() {
		return life <= 0;
	}

	public boolean isRecovering() {
		return recovering;
	}

	public void setLeft(boolean b) {
		left = b;
	}

	public void setRight(boolean b) {
		right = b;
	}

	public void setUp(boolean b) {
		up = b;
	}

	public void setDown(boolean b) {
		down = b;
	}

	public void setFiring(boolean b) {
		firing = b;
	}

	public void addScore(int i) {
		score += i;
	}

	// life up item 획득 시 실행
	public void gainLife() {

		// player life 수가 10 미만일 경우 증가
		if (life < 10)
			life++;
	}

	// enemy와 충돌 시 실행
	public void lostLife() {

		life--;
		recovering = true;
		recoveryTimer = System.nanoTime();
	}

	public int getPower() {
		return power;
	}

	public int getPowerLevel() {
		return powerLevel;
	}

	public int getRequiredPower() {

		// player 파워 레벨이 최대 파워 레벨인 3일 경우
		if (powerLevel == 3)
			// 파워 레벨 업을 위해 요구되는 파워 수는 파워 레벨이 2일 때 요구되는 파워 수를 반환
			return requiredPower[powerLevel - 1];
		// player 파워 레벨에서 파워 레벨 업을 위해 요구되는 파워 수 반환
		else
			return requiredPower[powerLevel];
	}

	// power up+1 또는 power up+2 아이템 획득 시 실행
	public void increasePower(int i) {

		power += i;

		// 파워 레벨이 최대 파워 레벨인 3일 경우
		if (powerLevel == 3) {
			// 파워를 파워 레벨이 2일 때 요구되는 파워 수로 설정
			if (power > getRequiredPower())
				power = getRequiredPower();
			return;
		}
		// 파워가 파워 레벨 업을 위해 요구되는 파워 수보다 클 경우
		if (power >= getRequiredPower()) {
			// 파워를 파워 레벨 업을 위해 요구되는 파워 수만큼 감소한 후 파워 레벨 증가
			power -= getRequiredPower();
			powerLevel++;
		}
	}

	public void setFreeze(boolean b) {
		freeze = b;
	}

	public static void setPpause(boolean b) {
		Ppause = b;
	}

	// player 좌표 정보 업데이트
	public void update() {

		// player freeze 아이템 사용 여부와 일시 정지 상태 여부가 모두 거짓일 경우 이동할 거리 설정
		if (freeze != true && Ppause != true) {

			if (left)
				dx = -speed;
			if (right)
				dx = speed;
			if (up)
				dy = -speed;
			if (down)
				dy = speed;

			x += dx;
			y += dy;
		}

		// 이동할 좌표가 panel 범위를 벗어난 경우 이동할 거리 재설정
		if (x < r)
			x = r;
		if (y < r)
			y = r;
		if (x > GamePanel.WIDTH - r)
			x = GamePanel.WIDTH - r;
		if (y > GamePanel.HEIGHT - r)
			y = GamePanel.HEIGHT - r;

		// player가 방향키를 이용해 이동하지 않았을 경우 이동할 거리 설정
		dx = 0;
		dy = 0;

		// player가 bullet을 발사하고 player freeze 아이템을 사용하지 않으며 일시 정지 상태가 아닐 경우
		if (firing && freeze != true && Ppause != true) {

			long elapsed = (System.nanoTime() - firingTimer) / 1000000;
			// player bullet 발사 후 경과 시간이 bullet 발사 지연 시간을 넘은 경우
			if (elapsed > firingDelay) {
				firingTimer = System.nanoTime();

				if (powerLevel == 0) {
					GamePanel.bullets.add(new Bullet(270, x, y));
					AudioPlayer.player.start(bulletSoundAudio);
				} else if (powerLevel == 1) {
					GamePanel.bullets.add(new Bullet(270, x + 5, y));
					GamePanel.bullets.add(new Bullet(270, x - 5, y));
					AudioPlayer.player.start(bulletSoundAudio);
				} else if (powerLevel == 3) {
					GamePanel.bullets.add(new Bullet(270, x, y));
					GamePanel.bullets.add(new Bullet(275, x + 5, y));
					GamePanel.bullets.add(new Bullet(265, x - 5, y));
					AudioPlayer.player.start(bulletSoundAudio);
				} else {
					GamePanel.bullets.add(new Bullet(270, x, y));
					GamePanel.bullets.add(new Bullet(0, x + 10, y));
					GamePanel.bullets.add(new Bullet(90, x, y + 10));
					GamePanel.bullets.add(new Bullet(180, x - 10, y));
					AudioPlayer.player.start(bulletSoundAudio);
				}
			}
		}

		// player가 회복 상태인 경우
		if (recovering) {

			long elapsed = (System.nanoTime() - recoveryTimer) / 1000000;
			// player 회복 상태 경과 시간이 2초를 넘을 경우 회복 상태 여부를 거짓으로 설정
			if (elapsed > 2000) {
				recovering = false;
				recoveryTimer = 0;
			}
		}

		// bullet 발사 효과음 설정
		try {
			bulletSoundInput = new FileInputStream(bulletSound);
			bulletSoundAudio = new AudioStream(bulletSoundInput);
		} catch (IOException e) {
		}
	}

	// player 그래픽 구현
	public void draw(Graphics2D g) {

		// player가 회복 상태인 경우
		if (recovering) {

			g.setColor(pcolor_recovering);
			g.fillOval(x - r, y - r, 2 * r, 2 * r);

			g.setStroke(new BasicStroke(2));
			g.setColor(pcolor_recovering.darker());
			g.drawOval(x - r, y - r, 2 * r, 2 * r);
		}
		// player가 회복 상태가 아닌 경우
		else {

			g.setColor(pcolor_default);
			g.fillOval(x - r, y - r, 2 * r, 2 * r);

			g.setStroke(new BasicStroke(2));
			g.setColor(pcolor_default.darker());
			g.drawOval(x - r, y - r, 2 * r, 2 * r);
		}
	}
}
