package be.kdg.se3.transportss.generation.entity;

import be.kdg.se3.transportss.generation.GeneratorServiceGeneration;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to define frequency schema used by {@link GeneratorServiceGeneration}
 *
 * Created by Joren Van de Vondel on 11/1/2016.
 */
public class FrequencySchema {
    // Map contains start hour of frequency, end hour is next key value
    Map<Integer, Integer> frequence;

    public FrequencySchema(Map<Integer, Integer> frequence) {
        this.frequence = frequence;
        if(this.frequence.keySet().stream().mapToInt(e -> e).min().getAsInt() != 0)
            frequence.put(0,
                    this.frequence.get(this.frequence.keySet().stream().mapToInt(e -> e).min().getAsInt()));
    }

    /**
     * Constructor that takes a single integer as the global frequency
     * @param integer global frequency
     */
    public FrequencySchema(Integer integer){
        frequence = new HashMap<>();
        frequence.put(new Integer(0),integer);
    }

    /**
     * @param hour current hour
     * @return frequency for the given hour
     */
    public int getFrequency(int hour){
        return frequence.get(frequence.keySet().stream().mapToInt(k -> k).filter(k -> k<=hour).max().getAsInt());
    }
}
