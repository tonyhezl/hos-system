package com.cdroho.service;

import com.cdroho.modle.NurseTriage;
import com.cdroho.modle.dto.*;

import java.util.List;

/**
 * 等候队列业务层
 * @author HZL
 * @date 2020-4-3
 */
public interface WaitQueueService {

    /**
     *
     * @return
     */
    SortRuleDto getSortRuleDto(long queueId,String triageIp,String type);

    /**
     * 锁定患者
     */
    List<SickDto> getLockedSick(long queueId,int timeBreak);

    /**
     * 科室锁定队列
     * @param queueId
     * @return
     */
    List<SickDto> getLockedSickMachine(long queueId);
    /**
     *正常等候患者
     */
    List<SickDto> getNomSick(long queueId,int timeBreak);

    /**
     * 科室等候患者
     * @param queueId
     * @return
     */
    List<SickDto> getNomSickMachine(long queueId);


    /**
     * 复诊患者
     */
    List<SickDto> getAginSick(long queueId,int timeBreak);

    /**
     * 科室复诊患者
     * @param queueId
     * @return
     */
    List<SickDto> getAginSickMachine(long queueId);
    /**
     * 过号患者
     */
    List<SickDto> getPassSick(long queueId,long state,int timeBreak);

    /**
     * 科室过号患者
     * @param queueId
     * @param state
     * @return
     */
    List<SickDto> getPassSickMachine(long queueId,long state);

    /**
     *优先患者
     */
    List<SickDto> getFirstSick(long queueId,int timeBreak);

    /**
     * 科室优先患者
     * @param queueId
     * @return
     */
    List<SickDto> getFirstSickMachine(long queueId);

    /**
     *未报到患者
     */
    List<SickDto> getNoReportSick(long queueId,int timeBreak);

    /**
     * 科室未报到患者
     * @param queueId
     * @return
     */
    List<SickDto> getNoReportSickForMachine(long queueId);

    /**
     * 操作患者优、初、复、过、正在、诊结
     * @param sickId
     * @return
     */
    String operateSicK(long sickId,long queueId,long type);

    /**
     * 更新正在就诊
     */
    Boolean updateSickCall(long sickState,long small,long big,long sickId,long type);

    /**
     * 更新锁定
     */
    Boolean updateLock(long sickId,long queueId);

    /**
     * 正在就诊
     * @param queueId
     * @return
     */
    List<SickDto> geTreatSick(long queueId,long sickId);


    /**
     * 报道查询患者
     * @param sickCode
     * @return
     */
    List<ScanDto> getSickByScan(String sickCode);

    /**
     * get患者
     * @param queueId
     * @param sickId
     * @return
     */
    List<SickDto> geSick(long queueId,long sickId);

    /**
     * 获取分诊台
     * @param ip
     * @return
     */
    NurseTriage getTriage(String ip);

    /**
     * 报道
     * @param oprTime
     * @param sickId
     * @return
     */
    Integer reportSick(String oprTime,long sickId);


}
