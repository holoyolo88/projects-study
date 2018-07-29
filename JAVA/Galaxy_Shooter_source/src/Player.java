import java.awt.BasicStroke;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.media.*;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Player {
	
	//�÷��̾� ��ġ
	private int x;
	private int y;
	private int r;
	
	//�÷��̾� �̵� ��ġ
	private int dx;
	private int dy;
	private int speed;
	
	//�÷��̾� �̵� ����
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	
	//�÷��̾� ����
	private boolean firing;
	private long firingTimer;
	private long firingDelay;
	
	//�浹 �� ȸ��
	private boolean recovering;
	private long recoveryTimer;
	
	//�÷��̾� ���
	private int life;
	
	//�÷��̾� ����
	private Color c1;
	private Color c2;
	
	//����
	private int score;
	
	//�Ŀ�
	private int powerLevel;
	private int power;
	private int[] requiredPower = {
			1,2,3,4,5
	};
	
	//����
	private File bulletSound = new File("sounds/bulletSound.wav");
	private AudioStream bulletSoundAudio;
    private InputStream bulletSoundInput;
	
    public boolean pause; // pause 여부
    public static boolean Gpause;
	
    //�÷��̾� ��ǥ ��ġ, �ӵ�, ���, �Ѿ� �߻� �ӵ� ����
	public Player() {
		x = GamePanel.WIDTH / 2;
		y = GamePanel.HEIGHT / 2+20; // wave 가리지 않도록
		r = 5;
		
		dx = 0;
		dy = 0;
		speed = 10;
		
		life = 3;
		
		c1 = Color.WHITE;
		c2 = Color.RED;
		
		firing = false;
		firingTimer = System.nanoTime();
		firingDelay = 250;
		
		recovering = false;
		recoveryTimer = 0;
		
		score = 0;
		pause =false;
		Gpause = false;
	}
	
	public void setPause(boolean b) { pause = b;}
	public static void setGpause(boolean b) { Gpause = b;}
	
	public int getx() { return x; }
	public int gety() { return y; }
	public int getr() { return r; }
	
	public int getScore() { return score; }
	
	public int getLifes() { return life; }
	
	public boolean isDead() { return life <= 0; }
	public boolean isRecovering() { return recovering; }
	
	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	public void setUp(boolean b) { up = b; }
	public void setDown(boolean b) { down = b; }
	
	public void setFiring(boolean b) { firing = b; }
	
	public void addScore(int i) { score += i; }
	
	public void gainLife() {
		life++;
	}
	
	public void lostLife(){
		life--;
		recovering = true;
		recoveryTimer = System.nanoTime();
	}
	
	public void increasePower(int i) {
		power += i;
		if (powerLevel == 4){
			if (power > requiredPower [powerLevel]) {
				power = requiredPower [powerLevel];
			}
			return;
		}
		if (power >= requiredPower [powerLevel]) {
			power -= requiredPower [powerLevel];
			powerLevel++;
		}
	}
	
	public int getPowerLevel() { return powerLevel; }
	public int getPower() { return power; }
	public int getRequiredPower() {return requiredPower [powerLevel]; }
	
	//�÷��̾��� ��ġ �����̴� ��ǥ �� ������Ʈ
	public void update(){
		if(pause != true && Gpause != true) //pause 여부 판단
		{if(left) {
			dx = -speed;
		}
		if(right) {
			dx = speed;
		}
		if(up) {
			dy = -speed;
		}
		if(down) {
			dy = speed;
		}
		
		x += dx;
		y += dy;}
		
		if(x < r) x = r;
		if(y < r) y = r;
		if(x > GamePanel.WIDTH - r) x = GamePanel.WIDTH - r;
		if(y > GamePanel.HEIGHT - r) y = GamePanel.HEIGHT - r;
		
		dx = 0;
		dy = 0;
		
		if (firing) {
			long elapsed = (System.nanoTime() - firingTimer) / 1000000;
			
			if (elapsed > firingDelay) {
				firingTimer = System.nanoTime();
				
				if(powerLevel < 2 ) {
					GamePanel.bullets.add(new Bullet(270, x, y));
					AudioPlayer.player.start(bulletSoundAudio);
				}
				else if(powerLevel < 4){
					GamePanel.bullets.add(new Bullet(270, x+5, y));
					GamePanel.bullets.add(new Bullet(270, x-5, y));
					AudioPlayer.player.start(bulletSoundAudio);
				}
				else {
					GamePanel.bullets.add(new Bullet(270, x, y));
					GamePanel.bullets.add(new Bullet(275, x+5, y));
					GamePanel.bullets.add(new Bullet(265, x-5, y));
					AudioPlayer.player.start(bulletSoundAudio);
				}
			}
			
		}
		
		if(recovering) {
			long elapsed = (System.nanoTime() - recoveryTimer) / 1000000;
			if (elapsed > 2000) {
				recovering = false;
				recoveryTimer = 0;
			}
		}
		
		try {
            bulletSoundInput = new FileInputStream(bulletSound);
            bulletSoundAudio = new AudioStream(bulletSoundInput);
        } catch (IOException e) {
        }
	}
	
	//�÷��̾� �׷��� ����
	public void draw(Graphics2D g) {
		
		if(recovering){
			g.setColor(c2);
			g.fillOval(x - r,  y - r, 2 * r,  2 * r);
			
			g.setStroke(new BasicStroke(2));
			g.setColor(c2.darker());
			g.drawOval(x - r, y - r, 2 * r, 2 * r);
		} else {
			g.setColor(c1);
			g.fillOval(x - r,  y - r, 2 * r,  2 * r);
			g.setStroke(new BasicStroke(2));
			g.setColor(c1.darker());
			g.drawOval(x - r, y - r, 2 * r, 2 * r);
		}
		
	}

}
