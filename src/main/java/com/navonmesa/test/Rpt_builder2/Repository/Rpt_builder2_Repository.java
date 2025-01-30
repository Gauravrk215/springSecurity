package com.realnet.Rpt_builder2.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.realnet.Rpt_builder2.Entity.Rpt_builder2_t;

@Repository
public interface Rpt_builder2_Repository extends JpaRepository<Rpt_builder2_t, Long> {
}