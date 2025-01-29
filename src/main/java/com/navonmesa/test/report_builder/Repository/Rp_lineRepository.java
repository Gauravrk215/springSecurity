package com.realnet.report_builder.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.realnet.report_builder.Entity.Rp_Line;

@Repository
public interface Rp_lineRepository extends CrudRepository<Rp_Line, Integer> {

	Rp_Line findById(int id);

}
