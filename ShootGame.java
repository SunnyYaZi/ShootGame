package com.zf.shoot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/** ������*/
public class ShootGame extends JPanel {
	public static final int WIDTH = 320;	//����Ŀ�
	public static final int HEIGHT = 568;	//����ĸ�
	
	//ͼƬ
	public static BufferedImage background;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage gameover;
	public static BufferedImage start;
	public static BufferedImage pause;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	
	public Hero hero = new Hero();			//Ӣ�ۻ�����
	public Bullet[] bullets = {};			//�ӵ�����
	public FlyingObject[] flyings = {};		//�������飨�л���С�۷䣩
	
	private int score = 0;					//����
	
	private int state;						//״̬
	private static final int START = 0;
	private static final int RUNNING = 1;
	private static final int PAUSE = 2;
	private static final int GAME_OVER = 3;
	
	/** static�飺����ͼƬ��Դ*/
	static{
		try {
			background = ImageIO.read(ShootGame.class.getResource("background.png"));
			airplane = ImageIO.read(ShootGame.class.getResource("airplane.png"));
			bee = ImageIO.read(ShootGame.class.getResource("bee.png"));
			bullet = ImageIO.read(ShootGame.class.getResource("bullet.png"));
			gameover = ImageIO.read(ShootGame.class.getResource("gameover.png"));
			start = ImageIO.read(ShootGame.class.getResource("start.png"));
			pause = ImageIO.read(ShootGame.class.getResource("pause.png"));
			hero0 = ImageIO.read(ShootGame.class.getResource("hero0.png"));
			hero1 = ImageIO.read(ShootGame.class.getResource("hero1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** paint����*/
	public void paint(Graphics g){
		g.drawImage(background,0,0,null);	//������ͼƬ
		paintHero(g);						//��Ӣ�ۻ�
		paintBullet(g);						//���ӵ�
		paintFlyingObject(g);				//�����ˣ��л���С�۷䣩
		paintScore(g);						//������
		paintState(g);						//��״̬
	}
	
	/** ��Ӣ�ۻ�*/
	public void paintHero(Graphics g){
		g.drawImage(hero.image,hero.x,hero.y,null);
	}
	
	/** ���ӵ�*/
	public void paintBullet(Graphics g){
		for(int i=0;i<bullets.length;i++){
			Bullet t = bullets[i];
			g.drawImage(t.image,t.x,t.y,null);
		}
	}
	
	/** ��������*/
	public void paintFlyingObject(Graphics g){
		for(int i=0;i<flyings.length;i++){
			FlyingObject f = flyings[i];
			g.drawImage(f.image,f.x,f.y,null);
		}
	}
	
	/** ������*/
	public void paintScore(Graphics g){
		int x = 10;
		int y = 25;
		g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
		g.setColor(new Color(0x000000));
		g.drawString("score:"+score, x, y);
		g.drawString("life:"+hero.getLife(), x, y+20);
	}
	
	/** ��״̬*/
	public void paintState(Graphics g){
		switch(state){
		case START:
			g.drawImage(start,0,0,null);
			break;
		case PAUSE:
			g.drawImage(pause,0,0,null);
			break;
		case GAME_OVER:
			g.drawImage(gameover,0,0,null);
			break;
		}
	}
	
	private Timer timer;		//��ʱ��
	private int interval = 10;	//(����)
	/** active����*/
	public void active(){
		MouseAdapter l = new MouseAdapter(){
			/** ��д����ƶ��¼�*/
			public void mouseMoved(MouseEvent e) {
				if(state == RUNNING){
					int x = e.getX();
					int y = e.getY();
					hero.moveTo(x, y);
				}
			}
			
			/** ��д������¼�*/
			public void mouseClicked(MouseEvent e){
				switch(state){
				case START:
					state = RUNNING;
					break;
				case GAME_OVER:
					hero = new Hero();
					flyings = new FlyingObject[0];
					bullets = new Bullet[0];
					state = START;
					break;
				}
			}
			
			/** ��д����Ƴ��¼�*/
			public void mouseExited(MouseEvent e){
				if(state == RUNNING){
					state = PAUSE;
				}
			}
			
			/** ��д��������¼�*/
			public void mouseEntered(MouseEvent e){
				if(state == PAUSE){
					state = RUNNING;
				}
			}
		};
		this.addMouseMotionListener(l);
		this.addMouseListener(l);
		
		timer = new Timer();	//��ʱ������
		timer.schedule(new TimerTask(){
			public void run(){	//��ʱִ�з���
				if(state == RUNNING){
					enterActive();			//�������볡
					stepActive();			//�������߲�
					shootActive();			//Ӣ�ۻ�������ӵ��볡��
					bangActive();			//�ӵ�����ˣ��л���С�۷䣩
					outOfBoundsActive();	//ɾ�����������
					checkGameOverActive();	//�����Ϸ�Ƿ����
				}
				repaint();					//�ػ�
			}
		}, interval, interval);
	}
	
	/** �������볡*/
	private int flyEnteredIndex = 0;
	public void enterActive(){
		flyEnteredIndex++;
		if(flyEnteredIndex % 40 == 0){//ÿ10*40��������һ�η�����
			FlyingObject obj = nextOne();
			flyings = Arrays.copyOf(flyings, flyings.length+1);//����
			flyings[flyings.length-1] = obj;
		}
	}
	
	/** �������߲�*/
	public void stepActive(){
		for(int i=0;i<flyings.length;i++){
			flyings[i].step();
		}
		
		for(int i=0;i<bullets.length;i++){
			bullets[i].step();
		}
		
		hero.step();
	}
	
	/** Ӣ�ۻ�����ӵ����ӵ��볡��*/
	private int shootIndex = 0;
	public void shootActive(){
		shootIndex++;
		if(shootIndex % 30 == 0){//ÿ10*30�������һ���ӵ�
			Bullet[] bs = hero.shoot();
			bullets = Arrays.copyOf(bullets, bullets.length+bs.length);//����
			System.arraycopy(bs, 0, bullets, bullets.length-bs.length, bs.length);
		}
	}
	
	/** �ӵ�����ˣ��л���С�۷䣩*/
	public void bangActive(){
		for(int i=0;i<bullets.length;i++){
			Bullet b = bullets[i];//�õ�ÿ���ӵ�
			bang(b);//����bang����
		}
	}
	
	/** ɾ������ķ�����*/
	public void outOfBoundsActive(){
		int index =0;//�±�
		FlyingObject[] flyingLives = new FlyingObject[flyings.length];//װ���ŵķ����û�ó��磩
		for(int i=0;i<flyings.length;i++){
			FlyingObject f = flyings[i];
			if(!f.outOfBounds()){//������
				flyingLives[index++] = f;
			}
		}
		flyings = Arrays.copyOf(flyingLives, index);//ע�⣺����ӦΪindex��ʾû���������ĸ�����������flyingLives.length
		
		index = 0;
		Bullet[] bulletLives = new Bullet[bullets.length];
		for(int i=0;i<bullets.length;i++){
			Bullet b = bullets[i];
			if(!b.outOfBounds()){
				bulletLives[index++] = b;
			}
		}
		bullets = Arrays.copyOf(bulletLives, index);
	}
	
	/** �����Ϸ�Ƿ����*/
	public void checkGameOverActive(){
		if(isGameOver()){
			state = GAME_OVER;
		}
	}
	
	/** �ж���Ϸ�Ƿ����*/
	public boolean isGameOver(){
		for(int i=0;i<flyings.length;i++){//����ײ�������������
			int index = -1;//�±�
			if(hero.hit(flyings[i])){//��ײ��������
				hero.subLife();//����
				hero.setDoubleFire(0);//��˫��������Ϊ��������
				index = i;//��¼��ײ�ķ�������±�
			}
			if(index != -1){//ɾ����ײ���ķ�����
				FlyingObject f = flyings[index];
				flyings[index] = flyings[flyings.length-1];
				flyings[flyings.length-1] = f;
				flyings = Arrays.copyOf(flyings, flyings.length-1);
			}
		}
		return hero.getLife()<=0;
	}
	
	public void bang(Bullet b){
		int index = -1;//�±�
		for(int i=0;i<flyings.length;i++){
			FlyingObject obj = flyings[i];//�õ�ÿһ��������
			if(obj.shootBy(b)){
				index = i;
				break;
			}
		}
		if(index != -1){
			FlyingObject one = flyings[index];//��ײ���ķ�����
			if(one instanceof Enemy){//����ǵл�
				Enemy e = (Enemy)one;
				score += e.getScore();//��÷���
			}else if(one instanceof Award){//�����С�۷�
				Award a = (Award)one;
				int type = a.getType();//��ȡ��������
				switch(type){
				case Award.DOUBLE_FIRE://�����˫������
					hero.addDoubleFire();
					break;
				case Award.LIFE://���������
					hero.addLife();
					break;
				}
			}
			//ɾ����ײ���ķ�����
			FlyingObject t = flyings[index];
			flyings[index] = flyings[flyings.length-1];
			flyings[flyings.length-1] = t;
			flyings = Arrays.copyOf(flyings, flyings.length-1);
		}
	}
	
	/** ���ɶ��󣬹���������һ��Ϊ��̬����*/
	public static FlyingObject nextOne(){
		Random rand = new Random();
		int num = rand.nextInt(10);
		if(num == 0){
			return new Bee();
		}else {
			return new Airplane();
		}
	}
	
	/** �������*/
	public static void main(String[] args) {
		JFrame frame = new JFrame("Fly");
		ShootGame game = new ShootGame();
		frame.add(game);
		
		frame.setSize(WIDTH, HEIGHT);
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);//��ʾ���������paint����
		
		game.active();			//����active����
	}
}
