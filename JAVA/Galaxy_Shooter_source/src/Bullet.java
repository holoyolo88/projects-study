import java.awt.Color;
import java.awt.Graphics2D;

public class Bullet {
	
	//�̻��� �߻� ��ǥ ����
	private double x;
	private double y;
	private int r;
	
	//�̻��� �߻� �̵� ��ǥ ����
	private double dx;
	private double dy;
	private double rad;
	private double speed;
	
	//�̻��� �߻� ���� ����
	private Color c1;
	
	//�̻��� �߻� ����
	public Bullet (double angle, int x, int y) {
		this.x = x;
		this.y = y;
		r = 3;
		
		rad = Math.toRadians(angle);
		speed = 15; //�̻��� �߻� �ӵ�
		dx = Math.cos(rad) * speed;
		dy = Math.sin(rad) * speed;
		
		c1 = Color.YELLOW; // �̻��� �߻� ����
	}
	
	public double getx() { return x; }
	public double gety() { return y; }
	public double getr() { return r; }
	
	//�̻��� ��ǥ ������Ʈ ����
	public boolean update() {
		
		x += dx;
		y += dy;
		
		if( x< -r || x > GamePanel.WIDTH + r ||
			y < -r || y > GamePanel.HEIGHT + r) {
			return true;
		}
		return false;
	}
	
	//�̻��� �׷��� ����
	public void draw (Graphics2D g) {
		
		g.setColor(c1);
		g.fillOval((int) (x - r),  (int) (y - r), 2 * r, 2 * r);
	}
}
