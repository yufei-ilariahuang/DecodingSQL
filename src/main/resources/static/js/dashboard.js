document.addEventListener('DOMContentLoaded', function() {
    // Check authentication status on page load
    checkAuthStatus();

    // Handle logout button if present
    const logoutLink = document.querySelector('a[href="/logout"]');
    if (logoutLink) {
        logoutLink.addEventListener('click', function(e) {
            e.preventDefault();
            logout();
        });
    }
});

// Function to check if user is authenticated
function checkAuthStatus() {
    console.log('Checking authentication status...');

    fetch('/api/auth/check', {
        method: 'GET',
        credentials: 'include' // Important for sending cookies
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Authentication check failed');
            }
            return response.json();
        })
        .then(data => {
            console.log('Auth check response:', data);

            if (!data.authenticated) {
                console.log('User is not authenticated, redirecting to login');
                window.location.href = '/auth';
            } else {
                console.log('User is authenticated as: ' + data.userRole);
                // You can update UI elements based on the user's role here if needed
            }
        })
        .catch(error => {
            console.error('Authentication check error:', error);
            // Redirect to login page on error
            window.location.href = '/auth';
        });
}

// Function to handle logout
function logout() {
    fetch('/logout', {
        method: 'GET',
        credentials: 'include'
    })
        .then(() => {
            // Redirect to home page after logout
            window.location.href = '/';
        })
        .catch(error => {
            console.error('Logout error:', error);
            // Redirect anyway
            window.location.href = '/';
        });
}