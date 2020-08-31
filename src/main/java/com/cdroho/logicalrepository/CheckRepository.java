package com.cdroho.logicalrepository;

import com.cdroho.modle.CheckSick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


/**
 *抽血dao
 * @author HZL
 * @date 2020-6-28
 */
@Repository
public interface CheckRepository extends JpaRepository<CheckSick,Integer> {
    /**
     * 查询出抽血不同窗口的人数
     * @param flag
     * @return
     */
    @Query(value ="select count(*) from check_sick where flag=? and is_report=2 and business=1" ,nativeQuery = true)
    Integer queryDifFlag(int  flag);

    /**
     * 查询出抽血不同窗口的患者
     * @param flag
     * @return
     */
    @Query(value ="select * from check_sick where flag=? and is_report=2 and business=1" ,nativeQuery = true)
    List<CheckSick> queryDifData(int flag);

    /**
     * 扫码报到
     * @param code
     * @return
     */
    @Query(value ="select * from check_sick where code like CONCAT('%',:code,'%') " ,nativeQuery = true)
    List<CheckSick> scanSick(String code);

    /**
     * 查询出药房不同窗口的患者
     * @param flag
     * @return
     */
    @Query(value ="select * from check_sick where flag=? and is_report=2 and business=2" ,nativeQuery = true)
    List<CheckSick> queryDifPha(int flag);

    /**
     * 查询出正在抽血的患者
     * @param flag
     * @return
     */
    @Query(value ="select * from check_sick where flag=? and is_report=2 and business=1 and state=5" ,nativeQuery = true)
    List<CheckSick> queryNowData(int  flag);

    /**
     * 查询出过号患者
     * @param flag
     * @return
     */
    @Query(value ="select * from check_sick where flag=? and is_report=2 and business=1 and state=6" ,nativeQuery = true)
    List<CheckSick> queryPassData(int  flag);

    /**
     * 查询出未到过号患者
     * @param flag
     * @return
     */
    @Query(value ="select * from check_sick where flag=? and is_report=2 and business=1 and state=1 and is_lock=0" ,nativeQuery = true)
    List<CheckSick> queryPassOfData(int  flag);

    /**
     * 查询出正常患者
     * @param flag
     * @return
     */
    @Query(value ="select * from check_sick where flag=? and is_report=2 and is_lock=0 and business=1 and state=0" ,nativeQuery = true)
    List<CheckSick> queryNomOfData(int  flag);


    /**
     * 查询出正常患者
     * @param flag
     * @return
     */
    @Query(value ="select * from check_sick where flag=? and is_report=2 and business=1 and is_lock=1 order by opr_time asc" ,nativeQuery = true)
    List<CheckSick> queryLocOfData(int  flag);


    @Query(value = "select n.lock_count as lockCount,n.pass_count as passCount,n.pass_weight as passWeight,n.return_weight as returnWeight,m.again_be_compared as againBeCompared,m.pass_be_compared as passBeCompared from nursetriage n " +
            "left join machine m on m.nurse_id=n.id " +
            "where n.triage_ip=?",nativeQuery = true)
    Object getSortRuleById(String triageIp);

    @Query(value = "SELECT b.triage_ip,b.triage_type,b.id FROM room a  " +
            "INNER JOIN nursetriage b ON a.nurse_id = b.id " +
            "INNER join machine_room mr on a.id=mr.room_id " +
            "INNER join machine ma on mr.machine_id=ma.id " +
            "WHERE a.room_ip = ?", nativeQuery = true)
    Object[] findNurseTriageByRoom(String ip);

    @Query(value = "select ma.machine_name as zsmc,ma.machine_ip as machineIp from room r " +
            "left join machine_room mr on r.id=mr.room_id left join machine ma " +
            "on mr.machine_id=ma.id where r.room_ip=?",
            nativeQuery = true)
    List<Object> queryAllByNurse(String roomIp);

    /**
     * 查询出药房正常患者
     * @param flag
     * @return
     */
    @Query(value ="select * from check_sick where flag=? and is_report=2 and business=2 and state=0" ,nativeQuery = true)
    List<CheckSick> queryNomOfPharmacy(int  flag);

    /**
     * 查询出取报告正常等候患者
     * @return
     */
    @Query(value ="select * from check_sick where is_report=2 and business=3 and state=0" ,nativeQuery = true)
    List<CheckSick> queryNomOfReport();

    /**
     * 查询出药房未到过号患者
     * @param flag
     * @return
     */
    @Query(value ="select * from check_sick where flag=? and is_report=2 and business=2 and state=6" ,nativeQuery = true)
    List<CheckSick> queryPassOfPharmacy(int  flag);

    /**
     * 查询出正在取药的患者
     * @param flag
     * @return
     */
    @Query(value ="select * from check_sick where flag=? and is_report=2 and business=2 and state=5" ,nativeQuery = true)
    List<CheckSick> queryNowPharmacy(int  flag);

    /**
     * 查询出正在取报告的患者
     * @return
     */
    @Query(value ="select * from check_sick where is_report=2 and business=3 and state=5" ,nativeQuery = true)
    List<CheckSick> queryNowReport();

    /**
     * 查询出药房不同窗口的患者
     * @param flag
     * @return
     */
    @Query(value ="select * from check_sick where flag=? and is_report=2 and business=2" ,nativeQuery = true)
    List<CheckSick> queryDifPharmacy(int  flag);

    /**
     * 检验
     * @return
     */
    @Query(value ="select * from check_sick where is_report=2 and business=3 and state=0" ,nativeQuery = true)
    List<CheckSick> queryNomReport();

    /**
     * 查询出检验未到过号患者
     * @param
     * @return
     */
    @Query(value ="select * from check_sick where is_report=2 and business=3 and state=6" ,nativeQuery = true)
    List<CheckSick> queryPassOfReport();

    /**
     * 报到0、初诊 1、未到过号 5、正在就诊 6、过号 9、诊结
     * @param flag
     * @param code
     */
    @Transactional(rollbackOn =Exception.class )
    @Modifying
    @Query(value = "UPDATE check_sick SET flag = ?,is_report=2,state=0 WHERE code = code;",nativeQuery = true)
    void updateReport(int flag,String code);

    @Transactional(rollbackOn =Exception.class )
    @Modifying
    @Query(value = "UPDATE check_sick SET is_report=2,state=0 WHERE code = code;",nativeQuery = true)
    void updateDirReport(String code);

    @Transactional(rollbackOn =Exception.class )
    @Modifying
    @Query(value = "UPDATE check_sick SET state = ?,is_lock=0,opr_time=?,call_count=?,call_time=? WHERE id = ?;",nativeQuery = true)
    void updateCall(long sickState,String oprTime,long countCall,String callTime,long sickId);

    @Transactional(rollbackOn =Exception.class )
    @Modifying
    @Query(value = "UPDATE check_sick SET is_lock=1,opr_time=? WHERE id=? and is_lock=0",nativeQuery = true)
    void updateLock(String oprTime,int sickId);

    @Transactional(rollbackOn =Exception.class )
    @Modifying
    @Query(value = "UPDATE check_sick SET state = ?,is_lock=0,opr_time=? WHERE id = ?;",nativeQuery = true)
    Integer updateFinish(int sickState,String oprTime,long sickId);

    @Transactional(rollbackOn =Exception.class )
    @Modifying
    @Query(value = "UPDATE check_sick SET state = ?,is_lock=0,opr_time=?,call_count=?,call_time=? WHERE id = ? and business=2;",nativeQuery = true)
    void updatePhaCall(long sickState,String oprTime,long countCall,String callTime,long sickId);

    /**
     * 取报告，更新正在就诊
     * @param sickState
     * @param oprTime
     * @param countCall
     * @param callTime
     * @param sickId
     */
    @Transactional(rollbackOn =Exception.class )
    @Modifying
    @Query(value = "UPDATE check_sick SET state = ?,is_lock=0,opr_time=?,call_count=?,call_time=? WHERE id = ? and business=3;",nativeQuery = true)
    void updateRepCall(long sickState,String oprTime,long countCall,String callTime,long sickId);

    @Transactional(rollbackOn =Exception.class )
    @Modifying
    @Query(value = "UPDATE check_sick SET is_lock=1,opr_time=? WHERE id=? and is_lock=0 and business=2",nativeQuery = true)
    void updatePhaLock(String oprTime,int sickId);

    @Transactional(rollbackOn =Exception.class )
    @Modifying
    @Query(value = "UPDATE check_sick SET state = ?,is_lock=0,opr_time=? WHERE id = ? and business=2;",nativeQuery = true)
    Integer updatePhaFinish(int sickState,String oprTime,long sickId);

    /**
     * 结诊取报告
     * @param sickState
     * @param oprTime
     * @param sickId
     * @return
     */
    @Transactional(rollbackOn =Exception.class )
    @Modifying
    @Query(value = "UPDATE check_sick SET state = ?,is_lock=0,opr_time=? WHERE id = ? and business=3;",nativeQuery = true)
    Integer updateRepFinish(int sickState,String oprTime,long sickId);

    /**
     * 抽血、药房、检验 过号/未到过号
     * @param sickState
     * @param oprTime
     * @param sickId
     */
    @Transactional(rollbackOn =Exception.class )
    @Modifying
    @Query(value = "UPDATE check_sick SET state = ?,is_lock=0,opr_time=? WHERE id = ?;",nativeQuery = true)
    void updatePass(int sickState,String oprTime,int sickId);

}
