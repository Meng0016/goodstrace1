pragma solidity ^0.4.25;
import "./Roles.sol";  //可以调用添加、删除、判断

contract Producer {
    using Roles for Roles.Role;  //生成一个角色
    event ProducerAdded(address indexed account);  //定义事件，当添加
    event ProducerRemoved(address indexed account);
    Roles.Role private _producers;

    constructor (address producer) public {  //构造
        _addProducer(producer);
    }
    modifier onlyProducer() {  //修正、检查安全
        require(isProducer(msg.sender), "ProducerRole: caller does not have the Producer role");
        _;
    }

    function isProducer(address account) public view returns (bool) {
        return _producers.has(account);  //判断
    }
    function addProducer(address account) public onlyProducer {
        _addProducer(account);  //添加生产商 ——直接调用
    }
    function renounceProducer() public {
        _removeProducer(msg.sender);  //删除  函数重名
    }

    function _addProducer(address account) internal {
        _producers.add(account);
        emit ProducerAdded(account);  //触发事件
    }
    function _removeProducer(address account) internal {
        _producers.remove(account);
        emit ProducerRemoved(account);
    }
}