/**
 * Custom UI utilities to replace native browser elements
 */

export const ui = {
  /**
   * Replaces native alert()
   */
  async alert(message, title = '시스템 알림') {
    return this.showModal({
      title,
      message,
      confirmText: '확인',
      showCancel: false
    });
  },

  /**
   * Replaces native confirm()
   */
  async confirm(message, title = '확인 요청') {
    return this.showModal({
      title,
      message,
      confirmText: '진행',
      cancelText: '취소',
      showCancel: true
    });
  },

  /**
   * Internal modal logic
   */
  showModal({ title, message, confirmText, cancelText, showCancel }) {
    return new Promise((resolve) => {
      let overlay = document.getElementById('custom-modal-overlay');
      
      if (!overlay) {
        overlay = document.createElement('div');
        overlay.id = 'custom-modal-overlay';
        overlay.className = 'modal-overlay';
        overlay.innerHTML = `
          <div class="modal-container">
            <h3 class="modal-title" id="modal-title"></h3>
            <p class="modal-message" id="modal-message"></p>
            <div class="modal-actions">
              <button id="modal-cancel-btn" class="btn-modal btn-modal-cancel"></button>
              <button id="modal-confirm-btn" class="btn-modal btn-modal-confirm"></button>
            </div>
          </div>
        `;
        document.body.appendChild(overlay);
      }

      const titleEl = overlay.querySelector('#modal-title');
      const messageEl = overlay.querySelector('#modal-message');
      const confirmBtn = overlay.querySelector('#modal-confirm-btn');
      const cancelBtn = overlay.querySelector('#modal-cancel-btn');

      titleEl.textContent = title;
      messageEl.textContent = message;
      confirmBtn.textContent = confirmText;
      cancelBtn.textContent = cancelText || '';
      cancelBtn.style.display = showCancel ? 'block' : 'none';

      const close = (result) => {
        overlay.classList.remove('active');
        // Wait for animation
        setTimeout(() => {
          resolve(result);
        }, 300);
      };

      confirmBtn.onclick = () => close(true);
      cancelBtn.onclick = () => close(false);
      overlay.onclick = (e) => {
        if (e.target === overlay && showCancel) close(false);
      };

      // Trigger reflow for animation
      overlay.offsetHeight;
      overlay.classList.add('active');
    });
  },

  /**
   * Custom Date Picker (Calendar)
   */
  async pickDate(initialDate = new Date()) {
    return new Promise((resolve) => {
      let overlay = document.getElementById('custom-date-overlay');
      if (!overlay) {
        overlay = document.createElement('div');
        overlay.id = 'custom-date-overlay';
        overlay.className = 'booking-overlay';
        overlay.style.display = 'none';
        overlay.innerHTML = `
          <div class="overlay-backdrop"></div>
          <div class="overlay-content">
            <button class="close-btn">&times;</button>
            <div class="theme-mini-info">
              <h2>날짜 선택</h2>
              <p>원하시는 예약 날짜를 선택해주세요.</p>
            </div>
            <div class="calendar-wrapper">
              <div class="calendar-header">
                <button class="calendar-nav-btn" id="prev-month">&lt;</button>
                <div class="calendar-month" id="calendar-month-year">May 2026</div>
                <button class="calendar-nav-btn" id="next-month">&gt;</button>
              </div>
              <div class="calendar-grid" id="calendar-grid">
                <!-- Weekdays and Days will be injected here -->
              </div>
            </div>
          </div>
        `;
        document.body.appendChild(overlay);
      }

      const grid = overlay.querySelector('#calendar-grid');
      const monthYearDisplay = overlay.querySelector('#calendar-month-year');
      const prevBtn = overlay.querySelector('#prev-month');
      const nextBtn = overlay.querySelector('#next-month');
      const closeBtn = overlay.querySelector('.close-btn');
      const backdrop = overlay.querySelector('.overlay-backdrop');

      let viewDate = new Date(initialDate);
      viewDate.setDate(1);

      const render = () => {
        grid.innerHTML = '';
        const year = viewDate.getFullYear();
        const month = viewDate.getMonth();
        
        monthYearDisplay.textContent = new Intl.DateTimeFormat('ko-KR', { year: 'numeric', month: 'long' }).format(viewDate);

        // Weekdays
        ['일', '월', '화', '수', '목', '금', '토'].forEach(day => {
          const el = document.createElement('div');
          el.className = 'calendar-weekday';
          el.textContent = day;
          grid.appendChild(el);
        });

        const firstDay = new Date(year, month, 1).getDay();
        const daysInMonth = new Date(year, month + 1, 0).getDate();
        const today = new Date();
        today.setHours(0, 0, 0, 0);

        // Empty cells
        for (let i = 0; i < firstDay; i++) {
          const el = document.createElement('div');
          el.className = 'calendar-day empty';
          grid.appendChild(el);
        }

        // Days
        for (let day = 1; day <= daysInMonth; day++) {
          const date = new Date(year, month, day);
          const el = document.createElement('div');
          el.className = 'calendar-day';
          el.textContent = day;

          if (date.getTime() === today.getTime()) el.classList.add('today');
          if (date < today) el.classList.add('disabled');
          
          const dateString = `${year}-${String(month + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
          if (dateString === initialDate) el.classList.add('selected');

          if (date >= today) {
            el.onclick = () => {
              overlay.style.display = 'none';
              document.body.style.overflow = 'auto';
              resolve(dateString);
            };
          }

          grid.appendChild(el);
        }
      };

      prevBtn.onclick = () => { viewDate.setMonth(viewDate.getMonth() - 1); render(); };
      nextBtn.onclick = () => { viewDate.setMonth(viewDate.getMonth() + 1); render(); };
      
      const close = () => {
        overlay.style.display = 'none';
        document.body.style.overflow = 'auto';
        resolve(null);
      };

      closeBtn.onclick = close;
      backdrop.onclick = close;

      overlay.style.display = 'flex';
      document.body.style.overflow = 'hidden';
      render();
    });
  }
};
