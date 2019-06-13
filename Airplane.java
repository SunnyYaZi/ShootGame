package com.zf.shoot;

import java.util.Random;

/**
 * 敌机：是飞行物，也是敌人
 * @author zf
 *
 */
public class Airplane extends FlyingObject implements Enemy {
	private int speed = 2;	//走的步数
	
	/**构造方法 */
	public Airplane(){
		image = ShootGame.airplane;
		width = image.getWidth();
		height = image.getHeight();
		y = -height;
		Random rand = new Random();
		x = rand.nextInt(ShootGame.WIDTH-width);
	}
	
	/** 敌机走步方法*/
	public void step(){
		y += speed;
	}
	
	/**打中一个敌机的5分 */
	public int getScore(){
		return 5;
	}
	
	/** 判断敌机是否出界*/
	public boolean outOfBounds() {
		return y>ShootGame.HEIGHT;
	}
}
