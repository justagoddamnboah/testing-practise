package edu.course.taskboard;

public class UrgencyPolicy {
    private final EstimatePolicy estimate = new EstimatePolicy();
    public int minutes(int points, boolean urgent) {
        return estimate.minutes(points) + (urgent ? 5 : 0);
    }

    public int zeroMinutes(int points, boolean urgent) {
        return 0;
    }
}