package com.example.demo.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//import javax.sql.DataSource;
//
//@Configuration
//@MapperScan("com.example.demo.mapper")
//@EnableTransactionManagement
//public class MyBatisConfig {
//
//    /**
//     * Create and configure the SqlSessionFactory
//     */
//    @Bean
//    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
//        sessionFactory.setDataSource(dataSource);
//
//        // Set configuration file (if it exists)
//        Resource configLocation = new ClassPathResource("mappers/mybatis-config.xml");
//        if (configLocation.exists()) {
//            sessionFactory.setConfigLocation(configLocation);
//        } else {
//            // Set configuration programmatically if the file doesn't exist
//            org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
//            configuration.setMapUnderscoreToCamelCase(true);
//            sessionFactory.setConfiguration(configuration);
//        }
//
//        // Set mapper locations
//        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//        try {
//            Resource[] mapperLocations = resolver.getResources("classpath:mappers/**/*.xml");
//            if (mapperLocations != null && mapperLocations.length > 0) {
//                sessionFactory.setMapperLocations(mapperLocations);
//            }
//        } catch (Exception e) {
//            // If mapper files can't be found, we'll use annotation-based mappers
//            System.out.println("No mapper XML files found. Using annotation-based mappers.");
//        }
//
//        // Set type aliases package
//        sessionFactory.setTypeAliasesPackage("com.example.demo.model");
//
//        return sessionFactory.getObject();
//    }
//
//    /**
//     * Create SqlSessionTemplate
//     */
//    @Bean
//    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//
//    /**
//     * Configure transaction manager
//     */
//    @Bean
//    public TransactionManager transactionManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//}

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@MapperScan("com.example.demo.mapper")
@EnableTransactionManagement
public class MyBatisConfig {

    /**
     * Create and configure the SqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        // Configure programmatically instead of using XML
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setCacheEnabled(true);
        sessionFactory.setConfiguration(configuration);

        // Set mapper locations
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] mapperLocations = resolver.getResources("classpath:mappers/**/*.xml");
            // Filter out mybatis-config.xml to avoid parsing issues
            List<Resource> filteredLocations = new ArrayList<>();
            for (Resource resource : mapperLocations) {
                if (!resource.getFilename().equals("mybatis-config.xml")) {
                    filteredLocations.add(resource);
                }
            }
            sessionFactory.setMapperLocations(filteredLocations.toArray(new Resource[0]));
        } catch (Exception e) {
            System.out.println("Error finding mapper files: " + e.getMessage());
        }

        // Set type aliases package
        sessionFactory.setTypeAliasesPackage("com.example.demo.model");

        return sessionFactory.getObject();
    }

    /**
     * Create SqlSessionTemplate
     */
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * Configure transaction manager
     */
    @Bean
    public TransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}