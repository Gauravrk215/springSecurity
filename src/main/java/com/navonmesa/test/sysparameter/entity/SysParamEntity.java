package com.realnet.sysparameter.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "System_paramaters")
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SysParamEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private int id;

	@Column(name = "SCHEDULER_TIME")
	private int schedulerTime;

	@Column(name = "LEASE_TAX_CODE")
	private String leaseTaxCode;

	@Column(name = "VESSEL_CONF_PROCESSLIMIT")
	private int vesselConfProcessLimit;

	@Column(name = "ROW_TO_DISPLAY")
	private int rowToDisplay;

	@Column(name = "LINK_TO_DISPLAY")
	private int linkToDisplay;

	@Column(name = "ROW_TO_ADD")
	private int rowToAdd;

	@Column(name = "LOV_ROW_TO_DISPLAY")
	private int lovRowToDisplay;

	@Column(name = "LOV_LINK_TO_DISPLAY")
	private int lovLinkToDisplay;

	@Column(name = "OID_SERVER_NAME")
	private String oidserverName;

	@Column(name = "OID_BASE")
	private String oidBase;

	@Column(name = "OID_ADMIN_USER")
	private String oidAdminUser;

	@Column(name = "OID_SERVER_PORT")
	private int oidServerPort;

	@Column(name = "USER_DEFAULT_GROUP")
	private int userDefaultGroup;

	@Column(name = "DEFAULT_DEPARTMENT")
	private String defaultDepartment;

	@Column(name = "DEFAULT_POSITION")
	private String defaultPosition;

	@Column(name = "SINGLE_CHARGE")
	private String singleCharge;

	@Column(name = "FIRST_DAYOF_THE_WEEK")
	private String firstDayOftheWeek;

	@Column(name = "HOUR_PER_SHIFT")
	private int hourPerShift;

	@Column(name = "CN_BILLING_FREQUENCY")
	private int cnBillingFrequency;

	@Column(name = "BILLING_DEPARTMENT_CODE")
	private String billingDepartmentCode;

	@Column(name = "BASE_PRICE_LIST")
	private String basePriceList;

	@Column(name = "NONCONTAINER_SERVICEORDER_AUTO_APPDEPCODE")
	private String nonContainerServiceOrder;

	@Column(name = "EDI_MAE_SCHEDULER_ONOFF")
	private int EdiMaeSchedulerONOFF;

	@Column(name = "EDI_SCHEDULER_ONOFF")
	private String EdiSchedulerONOFF;

	private String upload_Logo;
	private String upload_Logo_name;

	private String upload_Logo_path;

	private String Company_Display_Name;
	
	private boolean isRegitrationAllowed=true;

	@Transient
	@OneToMany(cascade = CascadeType.ALL)
	private List<SysParamUpload> sysParamUploads;
}
