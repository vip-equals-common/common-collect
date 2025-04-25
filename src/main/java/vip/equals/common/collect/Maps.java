package vip.equals.common.collect;

import java.util.*;
import java.util.function.Function;

/**
 * map的一些操作
 *
 * @author HJ
 * @since 1.0.0
 */
public final class Maps {

    private Maps() {
    }

    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<>(16);
    }

    public static <K, V> HashMap<K, V> newLinkHashMap() {
        return new LinkedHashMap<>(16);
    }

    public static <K, V> HashMap<K, V> newMap() {
        return new HashMap<>(16);
    }


    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> emptyMap() {
        return (Map<K, V>) Collections.EMPTY_MAP;
    }

    /**
     * map中value的转换
     *
     * @param map            原始map
     * @param valueConverter 转换器
     * @param <K>            key
     * @param <T>            原始类型
     * @param <R>            转换后类型
     * @return 转换后的map
     */
    public static <K, T, R> Map<K, R> to(Map<K, T> map, Function<T, R> valueConverter) {
        if (map == null) {
            return null;
        }
        Map<K, R> result = new HashMap<>(map.size());
        map.forEach((k, t) -> result.put(k, valueConverter.apply(t)));
        return result;
    }

    /**
     * map中key, value的转换
     *
     * @param map            原始map
     * @param keyConverter   转换器
     * @param valueConverter 转换器
     * @param <K>            key
     * @param <B>            key转换后类型
     * @param <T>            value原始类型
     * @param <R>            value转换后类型
     * @return 转换后的map
     */
    public static <K, B, T, R> Map<B, R> to(Map<K, T> map, Function<K, B> keyConverter, Function<T, R> valueConverter) {
        if (map == null) {
            return null;
        }
        Map<B, R> result = new HashMap<>(map.size());
        map.forEach((k, t) -> result.put(keyConverter.apply(k), valueConverter.apply(t)));
        return result;
    }


    /**
     * 按keys顺序，从map中获取数据，返回D的list
     *
     * @param keys 数据key
     * @param map  数据map
     * @return List<D>
     */
    public static <K, D> List<D> asListSeq(List<K> keys, Map<K, D> map) {
        if (keys == null) {
            return null;
        }
        if (keys.isEmpty()) {
            return new ArrayList<>();
        }
        if (map == null || map.isEmpty()) {
            return new ArrayList<>();
        }
        List<D> list = new ArrayList<>();
        for (K key : keys) {
            D d = map.get(key);
            if (d != null) {
                list.add(d);
            }
        }
        return list;
    }

    /**
     * 获取Map的部分key生成新的Map
     *
     * @param <K>  Key类型
     * @param <V>  Value类型
     * @param map  Map
     * @param keys 键列表
     * @return 新Map，只包含指定的key
     */
    public static <K, V> Map<K, V> getAny(Map<K, V> map, List<K> keys) {
        if (map == null || map.isEmpty() || keys == null || keys.isEmpty()) {
            return new HashMap<>(16);
        }
        Map<K, V> resultMap = new HashMap<>(16);
        for (K key : keys) {
            V v = map.get(key);
            if (v != null) {
                resultMap.put(key, v);
            }
        }
        return resultMap;
    }

    /**
     * 获取Map的部分key生成新的Map
     *
     * @param <K>  Key类型
     * @param <V>  Value类型
     * @param map  Map
     * @param keys 键列表
     * @return 新Map，只包含指定的key
     */
    @SafeVarargs
    public static <K, V> Map<K, V> getAny(Map<K, V> map, K... keys) {
        if (map == null || map.isEmpty() || keys == null || keys.length == 0) {
            return new HashMap<>(16);
        }
        Map<K, V> resultMap = new HashMap<>(16);
        for (K key : keys) {
            V v = map.get(key);
            if (v != null) {
                resultMap.put(key, v);
            }
        }
        return resultMap;
    }

    /**
     * 获取Map中包含的key列表
     *
     * @param <K>  Key类型
     * @param map  Map
     * @param keys 键列表
     * @return map中包含的key列表
     */
    public static <K> List<K> containKeys(Map<K, ?> map, List<K> keys) {
        if (map == null || map.isEmpty() || keys == null || keys.isEmpty()) {
            return new ArrayList<>();
        }
        List<K> resultList = new ArrayList<>();
        for (K key : keys) {
            if (map.containsKey(key)) {
                resultList.add(key);
            }
        }
        return resultList;
    }

    /**
     * 获取Map中包含的key列表
     *
     * @param <K>  Key类型
     * @param map  Map
     * @param keys 键列表
     * @return map中包含的key列表
     */
    public static <K, V> List<V> containKeyValues(Map<K, V> map, List<K> keys) {
        if (map == null || map.isEmpty() || keys == null || keys.isEmpty()) {
            return new ArrayList<>();
        }
        List<V> resultList = new ArrayList<>();
        for (K key : keys) {
            V v = map.get(key);
            if (v != null) {
                resultList.add(v);
            }
        }
        return resultList;
    }

    /**
     * 获取Map中不包含keys的列表
     *
     * @param <K>  Key类型
     * @param map  Map
     * @param keys 键列表
     * @return map中不包含的key列表
     */
    public static <K> List<K> notContainKeys(Map<K, ?> map, List<K> keys) {
        if (map == null || map.isEmpty()) {
            return keys == null ? new ArrayList<>() : new ArrayList<>(keys);
        }
        List<K> resultList = new ArrayList<>();
        if (keys == null || keys.isEmpty()) {
            return new ArrayList<>();
        }
        for (K key : keys) {
            if (!map.containsKey(key)) {
                resultList.add(key);
            }
        }
        return resultList;
    }
}
