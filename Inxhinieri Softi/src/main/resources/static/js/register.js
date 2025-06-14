const registerForm = document.getElementById('registerForm');

registerForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const firstNameRegex = /^[a-z ,.'-]+$/i;
    const lastNameRegex = /^[a-z ,.'-]+$/i;
    const usernameRegex = /^[a-zA-Z0-9_]{3,}$/;
    const emailRegex = /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$/;
    const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@$!%*?&.])[A-Za-z\d@$!%*?&.]{6,}$/;

    const firstName = document.getElementById('first-name').value.trim();
    const lastName = document.getElementById('last-name').value.trim();
    const username = document.getElementById('username').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();
    const confirmPassword = document.getElementById('confirm-password').value.trim();

    const firstNameError = document.getElementById('first-name-error');
    const lastNameError = document.getElementById('last-name-error');
    const usernameError = document.getElementById('username-error');
    const emailError = document.getElementById('email-error');
    const passwordError = document.getElementById('password-error');
    const confirmPasswordError = document.getElementById('confirm-password-error');

    firstNameError.style.display = 'none';
    lastNameError.style.display = 'none';
    usernameError.style.display = 'none';
    emailError.style.display = 'none';
    passwordError.style.display = 'none';
    confirmPasswordError.style.display = 'none';


    if (!firstName) {
        firstNameError.textContent = 'First name is required.';
        firstNameError.style.display = 'block';
        return;
    }
    if (!firstNameRegex.test(firstName)) {
        firstNameError.textContent = 'First name is invalid.';
        firstNameError.style.display = 'block';
        return;
    }

    if (!lastName) {
        lastNameError.textContent = 'Last name is required.';
        lastNameError.style.display = 'block';
        return;
    }
    if (!lastNameRegex.test(lastName)) {
        lastNameError.textContent = 'Last name is invalid.';
        lastNameError.style.display = 'block';
        return;
    }

    if (!username) {
        usernameError.textContent = 'Username is required.';
        usernameError.style.display = 'block';
        return;
    }
    if (!usernameRegex.test(username)) {
        usernameError.textContent = 'Username is invalid.';
        usernameError.style.display = 'block';
        return;
    }

    if (!email) {
        emailError.textContent = 'Email is required.';
        emailError.style.display = 'block';
        return;
    }
    if (!emailRegex.test(email)) {
        emailError.textContent = 'Email is invalid.';
        emailError.style.display = 'block';
        return;
    }

    if (!password) {
        passwordError.textContent = 'Password is required.';
        passwordError.style.display = 'block';
        return;
    }
    if (!passwordRegex.test(password)) {
        passwordError.textContent = 'Password is invalid.';
        passwordError.style.display = 'block';
        return;
    }

    if (!confirmPassword) {
        confirmPasswordError.textContent = 'Confirm password is required.';
        confirmPasswordError.style.display = 'block';
        return;
    }
    if (password !== confirmPassword) {
        confirmPasswordError.textContent = 'Passwords do not match.';
        confirmPasswordError.style.display = 'block';
        return;
    }

    try{
        const response = await fetch('http://localhost:8080/api/users/register', {
            method: 'POST', headers: {
                'Content-Type': 'application/json', 'Authorization': 'Basic ' + btoa("Admin" + ':' + "Test123!")
            }, body: JSON.stringify({firstName, lastName, username, email, password, confirmPassword , role: "ROLE_USER"}),
        });

        if (response.ok) {
            iziToast.success({
                title: 'Success',
                message: 'Register successful!',
                position: 'topCenter',
                timeout: 800,
                backgroundColor: 'linear-gradient(135deg, #b5e48c, #76c893)',
                titleColor: '#0f5000',
                messageColor: '#0f5000',
                pauseOnHover: false,
                onClosing: function() {
                    window.location.href = "/login";
                }
            });
        } else {
            const result = await response.json();
            iziToast.error({
                title: 'Error',
                message: result.message || 'Registration failed.',
                position: 'topCenter',
                timeout: 1500,
                backgroundColor: 'linear-gradient(135deg, #f44336, #e57373)',
                titleColor: '#ffffff',
                messageColor: '#ffffff',
                pauseOnHover: false
            });;
        }
    } catch (error) {
        iziToast.error({
            title: 'Error',
            message: 'An unexpected error occurred.',
            position: 'topCenter',
            timeout: 1500,
            backgroundColor: 'linear-gradient(135deg, #f44336, #e57373)',
            titleColor: '#ffffff',
            messageColor: '#ffffff',
            pauseOnHover: false
        });
    }
});
