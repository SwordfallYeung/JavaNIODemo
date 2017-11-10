package com.nio.channels.b_classes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author y15079
 * @create 2017-11-10 17:21
 * @desc
 *
 * 2 文件通道
 *   通道主要分为文件通道和Socket通道，由于文件通道只能处于阻塞模式，较为简单，因此先介绍文件通道
 *
 *   2.1 文件通道的创建
 *   再说一遍，文件通道总是处于阻塞模式。创建文件通道最常用的三个类是FileInputStream、FileOutputStream和RandomAccessFile,
 *   它们均提供类一个getChannel()方法，用来获取与之关联的通道。
 *   对于文件通道来说，FileInputStream创建的通道只能读，FileOutputStream创建的通道只能写，而RandomAccessFile可以创建同时具有读写功能的通道
 *   （使用"rw"参数创建）。
 *
 *    2.2 文件通道的position和文件空洞
 *    当创建了一个文件通道后，文件通道和文件流对象（FileInputStream，FileOutputStream和RandomAccessFile）共享此文件的position。文件流对象和文件通道
 *    的大部分读写操作（直接位置的读写操作不会造成position的位移）均会造成position的自动位移，这个位移对于两类对象来说是共享的。
 *
 *
 *
 * https://zhuanlan.zhihu.com/p/25914350
 *
 **/
public class FileChannelDemo {
	public static void main(String[] args) {

	}

	private static void testChannelCreate() throws Exception{
		final String filepath="resources/a.txt";

		RandomAccessFile randomAccessFile=new RandomAccessFile(filepath,"rw");
		FileChannel readAndWriteChannel=randomAccessFile.getChannel();
		FileInputStream fis=new FileInputStream(filepath);
		FileChannel readChannel=fis.getChannel();
		FileOutputStream fos=new FileOutputStream(filepath);
		FileChannel writeChannel=fos.getChannel();

		readAndWriteChannel.close();
		readChannel.close();
		writeChannel.close();
	}

	private static void testFilePosition(){
		final String filepath="resources/a.txt";

		try {
			//create a file with 26 char
			FileOutputStream fos=new FileOutputStream(filepath);
			StringBuilder sb=new StringBuilder();
			for (char c='a';c <= 'z'; c++){
				sb.append(c);
			}
			fos.write(sb.toString().getBytes());
			fos.flush();
			fos.close();

			//creat FileChannel
			RandomAccessFile file=new RandomAccessFile(filepath,"rw");
			FileChannel channel=file.getChannel();
			System.out.println("file position in FileChannel is ："+ channel.position());
			file.seek(5);
			System.out.println("file position in FileChannel is : "+channel.position());
			channel.position(10);
			System.out.println("file position in RandomAccessFile is : "+file.getFilePointer());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文件通道的读写，通道的读写都要通过ByteBuffer
	 * @throws Exception
	 */
	private static void testFileCopy() throws Exception{
		RandomAccessFile source=new RandomAccessFile("resources/a.txt","r");
		RandomAccessFile dest=new RandomAccessFile("resources/b.txt","rw");
		FileChannel srcChannel=source.getChannel();
		FileChannel destChannel=dest.getChannel();
		ByteBuffer buffer=ByteBuffer.allocate(8);

		while (srcChannel.read(buffer)!=-1){
			buffer.flip();
			while (buffer.hasRemaining()){
				destChannel.write(buffer);
			}
			buffer.clear();
		}
		srcChannel.close();
		destChannel.close();
	}
}
