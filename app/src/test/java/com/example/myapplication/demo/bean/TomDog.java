package com.example.myapplication.demo.bean;

public class TomDog extends Dog{
    public int init;
    public static int staticInit;

    {
        init = 1;
        System.out.println("real 构造代码块");
    }

    static {
        staticInit = 1;
        System.out.println("real 静态代码块");
    }

    public TomDog() {
        System.out.println("real 无参构造方法");
    }

    public TomDog(String name) {
        this.mName = name;
        System.out.println("real 有参构造方法 " + name);
    }
}
