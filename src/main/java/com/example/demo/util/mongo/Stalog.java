package com.example.demo.util.mongo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chen_bq
 * @description
 * @create: 2019/10/10 10:32
 **/
@Data
@Document
public class Stalog implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String mac;
    private Long uploadDate = 0L;
    @JSONField(name = "online_time")
    private Date onlineTime;
    private String sn;
    private Date updateTime;
    @JSONField(name = "auth_time")
    private Date authTime;
    @JSONField(name = "authoff_time")
    private Date authoffTime;
    @JSONField(name = "offline_time")
    private Date offlineTime;
    @JSONField(name = "ip")
    private String userIp;
    private String rssi;
    private Integer rssiInt;
    private String ssid;
    @JSONField(name = "wifi_up")
    private Long wifiUp;
    @JSONField(name = "wifi_down")
    private Long wifiDown;
    @JSONField(name = "uplink_up")
    private Long uplinkUp;
    @JSONField(name = "uplink_down")
    private Long uplinkDown;
    @JSONField(name = "log_type")
    private String logType;
    private String userName;
    private Integer buildingId;
    private Integer groupId;
    private Integer tenantId;
    private String buildingName;
    private Long wifiUpDown;
    private String groupName;
    private String deviceName;
    private String deviceAliasName;
    private Long activeTime;

    private Float uplinkRate;
    private Float downlinkRate;
    private Float updownlinkRate;
    private Integer timeDelay;
    private Integer pktLoseRate;
    private String band;
    private String channel;

    private String termidVersion;
    private String hardwareType;
    private String osType;
    private String osVersion;
    private String productModel;
    private String termidString;
    private String macPrefix;

    private Integer rawOffset;
    private Integer utcCode;
    private Integer systemOffset;

    private String reasonSource;
    private Integer reasonCode;

    private Integer score;
    private String scoreReason;

    private Integer floorNoise;
    private Integer utilization;

    private Date realOnlineTime;

    private String name;

    // hostname
    private String dhcpOption12;
    // requestlist
    private String dhcpOption55;
    // vendor class
    private String dhcpOption60;
    //max rate
    private Integer maxRate;

    private String productType;

    public Long getActiveTime() {
        if (uploadDate == null || onlineTime == null) {
            activeTime = 0L;
            return activeTime;
        }
        if (this.offlineTime != null) {
            activeTime = offlineTime.getTime() - onlineTime.getTime();
            return activeTime;
        }
        activeTime = uploadDate - onlineTime.getTime();
        return activeTime;
    }

    public void setActiveTime(Long activeTime) {
        this.activeTime = activeTime;
    }

}
