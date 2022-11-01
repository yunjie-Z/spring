package cn.zyj.springframework.beans.factory.support;

import cn.zyj.springframework.beans.BeansException;
import cn.zyj.springframework.core.io.Resource;
import cn.zyj.springframework.core.io.ResourceLoader;

public interface BeanDefinitionReader {
    BeanDefinitionRegistry getRegistry();

    ResourceLoader getResourceLoader();

    void loadBeanDefinitions(Resource resource)throws BeansException;

    void loadBeanDefinitions(Resource... resources)throws BeansException;

    void loadBeanDefinitions(String location)throws BeansException;

    void loadBeanDefinitions(String... locations) throws BeansException;
}
