package com.epam.gym.service.other;

import com.epam.gym.service.GreetingService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@Slf4j
@Service
public class GreetingServiceImpl implements GreetingService {
    @Value("${greeting.message}")
    private String greeting;

    @Override
    public void logGreeting() {
        log.info(greeting);
    }
}