package com.ebos.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ebos.tables.BookIssue;

@Repository
public interface BookIssueRepository extends JpaRepository<BookIssue, Long>{

	List<BookIssue> findByDueDateBefore(LocalDate date);

	List<BookIssue> findByIssueDateAfter(LocalDate oneWeekAgo);

	List<BookIssue> findByDueFeesGreaterThan(double threshold);
	
	 List<BookIssue> findByUserId(Long userId);
}
