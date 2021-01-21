package com.example.myapplication.demo.bean;

public abstract class Animal implements ILife {
    public abstract boolean run(int step);
    public void eat() {
        System.out.println("Animal eat");
    }
}
