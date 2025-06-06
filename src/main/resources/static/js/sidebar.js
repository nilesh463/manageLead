document.addEventListener("DOMContentLoaded", () => {
	const sidebar = document.getElementById('sidebar');

	// Function to toggle the sidebar
	function toggleSidebar() {
		sidebar.classList.toggle('open');
	}

	// Attach the toggle function to the button
	const toggleButton = document.querySelector('.toggle-btn');
	toggleButton.addEventListener('click', toggleSidebar);

	const userDropdown = document.querySelector(".user-dropdown");

	// Toggle dropdown menu
	if (userDropdown) {
		userDropdown.addEventListener("click", () => {
			userDropdown.classList.toggle("open");
		});

		// Close dropdown when clicking outside
		document.addEventListener("click", (event) => {
			if (!userDropdown.contains(event.target)) {
				userDropdown.classList.remove("open");
			}
		});
	}
});

// Disable right-click
/*document.addEventListener('contextmenu', (event) => {
	event.preventDefault();
	alert('Right-click is disabled.');
});*/

// Disable keyboard shortcuts like Ctrl+C, Ctrl+V, etc.
/*document.addEventListener('keydown', (event) => {
	if (event.ctrlKey || event.metaKey) {
		const restrictedKeys = ['c', 'v', 'x', 'u', 's', 'p'];
		if (restrictedKeys.includes(event.key.toLowerCase())) {
			event.preventDefault();
			alert('Copying or inspecting content is disabled.');
		}
	}
});*/


//const alertClose = document.getElementById("alertClose");

function showAlert(message) {
	const alertPopup = document.getElementById("alertPopup");
	const alertMessage = document.getElementById("alertMessage");

	// Set the message content
	alertMessage.textContent = message;

	// Show the alert popup
	alertPopup.classList.add("active");
	alertPopup.classList.remove("hidden");

	// Automatically close the popup after 2 seconds
	setTimeout(() => {
		hideAlert();
	}, 2000); // 2000ms = 2 seconds
}

function hideAlert() {
	const alertPopup = document.getElementById("alertPopup");
	alertPopup.classList.remove("active");
	alertPopup.classList.add("hidden");
}

function toggleLoader() {
	let loader = document.getElementById("loader");
	//const popup = document.getElementById("loaderPopup");
	loader.style.display = loader.style.display === "none" ? "block" : "none";
	//popup.style.display = popup.style.display === "none" ? "block" : "none";
}


// Example: Hide the loader after 5 seconds
//setTimeout(toggleLoader, 5000);

document.addEventListener("DOMContentLoaded", function () {
    const copyButton = document.querySelector(".copyClientUrl");

    copyButton.addEventListener("click", function () {
        let dataToCopy = [];
        document.querySelectorAll("td[data-column='clientSourceURL']").forEach(td => {
            let text = td.textContent.trim();
            if (text !== "-") {
                dataToCopy.push(text);
            }
        });

        if (dataToCopy.length > 0) {
            const textToCopy = dataToCopy.join("\n");
            copyToClipboard(textToCopy);
        } else {
            alert("No data to copy.");
        }
    });

    function copyToClipboard(text) {
        navigator.clipboard.writeText(text)
            .then(() =>showAlert("Client Source URLs copied!"))
            .catch(err => console.error("Error copying text: ", err));
    }
});

