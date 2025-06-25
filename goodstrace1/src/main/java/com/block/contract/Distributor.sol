pragma solidity ^0.4.25;
import "./Roles.sol";  //可以调用添加、删除、判断

contract Distributor {
    using Roles for Roles.Role;  //生成一个角色
    event DistributorAdded(address indexed account);  //定义事件，当添加
    event DistributorRemoved(address indexed account);
    Roles.Role private _distributor;

    constructor (address distributor) public {  //构造
        _addDistributor(distributor);
    }
    modifier onlyDistributor() {  //修正、检查安全
        require(isDistributor(msg.sender), "DistributorRole: caller does not have the Distributor role");
        _;
    }

    function isDistributor(address account) public view returns (bool) {
        return _distributor.has(account);  //判断
    }
    function addDistributorr(address account) public onlyDistributor {
        _addDistributor(account);  //添加生产商 ——直接调用
    }
    function renounceDistributor() public {
        _removeDistributor(msg.sender);  //删除  函数重名
    }

    function _addDistributor(address account) internal {
        _distributor.add(account);
        emit DistributorAdded(account);  //触发事件
    }
    function _removeDistributor(address account) internal {
        _distributor.remove(account);
        emit DistributorRemoved(account);
    }
}