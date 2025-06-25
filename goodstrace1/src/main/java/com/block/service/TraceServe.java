package com.block.service;
import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson.JSONArray;

public interface TraceServe {
    Dict newGood(String producerAddress, String name, int traceNumber, String traceName, int quality, String location);
    Dict addProducer(String account);
    Dict addDistributor(String account);
    Dict addRetailer(String account);
    Dict isProducer(String account);
    Dict isDistributor(String account);
    Dict isRetailer(String account);
    Dict addTraceInfoByDistributor(String distributorAddress, int traceNumber, String traceName, int quality, String location);
    Dict addTraceInfoByRetailer(String retailerAddress, int traceNumber, String traceName, int quality, String location);
    Dict getTraceInfo(int traceNumber);
    Dict getGood(int traceNumber);
    Dict getAllGood();
}