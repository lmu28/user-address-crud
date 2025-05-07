// API URLs
const API_BASE_URL = '/api';
const API = {
    USERS: `${API_BASE_URL}/users`,
    ADDRESSES: `${API_BASE_URL}/addresses`,
    AUTH: `${API_BASE_URL}/auth/login`,
    REGISTER: `${API_BASE_URL}/auth/register`
};

let loginModal, userModal, addressModal, confirmModal, registerModal;

// Vue приложение
const app = Vue.createApp({
    data() {
        return {
            currentPage: 'users',

            isAuthenticated: false,
            loginForm: {
                username: '',
                password: ''
            },
            loginError: '',

            registerForm: {
                username: '',
                password: '',
            },
            registerFieldErrors: [],
            registerError: '',

            users: [],
            addresses: [],           // Все адреса (полный список)
            displayAddresses: [],    // Отображаемые адреса (могут быть отфильтрованы)

            userSearch: '',
            userSearchText: '',
            addressSearch: '',
            addressSearchText: '',

            userForm: {
                id: null,
                firstName: '',
                lastName: '',
                middleName: '',
                phone: '',
                email: '',
                birthDate: '',
                addressId: null
            },
            userFormErrors: {},
            userError: '',
            isEditingUser: false,

            addressForm: {
                id: null,
                region: '',
                city: '',
                street: '',
                house: '',
                apartment: ''
            },
            addressFormErrors: {},
            isEditingAddress: false,

            deleteType: '',
            deleteId: null,
            confirmMessage: '',
            deleteError: ''
        };
    },

    computed: {
        filteredUsers() {
            if (!this.userSearch) return this.users;

            const search = this.userSearch.toLowerCase();
            return this.users.filter(user =>
                (user.firstName && user.firstName.toLowerCase().includes(search)) ||
                (user.lastName && user.lastName.toLowerCase().includes(search)) ||
                (user.middleName && user.middleName.toLowerCase().includes(search)) ||
                (user.email && user.email.toLowerCase().includes(search)) ||
                (user.phone && user.phone.includes(search))
            );
        },

        filteredAddresses() {
            if (!this.addressSearch) return this.displayAddresses;

            const search = this.addressSearch.toLowerCase();
            return this.displayAddresses.filter(address =>
                (address.region && address.region.toLowerCase().includes(search)) ||
                (address.city && address.city.toLowerCase().includes(search)) ||
                (address.street && address.street.toLowerCase().includes(search)) ||
                (address.house && address.house.toLowerCase().includes(search)) ||
                (address.apartment && address.apartment.toLowerCase().includes(search))
            );
        }
    },

    mounted() {
        // Инициализация модальных окон Bootstrap
        loginModal = new bootstrap.Modal(document.getElementById('loginModal'));
        userModal = new bootstrap.Modal(document.getElementById('userModal'));
        addressModal = new bootstrap.Modal(document.getElementById('addressModal'));
        confirmModal = new bootstrap.Modal(document.getElementById('confirmModal'));
        registerModal = new bootstrap.Modal(document.getElementById('registerModal'));

        this.checkAuth();

        this.loadUsers();
        this.loadAddresses();
    },

    methods: {
        formatDate(dateString) {
            if (!dateString) return '-';
            const date = new Date(dateString);
            return date.toLocaleDateString('ru-RU');
        },

        getUserAddress(user) {
            if (user.addressId === null || user.addressId === undefined) return 'Не указан';

            const address = this.addresses.find(a => a.id === user.addressId);
            if (!address) return 'Не найден';

            let result = `${address.city}, ${address.street}, ${address.house}`;
            if (address.apartment) {
                result += `, кв. ${address.apartment}`;
            }
            return result;
        },

        showRegisterModal() {
            this.registerForm = { username: '', password: '' };
            this.registerFieldErrors = [];
            this.registerError = '';
            registerModal.show();
        },

        getFieldError(fieldName) {
            const error = this.registerFieldErrors.find(err => err.field === fieldName);
            return error ? error.message : '';
        },

        register() {
            fetch(API.REGISTER, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(this.registerForm)
            })
                .then(async response => {
                    const data = await response.json();

                    if (!response.ok) {
                        this.registerFieldErrors = [];
                        this.registerError = '';

                        if (Array.isArray(data)) {
                            this.registerFieldErrors = data;
                        } else if (data.fieldErrors && Array.isArray(data.fieldErrors)) {
                            this.registerFieldErrors = data.fieldErrors;
                        } else if (data.message) {
                            this.registerError = data.message;
                        } else {
                            this.registerError = 'Ошибка регистрации';
                        }
                        throw new Error('Validation error');
                    }

                    // Успешная регистрация
                    localStorage.setItem('authToken', data.token);
                    this.isAuthenticated = true;
                    registerModal.hide();
                })
                .catch(error => {
                    console.error(error); // Для отладки
                });
        },

        // Авторизация
        checkAuth() {
            const token = localStorage.getItem('authToken');
            this.isAuthenticated = !!token;
        },

        showLoginModal() {
            this.loginForm = { username: '', password: '' };
            this.loginError = '';
            loginModal.show();
        },

        login() {
            fetch(API.AUTH, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(this.loginForm)
            })
                .then(response => {
                    if (response.ok) {
                        return response.json();
                    } else {
                        throw new Error('Неверные учетные данные');
                    }
                })
                .then(data => {
                    localStorage.setItem('authToken', data.token);
                    this.isAuthenticated = true;
                    loginModal.hide();
                })
                .catch(error => {
                    this.loginError = error.message;
                });
        },

        logout() {
            localStorage.removeItem('authToken');
            this.isAuthenticated = false;
        },

        loadUsers() {
            fetch(API.USERS)
                .then(response => response.json())
                .then(data => {
                    this.users = data;
                })
                .catch(error => console.error('Ошибка загрузки пользователей:', error));
        },

        searchUsers() {
            this.userSearch = this.userSearchText;

            if (!this.userSearch.trim()) {
                this.loadUsers();
                return;
            }

            fetch(`${API.USERS}/search?query=${encodeURIComponent(this.userSearch)}`)
                .then(response => response.json())
                .then(data => {
                    this.users = data;
                })
                .catch(error => console.error('Ошибка поиска пользователей:', error));
        },

        showUserForm() {
            this.userForm = {
                id: null,
                firstName: '',
                lastName: '',
                middleName: '',
                phone: '',
                email: '',
                birthDate: '',
                addressId: null
            };
            this.userFormErrors = {};
            this.userError = '';
            this.isEditingUser = false;
            userModal.show();
        },

        editUser(user) {
            this.userForm = { ...user };
            // Если дата в ISO формате, конвертируем в YYYY-MM-DD для input type="date"
            if (this.userForm.birthDate) {
                const date = new Date(this.userForm.birthDate);
                this.userForm.birthDate = date.toISOString().split('T')[0];
            }
            this.userFormErrors = {};
            this.userError = '';
            this.isEditingUser = true;
            userModal.show();
        },

        saveUser() {
            this.userFormErrors = {};
            this.userError = null;

            const requiredFields = {
                firstName: 'Имя',
                phone: 'Телефон',
                email: 'Email',
                birthDate: 'Дата рождения'
            };

            const errors = {};
            Object.entries(requiredFields).forEach(([field, label]) => {
                if (!this.userForm[field] && this.userForm[field] !== 0) {
                    errors[field] = `${label} обязательно для заполнения`;
                }
            });

            if (Object.keys(errors).length > 0) {
                this.userFormErrors = errors;
                return;
            }

            const userToSend = { ...this.userForm };

            Object.keys(userToSend).forEach(key => {
                if (userToSend[key] === '') {
                    userToSend[key] = null;
                }
            });

            const url = this.isEditingUser ? `${API.USERS}/${this.userForm.id}` : API.USERS;
            const method = this.isEditingUser ? 'PUT' : 'POST';

            fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('authToken')}`
                },
                body: JSON.stringify(userToSend)
            })
                .then(response => {
                    if (response.ok) {
                        return response.json();
                    } else {
                        return response.json().then(errors => {
                            throw errors;
                        });
                    }
                })
                .then(data => {
                    this.isEditingUser = false;
                    userModal.hide();
                    this.loadUsers();
                })
                .catch(errors => {
                    if (errors.fieldErrors) {
                        this.userFormErrors = errors.fieldErrors;
                    } else if (Array.isArray(errors)) {
                        errors.forEach(error => {
                            if (error.field && error.message) {
                                this.userFormErrors[error.field] = error.message;
                            }
                        });
                    } else if (errors.message) {
                        this.userError = errors.message;
                    } else {
                        this.userError = 'Ошибка при сохранении пользователя';
                        console.error('Ошибка при сохранении пользователя:', errors);
                    }
                });
        },

        deleteUser(id) {
            this.deleteType = 'user';
            this.deleteId = id;
            this.confirmMessage = 'Вы действительно хотите удалить пользователя?';
            confirmModal.show();
        },

        loadAddresses() {
            fetch(API.ADDRESSES)
                .then(response => response.json())
                .then(data => {
                    this.addresses = data;       // Сохраняем полный список адресов
                    this.displayAddresses = data.slice(); // Создаем копию для отображения
                })
                .catch(error => console.error('Ошибка загрузки адресов:', error));
        },

        searchAddresses() {
            this.addressSearch = this.addressSearchText;

            if (!this.addressSearch.trim()) {
                this.displayAddresses = this.addresses.slice(); // Восстанавливаем полный список для отображения
                return;
            }

            fetch(`${API.ADDRESSES}/search?query=${encodeURIComponent(this.addressSearch)}`)
                .then(response => response.json())
                .then(data => {
                    this.displayAddresses = data; // Обновляем только отображаемый список
                })
                .catch(error => console.error('Ошибка поиска адресов:', error));
        },

        showAddressForm() {
            this.addressForm = {
                id: null,
                region: '',
                city: '',
                street: '',
                house: '',
                apartment: ''
            };
            this.addressFormErrors = {};
            this.isEditingAddress = false;
            addressModal.show();
        },

        editAddress(address) {
            this.addressForm = { ...address };
            this.addressFormErrors = {};
            this.isEditingAddress = true;
            addressModal.show();
        },

        saveAddress() {
            this.addressFormErrors = {};

            const requiredFields = {
                region: 'Область',
                city: 'Город',
                street: 'Улица',
                house: 'Дом'
            };

            const errors = {};
            Object.entries(requiredFields).forEach(([field, label]) => {
                if (!this.addressForm[field]) {
                    errors[field] = `${label} обязательно для заполнения`;
                }
            });

            if (Object.keys(errors).length > 0) {
                this.addressFormErrors = errors;
                return;
            }

            const addressToSend = { ...this.addressForm };

            // Преобразуем пустые строки в null
            Object.keys(addressToSend).forEach(key => {
                if (addressToSend[key] === '') {
                    addressToSend[key] = null;
                }
            });

            const url = this.isEditingAddress ? `${API.ADDRESSES}/${this.addressForm.id}` : API.ADDRESSES;
            const method = this.isEditingAddress ? 'PUT' : 'POST';

            fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('authToken')}`
                },
                body: JSON.stringify(addressToSend)
            })
                .then(response => {
                    if (response.ok) {
                        return response.json();
                    } else {
                        return response.json().then(errors => {
                            throw errors;
                        });
                    }
                })
                .then(data => {
                    this.isEditingAddress = false;
                    addressModal.hide();
                    this.loadAddresses(); // Перезагружаем оба списка адресов
                })
                .catch(errors => {
                    if (errors.fieldErrors) {
                        this.addressFormErrors = errors.fieldErrors;
                    } else if (Array.isArray(errors)) {
                        errors.forEach(error => {
                            if (error.field && error.message) {
                                this.addressFormErrors[error.field] = error.message;
                            }
                        });
                    } else {
                        console.error('Ошибка при сохранении адреса:', errors);
                    }
                });
        },

        deleteAddress(id) {
            this.deleteError = null;
            this.deleteType = 'address';
            this.deleteId = id;
            this.confirmMessage = 'Вы действительно хотите удалить адрес?';
            confirmModal.show();
        },

        confirmDelete() {
            const url = this.deleteType === 'user'
                ? `${API.USERS}/${this.deleteId}`
                : `${API.ADDRESSES}/${this.deleteId}`;

            fetch(url, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('authToken')}`
                }
            })
                .then(response => {
                    if (response.ok) {
                        if (this.deleteType === 'user') {
                            this.loadUsers();
                        } else {
                            this.loadAddresses();
                        }
                        confirmModal.hide();
                    } else if (response.status === 409) {
                        this.deleteError = 'Невозможно удалить адрес, так как он используется пользователями';
                    } else {
                        this.deleteError = 'Ошибка при удалении';
                    }
                })
                .catch(error => {
                    this.deleteError = error.message;
                });
        }
    }
});

app.mount('#app');