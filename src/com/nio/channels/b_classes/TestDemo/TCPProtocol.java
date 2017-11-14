package com.nio.channels.b_classes.TestDemo;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * @author y15079
 * @create 2017-11-14 14:00
 * @desc
 *
 * TCPServerSelector与特定协议间通信
 *
 **/
public class TCPProtocol {

	private int bufferSize;

	public TCPProtocol(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	//接收一个SocketChannel的处理
	public void handleAccept(SelectionKey key)throws Exception{
		SocketChannel clientChannel=((ServerSocketChannel)key.channel()).accept();
		//设置非阻塞
		clientChannel.configureBlocking(false);
		clientChannel.register(key.selector(),SelectionKey.OP_READ, ByteBuffer.allocate(bufferSize));
	}

	//从一个SocketChannel读取信息的处理
	public void handleRead(SelectionKey key) throws Exception{
		//获得与客户端通信的信道
		SocketChannel clientChannel=(SocketChannel)key.channel();

		//得到并清空缓冲区
		ByteBuffer buffer=(ByteBuffer)key.attachment();
		buffer.clear();

		//读取信息获得读取的字节数
		long bytesRead=clientChannel.read(buffer);

		if (bytesRead==-1){
			//没有读取到内容的情况
			clientChannel.close();
		}else {
			//将缓冲区准备为数据传出状态
			buffer.flip();

			//将字节转化为UTF-16的字符串
			String receivedString= Charset.forName("UTF-16").newDecoder().decode(buffer).toString();

			//控制台打印出来
			System.out.println("接收到来自"+clientChannel.socket().getRemoteSocketAddress()+"的信息："+receivedString);

			//准备发送的文本
			String sendString="你好，客户端.@"+new Date().toString()+",已经收到你的信息，"+receivedString;
			buffer=ByteBuffer.wrap(sendString.getBytes("UTF-16"));
			clientChannel.write(buffer);

			//设置为下一次读取或是写入做准备
			key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		}
	}

	//向一个SocketChannel写入信息的处理
	public void handleWrite(SelectionKey key)throws Exception{
		//do nothing
	}

}
