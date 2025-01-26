package com.realnet.realm.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.realm.Entity.Realm;

@Repository
public interface RealmRepository extends JpaRepository<Realm, Long> {

	@Query(value = "select * from realm where  touser_id=?1", nativeQuery = true)
	List<Realm> findBytouserId(Long touser_id);

}