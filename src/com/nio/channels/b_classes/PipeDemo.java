package com.nio.channels.b_classes;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * @author y15079
 * @create 2017-11-13 11:47
 * @desc
 *
 * Pipe就是个空管子，这个空管子一头可以从管子里往外读，一头可以往管子里写，并且是FIFO结构操作流程：
 * 1. 首先要有一个对象往这个空管子里面写。这个空管子是有一点空间的，就在这个管子里。管子包含的空间是
 *    1024个字节。
 * 2. 然后另一个对象才能将这个装满了的管子里的内容读出来。
 *
 * Pipe类实现一个管道范例，不过它所创建的管道是进程内（Java虚拟机进程内部）而非进程间使用的，同一进程内的不同线程间通信。
 *
 * Pipe类定义了两个嵌套的通道类来实现管路。这两个类是Pipe.SourceChannel（管道负责读的一端）和Pipe.SinkChannel（管道负责写的一端）。
 * 这两个通道实例是在Pipe对象创建的同时被创建的，可以通过在Pipe对象上分别调用source( )和sink( )方法来取回。
 *
 * 管道可以被用来仅在同一个Java虚拟机内部传输数据。虽然有更加有效率的方式来在线程之间传输数据，但是使用管道的好处在于封装性。生产者线程和用户线程都能被写道通用的Channel API中。
 *
 * http://blog.csdn.net/will_awoke/article/details/26453085
 *
 **/
public class PipeDemo {
	public static void main(String[] args) throws Exception{
			test();
	}

	public static void test() throws Exception{
		//创建一个管道
		Pipe pipe=Pipe.open();
		final Pipe.SinkChannel psic=pipe.sink();
		final Pipe.SourceChannel psoc=pipe.source();

		Thread tPwriter=new Thread(){
			@Override
			public void run() {
				try {
					//创建一个线程，利用管道的写入口Pipe.SinkChannel类型的psic往管道里写入指定ByteBuffer的内容
					psic.write(ByteBuffer.wrap("Hello,Pipe!".getBytes("utf-16BE")));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		Thread tPreader=new Thread(){
			@Override
			public void run() {
				int bufferSize=1024*2;
				ByteBuffer buffer=ByteBuffer.allocate(bufferSize);
				try {
					//创建一个线程，利用管道的读入口Pipe.SourceChannel类型psoc将管道里的内容读到指定的ByteBuffer中
					psoc.read(buffer);
					System.out.println("Content: "+buffer);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		tPwriter.start();
		tPreader.start();
	}
}
