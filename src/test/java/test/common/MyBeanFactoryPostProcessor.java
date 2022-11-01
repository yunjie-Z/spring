package test.common;


import cn.zyj.springframework.beans.BeansException;
import cn.zyj.springframework.beans.PropertyValue;
import cn.zyj.springframework.beans.PropertyValues;
import cn.zyj.springframework.beans.factory.ConfigurableListableBeanFactory;
import cn.zyj.springframework.beans.factory.config.BeanDefinition;
import cn.zyj.springframework.beans.factory.config.BeanFactoryPostProcessor;

public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("userService");
        PropertyValues propertyValues = beanDefinition.getPropertyValues();

        propertyValues.addPropertyValue(new PropertyValue("company", "改为：字节跳动"));
    }

}
