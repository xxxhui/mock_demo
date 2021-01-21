package com.example.myapplication.demo;

public interface IDemo {
    /**
     * Mock实例所有方法
     */
    void testMockObject();

    /**
     * 修改Mock对象的方法实现
     */
    void testStubMethodBody();

    /**
     * 修改Mock对象的方法返回值
     */
    void testStubMethodReturn();

    /**
     * 根据Mock对象的方法参数返回Mock结果
     */
    void testStubMethodReturnWithParam();

    /**
     * Mock实例中部分方法
     */
    void testMockObjectMethodSome();

    /**
     * 根据Mock对象的方法参数选择走Mock实现还是调用真实对象的实现
     */
    void testCallRealMethodWithParam();

    /**
     * Mock一个类的所有实例及修改Mock对象的方法体
     */
    void testMockClassAllInstance();

    /**
     * Mock一个类的所有实例的部分方法
     */
    void testMockClassAllInstanceMethodSome();

    /**
     * Mock一个类的所有实例，被mock的方法根据不同参数返回不同结果
     */
    void testStubClassAllInstanceReturnWithParam();

    /**
     * Mock一个类的所有实例，被mock的方法根据不同参数选择调用mock方法还是真实实现
     */
    void testCallClassAllInstanceRealMethodWithParam();

    /**
     * 通过mocked的对象调用真实对象的实现
     */
    void testCallMockedObjectRealMethod();

    /**
     * 访问或者修改一个对象的不可访问修饰符修饰的方法(private)
     */
    void testStubInaccessibleMethodWithObject();

    /**
     * 针对类的所有对象，访问或者修改不可访问修饰符的方法
     */
    void testStubInaccessibleMethodWithClass();

    /**
     * Mock方法体内创建的对象
     */
    void testMockInnerNewObject();

    /**
     * Mock静态方法
     */
    void testMockStaticMethod();

    /**
     * Mock Native方法
     */
    void testMockNativeMethod();

    /**
     * Mock Final类final方法
     */
    void testMockFinalMethod();

    /**
     * Mock静态代码块
     */
    void testMockStaticBlock();

    /**
     * Mock构造函数及构造代码块
     */
    void testMockConstructorAndBlock();

    /**
     * 抑制方法中super的实现
     */
    void testSuppressFatherClassMethod();

    /**
     * MocK 当前类及其子类
     */
    void testMockFatherAndAllChildren();

    /**
     * 重置Mock对象
     */
    void testReset();

}
