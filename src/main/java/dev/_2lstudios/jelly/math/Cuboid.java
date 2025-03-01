package dev._2lstudios.jelly.math;

import org.bukkit.Location;

public class Cuboid {
    private Vector3 firstPoint;
    private Vector3 secondPoint;

    public Cuboid(Vector3 firstPoint, Vector3 secondPoint) {
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
    }

    public boolean isBetween(double xP, double zP) {
        double x1 = this.firstPoint.getX();
        double z1 = this.firstPoint.getZ();

        double x2 = this.secondPoint.getX();
        double z2 = this.secondPoint.getZ();

        return ((x1 < xP && xP < x2) || (x1 > xP && xP > x2)) && ((z1 < zP && zP < z2) || (z1 > zP && zP > z2));
    }

    public boolean isBetween(Location target) {
        double xP = target.getX();
        double zP = target.getZ();

        return this.isBetween(xP, zP);
    }

    public boolean isBetween(Vector3 target) {
        double xP = target.getX();
        double zP = target.getZ();

        return this.isBetween(xP, zP);
    }

    public Vector3 getFirstPoint() {
        return this.firstPoint;
    }

    public Vector3 getSecondPoint() {
        return this.secondPoint;
    }
}
