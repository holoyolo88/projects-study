import java.awt.*;

public class Bullet {

	// bullet 좌표
	private double x;
	private double y;

	// bullet 반지름
	private int r;

	// bullet 라디안 (원 둘레 위에서 반지름의 길이와 같은 길이를 갖는 호에 대응하는 중심각의 크기)
	// 원 360도 == 2PI 라디안
	private double rad;

	// bullet 이동 거리
	private double dx;
	private double dy;

	// bullet 속력
	private double speed;

	// bullet 색상
	private Color bcolor;

	// Bullet 클래스 생성자
	public Bullet(double angle, int x, int y) {

		this.x = x;
		this.y = y;

		r = 3;

		rad = Math.toRadians(angle);

		speed = 15;

		dx = Math.cos(rad) * speed; // 라디안에 따른 이동거리
		dy = Math.sin(rad) * speed; // 라디안에 따른 이동거리

		bcolor = Color.YELLOW;
	}

	public double getx() {
		return x;
	}

	public double gety() {
		return y;
	}

	public double getr() {
		return r;
	}

	// bullet 좌표 정보 업데이트
	public boolean update() {

		if (Player.Ppause != true) {
			x += dx;
			y += dy;
		}

		// bullet이 panel 범위를 벗어날 경우
		if (x < -r || x > GamePanel.WIDTH + r || y < -r || y > GamePanel.HEIGHT + r) {
			return true;
		}
		return false;
	}

	// bullet 그래픽 구현
	public void draw(Graphics2D g) {

		g.setColor(bcolor);
		g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
	}
}
