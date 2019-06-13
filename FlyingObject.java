package com.zf.shoot;

import java.awt.image.BufferedImage;

/**
 * 飞行物类
 * @author zf
 *
 */

public abstract class FlyingObject {
	protected int width;			//飞行物的宽
	protected int height;			//飞行物的高
	protected int x;				//飞行物的横坐标
	protected int y;				//飞行物的纵坐标
	protected BufferedImage image;	//飞行物的图片
	
	/**抽象方法：飞行物走步的方法 */
	public abstract void step();
	
	/**子弹打敌人（敌机和小蜜蜂） */
	public boolean shootBy(Bullet b){
		int x = this.x + width;
		int y = this.y + height;
		
		return b.x>=this.x && b.x<=x
			   &&
			   b.y>=this.y && b.y<=y;
	}
	
	/**抽象方法：判断飞行物是否出界 */
	public abstract boolean outOfBounds();
}
