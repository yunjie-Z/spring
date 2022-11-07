package cn.zyj.springframework.aop.framework.autoproxy;

import cn.zyj.springframework.aop.*;
import cn.zyj.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import cn.zyj.springframework.aop.framework.ProxyFactory;
import cn.zyj.springframework.beans.BeansException;
import cn.zyj.springframework.beans.PropertyValues;
import cn.zyj.springframework.beans.factory.BeanFactory;
import cn.zyj.springframework.beans.factory.BeanFactoryAware;
import cn.zyj.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import cn.zyj.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.Collection;

public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }


    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if(isInfrastructureClass(beanClass))return null;

        Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();
        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            ClassFilter classFilter =advisor.getPointcut().getClassFilter();
            if(!classFilter.matches(beanClass))continue;

            AdvisedSupport advisedSupport =new AdvisedSupport();
            TargetSource targetSource = null;
            try{
                targetSource = new TargetSource(beanClass.getDeclaredConstructor().newInstance());
            }catch (Exception e){
                e.printStackTrace();
            }
            advisedSupport.setTargetSource(targetSource);
            advisedSupport.setMethodInterceptor((MethodInterceptor)advisor.getAdvice());
            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            advisedSupport.setProxyTargetClass(false);
            return new ProxyFactory(advisedSupport).getProxy();
        }
        return null;
    }
    private boolean isInfrastructureClass(Class<?> beanClass) {
        return Advice.class.isAssignableFrom(beanClass) || Pointcut.class.isAssignableFrom(beanClass) || Advisor.class.isAssignableFrom(beanClass);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }


    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return pvs;
    }
}
