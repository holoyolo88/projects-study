import java.awt.*;

public class Text {

	// text 좌표
	private double x;
	private double y;

	// text 유지 시간
	private long susTextTime;

	// text 내용
	private String s;

	// text 생성 시각
	private long textTimer;

	// Text 클래스 생성자
	public Text(double x, double y, long susTextTime, String s) {

		this.x = x;
		this.y = y;
		this.susTextTime = susTextTime;
		this.s = s;
		textTimer = System.nanoTime();
	}

	// text 유지 정보 업데이트
	public boolean update() {

		long elapsed = (System.nanoTime() - textTimer) / 1000000;
		if (elapsed > susTextTime) {
			return true;
		}
		return false;
	}

	// text 그래픽 구현
	public void draw(Graphics2D g) {

		long elapsed = (System.nanoTime() - textTimer) / 1000000;
		int alpha = (int) (255 * Math.sin(3.14 * elapsed / susTextTime));
		if (alpha > 255)
			alpha = 255;
		g.setFont(new Font("Century Gothic", Font.PLAIN, 12));
		g.setColor(new Color(255, 255, 255, alpha));
		int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
		g.drawString(s, (int) (x - (length / 2)), (int) y);
	}
}
