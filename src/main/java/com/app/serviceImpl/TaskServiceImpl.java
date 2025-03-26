package com.app.serviceImpl;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.dto.CsvValidate;
import com.app.dto.CsvValidateResponce;
import com.app.dto.Responce;
import com.app.dto.StatusDto;
import com.app.dto.TaskDto;
import com.app.exceptionHandeler.InvalidData;
import com.app.exceptionHandeler.NotFound;
import com.app.modal.Task;
import com.app.modal.User;
import com.app.repository.TaskRepository;
import com.app.service.TaskHistoryService;
import com.app.service.TaskService;
import com.app.user.repository.UserRepository;
import com.app.utilityServices.DateUtility;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	TaskRepository taskRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	TaskHistoryService taskHistoryService;

	public static List<String> REQUIREDFIELDS = Arrays.asList("Client Name", "Client Email", "Client Phone Number",
			"Client Website", "Client Company Name", "Requirement Type", "Source", "Client Country",
			"Our Contact Source", "Lead Generation Date", "Comments", "Status", "Last Response Date",
			"Next Follow Up Date");

	@Override
	public CsvValidateResponce saveCsvData(MultipartFile file, Long userId) throws ParseException {
		CsvValidateResponce response = new CsvValidateResponce();
		List<TaskDto> csvDataList = new ArrayList<>();
		List<TaskDto> duplicateTasks = new ArrayList<>();
		Set<String> clientEmails = new HashSet<>();
		int dataRowCount = 0;
		int duplicateCount = 0;

		User user = null;
		Set<User> setUser = new HashSet<>();
		if (Objects.nonNull(userId)) {
			user = userRepository.findById(userId).orElse(null);
			if (user != null) {
				setUser.add(user);
			}
		}

		try (Reader reader = new InputStreamReader(file.getInputStream());
				CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(0) // Do not skip any line initially
						.build()) {

			String[] headers;
			try {
				headers = csvReader.readNext();

				response.setDataValidated(true);

				String[] values;
				try {
					while ((values = csvReader.readNext()) != null) {
						if (values.length < headers.length) {
							values = Arrays.copyOf(values, headers.length); // Fill missing columns with empty strings
						}
						System.out.println("Headers Length : "+headers.length);
						System.out.println("value Length : "+values.length);
						Task task = new Task();
						task.setClientName(getValue(values, 0));
						task.setClientEmail(getValue(values, 1));
						task.setClientPhoneNumber(getValue(values, 2));
						task.setClientWebsite(getValue(values, 3));
						task.setClientCompanyName(getValue(values, 4));
						task.setRequirementType(getValue(values, 5));
						task.setSource(getValue(values, 6));
						task.setClientCountry(getValue(values, 7));
						task.setOurContactSource(getValue(values, 8));
						task.setLeadGenerationDate(getValue(values, 9));
						task.setComments(getValue(values, 10));
						task.setStatus(getValue(values, 11));
						task.setLastResponseDate(getValue(values, 12));
						task.setNextFollowUpDate(getValue(values, 13));
						task.setClientSourceURL(getValue(values, 14));
						// Check for duplicate ClientEmail within the sheet
						/*if (!clientEmails.add(task.getClientEmail())
								|| taskRepository.existsByClientEmail(task.getClientEmail())) {
							duplicateCount++;
							duplicateTasks.add(convertToDto(task));
						} else {*/
							if (user != null) {
								task.setUsers(setUser);
								task.setAssign(true);
							}
							task = addTask(task); // Save task to DB
							csvDataList.add(convertToDto(task));
							dataRowCount++;
					/*	}*/
					}
				} catch (CsvValidationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // Read header row

			} catch (CsvValidationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			response.setDuplicateCount(duplicateCount);
			response.setCsvValidate(csvDataList);
			response.setDuplicateData(duplicateTasks);
			response.setCsvCount(dataRowCount);
			response.setUploaded(true);
			return response;
		} catch (IOException e) {
			throw new InvalidData("Error reading CSV file.", e);
		}
	}

//	@Override
	public CsvValidateResponce saveCsvDataORGs(MultipartFile file, Long userId) throws ParseException {
		CsvValidateResponce response = new CsvValidateResponce();
		List<TaskDto> csvDataList = new ArrayList<>(); // To store the valid tasks
		List<TaskDto> duplicateTasks = new ArrayList<>(); // To store the duplicate tasks
		Set<String> clientEmail = new HashSet<>(); // To track vehicle numbers for duplicates within the sheet
		int dataRowCount = 0; // To count rows containing valid data
		int duplicateCount = 0;

		User user = null;
		Set<User> setUser = new HashSet<>();
		if (Objects.nonNull(userId)) {
			user = userRepository.findById(userId).orElse(null);
			setUser.add(user);
		}

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			String line;
			String[] headers = null;
			boolean headerRow = true;
			response.setDataValidated(true);
			int lengths = 0;

			while ((line = reader.readLine()) != null) {
				String[] values = line.split(",");
				lengths = values.length;

				Task task = new Task();
				if (headerRow) {
					headers = values;

					// Validate header row length
					if (headers.length < REQUIREDFIELDS.size()) {
						response.setDataValidated(false);
					}

					// Validate header content and sequence
					for (int i = 0; i < REQUIREDFIELDS.size(); i++) {
						if (i >= headers.length || !REQUIREDFIELDS.get(i).equals(headers[i])) {
							response.setDataValidated(false);
						}
					}
					headerRow = false;
				} else {
					if (headers != null) {
						// Check if row length is less than required
						if (values.length < headers.length) {
							// Fill missing columns with default values (e.g., "")
							values = Arrays.copyOf(values, headers.length);
							Arrays.fill(values, lengths, headers.length, "");
						}

						// Map values to Task object with default handling
						task.setClientName(getValue(values, 0));
						task.setClientEmail(getValue(values, 1));
						task.setClientPhoneNumber(getValue(values, 2));
						task.setClientWebsite(getValue(values, 3));
						task.setClientCompanyName(getValue(values, 4));
						task.setRequirementType(getValue(values, 5));
						task.setSource(getValue(values, 6));
						task.setClientCountry(getValue(values, 7));
						task.setOurContactSource(getValue(values, 8));
						task.setLeadGenerationDate(getValue(values, 9));
						task.setComments(getValue(values, 10));
						task.setStatus(getValue(values, 11));
						task.setLastResponseDate(getValue(values, 12));
						task.setNextFollowUpDate(getValue(values, 13));
						task.setClientSourceURL(getValue(values, 14));

					}

					// Check for duplicate Vehicle Number within the sheet
//	                if (!clientEmail.add(task.getClientEmail())) {
//	                    duplicateCount++;
//	                    duplicateTasks.add(convertToDto(task));
//	                } else {
//	                	if(taskRepository.existsByClientEmail(task.getClientEmail())) {
//	                		duplicateCount++;
//		                    duplicateTasks.add(convertToDto(task));
//		                    
//	                	} else {
					if (user != null) {
						task.setUsers(setUser);
						task.setAssign(true);
					}
					task = addTask(task); // Save the task in the database
					// System.out.println("last_response_date: "+task.getLastResponseDate());
					csvDataList.add(convertToDto(task));
					dataRowCount++;
					/*
					 * } }
					 */
				}
			}

			response.setDuplicateCount(duplicateCount);
			response.setCsvValidate(csvDataList);
			response.setDuplicateData(duplicateTasks);
			response.setCsvCount(dataRowCount);
			response.setUploaded(true);
			return response;
		} catch (IOException e) {
			throw new InvalidData("Error reading CSV file.", e);
		}

	}

	private String getValue(String[] values, int index) {
	    return (index < values.length && values[index] != null) ? values[index].trim() : "";
	}


//	@Override
	public CsvValidateResponce saveCsvDatas(MultipartFile file, Long userId) throws ParseException {

		CsvValidateResponce responce = new CsvValidateResponce();
		List<TaskDto> csvDataList = new ArrayList<>();
		int dataRowCount = 0;
		int duplicateCount = 0;
		User user = null;
		Set<User> setUser = new HashSet<User>();
		if (Objects.nonNull(userId)) {
			user = userRepository.findById(userId).orElse(null);
			setUser.add(user);
		}
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
			String line;
			String[] headers = null;
			boolean headerRow = true;
			responce.setDataValidated(true);

			while ((line = reader.readLine()) != null) {
				String[] values = line.split(",");
				Task task = new Task();
				if (headerRow) {
					headers = values;
					// Validate header row
					if (!Arrays.asList(headers).containsAll(REQUIREDFIELDS)) {
						responce.setDataValidated(false);
					}

					// Validate header sequence
					for (int i = 0; i < REQUIREDFIELDS.size(); i++) {
						if (i >= headers.length || !REQUIREDFIELDS.get(i).equals(headers[i])) {
							responce.setDataValidated(false);
						}
					}

					headerRow = false;
				} else {
					CsvValidate csvValidate = new CsvValidate();

					for (int i = 0; i < headers.length; i++) {
						if (i >= values.length) {
							continue; // Skip if the value is missing in the CSV
						}

						String header = headers[i];
						String value = values[i].trim(); // trim to avoid any leading/trailing spaces

						switch (header) {

						case "Client Name":
							task.setClientName(value);
							break;
						case "Client Email":
							task.setClientEmail(value);
							break;
						case "Client Phone Number":
							task.setClientPhoneNumber(value);
							break;
						case "Client Website":
							task.setClientWebsite(value);
							break;
						case "Client Company Name":
							task.setClientCompanyName(value);
							break;
						case "Requirement Type":
							task.setRequirementType(value);
							break;
						case "Source":
							task.setSource(value);
							break;
						case "Client Country":
							task.setClientCountry(value);
							break;
						case "Our Contact Source":
							task.setOurContactSource(value);
							break;
						case "Lead Generation Date":
							task.setLeadGenerationDate(value);
							break;
						case "Status":
							task.setStatus(value);
							break;
						case "Last Response Date":
							task.setLastResponseDate(value);
							break;
						case "Next Follow up Date":
							task.setNextFollowUpDate(value);
							break;
						case "Comments":
							task.setComments(value);
							break;
						default:
							// Ignore other columns
							break;
						}
					}

					if (user != null) {
						task.setUsers(setUser);
						task.setAssign(true);
					}
					task = addTask(task);
				}
			}

			responce.setDuplicateCount(duplicateCount);
			// responce.setCsvValidate(csvDataList);
			responce.setCsvCount(dataRowCount);
			responce.setUploaded(true);
			return responce;
		} catch (IOException e) {
			throw new InvalidData("Error reading CSV file.", e);
		}
	}

	@Override
	public Task addTask(Task task) {
		task.setCompleted(false);
		task.setDeleted(false);
		return taskRepository.save(task);
	}

	@Override
	public Page<TaskDto> getAllTaskByIsAssignAndBySourceANdByIsDeleted(String source, boolean isDeleted, int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findBySourceAndIsDeleted(source, isDeleted, pageable);

		return taskPage.map(this::convertToDto);
	}
	
	@Override
	public Page<TaskDto> getAllTaskByIsCompletedANdByIsDeleted(boolean isCompleted, boolean isDeleted, int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findByIsCompletedAndIsDeleted(isCompleted, isDeleted,
				pageable);

		return taskPage.map(this::convertToDto);
	}
	
	@Override
	public Page<TaskDto> getAdminAllTaskByIsCompletedANdByIsDeleted(boolean isAssign, boolean isCompleted, boolean isDeleted, int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findByIsAssignAndIsCompletedAndIsDeleted(isAssign, isCompleted, isDeleted,
				pageable);

		return taskPage.map(this::convertToDto);
	}

	@Override
	public Page<TaskDto> getAllTaskByIsAssignANdByIsDeleted(boolean isAssign, boolean isDeleted, int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findByIsAssignAndIsDeleted(isAssign, isDeleted, pageable);

		return taskPage.map(this::convertToDto);
	}

	@Override
	public Page<TaskDto> getAllTaskByUserAndByIsAssignANdByIsDeleted(Long userId, boolean isAssign, boolean isDeleted,
			int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findByUsers_IdAndIsAssignAndIsDeleted(userId, isAssign, isDeleted,
				pageable);

		return taskPage.map(this::convertToDto);
	}

	@Override
	public Page<TaskDto> getAllTaskByUserAndByIsAssignAndByIsCompletedANdByIsDeleted(Long userId, boolean isAssign,
			boolean isCompleted, boolean isDeleted, int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findByUsers_IdAndIsAssignAndIsCompletedAndIsDeleted(userId, isAssign,
				isCompleted, isDeleted, pageable);

		return taskPage.map(this::convertToDto);
	}

	@Override
	public TaskDto getEdit(Long id) throws ParseException {
		Task task = taskRepository.findById(id).orElseThrow(() -> new NotFound("No Task Found By id: " + id));
		TaskDto taskdto = convertToDto(task);
//		taskdto.setLeadGenerationDate(DateUtility.convertToDDMMYYYY(task.getLeadGenerationDate()));
//		taskdto.setLastResponseDate(DateUtility.convertToDDMMYYYY(task.getLastResponseDate()));
//		taskdto.setNextFollowUpDate(DateUtility.convertToDDMMYYYY(task.getNextFollowUpDate()));
//		taskdto.setNextFollowUpDate(DateUtility.convertToDDMMYYYY(task.getNextFollowUpDate()));
		return taskdto;
	}

	private TaskDto convertToDto(Task task) {
		TaskDto taskDto = new TaskDto();

		// Map all fields
		taskDto.setId(task.getId());
		taskDto.setClientName(task.getClientName());
		taskDto.setClientEmail(task.getClientEmail());
		taskDto.setClientPhoneNumber(task.getClientPhoneNumber());
		taskDto.setClientWebsite(task.getClientWebsite());
		taskDto.setClientCompanyName(task.getClientCompanyName());
		taskDto.setRequirementType(task.getRequirementType());
		taskDto.setSource(task.getSource());
		taskDto.setClientCountry(task.getClientCountry());
		taskDto.setOurContactSource(task.getOurContactSource());
		taskDto.setLeadGenerationDate(task.getLeadGenerationDate());
		taskDto.setLastResponseDate(task.getLastResponseDate());
		taskDto.setNextFollowUpDate(task.getNextFollowUpDate());
		taskDto.setComments(task.getComments());
		taskDto.setStatus(task.getStatus());
		taskDto.setClientSourceURL(task.getClientSourceURL());
		// Map boolean fields
		taskDto.setAssign(task.isAssign());
		taskDto.setCompleted(task.isCompleted());
		taskDto.setDeleted(task.isDeleted());

		// Assign userInfo
		if (Objects.nonNull(task.getUsers())) {
			for (User user : task.getUsers()) {
				taskDto.setEmployeeAssinedName(user.getName());
				taskDto.setEmployeeAssinedEmail(user.getEmail());
			}
		}

		return taskDto;
	}

	@Override
	public Task addTask(TaskDto taskDto) throws ParseException {
		Task taskObj = new Task();

		if (Objects.nonNull(taskDto.getId())) {
			Set<User> userSet = new HashSet<User>();
			User user = userRepository.findById(taskDto.getId()).orElse(null);
			userSet.add(user);
			taskObj.setAssign(true);
			taskObj.setUsers(userSet);
		} else {
			taskObj.setAssign(taskDto.isAssign());
		}

		// Map all fields
		//taskObj.setId(taskDto.getId());
		taskObj.setClientName(taskDto.getClientName());
		taskObj.setClientEmail(taskDto.getClientEmail());
		taskObj.setClientPhoneNumber(taskDto.getClientPhoneNumber());
		taskObj.setClientWebsite(taskDto.getClientWebsite());
		taskObj.setClientCompanyName(taskDto.getClientCompanyName());
		taskObj.setRequirementType(taskDto.getRequirementType());
		taskObj.setSource(taskDto.getSource());
		taskObj.setClientCountry(taskDto.getClientCountry());
		taskObj.setOurContactSource(taskDto.getOurContactSource());
		System.out.println(taskDto.getLeadGenerationDate());
		System.out.println("Lead Generationn date: "+DateUtility.convertToMMDDYYYY(taskDto.getLeadGenerationDate()));
		taskObj.setLeadGenerationDate(DateUtility.convertToMMDDYYYY(taskDto.getLeadGenerationDate()));
		taskObj.setLastResponseDate(DateUtility.convertToMMDDYYYY(taskDto.getLastResponseDate()));
		taskObj.setNextFollowUpDate(DateUtility.convertToMMDDYYYY(taskDto.getNextFollowUpDate()));
		taskObj.setComments(taskDto.getComments());
		taskObj.setNextFollowUpDate(DateUtility.convertToMMDDYYYY(taskDto.getNextFollowUpDate()));
		taskObj.setClientSourceURL(taskDto.getClientSourceURL());
		// Map boolean fields
		
		taskObj.setCompleted(taskDto.isCompleted());
		taskObj.setDeleted(taskDto.isDeleted());

		// Map status
		taskObj.setStatus(taskDto.getStatus());

		taskObj = taskRepository.save(taskObj);

		return taskObj;
	}

	@Override
	public void taskAssign(List<Long> taskList, List<Long> userList) {
		if (Objects.nonNull(userList) && userList.get(0) > 0) {

			List<Task> list = new ArrayList<Task>();
			Task task = null;
			Set<User> userSet = new HashSet<User>();
			for (Long userId : userList) {
				User user = userRepository.findByIdAndStatusFalse(userId);
				userSet.add(user);
			}

			for (Long taskId : taskList) {
				task = taskRepository.findById(taskId).orElse(null);
				if (Objects.nonNull(task)) {
					task.setAssign(true);
					task.setUsers(userSet);
					list.add(task);
				}
			}
			taskRepository.saveAll(list);
		} else {

			taskDeAssign(taskList);
		}
	}

	@Override
	public void taskDeAssign(List<Long> taskList) {
		List<Task> list = new ArrayList<Task>();
		Task task = null;
		for (Long taskId : taskList) {
			task = taskRepository.findById(taskId).orElse(null);
			if (Objects.nonNull(task)) {
				task.setAssign(false);
				task.setUsers(null);
				list.add(task);
			}
		}
		taskRepository.saveAll(list);
	}

	@Override
	public void markAllCompleted(List<Long> taskList) {
		// TODO Auto-generated method stub
		List<Task> list = new ArrayList<Task>();
		Task task = null;
		for (Long taskId : taskList) {
			task = taskRepository.findById(taskId).orElse(null);
			if (Objects.nonNull(task)) {
				task.setCompleted(true);
				list.add(task);
			}
		}
		taskRepository.saveAll(list);
	}

	@Override
	public Responce statusUpdate(StatusDto statusDto) {
		Responce response = new Responce();
		response.setStatus(200L);

		Task task = taskRepository.findById(statusDto.getId()).orElse(null);
		String key = statusDto.getValidateKey();
		String oldValue = null;
		String newValue = statusDto.getMessage();
		if (key.equalsIgnoreCase("Status")) {
			oldValue = task.getStatus();
			task.setStatus(newValue);
			response.setMessage("Status updated.");
		} else if (key.equalsIgnoreCase("Requirement Type")) {
			oldValue = task.getRequirementType();
			task.setRequirementType(newValue);
			response.setMessage("Requirement Type updated.");
		} else if (key.equalsIgnoreCase("Source")) {
			oldValue = task.getSource();
			task.setSource(newValue);
			response.setMessage("Source updated.");
		} else if (key.equalsIgnoreCase("Client Source URL")) {
			oldValue = task.getSource();
			task.setClientSourceURL(newValue);
			response.setMessage("Client Source URL");
		} 
		task = taskRepository.save(task);
		taskHistoryService.createHistory(key, oldValue, newValue, task);
		return response;
	}
	



	@Override
	public Responce updateCommentAndMessage(StatusDto statusDto) {
		Responce response = new Responce();
		response.setStatus(200L);
		response.setMessage("Your " + statusDto.getValidateKey() + " is updated.");

		// Fetch the task from the database
		Task task = taskRepository.findById(statusDto.getId()).orElseThrow(() -> new NotFound("No Task Found."));

		String fieldName = statusDto.getValidateKey();
		String oldValue = null;
		String newValue = statusDto.getMessage();

		// Determine which field to update and track the old value
		switch (fieldName) {
		case "Comment":
			oldValue = task.getComments();
			task.setComments(newValue);
			break;
		case "Client Name":
			oldValue = task.getClientName();
			task.setClientName(newValue);
			break;
		case "Client Email":
			oldValue = task.getClientEmail();
			task.setClientEmail(newValue);
			break;
		case "Client Phone Number":
			oldValue = task.getClientPhoneNumber();
			task.setClientPhoneNumber(newValue);
			break;
		case "Client Website":
			oldValue = task.getClientWebsite();
			task.setClientWebsite(newValue);
			break;
		case "Client Company Name":
			oldValue = task.getClientCompanyName();
			task.setClientCompanyName(newValue);
			break;
		case "Requirement Type":
			oldValue = task.getRequirementType();
			task.setRequirementType(newValue);
			break;
		case "Source":
			oldValue = task.getSource();
			task.setSource(newValue);
			break;
		case "Client Country":
			oldValue = task.getClientCountry();
			task.setClientCountry(newValue);
			break;
		case "Our Contact Source":
			oldValue = task.getOurContactSource();
			task.setOurContactSource(newValue);
			break;
		case "Lead Generation Date":
			oldValue = task.getLeadGenerationDate();
			task.setLeadGenerationDate(newValue);
			break;
		case "Next FollowUp Date":
			oldValue = task.getNextFollowUpDate();
			task.setNextFollowUpDate(newValue);
			break;
		case "Last Response Date":
			oldValue = task.getLastResponseDate();
			task.setLastResponseDate(newValue);
			break;
		case "Client Source URL":
			oldValue = task.getClientSourceURL();
			task.setClientSourceURL(newValue);
			break;
		default:
			response.setMessage("Key not found.");
			return response;
		}
		taskHistoryService.createHistory(fieldName, oldValue, newValue, task);
		return response;
	}

	@Override
	public Page<TaskDto> getAllTaskByIsAssignAndByIsCompletedANdByIsDeletedAndStatus(boolean isAssign,
			boolean isCompleted, boolean isDeleted, String message, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findByIsAssignAndIsCompletedAndIsDeletedAndStatus(isAssign, isCompleted,
				isDeleted, message, pageable);

		return taskPage.map(this::convertToDto);
	}

	@Override
	public Page<TaskDto> getAllTaskByIsAssignAndByIsCompletedANdByIsDeletedAndRequirementType(boolean isAssign,
			boolean isCompleted, boolean isDeleted, String message, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findByIsAssignAndIsCompletedAndIsDeletedAndRequirementType(isAssign,
				isCompleted, isDeleted, message, pageable);

		return taskPage.map(this::convertToDto);
	}
	
	@Override
	public Page<TaskDto> getAllTaskByUserIdAndByIsAssignAndByIsCompletedANdByIsDeletedAndRequirementType(Long userId, boolean isAssign,
			boolean isCompleted, boolean isDeleted, String message, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findByUsers_IdAndIsAssignAndIsCompletedAndIsDeletedAndRequirementType(userId,isAssign,
				isCompleted, isDeleted, message, pageable);

		return taskPage.map(this::convertToDto);
	}

	@Override
	public Page<TaskDto> getAllTaskByIsAssignAndByIsCompletedANdByIsDeletedAndDispositionAndStatus(boolean isAssign,
			boolean isCompleted, boolean isDeleted, String disposition, String status, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findByIsAssignAndIsCompletedAndIsDeletedAndRequirementTypeAndStatus(
				isAssign, isCompleted, isDeleted, disposition, status, pageable);

		return taskPage.map(this::convertToDto);
	}

	@Override
	public Page<TaskDto> getAllTaskByUserAndByIsAssignAndByIsCompletedANdByIsDeletedAndStatus(Long userId,
			boolean isAssign, boolean isCompleted, boolean isDeleted, String message, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findByUsers_IdAndIsAssignAndIsCompletedAndIsDeletedAndStatus(userId,
				isAssign, isCompleted, isDeleted, message, pageable);

		return taskPage.map(this::convertToDto);
	}

	@Override
	public Page<TaskDto> getAllTaskByUserAndByIsAssignAndByIsCompletedANdByIsDeletedAndDisposition(Long userId,
			boolean isAssign, boolean isCompleted, boolean isDeleted, String message, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findByUsers_IdAndIsAssignAndIsCompletedAndIsDeletedAndRequirementType(
				userId, isAssign, isCompleted, isDeleted, message, pageable);

		return taskPage.map(this::convertToDto);
	}

	@Override
	public Page<TaskDto> getAllTaskByUserAndByIsAssignAndByIsCompletedANdByIsDeletedAndDispositionAndStatus(Long userId,
			boolean isAssign, boolean isCompleted, boolean isDeleted, String disposition, String status, int page,
			int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository
				.findByUsers_IdAndIsAssignAndIsCompletedAndIsDeletedAndRequirementTypeAndStatus(userId, isAssign,
						isCompleted, isDeleted, disposition, status, pageable);

		return taskPage.map(this::convertToDto);
	}
	

	@Override
	public Page<TaskDto> findBySourceAndRequirementTypeAndIsDeleted(String source, String requirementType,
			boolean isDeleted, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findBySourceAndRequirementTypeAndIsDeleted(source, requirementType,
				isDeleted, pageable);

		return taskPage.map(this::convertToDto);
	}
	
	@Override
	public Page<TaskDto> findByUsers_IdAndRequirementTypeAndStatusAndIsDeleted(Long userId, String requirementType, String status, boolean isDeleted, int page,
			int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository
				.findByUsers_IdAndRequirementTypeAndStatusAndIsDeleted(userId , requirementType,status, isDeleted, pageable);

		return taskPage.map(this::convertToDto);
	}
	
	@Override
	public Page<TaskDto> findByUsers_IdAndStatusAndIsDeleted(Long userId, String status, boolean isDeleted, int page,
			int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository
				.findByUsers_IdAndStatusAndIsDeleted(userId,status, isDeleted, pageable);

		return taskPage.map(this::convertToDto);
	}

	@Override
	public Page<TaskDto> findBySourceAndStatusAndIsDeleted(String source, String status, boolean isDeleted, int page,
			int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository
				.findBySourceAndStatusAndIsDeleted(source,status, isDeleted, pageable);

		return taskPage.map(this::convertToDto);
	}

	@Override
	public Page<TaskDto> findByUsers_IdAndSourceAndRequirementTypeAndIsDeleted(Long userId, String source,
			String requirementType, boolean isDeleted, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findByUsers_IdAndSourceAndRequirementTypeAndIsDeleted(userId, source,
				requirementType, isDeleted, pageable);

		return taskPage.map(this::convertToDto);
	}

	@Override
	public Page<TaskDto> findByUsers_IdAndSourceAndStatusAndIsDeleted(Long userId, String source, String status,
			boolean isDeleted, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findByUsers_IdAndSourceAndStatusAndIsDeleted(userId, source, status,
				isDeleted, pageable);

		return taskPage.map(this::convertToDto);
	}
	
	@Override
	public Page<TaskDto> findBySourceAndRequirementTypeAndStatusAndIsDeleted(String source,
			String requirementType, String status, boolean isDeleted, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findBySourceAndRequirementTypeAndStatusAndIsDeletedAndIsAssignFalse(source, requirementType, status,
				isDeleted, pageable);

		return taskPage.map(this::convertToDto);
	}

	@Override
	public Page<TaskDto> findByUsers_IdAndSourceAndRequirementTypeAndStatusAndIsDeleted(Long userId, String source,
			String requirementType, String status, boolean isDeleted, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findByUsers_IdAndSourceAndRequirementTypeAndStatusAndIsDeleted(userId, source, requirementType, status,
				isDeleted, pageable);

		return taskPage.map(this::convertToDto);
	}
	
///All
	@Override
	public Page<TaskDto> getAllBySourceAndRequirementTypeAndStatusAndIsDeleted(String source, String requirementType,
			String status, boolean isDeleted, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findBySourceAndRequirementTypeAndStatusAndIsDeleted(source, requirementType, status,
				isDeleted, pageable);

		return taskPage.map(this::convertToDto);
	}

	@Override
	public Page<TaskDto> getAlBySourceAndRequirementTypeAndIsDeleted(String source, String requirementType,
			boolean isDeleted, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findBySourceAndRequirementTypeAndIsDeleted(source, requirementType,
				isDeleted, pageable);

		return taskPage.map(this::convertToDto);
	}

	@Override
	public Page<TaskDto> getAllBySourceAndStatusAndIsDeleted(String source, String status, boolean isDeleted, int page,
			int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findBySourceAndStatusAndIsDeleted(source, status,
				isDeleted, pageable);

		return taskPage.map(this::convertToDto);
	}

	@Override
	public Page<TaskDto> getAllByRequirementTypeAndStatusAndIsDeleted(String requirementType, String status,
			boolean isDeleted, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findByRequirementTypeAndStatusAndIsDeleted(requirementType, status,
				isDeleted, pageable);

		return taskPage.map(this::convertToDto);
	}

	@Override
	public Page<TaskDto> getAllByRequirementTypeAndIsDeleted(String requirementType, boolean isDeleted, int page,
			int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findByRequirementTypeAndIsDeleted(requirementType, isDeleted, pageable);

		return taskPage.map(this::convertToDto);
	}

	@Override
	public Page<TaskDto> getAllBySourceAndIsDeleted(String source, boolean isDeleted, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findBySourceAndIsDeleted(source,isDeleted, pageable);

		return taskPage.map(this::convertToDto);
	}

	@Override
	public Page<TaskDto> getAllByStatusAndIsDeleted(String status, boolean isDeleted, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findByStatusAndIsDeleted(status, isDeleted, pageable);

		return taskPage.map(this::convertToDto);
	}

	@Override
	public Page<TaskDto> getAllByIsDeleted(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Task> taskPage = taskRepository.findByIsDeletedFalse(pageable);

		return taskPage.map(this::convertToDto);
	}
}
