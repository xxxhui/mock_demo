package com.example.myapplication.demo;

public interface IVerify {
    /**
     * 验证精确调用
     */
    void verifyInvoke();

    /**
     * 验证参数模糊匹配调用
     */
    void verifyInvokeWithParamMatcher();

    /**
     * 验证调用顺序
     */
    void verifyInvokeByOrder();

    /**
     * 方法参数验证
     */
    void verifyInvokeParams();

    /**
     * 延迟验证
     */
    void verifyDelay();

    /**
     * 验证没有调用过
     */
    void verifyNotInvoke();

    /**
     * 验证调用n次
     */
    void verifyNotInvokeWithTimes();
}
