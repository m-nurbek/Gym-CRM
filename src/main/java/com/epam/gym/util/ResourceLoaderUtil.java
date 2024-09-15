package com.epam.gym.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.IOException;

@UtilityClass
@Slf4j
public class ResourceLoaderUtil {
    public static File getResource(String source) {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(source);

        try {
            return resource.getFile();
        } catch (IOException e) {
            log.error("Failed to load the file {}: {}", resource.getFilename(), e.getMessage());
        }

        return null;
    }
}