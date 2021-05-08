package com.bruce;

/**
 * @BelongsProject: SSM-Annotation
 * @BelongsPackage: com.bruce
 * @CreateTime: 2021-05-08 16:34
 * @Description: TODO
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.util.Properties;

@Configuration
@EnableWebMvc
@ComponentScan("com.bruce.controller")
public class DispatcherConfig extends WebMvcConfigurerAdapter {


    @Autowired
    private PropertiesConfig propertyConfig;

    /**
     * 视图解析器
     * @return
     */
    @Bean
    public InternalResourceViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
        internalResourceViewResolver.setViewClass(JstlView.class);
        internalResourceViewResolver.setPrefix(propertyConfig.getWebViewPrefix());
        internalResourceViewResolver.setSuffix(propertyConfig.getWebViewSuffix());
        return internalResourceViewResolver;
    }

    /**
     * 设置统一错误处理要跳转的视图
     * @return
     */
    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver simpleMappingExceptionResolver = new SimpleMappingExceptionResolver();
        Properties properties = new Properties();
        properties.getProperty("java.lang.Exception", "error");
        simpleMappingExceptionResolver.setExceptionMappings(properties);
        return simpleMappingExceptionResolver;
    }

    /**
     * 添加静态资源
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(propertyConfig.getWebStaticHandler()).addResourceLocations(propertyConfig.getWebStaticResource()).setCachePeriod(propertyConfig.getWebStaticCachedPeriod());
    }

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
    }
}
