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

/** 主界面*/
public class ShootGame extends JPanel {
	public static final int WIDTH = 320;	//界面的宽
	public static final int HEIGHT = 568;	//界面的高
	
	//图片
	public static BufferedImage background;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage gameover;
	public static BufferedImage start;
	public static BufferedImage pause;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	
	public Hero hero = new Hero();			//英雄机对象
	public Bullet[] bullets = {};			//子弹数组
	public FlyingObject[] flyings = {};		//敌人数组（敌机和小蜜蜂）
	
	private int score = 0;					//分数
	
	private int state;						//状态
	private static final int START = 0;
	private static final int RUNNING = 1;
	private static final int PAUSE = 2;
	private static final int GAME_OVER = 3;
	
	/** static块：加载图片资源*/
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
	
	/** paint方法*/
	public void paint(Graphics g){
		g.drawImage(background,0,0,null);	//画背景图片
		paintHero(g);						//画英雄机
		paintBullet(g);						//画子弹
		paintFlyingObject(g);				//画敌人（敌机和小蜜蜂）
		paintScore(g);						//画分数
		paintState(g);						//画状态
	}
	
	/** 画英雄机*/
	public void paintHero(Graphics g){
		g.drawImage(hero.image,hero.x,hero.y,null);
	}
	
	/** 画子弹*/
	public void paintBullet(Graphics g){
		for(int i=0;i<bullets.length;i++){
			Bullet t = bullets[i];
			g.drawImage(t.image,t.x,t.y,null);
		}
	}
	
	/** 画飞行物*/
	public void paintFlyingObject(Graphics g){
		for(int i=0;i<flyings.length;i++){
			FlyingObject f = flyings[i];
			g.drawImage(f.image,f.x,f.y,null);
		}
	}
	
	/** 画分数*/
	public void paintScore(Graphics g){
		int x = 10;
		int y = 25;
		g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
		g.setColor(new Color(0x000000));
		g.drawString("score:"+score, x, y);
		g.drawString("life:"+hero.getLife(), x, y+20);
	}
	
	/** 画状态*/
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
	
	private Timer timer;		//定时器
	private int interval = 10;	//(毫秒)
	/** active方法*/
	public void active(){
		MouseAdapter l = new MouseAdapter(){
			/** 重写鼠标移动事件*/
			public void mouseMoved(MouseEvent e) {
				if(state == RUNNING){
					int x = e.getX();
					int y = e.getY();
					hero.moveTo(x, y);
				}
			}
			
			/** 重写鼠标点击事件*/
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
			
			/** 重写鼠标移出事件*/
			public void mouseExited(MouseEvent e){
				if(state == RUNNING){
					state = PAUSE;
				}
			}
			
			/** 重写鼠标移入事件*/
			public void mouseEntered(MouseEvent e){
				if(state == PAUSE){
					state = RUNNING;
				}
			}
		};
		this.addMouseMotionListener(l);
		this.addMouseListener(l);
		
		timer = new Timer();	//定时器对象
		timer.schedule(new TimerTask(){
			public void run(){	//定时执行方法
				if(state == RUNNING){
					enterActive();			//飞行物入场
					stepActive();			//飞行物走步
					shootActive();			//英雄机射击（子弹入场）
					bangActive();			//子弹打敌人（敌机和小蜜蜂）
					outOfBoundsActive();	//删除出界飞行物
					checkGameOverActive();	//检查游戏是否结束
				}
				repaint();					//重绘
			}
		}, interval, interval);
	}
	
	/** 飞行物入场*/
	private int flyEnteredIndex = 0;
	public void enterActive(){
		flyEnteredIndex++;
		if(flyEnteredIndex % 40 == 0){//每10*40毫秒生成一次飞行物
			FlyingObject obj = nextOne();
			flyings = Arrays.copyOf(flyings, flyings.length+1);//扩容
			flyings[flyings.length-1] = obj;
		}
	}
	
	/** 飞行物走步*/
	public void stepActive(){
		for(int i=0;i<flyings.length;i++){
			flyings[i].step();
		}
		
		for(int i=0;i<bullets.length;i++){
			bullets[i].step();
		}
		
		hero.step();
	}
	
	/** 英雄机射出子弹（子弹入场）*/
	private int shootIndex = 0;
	public void shootActive(){
		shootIndex++;
		if(shootIndex % 30 == 0){//每10*30毫秒射出一发子弹
			Bullet[] bs = hero.shoot();
			bullets = Arrays.copyOf(bullets, bullets.length+bs.length);//扩容
			System.arraycopy(bs, 0, bullets, bullets.length-bs.length, bs.length);
		}
	}
	
	/** 子弹打敌人（敌机和小蜜蜂）*/
	public void bangActive(){
		for(int i=0;i<bullets.length;i++){
			Bullet b = bullets[i];//得到每个子弹
			bang(b);//调用bang方法
		}
	}
	
	/** 删除出界的飞行物*/
	public void outOfBoundsActive(){
		int index =0;//下标
		FlyingObject[] flyingLives = new FlyingObject[flyings.length];//装活着的飞行物（没用出界）
		for(int i=0;i<flyings.length;i++){
			FlyingObject f = flyings[i];
			if(!f.outOfBounds()){//不出界
				flyingLives[index++] = f;
			}
		}
		flyings = Arrays.copyOf(flyingLives, index);//注意：这里应为index表示没出界飞行物的个数，而不是flyingLives.length
		
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
	
	/** 检查游戏是否结束*/
	public void checkGameOverActive(){
		if(isGameOver()){
			state = GAME_OVER;
		}
	}
	
	/** 判断游戏是否结束*/
	public boolean isGameOver(){
		for(int i=0;i<flyings.length;i++){//可能撞击到多个飞行物
			int index = -1;//下标
			if(hero.hit(flyings[i])){//若撞击到敌人
				hero.subLife();//减命
				hero.setDoubleFire(0);//将双倍火力变为单倍火力
				index = i;//记录被撞的飞行物的下标
			}
			if(index != -1){//删除被撞击的飞行物
				FlyingObject f = flyings[index];
				flyings[index] = flyings[flyings.length-1];
				flyings[flyings.length-1] = f;
				flyings = Arrays.copyOf(flyings, flyings.length-1);
			}
		}
		return hero.getLife()<=0;
	}
	
	public void bang(Bullet b){
		int index = -1;//下标
		for(int i=0;i<flyings.length;i++){
			FlyingObject obj = flyings[i];//得到每一个飞行物
			if(obj.shootBy(b)){
				index = i;
				break;
			}
		}
		if(index != -1){
			FlyingObject one = flyings[index];//被撞击的飞行物
			if(one instanceof Enemy){//如果是敌机
				Enemy e = (Enemy)one;
				score += e.getScore();//获得分数
			}else if(one instanceof Award){//如果是小蜜蜂
				Award a = (Award)one;
				int type = a.getType();//获取奖励类型
				switch(type){
				case Award.DOUBLE_FIRE://如果是双倍火力
					hero.addDoubleFire();
					break;
				case Award.LIFE://如果是增命
					hero.addLife();
					break;
				}
			}
			//删除被撞击的飞行物
			FlyingObject t = flyings[index];
			flyings[index] = flyings[flyings.length-1];
			flyings[flyings.length-1] = t;
			flyings = Arrays.copyOf(flyings, flyings.length-1);
		}
	}
	
	/** 生成对象，工厂方法，一般为静态方法*/
	public static FlyingObject nextOne(){
		Random rand = new Random();
		int num = rand.nextInt(10);
		if(num == 0){
			return new Bee();
		}else {
			return new Airplane();
		}
	}
	
	/** 程序入口*/
	public static void main(String[] args) {
		JFrame frame = new JFrame("Fly");
		ShootGame game = new ShootGame();
		frame.add(game);
		
		frame.setSize(WIDTH, HEIGHT);
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);//显示，尽快调用paint方法
		
		game.active();			//调用active方法
	}
}
