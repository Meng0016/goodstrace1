package com.block.service;

import cn.hutool.core.lang.Dict;

public interface BlockInfoServe {
    Dict blockByHash(String blockHash);
}