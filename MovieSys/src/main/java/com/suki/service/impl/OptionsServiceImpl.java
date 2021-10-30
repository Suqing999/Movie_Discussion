package com.suki.service.impl;

import com.suki.dao.OptionsMapper;
import com.suki.pojo.Options;
import com.suki.service.OptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OptionsServiceImpl implements OptionsService {

    @Autowired
    private OptionsMapper optionsMapper;

    @Override
    public Options getOptions() {
        return optionsMapper.getOptions();
    }

    @Override
    public void insertOptions(Options options) {
        optionsMapper.insert(options);
    }

    @Override
    public void updateOptions(Options options) {
        optionsMapper.update(options);
    }
}
