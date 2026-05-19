(() => {
    // 응답이 실패면 서버의 공통 에러 본문(code/message)을 담은 Error를 throw 한다.
    async function apiFetch(url, options) {
        const res = await fetch(url, options);
        if (res.ok) return res;

        let body = null;
        try {
            body = await res.json();
        } catch (e) {
            body = null;
        }

        const error = new Error((body && body.message) || '요청 처리에 실패했습니다.');
        error.code = body && body.code;
        error.status = res.status;
        throw error;
    }

    window.apiFetch = apiFetch;
})();
