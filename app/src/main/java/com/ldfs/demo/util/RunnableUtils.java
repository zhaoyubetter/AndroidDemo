package com.ldfs.demo.util;

import com.ldfs.demo.App;
import com.ldfs.demo.listener.RtnTask;

/**
 * 事件处理工具
 * 
 * @author momo
 * 
 */
public class RunnableUtils {

	public static <T> T runWiteTryCatch(RtnTask<T> task) {
		T t = null;
		if (!App.isDebug()) {
			try {
				if (null != task) {
					t = task.run();
				}
			} catch (Exception e) {
			}
		} else if (null != task) {
			t = task.run();
		}
		return t;
	}

	/**
	 * 在catch块中执行代码,确保程序不崩溃,无安全隐患
	 * 
	 * @param runnable
	 *            执行代码块
	 */
	public static void runWiteTryCatch(Runnable runnable) {
		if (!App.isDebug()) {
			try {
				if (null != runnable) {
					runnable.run();
				}
			} catch (Exception e) {
			}
		} else if (null != runnable) {
			runnable.run();
		}
	}

	/**
	 * 在catch块中执行代码,确保程序不崩溃,无安全隐患
	 * 
	 * @param runnable
	 *            执行代码块
	 * @param failAction
	 *            出现异常时执行代码块
	 */
	public static void runWiteTryCatch(Runnable runnable, Runnable failAction) {
		if (!App.isDebug()) {
			try {
				if (null != runnable) {
					runnable.run();
				}
			} catch (Exception e) {
				if (null != failAction) {
					failAction.run();
				}
			}
		} else if (null != runnable) {
			runnable.run();
		}
	}

	/**
	 * 子线程执行事件体
	 * 
	 * @param runnable
	 */
	public static void runWithExecutor(final Runnable runnable) {
		runWiteTryCatch(new Runnable() {
			@Override
			public void run() {
				new Thread(runnable).start();
			}
		});
	}
}
