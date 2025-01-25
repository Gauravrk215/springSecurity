package com.realnet.rb.response;
import java.util.List;

import com.realnet.fnd.response.PageResponse;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MasterResponse extends PageResponse {
  @ApiModelProperty(required = true, value = "")
  private List<String> items;
}
