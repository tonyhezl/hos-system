package com.cdroho.websocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import com.cdroho.utils.MySessionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.Session;


/**
 * 呼叫采用websocket通信
 * 前端与后端建立连接
 * 向页面发送当前呼叫的人的信息
 * @author HZL
 */
@Component
public class ProductExpireTask {

    private static MySessionModel mySessionModel = MySessionModel.getInstance();
    private Logger log = LoggerFactory.getLogger(ProductExpireTask.class);
    //@Scheduled (fixedRate=2000)
    public void productExpire() throws IOException {
        String ip ="192.168.2.60";
        String ip2 ="192.168.2.67";
        String[] strs={
        		"Test随机消息 ：30.1123",
                "Test随机消息 ：32.1021",
                "Test随机消息 ：33.1774",
                "Test随机消息 ：33.2372",
                "Test随机消息 ：31.0281",
                "Test随机消息 ：30.0222",
                "Test随机消息 ：32.1322",
                "Test随机消息 ：33.3221",
                "Test随机消息 ：31.2311",
                "Test随机消息 ：32.3112"
               };
        try {
            CopyOnWriteArraySet<ProductWebSocket> webSockets = MySessionModel.getCopyOnWriteArraySet();
            ProductWebSocket socket = null;
            if (null!=webSockets) {
                //服务端向客户端发送消息
                socket = new ProductWebSocket();
                 //通过ip找到与服务器建立的指定会话session
                for(ProductWebSocket productWebSocket:webSockets){
                    //如果ip与客户端ip相同则发送信息
                    if(ip.equals(productWebSocket.getClientip())){
                        socket = productWebSocket;
                    }
                    if(ip2.equals(productWebSocket.getClientip())){
                        socket = productWebSocket;
                    }
                }
                if(null==socket.getSession()){
                    log.info("暂无websocket连接建立");
                }else {
                    socket.sendMessage(socket.getClientip()+"------------------消息");
                }
            }else{
                log.info("暂无websocket连接建立");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    //随机返回字符串数组中的字符串
    public static String RandomStr(String[] strs){
        int random_index = (int) (Math.random()*strs.length);
        return strs[random_index];
    }
 
 
}
