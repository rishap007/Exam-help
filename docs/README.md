# 🎓 EduPlatform Documentation

## 📋 Project Overview
EduPlatform is a comprehensive online learning management system that allows students to enroll in courses, track progress, and interact with educational content.

## 🏗️ Technology Stack
- **Backend**: Spring Boot 3.x + Java 17
- **Frontend**: React 18 + TypeScript + Vite
- **Database**: PostgreSQL 15
- **Cache**: Redis 7
- **Storage**: MinIO (S3 compatible)
- **Authentication**: JWT-based

## 🚀 Quick Start
1. Clone this repository
2. Run: `docker-compose up --build`
3. Frontend: http://localhost:3000
4. Backend API: http://localhost:8080/api/v1

## 📁 Documentation Structure
- [Backend Documentation](./backend/) - Spring Boot API documentation
- [Frontend Documentation](./frontend/) - React application documentation  
- [Project Information](./project-info/) - Setup, deployment, and workflows

## ✅ Current Status
### Working Features:
- ✅ User Authentication (Register/Login/JWT)
- ✅ User Profile Management
- ✅ Course Management System
- ✅ Enrollment System (Enroll/Unenroll)
- ✅ Progress Tracking System
- ✅ Role-based Access Control

### Total Endpoints: 35+ working endpoints

## 📞 Development Team
- **Developer**: [Rishap] no:9060309901
- **Started**: October 2025
- **Last Updated**: $(Get-Date -Format "MMMM dd, yyyy")

---
*This documentation is maintained alongside the codebase. Please update when making changes.*
"@ | Out-File -FilePath "docs\README.md" -Encoding UTF8