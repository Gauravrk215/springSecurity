package com.realnet.rb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.realnet.rb.entity.RnTableListDto;
import com.realnet.rb.entity.Rn_rb_Column;

@Repository
public interface RnTableDtoRepo extends JpaRepository<RnTableListDto, Integer>
{
	//@Query(value = "SELECT table_name FROM information_schema.tables", nativeQuery = true)
	//List<RnTableListDto> getTableList();
}
