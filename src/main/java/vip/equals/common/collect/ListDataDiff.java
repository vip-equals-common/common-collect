package vip.equals.common.collect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.*;

/**
 * 比较2个list的内容，用于批量插入数据时，与原先已有数据进行比较，找出需要新增/更新/删除的数据
 * <pre>
 * 删除更新新增模式:
 * 删除数据 {@link #existingNotInSaveData()}
 * 更新数据 {@link #changedInSaveData()} {@link #changedInExistingData()}
 *    可使用 {@link #changedData(BiConsumer)} {@link #changedData(BiFunction)} 来处理数据
 * 新增数据 {@link #increaseData()}
 * 删除新增模式:
 * 删除数据 {@link #changedOrNotInSaveData()}
 * 新增数据 {@link #increaseOrChangedInSaveData()}
 * </pre>
 *
 * @author HJ
 * @since 1.0.0
 */
public class ListDataDiff<E, S, K> {

    private final List<E> existingData;
    private final List<S> saveData;
    private final Function<E, K> existingDataKey;
    private final Function<S, K> saveDataKey;
    private final BiPredicate<E, S> compare;

    /**
     * 新增的
     */
    private final List<S> increaseData;
    /**
     * 老的数据改变的
     */
    private final List<E> changedInExistingData;
    /**
     * 新的数据改变的
     */
    private final List<S> changedInSaveData;
    /**
     * 数据没有改变的
     */
    private final List<E> noChangeData;
    /**
     * 不在老的数据列表中的
     */
    private final List<E> existingNotInSaveData;

    /**
     * @param existingData    现有数据，一般为数据库中现有数据
     * @param saveData        要保存的数据
     * @param existingDataKey 现有数据的key
     * @param saveDataKey     保存数据的key
     * @param compare         比较数据是否相等
     */
    public ListDataDiff(List<E> existingData, List<S> saveData, Function<E, K> existingDataKey, Function<S, K> saveDataKey, BiPredicate<E, S> compare) {
        this.existingData = existingData != null ? existingData : Collections.emptyList();
        this.saveData = saveData != null ? saveData : Collections.emptyList();
        this.existingDataKey = existingDataKey;
        this.saveDataKey = saveDataKey;
        this.compare = compare;
        increaseData = new ArrayList<>();
        changedInExistingData = new ArrayList<>();
        changedInSaveData = new ArrayList<>();
        noChangeData = new ArrayList<>();
        existingNotInSaveData = new ArrayList<>();
        changed();
        save();
    }

    private void changed() {
        for (E e : existingData) {
            K oldKey = existingDataKey.apply(e);
            S find = null;
            for (S s : saveData) {
                if (Objects.equals(oldKey, saveDataKey.apply(s))) {
                    find = s;
                    break;
                }
            }
            // 找到key相同的
            if (find != null) {
                if (compare.test(e, find)) {
                    noChangeData.add(e);
                } else {
                    changedInExistingData.add(e);
                    changedInSaveData.add(find);
                }
            } else {
                existingNotInSaveData.add(e);
            }
        }
    }

    private void save() {
        for (S s : saveData) {
            K newKey = saveDataKey.apply(s);
            boolean find = false;
            for (E e : existingData) {
                if (Objects.equals(newKey, existingDataKey.apply(e))) {
                    find = true;
                    break;
                }
            }
            if (!find) {
                increaseData.add(s);
            }
        }
    }

    /**
     * 新增的数据，不为空; 删除更新新增模式 中的新增列表
     *
     * @return List<S>
     */
    public List<S> increaseData() {
        return increaseData;
    }

    /**
     * 新增的数据, size() > 0
     *
     * @return true > 0, false = 0
     */
    public boolean increaseDataIsNotEmpty() {
        return !increaseData.isEmpty();
    }

    /**
     * 转换新增的数据; 删除更新新增模式 中的新增列表
     *
     * @param converter 转换器
     * @return List<R>
     */
    public <R> List<R> increaseData(Function<S, R> converter) {
        return Lists.to(increaseData, converter);
    }

    /**
     * 处理新增的数据; 删除更新新增模式 中的新增列表
     *
     * @param consumer consumer
     */
    public List<S> increaseData(Consumer<S> consumer) {
        for (S s : increaseData) {
            consumer.accept(s);
        }
        return increaseData;
    }

    /**
     * 现有数据中，改变的，数量与getChangedInSaveData()一致; 删除更新新增模式 中的更新列表
     *
     * @return List<E>
     * @see #changedInSaveData()
     */
    public List<E> changedInExistingData() {
        return changedInExistingData;
    }

    /**
     * 新数据中，改变的，对应现有数据中，改变的，getChangedInExistingData()一致; 删除更新新增模式 中的更新列表
     *
     * @return List<S>
     * @see #changedInExistingData()
     */
    public List<S> changedInSaveData() {
        return changedInSaveData;
    }

    /**
     * 现有数据中,改变的数据数量, size > 0
     *
     * @return true > 0, false = 0
     */
    public boolean changedInExistingDataIsNotEmpty() {
        return !changedInExistingData.isEmpty();
    }

    /**
     * 合并改变的数据，比如把新数据的属性设置到原始数据对象中; 删除更新新增模式 中的更新列表
     *
     * @param function 处理器
     * @return List<R>
     */
    public <R> List<R> changedData(BiFunction<E, S, R> function) {
        List<R> rs = new ArrayList<>(changedInSaveData.size());
        for (int i = 0; i < changedInExistingData.size(); i++) {
            rs.add(function.apply(changedInExistingData.get(i), changedInSaveData.get(i)));
        }
        return rs;
    }

    /**
     * 处理改变的数据; 删除更新新增模式 中的更新列表
     *
     * @param consumer 处理器
     */
    public void changedData(BiConsumer<E, S> consumer) {
        for (int i = 0; i < changedInExistingData.size(); i++) {
            consumer.accept(changedInExistingData.get(i), changedInSaveData.get(i));
        }
    }

    /**
     * 没有变化的数据
     *
     * @return List<E>
     */
    public List<E> noChangeData() {
        return noChangeData;
    }

    /**
     * 不在老的数据列表中的; 删除更新新增模式 中的删除列表
     *
     * @return List<E>
     */
    public List<E> existingNotInSaveData() {
        return existingNotInSaveData;
    }

    /**
     * 不在老的数据列表中的, size > 0
     *
     * @return true > 0, false = 0
     */
    public boolean existingNotInSaveDataIsNotEmpty() {
        return !existingNotInSaveData.isEmpty();
    }

    /**
     * 转换不在老的数据列表中的数据; 删除更新新增模式 中的删除列表
     *
     * @param converter 转换器
     * @return List<R>
     */
    public <R> List<R> existingNotInSaveData(Function<E, R> converter) {
        return Lists.to(existingNotInSaveData, converter);
    }

    /**
     * 老的数据改变的 + 不在老的数据列表中的, size > 0; 删除新增模式 中的删除列表
     *
     * @return true > 0, false = 0
     */
    public boolean changedOrNotInSaveDataIsNotEmpty() {
        return !(changedInExistingData.isEmpty() && existingNotInSaveData.isEmpty());
    }

    /**
     * 老的数据改变的 + 不在老的数据列表中的; 删除新增模式 中的删除列表
     *
     * @return List<E>
     */
    public List<E> changedOrNotInSaveData() {
        List<E> list = new ArrayList<>();
        list.addAll(changedInExistingData);
        list.addAll(existingNotInSaveData);
        return list;
    }

    /**
     * 老的数据改变的 + 不在老的数据列表中的; 删除新增模式 中的删除列表
     *
     * @return List<R>
     */
    public <R> List<R> changedOrNotInSaveData(Function<E, R> converter) {
        List<R> list = new ArrayList<>();
        list.addAll(Lists.to(changedInExistingData, converter));
        list.addAll(Lists.to(existingNotInSaveData, converter));
        return list;
    }

    /**
     * 新增的 + 新的数据改变的, size > 0; 删除新增模式 中的新增列表
     *
     * @return true > 0, false = 0
     */
    public boolean increaseOrChangedInSaveDataIsNotEmpty() {
        return !(increaseData.isEmpty() && changedInSaveData.isEmpty());
    }

    /**
     * 新增的 + 新的数据改变的; 删除新增模式 中的新增列表
     *
     * @return List<R>
     */
    public List<S> increaseOrChangedInSaveData() {
        List<S> list = new ArrayList<>();
        list.addAll(increaseData);
        list.addAll(changedInSaveData);
        return list;
    }

    /**
     * 新增的 + 新的数据改变的; 删除新增模式 中的新增列表
     *
     * @return List<R>
     */
    public <R> List<R> increaseOrChangedInSaveData(Function<S, R> converter) {
        List<R> list = new ArrayList<>();
        list.addAll(Lists.to(increaseData, converter));
        list.addAll(Lists.to(changedInSaveData, converter));
        return list;
    }

    /**
     * 新增的 + 新的数据改变的; 删除新增模式 中的新增列表
     *
     * @return List<R>
     */
    public List<S> increaseOrChangedInSaveData(Consumer<S> consumer) {
        List<S> list = increaseOrChangedInSaveData();
        for (S s : list) {
            consumer.accept(s);
        }
        return list;
    }
}
