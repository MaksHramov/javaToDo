package com.example.javatodo.service;

import com.example.javatodo.dao.WorkDao;
import com.example.javatodo.model.Work;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkServiceTest {

    @Mock
    private WorkDao workDao;

    @InjectMocks
    private WorkService workService;

    @Test
    void filtersByAssigneeAndCategory() {
        when(workDao.findAll()).thenReturn(List.of(
                work(1, "Иван", "Работа"),
                work(2, "Оля", "Учёба")
        ));

        List<Work> result = workService.list("иван", "Работа", null, "id");

        assertEquals(1, result.size());
        assertEquals("Иван", result.get(0).assignee());
    }

    @Test
    void sortsByDueDate() {
        Work earlier = new Work(2, "B", "d", "Оля", "Учёба",
                LocalDate.now().minusDays(1), LocalDateTime.now(), "NEW");
        Work laterDue = new Work(3, "C", "d", "Иван", "Работа",
                LocalDate.now().plusDays(5), LocalDateTime.now(), "NEW");
        when(workDao.findAll()).thenReturn(List.of(laterDue, earlier));

        List<Work> result = workService.list(null, null, null, "due");

        assertEquals(earlier.id(), result.get(0).id());
        assertEquals(laterDue.id(), result.get(1).id());
    }

    @Test
    void createAndMoveGoToDao() {
        LocalDate due = LocalDate.now().plusDays(1);
        workService.createWork("Т", "О", "Иван", "Работа", due);
        verify(workDao).save("Т", "О", "Иван", "Работа", due);

        workService.updateCategory(5, "Личное");
        verify(workDao).updateCategory(5, "Личное");

        workService.updateStatus(5, "DONE");
        verify(workDao).updateStatus(5, "DONE");
    }

    @Test
    void overdueWhenDueDatePassedAndNotDone() {
        Work work = new Work(1, "Т", "О", "Иван", "Работа",
                LocalDate.now().minusDays(1), LocalDateTime.now(), "NEW");
        assertTrue(work.isOverdue());
    }

    private static Work work(long id, String assignee, String category) {
        return new Work(id, "t", "d", assignee, category,
                LocalDate.now(), LocalDateTime.now(), "NEW");
    }
}
