package com.realnet.sysparameter.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.realnet.sysparameter.entity.SysParamEntity;
import com.realnet.sysparameter.entity.SysParamUpload;

public interface SysParamService {

	public SysParamEntity getById(int id);

	SysParamEntity save(SysParamEntity sysparam);

	public SysParamEntity updateSysParamById(int id, SysParamEntity sysparam);

	public SysParamEntity updatelogo(int id, SysParamEntity sysparam) throws JsonProcessingException ;

	public boolean upload_logo(MultipartFile file, String uploadPath);

	public List<SysParamUpload> addallattachments(List<SysParamUpload> attachments);

	public List<SysParamUpload> getallattachmentsbyid(int id);
}
