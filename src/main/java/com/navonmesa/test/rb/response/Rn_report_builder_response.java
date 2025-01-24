package com.realnet.rb.response;

import java.util.List;

import com.realnet.fnd.response.PageResponse;
import com.realnet.rb.entity.Rn_report_builder;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Rn_report_builder_response extends PageResponse {
  @ApiModelProperty(required = true, value = "")
  private List<Rn_report_builder> items;
}