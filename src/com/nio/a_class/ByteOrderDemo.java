package com.nio.a_class;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author y15079
 * @create 2017-11-09 20:03
 * @desc
 *
 * ByteOrder定义类写入buffer时字节的顺序
 * --java默认是big-endian
 * API
 * --2个内置的ByteOrder
 * ByteOrder.BIG_ENDIAN和ByteOrder.LITTLE_ENDIAN
 * --ByteOrder.nativeOrder()
 * 返回本地jvm运行的硬件的字节顺序，使用和硬件一致的字节顺序可能使buffer更加有效
 * --ByteOrder.toString()
 * 返回ByteOrder的名字，BIG_ENDIAN或LITTLE_ENDIAN
 *
 * https://www.cnblogs.com/bronte/articles/1965167.html
 *
 * 不大懂
 **/
public class ByteOrderDemo {
	public static void main(String[] args) {
		ByteBuffer buf=ByteBuffer.allocate(4);
		System.out.println("Default java endian: "+buf.order().toString());

		buf.putShort((short)1);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		System.out.println("Now: "+buf.order().toString());
		buf.putShort((short)2);

		buf.flip();
		for (int i=0;i<buf.limit();i++){
			System.out.println(buf.get()&0xFF);
		}
		System.out.println("My PC:"+ByteOrder.nativeOrder().toString());
	}
//	//结果
//Default java endian: BIG_ENDIAN
//	Now: LITTLE_ENDIAN
//0
//		1
//		2
//		0
//	My PC: LITTLE_ENDIAN
}
