package com.cdroho.controller;


import com.cdroho.logicalrepository.CheckRepository;
import com.cdroho.logicalrepository.RoomRepository;
import com.cdroho.logicalrepository.TriageRepository;
import com.cdroho.modle.CheckSick;
import com.cdroho.modle.ConsultationRoom;
import com.cdroho.modle.DoctorManger;
import com.cdroho.modle.NurseTriage;
import com.cdroho.modle.dto.*;
import com.cdroho.service.WaitQueueService;
import com.cdroho.utils.*;
import com.cdroho.websocket.ProductWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import com.cdroho.utils.CheckWebSocketCall;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * @date 2020-6-28
 * @author HZL
 */
@CrossOrigin
@RestController
@RequestMapping("window")
public class WindowController {

    private Logger log = LoggerFactory.getLogger(WindowController.class);
    @Autowired
    private Environment environment;
    @Autowired
    private CheckRepository checkRepository;
    @Autowired
    private Tool tool;
    @Autowired
    private CallSickController callSickController;
    @Autowired
    private TriageRepository triageRepository;
    @Autowired
    private CheckWebSocketCall checkWebSocketCall;
    @Autowired
    private PhaWebSocketCall phaWebSocketCall;
    @Autowired
    private WaitQueueService queueService;
    @Autowired
    private RoomRepository roomRepository;
    private SortOne one = new SortOne(0);
    String oprtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    /**
     * 查询指定窗口队列
     * 1、抽血 2、药房
     * @param flag
     * @return
     */
    @PostMapping("queryData")
    public List<CheckSick> queryData(@RequestParam(value = "flag") int flag,
                                     @RequestParam(value = "loginType") String loginType){
        List<CheckSick> checkSicks=new ArrayList<>();
        if (loginType.equals("1")) {
            checkSicks=checkRepository.queryDifData(flag);
        }else{
            checkSicks=checkRepository.queryDifPharmacy(flag);
        }
        return checkSicks;
    }

    /**
     * 窗口报到
     * 抽血 药房
     * @param code
     * @return
     */
    @PostMapping("report")
    public Map report(@RequestParam(value = "code") String code){
        Map<String,Object> resultMap=new HashMap();

        /*//1号窗口
        Integer one= checkRepository.queryDifFlag(1);
        //2号窗口
        Integer two= checkRepository.queryDifFlag(2);
        //3号窗口
        Integer three= checkRepository.queryDifFlag(3);
        //4号窗口
        Integer four= checkRepository.queryDifFlag(4);
        int[] arr={one,two,three,four};
        Arrays.sort(arr);
        for(int i=0;i<arr.length-1;i++){
            if(arr[0]<Integer.parseInt(environment.getProperty("myconfig.windowSicks"))){
                //1窗口报到
                if(arr[0]==one){
                    checkRepository.updateReport(1,code);
                    resultMap.put("msg","1号窗口报到成功！");
                    break;
                }
                //2窗口报到
                if(arr[0]==two){
                    checkRepository.updateReport(2,code);
                    resultMap.put("msg","2号窗口报到成功！");
                    break;
                }
                //3窗口报到
                if(arr[0]==three){
                    checkRepository.updateReport(3,code);
                    resultMap.put("msg","3号窗口报到成功！");
                    break;
                }
                //4窗口报到
                if(arr[0]==four){
                    checkRepository.updateReport(4,code);
                    resultMap.put("msg","4号窗口报到成功！");
                    break;
                }
            }
        }*/
        checkRepository.updateDirReport(code);
        resultMap.put("msg","报到成功！");
        resultMap.put("return_code","success");
        return resultMap;
    }

    /**
     * 扫码报到
     * @param sickCode
     * @return
     */
    @PostMapping("scan")
    public Map scanCode(@RequestParam(value="sickCode") String sickCode){
        Map<String,Object> result=new HashMap<>();
        List<CheckSick> checkSicks=checkRepository.scanSick(sickCode);
        result.put("return_code","success");
        result.put("count",checkSicks.size());
        result.put("list",checkSicks);
        return result;
    }

    /**
     * 窗口叫号器等候队列
     * 1、抽血 2、药房
     * @param flag
     * @return
     */
    @PostMapping("windowDetail")
    public Map windowDetail(@RequestParam(value = "flag") int flag,@RequestParam(value = "loginCode") String loginCode,
                            @RequestParam(value = "type") String type,@RequestParam(value = "loginType") String loginType,
                            HttpServletRequest request){
        Map<String,Object> resultMap=new HashMap();
        if (loginType.equals("1")) {
            if(loginCode.equals("001")){
                if (type.equals("1")) {
                    List<CheckSick> checkSicks1=new ArrayList<>();
                    checkSicks1=getCheck(1,request);
                    resultMap.put("count",checkSicks1.size());
                    resultMap.put("code","0");
                    resultMap.put("msg","查询等候患者成功！");
                    resultMap.put("data",checkSicks1);
                    resultMap.put("ksname","1号窗口");
                }else{
                    List<CheckSick> pass=new ArrayList<>();
                    pass=checkRepository.queryPassData(flag);
                    resultMap.put("count",pass.size());
                    resultMap.put("code","0");
                    resultMap.put("msg","查询过号患者成功！");
                    resultMap.put("data",pass);
                    resultMap.put("ksname","1号窗口");
                }
            }
        }else if(loginType.equals("2")){
            //药房
            if(loginCode.equals("001")){
                if (type.equals("1")) {
                    List<CheckSick> nomSicks=new ArrayList<>();
                    nomSicks=checkRepository.queryNomOfPharmacy(flag);
                    resultMap.put("count",nomSicks.size());
                    resultMap.put("code","0");
                    resultMap.put("msg","查询等候患者成功！");
                    resultMap.put("data",nomSicks);
                    resultMap.put("ksname","1号窗口");
                }else{
                    List<CheckSick> passSicks=new ArrayList<>();
                    passSicks=checkRepository.queryPassOfPharmacy(flag);
                    resultMap.put("count",passSicks.size());
                    resultMap.put("code","0");
                    resultMap.put("msg","查询过号患者成功！");
                    resultMap.put("data",passSicks);
                    resultMap.put("ksname","1号窗口");
                }
            }
            if(loginCode.equals("002")){
                if (type.equals("1")) {
                    List<CheckSick> nomSicks=new ArrayList<>();
                    nomSicks=checkRepository.queryNomOfPharmacy(flag);
                    resultMap.put("count",nomSicks.size());
                    resultMap.put("code","0");
                    resultMap.put("msg","查询等候患者成功！");
                    resultMap.put("data",nomSicks);
                    resultMap.put("ksname","2号窗口");
                }else{
                    List<CheckSick> passSicks=new ArrayList<>();
                    passSicks=checkRepository.queryPassOfPharmacy(flag);
                    resultMap.put("count",passSicks.size());
                    resultMap.put("code","0");
                    resultMap.put("msg","查询过号患者成功！");
                    resultMap.put("data",passSicks);
                    resultMap.put("ksname","2号窗口");
                }
            }
        }else{
            //取报告
            if(loginCode.equals("006")){
                if(type.equals("1")) {
                    List<CheckSick> nomSicks=new ArrayList<>();
                    nomSicks=checkRepository.queryNomReport();
                    resultMap.put("count",nomSicks.size());
                    resultMap.put("code","0");
                    resultMap.put("msg","查询等候患者成功！");
                    resultMap.put("data",nomSicks);
                    resultMap.put("ksname","检验科窗口");
                }else{
                    List<CheckSick> passSicks=new ArrayList<>();
                    passSicks=checkRepository.queryPassOfReport();
                    resultMap.put("count",passSicks.size());
                    resultMap.put("code","0");
                    resultMap.put("msg","查询过号患者成功！");
                    resultMap.put("data",passSicks);
                    resultMap.put("ksname","检验科窗口");
                }
            }
        }


        return resultMap;
    }

    /**
     * 窗口叫号器登陆
     * 1、抽血 2、药房
     * @param loginCode
     * @return
     */
    @PostMapping("windowLogin")
    public Map windowLogin(@RequestParam(value = "loginCode") String loginCode,@RequestParam(value = "type") String type){
        Map<String,Object> resultMap=new HashMap();
        if(loginCode.equals("001")){
            resultMap.put("flag",1);
            resultMap.put("loginCode",loginCode);
            resultMap.put("type",type);
            resultMap.put("code", "success");
        }
        if(loginCode.equals("002")){
            resultMap.put("flag",2);
            resultMap.put("loginCode",loginCode);
            resultMap.put("type",type);
            resultMap.put("code", "success");
        }
        if(loginCode.equals("003")){
            resultMap.put("flag",3);
            resultMap.put("loginCode",loginCode);
            resultMap.put("type",type);
            resultMap.put("code", "success");
        }
        if(loginCode.equals("004")){
            resultMap.put("flag",4);
            resultMap.put("loginCode",loginCode);
            resultMap.put("type",type);
            resultMap.put("code", "success");
        }
        //取报告
        if(loginCode.equals("006")){
            resultMap.put("flag",6);
            resultMap.put("loginCode",loginCode);
            resultMap.put("type",type);
            resultMap.put("code", "success");
        }
        return resultMap;
    }

    /**
     * 获取指定窗口正在抽血的人
     * 1、抽血 2、药房
     * @param flag
     * @return
     */
    @PostMapping("queryNow")
    public Map queryNow(@RequestParam(value = "flag") int flag,@RequestParam(value = "loginType") String loginType){
        Map<String,Object>result= new HashMap<>();
        List<CheckSick> checkSicks=new ArrayList<>();
        if (loginType.equals("1")) {
            checkSicks=checkRepository.queryNowData(flag);
        }else if((loginType.equals("2"))){
            checkSicks=checkRepository.queryNowPharmacy(flag);
        }else{
            checkSicks=checkRepository.queryNowReport();
        }
        if(flag==1){
            result.put("ksname","1号窗口");
        }
        if(flag==2){
            result.put("ksname","2号窗口");
        }
        if(checkSicks.size()>0){
            result.put("msg","查询当前就诊人成功");
            result.put("data",checkSicks.get(0));
        }else{
            result.put("msg","当前就诊人暂无");
        }
        return result;
    }


    @RequestMapping("getCheck")
    public List<CheckSick> getCheck(@RequestParam(value = "flag") int flag, HttpServletRequest request){
        NurseTriage nurseTriage = new NurseTriage();
        //抽血分诊台确定
        nurseTriage=triageRepository.findNurseTriageById(80);
        String ip=nurseTriage.getTriageIp();
        List<List<CheckSick>> temResult = new ArrayList<>();
        List<CheckSick> finalResult=new ArrayList();
        List<CheckSick> result=new ArrayList();
        List<ComparatorQueueOne> specialSick = new ArrayList<>();
        List<CheckSick> waitSick = new ArrayList<>();
        List<CheckSick> lockedSick = new ArrayList<>();
        List<CheckSick> passSick = new ArrayList<>();
        SortRuleDto ruleDto = new SortRuleDto();
        //抽血队列确定
        ruleDto=getSortRuleDto(ip);
        lockedSick=checkRepository.queryLocOfData(flag);
        waitSick=checkRepository.queryNomOfData(flag);
        //未到过号患者
        passSick=checkRepository.queryPassOfData(flag);
        one.setLocation(ruleDto.getReturnWeight());
        if(passSick.size()>0&&ruleDto!=null){
            ComparatorQueueOne com = new ComparatorQueueOne();
            com.setId(2);
            com.setSickDtoList(passSick);
            specialSick.add(com);
        }
        Collections.sort(specialSick, new MyComprator());
        if(specialSick.size()>0){
            result=sort(waitSick,passSick,ruleDto.getAgainBeCompared(),one);
            //初诊不够插
            for (int i = 0; i < specialSick.size(); i++) {
                ComparatorQueueOne ary = specialSick.get(i);
                if (ary.getSickDtoList().size() > 0) {
                    result.addAll(ary.getSickDtoList());
                }
            }
        }else{
            result=waitSick;
        }
        finalResult.addAll(lockedSick);
        finalResult.addAll(result);
        return finalResult;
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
     * @param passSick
     * @param beCompared 当隔二插一的时候为0，当呼叫走一位的时候为1，当呼叫走两位的时候为2
     * @return
     */
    public static List<CheckSick> sort(List<CheckSick> waitSick,List<CheckSick> passSick,int beCompared,SortOne one){
        List<CheckSick> result=new ArrayList();

        for(int i=0;i<waitSick.size();i++) {

            int location = one.getLocation();

            if(beCompared<location) {

                result.add(waitSick.get(i));

                one.setLocation(--location);

            }else {
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
     * 窗口呼叫
     * @param flag
     * @param sickId
     * @param request
     * @return
     */
    @PostMapping("windowCall")
    public Map windowCall(@RequestParam(value = "flag") int flag, @RequestParam(value = "sickId") long sickId,
                          @RequestParam(value = "queueId") long queueId,
                          HttpServletRequest request){
        Map<String,Object> resultMap=new HashMap();
        String machineIp="";
        String triageIp="";
        List<RoomDto> roomDtos = new ArrayList<>();
        machineIp=tool.getIpAddr(request);
        //根据叫号器ip获取屏幕ip
        roomDtos=callSickController.getRoomByMachine(machineIp,request);
        //根据屏幕ip获取分诊台
        Object[] object = triageRepository.findNurseTriageByMachineIp(machineIp);
        Object[] obj=(Object[])object[0];
        triageIp=String.valueOf(obj[0]);
        CopyOnWriteArraySet<ProductWebSocket> webSockets = MySessionModel.getCopyOnWriteArraySet();
        List<CheckSick> finalResult = new ArrayList<CheckSick>();
        //服务端向客户端发送消息
        ProductWebSocket socket = new ProductWebSocket();
        long type=0;
        SortRuleDto ruleDto = new SortRuleDto();
        log.info("分诊台ip为--------------------------->"+triageIp);
        finalResult=checkWebSocketCall.websocketCall(flag,ruleDto,triageIp,queueId,machineIp,sickId,type,request,roomDtos);
        resultMap.put("code","success");
        resultMap.put("data",finalResult);
        return resultMap;
    }

    /**
     * 呼叫器重呼按钮操作
     * @param flag
     * @param request
     * @return
     */
    @PostMapping("windowReCall")
    public Map windowReCall(@RequestParam(value = "flag") int flag, @RequestParam(value = "sickId") long sickId,
                            @RequestParam(value = "queueId") long queueId,
                            HttpServletRequest request){
        Map<String,Object> resultMap=new HashMap();
        Map<String,Long> params = new HashMap<>();
        String machineIp="";
        String triageIp="";
        List<RoomDto> roomDtos = new ArrayList<>();
        machineIp=tool.getIpAddr(request);
        //根据叫号器ip获取屏幕ip
        roomDtos=callSickController.getRoomByMachine(machineIp,request);
        CopyOnWriteArraySet<ProductWebSocket> webSockets = MySessionModel.getCopyOnWriteArraySet();
        //根据屏幕ip获取分诊台
        Object[] object = triageRepository.findNurseTriageByMachineIp(machineIp);
        Object[] obj=(Object[])object[0];
        triageIp=String.valueOf(obj[0]);
        params.put("nurseId",Long.valueOf(String.valueOf(obj[2])));
        List<CheckSick> finalResult = new ArrayList<CheckSick>();
        params.put("sickId",sickId);
        params.put("queueId",queueId);
        long type=0;
        List<CheckSick> treatSick = new ArrayList<>();
        //获取正在就诊的人
        treatSick=checkRepository.queryNowData(flag);
        CheckSick treat=new CheckSick();
        treat=treatSick.get(0);
        type=treat.getCallCount()+1;
        SortRuleDto ruleDto = new SortRuleDto();
        finalResult=checkWebSocketCall.websocketCall(flag,ruleDto,triageIp,queueId,machineIp,sickId,type,request,roomDtos);
        resultMap.put("code","success");
        resultMap.put("data",finalResult);
        return resultMap;
    }

    /**
     * 呼叫器诊结按钮操作
     * @param flag
     * @param request
     * @return
     */
    @PostMapping("windowSetFinish")
    public Map setFinish(@RequestParam(value = "flag") int flag, @RequestParam(value = "sickId") long sickId,
                         HttpServletRequest request){
        Map<String,Object> resultMap=new HashMap();
        String oprtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Integer code;
        code= checkRepository.updateFinish(9,oprtime,sickId);
        if(code==1){
            resultMap.put("code","success");
            resultMap.put("msg","诊结成功！");
        }else{
            resultMap.put("code","fail");
            resultMap.put("msg","诊结失败！");
        }
        return resultMap;
    }

    /**
     * 药房呼叫
     * @param flag
     * @param sickId
     * @param queueId
     * @param request
     * @return
     */
    @PostMapping("phaCall")
    public Map phaCall(@RequestParam(value = "flag") int flag, @RequestParam(value = "sickId") long sickId,
                          @RequestParam(value = "queueId") long queueId,
                          HttpServletRequest request){
        Map<String,Object> resultMap=new HashMap();
        String machineIp="";
        String triageIp="";
        List<RoomDto> roomDtos = new ArrayList<>();
        machineIp=tool.getIpAddr(request);
        //根据叫号器ip获取屏幕ip
        roomDtos=callSickController.getRoomByMachine(machineIp,request);
        //根据屏幕ip获取分诊台
        Object[] object = triageRepository.findNurseTriageByMachineIp(machineIp);
        Object[] obj=(Object[])object[0];
        triageIp=String.valueOf(obj[0]);
        List<CheckSick> finalResult = new ArrayList<CheckSick>();
        //服务端向客户端发送消息
        long type=0;
        SortRuleDto ruleDto = new SortRuleDto();
        finalResult=phaWebSocketCall.websocketCall(flag,ruleDto,triageIp,queueId,machineIp,sickId,type,request,roomDtos);
        resultMap.put("code","success");
        resultMap.put("data",finalResult);
        return resultMap;
    }

    /**
     * 药房重呼
     * @param flag
     * @param sickId
     * @param queueId
     * @param request
     * @return
     */
    @PostMapping("phaReCall")
    public Map phaReCall(@RequestParam(value = "flag") int flag, @RequestParam(value = "sickId") long sickId,
                            @RequestParam(value = "queueId") long queueId,
                            HttpServletRequest request){
        Map<String,Object> resultMap=new HashMap();
        String machineIp="";
        String triageIp="";
        List<RoomDto> roomDtos = new ArrayList<>();
        machineIp=tool.getIpAddr(request);
        //根据叫号器ip获取屏幕ip
        roomDtos=callSickController.getRoomByMachine(machineIp,request);
        //根据屏幕ip获取分诊台
        Object[] object = triageRepository.findNurseTriageByMachineIp(machineIp);
        Object[] obj=(Object[])object[0];
        triageIp=String.valueOf(obj[0]);
        List<CheckSick> finalResult = new ArrayList<CheckSick>();
        long type=0;
        List<CheckSick> treatSick = new ArrayList<>();
        //获取正在就诊的人
        treatSick=checkRepository.queryNowReport();
        CheckSick treat=new CheckSick();
        treat=treatSick.get(0);
        type=treat.getCallCount()+1;
        SortRuleDto ruleDto = new SortRuleDto();
        finalResult=phaWebSocketCall.websocketCall(flag,ruleDto,triageIp,queueId,machineIp,sickId,type,request,roomDtos);
        resultMap.put("code","success");
        resultMap.put("data",finalResult);
        return resultMap;
    }

    /**
     * 药房诊结
     * @param flag
     * @param sickId
     * @param request
     * @return
     */
    @PostMapping("setPhaFinish")
    public Map setPhaFinish(@RequestParam(value = "flag") int flag, @RequestParam(value = "sickId") long sickId,
                         HttpServletRequest request){
        Map<String,Object> resultMap=new HashMap();
        String oprtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Integer code;
        if(flag==0){
            code= checkRepository.updateRepFinish(9,oprtime,sickId);
        }else{
            code= checkRepository.updatePhaFinish(9,oprtime,sickId);
        }
        if(code==1){
            resultMap.put("code","success");
            resultMap.put("msg","诊结成功！");
        }else{
            resultMap.put("code","fail");
            resultMap.put("msg","诊结失败！");
        }
        return resultMap;
    }

    /**
     * 抽血过号
     * @param flag
     * @param sickId
     * @param request
     * @return
     */
    @PostMapping("setPass")
    public Map setPass(@RequestParam(value = "flag") int flag, @RequestParam(value = "sickId") int sickId,
                       HttpServletRequest request){
        Map<String,Object> resultMap=new HashMap();
        List<CheckSick> checkSicks = new ArrayList<>();
        long count=0;
        long type=0;
        String ip = tool.getIpAddr(request);
        Object[] object = triageRepository.findNurseTriageByMachineIp(ip);
        Object[] obj=(Object[])object[0];
        String triageIp=String.valueOf(obj[0]);
        SortRuleDto ruleDto = new SortRuleDto();
        ruleDto=queueService.getSortRuleDto(6444,triageIp,"1");
        checkSicks=checkRepository.queryNowData(flag);
        CheckSick treat=new CheckSick();
        treat=checkSicks.get(0);
        count=treat.getCallCount();
        String oprtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        if(ruleDto.getPassCount()>0){
            if(ruleDto.getPassCount()<count){
                //更新为过号
                checkRepository.updatePass(6,oprtime,sickId);
                resultMap.put("code","success");
                resultMap.put("msg","已超过号次数，进入过号队列！");
            }else{
                //更新未到过号
                checkRepository.updatePass(1,oprtime,sickId);
                resultMap.put("code","success");
                resultMap.put("msg","更新未到过号成功！");
            }
        }

        return resultMap;
    }

    @PostMapping("setPhaPass")
    public Map setPhaPass(@RequestParam(value = "flag") int flag, @RequestParam(value = "sickId") int sickId,
                          HttpServletRequest request){
        Map<String,Object> resultMap=new HashMap();
        String oprtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        List<CheckSick> checkSicks = new ArrayList<>();
        long count=0;
        long type=0;
        String ip = tool.getIpAddr(request);
        Object[] object = triageRepository.findNurseTriageByMachineIp(ip);
        Object[] obj=(Object[])object[0];
        String triageIp=String.valueOf(obj[0]);
        SortRuleDto ruleDto = new SortRuleDto();
        if(flag==0){
            ruleDto=queueService.getSortRuleDto(6445,triageIp,"1");
            checkSicks=checkRepository.queryNowReport();
        }else{
            ruleDto=queueService.getSortRuleDto(6443,triageIp,"1");
            checkSicks=checkRepository.queryNowPharmacy(flag);
        }
        CheckSick treat=new CheckSick();
        treat=checkSicks.get(0);
        count=treat.getCallCount();
        if(count>2){
            //更新为过号
            checkRepository.updatePass(6,oprtime,sickId);
            resultMap.put("code","success");
            resultMap.put("msg","已超过号次数，进入过号队列！");
        }else{
            resultMap.put("msg","未超过号次数，无法过号！");
        }

        return resultMap;
    }

    /**
     * 抽血、药房、检验大屏数据来源
     * @param request
     * @return
     */
    @PostMapping("cxdata")
    public Map cxData(HttpServletRequest request,@RequestParam String loginType){
        Map<String,Object> resultMap=new HashMap();
        if (loginType.equals("1")) {
            resultMap= getData( request, "1");
        }else if(loginType.equals("2")){
            resultMap=  getData(request, "2");
        }else{
            resultMap=  getData(request, "3");
        }
        return resultMap;
    }

    @RequestMapping("getPassPha")
    public List<CheckSick> getPassPha(@RequestParam(value="flag") int flag){
        List<CheckSick> checkSicks=new ArrayList<>();
        checkSicks= checkRepository.queryPassOfPharmacy(flag);
        return checkSicks;
    }

    @RequestMapping("getPassBlo")
    public List<CheckSick> getPassBlo(@RequestParam(value="flag") int flag){
        List<CheckSick> checkSicks=new ArrayList<>();
        checkSicks= checkRepository.queryPassData(flag);
        return checkSicks;
    }

    private BigDataDto modleToDto(Object[] oArray){
        BigDataDto dto=new BigDataDto();
        dto.setZsmc((String)oArray[0]);
        dto.setMachineIp((String)oArray[1]);
        return dto;
    }

    public Map getData(HttpServletRequest request,String type){
        Map<String,Object> resultMap=new HashMap();
        List<BigDataDto> dtos = new ArrayList<>();
        List<Map> big=new ArrayList<>();
        List<CheckSick> treatSick = new ArrayList<CheckSick>();
        List<CheckSick> dtoList = new ArrayList<>();
        List<Object> objects =new ArrayList<>();
        int flag=0;
        String roomIp="";
        ConsultationRoom room = new ConsultationRoom();
        roomIp=tool.getIpAddr(request);
        long triageId=0;
        if (!(roomIp.equals(""))) {
            room = roomRepository.findByIp(roomIp);
        }
        if(room ==null){
            resultMap.put("msg","查询屏幕失败");
        }else {
            Object[] object = checkRepository.findNurseTriageByRoom(roomIp);
            Object[] oo=(Object[])object[0];
            String triageIp=(String) oo[0];
            triageId=Long.valueOf(String.valueOf(oo[2]));
            objects=checkRepository.queryAllByNurse(roomIp);
            for(Object o : objects){
                BigDataDto dto=new BigDataDto();
                dto= modleToDto((Object[]) o);
                dtos.add(dto);
            }
            //诊室数量，一个队列对应一个诊室
            resultMap.put("zscount",dtos.size());
            //获取医生模式大屏数据
            for(BigDataDto dto:dtos){
                List<SickDto> finalResult = new ArrayList<>();
                Map<String,Object> map=new HashMap();
                //获取当前就诊人,一个队列至多只有一个正在就诊
                if(dto.getZsmc().equals("1号窗口")){
                    flag=1;
                }
                if(dto.getZsmc().equals("2号窗口")){
                    flag=2;
                }
                if(dto.getZsmc().equals("3号窗口")){
                    flag=3;
                }
                if(dto.getZsmc().equals("4号窗口")){
                    flag=4;
                }
                if(dto.getZsmc().equals("5号窗口")){
                    flag=5;
                }
                if (type.equals("1")) {
                    treatSick=checkRepository.queryNowData(flag);
                    dtoList=getCheck(flag,request);
                }else if(type.equals("2")){
                    treatSick=checkRepository.queryNowPharmacy(flag);
                    dtoList=checkRepository.queryNomOfPharmacy(flag);
                }else{
                    treatSick=checkRepository.queryNowReport();
                    dtoList=checkRepository.queryNomOfReport();
                }
                map.put("now",treatSick);
                map.put("wait",dtoList);
                map.put("zsmc",dto.getZsmc());
                map.put("waitCount",dtoList.size());
                map.put("queueId",dtos.get(0).getQueueId());
                map.put("flag",flag);
                big.add(map);
            }

            if (big.size()!=0) {
                resultMap.put("data",big);
            }
        }
        return resultMap;
    }
}
