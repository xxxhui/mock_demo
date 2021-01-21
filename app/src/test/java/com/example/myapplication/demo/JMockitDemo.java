package com.example.myapplication.demo;


import com.example.myapplication.demo.bean.Animal;
import com.example.myapplication.demo.bean.Cat;
import com.example.myapplication.demo.bean.Dog;
import com.example.myapplication.demo.bean.FinalDog;
import com.example.myapplication.demo.bean.ILife;
import com.example.myapplication.demo.bean.TomDog;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import mockit.Capturing;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import mockit.Verifications;
import mockit.VerificationsInOrder;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class JMockitDemo implements IDemo, IVerify {

    @Injectable
    Dog mInjectableDog;

    @Override
    @Test
    public void testMockObject() {
        //方式一 利用注解@Injectable
        mInjectableDog.eat();//对象被mock，调用无效，不会输出任何信息
        Assert.assertNull(mInjectableDog.getName());
        Assert.assertFalse(mInjectableDog.run(10));
        //方式二 利用Expectations对象
        final Dog dog = new Dog();
        new Expectations(dog) {
            {
                dog.eat();
                dog.run(anyInt);
                dog.getName();
            }
        };
        dog.eat();//对象被mock，调用无效，不会输出任何信息
        Assert.assertNull(dog.getName());
        Assert.assertFalse(dog.run(10));
        System.out.println("------------分割线-----------------");
        Dog newDog = new Dog();//新创建的对象不受影响
        newDog.eat();//会被执行
        Assert.assertEquals(newDog.getName(), "Dog");
        Assert.assertTrue(newDog.run(10));
    }

    @Override
    @Test
    public void testStubMethodBody() {
        final Dog dog = new Dog();
        new Expectations(dog) {
            {
                dog.eat();
                result = new Delegate<Dog>() {
                    void eat() {
                        System.out.println("Mock dog eat");
                    }
                };
            }
        };
        dog.eat();
    }

    @Override
    @Test
    public void testStubMethodReturn() {
        final Dog dog = new Dog();
        new Expectations(dog) {
            {
                dog.getName();
                result = "mock dog";
            }
        };
        String name = dog.getName();
        Assert.assertEquals("mock dog", name);
    }

    @Override
    @Test
    public void testStubMethodReturnWithParam() {
        final Dog dog = new Dog();
        new Expectations(dog) {
            {
                dog.run(anyInt);
                result = new Delegate<Dog>() {
                    boolean run(int step) {
                        if (step == 1) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                };
            }
        };
        Assert.assertTrue(dog.run(1));
        Assert.assertFalse(dog.run(2));
        Assert.assertFalse(dog.run(1000));
    }

    @Override
    @Test
    public void testMockObjectMethodSome() {
        final Dog dog = new Dog();
        new Expectations(dog) {
            {
                dog.getName();
                result = "mock dog";
            }
        };
        String name = dog.getName();//getName被mock，其他方法并没有被mock
        boolean run = dog.run(1);
        Assert.assertEquals("mock dog", name);
        Assert.assertTrue(run);
    }

    @Override
    @Test
    public void testCallRealMethodWithParam() {
        final Dog dog = new Dog();
        new Expectations(dog) {
            {
                dog.run(anyInt);
                result = new Delegate<Dog>() {
                    boolean run(Invocation invocation, int step) {
                        if (step == 1) {
                            System.out.println("step=1, mocking");
                            return false;
                        } else {
                            return invocation.proceed(step);//走对象的真实实现
                        }
                    }
                };
            }
        };
        boolean run = dog.run(1);//被mock
        Assert.assertFalse(run);
        boolean run1 = dog.run(100);//走真实实现
        Assert.assertTrue(run1);
    }

//    @Mocked
//    JMockitMockedDog mMockedDog;//JMockitMockedDog类的所有实例都被mock 当前版本测试中发现会影响父类Dog，Dog的所有实例也被mock

    @Override
    @Test
    public void testMockClassAllInstance() {
        //方式一 利用@Mocked
//        JMockitMockedDog dog = new JMockitMockedDog();
//        Assert.assertNull(dog.getName());
//        JMockitMockedDog dog1 = new JMockitMockedDog();
//        Assert.assertNull(dog1.getName());
        //方式二 利用MockUp
        new MockUp<Dog>() {
            @Mock
            public boolean run(int step) {
                System.out.println("Mock Dog run: " + step);
                return false;
            }

            @Mock
            public String getName() {
                return "Mock Dog";
            }

            @Mock
            public void eat() {
                System.out.println("Mock Dog eat");
            }

            @Mock
            public String getInnerCatName() {
                Cat cat = new Cat();
                System.out.println("Mock Dog getInnerCatName");
                return cat.getName();
            }
        };
        Dog dog2 = new Dog();
        Assert.assertEquals("Mock Dog", dog2.getName());
        Assert.assertFalse(dog2.run(1));
        dog2.eat();
        System.out.println(dog2.getInnerCatName());
        Dog dog3 = new Dog();
        Assert.assertEquals("Mock Dog", dog3.getName());
        Assert.assertFalse(dog3.run(1));
        dog3.eat();
        System.out.println(dog3.getInnerCatName());
    }

    @Override
    @Test
    public void testMockClassAllInstanceMethodSome() {
        new MockUp<Dog>() {
            @Mock
            public String getName() {
                return "Mock Dog";
            }
        };
        Dog dog = new Dog();
        Assert.assertEquals("Mock Dog", dog.getName());//getName被mock
        Assert.assertTrue(dog.run(1));//其他方法没有被mock
        dog.eat();
    }

    @Override
    @Test
    public void testStubClassAllInstanceReturnWithParam() {
        new MockUp<Dog>() {
            @Mock
            public boolean run(int step) {
                if (step == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        Dog dog = new Dog();
        Assert.assertTrue(dog.run(1));
        Assert.assertFalse(dog.run(2));
    }

    @Override
    @Test
    public void testCallClassAllInstanceRealMethodWithParam() {
        new MockUp<Dog>() {
            @Mock
            public boolean run(Invocation invocation, int step) {
                if (step == 1) {
                    return false;
                } else {
                    return invocation.proceed(step);
                }
            }
        };
        Dog dog = new Dog();
        Assert.assertFalse(dog.run(1));//被mock
        Assert.assertTrue(dog.run(2));//调用真实实现
    }

    @Override
    @Test
    public void testCallMockedObjectRealMethod() {
        //不支持
    }

    @Override
    @Test
    public void testStubInaccessibleMethodWithObject() {
        //没有直接的支持，类似需求写起来比较麻烦
        final Dog dog = new Dog();
        new MockUp<Dog>() {
            @Mock
            private int getPrivateType(Invocation invocation) {
                if (invocation.getInvokedInstance().equals(dog)) {
                    return 1000;
                } else {
                    return invocation.proceed();
                }
            }

            @Mock
            protected int getProtectedType(Invocation invocation) {
                if (invocation.getInvokedInstance().equals(dog)) {
                    return 1000;
                } else {
                    return invocation.proceed();
                }
            }

            @Mock
            int getPackageType(Invocation invocation) {
                if (invocation.getInvokedInstance().equals(dog)) {
                    return 1000;
                } else {
                    return invocation.proceed();
                }
            }
        };
        Assert.assertEquals(1000, dog.getType(1));
        Assert.assertEquals(1000, dog.getType(2));
        Assert.assertEquals(1000, dog.getType(3));
        Dog dog2 = new Dog();
        Assert.assertEquals(0, dog2.getType(1));
        Assert.assertEquals(0, dog2.getType(2));
        Assert.assertEquals(0, dog2.getType(3));
    }

    @Override
    @Test
    public void testStubInaccessibleMethodWithClass() {
        new MockUp<Dog>() {
            @Mock
            private int getPrivateType() {
                return 1000;
            }

            @Mock
            protected int getProtectedType() {
                return 1000;
            }

            @Mock
            int getPackageType() {
                return 1000;
            }
        };
        Dog dog = new Dog();
        Assert.assertEquals(1000, dog.getType(1));
        Assert.assertEquals(1000, dog.getType(2));
        Assert.assertEquals(1000, dog.getType(3));
        Dog dog2 = new Dog();
        Assert.assertEquals(1000, dog2.getType(1));
        Assert.assertEquals(1000, dog2.getType(2));
        Assert.assertEquals(1000, dog2.getType(3));
    }

    @Override
    @Test
    public void testMockInnerNewObject() {
        //不能直接mock方法内创建的对象
        new MockUp<Cat>() {
            @Mock
            public String getName() {
                return "Mock cat";
            }
        };
        Dog dog = new Dog();
        Assert.assertEquals("Mock cat", dog.getInnerCatName());
    }

    @Override
    @Test
    public void testMockStaticMethod() {
        new MockUp<Dog>() {
            @Mock
            public int getStaticType() {
                return 1000;
            }

            @Mock
            private int getPrivateStaticType() {
                return 1000;
            }
        };
        Dog dog = new Dog();
        Assert.assertEquals(1000, Dog.getStaticType());
        Assert.assertEquals(1000, dog.getType(4));
    }

    @Override
    @Test
    public void testMockNativeMethod() {
        new MockUp<Dog>() {
            @Mock
            public int getStaticNativeType() {
                return 1000;
            }

            @Mock
            public int getNativeType() {
                return 1000;
            }
        };
        Dog dog = new Dog();
        Assert.assertEquals(1000, Dog.getStaticNativeType());
        Assert.assertEquals(1000, dog.getNativeType());
    }

    @Override
    @Test
    public void testMockFinalMethod() {
        new MockUp<FinalDog>() {
            @Mock
            public int getFinalType() {
                return 1000;
            }
        };
        FinalDog finalDog2 = new FinalDog();
        Assert.assertEquals(1000, finalDog2.getNativeType());
    }

    @Override
    @Test
    public void testMockStaticBlock() {
        //mock静态代码块时需要在目标类被加载前mock
        new MockUp<TomDog>() {
            @Mock
            public void $clinit() {//mock静态代码块
                System.out.println("mock static init");
            }
        };
        Assert.assertEquals(TomDog.staticInit, 0);
    }

    @Override
    @Test
    public void testMockConstructorAndBlock() {
        new MockUp<TomDog>() {
            @Mock
            public void $init() {//mock无参构造函数
                System.out.println("mock init");
            }

            @Mock
            public void $init(String name) {//mock有参构造函数
                System.out.println("mock init name: " + name);
            }

        };
        TomDog dog = new TomDog();
        Assert.assertEquals(TomDog.staticInit, 1);
        Assert.assertEquals(dog.init, 0);
    }

    @Override
    @Test
    public void testSuppressFatherClassMethod() {
        new MockUp<Animal>() {
            @Mock
            public void eat() {

            }
        };
        Dog dog = new Dog();
        dog.eat();
    }

    @Capturing
    ILife mCapturing;

    private void testMockFatherClassAndChild1() {
        //方式一
        //注意此处不能用Dog类，因为前面对Dog进行过@Injectable 有冲突
        new Expectations() {
            {
                mCapturing.getName();
                result = "mock name";
            }
        };
        ILife life = new ILife() {
            @Override
            public String getName() {
                return "life";
            }
        };
        Animal animal = new Animal() {
            @Override
            public boolean run(int step) {
                return true;
            }

            @Override
            public String getName() {
                return "animal";
            }
        };
        Assert.assertEquals("mock name", animal.getName());
        Assert.assertEquals("mock name", life.getName());
        Cat cat = new Cat();
        Assert.assertEquals("mock name", cat.getName());
    }

    private <T extends ILife> void testMockFatherClassAndChild2() {
        //方式二
        //注意此处不能用Dog类，因为前面对Dog进行过@Injectable 有冲突
        new MockUp<T>() {
            @Mock
            public String getName() {
                return "mock name";
            }
        };
        ILife life = new ILife() {
            @Override
            public String getName() {
                return "life";
            }
        };
        Animal animal = new Animal() {
            @Override
            public boolean run(int step) {
                return true;
            }

            @Override
            public String getName() {
                return "animal";
            }
        };
        Assert.assertNull(animal.getName());//对于abstract实现类的内部类无效
        Assert.assertEquals("mock name", life.getName());
        Cat cat = new Cat();
        Assert.assertEquals("mock name", cat.getName());
    }

    @Override
    @Test
    public void testMockFatherAndAllChildren() {
//        testMockFatherClassAndChild1();
        testMockFatherClassAndChild2();
    }

    @Override
    public void testReset() {
        //没有明确的重置Mock对象api

    }

    @Override
    @Test
    public void verifyInvoke() {
        final Dog dog = new Dog();
        new Expectations(dog) {
            {
                dog.run(anyInt);
                result = false;
            }
        };
        Assert.assertFalse(dog.run(1));
//        Assert.assertFalse(dog.run(2));
        new Verifications() {
            {
                dog.run(1);
            }
        };
    }

    @Override
    @Test
    public void verifyInvokeWithParamMatcher() {
        final Dog dog = new Dog();
        new Expectations(dog) {
            {
                dog.run(anyInt);
                result = false;
            }
        };
        Assert.assertFalse(dog.run(1));
//        Assert.assertFalse(dog.run(2));
        new Verifications() {
            {
                dog.run(anyInt);
            }
        };
    }

    @Override
    @Test
    public void verifyInvokeByOrder() {
        final Dog dog = new Dog();
        new Expectations(dog) {
            {
                dog.eat();
                dog.getName();
                result = "mock name";
            }
        };
        dog.eat();
        dog.getName();
        new VerificationsInOrder() {
            {
                dog.eat();
                dog.getName();
            }
        };
    }

    @Override
    @Test
    public void verifyInvokeParams() {
        //没有直接支持
        final Dog dog = new Dog();
        final List<Integer> params = new ArrayList<>();
        new Expectations(dog) {
            {
                dog.run(anyInt);
                result = new Delegate() {
                    public boolean run(int step) {
                        params.add(step);
                        return false;
                    }
                };
            }
        };
        dog.run(1);
        Assert.assertEquals(1, (int)params.get(0));
    }

    @Override
    public void verifyDelay() {
        //不支持
    }

    @Override
    @Test
    public void verifyNotInvoke() {
        final Dog dog = new Dog();
        new Expectations(dog) {
            {
                dog.run(anyInt);
                result = false;
                minTimes = 0;
            }
        };
        new Verifications() {
            {
                dog.run(anyInt);
                times = 0;
            }
        };
    }

    @Override
    @Test
    public void verifyNotInvokeWithTimes() {
        final Dog dog = new Dog();
        new Expectations(dog) {
            {
                dog.eat();
            }
        };
        dog.eat();
        dog.eat();
        dog.eat();
        new Verifications() {
            {
                dog.eat();
                times = 3;
            }
        };
    }
}
