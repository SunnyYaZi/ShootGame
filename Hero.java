package com.zf.shoot;

import java.awt.image.BufferedImage;

/**
 * Ӣ�ۻ��ࣺ�Ƿ�����
 * @author zf
 *
 */
public class Hero extends FlyingObject {
	private int doubleFire;					//˫������
	private int life;						//��
	private BufferedImage[] images = {};	//Ӣ�ۻ��߲�������ͼƬ��
	private int index;						//��ͼƬ��Ƶ��
	
	/** ���췽��*/
	public Hero(){
		image = ShootGame.hero0;
		width = image.getWidth();
		height = image.getHeight();
		x = 125;
		y = 300;
		doubleFire = 0;
		life = 3;
		images = new BufferedImage[]{ShootGame.hero0,ShootGame.hero1};
	}
	
	/** Ӣ�ۻ��߲�����ͼƬ��*/
	public void step(){
		int num = index++/10%images.length;//�˷���������10����һ��ͼƬ
		image = images[num];
	}
	
	/** Ӣ�ۻ����*/
	public Bullet[] shoot(){
		int xStep = this.width/4;
		int yStep = 20;
		if(doubleFire > 0){//˫������
			Bullet[] bs = new Bullet[2];
			bs[0] = new Bullet(this.x+1*xStep,this.y-yStep);
			bs[1] = new Bullet(this.x+3*xStep,this.y-yStep);
			return bs;
		}else {//��������
			Bullet[] bs = new Bullet[1];
			bs[0] = new Bullet(this.x+2*xStep,this.y-yStep);
			return bs;
		}
	}
	
	/**Ӣ�ۻ��ƶ� */
	public void moveTo(int x,int y){
		this.x = x - this.width/2;
		this.y = y - this.height/2;
	}
	
	/** ���˫������*/
	public void addDoubleFire(){
		doubleFire += 40;
	}
	
	/**���� */
	public void addLife(){
		life++;
	}
	
	/** ��ȡ����ֵ*/
	public int getLife() {
		return life;
	}
	
	/** �ж��Ƿ����*/
	public boolean outOfBounds() {
		return false;	//��������
	}
	
	/**ײ�����ˣ��л���С�۷䣩*/
	public boolean hit(FlyingObject other){
		int x1 = other.x-width/2;
		int x2 = other.x+other.width+width/2;
		int y1 = other.y-height/2;
		int y2 = other.y+other.height+height/2;
		int heroX = this.x+width/2;
		int heroY = this.y+height/2;
		return heroX>x1 && heroX<x2
			   &&
			   heroY>y1 && heroY<y2;
	}
	
	/** ����*/
	public void subLife(){
		life--;
	}
	
	/** ����˫������*/
	public void setDoubleFire(int doubleFire){
		this.doubleFire = doubleFire;
	}
}
