package com.nio.channels.b_classes.TestDemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @author y15079
 * @create 2017-11-14 13:44
 * @desc
 *
 * TCP SocketChannel与ServerSocketChannel
 *
 **/
public class TCPClientDemo {
	//信道选择器
	private Selector selector;

	//与服务器通信的信道
	SocketChannel socketChannel;

	//要连接的服务器Ip地址
	private String hostIp;

	//要连接的远程服务器在监听的端口
	private int hostListenningPort;

	public TCPClientDemo(String hostIp, int hostListenningPort) throws Exception{
		this.hostIp = hostIp;
		this.hostListenningPort = hostListenningPort;

		initialize();
	}

	/**
	 * 初始化
	 * @throws Exception
	 */
	private void initialize() throws Exception{
		//打开监听信道并设置为非阻塞模式
		socketChannel=SocketChannel.open(new InetSocketAddress(hostIp,hostListenningPort));
		socketChannel.configureBlocking(false);

		//打开并注册选择器到信道
		selector=Selector.open();
		socketChannel.register(selector, SelectionKey.OP_READ);

		//启动读取线程
		new TCPClientReadThread(selector);
	}

	/**
	 * 发送字符串到服务器
	 * @param message
	 * @throws Exception
	 */
	public void sendMsg(String message) throws Exception{
		ByteBuffer writeBuffer=ByteBuffer.wrap(message.getBytes("UTF-16"));
		socketChannel.write(writeBuffer);
	}

	public static void main(String[] args) throws Exception{
		TCPClientDemo clientDemo=new TCPClientDemo("127.0.0.1",1978);
		clientDemo.sendMsg("你好！Nio! 醉里挑灯看剑，梦回吹角连营");
	}


	public class TCPClientReadThread implements Runnable{
		private Selector selector;

		public TCPClientReadThread(Selector selector) {
			this.selector = selector;

			new Thread(this).start();
		}

		@Override
		public void run() {
			try {
				while (selector.select()>0){
					//遍历每个有可用IO操作Channel对应的SelectionKey
					for (SelectionKey sk:selector.selectedKeys()){

						//如果该SelectionKey对应的Channel中有可读的数据
						if (sk.isReadable()){
							//使用NIO读取Channel中的数据
							SocketChannel sc=(SocketChannel)sk.channel();
							ByteBuffer buffer=ByteBuffer.allocate(1024);
							sc.read(buffer);
							buffer.flip();

							//将字节转化为UTF-16的字符串
							String receivedString= Charset.forName("UTF-16").newDecoder().decode(buffer).toString();

							//控制台打印出来
							System.out.println("接收到来自服务器"+sc.socket().getRemoteSocketAddress()+"的信息："+receivedString);

							//为下一次读取作准备
							sk.interestOps(SelectionKey.OP_READ);
						}

						//删除正在处理的SelectionKey
						selector.selectedKeys().remove(sk);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
