package com.nio.a_class;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author y15079
 * @create 2017-11-10 11:22
 * @desc
 *
 * 从继承结构上来讲，MappedByteBuffer继承自ByteBuffer，所以ByteBuffer有的能力它全有；
 * 像变动position和limit指针啦、包装一个其他种类Buffer的视图啦，都可以。
 *
 * “MappedByteBuffer”为何而来？吾辈心中亦有惑（熊猫人之谜的梗）用一个字来概括就是快

 * 为什么快？因为它使用direct buffer的方式读写文件内容，这种方式的学名叫做内存映射。
 * 这种方式直接调用系统底层的缓存，没有JVM和系统之间的复制操作，所以效率大大的提高了。
 * 而且由于它这么快，还可以用它来在进程（或线程）间传递消息，基本上能达到和“共享内存页”相同的作用，只不过它是依托实体文件来运行的。
 *
 * 而且它还有另一种能力。就是它可以让我们读写那些因为太大而不能放进内存中的文件。有了它，我们就可以假定整个文件都放在内存中（实际上，大文件放在内存和虚拟内存中），
 * 基本上都可以将它当作一个特别大的数组来访问，这样极大的简化了对于大文件的修改等操作。
 **/
public class MappedByteBufferDemo {
	private static int length = 0x2FFFFFFF;//1G
	private abstract static class Tester{
		private String name;

		public Tester(String name) {
			this.name = name;
		}

		public void runTest(){
			System.out.print(name+": ");
			long start=System.currentTimeMillis();
			test();
			System.out.println(System.currentTimeMillis()-start+" ms");
		}

		public abstract void test();
	}

	private static Tester[] testers={
			new Tester("Stream RW") {
				@Override
				public void test() {
					try {
						FileInputStream fis=new FileInputStream("resources/a.txt");
						DataInputStream dis=new DataInputStream(fis);
						FileOutputStream fos=new FileOutputStream("resources/a.txt");
						DataOutputStream dos=new DataOutputStream(fos);
						byte b=(byte)0;
						for (int i=0;i<length;i++){
							dos.writeByte(b);
							dos.flush();
						}
						while (dis.read()!=-1){

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			},
			new Tester("Mapped RW") {
				@Override
				public void test() {
					try {
						FileChannel channel=FileChannel.open(Paths.get("resources/b.txt"),StandardOpenOption.READ,StandardOpenOption.WRITE);
						MappedByteBuffer mappedByteBuffer=channel.map(FileChannel.MapMode.READ_WRITE,0,length);

						for (int i=0;i<length;i++){
							mappedByteBuffer.put((byte)0);
						}
						mappedByteBuffer.flip();
						while (mappedByteBuffer.hasRemaining()){
							mappedByteBuffer.get();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			},
			new Tester("Mapped PRIVATE") {
				@Override
				public void test() {
					try {
						FileChannel channel=FileChannel.open(Paths.get("resources/c.txt"),StandardOpenOption.READ,StandardOpenOption.WRITE);
						MappedByteBuffer mappedByteBuffer=channel.map(FileChannel.MapMode.PRIVATE,0,length);

						for (int i=0;i<length;i++){
							mappedByteBuffer.put((byte)0);
						}
						mappedByteBuffer.flip();
						while (mappedByteBuffer.hasRemaining()){
							mappedByteBuffer.get();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
	};


	public static void main(String[] args) {
		for (Tester tester:testers){
			tester.runTest();
		}
	}
}
