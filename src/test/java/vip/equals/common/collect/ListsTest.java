package vip.equals.common.collect;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListsTest {

    @Test
    void to() {
        ArrayList<Integer> integers = Lists.newList(1, 2, 3);
        List<Integer> withNull = Lists.to(integers, (i) -> {
            if (i % 2 == 0) {
                return i;
            }
            return null;
        });
        assertEquals(withNull.size(), 1);
        assertEquals(withNull.get(0), 2);
    }

    @Test
    void toWithNull() {
        ArrayList<Integer> integers = Lists.newList(1, 2, 3);
        List<Integer> withNull = Lists.toWithNull(integers, (i) -> {
            if (i % 2 == 0) {
                return i;
            }
            return null;
        });
        assertEquals(withNull.size(), 3);
        assertNull(withNull.get(0));
        assertEquals(withNull.get(1), 2);
        assertNull(withNull.get(2));
        System.out.println(withNull);
    }
}