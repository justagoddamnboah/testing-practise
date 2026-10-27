package edu.course.library;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class BoundaryTest {
    @ParameterizedTest
    @CsvSource({"1,50", "2,100", "14,700"})
    void estimatesValidDays(int days, int expected) {
        assertThat(new EstimatePolicy().borrowed(days)).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 15, Integer.MAX_VALUE})
    void rejectsInvalidDays(int days) {
        assertThatThrownBy(() -> new EstimatePolicy().borrowed(days))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n"})
    void rejectsMissingName(String raw) {
        assertThatThrownBy(() -> new NamePolicy().normalize(raw))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void trimsOnlyOutsideSpaces() {
        assertThat(new NamePolicy().normalize("  John Doe  "))
                .isEqualTo("John Doe");
    }

    @Test
    void nameLengthBoundary() {
        assertThat(new NamePolicy().normalize("a".repeat(30))).hasSize(30);
        assertThatThrownBy(() -> new NamePolicy().normalize("a".repeat(31)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deadlineEqualToNowIsNotOverdue() {
        Instant now = Instant.parse("2026-10-03T12:00:00Z");
        DeadlinePolicy policy = new DeadlinePolicy(Clock.fixed(now, ZoneOffset.UTC));
        assertThat(policy.isOverdue(now)).isTrue();
        assertThat(policy.isOverdue(now.minusSeconds(1))).isTrue();
        assertThat(policy.isOverdue(now.plusSeconds(1))).isFalse();
    }
}