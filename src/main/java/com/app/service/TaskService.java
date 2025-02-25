package com.app.service;

import java.io.Reader;
import java.text.ParseException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.dto.CsvValidateResponce;
import com.app.dto.Responce;
import com.app.dto.StatusDto;
import com.app.dto.TaskDto;
import com.app.modal.Task;

@Service
public interface TaskService {

	public Task addTask(Task task);
	public CsvValidateResponce saveCsvData(MultipartFile file, Long userId)throws ParseException;
	public Page<TaskDto> getAllTaskByIsAssignANdByIsDeleted(boolean isAssign, boolean isDeleted, int page, int size);
	public Page<TaskDto> getAllTaskByIsAssignAndBySourceANdByIsDeleted(String source, boolean isDeleted, int page, int size);
	public Page<TaskDto> getAllTaskByIsCompletedANdByIsDeleted(boolean isCompleted, boolean isDeleted, int page, int size);
	public Page<TaskDto> getAllTaskByUserAndByIsAssignANdByIsDeleted(Long userId, boolean isAssign, boolean isDeleted, int page, int size);
	public Page<TaskDto> getAllTaskByUserAndByIsAssignAndByIsCompletedANdByIsDeleted(Long userId, boolean isAssign, boolean isCompleted, boolean isDeleted, int page, int size);
	public void taskAssign(List<Long> taskList, List<Long> userList);
	public void markAllCompleted(List<Long> taskList);
	void taskDeAssign(List<Long> taskList);
	public Task addTask(TaskDto taskDto)throws ParseException;
	public Responce statusUpdate(StatusDto statusDto);
	public Page<TaskDto> getAllTaskByIsAssignAndByIsCompletedANdByIsDeletedAndStatus(boolean b, boolean c, boolean d,
			String message, int page, int size);
	Page<TaskDto> getAllTaskByIsAssignAndByIsCompletedANdByIsDeletedAndRequirementType(boolean isAssign,
			boolean isCompleted, boolean isDeleted, String message, int page, int size);
	Page<TaskDto> getAllTaskByUserIdAndByIsAssignAndByIsCompletedANdByIsDeletedAndRequirementType(Long uderId, boolean isAssign,
			boolean isCompleted, boolean isDeleted, String message, int page, int size);
	Page<TaskDto> getAllTaskByIsAssignAndByIsCompletedANdByIsDeletedAndDispositionAndStatus(boolean isAssign,
			boolean isCompleted, boolean isDeleted, String disposition, String status, int page,
			int size);
	
	public Page<TaskDto> getAllTaskByUserAndByIsAssignAndByIsCompletedANdByIsDeletedAndStatus(Long userId, boolean b, boolean c, boolean d,
			String message, int page, int size);
	Page<TaskDto> getAllTaskByUserAndByIsAssignAndByIsCompletedANdByIsDeletedAndDisposition(Long userId, boolean isAssign,
			boolean isCompleted, boolean isDeleted, String message, int page, int size);
	Page<TaskDto> getAllTaskByUserAndByIsAssignAndByIsCompletedANdByIsDeletedAndDispositionAndStatus(Long userId, boolean isAssign,
			boolean isCompleted, boolean isDeleted, String disposition, String status, int page,
			int size);
	TaskDto getEdit(Long id) throws ParseException;
	public Responce updateCommentAndMessage(StatusDto statusDto);
	
	Page<TaskDto> findBySourceAndRequirementTypeAndIsDeleted(String source,String requirementType, boolean isDeleted,  int page, int size);
	Page<TaskDto> findBySourceAndStatusAndIsDeleted(String source,String status, boolean isDeleted, int page, int size);
	Page<TaskDto> findByUsers_IdAndSourceAndRequirementTypeAndIsDeleted(Long userId, String source,String requirementType, boolean isDeleted, int page, int size);
	Page<TaskDto> findByUsers_IdAndSourceAndStatusAndIsDeleted(Long userId, String source,String status, boolean isDeleted, int page, int size);
	Page<TaskDto> findByUsers_IdAndStatusAndIsDeleted(Long userId,String status, boolean isDeleted, int page, int size);
	Page<TaskDto> findByUsers_IdAndRequirementTypeAndStatusAndIsDeleted(Long userId,String requirementType ,String status, boolean isDeleted, int page, int size);
	Page<TaskDto> findBySourceAndRequirementTypeAndStatusAndIsDeleted(String source,String requirementType,String status, boolean isDeleted, int page, int size);
	Page<TaskDto> findByUsers_IdAndSourceAndRequirementTypeAndStatusAndIsDeleted(Long userId, String source,String requirementType, String status, boolean isDeleted, int page, int size);
	
	
	Page<TaskDto> getAllBySourceAndRequirementTypeAndStatusAndIsDeleted(String source,String requirementType, String status, boolean isDeleted, int page, int size);
	Page<TaskDto> getAlBySourceAndRequirementTypeAndIsDeleted(String source,String requirementType, boolean isDeleted, int page, int size);
	Page<TaskDto> getAllBySourceAndStatusAndIsDeleted(String source,String status, boolean isDeleted, int page, int size);
	Page<TaskDto> getAllByRequirementTypeAndStatusAndIsDeleted(String requirementType, String status, boolean isDeleted, int page, int size);
	Page<TaskDto> getAllByRequirementTypeAndIsDeleted(String requirementType, boolean isDeleted, int page, int size);
	Page<TaskDto> getAllBySourceAndIsDeleted(String source, boolean isDeleted, int page, int size);
	Page<TaskDto> getAllByStatusAndIsDeleted(String status, boolean isDeleted, int page, int size);
	Page<TaskDto> getAllByIsDeleted(int page, int size);
	Page<TaskDto> getAdminAllTaskByIsCompletedANdByIsDeleted(boolean isAssign, boolean isCompleted, boolean isDeleted,
			int page, int size);


}
