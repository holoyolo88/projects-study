import java.awt.*;

public class Enemy {

	// enemy 좌표
	private double x;
	private double y;

	// enemy 반지름
	private int r;

	// enemy 이동 거리
	private double dx;
	private double dy;

	// enemy 라디안
	private double rad;

	// enemy 속력
	private double speed;

	// enemy 체력
	private int health;

	// enemy 유형
	private int type;

	// enemy 랭크
	private int rank;

	// enemy 색상
	private Color ecolor;

	// enemy 준비 상태 정보
	private boolean ready;

	// enemy 사망 상태 정보
	private boolean dead;

	// bullet으로부터의 타격 여부
	private boolean hit;

	// bullet으로부터 타격받은 시각
	private long hitTimer;

	// slow down item 사용 여부
	private boolean slow;

	// pause 사용 여부
	public static boolean Epause;

	// Enemy 클래스 생성자
	public Enemy(int type, int rank) {
		this.type = type;
		this.rank = rank;
		Epause = false;

		// enemy 체력과 속력은 반비례, 체력과 반지름은 비례
		// enemy 유형 1 - 기본
		if (type == 1) {
			ecolor = Color.LIGHT_GRAY;
			if (rank == 1) {
				speed = 5;
				r = 7;
				health = 1;
			} else if (rank == 2) {
				speed = 5;
				r = 10;
				health = 2;
			} else if (rank == 3) {
				speed = 2.5;
				r = 20;
				health = 3;
			} else {
				speed = 1.5;
				r = 30;
				health = 4;
			}
		}

		// enemy 유형 2 - 속력 증가
		else if (type == 2) {
			ecolor = Color.BLUE;
			if (rank == 1) {
				speed = 7;
				r = 7;
				health = 2;
			} else if (rank == 2) {
				speed = 7;
				r = 10;
				health = 3;
			} else if (rank == 3) {
				speed = 5;
				r = 20;
				health = 3;
			} else {
				speed = 2.5;
				r = 30;
				health = 5;
			}
		}

		// enemy 유형 3 - 체력 증가
		else {
			ecolor = Color.GREEN;
			if (rank == 1) {
				speed = 3.5;
				r = 7;
				health = 5;
			} else if (rank == 2) {
				speed = 3.5;
				r = 10;
				health = 6;
			} else if (rank == 3) {
				speed = 3.5;
				r = 25;
				health = 7;
			} else {
				speed = 3.5;
				r = 40;
				health = 8;
			}
		}

		// enemy 시작 좌표를 난수로 설정
		x = Math.random() * GamePanel.WIDTH / 2 + GamePanel.WIDTH / 4;
		y = -r;

		double angle = Math.random() * 140 + 20;
		rad = Math.toRadians(angle);

		dx = Math.cos(rad) * speed;
		dy = Math.sin(rad) * speed;

		ready = false;
		dead = false;

		hit = false;
		hitTimer = 0;
	}

	public double getx() {
		return x;
	}

	public double gety() {
		return y;
	}

	public int getr() {
		return r;
	}

	public int getType() {
		return type;
	}

	public int getRank() {
		return rank;
	}

	public boolean isDead() {
		return dead;
	}

	public void setSlow(boolean b) {
		slow = b;
	}

	public void setEpause(boolean b) {
		Epause = b;
	}

	// enemy가 bullet으로부터 타격 받을 시 실행
	// 체력 감소, 사망 상태 판단, 타격 받은 시각 설정
	public void hit() {
		health--;
		if (health <= 0) {
			dead = true;
		}
		hit = true;
		hitTimer = System.nanoTime();
	}

	// enemy가 bullet으로부터 타격 받을 시 실행
	// 생성할 enemy 수 설정, 새로운 enemy 생성
	public void explode() {
		int amount = 0;
		// enemy 랭크가 1보다 클 경우 생성할 enemy 수 설정
		if (rank > 1) {
			if (type == 1) {
				amount = 3;
			} else if (type == 2) {
				amount = 3;
			} else {
				amount = 4;
			}
		}
		// amount만큼 새로운 enemy 생성
		for (int i = 0; i < amount; i++) {
			// 타격받은 enemy보다 랭크가 낮은 새로운 enemy 생성
			Enemy e = new Enemy(getType(), getRank() - 1);
			e.setSlow(slow);
			e.setEpause(Epause);
			e.x = this.x;
			e.y = this.y;
			double angle = 0;
			if (!ready) {
				angle = Math.random() * 140 + 20;
			} else {
				angle = Math.random() * 360;
			}
			e.rad = Math.toRadians(angle);
			GamePanel.enemies.add(e);
		}
	}

	// enemy 기본 정보 업데이트
	public void update() {

		// slow down item을 사용할 경우
		if (slow) {

			// 속력 감소
			x += dx * 0.1;
			y += dy * 0.1;
		}
		// slow down item을 사용하지 않을 경우
		else {

			// pause 상태가 아닐 경우 이동할 거리 설정
			if (Epause != true) {
				x += dx;
				y += dy;
			}
		}

		// enemy가 준비 상태가 아닐 경우
		if (!ready) {

			// enemy가 panel 범위 안이라면 준비 상태를 참으로 설정
			if (x > r && x < GamePanel.WIDTH - r && y > r && y < GamePanel.HEIGHT - r) {
				ready = true;
			}
		}

		// 이동할 좌표가 panel 범위를 벗어난 경우 이동할 거리 재설정
		if (x < r && dx < 0)
			dx = -dx;
		if (y < r && dy < 0)
			dy = -dy;
		if (x > GamePanel.WIDTH - r && dx > 0)
			dx = -dx;
		if (y > GamePanel.HEIGHT - r && dy > 0)
			dy = -dy;

		// bullet으로부터 타격받은 경우
		if (hit) {

			// 타격 후 경과 시간이 50이상일 경우 타격 상태를 거짓으로 전환
			long elapsed = (System.nanoTime() - hitTimer) / 1000000;
			if (elapsed > 50) {
				hit = false;
				hitTimer = 0;
			}
		}
	}

	// enemy 그래픽 구현
	public void draw(Graphics2D g) {

		// bullet으로부터 타격받은 경우
		if (hit) {
			g.setColor(Color.WHITE);
			g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);

			g.setColor(Color.WHITE.darker());
			g.setStroke(new BasicStroke(2));
			g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
		}
		// bullet으로부터 타격받지 않은 경우
		else {
			g.setColor(ecolor);
			g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
		}
	}

}
