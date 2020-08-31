package com.cdroho.eventlistenser;

import com.cdroho.logicalrepository.TriageRepository;
import com.cdroho.modle.NurseTriage;
import com.cdroho.modle.dto.QueueDto;
import com.cdroho.modle.dto.SickDto;
import com.cdroho.modle.dto.SortRuleDto;
import com.cdroho.service.WaitQueueService;
import com.cdroho.utils.ComparatorQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * 监听事件的监听器
 * 进行自定义事件的注册
 * @author HZL
 */
@Component
public class Mylistenser implements ApplicationListener<MyEvents> {

    @Autowired
    private WaitQueueService queueService;
    @Autowired
    private TriageRepository triageRepository;

    @Override
    public void onApplicationEvent(MyEvents event) {
        System.out.println("------监听到自定义事件发生------");
        event.printStr();
        System.out.println("------如果标识符为true："+event.getFlag()+"监听的事件成功，否则失败");
        System.out.println("------开始监听后的处理理逻辑------");
        //根据此报道的人找到所属叫号器绑定的屏幕进行websocket通信
        System.out.println("------查询出呼叫后的当前等候队列list");

    }

}
