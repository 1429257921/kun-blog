package com.kun.blog.enums;

import cn.hutool.core.collection.CollUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 跑圈主页数据计算枚举
 *
 * @author gzc
 * @since 2023/1/8 17:16
 **/
@Slf4j
@Getter
public enum RunCircleHomeCalculateEnum {
    /**
     * 枚举
     */
    DYNAMIC(0, "动态", 0.6D),
    ACTIVITY(1, "兴趣活动", 0.15D),
    CHAT(2, "约聊订单", 0.25D),
    ;

    /**
     * 索引(不能重复)
     */
    private final int index;
    /**
     * 名称
     */
    private final String name;
    /**
     * 概率
     */
    private final double probability;

    RunCircleHomeCalculateEnum(int index, String name, double probability) {
        this.index = index;
        this.name = name;
        this.probability = probability;
    }

    /**
     * 根据索引获取对应的枚举
     *
     * @param index 索引
     * @return 枚举对象
     */
    public static RunCircleHomeCalculateEnum get(Integer index) {
        for (RunCircleHomeCalculateEnum runCircleHomeCalculateEnum : RunCircleHomeCalculateEnum.values()) {
            if (runCircleHomeCalculateEnum.getIndex() == index) {
                return runCircleHomeCalculateEnum;
            }
        }
        return null;
    }

    /**
     * 获取排序后的概率集合
     *
     * @return 排序概率集合
     */
    public static List<Double> getSortProbabilityList() {
        RunCircleHomeCalculateEnum[] values = RunCircleHomeCalculateEnum.values();
        List<Double> list = new ArrayList<>(values.length);
        for (RunCircleHomeCalculateEnum runCircleHomeCalculateEnum : values) {
            list.add(runCircleHomeCalculateEnum.getProbability());
        }
        return list;
    }

    /**
     * 获取随机索引
     *
     * @param originalRateList 概率列表，保证顺序和实际物品对应
     * @return 命中索引
     */
    public static int getRandomIndex(List<Double> originalRateList) {
        if (CollUtil.isEmpty(originalRateList)) {
            return -1;
        }

        int size = originalRateList.size();

        // 计算总概率，这样可以保证不一定总概率是1
        double sumRate = 0d;
        for (double rate : originalRateList) {
            sumRate += rate;
        }

        // 计算每个物品在总概率的基础下的概率情况
        List<Double> sortOrignalRates = new ArrayList<>(size);
        Double tempSumRate = 0d;
        for (double rate : originalRateList) {
            tempSumRate += rate;
            sortOrignalRates.add(tempSumRate / sumRate);
        }

        // 根据区块值来获取抽取到的物品索引
        double nextDouble = Math.random();
        sortOrignalRates.add(nextDouble);
        Collections.sort(sortOrignalRates);

        return sortOrignalRates.indexOf(nextDouble);
    }

    /**
     * 根据概率随机获取枚举
     *
     * @return 枚举类
     */
    public static RunCircleHomeCalculateEnum getRandom() {
        // 获取概率集合
        List<Double> probabilityList = getSortProbabilityList();
        // 获取命中后的索引
        int index = getRandomIndex(probabilityList);
        // 根据索引获取对应的枚举
        RunCircleHomeCalculateEnum calculateEnum = get(index);
        log.info("命中对应动态->名称:{}，概率:{}", calculateEnum.getName(), calculateEnum.getProbability());
        return calculateEnum;
    }
}
