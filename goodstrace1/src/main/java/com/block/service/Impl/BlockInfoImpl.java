package com.block.service.Impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.http.HttpUtil;
import com.block.service.BlockInfoServe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
// 通过块hash获取块信息
public class BlockInfoImpl implements BlockInfoServe {
    @Value("${webase.front.url}")
    private String url;

    @Override
    public Dict blockByHash(String blockHash) {
        // 发送HTTP POST请求到WeBASE-Front的交易处理接口
        String s = String.format("http://%s/WeBASE-Front/%d/web3/blockByHash/%s", url, 1, blockHash);
        String responseBody = HttpUtil.get(s);
        // 将响应体封装到Dict中并返回
        Dict retDict = new Dict();
        retDict.set("result", responseBody); // 将响应体放入Dict中的"result"键
        return retDict; // 返回包含响应结果的Dict
    }
}
