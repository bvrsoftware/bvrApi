package org.bvr.web.config;


import com.bvr.core.configuration.MultipleMongoProperties;
import com.mongodb.Block;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.connection.SocketSettings;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Log4j2
@Configuration
//@DependsOn({"secretsManagerService"})
@EnableConfigurationProperties(MultipleMongoProperties.class)
public class MongoConfig extends AbstractMongoClientConfiguration {
    private static final String MONGODB_PREFIX = "mongodb://";

    private final MongoProperties mongoProperties;

    private final MultipleMongoProperties multipleMongoProperties;

    public MongoConfig(MultipleMongoProperties multipleMongoProperties) {
        this.multipleMongoProperties = multipleMongoProperties;
        this.mongoProperties = multipleMongoProperties.getPrimary();

    }

    @Override
    protected String getDatabaseName() {
        return mongoProperties.getDatabase();
    }
    @Bean
    @Primary
    @Override
    public MongoClient mongoClient() {
        log.info("Primary MongoClient instantiated");
        SocketSettings.builder().connectTimeout(2, TimeUnit.MINUTES);
        MongoCredential mongoCredential = MongoCredential.createCredential(mongoProperties.getUsername(),
                mongoProperties.getAuthenticationDatabase(),
                mongoProperties.getPassword());
        ConnectionString connectionString = new ConnectionString(mongoProperties.getUri());
        log.info("Mongo URI = " + connectionString);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder().applyConnectionString(connectionString)
                .applyToConnectionPoolSettings(new Block<ConnectionPoolSettings.Builder>() {
                    @Override
                    public void apply(final ConnectionPoolSettings.Builder builder) {
                        builder.maxSize(400).maxSize(50);
                    }
                }).credential(mongoCredential).build();
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    @Primary
    @Override
    public MongoDatabaseFactory mongoDbFactory() {
        log.info("Primary MongoDbFactory instantiated");
        return new SimpleMongoClientDatabaseFactory(mongoClient(),getDatabaseName());
    }

    @Bean
    @Primary
    public MongoTemplate mongoTemplate() {
        log.info("Primary MongoTemplate instantiated");
        return new MongoTemplate(mongoDbFactory(), (MongoConverter) customConversions());
    }

    @Override
    public MongoCustomConversions customConversions() {
        log.info("Custom converter instantiated");
        List<Converter<?, ?>> converterList = new ArrayList<Converter<?, ?>>();
        return new MongoCustomConversions(converterList);
    }
}
