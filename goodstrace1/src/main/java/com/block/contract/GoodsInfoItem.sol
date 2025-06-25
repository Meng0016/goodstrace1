pragma solidity >=0.4.22 <0.5.0;
pragma experimental ABIEncoderV2;

contract GoodsInfoItem {
    uint[] _timestamp; //保存商品流转过程中各个阶段的时间戳
    string[] _traceName; //保存商品流转过程各个阶段的用户名
    address[] _traceAddress;
    uint8[] _traceQuality; //保存商品流转过程中各个阶段的质量
    string[] _traceLocation;//保存商品流转过程中各个阶段的位置信息

    string _name; //商品名称
    string _currentTraceName; //当前用户名称
    uint8 _quality; //质量（0=优质 1=合格 2=不合格）
    uint8 _status; //状态（0:生产 1:分销 2:出售）
    string _location;
    address _owner;

    constructor(
        string memory name,
        string memory traceName,
        uint8 quality,
        address producer,
        string memory location
    ) public {
        _timestamp.push(now);
        _traceName.push(traceName);
        _traceAddress.push(producer);
        _traceQuality.push(quality);
        _traceLocation.push(location);
        _name = name;
        _currentTraceName = traceName;
        _quality = quality;
        _status = 0;
        _location = location;
        _owner = msg.sender;
    }

    function addTraceInfoByDistributor(
        string memory traceName,
        address distributor,
        uint8 quality,
        string memory location
    ) public returns (bool) {
        require(_status == 0, "status must be producing");
        require(_owner == msg.sender, "only trace contract can invoke");
        _timestamp.push(now);
        _traceName.push(traceName);
        _currentTraceName = traceName;
        _traceAddress.push(distributor);
        _traceLocation.push(location);
        _quality = quality;
        _traceQuality.push(_quality);
        _location = location;
        _status = 1;
        return true;
    }

    function addTraceInfoByRetailer(
        string memory traceName,
        address retailer,
        uint8 quality,
        string memory location
    ) public returns (bool) {
        require(_status == 1, "status must be distributing");
        require(_owner == msg.sender, "only trace contract can invoke");
        _timestamp.push(now);
        _traceName.push(traceName);
        _currentTraceName = traceName;
        _traceAddress.push(retailer);
        _traceLocation.push(location);
        _quality = quality;
        _traceQuality.push(_quality);
        _status = 2;
        _location = location;
        return true;
    }

    function getTraceInfo() public view
    returns (uint[] memory, string[] memory, address[] memory, uint8[] memory, string[] memory) {
        return (_timestamp, _traceName, _traceAddress, _traceQuality, _traceLocation);
    }

    function getGoods() public view
    returns (uint, string memory, string memory, string memory, address, uint8, string memory) {
        return (
            _timestamp[0],
            _traceName[0],   //角色名
            _name,   ///商品名
            _currentTraceName,  //当前溯源角色名字
            _traceAddress[0],
            _quality,
            _location
        );
    }
}