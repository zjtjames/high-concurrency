/**
 * created by Zheng Jiateng on 2019/8/1.
 */

package com.jmall.high.redis;
// 接口->抽象类->实现类 模板模式
public abstract class BasePrefix implements KeyPrefix{

    private int expireSeconds;

    private String prefix;

    public BasePrefix(String prefix) {//0代表永不过期
        this.prefix = prefix;
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    public int expireSeconds() {//默认0代表永不过期
        return expireSeconds;
    }

    public String getPrefix() {
        String className = this.getClass().getSimpleName();
        return className+":" + prefix;
    }
}
