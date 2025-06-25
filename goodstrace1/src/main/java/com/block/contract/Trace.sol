pragma solidity >=0.4.22 <0.6.0;
pragma experimental ABIEncoderV2;
import "./GoodsInfoItem.sol";
import "./Distributor.sol";
import "./Producer.sol";
import "./Retailer.sol";

contract Trace is Producer, Distributor, Retailer {
    mapping(uint256 => address) goods; //商品溯源id到具体商品溯源合约的映射表
    uint[] goodList;

    //构造函数
    constructor(
        address producer,
        address distributor,
        address retailer
    ) public Producer(producer) Distributor(distributor) Retailer(retailer) {}

    //name 商品名称
    //traceNumber 商品溯源id
    //traceName 当前用户名称
    //quality 当前商品质量
    function newGood(
        string name,
        uint256 traceNumber,
        string traceName,
        uint8 quality,
        string location
    ) public onlyProducer returns (address) {
        require(goods[traceNumber] == address(0), "traceNumber already exist");
        GoodsInfoItem good = new GoodsInfoItem(
            name,
            traceName,
            quality,
            msg.sender,
            location
        );
        goods[traceNumber] = good;
        goodList.push(traceNumber);
        return good;
    }

    function addTraceInfoByDistributor(
        uint256 traceNumber,
        string traceName,
        uint8 quality,
        string location
    ) public onlyDistributor returns (bool) {
        require(goods[traceNumber] != address(0), "traceNumber does not exist");
        return
            GoodsInfoItem(goods[traceNumber]).addTraceInfoByDistributor(
            traceName,
            msg.sender,
            quality,
            location
        );
    }

    function addTraceInfoByRetailer(
        uint256 traceNumber,
        string traceName,
        uint8 quality,
        string location
    ) public onlyRetailer returns (bool) {
        require(goods[traceNumber] != address(0), "traceNumber does not exist");
        return
            GoodsInfoItem(goods[traceNumber]).addTraceInfoByRetailer(
            traceName,
            msg.sender,
            quality,
            location
        );
    }

    //string[] 保存商品流转过程中各个阶段的相关信息
    //address[] 保存商品流转过程各个阶段的用户地址信息（和用户一对应）
    //uint8[] 保存商品流转过程中各个阶段的状态变化
    function getTraceInfo(
        uint256 traceNumber
    ) public constant returns (uint[], string[], address[], uint8[],string[]) {
require(goods[traceNumber] != address(0), "traceNumber does not exist");
return GoodsInfoItem(goods[traceNumber]).getTraceInfo();
}

function getGood(
uint256 traceNumber
) public constant returns (uint, string, string, string, address, uint8,string) {
require(goods[traceNumber] != address(0), "traceNumber does not exist");
return GoodsInfoItem(goods[traceNumber]).getGoods();
}

function getAllGood() public constant returns (uint[]) {
return goodList;
}
}