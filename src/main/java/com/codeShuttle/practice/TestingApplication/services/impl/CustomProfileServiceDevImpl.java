package com.codeShuttle.practice.TestingApplication.services.impl;

import com.codeShuttle.practice.TestingApplication.services.CustomProfileService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class CustomProfileServiceDevImpl implements CustomProfileService {

    @Override
    public String getData() {
        return "dev";
    }
}
