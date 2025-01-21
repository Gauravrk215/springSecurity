package com.realnet.rb.response;

import java.util.List;

import com.realnet.rb.entity.Rn_rb_Tables;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Rn_tableResponse {
	
	  @ApiModelProperty(required = true, value = "")
	  private List<Rn_rb_Tables> items;
	

}
