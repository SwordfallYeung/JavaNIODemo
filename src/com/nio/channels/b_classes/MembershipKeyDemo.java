package com.nio.channels.b_classes;

/**
 * @author y15079
 * @create 2017-11-13 11:41
 * @desc
 *
 * MembershipKey表示一个网络IP协议的多播分组成员信息token
 *
 * 一个MembershipKey表示一个多播组成员接受发送到多播组的报文的关系，如果源地址确定，
表示直接接受来至源地址的报文。我们可以通过#sourceAddress来判断源地址是否确定。
 *
 * 一个多播组成员关系在创建时，是有效的，直到MembershipKey调用#drop方法，之前他都是有效。
我们可以调用#isValid()来判断其是否有效。
 *
 * 如果MembershipKey的源地址是不确定的，底层操作系统支持源地址过滤，调用#block和#unblock将会
阻塞和解除阻塞从源地址发送过来的报文
 *
 **/
public class MembershipKeyDemo {
}
