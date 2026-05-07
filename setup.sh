#!/bin/bash

set -e

FRONTEND_REPO="https://github.com/Woowa-8th-BE-IQ/spring-roomescape-member-front.git"
FRONTEND_DIR="spring-roomescape-member-front"
EXPECTED_BACKEND_NAME="spring-roomescape-member"

# ─── 1. 사전 검증 ───────────────────────────────────────────

# Docker 설치 확인
if ! command -v docker &> /dev/null; then
  echo "❌ Docker가 설치되어 있지 않습니다."
  echo "   https://www.docker.com/products/docker-desktop/ 에서 설치 후 다시 시도해주세요."
  exit 1
fi

# 백엔드 루트에서 실행했는지 확인
if [ ! -f "build.gradle" ] && [ ! -f "build.gradle.kts" ]; then
  echo "❌ 백엔드 프로젝트 루트(build.gradle이 있는 곳)에서 실행해주세요."
  exit 1
fi

# docker-compose.yml 확인
if [ ! -f "docker-compose.yml" ]; then
  echo "❌ docker-compose.yml을 찾을 수 없습니다."
  exit 1
fi

BACKEND_PATH="$(pwd)"
BACKEND_NAME="$(basename "$BACKEND_PATH")"
PARENT_PATH="$(dirname "$BACKEND_PATH")"

# 폴더 이름 검증 (docker-compose.yml이 'spring-roomescape-member' 이름을 기대함)
if [ "$BACKEND_NAME" != "$EXPECTED_BACKEND_NAME" ]; then
  echo "❌ 현재 백엔드 폴더 이름이 '$BACKEND_NAME' 입니다."
  echo "   docker-compose.yml은 '$EXPECTED_BACKEND_NAME' 이름을 기대합니다."
  echo "   폴더 이름을 '$EXPECTED_BACKEND_NAME'으로 변경해주세요."
  exit 1
fi

echo "🚀 방탈출 풀스택 환경 세팅을 시작합니다..."
echo "   백엔드: $BACKEND_PATH"
echo ""

# ─── 2. 프론트엔드 클론 ─────────────────────────────────────

FRONTEND_PATH="$PARENT_PATH/$FRONTEND_DIR"

if [ -d "$FRONTEND_PATH" ]; then
  echo "⚠️  프론트엔드 폴더가 이미 존재합니다. 클론을 스킵합니다."
else
  echo "📥 프론트엔드 레포 클론 중..."
  git clone "$FRONTEND_REPO" "$FRONTEND_PATH"
  echo "✅ 프론트엔드 클론 완료"
fi

# ─── 3. docker-compose.yml을 부모 디렉토리로 복사 ──────────

cp "$BACKEND_PATH/docker-compose.yml" "$PARENT_PATH/docker-compose.yml"
echo "✅ docker-compose.yml 복사 완료"

# ─── 4. Docker Compose 실행 ─────────────────────────────────

cd "$PARENT_PATH"

echo ""
echo "🐳 Docker Compose 빌드 및 실행 중..."
echo "   (첫 실행은 수 분 소요될 수 있습니다)"
echo ""

# 'docker compose' (v2) 우선 사용, 없으면 'docker-compose' (v1)
if docker compose version &> /dev/null; then
  docker compose up --build -d
else
  docker-compose up --build -d
fi

# ─── 5. 완료 메시지 ─────────────────────────────────────────

echo ""
echo "═══════════════════════════════════════════════════════"
echo "✅ 실행 완료!"
echo "═══════════════════════════════════════════════════════"
echo ""
echo "   🌐 프론트엔드 → http://localhost:4173  ← 여기로 접속!"
echo "   🔌 백엔드 API → http://localhost:8080"
echo ""
echo "   ⚠️  localhost:8080을 브라우저로 직접 열면"
echo "      'No static resource' 에러가 보이는데 이건 정상입니다."
echo "      백엔드는 REST API 서버이고, 화면은 4173에서 봐주세요."
echo ""
echo "───────────────────────────────────────────────────────"
echo "   종료:  cd $PARENT_PATH && docker compose down"
echo "   로그:  docker compose logs backend"
echo "═══════════════════════════════════════════════════════"
