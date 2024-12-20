package com.navonmesa.test.session.response;

import com.navonmesa.test.Response.OperationResponse;
import com.navonmesa.test.session.entity.SessionItem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SessionResponse extends OperationResponse {
  @ApiModelProperty(required = true, value = "")
  private SessionItem item;
}
