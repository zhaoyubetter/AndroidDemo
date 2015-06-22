package com.ldfs.demo.annotation.item;

import com.ldfs.demo.R;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by cz on 15/6/21.
 * 分类进度
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateInfo {
    /**
     * 获取demo实现进度
     * @return rate
     */
    Rate rate() default Rate.CODING;

    /**  测试信息 */
    int beteInfo() default R.string.no_beteinfo;
}
