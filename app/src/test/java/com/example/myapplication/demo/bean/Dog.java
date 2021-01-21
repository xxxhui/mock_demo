package com.example.myapplication.demo.bean;

public class Dog extends Animal {

    protected String mName = "Dog";

    @Override
    public boolean run(int step) {
        System.out.println("Dog run: " + step);
        return true;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void eat() {
        super.eat();
        System.out.println("Dog eat");
    }

    public String getInnerCatName() {
        Cat cat = new Cat();
        System.out.println("Dog getInnerCatName");
        return cat.getName();
    }

    public Cat getCat() {
        return new Cat();
    }

    public int getType(int type) {
        switch (type) {
            case 1:
                return getPrivateType();
            case 2:
                return getProtectedType();
            case 3:
                return getPackageType();
            case 4:
                return getPrivateStaticType();
        }
        return -1;
    }

    private int getPrivateType() {
        return 0;
    }

    protected int getProtectedType() {
        return 0;
    }

    int getPackageType() {
        return 0;
    }

    public static int getStaticType() {
        return 0;
    }

    private static int getPrivateStaticType() {
        return 0;
    }

    public static native int getStaticNativeType();

    public native int getNativeType();
}
