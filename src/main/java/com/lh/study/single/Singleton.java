package com.lh.study.single;

/**
 * 双重校验锁实现对象单例（线程安全）
 */
public class Singleton {

    private volatile static Singleton singleton = null;
    private Singleton(){

    }
    public  static Singleton getSingleton(){
//        先判断对象是否已经实例过，没有实例才进入加锁代码
        if (singleton == null) {
            synchronized (Singleton.class) {
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }




}
