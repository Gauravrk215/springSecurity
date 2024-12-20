package com.navonmesa.test.session.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.navonmesa.test.session.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
	List<Token> findByUsername(String username);
	
	 List<Token> findByToken(String token);


	void deleteByUsername(String username);
}
