package edu.course.taskboard;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock TaskStore store;
    private final Instant now = Instant.parse("2026-10-03T12:00:00Z");
    private TaskService service() {
        return new TaskService(store, Clock.fixed(now, ZoneOffset.UTC));
    }

    @Test
    void savesNormalizedTaskWithOwnerAndTime() {
        when(store.save(any(TaskItem.class))).thenAnswer(call -> call.getArgument(0));
        TaskItem actual = service().create("  Learn Java  ", "alice");
        ArgumentCaptor<TaskItem> captor = ArgumentCaptor.forClass(TaskItem.class);
        verify(store).save(captor.capture());
        assertThat(captor.getValue().title()).isEqualTo("Learn Java");
        assertThat(actual.owner()).isEqualTo("alice");
        assertThat(actual.createdAt()).isEqualTo(now);
        assertThat(actual.id()).isNotNull();
    }

    @Test
    void savesNormalizedTaskWithOwnerAndTime_positive() {
        when(store.save(any(TaskItem.class))).thenAnswer(call -> call.getArgument(0));
        service().create(" Review API ", "alice");

        ArgumentCaptor<TaskItem> captor = ArgumentCaptor.forClass(TaskItem.class);
        verify(store).save(captor.capture());

        assertThat(captor.getValue().title()).isEqualTo("Review API");
        assertThat(captor.getValue().owner()).isEqualTo("alice");
    }

    @Test
    void duplicateIsNotSaved_normalizedTitle() {
        when(store.exists("alice", "Review API")).thenReturn(true);
        assertThatThrownBy(() -> service().create("  Review API  ", "alice"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("duplicate title");
        verify(store).exists("alice", "Review API");
        verify(store, never()).save(any());
    }

    @Test
    void NullTitleTask() {
        assertThatThrownBy(() -> service().create(null, "alice"))
                .isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(store);
    }

    @Test
    void createdAtComesFromInjectedClock() {
        Instant fixed = Instant.parse("2026-10-03T09:00:00Z");
        TaskService svc = new TaskService(store, Clock.fixed(fixed, ZoneOffset.UTC));

        when(store.save(any(TaskItem.class))).thenAnswer(call -> call.getArgument(0));
        svc.create("Review API", "alice");

        ArgumentCaptor<TaskItem> captor = ArgumentCaptor.forClass(TaskItem.class);
        verify(store).save(captor.capture());
        assertThat(captor.getValue().createdAt())
                .isEqualTo(Instant.parse("2026-10-03T09:00:00Z"));
    }

    @Test
    void invalidTitleDoesNotTouchRepository() {
        assertThatThrownBy(() -> service().create(" ", "alice"))
                .isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(store);
    }

    @Test
    void duplicateIsNotSaved() {
        when(store.exists("alice", "Learn Java")).thenReturn(true);
        assertThatThrownBy(() -> service().create("Learn Java", "alice"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("duplicate title");
        verify(store, never()).save(any());
    }
}