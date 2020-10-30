package com.cdroho.controller;

import com.cdroho.logicalrepository.DoctorRepository;
import com.cdroho.logicalrepository.QueueRepository;
import com.cdroho.logicalrepository.RoomRepository;
import com.cdroho.logicalrepository.TriageRepository;
import com.cdroho.modle.ConsultationRoom;
import com.cdroho.modle.DoctorManger;
import com.cdroho.modle.NurseTriage;
import com.cdroho.modle.dto.*;
import com.cdroho.service.WaitQueueService;
import com.cdroho.utils.*;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.omg.CORBA.DomainManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import javax.management.ObjectName;
import javax.management.Query;
import javax.naming.event.ObjectChangeListener;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 患者控制器
 * @author HZL
 * @date 2020-4-3
 */
@RestController
@CrossOrigin
@RequestMapping("wait")
public class PatientController {
    private Logger log = LoggerFactory.getLogger(PatientController.class);
    @Autowired
    private WaitQueueService queueService;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private TriageRepository triageRepository;
    @Autowired
    private QueueRepository queueRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    private Tool tool;
    private SortOne one = new SortOne(0);
    private static Map<String,Object> resultMap=new HashMap();
    private Object o;

    @RequestMapping("queueNomList")
    public List<SickDto> getPaientList(@RequestBody Map<String,Long> params, HttpServletRequest request){
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        int nowHours = now.get(Calendar.HOUR_OF_DAY);
        long queueId=params.get("queueId");
        long nurseId=params.get("nurseId");
        NurseTriage nurseTriage = new NurseTriage();
        nurseTriage=triageRepository.findNurseTriageById(nurseId);
        String ip=nurseTriage.getTriageIp();
        List<List<SickDto>> temResult = new ArrayList<>();
        List<SickDto> finalResult=new ArrayList();
        List<SickDto> result=new ArrayList();
        List<ComparatorQueue> specialSick = new ArrayList<>();
        List<SickDto> waitSick = new ArrayList<>();
        List<SickDto> lockedSick = new ArrayList<>();
        List<SickDto> firstSick = new ArrayList<>();
        List<SickDto> aginSick = new ArrayList<>();
        List<SickDto> passSick = new ArrayList<>();
        SortRuleDto ruleDto = new SortRuleDto();
        try {
            if (nurseTriage.getTriageType().equals("1")) {
                ruleDto=queueService.getSortRuleDto(queueId,ip,"1");
                if (nowHours<13) {
                    waitSick=queueService.getNomSick(queueId,1);
                    lockedSick=queueService.getLockedSick(queueId,1);
                    firstSick=queueService.getFirstSick(queueId,1);
                    aginSick=queueService.getAginSick(queueId,1);
                    long state=1;
                    //未到过号患者
                    passSick=queueService.getPassSick(queueId,state,1);
                }else if(nowHours>=13){
                    waitSick=queueService.getNomSick(queueId,2);
                    lockedSick=queueService.getLockedSick(queueId,2);
                    firstSick=queueService.getFirstSick(queueId,2);
                    aginSick=queueService.getAginSick(queueId,2);
                    long state=1;
                    //未到过号患者
                    passSick=queueService.getPassSick(queueId,state,2);
                }
            }else{
                ruleDto=queueService.getSortRuleDto(queueId,ip,"2");
                waitSick=queueService.getNomSickMachine(queueId);
                lockedSick=queueService.getLockedSickMachine(queueId);
                firstSick=queueService.getFirstSickMachine(queueId);
                aginSick=queueService.getAginSickMachine(queueId);
                long state=1;
                //未到过号患者
                passSick=queueService.getPassSickMachine(queueId,state);
            }
            one.setLocation(ruleDto.getReturnWeight());
            if(aginSick.size()>0&&ruleDto!=null){
                ComparatorQueue com = new ComparatorQueue();
                com.setId(1);
                com.setSickDtoList(aginSick);
                specialSick.add(com);
            }
            if(passSick.size()>0&&ruleDto!=null){
                ComparatorQueue com = new ComparatorQueue();
                com.setId(2);
                com.setSickDtoList(passSick);
                specialSick.add(com);
            }
            Collections.sort(specialSick, new MyComprator());
            if(specialSick.size()>0){
                result=sort(waitSick,aginSick,passSick,ruleDto.getAgainBeCompared(),one);
                //初诊不够插
                for (int i = 0; i < specialSick.size(); i++) {
                    ComparatorQueue ary = specialSick.get(i);
                    if (ary.getSickDtoList().size() > 0) {
                        result.addAll(ary.getSickDtoList());
                    }
                }
            }else{
                result=waitSick;
            }
        } catch (Exception e) {
            log.info("获取分诊台规则失败，队列号为："+queueId+"分诊台ip为："+ip);
        }
        finalResult.addAll(lockedSick);
        if(firstSick.size()>0){
            finalResult.addAll(firstSick);
        }
        finalResult.addAll(result);
        return finalResult;
    }

    /**
     * 按id比较
     */
    class MyComprator implements Comparator {
        @Override
        public int compare(Object arg0, Object arg1) {
            ComparatorQueue t1=(ComparatorQueue)arg0;
            ComparatorQueue t2=(ComparatorQueue)arg1;
            return t1.id>t2.id? 1:-1;
        }
    }

    /**
     * @param waitSick
     * @param aginSick
     * @param passSick
     * @param beCompared 当隔二插一的时候为0，当呼叫走一位的时候为1，当呼叫走两位的时候为2
     * @return
     */
    public static List<SickDto> sort(List<SickDto> waitSick,List<SickDto> aginSick,List<SickDto> passSick,int beCompared,SortOne one){
        List<SickDto> result=new ArrayList();

        for(int i=0;i<waitSick.size();i++) {

            int location = one.getLocation();

            if(beCompared<location) {

                result.add(waitSick.get(i));

                one.setLocation(--location);

            }else {
                if (aginSick.size()!=0) {
                    result.add(aginSick.get(0));

                    aginSick.remove(0);
                }
                if(passSick.size()!=0) {
                    result.add(passSick.get(0));

                    passSick.remove(0);
                }
                /**
                 * 唯一的动态变化条件就是当开始位不满足隔二插一(有可能是别的情况：隔二少了一位，隔二少了两位)
                 */
                if(/*隔二为隔一*/beCompared==1) {
                    one.setLocation(2);
                }else if(beCompared==2){
                    one.setLocation(3);
                }else {
                    one.setLocation(1);
                }
                //防止遗漏被插入的对象，所以这里多插了一个，
                //one.setLocation(1);上面位置就要变为1
                result.add(waitSick.get(i));
            }

        }

        one.setLocation(2);

        return result;
    }

    /**
     * 获取不同状态的患者
     * @param params
     * @return
     */
    @RequestMapping("diffentStateSick")
    public List<SickDto> diffentStateSick(@RequestBody Map<String,Long> params, HttpServletRequest request){
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        int nowHours = now.get(Calendar.HOUR_OF_DAY);
        long state=params.get("state");
        long queueId=params.get("queueId");
        long nurseId=params.get("nurseId");
        NurseTriage nurseTriage = new NurseTriage();
        nurseTriage=triageRepository.findNurseTriageById(nurseId);
        List<SickDto> sickDtos=new ArrayList<>();
        try {
            if(nurseTriage.getTriageType().equals("1")){
                if (nowHours<13) {
                    if(state==1){
                        sickDtos=this.getPaientList(params,request);
                    }
                    if(state==2){
                        sickDtos=queueService.getAginSick(queueId,1);
                    }
                    if(state==3){
                        long passState=6;
                        sickDtos=queueService.getPassSick(queueId,passState,1);
                    }
                    if(state==4){
                        sickDtos=queueService.getNoReportSick(queueId,1);
                    }
                }else if(nowHours>=13){
                    if(state==1){
                        sickDtos=this.getPaientList(params,request);
                    }
                    if(state==2){
                        sickDtos=queueService.getAginSick(queueId,2);
                    }
                    if(state==3){
                        long passState=6;
                        sickDtos=queueService.getPassSick(queueId,passState,2);
                    }
                    if(state==4){
                        sickDtos=queueService.getNoReportSick(queueId,1);
                    }
                }
            }else{
                if(state==1){
                    sickDtos=this.getPaientList(params,request);
                }
                if(state==2){
                    sickDtos=queueService.getAginSickMachine(queueId);
                }
                if(state==3){
                    long passState=6;
                    sickDtos=queueService.getPassSickMachine(queueId,passState);
                }
                if(state==4){
                    sickDtos=queueService.getNoReportSickForMachine(queueId);
                }
            }
        } catch (Exception e) {
            log.info("获取分诊台规则为空！");
        }
        return sickDtos;
    }

    /**
     * 大小屏页面过号患者
     * @param state
     * @param queueId
     * @return
     */
    @RequestMapping("getPass")
    public List<SickDto> getPass(@RequestParam(value="state") long state,@RequestParam(value="queueId") long queueId){
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        int nowHours = now.get(Calendar.HOUR_OF_DAY);
        List<SickDto> sickDtos=new ArrayList<>();
        if(state==3){
            long passState=6;
            if (nowHours<13) {
                sickDtos=queueService.getPassSick(queueId,passState,1);
            }else if(nowHours>=13){
                sickDtos=queueService.getPassSick(queueId,passState,2);
            }
        }
        return sickDtos;
    }

    /**
     * 患者操作
     * @param params
     * @return
     */
    @RequestMapping("operateSick")
    public Map operateSick(@RequestBody Map<String,Long> params) {
        long sickId = params.get("sickId");
        long type = params.get("type");
        long queueId = params.get("queueId");
        String msg = queueService.operateSicK(sickId, queueId, type);
        if (msg.equals("success")) {
            resultMap.put("code", "success");
            return resultMap;
        } else {
            resultMap.put("code","failed");
            return resultMap;
        }
    }

    /**
     * 大屏数据来源
     * @param request
     * @return
     */
    @GetMapping("bigoo")
    public Map BigRoomOO(HttpServletRequest request){
        Map<String,Object> result=new HashMap();
        Map<String,Object> sickData=new HashMap();
        List<BigDataDto> dtos = new ArrayList<>();
        List<Map> big=new ArrayList<>();
        List<String> pager=new ArrayList<>();
        List<Object> objects =new ArrayList<>();
        List<SickDto> treatSick = new ArrayList<SickDto>();
        List<SickDto> dtoList = new ArrayList<>();
        ConsultationRoom room = new ConsultationRoom();
        DoctorManger doctor=new DoctorManger();
        String ip="";
        String triageType="";
        String triageIp="";
        String doctorId="";
        long triageId=0;
        ip=tool.getIpAddr(request);
        if (!(ip.equals(""))) {
             room = roomRepository.findByIp(ip);
        }
        if(room ==null){
            result.put("msg","查询屏幕失败");
        }else{
            Object[] object = triageRepository.findNurseTriageByRoom(ip);
            if(object.length==0){
                result.put("msg","医生暂未登陆叫号器");
            }else{
                Object[] oo=(Object[])object[0];
                triageIp=(String) oo[0];
                triageType=(String) oo[1];
                triageId=Long.valueOf(String.valueOf(oo[2]));
                doctorId=String.valueOf(oo[3]);
                //根据分诊台查询所属队列,多个叫号器可以绑定一个大屏，叫号器与医生关联，医生与队列关联，所以能查询到多个队列
                //而一个小屏与叫号器是一一对应，所以一个小屏只能查询到一个队列
                if (triageType.equals("1")) {
                    objects=queueRepository.queryAllByNurse(ip);
                    for(Object o : objects){
                        BigDataDto dto=new BigDataDto();
                        dto= modleToDto((Object[]) o);
                        dtos.add(dto);
                    }
                }else{
                    //科室
                    objects=queueRepository.queryAllByMachine(ip);
                    for(Object o : objects){
                        BigDataDto dto=new BigDataDto();
                        dto= modleToDto((Object[]) o);
                        dtos.add(dto);
                    }
                }
                if(objects.size()==0){
                    result.put("msg","查询当前分诊台队列为空");
                    return resultMap;
                }else{
                    //诊室数量，一个队列对应一个诊室
                    resultMap.put("zscount",dtos.size());
                    if (triageType.equals("1")) {
                        //获取医生模式大屏数据
                        for(BigDataDto dto:dtos){
                            if(dto.getQueueId()==0){
                                result.put("msg",dto.getZsmc()+"诊室医生未登录");
                                result.put("zscount",dtos.size()-1);
                            }else{
                                List<SickDto> finalResult = new ArrayList<>();
                                Map<String,Long> params = new HashMap<>();
                                Map<String,Object> map=new HashMap();
                                params.put("queueId",dto.getQueueId());
                                params.put("nurseId",triageId);
                                //获取当前就诊人,一个队列至多只有一个正在就诊
                                treatSick=queueService.geTreatSick(dto.getQueueId(),0);
                                dtoList=getPaientList(params,request);
                                map.put("now",treatSick);
                                map.put("wait",dtoList);
                                map.put("zsmc",dto.getZsmc());
                                map.put("waitCount",dtoList.size());
                                map.put("doctor",FastJsonConvertUtil.convertJSONToObject(getDoctor(doctor,dto.getLoginId()),DoctorManger.class));
                                map.put("queueId",dtos.get(0).getQueueId());
                                big.add(map);
                            }
                        }

                        if (big.size()!=0) {
                            result.put("data",big);
                        }
                    }else{
                        //获取科室模式大屏数据
                        for(BigDataDto dto:dtos){
                            if(dto.getQueueId()==0){
                                result.put("msg",dto.getZsmc()+"诊室医生未登录");
                                result.put("zscount",dtos.size()-1);
                            }else{
                                List<SickDto> finalResult = new ArrayList<>();
                                Map<String,Long> params = new HashMap<>();
                                Map<String,Object> map=new HashMap();
                                params.put("nurseId",triageId);
                                params.put("queueId",dto.getQueueId());
                                //获取当前就诊人,一个队列至多只有一个正在就诊
                                treatSick=queueService.geTreatSick(dto.getQueueId(),0);
                                dtoList=getPaientList(params,request);
                                map.put("now",treatSick);
                                map.put("wait",dtoList);
                                map.put("waitCount",dtoList.size());
                                map.put("zsmc",dto.getZsmc());
                                map.put("doctor",FastJsonConvertUtil.convertJSONToObject(getDoctor(doctor,dto.getLoginId()),DoctorManger.class));
                                map.put("queueId",dtos.get(0).getQueueId());
                                big.add(map);
                            }

                        }

                        if (big.size()!=0) {
                            result.put("data",big);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 小屏数据来源
     * @param request
     * @return
     */
    @GetMapping("small")
    public Map smallRoom(HttpServletRequest request){
        Map<String,Object> result=new HashMap();
        Map<String,Object> map=new HashMap();
        Map<String,Object> sickData=new HashMap();
        List<BigDataDto> dtos = new ArrayList<>();
        List<String> big=new ArrayList<>();
        List<String> pager=new ArrayList<>();
        List<Object> objects =new ArrayList<>();
        List<SickDto> treatSick = new ArrayList<SickDto>();
        List<SickDto> dtoList = new ArrayList<>();
        ConsultationRoom room = new ConsultationRoom();
        DoctorManger doctor=new DoctorManger();
        String ip="";
        String triageType="";
        String triageIp="";
        String doctorId="";
        long triageId=0;
        ip=tool.getIpAddr(request);
        if (!(ip.equals(""))) {
            room = roomRepository.findByIp(ip);
        }
        if(room ==null){
            result.put("msg","查询屏幕失败");
        }else{
            Object[] object = triageRepository.findNurseTriageByRoom(ip);
            if(object.length==0){
                //result.put("msg","医生暂未登陆叫号器");
                result.put("msg","faild");
            }else{
                Object[] oo=(Object[])object[0];
                triageIp=(String) oo[0];
                triageType=(String) oo[1];
                triageId=Long.valueOf(String.valueOf(oo[2]));
                doctorId=String.valueOf(oo[3]);
                //根据分诊台查询所属队列,多个叫号器可以绑定一个大屏，叫号器与医生关联，医生与队列关联，所以能查询到多个队列
                //而一个小屏与叫号器是一一对应，所以一个小屏只能查询到一个队列
                if (triageType.equals("1")) {
                    objects=queueRepository.queryAllByNurse(ip);
                    for(Object o : objects){
                        BigDataDto dto=new BigDataDto();
                        dto= modleToDto((Object[]) o);
                        dtos.add(dto);
                    }
                }else{
                    //科室
                    objects=queueRepository.queryAllByMachine(ip);
                    for(Object o : objects){
                        BigDataDto dto=new BigDataDto();
                        dto= modleToDto((Object[]) o);
                        dtos.add(dto);
                    }
                }
                if(objects.size()==0){
                    result.put("msg","查询当前分诊台队列为空");
                    return resultMap;
                }else{
                    //诊室数量，一个队列对应一个诊室
                    resultMap.put("zscount",dtos.size());
                    if (triageType.equals("1")) {
                        //获取医生模式大屏数据
                        for(BigDataDto dto:dtos){
                            if(dto.getQueueId()==0){
                                result.put("msg","faild");
                                //result.put("msg",dto.getZsmc()+"诊室医生未登录");
                                result.put("zscount",dtos.size()-1);
                            }else{
                                List<SickDto> finalResult = new ArrayList<>();
                                Map<String,Long> params = new HashMap<>();
                                params.put("nurseId",triageId);
                                params.put("queueId",dto.getQueueId());
                                //获取当前就诊人,一个队列至多只有一个正在就诊
                                treatSick=queueService.geTreatSick(dto.getQueueId(),0);
                                try {
                                    dtoList=getPaientList(params,request);
                                } catch (Exception e) {
                                    log.info("分诊台id----->"+params.get("nurseId")+";队列id----->"+params.get("queueId"));
                                    log.info("获取等候队列失败，分诊台规则为空");
                                }
                                if (treatSick.size()>0) {
                                    map.put("now",treatSick);
                                }
                                if (dtoList.size()>0) {
                                    map.put("wait",dtoList);
                                }
                                map.put("waitCount",dtoList.size());
                                map.put("zsmc",dto.getZsmc());
                                big.add(JsonToMap.mapToJson(map));
                            }
                        }

                        if (big.size()!=0) {
                            result.put("data",big);
                        }
                        result.put("doctor", getDoctor(doctor,dtos.get(0).getLoginId()));
                        result.put("queueId",dtos.get(0).getQueueId());
                    }else{
                        //获取科室模式大屏数据
                        for(BigDataDto dto:dtos){
                            if(dto.getQueueId()==0){
                                result.put("msg",dto.getZsmc()+"诊室医生未登录");
                                result.put("zscount",dtos.size()-1);
                            }else{
                                List<SickDto> finalResult = new ArrayList<>();
                                Map<String,Long> params = new HashMap<>();
                                params.put("nurseId",triageId);
                                params.put("queueId",dto.getQueueId());
                                //获取当前就诊人,一个队列至多只有一个正在就诊
                                treatSick=queueService.geTreatSick(dto.getQueueId(),0);
                                dtoList=getPaientList(params,request);
                                if (treatSick.size()>0) {
                                    map.put("now",treatSick);
                                }
                                if (dtoList.size()>0) {
                                    map.put("wait",dtoList);
                                }
                                map.put("waitCount",dtoList.size());
                                map.put("zsmc",dto.getZsmc());
                                big.add(JsonToMap.mapToJson(map));
                            }

                        }

                        if (big.size()!=0) {
                            result.put("data",big);
                        }
                        result.put("doctor", getDoctor(doctor,dtos.get(0).getLoginId()));
                        result.put("queueId",dtos.get(0).getQueueId());
                    }
                }
            }
        }
        return result;
    }


    private BigDataDto modleToDto(Object[] oArray){

        BigDataDto dto=new BigDataDto();
        //object转long
        Long queueid= null;
        if (oArray[0]!=null) {
            queueid = Long.valueOf(String.valueOf(oArray[0]));
            dto.setQueueId(queueid);
        }else{
            dto.setQueueId(0);
        }

        if (oArray[1]!=null) {
            dto.setQueueName((String)oArray[1]);
        }else{
            dto.setQueueName("");
        }
        dto.setZsmc((String)oArray[2]);
        dto.setMachineIp((String)oArray[3]);
        if (oArray[4]!=null) {
            dto.setLoginId((String)oArray[4]);
        }else{
            dto.setLoginId("");
        }
        return dto;
    }

    /**
     * 删除指定key
     * @param resultMap
     * @param obj
     */
    private void deleteKey(Map<String,Object> resultMap,String obj){
        Iterator<String> iter = resultMap.keySet().iterator();
        while(iter.hasNext()){
            if(iter.next().equals(obj)){
                iter.remove();
            }
        }
    }

    /**
     * 根据医生登陆id获取医生信息
     * @param doctor
     * @param loginCode
     * @return
     */
    private  String getDoctor(DoctorManger doctor,String loginCode){
        doctor=doctorRepository.findByCode(loginCode);
        if(null!=doctor){
            return FastJsonConvertUtil.convertObjectToJSON(doctor);
        }else{
            return "";
        }
    }


    /**
     * 叫号器数据来源
     * @param type
     * @param queueId
     * @param request
     * @return
     */
    @PostMapping("machinedatatwo")
    public Map getMachineDataTwo(@RequestParam(value="type") String type,@RequestParam(value="queueId") long queueId,
                              @RequestParam(value="loginCode") String loginCode,HttpServletRequest request){
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        int nowHours = now.get(Calendar.HOUR_OF_DAY);
        Map<String,Long> params=new HashMap<String,Long>();
        Map<String,Object> result=new HashMap<String,Object>();
        try {
            Object[] o= queueRepository.queryNurseByLoginCode(loginCode);
            Object[] oarr=null;
            if(o.length==0){
                result.put("code","1");
                result.put("msg","暂无挂号队列！");
                return result;
            }else{
                oarr=(Object[])o[0];
            }
            Long nurseId;
            nurseId = Long.valueOf(String.valueOf(oarr[0]));
            params.put("queueId",queueId);
            params.put("nurseId",nurseId);
        } catch (NumberFormatException e) {
           log.info("叫号器暂无医生");
        }
        List<SickDto> wait=new ArrayList<>();
        if(type.equals("1")){
            try {
                wait=getPaientList(params,request);
            } catch (Exception e) {
                log.info("获取分诊台规则为空！");
            }
            if(wait.size()>0){
                result.put("count",wait.size());
                result.put("code","0");
                result.put("msg","查询等候患者成功！");
                result.put("data",trans(wait));
            }else{
                result.put("code","1");
                result.put("msg","暂无等候患者！");
            }
        }else{
            List<SickDto> sickDtos=new ArrayList<>();
            long passState=6;
            if (nowHours<13) {
                sickDtos=queueService.getPassSick(queueId,passState,1);
            }else if(nowHours>=13){
                sickDtos=queueService.getPassSick(queueId,passState,2);
            }
            if(sickDtos.size()>0){
                result.put("count",sickDtos.size());
                result.put("code","0");
                result.put("msg","查询过号患者成功！");
                result.put("data",sickDtos);
            }else if(sickDtos.size()==0){
                result.put("code","1");
                result.put("msg","暂无过号患者！");
            }else{
                result.put("code","1");
                result.put("msg","查询过号患者失败！");
            }
        }
        return result;
    }

    @PostMapping("machinedata")
    public Map getMachineData(@RequestParam(value="type") String type,@RequestParam(value="queueId") long queueId,
                              @RequestParam(value="loginCode") String loginCode,HttpServletRequest request){
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        int nowHours = now.get(Calendar.HOUR_OF_DAY);
        Map<String,Long> params=new HashMap<String,Long>();
        Map<String,Object> result=new HashMap<String,Object>();
        try {
            Object[] o= queueRepository.queryNurseByLoginCode(loginCode);
            Object[] oarr=null;
            if(o.length==0){
                result.put("code","1");
                result.put("msg","暂无挂号队列！");
                return result;
            }else{
                oarr=(Object[])o[0];
            }
            Long nurseId;
            nurseId = Long.valueOf(String.valueOf(oarr[0]));
            params.put("queueId",queueId);
            params.put("nurseId",nurseId);
        } catch (NumberFormatException e) {
            log.info("叫号器暂无医生");
        }
        List<SickDto> wait=new ArrayList<>();
        if(type.equals("1")){
            try {
                wait=getPaientList(params,request);
            } catch (Exception e) {
                log.info("获取分诊台规则为空！");
            }
            if(wait.size()>0){
                result.put("count",wait.size());
                result.put("code","0");
                result.put("msg","查询等候患者成功！");
                result.put("data",wait);
            }else{
                result.put("code","1");
                result.put("msg","暂无等候患者！");
            }
        }else{
            List<SickDto> sickDtos=new ArrayList<>();
            long passState=6;
            if (nowHours<13) {
                sickDtos=queueService.getPassSick(queueId,passState,1);
            }else if(nowHours>=13){
                sickDtos=queueService.getPassSick(queueId,passState,2);
            }
            if(sickDtos.size()>0){
                result.put("count",sickDtos.size());
                result.put("code","0");
                result.put("msg","查询过号患者成功！");
                result.put("data",sickDtos);
            }else if(sickDtos.size()==0){
                result.put("code","1");
                result.put("msg","暂无过号患者！");
            }else{
                result.put("code","1");
                result.put("msg","查询过号患者失败！");
            }
        }
        return result;
    }
    /**
     * 扫码报到
     * @return
     */
    @PostMapping("scan")
    public Map scanCode(@RequestParam(value="sickCode") String sickCode){
        log.info("患者卡号为："+sickCode);
        Map<String,Object> result=new HashMap<>();
        List<ScanDto> reportDtos=new ArrayList<>();
        reportDtos=queueService.getSickByScan(sickCode);
        result.put("return_code","success");
        result.put("count",reportDtos.size());
        result.put("list",reportDtos);
        return result;
    }

    /**
     * 患者报到
     * @return
     */
    @PostMapping("reportSick")
    public Map reportSick( @RequestParam(value="sickState") int sickState,@RequestParam(value="queueId") long queueId,@RequestParam(value="id") long id,
           @RequestParam(value="isReport") int isReport){
        Map<String,Object> result=new HashMap<>();
        String oprtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //上午的号，不能下午报道；反之
        Calendar c = Calendar.getInstance();
        int h=c.get(Calendar.HOUR_OF_DAY);
        if (isReport==1) {
            Integer flag=queueService.reportSick(oprtime,id);
            if(flag==1){
                result.put("msg","报到成功！");
                result.put("return_code","success");
            }else{
                result.put("msg","报到失败！");
            }
        }
        if(isReport==2) {
            //复诊报道
            if (sickState == 9) {
                queueService.operateSicK(id, queueId, 2);
                result.put("msg", "复诊报到成功！");
                result.put("return_code","success");
            } else if (sickState == 6) {
                //过号签到变为未到过号，按规则进入队列
                queueService.operateSicK(id, queueId, 5);
                result.put("msg", "过号报到成功！");
                result.put("return_code","success");
            } else {
                result.put("msg", "已报到，请勿重复报到！");
            }
        }
        //0-全天，1-上午，2-下午
        /*if (dto.getIsReport()==1) {
            if(h<=12){
                //上午
                if(dto.getTimerBreak()==2){
                    result.put("msg","不在报到时间，你的报到时间是下午！");
                }else{
                    Integer flag=queueService.reportSick(oprtime,dto.getId());
                    if(flag==1){
                        result.put("msg","报到成功！");
                    }else{
                        result.put("msg","报到失败！");
                    }
                }
            }else if(h>12){
                if(dto.getTimerBreak()==1){
                    result.put("msg","不在报到时间，你的报到时间是上午！");
                }else{
                    Integer flag=queueService.reportSick(oprtime,dto.getId());
                    if(flag==1){
                        result.put("msg","报到成功！");
                    }else{
                        result.put("msg","报到失败！");
                    }
                }
            }
        }else if(dto.getIsReport()==2){
            //复诊报道
            if(dto.getSickState()==9){
                queueService.operateSicK(dto.getId(), dto.getQueueId(), 2);
                result.put("msg","复诊报到成功！");
            }else if(dto.getSickState()==6){
                //过号签到变为未到过号，按规则进入队列
                queueService.operateSicK(dto.getId(), dto.getQueueId(), 5);
                result.put("msg","过号报到成功！");
            }else{
                result.put("msg","已报到，请勿重复报到！");
            }
        }*/
        return result;
    }


    private Map dtosToMap(Map<String,Object> map, SickDto dtos){
        map.put("sickName",dtos.getSickName());
        map.put("sickNumber",dtos.getSickName());
        map.put("sickState",dtos.getSickName());
        return map;
    }

    @GetMapping("big")
    public Map BigRoom(HttpServletRequest request){
        Map<String,Object> result=new HashMap();
        Map<String,Object> sickData=new HashMap();
        List<BigDifDto> dtos = new ArrayList<>();
        List<Map> big=new ArrayList<>();
        List<String> pager=new ArrayList<>();
        List<Object> objects =new ArrayList<>();
        List<SickDto> treatSick = new ArrayList<SickDto>();
        List<SickDto> dtoList = new ArrayList<>();
        ConsultationRoom room = new ConsultationRoom();
        DoctorManger doctor=new DoctorManger();
        String ip="";
        String triageType="";
        String triageIp="";
        String doctorId="";
        long triageId=0;
        ip=tool.getIpAddr(request);
        if (!(ip.equals(""))) {
            room = roomRepository.findByIp(ip);
        }
        if(room ==null){
            result.put("msg","查询屏幕失败");
        }else{
            Object[] object = triageRepository.findNom(ip);
            if(object.length==0){
                result.put("msg","医生暂未登陆叫号器");
            }else{
                doctorId=String.valueOf(object[0]);
                //根据分诊台查询所属队列,多个叫号器可以绑定一个大屏，叫号器与医生关联，医生与队列关联，所以能查询到多个队列
                //而一个小屏与叫号器是一一对应，所以一个小屏只能查询到一个队列
                objects=queueRepository.queryAllZs(ip);
                for(Object o : objects){
                    BigDifDto dto=new BigDifDto();
                    dto= modleDifToDto((Object[]) o);
                    dtos.add(dto);
                }

                if(objects.size()==0){
                    result.put("msg","查询当前分诊台队列为空");
                    return resultMap;
                }else{
                    //诊室数量，一个队列对应一个诊室
                    resultMap.put("zscount",dtos.size());
                        //获取医生模式大屏数据
                        for(BigDifDto dto:dtos){
                            if(dto.getQueueId()==0){
                                result.put("msg",dto.getZsmc()+"诊室医生未登录");
                                result.put("zscount",dtos.size()-1);
                            }else{
                                List<SickDto> finalResult = new ArrayList<>();
                                Map<String,Long> params = new HashMap<>();
                                Map<String,Object> map=new HashMap();
                                params.put("queueId",dto.getQueueId());
                                params.put("nurseId",dto.getNursId());
                                //获取当前就诊人,一个队列至多只有一个正在就诊
                                treatSick=queueService.geTreatSick(dto.getQueueId(),0);
                                try {
                                    dtoList=getPaientList(params,request);
                                } catch (Exception e) {
                                    log.info("获取等候队列失败，分诊台规则为空！");
                                }
                                map.put("now",treatSick);
                                map.put("wait",dtoList);
                                map.put("zsmc",dto.getZsmc());
                                map.put("waitCount",dtoList.size());
                                map.put("doctor",FastJsonConvertUtil.convertJSONToObject(getDoctor(doctor,dto.getLoginId()),DoctorManger.class));
                                map.put("queueId",dtos.get(0).getQueueId());
                                big.add(map);
                            }
                        }

                        if (big.size()!=0) {
                            result.put("data",big);
                        }

                }
            }
        }
        return result;
    }

    private BigDifDto modleDifToDto(Object[] oArray){

        BigDifDto dto=new BigDifDto();
        //object转long
        Long queueid= null;
        if (oArray[0]!=null) {
            queueid = Long.valueOf(String.valueOf(oArray[0]));
            dto.setQueueId(queueid);
        }else{
            dto.setQueueId(0);
        }

        if (oArray[1]!=null) {
            dto.setQueueName((String)oArray[1]);
        }else{
            dto.setQueueName("");
        }
        dto.setZsmc((String)oArray[2]);
        dto.setMachineIp((String)oArray[3]);
        if (oArray[4]!=null) {
            dto.setLoginId((String)oArray[4]);
        }else{
            dto.setLoginId("");
        }

        Long triageId= null;
        if (oArray[0]!=null) {
            triageId = Long.valueOf(String.valueOf(oArray[5]));
            dto.setNursId(triageId);
        }else{
            dto.setNursId(0);
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
