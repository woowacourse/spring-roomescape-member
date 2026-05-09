document.addEventListener('DOMContentLoaded', () => {
  const popularContainer = document.getElementById('popular-themes-container');
  const allThemesContainer = document.getElementById('all-themes-container');

  // Load Popular Themes
  fetch('/themes/popular')
    .then(res => res.json())
    .then(themes => {
      popularContainer.innerHTML = '';
      if(themes.length === 0) {
        popularContainer.innerHTML = '<p class="empty-state">아직 인기 테마가 없습니다.</p>';
        return;
      }
      themes.forEach((t, i) => {
        popularContainer.innerHTML += `
          <div class="theme-card">
            <div class="rank-badge">${i+1}위</div>
            <img src="${t.thumbnail}" alt="${t.name}" class="theme-img">
            <div class="theme-info">
              <h3 class="theme-name">${t.name}</h3>
              <p class="theme-desc">${t.description}</p>
              <button type="button" class="btn-primary" onclick="location.href='/reservation.html?themeId=${t.id}'">예약하기</button>
            </div>
          </div>
        `;
      });
    });

  // Load All Themes
  fetch('/themes')
    .then(res => res.json())
    .then(themes => {
      allThemesContainer.innerHTML = '';
      themes.forEach(t => {
        allThemesContainer.innerHTML += `
          <div class="theme-card">
            <img src="${t.thumbnail}" alt="${t.name}" class="theme-img">
            <div class="theme-info">
              <h3 class="theme-name">${t.name}</h3>
              <p class="theme-desc">${t.description}</p>
              <button type="button" class="btn-primary" onclick="location.href='/reservation.html?themeId=${t.id}'">예약하기</button>
            </div>
          </div>
        `;
      });
    });
});
