package org.example.repisitory;

import java.util.Random;

public interface GeneralRepository {
     static Long generateID() {
        return new Random().nextLong(9000) + 1000;
    }
}
