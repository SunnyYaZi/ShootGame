package com.zf.shoot;
/**
 * 奖励
 * @author zf
 *
 */
public interface Award {
	int DOUBLE_FIRE = 0;	//双倍火力
	int LIFE = 1;			//命
	
	int getType();			//获取奖励类型
}
