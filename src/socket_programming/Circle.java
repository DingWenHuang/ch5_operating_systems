package socket_programming;

import java.io.Serializable;

public class Circle implements Serializable {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return this.radius;
    }
}
