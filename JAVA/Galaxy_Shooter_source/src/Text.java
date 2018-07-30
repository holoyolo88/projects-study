import java.awt.*;

public class Text {

	// text 좌표
	private double x;
	private double y;

	// text 유지 시간
	private long time;

	// text 내용
	private String s;

	// text 생성 시각
	private long start;

	// Text 클래스 생성자
	public Text(double x, double y, long time, String s) {
		
		this.x = x;
		this.y = y;
		this.time = time;
		this.s = s;
		start = System.nanoTime();
	}

	// text 유지 정보 업데이트
	public boolean update() {

		long elapsed = (System.nanoTime() - start) / 1000000;
		if (elapsed > time) {
			return true;
		}
		return false;
	}

	// text 그래픽 구현
	public void draw(Graphics2D g) {

		long elapsed = (System.nanoTime() - start) / 1000000;
		int alpha = (int) (255 * Math.sin(3.14 * elapsed / time));
		if (alpha > 255) alpha = 255;
		g.setFont(new Font("Century Gothic", Font.PLAIN, 12));
		g.setColor(new Color(255, 255, 255, alpha));
		int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
		g.drawString(s, (int) (x - (length / 2)), (int) y);
	}

}
