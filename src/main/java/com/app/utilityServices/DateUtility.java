package com.app.utilityServices;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class DateUtility {

	// Convert yyyy-mm-dd to MM/dd/yyyy
	public static String convertToMMDDYYYY(String inputDate) throws ParseException {
		if (inputDate != null && inputDate != "") {
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

	        // Parse the input date string to LocalDate
	        LocalDate date = LocalDate.parse(inputDate, inputFormatter);

	        // Format the date into the desired format
	        return date.format(outputFormatter);
		} else {
			return "";
		}
	}

	// Convert MM/dd/yyyy to dd-MM-yyyy
	public static String convertToDDMMYYYY(String date) throws ParseException {
		if (date != null && date != "") {

			SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
			SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date parsedDate = inputFormat.parse(date);
			return outputFormat.format(parsedDate);
		} else {
			return "";
		}
	}

	public static String getCurrentDateFormatted() {
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		return currentDate.format(formatter);
	}

}
