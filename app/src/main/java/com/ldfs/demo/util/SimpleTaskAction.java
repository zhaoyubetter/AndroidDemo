package com.ldfs.demo.util;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;

import com.ldfs.demo.util.HandleTask.TaskAction;

/**
 * 简易task执行体
 * 
 * @author momo
 * 
 * @param <T>
 */
public class SimpleTaskAction<T> implements TaskAction<T>, Callback {
	private Handler mHanlder;
	private final TaskAction<T> mAction;

	public SimpleTaskAction(TaskAction<T> action) {
		super();
		this.mAction = action;
		mHanlder = new Handler(Looper.getMainLooper(), this);
	}

	@Override
	public T run() {
		T t = null;
		if (null != mAction) {
			t = mAction.run();
		}
		return t;
	}

	@Override
	public void postRun(T t) {
		Message msg = mHanlder.obtainMessage();
		msg.obj = t;
		mHanlder.sendMessage(msg);
	}

	@Override
	public boolean handleMessage(Message msg) {
		T t = null;
		if (null != msg.obj) {
			t = (T) msg.obj;
		}
		// UI线程执行事件体
		mAction.postRun(t);
		return true;
	}

}
