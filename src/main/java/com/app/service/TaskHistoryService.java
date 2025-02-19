package com.app.service;

import org.springframework.stereotype.Service;

import com.app.dto.TaskHistoryResponse;
import com.app.modal.Task;

@Service
public interface TaskHistoryService {

	void createHistory(String fieldName, String oldValue, String newValue, Task task);

	TaskHistoryResponse getTaskHistory(Long taskId);
	

}
