package com.nio.channels.b_classes;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author y15079
 * @create 2017-11-13 13:41
 * @desc
 *
 * Selector（选择器）是Java NIO中能够检测一到多个NIO通道，并能够知晓通道是否为诸如读写事件做好准备的组件。
 * 这样，一个单独的线程可以管理多个channel，从而管理多个网络连接。
 *
 * 主要方法：
 * 			1. open 打开一个选择器
 * 			2. selectedKeys 返回此选择器的已选择键集
 * 		    3. select 选择一组键，其相应的通道已为IO操作准备就绪
 * 		    4. close 关闭此选择器
 *
 *  Selector类可用于避免使用非阻塞式客户端中很浪费资源的“忙等”方法。例如，考虑一个即时消息发送器。
 *  可能有上千个客户端同时连接到了服务器，但在任何时刻都只有非常少量的消息需要读取和分发。
 *  这就需要一种方法阻塞等待，直到至少有一个信道可以进行I/O操作，并指出是哪个信道。NIO的Selector就
 *  实现了这个功能。一个Selector实例可以同时检查一组信道的I/O状态。
 *
 *  那么如何使用Selector来监听？首先需要创建一个Selector实例（使用静态工厂方法open()）并将其注册
 *  （一个信道可以注册多个Selector实例）到想要监听的信道上。
 *
 * http://blog.csdn.net/coder_py/article/details/69257783
 *
 * http://blog.csdn.net/u010536377/article/details/41843463
 *
 * 向Selector注册通道
 * 为了将Channel和Selector配合使用，必须将channel注册到selector上。
 * 通过SelectableChannel.register()方法来实现。
 * channel.configureBlocking(false);
   SelectionKey key = channel.register(selector, SelectionKey.OP_READ)
 *
 * 与Selector一起使用时，Channel必须处于非阻塞模式下。
 * 这意味着FileChannel与Selector不能一起使用。
 *
 * 通道触发了一个事件意思是该事件已经就绪。所以，某个channel成功连接到另一个服务器称为“连接就绪”。
 * 一个server socket channel准备好接收新进入的连接称为“接收就绪”。
 * 一个有数据可读的通道可以说是“读就绪”。等待写数据的通道可以说是“写就绪”。
 *  SelectionKey.OP_CONNECT
	SelectionKey.OP_ACCEPT
    SelectionKey.OP_READ
    SelectionKey.OP_WRITE
 *
 * register()返回值——SelectionKey, Selector中的SelectionKey集合
 * 只要ServerSocketChannel及SocketChannel向Selector注册了特定的事件，Selector就会监控这些事件是否发生。
 *
 * SelectableChannel的register()方法返回一个SelectionKey对象，该对象是用于跟踪这些被注册事件的句柄。
 * 一个Selector对象会包含3中类型的SelectionKey集合：
 *   all-keys集合 —— 当前所有向Selector注册的SelectionKey的集合，Selector的keys()方法返回该集合
	 selected-keys集合 —— 相关事件已经被Selector捕获的SelectionKey的集合，Selector的selectedKeys()方法返回该集合
	 cancelled-keys集合 —— 已经被取消的SelectionKey的集合，Selector没有提供访问这种集合的方法
 * 当register()方法执行时，新建一个SelectioKey，并把它加入Selector的all-keys集合中。
 *
 * 在执行Selector的select()方法时，如果与SelectionKey相关的事件发生了，这个SelectionKey就被加入到Selected-keys集合中，
 * 程序直接调用selected-keys集合的remove()方法，或者调用它的iterator的remove()方法，都可以从selected-keys集合中删除一个SelectionKey对象。
 *
 * http://blog.csdn.net/robinjwong/article/details/41792623
 *
 *
 **/
public class SelectorDemo {

	public static void main(String[] args) throws Exception {

	}

	public static void test01() throws Exception{
		Selector selector=Selector.open();
		DatagramChannel channel=DatagramChannel.open();
		channel.configureBlocking(false);
		channel.socket().bind(new InetSocketAddress(80));//绑定端口
		channel.register(selector, SelectionKey.OP_READ);//注册
		//最后，调用选择器上的select方法。
		int num=selector.select();//获取可进行IO操作的信道数量。
		//如果在一个单独的线程中，通过调用select() 方法就能检查多个信道是否准备IO操作。
		//如果经过一段时间后任然没有信道准备好，则返回0，并允许程序继续执行其他任务。

		//那么如何在信道上对"感兴趣的"IO操作进行监听？
		//Selector与Channel之间的关联由一个SelectionKey实例表示。SelectionKey维护了一个信道上感兴趣的操作类型信息，
		//并将这些信息存放在一个int型的位图中，该int型数据的每一位都有相应的含义。
	}

	//服务端的实现
	public static void UdpServer() throws Exception{
		int servPort=999;
		Selector selector=Selector.open();//打开一个选择器
		DatagramChannel datagramChannel=DatagramChannel.open();
		datagramChannel.configureBlocking(false);
		datagramChannel.socket().bind(new InetSocketAddress(servPort));
		datagramChannel.register(selector,SelectionKey.OP_READ);
		//channel.register(selector,1)；与上句子同效果
		while (true){
			int num=selector.select();
			if (num==0){
				continue;
			}
			Iterator<SelectionKey> keys=selector.selectedKeys().iterator();
			while (keys.hasNext()){
				SelectionKey k=keys.next();

				if ((k.readyOps()&SelectionKey.OP_READ)== SelectionKey.OP_READ){
					DatagramChannel cc=(DatagramChannel) k.channel();
					//非阻塞
					cc.configureBlocking(false);
					ByteBuffer buffer=ByteBuffer.allocate(255);
					//接收数据并读到buffer中
					buffer.clear();
					datagramChannel.receive(buffer);
					buffer.flip();
					byte[] b=new byte[buffer.remaining()];
					for (int i=0;i<buffer.remaining();i++){
						b[i]=buffer.get(i);
					}
					Charset charset=Charset.forName("UTF-8");
					CharsetDecoder decoder=charset.newDecoder();
					CharBuffer charBuffer=decoder.decode(buffer);
					System.out.println("The imformation recevied:"+charBuffer.toString());
					keys.remove(); //一定要remove
				}
			}
		}
	}

	public static void UdpClient() throws Exception{
		String s=null;
		while ((s=new Scanner(System.in).nextLine())!=null){
			DatagramChannel dc=null;
			dc=DatagramChannel.open();
			SocketAddress address=new InetSocketAddress("localhost",999);
			ByteBuffer bb=ByteBuffer.allocate(255);
			byte[] b=new byte[130];
			bb.clear();
			bb.put(b);
			bb.flip();
			dc.send(bb,address);
		}
	}
}
