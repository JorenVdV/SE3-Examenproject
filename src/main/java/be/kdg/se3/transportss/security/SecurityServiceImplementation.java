package be.kdg.se3.transportss.security;

import be.kdg.se3.transportss.simulator.service.SecurityService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Joren Van de Vondel on 11/6/2016.
 */
public class SecurityServiceImplementation implements SecurityService{
    private Map<Integer, List<Integer>> checklist = new HashMap<>();

    public void addCheck(int conveyorID, int luggageID){
        if (checklist.containsKey(conveyorID))checklist.get(conveyorID).add(luggageID);
        else checklist.putIfAbsent(conveyorID, Arrays.asList(luggageID));
    }

    @Override
    public boolean isRemoved(int conveyorID, int luggageID) {
        return checklist.containsKey(conveyorID)
                 && checklist.get(conveyorID).contains(luggageID);
    }
}
