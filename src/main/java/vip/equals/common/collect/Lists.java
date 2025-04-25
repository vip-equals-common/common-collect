package vip.equals.common.collect;

import org.apache.commons.lang3.StringUtils;
import vip.equals.common.function.TriConsumer;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * list的工具栏，stream中的一些操作
 *
 * @author HJ
 * @since 1.0.0
 */
public final class Lists {

    private Lists() {
    }

    /**
     * newList
     *
     * @return ArrayList
     */
    public static <T> ArrayList<T> newList() {
        return new ArrayList<>();
    }

    /**
     * newArrayList
     *
     * @return ArrayList
     */
    public static <T> ArrayList<T> newArrayList() {
        return new ArrayList<>();
    }

    /**
     * newList
     *
     * @param elements T
     * @return ArrayList
     */
    @SafeVarargs
    public static <T> ArrayList<T> newList(T... elements) {
        return newArrayList(elements);
    }

    /**
     * newArrayList
     *
     * @param elements T
     * @return ArrayList
     */
    @SafeVarargs
    public static <T> ArrayList<T> newArrayList(T... elements) {
        ArrayList<T> list = new ArrayList<>(elements.length);
        Collections.addAll(list, elements);
        return list;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> emptyList() {
        return (List<T>) Collections.EMPTY_LIST;
    }

    /**
     * list转换，如果converter返回null，则不添加到list中
     *
     * @param list      原始
     * @param converter 转换器
     * @param <T>       原始类型
     * @param <R>       转换后类型
     * @return 转换后的list
     */
    public static <T, R> List<R> to(List<T> list, Function<T, R> converter) {
        if (list == null) {
            return null;
        }
        return stream(list).map(converter).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * list转换,如果converter返回null，会添加到返回的list中
     *
     * @param list      原始
     * @param converter 转换器
     * @param <T>       原始类型
     * @param <R>       转换后类型
     * @return 转换后的list
     */
    public static <T, R> List<R> toWithNull(List<T> list, Function<T, R> converter) {
        if (list == null) {
            return null;
        }
        return stream(list).map(converter).collect(Collectors.toList());
    }

    /**
     * 合并list中的每个list
     *
     * @param list 列表
     * @return 合并了所有list中list的内容
     */
    public static <D> List<D> collect(List<List<D>> list) {
        if (list == null) {
            return null;
        }
        if (list.isEmpty()) {
            return new ArrayList<>();
        }
        return stream(list).flatMap(Collection::stream).collect(Collectors.toList());
    }

    /**
     * 合并list中的每个元素中的list子属性
     *
     * @param list         列表
     * @param listFunction 获取子列表的function
     * @return 所有子属性list的内容
     */
    public static <D, SD> List<SD> collect(List<D> list, Function<D, List<SD>> listFunction) {
        if (list == null) {
            return null;
        }
        if (list.isEmpty()) {
            return new ArrayList<>();
        }
        return stream(list).map(listFunction).flatMap(Collection::stream).collect(Collectors.toList());
    }

    /**
     * 合并D中SD（sd list）的所有F属性
     *
     * @param list          D列表
     * @param listFunction  D中获取List<SD>的function
     * @param fieldFunction SD中获取F属性的function
     * @return 所有F属性的内容
     */
    public static <D, SD, F> List<F> collect(List<D> list, Function<D, List<SD>> listFunction, Function<SD, F> fieldFunction) {
        if (list == null) {
            return null;
        }
        if (list.isEmpty()) {
            return new ArrayList<>();
        }
        return stream(list).map(listFunction).flatMap(Collection::stream).map(fieldFunction).collect(Collectors.toList());
    }

    /**
     * String list -> Integer list
     *
     * @param list String list
     * @return Integer list
     */
    public static List<Integer> toInteger(List<String> list) {
        if (list == null) {
            return null;
        }
        List<Integer> results = new ArrayList<>(list.size());
        for (String s : list) {
            try {
                results.add(Integer.parseInt(s));
            } catch (Exception ignored) {
            }
        }
        return results;
    }

    /**
     * String list -> Long list
     *
     * @param list String list
     * @return Long list
     */
    public static List<Long> toLong(List<String> list) {
        if (list == null) {
            return null;
        }
        List<Long> results = new ArrayList<>(list.size());
        for (String s : list) {
            try {
                results.add(Long.parseLong(s));
            } catch (Exception ignored) {
            }
        }
        return results;
    }

    /**
     * map中value list 的转换
     *
     * @param map       原始map
     * @param converter 转换器
     * @param <K>       key
     * @param <T>       原始类型
     * @param <R>       转换后类型
     * @return 转换后的map
     */
    public static <K, T, R> Map<K, List<R>> to(Map<K, List<T>> map, Function<T, R> converter) {
        if (map == null) {
            return null;
        }
        Map<K, List<R>> result = new HashMap<>(map.size());
        map.forEach((k, t) -> result.put(k, to(t, converter)));
        return result;
    }

    /**
     * list转为set
     *
     * @param list      原始
     * @param converter 转换器
     * @param <T>       原始类型
     * @param <R>       转换后类型
     * @return 转换后的set
     */
    public static <T, R> Set<R> toSet(List<T> list, Function<T, R> converter) {
        if (list == null) {
            return null;
        }
        return stream(list).map(converter).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    /**
     * list转换
     *
     * @param list      原始
     * @param converter 转换器
     * @param <T>       原始类型
     * @param <R>       转换后类型
     * @return 转换后的list
     */
    public static <T, R> List<R> toUniqueList(List<T> list, Function<T, R> converter) {
        if (list == null) {
            return null;
        }
        return stream(list).map(converter).filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }

    /**
     * 过滤list
     *
     * @param list      需要过滤的list
     * @param predicate Predicate
     * @param <T>       类型
     * @return 过滤过的list
     */
    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        if (list == null) {
            return null;
        }
        return stream(list).filter(predicate).collect(Collectors.toList());
    }

    /**
     * 过滤list中的null
     *
     * @param list 需要过滤的list
     * @param <T>  类型
     * @return 过滤过的list
     */
    public static <T> List<T> filterNull(List<T> list) {
        if (list == null) {
            return null;
        }
        return stream(list).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * list过滤，并转为set
     *
     * @param list      原始
     * @param predicate 过滤器
     * @param <T>       原始类型
     * @return set
     */
    public static <T> Set<T> filterToSet(List<T> list, Predicate<T> predicate) {
        if (list == null) {
            return null;
        }
        return stream(list).filter(predicate).collect(Collectors.toSet());
    }

    /**
     * list过滤转换，并转为set
     *
     * @param list      原始
     * @param predicate 过滤器
     * @param converter 转换器
     * @param <T>       原始类型
     * @return set
     */
    public static <T, R> Set<R> filterToSet(List<T> list, Predicate<T> predicate, Function<T, R> converter) {
        if (list == null) {
            return null;
        }
        return stream(list).filter(predicate).map(converter).collect(Collectors.toSet());
    }

    /**
     * list转换
     *
     * @param list      原始
     * @param predicate 过滤器
     * @param converter 转换器
     * @param <T>       原始类型
     * @param <R>       转换后类型
     * @return 转换后的list
     */
    public static <T, R> List<R> filterTo(List<T> list, Predicate<T> predicate, Function<T, R> converter) {
        if (list == null) {
            return null;
        }
        return stream(list).filter(predicate).map(converter).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * sourceList 中是否包含一个 checkList中的一个元素
     *
     * @param sourceList 集合
     * @param checkList  检查的数组
     * @return true/false
     */
    public static <D> boolean hasOne(List<D> sourceList, List<D> checkList) {
        if (sourceList == null || sourceList.isEmpty() || checkList == null || checkList.isEmpty()) {
            return false;
        }
        for (D check : checkList) {
            if (sourceList.contains(check)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 从collection中查找某个属性值等于v的对象
     *
     * @param collection 集合
     * @param v          值 可以为null
     * @param function   获取属性的方法
     * @param <E>        对象
     * @param <V>        值
     * @return 查找到的对象
     */
    public static <E, V> E findOne(Collection<E> collection, V v, Function<E, V> function) {
        if (collection == null) {
            return null;
        }
        for (E e : collection) {
            if (v == null ? function.apply(e) == null : v.equals(function.apply(e))) {
                return e;
            }
        }
        return null;
    }

    /**
     * 从collection中查找某个属性值等于的对象
     *
     * @param collection 集合
     * @param function   获取属性的方法
     * @param <E>        对象
     * @return 查找到的对象
     */
    public static <E> E findOne(Collection<E> collection, Predicate<E> function) {
        return findOne(collection, function, (Supplier<E>) null);
    }

    /**
     * 从collection中查找某个属性值等于的对象
     *
     * @param collection 集合
     * @param function   获取属性的方法
     * @param <E>        对象
     * @return 查找到的对象
     */
    public static <E> E findOne(Collection<E> collection, Predicate<E> function, Supplier<E> notFindGet) {
        if (collection == null) {
            return null;
        }
        for (E e : collection) {
            if (function.test(e)) {
                return e;
            }
        }
        return notFindGet == null ? null : notFindGet.get();
    }

    /**
     * 从collection中查找是否存在某个属性值等于的对象
     *
     * @param collection 集合
     * @param function   获取属性的方法
     * @param <E>        对象
     * @return true/false
     */
    public static <E> boolean matchOne(Collection<E> collection, Predicate<E> function) {
        return findOne(collection, function) != null;
    }

    /**
     * 收集对象连的某个属性值
     *
     * @param collection 集合
     * @param function   获取属性的方法
     * @param <T>        对象
     * @param <R>        返回的属性值
     * @return 属性的集合
     */
    public static <T, R> List<R> toList(Collection<T> collection, Function<T, R> function) {
        if (collection == null) {
            return null;
        }
        return collection.stream().map(function).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 收集对象连的某个属性值
     *
     * @param collection 集合
     * @param function   获取属性的方法
     * @param <T>        对象
     * @param <R>        返回的属性值
     * @return 属性的集合
     */
    public static <T, R> Set<R> toSet(Collection<T> collection, Function<T, R> function) {
        if (collection == null) {
            return null;
        }
        return collection.stream().map(function).filter(Objects::nonNull).collect(Collectors.toSet());
    }


    /**
     * 只要有一个符合
     *
     * @param list      要检查的list
     * @param predicate 匹配器
     * @param <T>       对象
     * @return 是否有一个符合
     */
    public static <T> boolean anyMatch(List<T> list, Predicate<T> predicate) {
        if (list == null) {
            return false;
        }
        return stream(list).anyMatch(predicate);
    }

    /**
     * 都符合
     *
     * @param list      要检查的list
     * @param predicate 匹配器
     * @param <T>       对象
     * @return 是否全部符合
     */
    public static <T> boolean allMatch(List<T> list, Predicate<T> predicate) {
        if (list == null) {
            return false;
        }
        return stream(list).allMatch(predicate);
    }

    /**
     * 根据某属性进行distinct
     *
     * @param list       要检查的list
     * @param toProperty 获取属性
     * @param <T>        对象
     * @param <R>        属性类型
     * @return 根据属性过滤过的list
     */
    public static <T, R> List<T> distinctByProperty(List<T> list, Function<T, R> toProperty) {
        if (list == null) {
            return null;
        }
        Set<R> set = new HashSet<>();
        return stream(list).filter(t -> set.add(toProperty.apply(t))).collect(Collectors.toList());
    }

    /**
     * list转map
     *
     * @param list        集合
     * @param keyFunction key
     * @param <T>         对象
     * @param <K>         key
     * @return map
     */
    public static <T, K> Map<K, T> toMap(List<T> list, Function<T, K> keyFunction) {
        if (list == null) {
            return null;
        }
        return stream(list).collect(Collectors.toMap(keyFunction, v -> v));
    }

    /**
     * list转map
     *
     * @param list          集合
     * @param keyFunction   key
     * @param valueFunction value
     * @param <T>           对象
     * @param <K>           key
     * @return map
     */
    public static <T, K, V> Map<K, V> toMap(List<T> list, Function<T, K> keyFunction, Function<T, V> valueFunction) {
        if (list == null) {
            return null;
        }
        return stream(list).collect(Collectors.toMap(keyFunction, valueFunction));
    }

    /**
     * 分组
     *
     * @param list        集合
     * @param keyFunction 分组key
     * @param <T>         对象
     * @param <K>         key
     * @return map
     */
    public static <K, T> Map<K, List<T>> group(List<T> list, Function<T, K> keyFunction) {
        if (list == null) {
            return null;
        }
        return stream(list).collect(Collectors.groupingBy(keyFunction));
    }

    /**
     * 分组
     *
     * @param list        集合
     * @param keyFunction 分组key
     * @param converter   转换器
     * @param <T>         对象
     * @param <K>         key
     * @return map
     */
    public static <K, T, R> Map<K, List<R>> group(List<T> list, Function<T, K> keyFunction, Function<T, R> converter) {
        if (list == null) {
            return null;
        }
        return stream(list).collect(Collectors.groupingBy(keyFunction, Collectors.mapping(converter, Collectors.toList())));
    }

    /**
     * 分组为set
     *
     * @param list        集合
     * @param keyFunction 分组key
     * @param <T>         对象
     * @param <K>         key
     * @return map
     */
    public static <K, T> Map<K, Set<T>> groupSet(List<T> list, Function<T, K> keyFunction) {
        if (list == null) {
            return null;
        }
        return stream(list).collect(Collectors.groupingBy(keyFunction, Collectors.toSet()));
    }

    /**
     * 分组为set并转换
     *
     * @param list        集合
     * @param keyFunction 分组key
     * @param converter   转换器
     * @param <T>         对象
     * @param <K>         key
     * @return map
     */
    public static <K, T, R> Map<K, Set<R>> groupSet(List<T> list, Function<T, K> keyFunction, Function<T, R> converter) {
        if (list == null) {
            return null;
        }
        return stream(list).collect(Collectors.groupingBy(keyFunction, Collectors.mapping(converter, Collectors.toSet())));
    }

    private static <T> Stream<T> stream(List<T> list) {
        return list.stream();
    }

    /**
     * 比较2个list中的每个元素是否都相等
     *
     * @param aList list
     * @param bList list
     * @return true/false
     */
    public static boolean equals(List<?> aList, List<?> bList) {
        if (aList == null || bList == null) {
            return false;
        }
        if (aList == bList) {
            return true;
        }
        if (aList.size() != bList.size()) {
            return false;
        }
        for (int i = 0; i < aList.size(); i++) {
            if (!Objects.equals(aList.get(i), bList.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 按keys顺序，从map中获取数据，返回D的list
     *
     * @param keys 数据key
     * @param map  数据map
     * @return List<D>
     */
    public static <K, D> List<D> asListSeq(List<K> keys, Map<K, D> map) {
        return Maps.asListSeq(keys, map);
    }

    /**
     * 把list中对象的某个属性用separator链接起来
     *
     * @param list      集合
     * @param function  Function
     * @param separator 链接字符串
     * @return String
     * @see StringUtils#join(Iterable, String)
     */
    public static <T, R> String join(List<T> list, Function<T, R> function, String separator) {
        if (list == null || list.isEmpty()) {
            return StringUtils.EMPTY;
        }
        List<R> to = to(list, function);
        return StringUtils.join(to, separator);
    }

    /**
     * 把list中对象的某个属性用separator链接起来
     *
     * @param list      集合
     * @param predicate 过滤器
     * @param separator 链接字符串
     * @return String
     * @see StringUtils#join(Iterable, String)
     */
    public static <T> String join(List<T> list, Predicate<T> predicate, String separator) {
        if (list == null || list.isEmpty()) {
            return StringUtils.EMPTY;
        }
        List<T> to = filter(list, predicate);
        return StringUtils.join(to, separator);
    }

    /**
     * 把list中对象的某个属性用separator链接起来
     *
     * @param list      集合
     * @param predicate 过滤器
     * @param function  转换器
     * @param separator 链接字符串
     * @return String
     * @see StringUtils#join(Iterable, String)
     */
    public static <T, R> String join(List<T> list, Predicate<T> predicate, Function<T, R> function, String separator) {
        if (list == null || list.isEmpty()) {
            return StringUtils.EMPTY;
        }
        List<R> to = filterTo(list, predicate, function);
        return StringUtils.join(to, separator);
    }

    /**
     * 从list中查找对象的某个属性是否在ks里
     * <p>
     * 如：list为用户列表，ks为用户id，现需要在用户列表(list)中找出所有ks里指定的id
     * </p>
     *
     * @param list   对象列表
     * @param ks     值列表
     * @param getKey 对的某个属性值
     * @return list
     */
    public static <T, K> List<T> in(List<T> list, List<K> ks, Function<T, K> getKey) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        if (ks == null || ks.isEmpty()) {
            return new ArrayList<>();
        }
        List<T> result = new ArrayList<>();
        for (T t : list) {
            K k = getKey.apply(t);
            if (k != null && ks.contains(k)) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * 从list中查找对象的某个属性不在ks里
     *
     * @param list   对象列表
     * @param ks     值列表
     * @param getKey 对的某个属性值
     * @return list
     */
    public static <T, K> List<T> notIn(List<T> list, List<K> ks, Function<T, K> getKey) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        // ks没有，则全部在
        if (ks == null || ks.isEmpty()) {
            return list;
        }
        List<T> result = new ArrayList<>();
        for (T t : list) {
            K k = getKey.apply(t);
            if (k == null || !ks.contains(k)) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * 对象中某个long属性值的总和
     *
     * @param list     列表
     * @param getValue 获取long值
     * @return 总和
     */
    public static <T> long longSum(List<T> list, Function<T, Long> getValue) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        long total = 0;
        for (T t : list) {
            Long value = getValue.apply(t);
            if (value != null) {
                total += value;
            }
        }
        return total;
    }

    /**
     * 对象中某个int属性值的总和
     *
     * @param list     列表
     * @param getValue 获取int值
     * @return 总和
     */
    public static <T> int intSum(List<T> list, Function<T, Integer> getValue) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        int total = 0;
        for (T t : list) {
            Integer value = getValue.apply(t);
            if (value != null) {
                total += value;
            }
        }
        return total;
    }

    /**
     * 从list中获取元素，会检查index是否越界
     *
     * @param list  列表
     * @param index index
     * @return T
     */
    public static <T> T safeGet(List<T> list, int index) {
        if (list == null || list.isEmpty() || list.size() <= index) {
            return null;
        }
        return list.get(index);
    }

    /**
     * 循环list
     *
     * @param list     列表
     * @param consumer 处理函数
     */
    public static <T> void safeForEach(List<T> list, Consumer<T> consumer) {
        if (list == null || list.isEmpty()) {
            return;
        }
        list.forEach(consumer);
    }

    /**
     * 把addList中的元素添加到srcList中，添加时会去掉srcLit中已有的元素
     *
     * @param srcList 原始列表
     * @param addList 添加的列表
     * @param equals  BiPredicate, 检查2个元素是否相等
     */
    public static <T> void mergeUnique(List<T> srcList, List<T> addList, BiPredicate<T, T> equals) {
        if (srcList == null || addList == null) {
            return;
        }
        List<T> notInList = new ArrayList<>(addList.size());
        for (T add : addList) {
            boolean contain = false;
            for (T src : srcList) {
                if (equals.test(src, add)) {
                    contain = true;
                    break;
                }
            }
            if (!contain) {
                notInList.add(add);
            }
        }
        srcList.addAll(notInList);
    }

    /**
     * 取出list中同index的元素，通过biConsumer消费，ts与ds的size必须一致
     *
     * @param ts         列表
     * @param ds         列表
     * @param biConsumer BiConsumer
     */
    public static <T, D> void biForEach(List<T> ts, List<D> ds, BiConsumer<T, D> biConsumer) {
        if (ts == null || ts.isEmpty() || ds == null || ds.isEmpty()) {
            return;
        }
        for (int i = 0; i < ts.size(); i++) {
            biConsumer.accept(ts.get(i), ds.get(i));
        }
    }

    /**
     * 取出list中同index的元素，通过biConsumer消费，ts/ds/os的size必须一致
     *
     * @param ts          列表
     * @param ds          列表
     * @param os          列表
     * @param triConsumer TriConsumer
     */
    public static <T, D, O> void triForEach(List<T> ts, List<D> ds, List<O> os, TriConsumer<T, D, O> triConsumer) {
        if (ts == null || ts.isEmpty() || ds == null || ds.isEmpty() || os == null || os.isEmpty()) {
            return;
        }
        for (int i = 0; i < ts.size(); i++) {
            triConsumer.accept(ts.get(i), ds.get(i), os.get(i));
        }
    }

    /**
     * 如果ts中某元素的key与ds中某元素的key相等，则调用consumer
     *
     * @param ts       列表
     * @param ds       列表
     * @param keyFunc  T的key
     * @param consumer TriConsumer
     */
    public static <T, K> void sameKeyConsume(List<T> ts, List<T> ds, Function<T, K> keyFunc, TriConsumer<K, T, T> consumer) {
        sameKeyConsume(ts, ds, keyFunc, keyFunc, consumer);
    }

    /**
     * 如果ts中某元素的key与ds中某元素的key相等，则调用consumer
     *
     * @param ts       列表
     * @param ds       列表
     * @param tKey     T的key
     * @param dKey     D的key
     * @param consumer TriConsumer
     */
    public static <T, D, K> void sameKeyConsume(List<T> ts, List<D> ds, Function<T, K> tKey, Function<D, K> dKey, TriConsumer<K, T, D> consumer) {
        if (ts == null || ts.isEmpty() || ds == null || ds.isEmpty()) {
            return;
        }
        for (T t : ts) {
            K tk = tKey.apply(t);
            if (tk == null) {
                continue;
            }
            for (D d : ds) {
                if (Objects.equals(tk, dKey.apply(d))) {
                    consumer.accept(tk, t, d);
                    break;
                }
            }
        }
    }

    /**
     * 把2个list中同index元素的key映射为map
     *
     * @param ts      列表
     * @param ds      列表
     * @param keyFunc T的key,D的key
     * @return key map
     */
    public static <T, TK> Map<TK, TK> toKeyMap(List<T> ts, List<T> ds, Function<T, TK> keyFunc) {
        return toKeyMap(ts, ds, keyFunc, keyFunc);
    }

    /**
     * 把2个list中同index元素的key映射为map
     *
     * @param ts   列表
     * @param ds   列表
     * @param tKey T的key
     * @param dKey D的key
     * @return key map
     */
    public static <T, D, TK, DK> Map<TK, DK> toKeyMap(List<T> ts, List<D> ds, Function<T, TK> tKey, Function<D, DK> dKey) {
        if (ts == null || ts.isEmpty() || ds == null || ds.isEmpty()) {
            return Maps.newMap();
        }
        assert ts.size() == ds.size();
        Map<TK, DK> map = Maps.newMap();
        for (int i = 0; i < ts.size(); i++) {
            map.put(tKey.apply(ts.get(i)), dKey.apply(ds.get(i)));
        }
        return map;
    }
}

