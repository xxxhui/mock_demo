package com.example.myapplication.demo;

import com.example.myapplication.demo.bean.Dog;
import com.example.myapplication.demo.bean.TomCat;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class MockitoDemo implements IDemo, IVerify{

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

    @Test
    public void testInnerClassInstance() {
        TomCat tomCat = new TomCat();
        Assert.assertEquals("Tom cat", tomCat.getLifeName());
        TomCat spyTomCat = Mockito.spy(tomCat);
        Assert.assertEquals("Tom cat", spyTomCat.getLifeName());
        Mockito.when(spyTomCat.getName()).thenReturn("Mock cat");
        Assert.assertEquals("Tom cat", spyTomCat.getLifeName());//并不是"Mock cat"
    }

    @Override
    @Test
    public void verifyInvoke() {
        Dog dog = Mockito.mock(Dog.class);
        Mockito.when(dog.run(Mockito.anyInt())).thenReturn(false);
        dog.run(1);
        Mockito.verify(dog).run(1);
    }

    @Override
    @Test
    public void verifyInvokeWithParamMatcher() {
        Dog dog = Mockito.mock(Dog.class);
        Mockito.when(dog.run(Mockito.anyInt())).thenReturn(false);
        dog.run(1);
        Mockito.verify(dog).run(Mockito.anyInt());
    }

    @Override
    @Test
    public void verifyInvokeByOrder() {
        Dog dog = Mockito.spy(Dog.class);
        dog.eat();
        dog.getName();
        InOrder inOrder = Mockito.inOrder(dog);
        inOrder.verify(dog).eat();
        inOrder.verify(dog).getName();
    }

    @Override
    @Test
    public void verifyInvokeParams() {
        Dog dog = Mockito.mock(Dog.class);
        Mockito.when(dog.run(1)).thenReturn(false);
        Assert.assertFalse(dog.run(1));
        ArgumentCaptor<Integer> acInt = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(dog).run(acInt.capture());
        Assert.assertEquals(1, (int)acInt.getValue());
    }

    @Override
    @Test
    public void verifyDelay() {
        final Dog dog = Mockito.mock(Dog.class);
        long s = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                dog.getName();
            }
        }).start();
        Mockito.verify(dog, Mockito.after(1000)).getName();
        long e = System.currentTimeMillis();
        System.out.println("verify getName delay: "+(e-s));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                dog.eat();
            }
        }).start();
        Mockito.verify(dog, Mockito.timeout(1000)).eat();
        long e2 = System.currentTimeMillis();
        System.out.println("verify eat delay: "+(e2-e));
    }

    @Override
    @Test
    public void verifyNotInvoke() {
        Dog dog = Mockito.mock(Dog.class);
        Mockito.verify(dog, Mockito.never()).eat();
    }

    @Override
    @Test
    public void verifyInvokeWithTimes() {
        Dog dog = Mockito.mock(Dog.class);
        dog.eat();
        dog.eat();
        dog.eat();
        Mockito.verify(dog, Mockito.times(3)).eat();
    }
}
