package cn.zyj.springframework.beans.factory.support;

import cn.zyj.springframework.beans.BeansException;
import cn.zyj.springframework.beans.factory.BeanFactory;
import cn.zyj.springframework.beans.factory.FactoryBean;
import cn.zyj.springframework.beans.factory.config.BeanDefinition;
import cn.zyj.springframework.beans.factory.config.BeanPostProcessor;
import cn.zyj.springframework.beans.factory.config.ConfigurableBeanFactory;
import cn.zyj.springframework.util.ClassUtils;
import cn.zyj.springframework.util.StringValueResolver;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport  implements ConfigurableBeanFactory {

    /** BeanPostProcessors to apply in createBean */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();


    /**
     * ClassLoader to resolve bean class names with, if necessary
     */
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    /**
     * String resolvers to apply e.g. to annotation attribute values
     */
    private final List<StringValueResolver> embeddedValueResolvers = new ArrayList<>();


    public ClassLoader getBeanClassLoader() {
        return this.beanClassLoader;
    }


    @Override
    public Object getBean(String name) throws BeansException {
       return doGetBean(name,null);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return doGetBean(name,args);
    }
    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return (T) getBean(name);
    }

    protected <T> T doGetBean(final  String name, final Object[]args){
        Object sharedInstance  = getSingleton(name);
        if(sharedInstance !=null){
            return (T) getObjectForBeanInstance(sharedInstance ,name);
        }
        BeanDefinition beanDefinition = getBeanDefinition(name);
        Object bean = createBean(name,beanDefinition,args);
        return (T) getObjectForBeanInstance(bean,name);
    }


    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition,Object[] args) throws BeansException;


    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor){
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    /**
     * Return the list of BeanPostProcessors that will get applied
     * to beans created with this factory.
     */
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }


    private Object getObjectForBeanInstance(Object beanInstance,String beanName){
        if(!(beanInstance instanceof FactoryBean)){
            return beanInstance;
        }

        Object object = getCachedObjectForFactoryBean(beanName);

        if(object==null){
            FactoryBean<?> factoryBean = (FactoryBean<?>) beanInstance;
            object = getObjectFromFactoryBean(factoryBean,beanName);
        }
        return object;
    }


    @Override
    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
        this.embeddedValueResolvers.add(valueResolver);
    }

    @Override
    public String resolveEmbeddedValue(String value) {
        String result = value;
        for (StringValueResolver resolver : this.embeddedValueResolvers) {
            result = resolver.resolveStringValue(result);
        }
        return result;
    }
}
