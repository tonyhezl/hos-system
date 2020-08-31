package com.cdroho.absservice;

import com.cdroho.domaindto.DomainPatientDto;
import com.cdroho.modle.dto.RuleDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 排序模版方法类
 * @author HZL
 */
public abstract class AbstractQueueService {
    /**
     *排序规则参数数组
     */
    Map<String,RuleDto> ruleMap = new HashMap<>();

    /**
     * 获取不同状态的患者
     */
    public List<DomainPatientDto> getAllWaitPatientForQueue(){
        return null;
    }

    /**
     *根据配置文件获取指定队列的排序规则
     */
    public Map<String,RuleDto> getEspecialRuleForTriage(){
        return null;
    }

    /**
     *过号、迟到、复诊、初诊、优先、锁定患者的排序
     */
    public abstract void sortPatientQueue();



}
