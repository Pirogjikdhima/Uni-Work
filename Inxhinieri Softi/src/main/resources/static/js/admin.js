const API_CALORIES_URL = "http://localhost:8080/calories";
const API_USERS_URL = "http://localhost:8080/api";
const AUTH_HEADER = 'Basic ' + btoa("Admin:Test123!");
const sessionData = document.getElementById('sessionData');
const username = sessionData.getAttribute('data-username');
const role = sessionData.getAttribute('data-role');
const adminPanel = document.getElementById('adminPanel');

function logout() {
    Swal.fire({
        title: 'Are you sure you want to log out?',
        text: 'You will be redirected to the logout page.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Yes, log out',
        cancelButtonText: 'Cancel',
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = "/logout";
        }
    });
}

function showSection(sectionId) {
    document.querySelectorAll('.content-section').forEach(section => {
        section.classList.remove('active');
    });
    document.getElementById(sectionId + '-section').classList.add('active');
}

function loadUsers() {
    fetch(`${API_USERS_URL}/users`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `${AUTH_HEADER}`,
        },
    })
        .then((response) => response.json())
        .then((users) => {
            if (users) {
                const table = $("#userTable").DataTable({
                    destroy: true,
                    data: users,
                    dom: '<"table-header"<"table-title"<"add-user-button">><"search"f>>rtip',
                    columns: [
                        {data: "id"},
                        {data: "firstName"},
                        {data: "lastName"},
                        {data: "username"},
                        {data: "email"},
                        {
                            data: "id",
                            render: function (data) {
                                return `
                                    <button class="action-btn edit-btn" onclick="editUser(${data})">
                                        <i class="fas fa-edit"></i> Edit
                                    </button>
                                    <button class="action-btn delete-btn" onclick="deleteUser(${data})">
                                        <i class="fas fa-trash"></i> Delete
                                    </button>
                                `;
                            },
                        },
                    ],
                    pageLength: 10,
                    order: [[0, "asc"]],
                    responsive: true,
                    language: {
                        search: "",
                        searchPlaceholder: "Search users...",
                        lengthMenu: "Show _MENU_ users per page",
                        info: "Showing _START_ to _END_ of _TOTAL_ users",
                        emptyTable: "No users found",
                    },
                    columnDefs: [
                        {
                            targets: -1,
                            orderable: false,
                            searchable: false,
                        },
                    ],
                    initComplete: function () {
                        $("div.add-user-button").html(`
                            <button class="action-btn add-btn" onclick="addUser()">
                                <i class="fas fa-plus"></i> Add User
                            </button>
                        `);
                    }
                });
            } else {
                iziToast.error({
                    title: 'Error',
                    message: 'Failed to load users.',
                    position: 'topRight',
                    timeout: 1500,
                    backgroundColor: 'linear-gradient(135deg, #f44336, #e57373)',
                    titleColor: '#ffffff',
                    messageColor: '#ffffff',
                    pauseOnHover: false
                });
            }
        })
        .catch((error) => {
            iziToast.error({
                title: 'Error',
                message: 'Error:'+ error,
                position: 'topRight',
                timeout: 1500,
                backgroundColor: 'linear-gradient(135deg, #f44336, #e57373)',
                titleColor: '#ffffff',
                messageColor: '#ffffff',
                pauseOnHover: false
            });
        });
}

function loadFoodEntries() {
    fetch(`${API_CALORIES_URL}/data`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `${AUTH_HEADER}`,
        },
    })
        .then((response) => response.json())
        .then((responseData) => {
            if (responseData) {
                const table = $("#entriesTable").DataTable({
                    destroy: true,
                    data: responseData,
                    dom: '<"table-header"<"table-title"<"add-button">><"search"f>>rtip',
                    columns: [
                        {data: "username"},
                        {data: "foodName"},
                        {data: "calories"},
                        {
                            data: "price",
                            render: function (data) {
                                return `$${data.toFixed(2)}`;
                            }
                        },
                        {
                            data: "dateTime",
                            render: function (data) {
                                const date = new Date(data);
                                const year = date.getFullYear();
                                const month = String(date.getMonth() + 1).padStart(2, '0');
                                const day = String(date.getDate()).padStart(2, '0');
                                const hours = String(date.getHours()).padStart(2, '0');
                                const minutes = String(date.getMinutes()).padStart(2, '0');
                                const seconds = String(date.getSeconds()).padStart(2, '0');

                                return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
                            }
                        },
                        {
                            data: null,
                            render: function (data) {
                                return `
                                    <button class="action-btn edit-btn" onclick="editEntry(${data.id}, '${data.username}','${data.foodName}', '${data.calories}','${data.price}', '${data.dateTime}')">
                                        <i class="fas fa-edit"></i> Edit
                                    </button>
                                    <button class="action-btn delete-btn" onclick="deleteEntry(${data.id}, '${data.username}')">
                                        <i class="fas fa-trash"></i> Delete
                                    </button>
                                `;
                            },
                        },
                    ],
                    pageLength: 10,
                    order: [[4, "desc"]],
                    responsive: true,
                    language: {
                        search: "",
                        searchPlaceholder: "Search entries...",
                        lengthMenu: "Show _MENU_ entries per page",
                        info: "Showing _START_ to _END_ of _TOTAL_ entries",
                        emptyTable: "No entries found",
                    },
                    columnDefs: [{
                        targets: -1,
                        orderable: false,
                        searchable: false,
                    }],
                    initComplete: function () {
                        $("div.add-button").html(`
                            <button class="action-btn add-btn" onclick="addEntry()">
                                <i class="fas fa-plus"></i> Add Entry
                            </button>
                        `);
                    }
                });
            } else {
                iziToast.error({
                    title: 'Error',
                    message: "Failed to load food entries.",
                    position: 'topRight',
                    timeout: 1500,
                    backgroundColor: 'linear-gradient(135deg, #f44336, #e57373)',
                    titleColor: '#ffffff',
                    messageColor: '#ffffff',
                    pauseOnHover: false
                });
            }
        })
        .catch((error) => {
            iziToast.error({
                title: 'Error',
                message: 'Error:'+ error,
                position: 'topRight',
                timeout: 1500,
                backgroundColor: 'linear-gradient(135deg, #f44336, #e57373)',
                titleColor: '#ffffff',
                messageColor: '#ffffff',
                pauseOnHover: false
            });
        });
}

function deleteUser(userID) {
    Swal.fire({
        title: "Are you sure you want to delete this User?",
        text: "The account will be lost forever.",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Yes, delete it!",
        cancelButtonText: "No, keep it",
        customClass: {
            confirmButton: 'swal-confirm-button',
            cancelButton: 'swal-cancel-button'
        }
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`${API_USERS_URL}/users/${userID}`, {
                method: 'DELETE', headers: {
                    'Content-Type': 'application/json',
                    "Authorization": `${AUTH_HEADER}`,
                },
            })
                .then((response) => {
                    if (response.ok) {
                        Swal.fire({
                            title: "User deleted successfully!", icon: "success"
                        }).then(() => {
                            loadUsers();
                        });
                    } else {
                        Swal.fire({
                            title: data.message || "An error occurred.", icon: "error"
                        });
                    }
                })
                .catch((error) => {
                    Swal.fire({
                        title: "Failed to Delete User.", icon: "error"
                    });
                });
        }

    });
}

function addUser() {
    Swal.fire({
        title: 'Add New User',
        html: `
            <input id="swal-first-name" class="swal2-input" placeholder="First Name">
            <input id="swal-last-name" class="swal2-input" placeholder="Last Name">
            <input id="swal-username" class="swal2-input" placeholder="Username">
            <input id="swal-email" class="swal2-input" placeholder="Email">
            <input id="swal-password" type="password" class="swal2-input" placeholder="Password">
            <input id="swal-role" class="swal2-input" placeholder="Role">
        `,
        focusConfirm: false,
        preConfirm: () => {
            const firstName = document.getElementById('swal-first-name').value.trim();
            const lastName = document.getElementById('swal-last-name').value.trim();
            const username = document.getElementById('swal-username').value.trim();
            const email = document.getElementById('swal-email').value.trim();
            const password = document.getElementById('swal-password').value.trim();
            const role = document.getElementById('swal-role').value.trim();

            if (!firstName || !lastName || !username || !email || !password || !role) {
                Swal.showValidationMessage('Please fill out all fields.');
                return false;
            }

            return { firstName, lastName, username, email, password, role };
        }
    }).then(async (result) => {
        if (result.isConfirmed) {
            const { firstName, lastName, username, email, password, role } = result.value;

            try {
                const response = await fetch(`${API_USERS_URL}/users/register`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `${AUTH_HEADER}`
                    },
                    body: JSON.stringify({ firstName, lastName, username, email, password, confirmPassword: password, role }),
                });

                if (response.ok) {
                    Swal.fire('Success', 'User added successfully!', 'success').then(() => {
                        loadUsers();
                    });
                } else {
                    const result = await response.json();
                    Swal.fire('Error', result.message || 'Failed to add user.', 'error');
                }
            } catch (error) {
                Swal.fire('Error', 'An unexpected error occurred.', 'error');
            }
        }
    });
}

function editUser(userID) {
    fetch(`${API_USERS_URL}/users/${userID}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `${AUTH_HEADER}`,
        },
    })
        .then((response) => response.json())
        .then((userData) => {
            if (userData) {
                Swal.fire({
                    title: 'Edit User',
                    html: `
                    <input id="swal-input-firstName" class="swal2-input" placeholder="First Name" value="${userData.firstName}">
                    <input id="swal-input-lastName" class="swal2-input" placeholder="Last Name" value="${userData.lastName}">
                    <input id="swal-input-username" class="swal2-input" placeholder="Username" value="${userData.username}">
                    <input id="swal-input-email" class="swal2-input" type="email" placeholder="Email" value="${userData.email}">
                    <input id="swal-input-password" class="swal2-input" type="password" placeholder="New Password">
                    <input id="swal-input-confirmPassword" class="swal2-input" type="password" placeholder="Confirm Password">
                `,
                    focusConfirm: false,
                    preConfirm: () => {
                        const firstName = document.getElementById('swal-input-firstName').value;
                        const lastName = document.getElementById('swal-input-lastName').value;
                        const username = document.getElementById('swal-input-username').value;
                        const email = document.getElementById('swal-input-email').value;
                        const password = document.getElementById('swal-input-password').value;
                        const confirmPassword = document.getElementById('swal-input-confirmPassword').value;

                        if (!password || !confirmPassword) {
                            Swal.showValidationMessage('Password and confirm password are required.');
                            return false;
                        }

                        if (password !== confirmPassword) {
                            Swal.showValidationMessage('Passwords do not match.');
                            return false;
                        }

                        return {firstName, lastName, username, email, password, confirmPassword};
                    },
                    showCancelButton: true,
                    confirmButtonText: 'Save Changes',
                }).then((result) => {
                    if (result.isConfirmed) {
                        const updatedUser = {
                            firstName: result.value.firstName || userData.firstName,
                            lastName: result.value.lastName || userData.lastName,
                            username: result.value.username || userData.username,
                            email: result.value.email || userData.email,
                            password: result.value.password,
                            confirmPassword: result.value.confirmPassword,
                        };

                        fetch(`${API_USERS_URL}/users/${userID}`, {
                            method: 'PUT',
                            headers: {
                                "Content-Type": "application/json",
                                "Authorization": `${AUTH_HEADER}`,
                            },
                            body: JSON.stringify(updatedUser)
                        })
                            .then((response) => {
                                if (response.ok) {
                                    Swal.fire({
                                        title: "User updated successfully!", icon: "success"
                                    }).then(() => {
                                        loadUsers();
                                    });
                                } else {
                                    Swal.fire({
                                        title: "An error occurred.", icon: "error"
                                    });
                                }
                            })
                            .catch((error) => {
                                Swal.fire({
                                    title: "Failed to update user.", text: error.message, icon: "error"
                                });
                            });
                    }
                });
            } else {
                Swal.fire({
                    title: "User not found", icon: "error"
                });
            }
        })
        .catch((error) => {
            Swal.fire({
                title: "Error fetching user data.", text: error.message, icon: "error"
            });
        });
}

function deleteEntry(entryId, entryUsername) {
    Swal.fire({
        title: "Are you sure you want to delete this entry?",
        text: "The data will be lost forever.",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Yes, delete it!",
        cancelButtonText: "No, keep it",
        customClass: {
            confirmButton: 'swal-confirm-button',
            cancelButton: 'swal-cancel-button'
        }
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`${API_CALORIES_URL}/delete/${entryId}?username=${entryUsername}`, {
                method: 'DELETE',
                headers: {
                    "Authorization": `${AUTH_HEADER}`
                }
            })
                .then((response) => {
                    if (response.ok || response.status === 204) {
                        Swal.fire({
                            title: "Entry deleted successfully!", icon: "success"
                        }).then(() => {
                            loadFoodEntries();
                            updateReports();
                        });

                    } else {
                        Swal.fire({
                            title:"An error occurred.",
                            text: data.message,
                            icon: "error"
                        });
                    }
                })
                .catch((error) => {
                    Swal.fire({
                        title: "Failed to Delete Entry.",
                        text: error.message,
                        icon: "error"
                    });
                });
        }

    });
}

function addEntry() {
    Swal.fire({
        title: 'Add Food Entry',
        html: `
            <input id="swal-input-userName" class="swal2-input" placeholder="Username">
            <input id="swal-input-foodName" class="swal2-input" placeholder="Food Name">
            <input id="swal-input-calories" class="swal2-input" type="number" placeholder="Calories">
            <input id="swal-input-price" class="swal2-input" type="number" step="0.01" placeholder="Price">
            <input id="swal-input-dateTime" class="swal2-input" type="datetime-local" placeholder="Date Time">
        `,
        focusConfirm: false,
        preConfirm: () => {
            const userName = document.getElementById('swal-input-userName').value;
            const foodName = document.getElementById('swal-input-foodName').value;
            const calories = document.getElementById('swal-input-calories').value;
            const price = document.getElementById('swal-input-price').value;
            const dateTime = document.getElementById('swal-input-dateTime').value;
            if (!userName || !foodName || !calories || !price) {
                Swal.showValidationMessage('Please enter all fields');
                return false;
            }
            return { userName, foodName, calories, price, dateTime };
        }
    }).then((result) => {
        if (result.isConfirmed) {
            const entry = {
                foodName: result.value.foodName,
                calories: parseInt(result.value.calories),
                price: parseFloat(result.value.price),
                dateTime: result.value.dateTime ? new Date(new Date(result.value.dateTime).setHours(new Date(result.value.dateTime).getHours() + 1)).toISOString() : null
            };

            fetch(`${API_CALORIES_URL}/add?username=${result.value.userName}`, {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `${AUTH_HEADER}`
                },
                body: JSON.stringify(entry)
            })
                .then((response) => {
                    if (response.ok) {
                        Swal.fire({
                            title: "Entry added successfully!", icon: "success"
                        }).then(() => {
                            loadFoodEntries();
                            updateReports();
                        });
                    } else {
                        Swal.fire({
                            title: "An error occurred.", icon: "error"
                        });
                    }
                })
                .catch((error) => {
                    Swal.fire({
                        title: "Failed to add entry.", icon: "error"
                    });
                });
        }
    });
}

function editEntry(entryId, entryUsername , entryFoodName, entryCalories, entryPrice, entryDateTime) {
    Swal.fire({
        title: 'Edit Food Entry',
        html: `
            <input id="swal-input-foodName" class="swal2-input" placeholder="Food Name">
            <input id="swal-input-calories" class="swal2-input" type="number" placeholder="Calories">
            <input id="swal-input-price" class="swal2-input" type="number" step="0.01" placeholder="Price">
            <input id="swal-input-dateTime" class="swal2-input" type="datetime-local" placeholder="Date Time">
        `,
        focusConfirm: false,
        preConfirm: () => {
            const foodName = document.getElementById('swal-input-foodName').value;
            const calories = document.getElementById('swal-input-calories').value;
            const price = document.getElementById('swal-input-price').value;
            const dateTime = document.getElementById('swal-input-dateTime').value;
            return {foodName, calories, price, dateTime };
        }
    }).then((result) => {
        if (result.isConfirmed) {
            const entry = {
                foodName: result.value.foodName || entryFoodName,
                calories: result.value.calories ? parseInt(result.value.calories) : entryCalories,
                price: result.value.price ? parseInt(result.value.price) : entryPrice,
                dateTime: result.value.dateTime ? new Date(new Date(result.value.dateTime).setHours(new Date(result.value.dateTime).getHours() + 1)).toISOString() : entryDateTime
            };

            fetch(`${API_CALORIES_URL}/update/${entryId}?username=${entryUsername}`, {
                method: 'PUT',
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `${AUTH_HEADER}`
                },
                body: JSON.stringify(entry)
            })
                .then((response) => {
                    if (response.ok) {
                        Swal.fire({
                            title: "Entry updated successfully!", icon: "success"
                        }).then(() => {
                            loadFoodEntries();
                            updateReports();
                        });
                    } else {
                        Swal.fire({
                            title: "An error occurred.", icon: "error"
                        });
                    }
                })
                .catch((error) => {
                    Swal.fire({
                        title: "Failed to update entry.", text: error.message, icon: "error"
                    });
                });
        }
    });
}

function initializeCharts() {

    const weeklyComparisonCtx = document.getElementById('weeklyComparisonChart').getContext('2d');
    new Chart(weeklyComparisonCtx, {
        type: 'bar',
        data: {
            labels: [],
            datasets: [
                {
                    label: 'This Week',
                    data: [],
                    backgroundColor: '#3b82f6'
                },
                {
                    label: 'Last Week',
                    data: [],
                    backgroundColor: '#93c5fd'
                }
            ]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });

    const averageCaloriesCtx = document.getElementById('averageCaloriesChart').getContext('2d');
    new Chart(averageCaloriesCtx, {
        type: 'bar',
        data: {
            labels: ['Average Calories'],
            datasets: [{
                label: 'Average Calories per User',
                data: [],
                backgroundColor: '#3b82f6'
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

function updateReports() {

    fetch(`${API_CALORIES_URL}/admin/report`)
        .then(response => response.json())
        .then(data => {

            const getDayOfWeek = (dateString) => {
                const date = new Date(dateString);
                return date.toLocaleDateString('en-US', {weekday: 'long'});
            };

            const allDaysOfWeek = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];

            const weeklyComparisonChart = Chart.getChart('weeklyComparisonChart');
            if (weeklyComparisonChart) {
                const thisWeekData = Object.keys(data.entriesThisWeek).reduce((acc, date) => {
                    acc[getDayOfWeek(date)] = data.entriesThisWeek[date];
                    return acc;
                }, {});

                const lastWeekData = Object.keys(data.entriesLastWeek).reduce((acc, date) => {
                    acc[getDayOfWeek(date)] = data.entriesLastWeek[date];
                    return acc;
                }, {});

                const thisWeekValues = allDaysOfWeek.map(day => thisWeekData[day] || 0);
                const lastWeekValues = allDaysOfWeek.map(day => lastWeekData[day] || 0);

                weeklyComparisonChart.data.labels = allDaysOfWeek;
                weeklyComparisonChart.data.datasets[0].data = thisWeekValues;
                weeklyComparisonChart.data.datasets[1].data = lastWeekValues;
                weeklyComparisonChart.update();
            }

            const userData = data['averageCaloriesPerUser'];

            const averageCaloriesChart = Chart.getChart('averageCaloriesChart');
            if (averageCaloriesChart) {
                const labels = Object.keys(userData);
                const values = Object.values(userData);

                averageCaloriesChart.data.labels = labels;
                averageCaloriesChart.data.datasets[0].data = values;
                averageCaloriesChart.update();
            }
            const priceExceededTable = document.getElementById('priceExceededTable');
            priceExceededTable.innerHTML = '';
            data.usersExceedingMonthlyLimit.forEach(user => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${user}</td>
                `;
                priceExceededTable.appendChild(row);
            });
        })
        .catch(error => {
            iziToast.error({
                title: 'Error',
                message: 'Error updating reports:' + error,
                position: 'topRight',
                timeout: 1500,
                backgroundColor: 'linear-gradient(135deg, #f44336, #e57373)',
                titleColor: '#ffffff',
                messageColor: '#ffffff',
                pauseOnHover: false
            });
        });
}

document.addEventListener('DOMContentLoaded', () => {
    adminPanel.textContent = `Admin Panel - ${username}`;
    loadUsers();
    loadFoodEntries();
    updateReports();
    initializeCharts();
    setInterval(() => {
        updateReports();
    }, 300000);
});