package com.app.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.app.config.CurrentSession;
import com.app.dto.CsvValidateResponce;
import com.app.dto.Responce;
import com.app.dto.StatusDto;
import com.app.dto.TaskAssignRequest;
import com.app.dto.TaskDto;
import com.app.modal.User;
import com.app.service.TaskService;
@RequestMapping("/task")
@Controller
public class TaskController {
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	CurrentSession session;
	
	@GetMapping("/{id}")
	public String dashboard(@PathVariable Long id, Model model) {
		User user = session.currentUser();
		model.addAttribute("user", user);
		model.addAttribute("id", id);
	    return "admin/taskDetails";
	}
	
	@GetMapping("/emp/{id}")
	public String details(@PathVariable Long id, Model model) {
		User user = session.currentUser();
		model.addAttribute("user", user);
		model.addAttribute("id", id);
	    return "employee/taskDetails";
	}
	
	@PostMapping("/upload")
	public ResponseEntity<CsvValidateResponce> taskUpload(@RequestParam(required = false) Long userId, @RequestParam("file") MultipartFile file) throws ParseException {
		
		return ResponseEntity.ok(taskService.saveCsvData(file, userId));
	}
	
	
	
	@GetMapping("/admin-all")
	public ResponseEntity<Map<String, Object>> getAdminAllTaskByIsCompletedANdByIsDeleted(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.getAdminAllTaskByIsCompletedANdByIsDeleted(false, false, false,
				page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/all")
	public ResponseEntity<Map<String, Object>> getAllTasks(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.getAllTaskByIsCompletedANdByIsDeleted(false, false,
				page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/by-status/{message}")
	public ResponseEntity<Map<String, Object>> getAllTasksByStatus(@PathVariable String message,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.getAllTaskByIsAssignAndByIsCompletedANdByIsDeletedAndStatus(false, false, false, message,
				page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/by-requirement-type/{message}")
	public ResponseEntity<Map<String, Object>> getAllTasksByDisposition(@PathVariable String message,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.getAllTaskByIsAssignAndByIsCompletedANdByIsDeletedAndRequirementType(false, false, false, message,
				page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/by-status-requirementType/{requirementType}/{status}")
	public ResponseEntity<Map<String, Object>> getAllTasksByAtatusAndDisposition(@PathVariable String requirementType, @PathVariable String status,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.getAllTaskByIsAssignAndByIsCompletedANdByIsDeletedAndDispositionAndStatus(false, false, false, requirementType, status,
				page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/by-status/{userId}/{message}")
	public ResponseEntity<Map<String, Object>> getAllTasksByUserAndStatus(@PathVariable Long userId, @PathVariable String message,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.getAllTaskByIsAssignAndByIsCompletedANdByIsDeletedAndStatus(false, false, false, message,
				page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/by-requirement-type/{userId}/{message}")
	public ResponseEntity<Map<String, Object>> getAllTasksByUserAndDisposition(@PathVariable Long userId, @PathVariable String message,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.getAllTaskByUserIdAndByIsAssignAndByIsCompletedANdByIsDeletedAndRequirementType(userId, false, false, false, message,
				page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/by-status-requirement-type/{userId}/{disposition}/{status}")
	public ResponseEntity<Map<String, Object>> getAllTasksByUserAndAtatusAndDisposition(@PathVariable Long userId, @PathVariable String disposition, @PathVariable String status,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.getAllTaskByIsAssignAndByIsCompletedANdByIsDeletedAndDispositionAndStatus(false, false, false, disposition, status,
				page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/source/{sourceType}")
	public ResponseEntity<Map<String, Object>> getAllTaskBySource(@PathVariable String sourceType,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.getAllTaskByIsAssignAndBySourceANdByIsDeleted(sourceType, false,
				page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/source/{userId}/{sourceType}")
	public ResponseEntity<Map<String, Object>> getAllTaskBySourceAndUser(@PathVariable String sourceType,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.getAllTaskByIsAssignAndBySourceANdByIsDeleted(sourceType, false,
				page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/emp-all/{userId}")
	public ResponseEntity<Map<String, Object>> getAllTaskByUser(@PathVariable Long userId,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.getAllTaskByUserAndByIsAssignANdByIsDeleted(userId, true, false, page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/by-user-status/{userId}/{status}")
	public ResponseEntity<Map<String, Object>> getByUsers_IdAndStatusAndIsDeleted(@PathVariable Long userId, @PathVariable String status,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.findByUsers_IdAndStatusAndIsDeleted(userId, status, false, page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/by-user-requirement-type-status/{userId}/{requirementType}/{status}")
	public ResponseEntity<Map<String, Object>> getByUsers_IdAndRequirementTypeAndStatusAndIsDeleted(@PathVariable Long userId, @PathVariable String requirementType, @PathVariable String status,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.findByUsers_IdAndRequirementTypeAndStatusAndIsDeleted(userId, requirementType, status, false, page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/by-user-source-requirement-type/{userId}/{source}/{requirementType}")
	public ResponseEntity<Map<String, Object>> getByUsers_IdAndSourceAndRequirementTypeAndIsDeleted(@PathVariable Long userId, @PathVariable String source, @PathVariable String requirementType,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.findByUsers_IdAndSourceAndRequirementTypeAndIsDeleted(userId,source, requirementType, false, page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/by-user-source-status/{userId}/{source}/{sataus}")
	public ResponseEntity<Map<String, Object>> getByUsers_IdAndSourceAndStatusAndIsDeleted(@PathVariable Long userId, @PathVariable String source, @PathVariable String sataus, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.findByUsers_IdAndSourceAndStatusAndIsDeleted(userId,source, sataus, false, page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/by-source-sataus/{source}/{sataus}")
	public ResponseEntity<Map<String, Object>> getBySourceAndStatusAndIsDeleted(@PathVariable String source, @PathVariable String sataus,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.findBySourceAndStatusAndIsDeleted(source, sataus, false,page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/by-source-requirement-type/{source}/{requirementType}")
	public ResponseEntity<Map<String, Object>> getBySourceAndRequirementTypeAndIsDeleted(@PathVariable String source, @PathVariable String requirementType,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.findBySourceAndRequirementTypeAndIsDeleted(source, requirementType, false,page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/by-source-requirement-status/{source}/{requirementType}/{status}")
	public ResponseEntity<Map<String, Object>> getBySourceAndRequirementTypeAndStatusAndIsDeleted(@PathVariable String source, @PathVariable String requirementType, @PathVariable String status,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.findBySourceAndRequirementTypeAndStatusAndIsDeleted(source, requirementType, status, false,page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/by-user-source-requirement-status/{userId}/{source}/{requirementType}/{status}")
	public ResponseEntity<Map<String, Object>> getByUsers_IdAndSourceAndRequirementTypeAndStatusAndIsDeleted(@PathVariable Long userId,@PathVariable String source, @PathVariable String requirementType, @PathVariable String status,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.findByUsers_IdAndSourceAndRequirementTypeAndStatusAndIsDeleted(userId, source, requirementType, status, false,page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/all-by-source-requirement-status/{source}/{requirementType}/{status}")
	public ResponseEntity<Map<String, Object>> getAllBySourceAndRequirementTypeAndStatusAndIsDeleted(@PathVariable String source, @PathVariable String requirementType, @PathVariable String status,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.getAllBySourceAndRequirementTypeAndStatusAndIsDeleted(source, requirementType, status, false,page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/all-by-source-requirement/{source}/{requirementType}")
	public ResponseEntity<Map<String, Object>> getAlBySourceAndRequirementTypeAndIsDeleted(@PathVariable String source, @PathVariable String requirementType, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.getAlBySourceAndRequirementTypeAndIsDeleted(source, requirementType, false,page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/all-by-source-status/{userId}/{source}/{requirementType}/{status}")
	public ResponseEntity<Map<String, Object>> getAllBySourceAndStatusAndIsDeleted(@PathVariable String source, @PathVariable String status,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.getAllBySourceAndStatusAndIsDeleted(source, status, false,page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/all-by-requirement-status/{requirementType}/{status}")
	public ResponseEntity<Map<String, Object>> getAllByRequirementTypeAndStatusAndIsDeleted(@PathVariable String requirementType, @PathVariable String status,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.getAllByRequirementTypeAndStatusAndIsDeleted(requirementType, status, false,page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/all-by-requirement/{requirementType}")
	public ResponseEntity<Map<String, Object>> getAllByRequirementTypeAndIsDeleted(@PathVariable String requirementType, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.getAllByRequirementTypeAndIsDeleted(requirementType, false,page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/all-by-source/{source}")
	public ResponseEntity<Map<String, Object>> getAllBySourceAndIsDeleted(@PathVariable String source, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.getAllBySourceAndIsDeleted(source, false,page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/all-by-status/{status}")
	public ResponseEntity<Map<String, Object>> getAllByStatusAndIsDeleted(@PathVariable String status,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.getAllByStatusAndIsDeleted(status, false,page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	} 
	
	@GetMapping("/all-leads")
	public ResponseEntity<Map<String, Object>> getAllByIsDeleted(@PathVariable String status,@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<TaskDto> taskPage = taskService.getAllByIsDeleted(page, size);

		Map<String, Object> response = new HashMap<>();
		response.put("tasks", taskPage.getContent());
		response.put("currentPage", taskPage.getNumber());
		response.put("totalPages", taskPage.getTotalPages());
		response.put("totalElements", taskPage.getTotalElements());

		return ResponseEntity.ok(response);
	}
	
	
	@PostMapping("/assign")
	public ResponseEntity<Responce> taskAssign(@RequestBody TaskAssignRequest request){
		Responce response = new Responce();
		List<Long> taskList = request.getTaskList();
	    List<Long> userList = request.getUserList();
		taskService.taskAssign(taskList,userList);
		response.setStatus(200L);
		response.setMessage("Selected task is assign to the selected employee.");
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/de-assign")
	public ResponseEntity<Responce> taskDeAssign(@RequestBody TaskAssignRequest request){
		Responce response = new Responce();
		List<Long> taskList = request.getTaskList();
//	    List<Long> userList = request.getUserList();
		taskService.taskDeAssign(taskList);
		response.setStatus(200L);
		response.setMessage("Selected task is assign to the admin employee.");
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/mark-all-completed")
	public ResponseEntity<Responce> markAllCompleted(@RequestBody TaskAssignRequest request){
		Responce response = new Responce();
	    List<Long> userList = request.getUserList();
		taskService.markAllCompleted(userList);
		response.setStatus(200L);
		response.setMessage("Selected task is assign to the selected employee.");
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/add")
	public ResponseEntity<Responce> addNewTask(@RequestBody TaskDto taskDto) throws ParseException{
		Responce response = new Responce();
		taskService.addTask(taskDto);
		//System.out.println(taskService.addTask(taskDto));
		response.setStatus(200L);
		response.setMessage("New added.");
		return ResponseEntity.ok(response);
	} 
	
	
	@PostMapping("/status")
	public ResponseEntity<Responce> updateStatus(@RequestBody StatusDto statusDto){
		
		return ResponseEntity.ok(taskService.statusUpdate(statusDto));
	}
	
	@GetMapping("/details/{taskId}")
	public ResponseEntity<TaskDto> edit(@PathVariable Long taskId) throws ParseException{
		
		return ResponseEntity.ok(taskService.getEdit(taskId));
	}
	
	@PostMapping("/update")
	public ResponseEntity<Responce> updateCommentAndMessage(@RequestBody StatusDto statusDto){
		
		return ResponseEntity.ok(taskService.updateCommentAndMessage(statusDto));
	}
	
}
