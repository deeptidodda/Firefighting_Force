package main.firefighters;

import main.api.api.CityNode;
import main.api.api.Firefighter;

public class FirefighterImpl implements Firefighter {

    private CityNode location;
    private int distanceTravelled;

    public FirefighterImpl(CityNode location) {
        this.location = location;
    }

    public void setLocation(CityNode location) {
        this.location = location;
    }

    public void setDistanceTravelled(int distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    @Override
    public CityNode getLocation() {
        return this.location;
    }

    @Override
    public int distanceTraveled() {
        return this.distanceTravelled;
    }
}
