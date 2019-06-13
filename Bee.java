package com.zf.shoot;

import java.util.Random;

/**
 * �۷��ࣺ�Ƿ����Ҳ�ǽ���
 * @author zf
 *
 */
public class Bee extends FlyingObject implements Award {
	private int xSpeed = 1;		//x�����ߵĲ���
	private int ySpeed = 2;		//y�����ߵĲ���
	private int awardType;		//����������
	
	/** ���췽��*/
	public Bee(){
		image = ShootGame.bee;
		width = image.getWidth();
		height = image.getHeight();
		y = -height;
		Random rand = new Random();
		x = rand.nextInt(ShootGame.WIDTH-width);
		awardType = rand.nextInt(2);
	}

	/** С�۷��߲�����*/
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
	
	/** ��ȡ��������*/
	public int getType() {
		return awardType;
	}
	
	/** �ж�С�۷��Ƿ����*/
	public boolean outOfBounds(){
		return y>ShootGame.HEIGHT;
	}
}
