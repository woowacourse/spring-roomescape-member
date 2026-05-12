async function parseError(response) {
  const text = await response.text();

  if (text) {
    try {
      const body = JSON.parse(text);
      throw new Error(body.message || text);
    } catch (error) {
      if (error instanceof SyntaxError) {
        throw new Error(text);
      }

      throw error;
    }
  }

  throw new Error("요청 처리에 실패했습니다.");
}

export async function requestJson(url, options = {}) {
  const response = await fetch(url, options);

  if (!response.ok) {
    await parseError(response);
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}
