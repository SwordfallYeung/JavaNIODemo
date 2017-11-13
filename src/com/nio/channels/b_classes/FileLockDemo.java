package com.nio.channels.b_classes;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author y15079
 * @create 2017-11-10 19:37
 * @desc
 *
 *  2.4 文件锁定
 * 	  一、Java引进了文件锁机制，用来在进程之间进行文件的共享与独占锁定。文件锁定是在进程之间进行的，一个进程的多个线程之间，文件锁定无效；
 * 	  二、锁定分为共享锁与独占锁，
 *
 **/
public class FileLockDemo {

	private static String filepath="resources/a.txt";
	private static Random random=new Random();

	public static void main(String[] args) {

	}
	public static void lockAndWrite(FileChannel channel){
		try {
			ByteBuffer buffer=ByteBuffer.allocate(4);
			int i=0;
			while (true){
				System.out.println("Writer try to lock file...");
				FileLock lock=channel.lock(0,4,false);

				buffer.putInt(0,i);
				buffer.position(0).limit(4);
				System.out.println("buffer is :"+buffer);
				channel.write(buffer,0);
				channel.force(true);
				buffer.clear();
				System.out.println("Writer write :"+i++);

				lock.release();
				System.out.println("Sleeping...");
				TimeUnit.SECONDS.sleep(random.nextInt(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void lockAndRead(FileChannel channel){
		try {
			ByteBuffer buffer=ByteBuffer.allocate(4);
			while (true){
				System.out.println("Reader try to lock file...");
				FileLock lock=channel.lock(0,4,true);

				buffer.clear();
				channel.read(buffer,0);
				buffer.flip();
				System.out.println("buffer is :"+buffer);
				int i=buffer.getInt(0);
				System.out.println("Reader read : "+i);

				lock.release();
				System.out.println("Sleeping...");
				TimeUnit.SECONDS.sleep(random.nextInt(3));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
