package com.cdroho.utils;

import com.cdroho.controller.PatientController;
import com.cdroho.controller.WindowController;
import com.cdroho.logicalrepository.CheckRepository;
import com.cdroho.logicalrepository.MachineRepository;
import com.cdroho.modle.CheckSick;
import com.cdroho.modle.dto.RoomDto;
import com.cdroho.modle.dto.SortRuleDto;
import com.cdroho.service.WaitQueueService;
import com.cdroho.websocket.ProductWebSocket;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 抽血呼叫
 * @author HZL
 * @date 2020-6-28
 */
public class CheckWebSocketCall {

    private Logger log = LoggerFactory.getLogger(CheckWebSocketCall.class);

    private CopyOnWriteArraySet<ProductWebSocket> webSockets = MySessionModel.getCopyOnWriteArraySet();

    private static Map<String,Object> resultMap=new HashMap();

    @Autowired
    private WaitQueueService queueService;

    @Autowired
    private PatientController sickController;

    @Autowired
    private CheckRepository checkRepository;

    @Autowired
    MachineRepository machineRepository;

    @Autowired
    private  WindowController windowController;


    String oprtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    /**
     * type用来判断重呼还是呼叫
     * @param flag
     * @param ruleDto
     * @param ip
     * @param queueId
     * @param sickId
     * @param type
     * @param request
     * @return
     */
    public List<CheckSick> websocketCall(int flag, SortRuleDto ruleDto,String ip, long queueId,String macIp,
                                       long sickId, long type, HttpServletRequest request, List<RoomDto> roomDtos){
        List<CheckSick> finalResult = new ArrayList<CheckSick>();
        List<CheckSick> treatSick = new ArrayList<CheckSick>();
        List<CheckSick> waitSick = new ArrayList<CheckSick>();
        List<CheckSick> passSick = new ArrayList<CheckSick>();
        passSick=checkRepository.queryPassOfData(flag);
        ProductWebSocket socket = new ProductWebSocket();
        //正在就诊5,大、小屏叫号标识
        long sickState=5, small=66, big=77;
        Boolean flagg=false;
        try {
            //更新患者为就诊状态
            flagg=updateSickCall(sickState,small,big,type,sickId);
            ruleDto=getSortRuleDto(ip);
            //锁定患者
            if(flagg==true){
                //等候队列
                List<CheckSick> dtoList=windowController.getCheck(flag,request);
                //重呼不更新锁定
                if(dtoList.size()>0&&type==0){
                    for(int i=0;i<dtoList.size();i++){
                        if(i < ruleDto.getLockCount()){
                            if (dtoList.get(i).getIsLock()==0) {
                                updateLock(dtoList.get(i).getId());
                            }
                        }else{
                            break;
                        }
                    }
                }
            }
            treatSick=checkRepository.queryNowData(flag);
            waitSick=windowController.getCheck(flag,request);
            finalResult.addAll(treatSick);
            finalResult.addAll(waitSick);
            //大小屏两个ip分别对应两个ProductWebSocket对象
            for(ProductWebSocket productWebSocket:webSockets){
               for(RoomDto dto:roomDtos){
                   if(dto.getRoomIp().equals(productWebSocket.getClientip())){
                       Map<String,Object> map=new HashMap();
                       map.put("now",treatSick);
                       map.put("wait",waitSick);
                       map.put("zsmc",machineRepository.getMachineByIp(macIp).getMachineName());
                       socket = productWebSocket;
                       if(null==socket.getSession()){
                           log.info("暂无websocket连接建立");
                       }else {
                           try {
                               socket.sendMessage(JsonToMap.mapToJson(map).toString());
                               AsmxWS.voice("请"+treatSick.get(0).getName()+"到"+machineRepository.getMachineByIp(macIp).getMachineName()+"抽血",dto.getRoomIp());
                               log.info("调用成功！");
                           } catch (DocumentException e) {
                               log.info("调用语音发声错误！");
                           }
                           //重呼不更新规则
                           if (passSick.size()>0) {
                               if (type==0) {
                                   if(ruleDto.getAgainBeCompared()==2){
                                       machineRepository.updateMachineRule(0,0,macIp);
                                   }else{
                                       int beCompared=ruleDto.getAgainBeCompared();
                                       machineRepository.updateMachineRule(beCompared+1,0,macIp);
                                   }
                               }
                           }
                       }
                   }
               }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        resultMap.put("count",finalResult.size()-1);
        resultMap.put("data",finalResult);
        return finalResult;
    }

    public Boolean updateSickCall(long sickState, long small, long big,long type, long sickId) {
        String oprtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Boolean falg=false;
        if(type==0){
            checkRepository.updateCall(sickState,oprtime,type+1,oprtime,sickId);
        }else{
            //重呼
            checkRepository.updateCall(sickState,oprtime,type,oprtime,sickId);
        }
        falg=true;
        return falg;
    }

    public SortRuleDto getSortRuleDto(String triageIp) {
        Object o=null;
        o=checkRepository.getSortRuleById(triageIp);
        Object[] oArray=(Object[])o;
        SortRuleDto ruleDto =new SortRuleDto();
        ruleDto.setLockCount((int)oArray[0]);
        ruleDto.setPassCount((int)oArray[1]);
        ruleDto.setPassWeight((int)oArray[2]);
        ruleDto.setReturnWeight((int)oArray[3]);
        if (null!=oArray[4]) {
            ruleDto.setAgainBeCompared(Integer.valueOf(oArray[4].toString()));
        }
        if (null!=oArray[5]) {
            ruleDto.setPassBeCompared(Integer.valueOf(oArray[5].toString()));
        }
        return ruleDto;
    }

    public Boolean updateLock(int sickId) {
        String oprtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        boolean flag=false;
        checkRepository.updateLock(oprtime,sickId);
        flag=true;
        return flag;
    }
}
