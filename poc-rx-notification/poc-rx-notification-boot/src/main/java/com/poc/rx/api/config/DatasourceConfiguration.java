package com.poc.rx.api.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.SimpleUserTypeResolver;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

/**
 * DataSource Configuration
 * @author Maksym Khudiakov
 */
@Configuration
@PropertySource("classpath:datasource.properties")
@EnableCassandraRepositories(basePackages = { "com.poc.rx" })
public class DatasourceConfiguration {

    @Value("${spring.data.cassandra.contact-points}")
    private String contactPoints;

    @Value("${spring.data.cassandra.local-datacenter}")
    private String localDatacenter;

    @Value("${spring.data.cassandra.keyspace-name}")
    private String keyspaceName;

    /* Factory bean that creates the com.datastax.oss.driver.api.core.CqlSession instance */
    @Bean
    public CqlSessionFactoryBean sessionFactory() {
        CqlSessionFactoryBean sessionFactory = new CqlSessionFactoryBean();
        sessionFactory.setLocalDatacenter(localDatacenter);
        sessionFactory.setContactPoints(contactPoints);
        sessionFactory.setKeyspaceName(keyspaceName);
        return sessionFactory;
    }

    @Bean
    public CassandraMappingContext mappingContext(CqlSession cqlSession) {
        CassandraMappingContext mappingContext = new CassandraMappingContext();
        mappingContext.setUserTypeResolver(new SimpleUserTypeResolver(cqlSession));
        return mappingContext;
    }

    @Bean
    public CassandraConverter converter(CassandraMappingContext mappingContext) {
        return new MappingCassandraConverter(mappingContext);
    }

    @Bean
    public CassandraOperations cassandraTemplate(CqlSessionFactoryBean sessionFactoryBean, CassandraConverter converter) {
        return new CassandraTemplate(sessionFactoryBean.getObject(), converter);
    }
}
