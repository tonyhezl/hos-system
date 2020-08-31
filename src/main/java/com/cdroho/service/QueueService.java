package com.cdroho.service;

import com.cdroho.domaindto.DomainPatientDto;
import com.cdroho.modle.Person;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 获取等候队列业务类
 * @author HZL
 */
public interface QueueService {

    /**
     * 获取按医生叫号的等候队列
     * @param queueTypeId
     * @param loginId
     * @return
     */
    List<DomainPatientDto> getWaitQueueForDoctor(String queueTypeId, String loginId);

    /**
     * 获取按科室叫号的等候队列
     * @param queueTypeId
     * @return
     */
    List<DomainPatientDto> getWaitQueueForPager(String ip,String queueTypeId);

    /**
     * 医生锁定队列
     * @param queueTypeId
     * @param loginId
     * @return
     */
    List<DomainPatientDto> getLockedQueueForDoctor(String queueTypeId,String loginId);

    /**
     * 科室锁定队列
     * @param ip
     * @param queueTypeId
     * @return
     */
    List<DomainPatientDto> getlockedQueueForPager(String ip,String queueTypeId);
}
