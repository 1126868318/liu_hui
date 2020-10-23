package com.lh.study.spring.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @program: liu_hui
 * @description: 验证spring Bean 的生命周期类
 * @author: Mr.Lhui
 * @create: 2020-07-24 11:20
 **/
public class Book
implements
        BeanNameAware        //spring bean setBeanName 运行
        , BeanFactoryAware
    , ApplicationContextAware
    , InitializingBean
    , DisposableBean
{
    private  String bookName;
    public Book(){
        System.out.println("Book Initializing ");
    }



    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("Book.setBeanFactory invoke");
    }

    @Override
    public void setBeanName(String s) {
        System.out.println("Book.setBeanName invoke");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("Book.destory invoke");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Book.afterPropertiesSet invoke");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("Book.setApplicationContext invoke");
    }


    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
        System.out.println("setBookName: Book name has set.");
    }


    public  void  myPostconstruct(){
        System.out.println("Book.myPostConstruct invoke");
    }

    //自定义初始化方法
    @PostConstruct
    public  void  springPostConstruct(){
        System.out.println("@PostConstruct");
    }

    public void  myPreDestory(){
        System.out.println("Book.myPreDestory invoke");
        System.out.println("---------------destroy-----------------");
    }

    //自定义销毁方法
    @PreDestroy
    public void springPreDestory() {
        System.out.println("@PreDestory");
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("------inside finalize-----");
    }
}

