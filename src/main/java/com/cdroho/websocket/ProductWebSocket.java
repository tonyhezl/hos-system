package com.cdroho.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import com.cdroho.utils.MySessionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * "@ServerEndpoint"---->建立websocket连接，标注被客户端连接的一方（即：接收客户端请求url的服务端）
 * 问题：每次新客户端连接CopyOnWriteArraySet<ProductWebSocket>长度只为1
 * 原因：ProductWebSocket加@Component默认单例模式，所以每次都是一个。开启多例即可
 * 一个连接对应一个ProductWebSocket对象
 * @author HZL
 *
 */
@Component
@Scope("prototype")
@ServerEndpoint(value = "/productWebSocket/{clinetip}", configurator = MyEndpointConfigure.class)
public class ProductWebSocket {

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static final AtomicInteger OnlineCount = new AtomicInteger(0);

    private String clientip="";
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的ProductWebSocket对象。
     */
    private static CopyOnWriteArraySet<ProductWebSocket> webSocketSet = MySessionModel.getCopyOnWriteArraySet();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    private Logger log = LoggerFactory.getLogger(ProductWebSocket.class);

    /**
     * 连接建立成功调用的方法
     * 当客户端ip地址不一致时
     */
    @OnOpen
    public void onOpen(@PathParam("clinetip")String clinetip,Session session) {
        this.session = session;
        this.setClientip(clinetip);
        webSocketSet.add(this);
        //在线数加1
        addOnlineCount();
        log.info("新客户端连入，用户ip：" + clinetip+"连接成功-"+"-当前在线人数为："+webSocketSet.size());
        /*if(clinetip!=null) {
            List<String> totalPushMsgs = new ArrayList<String>();
            totalPushMsgs.add(clinetip+"连接成功-"+"-当前在线人数为："+webSocketSet.size());
            if(totalPushMsgs != null && !totalPushMsgs.isEmpty()) {
                totalPushMsgs.forEach(e -> {
                        sendMessage(e);
                });
            }
        }*/
    }
 
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        log.info("一个客户端关闭连接");
        // 从set中删除
        webSocketSet.remove(this);
        // 在线数减1
        subOnlineCount();
    }
 
    /**
     * 收到客户端消息后调用的方法
     * @param message
     * 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {

         log.info("用户发送过来的消息为："+message);

    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("websocket出现错误");
        error.printStackTrace();
    }

    public void sendMessage(String message){
        try {
            this.session.getBasicRemote().sendText(message);
            log.info("推送消息成功，消息为：" + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message) throws IOException {
        for (ProductWebSocket productWebSocket : webSocketSet) {
            productWebSocket.sendMessage(message);
        }

    }

    public String getClientip() {
        return clientip;
    }

    public void setClientip(String clientip) {
        this.clientip = clientip;
    }

    public static synchronized int getOnlineCount() {
        return OnlineCount.get();
    }
 
    public static synchronized void addOnlineCount() {
        OnlineCount.incrementAndGet(); // 在线数加1
    }
 
    public static synchronized void subOnlineCount() {
        OnlineCount.decrementAndGet(); // 在线数加1
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
