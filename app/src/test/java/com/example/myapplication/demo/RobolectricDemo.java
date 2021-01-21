package com.example.myapplication.demo;

import com.example.myapplication.demo.bean.Cat;
import com.example.myapplication.demo.bean.Dog;
import com.example.myapplication.demo.bean.FinalDog;
import com.example.myapplication.demo.bean.ILife;
import com.example.myapplication.demo.bean.TomDog;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.internal.SandboxTestRunner;
import org.robolectric.internal.bytecode.SandboxConfig;
import org.robolectric.shadow.api.Shadow;

@RunWith(SandboxTestRunner.class)
public class RobolectricDemo implements IDemo {

    @Override
    @Test
    public void testMockObject() {
        //没有针对单个实例的mock
    }

    @Override
    public void testStubMethodBody() {
        //没有针对单个实例的mock
    }

    @Override
    public void testStubMethodReturn() {
        //没有针对单个实例的mock
    }

    @Override
    public void testStubMethodReturnWithParam() {
        //没有针对单个实例的mock
    }

    @Override
    public void testMockObjectMethodSome() {
        //没有针对单个实例的mock
    }

    @Override
    public void testCallRealMethodWithParam() {
        //没有针对单个实例的mock
    }

    @Implements(Cat.class)
    public static class ShadowCat{
        @Implementation
        public boolean run(int step) {
            System.out.println("Mock cat run: " + step);
            return false;
        }

        @Implementation
        public String getName() {
            return "Mock Cat";
        }

        @Implementation
        public void eat() {
            System.out.println("Mock Cat eat");
        }
    }

    @Override
    @Test
    @SandboxConfig(shadows = {ShadowCat.class})
    public void testMockClassAllInstance() {
        //Shadow类必须是publish的
        Cat cat = new Cat();
        cat.eat();
        Assert.assertFalse(cat.run(1));
        Assert.assertEquals("Mock Cat", cat.getName());
        Cat cat1 = new Cat();
        cat1.eat();
        Assert.assertFalse(cat1.run(1));
        Assert.assertEquals("Mock Cat", cat1.getName());
    }

    @Override
    public void testMockClassAllInstanceMethodSome() {
        //Shadow类里不重写的方法就没有被mock
    }

    @Implements(Cat.class)
    public static class ShadowCat2 {
        @Implementation
        public boolean run(int step) {
            System.out.println("Mock cat run: " + step);
            if(step == 1) {
                return true;
            }else {
                return false;
            }
        }
    }

    @Override
    @Test
    @SandboxConfig(shadows = {ShadowCat2.class})
    public void testStubClassAllInstanceReturnWithParam() {
        Cat cat = new Cat();
        Assert.assertTrue(cat.run(1));
        Assert.assertFalse(cat.run(2));
    }

    @Implements(Cat.class)
    public static class ShadowCat3 {
        
        @RealObject
        Cat mCat;
        
        @Implementation
        public boolean run(int step) {
            System.out.println("Mock cat run: " + step);
            if(step == 1) {
                return false;
            }else {
                return Shadow.directlyOn(mCat, Cat.class).run(step);
            }
        }
    }

    @Override
    @SandboxConfig(shadows = {ShadowCat3.class})
    @Test
    public void testCallClassAllInstanceRealMethodWithParam() {
        Cat cat = new Cat();
        Assert.assertTrue(cat.run(200));
        Assert.assertFalse(cat.run(1));
    }

    @Override
    @SandboxConfig(shadows = {ShadowCat2.class})
    @Test
    public void testCallMockedObjectRealMethod() {
        Cat cat = new Cat();
        Assert.assertFalse(cat.run(2));//run被mock
        Assert.assertTrue(Shadow.directlyOn(cat, Cat.class).run(2));//调用真实实现
    }

    @Implements(Dog.class)
    public static class ShadowDog {
        @Implementation
        protected int getPrivateType() {
            return 1000;
        }
        @Implementation
        protected int getProtectedType() {
            return 1000;
        }
        @Implementation
        public int getPackageType() {
            return 1000;
        }
        @Implementation
        public static int getStaticType() {
            return 1000;
        }
        @Implementation
        private static int getPrivateStaticType() {
            return 1000;
        }
        @Implementation
        public static int getStaticNativeType() {
            return 1000;
        }
        @Implementation
        public int getNativeType() {
            return 1000;
        }
    }
    @Override
    @SandboxConfig(shadows = {ShadowDog.class})
    @Test
    public void testStubInaccessibleMethodWithObject() {
        //不能针对单个对象进行mock
    }

    @Override
    public void testStubInaccessibleMethodWithClass() {
        //注意私有方法和包访问方法的修饰符在Shadow中需要是publish或者protected才能生效
        Dog dog = new Dog();
        Assert.assertEquals(1000, dog.getType(1));
        Assert.assertEquals(1000, dog.getType(2));
        Assert.assertEquals(1000, dog.getType(3));
    }

    @Override
    @SandboxConfig(shadows = {ShadowCat.class})
    @Test
    public void testMockInnerNewObject() {
        //注意所有cat实例都被mock
        Dog dog = new Dog();
        String name = dog.getCat().getName();
        Assert.assertEquals("Mock Cat", name);
    }

    @Override
    @SandboxConfig(shadows = {ShadowDog.class})
    @Test
    public void testMockStaticMethod() {
        Assert.assertEquals(1000, Dog.getStaticType());
    }

    @Override
    @SandboxConfig(shadows = {ShadowDog.class})
    @Test
    public void testMockNativeMethod() {
        Assert.assertEquals(1000, Dog.getStaticNativeType());
        Dog dog = new Dog();
        Assert.assertEquals(1000, dog.getNativeType());
    }

    @Implements(FinalDog.class)
    public static class ShadowFinalDog {
        @Implementation
        public int getFinalType() {
            return 1000;
        }
    }

    @Override
    @SandboxConfig(shadows = {ShadowFinalDog.class})
    @Test
    public void testMockFinalMethod() {
        FinalDog dog = new FinalDog();
        Assert.assertEquals(1000, dog.getFinalType());
    }

    @Implements(TomDog.class)
    public static class ShadowTomDog {

        public String mName;

        @Implementation
        protected void __constructor__() {
        }

        @Implementation
        protected void __constructor__(String name) {
            this.mName = name;
        }

        @Implementation
        protected static void __staticInitializer__() {
        }
    }

    @Override
    @SandboxConfig(shadows = {ShadowTomDog.class})
    @Test
    public void testMockStaticBlock() {
        Assert.assertEquals(0, TomDog.staticInit);
    }

    @Override
    @SandboxConfig(shadows = {ShadowTomDog.class})
    @Test
    public void testMockConstructorAndBlock() {
        TomDog tomDog = new TomDog();
        Assert.assertEquals(0, tomDog.init);
    }

    @Implements(Dog.class)
    public static class ShadowDog1 {
        @Implementation
        public void eat() {
//            super.eat();
            System.out.println("Dog eat");
        }
    }

    @Override
    @SandboxConfig(shadows = {ShadowDog1.class})
    @Test
    public void testSuppressFatherClassMethod() {
        Dog dog = new Dog();
        dog.eat();
    }

    @Implements(ILife.class)
    public static class ShadowILife {
        @Implementation
        public String getName() {
            return "Mock life";
        }
    }

    @Override
    @SandboxConfig(shadows = {ShadowILife.class})
    @Test
    public void testMockFatherAndAllChildren() {
        Dog dog = new Dog();
        System.out.println(dog.getName());
    }

    @Override
    public void testReset() {
        //不支持
    }
}
