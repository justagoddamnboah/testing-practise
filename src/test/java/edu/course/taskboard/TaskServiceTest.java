package edu.course.taskboard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void invalidTitleDoesNotTouchRepository() {
        assertThatThrownBy(() -> service().create(" ", "alice"))
                .isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(store);
    }

    @Test
    void duplicateIsNotSaved() {
        when(store.exists("alice", "Learn Java")).thenReturn(true);
        assertThatThrownBy(() -> service().create("Learn Java", "alice"))
                .isInstanceOf(IllegalStateException.class).hasMessage("duplicate title");
        verify(store, never()).save(any());
    }
}