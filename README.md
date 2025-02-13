# 📝 게시판(BBS) API

## 🚀 프로젝트 개요
이 프로젝트는 여러 개의 게시판을 관리할 수 있는 **RESTful API**입니다.  
Spring Boot와 JPA를 기반으로 개발되었으며, **JWT 인증, 검색, 정렬, 댓글, 좋아요, 조회수 증가, 파일 업로드** 등을 포함합니다.

---

## 📌 1️⃣ 기술 스택
| 분류 | 기술 |
|------|------|
| **Backend** | Spring Boot, Spring Data JPA, Spring Security |
| **Database** | MySQL, PostgreSQL |
| **Authentication** | JWT (Json Web Token), OAuth2 (Google, GitHub) |
| **Cache** | Redis (조회수, 인기순 정렬) |
| **Search** | ElasticSearch (게시글 검색) |
| **Logging** | SLF4J, Logback |
| **File Upload** | Amazon S3, Local Storage |
| **API Documentation** | Swagger, Spring REST Docs |
| **Testing** | JUnit, Mockito |
| **Deployment** | Docker, GitHub Actions (CI/CD) |

---

## 📌 2️⃣ 주요 기능
### ✅ **게시판 관리**
- 여러 개의 게시판을 생성하고 관리 가능
- 게시판별로 게시글 구분

### ✅ **게시글 CRUD**
- 특정 게시판에 게시글 작성
- 페이징, 검색, 정렬 기능 제공

### ✅ **댓글 CRUD**
- 댓글 및 대댓글 작성 가능
- 댓글에 대한 좋아요 기능 제공

### ✅ **좋아요 기능**
- 게시글 및 댓글 좋아요 추가/취소
- 좋아요 개수 조회 가능

### ✅ **조회수 증가**
- IP/세션 기반 중복 방지 적용
- Redis 캐싱을 활용한 조회수 증가 최적화

### ✅ **검색 및 정렬**
- 게시글 제목, 내용, 작성자로 검색 가능
- 인기순, 최신순, 조회수순 정렬 기능 제공

### ✅ **파일 업로드**
- 게시글 및 댓글에 파일(이미지, PDF 등) 첨부 가능
- Amazon S3 또는 로컬 저장소 지원

### ✅ **JWT 인증 및 보안**
- JWT 기반 로그인/회원가입
- OAuth2 로그인 (Google, GitHub)
- 관리자 권한(Role) 관리

---

## 📌 3️⃣ API 엔드포인트

### 📍 **1. 게시판 관리**
| HTTP | 엔드포인트 | 설명 |
|------|-----------|------|
| `POST` | `/api/boards` | 새로운 게시판 생성 |
| `GET` | `/api/boards` | 전체 게시판 목록 조회 |
| `GET` | `/api/boards/{boardId}` | 특정 게시판 조회 |

### 📍 **2. 게시글 관리**
| HTTP | 엔드포인트 | 설명 |
|------|-----------|------|
| `POST` | `/api/boards/{boardId}/posts` | 특정 게시판에 게시글 작성 |
| `GET` | `/api/boards/{boardId}/posts` | 특정 게시판의 게시글 목록 조회 |
| `GET` | `/api/posts/{postId}` | 특정 게시글 상세 조회 |
| `PUT` | `/api/posts/{postId}` | 게시글 수정 |
| `DELETE` | `/api/posts/{postId}` | 게시글 삭제 |

### 📍 **3. 댓글 관리**
| HTTP | 엔드포인트 | 설명 |
|------|-----------|------|
| `POST` | `/api/posts/{postId}/comments` | 특정 게시글에 댓글 등록 |
| `GET` | `/api/posts/{postId}/comments` | 특정 게시글의 댓글 목록 조회 |
| `PUT` | `/api/comments/{commentId}` | 댓글 수정 |
| `DELETE` | `/api/comments/{commentId}` | 댓글 삭제 |

### 📍 **4. 좋아요 기능**
| HTTP | 엔드포인트 | 설명 |
|------|-----------|------|
| `POST` | `/api/posts/{postId}/like` | 게시글 좋아요 추가 |
| `DELETE` | `/api/posts/{postId}/like` | 게시글 좋아요 취소 |
| `POST` | `/api/comments/{commentId}/like` | 댓글 좋아요 추가 |
| `DELETE` | `/api/comments/{commentId}/like` | 댓글 좋아요 취소 |
