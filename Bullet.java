package com.zf.shoot;
/**
 * 子弹类：是飞行物
 * @author zf
 *
 */
public class Bullet extends FlyingObject {
	private int speed = 3;	//走的步数
	
	/** 构造方法*/
	public Bullet(int x,int y){
		image = ShootGame.bullet;
		width = image.getWidth();
		height = image.getHeight();
		this.x = x;
		this.y = y;
	}
	
	/**子弹走步*/
	public void step(){
		y -= speed;
	}
	
	/** 判断子弹是否出界*/
	public boolean outOfBounds() {
		return y<-height;
	}
}
