package cn.zyj.springframework.beans.factory.config;

/**
 * Bean的引用
 */
public class BeanReference {

    private final String beanName;

    public BeanReference(String beanName){
        this.beanName = beanName;
    }

    public String getBeanName(){
        return beanName;
    }
}
