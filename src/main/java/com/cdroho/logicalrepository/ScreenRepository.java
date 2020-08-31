package com.cdroho.logicalrepository;

import com.cdroho.modle.DoctorManger;
import com.cdroho.modle.Screen;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 屏幕dao
 * @author HZL
 * @date 2020-5-6
 */
@Repository
public interface ScreenRepository extends PagingAndSortingRepository<Screen,Integer>{
	/**
	 * Native SQL Query
	 * 所谓本地查询，就是使用原生的sql语句
	 * 返回多条记录，list集合
	 * @param name
	 * @return
	 */
	@Query(value = "SELECT * FROM screen WHERE name=?", nativeQuery = true)
	List<Object> findByNameList(String name);
	
	/**
	 * 返回一条记录
	 * @param name
	 * @return
	 */
	@Query(value = "SELECT * FROM screen WHERE name=?", nativeQuery = true)
	Object findByName(String name);


	@Transactional
	@Modifying
	@Query(value = "delete from screen where id=?",nativeQuery = true)
	void deleteScreenBy(Long screenId);

	@Query(value = "select m.id,m.name as screenName,m.ip as screenIp,m.profile as screenProfile," +
			"n.`name` as nurseName from screen m left join nursetriage n on n.id=m.nurse_id",
			nativeQuery = true)
	List<Object> queryAll();


	@Transactional
	@Modifying
	@Query(value = "INSERT INTO screen (name,ip,profile,nurse_id) " +
			"VALUES(?,?,?,?)",nativeQuery = true)
	void insertScreen(String name,String ip,String profile,long triageId);

	@Transactional
	@Modifying
	@Query(value = "UPDATE screen SET name = ?,ip = ?,profile = ?,nurse_id=? WHERE id = ?",nativeQuery = true)
	void updateScreen(String name,String ip,String profile,long triageId,long id);
}
