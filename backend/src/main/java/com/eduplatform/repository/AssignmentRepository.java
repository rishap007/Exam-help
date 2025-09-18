package com.eduplatform.repository;

import com.eduplatform.model.Assignment;
import com.eduplatform.model.Lesson;
import com.eduplatform.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AssignmentRepository extends BaseRepository<Assignment> {

    List<Assignment> findByLessonOrderByDueDateAsc(Lesson lesson);

    @Query("SELECT a FROM Assignment a WHERE a.dueDate BETWEEN :now AND :futureDate")
    List<Assignment> findAssignmentsDueSoon(@Param("now") LocalDateTime now,
                                            @Param("futureDate") LocalDateTime futureDate);
    
    long countByLesson(Lesson lesson);
}