package com.cdroho.mynetty;



/**
 * Netty计算机网络通信框架，也就是像运输层那样负责数据可靠的传输
 * 数据是从socket缓冲区到网卡缓存，在发送出去
 * 例如:用户程序想读取磁盘上的某一段数据，那么用户的程序就会通过操作系统调用来调用内核态的接口，请求操作系统来完成某种操作
 * Netty接收和发送数据时，对堆外内存(被OS管理，减少了从堆内内存到其的复制操作)读写，再有堆外内存到sokcet缓冲区
 */
public class DeNetty {
    public void test(){
        System.out.println("hello world");
    }
}
