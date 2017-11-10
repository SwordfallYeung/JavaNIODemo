package com.nio.channels.b_classes;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @author y15079
 * @create 2017-11-10 16:38
 * @desc
 *
 * 文件IO
 * FileChannel是处理文件IO中的核心Channel实现。FileChannel 不能直接构建，只能通过FileInputStream，FileOutputStream，RandomAccessFile通过
 * getChannel()获得FileChannel实例对象，获得的FileChannel对象的权限也由相关的File对象所限制，也就说FileInputStream所获得的Channel对象只能使用read(),
 * 如果使用write()，则会相关异常。
 *
 * FileChannel是线程安全的，多个现在可以共享一个FileChannel实例并调用read，write而不会产生多线程问题。
 *
 * 对于文件IO，最大的意思是异步IO，它可以使得一个线程处理多个IO的时候，可以不必等待IO的完成，而是使用异步的模式
 *
 * Java NIO（新IO）与Java传统IO（即IO流）之间最大的区别在于，NIO提供了一套异步IO解决方案，其目的在于使用单线程来监控多路异步IO，使得IO效率，尤其是服务器端的IO效率
 * 得到大幅提高。为了实现这一套异步IO解决方案，NIO引入了三个概念，即缓冲区(Buffer)、通道（Channel）和选择器（Selector）
 *
 * Java的传统IO只有阻塞模式，但Java NIO却提供了阻塞和非阻塞两种IO模式，这两种模式就是通过通道来体现的。
 *
 * https://zhuanlan.zhihu.com/p/25914350
 **/
public class ChannelsDemo {
	public static void main(String[] args) {
		test();
	}

	/**
	 * 创建两个通道，一个从System.in读入字节，另一个将字节写入System.out
	 */
	public static void test(){
		ReadableByteChannel readableByteChannel= Channels.newChannel(System.in);
		WritableByteChannel writableByteChannel= Channels.newChannel(System.out);
		ByteBuffer buffer=ByteBuffer.allocate(1024);

		try {
			while (readableByteChannel.read(buffer)!=-1){
				buffer.flip();
				while (buffer.hasRemaining()){
					writableByteChannel.write(buffer);
				}
				buffer.clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
