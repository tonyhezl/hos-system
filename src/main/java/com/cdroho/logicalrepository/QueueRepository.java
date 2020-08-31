package com.cdroho.logicalrepository;

import com.cdroho.modle.NurseTriage;
import com.cdroho.modle.QueueManger;
import com.cdroho.modle.QueuingMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

/**
 * 医生队列管理
 * @author HZL
 * @date 2020-3-30
 */
@Repository
public interface QueueRepository extends JpaRepository<QueueManger,Integer> {

	/**
	 * 返回一条记录
	 * @param queueId
	 * @return
	 */
	@Query(value = "SELECT * FROM queuelist WHERE id=?", nativeQuery = true)
	QueueManger findQueueMangersById(long queueId);


	@Transactional
	@Modifying
	@Query(value = "delete from queuelist where id=?",nativeQuery = true)
	void deleteQueueMangerById(long queueId);


	/**
	 * 新增医生队列
	 * @param queueName
	 * @param queueProfile
	 * @param floorProfile
	 * @param triageId
	 */
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO queuelist (queue_name, queue_profile,floor_profile,nurse_id) " +
			"VALUES(?,?,?,?)",nativeQuery = true)
	void insertQueueManger(String queueName,String queueProfile,String floorProfile,Long triageId);


	@Transactional
	@Modifying
	@Query(value = "UPDATE queuelist SET queue_name = ?,queue_profile = ?,floor_profile = ?" +
			",nurse_id=? WHERE id = ?",nativeQuery = true)
	void updateQueueManger(String queueName,String queueProfile,String floorProfile,long triageId,Long queueMangerId);


	/**
	 * 返回一条记录
	 * @param name
	 * @return
	 */
	@Query(value = "SELECT * FROM queuelist WHERE queue_name=?", nativeQuery = true)
	QueueManger findByName(String name);


	/**
	 * jpa----->设置map自动转换成查询字段为key的map
	 * jpa----->设置list自动转换成值为查询结果值的数组
	 * @return
	 */
	@Query(value = "select m.id,m.queue_name as queueName,m.queue_profile as queueProfile,m.floor_profile as floorProfile," +
			"n.`name` as nurseName " +
			"from queuelist m left join nursetriage n on n.id=m.nurse_id",
			nativeQuery = true)
	List<Object> queryAll();

	@Query(value = "select q.id,q.queue_name as queueName,r.room_name as zsmc,ma.machine_ip as machineIp,d.doctor_code as loginId from room r " +
			"left join machine_room mr on r.id=mr.room_id left join machine ma " +
			"on mr.machine_id=ma.id left join doctor d on ma.doctor_id=d.id inner join doctorwithqueue dw on d.id=dw.doctor_id " +
			"left join queuelist q on dw.queue_id=q.id where r.room_ip=?",
			nativeQuery = true)
	List<Object> queryAllByNurse(String roomIp);

	/**
	 *针对一个区不同分诊台下的诊室共同显示在大屏上
	 * @param roomIp
	 * @return
	 */
	@Query(value = "select q.id,q.queue_name as queueName,m.machine_profile as zsmc,m.machine_ip as machineIp,d.doctor_code as loginId,n.id as nurseId from nursetriage n " +
			"left join machine m on n.id=m.nurse_id left join machine_room mr on m.id=mr.machine_id left join room r on mr.room_id=r.id " +
			"inner join doctor d on m.doctor_id=d.id inner join doctorwithqueue dr on m.doctor_id=dr.doctor_id " +
			"inner join queuelist q on dr.queue_id=q.id where room_ip=?",
			nativeQuery = true)
	List<Object> queryAllZs(String roomIp);


	@Query(value = "select m.id,m.queue_name as queueName,r.room_name as zsmc,ma.machine_ip as machineIp,d.doctor_code as loginId " +
			"from queuelist m left join machine_queue n on m.id=n.queue_id left join machine ma on n.machine_id=ma.id left join doctor d on ma.doctor_id=d.id " +
			"left join machine_room mr on ma.id=mr.machine_id left join room r on mr.room_id=r.id " +
			"where r.room_ip=?",
			nativeQuery = true)
	List<Object> queryAllByMachine(String ip);

	@Query(value = "select q.id,q.queue_name as queueName,ma.machine_name as zsmc,ma.machine_ip as machineIp,d.doctor_code as loginId from doctor d " +
			"left join doctorwithqueue dw on d.id=dw.doctor_id left join queuelist q on dw.queue_id=q.id left join machine ma on d.id=ma.doctor_id where d.doctor_code=?",
			nativeQuery = true)
	List<Object> queryAllByDoctor(String logCode);

	/**
	 * 穿梭框数据
	 * @param machineId
	 * @return
	 */
	@Query(value = "select q.id,q.queue_name as queueName,q.queue_profile as queueProfile,q.floor_profile as floorProfile " +
			"from queuelist q inner join machine m on q.nurse_id=m.nurse_id where m.id=?",nativeQuery = true)
	List<Object> queryTransferQueue(long machineId);

	/**
	 * 转诊待选队列
	 * @param nurseId
	 * @return
	 */
	@Query(value = "select q.id,q.queue_name as queueName,q.queue_profile as queueProfile,q.floor_profile as floorProfile " +
			"from queuelist q  inner join nursetriage n on q.nurse_id=n.id where n.id=?",nativeQuery = true)
	List<Object> querySwapQueue(long nurseId);

	/**
	 * 跟新队列与呼叫器记录
	 * @param queueId
	 * @param machineId
	 */
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO machine_queue (queue_id,machine_id) VALUES(?,?)",nativeQuery = true)
	void insertMachineWithQueue(long queueId,long machineId );

	/**
	 * 删除队列与呼叫器关系记录
	 * @param machineId
	 * @param queueId
	 */
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM machine_queue WHERE machine_id=? AND queue_id=?",nativeQuery = true)
	void deleteMachineWithQueue(long machineId,long queueId);

	/**
	 *
	 * @param nurseId
	 * @return
	 */
	@Query(value = "select q.id,q.queue_name as queueName,q.queue_profile as queueProfile,q.floor_profile as floorProfile " +
			"from queuelist q inner join machine_queue m on q.id=m.queue_id where q.nurse_id=?",nativeQuery = true)
	List<Object> queryAlreadyTransfer(long nurseId);


	/**
	 * 根据医生登陆工号获取分诊台
	 * @param loginCode
	 * @return
	 */
	@Query(value = "SELECT a.* FROM nursetriage a INNER JOIN machine b ON a.id = b.nurse_id " +
			"INNER JOIN doctor c ON b.doctor_id = c.id WHERE c.doctor_code =?",nativeQuery = true)
	Object[] queryNurseByLoginCode(String loginCode);

	/**
	 * 查找当前队列下初诊状态下号序最大的
	 * @param queueId
	 * @param sickState
	 * @return
	 */
	@Query(value ="SELECT  max(sick_number) as maxnumber FROM sickqueue WHERE queue_id=? and sick_state=?" ,nativeQuery = true)
	Object getMaxNum(long queueId,long sickState);

	@Query(value = "select d.id from doctor d " +
			"left join doctorwithqueue dw on d.id=dw.doctor_id left join queuelist q on dw.queue_id=q.id  where q.id=?",
			nativeQuery = true)
	List<Object> queryDoctor(long queueId);

	@Transactional
	@Modifying
	@Query(value = "UPDATE sickqueue SET sick_number = ?,doctor_id = ?,queue_id = ?" +
			" WHERE id = ?",nativeQuery = true)
	void updateSwapDoctor(int scikNumber,int doctorId,long queueId,long sickId);

	@Transactional
	@Modifying
	@Query(value = "UPDATE sickqueue SET sick_number = ?,queue_id = ?,doctor_id=?" +
			" WHERE id = ?",nativeQuery = true)
	void updateSwapMachine(int scikNumber,long queueId,Integer doctorId,long sickId);
}


