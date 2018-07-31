import java.awt.*;

public class Item {

	// item 좌표
	private double x;
	private double y;

	// item 반지름
	private int r;

	// item 유형
	private int type;

	// item 색상
	private Color icolor;

	// Item 클래스 생성자
	public Item(int type, double x, double y) {

		this.type = type;
		this.x = x;
		this.y = y;

		// life up 아이템
		if (type == 1) {
			icolor = Color.PINK;
			r = 5;
		}

		// power up+1 아이템
		if (type == 2) {
			icolor = Color.YELLOW;
			r = 5;
		}

		// power up+2 아이템
		if (type == 3) {
			icolor = Color.ORANGE;
			r = 5;
		}

		// slow down 아이템
		if (type == 4) {
			icolor = Color.WHITE;
			r = 5;
		}

		// score -100 아이템
		if (type == 5) {
			icolor = Color.BLUE;
			r = 5;
		}

		// player freeze 아이템
		if (type == 6) {
			icolor = Color.GREEN;
			r = 5;
		}
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

	public int getType() {
		return type;
	}

	// item 정보 업데이트
	public boolean update() {

		// player pause 상태와 enemy pause 상태가 모두 거짓이라면
		if (Player.Ppause != true && Enemy.Epause != true) {
			y += 2;

			// power up+1 아이템 낙하 속력 2배
			if (this.type == 2)
				y += 2;

			// power up+2 아이템 낙하 속력 2배
			if (this.type == 3)
				y += 2;

			if (y > GamePanel.HEIGHT + r) {
				return true;
			}
		}
		return false;
	}

	// item 그래픽 구현
	public void draw(Graphics2D g) {

		// score -100 아이템과 player freeze 아이템의 경우
		if (this.type == 5 || this.type == 6) {

			g.setColor(icolor);
			int xarr[] = { (int) (x - r), (int) (x + r), (int) x };
			int yarr[] = { (int) (y + r), (int) (y + r), (int) (y - r) - 1 };

			// 삼각형 그리기
			g.fillPolygon(xarr, yarr, 3);
		} else {

			g.setColor(icolor);

			// 사각형 그리기
			g.fillRect((int) (x - r), (int) (y - r), 2 * r, 2 * r);
		}
	}
}
