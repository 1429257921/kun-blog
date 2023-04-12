package com.kun.blog;

import com.alibaba.fastjson.JSON;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TODO
 * run_circle_home
 * @author gzc
 * @since 2023/1/8 16:20
 **/
public class RandomTest {
    @Getter
    private enum RunCircleHomeCalculateEnum {
        /**
         *
         */
        DYNAMIC(0, "动态", 0.6D),
        ACTIVITY(1, "兴趣活动", 0.15D),
        CHAT(2, "约聊订单", 0.25D),
        ;

        private final int index;
        private final String name;
        private final double probability;

        RunCircleHomeCalculateEnum(int index, String name, double probability) {
            this.index = index;
            this.name = name;
            this.probability = probability;
        }

        public static RunCircleHomeCalculateEnum get(Integer index) {
            for (RunCircleHomeCalculateEnum runCircleHomeCalculateEnum : RunCircleHomeCalculateEnum.values()) {
                if (runCircleHomeCalculateEnum.getIndex() == index) {
                    return runCircleHomeCalculateEnum;
                }
            }
            return null;
        }
    }

    private static class Random {
        public final AtomicLong seed = new AtomicLong();

        public final static long C = 1;

        public final static long A = 48271;

        public final static long M = (1L << 31) - 1;

        public Random(int seed) {
            this.seed.set(seed);
        }

        public Random() {
            this.seed.set(System.nanoTime());
        }

        public long nextLong() {
            seed.set(System.nanoTime());
            return (A * seed.longValue() + C) % M;
        }

        public int nextInt(int number) {
            return new Long((A * System.nanoTime() + C) % number).intValue();
        }
    }

    /**
     * 抽奖方法
     * <br/>
     * create by: leigq
     * <br/>
     * create time: 2019/7/5 23:08
     *
     * @param orignalRates 商品中奖概率列表，保证顺序和实际物品对应
     * @return 中奖商品索引
     */
    public static int lottery(List<Double> orignalRates) {

        if (orignalRates == null || orignalRates.isEmpty()) {
            return -1;
        }

        int size = orignalRates.size();

        // 计算总概率，这样可以保证不一定总概率是1
        double sumRate = 0d;
        for (double rate : orignalRates) {
            sumRate += rate;
        }

        // 计算每个物品在总概率的基础下的概率情况
        List<Double> sortOrignalRates = new ArrayList<>(size);
        Double tempSumRate = 0d;
        for (double rate : orignalRates) {
            tempSumRate += rate;
            sortOrignalRates.add(tempSumRate / sumRate);
        }

        // 根据区块值来获取抽取到的物品索引
        double nextDouble = Math.random();
        sortOrignalRates.add(nextDouble);
        Collections.sort(sortOrignalRates);

        return sortOrignalRates.indexOf(nextDouble);
    }

    public static void main(String[] args) {
        RunCircleHomeCalculateEnum[] runCircleHomeCalculateEnums = RunCircleHomeCalculateEnum.values();
        List<Double> list1 = new ArrayList<>(runCircleHomeCalculateEnums.length);
        for (RunCircleHomeCalculateEnum runCircleHomeCalculateEnum : runCircleHomeCalculateEnums) {
            list1.add(runCircleHomeCalculateEnum.probability);
        }

        final int max = 1000000;
        Map<RunCircleHomeCalculateEnum, Integer> map = new HashMap<>(max);
        for (int i = 0; i < max; i++) {
            RunCircleHomeCalculateEnum runCircleHomeCalculateEnum = RunCircleHomeCalculateEnum.get(lottery(list1));
            String name = runCircleHomeCalculateEnum.getName();
            Integer integer = map.get(runCircleHomeCalculateEnum);
            if (integer == null) {
                map.put(runCircleHomeCalculateEnum, 1);
            } else {
                map.put(runCircleHomeCalculateEnum, integer + 1);
            }
        }
        System.out.println(JSON.toJSONString(map));
        map.forEach((k, v) -> {
//            System.out.println("k:" + k + ",v:" + v);
            BigDecimal divide = new BigDecimal(v).divide(new BigDecimal(max));
            System.out.println(k.probability + "," + k + "," + divide);
        });
        if (true) {
            return;
        }

        List<RunCircleHomeCalculateEnum> list = new ArrayList<>(100);
        for (RunCircleHomeCalculateEnum runCircleHomeCalculateEnum : runCircleHomeCalculateEnums) {
            Double v = runCircleHomeCalculateEnum.getProbability() * 100;
            for (int i = 0; i < v.intValue(); i++) {
                list.add(runCircleHomeCalculateEnum);
            }
        }
        System.out.println(list.size());
        System.out.println(JSON.toJSONString(list));

        Map<String, Integer> result = new HashMap<>(3);
        for (int i = 0; i < 10000; i++) {
            int ran = new Random().nextInt(list.size());
            System.out.println(ran);
            RunCircleHomeCalculateEnum runCircleHomeCalculateEnum = list.get(-ran);
            Integer integer = result.get(runCircleHomeCalculateEnum.getName());
            if (integer == null) {
                result.put(runCircleHomeCalculateEnum.getName(), 1);
            } else {
                result.put(runCircleHomeCalculateEnum.getName(), integer + 1);
            }
        }
        System.out.println(JSON.toJSONString(result));
    }
}
