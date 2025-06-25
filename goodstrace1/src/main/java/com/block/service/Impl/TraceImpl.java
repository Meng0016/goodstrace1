package com.block.service.Impl;

import cn.hutool.core.lang.Dict;
import com.block.service.TraceServe;
import com.block.utils.IOUtil;
import com.block.utils.WeBASEUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TraceImpl implements TraceServe {

    @Value("${system.contract.producerAddress}")
    public String producerAddress;

    @Value("${system.contract.retailerAddress}")
    public String retailerAddress;

    @Value("${system.contract.distributorAddress}")
    public String distributorAddress;

    @Value("${system.contract.traceAddress}")
    public String contractAddress;

    @Value("${system.contract.admin}")
    public String adminAddress;

    public static String ABI = IOUtil.readResourceAsString("abi/Trace.abi");
    public static String contractName = "Trace";

    @Override
    public Dict newGood(String producerAddress, String name, int traceNumber, String traceName, int quality, String location) {
        String funcName = "newGood";
        ArrayList<Object> funcParam = new ArrayList<>();
        funcParam.add(name);
        funcParam.add(traceNumber);
        funcParam.add(traceName);
        funcParam.add(quality);
        funcParam.add(location);
        return WeBASEUtils.request(producerAddress, funcName, funcParam, ABI, contractName, contractAddress);
    }

    @Override
    public Dict addProducer(String account) {
        String funcName = "addProducer";
        ArrayList<Object> funcParam = new ArrayList<>();
        funcParam.add(account);
        return WeBASEUtils.request(producerAddress, funcName, funcParam, ABI, contractName, contractAddress);
    }

    @Override
    public Dict addDistributor(String account) {
        String funcName = "addDistributor";
        ArrayList<Object> funcParam = new ArrayList<>();
        funcParam.add(account);
        return WeBASEUtils.request(distributorAddress, funcName, funcParam, ABI, contractName, contractAddress);
    }

    @Override
    public Dict addRetailer(String account) {
        String funcName = "addRetailer";
        ArrayList<Object> funcParam = new ArrayList<>();
        funcParam.add(account);
        return WeBASEUtils.request(retailerAddress, funcName, funcParam, ABI, contractName, contractAddress);
    }

    @Override
    public Dict isProducer(String account) {
        String funcName = "isProducer";
        ArrayList<Object> funcParam = new ArrayList<>();
        funcParam.add(account);
        return WeBASEUtils.request(adminAddress, funcName, funcParam, ABI, contractName, contractAddress);
    }

    @Override
    public Dict isDistributor(String account) {
        String funcName = "isDistributor";
        ArrayList<Object> funcParam = new ArrayList<>();
        funcParam.add(account);
        return WeBASEUtils.request(adminAddress, funcName, funcParam, ABI, contractName, contractAddress);
    }

    @Override
    public Dict isRetailer(String account) {
        String funcName = "isRetailer";
        ArrayList<Object> funcParam = new ArrayList<>();
        funcParam.add(account);
        return WeBASEUtils.request(adminAddress, funcName, funcParam, ABI, contractName, contractAddress);
    }

    @Override
    public Dict addTraceInfoByDistributor(String distributorAddress, int traceNumber, String traceName, int quality, String location) {
        String funcName = "addTraceInfoByDistributor";
        ArrayList<Object> funcParam = new ArrayList<>();
        funcParam.add(traceNumber);
        funcParam.add(traceName);
        funcParam.add(quality);
        funcParam.add(location);
        return WeBASEUtils.request(distributorAddress, funcName, funcParam, ABI, contractName, contractAddress);
    }

    @Override
    public Dict addTraceInfoByRetailer(String retailerAddress, int traceNumber, String traceName, int quality, String location) {
        String funcName = "addTraceInfoByRetailer";
        ArrayList<Object> funcParam = new ArrayList<>();
        funcParam.add(traceNumber);
        funcParam.add(traceName);
        funcParam.add(quality);
        funcParam.add(location);
        return WeBASEUtils.request(retailerAddress, funcName, funcParam, ABI, contractName, contractAddress);
    }

    @Override
    public Dict getTraceInfo(int traceNumber) {
        String funcName = "getTraceInfo";
        ArrayList<Object> funcParam = new ArrayList<>();
        funcParam.add(traceNumber);
        return WeBASEUtils.request(adminAddress, funcName, funcParam, ABI, contractName, contractAddress);
    }

    @Override
    public Dict getGood(int traceNumber) {
        String funcName = "getGood";
        ArrayList<Object> funcParam = new ArrayList<>();
        funcParam.add(traceNumber);
        return WeBASEUtils.request(adminAddress, funcName, funcParam, ABI, contractName, contractAddress);
    }

    @Override
    public Dict getAllGood() {
        String funcName = "getAllGood";
        return WeBASEUtils.request(adminAddress, funcName, null, ABI, contractName, contractAddress);
    }
}
