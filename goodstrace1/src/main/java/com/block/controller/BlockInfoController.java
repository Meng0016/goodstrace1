package com.block.controller;

import com.alibaba.fastjson.JSONObject;
import com.block.dao.DBConnection;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class BlockInfoController {
    @ResponseBody
    @CrossOrigin
    @PostMapping(path = "/getBlockInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject getBlockInfo(@RequestBody JSONObject jsonObject) throws Exception {
        JSONObject output = new JSONObject();
        JSONObject blockInfo = DBConnection.getBlockInfoByTraceNumber(jsonObject.getString("traceNumber"),
                jsonObject.getString("type"));
        output.put("blockInfo", blockInfo);
        return output;
    }
}