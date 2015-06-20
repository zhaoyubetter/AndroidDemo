package com.ldfs.demo.util;

/**
 * 循环列表
 * 
 * @author momo
 * 
 */
public class LoopList<E> {
	private Entry mFirst;
	private Entry mLast;
	private Entry mCurrentEntry;
	private int size;

	public LoopList() {
		super();
	}

	public void offer(E e) {
		Entry entry = null;
		if (null == mFirst) {
			entry = new Entry(e, null);
			mCurrentEntry = mFirst = mLast = entry;
		} else {
			entry = new Entry(e, mFirst);
			mLast.next = entry;
			mLast = entry;
		}
		size++;
	}

	public void peek() {
	}

	public E next() {
		E e = mCurrentEntry.e;
		mCurrentEntry = mCurrentEntry.next;
		return e;
	}

	public class Entry {
		private E e;
		public Entry next;

		public Entry(E e, Entry next) {
			super();
			this.e = e;
			this.next = next;
		}

	}

}
