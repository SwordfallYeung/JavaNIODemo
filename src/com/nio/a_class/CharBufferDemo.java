package com.nio.a_class;

import java.nio.ByteOrder;
import java.nio.CharBuffer;

/**
 * @author y15079
 * @create 2017-11-10 10:33
 * @desc
 *
 * 和ByteBuffer类似，
 *
 * 缓冲区是由分配（allocate）或包装（wrap）操作创建的。分配（allocate）操作创建一个缓冲区对象并分配一个私有的空间来储存容量大小的数据元素。
 * 包装（wrap）操作创建一个缓冲区对象但是不分配任何空间来存储数据元素，它使用你提供的数组作为存储空间来储存缓冲区中的数据元素
 *
 **/
public class CharBufferDemo {
	public static void main(String[] args) {

	}

	public static void test01(){
		// 这段代码隐含地从堆空间中分配了一个 char 型数组作为备份存储器来储存 100 个 char 变量。
		CharBuffer charBuffer=CharBuffer.allocate(100);

		char[] myArray=new char[100];
		CharBuffer charBuffer1=CharBuffer.wrap(myArray);

		CharBuffer charBuffer2=CharBuffer.wrap(myArray,12,42);

	}

	public static void test02(){
		//复制缓冲区
		CharBuffer buffer=CharBuffer.allocate(8);
		buffer.position(3).limit(6).mark().position(5);
		CharBuffer dupeBuffer=buffer.duplicate();
		buffer.clear();
	}

	public static void test03(){
		//分割缓冲区与复制相似，但 slice() 创建一个从原始缓冲区的当前 position 开始的新缓冲区，
		// 并且其容量是原始缓冲区的剩余元素数量（limit - position）。这个新缓冲区与原始缓冲区共享一段数据元素子序列。
		// 分割出来的缓冲区也会继承只读和直接属性。
		CharBuffer buffer=CharBuffer.allocate(8);
		buffer.position(3).limit(5);
		CharBuffer sliceBuffer=buffer.slice();
	}
}
