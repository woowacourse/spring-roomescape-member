import { api } from './api.js';

const themeGrid = document.getElementById('theme-grid');
const bookingOverlay = document.getElementById('booking-overlay');
const closeOverlayBtn = document.getElementById('close-overlay');
const dateInput = document.getElementById('reservation-date');
const timeSlotsContainer = document.getElementById('time-slots');
const nextBtn = document.getElementById('next-to-step-2');
const reserveBtn = document.getElementById('reserve-btn');
const finishBtn = document.getElementById('finish-btn');

let selectedTime = null;
const reservationProduct = {
  id: 'room-escape',
  name: 'Room Escape Reservation',
  description: 'Book one of the available room escape times.'
};

// --- Navigation ---
const goToStep = (stepNumber) => {
  document.querySelectorAll('.step-pane').forEach(p => p.classList.remove('active'));
  document.querySelectorAll('.step').forEach(s => s.classList.remove('active'));
  
  document.getElementById(`step-${stepNumber}-content`).classList.add('active');
  document.querySelector(`.step[data-step="${stepNumber}"]`).classList.add('active');
};

const openBooking = (theme) => {
  selectedTime = null;
  reserveBtn.disabled = false;
  reserveBtn.textContent = 'Reserve Now';
  document.getElementById('selected-theme-name').textContent = theme.name;
  document.getElementById('selected-theme-desc').textContent = theme.description;
  
  // Set default date
  const today = new Date().toISOString().split('T')[0];
  dateInput.value = today;
  dateInput.min = today;
  
  bookingOverlay.style.display = 'flex';
  document.body.style.overflow = 'hidden'; // Prevent background scroll
  
  updateTimes();
  goToStep(1);
};

const closeBooking = () => {
  bookingOverlay.style.display = 'none';
  document.body.style.overflow = 'auto';
  selectedTime = null;
  updateTimes();
  goToStep(1);
};

// --- Data Loading ---
const loadThemes = async () => {
  themeGrid.innerHTML = `
    <div class="theme-card" data-id="${reservationProduct.id}">
      <div class="theme-image integrated-placeholder"></div>
      <div class="theme-content">
        <h3 class="theme-title">${reservationProduct.name}</h3>
        <p class="theme-description">${reservationProduct.description}</p>
      </div>
    </div>
  `;
};

const updateTimes = async () => {
  if (!dateInput.value) return;

  nextBtn.disabled = true;
  timeSlotsContainer.innerHTML = '<p style="grid-column: 1/-1; text-align: center; color: #8e8e93;">Checking availability...</p>';

  try {
    const times = await api.getReservableTimes(dateInput.value);
    if (times.length === 0) {
      timeSlotsContainer.innerHTML = '<p style="grid-column: 1/-1; text-align: center;">No times available for this date.</p>';
      return;
    }
    
    timeSlotsContainer.innerHTML = times.map(time => `
      <div class="time-slot ${time.available ? '' : 'disabled'}" 
           data-id="${time.timeId}" 
           data-available="${time.available}">
        ${time.startAt}
      </div>
    `).join('');
  } catch (error) {
    timeSlotsContainer.innerHTML = '<p style="grid-column: 1/-1; color: #ff3b30;">Failed to load times.</p>';
  }
};

// --- Event Listeners ---
themeGrid.addEventListener('click', async (e) => {
  const card = e.target.closest('.theme-card');
  if (!card) return;

  openBooking(reservationProduct);
});

dateInput.addEventListener('change', updateTimes);

timeSlotsContainer.addEventListener('click', (e) => {
  const slot = e.target.closest('.time-slot');
  if (!slot || slot.classList.contains('disabled')) return;

  document.querySelectorAll('.time-slot').forEach(s => s.classList.remove('selected'));
  slot.classList.add('selected');
  
  selectedTime = {
    id: slot.dataset.id,
    startAt: slot.textContent.trim()
  };
  
  nextBtn.disabled = false;
});

nextBtn.addEventListener('click', () => goToStep(2));
document.getElementById('back-to-step-1').addEventListener('click', () => goToStep(1));

reserveBtn.addEventListener('click', async () => {
  const name = document.getElementById('reservation-name').value;
  if (!name) {
    alert('Please enter your name');
    return;
  }

  reserveBtn.disabled = true;
  reserveBtn.textContent = 'Processing...';

  try {
    const reservation = {
      name,
      date: dateInput.value,
      timeId: selectedTime.id
    };

    await api.createReservation(reservation);
    
    document.getElementById('reservation-summary').innerHTML = `
      <p><strong>Reservation:</strong> ${reservationProduct.name}</p>
      <p><strong>Date:</strong> ${dateInput.value}</p>
      <p><strong>Time:</strong> ${selectedTime.startAt}</p>
      <p><strong>Reserved by:</strong> ${name}</p>
    `;
    
    goToStep(3);
  } catch (error) {
    alert(error.message || 'Failed to complete reservation. Please try again.');
    reserveBtn.disabled = false;
    reserveBtn.textContent = 'Reserve Now';
  }
});

finishBtn.addEventListener('click', closeBooking);
closeOverlayBtn.addEventListener('click', () => {
  if (confirm('Close booking? Your progress will be lost.')) closeBooking();
});

// Initial load
loadThemes();
