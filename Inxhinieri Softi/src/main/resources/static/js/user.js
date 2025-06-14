// URL baze per API dhe header-i i autorizimit
const API_BASE_URL = "http://localhost:8080/calories";
const AUTH_HEADER = 'Basic ' + btoa("Admin:Test123!");

// Marrja e te dhenave nga login-i
const sessionData = document.getElementById('sessionData');
const username = sessionData.getAttribute('data-username');
const role = sessionData.getAttribute('data-role');
const userPanel = document.getElementById('userPanel');

let chartInstance = null;
let dashInstance = null;

document.getElementById('foodForm').onsubmit = function (e) {
    e.preventDefault();

    const entry = {
        username: username,
        dateTime: new Date().toISOString(),
        foodName: document.getElementById('foodName').value,
        calories: parseInt(document.getElementById('calories').value),
        price: parseFloat(document.getElementById('price').value),
    };

    fetch(`${API_BASE_URL}/add?username=${entry.username}`, {
        method: 'POST',
        headers: getAuthHeaders(),
        body: JSON.stringify(entry),
    })
        .then(response => response.json())
        .then(data => {

            addFoodEntryToUI(data);
            calorieWarning();
            expenditureWarning();
            updateCalorieLimitTable();
            closeModal();

            this.reset();
            window.href.location="/user";

        })
};

function getAuthHeaders() {
    return {
        'Content-Type': 'application/json', 'Authorization': AUTH_HEADER
    };
}

function showSection(sectionId) {
    document.querySelectorAll('.content-section').forEach(section => {
        section.classList.remove('active');
    });
    document.getElementById(sectionId + '-section').classList.add('active');
}

function openModal() {
    document.getElementById('modal').style.display = 'block';
}

function closeModal() {
    document.getElementById('modal').style.display = 'none';
}

function addFoodEntryToUI(entry) {
    const container = document.getElementById('foodEntries');
    const div = document.createElement('div');

    let parsedEntry;
    try {
        parsedEntry = typeof entry === 'string' ? JSON.parse(entry) : entry;
    } catch (error) {
        console.error('Error parsing entry:', error);
        parsedEntry = entry;
    }

    const foodName = parsedEntry.foodName || 'N/A';
    const dateTime = parsedEntry.dateTime ? new Date(parsedEntry.dateTime) : new Date();
    const formattedDate = dateTime.toLocaleDateString('en-GB', {
        day: '2-digit',
        month: 'short',
        year: 'numeric'
    });
    const formattedTime = dateTime.toLocaleTimeString('en-GB', {
        hour: '2-digit',
        minute: '2-digit'
    });
    const calories = parsedEntry.calories !== undefined ? parsedEntry.calories : 'N/A';
    const price = parsedEntry.price !== undefined ? parsedEntry.price : 'N/A';

    div.className = 'food-entry';
    div.innerHTML = `
        <div class="food-entry-icon">
            <i class="fas fa-utensils"></i>
        </div>
        <div class="food-entry-content">
            <div class="food-entry-header">
                <h3 class="food-name">${foodName}</h3>
                <div class="food-entry-date">
                    <i class="far fa-calendar"></i> ${formattedDate}
                    <i class="far fa-clock"></i> ${formattedTime}
                </div>
            </div>
            <div class="food-entry-details">
                <div class="detail-item">
                    <i class="fas fa-fire"></i>
                    <span>${calories} calories</span>
                </div>
                <div class="detail-item">
                    <i class="fas fa-euro-sign"></i>
                    <span>${price.toFixed(2)}</span>
                </div>
            </div>
        </div>
    `;

    container.insertBefore(div, container.firstChild);
}

function filterByDate() {
    const startDate = document.getElementById('startDate').value + 'T00:00:00';
    const endDate = document.getElementById('endDate').value + 'T23:59:59';

    fetch(`${API_BASE_URL}/user/${username}/filter-calories-data?fromDate=${startDate}&toDate=${endDate}`, {
        headers: getAuthHeaders()
    })
        .then(response => response.json())
        .then(data => {
            if (!Array.isArray(data)) {
                iziToast.error({
                    title: 'Error',
                    message: 'Invalid data format received.',
                    position: 'topRight',
                });
                return;
            }

            const dates = {};
            const start = new Date(startDate);
            const end = new Date(endDate);

            for (let d = start; d <= end; d.setDate(d.getDate() + 1)) {
                const dateStr = d.toLocaleDateString('en-GB');
                dates[dateStr] = 0;
            }

            data.forEach(entry => {
                const date = new Date(entry.dateTime).toLocaleDateString('en-GB');
                if (dates[date] !== undefined) {
                    dates[date] += entry.calories;
                }
            });

            const labels = Object.keys(dates);
            const caloriesData = Object.values(dates);

            const ctx = document.getElementById('customChart').getContext('2d');

            if (chartInstance) {
                chartInstance.destroy();
            }

            chartInstance = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Calories',
                        data: caloriesData,
                        backgroundColor: 'rgba(75, 192, 192, 0.2)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    },
                    plugins: {
                        legend: { position: 'top' }
                    }
                }
            });
        })
        .catch(() => {
            iziToast.error({
                title: 'Error',
                message: 'Failed to filter data. Please check the date range.',
                position: 'topRight',
            });
        });
}

function getTodayCalories() {
    fetch(`${API_BASE_URL}/user/${username}`, {
        headers: getAuthHeaders()
    })
        .then(response => response.json())
        .then(data => {
            if (typeof data === 'string') {
                document.getElementById('todayCalories').innerText = '0 cal';
                document.getElementById('weeklyAverage').innerText = '0 cal';
                return;
            }

            data.forEach(entry => addFoodEntryToUI(entry));

            const today = new Date().toISOString().split('T')[0];
            const todayCalories = data
                .filter(entry => entry.dateTime.startsWith(today))
                .reduce((sum, entry) => sum + entry.calories, 0);
            document.getElementById('todayCalories').innerText = `${todayCalories} cal`;
        })
        .catch(() => {
            iziToast.error({
                title: 'Error',
                message: 'Failed to load today’s calorie data.',
                position: 'topRight',
            });
            document.getElementById('todayCalories').innerText = '0 cal';
            document.getElementById('weeklyAverage').innerText = '0 cal';
        });
}

function getWeeklyCalories() {
    fetch(`${API_BASE_URL}/user/${username}/total-calories-week`, {
        headers: getAuthHeaders()
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === 404) {
                document.getElementById('weeklyAverage').innerText = '0 cal';
                return;
            }

            const totalCalories = Object.values(data).reduce((sum, value) => sum + value, 0) / 7;
            document.getElementById('weeklyAverage').innerText = `${totalCalories.toFixed(2)} cal`;

            const dash = document.getElementById('dashboardChart').getContext('2d');

            const labels = Object.keys(data).map(date => {
                return new Date(date).toLocaleString('en-GB', { weekday: 'long' });
            }).reverse();
            const caloriesData = Object.values(data).reverse();

            if (dashInstance) {
                dashInstance.destroy();
            }

            dashInstance = new Chart(dash, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Calories',
                        data: caloriesData,
                        backgroundColor: 'rgba(75, 192, 192, 0.2)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    },
                    plugins: {
                        legend: { position: 'top' }
                    }
                }
            });
        })
        .catch(() => {
            iziToast.error({
                title: 'Error',
                message: 'Failed to load weekly calorie data.',
                position: 'topRight',
            });
        });
}

function getTotalWeeklyExpenditure() {
    fetch(`${API_BASE_URL}/user/${username}/total-expenditure-week`, {
        headers: getAuthHeaders()
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === 404) {
                iziToast.error({
                    title: 'Error',
                    message: 'Weekly expenditure data not found.',
                    position: 'topRight',
                });
                document.getElementById('monthlySpending').innerText = '€0.00';
                return;
            }

            document.getElementById('monthlySpending').innerText = `€${data.toFixed(2)}`;

        })
        .catch(error => {
            iziToast.error({
                title: 'Error',
                message: 'Failed to load weekly expenditure data.',
                position: 'topRight',
            });
        });
}

function getDaysExceedingCalories() {
    fetch(`${API_BASE_URL}/user/${username}/exceed-calorie-threshold-total`, {
        headers: getAuthHeaders()
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === 404) {
                iziToast.error({
                    title: 'Error',
                    message: data.message || 'Calorie threshold data not found.',
                    position: 'topRight',
                });
                return;
            }

            document.getElementById('weeklyCalorieThreshold').innerText = data;

        })
        .catch(error => {
            iziToast.error({
                title: 'Error',
                message: 'Failed to load calorie limit data.',
                position: 'topRight',
            });
        });
}

function expenditureWarning() {
    fetch(`${API_BASE_URL}/user/${username}/spendings-exceeding-1000`, {
        headers: getAuthHeaders()
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === 404) {
                iziToast.error({
                    title: 'Error',
                    message: data.message || 'No spending data found.',
                    position: 'topRight',
                });
                return;
            }
            const currentYearMonth = new Date().toISOString().slice(0, 7);
            const currentMonthExceeding = data[currentYearMonth];
            if (currentMonthExceeding) {
                iziToast.warning({
                    title: 'Warning',
                    message: 'You have exceeded the monthly spending limit of €1000!',
                    position: 'topRight',
                    timeout: false,
                });
            }
        })
        .catch(error => {
            iziToast.error({
                title: 'Error',
                message: 'Failed to load monthly spending data.',
                position: 'topRight',
            });
        });
}

function calorieWarning() {
    fetch(`${API_BASE_URL}/user/${username}/exceeding-2500`, {
        headers: getAuthHeaders()
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === 404) {
                iziToast.error({
                    title: 'Error',
                    message: data.message,
                    position: 'topRight',
                });
                return;
            }
            const today = new Date().toISOString().split('T')[0];
            const todayExceeding = data[today];
            if (todayExceeding) {
                iziToast.warning({
                    title: 'Warning',
                    message: 'You have exceeded the daily calorie limit of 2500!',
                    position: 'topRight',
                    timeout: false,
                });
            }
        })
        .catch(error => {
            iziToast.error({
                title: 'Error',
                message: 'Failed to load daily calorie data.',
                position: 'topRight',
            });
        });
}

function updateCalorieLimitTable() {
    fetch(`${API_BASE_URL}/user/${username}/exceeding-2500`, {
        headers: getAuthHeaders()
    })
        .then(response => response.json())
        .then(data => {
            const tableContainer = document.getElementById('calorieLimitTable');
            const table = document.createElement('table');
            table.className = 'calorie-limit-table';

            table.innerHTML = `
            <thead>
                <tr>
                    <th>Date</th>
                    <th>Calories</th>
                    <th>Status</th>
                </tr>
            </thead>
        `;

            const tbody = document.createElement('tbody');
            Object.entries(data)
                .sort((a, b) => new Date(b[0]) - new Date(a[0]))
                .forEach(([date, calories]) => {
                    const formattedDate = new Date(date).toLocaleDateString('en-GB', {
                        year: 'numeric',
                        month: 'short',
                        day: 'numeric'
                    });

                    tbody.innerHTML += `
                    <tr>
                        <td>${formattedDate}</td>
                        <td>${calories.toLocaleString()} cal</td>
                        <td class="${calories > 2500 ? 'exceeding' : 'within-limit'}">
                            ${calories > 2500 ? 'Exceeding' : 'Within Limit'}
                        </td>
                    </tr>
                `;
                });

            table.appendChild(tbody);
            tableContainer.innerHTML = '';
            tableContainer.appendChild(table);
        });
}

document.addEventListener('DOMContentLoaded', () => {
    userPanel.textContent = `Calorie Tracker ${username}`;
    getTodayCalories();
    getWeeklyCalories();
    getTotalWeeklyExpenditure();
    getDaysExceedingCalories();
    expenditureWarning();
    calorieWarning();
    updateCalorieLimitTable();

    setInterval(() => {
        updateCalorieLimitTable();
    }, 300000);
});

window.onclick = function (event) {
    if (event.target === document.getElementById('modal')) {
        closeModal();
    }
};