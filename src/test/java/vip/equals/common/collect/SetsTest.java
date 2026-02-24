package vip.equals.common.collect;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author HJ
 * @version V1.1.0
 */
class SetsTest {

    @Test
    void integers() {
        List<Item> list = new ArrayList<>();
        list.add(new Item("1", 1, 1));
        list.add(new Item("2", 2, 3));
        Set<Integer> integers = Sets.integers(list, Item::getCreatorId, Item::getUpdaterId);
        assertEquals(3, integers.size());
        assertTrue(integers.contains(1));
        assertTrue(integers.contains(2));
        assertTrue(integers.contains(3));
    }


    public static class Item {
        private String name;
        private Integer creatorId;
        private Integer updaterId;

        public Item(String name, Integer creatorId, Integer updaterId) {
            this.name = name;
            this.creatorId = creatorId;
            this.updaterId = updaterId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(Integer creatorId) {
            this.creatorId = creatorId;
        }

        public Integer getUpdaterId() {
            return updaterId;
        }

        public void setUpdaterId(Integer updaterId) {
            this.updaterId = updaterId;
        }
    }
}