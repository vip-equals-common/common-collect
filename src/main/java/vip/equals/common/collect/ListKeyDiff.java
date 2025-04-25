package vip.equals.common.collect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 计算existingData中的key，与keys中的差异
 * <pre>
 * 如设置某个角色的权限值，页面传入了权限key的列表，需要从数据库中取出某角色所有原有的权限值，然后与key的列表进行比对，
 * 找出需要新增的及需要删除的，然后进行新增和删除
 * existingData -> 原先某角色下的所有权限
 * keys -> 页面传入了权限key的列表
 * 新增的keys {@link #newKeys}
 * 删除的数据 {@link #notInExistingData}
 * </pre>
 *
 * @author HJ
 * @since 1.0.0
 */
public class ListKeyDiff<E, K> {

    private final List<E> existingData;
    private final List<K> keys;
    private final Function<E, K> keyFunction;

    private final List<E> notInExistingData;
    private final List<K> newKeys;

    public ListKeyDiff(List<E> existingData, List<K> keys, Function<E, K> keyFunction) {
        this.existingData = existingData != null ? existingData : Collections.emptyList();
        this.keys = keys != null ? keys : Collections.emptyList();
        this.keyFunction = keyFunction;
        this.notInExistingData = new ArrayList<>();
        this.newKeys = new ArrayList<>();
        calculate();
    }

    private void calculate() {
        List<K> oldKeys = new ArrayList<>();
        for (E e : existingData) {
            K k = keyFunction.apply(e);
            oldKeys.add(k);
            if (!keys.contains(k)) {
                notInExistingData.add(e);
            }
        }
        for (K key : keys) {
            if (!oldKeys.contains(key)) {
                newKeys.add(key);
            }
        }
    }

    /**
     * 需要删除的数据
     *
     * @return true > 0, false = 0
     */
    public boolean notInExistingDataIsNotEmpty() {
        return !notInExistingData.isEmpty();
    }

    /**
     * 需要删除的数据
     *
     * @return List
     */
    public List<E> notInExistingData() {
        return notInExistingData;
    }

    /**
     * 需要删除数据的keys
     *
     * @return List
     */
    public List<K> notInExistingDataKeys() {
        return notInExistingData(keyFunction);
    }

    /**
     * 需要删除的数据
     *
     * @return List
     */
    public <R> List<R> notInExistingData(Function<E, R> converter) {
        return Lists.to(notInExistingData, converter);
    }

    /**
     * 需要删除的数据
     *
     * @return List
     */
    public List<E> notInExistingData(Consumer<E> consumer) {
        for (E e : notInExistingData) {
            consumer.accept(e);
        }
        return notInExistingData;
    }

    /**
     * 新增数据的key
     *
     * @return true > 0, false = 0
     */
    public boolean newKeysIsNotEmpty() {
        return !newKeys.isEmpty();
    }

    /**
     * 新增数据的key
     *
     * @return List
     */
    public List<K> newKeys() {
        return newKeys;
    }

    /**
     * 新增数据
     *
     * @return List
     */
    public <R> List<R> newKeys(Function<K, R> converter) {
        return Lists.to(newKeys, converter);
    }
}
