package com.cdroho.logicalrepository;


import com.cdroho.modle.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 规则抽象
 */
@Repository
public interface RuleRepository extends JpaRepository<Person,Integer>{
    /**
     * 普通排序规则
     */
    @Query(value = "SELECT a.call_buffer,a.return_flag_step,a.late_flag_step,b.call_pass_first_flag,b.call_pass_rule_flag," +
            "b.call_return_first_flag,b.call_return_rule_flag,b.call_late_first_flag,b.call_late_rule_flag " +
            "from triage a INNER JOIN pager b ON a.triage_id = b.triage_id WHERE b.ip = ?", nativeQuery = true)
    List<Object> commonRule(@Param("ip") String ip);

    /**
     * 分时段规则
     */
   // List<Object> daypartingRule();

    /**
     *其他排序规则
     */
    //List<Object> othersRule();
}
