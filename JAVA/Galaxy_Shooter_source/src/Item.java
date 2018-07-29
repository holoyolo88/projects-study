import java.awt.Color;
import java.awt.Graphics2D;

public class Item {
	
	//�Ŀ� �� ��ǥ
	private double x;
	private double y;
	private int r;
	
	private int type;
	private Color color1;
	//�Ŀ� �� ����
	public Item (int type, double x, double y) {
		this.type = type;
		this.x = x;
		this.y = y;
		
		if(type == 1) {
			color1 = Color.PINK; // life up
			r = 5;
		}
		if(type == 2) {
			color1 = Color.YELLOW; // power up+1
			r = 5;
		}
		if(type == 3) {
			color1 = Color.ORANGE; // power up +2 GREEN enemy와 구분
			r = 5;
		}
		if(type == 4) {
			color1 = Color.WHITE; // slow down
			r = 5;
		}
		if(type == 5) {
			color1 = Color.RED; // score -100
			r=5;
		}
		if(type == 6)
		{
			color1 = Color.GREEN; // player 정지
			r=5;
		}
	}
	
	//�Ŀ� �� �Լ�
	public double getx() { return x; }
	public double gety() { return y; }
	public double getr() { return r; }

	public int getType() { return type; }
	
	//�Ŀ� �� ������Ʈ
	public boolean update() {
		
		y += 2;
		if(this.type==2) // YELLOW item의 낙하 속도 2배 증가
			y+=2;
		if(this.type==3) // ORANG item의 낙하 속도 2배 증가
			y+=2;
		if(y > GamePanel.HEIGHT + r) {
			return true;
		}
		
		return false;
	}
	
	//�Ŀ� �� �׷��� ����
	public void draw(Graphics2D g) {
		if(this.type == 5 || this.type ==6)
			{
			g.setColor(color1);
			int xarr[]= {(int)(x-r),(int)(x+r), (int)x};
			int yarr[]= {(int)(y+r),(int)(y+r), (int)(y-r)-1};
			g.fillPolygon(xarr, yarr, 3);
			}
		else
		{g.setColor(color1);
		g.fillRect((int) (x - r), (int) (y - r), 2 * r, 2 * r);
		}
	}
}
