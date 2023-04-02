package com.aierx.gateway.model.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppModel {
    String appkey;



    List<MachineModel> machineModels;
}
