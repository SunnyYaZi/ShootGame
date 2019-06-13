package com.zf.shoot;

import java.util.Random;

/**
 * 蜜蜂类：是飞行物，也是奖励
 * @author zf
 *
 */
public class Bee extends FlyingObject implements Award {
	private int xSpeed = 1;		//x方向走的步数
	private int ySpeed = 2;		//y方向走的步数
	private int awardType;		//奖励的类型
	
	/** 构造方法*/
	public Bee(){
		image = ShootGame.bee;
		width = image.getWidth();
		height = image.getHeight();
		y = -height;
		Random rand = new Random();
		x = rand.nextInt(ShootGame.WIDTH-width);
		awardType = rand.nextInt(2);
	}

	/** 小蜜蜂走步方法*/
	public void step(){
		x += xSpeed;
		y += ySpeed;
		if(x<0){
			xSpeed = 1;
		}
		if(x>(ShootGame.WIDTH-width)){
			xSpeed = -1;
		}
	}
	
	/** 获取奖励类型*/
	public int getType() {
		return awardType;
	}
	
	/** 判断小蜜蜂是否出界*/
	public boolean outOfBounds(){
		return y>ShootGame.HEIGHT;
	}
}
