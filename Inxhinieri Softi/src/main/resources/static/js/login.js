const loginForm = document.getElementById('loginForm');

loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
 // Perdor regex patterns per validimin e te dhenave
    const usernameRegex = /^[a-zA-Z0-9_]{3,}$/;
    const emailRegex = /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$/;
    const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*.])[A-Za-z\d!@#$%^&*.]{6,}$/;

    const identifierError = document.getElementById('identifier-error');
    const passwordError = document.getElementById('password-error');

    //Merr inputin nga user-i ne formen e login-it
    const identifier = document.getElementById('loginIdentifier').value.trim();
    const password = document.getElementById('loginPassword').value.trim();

    identifierError.style.display = 'none';
    passwordError.style.display = 'none';

//Validimi i inputit (email ose username)
    if (!identifier) {
        identifierError.textContent = 'Username or Email is required.';
        identifierError.style.display = 'block';
        return;
    }
    if (!usernameRegex.test(identifier) && !emailRegex.test(identifier)) {
        identifierError.textContent = 'Username or Email is invalid.';
        identifierError.style.display = 'block';
        return;
    }
//Validimi i passwordit
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
    //Dergimi i POST Request ne login API ne backend
    try {
        const response = await fetch('http://localhost:8080/api/auth/login', {
            method: 'POST', headers: {
                'Content-Type': 'application/json', 'Authorization': 'Basic ' + btoa("Admin" + ':' + "Test123!")
            }, body: JSON.stringify({identifier, password}),
        });


        if (response.ok) {
            const result = await response.json();
            const role = result.role;

            iziToast.success({
                title: 'Success',
                message: 'Login successful!',
                position: 'topCenter',
                timeout: 800,
                backgroundColor: 'linear-gradient(135deg, #b5e48c, #76c893)',
                titleColor: '#0f5000',
                messageColor: '#0f5000',
                pauseOnHover: false,
                onClosing: function() {
                    //E con perdoruesin ne faqen perkatese sipas rolit(admin ose user)
                    if (role === "ROLE_ADMIN") {
                        window.location.href = "/admin";
                    } else if (role === "ROLE_USER") {
                        window.location.href = "/user";
                    }
                }
            });
        } else {
            const result = await response.json();
            iziToast.error({
                title: 'Error',
                message: result.message || 'Login failed.',
                position: 'topCenter',
                timeout: 1500,
                backgroundColor: 'linear-gradient(135deg, #f44336, #e57373)',
                titleColor: '#ffffff',
                messageColor: '#ffffff',
                pauseOnHover: false
            });
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
