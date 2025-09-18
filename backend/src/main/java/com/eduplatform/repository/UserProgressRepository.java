package com.eduplatform.repository;

import com.eduplatform.model.Course;
import com.eduplatform.model.User;
import com.eduplatform.model.UserProgress;
import com.eduplatform.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProgressRepository extends BaseRepository<UserProgress> {

    Optional<UserProgress> findByUserAndCourse(User user, Course course);

    List<UserProgress> findByUser(User user);
}