package com.cdroho.logicalrepository;

import com.cdroho.modle.QueuingMachine;
import com.cdroho.modle.dto.MachineDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * 呼叫器dao
 * @author HZL
 * @date 2020-3-26
 */
@Repository
public interface MachineRepository extends PagingAndSortingRepository<QueuingMachine,Integer>{
	/**
	 * Native SQL Query
	 * 所谓本地查询，就是使用原生的sql语句
	 * 返回多条记录，list集合
	 * @param name
	 * @return
	 */
	@Query(value = "SELECT * FROM machine WHERE machine_name=?", nativeQuery = true)
	List<QueuingMachine> findByNameList(String name);
	
	/**
	 * 返回一条记录
	 * @param name
	 * @return
	 */
	@Query(value = "SELECT * FROM machine WHERE machine_name=?", nativeQuery = true)
	QueuingMachine findByName(String name);


	@Transactional
	@Modifying
	@Query(value = "delete from machine where id=?",nativeQuery = true)
	void deleteQueuingMachineById(Long machineId);


	/**
	 * jpa----->设置map自动转换成查询字段为key的map
	 * jpa----->设置list自动转换成值为查询结果值的数组
	 * @return
	 */
	@Query(value = "select m.id,m.machine_name as machineName,m.room_name as roomName,m.machine_ip as machineIp," +
			"m.machine_profile as machineProfile,n.`name` as nurseName " +
			"from machine m left join nursetriage n on n.id=m.nurse_id",
			nativeQuery = true)
	List<Object> queryAll();


	@Transactional
	@Modifying
	@Query(value = "UPDATE machine SET machine_name = ?,machine_ip = ?,machine_profile = ?" +
			",room_name=?,nurse_id=? WHERE id = ?",nativeQuery = true)
	void updateMachine(String name,String machineIp,String machineProfile,String roomName,long triageId,Long machineId);


	/**
	 * 新增呼叫器并设置分诊台id
	 * @param name
	 * @param machineIp
	 * @param machineProfile
	 * @param roomName
	 * @param triageId
	 */
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO machine (machine_name, machine_ip,machine_profile,room_name,again_be_compared,pass_be_compared,nurse_id) " +
			"VALUES(?,?,?,?,?,?,?)",nativeQuery = true)
	void insertMachine(String name,String machineIp,String machineProfile,String roomName,long againBeCompared,long passBeCompared,Long triageId);


	/**
	 * 返回一条记录
	 * @param machineId
	 * @return
	 */
	@Query(value = "SELECT * FROM machine WHERE id=?", nativeQuery = true)
	QueuingMachine findQueuingMachineByIdById(Long machineId);

	/**
	 * 返回一条记录
	 * @param machineIp
	 * @return
	 */
	@Query(value = "SELECT * FROM machine WHERE machine_ip=?", nativeQuery = true)
	QueuingMachine getMachineByIp(String machineIp);

	@Transactional(rollbackOn =Exception.class)
	@Modifying
	@Query(value = "UPDATE machine SET again_be_compared = ?,pass_be_compared = ? WHERE machine_ip = ?",nativeQuery = true)
	void updateMachineRule(int againBecompared,int passBecompared,String machineIp);

	@Transactional(rollbackOn =Exception.class)
	@Modifying
	@Query(value = "UPDATE machine SET doctor_id=?,login_time=? where id=?",nativeQuery = true)
	void updateMachineForDoctor(long doctorId,String nowDate,long machineId);

	@Transactional(rollbackOn =Exception.class)
	@Modifying
	@Query(value = "UPDATE machine a LEFT JOIN doctor b ON a.doctor_id = b.id SET a.doctor_id = NULL WHERE a.machine_ip <> ? AND b.doctor_code=?",nativeQuery = true)
	void updateMachineForClean(String machineIp,String loginCode);

	@Transactional(rollbackOn =Exception.class)
	@Modifying
	@Query(value = "UPDATE machine SET doctor_id=null",nativeQuery = true)
	void updateMachineDoctor();

}
