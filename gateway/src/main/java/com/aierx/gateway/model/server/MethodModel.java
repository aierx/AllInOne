package com.aierx.gateway.model.server;

import com.aierx.gateway.rpc.http.HttpMethodModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ui.Model;

import java.util.List;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = HttpMethodModel.class, name = "http")})
public class MethodModel {

    @JsonIgnore
    private String type;

    private String methodName;

    private List<Object> paramTypes;
}
