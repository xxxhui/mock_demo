package com.example.myapplication.demo.bean;

public class Cat extends Animal {
    @Override
    public boolean run(int step) {
        System.out.println("Cat run: " + step);
        return true;
    }

    @Override
    public String getName() {
        return "Cat";
    }

    @Override
    public void eat() {
        super.eat();
        System.out.println("Cat eat");
    }
}
