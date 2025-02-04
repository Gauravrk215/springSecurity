package com.realnet.sqlworkbech.Repository;

import org.checkerframework.common.util.report.qual.ReportCreation;
import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realnet.sqlworkbech.Entity.SqlModel;

@Repository
public interface SqlRepository extends JpaRepository<SqlModel, Long>{

}
