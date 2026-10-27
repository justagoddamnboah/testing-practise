package edu.course.taskboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class UrgencyPolicyTest {
    // @Test void urgentTaskAddsFiveMinutes() {
    //     assertEquals(20, new UrgencyPolicy().minutes(1, true));
    // }

    @Test void ZeroUrgentTaskAddsFiveMinutes() {
        assertEquals(20, new UrgencyPolicy().zeroMinutes(1, true));
    }

    @Test void ordinaryTaskKeepsEstimate() {
        assertEquals(15, new UrgencyPolicy().minutes(1, false));
    }
    @Test void urgencyDoesNotMakeInvalidPointsValid() {
        assertThrows(IllegalArgumentException.class, () -> new UrgencyPolicy().minutes(0, true));
    }
}