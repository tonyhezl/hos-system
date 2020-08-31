package com.cdroho.logicalrepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.cdroho.modle.Person;

/**
 * PagingAndSortingRepository	继承	 	CrudRepository
 * JpaRepository  				继承 		PagingAndSortingRepository
 * 也就是说， CrudRepository 提供基本的增删改查；PagingAndSortingRepository 提供分页和排序方法；JpaRepository 提供JPA需要的方法。
 * @Repository------->持久层标识
 * @Service   ------->业务层标识
 * @Component ------->中立的标识
 * 上述三个注解都是标识该类为一个组件，供其他类调用或者使用的。
 * @author HZL
 *
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<Person,Integer>{
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
	 * 返回一条记录
	 * @param name
	 * @return
	 */
	@Query(value = "SELECT * FROM PERSON WHERE name=?", nativeQuery = true)
	Person findByName(String name);
	
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
}
