document.addEventListener('DOMContentLoaded', () => {
    /*
    TODO: [3단계] 인기 테마 - 인기 테마 목록 조회 API 호출
    */
    const nowDate = new Date();
    const endDate = new Date(nowDate.getTime() - 24 * 60 * 60 * 1000)
    const startDate = new Date(endDate.getTime() - 7 * 24 * 60 * 60 * 1000);

    const endDateParam = convertDate(endDate);
    const startDateParam = convertDate(startDate);

    requestRead('/themes/popular?startDate=' + startDateParam + "&endDate=" + endDateParam) // 인기 테마 목록 조회 API endpoint
        .then(render)
        .catch(error => console.error('Error fetching times:', error));
});

function convertDate(date) {
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2);
    const day = ('0' + date.getDate()).slice(-2);
    return year + '-' + month + '-' + day;
}

function render(data) {
    const container = document.getElementById('theme-ranking');

    /*
    TODO: [3단계] 인기 테마 - 인기 테마 목록 조회 API 호출 후 렌더링
          response 명세에 맞춰 name, thumbnail, description 값 설정
    */
    data.forEach(theme => {
        const {name, thumbnail, description} = theme;

        const htmlContent = `
            <img class="mr-3 img-thumbnail" src="${thumbnail}" alt="${name}">
            <div class="media-body">
                <h5 class="mt-0 mb-1">${name}</h5>
                ${description}
            </div>
        `;

        const div = document.createElement('li');
        div.className = 'media my-4';
        div.innerHTML = htmlContent;

        container.appendChild(div);
    })
}

function requestRead(endpoint) {
    return fetch(endpoint)
        .then(response => {
            if (response.status === 200) return response.json();
            throw new Error('Read failed');
        });
}
