package com.example.myapplication.demo.bean;

public class TomCat extends Cat{
    private ILife mLife = new ILife() {
        @Override
        public String getName() {
            return TomCat.this.getName();
        }
    };

    @Override
    public String getName() {
        return "Tom cat";
    }

    public String getLifeName() {
        return mLife.getName();
    }
}
