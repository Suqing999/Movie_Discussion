package com.suki.service;

import com.suki.pojo.Options;

public interface OptionsService {
    Options getOptions();

    void insertOptions(Options options);

    void updateOptions(Options options);
}
