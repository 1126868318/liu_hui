package com.lh.study.spring;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: liu_hui
 * @description: spring得概念
 * @author: Mr.Lhui
 * @create: 2020-07-23 17:41
 **/
public class StudySpring {

    /**
     * 一，spring bean 的生命周期
 *         1）实例化
 *         2）属性赋值
 *         3）初始化
 *         4）销毁
     *
     * 二，spring bean 的作用域有哪些
     *      1）singleton： 在Spring Ioc容器中仅存在一个Bean实例，Bean以单例方式存在，默认值
     *      2）prototype： 每次从容器中调用Bean时，都返回一个新的实例，即每次调用getBean()时，相当于执行new XxxBean()
     *      3)request：    每次HttP请求都会创建一个新的Bean，该作用域仅适用于WebApplicationContext环境
     *      4）session：    同一个http session共享一个bean， 不同session使用不同的bean ，仅适用于webapplicationcontext环境
     *      5）globalsession：一般用于portlet应用环境，该作用域仅适用于webapplicationcontext环境
     *
     * 三、什么是IOC
     *      控制反转/反转控制。它是一种思想不是一个技术实现，描述的是：java开发领域对象的创建以及管理的问题
     *      1、为什么叫控制反转？
     *          控制：指的是对象创建（实例化、管理）的权力
     *          反转：控制权交给外部环境（spring框架、ioc容器）
     *      2、ioc解决了什么问题？
     *          ioc的思想就是两方之间不互相依赖，由第三方容器来管理相关资源。这样有什么好处呢？
     *              1、对象之间的耦合度或者说依赖程度降低；
     *              2、资源变的容易管理；
     *      3、什么是依赖注入？
     *          di :dependecy inject 依赖注入是实现控制反转的一种设计模式，依赖注入就是将实例变量传入到一个对象中去。
     *
     * 四、什么是aop
     *      aspect oriented programming 面向切面编程，aop是oop（面向对象编程）的一种延续。
     *      1、aop解决了什么问题？
     *          在不改变原有业务逻辑的情况下，增强横切逻辑代码，根本上解耦合，避免横切逻辑代码重复。
     *      2、aop为什么叫面向切面编程？
     *          切：指的是横切逻辑，原有业务逻辑代码不动，只能操作横切逻辑代码，所以面向横切逻辑
     *          面：横切逻辑代码往往要影响的是很多个方法，每个方法如同一个点，多个点构成一个面。这里有一个面的概念。
     *五、spring中的设计模式有哪些？
     *      1、控制反转（IOC）和依赖注入（DI）
     *      2、工厂设计模式
     *          Spring使用工厂模式通过beanfactory、applicationcontext创建bean对象
     *      3、单例设计模式
     *          Spring AOP功能的实现
     *      4、代理设计模式
     *      5、模板方法模式
     *      6、观察者模式
     *      7、适配器模式
     *      8、装饰者模式
     *
     *
     *
     *
     *
     */


    /**
     * spring事务得隔离级别和传播行为
     */
    @Transactional(rollbackFor = Exception.class //加上这个注解可以让事务在遇到非运行时异常也回滚
            //事务得传播行为：
            , propagation = Propagation.REQUIRED  //默认传播行为，如果有事务那么加入此事务，没有就新建一个事务
//    ,propagation = Propagation.SUPPORTS //如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行
//      ,propagation = Propagation.MANDATORY    // 如果当前存在事务，则加入该事务；如果当前没有事务，则抛出异常。
//       ,propagation = Propagation.REQUIRES_NEW //创建一个新的事务，如果当前存在事务，则把当前事务挂起。
//            ,propagation = Propagation.NOT_SUPPORTED // 以非事务方式运行，如果当前存在事务，则把当前事务挂起。
//            ,propagation = Propagation.NEVER // 以非事务方式运行，如果当前存在事务，则抛出异常。
//            ,propagation = Propagation.NESTED //如果当前存在事务，则创建一个事务作为当前事务的嵌套事务来运行；如果当前没有事务，则该取值等价于TransactionDefinition.PROPAGATION_REQUIRED。


            //事务得隔离级别
            , isolation = Isolation.DEFAULT //使用数据库默认得隔离级别      可重复读 类似 REPEATABLE_READ
//      ,isolation = Isolation.READ_UNCOMMITTED //读未提交
//        ,isolation = Isolation.READ_COMMITTED//读已提交
//          ,isolation = Isolation.SERIALIZABLE//串行化
    )
    public void testTransactional() {
    }


}

