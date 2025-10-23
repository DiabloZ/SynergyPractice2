const App = {
  model: {
    token: null,
    accounts: [],
    messages: [],
    user: null,
  },

  dom: {},

  api: {
    _fetch: (url, options) => {
      const defaultOptions = {
        headers: {
          'content-type': 'application/json',
        },
      };
      if (App.model.token) {
        defaultOptions.headers['Authorization'] = 'Bearer ' + App.model.token;
      }
      return fetch(url, {...defaultOptions, ...options, headers: {...defaultOptions.headers, ...options?.headers}}).then(r => {
          if (!r.ok) {
              return r.json().then(err => Promise.reject(err));
          }
          return r.json();
      });
    },
    register: (u, p) => App.api._fetch('/api/auth/register', {method: 'POST', body: JSON.stringify({username: u, password: p})}),
    login: (u, p) => App.api._fetch('/api/auth/login', {method: 'POST', body: JSON.stringify({username: u, password: p})}),
    createAccount: (currency) => App.api._fetch('/api/accounts', {method: 'POST', body: JSON.stringify({currency})}),
    listAccounts: () => App.api._fetch('/api/accounts'),
    pay: (payment) => App.api._fetch('/api/payments', {method: 'POST', body: JSON.stringify(payment)}),
  },

  init() {
    this.cacheDOMElements();
    this.bindEventListeners();
    this.checkSession();
  },

  cacheDOMElements() {
    this.dom.userBadge = document.getElementById('userBadge');
    this.dom.logoutBtn = document.getElementById('logoutBtn');
    this.dom.authCard = document.getElementById('authCard');
    this.dom.createAccountCard = document.getElementById('createAccountCard');
    this.dom.transferCard = document.getElementById('transferCard');
    this.dom.accountsCard = document.getElementById('accountsCard');
    this.dom.username = document.getElementById('username');
    this.dom.password = document.getElementById('password');
    this.dom.registerBtn = document.getElementById('registerBtn');
    this.dom.loginBtn = document.getElementById('loginBtn');
    this.dom.currency = document.getElementById('currency');
    this.dom.createAccountBtn = document.getElementById('createAccountBtn');
    this.dom.fromId = document.getElementById('fromId');
    this.dom.toId = document.getElementById('toId');
    this.dom.amount = document.getElementById('amount');
    this.dom.transferBtn = document.getElementById('transferBtn');
    this.dom.accountsList = document.getElementById('accountsList');
    this.dom.messagesList = document.getElementById('messagesList');
    this.dom.toastContainer = document.getElementById('toastContainer');
  },

  bindEventListeners() {
    this.dom.registerBtn.onclick = () => this.handleRegister();
    this.dom.loginBtn.onclick = () => this.handleLogin();
    this.dom.logoutBtn.onclick = () => this.handleLogout();
    this.dom.createAccountBtn.onclick = () => this.handleCreateAccount();
    this.dom.transferBtn.onclick = () => this.handleTransfer();
  },

  checkSession() {
    this.model.token = localStorage.getItem('fintech_token');
    this.model.user = localStorage.getItem('fintech_user');
    if (this.model.token && this.model.user) {
      this.setAuthorizedUI(true, this.model.user);
      this.refreshAccounts();
      this.addMessage('Сессия восстановлена');
    }
  },

  async handleRegister() {
    const username = this.dom.username.value;
    const password = this.dom.password.value;
    try {
      const res = await this.api.register(username, password);
      this.addMessage(`Пользователь ${res.username} зарегистрирован`);
      this.toast('Регистрация прошла успешно', 'success');
    } catch (e) {
      this.addMessage('Ошибка регистрации: ' + (e.message || ''));
      this.toast('Ошибка регистрации', 'danger');
      console.error(e);
    }
  },

  async handleLogin() {
    const username = this.dom.username.value;
    const password = this.dom.password.value;
    try {
      const res = await this.api.login(username, password);
      if (res.token) {
        this.model.token = res.token;
        this.model.user = username;
        localStorage.setItem('fintech_token', res.token);
        localStorage.setItem('fintech_user', username);
        this.setAuthorizedUI(true, username);
        this.addMessage('Вход выполнен');
        this.toast('Вход выполнен', 'success');
        await this.refreshAccounts();
      } else {
        this.addMessage('Неверные учетные данные');
        this.toast('Неверные учетные данные', 'warning');
      }
    } catch (e) {
      this.addMessage('Ошибка входа: ' + (e.status || ''));
      this.toast('Ошибка при входе', 'danger');
      console.error(e);
    }
  },

  handleLogout() {
    this.model.token = null;
    this.model.user = null;
    localStorage.removeItem('fintech_token');
    localStorage.removeItem('fintech_user');
    this.setAuthorizedUI(false, null);
    this.addMessage('Выход выполнен');
    this.toast('Вы вышли из системы', 'info');
    this.renderAccounts();
  },

  async handleCreateAccount() {
    const currency = this.dom.currency.value || 'RUB';
    try {
      const acc = await this.api.createAccount(currency);
      this.addMessage(`Счёт создан #${acc.id}`);
      this.toast(`Счёт создан #${acc.id}`, 'success');
      await this.refreshAccounts();
    } catch (e) {
      this.addMessage('Ошибка создания счёта');
      this.toast('Ошибка создания счёта', 'danger');
      console.error(e);
    }
  },

  async handleTransfer() {
    const fromIdValue = this.dom.fromId.value;
    const from = fromIdValue ? parseInt(fromIdValue) : null;
    const to = parseInt(this.dom.toId.value);
    const amount = Math.round(parseFloat(this.dom.amount.value || '0') * 100);
    const action = from ? 'Перевод' : 'Пополнение';

    if (from) {
      const fromAccount = this.model.accounts.find(a => a.id === from);
      if (!fromAccount) {
        this.addMessage('Счет отправителя не найден или не принадлежит вам');
        this.toast('Счет отправителя не найден или не принадлежит вам', 'danger');
        return;
      }
    }
    
    if (!to || !amount) {
        this.addMessage('Необходимо указать счет получателя и сумму');
        this.toast('Необходимо указать счет получателя и сумму', 'warning');
        return;
    }

    try {
      const r = await this.api.pay({ fromAccountId: from, toAccountId: to, amountCents: amount });
      if (r.status === 'ok') {
        this.addMessage(`${action} выполнен`);
        this.toast(`${action} успешен`, 'success');
        await this.refreshAccounts();
      } else {
        this.addMessage(`${action} не выполнен: ${r.status}`);
        this.toast(`${action} не выполнен`, 'warning');
      }
    } catch (e) {
      this.addMessage(`Ошибка: ${action} не удался. ${e.status || ''}`);
      this.toast(`Ошибка ${action.toLowerCase()}`, 'danger');
      console.error(e);
    }
  },

  setAuthorizedUI(isAuth, username) {
    const { authCard, createAccountCard, transferCard, accountsCard, userBadge, logoutBtn } = this.dom;
    if (isAuth) {
      authCard.classList.add('d-none');
      createAccountCard.classList.remove('d-none');
      transferCard.classList.remove('d-none');
      accountsCard.classList.remove('d-none');
      userBadge.classList.remove('bg-secondary');
      userBadge.classList.add('bg-success');
      userBadge.textContent = username || 'Авторизован';
      logoutBtn.classList.remove('d-none');
    } else {
      authCard.classList.remove('d-none');
      createAccountCard.classList.add('d-none');
      transferCard.classList.add('d-none');
      accountsCard.classList.add('d-none');
      userBadge.classList.remove('bg-success');
      userBadge.classList.add('bg-secondary');
      userBadge.textContent = 'Не авторизован';
      logoutBtn.classList.add('d-none');
    }
  },

  async refreshAccounts() {
    if (!this.model.token) return;
    try {
      const list = await this.api.listAccounts();
      this.model.accounts = Array.isArray(list) ? list : [];
      this.renderAccounts();
    } catch (e) {
      this.addMessage('Ошибка загрузки счетов');
      console.error(e);
    }
  },

  renderAccounts() {
    this.dom.accountsList.innerHTML = '';
    this.model.accounts.forEach(a => {
      const li = document.createElement('li');
      li.className = 'list-group-item d-flex justify-content-between align-items-center';
      li.innerHTML = `<div><strong>#${a.id}</strong> ${a.currency}</div><div class="balance">${(a.balanceCents / 100).toFixed(2)}</div>`;
      this.dom.accountsList.appendChild(li);
    });
  },

  addMessage(msg) {
    this.model.messages.unshift({text: msg, time: new Date().toLocaleString()});
    this.renderMessages();
  },

  renderMessages() {
    this.dom.messagesList.innerHTML = '';
    this.model.messages.slice(0, 30).forEach(m => {
      const li = document.createElement('li');
      li.className = 'list-group-item';
      li.textContent = `[${m.time}] ${m.text}`;
      this.dom.messagesList.appendChild(li);
    });
  },

  toast(message, type = 'primary', timeout = 4000) {
    const id = 't' + Date.now();
    const html = `<div id="${id}" class="toast align-items-center text-bg-${type} border-0" role="alert" aria-live="assertive" aria-atomic="true">
      <div class="d-flex">
        <div class="toast-body">${message}</div>
        <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
      </div>
    </div>`;
    this.dom.toastContainer.insertAdjacentHTML('beforeend', html);
    const tEl = document.getElementById(id);
    const bs = new bootstrap.Toast(tEl, { delay: timeout });
    bs.show();
    setTimeout(() => tEl.remove(), timeout + 500);
  },
};

document.addEventListener('DOMContentLoaded', () => App.init());
