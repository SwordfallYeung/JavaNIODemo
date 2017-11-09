package com.nio.a_class;

/**
 * @author y15079
 * @create 2017-11-09 15:07
 * @desc
 *
 * Buffer
 * 一、属性
 * Buffer有四个基本属性：
 *  1.capacity 容量，buffer能够容纳的最大元素数目，在Buffer创建时设定并不能更改
 *  2.limit buffer中有效位置数目，不能对超过limit中的区域进行读写
 *  3.position 下一个读或者写的位置
 *  4.mark 用于记忆的标志位，配合reset()使用，初始值未设定，调用mark后将当前position设为值
 *  四者关系：0 <= mark <= position <= limit <= capacity
 *
 *  Buffer是非线程安全类。为抽象类
 *
 **/
public class BufferDemo {
}
