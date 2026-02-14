# Context Core - Análise Macro do Projeto BookSalesOnline

**Data da Análise**: 2026-02-07  
**Versão do Documento**: 1.0  
**Status**: Produção

---

## 📋 Tipo de Aplicação

**API REST de Catálogo de Livros Online** - Aplicação monolítica de backend para gerenciamento de vendas de livros, com suporte a:
- Publicações e publicadores (editoras)
- Idiomas suportados
- Países de origem
- Imagens em armazenamento em nuvem (AWS S3)
- Caching distribuído (Redis)
- Métricas e observabilidade (Prometheus)

---

## 🛠️ Stack Tecnológica

### Linguagem & Runtime
- **Java**: 11
- **JDK**: Eclipse Temurin 17 (Dockerfile)

### Frameworks & Bibliotecas Principais
| Componente | Versão | Propósito |
|-----------|--------|----------|
| Spring Boot | 2.7.9 | Framework web e injeção de dependência |
| Spring Data JPA | 2.7.9 | ORM e acesso a dados |
| Spring Cache | 3.1.0 | Abstração de cache |
| Spring Data Redis | 3.3.3 | Client Redis |
| PostgreSQL Driver | 42.5.4 | Banco de dados relacional |
| Jedis | 3.10.0 | Cliente Redis Java |
| Jackson Core | 2.13.5 | Serialização JSON |
| AWS Java SDK | 1.12.481 | Integração com S3 |
| Micrometer Prometheus | Latest | Métricas e monitoramento |
| SpringDoc OpenAPI | 1.8.0 | Documentação Swagger/OpenAPI |
| Lombok | 1.18.34 | Geração de boilerplate |
| JetBrains Annotations | 24.1.0 | Validação estática |

### Dependências de Teste
| Componente | Versão | Propósito |
|-----------|--------|----------|
| JUnit 5 | Latest | Framework de testes |
| AssertJ | 3.24.2 | Assertions fluentes |
| Rest Assured | 5.3.0 | Testes de API |
| TestContainers | 1.18.1 | Containers para testes (PostgreSQL, Redis) |
| PIT Mutation | 1.16.1 | Testes de qualidade (mutation testing) |
| ArchUnit | 1.2.1 | Validação arquitetural em tempo de teste |
| JaCoCo | 0.8.8 | Cobertura de código |

### Infraestrutura
- **Banco de Dados**: PostgreSQL
- **Cache Distribuído**: Redis
- **Storage em Nuvem**: AWS S3 (LocalStack em desenvolvimento)
- **Containerização**: Docker + Docker Compose
- **Build**: Maven 3.x (wrapper `./mvnw`)

### Configuração de Porta
- **API**: `localhost:8081`
- **Context Path**: `/api/v1`
- **API Docs**: `/api/v1/api-docs`
- **Actuator**: `/actuator`

---

## 🏗️ Arquitetura Geral

### Padrão Predominante

**Arquitetura Hexagonal (Ports & Adapters)** implementada com **Clean Architecture**

#### Princípios
- Separação clara entre domínio (core), aplicação (use cases) e adaptadores (infraestrutura)
- Inversão de dependências através de interfaces (ports)
- Camadas não conhecem implementações concretas, apenas abstrações
- Testes de unidade podem mockar todas as dependências

#### Fluxo de Requisição
```
HTTP Request
    ↓
[Controller v1] (entrada)
    ↓
[DTO Mapper] (transformação)
    ↓
[Use Case] (lógica de negócio)
    ↓
[Repository Mediator] (service locator)
    ↓
[Repository] (implements DataQuery/DataCommand)
    ↓
[Entity Mapper] (transformação)
    ↓
[JPA Entity] → Database
```

### Organização por Camadas

#### **1. Domínio (`domain/`)**
Núcleo puro da aplicação, sem dependências técnicas.

```
domain/
├── Country.java              # Entidade: País
├── Publisher.java            # Entidade: Editora
├── Publication.java          # Entidade: Publicação
├── Language.java             # Entidade: Idioma
├── PublicationImage.java     # Entidade: Imagem de Publicação
├── PublicationImageContent.java
├── commom/
│   ├── BaseDomain.java       # Classe base: id + name
│   └── ...
├── enums/                    # Tipos enumerados
└── exceptions/               # Exceções de domínio
```

**Características**:
- Herdam de `BaseDomain` (id + name)
- Validações via `@NotBlank`, `@NotNull`
- Sem dependências do Spring
- Data objects com Lombok `@Data`
- Sem lógica anêmica (comportamento encapsulado)

#### **2. Aplicação (`application/`)**
Orquestra use cases e define contratos.

```
application/
├── usecases/
│   ├── CreateEntityUseCaseImpl.java    # CRUD genérico
│   ├── GetAllEntitiesUseCaseImpl.java
│   ├── GetEntityByIdUseCaseImpl.java
│   ├── UpdateEntityUseCaseImpl.java
│   ├── RemoveEntityUseCaseImpl.java
│   ├── country/
│   │   ├── RemoveCountryUseCaseImpl.java
│   │   └── GetPublishersByCountryUseCaseImpl.java
│   ├── publication/
│   │   ├── CreatePublicationImageUseCaseImpl.java
│   │   └── ...
│   └── publisher/
│       ├── CreatePublisherUseCaseImpl.java
│       ├── UpdatePublisherUseCaseImpl.java
│       └── RemovePublisherUseCaseImpl.java
│
├── mediators/
│   ├── UseCaseMediatorImpl.java        # Service Locator para Use Cases
│   └── RepositoryMediatorImpl.java     # Service Locator para Repos
│
└── ports/
    ├── in/                             # Contratos de entrada (use cases)
    │   ├── usecases/
    │   │   ├── CreateEntityUseCase.java
    │   │   ├── GetAllEntitiesUseCase.java
    │   │   └── ...
    │   └── common/
    │       ├── UseCaseMediator.java
    │       └── RepositoryMediator.java
    └── out/                            # Contratos de saída (persistência)
        ├── DataCommand.java            # save(), remove()
        ├── DataQuery.java              # getAll(), getById()
        ├── base/DataCommandQuery.java
        ├── publisher/
        ├── image/
        ├── publication/
        └── storage/
```

**Características**:
- Use cases implementam lógica orquestrada
- Genéricos: CRUD padrão
- Específicos: operações por agregado
- Mediators (antipadrão atual) resolvem dependências via reflection

#### **3. Adaptadores (`adapters/`)**
Conecta core à infraestrutura e mundo exterior.

##### **Controllers (`/controllers/v1/`)**
```
adapters/controllers/v1/
├── CountryController.java
├── PublisherController.java
├── PublicationController.java
├── LanguageController.java
├── ImageController.java
├── model/                    # DTOs
│   ├── CountryDto.java
│   ├── PublisherDto.java
│   └── ...
├── mappers/                  # DTO ↔ Domain
│   ├── CountryDtoMapper.java
│   ├── PublisherDtoMapper.java
│   └── ...
└── commom/
    └── BaseDto.java
```

**Responsabilidades**:
- Mapear HTTP → Use Cases
- Validar entrada (anotações `@Valid`)
- Retornar status HTTP apropriados
- Caching em nível de consulta (`@Cacheable`)

**Endpoints Principais**:
- `GET /api/v1/countries` - Listar países
- `GET /api/v1/countries/{id}` - Obter país por ID
- `GET /api/v1/countries/{id}/publishers` - Editoras por país
- `POST /api/v1/countries` - Criar país
- `PUT /api/v1/countries/{id}` - Atualizar país
- `DELETE /api/v1/countries/{id}` - Remover país

##### **Repositórios (`/repositories/`)**
```
adapters/repositories/
├── CountryRepository.java          # implements DataQuery, DataCommand
├── PublisherRepository.java
├── PublicationRepository.java
├── LanguageRepository.java
├── ImageRepository.java
│
├── data/
│   ├── CountryData.java            # extends JpaRepository
│   ├── PublisherData.java
│   └── ...
│
├── entities/
│   ├── CountryEntity.java          # @Entity JPA
│   ├── PublisherEntity.java
│   └── ...
│
└── mappers/
    ├── CountryEntityMapper.java    # Entity ↔ Domain
    ├── PublisherEntityMapper.java
    └── ...
```

**Padrão**:
- `*Repository`: Implementa ports, orquestra `*Data` + `*EntityMapper`
- `*Data`: Extends `JpaRepository<Entity, Integer>`
- `*Entity`: Modela tabela relacional
- `*EntityMapper`: Converte Entity ↔ Domain

##### **Storage (`/storage/`)**
```
adapters/storage/
└── S3StorageService.java          # Upload/download em S3/LocalStack
```

##### **Configuration (`/configuration/`)**
```
adapters/configuration/
├── AwsS3Config.java               # Bean AmazonS3Client
├── model/
│   └── AwsConfigProperties.java    # Properties binding
└── toggles/
    └── Feature toggles (experimental)
```

---

## 📝 Convenções

### Nomenclatura por Tipo

| Tipo | Padrão | Exemplo |
|------|--------|---------|
| Entidade de Domínio | `[Nome]` | `Country`, `Publisher` |
| DTO | `[Nome]Dto` | `CountryDto`, `PublisherDto` |
| Entidade JPA | `[Nome]Entity` | `CountryEntity`, `PublisherEntity` |
| Repositório Data | `[Nome]Data` | `CountryData`, `PublisherData` |
| Repositório (Adapter) | `[Nome]Repository` | `CountryRepository`, `PublisherRepository` |
| Mapper DTO | `[Nome]DtoMapper` | `CountryDtoMapper`, `PublisherDtoMapper` |
| Mapper Entity | `[Nome]EntityMapper` | `CountryEntityMapper`, `PublisherEntityMapper` |
| Use Case | `[Verbo][Nome]UseCaseImpl` | `CreateCountryUseCaseImpl`, `RemovePublisherUseCaseImpl` |
| Use Case (genérico) | `[Verbo]EntityUseCaseImpl` | `CreateEntityUseCaseImpl`, `GetAllEntitiesUseCaseImpl` |
| Controller | `[Nome]Controller` | `CountryController`, `PublisherController` |
| Mediator | `[Nome]MediatorImpl` | `UseCaseMediatorImpl`, `RepositoryMediatorImpl` |
| Interface de Port | `[Nome]UseCase` ou `[Nome]Mediator` | `CreateEntityUseCase`, `UseCaseMediator` |
| Exceção | `[Nome]Exception` | `ValidationException`, `RemoveException` |

### Organização de Pastas

- **Versionamento de API**: Todas as camadas de controllers em `/v1/`
- **Separação clara**: `domain/` | `application/` | `adapters/`
- **Agrupamento por agregado**: `country/`, `publisher/`, `publication/`
- **Camadas horizontais**: `mappers/`, `entities/`, `data/` dentro de `/repositories/`
- **⚠️ Typo sistemático**: Pasta `commom/` em vez de `common/` (pendente correção)

### Versionamento de Recursos
- **API Version**: `/api/v1`
- **DTOs**: Localizados em `/v1/model/`
- **Controllers**: Localizados em `/v1/`
- **Futura**: Preparado para `/v2/` (estrutura escalável)

---

## 🎯 Responsabilidades por Camada

### Camada de Domínio

**Responsabilidade Central**: Modelar entidades de negócio puras

**Não faz**:
- ❌ Depender de Spring ou bibliotecas técnicas
- ❌ Acessar banco de dados
- ❌ Fazer HTTP requests
- ❌ Serializar JSON

**Faz**:
- ✅ Encapsula dados de negócio (`id`, `name`, atributos específicos)
- ✅ Validações de domínio (`@NotBlank`, `@NotNull`)
- ✅ Comportamentos de negócio (regras)
- ✅ Define exceções de domínio

**Exemplo - Country**:
```java
@Data
public class Country extends BaseDomain {
    @NotBlank
    private String nationality;
    
    // Comportamento de negócio
    public boolean isValidForMarket(Publication pub) {
        // validação de lógica
    }
}
```

### Camada de Aplicação

**Responsabilidade Central**: Orquestrar use cases

**Não faz**:
- ❌ Implementações técnicas diretas
- ❌ SQL ou queries específicas

**Faz**:
- ✅ Coordena uso de repositórios
- ✅ Aplica regras transversais
- ✅ Valida pré-condições
- ✅ Lança exceções apropriadas
- ✅ Compõe lógica complexa

**Exemplo - CreateEntityUseCaseImpl**:
```java
@Service
public class CreateEntityUseCaseImpl implements CreateEntityUseCase {
    protected final RepositoryMediator mediator;
    
    public <T> T execute(Class<T> clazz, BaseDomain domain) {
        // Validação (chamada por anotação no domain)
        var command = mediator.getCommand(clazz);
        return command.save((T) domain);
    }
}
```

### Camada de Adaptadores

**Responsabilidade Central**: Traduzir entre domínio e infraestrutura

#### Controllers
- Validam HTTP input (`@Valid`, `@PathVariable`, `@RequestBody`)
- Transformam DTO → Domain via mappers
- Chamam use cases via mediators
- Transformam resultado → DTO
- Retornam HTTP status apropriados
- Implementam caching (`@Cacheable`)

#### Repositórios
- Implementam contracts de ports (`DataQuery<T>`, `DataCommand<T>`)
- Mapeiam Domain ↔ Entity JPA
- Delegam para Spring Data (`*Data` extends `JpaRepository`)
- Aplicam lógica de persistência específica

#### Mappers
- **DtoMapper**: Domínio ↔ DTO (serialização HTTP)
- **EntityMapper**: Domínio ↔ JPA Entity (persistência BD)
- Conversão bidirecional sem lógica de negócio

#### Storage
- Abstrai acesso a S3 (LocalStack em dev)
- Upload/download de imagens

#### Configuration
- Beans Spring
- Properties binding
- Feature toggles

---

## ⚠️ Anti-padrões Identificados

### 🔴 1. Service Locator Antipattern (Crítico)

**Problema**:
```java
// UseCaseMediatorImpl
public <T> T get(Class<T> clazz) throws NoSuchMethodException {
    return (T) useCases.get(clazz);  // Runtime lookup!
}

// Controller
var createUseCase = mediator.get(CreateEntityUseCase.class);
createUseCase.execute(...);
```

**Impacto**:
- ❌ IDE não consegue refatorar (refactoring safe breaks)
- ❌ Erros só aparecem em tempo de execução
- ❌ `throws NoSuchMethodException` em controllers
- ❌ Difícil de testar (mock complexo)
- ❌ Coupling implícito

**Solução Preferida** (Dependency Injection):
```java
@RestController
@AllArgsConstructor
public class CountryController {
    private final CreateEntityUseCase createUseCase;
    private final GetAllEntitiesUseCase getAllUseCase;
    // ... campo por use case específico
    
    // Sem necessidade de mediator
}
```

### 🟠 2. Typo em Nomenclatura Sistemática

**Problema**: Pasta `commom/` em vez de `common/`

**Localidades**:
- `/src/main/java/com/renan/booksalesonline/domain/commom/`
- `/src/main/java/com/renan/booksalesonline/adapters/controllers/v1/commom/`

**Impacto**:
- Dificulta busca por "common"
- Confusão em pair programming
- Persistirá em histórico git

**Solução**: Renomear com `git mv`

### 🟠 3. Versionamento de API Incompleto

**Problema**: 
- Todos os DTOs, mappers e controllers estão em `/v1/`
- Sem estrutura isolada para `/v2/`
- Mudanças quebram compatibilidade

**Impacto**:
- Impossível manter múltiplas versões
- Deprecação de endpoints difícil

**Solução**:
```
controllers/
├── v1/
│   ├── CountryController.java
│   └── model/CountryDto.java
├── v2/
│   ├── CountryControllerV2.java
│   └── model/CountryDtoV2.java
```

### 🟠 4. Falta de Tratamento Global de Erros

**Problema**: Sem `@RestControllerAdvice`

**Impacto**:
- Controllers repetem error handling
- Inconsistência em responses de erro
- HTTP status code não padronizado

**Solução**: Implementar GlobalExceptionHandler
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex) {
        return ResponseEntity.status(400).body(...);
    }
}
```

### 🟠 5. Caching Sem Estratégia de Invalidação

**Problema**:
```java
@GetMapping("/countries/{id}")
@Cacheable(value = "countryDto", key = "#id")
public CountryDto getCountryById(@PathVariable int id) { ... }

@PutMapping("/countries/{id}")
public CountryDto update(@PathVariable int id, ...) {
    // Cache NÃO é invalidado! Dados stale!
}
```

**Impacto**:
- Dados desincronizados com banco
- Usuários veem informações antigas
- Erros intermitentes difíceis de debug

**Solução**: Adicionar `@CacheEvict`
```java
@PutMapping("/countries/{id}")
@CacheEvict(value = "countryDto", key = "#id")
public CountryDto update(@PathVariable int id, ...) { ... }

@DeleteMapping("/countries/{id}")
@CacheEvict(value = "countryDto", key = "#id")
public void delete(@PathVariable int id) { ... }
```

### 🟡 6. Entidades JPA Espelham Domain (Duplicação)

**Problema**:
```java
// Domain
public class Country extends BaseDomain {
    private String nationality;
}

// JPA Entity
@Entity
public class CountryEntity {
    private int id;
    private String name;
    private String nationality;  // Duplicado!
}
```

**Impacto**:
- Mudança em persistência replica para domínio
- Difícil manter sincronizados
- Violação DRY

**Solução**:
- Usar `@Embedded` para POJOs compartilhados
- Ou aceitar como "boundary object"

### 🟡 7. Casting Unsafe em RepositoryMediatorImpl

**Problema**:
```java
var command = mediator.getCommand(clazz);
return command.save((T) domain);  // Type erasure!
```

**Impacto**:
- Erros em runtime se tipo errado
- Sem segurança de tipo

**Solução**: Generic Factory Pattern
```java
public interface RepositoryFactory {
    <T extends BaseDomain> DataCommand<T> getCommand(Class<T> clazz);
}
```

### 🟡 8. Falta de Logging Estruturado

**Problema**: Sem logs observáveis (SLF4J)

**Impacto**:
- Dificuldade em troubleshooting
- Sem rastreamento de requisições

**Solução**: Adicionar SLF4J + correlationId
```java
@Slf4j
@Service
public class CreateEntityUseCaseImpl {
    public <T> T execute(Class<T> clazz, BaseDomain domain) {
        log.info("Creating entity of type={} with id={}", clazz.getName(), domain.getId());
        // ...
    }
}
```

### 🟡 9. Validações Apenas em Domain (DTOs sem @Valid)

**Problema**:
```java
@PostMapping("/countries")
public CountryDto create(@RequestBody CountryDto countryRequest) {
    // Sem validação de JSON!
}
```

**Impacto**:
- DTOs podem ser criados inválidos
- Validação ocorre tarde no ciclo

**Solução**: Duplicar validações ou usar `@Valid`
```java
@PostMapping("/countries")
public CountryDto create(@Valid @RequestBody CountryDto countryRequest) { ... }
```

### 🟡 10. Acoplamento ao AWS SDK Concreto

**Problema**:
```java
@Autowired AmazonS3Client amazonS3;  // Acoplado ao AWS!
```

**Impacto**:
- Difícil migrar para outro storage (GCS, Azure)
- Testes requerem mocking do AWS SDK

**Solução**: Abstração via interface
```java
public interface StoragePort {
    void upload(String path, byte[] content);
    byte[] download(String path);
}

public class S3StorageAdapter implements StoragePort {
    @Autowired AmazonS3Client amazonS3;
    // ...
}
```

---

## 🧪 Estratégia de Testes

### Níveis de Teste Implementados

| Nível | Tipo | Ferramenta | Exemplo |
|-------|------|-----------|---------|
| Unit | JUnit 5 | AssertJ | `*UseCaseImplTest.java` |
| Integration | TestContainers | PostgreSQL, Redis | `*RepositoryTest.java` |
| Functional | Rest Assured | `SpringBootTest` | `*ControllerFunctionalTest.java` |
| Architecture | ArchUnit | Validação de arquitetura | (pendente) |
| Mutation | PIT | Qualidade de testes | `pitest:mutationCoverage` |
| Coverage | JaCoCo | Cobertura de código | `target/site/jacoco/` |

### Estrutura de Testes

```
src/test/java/com/renan/booksalesonline/tests/
├── adapters/
│   ├── controllers/v1/
│   │   ├── CountryControllerFunctionalTest.java
│   │   ├── PublisherControllerFunctionalTest.java
│   │   └── mappers/
│   │       ├── CountryDtoMapperTest.java
│   │       └── ...
│   ├── repositories/
│   │   ├── CountryRepositoryTest.java
│   │   ├── entities/
│   │   │   ├── CountryEntityTest.java
│   │   │   └── ...
│   │   └── mappers/
│   │       ├── CountryEntityMapperTest.java
│   │       └── ...
│   └── controllers/v1/model/
│       ├── CountryDtoTest.java
│       └── ...
├── application/
│   ├── usecases/
│   │   ├── CreateEntityUseCaseImplTest.java
│   │   ├── RemoveEntityUseCaseImplTest.java
│   │   ├── country/
│   │   │   ├── RemoveCountryUseCaseImplTest.java
│   │   │   └── ...
│   │   └── publisher/
│   │       ├── CreatePublisherUseCaseImplTest.java
│   │       └── ...
│   └── mediators/
│       ├── UseCaseMediatorImplTest.java
│       └── RepositoryMediatorImplTest.java
├── domain/
│   └── (domain objects são testados via use cases)
└── testhelpers/
    ├── BookSalesOnlineContainerTest.java
    └── RestClientTesting.java
```

### Padrões de Teste

**Functional Test**:
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CountryControllerFunctionalTest extends BookSalesOnlineContainerTest {
    // TestContainers de PostgreSQL + Redis
    // Testes via Rest Assured
}
```

**Unit Test**:
```java
public class CreateCountryUseCaseImplTest {
    @Mock private RepositoryMediator mediator;
    @InjectMocks private CreateEntityUseCaseImpl useCase;
    
    @Test
    void shouldCreateCountry() { ... }
}
```

---

## 🚀 Como Começar

### Setup Local

1. **Pré-requisitos**:
   ```bash
   java -version      # Java 11+
   mvn -version       # Maven 3.6+
   docker --version   # Docker 20.10+
   ```

2. **Clonar e entrar no projeto**:
   ```bash
   git clone <repo>
   cd bookSalesOnline
   ```

3. **Subir infraestrutura**:
   ```bash
   docker-compose up -d postgres redis localstack
   ```

4. **Executar aplicação**:
   ```bash
   ./mvnw spring-boot:run
   ```

5. **Acessar API**:
   - Base: `http://localhost:8081/api/v1`
   - Swagger: `http://localhost:8081/api/v1/swagger-ui.html`
   - API Docs: `http://localhost:8081/api/v1/api-docs`

### Executar Testes

```bash
# Testes unitários
./mvnw test

# Testes com cobertura
./mvnw clean verify

# Relatório de cobertura
./mvnw clean verify
open target/site/jacoco/index.html

# Mutation testing
./mvnw pitest:mutationCoverage
open target/pit-reports/index.html
```

---

## 📊 Fluxo de Desenvolvimento

### Adicionar Nova Feature (Ex: Criar nova entidade "Category")

1. **Criar entidade de domínio**:
   ```java
   // src/main/java/com/renan/booksalesonline/domain/Category.java
   public class Category extends BaseDomain {
       @NotBlank
       private String description;
   }
   ```

2. **Criar JPA Entity**:
   ```java
   // src/main/java/com/renan/booksalesonline/adapters/repositories/entities/CategoryEntity.java
   @Entity
   @Table(name = "category")
   public class CategoryEntity { ... }
   ```

3. **Criar Data Repository**:
   ```java
   // src/main/java/com/renan/booksalesonline/adapters/repositories/data/CategoryData.java
   public interface CategoryData extends JpaRepository<CategoryEntity, Integer> { }
   ```

4. **Criar Mappers**:
   ```java
   // CategoryDtoMapper, CategoryEntityMapper
   ```

5. **Criar Repository Adapter**:
   ```java
   // src/main/java/com/renan/booksalesonline/adapters/repositories/CategoryRepository.java
   @Repository
   public class CategoryRepository implements DataQuery<Category>, DataCommand<Category> { ... }
   ```

6. **Criar DTOs**:
   ```java
   // src/main/java/com/renan/booksalesonline/adapters/controllers/v1/model/CategoryDto.java
   ```

7. **Criar Controller**:
   ```java
   // src/main/java/com/renan/booksalesonline/adapters/controllers/v1/CategoryController.java
   ```

8. **Registrar em Mediators**:
   - Adicionar em `RepositoryMediatorImpl`
   - Adicionar em `UseCaseMediatorImpl` (se use cases específicos)

9. **Testes**:
   - Unit tests para domain
   - Integration tests para repository
   - Functional tests para controller

---

## 🎓 Boas Práticas Estabelecidas

✅ **Padrões a Seguir**:

1. **Hierarquia de Domínio**:
   - Sempre estender `BaseDomain` (id + name)
   - Validar com `@NotBlank`, `@NotNull`

2. **Separação de Responsabilidades**:
   - Domain: negócio
   - Use Case: orquestração
   - Adapter: plumbing técnico
   - Controller: HTTP boundary

3. **Mapeamento**:
   - Sempre mapear entre camadas
   - Nunca passar Entity/DTO para domínio

4. **Nomeação**:
   - Seguir padrões de sufixos
   - Agrupar por agregado quando apropriado

5. **Testes**:
   - Testar comportamento, não implementação
   - Use cases devem ser isoláveis
   - Controllers testados via Rest Assured

⚠️ **Anti-padrões a Evitar**:

- ❌ Chamar repositórios diretamente de controllers
- ❌ Lógica de negócio em mappers ou controllers
- ❌ Expor JPA Entities em responses HTTP
- ❌ Usar mediators diretamente (refatorar para DI)
- ❌ Caching sem invalidação

---

## 🔧 Próximas Prioridades de Refatoração

### Curto Prazo (Sprint Atual)
1. ✅ Documentar arquitetura (este arquivo)
2. 🔲 Renomear `commom/` → `common/`
3. 🔲 Implementar `@RestControllerAdvice` global

### Médio Prazo (2-3 Sprints)
4. 🔲 Substituir Mediators por Dependency Injection direto
5. 🔲 Adicionar estratégia de cache invalidation (`@CacheEvict`)
6. 🔲 Padronizar hierarquia de exceções
7. 🔲 Adicionar logging estruturado (SLF4J + correlationId)

### Longo Prazo (Roadmap Técnico)
8. 🔲 Implementar versionamento de API (`/v2/`)
9. 🔲 Abstração de Storage via interface (StoragePort)
10. 🔲 Circuit breaker para chamadas externas
11. 🔲 Validação arquitetural com ArchUnit tests
12. 🔲 Event sourcing para auditoria

---

## 📚 Referências de Leitura

- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Hexagonal Architecture - Alistair Cockburn](https://alistair.cockburn.us/hexagonal-architecture/)
- [Domain-Driven Design - Eric Evans](https://en.wikipedia.org/wiki/Domain-driven_design)
- [Spring Boot Best Practices](https://spring.io/guides)

---

## 📞 Contato & Contribuições

Para dúvidas arquiteturais ou sugestões:
- Abrir issue com tag `architecture`
- Discutir em PR antes de implementar grandes mudanças
- Validar contra este documento antes de comprometer

**Última Atualização**: 2026-02-07  
**Mantido por**: Equipe de Arquitetura

