package com.cdroho.logicalrepository;

import com.cdroho.modle.DoctorManger;
import com.cdroho.modle.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRespository extends PagingAndSortingRepository<User,Integer> {
    /**
     * 返回一条记录
     * @param username
     * @return
     */
    @Query(value = "SELECT * FROM user_t WHERE username=?", nativeQuery = true)
    User findUserByName(String username);


}
