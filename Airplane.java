package com.zf.shoot;

import java.util.Random;

/**
 * �л����Ƿ����Ҳ�ǵ���
 * @author zf
 *
 */
public class Airplane extends FlyingObject implements Enemy {
	private int speed = 2;	//�ߵĲ���
	
	/**���췽�� */
	public Airplane(){
		image = ShootGame.airplane;
		width = image.getWidth();
		height = image.getHeight();
		y = -height;
		Random rand = new Random();
		x = rand.nextInt(ShootGame.WIDTH-width);
	}
	
	/** �л��߲�����*/
	public void step(){
		y += speed;
	}
	
	/**����һ���л���5�� */
	public int getScore(){
		return 5;
	}
	
	/** �жϵл��Ƿ����*/
	public boolean outOfBounds() {
		return y>ShootGame.HEIGHT;
	}
}
