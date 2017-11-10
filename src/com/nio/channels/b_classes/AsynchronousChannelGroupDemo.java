package com.nio.channels.b_classes;

/**
 * @author y15079
 * @create 2017-11-10 14:47
 * @desc
 *
 * 每一个asynchronous channel都属于同一个group，共享一个Java线程池
 * 1.从代码创建角度来看无论是AsynchronousServerSocketChannel还是AsynchronousSocketChannel的创建都使用
 *   各自的静态方法open，而open方法便需要asynchronousChannelGroup。
 * 2. 可以发现AsynchronousChannelGroup其内部其实是一个（一些）线程池进行实质工作的；
 *    而他们干的活就是等待IO事件，处理数据，分发各个注册的completion handlers。
 *
 * 抽象类
 **/
public class AsynchronousChannelGroupDemo {
}
