package org.protelis.vm.impl;

public class P2D {
    private final Double x;
    private final Double y;

    public P2D(final Double x, final Double y) {
        this.x = x;
        this.y = y;
    }
    
    public Double getX() {
        return x;
    }
    
    public Double getY() {
        return y;
    }
    
    public static Double getDistanceTo(P2D p) {
        return Math.sqrt(Math.pow(p.getX(), 2) + Math.pow(p.getX(), 2));
    }
}
