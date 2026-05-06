export async function api(path, options = {}) {
  const init = {
    method: options.method || "GET",
    headers: {
      Accept: "application/json"
    }
  };

  if (options.body !== undefined) {
    init.headers["Content-Type"] = "application/json";
    init.body = JSON.stringify(options.body);
  }

  let response;
  try {
    response = await fetch(path, init);
  } catch (error) {
    throw new Error("서버와 연결할 수 없습니다. Spring Boot 서버가 실행 중인지 확인해 주세요.");
  }

  if (response.status === 204) {
    return null;
  }

  const text = await response.text();
  const data = parseJson(text);

  if (!response.ok) {
    throw new Error(resolveErrorMessage(data, text, response.status));
  }

  return data;
}

function parseJson(text) {
  if (!text) {
    return null;
  }

  try {
    return JSON.parse(text);
  } catch (error) {
    return null;
  }
}

function resolveErrorMessage(data, text, status) {
  if (data?.errorMessage) {
    return data.errorMessage;
  }

  if (data?.message) {
    return data.message;
  }

  if (text) {
    return text;
  }

  if (status === 404) {
    return "요청한 데이터를 찾을 수 없습니다.";
  }

  return "요청을 처리하지 못했습니다. 잠시 후 다시 시도해 주세요.";
}
