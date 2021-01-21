package com.example.myapplication.demo;

import com.example.myapplication.demo.bean.Animal;
import com.example.myapplication.demo.bean.Cat;
import com.example.myapplication.demo.bean.Dog;
import com.example.myapplication.demo.bean.FinalDog;
import com.example.myapplication.demo.bean.TomDog;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Method;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PowerMockDemo.class, Dog.class, FinalDog.class})
@SuppressStaticInitializationFor({"com.segway.robot.mock.demo.bean.TomDog"})
public class PowerMockDemo extends MockitoDemo{
    @Override
    @Test
    public void testMockClassAllInstance() {
        //创建对象的外部类需要添加到@PrepareForTes
        try {
            TomDog mockDog = Mockito.mock(TomDog.class);
            PowerMockito.whenNew(TomDog.class).withNoArguments().thenReturn(mockDog);

            TomDog tomDog = new TomDog();
            Assert.assertEquals(tomDog, mockDog);
            TomDog tomDog2 = new TomDog();
            Assert.assertEquals(tomDog2, mockDog);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    @Test
    public void testMockClassAllInstanceMethodSome() {
        //创建对象的外部类需要添加到@PrepareForTes
        try {
            TomDog spyDog = Mockito.spy(TomDog.class);
            Mockito.when(spyDog.getName()).thenReturn("mock name");
            PowerMockito.whenNew(TomDog.class).withNoArguments().thenReturn(spyDog);

            TomDog tomDog = new TomDog();
            Assert.assertEquals("mock name", tomDog.getName());
            tomDog.eat();//eat方法并没有被mock
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Test
    public void testStubClassAllInstanceReturnWithParam() {
        //创建对象的外部类需要添加到@PrepareForTes
        try {
            TomDog mockDog = Mockito.mock(TomDog.class);
            Mockito.doAnswer(new Answer<Boolean>() {
                @Override
                public Boolean answer(InvocationOnMock invocation) throws Throwable {
                    int step = invocation.getArgument(0);
                    if(step == 1) {
                        return true;
                    }else {
                        return false;
                    }
                }
            }).when(mockDog).run(Mockito.anyInt());
            PowerMockito.whenNew(TomDog.class).withNoArguments().thenReturn(mockDog);

            TomDog tomDog = new TomDog();
            Assert.assertTrue(tomDog.run(1));
            Assert.assertFalse(tomDog.run(2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Test
    public void testCallClassAllInstanceRealMethodWithParam() {
        //创建对象的外部类需要添加到@PrepareForTes
        try {
            TomDog mockDog = Mockito.mock(TomDog.class);
            Mockito.doAnswer(new Answer<Boolean>() {
                @Override
                public Boolean answer(InvocationOnMock invocation) throws Throwable {
                    int step = invocation.getArgument(0);
                    if(step == 1) {
                        return false;
                    }else {
                        return (Boolean) invocation.callRealMethod();
                    }
                }
            }).when(mockDog).run(Mockito.anyInt());
            PowerMockito.whenNew(TomDog.class).withNoArguments().thenReturn(mockDog);

            TomDog tomDog = new TomDog();
            Assert.assertTrue(tomDog.run(1000));
            Assert.assertFalse(tomDog.run(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Test
    public void testStubInaccessibleMethodWithObject() {
        Dog mockDog = PowerMockito.spy(new Dog());
        try {
            PowerMockito.doAnswer(new Answer<Integer>() {
                @Override
                public Integer answer(InvocationOnMock invocation) throws Throwable {
                    return 1000;
                }
            }).when(mockDog, "getPrivateType");
            PowerMockito.doAnswer(new Answer<Integer>() {
                @Override
                public Integer answer(InvocationOnMock invocation) throws Throwable {
                    return 1000;
                }
            }).when(mockDog, "getProtectedType");
            PowerMockito.doAnswer(new Answer<Integer>() {
                @Override
                public Integer answer(InvocationOnMock invocation) throws Throwable {
                    return 1000;
                }
            }).when(mockDog, "getPackageType");
            Assert.assertEquals(1000, mockDog.getType(1));
            Assert.assertEquals(1000, mockDog.getType(2));
            Assert.assertEquals(1000, mockDog.getType(3));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Dog dog = new Dog();
        Assert.assertEquals(0, dog.getType(1));
        Assert.assertEquals(0, dog.getType(2));
        Assert.assertEquals(0, dog.getType(3));
    }

    @Override
    @Test
    public void testStubInaccessibleMethodWithClass() {
        //创建对象的外部类需要添加到@PrepareForTes
        try {
            Dog spy = PowerMockito.spy(new Dog());
            PowerMockito.whenNew(Dog.class).withNoArguments().thenReturn(spy);
            Dog dog = new Dog();
            PowerMockito.doAnswer(new Answer<Integer>() {
                @Override
                public Integer answer(InvocationOnMock invocation) throws Throwable {
                    return 1000;
                }
            }).when(dog, "getPrivateType");
            PowerMockito.doAnswer(new Answer<Integer>() {
                @Override
                public Integer answer(InvocationOnMock invocation) throws Throwable {
                    return 1000;
                }
            }).when(dog, "getProtectedType");
            PowerMockito.doAnswer(new Answer<Integer>() {
                @Override
                public Integer answer(InvocationOnMock invocation) throws Throwable {
                    return 1000;
                }
            }).when(dog, "getPackageType");
            Assert.assertEquals(1000, dog.getType(1));
            Assert.assertEquals(1000, dog.getType(2));
            Assert.assertEquals(1000, dog.getType(3));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    @Test
    public void testMockInnerNewObject() {
        try {
            PowerMockito.whenNew(Cat.class).withNoArguments().thenReturn(Mockito.mock(Cat.class));
            Dog dog = new Dog();
            Cat cat = dog.getCat();
            Assert.assertNull(cat.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Test
    public void testMockStaticMethod() {
        PowerMockito.mockStatic(Dog.class);
        try {
            PowerMockito.doAnswer(new Answer<Integer>() {
                @Override
                public Integer answer(InvocationOnMock invocation) throws Throwable {
                    return 1000;
                }
            }).when(Dog.class, "getStaticType");
            PowerMockito.doAnswer(new Answer<Integer>() {
                @Override
                public Integer answer(InvocationOnMock invocation) throws Throwable {
                    return 1000;
                }
            }).when(Dog.class, "getPrivateStaticType");
            PowerMockito.doAnswer(new Answer<Integer>() {
                @Override
                public Integer answer(InvocationOnMock invocation) throws Throwable {
                    return 1000;
                }
            }).when(Dog.class, "getStaticNativeType");
            Assert.assertEquals(1000, Dog.getStaticType());
            Assert.assertEquals(1000, Dog.getStaticNativeType());
            Assert.assertEquals(1000, new Dog().getType(4));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Test
    public void testMockFinalMethod() {
        //需要添加类到@PrepareForTest
        FinalDog dog = PowerMockito.mock(FinalDog.class);
        PowerMockito.when(dog.getFinalType()).thenReturn(1000);
        Assert.assertEquals(1000, dog.getFinalType());
    }

    @Override
    @Test
    public void testMockStaticBlock() {
        //添加类名到@SuppressStaticInitializationFor
        Assert.assertEquals(0, TomDog.staticInit);
    }

    @Override
    @Test
    public void testMockConstructorAndBlock() {
        TomDog mock = Mockito.mock(TomDog.class);
        try {
            PowerMockito.whenNew(TomDog.class).withNoArguments().thenReturn(mock);
            PowerMockito.whenNew(TomDog.class).withArguments(Mockito.anyString()).thenReturn(mock);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TomDog tomDog = new TomDog();
        Assert.assertEquals(0, tomDog.init);
    }

    @Override
    @Test
    public void testSuppressFatherClassMethod() {
        Method eat = PowerMockito.method(Animal.class, "eat");
        PowerMockito.suppress(eat);
        Dog dog = new Dog();
        dog.eat();//不会输出"Animal eat"
    }

    @Override
    @Test
    public void testMockFatherAndAllChildren() {
        //不支持 所有用PowerMock Mock内部类就比较困难
    }

    @Override
    @Test
    public void testReset() {
        //没有提供重置方法，如果需要，可以通过再次mock实现
    }
}
