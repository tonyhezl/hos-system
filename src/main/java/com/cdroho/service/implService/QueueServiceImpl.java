package com.cdroho.service.implService;

import com.cdroho.domaindto.DomainPatientDto;
import com.cdroho.logicalrepository.PatientQueueRepository;
import com.cdroho.logicalrepository.RuleRepository;
import com.cdroho.service.QueueService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class QueueServiceImpl implements QueueService {

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private PatientQueueRepository queueRepository;

    @Override
    public List<DomainPatientDto> getWaitQueueForDoctor(String queueTypeId, String loginId) {
        return null;
    }

    @Override
    public List<DomainPatientDto> getWaitQueueForPager(String ip,String queueTypeId) {
        return null;
    }

    @Override
    public List<DomainPatientDto> getLockedQueueForDoctor(String queueTypeId, String loginId) {

        List<Object> lockedObject=queueRepository.getLockedForDoctorList(loginId,queueTypeId);

        List<DomainPatientDto> dtoList = new ArrayList<>();

        if(lockedObject.size()>0){

            for (int i=0;i<lockedObject.size();i++) {

                DomainPatientDto patientDto =new DomainPatientDto();

                BeanUtils.copyProperties(lockedObject.get(i),patientDto);

                dtoList.add(patientDto);
            }

        }

        return dtoList;
    }

    @Override
    public List<DomainPatientDto> getlockedQueueForPager(String ip, String queueTypeId) {
        return null;
    }
}
