package com.app.SchedulerService;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.FollowUpLeads;
import com.app.modal.Task;
import com.app.modal.User;
import com.app.repository.TaskRepository;
import com.app.utilityServices.DateUtility;
import com.app.utilityServices.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class MyTaskScheduler {

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	EmailService emailService;

	@Transactional
//	@Scheduled(cron = "0 * * * * ?") // Runs every minute
	@Scheduled(cron = "0 0 9 * * ?", zone = "Asia/Kolkata")
	public void getAllScheduledTask() {
		String followUpDate = DateUtility.getCurrentDateFormatted();
		@SuppressWarnings("unchecked")
		Map<User, List<Task>> tasksByUser = getTaskByUser(followUpDate);

		tasksByUser.forEach((user, tasks) -> {
			List<String> ccRecipients = new ArrayList<>();
			List<FollowUpLeads> followUpLeads = tasks.stream().map(this::mapTOFollowUpDto).collect(Collectors.toList());

			ccRecipients.add("support@interestbudsolutions.com");
			try {
				emailService.sendEmail(user, followUpDate, ccRecipients, followUpLeads);
			} catch (UnsupportedEncodingException | MessagingException e) {
				e.printStackTrace();
			}
		});

	}

	FollowUpLeads mapTOFollowUpDto(Task task) {
		FollowUpLeads followUpLeads = new FollowUpLeads();
		followUpLeads.setClientEmail(task.getClientEmail());
		followUpLeads.setClientName(task.getClientName());
		followUpLeads.setNextFollowUpDate(task.getNextFollowUpDate());
		followUpLeads.setOurContactSource(task.getOurContactSource());
		return followUpLeads;
	}

	public Map getTaskByUser(String followUpDate) {

		List<Task> taskList = taskRepository.findByNextFollowUpDate(followUpDate);

		// Group tasks by users
		Map<User, List<Task>> tasksByUser = taskList.stream()
				.filter(task -> task.getUsers() != null && !task.getUsers().isEmpty())
				.flatMap(task -> task.getUsers().stream().map(user -> new AbstractMap.SimpleEntry<>(user, task)))
				.collect(Collectors.groupingBy(Map.Entry::getKey,
						Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

		// Tasks without users
		List<Task> tasksWithoutUser = taskList.stream()
				.filter(task -> task.getUsers() == null || task.getUsers().isEmpty()).collect(Collectors.toList());
		User user = new User();
		user.setName("Admin");
		user.setEmail("support@interestbudsolutions.com");
		tasksByUser.put(user, tasksWithoutUser);
		System.out.println("Task size: " + tasksByUser.size());
		return tasksByUser;
	}

	@Autowired
    private JavaMailSender mailSender;


    private void sendBackupEmail(String filePath, String date) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo("mukul@interestbudsolutions.com"); // Change to recipient's email
        helper.setSubject("Daily Database Backup "+date);
        helper.setText("Please find the attached database backup.",true);
		helper.setFrom("support@interestbudsolutions.com", "From IBSLeads Database Support");

        File backupFile = new File(filePath);
        helper.addAttachment(backupFile.getName(), backupFile);

        mailSender.send(message);
        System.out.println("Backup email sent successfully!");
    }

    @Scheduled(cron = "0 0 22 * * ?")  // Runs daily at 10 PM
	public void exportDatabaseLinux() {
        try {
            // Get current date in yyyy-MM-dd format
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            System.out.println("Timestamp: " + timestamp);

            // Backup file path
            String backupDir = "/opt/javaProjects/ibsInvoice/dbbackup/";
            String backupFilePath = backupDir + "IbsLeads_" + timestamp + ".sql";

            // Ensure backup directory exists
            new File(backupDir).mkdirs();

            // Construct the mysqldump command
            String command = "mysqldump -u root --password=mukul@Ibs2025# leads > " + backupFilePath + " 2>&1";

            // Execute the command
            Process process = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Database backup completed: " + backupFilePath);
                sendBackupEmail(backupFilePath, timestamp);
            } else {
                System.err.println("Database backup failed. Check for errors.");
            }

        } catch (IOException | InterruptedException | MessagingException e) {
            e.printStackTrace();
        }
    }

}
