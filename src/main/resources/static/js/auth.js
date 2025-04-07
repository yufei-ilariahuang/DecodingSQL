// Static resource that should be placed in src/main/resources/static/js/auth.js

document.addEventListener('DOMContentLoaded', function() {
    // Tab switching functionality
    const loginTab = document.getElementById('loginTab');
    const registerTab = document.getElementById('registerTab');
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');

    if (loginTab && registerTab) {
        loginTab.addEventListener('click', function() {
            loginTab.classList.add('active');
            registerTab.classList.remove('active');
            loginForm.style.display = 'block';
            registerForm.style.display = 'none';
        });

        registerTab.addEventListener('click', function() {
            registerTab.classList.add('active');
            loginTab.classList.remove('active');
            registerForm.style.display = 'block';
            loginForm.style.display = 'none';
        });
    }

    // Role selection in registration form
    const roleOptions = document.querySelectorAll('.role-option');
    roleOptions.forEach(option => {
        option.addEventListener('click', function() {
            roleOptions.forEach(opt => opt.classList.remove('active'));
            this.classList.add('active');
            this.querySelector('input').checked = true;
        });
    });

    // Student login button
    const studentLoginBtn = document.getElementById('studentLoginBtn');
    if (studentLoginBtn) {
        studentLoginBtn.addEventListener('click', function() {
            console.log('Student login button clicked');
            attemptLogin('student');
        });
    }

    // Instructor login button
    const instructorLoginBtn = document.getElementById('instructorLoginBtn');
    if (instructorLoginBtn) {
        instructorLoginBtn.addEventListener('click', function() {
            attemptLogin('instructor');
        });
    }
    // Register button
    const registerBtn = document.getElementById('registerBtn');
    if (registerBtn) {
        registerBtn.addEventListener('click', function() {
            const username = document.getElementById('registerUsername').value;
            const email = document.getElementById('registerEmail').value;
            const password = document.getElementById('registerPassword').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            const userType = document.querySelector('input[name="userType"]:checked').value;
            const errorDiv = document.getElementById('registerError');

            // Basic validation
            if (password !== confirmPassword) {
                errorDiv.textContent = 'Passwords do not match.';
                errorDiv.style.display = 'block';
                return;
            }

            // Create request payload
            const registerData = {
                username: username,
                email: email,
                password: password,
                confirmPassword: confirmPassword,
                userType: userType
            };

            console.log(`Attempting to register ${userType}: ${username}`);

            // Clear previous errors
            errorDiv.style.display = 'none';

            // Send registration request
            fetch('/api/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(registerData)
            })
                .then(response => {
                    if (response.ok) {
                        return response.json().then(data => {
                            // Show success message and switch to login
                            alert('Registration successful! Please log in.');
                            loginTab.click();
                        });
                    } else {
                        return response.json().then(error => {
                            // Display error message
                            errorDiv.textContent = error.message || 'Registration failed. Please try again.';
                            errorDiv.style.display = 'block';
                        }).catch(err => {
                            // Handle non-JSON response
                            errorDiv.textContent = 'Registration failed. Please try again.';
                            errorDiv.style.display = 'block';
                        });
                    }
                })
                .catch(error => {
                    console.error('Registration error:', error);
                    errorDiv.textContent = 'An error occurred. Please try again later.';
                    errorDiv.style.display = 'block';
                });
        });
    }

    // Login function that handles different user types
    function attemptLogin(userType) {
        const email = document.getElementById('loginEmail').value;
        const password = document.getElementById('loginPassword').value;
        const errorDiv = document.getElementById('loginError');

        // Create request payload
        const loginData = {
            email: email,
            password: password,
            userType: userType
        };

        console.log(`Attempting ${userType} login for ${email}`);

        // Clear previous errors
        errorDiv.style.display = 'none';

        // Send login request
        fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(loginData),
            credentials: 'include' // Important for session cookies
        })
            .then(response => {
                if (response.ok) {
                    return response.json().then(data => {
                        // Redirect based on user type
                        window.location.href = '/dashboard';
                    });
                } else {
                    return response.json().then(error => {
                        // Display error message
                        errorDiv.textContent = error.message || `${userType} login failed. Please check your credentials.`;
                        errorDiv.style.display = 'block';
                    }).catch(err => {
                        // Handle non-JSON response
                        errorDiv.textContent = 'Login failed. Please check your credentials.';
                        errorDiv.style.display = 'block';
                    });
                }
            })
            .catch(error => {
                console.error('Login error:', error);
                errorDiv.textContent = 'An error occurred. Please try again later.';
                errorDiv.style.display = 'block';
            });
    }

    // Password validation for registration
    const registerPassword = document.getElementById('registerPassword');
    const confirmPassword = document.getElementById('confirmPassword');

    if (confirmPassword) {
        confirmPassword.addEventListener('input', function() {
            if (this.value !== registerPassword.value) {
                this.classList.add('is-invalid');
                if (!this.nextElementSibling || !this.nextElementSibling.classList.contains('error-message')) {
                    const errorMsg = document.createElement('div');
                    errorMsg.className = 'error-message';
                    errorMsg.textContent = 'Passwords do not match';
                    this.parentNode.appendChild(errorMsg);
                }
            } else {
                this.classList.remove('is-invalid');
                const errorMsg = this.nextElementSibling;
                if (errorMsg && errorMsg.classList.contains('error-message')) {
                    errorMsg.remove();
                }
            }
        });
    }

    // Floating elements animation
    const floatingElements = document.querySelectorAll('.floating-element');

    floatingElements.forEach(element => {
        // Random starting position
        const x = Math.random() * 10 - 5;
        const y = Math.random() * 10 - 5;

        // Random animation duration between 15-30s
        const duration = Math.random() * 15 + 15;

        // Apply animation
        element.style.animation = `float ${duration}s infinite ease-in-out`;
    });

    // Add keyframes for floating animation
    if (!document.querySelector('style[data-animation="float"]')) {
        const style = document.createElement('style');
        style.setAttribute('data-animation', 'float');
        style.innerHTML = `
        @keyframes float {
            0% { transform: translate(0, 0) rotate(0deg); opacity: 0.1; }
            25% { transform: translate(5px, 10px) rotate(3deg); opacity: 0.15; }
            50% { transform: translate(10px, 5px) rotate(5deg); opacity: 0.1; }
            75% { transform: translate(5px, -5px) rotate(2deg); opacity: 0.15; }
            100% { transform: translate(0, 0) rotate(0deg); opacity: 0.1; }
        }
    `;
        document.head.appendChild(style);
    }
});

// Toggle password visibility
function togglePassword(inputId) {
    const passwordInput = document.getElementById(inputId);
    const toggleButton = passwordInput.parentNode.querySelector('.password-toggle');
    const icon = toggleButton.querySelector('i');

    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
    } else {
        passwordInput.type = 'password';
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
    }
}