// Function to fetch and populate data
async function fetchTaskDetails(taskId) {
	try {
		
		const response = await fetch(`/task/details/${taskId}`);
		if (response.ok) {
			const data = await response.json();
			console.log(data);
			// Populate form fields
			document.getElementById('clientName').value = data.clientName || '';
			document.getElementById('clientEmail').value = data.clientEmail || '';
			document.getElementById('clientPhoneNumber').value = data.clientPhoneNumber || '';
			document.getElementById('clientWebsite').value = data.clientWebsite || '';
			document.getElementById('clientCompanyName').value = data.clientCompanyName || '';
			document.getElementById('requirementType').value = data.requirementType || '';
			document.getElementById('source').value = data.source || '';
			document.getElementById('clientCountry').value = data.clientCountry || '';
			document.getElementById('ourContactSource').value = data.ourContactSource || '';
			document.getElementById('leadGenerationDate').value = data.leadGenerationDate || '';
			document.getElementById('lastResponseDate').value = data.lastResponseDate || '';
			document.getElementById('nextFollowUpDate').value = data.nextFollowUpDate || '';
			document.getElementById('comments').value = data.comments || '';
			document.getElementById('status').value = data.status || '';
			document.getElementById('id').value = data.id || '';
		} else {
			console.error('Failed to fetch task details:', response.statusText);
		}
	} catch (error) {
		console.error('Error fetching task details:', error);
	}
}

// Helper function to get the task ID from the URL
function getTaskIdFromURL() {
	const urlParams = new URLSearchParams(window.location.search);
	return urlParams.get('id'); // Adjust 'id' to match the query parameter name in your URL
}


function closeHistory() {
	document.getElementById("taskModal").style.display = "none";
}

async function renderTaskHistory() {
	
	const taskId =  document.getElementById('id').value;
	//document.getElementById("taskTitle").textContent = "History"
	document.getElementById("taskModal").style.display = "block";

	const response = await fetch(`/task-history/${taskId}`);
	const data = await response.json();

	const container = document.getElementById('history-container');
	const history = data.taskHistory;
	
	history.forEach(entry => {
		
		// Create a section for each date
		const dateSection = document.createElement('div');
		dateSection.classList.add('date-section');

		// Add date header
		const dateHeader = document.createElement('div');
		dateHeader.classList.add('date-header');
		dateHeader.textContent = `Date: ${entry.date}`;
		dateSection.appendChild(dateHeader);

		// Create a table for the updated fields
		const table = document.createElement('table');
		const thead = document.createElement('thead');
		const tbody = document.createElement('tbody');

		// Add table headers
		thead.innerHTML = `
                    <tr>
                        <th>Field Name</th>
                        <th>Old Value</th>
                        <th>New Value</th>
                    </tr>
                `;

		// Add table rows for updated fields
		entry.updatedFields.forEach(field => {
			const row = document.createElement('tr');

			const fieldNameCell = document.createElement('td');
			fieldNameCell.textContent = field.fieldName;

			const oldValueCell = document.createElement('td');
			oldValueCell.textContent = field.oldValue || 'N/A';

			const newValueCell = document.createElement('td');
			newValueCell.textContent = field.newValue;
			newValueCell.classList.add('highlight');

			row.appendChild(fieldNameCell);
			row.appendChild(oldValueCell);
			row.appendChild(newValueCell);

			tbody.appendChild(row);
		});

		table.appendChild(thead);
		table.appendChild(tbody);
		dateSection.appendChild(table);

		container.appendChild(dateSection);
	});
}
