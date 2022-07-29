package com.landon.debug.inf;

/**
 * @Author Ren Wenzhang
 * @Date 2022/6/27/027 19:14
 * @Description
 */
public interface InfEvent<T> {
    void perform(T t);
}
