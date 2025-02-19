
let currentPage = 0;
const pageSize = 50;
let currentSize = 50;
let sortOrder = 1; // 1 for ascending, -1 for descending
//Employee List url
const API_URL = window.location.origin + "/task/all";


//add new Employee Start
function addTask() {
	document.getElementById("taskTitle").textContent = "Add Task"
	document.getElementById("taskModal").style.display = "block";

	document.getElementById("id").value = document.getElementById("userId").value;
}

function closeEmployeePopup() {
	document.getElementById("taskModal").style.display = "none";
}

function submitForm() {
	const form = document.getElementById("taskForm");
	const formData = new FormData(form);
	const userDto = Object.fromEntries(formData.entries());

	fetch("/task/add", {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify(userDto),
	})
		.then((response) => response.json())
		.then((data) => {

			if (data.status === 200) {
				fetchTasks(currentPage);
				closeEmployeePopup();
				form.reset();
				showAlert("Task added sucessfully.");
			}
		})
		.catch((error) => {
			console.error("Error:", error);
		});
}
//add new Employee End//

async function fetchTasks(page = currentPage, size = currentSize) {
	try {

		var sourceType = document.getElementById('sourceType');
		if (sourceType != null) {
			sourceType = sourceType.value;
		} else {
			sourceType = '';
		}

		var requirementTyple = encodeURIComponent(document.getElementById('selectRequirementType').value);
		var selectStatus = encodeURIComponent(document.getElementById('selectStatus').value);
		var userId = encodeURIComponent(document.getElementById('userId').value);

		var url = `/task/emp-all/${userId}?page=${page}&size=${size}`;

		if (requirementTyple != '' && selectStatus != '' && sourceType != '') {
			url = url = `/task/by-user-source-requirement-status/` + userId + `/` + sourceType + `/` + requirementTyple + `/` + selectStatus;
		} else if (requirementTyple != '' && selectStatus != '') {
			url = url = `/task/by-user-requirement-type-status/${userId}/${requirementTyple}/${selectStatus}`;
		} else if (requirementTyple != '' && sourceType != '') {
			url = url = `/task/by-user-source-requirement-type/` + userId + `/` + sourceType + `/` + requirementTyple;
		} else if (selectStatus != '' && sourceType != '') {
			url = url = `/task/by-user-source-status/` + userId + `/` + sourceType + `/` + selectStatus;
		} else if (requirementTyple != '') {
			url = `/task/by-requirement-type/` + userId + `/` + requirementTyple;
		} else if (selectStatus != '') {
			url = url = `/task/by-user-status/${userId}/${selectStatus}`;
		} else if (sourceType != '') {
			url = url = `/task/source/${userId}/${sourceType}`;
		}

		/*if (selectRequirementType && selectStatus) {
			url = `/task/by-status-disposition/${userId}/${selectRequirementType}/${selectStatus}?page=${page}&size=${size}`;
		} else if (selectRequirementType) {
			url = `/task/by-requirement-type/${userId}/${selectRequirementType}?page=${page}&size=${size}`;
		} else if (selectStatus) {
			url = `/task/by-status/${userId}/${selectStatus}?page=${page}&size=${size}`;
		} else {
			
			if (taskType == 'Linkedin') {
				
				url = url = `/task/source/${userId}/${taskType}`;
				
			} else {
				url = url = `/task/emp-all/${userId}`;
				
			}
		}*/
		const response = await fetch(url);
		const data = await response.json();

		// Populate table with task data
		const userTableBody = document.getElementById("userTableBody");
		userTableBody.innerHTML = ""; // Clear existing rows

		const statusOptions = [
			"Select Status",
			"Active",
			"Inactive",
			"Hold",
			"Closed"
		];

		const requirementType = [
			"Select Requirement Type",
			"App",
			"Website",
			"Marketing",
			"Others"
		];

		const source = [
			"Select",
			"Linkedin",
			"Email",
			"others"
		];
		if (data.tasks.length == 0) {
			userTableBody.style.textAlign = "center";
			userTableBody.innerText = "No Task assign for you."
		}

		data.tasks.forEach((task, index) => {
			const row = document.createElement("tr");

			// Generate the <select> dropdown with options
			const selectOptions = statusOptions.map(option => {
				const isSelected = task.status === option ? 'selected' : '';
				return `<option value="${option}" ${isSelected}>${option}</option>`;
			}).join("");

			const requirementTypeOptions = requirementType.map(option => {
				const isSelected = task.requirementType === option ? 'selected' : '';
				return `<option value="${option}" ${isSelected}>${option}</option>`;
			}).join("");

			const sourceOptions = source.map(option => {
				const isSelected = task.source === option ? 'selected' : '';
				return `<option value="${option}" ${isSelected}>${option}</option>`;
			}).join("");

			row.setAttribute("data-row", JSON.stringify(task));
			// <span ><a href="/task/emp/${task.id}" title="History" style="color: green"><i class="fa fa-info-circle" aria-hidden="true"></i></a></span>
			row.innerHTML = `
        <td class="srWidth">
            <input type="checkbox" class="rowCheckbox direction-left" data-task-id="${task.id}" />
            <span class="serialNumber">${page * size + index + 1}</span>
             <span ><a href="/task/emp/${task.id}" title="History" style="color: green"><i class="fa fa-info-circle" aria-hidden="true"></i></a></span>
        </td>
        <td data-column="clientName" onclick="confirmation('${task.clientName}','${task.id}','Client Name')">${task.clientName}</td>
        <td data-column="clientEmail" onclick="confirmation('${task.clientEmail}','${task.id}','Client Email')">${task.clientEmail}</td>
        <td data-column="clientPhoneNumber" onclick="confirmation('${task.clientPhoneNumber}','${task.id}','Client Phone Number')">${task.clientPhoneNumber || "-"}</td>
        <td data-column="clientWebsite" onclick="confirmation('${task.clientWebsite}','${task.id}','Client Website')">${task.clientWebsite || "-"}</td>
        <td data-column="clientCompanyName" onclick="confirmation('${task.clientCompanyName}','${task.id}','Client Company Name')">${task.clientCompanyName}</td>
        <td data-column="clientCountry" onclick="confirmation('${task.clientCountry}','${task.id}','Client Country')">${task.clientCountry || "-"}</td>
        <td data-column="ourContactSource" onclick="confirmation('${task.ourContactSource}','${task.id}','Our Contact Source')">${task.ourContactSource || "-"}</td>
        <td data-column="leadGenerationDate" onclick="dateUpdate('${task.leadGenerationDate}', '${task.id}', 'Lead Generation Date')">${task.leadGenerationDate || "-"}</td>
		 <td data-column="status">
				<select class="form-control statusDropdown" data-task-id="${task.id}" onchange="statusUpdate(this.value, '${task.id}', 'Status')">
					${selectOptions}
				</select>
			</td>
		 <td data-column="lastResponseDate" onclick="dateUpdate('${task.lastResponseDate}', '${task.id}', 'Last Response Date')">${task.lastResponseDate || "-"}</td>
		 <td data-column="source">
				<select class="form-control messageStatusDropdown" data-task-id="${task.id}" onchange="statusUpdate(this.value, '${task.id}', 'Source')">
					${sourceOptions}
				</select>
			</td>
		 <td data-column="requirementType">
				<select class="form-control dispositionDropdown" data-task-id="${task.id}" onchange="statusUpdate(this.value, '${task.id}', 'Requirement Type')">
					${requirementTypeOptions}
				</select>
			</td>
		 <td data-column="nextFollowUpDate" onclick="dateUpdate('${task.nextFollowUpDate}', '${task.id}', 'Next FollowUp Date')">${task.nextFollowUpDate || "-"}</td>
		 <td data-column="clientSourceURL" onclick="confirmation('${task.clientSourceURL}', '${task.id}', 'Client Source URL')">
    ${task.clientSourceURL ? task.clientSourceURL.split(' ').slice(0, 4).join(' ') + (task.clientSourceURL.split(' ').length > 4 ? '...' : '') : '-'}
</td>
		 <td data-column="comments" onclick="confirmation('${task.comments}', '${task.id}', 'Comment')">
    ${task.comments ? task.comments.split(' ').slice(0, 4).join(' ') + (task.comments.split(' ').length > 4 ? '...' : '') : '-'}
</td>
      `;
			userTableBody.appendChild(row);
			
			updateSelectClass(task.status, task.id);
		});

		// Update pagination buttons
		setupPagination(data.currentPage, data.totalPages);
	} catch (error) {
		console.error("Error fetching tasks:", error);
	}
}


function updateSelectClass(status, id) {
	
		var textColor = "";
		var selectElement = document.querySelector(`.statusDropdown[data-task-id="${id}"]`);
	switch (status) {
		case "Active":
    		selectElement.classList.add('active-status');
			break;
		case "Inactive":
			selectElement.classList.add('inactive-status');
			textColor = "lightblue";
			break;
		case "Hold":
			selectElement.classList.add('hold-status');
			textColor = "yellow";
			break;
		case "Closed":
			selectElement.classList.add('closed-status');
			textColor = "lightcoral";
			break;
		default:
			selectElement.classList.add('default-status');
			textColor = "black";
	}
	
	return textColor;
}

function statusUpdate(option, taskId, validateKey) {
	const payload = {
		id: taskId,
		message: option, // Convert to numbers if IDs are numeric
		validateKey: validateKey
	};

	fetch('/task/status', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(payload)
	})
		.then(response => response.json())
		.then(data => {
			fetchTasks(currentPage);
			showAlert(data.message);
		})
		.catch(error => {
			console.error('Error:', error);
		});
}

function updateEntriesPerPage(size) {
	currentSize = parseInt(size, 10); // Update entries per page
	currentPage = 0; // Reset to the first page
	fetchTasks(); // Fetch tasks with the new page size
}

function setupPagination(current, totalPages) {

	const paginationContainer = document.getElementById("paginationButtons");
	paginationContainer.innerHTML = ""; // Clear existing buttons

	// Previous button
	const prevButton = document.createElement("button");
	prevButton.textContent = "Previous";
	prevButton.className = current === 0 ? "disabled" : "";
	prevButton.disabled = current === 0;
	prevButton.addEventListener("click", () => fetchTasks(current - 1));
	paginationContainer.appendChild(prevButton);

	// Page numbers
	for (let i = 0; i < totalPages; i++) {
		const pageButton = document.createElement("button");
		pageButton.textContent = i + 1;
		pageButton.className = i === current ? "disabled" : "";
		pageButton.disabled = i === current;
		pageButton.addEventListener("click", () => fetchTasks(i));
		paginationContainer.appendChild(pageButton);
	}

	// Next button
	const nextButton = document.createElement("button");
	nextButton.textContent = "Next";
	nextButton.className = current === totalPages - 1 ? "disabled" : "";
	nextButton.disabled = current === totalPages - 1;
	nextButton.addEventListener("click", () => fetchTasks(current + 1));
	paginationContainer.appendChild(nextButton);
}

//filter Table
function filterTable(searchValue) {
	const userTableBody = document.getElementById("userTableBody");
	const rows = Array.from(userTableBody.rows);

	// Clear the "Select All" checkbox when the filter changes
	document.getElementById("selectAll").checked = false;

	if (searchValue.trim() === "") {
		// If search is empty, re-fetch and reload the default table data
		fetchTasks();
		return;
	}

	// Split the input value into individual filters based on spaces
	const filters = searchValue.trim().toLowerCase().split(/\s+/);

	rows.forEach((row) => {
		const rowData = JSON.parse(row.getAttribute("data-row") || "{}"); // Parse data-row JSON
		const rowContent = JSON.stringify(rowData).toLowerCase(); // Flatten row data for search

		// Check if the row matches all filters
		const matchesAllFilters = filters.every((filter) => rowContent.includes(filter));

		// Show or hide the row based on the filters
		row.style.display = matchesAllFilters ? "" : "none";
	});
}

// Table check box
function toggleSelectAll(selectAllCheckbox) {
	const userTableBody = document.getElementById("userTableBody");
	const visibleRows = Array.from(userTableBody.rows).filter(row => row.style.display !== "none");

	visibleRows.forEach(row => {
		const checkbox = row.querySelector(".rowCheckbox");
		if (checkbox) {
			checkbox.checked = selectAllCheckbox.checked;
		}
	});
}

function getSelectedRows() {
	const selectedCheckboxes = document.querySelectorAll(".rowCheckbox:checked");
	const selectedIds = Array.from(selectedCheckboxes).map(checkbox => checkbox.dataset.taskId);
	console.log("Selected Task IDs:", selectedIds);
}

//Table Filter



function sortTable(column) {
	const userTableBody = document.getElementById("userTableBody");
	const rows = Array.from(userTableBody.rows);

	// Check if the column is numeric or date
	const isNumericColumn = ['partnerRate', 'partnerNumber'].includes(column);
	const isDateColumn = ['newExpiryDate', 'policyIssuedDate'].includes(column);

	rows.sort((a, b) => {
		const cellA = a.querySelector(`[data-column="${column}"]`).innerText.trim();
		const cellB = b.querySelector(`[data-column="${column}"]`).innerText.trim();

		if (isNumericColumn) {
			// Numeric sorting
			return sortOrder * (parseFloat(cellA) - parseFloat(cellB));
		} else if (isDateColumn) {
			// Date sorting
			const dateA = new Date(cellA); // Convert to Date object
			const dateB = new Date(cellB);
			return sortOrder * (dateA - dateB); // Sort based on date comparison
		} else {
			// Alphabetic sorting
			return sortOrder * cellA.localeCompare(cellB);
		}
	});

	// Rebuild the table body with sorted rows
	userTableBody.innerHTML = "";
	rows.forEach((row, index) => {
		row.querySelector(".serialNumber").innerText = index + 1; // Update serial number
		userTableBody.appendChild(row);
	});

	// Toggle sort order for next click
	sortOrder *= -1;
}


//Edit Employee

function editEmployee(id) {
	fetch("/emp/edit/" + id, {
		method: "GET",
		headers: {
			"Content-Type": "application/json",
		},
	})
		.then((response) => {
			if (!response.ok) {
				throw new Error("Network response was not ok " + response.statusText);
			}
			return response.json();
		})
		.then((data) => {

			addTask();
			// Populate the form fields with the response data empTitle
			document.getElementById("taskTitle").textContent = "Update task deatils"
			document.getElementById("firstName").value = data.firstName || "";
			document.getElementById("lastName").value = data.lastName || "";
			document.getElementById("phone").value = data.phone || "";
			document.getElementById("email").value = data.email || "";
			document.getElementById("password").value = data.password || "";
			document.getElementById("country").value = data.country || "";
			//document.getElementById("countryCallingCode").value = data.countryCallingCode || "";
			document.getElementById("aadharNumber").value = data.aadharNumber || "";
			document.getElementById("empNo").value = data.empNo || "";
			document.getElementById("id").value = data.id || "";
		})
		.catch((error) => {
			console.error("Error fetching employee data:", error);
			alert("Failed to load employee details. Please try again.");
		});
}

function redirectToWhatsApp(mobileNumber, message) {
	if (!mobileNumber) {
		showAlert("Mobile number is missing.");
		return;
	}
	const whatsappUrl = `https://web.whatsapp.com/send?phone=` + mobileNumber + `&text=+` + message;
	window.open(whatsappUrl, '_blank');
}

//Message Update
function confirmation(text, id, validateKey) {
	const popup = document.getElementById("commentPopup");

	if (!popup) {
		console.error("Popup element not found!");
		return;
	}

	popup.style.display = "flex";

	const userComment = document.getElementById("userComment");
	if (userComment) {
		userComment.value = '';
		userComment.value = text || '';
	} else {
		console.error("User comment textarea not found!");
		return;
	}

	const confrmLable = document.getElementById("confrmLable");
	if (confrmLable) {
		confrmLable.innerHTML = `Enter ${validateKey} :`;
	} else {
		console.error("Confirmation label not found!");
		return;
	}

	const yesButton = document.getElementById("commentDelYes");
	const noButton = document.getElementById("commentDelNo");

	if (yesButton && noButton) {
		yesButton.replaceWith(yesButton.cloneNode(true));
		noButton.replaceWith(noButton.cloneNode(true));

		document.getElementById("commentDelYes").addEventListener("click", function() {
			const textData = document.getElementById("userComment").value;
			updateCommentAndMessage(textData, id, validateKey);
			popup.style.display = "none";
		});

		document.getElementById("commentDelNo").addEventListener("click", function() {
			if (userComment) userComment.value = '';
			popup.style.display = "none";
		});
	} else {
		console.error("Yes or No button not found!");
	}
}


function updateCommentAndMessage(messageText, taskId, validateKey) {
	const payload = {
		id: taskId,
		message: messageText,
		validateKey: validateKey
	};

	fetch('/task/update', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(payload)
	})
		.then(response => response.json())
		.then(data => {
			fetchTasks(currentPage);
			showAlert(data.message);
		})
		.catch(error => {
			console.error('Error:', error);
		});
}

function addTask() {
	document.getElementById("taskTitle").textContent = "Add Task"
	document.getElementById("taskModal").style.display = "block";
	document.getElementById("id").value = document.getElementById("userId").value;
}

function closeEmployeePopup() {
	document.getElementById("taskModal").style.display = "none";
}

function submitForm() {
	const form = document.getElementById("taskForm");
	const formData = new FormData(form);
	const userDto = Object.fromEntries(formData.entries());

	fetch("/task/add", {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify(userDto),
	})
		.then((response) => response.json())
		.then((data) => {
			console.log(data);
			showAlert(data.message);
			if (data.status === 200) {
				fetchTasks(currentPage);
				closeEmployeePopup();
				form.reset();
			}
		})
		.catch((error) => {
			console.error("Error:", error);
		});
}


function dateUpdate(currentDate, taskId, label) {
	// Remove any previously added date input to avoid duplicates
	const existingInput = document.getElementById("dynamicDateInput");
	if (existingInput) {
		existingInput.remove();
	}

	// Create a dynamic Flatpickr input field
	const dateInput = document.createElement("input");
	dateInput.type = "text"; // Flatpickr works with text input
	dateInput.id = "dynamicDateInput";
	dateInput.style.position = "absolute";
	dateInput.style.zIndex = "9999"; // Ensure it's on top
	dateInput.style.left = `${event.clientX}px`; // Position near the click
	dateInput.style.top = `${event.clientY}px`; // Position near the click

	document.body.appendChild(dateInput); // Append the input to the document

	// Initialize Flatpickr with custom format
	flatpickr(dateInput, {
		dateFormat: "m/d/Y", // Set the format to mm/dd/yyyy
		defaultDate: currentDate, // Pre-fill the current date if available
		onClose: function(selectedDates, dateStr) {
			if (dateStr) {
				console.log(`Selected Date: ${dateStr}`);
				console.log(`Task ID: ${taskId}`);
				console.log(`Label: ${label}`);

				updateCommentAndMessage(dateStr, taskId, label);

				// Update the clicked cell with the new date
				const cell = event.target;
				cell.textContent = dateStr;
			}

			// Remove the input element after selection
			dateInput.remove();
		},
	});

	// Automatically open the calendar
	dateInput.focus();
}