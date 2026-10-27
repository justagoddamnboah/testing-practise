package edu.course.taskboard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.time.*;
import static org.assertj.core.api.Assertions.*;

class BoundaryTest {
    @ParameterizedTest
    @CsvSource({"1,15", "2,30", "8,120"})
    void estimatesValidPoints(int points, int expected) {
        assertThat(new EstimatePolicy().minutes(points)).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 9, Integer.MAX_VALUE})
    void rejectsInvalidPoints(int points) {
        assertThatThrownBy(() -> new EstimatePolicy().minutes(points))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void rejectsMissingTitle(String raw) {
        assertThatThrownBy(() -> new TitlePolicy().normalize(raw))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void trimsOnlyOutsideSpaces() {
        assertThat(new TitlePolicy().normalize("  Learn Java  "))
                .isEqualTo("Learn Java");
    }

    @Test
    void titleLengthBoundary() {
        assertThat(new TitlePolicy().normalize("a".repeat(80))).hasSize(80);
        assertThatThrownBy(() -> new TitlePolicy().normalize("a".repeat(81)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deadlineEqualToNowIsNotOverdue() {
        Instant now = Instant.parse("2026-10-03T12:00:00Z");
        DeadlinePolicy policy = new DeadlinePolicy(Clock.fixed(now, ZoneOffset.UTC));
        assertThat(policy.isOverdue(now)).isFalse();
        assertThat(policy.isOverdue(now.minusSeconds(1))).isTrue();
        assertThat(policy.isOverdue(now.plusSeconds(1))).isFalse();
    }
}