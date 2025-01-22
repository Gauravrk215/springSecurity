package com.realnet.rb.response;

import java.util.List;

import com.realnet.rb.entity.Rn_Rb_Std_Param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Rn_stdParamResponse {
	
	
		
		@ApiModelProperty(required = true, value = "")
		  private List<Rn_Rb_Std_Param> items;
}