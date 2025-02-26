package com.realnet.users.entity1;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class Registration {

	private String first_name;

	private String last_name;

	private Long mob_no;

	private String email;

	private Long account_id;

	private Long usrGrpId;

	@NotBlank
	@Size(min = 6, max = 40)
	private String new_password;

	private String confirm_password;
	private String accesstype;
	
	private String gender;
	private String date_of_birth;
	@Lob
	private String how_you_know_aboutus;
}
