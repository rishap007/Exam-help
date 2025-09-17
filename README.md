

## 📁 Project Structure

```
eduplatform/
├── frontend/                 # React application
│   ├── src/
│   │   ├── components/      # Reusable UI components
│   │   ├── pages/          # Page components
│   │   ├── services/       # API services
│   │   ├── utils/          # Utility functions
│   │   ├── hooks/          # Custom React hooks
│   │   └── context/        # React context providers
│   └── tests/              # Frontend tests
│
├── backend/                 # Spring Boot application
│   ├── src/main/java/com/eduplatform/
│   │   ├── controller/     # REST controllers
│   │   ├── service/        # Business logic
│   │   ├── repository/     # Data access layer
│   │   ├── model/          # Entity models
│   │   ├── config/         # Configuration classes
│   │   ├── security/       # Security configuration
│   │   └── dto/            # Data transfer objects
│   └── src/test/java/      # Backend tests
│
├── ai-service/             # Python AI service
│   ├── src/                # Python source code
│   ├── models/             # AI models
│   ├── tests/              # AI service tests
│   └── requirements/       # Python dependencies
│
├── infrastructure/         # Infrastructure as Code
│   ├── kubernetes/         # K8s manifests
│   ├── terraform/          # Terraform configs
│   ├── helm/              # Helm charts
│   └── monitoring/        # Monitoring configs
│
├── docker/                 # Docker configurations
│   ├── frontend/          # Frontend Dockerfile
│   ├── backend/           # Backend Dockerfile
│   ├── ai-service/        # AI service Dockerfile
│   ├── nginx/             # Nginx configuration
│   └── database/          # Database scripts
│
├── scripts/               # Development scripts
│   ├── development/       # Dev environment scripts
│   ├── deployment/        # Deployment scripts
│   ├── testing/           # Testing scripts
│   └── database/          # Database scripts
│
├── docs/                  # Documentation
│   ├── api/               # API documentation
│   ├── architecture/      # Architecture docs
│   ├── deployment/        # Deployment guides
│   └── user-guide/        # User documentation
│
└── config/                # Configuration files
    ├── development/       # Dev configs
    ├── staging/           # Staging configs
    └── production/        # Production configs
```

## 🛠️ Development Workflow

### Daily Development
```bash
# Start the development environment
./scripts/development/dev-start.sh

# View logs for all services
./scripts/development/dev-logs.sh

# View logs for specific service
./scripts/development/dev-logs.sh backend

# Open shell in backend container
./scripts/development/dev-shell.sh backend

# Check environment status
./scripts/development/dev-status.sh

# Stop environment
./scripts/development/dev-stop.sh

# Reset environment (removes all data!)
./scripts/development/dev-reset.sh
```

### Hot Reloading
- **Frontend**: Automatically reloads on file changes
- **Backend**: Supports Spring Boot DevTools hot reload
- **AI Service**: FastAPI auto-reload enabled in development

### Database Management
```bash
# Connect to PostgreSQL
./scripts/development/dev-shell.sh postgres

# View database in PgAdmin
# Visit http://localhost:5050
# Login: admin@eduplatform.com / admin

# Connect to Redis
./scripts/development/dev-shell.sh redis

# View Redis data
# Visit http://localhost:8081
```

## 🔧 Technology Stack

### Frontend
- **Framework**: React 18+ with TypeScript
- **Styling**: Tailwind CSS / Material-UI
- **State Management**: Redux Toolkit / Zustand
- **Routing**: React Router v6
- **HTTP Client**: Axios
- **Testing**: Jest + React Testing Library

### Backend
- **Framework**: Spring Boot 3.x
- **Language**: Java 17+
- **Database**: PostgreSQL 15
- **Cache**: Redis 7
- **Security**: Spring Security + JWT
- **Testing**: JUnit 5 + Testcontainers

### AI Service
- **Framework**: FastAPI
- **Language**: Python 3.11+
- **ML Libraries**: TensorFlow/PyTorch, Hugging Face
- **API**: OpenAI Integration
- **Testing**: pytest + httpx

### Infrastructure
- **Containerization**: Docker + Docker Compose
- **Orchestration**: Kubernetes
- **Reverse Proxy**: Nginx
- **Monitoring**: Prometheus + Grafana
- **CI/CD**: GitHub Actions / GitLab CI

## 🏃‍♂️ Available Scripts

### Development Scripts
| Script | Description |
|--------|-------------|
| `dev-start.sh` | Start complete development environment |
| `dev-stop.sh` | Stop all services gracefully |
| `dev-reset.sh` | Reset environment (⚠️ deletes all data) |
| `dev-logs.sh` | View service logs |
| `dev-shell.sh` | Open shell in service container |
| `dev-status.sh` | Check environment health status |

### Future Scripts (will be added)
| Script | Description |
|--------|-------------|
| `build-all.sh` | Build all Docker images |
| `test-all.sh` | Run all test suites |
| `deploy-staging.sh` | Deploy to staging environment |
| `backup-db.sh` | Backup database |
| `restore-db.sh` | Restore database from backup |

## 🔐 Environment Configuration

### Required Environment Variables

#### Database Configuration
```bash
DB_HOST=postgres
DB_PORT=5432
DB_NAME=eduplatform_dev
DB_USERNAME=eduplatform_user
DB_PASSWORD=your_secure_password
```

#### Authentication
```bash
JWT_SECRET=your_jwt_secret_key_min_256_bits
JWT_EXPIRATION=86400000
```

#### External Services
```bash
# AI Services
OPENAI_API_KEY=your_openai_api_key
HUGGINGFACE_API_TOKEN=your_huggingface_token

# Payment Processing
STRIPE_SECRET_KEY=sk_test_your_stripe_secret
STRIPE_PUBLISHABLE_KEY=pk_test_your_stripe_publishable

# Email Service
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USERNAME=your_email@gmail.com
SMTP_PASSWORD=your_app_password
```

### Environment Files
- `.env.example` - Template with all required variables
- `.env.local` - Your local development settings (gitignored)
- `.env.development` - Development environment defaults
- `.env.staging` - Staging environment settings
- `.env.production` - Production environment settings

## 🧪 Testing Strategy

### Testing Levels
1. **Unit Tests**: Individual component/function testing
2. **Integration Tests**: Service-to-service communication
3. **End-to-End Tests**: Complete user workflows
4. **Load Tests**: Performance and scalability
5. **Security Tests**: Vulnerability assessments

### Running Tests
```bash
# Frontend tests
docker-compose exec frontend npm test

# Backend tests
docker-compose exec backend ./gradlew test

# AI service tests
docker-compose exec ai-service pytest

# Integration tests (future)
./scripts/testing/run-integration-tests.sh

# Load tests (future)
./scripts/testing/run-load-tests.sh
```

## 📊 Monitoring & Observability

### Development Monitoring
- **Application Logs**: `./scripts/development/dev-logs.sh`
- **Database Admin**: http://localhost:5050
- **Redis Admin**: http://localhost:8081
- **Health Checks**: Built into each service

### Production Monitoring (Future)
- **Metrics**: Prometheus + Grafana
- **Logging**: ELK Stack / Fluentd
- **Tracing**: Jaeger
- **Uptime**: Custom health check endpoints

## 🚀 Deployment Strategy

### Development
- Docker Compose with hot reloading
- Local volume mounts for live code changes
- Development database with seed data

### Staging (Future)
- Kubernetes cluster
- Production-like environment
- Automated testing pipeline
- Blue-green deployments

### Production (Future)
- Kubernetes with auto-scaling
- High availability setup
- Monitoring and alerting
- Disaster recovery procedures

## 🔒 Security Features

### Authentication & Authorization
- JWT-based authentication
- Role-based access control (RBAC)
- Password encryption (bcrypt)
- Session management
- API rate limiting

### Data Security
- Database encryption at rest
- HTTPS/TLS encryption in transit
- Input validation and sanitization
- SQL injection prevention
- XSS protection

### Infrastructure Security
- Container security scanning
- Dependency vulnerability checks
- Security headers configuration
- Environment variable management
- Regular security updates

## 🤝 Contributing

### Development Setup
1. Follow the Quick Start guide
2. Create feature branch: `git checkout -b feature/your-feature`
3. Make changes and test locally
4. Run tests: `./scripts/testing/test-all.sh` (when available)
5. Commit changes: `git commit -m "Add your feature"`
6. Push branch: `git push origin feature/your-feature`
7. Create Pull Request

### Code Standards
- **Frontend**: ESLint + Prettier configuration
- **Backend**: Checkstyle + SpotBugs
- **Python**: Black + flake8
- **Git**: Conventional commit messages
- **Documentation**: Update README for significant changes

### Pull Request Process
1. Ensure all tests pass
2. Update documentation if needed
3. Add tests for new functionality
4. Request review from team members
5. Address review feedback
6. Squash commits before merge

## 📚 Documentation

### Available Documentation
- `docs/api/` - API documentation and schemas
- `docs/architecture/` - System architecture details
- `docs/deployment/` - Deployment procedures
- `docs/user-guide/` - End user documentation

### API Documentation
- Backend API: http://localhost:8080/swagger-ui.html (future)
- AI Service API: http://localhost:8000/docs (future)

## 🆘 Troubleshooting

### Common Issues

#### Services Won't Start
```bash
# Check Docker is running
docker info

# Check environment file exists
ls -la .env.local

# View service logs
./scripts/development/dev-logs.sh [service-name]

# Reset environment
./scripts/development/dev-reset.sh
```

#### Database Connection Issues
```bash
# Check PostgreSQL is running
./scripts/development/dev-status.sh

# Connect to database directly
./scripts/development/dev-shell.sh postgres

# Check database logs
./scripts/development/dev-logs.sh postgres
```

#### Port Conflicts
```bash
# Check what's using ports
lsof -i :3000  # Frontend
lsof -i :8080  # Backend
lsof -i :5432  # PostgreSQL

# Stop conflicting processes
sudo kill -9 [PID]
```

#### Memory Issues
```bash
# Check Docker memory usage
docker stats

# Clean up unused resources
docker system prune -f

# Increase Docker memory in Docker Desktop settings
```

### Getting Help
1. Check the logs: `./scripts/development/dev-logs.sh`
2. Verify environment: `./scripts/development/dev-status.sh`
3. Reset if needed: `./scripts/development/dev-reset.sh`
4. Check Docker resources: `docker system df`
5. Create issue in project repository

## 📅 Development Roadmap

### Phase 1: Foundation (Weeks 1-2) ✅
- [x] Project structure setup
- [x] Docker development environment
- [x] Basic documentation
- [ ] Git workflows and CI/CD basics

### Phase 2: Backend Core (Weeks 2-4)
- [ ] Spring Boot application setup
- [ ] Database schema and migrations
- [ ] Authentication and authorization
- [ ] Core API endpoints

### Phase 3: Frontend Core (Weeks 4-6)
- [ ] React application setup
- [ ] UI component library
- [ ] Authentication flow
- [ ] Basic user interface

### Phase 4: AI Integration (Weeks 6-8)
- [ ] AI service setup
- [ ] Model integration
- [ ] AI-powered features
- [ ] Machine learning pipeline

### Phase 5: Advanced Features (Weeks 8-12)
- [ ] Microservices architecture
- [ ] Payment integration
- [ ] Advanced dashboards
- [ ] Mobile responsiveness

### Phase 6: Production (Weeks 12-16)
- [ ] Kubernetes deployment
- [ ] Monitoring and logging
- [ ] Security hardening
- [ ] Performance optimization

## 📞 Support & Contact

- **Project Repository**: [Your GitHub/GitLab URL]
- **Documentation**: [Your Documentation URL]
- **Issue Tracker**: [Your Issue Tracker URL]
- **Team Chat**: [Your Slack/Discord URL]

## 📄 License

This project is licensed under the [MIT License](LICENSE) - see the LICENSE file for details.

