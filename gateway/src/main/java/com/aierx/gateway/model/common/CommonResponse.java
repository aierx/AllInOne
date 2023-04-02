package com.aierx.gateway.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse {
    int code;
    Long traceId;
    String msg;
    Object data;
}
