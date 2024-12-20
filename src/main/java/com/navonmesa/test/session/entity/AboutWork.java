package com.navonmesa.test.session.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class AboutWork {
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id ;
	private String name;
	
	private String password;
	private Long mobile;
	private String email;
	private String companyname;
	private String pancard;
	private String working;
	private String managing_work;
//	@OneToOne(mappedBy = "user",fetch = FetchType.LAZY,
//            cascade = CascadeType.ALL)
//	private User user;
}
