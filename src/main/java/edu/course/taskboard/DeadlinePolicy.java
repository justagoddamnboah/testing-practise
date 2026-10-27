package edu.course.taskboard;

import java.time.Clock;
import java.time.Instant;

public class DeadlinePolicy {
    private final Clock clock;
    
    public DeadlinePolicy(Clock clock) {
        this.clock = clock;
    }

    public boolean isOverdue(Instant deadline) {
        return deadline.isBefore(clock.instant());
    }
}