# PostgreSQL 13 (Alpine 버전) 이미지를 사용합니다.
FROM postgres:13-alpine

# PostgreSQL 환경 변수 설정 (필요에 따라 값 변경)
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres
ENV POSTGRES_DB=mydatabase

# PostgreSQL 기본 포트 노출
EXPOSE 5432

# 필요시 초기화 스크립트 복사
# Docker 컨테이너 시작 시 /docker-entrypoint-initdb.d/ 폴더에 있는 모든 스크립트를 실행합니다.
# COPY init.sql /docker-entrypoint-initdb.d/

# PostgreSQL 공식 이미지의 엔트리포인트 스크립트가 자동으로 실행됩니다.