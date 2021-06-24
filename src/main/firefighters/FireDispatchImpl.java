package main.firefighters;

import main.api.api.City;
import main.api.api.CityNode;
import main.api.api.FireDispatch;
import main.api.api.Firefighter;
import main.api.exceptions.NoFireFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FireDispatchImpl implements FireDispatch {

    private final City city;

    private List<Firefighter> firefighters;
    private CityNode fireStationLocation;

    public FireDispatchImpl(City city) {
        this.city = city;
        this.fireStationLocation = city.getFireStation().getLocation();
    }

    @Override
    public void setFirefighters(int numFirefighters) {
        this.firefighters = new ArrayList<>(numFirefighters);
        for (int i = 0; i < numFirefighters; i++) {
            this.firefighters.add(new FirefighterImpl(this.fireStationLocation));
        }
    }

    @Override
    public List<Firefighter> getFirefighters() {
        return this.firefighters;
    }

    @Override
    public void dispatchFirefighers(CityNode... burningBuildings) throws NoFireFoundException {

        // Sort buildings by the distance from the firestation
        Arrays.sort(burningBuildings, (building1, building2) -> {
            int distanceBetweenFStoBuilding1 = this.distanceBetweenLocations(fireStationLocation, building1);
            int distanceBetweenFStoBuilding2 = this.distanceBetweenLocations(fireStationLocation, building2);
            if (distanceBetweenFStoBuilding1 < distanceBetweenFStoBuilding2) return -1;
            return 1;
        });

        // We use a greedy algorithm that minimizes distance of all the fire fighters
        // by sending the nearest firefighter to the first building whose x coordinate is
        // closest to the firestation.
        for (int i = 0; i < burningBuildings.length; i++) {
            FirefighterImpl closestFF = findClosestFireFighter(burningBuildings[i]);
            closestFF.setDistanceTravelled(closestFF.distanceTraveled() + distanceBetweenLocations(closestFF.getLocation(), burningBuildings[i]));
            closestFF.setLocation(burningBuildings[i]);
            this.city.getBuilding(burningBuildings[i]).extinguishFire();
        }
    }

    /**
     * Sends the nearest fire fighter to the burning building. The building is extinguished
     * and distance/location of firefighter is updated.
     *
     * @param burningBuilding the burning building city node
     * @throws NoFireFoundException
     */
    private FirefighterImpl findClosestFireFighter(CityNode burningBuilding) {
        FirefighterImpl closestFF = null;
        int leastDistance = -1;
        for (Firefighter firefighter : firefighters) {
            int ffDistance = distanceBetweenLocations(firefighter.getLocation(), burningBuilding);
            if (ffDistance < leastDistance || leastDistance == -1) {
                leastDistance = ffDistance;
                closestFF = (FirefighterImpl) firefighter;
            }
        }
        return closestFF;
    }

    /**
     * Sends the distance between two city coordinates.
     * @param sourceCityNode the source city node
     * @param destinationCityNode the destination city node
     */
    private int distanceBetweenLocations(CityNode sourceCityNode, CityNode destinationCityNode) {
        return (Math.abs(sourceCityNode.getX() - destinationCityNode.getX()) + Math.abs(sourceCityNode.getY() - destinationCityNode.getY()));
    }
}
