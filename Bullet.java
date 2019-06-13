package com.zf.shoot;
/**
 * �ӵ��ࣺ�Ƿ�����
 * @author zf
 *
 */
public class Bullet extends FlyingObject {
	private int speed = 3;	//�ߵĲ���
	
	/** ���췽��*/
	public Bullet(int x,int y){
		image = ShootGame.bullet;
		width = image.getWidth();
		height = image.getHeight();
		this.x = x;
		this.y = y;
	}
	
	/**�ӵ��߲�*/
	public void step(){
		y -= speed;
	}
	
	/** �ж��ӵ��Ƿ����*/
	public boolean outOfBounds() {
		return y<-height;
	}
}
