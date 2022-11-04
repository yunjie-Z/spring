package cn.zyj.springframework.beans.factory;

import cn.zyj.springframework.beans.BeansException;
import cn.zyj.springframework.beans.PropertyValue;
import cn.zyj.springframework.beans.PropertyValues;
import cn.zyj.springframework.beans.factory.config.BeanDefinition;
import cn.zyj.springframework.beans.factory.config.BeanFactoryPostProcessor;
import cn.zyj.springframework.core.io.DefaultResourceLoader;
import cn.zyj.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Properties;

public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {

    /**
     * Default placeholder prefix: {@value}
     */
    public static final String DEFAULT_PLACEHODER_PREFIX = "${";

    /**
     * Default placeholder suffix: {@value}
     */
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    private String location;

    public void setLocation(String location){
        this.location = location;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(location);
            Properties properties = new Properties();
            properties.load(resource.getInputStream());

            String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
            for (String beanName : beanDefinitionNames) {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);

                PropertyValues propertyValues = beanDefinition.getPropertyValues();
                for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                    Object value = propertyValue.getValue();
                    if(!(value instanceof String))continue;
                    String strVal = (String) value;
                    StringBuffer buffer = new StringBuffer(strVal);
                    int startIdx = strVal.indexOf(DEFAULT_PLACEHODER_PREFIX);
                    int stopIdx = strVal.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);
                    if(startIdx!=-1 && stopIdx!=-1 && startIdx<stopIdx){
                        String propKey = strVal.substring(startIdx+2,stopIdx);
                        String propVal = properties.getProperty(propKey);
                        buffer.replace(startIdx,stopIdx+1,propVal);
                        propertyValues.addPropertyValue(new PropertyValue(propertyValue.getName(), buffer.toString()));
                    }
                }

            }

        }catch (IOException e){
            throw new BeansException("Could not load properties", e);
        }
    }
}
