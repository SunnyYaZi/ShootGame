package com.zf.shoot;

import java.awt.image.BufferedImage;

/**
 * ��������
 * @author zf
 *
 */

public abstract class FlyingObject {
	protected int width;			//������Ŀ�
	protected int height;			//������ĸ�
	protected int x;				//������ĺ�����
	protected int y;				//�������������
	protected BufferedImage image;	//�������ͼƬ
	
	/**���󷽷����������߲��ķ��� */
	public abstract void step();
	
	/**�ӵ�����ˣ��л���С�۷䣩 */
	public boolean shootBy(Bullet b){
		int x = this.x + width;
		int y = this.y + height;
		
		return b.x>=this.x && b.x<=x
			   &&
			   b.y>=this.y && b.y<=y;
	}
	
	/**���󷽷����жϷ������Ƿ���� */
	public abstract boolean outOfBounds();
}
