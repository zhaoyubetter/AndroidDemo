package com.ldfs.demo.annotation.item;

/**
 * Created by cz on 15/6/21.
 *
 * @author cz
 *         进度信息(rate info)
 */
public enum Rate {
    /**
     * 创建demo
     */
    CREATE,
    /**
     * demo实现中
     */
    CODING,
    /**
     * demo完成,处于测试使用阶段
     */
    COMPLETE_BATE,
    /**
     * demo完结
     */
    COMPLETE;

    @Override
    public String toString() {
        String value = null;
        switch (this) {
            case CREATE:
                value = "Demo创建";
                break;
            case CODING:
                value = "Demo实现中";
                break;
            case COMPLETE_BATE:
                value = "Demo功能基本实现,测试中";
                break;
            case COMPLETE:
                value = "Demo功能己实现,并未发现异常.可使用";
                break;
        }
        return value;
    }
}
