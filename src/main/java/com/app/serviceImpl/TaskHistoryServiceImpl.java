package com.app.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dto.TaskHistoryDto;
import com.app.dto.TaskHistoryResponse;
import com.app.dto.UpdatedFieldDto;
import com.app.modal.Task;
import com.app.modal.TaskHistory;
import com.app.repository.TaskHistoryRepository;
import com.app.service.TaskHistoryService;

@Service
public class TaskHistoryServiceImpl implements TaskHistoryService{
	
	@Autowired
	TaskHistoryRepository taskHistoryRepository;
	
	@Override
	public TaskHistoryResponse getTaskHistory(Long taskId) {
	    List<TaskHistory> historyList = taskHistoryRepository.findByTaskId(taskId);

	    
	    Map<LocalDate, List<TaskHistory>> groupedByDate = historyList.stream()
	        .collect(Collectors.groupingBy(h -> h.getUpdatedAt().toLocalDate()));

	    
	    List<TaskHistoryDto> historyDtos = groupedByDate.entrySet().stream()
	        .map(entry -> new TaskHistoryDto(
	            entry.getKey(),
	            entry.getValue().stream()
	                .map(h -> new UpdatedFieldDto(h.getFieldName(), h.getOldValue(), h.getNewValue()))
	                .collect(Collectors.toList())
	        ))
	        .collect(Collectors.toList());

	    return new TaskHistoryResponse(taskId, historyDtos);
	}
	
	@Override
	public void createHistory(String fieldName, String oldValue, String newValue, Task task) {
		// Create a TaskHistory record
	    TaskHistory taskHistory = new TaskHistory();
	    taskHistory.setTask(task);
	    taskHistory.setFieldName(fieldName);
	    taskHistory.setOldValue(oldValue);
	    taskHistory.setNewValue(newValue);
	    taskHistory.setUpdatedAt(LocalDateTime.now());

	    taskHistoryRepository.save(taskHistory);
	}


}
