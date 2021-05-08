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
 *  * tomcat启动时
 *  * 1)找到类路径下的META-INF目录
 *  * 2)找到META-INF文件夹下的services目录
 *  * 3)读取services目录下的/META-INF/services/javax.servlet.ServletContainerInitializer目录
 *  * 4)依据文件中定义的类全名(SpringServletContainerInitializer),并创建对象
 *  * 5)依据文件中定义的类上的注解@HandlesTypes注解声明的类型进行加载
 *  * 6)然后调用类(SpringServletContainerInitializer)中的onStartup方法,并将@HandlesTypes声明这些类型传递给此方法
 *  * 7)最后进行对象的创建和初始化
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