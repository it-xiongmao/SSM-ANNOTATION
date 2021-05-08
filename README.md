
最近在给学生们讲Spring+Mybatis整合，根据有的学生反映还是基于注解实现整合便于理解，毕竟在先前的工作中团队里还没有人完全舍弃配置文件进行项目开发，由于这两个原因，我索性参考spring官方文档研究出完全基于注解整合ssm框架。毕竟无配置化也是Spring官方所推行的，要不SpringBoot存在的意义为何嘛！
## 1.整合思路
1）目标：毫无保留的将配置文件的所有配置项改变注解加创建对象的方式实现
2）Spring提供的 `@Bean` `@Configuration` `@ComponentScan` `@EnableTransactionManagement` `@EnableWebMvc` 等 需要知道其含义

## 2.创建SSM的web项目
![在这里插入图片描述](https://img-blog.csdnimg.cn/2021050819202957.png)
## 3.在config包下分别创建配置类与属性文件
### 3.1. AppConfig.java
 没什么好说的，这里主要创建Spring与Mybatis整合的相关对象以及声明式事务切面，我们把配置文件中的东西通通用java代码创建，注意@Bean注解的使用
 

```java
package com.bruce;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @BelongsProject: SSM-Annotation
 * @BelongsPackage: com.bruce
 * @CreateTime: 2021-05-08 16:29
 * @Description: TODO
 */
@Configuration //表明此类是配置类
@ComponentScan // 扫描自定义的组件(repository service component controller)
@PropertySource("classpath:application.properties") // 读取application.properties
@MapperScan("com.bruce.mapper") //扫描Mybatis的Mapper接口
@EnableTransactionManagement //开启事务管理
public class AppConfig {

    /**
     * 配置数据源
     * @date 2018/6/24
     **/
    @Bean
    public DataSource dataSource(PropertiesConfig propertiesConfig) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(propertiesConfig.getUser());
        dataSource.setPassword(propertiesConfig.getPassword());
        dataSource.setUrl(propertiesConfig.getUrl());
        dataSource.setDriverClassName(propertiesConfig.getDriver());
        return dataSource;
    }

    /**
     * 配置mybatis的SqlSessionFactoryBean
     * @param dataSource
     * @param propertiesConfig
     * @return
     */
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource, PropertiesConfig propertiesConfig) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage(propertiesConfig.getMybatisTypeAliasPackage());
        // 动态获取SqlMapper
        PathMatchingResourcePatternResolver classPathResource = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(classPathResource.getResources(propertiesConfig.getMybatisMapperLocations()));
        return sqlSessionFactoryBean;
    }

    /**
     * 配置spring的声明式事务
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
        return dataSourceTransactionManager;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        return propertySourcesPlaceholderConfigurer;
    }
}

```
### 3.2.DispatcherConfig
此处配置SpringMVC的视图解析器，静态资源等，依旧照搬配置文件中的代码
```java
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

```
### 3.3.PropertiesConfig
此处用于读取application.properties的文件内容 注意@Value与@PropertySource的含义
```java
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
```
### 3.4.MyWebAppInitializer

```java
package com.bruce;

/**
 * @BelongsProject: SSM-Annotation
 * @BelongsPackage: com.bruce
 * @CreateTime: 2021-05-08 16:35
 * @Description: TODO
 */

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

/**
 *   初始化servlet WebApplicationContext 相关
 *  * tomcat启动时
 *  * 1)找到类路径下的META-INF目录
 *  * 2)找到META-INF文件夹下的services目录
 *  * 3)读取services目录下的/META-INF/services/javax.servlet.ServletContainerInitializer目录
 *  * 4)依据文件中定义的类全名(SpringServletContainerInitializer),并创建对象
 *  * 5)依据文件中定义的类上的注解@HandlesTypes注解声明的类型进行加载
 *  * 6)然后调用类(SpringServletContainerInitializer)中的onStartup方法,并将@HandlesTypes声明这些类型传递给此方法
 *  * 7)最后进行对象的创建和初始化
 * 在 Servlet 3.0 环境下，Servlet 容器会在 classpath 下搜索实现了 javax.servlet
 * .ServletContainerInitializer 接口的任何类，找到之后用它来初始化 Servlet 容器。
 * Spring 实现了以上接口，实现类叫做 SpringServletContainerInitializer， 它会依次搜寻实现了 WebApplicationInitializer的任何类，并委派这个类实现配置。之后，Spring 3.2 开始引入一个简易的 WebApplicationInitializer 实现类，这就是 AbstractAnnotationConfigDispatcherServletInitializer。
 * 所以 MyWebAppInitializer 继承 AbstractAnnotationConfigDispatcherServletInitializer之后，也就是间接实现了 WebApplicationInitializer，在 Servlet 3.0 容器中，它会被自动搜索到，被用来配置 servlet 上下文。
 *
 *  
 */
public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{AppConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{DispatcherServlet.class};
    }

    /**
     * DispatcherServlet 映射
     * @return
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    /**
     * 添加过滤器
     *
     * @return
     */
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return new Filter[]{characterEncodingFilter};
    }
}
```
在这里请大家关注一下这个类，这段代码的含义和配置SpringMVC的含义一样:
相当于以下配置：

```xml
<web-app>
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/root-context.xml</param-value>
    </context-param>
    <servlet>
        <servlet-name>dispatcher</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value></param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>dispatcher</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
</web-app>
```
## 3.5. application.properties

```properties
#数据库连接
spring.datasource.user=root
spring.datasource.password=123456
spring.datasource.driver=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/spring2021?characterEncoding=utf-8&useSSL=false

#web设置相关
spring.web.view.prefix=/WEB-INF/jsp/
spring.web.view.suffix=.jsp
spring.web.static.handler=/static/**
spring.web.static.resource=classpath:/static/
spring.web.static.cache.period=360000

#mybatis设置相关
mybatis.type.alias.package=com.bruce.pojo
mybatis.mapperLocations=classpath*:mappers/*.xml
```
## 3.6. 创建MyBatis对应的mapper

```java
package com.bruce.mapper;

import com.bruce.pojo.User;

import java.util.List;

/**
 * @BelongsProject: Spring-MyBatis-2021
 * @BelongsPackage: com.sm.mapper
 * @CreateTime: 2021-05-08 15:21
 * @Description: TODO
 */
public interface UserMapper {

    List<User> findUsers();

    int addUser(User user);
}

```
## 3.7.创建业务逻辑

```java
package com.bruce.service.impl;

import com.bruce.mapper.UserMapper;
import com.bruce.pojo.User;
import com.bruce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @BelongsProject: Spring-MyBatis-2021
 * @BelongsPackage: com.sm.service.impl
 * @CreateTime: 2021-05-08 15:26
 * @Description: TODO
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    public List<User> findUsers() {
        return userMapper.findUsers();
    }

    public int addUser(User user) {
        return userMapper.addUser(user);
    }
}

```
### 3.8.创建Controller

```java
package com.bruce.controller;

import com.bruce.pojo.User;
import com.bruce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @BelongsProject: SSM-WEB-ANNOTATION
 * @BelongsPackage: com.bruce.controller
 * @CreateTime: 2021-05-08 16:43
 * @Description: TODO
 */
@RequestMapping
@Controller
public class UserController {

    @Autowired
    UserService us;

    @ResponseBody
    @RequestMapping("/list")
    public List<User> list(){
        System.out.println("控制器");
        return us.findUsers();
    }

    @RequestMapping("/show")
    public String show(Model model){
        model.addAttribute("msg","SSM全注解整合");
        return "show";
    }

}

```
### 3.9.show.jsp文件中内容

```html
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
   <h1>show:${msg}</h1>
</body>
</html>
```
### 3.10.启动tomcat后访问http://localhost:8080/show到如下界面
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210508192756228.png)
### 3.11.访问控制器方法
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210508192850765.png)
### 3.12.访问静态资源
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210508192926793.png)
OK！大功告成，注意前4步里面注解的运用，后面的步骤和往常的写法无异，想必大家都很熟悉了吧。
