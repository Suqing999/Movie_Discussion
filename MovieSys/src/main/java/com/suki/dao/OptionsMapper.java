package com.suki.dao;

import com.suki.pojo.Options;

public interface OptionsMapper {
    Options getOptions();

    void insert(Options options);

    void update(Options options);
}
