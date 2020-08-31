package com.cdroho.logicalrepository;

import com.cdroho.modle.NurseTriage;
import com.cdroho.modle.Person;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 分诊台dao
 */
@Repository
public interface TriageRepository extends PagingAndSortingRepository<NurseTriage,Integer>{
	/**
	 * Native SQL Query
	 * 所谓本地查询，就是使用原生的sql语句
	 * 返回多条记录，list集合
	 * @param name
	 * @return
	 */
	@Query(value = "SELECT * FROM PERSON WHERE name=?", nativeQuery = true)
	List<Person> findByNameList(String name);

	/**
	 * 根据id查询分诊台
	 * @param nurseId
	 * @return
	 */
	@Query(value = "SELECT * FROM nursetriage WHERE id=?", nativeQuery = true)
	NurseTriage findNurseTriageById(long nurseId);

	/**
	 * 根据id查询分诊台
	 * @param
	 * @return
	 */
	@Query(value = "SELECT * FROM nursetriage", nativeQuery = true)
	List<NurseTriage> findList();


	@Query(value = "SELECT b.triage_ip,b.triage_type,b.id,d.id as doctorId FROM room a  " +
			"INNER JOIN nursetriage b ON a.nurse_id = b.id " +
			"INNER join machine_room mr on a.id=mr.room_id " +
			"INNER join machine ma on mr.machine_id=ma.id " +
			"INNER join doctor d on ma.doctor_id=d.id " +
			"WHERE a.room_ip = ?", nativeQuery = true)
	Object[] findNurseTriageByRoom(String ip);

	@Query(value = "SELECT d.id as doctorId FROM room a  " +
			"INNER join machine_room mr on a.id=mr.room_id " +
			"INNER join machine ma on mr.machine_id=ma.id " +
			"INNER join doctor d on ma.doctor_id=d.id " +
			"WHERE a.room_ip = ?", nativeQuery = true)
	Object[] findNom(String ip);

	@Query(value = "select b.triage_ip,b.triage_type,b.id from nursetriage b left join machine m on b.id=m.nurse_id " +
			"where m.machine_ip=? ", nativeQuery = true)
	Object[] findNurseTriageByMachineIp(String ip);
	
	/**
	 * 返回一条记录
	 * @param name
	 * @return
	 */
	@Query(value = "SELECT * FROM nursetriage WHERE name=?", nativeQuery = true)
	NurseTriage findByName(String name);
	
	/**
	 * HQL里面查询的from表名要是你映射的对应实体类的名称，而不是你数据库里面的表名
	 * 查询的属性，转则找不到对应的换器转换成person类型，所以不能用person封装
	 * HQL查所有不能用select * from xxx
	 * @param age
	 * @param name
	 * @return
	 */
	@Query(value = "from Person b where b.age = :age AND b.name=:name")
	Person findByNamedParam(@Param("age") int age, @Param("name") String name);
	
	
	@Query(value = "from Person b where b.name like %:name%")
    List<Person> findByNameMatch(@Param("name") String name);

	/**
	 * @param ip
	 * @return
	 */
	@Query(value = "SELECT a.return_flag_step,a.late_flag_step FROM triage as a inner join pager as b on a.triage_id" +
			" = b.triage_id where  b.ip=?", nativeQuery = true)
	List<Object[]> findRuleByIp(@Param("ip") String ip);

	@Transactional
	@Modifying
	@Query(value = "delete from nursetriage where id=?",nativeQuery = true)
	void deleteNurseTriageBy(Long triageId);

	@Query(value = "SELECT * FROM nursetriage", nativeQuery = true)
	List<NurseTriage> findAllNurse();

	@Transactional
	@Modifying
	@Query(value = "UPDATE nursetriage SET name = ?,triage_ip = ?,triage_type = ?,lock_count=?,pass_count=?,pass_weight=?,return_weight=? WHERE id = ?;",nativeQuery = true)
	void updateNurseTriageBy(String name,String triageIp,String triage_type,int lockCount,int passCount,int passWeight,int returnWeight,Long triageId);


}
