package com.cdroho.utils;

import com.cdroho.controller.PatientController;
import com.cdroho.logicalrepository.MachineRepository;
import com.cdroho.modle.dto.*;
import com.cdroho.service.WaitQueueService;
import com.cdroho.websocket.ProductWebSocket;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * websocket呼叫
 * 此对象唯一，加synchronized修饰方法，锁定该对象。
 * 则其他访问该对象的线程被阻塞
 * @author HZL
 * @date 2020-4-20
 */
public class WebSocketCall {

    private Logger log = LoggerFactory.getLogger(WebSocketCall.class);

    private CopyOnWriteArraySet<ProductWebSocket> webSockets = MySessionModel.getCopyOnWriteArraySet();

    private static Map<String,Object> resultMap=new HashMap();

    @Autowired
    private WaitQueueService queueService;

    @Autowired
    private PatientController sickController;

    @Autowired
    MachineRepository machineRepository;

    /**
     * type用来判断重呼还是呼叫
     * @param params
     * @param ruleDto
     * @param ip
     * @param queueId
     * @param sickId
     * @param type
     * @param request
     * @return
     */
    public synchronized List<SickDto> websocketCall(Map<String,Long> params, SortRuleDto ruleDto,String ip, long queueId,String macIp,
                                       long sickId, long type, HttpServletRequest request, List<RoomDto> roomDtos){
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        int nowHours = now.get(Calendar.HOUR_OF_DAY);
        boolean code=false;
        List<SickDto> finalResult = new ArrayList<SickDto>();
        List<SickDto> treatSick = new ArrayList<SickDto>();
        List<SickDto> waitSick = new ArrayList<SickDto>();
        List<SickDto> passSick = new ArrayList<SickDto>();
        List<SickDto> aginSick = new ArrayList<SickDto>();
        List<SickDto> passMac = new ArrayList<SickDto>();
        List<SickDto> againMac = new ArrayList<SickDto>();
        long state=1;
        if (nowHours<13) {
            passSick=queueService.getPassSick(queueId,state,1);
            aginSick=queueService.getAginSick(queueId,1);
        }else if(nowHours>=13){
            passSick=queueService.getPassSick(queueId,state,2);
            aginSick=queueService.getAginSick(queueId,2);
        }

        againMac=queueService.getAginSickMachine(queueId);
        long stateMac=1;
        //未到过号患者
        passMac=queueService.getPassSickMachine(queueId,stateMac);

        ProductWebSocket socket = new ProductWebSocket();
        ProductWebSocket callSocket = new ProductWebSocket();
        //正在就诊8,大、小屏叫号标识
        long sickState=8, small=66, big=77;
        Boolean flag=false;
        try {
            //更新患者为就诊状态
            flag=queueService.updateSickCall(sickState,small,big,type,sickId);
            ruleDto=queueService.getSortRuleDto(queueId,ip,"1");
            //锁定患者
            if(flag==true){
                //等候队列
                List<SickDto> dtoList=sickController.getPaientList(params,request);
                //重呼不更新锁定
                if(dtoList.size()>0&&type==0){
                    for(int i=0;i<dtoList.size();i++){
                        if(i < ruleDto.getLockCount()){
                            if (dtoList.get(i).getIsLock()==0) {
                                queueService.updateLock(dtoList.get(i).getId(),queueId);
                            }
                        }else{
                            break;
                        }
                    }
                }
            }
            treatSick=queueService.geTreatSick(queueId,sickId);
            waitSick=sickController.getPaientList(params,request);
            finalResult.addAll(treatSick);
            finalResult.addAll(waitSick);
            //大小屏两个ip分别对应两个ProductWebSocket对象
            for(ProductWebSocket productWebSocket:webSockets){
                if(macIp.equals(productWebSocket.getClientip())){
                    Map<String,Object> map=new HashMap();
                    map.put("wait",trans(waitSick));
                    callSocket = productWebSocket;
                    callSocket.sendMessage(JsonToMap.mapToJson(map).toString());
                }
            }
            for(ProductWebSocket productWebSocket:webSockets){
               for(RoomDto dto:roomDtos){
                   if(dto.getRoomIp().equals(productWebSocket.getClientip())){
                       Map<String,Object> map=new HashMap();
                       map.put("now",treatSick);
                       map.put("wait",waitSick);
                       map.put("zsmc",machineRepository.getMachineByIp(macIp).getMachineProfile());
                       socket = productWebSocket;
                       if(null==socket.getSession()){
                           log.info("暂无websocket连接建立");
                       }else {
                           socket.sendMessage(JsonToMap.mapToJson(map).toString());
                           try {
                               long start = System.currentTimeMillis();
                               AsmxWS.voice("请"+treatSick.get(0).getSickName()+"到"+machineRepository.getMachineByIp(macIp).getRoomName()+"就诊",dto.getRoomIp());
                               long end = System.currentTimeMillis();
                               log.info("调用语音成功！耗时"+(end-start)/1000+"秒");
                           } catch (DocumentException e) {
                               log.error("调用语音发声错误！");
                           }
                           code=true;
                           //重呼不更新规则
                           if (type==0) {
                               try {
                                   long start1 = System.currentTimeMillis();
                                   AsmwebService.deliver(FastJsonConvertUtil.convertObjectToJSON(swap(treatSick,waitSick,machineRepository.getMachineByIp(macIp).getRoomName()
                                           ,dto.getNurseName(),queueId)));
                                   long end1 = System.currentTimeMillis();
                                   log.info("推送成功！耗时"+(end1-start1)/1000+"秒");
                               } catch (DocumentException e) {
                                   log.error("推送失败！");
                               }
                               if (passSick.size()>0||aginSick.size()>0) {
                                   if(ruleDto.getAgainBeCompared()==2){
                                       machineRepository.updateMachineRule(0,0,macIp);
                                   }else{
                                       int beCompared=ruleDto.getAgainBeCompared();
                                       machineRepository.updateMachineRule(beCompared+1,0,macIp);
                                   }
                               }
                               if (passMac.size()>0||againMac.size()>0) {
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

    public WbsDto swap(List<SickDto> treatSick ,List<SickDto> wait,String zsmc,String nurseName,long queueId){
        WbsDto dto = new WbsDto();
        if(treatSick.size()>0&&wait.size()>2){
            dto.setIdqxh(treatSick.get(0).getSickNumber());
            dto.setIqmjzrssy(2);
            dto.setCxm(wait.get(2).getSickName());
            dto.setCzsmc(zsmc);
            dto.setCksmc(nurseName);
            dto.setCysmc(wait.get(2).getDoctorName());
            dto.setLdlxhl((int)queueId);
            dto.setResultCode("0");
            dto.setResultMessage("推送成功！");
        }else{
            dto.setResultCode("1");
            dto.setResultMessage("暂无等候患者，推送失败！");
        }
        return dto;
    }

    public List<CallDto> trans(List<SickDto> wait){
        List<CallDto> callList=new ArrayList<CallDto>();
        for(SickDto dto:wait){
            CallDto callDto=new CallDto();
            callDto.setId(dto.getId());
            callDto.setSickName(dto.getSickName());
            callDto.setSickNumber(dto.getSickNumber());
            callDto.setSickState(dto.getSickState());
            callList.add(callDto);
        }
        return callList;
    }
}
