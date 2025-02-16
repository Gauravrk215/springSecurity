package com.realnet.users.entity1;

import java.io.Serializable;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.realnet.users.entity.PasswordResetToken;
import com.realnet.users.entity.Role;
import com.realnet.users.entity.Sys_Accounts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "SEC_USERS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AppUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "user_id")
	@GeneratedValue(generator = "SecUsers_gen")
	@SequenceGenerator(name = "SecUsers_gen", sequenceName = "sec_users_sequencs", initialValue = 10007300, allocationSize = 1)
	private Long userId;

	@Column(name = "user_name", unique = true)
	private String username;

	private String userPassw;
	private String title;
	private String shortName;
	private String fullName;
	private Date expiryDate;
	private String daysMth;
	private Long noDaysMth;
	private String status;
	private String changePassw;

	private String createby;

//	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "createdate", nullable = false, updatable = false)
	@CreatedDate
	private LocalDateTime createdate;

	private String updateby;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedate;

	@ManyToOne
	@JoinColumn(name = "usr_grp")
	private AppUserRole usrGrp;

	private String langCode;

	private String firstLogin;

	@ManyToOne
	@JoinColumn(name = "position_code")
	private AppUserPosition positionCode;

	@ManyToOne
	@JoinColumn(name = "department_code")
	private AppUserDepartment departmentCode;

	@JsonBackReference
	@ManyToOne
	private Sys_Accounts Account;

	private String email;
	private String notification;
	private Long customerId;
	private String password1;
	private String password2;
	private String password3;
	private String password4;
	private Long pwdChangedCnt;
	private Date lastPwdChangedDate;
	private Blob photo;
	private String photoName;

	private String provider;
	private String country;
	private String depString;
	private boolean isBlocked;
	private String about;
	private Long checknumber;
	private String working;
	private String accesstype;
//	private Integer access_duration;
//
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-mm-yyyy")
//	private Date access_till_date;

	private String random_no;
	private Long mob_no;

	private boolean IsComplete;

	private boolean active;

	@Column(name = "multitime")
	private String multitime;

	private String multilingual;

	@Transient
	private String customerNumer;
	@Transient
	private String positionCodeString;
	@Transient
	private String departmentCodeString;

	// @Transient
	private Long usrGrpId;
	@Transient
	private String confirmPassword;
	@Transient
	private String usrGrpName;
	@Transient
	private StringBuilder totalLogInfo;

	@Transient
	@JsonIgnore
	private PasswordResetToken pass;

	@ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.DETACH,
			CascadeType.ALL, CascadeType.REFRESH })
	@JoinTable(name = "SECUSER_ROLES", joinColumns = { @JoinColumn(name = "SECUSER_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "ROLE_ID") })
	private Set<Role> roles;

}
