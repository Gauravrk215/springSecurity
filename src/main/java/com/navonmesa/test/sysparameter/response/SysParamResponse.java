package com.realnet.sysparameter.response;

import java.util.List;

import com.realnet.fnd.response.PageResponse;
import com.realnet.sysparameter.entity.SysParamEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SysParamResponse  extends PageResponse{

	 @ApiModelProperty(required = true, value = "")
	 private List<SysParamEntity> systemParam;
}
