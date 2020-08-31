package com.cdroho.logicalrepository;

import com.cdroho.modle.CheckSick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PharmacyRepository extends JpaRepository<CheckSick,Integer> {

}
