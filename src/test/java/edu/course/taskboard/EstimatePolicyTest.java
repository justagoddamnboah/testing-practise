package edu.course.taskboard;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EstimatePolicyTest {
    @Test
    void onePointTakesFifteenMinutes() {
        EstimatePolicy policy = new EstimatePolicy();
        int actual = policy.minutes(1);
        assertEquals(15, actual);
    }

    @Test
    void zeroPointsAreRejected() {
        EstimatePolicy policy = new EstimatePolicy();
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class, () -> policy.minutes(0));
        assertEquals("points must be between 1 and 8", error.getMessage());
    }
}