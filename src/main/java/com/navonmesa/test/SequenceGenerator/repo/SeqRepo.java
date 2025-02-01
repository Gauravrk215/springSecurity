package com.realnet.SequenceGenerator.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realnet.SequenceGenerator.Entity.Sequence;
@Repository
public interface SeqRepo extends JpaRepository<Sequence, Long> {

	
	
	Optional<Sequence> findByPrefixAndSuffix(String pre, String suf);
}
