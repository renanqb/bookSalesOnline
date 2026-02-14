# Feature Template - BookSalesOnline

**Versão**: 1.0  
**Data de Criação**: 2026-02-07  
**Status**: Guia Padrão

---

> Use este template para documentar qualquer nova feature no projeto BookSalesOnline.
> Preencha todas as seções antes de iniciar implementação.

---

## Feature: [Nome Descritivo da Feature]

### Exemplo: Feature: CRUD de Publicações (Livros)

---

## 📋 Objetivo

**O que esta feature faz?**

Descreva em 1-3 linhas clara e concisa o propósito da feature.

```
Exemplo:
Permitir que usuários criem, leiam, atualizem e removam publicações (livros),
incluindo associação com editoras, idiomas e upload de imagens de capa.
```

**Valor de negócio**:
- ✅ Ponto 1 (e.g., "Usuários podem gerenciar catálogo de livros")
- ✅ Ponto 2
- ✅ Ponto 3

---

## 🎯 Escopo Incluído

**Exatamente o que será implementado:**

### 1. Domínio
```
- [ ] Entidade Publication
- [ ] Entidade PublicationImage
- [ ] Validações (@NotBlank, @NotNull)
- [ ] Enums (e.g., PublicationStatus, Genre)
```

### 2. Use Cases
```
- [ ] CreatePublicationUseCase
- [ ] GetPublicationByIdUseCase
- [ ] UpdatePublicationUseCase
- [ ] RemovePublicationUseCase
- [ ] GetAllPublicationsUseCase
- [ ] UploadPublicationImageUseCase
```

### 3. Persistência
```
- [ ] PublicationEntity (@Entity)
- [ ] PublicationData (extends JpaRepository)
- [ ] PublicationRepository (implements DataQuery, DataCommand)
- [ ] PublicationImageEntity
- [ ] PublicationImageData
- [ ] PublicationImageRepository
- [ ] Migrações SQL (schema)
```

### 4. API (HTTP)
```
- [ ] PublicationController
  - [ ] POST /publications (CREATE)
  - [ ] GET /publications (LIST com paginação)
  - [ ] GET /publications/{id} (READ by ID)
  - [ ] PUT /publications/{id} (UPDATE)
  - [ ] DELETE /publications/{id} (DELETE)
- [ ] ImageController
  - [ ] POST /publications/{id}/images (UPLOAD)
  - [ ] GET /publications/{id}/images (LIST imagens)
```

### 5. DTOs e Mappers
```
- [ ] PublicationDto
- [ ] PublicationImageDto
- [ ] PublicationDtoMapper
- [ ] PublicationEntityMapper
- [ ] PublicationImageDtoMapper
- [ ] PublicationImageEntityMapper
```

### 6. Testes
```
- [ ] Unit Tests (50+ casos)
  - [ ] Domain entity tests
  - [ ] Use case tests (com mocks)
  - [ ] Mapper tests
- [ ] Integration Tests
  - [ ] Repository tests (mock JpaRepository)
- [ ] E2E Tests
  - [ ] PublicationControllerFunctionalTest
  - [ ] ImageControllerFunctionalTest
```

### 7. Documentação
```
- [ ] README.md atualizado
- [ ] API endpoints documentados (Swagger)
- [ ] Testes documentados em critical-test-flows.md
```

---

## 🚫 Fora de Escopo

**Explicitamente NÃO fazer nesta feature:**

```
❌ NÃO incluir:
  - [ ] Busca avançada por título/autor (v2.0)
  - [ ] Recomendações baseadas em histórico (ML - futuro)
  - [ ] Integração com API externa de dados ISBN (v2.0)
  - [ ] Suporte a múltiplos formatos (PDF, EPUB - v2.0)
  - [ ] Controle de permissões por usuário (v2.0)
  - [ ] Integração com plataforma de pagamento (fora do escopo atual)
  - [ ] Índices de busca (Elasticsearch) (v3.0)
  - [ ] Sincronização com estoque (sistema separado)
  - [ ] Relatórios de vendas (módulo BI)
```

**Justificativa de exclusão:**

Estas funcionalidades requerem:
- Design adicional (quanto tempo?)
- Dependências externas (API, BD, ferramentas)
- Testes mais complexos
- Maior complexidade técnica

Melhor implementar em sprints subsequentes com story separada.

---

## 🔧 Dependências Técnicas

### 1. Dependências de Código (Módulos)

**Será necessário usar:**

```
✅ Domain Layer:
   - Estender BaseDomain
   - Usar validações (@NotBlank, @NotNull)
   - Criar exceptions de domínio (PublicationException)

✅ Application Layer:
   - Criar use cases estendendo padrão (CreateEntityUseCase)
   - Usar RepositoryMediator (até refatorar para DI)
   - Orquestrar S3StorageService para imagens

✅ Adapter Layer:
   - Implementar Repository (DataQuery, DataCommand)
   - Criar DTOs com @JsonProperty
   - Mappers (DtoMapper, EntityMapper)

✅ Infrastructure:
   - Usar TestContainers (PostgreSQL)
   - Usar Redis para cache opcional
   - Spring Security (se necessário auth)
```

### 2. Dependências de Biblioteca

**Adicionar ao pom.xml:**

```xml
<!-- Já existem -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Possível novo -->
<!-- Se precisar de validação mais avançada -->
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
</dependency>
```

### 3. Dependências de Banco de Dados

**Schema SQL necessário:**

```sql
CREATE TABLE publication (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    -- adicionar campos específicos
);

CREATE TABLE publication_image (
    id SERIAL PRIMARY KEY,
    publication_id INTEGER NOT NULL,
    url VARCHAR(500),
    FOREIGN KEY (publication_id) REFERENCES publication(id)
);
```

### 4. Dependências de Configuração

**application.properties necessário:**

```properties
# Paginação
spring.jpa.properties.hibernate.jdbc.fetch_size=50

# S3 para imagens (já configurado)
aws.s3.bucket=booksalesonline-publicationImages
```

### 5. Dependências Externas

```
✅ AWS S3 (LocalStack em dev) - já temos
✅ PostgreSQL - já temos via TestContainers
✅ Redis (opcional para cache) - já temos
❌ Elasticsearch - NÃO será usado nesta feature
❌ API externa - NÃO será usada nesta feature
```

---

## ⚠️ Riscos

### 🔴 Riscos Críticos (Stop-the-show)

```
RISCO 1: Integridade Referencial
┌────────────────────────────────────────────────┐
│ Se Publisher for deletado, Publication orfã    │
│                                                 │
│ Impacto: Constraint violation, erro em DELETE  │
│ Probabilidade: ALTA (em testes E2E)            │
│ Severidade: CRÍTICA (quebra CRUD)              │
│                                                 │
│ Mitigação:                                      │
│ ✅ Adicionar ON DELETE CASCADE na FK           │
│ ✅ Testar Fluxo 4 (DELETE Publisher) com Pubs  │
│ ✅ Documentar comportamento esperado           │
└────────────────────────────────────────────────┘

RISCO 2: S3 Upload Timeout
┌────────────────────────────────────────────────┐
│ Upload de imagem grande falha ou timeout       │
│                                                 │
│ Impacto: Usuário perde imagem, transação      │
│ Probabilidade: MÉDIA (depende de tamanho)      │
│ Severidade: ALTA (feature crítica de upload)   │
│                                                 │
│ Mitigação:                                      │
│ ✅ Validar tamanho max (e.g., 5MB)             │
│ ✅ Adicionar retry logic com circuit breaker   │
│ ✅ Timeout: 30s (configurável)                 │
│ ✅ Testar com LocalStack (não AWS real)        │
└────────────────────────────────────────────────┘

RISCO 3: Cache Invalidation
┌────────────────────────────────────────────────┐
│ UPDATE Publication sem invalidar cache Redis   │
│                                                 │
│ Impacto: Usuários veem dados antigos           │
│ Probabilidade: ALTA (se esquecer @CacheEvict) │
│ Severidade: ALTA (data inconsistency)          │
│                                                 │
│ Mitigação:                                      │
│ ✅ Adicionar @CacheEvict em UPDATE/DELETE      │
│ ✅ Testar Fluxo 8 (CACHE invalidation)         │
│ ✅ Code review obrigatório                     │
└────────────────────────────────────────────────┘
```

### 🟠 Riscos Altos (Afetam cronograma)

```
RISCO 4: Paginação em Listagem Grande
┌────────────────────────────────────────────────┐
│ SELECT * FROM publication sem índices = lento │
│                                                 │
│ Impacto: Query > 5 segundos em 100k registros  │
│ Probabilidade: MÉDIA (crescimento futuro)      │
│ Severidade: ALTA (performance)                 │
│                                                 │
│ Mitigação:                                      │
│ ✅ Criar índices: CREATE INDEX pub_name        │
│ ✅ Testar com dados realistas (load test)      │
│ ✅ Paginação padrão: 20 items/página           │
│ ✅ Benchmark antes/depois                      │
└────────────────────────────────────────────────┘

RISCO 5: Mapper Incompleteto
┌────────────────────────────────────────────────┐
│ Novo atributo em Publication não mapeado       │
│                                                 │
│ Impacto: DTO.field == null quando não deveria  │
│ Probabilidade: MÉDIA (erro humano)             │
│ Severidade: MÉDIA (bug em resposta API)        │
│                                                 │
│ Mitigação:                                      │
│ ✅ Usar recursive comparison em testes         │
│ ✅ Checklist: "Atualizar todos 3 mappers"      │
│ ✅ Code review verifica mappers                │
│ ✅ Testes de mapper bidirecional               │
└────────────────────────────────────────────────┘

RISCO 6: Validação de FK Faltando
┌────────────────────────────────────────────────┐
│ Criar Publication com Publisher.id inexistente │
│                                                 │
│ Impacto: Constraint violation em BD, erro 500  │
│ Probabilidade: ALTA (sem validação no UseCase)│
│ Severidade: ALTA (dados inválidos)             │
│                                                 │
│ Mitigação:                                      │
│ ✅ UseCase valida Publisher.getById() != null  │
│ ✅ Testar Fluxo 6 (validação FK)               │
│ ✅ Retornar 400 Bad Request, não 500           │
│ ✅ Mensagem de erro clara                      │
└────────────────────────────────────────────────┘
```

### 🟡 Riscos Médios (Observar)

```
RISCO 7: TestContainers Lento
┌────────────────────────────────────────────────┐
│ Spin-up de PostgreSQL em cada test class       │
│                                                 │
│ Impacto: Suite de testes leva > 10 segundos    │
│ Probabilidade: BAIXA (já otimizado com @ClassRule)
│ Severidade: MÉDIA (developer experience)       │
│                                                 │
│ Mitigação:                                      │
│ ✅ Usar @ClassRule (shared container)          │
│ ✅ Não criar novo container por teste          │
│ ✅ Target: < 5 segundos suite total            │
└────────────────────────────────────────────────┘

RISCO 8: Concorrência em Upload
┌────────────────────────────────────────────────┐
│ 2 uploads simultâneos do mesmo arquivo         │
│                                                 │
│ Impacto: Race condition em S3, nomes duplicados│
│ Probabilidade: BAIXA (não é caso comum)        │
│ Severidade: MÉDIA (edge case)                  │
│                                                 │
│ Mitigação:                                      │
│ ✅ Usar UUID em nome de arquivo                │
│ ✅ Testar com múltiplas threads (futuro)       │
│ ✅ Logging de uploads                          │
└────────────────────────────────────────────────┘

RISCO 9: Refatoração de Mediators
┌────────────────────────────────────────────────┐
│ Se refatorar Mediators durante feature         │
│                                                 │
│ Impacto: Conflito em merge, testes quebram     │
│ Probabilidade: BAIXA (roadmap separado)        │
│ Severidade: MÉDIA (cronograma impactado)       │
│                                                 │
│ Mitigação:                                      │
│ ✅ Feature branch isolada                      │
│ ✅ Merge rapidamente                           │
│ ✅ Não fazer refatoração em paralelo           │
└────────────────────────────────────────────────┘
```

---

## 📊 Matriz de Decisão de Risco

```
RISCO              PROBABILIDADE  SEVERIDADE  PRIORIDADE  AÇÃO
────────────────────────────────────────────────────────────────
FK Constraint      ALTA           CRÍTICA     🔴 IMEDIATO  Validar no UC
Cache Stale        ALTA           ALTA        🔴 IMEDIATO  @CacheEvict
S3 Timeout         MÉDIA          ALTA        🟠 URGENTE   Retry/timeout
Mapper Incompleto  MÉDIA          MÉDIA       🟠 URGENTE   Checklist
Paginação Lenta    MÉDIA          ALTA        🟠 URGENTE   Índices BD
Concorrência       BAIXA          MÉDIA       🟡 MONITORAR UUID nome
TestContainers     BAIXA          MÉDIA       🟡 MONITORAR @ClassRule
Refatoração        BAIXA          MÉDIA       🟡 MONITORAR Branch iso.
```

---

## 🛡️ Estratégia de Mitigação (Por Prioridade)

### Fase 1: Antes de Implementar
```
[ ] Validar FK no UseCase (não apenas BD)
[ ] Planejar estratégia de cache invalidation
[ ] Definir tamanho máximo de upload (5MB)
[ ] Criar índices de performance no schema
```

### Fase 2: Durante Implementação
```
[ ] Adicionar @CacheEvict em UPDATE/DELETE
[ ] Implementar retry logic para S3
[ ] Criar checklist de "novos atributos = update mappers"
[ ] Validar bidirecional em testes de mapper
```

### Fase 3: Testes
```
[ ] Testar FK validation (happy path + error)
[ ] Testar cache invalidation (UPDATE/DELETE)
[ ] Testar upload timeout (mock S3 timeout)
[ ] Testar paginação com dados realistas
[ ] Load test: 100k registros
```

### Fase 4: Code Review
```
[ ] Validar presença de @CacheEvict
[ ] Verificar validação FK em UseCase
[ ] Revisar exception types
[ ] Confirmar testes E2E + Unit
```

---

## 📈 Critérios de Aceitação

Feature considerada **PRONTA** quando:

```
✅ Testes
  - [ ] Unit tests: 50+ casos (coverage > 70%)
  - [ ] Integration tests: Repository mocked
  - [ ] E2E tests: PublicationControllerFunctionalTest
  - [ ] Cobertura de mutation: > 70%
  - [ ] Todos testes passam: ./mvnw clean verify

✅ Código
  - [ ] Segue padrão arquitetural (Domain → UC → Adapter)
  - [ ] Sem código duplicado (DRY)
  - [ ] Mappers bidirecionais funcionam
  - [ ] FK validação implementada
  - [ ] Cache invalidation presente

✅ Documentação
  - [ ] Fluxos documentados em critical-test-flows.md
  - [ ] API endpoints documentados (Swagger)
  - [ ] README atualizado
  - [ ] Riscos mitigados

✅ Performance
  - [ ] Suite de testes: < 5 segundos
  - [ ] GET listagem: < 200ms (20 items)
  - [ ] S3 upload: < 30s com retry
  - [ ] Índices criados e verificados

✅ Qualidade
  - [ ] Code review aprovado (2+ reviewers)
  - [ ] Lint/checkstyle: zero warnings
  - [ ] Sem TODO/FIXME pendentes
  - [ ] Todos os pontos sensíveis mitigados
```

---

## 📋 Checklist de Implementação

Copie e use este checklist:

```
DOMÍNIO
  - [ ] Publication extends BaseDomain
  - [ ] PublicationImage extends BaseDomain
  - [ ] Validações (@NotBlank, @NotNull)
  - [ ] Exceções (PublicationException, etc)

USE CASES
  - [ ] CreatePublicationUseCase (com FK validation)
  - [ ] GetPublicationByIdUseCase
  - [ ] UpdatePublicationUseCase (com @CacheEvict)
  - [ ] RemovePublicationUseCase
  - [ ] GetAllPublicationsUseCase
  - [ ] UploadPublicationImageUseCase (com S3)

PERSISTÊNCIA
  - [ ] PublicationEntity (com schema SQL)
  - [ ] PublicationData (JpaRepository)
  - [ ] PublicationRepository (DataQuery, DataCommand)
  - [ ] PublicationImageEntity
  - [ ] PublicationImageData
  - [ ] PublicationImageRepository
  - [ ] Índices criados no schema

API
  - [ ] PublicationController (CRUD endpoints)
  - [ ] ImageController (upload endpoints)
  - [ ] HTTP status codes corretos
  - [ ] Error handling global

MAPPERS
  - [ ] PublicationDtoMapper (toDomain + fromDomain)
  - [ ] PublicationEntityMapper (toDomain + fromEntity)
  - [ ] PublicationImageDtoMapper
  - [ ] PublicationImageEntityMapper
  - [ ] Testes de mapper bidirecional

TESTES
  - [ ] Domain entity tests
  - [ ] Use case tests (mock)
  - [ ] Repository tests (mock JpaRepository)
  - [ ] Controller E2E tests (TestContainers)
  - [ ] Mapper tests (bidirecional)
  - [ ] Fluxos: CREATE, READ, UPDATE, DELETE, LIST
  - [ ] Erro: FK não existe, ID inexistente
  - [ ] Cache: @CacheEvict funcionando
  - [ ] S3: upload com timeout

DOCUMENTAÇÃO
  - [ ] Fluxos adicionados em critical-test-flows.md
  - [ ] README.md atualizado
  - [ ] Swagger/OpenAPI documentado
  - [ ] Riscos mitigados e documentados

QUALIDADE
  - [ ] ./mvnw clean verify passa
  - [ ] Coverage > 70%
  - [ ] Mutation > 70%
  - [ ] Code review aprovado
  - [ ] Performance validada
```

---

## 📞 Escalação de Riscos

Se durante implementação descobrir:

```
🔴 CRÍTICO (bloqueador):
  ├─ FK constraint falha
  ├─ S3 não responde
  ├─ Cache nunca invalida
  └─ → Reportar e parar
      Contatar: Tech Lead, Architect

🟠 ALTO (importante):
  ├─ Query muito lenta
  ├─ Mapper incompleto
  ├─ Validação faltando
  └─ → Reportar antes de merge
      Contatar: Senior Developer

🟡 MÉDIO (observar):
  ├─ Testes lentos
  ├─ Edge cases não cobertos
  └─ → Documentar em PR
      Contatar: Code Reviewer
```

---

## 📌 Notas Importantes

1. **Sempre preencher este template ANTES de codificar**
2. **Revisar riscos com time (code review)**
3. **Atualizar documentação conforme avança**
4. **Usar como checklist durante feature**
5. **Ao finalizar, adicionar fluxos em critical-test-flows.md**

---

## 🔗 Referências

- [context-core.md](context-core.md) - Arquitetura
- [testing-strategy.md](testing-strategy.md) - Estratégia de testes
- [context-map-testing.md](context-map-testing.md) - 10 módulos
- [critical-test-flows.md](critical-test-flows.md) - Fluxos críticos
- [critical-flows-summary.md](critical-flows-summary.md) - Quick ref

---

**Template Version**: 1.0  
**Last Updated**: 2026-02-07  
**Maintained by**: Architecture Team

