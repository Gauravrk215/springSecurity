package com.realnet.fnd.repository1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realnet.fnd.entity1.Query;
@Repository
public interface QueryRepository extends JpaRepository<Query, Long> {

}
