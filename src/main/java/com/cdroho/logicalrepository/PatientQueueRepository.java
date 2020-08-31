package com.cdroho.logicalrepository;

import com.cdroho.modle.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 获取不同的患者队列
 * JpaRepository必须指定一个类型，该类型必须是@Entity标注
 * @author HZL
 */
@Repository
public interface PatientQueueRepository extends JpaRepository<Person,Integer> {

    /**
     * 指定一个Person类型，就必须包含一条查询此类型的语句
     * @param name
     * @return
     */
    //@Query(value = "SELECT * FROM PERSON WHERE name=?", nativeQuery = true)
    //Person findByName(String name);

    /**
     * 锁定队列于医生
     * @param loginId
     * @param queueId
     * @return
     */
    @Query(value = "SELECT a.id, a.patient_id, a.register_id, a.patient_name, a.state_patient, a.time_interval, a.is_display, a.patient_source_code, a.queue_type_id, a.opr_time, a.late_lock, a.register_id2, a.state_patient2 FROM patient_queue a LEFT JOIN queue_type b ON a.queue_type_id = b.queue_type_id LEFT JOIN triage c ON b.triage_id = c.triage_id LEFT JOIN doctor d ON a.doctor_id = d.doctor_id WHERE a.state_patient IN (0,2,3, 4,5, 6, 7,8,9,54) AND b.queue_type_id = ? AND a.is_display = 2 AND a.is_deleted = 0 AND a.late_lock=1 ORDER BY a.opr_time",
            nativeQuery = true)
    List<Object> getLockedForDoctorList(@Param("loginId") String loginId,@Param("queueTypeId") String queueId);

    /**
     * 锁定队列于科室
     * @param ip
     * @param queueId
     * @return
     */
    @Query(value = "select a.id, a.patient_id, a.register_id, a.state_patient, a.patient_name, a.time_interval, a.is_display, a.queue_type_id, a.patient_source_code, a.opr_time, a.late_lock, a.register_id2,selectTQrule a.state_patient2 FROM patient_queue a INNER JOIN queue_type b ON a.queue_type_id=b.queue_type_id INNER JOIN rlt_pager2queue_type c ON c.queue_type_id=b.queue_type_id INNER JOIN pager d ON d.id=c.pager_id WHERE d.ip=? and a.queue_type_id=? AND a.state_patient IN (0,2,3, 4,5, 6, 7,8,54) AND a.is_display = '2' and a.is_deleted=0 and a.late_lock=1 GROUP BY a.patient_source_code ORDER BY a.opr_time",
            nativeQuery = true)
    List<Object> getLockedForPagerList(@Param("ip") String ip,@Param("queueTypeId") String queueId);

    /**
     * 初诊队列于医生
     * @return
     */
    //@Query(value = "")
    //List<Object> getChuZhenForDoctorList();

    /**
     * 初诊队列于科室
     * @return
     */
    //@Query(value = "")
    //List<Object> getChuZhenForPagerList();

    /**
     * 复诊队列
     */
    //@Query(value = "")
    //List<Object> getReturnList();

    /**
     * 过号队列
     */
    //@Query(value = "")
    //List<Object> getPassList();

}
