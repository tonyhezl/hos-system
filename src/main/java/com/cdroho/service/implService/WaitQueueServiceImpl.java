package com.cdroho.service.implService;

import com.cdroho.logicalrepository.WaitSickRespository;
import com.cdroho.modle.NurseTriage;
import com.cdroho.modle.dto.ReportDto;
import com.cdroho.modle.dto.ScanDto;
import com.cdroho.modle.dto.SickDto;
import com.cdroho.modle.dto.SortRuleDto;
import com.cdroho.service.WaitQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 等候患者业务实现类
 * 在SpringBoot的架构中已经存在了cglib的jar包。这时候如果再在POM文件中引入cglib2.2的jar包，
 * 并且在代码中导入使用的是cglib2.2的类，就会导致asm类的版本冲突，导致报错。
 * @author HZL
 * @date 2020-4-3
 */
@SuppressWarnings("all")
@Service
public class WaitQueueServiceImpl implements WaitQueueService {
    @Autowired
    private WaitSickRespository sickRespository;

    private Logger log = LoggerFactory.getLogger(WaitQueueServiceImpl.class);

    String oprtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    @Override
    public List<SickDto> getLockedSick(long queueId,int timeBreak) {
        List<Object> nomWait=sickRespository.getLockedSickById(queueId,timeBreak);
        List<SickDto> sickDtos=getList(nomWait);
        return  sickDtos;
    }

    @Override
    public List<SickDto> getLockedSickMachine(long queueId) {
        List<Object> nomWait=sickRespository.getLockedSickByMachine(queueId);
        List<SickDto> sickDtos=getList(nomWait);
        return  sickDtos;
    }

    @Override
    public List<SickDto> getNomSick(long queueId,int timeBreak) {
        List<Object> nomWait=sickRespository.getNomWaitQueueById(queueId,timeBreak);
        List<SickDto> sickDtos=getList(nomWait);
        return  sickDtos;
    }

    @Override
    public List<SickDto> getNomSickMachine(long queueId) {
        List<Object> nomWait=sickRespository.getNomWaitQueueByMachine(queueId);
        List<SickDto> sickDtos=getList(nomWait);
        return  sickDtos;
    }

    @Override
    public List<SickDto> getAginSick(long queueId,int timeBreak) {
        List<Object> nomWait=sickRespository.getAginSickById(queueId,timeBreak);
        List<SickDto> sickDtos=getList(nomWait);
        return  sickDtos;
    }
    @Override
    public List<SickDto> getAginSickMachine(long queueId) {
        List<Object> nomWait=sickRespository.getAginSickByMachine(queueId);
        List<SickDto> sickDtos=getList(nomWait);
        return  sickDtos;
    }
    @Override
    public List<SickDto> getPassSick(long queueId,long state,int timeBreak) {
        List<Object> nomWait=sickRespository.getPassSickById(queueId,state,timeBreak);
        List<SickDto> sickDtos=getList(nomWait);
        return  sickDtos;
    }
    @Override
    public List<SickDto> getPassSickMachine(long queueId,long state) {
        List<Object> nomWait=sickRespository.getPassSickByMachine(queueId,state);
        List<SickDto> sickDtos=getList(nomWait);
        return  sickDtos;
    }
    //@Cacheable(value="sicks")
    @Override
    public List<SickDto> getFirstSick(long queueId,int timeBreak) {
        List<Object> nomWait=sickRespository.getFirstSickById(queueId,timeBreak);
        List<SickDto> sickDtos=getList(nomWait);
        return  sickDtos;
    }

    @Override
    public List<SickDto> getFirstSickMachine(long queueId) {
        List<Object> nomWait=sickRespository.getFirstSickByMachine(queueId);
        List<SickDto> sickDtos=getList(nomWait);
        return  sickDtos;
    }

    @Override
    public List<SickDto> getNoReportSick(long queueId,int timeBreak) {
        List<Object> noReport=sickRespository.getNoReportSickById(queueId);
        List<SickDto> sickDtos=getList(noReport);
        return  sickDtos;
    }

    @Override
    public List<SickDto> getNoReportSickForMachine(long queueId) {
        List<Object> noReport=sickRespository.getNoReportSickByMachine(queueId);
        List<SickDto> sickDtos=getList(noReport);
        return  sickDtos;
    }

    @Override
    public List<SickDto> geTreatSick(long queueId,long sickId) {
        List<Object> noReport= null;
        if (sickId!=0) {
            noReport = sickRespository.geTreatSick(queueId,sickId);
        }else {
            noReport = sickRespository.geTreatSickNoId(queueId);
        }
        List<SickDto> sickDtos=getList(noReport);
        return  sickDtos;
    }

    @Override
    public Boolean updateSickCall(long sickState, long small, long big,long type, long sickId) {
        String oprtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Boolean falg=false;
        if(type==0){
            sickRespository.updateCall(sickState,oprtime,small,big,type+1,oprtime,sickId);
        }else{
            sickRespository.updateCall(sickState,oprtime,small,big,type,oprtime,sickId);
        }
        falg=true;
        return falg;
    }

    @Override
    public Boolean updateLock(long sickId,long queueId) {
        boolean flag=false;
        sickRespository.updateLock(oprtime,sickId,queueId);
        flag=true;
        return flag;
    }

    @Override
    public String operateSicK(long sickId,long queueId,long type) {
        if(type==1){
            //初诊
            long first = 0;
            try {
                sickRespository.updateFirst(first,oprtime,sickId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (type==2) {
            long max = 0;
            long again = 2;
            Object maxs = null;
            try {
                maxs = sickRespository.getNoReportSickById(queueId,again);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //复诊号序单独重排 从1开始
            if (null==maxs) {
                max = 1;
            } else {
                max = Long.valueOf(String.valueOf(maxs)) + 1;
            }
            try {
                sickRespository.updateAgain(type, max, oprtime, type,sickId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(type==3){
            //过号患者需重新签到，直接进入过号队列
            long pass=6;
            try {
                sickRespository.updatePass(pass,oprtime,sickId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(type==4){
            //优先
            long top = 5;
            try {
                sickRespository.updateTop(top,oprtime,sickId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //未到过号，进入等候队列，按规则插入
        if(type==5){
            long noPass = 1;
            try {
                sickRespository.updateNoPass(noPass,oprtime,sickId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(type==6){
            //诊结
            long finish = 9;
            try {
                sickRespository.updateFinish(finish,oprtime,sickId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "success";
    }

    @Override
    public List<SickDto> geSick(long queueId, long sickId) {
        List<Object> noReport=sickRespository.geSick(queueId,sickId);
        List<SickDto> sickDtos=getList(noReport);
        return  sickDtos;
    }

    @Override
    public List<ScanDto> getSickByScan(String sickCode) {
        List<Object> sick=sickRespository.getSickByScan(sickCode);
        List<ScanDto> reportDtos=getListRep(sick);
        return  reportDtos;
    }

    @Override
    public SortRuleDto getSortRuleDto(long queueId,String triageIp,String type) {
        Object o=null;
        if(type.equals("1")){
             o=sickRespository.getSortRuleById(queueId,triageIp);
        }else{
             o=sickRespository.getSortRuleByIdMachine(queueId,triageIp);
        }
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

    @Override
    public NurseTriage getTriage(String ip) {
        List<Object> objects=sickRespository.getTriage(ip);
        NurseTriage dto=new NurseTriage();
        for(int i=0;i<objects.size();i++){
            Object[] oArray=(Object[]) objects.get(i);
            dto.setTriageIp((String) oArray[0]);
            dto.setTriageType((String) oArray[2]);
        }
        return dto;
    }

    @Override
    public Integer reportSick(String oprTime,long sickId){
        Integer flag;
        flag=sickRespository.updateReport(oprTime,sickId);
        return flag;
    }

    public SickDto ObjectToDto(Object[] oArray, SickDto sickDto){
        Long id=Long.valueOf(String.valueOf(oArray[0]));
        sickDto.setId(id);
        sickDto.setSickName((String) oArray[1]);
        if(oArray[2]!=null){
            sickDto.setSickNumber((int)oArray[2]);
        }
        if(oArray[3]!=null){
            sickDto.setSickState((int)oArray[3]);
        }
        if(oArray[4]!=null){
            sickDto.setSickCode((String) oArray[4]);
        }
        if(oArray[5]!=null){
            sickDto.setIsLock((int)oArray[5]);
        }
        if(oArray[6]!=null){
            sickDto.setHisTime((String) oArray[6]);
        }
        if(oArray[7]!=null){
            sickDto.setCallTime((String) oArray[7]);
        }
        if(oArray[8]!=null){
            sickDto.setDoctorName((String) oArray[8]);
        }
        if(oArray[9]!=null){
            sickDto.setOpr_time((String) oArray[9]);
        }
        if(oArray[10]!=null){
            sickDto.setTimeBreak((int)oArray[10]);
        }
        if(oArray[11]!=null){
            sickDto.setCallCount((int)oArray[11]);
        }
        return sickDto;
    }

    public List<SickDto> getList(List<Object> nomWait){
        List<SickDto> sickDtos=new ArrayList<>();
        if(nomWait.size()>0){

            for (int i=0;i<nomWait.size();i++) {
                Object[] oArray=(Object[]) nomWait.get(i);
                SickDto sickDto=new SickDto();
                sickDto =ObjectToDto(oArray,sickDto);
                sickDtos.add(sickDto);
            }
        }
        return  sickDtos;
    }


    public List<ScanDto> getListRep(List<Object> nomWait){
        List<ScanDto> reportDtos=new ArrayList<>();
        if(nomWait.size()>0){

            for (int i=0;i<nomWait.size();i++) {
                Object[] oArray=(Object[]) nomWait.get(i);
                ScanDto reportDto=new ScanDto();
                reportDto =ObjectToRepDto(oArray,reportDto);
                reportDtos.add(reportDto);
            }
        }
        return  reportDtos;
    }

    public ScanDto ObjectToRepDto(Object[] oArray, ScanDto reportDto){
        Long id=Long.valueOf(String.valueOf(oArray[0]));
        reportDto.setId(id);
        reportDto.setSickName((String) oArray[1]);
        if(oArray[2]!=null){
            reportDto.setQueueName((String) oArray[2]);
        }
        if(oArray[3]!=null){
            reportDto.setSickState((int) oArray[3]);
        }
        if(oArray[4]!=null){
            reportDto.setSickNumber((int)oArray[4]);
        }
        if(oArray[5]!=null){
            reportDto.setTimerBreak((int)oArray[5]);
        }
        if(oArray[6]!=null){
            reportDto.setIsReport((int)oArray[6]);
        }
        Long queueId=Long.valueOf(String.valueOf(oArray[7]));
        reportDto.setQueueId(queueId);
        if(oArray[9]!=null){
            reportDto.setHisDate(String.valueOf(oArray[9]));
        }
        if(oArray[10]!=null){
            reportDto.setBeginTime(String.valueOf(oArray[10]));
        }
        if(oArray[11]!=null){
            reportDto.setLastTime(String.valueOf(oArray[11]));
        }
        if(oArray[12]!=null){
            reportDto.setSickCode(String.valueOf(oArray[12]));
        }
        return reportDto;
    }

}
