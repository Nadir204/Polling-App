package com.PollingSystem.PollWeb.repo;




import com.PollingSystem.PollWeb.Entity.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface PollRepo extends JpaRepository<Poll, Long> {

    // Query to get polls ordered by id desc, with pagination (to get latest) without it latest ta show kore na
    @Query("SELECT p FROM Poll p ORDER BY p.id DESC")
    List<Poll> findLatestPoll(Pageable pageable);
}