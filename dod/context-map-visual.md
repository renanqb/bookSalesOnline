# Diagrama Visual do Mapa de Contexto

## Fluxo de Teste por Módulo

```
┌────────────────────────────────────────────────────────────────────┐
│                    CLIENT HTTP REQUEST                             │
│                  (External to system)                              │
└────────────────────────┬─────────────────────────────────────────┘
                         │
        ┌────────────────▼────────────────┐
        │  LAYER: ADAPTERS                │
        │                                 │
        │  MODULE 4: CONTROLLERS          │
        │  CountryController              │
        │                                 │
        │  🧪 TEST TYPE: E2E              │
        │  🔧 TOOL: @SpringBootTest       │
        │  ⚡ SPEED: 100-500ms            │
        │  🎯 EXAMPLE: Controller*Test.java
        │                                 │
        │  ✅ TEST: HTTP via Rest Assured │
        │  ✅ MOCK: None (real)           │
        │  ✅ BD: TestContainers (real)   │
        │  ❌ MOCK mediator: No           │
        └────────────────┬────────────────┘
                         │
        ┌────────────────▼────────────────┐
        │  LAYER: ADAPTERS                │
        │                                 │
        │  MODULE 7: MAPPERS              │
        │  CountryDtoMapper               │
        │                                 │
        │  🧪 TEST TYPE: UNIT             │
        │  🔧 TOOL: JUnit + AssertJ       │
        │  ⚡ SPEED: < 1ms                │
        │  🎯 EXAMPLE: *DtoMapper*Test.java
        │                                 │
        │  ✅ TEST: Bidirecional (DTO↔Dom)
        │  ✅ MOCK: None                  │
        │  ✅ BD: None                    │
        │  ✅ SPRING: No                  │
        └────────────────┬────────────────┘
                         │
        ┌────────────────▼────────────────┐
        │  LAYER: APPLICATION             │
        │                                 │
        │  MODULE 2: USE CASES            │
        │  CreateEntityUseCaseImpl         │
        │                                 │
        │  🧪 TEST TYPE: UNIT             │
        │  🔧 TOOL: Mockito               │
        │  ⚡ SPEED: 1-10ms               │
        │  🎯 EXAMPLE: *UseCase*Test.java │
        │                                 │
        │  ✅ TEST: Lógica orquestração   │
        │  ✅ MOCK: RepositoryMediator    │
        │  ✅ MOCK: DataCommand/Query     │
        │  ❌ BD: None                    │
        │  ❌ @SpringBootTest: No         │
        └────────────────┬────────────────┘
                         │
        ┌────────────────▼────────────────┐
        │  LAYER: ADAPTERS                │
        │                                 │
        │  MODULE 3: MEDIATORS (⚠️ APOD)  │
        │  RepositoryMediatorImpl          │
        │  UseCaseMediatorImpl             │
        │                                 │
        │  🧪 TEST TYPE: UNIT             │
        │  🔧 TOOL: Mockito               │
        │  ⚡ SPEED: 1-10ms               │
        │  🎯 EXAMPLE: *Mediator*Test.java
        │                                 │
        │  ✅ TEST: Registry lookup       │
        │  ✅ MOCK: Todos use cases/repos │
        │  ❌ EVITAR: Usar em produção    │
        │  ❓ FUTURO: Remover             │
        └────────────────┬────────────────┘
                         │
        ┌────────────────▼────────────────┐
        │  LAYER: ADAPTERS                │
        │                                 │
        │  MODULE 5: REPOSITORIES         │
        │  CountryRepository              │
        │                                 │
        │  🧪 TEST TYPE: UNIT (mock)      │
        │  🔧 TOOL: Mockito               │
        │  ⚡ SPEED: 1-10ms               │
        │  🎯 EXAMPLE: Repository*Test.java
        │                                 │
        │  ✅ TEST: Mapping Dom↔Entity    │
        │  ✅ MOCK: JpaRepository (*Data) │
        │  ✅ TEST: Paginação             │
        │  ❌ BD real: No                 │
        │  ❌ @SpringBootTest: No         │
        └────────────────┬────────────────┘
                         │
        ┌────────────────▼────────────────┐
        │  LAYER: ADAPTERS                │
        │                                 │
        │  MODULE 6: DATA (JpaRepository) │
        │  CountryData extends JpaRepo    │
        │                                 │
        │  🧪 TEST TYPE: E2E ou UNIT      │
        │  🔧 TOOL: @SpringBootTest       │
        │  ⚡ SPEED: Var. (BD real)       │
        │  🎯 EXAMPLE: Via Repository     │
        │                                 │
        │  ❌ NÃO teste direto            │
        │  ✅ TESTE via Repository        │
        │  ✅ TESTE via E2E (TestContainers)
        └────────────────┬────────────────┘
                         │
        ┌────────────────▼────────────────┐
        │  LAYER: ADAPTERS                │
        │                                 │
        │  MODULE 8: ENTITIES (JPA)       │
        │  CountryEntity                  │
        │                                 │
        │  🧪 TEST TYPE: UNIT             │
        │  🔧 TOOL: JUnit + AssertJ       │
        │  ⚡ SPEED: < 1ms                │
        │  🎯 EXAMPLE: Entity*Test.java   │
        │                                 │
        │  ✅ TEST: Instanciação          │
        │  ✅ TEST: Atributos             │
        │  ❌ MOCK: None                  │
        │  ❌ @SpringBootTest: No         │
        └────────────────┬────────────────┘
                         │
        ┌────────────────▼────────────────┐
        │  LAYER: DOMAIN                  │
        │                                 │
        │  MODULE 1: ENTITIES (Domain)    │
        │  Country, Publisher, etc        │
        │                                 │
        │  🧪 TEST TYPE: UNIT             │
        │  🔧 TOOL: JUnit + AssertJ       │
        │  ⚡ SPEED: < 1ms                │
        │  🎯 EXAMPLE: *Domain*Test.java  │
        │                                 │
        │  ✅ TEST: Instanciação          │
        │  ✅ TEST: Validações (@NotBlank)
        │  ✅ TEST: Comportamento negócio │
        │  ❌ MOCK: Nenhum                │
        │  ❌ @SpringBootTest: No         │
        │  ❌ BD: None                    │
        │  ❌ Spring: None                │
        └────────────────┬────────────────┘
                         │
        ┌────────────────▼────────────────┐
        │  DATABASE (PostgreSQL)          │
        │  [Testado via TestContainers]   │
        └────────────────────────────────┘
```

---

## Matriz de Dependências Visita

```
┌──────────────┬──────────────┬──────────────┬──────────────┬──────────────┐
│   Domain     │ Application  │   Adapters   │   Adapters   │Infrastructure
│              │              │              │              │
│  1. Entities │ 2. Use Cases │ 4. Ctrl      │ 5. Repos     │ 10. Containers
│              │              │ 7. Mappers   │ 6. Data      │
│              │              │ 8. Entities  │ 9. Storage   │
│              │              │ 3. Mediator  │              │
└──────────────┴──────────────┴──────────────┴──────────────┴──────────────┘

DEPENDÊNCIAS:

Ctrl → Use Cases       (Use cases fazem orquestração)
       Mappers         (Transforma DTO → Domain)

Use Cases → Domain     (Trabalha com entidades)
            Mediators  (Resolve repositories - antipadrão)
            Ports      (Interfaces, não concreto)

Mediators → Use Cases  (Service Locator)
            Repos      (Service Locator)

Repos → Data           (JpaRepository)
        Mappers        (Entity ↔ Domain)
        Domain         (Retorna entidades)

Data → JPA/Hibernate   (Não conhece domain)

Mappers → Domain       (Puro, sem dependências)
          DTO/Entity   (Puro)

Entities (Domain) → Base (Herança)
Entities (JPA) → Hibernate (Anotações)

Storage → AWS SDK      (S3)
          Domain       (PublicationImage)
```

---

## Matriz de Teste Simplificada

```
┌─────────────────┬──────────────┬──────────────┬──────────────┐
│   MÓDULO        │   UNIT TEST  │  INTEGRATION │   E2E TEST   │
├─────────────────┼──────────────┼──────────────┼──────────────┤
│ 1. Domain       │  ✅ YES      │  ❌ NO       │  ❌ NO       │
│ 2. UseCase      │  ✅ YES      │  ❌ NO       │  ✅ YES      │
│ 3. Mediator     │  ✅ YES      │  ❌ NO       │  ❌ NO       │
│ 4. Controller   │  ❌ NO       │  ❌ NO       │  ✅ YES      │
│ 5. Repository   │  ✅ YES      │  ✅ YES      │  ✅ YES      │
│ 6. Data         │  ❌ NO       │  ❌ NO       │  ✅ YES      │
│ 7. Mapper       │  ✅ YES      │  ❌ NO       │  ✅ YES      │
│ 8. Entity       │  ✅ YES      │  ❌ NO       │  ✅ YES      │
│ 9. Storage      │  ✅ YES      │  ❌ NO       │  ✅ YES      │
│ 10. Container   │  ❌ NO       │  ❌ NO       │  ✅ YES      │
└─────────────────┴──────────────┴──────────────┴──────────────┘

Legend:
✅ YES    = Recomendado e implementado
⚠️  MAYBE = Se necessário
❌ NO     = Evitar ou não aplicável
```

---

## Pirâmide de Testes

```
                        △
                       /|\
                      / | \
                     /  |  \  ~ 10% E2E Tests
                    /   |   \ (Controller + Containers)
                   /    |    \
                  /     |     \
                 /      |      \
                /____ __△____ __\
               /      /  |  \      \
              /      /   |   \      \
             /      /    |    \      \
            /      /     |     \      \
           /______/      |      \______\
          /              |              \
         /    ~ 20% Integration Tests    \
        /    (Repository com Mock JpaRepo)\
       /__________________________________ \
      /                                    \
     /        ~ 70% Unit Tests              \
    /        (Domain, UseCase, Mapper,       \
   /          Entity, Storage - SEM Spring)   \
  /__________________________________________\

Velocidade:    <1ms          1-10ms           100-500ms
Isolamento:    Total         Mock Partial     Real Infrastructure
Framework:     JUnit+Assert  JUnit+Mockito    Spring+TestContainers
```

---

## Fluxo de Teste Recomendado

```
DESENVOLVIMENTO
     │
     ├─→ 1. Unit Test (Domain/Entity/Mapper)
     │    └─ Verde rápido ✅ (< 1ms)
     │
     ├─→ 2. Unit Test (UseCase com Mock)
     │    └─ Verde rápido ✅ (1-10ms)
     │
     ├─→ 3. Unit Test (Repository com Mock JpaRepository)
     │    └─ Verde rápido ✅ (1-10ms)
     │
     ├─→ 4. E2E Test (Controller com TestContainers)
     │    └─ Verde mais lento ✅ (100-500ms)
     │
     └─→ 5. Refatorar com confiança ✅
          └─ Testes garantem não quebrou nada

MERGE/COMMIT
     │
     ├─→ ./mvnw clean verify
     │   ├─ Testes: < 5 segundos
     │   ├─ Cobertura: > 70%
     │   ├─ Mutation: > 70%
     │   └─ Build: GREEN ✅
     │
     └─→ PIPELINE CI/CD ✅
         └─ Merge seguro!
```

---

## Padrão de Nomenclatura em Testes

```
✅ BOM:
should_create_country_successfully
should_not_create_country_when_nationality_is_blank
should_return_all_countries_paginated
should_throw_validation_exception_when_country_invalid
should_map_dto_to_domain_correctly
should_save_entity_with_id_generated

❌ RUIM:
test1, test2, test3
testCountry
countryTest
test
myTest
```

---

## Checklist Visual de Implementação

```
NOVO FEATURE? SIGA ESTE CHECKLIST:

[ ] Domain Entity
    [ ] Criar classe extends BaseDomain
    [ ] Adicionar validações (@NotBlank)
    [ ] Escrever unit tests (EntityTest)
    [ ] Rodar: ./mvnw test

[ ] Use Case
    [ ] Criar interface extends *UseCase
    [ ] Criar impl com lógica orquestrada
    [ ] Mock de mediator em teste
    [ ] Escrever unit tests (*UseCaseTest)
    [ ] Testar happy path + error cases

[ ] Repository
    [ ] Criar interface Data extends JpaRepository
    [ ] Criar Repository impl (DataQuery, DataCommand)
    [ ] Criar mappers (EntityMapper)
    [ ] Mock JpaRepository em teste
    [ ] Escrever unit tests (RepositoryTest)
    [ ] Validar mapeamento Domain ↔ Entity

[ ] Controller
    [ ] Criar DTOs (com @JsonProperty)
    [ ] Criar DtoMappers
    [ ] Criar Controller (endpoints)
    [ ] Injetar mediators
    [ ] Escrever E2E tests (ControllerFunctionalTest)
    [ ] Testar status codes HTTP

[ ] Geral
    [ ] Rodar: ./mvnw clean verify
    [ ] Verificar cobertura > 70%
    [ ] Verificar mutation score > 70%
    [ ] Nomear testes com should_...
    [ ] Padrão AAA em todos testes
    [ ] Nenhum mocks desnecessários
    [ ] Nenhum teste sem assertions

[ ] Merge
    [ ] Todos testes verdes ✅
    [ ] Code review aprovado ✅
    [ ] Merge main ✅
```

---

## Problemas Comuns e Soluções

```
PROBLEMA                    │ SOLUÇÃO
────────────────────────────┼──────────────────────────────
Teste muito lento (> 1s)    │ Remover @SpringBootTest, usar @Mock
Teste falha intermitente    │ Não compartilhar estado, usar fixtures locais
Mock não funciona           │ Usar @ExtendWith(MockitoExtension.class)
NullPointerException        │ Validar setup, adicionar assertions
"Nunca consegui mockar X"   │ Consultar context-map-testing.md (Módulo X)
Cobertura baixa             │ Testar error cases, não apenas happy path
Testes não isolados         │ Criar dados localmente, não compartilhar
Não sei qual framework usar │ Consultar tabela "Matriz de Teste"
```

---

## Links Rápidos

```
📚 LEITURA:
   context-core.md          ← Arquitetura geral
   testing-strategy.md      ← Estratégia completa
   context-map-testing.md   ← Este documento em detalhe

🎯 REFERÊNCIA:
   src/test/java/adapters/controllers/v1/CountryControllerFunctionalTest.java
   src/test/java/application/usecases/CreateEntityUseCaseImplTest.java
   src/test/java/adapters/repositories/CountryRepositoryTest.java
   src/test/java/adapters/controllers/v1/mappers/CountryDtoMapperTest.java

🚀 COMANDOS:
   ./mvnw test                    ← Rodar todos testes
   ./mvnw clean verify            ← Testes + cobertura
   ./mvnw pitest:mutationCoverage ← Mutation testing
   open target/site/jacoco/index.html ← Ver cobertura
```

---

**Versão**: 1.0  
**Data**: 2026-02-07  
**Status**: Ativo

