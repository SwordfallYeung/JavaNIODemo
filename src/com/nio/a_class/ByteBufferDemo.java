package com.nio.a_class;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * @author y15079
 * @create 2017-11-09 15:18
 * @desc
 *
 * 在buffer的子类中，ByteBuffer是一个地位较为特殊的类，因为在java.io.channels中定义的各种channel的IO操作基本上都是围绕ByteBuffer展开的。
 * ByteBuffer定义类4个static方法来做创建工作：
 *    1.allocate(int capacity) //创建一个指定capacity的ByteBuffer.
 *    2.allocateDirect(int capacity)//创建一个direct的ByteBuffer，这样的ByteBuffer在参与IO操作时性能会更好
 *    wrap(byte[] array)
 *    wrap(byte[] array,int offset,int length)//把一个byte数组或byte数组的一部分包装成byteBuffer.
 *
 * 	  ByteBuffer 并且这三个指针的关系是position<=limit<= capacity.
 * 	  position是当前读写的位置。
 * 	  limit是最大能读写的位置
 * 	  capacity是缓存的容量。
 *
 *    ByteBuffer定义一系列get和put操作来从中读写byte数据。
 *
// capacicty:作为一个内存块，Buffer有一个固定的大小值，也叫“capacity”.
// 你只能往里写capacity个byte、long，char等类型。一旦Buffer满了，需要将其清空（通过读数据或者清除数据）才能继续写数据往里写数据。

// position
// 当你写数据到Buffer中时，position表示当前的位置。初始的position值为0.当一个byte、long等数据写到Buffer后，
// position会向前移动到下一个可插入数据的Buffer单元。position最大可为capacity – 1.
// 当读取数据时，也是从某个特定位置读。当将Buffer从写模式切换到读模式，position会被重置为0.
// 当从Buffer的position处读取数据时，position向前移动到下一个可读的位置。

// limit
// 在写模式下，Buffer的limit表示你最多能往Buffer里写多少数据。 写模式下，limit等于Buffer的capacity。
// 当切换Buffer到读模式时，
// limit表示你最多能读到多少数据。因此，当切换Buffer到读模式时，limit会被设置成写模式下的position值。
// 换句话说，你能读到之前写入的所有数据（limit被设置成已写数据的数量，这个值在写模式下就是position）

// flip
// flip方法将Buffer从写模式切换到读模式。调用flip()方法会将position设回0，并将limit设置成之前position的值。
// 换句话说，position现在用于标记读的位置，limit表示之前写进了多少个byte、char等 —— 现在能读取多少个byte、char等。

// rewind
// 将position设回0，所以你可以重读Buffer中的所有数据。limit保持不变，仍然表示能从Buffer中读取多少个元素（byte、char等）。

// 一旦读完Buffer中的数据，需要让Buffer准备好再次被写入。可以通过clear()或compact()方法来完成。
// clear
// 如果调用的是clear()方法，position将被设回0，limit被设置成 capacity的值。换句话说，Buffer
// 被清空了。Buffer中的数据并未清除，只是这些标记告诉我们可以从哪里开始往Buffer里写数据。
// 如果Buffer中有一些未读的数据，调用clear()方法，数据将“被遗忘”，意味着不再有任何标记会告诉你哪些数据被读过，哪些还没有。
// compact
// 如果Buffer中仍有未读的数据，且后续还需要这些数据，但是此时想要先先写些数据，那么使用compact()方法。
// compact()方法将所有未读的数据拷贝到Buffer起始处。然后将position设到最后一个未读元素正后面。limit属性依然像clear()方法一样，设置成capacity。现在Buffer准备好写数据了，但是不会覆盖未读的数据。
 *
 * http://blog.csdn.net/earbao/article/details/48028459
 **/
public class ByteBufferDemo {
	public static void main(String[] args) throws Exception{
//		testFromSystem();
//		test02();
//		test03();
		test01();
	}

	public static void testFromSystem()throws Exception{
		//创建一个capacity为256的ByteBuffer
		ByteBuffer byteBuffer=ByteBuffer.allocate(256);
		while (true){
			//从标准输入流读入一个字符
			int c=System.in.read();
			//当读到输入流结束时，退出循环
			if(c==-1){
				break;
			}
			//把读入的字符写入ByteBuffer中
			byteBuffer.put((byte)c);
			//当读完一行时，输出收集的字符
			if (c=='\n'){
				//调用flip()使limit变为当前的position的值，position变为0，
				//为接下来从ByteBuffer读取做准备
				byteBuffer.flip();
				//构建一个byte数组
				byte[] content=new byte[byteBuffer.limit()];
				//从ByteBuffer中读取数据到byte数组中
				byteBuffer.get(content);
				//把byte数组的内容写到标准输出
				System.out.print(new String(content));
				//调用clear()使position变为0,limit变为capacity的值，
				//为接下来写入数据到ByteBuffer中做准备
				byteBuffer.clear();
			}
		}
	}

	public static void test02(){
		ByteBuffer buffer=ByteBuffer.allocate(16);
		System.out.println("ByteBuffer :");
		System.out.println("capacity:"+buffer.capacity());
		buffer.put(new byte[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15});
		System.out.println("put byte[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15} into buffer.");
		System.out.println("limit:"+buffer.limit());
		System.out.println("position:"+buffer.position());
		buffer.flip();//数据由写转为读取
		System.out.println("ByteBuffer 执行flip，转为读取");
		byte[] dst=new byte[10];
		buffer.get(dst,0,dst.length);
		System.out.println(String.format("byte[]:%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", dst[0], dst[1], dst[2],
				dst[3], dst[4], dst[5], dst[6], dst[7], dst[8], dst[9]));
		System.out.println("读取完10个字节的数据后：");
		System.out.println("limit:"+buffer.limit());
		System.out.println("position:"+buffer.position());
		buffer.rewind();//这什么 重新读取数据
		System.out.println("执行rewind，重新读取数据");
		System.out.println("limit:"+buffer.limit());
		System.out.println("position:"+buffer.position());
		byte[] dt=new byte[10];
		buffer.get(dt,0,dst.length);
		System.out.println(String.format("byte[]:%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", dt[0], dt[1], dt[2],
				dt[3], dt[4], dt[5], dt[6], dt[7], dt[8], dt[9]));
		System.out.println("读取完10个字节的数据后：");
		System.out.println("limit:"+buffer.limit());
		System.out.println("position:"+buffer.position());
		System.out.println("在当前位置做标记mark");
		buffer.mark();
		buffer.get();
		buffer.get();
		buffer.get();
		System.out.println("读取3个字节后position:"+buffer.position());

		buffer.reset();

		System.out.println("执行reset后position的位置："+buffer.position());

		buffer.compact();
		System.out.println("取出10个字节后，执行完compact后ByteBuffer第一个字节");
	}

	private static void test03(){
		ByteBuffer buffer=ByteBuffer.allocate(5);
		System.out.println("position初始化："+buffer.position());
		System.out.println("limit初始化："+buffer.limit());
		System.out.println("capacity初始化："+buffer.capacity());

		System.out.println();
		buffer.put((byte)1);

		System.out.println("放入1个字节，position："+buffer.position());
		System.out.println("放入1个字节，limit:"+buffer.limit());
		System.out.println("放入1个字节，capacity:"+buffer.capacity());

		System.out.println();
		buffer.flip();//写转为读，并把position设置为0

		System.out.println("flip之后，position："+buffer.position());
		System.out.println("flip之后，limit："+buffer.limit());
		System.out.println("flip之后，capacity："+buffer.capacity());

		System.out.println();
		buffer.get();

		System.out.println("拿出一个字节，position："+buffer.position());
		System.out.println("拿出一个字节，limit："+buffer.limit());
		System.out.println("拿出一个字节，capacity："+buffer.capacity());
	}

	private static void test01(){
		String str="helloworld";
		ByteBuffer buffer=ByteBuffer.wrap(str.getBytes());//wrap是什么，Buffer大小为当前输入的字节数大小
		System.out.println("position:"+buffer.position()+"\t limit:"+buffer.limit());
		//读取两个字节
		buffer.get();
		buffer.get();
		System.out.println("两个get() position:"+(char)buffer.get(buffer.position())+"\t limit:"+buffer.limit());
		buffer.mark();
		System.out.println("mark() position:"+buffer.position()+"\t limit:"+buffer.limit());
		buffer.flip();//读转为写，position为0
		System.out.println("flip() position:"+buffer.position()+"\t limit:"+buffer.limit());

	}


	public static byte[] getBytes (char[] chars) {//将字符转为字节(编码)
		Charset cs = Charset.forName ("UTF-8");
		CharBuffer cb = CharBuffer.allocate (chars.length);

		cb.put (chars);
		cb.flip ();
		ByteBuffer bb = cs.encode (cb);
		return bb.array();
	}

	public static char[] getChars(byte[] bytes) {// 将字节转为字符(解码)
		Charset cs = Charset.forName("UTF-8");
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes);
		bb.flip();
		CharBuffer cb = cs.decode(bb);
		return cb.array();
	}
}
