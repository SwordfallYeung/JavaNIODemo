一、基本概念描述<br/>
1.1 I/O简介<br/>
I/O即输入输出，是计算机与外界的一个接口。在java编程中，一般使用流的方式来处理IO，所有的IO都被视作是单个字节的移动，通过stream对象一次移动一个字节。流IO负责把对象转换为字节，然后再转换为对象。<br/>
<br/>
1.2 什么是NIO<br/>
NIO即New IO，这个库是在JDK1.4中引入的。NIO和IO有相同的作用和目的，但实现方式不同，NIO主要用到的是块，所以NIO的效率要比IO高很多。<br/>
<br/>
在Java API中提供了两套NIO，一套是针对标准输入输出NIO，另一套是网络编程IO<br/>
<br/>
1.3 流与块的比较<br/>
NIO和IO最大的区别是数据打包和传输方式。IO是以流的方式处理数据，而NIO是以块的方式处理数据。<br/>
<br/>
面向流的IO一次一个字节的处理数据，一个输入流产生一个字节，一个输出流就消费一个字节。为流式数据创建过滤器就变得非常容易，链接几个过滤器，以便对数据进行处理非常方便而简单，但是面向流的IO通常处理的很慢。<br/>
<br/>
面向块的IO系统以块的形式处理数据。每一个操作都在一步中产生或消费一个数据块。按块要比按流快的多，但面向块的IO缺少类面向流IO所具有的雅兴和简单性。<br/>
<br/>
二、NIO基础<br/>
Buffer和Channel是标准NIO中的核心对象，几乎每一个IO操作中都会用到它们。<br/>
<br/>
Channel是对原IO中流从模拟，任何来源和目的数据都必须通过一个Channel对象。一个Buffer实质上是一个容器对象，发给Channel的所有对象都必须先放到Buffer中；同样的，从Channel中读取的任何数据都要读到Buffer中。<br/>
<br/>
2.1 关于Buffer（缓冲区）<br/>
Buffer是一个对象，它包含一些要写入或读出的数据。在NIO中，数据是放入buffer对象的，而在IO中，数据是直接写入或者读到Stream对象的。应用程序不能直接对Channel进行读写操作，而必须通过buffer来进行的，即Channel是通过Buffer来读写数据的。<br/>
<br/>
在NIO中，所有的数据都是用Buffer处理的，它是NIO读写数据的中转池。Buffer实质上是一个数组，通常是一个字节数据，但也可以是其他类型的数组。但一个缓冲区不仅仅是一个数组，重要的是它提供了对数据的结构化访问，而且还可以跟踪系统的读写进程。<br/>
<br/>
2.2 关于Channel（通道）<br/>
Channel是一个对象，可以通过它读取和写入数据。可以把它看作是IO中的流。但是它和流相比还有一些不同：<br/>
 1.Channel是双向的，既可以读又可以写，而流是单向的<br/>
 2.Channel可以进行异步的读写<br/>
 3.对Channel的读写必须通过buffer对象<br/>
 <br/>
所有数据都通过Buffer对象处理，所以，你永远不会将字节直接写入到Channel中，相反，你是将数据写入到Buffer中；同样，你也不会从Channel中读取字节，而是将数据从Channel读入Buffer，再从Buffer获取这个字节。<br/>
因为Channel是双向的，所以Channel可以比流更好地反映出底层操作系统的真实情况。<br/>
<br/>
Channel用于在字节缓冲区和位于通道另一侧的实体（通常是一个文件或套接字）之间有效地传输数据。<br/>
<br/>
通道是一种途径，借助该途径，可以用最小的总开销来访问操作系统本身的I/O服务。缓冲区则是通道内部用来发送和接收数据的端点。通道channel充当连接I/O服务的导管：<br/>
![Image text](https://github.com/qiushangwenyue/JavaNIODemo/blob/master/img/hello.png)<br/>
<br/>
NIO通信过程（涉及serverSocketChannel、socketChannel、selector、buffer）:<br/>
![Image text](https://github.com/qiushangwenyue/JavaNIODemo/blob/master/img/world.png)<br/>