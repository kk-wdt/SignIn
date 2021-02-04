package com.kktt.jesus.utils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommonUtil {

    /**
     * 计算切分次数
     * @param size 需要分割的集合长度
     * @param maxNum 集合最大的数量
     * @return 切分次数
     */
    private static Integer countStep(Integer size, Integer maxNum) {
        return (size + maxNum - 1) / maxNum;
    }

    /**
     * 将一个集合进行分割
     * @param collections 需要分割的集合 (List LinkedHashSet TreeSet LinkedList)
     * @param maxNum 集合最大的数量
     * @param <T>
     * @return 分割后的集合
     */
    public static <T> List<List<T>> subCollection(List<T> collections, int maxNum){

        int limit = countStep(collections.size(), maxNum);
        //方法一：使用流遍历操作
//        Collection<Collection<T>> mglist = new ArrayList<>();
//        Stream.iterate(0, n -> n + 1).limit(limit).forEach(i -> {
//            mglist.add(collections.stream().skip(i * maxNum).limit(maxNum).collect(Collectors.toList()));
//        });
//        return mglist;

        //方法二：获取分割后的集合
        List<List<T>> splitCollection = Stream.iterate(
                0, n -> n + 1).limit(limit).parallel().map(
                a -> collections.stream().skip(a * maxNum).limit(maxNum).parallel()
                        .collect(Collectors.toList())).collect(Collectors.toList());

        return splitCollection;
    }

}
