package cn.zyj.springframework.aop.framework;

import cn.zyj.springframework.aop.AdvisedSupport;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * CGLIB2-based {@link AopProxy} implementation for the Spring AOP framework.
 *
 * <p><i>Requires CGLIB 2.1+ on the classpath.</i>.
 * As of Spring 2.0, earlier CGLIB versions are not supported anymore.
 *
 * <p>Objects of this type should be obtained through proxy factories,
 * configured by an AdvisedSupport object. This class is internal
 * to Spring's AOP framework and need not be used directly by client code.
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    private final AdvisedSupport advised;


    public JdkDynamicAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),advised.getTargetSource().getTargetClass(),this );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(advised.getMethodMatcher().matches(method,advised.getTargetSource().getTarget().getClass())){
            MethodInterceptor methodInterceptor = advised.getMethodInterceptor();
            return methodInterceptor.invoke(new ReflectiveMethodInvocation(advised.getTargetSource().getTarget(),method,args));
        }
        return method.invoke(advised.getTargetSource().getTarget(),args);
    }
}
