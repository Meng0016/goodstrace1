package com.block.utils;

import cn.hutool.core.lang.Dict;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.block.config.ConfigLoader;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.List;

@Component
public class WeBASEUtils {

    /**
     * 向WeBASE-Front发送交易请求的方法。
     *
     * @param userAddress    用户的区块链地址。
     * @param funcName       要调用的合约函数名称。
     * @param funcParam      调用合约函数时传递的参数列表，为String数组。
     * @param ABI            合约的ABI信息，用于确定如何调用合约。
     * @param contractName   合约的名称。
     * @param contractAddress 合约在区块链上的地址。
     * @return Dict 返回从WeBASE-Front接口获得的响应结果。
     */
    public static Dict request(
            String userAddress,
            String funcName,
            List funcParam,
            String ABI,
            String contractName,
            String contractAddress) {
        // 解析合约ABI成为JSON数组
        JSONArray abiJSON = JSONUtil.parseArray(ABI);

        // 构建请求数据的JSON对象
        JSONObject data = JSONUtil.createObj();
        data.set("groupId", "1"); // 设置群组ID，默认值为1
        data.set("user", userAddress); // 设置用户地址
        data.set("contractName", contractName); // 设置合约名称
        data.set("contractAddress", contractAddress); // 设置合约地址
        data.set("funcName", funcName); // 设置方法名
        data.set("funcParam", funcParam); // 设置方法参数，确保为JSONArray
        data.set("contractAbi", abiJSON); // 设置合约的ABI

        // 将请求数据转换为JSON字符串
        String dataString = JSONUtil.toJsonStr(data);
        String url = String.format("http://%s/WeBASE-Front/trans/handle", ConfigLoader.getWebASEUrl());

        // 发送HTTP POST请求到WeBASE-Front的交易处理接口
        String responseBody = HttpRequest.post(url)
                .header(Header.CONTENT_TYPE, "application/json") // 设置请求头，表明内容类型为JSON
                .body(dataString) // 设置请求体为JSON字符串
                .execute() // 执行请求
                .body(); // 获取响应体

        // 将响应体封装到Dict中并返回
        Dict retDict = new Dict();
        retDict.set("result", responseBody); // 将响应体放入Dict中的"result"键
        return retDict; // 返回包含响应结果的Dict
    }
}
