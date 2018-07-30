import java.awt.*;

public class Explosion {

	// explosion 좌표
	private double x;
	private double y;

	// explosion 반지름
	private int r;

	// explosion 최대 반지름
	private int maxRadius;

	// explosion 유형
	private int type;

	// Explosion 클래스 생성자
	public Explosion(double x, double y, int r, int max, int type) {
		
		this.x = x;
		this.y = y;
		this.r = r;
		maxRadius = max;
		// enemy 유형에 따라 explosion 유형 설정
		this.type = type;
	}

	// explosion 정보 업데이트
	// enemy가 bullet으로부터 타격받 실행
	public boolean update() {
		
		r += 2;	
		if (r >= maxRadius) {
			return true;
		}
		return false;

	}

	// explosion 그래픽 구현
	public void draw(Graphics2D g) {

		// explosion 유형 1의 경우
		if( type == 1)
		g.setColor(Color.LIGHT_GRAY);
		// explosion 유형 2의 경우
		else if( type == 2)
		g.setColor(Color.BLUE);
		// explosion 유형 3의 경우
		else g.setColor(Color.GREEN);
		
		g.setStroke(new BasicStroke(3));
		g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
	}
}
