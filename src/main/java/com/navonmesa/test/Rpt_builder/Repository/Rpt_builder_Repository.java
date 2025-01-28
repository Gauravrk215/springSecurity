package com.realnet.Rpt_builder.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.realnet.Rpt_builder.Entity.Rpt_builder_t;

@Repository
public interface Rpt_builder_Repository extends JpaRepository<Rpt_builder_t, Long> {
}