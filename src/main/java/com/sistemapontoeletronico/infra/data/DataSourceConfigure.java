package com.sistemapontoeletronico.infra.data;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class DataSourceConfigure {
    private String driverClassName = "org.postgresql.Driver";
    private String url = "jdbc:postgresql://0.0.0.0:5432/relogioponto";
    private String username = "trainee";
    private String password = "123";
    private Database database = Database.POSTGRESQL;
    private boolean generateDdl = true;
    private boolean showSql = false;
    private boolean prepareConnection = true;
    private String databasePlatform = "org.hibernate.dialect.PostgreSQLDialect";

    @Bean
    public DataSource dataSource() {
        DataSource source = DataSourceBuilder
                            .create()
                            .driverClassName(driverClassName)
                            .url(url)
                            .username(username)
                            .password(password)
                            .build();
        return source;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(database);
        adapter.setGenerateDdl(generateDdl);
        adapter.setShowSql(showSql);
        adapter.setDatabasePlatform(databasePlatform);
        adapter.setPrepareConnection(prepareConnection);
        return adapter;
    }
}
