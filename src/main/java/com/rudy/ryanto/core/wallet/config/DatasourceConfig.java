package com.rudy.ryanto.core.wallet.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatasourceConfig {

    /**
     * This class is configuration for datasource
     */
    @Value("${db.host}")
    private String host;
    @Value("${db.username}")
    private String username;
    @Value("${db.password}")
    private String password;
    @Value("${db.pool.maximum}")
    private String maxPool;
    @Value("${db.pool.minimum}")
    private String minPool;
    @Value("${db.driver.class.name}")
    private String driverClassName;
    @Value("${db.pool.idle}")
    private String minPoolIdle;
    @Value("${db.pool.leak.detect.threshold}")
    private String leakDetectionThreshold;
    @Value("${db.pool.life.time.max}")
    private String maxLifeTime;
    @Value("${db.pool.connect.time.out}")
    private String connectionTimeOut;
    @Value("${db.pool.keepAlive}")
    private String keepAlive;

    public HikariConfig hikariConfig() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(host);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setMaximumPoolSize(Integer.parseInt(maxPool));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(minPool));
        hikariConfig.setMinimumIdle(Integer.parseInt(minPoolIdle));
        hikariConfig.setLeakDetectionThreshold(Long.parseLong(leakDetectionThreshold));
        hikariConfig.setMaxLifetime(Long.parseLong(maxLifeTime));
        hikariConfig.setConnectionTimeout(Long.parseLong(connectionTimeOut));
        hikariConfig.setKeepaliveTime(Long.parseLong(keepAlive));
        hikariConfig.setPoolName("db-pool-");
        return hikariConfig;
    }

    @Bean
    public static HikariDataSource hikariDataSource() {
        return new HikariDataSource(hikariDataSource());
    }
}