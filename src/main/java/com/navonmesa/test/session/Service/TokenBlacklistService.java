package com.navonmesa.test.session.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.navonmesa.test.session.Repository.TokenRepository;
import com.navonmesa.test.session.entity.Token;

@Service
public class TokenBlacklistService {

	@Autowired
	private TokenRepository tokenRepository;

	private Set<String> blacklistedTokens = new HashSet<>();

	public void blacklistToken(String token) {
		blacklistedTokens.add(token);
	}

//	public boolean isTokenBlacklisted(String token) {
//		return blacklistedTokens.contains(token);
//	}

	private Map<String, Set<String>> userTokenBlacklist = new HashMap<>();

	public void blacklistToken(String username, String token) {
		userTokenBlacklist.computeIfAbsent(username, k -> new HashSet<>()).add(token);
	}

	// Blacklist all tokens for a given user
	public void blacklistTokensForUser(String username) {
		// Fetch all tokens from the DB for the user
		List<Token> tokensToBlacklist = tokenRepository.findByUsername(username);

		// Add all tokens to the blacklist
		for (Token token : tokensToBlacklist) {
			blacklistedTokens.add(token.getToken());
		}

		tokenRepository.findByUsername(username).forEach(user -> tokenRepository.delete(user));
		// Delete tokens from DB after blacklisting
//		tokenRepository.deleteByUsername(username);
	}

	public boolean isTokenBlacklisted(String token) {
		// Iterate over blacklisted tokens and check if this token is blacklisted

		boolean istoken = false;
		boolean anyMatch = userTokenBlacklist.values().stream().anyMatch(set -> set.contains(token));
		if (anyMatch) {
			istoken = anyMatch;

		}
		List<Token> list = tokenRepository.findByToken(token);

		if (list.isEmpty()) {
			istoken = true;
		}

		return istoken;
	}
}
