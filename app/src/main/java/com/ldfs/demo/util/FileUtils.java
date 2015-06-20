package com.ldfs.demo.util;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * 文件帮助类
 * 
 * @author momo
 * @Date 2014/6/9
 */
public class FileUtils {
	public static final int KB = 1024;
	public static final long MB = 1024 * KB;
	public static final long GB = 1024 * MB;

	/**
	 * 检测SD卡否挂载
	 * 
	 * @return
	 */
	public static boolean isAvailable() {
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获得指定文件内,文件总大小
	 */
	public static long getFileLength(File file) {
		long length = 0;
		if (file.isDirectory()) {
			ScanListener<File> listener = new ScanListener<File>() {
				public long total;

				@Override
				public void scan(File f) {
					total += f.length();
				}

				@Override
				public Long result() {
					return total;
				}
			};
			scanFile(file, listener);
			length = listener.result();
		} else {
			length = file.length();
		}
		return length;
	}

	/**
	 * 将大小转为文件大小格式
	 * 
	 * @param length
	 * @return
	 */
	public static String FormetFileSize(long length) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = null;
		if (100 < length) {
			if (length < KB) {
				fileSizeString = df.format((double) length) + "B";
			} else if (length < MB) {
				fileSizeString = df.format((double) length / KB) + "K";
			} else if (length < GB) {
				fileSizeString = df.format((double) length / MB) + "M";
			} else {
				fileSizeString = df.format((double) length / GB) + "G";
			}
		}
		return fileSizeString;
	}

	/**
	 * 遍历文件操作
	 * 
	 * @param file
	 * @param listener
	 */
	public static void scanFile(File file, ScanListener<File> listener) {
		LinkedList<File> files = new LinkedList<File>();
		files.add(file);
		while (files.size() > 0) {
			File f = files.removeFirst();
			if (f.isDirectory()) {
				File[] listFiles = f.listFiles();
				if (null != listFiles && 0 < listFiles.length) {
					files.addAll(Arrays.asList(listFiles));
				}
			} else if (null != listener) {
				listener.scan(f);
			}
		}
		if (null != listener) {
			listener.result();
		}
	}

	/**
	 * 清空目录内所有文件
	 */
	public static void clearFile(File file) {
		File[] listFiles = null;
		if (file.exists()) {
			listFiles = file.listFiles();
			if (null != listFiles && 0 < listFiles.length) {
				int length = listFiles.length;
				for (int i = 0; i < length; i++) {
					listFiles[i].delete();
				}
			}
		}
	}

	/**
	 * 从asset内读取文件内容
	 * 
	 * @param resource
	 * @param fileName
	 * @return
	 */
	public static String getContentFromAssets(Resources resource, String fileName) {
		String result = new String();
		BufferedReader bufReader = null;
		try {
			bufReader = new BufferedReader(new InputStreamReader(resource.getAssets().open(fileName)));
			String line = new String();
			while ((line = bufReader.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			result = new String();
		} finally {
			IOUtils.closeStream(bufReader);
		}
		return result;
	}

	/**
	 * 从asset内读取文件内容
	 * 
	 * @param localFile
	 * @return
	 */
	public static String getContentFromFile(File localFile) {
		String result = new String();
		BufferedReader bufReader = null;
		try {
			bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(localFile)));
			String line = new String();
			while ((line = bufReader.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			result = new String();
		} finally {
			IOUtils.closeStream(bufReader);
		}
		return result;
	}

	/**
	 * 拷贝css样式到sd卡
	 */
	public static void copyFileFromAsset(Context context, String assetFileName, File toFile) {
		InputStream open = null;
		FileWriter writer = null;
		try {
			open = context.getAssets().open(assetFileName);
			if (!toFile.exists() && null != open) {
				writer = new FileWriter(toFile);
				byte[] bys = new byte[1024];
				int count = -1;
				while (-1 != (count = open.read(bys))) {
					writer.write(new String(bys, 0, count));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeStream(open);
			IOUtils.closeStream(writer);
		}
	}

	/**
	 * 子线程内,忽略异常清空WEBVIEW缓存
	 * 
	 * @param context
	 */
	public static void clearWebViewCache(final Context context) {
		if (null != context) {
			RunnableUtils.runWithExecutor(new Runnable() {
				@Override
				public void run() {
					// 删除数据库
					context.deleteDatabase("webview.db");
					context.deleteDatabase("webviewCache.db");
					// WebView 缓存文件
					File appCacheDir = new File(context.getFilesDir().getAbsolutePath() + "/webcache");
					File webviewCacheDir = new File(context.getCacheDir().getAbsolutePath() + "/webviewCache");
					// 删除webview 缓存目录
					if (webviewCacheDir.exists()) {
						deleteFile(webviewCacheDir);
					}
					// 删除webview 缓存 缓存目录
					if (appCacheDir.exists()) {
						deleteFile(appCacheDir);
					}
				}
			});
		}
	}

	/**
	 * 删除文件或文件夹内所有文件
	 * 
	 * @param file
	 */
	public static void deleteFile(File file) {
		if (null != file && file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File[] files = file.listFiles();
				if (null != files) {
					for (int i = 0; i < files.length; i++) {
						deleteFile(files[i]);
					}
				}
			}
		}
	}

	/**
	 * 扫措监听
	 * 
	 * @param <E>
	 */
	public interface ScanListener<E> {
		void scan(E e);

		<T> T result();
	}
}
