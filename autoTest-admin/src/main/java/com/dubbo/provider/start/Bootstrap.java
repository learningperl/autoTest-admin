package com.dubbo.provider.start;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.java.statics.Mysql;
import com.java.statics.property;
public class Bootstrap {

	public static void main(String[] args) throws Exception {
		// 启动spring容器，把服务器启动之后注册到Zookeeper
		System.out.println("开始启动服务...");
		Run();
	}
	
	public static void Run() {

		new Thread(new Runnable() {// 启动数据获取，更新显示进程

					public void run() {

						new property();
						new Mysql();
						ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "provider.xml" });
						context.start();
						System.out.println("服务启动成功...");
						while(true){
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
							}
						}

					}

				}).start(); //

	}
}
