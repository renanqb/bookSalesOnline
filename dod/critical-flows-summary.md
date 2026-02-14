# Sumário Visual: Fluxos Críticos de Teste

**BookSalesOnline - Quick Reference**

---

## 📊 12 Fluxos Críticos em 1 Página

```
┌─────────────────────────────────────────────────────────────────┐
│  FLUXO                   TIPO   TESTE CLASSE              STATUS  │
├─────────────────────────────────────────────────────────────────┤
│ 1. CREATE Country        E2E    CountryControllerFT      ✅      │
│ 2. READ Country (by ID)  E2E    CountryControllerFT      ✅      │
│ 3. UPDATE Country        E2E    CountryControllerFT      ✅      │
│ 4. DELETE Country        E2E    CountryControllerFT      ✅      │
│ 5. LIST All Countries    E2E    CountryControllerFT      ✅      │
│ 6. CREATE Publisher      E2E    PublisherControllerFT    ✅      │
│ 7. GET Publishers/Country E2E   CountryControllerFT      ✅      │
│ 8. CACHE Read            E2E    CountryControllerFT      ✅      │
│ 9. ERROR (ID not found)  E2E    CountryControllerFT      ✅      │
│ 10. Create Entity UC     UNIT   CreateEntityUseCaseTest  ✅      │
│ 11. Remove Entity UC     UNIT   RemoveEntityUseCaseTest  ✅      │
│ 12. DTO ↔ Domain Map     UNIT   CountryDtoMapperTest    ✅      │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🎯 Fluxo Rápido de Entendimento

### FLUXO 1: CREATE Country
```
POST /countries {name, nationality}
↓
Controller → UseCase → Repository → BD
↓
Status 201 + ID gerado
```
**Sensível a**: Mapper, HTTP status, FK constraints

### FLUXO 2: READ Country
```
GET /countries/{id}
↓
Controller → UseCase → Repository → Cache/BD
↓
Status 200 + dados completos
```
**Sensível a**: Cache, mapper, índices BD

### FLUXO 3: UPDATE Country
```
PUT /countries/{id} {name, nationality}
↓
Controller → UseCase → Repository → BD
↓
Status 200 + dados atualizados + Cache EVICT
```
**Sensível a**: Cache invalidation, constraints

### FLUXO 4: DELETE Country
```
DELETE /countries/{id}
↓
Controller → UseCase → Repository → BD
↓
Status 204 + sem conteúdo
```
**Sensível a**: FK constraints, cascading deletes

### FLUXO 5: LIST Countries
```
GET /countries
↓
Controller → UseCase → Repository → BD
↓
Status 200 + array[3] (argentina, brazil, chile)
```
**Sensível a**: Paginação, índices, ordenação

### FLUXO 6: CREATE Publisher (com validação)
```
POST /publishers {name, history, country{id}}
↓
UseCase: Valida se Country.id existe
↓
Repository → BD
↓
Status 201 + ID gerado + FK set
```
**Sensível a**: Validação FK, Exception type

### FLUXO 7: GET Publishers by Country
```
GET /countries/{countryId}/publishers
↓
Repository: SELECT * WHERE country_id = ?
↓
Status 200 + array[2] (pub1, pub2 do Brasil)
```
**Sensível a**: Query customizado, joins, eager loading

### FLUXO 8: CACHE Read
```
GET /countries/{id}  (1ª vez) → BD (50ms)
GET /countries/{id}  (2ª vez) → Cache (2ms)
↓
Tempo: 25x mais rápido
```
**Sensível a**: @CacheEvict em UPDATE/DELETE

### FLUXO 9: ERROR - Delete Inexistente
```
DELETE /countries/99  (não existe)
↓
UseCase: getById(99) retorna null
↓
RemoveException lançada
↓
Status 406 NOT_ACCEPTABLE
```
**Sensível a**: Validação null, exception handling

### FLUXO 10: Create Entity UC (Unit)
```
UseCase.execute(Country.class, domain)
↓
Mediator.getCommand() → mock
↓
Command.save() → mock retorna domain.id=1
↓
assertThat(resultado.getId()) == 1
```
**Sensível a**: Mediator refactoring, mock setup

### FLUXO 11: Remove Entity UC (Unit)
```
UseCase.execute(Country.class, 1)
↓
Happy: Query.getById(1) → retorna domain
       Command.remove() → executa
       assertDoesNotThrow()

Error: Query.getById(99) → null
       assertThrows(RemoveException)
```
**Sensível a**: Validação null, exception type

### FLUXO 12: DTO ↔ Domain Map
```
CountryDto("Brazil", "Brazilian")
  ↓
CountryDtoMapper.toDomain()
  ↓
Country(0, "Brazil", "Brazilian")
  ↓
assertThat(actual).usingRecursiveComparison().isEqualTo(expected)
```
**Sensível a**: Novos atributos, renomeação, tipo mudança

---

## ⚠️ Top 10 Pontos Sensíveis

```
1. DTO Mapper       → Muda atributo? Mapper quebra
2. Cache Eviction   → Esqueceu @CacheEvict? Data stale
3. HTTP Status      → Mudou 201 para 200? Teste falha
4. FK Validation    → Removeu validação? Constraint error
5. Exception Type   → Mudou ValidationException? Tipo mismatch
6. Repository Query → Mudou WHERE clause? Resultado errado
7. Entity Mapper    → Mapeamento incompleto? Dados nulos
8. Null Handling    → Sem null check? NullPointerException
9. Mediator        → Remover use case do registry? ClassCastException
10. Redis TTL      → Mudou 60s para 3600s? Cache longo demais
```

---

## 🔗 Dependências Entre Fluxos

```
SETUP (@BeforeAll)
    ├─ argentina, brazil, chile (países)
    └─ publisher1, publisher2 (editoras Brasil)

    ↓

FLUXO 1: CREATE Country (USA)
    ↓
FLUXO 2: READ Country (USA)
    ↓
FLUXO 3: UPDATE Country (USA → USA updated)
    ↓
FLUXO 8: CACHE READ (USA do cache 2ª vez)
    ↓
FLUXO 4: DELETE Country (USA)
    ↓
FLUXO 5: LIST Countries (3 apenas, sem USA)

PARALELO:
FLUXO 6: CREATE Publisher
    ↓
FLUXO 7: GET Publishers/Country

UNITÁRIOS (sem dependência):
FLUXO 10: Create UC (mock)
FLUXO 11: Remove UC (mock)
FLUXO 12: DTO Mapping

ERRO:
FLUXO 9: Delete Inexistente (ID=99)
```

---

## 📋 Checklist Rápido

### Ao Adicionar Nova Feature:
```
[ ] Escrever E2E test primeiro (RED)
[ ] Implementar (GREEN)
[ ] Rodar: ./mvnw test
[ ] Cobertura > 70%?
[ ] Testar happy path + error case
[ ] Verificar cache invalidation
[ ] Validar FK constraints
[ ] Documentar sensibilidades
```

### Ao Modificar Entity/DTO:
```
[ ] Atualizar Mapper
[ ] Atualizar testes (Fluxo 12)
[ ] Rodar Fluxo 1-9 (E2E)
[ ] Cobertura OK?
```

### Ao Mudar Repository:
```
[ ] Testar Fluxo 2 (READ)
[ ] Testar Fluxo 5 (LIST)
[ ] Testar performance
[ ] Índices criados?
```

### Ao Modificar Cache:
```
[ ] Testar Fluxo 8 (CACHE)
[ ] Testar Fluxo 3 (UPDATE + evict)
[ ] Testar Fluxo 4 (DELETE + evict)
[ ] Medir tempo resposta
```

---

## 📊 Matriz Rápida de Impacto

```
MUDANÇA              FLUXOS AFETADOS            CRITICIDADE
────────────────────────────────────────────────────────────
Mapper              1,2,3,6,12                 🔴 CRÍTICO
Cache               2,3,4,8                    🔴 CRÍTICO
HTTP Status         1,3,4,6,9                  🟠 ALTO
FK Validation       6,7                        🟠 ALTO
Exception Type      9,11                       🟠 ALTO
Repository Query    2,5,7                      🟠 ALTO
Entity Mapper       1,2,3,5                    🟠 ALTO
Null Handling       2,11                       🟡 MÉDIO
UseCase Logic       1,3,10,11                  🟡 MÉDIO
Mediator            1,3,10,11                  🟡 MÉDIO
```

---

## 🎬 Execução dos Testes

```bash
# Rodar todos
./mvnw test

# Rodar específico (e.g., Country)
./mvnw test -Dtest=CountryControllerFunctionalTest

# Com cobertura
./mvnw clean verify

# Com mutation
./mvnw pitest:mutationCoverage

# Tempo esperado
Total suite: < 5 segundos
Unitários: < 1 segundo
E2E: ~3-4 segundos (TestContainers)
```

---

## 📍 Onde Estão os Testes

```
src/test/java/com/renan/booksalesonline/tests/

E2E Tests:
├── adapters/controllers/v1/
│   ├── CountryControllerFunctionalTest.java
│   ├── PublisherControllerFunctionalTest.java
│   └── ...

Unit Tests:
├── application/usecases/
│   ├── CreateEntityUseCaseImplTest.java
│   ├── RemoveEntityUseCaseImplTest.java
│   └── ...
├── adapters/repositories/
│   ├── CountryRepositoryTest.java
│   └── ...
└── adapters/controllers/v1/mappers/
    ├── CountryDtoMapperTest.java
    └── ...
```

---

## 🚀 Próximos Fluxos para Testar

```
[ ] Publication (livros) CRUD
[ ] Image upload para S3
[ ] Advanced search/filtering
[ ] Soft delete
[ ] Batch operations
[ ] Concurrent updates (race conditions)
[ ] Transactions complexas
```

---

**Referência Rápida**: Critical-test-flows.md (detalhado)  
**Atualizado**: 2026-02-07  
**Mantido por**: QA Team

