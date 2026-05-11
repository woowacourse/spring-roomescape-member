(() => {
    const track = document.querySelector('.carousel-track');
    if (!track) return;
    const slides = track.children;
    if (slides.length === 0) return;

    const dotsContainer = document.querySelector('.carousel-dots');
    let index = 0;

    for (let i = 0; i < slides.length; i++) {
        const dot = document.createElement('button');
        dot.className = 'carousel-dot' + (i === 0 ? ' active' : '');
        dot.type = 'button';
        dot.addEventListener('click', () => goTo(i));
        dotsContainer.appendChild(dot);
    }

    function goTo(i) {
        index = (i + slides.length) % slides.length;
        track.style.transform = `translateX(-${index * 100}%)`;
        document.querySelectorAll('.carousel-dot').forEach((d, di) => {
            d.classList.toggle('active', di === index);
        });
    }

    document.querySelector('.carousel-arrow.prev').addEventListener('click', () => goTo(index - 1));
    document.querySelector('.carousel-arrow.next').addEventListener('click', () => goTo(index + 1));

    setInterval(() => goTo(index + 1), 5000);
})();
