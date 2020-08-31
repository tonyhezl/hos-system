package com.cdroho.utils;

import com.cdroho.websocket.ProductWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.CopyOnWriteArraySet;



public final class MySessionModel {
	private static Logger log = LoggerFactory.getLogger(MySessionModel.class);
	private static MySessionModel mySessionModel;

	/**
	 * 如果使用final修饰如：private final static CopyOnWriteArraySet<ProductWebSocket> webSocketSet;
	 * 则引用无法修改其值，释义：如果使用new关键字进行初始化对象，则其内的引用成员的地址无法修改，也就是无法修改其内的成员变量的值
	 */
	private static CopyOnWriteArraySet<ProductWebSocket> webSocketSet;

	private MySessionModel() {

	}

	synchronized public static MySessionModel getInstance() {
		if (null == mySessionModel) {
			try {
				// 模拟在创建对象之前做一些准备工作
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			mySessionModel = new MySessionModel();
		}
		return mySessionModel;
	}

	synchronized public static CopyOnWriteArraySet<ProductWebSocket> getCopyOnWriteArraySet() {
		if (null == webSocketSet) {
			try {
				// 模拟在创建对象之前做一些准备工作
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			webSocketSet = new CopyOnWriteArraySet<ProductWebSocket>();
		}
		return webSocketSet;
	}
}
