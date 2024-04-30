document.getElementById("registerButton").addEventListener("click", function() {
    // Create an object with the form data
    var formData = {
        username: document.getElementById("username").value,
        password: document.getElementById("password").value
    };

    // Make a fetch request
    fetch("/enmo_skill_backend_war_exploded/user", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            if (response.ok) {
                // Successful login (status code 200), redirect to UserRegister.js
                window.location.href = '/enmo_skill_backend_war_exploded/Login/login.html';
            } else if (response.status === 401) {
                // Unauthorized login (status code 401), display an error message
                console.log('Registration unsuccessful');
            } else {
                // Handle other status codes or errors
                console.error('Error:', response.status);
            }
        })
        .catch(error => {
            // Handle network errors or other exceptions
            console.error("An error occurred:", error);
        });
});