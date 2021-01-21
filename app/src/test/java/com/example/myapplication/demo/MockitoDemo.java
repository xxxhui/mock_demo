package com.example.myapplication.demo;

import com.example.myapplication.demo.bean.Dog;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class MockitoDemo implements IDemo {

    @Override
    @Test
    public void testMockObject() {
        Dog dog = Mockito.mock(Dog.class);
        String name = dog.getName();
        dog.eat();//不打印任何信息
        boolean run = dog.run(10);
        Assert.assertNull(name);
        Assert.assertFalse(run);
    }

    @Override
    @Test
    public void testStubMethodBody() {
        Dog dog = Mockito.mock(Dog.class);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                System.out.println("Mock eat");
                return false;
            }
        }).when(dog).eat();
        dog.eat();
    }

    @Override
    @Test
    public void testStubMethodReturn() {
        Dog dog = Mockito.mock(Dog.class);
        Mockito.doAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "Mock getName";
            }
        }).when(dog).getName();
        Assert.assertEquals("Mock getName", dog.getName());
    }

    @Override
    @Test
    public void testStubMethodReturnWithParam() {
        Dog dog = Mockito.mock(Dog.class);
        Mockito.doAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                int param = invocation.getArgument(0);
                System.out.println("Mock run param: " + param);
                if (param == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        }).when(dog).run(Mockito.anyInt());
        Assert.assertFalse(dog.run(2));
        Assert.assertTrue(dog.run(1));
    }

    @Override
    @Test
    public void testMockObjectMethodSome() {
        Dog dog = Mockito.spy(Dog.class);
        Mockito.doAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "Mock getName";
            }
        }).when(dog).getName();
        Assert.assertEquals("Mock getName", dog.getName());//getName被mock
        Assert.assertTrue(dog.run(1));//run方式正常调用
    }

    @Override
    @Test
    public void testCallRealMethodWithParam() {
        Dog dog = Mockito.mock(Dog.class);
        Mockito.doAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                int param = invocation.getArgument(0);
                System.out.println("Mock run param: " + param);
                if (param == 1) {
                    return false;
                } else {
                    return (Boolean) invocation.callRealMethod();
                }
            }
        }).when(dog).run(Mockito.anyInt());
        Assert.assertFalse(dog.run(1));
        Assert.assertTrue(dog.run(2));
    }

    @Override
    @Test
    public void testMockClassAllInstance() {
        //不支持
//        Mockito.mockConstruction(Dog.class);
//        Dog dog = new Dog();
//        Assert.assertNull(dog.getName());
    }

    @Override
    public void testMockClassAllInstanceMethodSome() {
        //不支持
    }

    @Override
    public void testStubClassAllInstanceReturnWithParam() {
        //不支持
    }

    @Override
    public void testCallClassAllInstanceRealMethodWithParam() {
        //不支持
    }

    @Override
    @Test
    public void testCallMockedObjectRealMethod() {
        Dog dog = Mockito.mock(Dog.class);
        Assert.assertNull(dog.getName());
        Mockito.when(dog.getName()).thenCallRealMethod();
        Assert.assertNull("Dog", dog.getName());
    }

    @Override
    public void testStubInaccessibleMethodWithObject() {
        //不支持
    }

    @Override
    public void testStubInaccessibleMethodWithClass() {
        //不支持
    }

    @Override
    public void testMockInnerNewObject() {
        //不支持
    }

    @Override
    public void testMockStaticMethod() {
        //不支持
    }

    @Override
    @Test
    public void testMockNativeMethod() {
        //静态方法不能被mock
        Dog dog = Mockito.mock(Dog.class);
        Mockito.when(dog.getNativeType()).thenReturn(1000);
        Assert.assertEquals(1000, dog.getNativeType());
    }

    @Override
    @Test
    public void testMockFinalMethod() {
        //不支持
//        FinalDog dog = Mockito.mock(FinalDog.class);
//        Mockito.when(dog.getFinalType()).thenReturn(1000);
//        Assert.assertEquals(1000, dog.getFinalType());
    }

    @Override
    public void testMockStaticBlock() {
        //不支持
    }

    @Override
    public void testMockConstructorAndBlock() {
        //不支持
    }

    @Override
    public void testSuppressFatherClassMethod() {
        //不支持
    }

    @Override
    public void testMockFatherAndAllChildren() {
        //不支持
    }

    @Override
    @Test
    public void testReset() {
        Dog dog = Mockito.spy(Dog.class);
        Mockito.when(dog.run(Mockito.anyInt())).thenReturn(true);
        Assert.assertTrue(dog.run(1));
        Mockito.reset(dog);
        Assert.assertFalse(dog.run(1));

    }
}
