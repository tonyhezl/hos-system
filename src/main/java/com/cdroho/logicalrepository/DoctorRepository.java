package com.cdroho.logicalrepository;

import com.cdroho.modle.DoctorManger;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

/**
 * 医生dao
 * @author HZL
 * @date 2020-3-26
 */
@Repository
public interface DoctorRepository extends PagingAndSortingRepository<DoctorManger,Integer>{
	/**
	 * Native SQL Query
	 * 所谓本地查询，就是使用原生的sql语句
	 * 返回多条记录，list集合
	 * @param name
	 * @return
	 */
	@Query(value = "SELECT * FROM doctor WHERE doctor_name=?", nativeQuery = true)
	List<DoctorManger> findByNameList(String name);
	
	/**
	 * 返回一条记录
	 * @param name
	 * @return
	 */
	@Query(value = "SELECT * FROM doctor WHERE doctor_name=?", nativeQuery = true)
	DoctorManger findByName(String name);


	@Query(value = "SELECT * FROM doctor WHERE doctor_code=?", nativeQuery = true)
	DoctorManger findByCode(String doctorCode);


	@Transactional
	@Modifying
	@Query(value = "delete from doctor where id=?",nativeQuery = true)
	void deleteNurseTriageBy(Long doctorId);
}
