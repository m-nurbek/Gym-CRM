package com.epam.repository.manager;

import com.epam.entity.Entity;
import com.epam.repository.MapRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.List;

@Slf4j
public class RepositoryManagerImpl<T extends Entity<ID>, ID extends BigInteger> implements RepositoryManager {
    private final MapRepository<T, ID> repository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private File file;
    private final Class<T> entityClass;

    public RepositoryManagerImpl(MapRepository<T, ID> repository, String source) {
        this.repository = repository;

        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(source);

        try {
            file = resource.getFile();
        } catch (IOException e) {
            log.error("Failed to load the file {}: {}", resource.getFilename(), e.getMessage());
        }

        // Get the class of the entity
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
            this.entityClass = (Class<T>) actualTypeArguments[0];
        } else {
            throw new IllegalArgumentException("Class is not parameterized with generic types");
        }

        log.debug("Initialized bean {}", this.getClass().getName());
    }

    @Override
    @PostConstruct
    public void loadFromFile() {
        log.debug("FILE -> REPOSITORY: Trying to load data from a file '{}'", file.getName());

        try {
            if (file.exists()) {
                List<T> entityList = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, entityClass));
                repository.saveAll(entityList);
                log.debug("FILE -> REPOSITORY: Loaded data from the '{}' file into the repository", file.getName());
            }

        } catch (IOException e) {
            log.error("FILE -> REPOSITORY: Failed to load data from file: {}", e.getMessage());
        }
    }

//    @Override
//    @PreDestroy
//    public void saveToFile() {
//        log.debug("REPOSITORY -> FILE: Trying to load data to a file '{}'", file.getName());
//
//        try (FileWriter fileWriter = new FileWriter(file, false)) {
//            StringWriter writer = new StringWriter();
//            objectMapper.writeValue(writer, repository.findAll());
//            fileWriter.write(writer.toString());
//
//            log.debug("REPOSITORY -> FILE: Loaded data from the repository to the '{}' file", file.getName());
//
//        } catch (IOException e) {
//            log.error("REPOSITORY -> FILE: Failed to save data to file: {}", e.getMessage());
//        }
//    }
}