package vip.equals.common.collect;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * 收集器
 * <pre>
 *     Item对象中有 creatorId, updaterId，要把creatorId, updaterId都收集起来
 *     Collectors.integers(items, Item::getCreatorId, Item::getUpdaterId);
 * </pre>
 *
 * @author HJ
 * @since 1.1.0
 */
public class Sets {

    /**
     * 创建一个空的Set
     *
     * @return 空的Set
     */
    public static <E> HashSet<E> newHashSet() {
        return new HashSet<>();
    }

    /**
     * 创建一个包含指定元素Set
     *
     * @param elements 元素
     * @return 包含指定元素Set
     */
    @SafeVarargs
    public static <E> HashSet<E> newHashSet(E... elements) {
        HashSet<E> set = newHashSetWithExpectedSize(elements.length);
        Collections.addAll(set, elements);
        return set;
    }

    /**
     * 创建一个指定容量的Set
     *
     * @param expectedSize 容量
     * @return 指定容量的Set
     */
    public static <E> HashSet<E> newHashSetWithExpectedSize(int expectedSize) {
        return new HashSet<>(expectedSize);
    }

    /**
     * 创建一个包含指定元素Set
     *
     * @param collection 元素
     * @return 创建一个包含指定元素Set
     */
    public static <E> HashSet<E> newHashSet(Collection<E> collection) {
        return new HashSet<>(collection);
    }

    /**
     * 空的Set
     *
     * @return 空的Set
     */
    public static <E> Set<E> emptySet() {
        return Collections.emptySet();
    }

    /**
     * 收集collections中每个对象的多个Byte属性
     *
     * @param collections 对象集合
     * @param functions   获取Byte属性的function
     * @return Byte的Set集合
     */
    @SafeVarargs
    public static <T> Set<Byte> bytes(Collection<T> collections, Function<T, Byte>... functions) {
        return attrs(collections, functions);
    }


    /**
     * 收集collections中每个对象的多个Short属性
     *
     * @param collections 集合
     * @param functions   获取Short属性的function
     * @return Short的Set集合
     */
    @SafeVarargs
    public static <T> Set<Short> shorts(Collection<T> collections, Function<T, Short>... functions) {
        return attrs(collections, functions);
    }

    /**
     * 收集collections中每个对象的多个Integer属性
     *
     * @param collections 集合
     * @param functions   获取Integer属性的function
     * @return Integer的Set集合
     */
    @SafeVarargs
    public static <T> Set<Integer> integers(Collection<T> collections, Function<T, Integer>... functions) {
        return attrs(collections, functions);
    }

    /**
     * 收集collections中每个对象的多个Long属性
     *
     * @param collections 集合
     * @param functions   获取Long属性的function
     * @return Long的Set集合
     */
    @SafeVarargs
    public static <T> Set<Long> longs(Collection<T> collections, Function<T, Long>... functions) {
        return attrs(collections, functions);
    }

    /**
     * 收集collections中每个对象的多个Long属性
     *
     * @param collections 集合
     * @param functions   获取Long属性的function
     * @return Long的Set集合
     */
    @SafeVarargs
    public static <T> Set<Double> doubles(Collection<T> collections, Function<T, Double>... functions) {
        return attrs(collections, functions);
    }

    /**
     * 收集collections中每个对象的多个Long属性
     *
     * @param collections 集合
     * @param functions   获取Long属性的function
     * @return Long的Set集合
     */
    @SafeVarargs
    public static <T> Set<Float> floats(Collection<T> collections, Function<T, Float>... functions) {
        return attrs(collections, functions);
    }

    /**
     * 收集collections中每个对象的多个String属性
     *
     * @param collections 集合
     * @param functions   获取String属性的function
     * @return String的Set集合
     */
    @SafeVarargs
    public static <T> Set<String> strings(Collection<T> collections, Function<T, String>... functions) {
        return attrs(collections, functions);
    }

    /**
     * 收集collections中每个对象的多个属性
     *
     * @param collections 集合
     * @param functions   获取属性的function
     * @return 属性的Set集合
     */
    @SafeVarargs
    public static <T, A> Set<A> attrs(Collection<T> collections, Function<T, A>... functions) {
        if (collections == null || collections.size() == 0) {
            return new HashSet<>();
        }
        if (functions == null || functions.length == 0) {
            return new HashSet<>();
        }
        Set<A> set = new HashSet<>();
        for (T t : collections) {
            for (Function<T, A> function : functions) {
                A apply = function.apply(t);
                if (apply != null) {
                    set.add(apply);
                }
            }
        }
        return set;
    }
}
