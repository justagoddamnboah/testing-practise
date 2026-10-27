package edu.course.library;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class EstimatePolicyTest {
    @Test
    void oneDayCostsFifty() {
        EstimatePolicy policy = new EstimatePolicy();
        int actual = policy.borrowed(1);
        assertEquals(50, actual);
    }

    @Test
    void zeroDaysAreRejected() {
        EstimatePolicy policy = new EstimatePolicy();
        IllegalArgumentException error = assertThrows(
            IllegalArgumentException.class, () -> policy.borrowed(0)
        );
        assertEquals(
            "you can borrow a book for 1 day at the least and 14 days at the most",
                    error.getMessage()
                );
    }
}