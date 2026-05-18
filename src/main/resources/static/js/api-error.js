window.RoomescapeApi = (() => {
    class ApiError extends Error {
        constructor(message, code, status) {
            super(message);
            this.name = 'ApiError';
            this.code = code;
            this.status = status;
        }
    }

    async function parseError(response, fallbackMessage) {
        const fallback = fallbackMessage || '요청을 처리하지 못했습니다.';

        try {
            const error = await response.json();
            const message = error.message || fallback;
            const action = error.action ? `\n${error.action}` : '';

            return {
                message: `${message}${action}`,
                code: error.code,
                status: response.status
            };
        } catch (e) {
            return {
                message: fallback,
                code: undefined,
                status: response.status
            };
        }
    }

    async function request(url, options, fallbackMessage) {
        const response = await fetch(url, options);

        if (!response.ok) {
            const error = await parseError(response, fallbackMessage);
            throw new ApiError(error.message, error.code, error.status);
        }

        if (response.status === 204) {
            return null;
        }

        const body = await response.text();
        if (!body) {
            return null;
        }

        return JSON.parse(body);
    }

    return {request, parseError, ApiError};
})();
