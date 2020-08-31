package com.cdroho.logicalrepository;

import com.cdroho.modle.NurseTriage;
import com.cdroho.modle.SickMans;
import com.cdroho.modle.dto.SortRuleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 等候患者Dao
 * @author HZL
 * @date 2020-4-3
 * 不能含有不含sql的空方法。否则会报错
 */
@Repository
public interface WaitSickRespository extends JpaRepository<SickMans,Long> {
    /**
     * 正常等候患者(医生模式)
     * @return
     */
    @Query(value = "select s.id,s.sick_name as sickName,s.sick_number as sickNumber,s.sick_state as sickState,s.sick_code as sickCode," +
            "s.is_lock as isLock,s.his_time as hisTime,s.call_time as callTime, d.doctor_name as doctorName,s.opr_time as oprTime,s.time_break as timeBreak,s.call_count as callCount " +
            "from sickqueue s left join queuelist q on s.queue_id=q.id left join nursetriage n " +
            "on q.nurse_id=n.id left join doctor d on s.doctor_id=d.id where s.queue_id=? and s.sick_state=0 and s.is_report=2 and s.is_lock=0 and s.time_break=? ORDER BY s.time_break ASC," +
            "s.sick_number ASC,s.opr_time,s.his_time",
            nativeQuery = true)
    List<Object> getNomWaitQueueById(long queueId,int timeBreak);


    @Query(value = "select s.id,s.sick_name as sickName,s.sick_number as sickNumber,s.sick_state as sickState,s.sick_code as sickCode," +
            "s.is_lock as isLock,s.his_time as hisTime,s.call_time as callTime, d.doctor_name as doctorName,s.opr_time as oprTime,s.time_break as timeBreak,s.call_count as callCount " +
            "from sickqueue s left join machine_queue q on s.queue_id=q.queue_id left join machine n " +
            "on q.machine_id=n.id left join doctor d on s.doctor_id=d.id where s.queue_id=? and s.sick_state=0 and s.is_report=2 and s.is_lock=0 ORDER BY s.time_break ASC," +
            "s.sick_number ASC,s.opr_time,s.his_time",
            nativeQuery = true)
    List<Object> getNomWaitQueueByMachine(long queueId);


    @Query(value ="select s.id,s.sick_name as sickName,s.sick_number as sickNumber,s.sick_state as sickState,s.sick_code as sickCode,s.is_lock as isLock,s.his_time as hisTime,s.call_time as callTime, d.doctor_name as doctorName,s.opr_time as oprTime,s.time_break as timeBreak,s.call_count as callCount  from sickqueue s " +
            "left join queuelist q on s.queue_id=q.id left join doctor d on s.doctor_id = d.id " +
            "where s.queue_id=? and s.is_report=2 and s.is_lock=1 and s.sick_state in(0,1,2,3) and s.time_break=? ORDER BY s.time_break,s.opr_time,s.sick_number ASC,s.his_time" ,nativeQuery = true)
    List<Object> getLockedSickById(long queueId,int timeBreak);

    @Query(value ="select s.id,s.sick_name as sickName,s.sick_number as sickNumber,s.sick_state as sickState,s.sick_code as sickCode,s.is_lock as isLock,s.his_time as hisTime,s.call_time as callTime, d.doctor_name as doctorName,s.opr_time as oprTime,s.time_break as timeBreak,s.call_count as callCount  from sickqueue s " +
            "left join machine_queue q on s.queue_id=q.queue_id left join machine m on q.machine_id=m.id left join doctor d on s.doctor_id = d.id " +
            "where s.queue_id=? and s.is_report=2 and s.is_lock=1 and s.sick_state in(0,1,2,3) ORDER BY s.time_break,,s.opr_time,s.sick_number ASC,s.his_time" ,nativeQuery = true)
    List<Object> getLockedSickByMachine(long queueId);


    @Query(value ="select s.id,s.sick_name as sickName,s.sick_number as sickNumber,s.sick_state as sickState,s.sick_code as sickCode,s.is_lock as isLock,s.his_time as hisTime,s.call_time as callTime, d.doctor_name as doctorName,s.opr_time as oprTime,s.time_break as timeBreak,s.call_count as callCount  from sickqueue s " +
            "left join queuelist q on s.queue_id=q.id left join doctor d on s.doctor_id = d.id " +
            "where s.queue_id=? and s.is_report=2 and s.sick_state =2 and s.is_lock=0 and s.time_break=? ORDER BY s.time_break,s.row_number" ,nativeQuery = true)
    List<Object> getAginSickById(long queueId,int timeBreak);



    @Query(value ="select s.id,s.sick_name as sickName,s.sick_number as sickNumber,s.sick_state as sickState,s.sick_code as sickCode,s.is_lock as isLock,s.his_time as hisTime,s.call_time as callTime, d.doctor_name as doctorName,s.opr_time as oprTime,s.time_break as timeBreak,s.call_count as callCount  from sickqueue s " +
            "left join machine_queue q on s.queue_id=q.queue_id left join machine m on q.machine_id=m.id left join doctor d on s.doctor_id = d.id " +
            "where s.queue_id=? and s.is_report=2 and s.sick_state =2 and s.is_lock=0" ,nativeQuery = true)
    List<Object> getAginSickByMachine(long queueId);


    @Query(value ="select s.id,s.sick_name as sickName,s.sick_number as sickNumber,s.sick_state as sickState,s.sick_code as sickCode,s.is_lock as isLock,s.his_time as hisTime,s.call_time as callTime, d.doctor_name as doctorName,s.opr_time as oprTime,s.time_break as timeBreak,s.call_count as callCount  from sickqueue s " +
            "left join queuelist q on s.queue_id=q.id left join doctor d on s.doctor_id = d.id " +
            "where s.queue_id=? and s.is_report=2 and s.sick_state =? and s.is_lock=0 and s.time_break=?" ,nativeQuery = true)
    List<Object> getPassSickById(long queueId,long state,int timeBreak);


    @Query(value ="select s.id,s.sick_name as sickName,s.sick_number as sickNumber,s.sick_state as sickState,s.sick_code as sickCode,s.is_lock as isLock,s.his_time as hisTime,s.call_time as callTime, d.doctor_name as doctorName,s.opr_time as oprTime,s.time_break as timeBreak,s.call_count as callCount  from sickqueue s " +
            "left join machine_queue q on s.queue_id=q.queue_id left join machine m on q.machine_id=m.id left join doctor d on s.doctor_id = d.id " +
            "where s.queue_id=? and s.is_report=2 and s.sick_state =? and s.is_lock=0" ,nativeQuery = true)
    List<Object> getPassSickByMachine(long queueId,long state);


    @Query(value ="select s.id,s.sick_name as sickName,s.sick_number as sickNumber,s.sick_state as sickState,s.sick_code as sickCode,s.is_lock as isLock,s.his_time as hisTime,s.call_time as callTime, d.doctor_name as doctorName,s.opr_time as oprTime,s.time_break as timeBreak,s.call_count as callCount  from sickqueue s " +
            "left join queuelist q on s.queue_id=q.id left join doctor d on s.doctor_id = d.id " +
            "where s.queue_id=? and s.is_report=2 and s.sick_state=5 and s.is_lock=0 and s.time_break=? ORDER BY s.time_break,s.sick_number ASC,s.opr_time,s.his_time" ,nativeQuery = true)
    List<Object> getFirstSickById(long queueId,int timeBreak);


    @Query(value ="select s.id,s.sick_name as sickName,s.sick_number as sickNumber,s.sick_state as sickState,s.sick_code as sickCode,s.is_lock as isLock,s.his_time as hisTime,s.call_time as callTime, d.doctor_name as doctorName,s.opr_time as oprTime,s.time_break as timeBreak,s.call_count as callCount  from sickqueue s " +
            "left join machine_queue q on s.queue_id=q.queue_id left join machine m on q.machine_id=m.id left join doctor d on s.doctor_id = d.id " +
            "where s.queue_id=? and s.is_report=2 and s.sick_state=5 and s.is_lock=0 ORDER BY s.time_break,s.sick_number ASC,s.opr_time,s.his_time" ,nativeQuery = true)
    List<Object> getFirstSickByMachine(long queueId);



    @Query(value = "select n.lock_count as lockCount,n.pass_count as passCount,n.pass_weight as passWeight,n.return_weight as returnWeight,m.again_be_compared as againBeCompared,m.pass_be_compared as passBeCompared from queuelist q " +
            "left join nursetriage n on q.nurse_id =n.id left join doctorwithqueue mq on q.id=mq.queue_id left join machine m on mq.doctor_id=m.doctor_id where q.id=? and n.triage_ip=?",nativeQuery = true)
    Object getSortRuleById(long queueId,String triageIp);


    @Query(value = "select n.lock_count as lockCount,n.pass_count as passCount,n.pass_weight as passWeight,n.return_weight as returnWeight,m.again_be_compared as againBeCompared,m.pass_be_compared as passBeCompared from queuelist q " +
            "left join nursetriage n on q.nurse_id =n.id left join machine_queue mq on q.id=mq.queue_id left join machine m on mq.machine_id=m.id where q.id=? and n.triage_ip=?",nativeQuery = true)
    Object getSortRuleByIdMachine(long queueId,String triageIp);

    @Query(value ="select s.id,s.sick_name as sickName,s.sick_number as sickNumber,s.sick_state as sickState,s.sick_code as sickCode,s.is_lock as isLock,s.his_time as hisTime,s.call_time as callTime, d.doctor_name as doctorName,s.opr_time as oprTime,s.time_break as timeBreak,s.call_count as callCount  from sickqueue s " +
            "left join queuelist q on s.queue_id=q.id left join doctor d on s.doctor_id = d.id " +
            "where s.queue_id=? and s.is_report=1 and s.sick_state in(0,1,2,3,5) and s.is_lock=0 ORDER BY s.time_break,s.sick_number ASC" ,nativeQuery = true)
    List<Object> getNoReportSickById(long queueId);

    @Query(value ="select s.id,s.sick_name as sickName,s.sick_number as sickNumber,s.sick_state as sickState,s.sick_code as sickCode,s.is_lock as isLock,s.his_time as hisTime,s.call_time as callTime, d.doctor_name as doctorName,s.opr_time as oprTime,s.time_break as timeBreak,s.call_count as callCount  from sickqueue s " +
            "left join machine_queue q on s.queue_id=q.queue_id left join machine m on q.machine_id=m.id left join doctor d on s.doctor_id = d.id  " +
            "where s.queue_id=? and s.is_report=1 and s.sick_state in(0,1,2,3,5) and s.is_lock=0" ,nativeQuery = true)
    List<Object> getNoReportSickByMachine(long queueId);


    @Query(value ="select s.id,s.sick_name as sickName,s.sick_number as sickNumber,s.sick_state as sickState,s.sick_code as sickCode,s.is_lock as isLock,s.his_time as hisTime,s.call_time as callTime, d.doctor_name as doctorName,s.opr_time as oprTime,s.time_break as timeBreak,s.call_count as callCount  from sickqueue s " +
            "left join queuelist q on s.queue_id=q.id left join doctor d on s.doctor_id = d.id " +
            "where s.queue_id=? and s.id=? and s.is_report=2 and s.sick_state=8 ORDER BY s.time_break,s.sick_number ASC,s.opr_time,s.his_time" ,nativeQuery = true)
    List<Object> geTreatSick(long queueId,long sickId);


    @Query(value ="select s.id,s.sick_name as sickName,s.sick_number as sickNumber,s.sick_state as sickState,s.sick_code as sickCode,s.is_lock as isLock,s.his_time as hisTime,s.call_time as callTime, d.doctor_name as doctorName,s.opr_time as oprTime,s.time_break as timeBreak,s.call_count as callCount  from sickqueue s " +
            "left join queuelist q on s.queue_id=q.id left join doctor d on s.doctor_id = d.id " +
            "where s.queue_id=? and s.is_report=2 and s.sick_state=8 ORDER BY s.time_break,s.sick_number ASC,s.opr_time,s.his_time" ,nativeQuery = true)
    List<Object> geTreatSickNoId(long queueId);

    @Query(value ="select s.id,s.sick_name as sickName,s.sick_number as sickNumber,s.sick_state as sickState,s.sick_code as sickCode,s.is_lock as isLock,s.his_time as hisTime,s.call_time as callTime, d.doctor_name as doctorName,s.opr_time as oprTime,s.time_break as timeBreak,s.call_count as callCount  from sickqueue s " +
            "left join queuelist q on s.queue_id=q.id left join doctor d on s.doctor_id = d.id " +
            "where s.queue_id=? and s.id=? and s.is_report=2 ORDER BY s.time_break,s.sick_number ASC,s.opr_time,s.his_time" ,nativeQuery = true)
    List<Object> geSick(long queueId,long sickId);

    @Query(value ="select s.id,s.sick_name as sickName,q.queue_name as queueName,s.sick_state as sickState,s.sick_number as sickNumber,s.time_break as timerBreak,s.is_report as isReport,s.queue_id as queueId,q.nurse_id as nurseId " +
            ",s.his_time as hisDate,s.begin_time as beginTime,s.last_time as lastTime,s.sick_code as sickCode from sickqueue s " +
            "left join queuelist q on s.queue_id=q.id where sick_code like CONCAT('%',:sickCode,'%') " ,nativeQuery = true)
    List<Object> getSickByScan(String sickCode);

    @Query(value = "SELECT b.ip,b.triage_type FROM room a INNER JOIN nursetriage b ON a.triage_id = b.triage_id WHERE a.ip = ?",nativeQuery = true)
    List<Object> getTriage(String ip);

    @Transactional(rollbackOn =Exception.class )
    @Modifying
    @Query(value = "UPDATE sickqueue SET sick_state = ?,row_number=?,opr_time=?,row_state=? WHERE id = ?;",nativeQuery = true)
    void updateAgain(long sickState,long rowNumber,String oprTime,long rowState,long sickId);

    @Query(value ="SELECT  max(row_number) as maxnumber FROM sickqueue WHERE queue_id=? and row_state=?" ,nativeQuery = true)
    Object getNoReportSickById(long queueId,long sickState);

    @Transactional(rollbackOn =Exception.class )
    @Modifying
    @Query(value = "UPDATE sickqueue SET sick_state = ?,is_lock=0,row_number=null,row_state=null,opr_time=? WHERE id = ?;",nativeQuery = true)
    void updateFirst(long sickState,String oprTime,long sickId);

    @Transactional(rollbackOn =Exception.class )
    @Modifying
    @Query(value = "UPDATE sickqueue SET sick_state = ?,is_lock=0,opr_time=? WHERE id = ?;",nativeQuery = true)
    void updatePass(long sickState,String oprTime,long sickId);

    @Transactional(rollbackOn =Exception.class )
    @Modifying
    @Query(value = "UPDATE sickqueue SET sick_state = ?,is_lock=0,opr_time=? WHERE id = ?;",nativeQuery = true)
    void updateTop(long sickState,String oprTime,long sickId);

    @Transactional(rollbackOn =Exception.class )
    @Modifying
    @Query(value = "UPDATE sickqueue SET sick_state = ?,is_lock=0,opr_time=? WHERE id = ?;",nativeQuery = true)
    void updateNoPass(long sickState,String oprTime,long sickId);

    @Transactional(rollbackOn =Exception.class )
    @Modifying
    @Query(value = "UPDATE sickqueue SET sick_state = ?,is_lock=0,opr_time=? WHERE id = ?;",nativeQuery = true)
    void updateFinish(long sickState,String oprTime,long sickId);

    @Transactional(rollbackOn =Exception.class )
    @Modifying
    @Query(value = "UPDATE sickqueue SET sick_state = ?,is_lock=0,opr_time=?,small_screen_state=?,big_screen_state=?,call_count=?,call_time=? WHERE id = ?;",nativeQuery = true)
    void updateCall(long sickState,String oprTime,long small,long big,long countCall,String callTime,long sickId);

    @Transactional(rollbackOn =Exception.class )
    @Modifying
    @Query(value = "UPDATE sickqueue SET is_lock=1,opr_time=? WHERE id=? and is_lock=0 and queue_id=?",nativeQuery = true)
    void updateLock(String oprTime,long sickId,long queueId);


    @Transactional(rollbackOn =Exception.class )
    @Modifying
    @Query(value = "UPDATE sickqueue SET is_report=2,opr_time=? WHERE id=?",nativeQuery = true)
    Integer updateReport(String oprtime,long sickId);

}
