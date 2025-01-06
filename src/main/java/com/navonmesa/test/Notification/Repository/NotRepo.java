package com.realnet.Notification.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.Notification.Entity.NotEntity;

@Repository
public interface NotRepo extends JpaRepository<NotEntity, Long>{
	
	@Query(value = "select * from not_entity order by id desc limit 10", nativeQuery = true)
	List<NotEntity> findTopByOrderByd();


}
