package com.arkflame.squidgame.player;

import org.bukkit.Location;

import com.arkflame.squidgame.jelly.math.Cuboid;
import com.arkflame.squidgame.jelly.math.Vector3;

public class PlayerWand {

    private Vector3 firstPoint;
    private Vector3 secondPoint;

    public Vector3 getFirstPoint() {
        return this.firstPoint;
    }

    public Vector3 getSecondPoint() {
        return this.secondPoint;
    }

    public Cuboid getCuboid() {
        return new Cuboid(this.firstPoint, this.secondPoint);
    }

    public void setFirstPoint(Vector3 vector) {
        this.firstPoint = vector;
    }

    public void setSecondPoint(Vector3 vector) {
        this.secondPoint = vector;
    }

    public void setFirstPoint(Location loc) {
        this.firstPoint = new Vector3(loc.getX(), loc.getY(), loc.getZ());
    }

    public void setSecondPoint(Location loc) {
        this.secondPoint = new Vector3(loc.getX(), loc.getY(), loc.getZ());
    }

    public boolean isComplete() {
        return this.firstPoint != null && this.secondPoint != null;
    }
}
