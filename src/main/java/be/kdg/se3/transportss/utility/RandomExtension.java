package be.kdg.se3.transportss.utility;

import java.util.List;
import java.util.Random;

/**
 * Extension class containing simple extension on the random library
 *
 * Created by Joren Van de Vondel on 11/3/2016.
 */
public class RandomExtension {
    private final static Random RANDOMNO = new Random();

    public static <T> T getRandomFromList(List<T> list){
        return list.get(RANDOMNO.nextInt(list.size()));
    }
}
