package com.ldfs.demo.listener;

import android.os.Bundle;

/**
 * 界面操作监听
 * 
 * @author Administrator
 * 
 */
public interface OperatListener {
	/** 按下返回键 */
	int BACK_PRESS = 1;

	int INIT_DATA = 2;

	/**
	 * 当fragment界面发生操作时回调
	 * 
	 * @param optionId
	 *            : 操作id
	 */
	void onOperate(int optionId, Bundle bundle);
}
