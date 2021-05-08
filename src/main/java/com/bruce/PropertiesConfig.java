package com.bruce;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("classpath:application.properties")
public class PropertiesConfig {

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.driver}")
    private String driver;
    @Value("${spring.datasource.user}")
    private String user;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.web.view.prefix}")
    private String webViewPrefix;
    @Value("${spring.web.view.suffix}")
    private String webViewSuffix;
    @Value("${spring.web.static.handler}")
    private String webStaticHandler;
    @Value("${spring.web.static.resource}")
    private String webStaticResource;
    @Value("${spring.web.static.cache.period}")
    private Integer webStaticCachedPeriod;
    @Value("${mybatis.type.alias.package}")
    private String mybatisTypeAliasPackage;

    @Value("${mybatis.mapperLocations}")
    private String mybatisMapperLocations;

    public String getWebViewPrefix() {
        return webViewPrefix;
    }

    public String getWebViewSuffix() {
        return webViewSuffix;
    }

    public String getWebStaticHandler() {
        return webStaticHandler;
    }

    public String getWebStaticResource() {
        return webStaticResource;
    }

    public Integer getWebStaticCachedPeriod() {
        return webStaticCachedPeriod;
    }

    public String getMybatisTypeAliasPackage() {
        return mybatisTypeAliasPackage;
    }

    public String getUrl() {
        return url;
    }

    public String getDriver() {
        return driver;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getMybatisMapperLocations() {
        return mybatisMapperLocations;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}