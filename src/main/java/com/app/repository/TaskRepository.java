package com.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.modal.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
	
	Page<Task> findByIsAssignAndIsDeleted(boolean isAssign, boolean isDeleted, Pageable pageable);
    Page<Task> findByIsAssignAndIsCompletedAndIsDeleted(boolean isAssign, boolean isCompleted, boolean isDeleted, Pageable pageable);
    Page<Task> findByUsers_IdAndIsAssignAndIsDeleted(Long userId, boolean isAssign, boolean isDeleted, Pageable pageable);
    Page<Task> findByUsers_IdAndIsAssignAndIsCompletedAndIsDeleted(Long userId, boolean isAssign, boolean isCompleted, boolean isDeleted, Pageable pageable);
	Page<Task> findByIsAssignAndIsCompletedAndIsDeletedAndStatus(boolean isAssign, boolean isCompleted,
			boolean isDeleted, String status, Pageable pageable);
	Page<Task> findByIsAssignAndIsCompletedAndIsDeletedAndRequirementType(boolean isAssign, boolean isCompleted,
					boolean isDeleted, String requirementType, Pageable pageable);
	Page<Task> findByUsers_IdAndIsAssignAndIsCompletedAndIsDeletedAndRequirementType(Long userId, boolean isAssign, boolean isCompleted,
			boolean isDeleted, String requirementType, Pageable pageable);
	Page<Task> findByIsAssignAndIsCompletedAndIsDeletedAndRequirementTypeAndStatus(boolean isAssign, boolean isCompleted,
			boolean isDeleted,String requirementType, String status, Pageable pageable);
	
	Page<Task> findByUsers_IdAndIsAssignAndIsCompletedAndIsDeletedAndStatus(Long userId, boolean isAssign, boolean isCompleted,
			boolean isDeleted, String status, Pageable pageable);

	Page<Task> findByUsers_IdAndIsAssignAndIsCompletedAndIsDeletedAndRequirementTypeAndStatus(Long userId, boolean isAssign, boolean isCompleted,
			boolean isDeleted,String disposition, String status, Pageable pageable);
	boolean existsByClientEmail(String clientEmail);
	
	Page<Task> findBySourceAndRequirementTypeAndStatusAndIsDeletedAndIsAssignFalse(String source,String requirementType, String status, boolean isDeleted, Pageable pageable);
	Page<Task> findBySourceAndRequirementTypeAndIsDeletedAndIsAssignFalse(String source,String requirementType, boolean isDeleted, Pageable pageable);
	Page<Task> findBySourceAndStatusAndIsDeletedAndIsAssignFalse(String source,String status, boolean isDeleted, Pageable pageable);
	
	Page<Task> findByUsers_IdAndRequirementTypeAndStatusAndIsDeleted(Long userId, String requirementType, String status, boolean isDeleted, Pageable pageable);
	
	Page<Task> findByUsers_IdAndSourceAndRequirementTypeAndStatusAndIsDeleted(Long userId, String source, String requirementType , String status, boolean isDeleted, Pageable pageable);
	Page<Task> findByUsers_IdAndSourceAndRequirementTypeAndIsDeleted(Long userId, String source,String requirementType, boolean isDeleted, Pageable pageable);
	Page<Task> findByUsers_IdAndSourceAndStatusAndIsDeleted(Long userId, String source,String status, boolean isDeleted, Pageable pageable);
	Page<Task> findByUsers_IdAndStatusAndIsDeleted(Long userId,String status, boolean isDeleted, Pageable pageable);
	
	
	Page<Task> findBySourceAndRequirementTypeAndStatusAndIsDeleted(String source,String requirementType, String status, boolean isDeleted, Pageable pageable);
	Page<Task> findBySourceAndRequirementTypeAndIsDeleted(String source,String requirementType, boolean isDeleted, Pageable pageable);
	Page<Task> findBySourceAndStatusAndIsDeleted(String source,String status, boolean isDeleted, Pageable pageable);
	Page<Task> findByRequirementTypeAndStatusAndIsDeleted(String requirementType, String status, boolean isDeleted, Pageable pageable);
	
	Page<Task> findByRequirementTypeAndIsDeleted(String requirementType, boolean isDeleted, Pageable pageable);
	Page<Task> findBySourceAndIsDeleted(String source, boolean isDeleted, Pageable pageable);
	Page<Task> findByStatusAndIsDeleted(String status, boolean isDeleted, Pageable pageable);
	Page<Task> findByIsDeletedFalse(Pageable pageable);
	List<Task> findByNextFollowUpDate(String nextFollowUpDate);
}
