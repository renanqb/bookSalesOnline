# Regras Arquiteturais Formalizadas
## BookSalesOnline

**Data de Criação**: 2026-02-07  
**Versão**: 1.0  
**Status**: Guia Normativo

---

> Este documento formaliza as regras arquiteturais implícitas do projeto.
> Deve ser validado por ArchUnit em testes automatizados.

---

## 📋 1. Estrutura de Camadas (Layer Isolation)

### 1.1 Pirâmide Arquitetural Permitida

```
                    ▲ ENTRADA (HTTP)
                    │
        ┌─────────────────────────┐
        │   ADAPTERS (Controllers)│ ← Web Layer
        │   (entrada de dados)    │
        └────────────┬────────────┘
                     │
        ┌─────────────▼────────────┐
        │  APPLICATION (Use Cases) │ ← Application Layer
        │  (orquestração)          │
        └────────────┬────────────┘
                     │
        ┌─────────────▼────────────┐
        │   DOMAIN (Entities)      │ ← Domain Layer
        │   (regras de negócio)    │
        └─────────────────────────┘
                     │
                     ▼
        ┌─────────────────────────┐
        │ INFRASTRUCTURE (Adapters)│ ← Persistence Layer
        │ (BD, Cache, S3)         │
        └─────────────────────────┘
```

### 1.2 Fluxo de Controle de Dependências

```
Controllers
    ↓ (depende de)
Use Cases
    ↓ (depende de)
Domain + Ports (interfaces)
    ↓ (depende de)
Repositories (implementação)
    ↓ (depende de)
JPA Data + Entities
    ↓ (depende de)
Infrastructure (BD, S3, Redis)

Regra: Fluxo é UNI-DIRECIONAL
Nenhuma seta volta para cima! ⚠️
```

---

## ✅ 2. Dependências Permitidas

### 2.1 Dependências Entre Camadas (Fluxo Descendente)

```
DOMAIN LAYER:
  ✓ Pode depender de:
    - Lombok (@Data, @AllArgsConstructor, etc)
    - javax.validation (@NotBlank, @NotNull)
    - Java stdlib (java.time, java.util)
  
  ✗ NÃO pode depender de:
    - Spring Framework
    - JPA/Hibernate
    - Qualquer adaptor ou controller

APPLICATION LAYER:
  ✓ Pode depender de:
    - Domain entities
    - Ports (interfaces out: DataQuery, DataCommand)
    - RepositoryMediator (até refatorar)
    - UseCaseMediator (até refatorar)
    - Spring @Service
    - Lombok
  
  ✗ NÃO pode depender de:
    - Controllers (HTTP boundary)
    - Repositories concretos (usar ports)
    - JPA/Hibernate direto
    - External APIs direto (encapsular em adapter)

ADAPTER LAYER (Controllers):
  ✓ Pode depender de:
    - Use Cases (via mediator ou DI)
    - DTOs
    - Mappers (DTO ↔ Domain)
    - Spring @RestController, @RequestMapping, etc
    - Domain entities (ler, não modificar aqui)
  
  ✗ NÃO pode depender de:
    - Repository concreto (passar por use case)
    - JPA direto
    - Database queries
    - Business logic (deve estar em use case)

ADAPTER LAYER (Repositories):
  ✓ Pode depender de:
    - Domain entities (read-only)
    - Ports (interfaces out)
    - JPA Data (extends JpaRepository)
    - Entities (JPA)
    - Mappers (Entity ↔ Domain)
    - Spring @Repository
  
  ✗ NÃO pode depender de:
    - Controllers
    - Use Cases
    - DTOs
    - Business logic (must be in use case)

INFRASTRUCTURE (Data/Storage):
  ✓ Pode depender de:
    - JPA/Hibernate
    - AWS SDK
    - Redis
    - Spring Data
    - JDBC
  
  ✗ NÃO pode depender de:
    - Qualquer lógica de negócio
    - Controllers
    - Application layer
```

### 2.2 Dependências Permitidas Entre Componentes (Mesmo Nível)

```
DOMAIN:
  Entidade A pode depender de Entidade B?
  ✓ SIM, se B é outra entidade ou tipo de domínio
  ✓ Exemplo: Publisher depende de Country (relacionamento)
  ✗ NÃO se criar acoplamento circular
  
APPLICATION:
  UseCase A pode depender de UseCase B?
  ✓ SIM, via mediator (atual) ou DI (futuro)
  ✗ NÃO acesso direto a privates de outra
  
ADAPTER:
  Repository A pode depender de Repository B?
  ✓ SIM, se precisa buscar dados relacionados
  ✗ NÃO deve compartilhar estado
  
  Mapper A pode depender de Mapper B?
  ✓ SIM, para converter nested objects
  ✓ Exemplo: PublisherDtoMapper usa CountryDtoMapper
```

### 2.3 Dependências de Bibliotecas Externas Permitidas

```
OBRIGATÓRIAS (já em pom.xml):
  ✓ org.springframework.boot:spring-boot-starter-web
  ✓ org.springframework.boot:spring-boot-starter-data-jpa
  ✓ org.springframework.boot:spring-boot-starter-cache
  ✓ org.springframework.boot:spring-boot-starter-actuator
  ✓ org.postgresql:postgresql (driver)
  ✓ org.projectlombok:lombok
  ✓ com.amazonaws:aws-java-sdk-s3
  ✓ redis.clients:jedis

PARA TESTES:
  ✓ org.junit.jupiter:junit-jupiter
  ✓ org.mockito:mockito-core
  ✓ org.assertj:assertj-core
  ✓ org.testcontainers:testcontainers
  ✓ org.testcontainers:testcontainers-postgresql
  ✓ com.amazonaws:aws-java-sdk (para mock)

NOVAS DEPENDÊNCIAS:
  ✓ Propostas aprovadas em FEATURE_TEMPLATE.md
  ✓ Tech Lead deve revisar impacto
  ✗ NÃO adicionar sem justificativa

PROIBIDAS:
  ✗ JEE (Java EE), é Spring-based
  ✗ Arquitetura de microserviços (usar monolito)
  ✗ Relatório (BI) libraries
  ✗ ML/AI libraries (fora de escopo)
  ✗ Legacy drivers (não suportados)
```

---

## ❌ 3. Dependências Proibidas (Violar = Review Rejection)

### 3.1 Dependências que Quebram Arquitetura

```
NUNCA PERMITIR:

1. CONTROLLERS ACESSANDO BD DIRETO
   ❌ @RestController
       @Autowired private Repository repo;  // ❌ NUNCA!
       public void create() {
           repo.save(...);  // ❌ Contorna use case
       }
   
   ✅ FAZER ASSIM:
       @RestController
       public void create() {
           useCase.execute(...);  // ✅ Via use case

2. USE CASES ACESSANDO CONTROLLERS
   ❌ UseCase NÃO pode:
       - Chamar métodos de controller
       - Retornar DTOs (retorna Domain)
       - Conhecer status HTTP
   
   ✅ FAZER ASSIM:
       UseCase retorna Domain puro
       Controller mapeia Domain → DTO
       Controller retorna HTTP response

3. DOMAIN DEPENDENDO DE INFRASTRUCTURE
   ❌ Entity não pode:
       - Ter @Entity (é Domain, não JPA)
       - Ter @Column (é Domain, não BD)
       - Conhecer JDBC/SQL
       - Ter referência a repository
   
   ✅ FAZER ASSIM:
       Domain: class Country extends BaseDomain
       JPA: @Entity CountryEntity
       Mapper traduz entre eles

4. TESTES UNITÁRIOS COM SPRING
   ❌ Unit tests NÃO podem ter:
       - @SpringBootTest (muito lento)
       - @Autowired (use @Mock)
       - TestContainers (use mock)
   
   ✅ FAZER ASSIM:
       @ExtendWith(MockitoExtension.class)
       @Mock private Dependency dep;

5. CIRCULAR DEPENDENCIES
   ❌ A → B → A (ciclo)
   ❌ A → B → C → A (ciclo)
   
   ✓ PERMITIDO: A → B, A → C (múltiplas, não circular)

6. LEAKAGE DE IMPLEMENTAÇÃO
   ❌ Controller retorna JPA Entity:
       @GetMapping("/countries/{id}")
       public CountryEntity get() { ... }  // ❌ Entity vaza!
   
   ✅ FAZER ASSIM:
       public CountryDto get() { ... }  // ✅ DTO seguro

7. BUSINESS LOGIC ESPALHADA
   ❌ Lógica de negócio em:
       - Controllers
       - Repositories
       - Mappers
       - Entities JPA
   
   ✅ FAZER ASSIM:
       Toda lógica em: Domain ou Use Cases

8. TESTE DEPENDENDO DE OUTRO TESTE
   ❌ @Test
       public void test1() { ... }
       
       @Test  // ❌ Depende que test1 rodou antes!
       public void test2() { ... }
   
   ✅ FAZER ASSIM:
       Cada teste é independente
       Sem @Order a menos que E2E sequencial

9. MEDIATOR EM TESTES
   ❌ Unit test mockando mediator:
       @Mock private RepositoryMediator mediator;
   
   ✅ FAZER ASSIM (futuro sem mediator):
       @Mock private DataCommand<T> command;
       @Mock private DataQuery<T> query;

10. CACHE SEM INVALIDAÇÃO
    ❌ @Cacheable em GET
        mas SEM @CacheEvict em UPDATE/DELETE
    
    ✅ FAZER ASSIM:
        @CacheEvict em UPDATE + DELETE
        Sempre invalidar junto com mudança
```

### 3.2 Padrões Anti-Arquiteturais Proibidos

```
1. SERVICE LOCATOR PATTERN (Antipadrão - será refatorado)
   ❌ Atual (temporário):
       var useCase = mediator.get(SomeUseCase.class);  // Lookup em runtime
   
   ✅ FUTURO (Dependency Injection):
       @Autowired private SomeUseCase useCase;  // Type-safe

2. GOD OBJECT (Entidade faz tudo)
   ❌ Publication entity com:
       - save()
       - delete()
       - validate()
       - upload()
   
   ✅ FAZER ASSIM:
       Publication: dados puro
       PublicationUseCase: operações
       PublicationRepository: persistência

3. MIXED CONCERNS (Responsabilidades misturadas)
   ❌ Controller com:
       - Validação BD
       - Cálculos de negócio
       - Acesso a cache
   
   ✅ FAZER ASSIM:
       UseCase: validação + cálculos
       Controller: input validation + mapping

4. TIGHT COUPLING (Acoplamento forte)
   ❌ Controller direto em Repository:
       public CountryEntity get() {
           return repo.findById(1);  // Direto!
       }
   
   ✅ FAZER ASSIM:
       Passar sempre por Use Case

5. LEAKY ABSTRACTIONS
   ❌ Port expõe detalhe de implementação:
       public List<CountryEntity> getAll();  // Retorna Entity!
   
   ✅ FAZER ASSIM:
       public List<Country> getAll();  // Retorna Domain

6. HARDCODED VALUES
   ❌ Constantes espalhadas:
       if (id == 99) throw new Exception();  // 99 aonde?
   
   ✅ FAZER ASSIM:
       private static final int INVALID_ID = 99;

7. NULL POINTER CHECKS MÚLTIPLOS
   ❌ if (country != null)
      if (country.getPublisher() != null)
      if (country.getPublisher().getHistory() != null)
   
   ✅ FAZER ASSIM:
       Usar Optional<T> ou validar em UseCase

8. MISSING ERROR HANDLING
   ❌ Sem try-catch em métodos críticos
   ❌ Deixar StackTrace vazar para cliente
   
   ✅ FAZER ASSIM:
       GlobalExceptionHandler com @ControllerAdvice
       Retornar ErrorResponse estruturado

9. MAGIC STRINGS/NUMBERS
   ❌ "countries/1/publishers"  // Magic string!
      20  // Que são 20? Page size?
   
   ✅ FAZER ASSIM:
       private static final String COUNTRIES_PATH = "countries";
       private static final int DEFAULT_PAGE_SIZE = 20;

10. PREMATURE OPTIMIZATION
    ❌ Cache em lugar errado
    ❌ Índice desnecessário
    ❌ Lógica complexa sem motivo
    
    ✅ FAZER ASSIM:
        YAGNI: Implement when needed
        Medir antes de otimizar
```

---

## 📍 4. Localização de Lógica de Negócio

### 4.1 Onde DEVE Estar Cada Tipo de Lógica

```
┌─────────────────────────────────────────────────────────────┐
│ TIPO DE LÓGICA          LOCAL CORRETO       NUNCA AQUI       │
├─────────────────────────────────────────────────────────────┤
│ Validação de entrada    Domain Entity       Controller        │
│                         (via @NotBlank)     (apenas @Valid)   │
├─────────────────────────────────────────────────────────────┤
│ Validação de negócio    Use Case            Repository/Ctrl   │
│ (FK exists, etc)        (GetById + throw)   (direto em BD)    │
├─────────────────────────────────────────────────────────────┤
│ Orquestração            Use Case            Mapper/Entity     │
│ (múltiplos steps)       (coordena calls)    (não coordena)    │
├─────────────────────────────────────────────────────────────┤
│ Transformação dados     Mapper              Domain/UseCase    │
│ (DTO ↔ Domain)          (conversão)         (lógica aqui)     │
├─────────────────────────────────────────────────────────────┤
│ Persistência            Repository          Controller        │
│ (salvar no BD)          (DataCommand)       (não persiste)    │
├─────────────────────────────────────────────────────────────┤
│ Cálculos complexos      Domain/UseCase      Repository        │
│ (matemática negócio)    (regras)            (apenas fetch)    │
├─────────────────────────────────────────────────────────────┤
│ Cache                   Repository/UC       Controller        │
│ (estratégia)            (via annotation)    (sem cache logic) │
├─────────────────────────────────────────────────────────────┤
│ Filtros/Busca           Repository          Domain Entity     │
│ (queries customizadas)  (SQL qualificado)   (não faz query)   │
├─────────────────────────────────────────────────────────────┤
│ HTTP Response           Controller          UseCase/Domain    │
│ (status codes)          (HTTP knowledge)    (puro domínio)    │
├─────────────────────────────────────────────────────────────┤
│ I/O Externo (S3, etc)   Adapter/Service     UseCase direto    │
│                         (encapsulado)       (não exposto)     │
└─────────────────────────────────────────────────────────────┘
```

### 4.2 Exemplos Práticos de Localização Correta

#### Validação de FK (Country existe?)

```
❌ ERRADO: Em Repository
   public Country save(Country country) {
       if (countryData.findById(country.getId()) == null) {
           throw new Exception();
       }
       return countryData.save(...);
   }

❌ ERRADO: Em Controller
   @PostMapping("/publishers")
   public PublisherDto create(@RequestBody PublisherDto dto) {
       if (repo.getCountry(dto.countryId) == null) {  // Lógica aqui!
           return error();
       }
       return useCase.execute(dto);
   }

✅ CORRETO: Em Use Case
   public class CreatePublisherUseCaseImpl {
       public Publisher execute(Publisher publisher) {
           // Validação de negócio AQUI
           var country = publisherRepository.getCountry(publisher.countryId);
           if (country == null) {
               throw new ValidationException("Country not found");
           }
           return publisherRepository.save(publisher);
       }
   }
```

#### Cálculo de Preço com Desconto

```
❌ ERRADO: Em Repository
   public List<Publication> getAllWithDiscount() {
       var publications = repo.findAll();
       publications.forEach(p -> 
           p.setPrice(p.getPrice() * 0.9)  // Cálculo aqui!
       );
       return publications;
   }

❌ ERRADO: Em Mapper
   public PublicationDto fromDomain(Publication pub) {
       var dto = new PublicationDto();
       dto.setPrice(pub.getPrice() * 0.9);  // Cálculo de negócio!
       return dto;
   }

✅ CORRETO: Em Domain ou Use Case
   public class Publication extends BaseDomain {
       public BigDecimal getPriceWithDiscount(double rate) {
           return price.multiply(new BigDecimal(1 - rate));
       }
   }
   
   // OU em Use Case:
   public class GetPublicationWithDiscountUseCaseImpl {
       public Publication execute(int id, double discountRate) {
           var publication = repo.getById(id);
           return publication.getPriceWithDiscount(discountRate);
       }
   }
```

#### Lógica de Cache Invalidation

```
❌ ERRADO: Sem @CacheEvict
   public Publication update(Publication pub) {
       return repo.update(pub);  // Cache fica stale!
   }

✅ CORRETO: Com @CacheEvict
   @CacheEvict(value = "publication", key = "#pub.id")
   public Publication update(Publication pub) {
       return repo.update(pub);  // Cache invalidado!
   }
```

---

## 🧪 5. Restrições Específicas para Testes

### 5.1 Localização Correta de Testes

```
PADRÃO DE PASTA:
src/test/java/com/renan/booksalesonline/tests/
├── adapters/
│   ├── controllers/v1/
│   │   ├── CountryControllerFunctionalTest.java      (E2E)
│   │   ├── mappers/
│   │   │   └── CountryDtoMapperTest.java             (Unit)
│   │   └── model/
│   │       └── CountryDtoTest.java                   (Unit)
│   ├── repositories/
│   │   ├── CountryRepositoryTest.java                (Unit + Mock)
│   │   ├── entities/
│   │   │   └── CountryEntityTest.java                (Unit)
│   │   └── mappers/
│   │       └── CountryEntityMapperTest.java          (Unit)
│   └── storage/
│       └── S3StorageServiceTest.java                 (Unit + Mock)
├── application/
│   ├── usecases/
│   │   ├── CreateEntityUseCaseImplTest.java          (Unit + Mock)
│   │   ├── RemoveEntityUseCaseImplTest.java          (Unit + Mock)
│   │   └── publisher/
│   │       └── CreatePublisherUseCaseImplTest.java   (Unit + Mock)
│   └── mediators/
│       └── UseCaseMediatorImplTest.java              (Unit + Mock)
├── domain/
│   └── (Domain entities NÃO tem testes separados)
│       (Testadas via Use Case tests)
└── testhelpers/
    ├── BookSalesOnlineContainerTest.java
    ├── BookSalesOnlineDatabaseContainer.java
    └── RestClientTesting.java

REGRA: Test class localização espelha classe testada
  - CountryController → CountryControllerFunctionalTest
  - CreateCountryUseCase → CreateCountryUseCaseTest
  - CountryRepository → CountryRepositoryTest
```

### 5.2 Restrições de Tipo de Teste por Camada

```
DOMAIN LAYER:
  ✓ Permitido: Unit tests (instantiação, validações)
  ✓ Framework: JUnit 5 + AssertJ (sem Spring)
  ✓ Mocks: Nenhum necessário
  ✓ Tempo esperado: < 1ms por teste
  ✗ NÃO permitido: @SpringBootTest, TestContainers

APPLICATION LAYER:
  ✓ Permitido: Unit tests (com mocks)
  ✓ Framework: JUnit 5 + Mockito + AssertJ
  ✓ Mocks: RepositoryMediator, DataCommand, DataQuery
  ✓ Tempo esperado: 1-10ms por teste
  ✗ NÃO permitido: @SpringBootTest, I/O real, BD real

ADAPTER - REPOSITORIES:
  ✓ Permitido: Unit tests (mock JpaRepository)
  ✓ Framework: JUnit 5 + Mockito
  ✓ Mocks: *Data (JpaRepository)
  ✓ Tempo esperado: 1-10ms
  ✗ NÃO permitido: @SpringBootTest, BD real

ADAPTER - CONTROLLERS:
  ✓ Permitido: E2E/Functional tests (@SpringBootTest)
  ✓ Framework: Spring Boot Test + Rest Assured + TestContainers
  ✓ BD: TestContainers (real)
  ✓ Tempo esperado: 100-500ms por teste
  ✓ Obrigatório: TestContainers + CRUD completo

ADAPTER - MAPPERS/ENTITIES:
  ✓ Permitido: Unit tests (sem Spring)
  ✓ Framework: JUnit 5 + AssertJ
  ✓ Mocks: Nenhum
  ✓ Tempo esperado: < 1ms
  ✗ NÃO permitido: @SpringBootTest
```

### 5.3 Restrições de Assertions

```
BOM (Significativo):
  ✓ assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED)
  ✓ assertThat(result.getId()).isGreaterThan(0)
  ✓ assertThrows(ValidationException.class, () -> useCase.execute(...))
  ✓ assertThat(actual).usingRecursiveComparison().isEqualTo(expected)

RUIM (Trivial):
  ✗ assertThat(result).isNotNull()                     # Óbvio!
  ✗ assertThat(id).isGreaterThan(0)                    # Vago!
  ✗ // Sem assertions (teste que só executa)
  ✗ assertThat(obj).isInstanceOf(Object.class)         # Sempre true!

REGRA:
  1-3 assertions por teste (máximo 5 se relacionadas)
  Cada assertion responde: "Por que testo isto?"
  Remover assertions triviais
  Assertions SIGNIFICATIVAS obrigatórias
```

### 5.4 Restrições de Mocks

```
PERMITIDO:
  ✓ Mock de dependências externas
  ✓ Mock de RepositoryMediator (até refatorar)
  ✓ Mock de JpaRepository (*Data)
  ✓ Mock de DataCommand, DataQuery
  ✓ Stub de constantes e dados fixos

PROIBIDO:
  ✗ Mock de classes testadas (use real)
  ✗ Mock desnecessário (se não usa, remove)
  ✗ Mock em E2E tests (tudo real com TestContainers)
  ✗ Mock de domain entities (cria real)
  ✗ Mock de mappers (testa real)

REGRA:
  - Cada @Mock tem purpose claro
  - Mock apenas se quebra teste sem ele
  - Remove mock se teste passa sem
```

### 5.5 Restrições de Isolamento

```
OBRIGATÓRIO:
  ✓ Cada teste independente
  ✓ Dados criados localmente (não compartilhados)
  ✓ Sem @Order (exceto E2E sequencial)
  ✓ Sem variáveis static (exceto constantes)
  ✓ Sem dependência de teste anterior

PROIBIDO:
  ✗ Teste que depende de outro
  ✗ Estado compartilhado entre testes
  ✗ @BeforeEach com setup compartilhado
  ✗ Ordem de execução crítica (em unit tests)

EXCEÇÃO:
  E2E tests podem ter @Order + @TestMethodOrder
  Razão: Sequência de CRUD (1: CREATE → 2: READ → 3: UPDATE)
  MAS: Dados em @BeforeAll + @Transactional para isolamento
```

### 5.6 Restrições de Performance

```
OBRIGATÓRIO:
  ✓ Unit tests: < 1 segundo (total)
  ✓ Integration tests: 1-2 segundos
  ✓ E2E tests: 2-4 segundos
  ✓ Suite total: < 5 segundos

PROIBIDO:
  ✗ Unit test > 100ms (indica @SpringBootTest ou I/O)
  ✗ E2E test > 1 segundo cada (parallelizar?)
  ✗ Suite > 10 segundos (refatorar)
  ✗ Thread.sleep() em testes (usar @Transactional)
  ✗ LocalDateTime.now() (mockar ou injetar)

REGRA:
  Fast feedback = desenvolvedores rodam testes sempre
  Lento = desenvolvedores pulam testes
```

### 5.7 Restrições de Coverage

```
OBRIGATÓRIO:
  ✓ Coverage ≥ 70% (JaCoCo)
  ✓ Mutation score ≥ 70% (PIT)
  ✓ Novo código sempre testado
  ✓ Happy path + error cases

PROIBIDO:
  ✗ Coverage < 70% (rejeitar em review)
  ✗ Mutation < 70% (testes fracos)
  ✗ Novo código sem testes
  ✗ Coverage diminuir (manter ou aumentar)
  ✗ Apenas happy path (precisa error cases)

EXCLUSÕES PERMITIDAS (sem coverage):
  - Main.class (entry point)
  - @Configuration classes
  - Exception classes (construtores)
  - *Dto classes (Lombok generated)
  - *Entity classes (JPA generated)
```

---

## 🔐 6. Validação de Regras (ArchUnit)

### 6.1 Testes de Arquitetura Recomendados

```
@AnalyzeClasses(packages = "com.renan.booksalesonline")
public class ArchitectureTests {
    
    // Regra 1: Domain não depende de Spring
    @ArchTest
    static final ArchRule domain_should_not_depend_on_spring =
        classes()
            .that().resideInAPackage("..domain..")
            .should().notDependOnClassesThat()
                .resideInAPackage("org.springframework..");
    
    // Regra 2: Controllers não acessam Repository direto
    @ArchTest
    static final ArchRule controllers_should_not_access_repositories_directly =
        classes()
            .that().resideInAPackage("..controllers..")
            .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                    "..controllers..",
                    "..ports..",
                    "..mappers..",
                    "..model..",
                    "java..",
                    "org.springframework..");
    
    // Regra 3: Use Cases não acessam JPA direto
    @ArchTest
    static final ArchRule usecases_should_not_depend_on_jpa =
        classes()
            .that().resideInAPackage("..usecases..")
            .should().notDependOnClassesThat()
                .resideInAPackage("javax.persistence..");
    
    // Regra 4: Repositories não contêm lógica de negócio
    @ArchTest
    static final ArchRule repositories_should_not_have_business_logic =
        methods()
            .that().areDeclaredInClassesThat()
                .resideInAPackage("..repositories..")
                .and().haveNameMatching(".*[Cc]alculate.*|.*[Cc]heck.*")
            .should().notExist();
    
    // Regra 5: Camadas isoladas (sem ciclos)
    @ArchTest
    static final ArchRule layers_are_isolated =
        layeredArchitecture()
            .layer("Domain").definedBy("..domain..")
            .layer("Application").definedBy("..application..")
            .layer("Adapters").definedBy("..adapters..")
            .layer("Infrastructure").definedBy("..persistence..")
            
            .whereLayer("Adapters").mayNotAccessAnyLayer()
            .whereLayer("Application").mayOnlyAccessLayers("Domain")
            .whereLayer("Domain").mayNotAccessAnyLayer();
}
```

### 6.2 Como Rodar Validação de Arquitetura

```bash
# Criar teste com ArchUnit
./mvnw test -Dtest=ArchitectureTests

# Ou com Maven Enforcer
./mvnw clean verify

# Resultado esperado:
✓ Domain não depende de Spring
✓ Controllers não acessam Repository direto
✓ Use Cases não dependem de JPA
✓ Sem ciclos entre camadas
✓ Isolamento mantido
```

---

## 📊 7. Matriz de Regras

```
┌─────────────────────────────┬────────────┬────────────────────┐
│ REGRA                       │ CATEGORIA  │ VIOLAÇÃO = ?       │
├─────────────────────────────┼────────────┼────────────────────┤
│ Domain sem Spring           │ Crítica    │ Code Review REJECT │
│ Controllers sem Repository  │ Crítica    │ Code Review REJECT │
│ Use Cases sem Business Lg   │ Crítica    │ Code Review REJECT │
│ Sem ciclos de dependência   │ Crítica    │ Code Review REJECT │
│                             │            │                    │
│ Tests com @ Padrão          │ Alta       │ Code Review REJECT │
│ Coverage ≥ 70%              │ Alta       │ Code Review REJECT │
│ Mutation ≥ 70%              │ Alta       │ Code Review REJECT │
│ Suite < 5s                  │ Alta       │ Performance Review │
│                             │            │                    │
│ Tests independentes         │ Média      │ Author fix before  │
│ Assertions significativas    │ Média      │ Code Review REJECT │
│ Sem duplicação (DRY)        │ Média      │ Code Review FIX    │
│ Mappers bidirecional        │ Média      │ Code Review FIX    │
│                             │            │                    │
│ Naming conventions          │ Baixa      │ Code Review COMMENT│
│ Código legível              │ Baixa      │ Author Improvement │
└─────────────────────────────┴────────────┴────────────────────┘
```

---

## 🚨 8. Enforcement Strategy

### 8.1 Como as Regras são Enforçadas

```
BUILD TIME:
  ✓ ArchUnit tests validam estrutura (./mvnw verify)
  ✓ Checkstyle valida nomeação
  ✓ SpotBugs valida bugs comuns
  ✓ Jacoco verifica coverage

COMMIT TIME:
  ✓ Pre-commit hooks (opcional)
  ✓ ./mvnw clean verify DEVE passar
  ✓ Sem warnings ignorados

CODE REVIEW TIME:
  ✓ Tech Lead valida regras críticas
  ✓ QA valida testes
  ✓ Checklist da DoD
  ✓ ArchUnit screenshots

RUNTIME:
  ✓ Spring Bean validation
  ✓ Global exception handler
  ✓ Observabilidade com métricas

APÓS MERGE:
  ✓ CI/CD pipeline roda tudo
  ✓ Alertas em caso de regressão
```

### 8.2 Escalação de Violação

```
CRÍTICA (Paralisa desenvolvimento):
  ❌ Domain depende de Spring
  ❌ Controller acessa Repository direto
  ❌ Ciclo de dependência
  
  Ação: Code Review REJEITA
         Dev DEVE refatorar
         Tech Lead aprova refatoração

ALTA (Bloqueia merge):
  ❌ Coverage < 70%
  ❌ Mutation < 70%
  ❌ Suite > 10 segundos
  
  Ação: Code Review REJEITA
         Dev adiciona testes
         Roda ./mvnw verify novamente

MÉDIA (Discussão em review):
  ❌ Teste não independente
  ❌ Assertions triviais
  ❌ Duplicação de código
  
  Ação: Code Review pede MUDANÇAS
         Dev corrige
         Re-review necessário

BAIXA (Sugestão):
  ❌ Naming não segue padrão
  ❌ Código não legível
  
  Ação: Code Review COMENTA
         Dev melhora (opcional)
         Merge permitido com comentário
```

---

## 📚 Referências

Estas regras são derivadas de:
- [context-core.md](context-core.md) - Arquitetura
- [testing-strategy.md](testing-strategy.md) - Estratégia de testes
- [DEFINITION_OF_DONE.md](DEFINITION_OF_DONE.md) - DoD
- [FEATURE_TEMPLATE.md](FEATURE_TEMPLATE.md) - Feature planning
- [critical-test-flows.md](critical-test-flows.md) - Fluxos críticos

---

## ✅ Checklist de Conformidade

Antes de submeter PR:

```
DEPENDÊNCIAS:
  [ ] Domain sem Spring?
  [ ] Controllers sem Repository direto?
  [ ] Use Cases sem JPA direto?
  [ ] Sem ciclos de dependência?

TESTES:
  [ ] Tests estão no local correto?
  [ ] Type de teste correto (unit/integration/E2E)?
  [ ] Coverage ≥ 70%?
  [ ] Mutation ≥ 70%?
  [ ] Suite < 5 segundos?

LÓGICA:
  [ ] Validação em Use Case?
  [ ] Orquestração em Use Case?
  [ ] Persistência em Repository?
  [ ] Transformação em Mapper?

QUALIDADE:
  [ ] Testes independentes?
  [ ] Assertions significativas?
  [ ] DRY (sem duplicação)?
  [ ] Código legível?

CONFORMIDADE:
  [ ] ./mvnw clean verify PASSA?
  [ ] ArchUnit PASSA?
  [ ] Nenhum warning?
  [ ] DEFINITION_OF_DONE cumprida?

✅ OK para merge!
```

---

**Versão**: 1.0  
**Data**: 2026-02-07  
**Status**: 🔴 OBRIGATÓRIO - Conformidade não-negociável  
**Enforcement**: ArchUnit + Code Review + CI/CD

> Estas regras garantem a manutenibilidade, testabilidade e escalabilidade do projeto.
> Violações comprometem a arquitetura e são rejeitadas em review.

