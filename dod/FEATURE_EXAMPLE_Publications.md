# Feature: CRUD de Publicações (Livros)

**Status**: Exemplo Prático do Template  
**Data**: 2026-02-07

---

## 📋 Objetivo

Permitir que usuários criem, leiam, atualizem e removam publicações (livros), incluindo associação com editoras, idiomas e upload de imagens de capa. Isso habilita a gestão completa do catálogo de livros da plataforma.

**Valor de negócio**:
- ✅ Usuários podem adicionar novos livros ao catálogo
- ✅ Sincronização automática de capa via S3
- ✅ Relacionamento correto Publisher → Publication → Language
- ✅ Suporte a múltiplos idiomas por publicação

---

## 🎯 Escopo Incluído

### 1. Domínio
```
- [x] Entidade Publication (extends BaseDomain)
  - title: String (obrigatório)
  - description: String
  - publisherId: Integer (FK para Publisher)
  - isbn: String (único, opcional)
  - publishedDate: LocalDate
  - language: Language (FK)

- [x] Entidade PublicationImage (extends BaseDomain)
  - publicationId: Integer (FK)
  - url: String (URL S3)
  - imageContent: PublicationImageContent

- [x] PublicationStatus enum (DRAFT, PUBLISHED, OUT_OF_PRINT)

- [x] Validações
  - @NotBlank em title
  - @NotNull em publisherId, language
  - ISBN format validation (regex)
```

### 2. Use Cases
```
- [x] CreatePublicationUseCase
  - Valida Publisher.id existe (FK)
  - Valida Language.id existe (FK)
  - Retorna Publication com ID gerado

- [x] GetPublicationByIdUseCase
  - Recupera por ID
  - Cache habilitado (@Cacheable)

- [x] UpdatePublicationUseCase
  - Valida FK novamente
  - Invalida cache (@CacheEvict)
  - Valida ISBN único

- [x] RemovePublicationUseCase
  - Valida existência
  - Remove imagens associadas (cascade)
  - Invalida cache

- [x] GetAllPublicationsUseCase
  - Com paginação (padrão 20 items)
  - Ordenação por publishedDate DESC

- [x] UploadPublicationImageUseCase
  - Upload para S3
  - Valida tamanho máximo (5MB)
  - Retry em timeout (3 tentativas)
```

### 3. Persistência
```
- [x] PublicationEntity
- [x] PublicationData (extends JpaRepository)
- [x] PublicationRepository (DataQuery, DataCommand)
- [x] PublicationImageEntity
- [x] PublicationImageData
- [x] PublicationImageRepository
- [x] Schema SQL:
  CREATE TABLE publication (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    title VARCHAR(500) NOT NULL,
    description TEXT,
    publisher_id INTEGER NOT NULL REFERENCES publisher(id) ON DELETE CASCADE,
    language_id INTEGER NOT NULL REFERENCES language(id),
    isbn VARCHAR(20) UNIQUE,
    published_date DATE,
    status VARCHAR(50) DEFAULT 'DRAFT',
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP
  );
  
  CREATE TABLE publication_image (
    id SERIAL PRIMARY KEY,
    publication_id INTEGER NOT NULL REFERENCES publication(id) ON DELETE CASCADE,
    url VARCHAR(500),
    created_at TIMESTAMP DEFAULT NOW()
  );
  
  -- Índices para performance
  CREATE INDEX idx_publication_publisher ON publication(publisher_id);
  CREATE INDEX idx_publication_language ON publication(language_id);
  CREATE INDEX idx_publication_isbn ON publication(isbn);
```

### 4. API (HTTP)
```
- [x] PublicationController
  - POST /api/v1/publications (CREATE)
    Request: { title, description, publisherId, languageId, isbn }
    Response: PublicationDto (201 CREATED)
  
  - GET /api/v1/publications (LIST)
    Query: ?page=0&size=20
    Response: PublicationDto[] (200 OK)
  
  - GET /api/v1/publications/{id} (READ)
    Response: PublicationDto com imagens (200 OK)
    Cache: 60 segundos
  
  - PUT /api/v1/publications/{id} (UPDATE)
    Request: { title, description, ... }
    Response: PublicationDto (200 OK)
    Cache evict: @CacheEvict
  
  - DELETE /api/v1/publications/{id} (DELETE)
    Response: null (204 NO_CONTENT)

- [x] ImageController
  - POST /api/v1/publications/{id}/images (UPLOAD)
    Request: MultipartFile
    Response: PublicationImageDto (201 CREATED)
    Upload para S3
  
  - GET /api/v1/publications/{id}/images (LIST IMAGENS)
    Response: PublicationImageDto[] (200 OK)
  
  - DELETE /api/v1/publications/{id}/images/{imageId} (REMOVER)
    Response: null (204 NO_CONTENT)
```

### 5. DTOs e Mappers
```
- [x] PublicationDto
  - id, title, description, publisherId, languageId, isbn, status
  - publisherName, languageName (para referência)

- [x] PublicationImageDto
  - id, publicationId, url

- [x] PublicationDtoMapper
  - toDomain(PublicationDto): Publication
  - fromDomain(Publication): PublicationDto
  - fromDomain(Publication[]): PublicationDto[]

- [x] PublicationEntityMapper
  - toDomain(PublicationEntity): Publication
  - toEntity(Publication): PublicationEntity
  - (validar conversão bidirecional)

- [x] PublicationImageDtoMapper
- [x] PublicationImageEntityMapper
```

### 6. Testes
```
- [x] Unit Tests (60+ casos)
  - Domain:
    [ ] Publication instantiation
    [ ] Validações (@NotBlank, @NotNull)
    [ ] ISBN format validation
  
  - Use Cases:
    [ ] CreatePublicationUseCase (happy path + erro FK)
    [ ] UpdatePublicationUseCase (happy path + erro ISBN duplicado)
    [ ] RemovePublicationUseCase (happy path + erro not found)
    [ ] UploadImageUseCase (happy path + erro tamanho)
  
  - Mappers:
    [ ] PublicationDtoMapper (DTO → Domain, Domain → DTO)
    [ ] PublicationEntityMapper (Entity → Domain, Domain → Entity)
    [ ] PublicationImageMappers

- [x] Integration Tests
  - Repository:
    [ ] Save publication
    [ ] Update with FK validation
    [ ] Remove with cascade
    [ ] Findall with pagination

- [x] E2E Tests
  - PublicationControllerFunctionalTest
    [ ] Fluxo 1: CREATE Publication
    [ ] Fluxo 2: READ Publication by ID (com cache)
    [ ] Fluxo 3: UPDATE Publication (com cache evict)
    [ ] Fluxo 4: DELETE Publication
    [ ] Fluxo 5: LIST Publications (paginado)
    [ ] Erro: CREATE com Publisher inexistente
    [ ] Erro: CREATE com ISBN duplicado
  
  - ImageControllerFunctionalTest
    [ ] Fluxo: UPLOAD Image
    [ ] Fluxo: LIST Images
    [ ] Erro: Upload arquivo > 5MB
    [ ] Erro: S3 timeout (mock)
```

### 7. Documentação
```
- [x] critical-test-flows.md atualizado com 5 fluxos novos
- [x] README.md: nova seção "Publication Management"
- [x] Swagger/OpenAPI: endpoints documentados
- [x] FEATURE_TEMPLATE.md: este documento
```

---

## 🚫 Fora de Escopo

```
❌ NÃO fazer nesta feature:

1. Busca Avançada (v2.0)
   - Busca por título/autor/ISBN
   - Filtros por status, data, idioma
   - Requer Elasticsearch

2. Recomendações (ML - futuro)
   - "Livros relacionados"
   - Machine Learning
   - Requer novo serviço

3. Integração ISBN
   - Buscar dados em API externa
   - ISBN lookup automático
   - Requer integração com serviço

4. Múltiplos Formatos
   - EPUB, PDF, MOBI
   - Conversão de formatos
   - Requer library de conversão

5. Controle de Permissões
   - "Apenas admin pode criar"
   - Spring Security roles
   - Será em feature separada

6. Integração Pagamento
   - Preço de venda
   - Royalties
   - Sistema separado

7. Sincronização de Estoque
   - Integração com warehouse
   - Controle de quantidade
   - Sistema separado

8. Auditoria
   - "Quem criou, quando, modificado por quem"
   - Histórico de mudanças
   - Feature separada (v2.0)

Justificativa: Estas funcionalidades requerem design adicional,
dependências externas e maior complexidade. Melhor implementar em
sprints subsequentes.
```

---

## 🔧 Dependências Técnicas

### 1. Dependências de Código

```
✅ Estender Domain Layer (BaseDomain):
   - Publication extends BaseDomain
   - PublicationImage extends BaseDomain
   - Validações: @NotBlank, @NotNull, regex para ISBN

✅ Usar Application Layer Pattern:
   - Criar use cases estendendo CreateEntityUseCase
   - Validação FK em CreatePublicationUseCase
   - Cache invalidation em UpdatePublicationUseCase

✅ Implementar Adapter Pattern:
   - Repository implements DataQuery<Publication>, DataCommand<Publication>
   - DTOs com @JsonProperty
   - Mappers: DtoMapper, EntityMapper

✅ Integração S3:
   - Usar S3StorageService existente
   - Upload de PublicationImageContent
   - Retry logic com timeout

✅ TestContainers:
   - PostgreSQL para testes
   - Redis para cache (opcional)
   - LocalStack para S3 (já existe)
```

### 2. Dependências de Biblioteca

```
Verificar se precisa adicionar:

<!-- Validação avançada -->
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>7.0.5</version>
</dependency>

<!-- Já tem todas as outras -->
- spring-boot-starter-web ✓
- spring-boot-starter-data-jpa ✓
- spring-boot-starter-cache ✓
- aws-java-sdk ✓
- lombk ✓
- jackson-core ✓
```

### 3. Banco de Dados

```
Schema necessário:
✓ Tabela: publication
✓ Tabela: publication_image
✓ Foreign keys: publisher_id, language_id
✓ Índices: publication_publisher, publication_language, publication_isbn
✓ ON DELETE CASCADE para integridade referencial
```

### 4. Configuração

```
application.properties:
- spring.jpa.properties.hibernate.jdbc.fetch_size=50
- aws.s3.bucket=booksalesonline-publicationImages (já existe)
- upload.max-file-size=5MB
- upload.timeout=30000 (ms)
```

### 5. Serviços Externos

```
✓ AWS S3 (via LocalStack em dev) - EXISTENTE
✓ PostgreSQL - EXISTENTE (TestContainers)
✓ Redis - OPCIONAL mas existente
✓ Nenhuma API externa necessária
```

---

## ⚠️ Riscos

### 🔴 Riscos Críticos

```
RISCO 1: Publisher ou Language não existem
┌─────────────────────────────────────────────────────┐
│ CREATE Publication com publisherId=99 (não existe)  │
│                                                      │
│ Impacto: Foreign key constraint violation           │
│          Usuário recebe erro 500 em vez de 400      │
│ Probabilidade: ALTA (usuário pode errar)            │
│ Severidade: CRÍTICA (quebra CRUD)                   │
│                                                      │
│ MITIGAÇÃO:                                          │
│ ✅ Validar em CreatePublicationUseCase:             │
│    if (publisherRepository.getById(pubId) == null) │
│       throw ValidationException("Publisher not found")
│                                                      │
│ ✅ Testar Fluxo: CREATE Publication (erro FK)       │
│ ✅ HTTP: Retornar 400 Bad Request, não 500          │
│ ✅ Mensagem: "Publisher com ID XXX não existe"      │
│                                                      │
│ Owner: Dev                                          │
│ Verificação: Code review + Unit test                │
└─────────────────────────────────────────────────────┘

RISCO 2: S3 Upload Timeout ou Falha
┌─────────────────────────────────────────────────────┐
│ Upload de imagem grande (10MB) para S3              │
│ Timeout após 30 segundos                            │
│                                                      │
│ Impacto: Usuário perde imagem, transação incompleta │
│ Probabilidade: MÉDIA (depende de tamanho arquivo)   │
│ Severidade: ALTA (feature crítica de upload)        │
│                                                      │
│ MITIGAÇÃO:                                          │
│ ✅ Validar tamanho ANTES de upload (max 5MB)        │
│    @Size(max = 5242880) // 5MB                      │
│                                                      │
│ ✅ Retry com circuit breaker:                       │
│    - Tentativa 1: timeout após 30s                  │
│    - Tentativa 2: wait 5s, retry                    │
│    - Tentativa 3: wait 10s, retry                   │
│    - Falha final: throw StorageException            │
│                                                      │
│ ✅ Usar CompletableFuture para async upload         │
│ ✅ Testar com LocalStack (não AWS real)             │
│ ✅ Testar com mock que simula timeout               │
│                                                      │
│ Owner: Dev + QA                                     │
│ Verificação: E2E test + Load test                   │
└─────────────────────────────────────────────────────┘

RISCO 3: Cache não invalida em UPDATE
┌─────────────────────────────────────────────────────┐
│ User 1: GET /publications/1 → cache por 60s         │
│ Admin: PUT /publications/1 (muda título)            │
│ User 1: GET /publications/1 → vê título ANTIGO      │
│                                                      │
│ Impacto: Usuários veem dados desincronizados        │
│ Probabilidade: ALTA (se esquecer @CacheEvict)       │
│ Severidade: CRÍTICA (data inconsistency)            │
│                                                      │
│ MITIGAÇÃO:                                          │
│ ✅ Adicionar em UpdatePublicationUseCase:           │
│    @CacheEvict(value = "publication", key = "#id") │
│                                                      │
│ ✅ Também em RemovePublicationUseCase               │
│                                                      │
│ ✅ Testar Fluxo 8: UPDATE + verifica cache evict    │
│ ✅ Code review: buscar @CacheEvict obrigatório      │
│ ✅ Checklist: "UPDATE sempre tem @CacheEvict"       │
│                                                      │
│ Owner: Dev                                          │
│ Verificação: Code review + E2E test                 │
└─────────────────────────────────────────────────────┘
```

### 🟠 Riscos Altos

```
RISCO 4: ISBN não é único (duplicado)
┌─────────────────────────────────────────────────────┐
│ Publication 1: ISBN = "978-3-16-148410-0"           │
│ Publication 2: ISBN = "978-3-16-148410-0" (igual!)  │
│                                                      │
│ Impacto: Constraint violation, erro 500             │
│ Probabilidade: MÉDIA (usuário pode digitar errado)  │
│ Severidade: ALTA (dados inválidos)                  │
│                                                      │
│ MITIGAÇÃO:                                          │
│ ✅ Schema: CREATE UNIQUE INDEX idx_isbn             │
│ ✅ Validar em UpdatePublicationUseCase              │
│ ✅ Testar: CREATE + UPDATE com ISBN duplicado       │
│ ✅ Retornar 400 Bad Request com mensagem clara      │
│                                                      │
│ Owner: Dev                                          │
│ Verificação: Unit test + E2E test                   │
└─────────────────────────────────────────────────────┘

RISCO 5: Mapper incompleto (novo campo)
┌─────────────────────────────────────────────────────┐
│ Adicionar campo: Publication.seoDescription         │
│ ESQUECEU de: PublicationDtoMapper.toDomain()        │
│                                                      │
│ Impacto: DTO.seoDescription = null quando deveria   │
│ Probabilidade: MÉDIA (erro humano em mapper)        │
│ Severidade: MÉDIA (bug em resposta HTTP)            │
│                                                      │
│ MITIGAÇÃO:                                          │
│ ✅ Usar recursive comparison em testes:             │
│    assertThat(actual)                              │
│      .usingRecursiveComparison()                   │
│      .isEqualTo(expected)                          │
│                                                      │
│ ✅ Checklist: "Novo campo = atualizar 4 mappers"    │
│    - PublicationDtoMapper (2 direções)              │
│    - PublicationEntityMapper (2 direções)           │
│                                                      │
│ ✅ Code review obrigatório em mappers               │
│ ✅ Testar mapper bidirecional em Fluxo 12           │
│                                                      │
│ Owner: Dev + Code Reviewer                          │
│ Verificação: Code review + Mapper unit test         │
└─────────────────────────────────────────────────────┘

RISCO 6: Paginação lenta com muitos registros
┌─────────────────────────────────────────────────────┐
│ SELECT * FROM publication → 100k registros          │
│ Query sem índices: 10+ segundos                     │
│                                                      │
│ Impacto: GET /publications muito lenta              │
│ Probabilidade: MÉDIA (crescimento futuro)           │
│ Severidade: ALTA (performance)                      │
│                                                      │
│ MITIGAÇÃO:                                          │
│ ✅ Criar índices no schema:                         │
│    CREATE INDEX idx_publication_publisher           │
│    CREATE INDEX idx_publication_language            │
│    CREATE INDEX idx_publication_isbn                │
│                                                      │
│ ✅ Paginação padrão: 20 items/página                │
│ ✅ Ordenação padrão: BY publishedDate DESC           │
│ ✅ Load test: 100k registros, medir tempo           │
│ ✅ Target: < 200ms para GET listagem                │
│                                                      │
│ Owner: Dev + DBA                                    │
│ Verificação: Load test antes/depois índices         │
└─────────────────────────────────────────────────────┘
```

### 🟡 Riscos Médios

```
RISCO 7: TestContainers lento
┌─────────────────────────────────────────────────────┐
│ Spin-up PostgreSQL + Redis + S3 = lento             │
│                                                      │
│ Impacto: Testes E2E demoram > 10 segundos           │
│ Probabilidade: BAIXA (usando @ClassRule)            │
│ Severidade: MÉDIA (developer experience)            │
│                                                      │
│ MITIGAÇÃO:                                          │
│ ✅ Usar @ClassRule (shared container)               │
│ ✅ Não criar novo container por teste               │
│ ✅ Target: < 5 segundos suite total                 │
│                                                      │
│ Owner: QA                                           │
│ Verificação: Medir tempo execução                   │
└─────────────────────────────────────────────────────┘

RISCO 8: Concorrência em uploads
┌─────────────────────────────────────────────────────┐
│ User A + User B uploadam mesma imagem simultaneamente
│                                                      │
│ Impacto: Race condition, nomes duplicados           │
│ Probabilidade: BAIXA (edge case)                    │
│ Severidade: MÉDIA (arquivo duplicado)               │
│                                                      │
│ MITIGAÇÃO:                                          │
│ ✅ Usar UUID em nome de arquivo:                    │
│    s3key = UUID.randomUUID() + "_" + originalName   │
│                                                      │
│ ✅ Testar com múltiplas threads (futuro)            │
│ ✅ Logging detalhado de uploads                     │
│                                                      │
│ Owner: Dev                                          │
│ Verificação: Thread safety test (futuro)            │
└─────────────────────────────────────────────────────┘
```

---

## 📊 Matriz de Risco

```
RISCO                    PROB    SEV    PRIO    STATUS    OWNER
──────────────────────────────────────────────────────────────────
FK não existe           ALTA    CRÍT   🔴     Pendente   Dev
S3 timeout              MÉDIA   ALTA   🔴     Pendente   Dev
Cache não invalida      ALTA    CRÍT   🔴     Pendente   Dev
ISBN duplicado          MÉDIA   ALTA   🟠     Pendente   Dev
Mapper incompleto       MÉDIA   MÉDIA  🟠     Pendente   Review
Paginação lenta         MÉDIA   ALTA   🟠     Pendente   DBA
TestContainers          BAIXA   MÉDIA  🟡     Observar   QA
Concorrência upload     BAIXA   MÉDIA  🟡     Observar   Dev
```

---

## ✅ Critérios de Aceitação

Feature PRONTA quando:

```
TESTES
[  ] Unit tests: 60+ casos
[  ] Coverage: > 70%
[  ] Mutation score: > 70%
[  ] ./mvnw clean verify: PASSA
[  ] Todos os 9 fluxos testados (E2E)
[  ] Erro cases: FK, ISBN, tamanho arquivo

CÓDIGO
[  ] Segue padrão: Domain → UC → Adapter
[  ] Sem duplicação (DRY)
[  ] Mappers bidirecionais
[  ] Cache invalidation presente (@CacheEvict)
[  ] FK validação em UseCase
[  ] Retry logic em S3
[  ] Índices criados

PERFORMANCE
[  ] Suite testes: < 5 segundos
[  ] GET lista: < 200ms (20 items)
[  ] Upload S3: < 30s com retry
[  ] Query larga (100k): < 200ms

DOCUMENTAÇÃO
[  ] Fluxos em critical-test-flows.md
[  ] API em Swagger
[  ] README.md atualizado
[  ] Riscos mitigados

QUALIDADE
[  ] Code review: 2 aprovadores
[  ] Sem warnings lint
[  ] Todos riscos mitigados
```

---

## 📋 Checklist de Implementação

```
DOMÍNIO
  [  ] Publication extends BaseDomain
  [  ] PublicationImage extends BaseDomain
  [  ] PublicationStatus enum
  [  ] Validações (@NotBlank, @NotNull, ISBN regex)
  [  ] Exceções customizadas

USE CASES
  [  ] CreatePublicationUseCase (com FK validation)
  [  ] UpdatePublicationUseCase (com @CacheEvict)
  [  ] RemovePublicationUseCase
  [  ] GetAllPublicationsUseCase (com paginação)
  [  ] GetPublicationByIdUseCase (com cache)
  [  ] UploadImageUseCase (com retry + timeout)

PERSISTÊNCIA
  [  ] PublicationEntity + PublicationData
  [  ] PublicationRepository
  [  ] PublicationImageEntity + PublicationImageData
  [  ] PublicationImageRepository
  [  ] Schema SQL com FK e índices
  [  ] Migrações DB

API
  [  ] PublicationController (CRUD)
  [  ] ImageController (upload)
  [  ] HTTP status codes corretos
  [  ] Error handling global

MAPPERS
  [  ] PublicationDtoMapper (toDomain + fromDomain)
  [  ] PublicationEntityMapper
  [  ] PublicationImageMappers
  [  ] Testes de mapper

TESTES
  [  ] Domain entity tests
  [  ] Use case tests (mock)
  [  ] Repository tests (mock JpaRepository)
  [  ] Controller E2E tests
  [  ] Mapper tests (bidirecional)
  [  ] Fluxos: CREATE, READ, UPDATE, DELETE, LIST
  [  ] Erros: FK, ISBN, tamanho arquivo

DOCUMENTAÇÃO
  [  ] Critical test flows atualizado
  [  ] README.md atualizado
  [  ] Swagger documentado
  [  ] FEATURE_TEMPLATE.md preenchido

QUALIDADE
  [  ] ./mvnw clean verify PASSA
  [  ] Coverage > 70%
  [  ] Code review APROVADO
  [  ] Performance validada
```

---

**Status**: Exemplo Prático  
**Criado**: 2026-02-07  
**Próximo Passo**: Preencher este template antes de começar feature real

