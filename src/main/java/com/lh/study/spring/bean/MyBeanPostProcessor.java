package com.lh.study.spring.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @program: liu_hui
 * @description: 自定义实现BeanPostProcessor 的MyBeanPostProcessor：
 * @author: Mr.Lhui
 * @create: 2020-07-24 11:43
 **/
public class MyBeanPostProcessor
implements BeanPostProcessor
{
//容器加载得时候会加载一些其他得bean，会调用初始化前和初始化后方法
//    这次只关注 book（bean）得生命周期


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof Book){
            System.out.println("MyBeanPostProcessor.postProcessBeforeInitialization");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof Book){
            System.out.println("MyBeanPostProcessor.postProcessAfterInitialization");
        }
        return bean;
    }
}

