import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Enemy {
	
	//�� �⺻ ��ǥ ����
	private double x;
	private double y;
	private int r;
	
	//�� �̵� ��ǥ ����
	private double dx;
	private double dy;
	private double rad;
	private double speed;
	
	//�� �⺻ ���� ����
	private int health;
	private int type;
	private int rank;
	
	//�� ĳ���� �� ����
	private Color c1;
	
	//�� �غ� �� ��� ó�� ����
	private boolean ready;
	private boolean dead;
	
	//�� ���� ���� ����
	private boolean hit;
	private long hitTimer;
	
	//������ �������� ����
	private boolean slow;
	public boolean Epause;
	
	
	//�� Ÿ�� �� �� ����
	public Enemy (int type, int rank) {
		this.type = type;
		this.rank = rank;
		Epause = false;
		
		//�⺻ ��
		if (type == 1) {
			c1 = Color.LIGHT_GRAY;
			if(rank == 1) { // 효율성 고려해 if문 여러개 대신 if else문으로 변경
				speed = 5; // �� �̵��ӵ�
				r = 7; // �� ũ��
				health = 1; // �� ü��
			}
			else if(rank == 2) { 
				speed = 5;
				r = 10;
				health = 2;
			}
			else if(rank == 3) {
				speed = 2.5;
				r = 20;
				health = 3;
			}
			else {
				speed = 1.5;
				r = 30;
				health = 4;
			}
		}
		
		//���� ��
		else if (type == 2) {
			c1 = Color.BLUE; // player 충돌 시와 식별하기 위해 blue로 변경
			if(rank == 1) {
				speed = 7;
				r = 7;
				health = 2;
			}
			else if(rank == 2) {
				speed = 7;
				r = 10;
				health = 3;
			}
			else if(rank == 3) {
				speed = 5;
				r = 20;
				health = 3;
			}
			else {
				speed = 2.5;
				r = 30;
				health = 5;
			}
		}
		
		//�������� ����� ��
		else {
			c1 = Color.GREEN;
			if(rank == 1) {
				speed = 3.5;
				r = 7;
				health = 5;
			}
			else if(rank == 2) {
				speed = 3.5;
				r = 10;
				health = 6;
			}
			else if(rank == 3) {
				speed = 3.5;
				r = 25;
				health = 7;
			}
			else {
				speed = 3.5;
				r = 40;
				health = 8;
			}
		}
		
		x = Math.random() * GamePanel.WIDTH / 2 + GamePanel.WIDTH / 4;
		y = -r;
		
		double angle = Math.random() * 140 + 20;
		rad = Math.toRadians(angle);
		
		dx = Math.cos(rad) * speed; // 각도에 따른 이동
		dy = Math.sin(rad) * speed; // 각도에 따른 이동
		
		ready = false;
		dead = false;
		
		hit = false;
		hitTimer = 0;
	}
	
	public double getx() { return x; }
	public double gety() { return y; }
	public int getr() { return r; }
	
	public int getType() { return type; }
	public int getRank() { return rank; }
	
	public void setSlow(boolean b) { slow = b; }
	
	public void setEpause(boolean b) { Epause = b;} //pause 구현을 위한 함수
	
	
	public boolean isDead () { return dead; }
	
	//�Ѿ˿� ������ �� ����
	public void hit() {
		health--;
		if(health <= 0) {
			dead = true;
		}
		hit = true;
		hitTimer = System.nanoTime();
	}
	
	//�� ���� ����
	public void explode() {
		int amount = 0;
		if(rank > 1) {
			if (type == 1) {
				amount = 3;
			}
			else if (type == 2) {
				amount = 3;
			}
			else {
				amount = 4;
			}

		}
		for (int i = 0; i < amount; i++) {
			Enemy e = new Enemy(getType(), getRank() - 1);
			e.setSlow(slow);
			e.setEpause(Epause);
			e.x = this.x;
			e.y = this.y;
			double angle = 0;
			if(!ready) {
				angle = Math.random() * 140 + 20;
			}
			else {
				angle = Math.random() * 360;
			}
			e.rad = Math.toRadians(angle);
			GamePanel.enemies.add(e);
		}
	}
	
	//�� ��ġ ��ǥ ������Ʈ ����
	public void update() {
		if (slow) {
			x += dx * 0.1;
			y += dy * 0.1;
		} else {
			if(Epause!=true)
			{x += dx;
			y += dy;}
		}
		
		if(!ready) {
			if (x > r && x < GamePanel.WIDTH - r &&
				y > r && y < GamePanel.HEIGHT - r) {
				ready = true;
			}
		}
		
		if(x < r && dx < 0) dx = -dx;
		if(y < r && dy < 0) dy = -dy;
		if(x > GamePanel.WIDTH - r && dx > 0) dx = -dx;
		if(y > GamePanel.HEIGHT - r && dy > 0) dy = -dy;
		
		if(hit) {
			long elapsed = (System.nanoTime() - hitTimer) / 1000000; //bullet과 충돌 후 경과시간
			if (elapsed > 50) {
				hit = false;
				hitTimer = 0;
			}
		}
	}
	
	//�� �׷��� ����
	public void draw (Graphics2D g) {
		
		if (hit) {
//			g.setColor(Color.WHITE); 
//			g.setStroke(new BasicStroke(3)); 
//			g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r); 
//			
//			
//			g.setColor(Color.WHITE.darker()); // c1 -> Color
//			g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
//			g.setStroke(new BasicStroke(1));
		}
		else {
		g.setColor(c1); 
		g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r); 
		
		g.setColor(c1.darker());
		g.setStroke(new BasicStroke(1));
		g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
		
		}
	}

}
