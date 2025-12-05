package com.aisystems.firefliescrmautomation.configuration;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IHSP-2 Create Java Spring Boot Project with basic Settings for supporting REST API Operations
 * https://AISystems.atlassian.net/browse/IHSP-2
 *
 * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
 * @since 2 Junio 2025 4:20 AM GMT -5 Bogotá DC Colombia
 */

@Configuration
@ConfigurationProperties(prefix = "spring.aisystems-datasource")
public class AISystemsDataSourceConfig {

    private String url;
    private String username;
    private String password;
    private String driverClassName;

    @Autowired
    private Environment env;

    private static final Logger log = LoggerFactory.getLogger(AISystemsDataSourceConfig.class);

    /**
     * 
     * Gets the database URL.
     * @return the database URL
     * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
     * @since 5 December 2025 4:45 AM GMT -5 Bogotá DC Colombia
     */
    public String getUrl() { return url; }
    /**
     * Sets the database URL.
     * @param url the database URL
     * @author Manuela Cortés Granados (manuelacortesgranados@gmail.com)
     * @since 5 December 2025 4:45 AM GMT -5 Bogotá DC Colombia
     */
    public void setUrl(String url) { this.url = url; }

    /**
     * Gets the database username.
     * @return the database username
     */
    public String getUsername() { return username; }
    /**
     * Sets the database username.
     * @param username the database username
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Gets the database password.
     * @return the database password
     */
    public String getPassword() { return password; }
    /**
     * Sets the database password.
     * @param password the database password
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * Gets the JDBC driver class name.
     * @return the driver class name
     */
    public String getDriverClassName() { return driverClassName; }
    /**
     * Sets the JDBC driver class name.
     * @param driverClassName the driver class name
     */
    public void setDriverClassName(String driverClassName) { this.driverClassName = driverClassName; }

    private static final Pattern PLACEHOLDER = Pattern.compile("\\$\\{([^}]+)}");

    /**
     * Resolves a value if it is a placeholder (e.g., ${VAR_NAME}), using Spring Environment or system environment variables.
     * @param value the value to resolve
     * @return the resolved value or the original value if not a placeholder
     */
    private String resolveIfPlaceholder(String value) {
        if (value == null) return null;
        Matcher m = PLACEHOLDER.matcher(value.trim());
        if (m.matches()) {
            String key = m.group(1);
            // Try Spring Environment first, then system env vars
            String v = env.getProperty(key);
            if (v == null) v = System.getenv(key);
            return v;
        }
        // Not a simple placeholder, return original
        return value;
    }

    /**
     * Creates and configures the main DataSource bean for the application.
     * Resolves all configuration properties and applies sensible defaults and fallbacks.
     * @return the configured DataSource
     */
    @Bean(name = "AISystemsDataSource")
    public DataSource dataSource() {
        String resolvedUrl = resolveIfPlaceholder(this.url);
        if (resolvedUrl == null) resolvedUrl = this.url; // fallback to original value if not placeholder
        String resolvedUsername = resolveIfPlaceholder(this.username);
        if (resolvedUsername == null) resolvedUsername = this.username;
        String resolvedPassword = resolveIfPlaceholder(this.password);
        if (resolvedPassword == null) resolvedPassword = this.password;
        String resolvedDriver = resolveIfPlaceholder(this.driverClassName);
        if (resolvedDriver == null) resolvedDriver = this.driverClassName;

        // If url still contains an unresolved placeholder or is null/empty, try common env var name as fallback
        if (resolvedUrl == null || resolvedUrl.trim().isEmpty() || resolvedUrl.contains("${")) {
            String alt = env.getProperty("AISYSTEMS_DB_DATASOURCE_URL");
            if (alt == null) alt = System.getenv("AISYSTEMS_DB_DATASOURCE_URL");
            if (alt != null && !alt.trim().isEmpty()) resolvedUrl = alt;
        }

        if (resolvedUsername == null || resolvedUsername.trim().isEmpty()) {
            String altU = env.getProperty("AISYSTEMS_DB_USERNAME");
            if (altU == null) altU = System.getenv("AISYSTEMS_DB_USERNAME");
            if (altU != null) resolvedUsername = altU;
        }

        if (resolvedPassword == null || resolvedPassword.trim().isEmpty()) {
            String altP = env.getProperty("AISYSTEMS_DB_PASSWORD");
            if (altP == null) altP = System.getenv("AISYSTEMS_DB_PASSWORD");
            if (altP != null) resolvedPassword = altP;
        }

        if (resolvedUrl == null || resolvedUrl.trim().isEmpty()) {
            throw new IllegalStateException("Database URL is not configured. Set 'AISYSTEMS_DB_DATASOURCE_URL' environment variable or configure 'spring.AISystems-datasource.url' in application.properties");
        }

        if (resolvedDriver == null || resolvedDriver.trim().isEmpty()) {
            resolvedDriver = "com.mysql.cj.jdbc.Driver"; // sensible default
        }

        // Log resolved values for debugging (mask password)
        try {
            String maskedUrl = resolvedUrl.replaceAll("(?<=://)([^:/]+)(?=:)", "****");
            // fallback mask if regex didn't match
            if (maskedUrl.equals(resolvedUrl)) {
                maskedUrl = resolvedUrl.replaceAll("(//).*@", "$1****@");
            }
            log.info("Resolved DB URL: {} (masked), username: {}", maskedUrl, resolvedUsername);
        } catch (Exception ex) {
            log.debug("Failed to mask DB URL for logging: {}", ex.getMessage());
        }

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(resolvedUrl);
        dataSource.setUsername(resolvedUsername);
        dataSource.setPassword(resolvedPassword);
        dataSource.setDriverClassName(resolvedDriver);

        dataSource.setMaximumPoolSize(20);
        dataSource.setMinimumIdle(20);
        dataSource.setConnectionTimeout(6000000);
        dataSource.setIdleTimeout(1000000);
        dataSource.setMaxLifetime(20000000);
        dataSource.setAutoCommit(true);

        return dataSource;
    }

    /**
     * Creates a JdbcTemplate bean using the configured DataSource.
     * @param ds the DataSource to use
     * @return a JdbcTemplate instance
     */
    @Bean(name = "AISystemsJdbcTemplate")
    public JdbcTemplate AISystemsJdbcTemplate(@Qualifier("AISystemsDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }
}
