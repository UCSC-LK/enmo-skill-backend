document.getElementById("loginForm").addEventListener("submit", function(event) {
    event.preventDefault(); // Prevent the default form submission

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const messageDiv = document.getElementById("messageDiv");

    // Create a JavaScript object representing the user data
    const userData = {
        username: username,
        password: password
    };

    // Convert the JavaScript object to JSON
    const jsonData = JSON.stringify(userData);

    // Send a POST request with JSON data to your backend
    fetch('/enmo_skill_backend_war_exploded/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: jsonData
    })
        .then(response => {
            if (response.ok) {
                // Successful login (status code 200), parse the JSON response and display the success message
                response.json().then((data) => {
                    console.log('Message content:', data.message);
                    messageDiv.innerHTML  = 'Login successful: ' + data.message;
                });
                // Successful login (status code 200), redirect to UserRegister.js
                window.location.href = '/enmo_skill_backend_war_exploded/Register/register.html';
            } else if (response.status === 401) {
                // Unauthorized login (status code 401), display an error message
                response.json().then((data) => {
                    console.log('Error message content:', data.message);
                    messageDiv.innerHTML  = 'Login unsuccessful: ' + data.message;
                });
                console.log('Login unsuccessful');
            } else {
                // Handle other status codes or errors
                console.error('Error:', response.status);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            messageDiv.innerHTML =  error;
        });
});
