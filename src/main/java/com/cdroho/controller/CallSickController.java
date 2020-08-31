package com.cdroho.controller;

import com.cdroho.logicalrepository.RoomRepository;
import com.cdroho.logicalrepository.TriageRepository;
import com.cdroho.modle.ConsultationRoom;
import com.cdroho.modle.dto.RoomDto;
import com.cdroho.modle.dto.SickDto;
import com.cdroho.modle.dto.SortRuleDto;
import com.cdroho.service.WaitQueueService;
import com.cdroho.utils.MySessionModel;
import com.cdroho.utils.Tool;
import com.cdroho.utils.WebSocketCall;
import com.cdroho.websocket.ProductExpireTask;
import com.cdroho.websocket.ProductWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.Session;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 呼叫控制器
 * 规则ip参数改为分诊台ip
 * @author HZL
 * @date 2020-4-20
 */
@RestController
@CrossOrigin
@RequestMapping("callogic")
public class CallSickController {

    private Logger log = LoggerFactory.getLogger(CallSickController.class);
    private static MySessionModel mySessionModel = MySessionModel.getInstance();
    @Autowired
    private WaitQueueService queueService;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private TriageRepository triageRepository;
    @Autowired
    private Tool tool;
    @Autowired
    private WebSocketCall webSocketCall;


    /**
     * 呼叫器呼叫按钮操作
     * 通过查询与叫号器绑定的屏幕，获取屏幕的ip，通过websocket向这些屏幕发送消息
     * @param params
     * @param request
     * @return
     */
    @PostMapping("call")
    public Map call(@RequestParam(value = "queueId") long queueId,@RequestParam(value = "sickId") long sickId,HttpServletRequest request){
        Map<String,Object> resultMap=new HashMap();
        Map<String,Long> params = new HashMap<>();
        String machineIp="";
        String triageIp="";
        List<RoomDto> roomDtos = new ArrayList<>();
        machineIp=tool.getIpAddr(request);
        //根据叫号器ip获取屏幕ip
        roomDtos=getRoomByMachine(machineIp,request);
        //根据屏幕ip获取分诊台
        Object[] object = triageRepository.findNurseTriageByMachineIp(machineIp);
        Object[] obj=(Object[])object[0];
        triageIp=String.valueOf(obj[0]);
        CopyOnWriteArraySet<ProductWebSocket> webSockets = MySessionModel.getCopyOnWriteArraySet();
        List<SickDto> finalResult = new ArrayList<SickDto>();
        //服务端向客户端发送消息
        ProductWebSocket socket = new ProductWebSocket();
        /*long queueId=params.get("queueId");
        long sickId=params.get("sickId");*/
        params.put("sickId",sickId);
        params.put("queueId",queueId);
        params.put("nurseId",Long.valueOf(String.valueOf(obj[2])));
        long type=0;
        SortRuleDto ruleDto = new SortRuleDto();
        log.info("分诊台ip为--------------------------->"+triageIp);
        finalResult=webSocketCall.websocketCall(params,ruleDto,triageIp,queueId,machineIp,sickId,type,request,roomDtos);
        resultMap.put("code","success");
        resultMap.put("data",finalResult);
        return resultMap;
    }

    /**
     * 呼叫器重呼按钮操作
     * @param params
     * @param request
     * @return
     */
    @PostMapping("reCall")
    public Map reCall(@RequestParam(value = "queueId") long queueId,@RequestParam(value = "sickId") long sickId,HttpServletRequest request){
        Map<String,Object> resultMap=new HashMap();
        Map<String,Long> params = new HashMap<>();
        String machineIp="";
        String triageIp="";
        List<RoomDto> roomDtos = new ArrayList<>();
        machineIp=tool.getIpAddr(request);
        //根据叫号器ip获取屏幕ip
        roomDtos=getRoomByMachine(machineIp,request);
        CopyOnWriteArraySet<ProductWebSocket> webSockets = MySessionModel.getCopyOnWriteArraySet();
        //根据屏幕ip获取分诊台
        Object[] object = triageRepository.findNurseTriageByMachineIp(machineIp);
        Object[] obj=(Object[])object[0];
        triageIp=String.valueOf(obj[0]);
        params.put("nurseId",Long.valueOf(String.valueOf(obj[2])));
        List<SickDto> finalResult = new ArrayList<SickDto>();
        params.put("sickId",sickId);
        params.put("queueId",queueId);
        long type=0;
        List<SickDto> treatSick = new ArrayList<>();
        //获取正在就诊的人
        treatSick=queueService.geTreatSick(queueId,sickId);
        SickDto treat=new SickDto();
        treat=treatSick.get(0);
        type=treat.getCallCount()+1;
        SortRuleDto ruleDto = new SortRuleDto();
        finalResult=webSocketCall.websocketCall(params,ruleDto,triageIp,queueId,machineIp,sickId,type,request,roomDtos);
        resultMap.put("code","success");
        resultMap.put("data",finalResult);
        return resultMap;
    }

    /**
     * 呼叫器过号按钮操作
     * @param params
     * @param request
     * @return
     */
    @PostMapping("setPass")
    public Map setPass(@RequestParam(value = "sickId") long sickId,
                       @RequestParam(value = "queueId") long queueId,HttpServletRequest request){
        Map<String,Object> resultMap=new HashMap();
        List<SickDto> sick = new ArrayList<>();
        long count=0;
        long type=0;
        String ip = tool.getIpAddr(request);
        Object[] object = triageRepository.findNurseTriageByMachineIp(ip);
        Object[] obj=(Object[])object[0];
        String triageIp=String.valueOf(obj[0]);
        SortRuleDto ruleDto = new SortRuleDto();
        ruleDto=queueService.getSortRuleDto(queueId,triageIp,"1");
        sick=queueService.geSick(queueId,sickId);
        SickDto treat=new SickDto();
        treat=sick.get(0);
        count=treat.getCallCount();
        if(ruleDto.getPassCount()>0){
            if(ruleDto.getPassCount()<count){
                //更新为过号
                type=3;
                queueService.operateSicK(sickId, queueId, type);
                resultMap.put("code","success");
                resultMap.put("msg","已超过号次数，进入过号队列！");
            }else{
                //更新未到过号
                type=5;
                queueService.operateSicK(sickId, queueId, type);
                resultMap.put("code","success");
                resultMap.put("msg","更新未到过号成功！");
            }
        }

        return resultMap;
    }

    /**
     * 呼叫器诊结按钮操作
     * @param params
     * @param request
     * @return
     */
    @PostMapping("setFinish")
    public Map setFinish(@RequestParam(value = "queueId") long queueId,@RequestParam(value = "sickId") long sickId,HttpServletRequest request){
        Map<String,Object> resultMap=new HashMap();
        long type=6;
        String code="";
        code= queueService.operateSicK(sickId, queueId, type);
        if(code.equals("success")){
            resultMap.put("code","success");
            resultMap.put("msg","诊结成功！");
        }else{
            resultMap.put("code","fail");
            resultMap.put("msg","诊结失败！");
        }
        return resultMap;
    }

    /**
     *获取当前就诊人
     * @param queueId
     * @param sickId
     * @return
     */
    @PostMapping("queryNowSick")
    public Map getNowSick(@RequestParam(value = "queueId") long queueId){
        Map<String,Object> result= new HashMap<>();
        List<SickDto> treatSick = new ArrayList<>();
        treatSick=queueService.geTreatSick(queueId,0);
        if(treatSick.size()>0){
            result.put("msg","查询当前就诊人成功");
            result.put("data",treatSick.get(0));
        }else{
            result.put("msg","当前就诊人暂无");
        }
        return result;
    }
    /**
     * 获取客户端IP地址
     * @param request
     * @return
     */
    @GetMapping("clientinfo")
    public Map getClientInfo(HttpServletRequest request){
        Map<String,Object> resultMap=new HashMap();
        String ip = tool.getIpAddr(request);
        if(ip != null && !"".equals(ip)){
            resultMap.put("code","success");
            resultMap.put("ip",ip);

        }else{
            resultMap.put("code","fail");
            resultMap.put("ip","");
        }
        return resultMap;
    }

    private RoomDto modleToDto(Object[] oArray){
        RoomDto dto=new RoomDto();
        Long id=Long.valueOf(String.valueOf(oArray[0]));
        dto.setId(id);
        dto.setRoomIp((String)oArray[1]);
        dto.setRoomName((String)oArray[2]);
        dto.setRoomProfile((String)oArray[3]);
        dto.setNurseName((String)oArray[4]);
        return dto;
    }

    /**
     * 根据叫号器获取绑定的屏幕
     * @param machineIp
     * @param request
     * @return
     */
    public List<RoomDto> getRoomByMachine(String machineIp,HttpServletRequest request){
        //根据叫号器ip获取屏幕ip
        ConsultationRoom room = new ConsultationRoom();
        List<RoomDto> roomDtos=new ArrayList<>();
        String roomIp="";
        roomIp=tool.getIpAddr(request);
        if (!(roomIp.equals(""))) {
            room = roomRepository.findByIp(roomIp);
        }
        List<Object> objects = roomRepository.findRoomWithMachineIp(roomIp);
        for(Object o : objects){
            RoomDto dto=new RoomDto();
            dto=modleToDto((Object[]) o);
            roomDtos.add(dto);
        }
        return roomDtos;
    }
}
