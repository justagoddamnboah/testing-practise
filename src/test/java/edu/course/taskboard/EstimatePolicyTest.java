package edu.course.taskboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class EstimatePolicyTest {
    @Test
    void onePointTakesFifteenMinutes() {
        EstimatePolicy policy = new EstimatePolicy();
        int actual = policy.minutes(1);
        assertEquals(15, actual);
    }

    @Test
    void threePointsTakesFortyFiveMinutes() {
        EstimatePolicy policy = new EstimatePolicy();
        int actual = policy.minutes(3);
        assertEquals(45, actual);
    }

    @Test
    IllegalArgumentException tenPointsAreRejected() {
        EstimatePolicy policy = new EstimatePolicy();
        return assertThrows(IllegalArgumentException.class, () -> policy.minutes(10));
    }

    @Test
    void checkErrorMessage() {
        IllegalArgumentException error = tenPointsAreRejected();
        assertEquals("points must be between 1 and 8", error.getMessage());
    }

    @Test
    void eightPointsTakesOneHundredTwenty() {
        EstimatePolicy policy = new EstimatePolicy();
        int actual = policy.minutes(8);
        assertEquals(120, actual);
    }

    @Test
    void negativePointsAreRejected() {
        EstimatePolicy policy = new EstimatePolicy();
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class, () -> policy.minutes(-1));
        assertEquals("points must be between 1 and 8", error.getMessage());
    }

    @Test
    void zeroPointsAreRejected() {
        EstimatePolicy policy = new EstimatePolicy();
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class, () -> policy.minutes(0));
        assertEquals("points must be between 1 and 8", error.getMessage());
    }
}