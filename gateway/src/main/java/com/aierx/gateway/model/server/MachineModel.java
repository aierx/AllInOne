package com.aierx.gateway.model.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineModel {
    private String ip;

    private int port;

    String slimline;

    private List<ServiceModel> serviceModelList;
}
