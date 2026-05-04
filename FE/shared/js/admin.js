import { api } from './api.js';

const themeList = document.getElementById('theme-list');
const timeList = document.getElementById('time-list');

const loadThemes = async () => {
  themeList.innerHTML = `
    <div class="item">
      <div class="item-info">
        <span class="item-name">Theme API is not available in the current BE.</span>
        <span class="item-meta">Only reservation times can be managed here.</span>
      </div>
    </div>
  `;

  document.getElementById('theme-name').disabled = true;
  document.getElementById('theme-description').disabled = true;
  document.getElementById('theme-thumbnail').disabled = true;
  document.getElementById('add-theme-btn').disabled = true;
};

const loadTimes = async () => {
  const times = await api.getTimes();
  timeList.innerHTML = times.map(time => `
    <div class="item">
      <div class="item-info">
        <span class="item-name">${time.startAt}</span>
      </div>
      <button class="btn-delete" data-id="${time.id}" data-type="time">Delete</button>
    </div>
  `).join('');
};

document.getElementById('add-theme-btn').addEventListener('click', async () => {
  alert('Theme API is not available in the current BE.');
});

document.getElementById('add-time-btn').addEventListener('click', async () => {
  const startAt = document.getElementById('time-start').value;

  if (!startAt) {
    alert('Please select a time');
    return;
  }

  await api.createTime(startAt);
  document.getElementById('time-start').value = '';
  loadTimes();
});

document.addEventListener('click', async (e) => {
  if (e.target.classList.contains('btn-delete')) {
    const id = e.target.dataset.id;
    const type = e.target.dataset.type;

    if (confirm(`Are you sure you want to delete this ${type}?`)) {
      if (type === 'theme') {
        await api.deleteTheme(id);
        loadThemes();
      } else {
        await api.deleteTime(id);
        loadTimes();
      }
    }
  }
});

loadThemes();
loadTimes();
