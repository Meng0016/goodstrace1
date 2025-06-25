pragma solidity ^0.4.25;
import "./Roles.sol";  //可以调用添加、删除、判断

contract Retailer {
    using Roles for Roles.Role;  //生成一个角色
    event RetailerAdded(address indexed account);  //定义事件，当添加
    event RetailerRemoved(address indexed account);
    Roles.Role private _retailer;

    constructor (address retailer) public {  //构造
        _addRetailer(retailer);
    }
    modifier onlyRetailer() {  //修正、检查安全
        require(isRetailer(msg.sender), "RetailerRole: caller does not have the Retailer role");
        _;
    }

    function isRetailer(address account) public view returns (bool) {
        return _retailer.has(account);  //判断
    }
    function addRetailer(address account) public onlyRetailer {
        _addRetailer(account);  //添加生产商 ——直接调用
    }
    function renounceRetailer() public {
        _removeRetailer(msg.sender);  //删除  函数重名
    }

    function _addRetailer(address account) internal {
        _retailer.add(account);
        emit RetailerAdded(account);  //触发事件
    }
    function _removeRetailer(address account) internal {
        _retailer.remove(account);
        emit RetailerRemoved(account);
    }
}