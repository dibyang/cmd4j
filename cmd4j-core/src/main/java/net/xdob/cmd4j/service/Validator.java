package net.xdob.cmd4j.service;

/**
 * 输入校验接口
 */
@FunctionalInterface
public interface Validator<T> {

    /**
     * 校验输入是否合法
     * @param value
     * @return 错误信息
     */
    String valid(T value);
}
