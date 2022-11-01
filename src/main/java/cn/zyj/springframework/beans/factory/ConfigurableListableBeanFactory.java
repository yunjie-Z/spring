package cn.zyj.springframework.beans.factory;

import cn.zyj.springframework.beans.BeansException;
import cn.zyj.springframework.beans.factory.config.AutowireCapableBeanFactory;
import cn.zyj.springframework.beans.factory.config.BeanDefinition;
import cn.zyj.springframework.beans.factory.config.BeanPostProcessor;
import cn.zyj.springframework.beans.factory.config.ConfigurableBeanFactory;
/**
 * Configuration interface to be implemented by most listable bean factories.
 * In addition to {@link ConfigurableBeanFactory}, it provides facilities to
 * analyze and modify bean definitions, and to pre-instantiate singletons.
 *
 */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {
    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    void preInstantiateSingletons() throws BeansException;

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
