package com.epam.gym.repository.hibernate.init;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

import static com.epam.gym.util.ResourceLoaderUtil.getResource;

@Component
@Slf4j
public class DatabaseInitializer {
    private final SessionFactory sessionFactory;
    private final File[] sqlData;

    @Autowired
    public DatabaseInitializer(SessionFactory sessionFactory, @Value("${sql.init-data}") String[] initData) {
        this.sessionFactory = sessionFactory;
        sqlData = new File[initData.length];

        for (int i = 0; i < initData.length; i++) {
            sqlData[i] = getResource(initData[i]);
        }
    }

    @PostConstruct
    public void loadSqlData() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            for (File file : sqlData) {
                String content = readSqlFile(file);

                String[] sqlStatements = content.split(";");

                // execute each sql statement
                for (String sql : sqlStatements) {
                    sql = sql.trim();
                    if (!sql.isEmpty()) {
                        session.createNativeQuery(sql).executeUpdate();
                    }
                }
            }

            transaction.commit();
            log.debug("SQL data loaded successfully!");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            log.error("Error while loading sql data to database: {}", e.getMessage());
        } finally {
            session.close();
        }
    }

    private String readSqlFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            return br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            log.error("Failed to read sql files: {}", e.getMessage());
        }

        return "";
    }
}