package com.example.demo.util.common;

import com.example.demo.util.mongo.analysis.entity.StatisticsLog;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.util.common.TypeChangeUtils.changeListEntity2ListOfListString;
import static org.junit.Assert.*;

public class TypeChangeUtilsTest {

    @Test
    public void changeListEntity2ListOfListStringTest(){
        String message1 = "2019-10-15T00:01:21.355+0000 I COMMAND  [conn53] command macc2.onofflineUserHistory command: aggregate { aggregate: \"onofflineUserHistory\", pipeline: [ { $match: { offlineTime: { $gte: new Date(1571011200000), $lte: new Date(1571097600000) }, wifiUpDown: { $gte: 5 } } }, { $group: { _id: { ssid: \"$ssid\", buildingId: \"$buildingId\", tenantId: \"$tenantId\", mac: \"$mac\", utcCode: \"$utcCode\" }, wifiUpDown: { $sum: \"$wifiUpDown\" } } }, { $group: { _id: { ssid: \"$_id.ssid\", buildingId: \"$_id.buildingId\", tenantId: \"$_id.tenantId\", utcCode: \"$_id.utcCode\" }, total: { $sum: 1 }, wifiUpDown: { $sum: \"$wifiUpDown\" } } }, { $project: { total: 1, wifiUpDown: 1 } } ], allowDiskUse: true } keyUpdates:0 writeConflicts:0 numYields:11595 reslen:369859 locks:{ Global: { acquireCount: { r: 23324 }, acquireWaitCount: { r: 1 }, timeAcquiringMicros: { r: 61132 } }, Database: { acquireCount: { r: 11662 }, acquireWaitCount: { r: 2 }, timeAcquiringMicros: { r: 8617 } }, Collection: { acquireCount: { r: 11662 } } } protocol:op_query 21349ms";
        String formatDateTime1 = "2019-10-15T00:01:21.355";
        String message2 = "2019-10-15T00:01:30.234+0000 I COMMAND  [conn53] remove macc2.dayStaAndFlowCountBySSID query: { isZeroData: \"false\" } ndeleted:3159 keyUpdates:0 writeConflicts:0 numYields:1791 locks:{ Global: { acquireCount: { r: 4951, w: 4951 } }, Database: { acquireCount: { w: 4951 } }, Collection: { acquireCount: { w: 1792 } }, Metadata: { acquireCount: { w: 3159 } }, oplog: { acquireCount: { w: 3159 } } } 1992ms";
        String formatDateTime2 = "2019-10-15T00:01:30.234";
        String message3 = "2019-10-15T00:09:39.254+0000 I COMMAND  [conn53] insert macc2.tmp.agg_out.16991 ninserted:59 keyUpdates:0 writeConflicts:0 numYields:0 locks:{ Global: { acquireCount: { r: 25533, w: 2227 } }, Database: { acquireCount: { r: 11652, w: 2226, R: 1, W: 1 }, acquireWaitCount: { R: 1, W: 1 }, timeAcquiringMicros: { R: 311, W: 365 } }, Collection: { acquireCount: { r: 11652, w: 1 } }, Metadata: { acquireCount: { w: 142332 } }, oplog: { acquireCount: { w: 2225 } } } 1032ms";
        String formatDateTime3 = "2019-10-15T00:09:39.254";
        List<StatisticsLog> list = new ArrayList();
        StatisticsLog logEntity1 = new StatisticsLog();
        logEntity1.setLogMessage(message1);
        logEntity1.setLogDateTime(formatDateTime1);
        list.add(logEntity1);
        StatisticsLog logEntity2 = new StatisticsLog();
        logEntity2.setLogMessage(message2);
        logEntity2.setLogDateTime(formatDateTime2);
        list.add(logEntity2);
        StatisticsLog logEntity3 = new StatisticsLog();
        logEntity3.setLogMessage(message3);
        logEntity3.setLogDateTime(formatDateTime3);
        list.add(logEntity3);
        List outList = changeListEntity2ListOfListString(list);
        System.out.println(outList);
    }


}