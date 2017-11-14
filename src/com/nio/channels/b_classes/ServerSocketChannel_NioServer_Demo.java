package com.nio.channels.b_classes;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author y15079
 * @create 2017-11-13 16:51
 * @desc
 *
 * 抽象类
 *
 * ServerSocketChannel与SocketChannel通常联合使用，用于作服务端
 *
 *  *  抽象类 SocketChannel
 *
 * Server端 看不懂
 * Java NIO是非阻塞IO的实现，基于事件驱动，非常适用于服务器需要维持大量连接，但是数据交换量不大的情况，
 * 例如一些即时通信的服务等等，它主要有三个部分组成：
 *    Channels
 *    Buffers
 *    Selectors
 *
 * Channel 有两种ServerSocketChannel和SocketChannel，ServerSocketChannel可以监听新加入的Socket连接，SocketChannel用于读和写操作。
 * NIO总是把缓冲区的数据写入通道，或者把通道里的数据读出到缓冲区。
 *
 * Buffer本质上是一块用于读写的内存，只是被包装成了buffer对象，你可以通过allocateDirect()或者allocate()申请内存空间，
 * Buffer尤其需要理解三个概念，capacity、position、limit，
 * capacity是固定大小，position是当前读写位置，limit是一个类似于门限的值，用于控制读写的最大的位置。Buffer的常用方法有clear、compact、flip等等，
 * 还有比如Buffer的静态方法wrap等等。
 *
 * Selector用于检测通道，我们通过它才知道哪个通道发生了哪个事件，所以如果需要用selector的话就需要首先进行register，然后遍历SelectionKey对事件进行处理。
 * 它一共有SelectionKey.OP_CONNECT、SelectionKey.OP_ACCEPT、SelectionKey.OP_READ、SelectionKey.OP_WRITE四种类型
 *
 * 服务端
 *
 **/
public class ServerSocketChannel_NioServer_Demo {
	//通道管理器
	private Selector selector;

	//获取一个ServerSocket通道，并初始化通道
	public ServerSocketChannel_NioServer_Demo init(int port) throws Exception{
		//获取一个ServerSocket通道，打开监听信道
		ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
		//设置为非阻塞模式
		serverSocketChannel.configureBlocking(false);
		//与本地端口绑定
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		//获取通道管理器，创建选择器
		selector=Selector.open();
		//将通道管理器与通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件，
		//只有当该事件到达时，Selector.select()会返回，否则一直阻塞。
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		return this;
	}

	public void listen() throws Exception{
		System.out.println("服务器端启动成功");

		//使用轮询访问selector
		while (true){
			//当有注册的事件到达时，方法返回，否则阻塞。
			selector.select();

			//获取selector中的迭代器，选中项为注册的事件
			Iterator<SelectionKey> ite=selector.selectedKeys().iterator();

			while (ite.hasNext()){
				SelectionKey key=ite.next();
				//删除已选key，防止重复处理
				ite.remove();
				//客户端请求连接事件
				if (key.isAcceptable()){
					ServerSocketChannel server=(ServerSocketChannel) key.channel();
					//获得客户端连接通道
					SocketChannel channel=server.accept();
					//设置为非阻塞模式
					channel.configureBlocking(false);
					//向客户端发消息
					channel.write(ByteBuffer.wrap(new String("send message to client").getBytes()));
					//在与客户端连接成功后，为客户端通道注册SelectionKey.OP_READ事件。
					channel.register(selector,SelectionKey.OP_READ);

					System.out.println("客户端请求连接事件");
				}else if (key.isReadable()){
					//有可读数据事件
					//获取客户端传输数据可读消息通道。
					SocketChannel channel=(SocketChannel)key.channel();
					//创建读取数据缓冲器
					ByteBuffer buffer=ByteBuffer.allocate(40);
					int read=channel.read(buffer);
					byte[] data=buffer.array();
					String message=new String(data);

					System.out.println("receive message from client, size:"+ buffer.position()+"msg: "+message);

				}
			}
		}
	}

	public static void main(String[] args) throws Exception{
		new ServerSocketChannel_NioServer_Demo().init(9981).listen();
	}
}
