package com.block.controller;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.hutool.core.lang.Dict;
import com.block.dao.DBConnection;
import com.block.service.BlockInfoServe;
import com.block.service.TraceServe;

@Controller
public class TraceController {

    private static final int TIME_LIST_INDEX = 0;
    private static final int NAME_LIST_INDEX = 1;
    private static final int ADDRESS_LIST_INDEX = 2;
    private static final int QUALITY_LIST_INDEX = 3;
    private static final int LOCATION_LIST_INDEX = 4;

    @Resource
    private TraceServe traceServe;
    @Resource
    private BlockInfoServe blockInfoServe;

    @Value("${system.contract.producerAddress}")
    public String producerAddress;
    @Value("${system.contract.retailerAddress}")
    public String retailerAddress;
    @Value("${system.contract.distributorAddress}")
    public String distributorAddress;
    @Value("${system.contract.traceAddress}")
    public String contractAddress;


    @ResponseBody
    @GetMapping(path = "/userinfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public String userInfo(String userName) {
        JSONObject _outPut = new JSONObject();
        if (userName.equals("producer")) {
            _outPut.put("address", producerAddress);
        } else if (userName.equals("distributor")) {
            _outPut.put("address", distributorAddress);
        } else if (userName.equals("retailers")) {
            _outPut.put("address", retailerAddress);
        } else {
            _outPut.put("error", "user not found");
        }
        return _outPut.toJSONString();
    }

    @ResponseBody
    @PostMapping(path = "/addProduce", produces = MediaType.APPLICATION_JSON_VALUE)
    public String addProduce(@RequestBody JSONObject jsonParam) throws SQLException {
        JSONObject _outPutObj = new JSONObject();
        String account = jsonParam.getString("account");
        System.out.println(account);
        Dict responseBody = traceServe.addProducer(account);
        JSONObject msg = JSONObject.parseObject((String) responseBody.get("result"));
        Dict blockInfo = blockInfoServe.blockByHash((String) msg.get("blockHash"));
        JSONObject blockInfoJson = JSONObject.parseObject((String) blockInfo.get("result"));
        int trace_number = jsonParam.containsKey("traceNumber") ? (int) jsonParam.get("traceNumber") : 0;

        if (msg.get("message").equals("Success")) {
            _outPutObj.put("ret", 1);
            _outPutObj.put("msg", msg.get("message"));
            _outPutObj.put("data", msg);
            _outPutObj.put("blockInfo", blockInfoJson);
            DBConnection.addBlockInfo(msg);
            DBConnection.addGoodsInfo(String.valueOf(trace_number), msg.getString("blockNumber"), "producer");
        } else {
            _outPutObj.put("ret", 0);
            _outPutObj.put("msg", msg.get("message"));
        }
        return _outPutObj.toJSONString();
    }

    @ResponseBody
    @PostMapping(path = "/addDistributor", produces = MediaType.APPLICATION_JSON_VALUE)
    public String addDistributor(String account) throws SQLException {
        JSONObject _outPutObj = new JSONObject();
        Dict responseBody = traceServe.addDistributor(account);
        JSONObject msg = JSONObject.parseObject((String) responseBody.get("result"));
        Dict blockInfo = blockInfoServe.blockByHash((String) msg.get("blockHash"));
        JSONObject blockInfoJson = JSONObject.parseObject((String) blockInfo.get("result"));

        if (msg.get("message").equals("Success")) {
            _outPutObj.put("ret", 1);
            _outPutObj.put("msg", msg.get("message"));
            _outPutObj.put("data", msg);
            _outPutObj.put("blockInfo", blockInfoJson);
            DBConnection.addBlockInfo(msg);
        } else {
            _outPutObj.put("ret", 0);
            _outPutObj.put("msg", msg.get("message"));
        }
        return _outPutObj.toJSONString();
    }

    @ResponseBody
    @PostMapping(path = "/addRetailer", produces = MediaType.APPLICATION_JSON_VALUE)
    public String addRetailer(String account) throws SQLException {
        JSONObject _outPutObj = new JSONObject();
        Dict responseBody = traceServe.addRetailer(account);
        JSONObject msg = JSONObject.parseObject((String) responseBody.get("result"));
        Dict blockInfo = blockInfoServe.blockByHash((String) msg.get("blockHash"));
        JSONObject blockInfoJson = JSONObject.parseObject((String) blockInfo.get("result"));

        if (msg.get("message").equals("Success")) {
            _outPutObj.put("ret", 1);
            _outPutObj.put("msg", msg.get("message"));
            _outPutObj.put("data", msg);
            _outPutObj.put("blockInfo", blockInfoJson);
            DBConnection.addBlockInfo(msg);
        } else {
            _outPutObj.put("ret", 0);
            _outPutObj.put("msg", msg.get("message"));
        }
        return _outPutObj.toJSONString();
    }

    @ResponseBody
    @GetMapping(path = "/isProducer", produces = MediaType.APPLICATION_JSON_VALUE)
    public String isProducer(String account) {
        JSONObject _outPutObj = new JSONObject();
        Dict responseBody = traceServe.isProducer(account);
        String s = (String) responseBody.get("result");
        if (s.contains("true")) {
            _outPutObj.put("ret", 1);
        } else {
            _outPutObj.put("ret", 0);
        }
        return _outPutObj.toJSONString();
    }

    @ResponseBody
    @GetMapping(path = "/isDistributor", produces = MediaType.APPLICATION_JSON_VALUE)
    public String isDistributor(String account) {
        JSONObject _outPutObj = new JSONObject();
        Dict responseBody = traceServe.isDistributor(account);
        String s = (String) responseBody.get("result");
        if (s.contains("true")) {
            _outPutObj.put("ret", 1);
        } else {
            _outPutObj.put("ret", 0);
        }
        return _outPutObj.toJSONString();
    }

    @ResponseBody
    @GetMapping(path = "/isRetailer", produces = MediaType.APPLICATION_JSON_VALUE)
    public String isRetailer(String account) {
        JSONObject _outPutObj = new JSONObject();
        Dict responseBody = traceServe.isRetailer(account);
        String s = (String) responseBody.get("result");
        if (s.contains("true")) {
            _outPutObj.put("ret", 1);
        } else {
            _outPutObj.put("ret", 0);
        }
        return _outPutObj.toJSONString();
    }

    @ResponseBody
    @PostMapping(path = "/produce", produces = MediaType.APPLICATION_JSON_VALUE)
    public String produce(@RequestBody JSONObject jsonParam) throws SQLException {
        JSONObject _outPutObj = new JSONObject();
        if (jsonParam == null) {
            _outPutObj.put("error", "invalid parameter");
            return _outPutObj.toJSONString();
        }
        int trace_number = jsonParam.containsKey("traceNumber") ? (int) jsonParam.get("traceNumber") : 0;
        String goods_name = jsonParam.containsKey("goodsName") ? (String) jsonParam.get("goodsName") : "";
        String trace_name = jsonParam.containsKey("traceName") ? (String) jsonParam.get("traceName") : "";
        int quality = jsonParam.containsKey("quality") ? (int) jsonParam.get("quality") : 0;
        String location = jsonParam.containsKey("location") ? (String) jsonParam.get("location") : "";
        String producerAddress = jsonParam.containsKey("producerAddress") ? (String) jsonParam.get("producerAddress") : "";

        Dict responseBody = traceServe.newGood(producerAddress, goods_name, trace_number, trace_name, quality, location);
        JSONObject msg = JSONObject.parseObject((String) responseBody.get("result"));
        Dict blockInfo = blockInfoServe.blockByHash((String) msg.get("blockHash"));
        JSONObject blockInfoJson = JSONObject.parseObject((String) blockInfo.get("result"));

        if (msg.get("message").equals("Success")) {
            _outPutObj.put("ret", 1);
            _outPutObj.put("msg", msg.get("message"));
            _outPutObj.put("data", msg);
            _outPutObj.put("blockInfo", blockInfoJson);
            DBConnection.addBlockInfo(msg);
            DBConnection.addGoodsInfo(String.valueOf(trace_number), msg.getString("blockNumber"), "producer");
        } else {
            _outPutObj.put("ret", 0);
            _outPutObj.put("msg", msg.get("message"));
        }
        return _outPutObj.toJSONString();
    }

    @ResponseBody
    @PostMapping(path = "/addDistribution", produces = MediaType.APPLICATION_JSON_VALUE)
    public String addDistribution(@RequestBody JSONObject jsonParam) throws SQLException {
        JSONObject _outPutObj = new JSONObject();
        if (jsonParam == null) {
            _outPutObj.put("error", "invalid parameter");
            return _outPutObj.toJSONString();
        }
        int trace_number = jsonParam.containsKey("traceNumber") ? (int) jsonParam.get("traceNumber") : 0;
        String trace_name = jsonParam.containsKey("traceName") ? (String) jsonParam.get("traceName") : "";
        int quality = jsonParam.containsKey("quality") ? (int) jsonParam.get("quality") : 0;
        String location = jsonParam.containsKey("location") ? (String) jsonParam.get("location") : "";
        String distributorAddress = jsonParam.containsKey("distributorAddress") ? (String) jsonParam.get("distributorAddress") : "";

        Dict responseBody = traceServe.addTraceInfoByDistributor(distributorAddress, trace_number, trace_name, quality, location);
        JSONObject msg = JSONObject.parseObject((String) responseBody.get("result"));
        Dict blockInfo = blockInfoServe.blockByHash((String) msg.get("blockHash"));
        JSONObject blockInfoJson = JSONObject.parseObject((String) blockInfo.get("result"));

        if (msg.get("message").equals("Success")) {
            _outPutObj.put("ret", 1);
            _outPutObj.put("msg", msg.get("message"));
            _outPutObj.put("data", msg);
            _outPutObj.put("blockInfo", blockInfoJson);
            DBConnection.addBlockInfo(msg);
            DBConnection.addGoodsInfo(String.valueOf(trace_number), msg.getString("blockNumber"), "distributor");
        } else {
            _outPutObj.put("ret", 0);
            _outPutObj.put("msg", msg.get("message"));
        }
        return _outPutObj.toJSONString();
    }

    @ResponseBody
    @PostMapping(path = "/addRetail", produces = MediaType.APPLICATION_JSON_VALUE)
    public String add_trace_by_retailer(@RequestBody JSONObject jsonParam) throws SQLException {
        JSONObject _outPutObj = new JSONObject();
        if (jsonParam == null) {
            _outPutObj.put("error", "invalid parameter");
            return _outPutObj.toJSONString();
        }
        int trace_number = jsonParam.containsKey("traceNumber") ? (int) jsonParam.get("traceNumber") : 0;
        String trace_name = jsonParam.containsKey("traceName") ? (String) jsonParam.get("traceName") : "";
        int quality = jsonParam.containsKey("quality") ? (int) jsonParam.get("quality") : 0;
        String location = jsonParam.containsKey("location") ? (String) jsonParam.get("location") : "";
        String retailerAddress = jsonParam.containsKey("retailerAddress") ? (String) jsonParam.get("retailerAddress") : "";

        Dict responseBody = traceServe.addTraceInfoByRetailer(retailerAddress, trace_number, trace_name, quality, location);
        JSONObject msg = JSONObject.parseObject((String) responseBody.get("result"));
        Dict blockInfo = blockInfoServe.blockByHash((String) msg.get("blockHash"));
        JSONObject blockInfoJson = JSONObject.parseObject((String) blockInfo.get("result"));

        if (msg.get("message").equals("Success")) {
            _outPutObj.put("ret", 1);
            _outPutObj.put("msg", msg.get("message"));
            _outPutObj.put("data", msg);
            _outPutObj.put("blockInfo", blockInfoJson);
            DBConnection.addBlockInfo(msg);
            DBConnection.addGoodsInfo(String.valueOf(trace_number), msg.getString("blockNumber"), "retailer");
        } else {
            _outPutObj.put("ret", 0);
            _outPutObj.put("msg", msg.get("message"));
        }
        return _outPutObj.toJSONString();
    }

    @ResponseBody
    @GetMapping(path = "/foodlist", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getlist() {
        Dict responseBody = traceServe.getAllGood();
        String resultString = responseBody.get("result").toString();
        JSONArray numList = JSON.parseArray(resultString);
        String numList2 = numList.getString(0);
        JSONArray innerArray = JSON.parseArray(numList2);
        JSONArray resList = new JSONArray();
        for (int i = 0; i < innerArray.size(); i++) {
            Dict good = traceServe.getGood((Integer) innerArray.get(i));
            resList.add(good);
        }
        return resList.toJSONString();
    }

    @ResponseBody
    @GetMapping(path = "/trace", produces = MediaType.APPLICATION_JSON_VALUE)
    public String trace(int traceNumber) {
        JSONObject _outPut = new JSONObject();
        if (traceNumber <= 0) {
            _outPut.put("error", "invalid parameter");
            return _outPut.toJSONString();
        }
        List res = get_trace(traceNumber);
        JSONArray o = new JSONArray(res);
        return o.toJSONString();
    }

    @ResponseBody
    @GetMapping(path = "/good", produces = MediaType.APPLICATION_JSON_VALUE)
    public String goods(int traceNumber) {
        JSONObject _outPut = new JSONObject();
        if (traceNumber <= 0) {
            _outPut.put("error", "invalid parameter");
            return _outPut.toJSONString();
        }
        return get_food(traceNumber);
    }

    @ResponseBody
    @GetMapping(path = "/newtracelist", produces = MediaType.APPLICATION_JSON_VALUE)
    public String get_latest() {
        String jsonString = get_good_list().toString();
        JSONArray outerArray = JSON.parseArray(jsonString);
        JSONArray innerArray = JSON.parseArray(outerArray.getString(0));
        int[] num_list = new int[innerArray.size()];
        for (int i = 0; i < innerArray.size(); i++) {
            num_list[i] = innerArray.getInteger(i);
        }
        JSONArray resList = new JSONArray();
        for (int j = 0; j < num_list.length; j++) {
            List trace = get_trace(num_list[j]);
            resList.add(trace.get(trace.size() - 1));
        }
        return resList.toJSONString();
    }

    @ResponseBody
    @GetMapping(path = "/producing", produces = MediaType.APPLICATION_JSON_VALUE)
    public String get_producing() {
        String jsonString = get_good_list().toString();
        JSONArray outerArray = JSON.parseArray(jsonString);
        JSONArray innerArray = JSON.parseArray(outerArray.getString(0));
        int[] num_list = new int[innerArray.size()];
        for (int i = 0; i < innerArray.size(); i++) {
            num_list[i] = innerArray.getInteger(i);
        }
        JSONArray resList = new JSONArray();
        for (int i = 0; i < num_list.length; i++) {
            JSONArray trace = get_trace(num_list[i]);
            if (trace.size() == 1) {
                resList.add(trace.get(0));
            }
        }
        return resList.toJSONString();
    }

    @ResponseBody
    @GetMapping(path = "/distributing", produces = MediaType.APPLICATION_JSON_VALUE)
    public String get_distributing() {
        String jsonString = get_good_list().toString();
        JSONArray outerArray = JSON.parseArray(jsonString);
        JSONArray innerArray = JSON.parseArray(outerArray.getString(0));
        int[] num_list = new int[innerArray.size()];
        for (int i = 0; i < innerArray.size(); i++) {
            num_list[i] = innerArray.getInteger(i);
        }
        JSONArray resList = new JSONArray();
        for (int i = 0; i < num_list.length; i++) {
            List trace = get_trace(num_list[i]);
            if (trace.size() == 2) {
                resList.add(trace.get(1));
            }
        }
        return resList.toJSONString();
    }

    @ResponseBody
    @GetMapping(path = "/retailing", produces = MediaType.APPLICATION_JSON_VALUE)
    public String get_retailing() {
        String jsonString = get_good_list().toString();
        JSONArray outerArray = JSON.parseArray(jsonString);
        JSONArray innerArray = JSON.parseArray(outerArray.getString(0));
        int[] num_list = new int[innerArray.size()];
        for (int i = 0; i < innerArray.size(); i++) {
            num_list[i] = innerArray.getInteger(i);
        }
        JSONArray resList = new JSONArray();
        for (int i = 0; i < num_list.length; i++) {
            List trace = get_trace(num_list[i]);
            if (trace.size() == 3) {
                resList.add(trace.get(2));
            }
        }
        return resList.toJSONString();
    }

    /**
     * 获取商品列表，从服务层获取并转换为JSONArray
     * @return 包含商品列表的JSONArray
     */
    private JSONArray get_good_list() {
        String responseStr = traceServe.getAllGood().get("result").toString();
        JSONArray responseJsonObj = JSON.parseArray(responseStr);
        return responseJsonObj;
    }

    /**
     * 从链上获取某个商品的基本信息
     * @param traceNumber 商品溯源id，商品溯源过程中的标识符
     * @return 对应商品的信息JSON字符串
     */
    private String get_food(int traceNumber) {
        String responseStr = traceServe.getGood(traceNumber).get("result").toString();
        JSONArray food = JSON.parseArray(responseStr);

        JSONObject _outPut = new JSONObject();
        _outPut.put("timestamp", food.get(0));
        _outPut.put("produce", food.get(1));
        _outPut.put("name", food.get(2));
        _outPut.put("current", food.get(3));
        _outPut.put("address", food.get(4));
        _outPut.put("quality", food.get(5));

        return _outPut.toJSONString();
    }

    /**
     * 获取商品溯源信息并组装成特定JSON结构返回
     * @param traceNumber 商品溯源id，用于标识要查询的商品
     * @return 包含商品溯源各环节信息的JSONArray，每个元素是 JSONObject，包含商品溯源各字段信息
     */
    private JSONArray get_trace(int traceNumber) {
        // 获取商品基本信息
        String responseStr = traceServe.getGood(traceNumber).get("result").toString();
        JSONArray goods = JSON.parseArray(responseStr);
        // 获取商品溯源信息
        String jsonString = traceServe.getTraceInfo(traceNumber).get("result").toString();
        // 将JSON字符串解析为JSONArray
        JSONArray outerArray = JSON.parseArray(jsonString);
        // 初始化各类型的JSONArray
        JSONArray time_list = new JSONArray();
        JSONArray name_list = new JSONArray();
        JSONArray address_list = new JSONArray();
        JSONArray quality_list = new JSONArray();
        JSONArray location_list = new JSONArray();

        // 遍历每个内部数组，并将其元素添加到对应的JSONArray中
        for (int i = 0; i < outerArray.size(); i++) {
            JSONArray innerArray = JSON.parseArray(outerArray.getString(i));
            switch (i) {
                case TIME_LIST_INDEX: // 时间列表
                    time_list.addAll(innerArray);
                    break;
                case NAME_LIST_INDEX: // 名称列表
                    name_list.addAll(innerArray);
                    break;
                case ADDRESS_LIST_INDEX: // 地址列表
                    address_list.addAll(innerArray);
                    break;
                case QUALITY_LIST_INDEX: // 质量列表
                    quality_list.addAll(innerArray);
                    break;
                case LOCATION_LIST_INDEX: // 位置列表
                    location_list.addAll(innerArray);
                    break;
                default:
                    // 如果有更多的数组类型，可以在这里添加更多的case处理
                    break;
            }
        }

        JSONArray _outPut = new JSONArray();
        for (int i = 0; i < time_list.size(); i++) {
            if (i == 0) {
                JSONObject _outPutObj = new JSONObject();
                _outPutObj.put("traceNumber", traceNumber);
                _outPutObj.put("name", goods.get(2));
                _outPutObj.put("produce_time", goods.get(0));
                _outPutObj.put("timestamp", time_list.get(i));
                _outPutObj.put("from", name_list.get(i));
                _outPutObj.put("quality", quality_list.get(i));
                _outPutObj.put("from_address", address_list.get(i));
                _outPutObj.put("location", location_list.get(i));
                _outPut.add(_outPutObj);
            } else {
                JSONObject _outPutObj = new JSONObject();
                _outPutObj.put("traceNumber", traceNumber);
                _outPutObj.put("name", goods.get(2));
                _outPutObj.put("produce_time", goods.get(0));
                _outPutObj.put("timestamp", time_list.get(i));
                _outPutObj.put("from", name_list.get(i - 1));
                _outPutObj.put("to", name_list.get(i));
                _outPutObj.put("quality", quality_list.get(i));
                _outPutObj.put("from_address", address_list.get(i - 1));
                _outPutObj.put("to_address", address_list.get(i));
                _outPutObj.put("location", location_list.get(i));
                _outPut.add(_outPutObj);
            }
        }
        return _outPut;
    }
}
