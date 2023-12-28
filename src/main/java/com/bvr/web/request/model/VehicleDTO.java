package com.bvr.web.request.model;

import com.bvr.core.domain.BvrSoftwareUser;
import com.bvr.core.domain.Device;
import com.bvr.core.domain.DevicePacketMeta;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleDTO {

    private Integer indexNo;
    private String ouid;
    private String state;
    private String vehicleNo;
    private String clientUsername;
    private String transporterUsername;
    private String ownerUsername;
    //optional field
    private String alias = "";
    private String companyName = "";
    private String driverPhoneNumber = "";

    private String lu;
    private String since;
    private String added;
    private String subscriptionDue;
    private String subscriptionStart;
    //
    private double mileage;
    private int overspeed;
    private long odometer;
    //
    private String deviceType;
    private String vehicleType;
    //
    private boolean lock;
    private boolean status;
    private boolean logger;
    private String deviceStatus;
    private String vehicleStatus;
    //private boolean ignitionWire;
    private boolean ignitionWirePositive;
    //
    private String imei;
    private String simNo;
    private String operator;
    private Boolean assigned;
    private String devicePassword;
    private String deviceId;
    private boolean ignitionNotConnected;
    //
    private Device device;
    private BvrSoftwareUser transporter;
    private boolean ac;
    private String address;
    private DevicePacketMeta devicePacketMeta;
    private String inactiveDate;
    private boolean lockEnable;
    private boolean acPositive;
    private String remark;
    private double[] location;
    private boolean analog;
    @Deprecated
    private Boolean fuel;
    private Boolean rs32;
    private int port;
    private String loadingStatus;
    private String deletedDate;
    private String extraRemark;
    private String modifiedDate;
    private Map<String, String> dynamicFields;
    private boolean parkAlarmOnIgnitionOn;
    private Integer SensorType;
    private Short temperatureDelta;
    private List<DeviceSensorDTO> sensorDetailList;
    private String attachedCoin;
    private Boolean newCoin;
    //for vehicle idle time
    private Integer idleHour;
    private Integer idleMinute;
    private Boolean attendance;
    private Short attendanceType;
    private Boolean isAutoRenewal;
    private Boolean coinRefund;

}
