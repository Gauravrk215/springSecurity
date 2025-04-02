package com.realnet.FabricIcardLines.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.FabricIcardLines.Entity.FabricIcardLines;

@Repository
public interface FabricIcardLinesRepository extends JpaRepository<FabricIcardLines, Long> {

	@Query(value = "select * from fabric_icard_lines where header_id=?1", nativeQuery = true)
	FabricIcardLines getbyheaderId(String header_id);

}