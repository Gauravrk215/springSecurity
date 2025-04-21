package com.realnet.session.entity;

import javax.persistence.*;

import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;
	private String username;

	private String token;

	private LocalDateTime createdAt;

	// Getters and setters
}
