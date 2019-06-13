package com.zf.shoot;

import java.awt.image.BufferedImage;

/**
 * 英雄机类：是飞行物
 * @author zf
 *
 */
public class Hero extends FlyingObject {
	private int doubleFire;					//双倍火力
	private int life;						//命
	private BufferedImage[] images = {};	//英雄机走步（即切图片）
	private int index;						//切图片的频率
	
	/** 构造方法*/
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
	
	/** 英雄机走步（切图片）*/
	public void step(){
		int num = index++/10%images.length;//此方法被调用10次切一次图片
		image = images[num];
	}
	
	/** 英雄机射击*/
	public Bullet[] shoot(){
		int xStep = this.width/4;
		int yStep = 20;
		if(doubleFire > 0){//双倍火力
			Bullet[] bs = new Bullet[2];
			bs[0] = new Bullet(this.x+1*xStep,this.y-yStep);
			bs[1] = new Bullet(this.x+3*xStep,this.y-yStep);
			return bs;
		}else {//单倍火力
			Bullet[] bs = new Bullet[1];
			bs[0] = new Bullet(this.x+2*xStep,this.y-yStep);
			return bs;
		}
	}
	
	/**英雄机移动 */
	public void moveTo(int x,int y){
		this.x = x - this.width/2;
		this.y = y - this.height/2;
	}
	
	/** 获得双倍火力*/
	public void addDoubleFire(){
		doubleFire += 40;
	}
	
	/**增命 */
	public void addLife(){
		life++;
	}
	
	/** 获取生命值*/
	public int getLife() {
		return life;
	}
	
	/** 判断是否出界*/
	public boolean outOfBounds() {
		return false;	//永不出界
	}
	
	/**撞击敌人（敌机和小蜜蜂）*/
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
	
	/** 减命*/
	public void subLife(){
		life--;
	}
	
	/** 设置双倍火力*/
	public void setDoubleFire(int doubleFire){
		this.doubleFire = doubleFire;
	}
}
