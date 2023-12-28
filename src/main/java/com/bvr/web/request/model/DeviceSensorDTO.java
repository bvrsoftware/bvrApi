package com.bvr.web.request.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceSensorDTO {

    private String sensorType;
    private String connectedTo;
    private Short ioId;
    private Boolean inverse;
    private Boolean rs232;
}
