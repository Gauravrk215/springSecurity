package com.realnet.users.service1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realnet.users.entity.Sys_Accounts;
import com.realnet.users.repository.SysAccountRepo;

@Service
public class SysAccountService {

	@Autowired
	private SysAccountRepo sysAccountRepo;

	public Sys_Accounts save(Sys_Accounts sys) {
		Sys_Accounts accounts = sysAccountRepo.save(sys);

		return accounts;

	}

	public List<Sys_Accounts> getall() {

		List<Sys_Accounts> getall = sysAccountRepo.findAll();
		return getall;
	}

	public Sys_Accounts getBYId(Long account_id) {

		Sys_Accounts accounts = sysAccountRepo.findById(account_id).get();
		return accounts;
	}

	public Sys_Accounts findByEmail(String email) {

		Sys_Accounts accounts = sysAccountRepo.findByEmail(email);
		return accounts;
	}

}
