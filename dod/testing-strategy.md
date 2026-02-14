# Estratégia de Testes - BookSalesOnline

**Data da Análise**: 2026-02-07  
**Versão do Documento**: 1.0  

---

## Visão Geral

A estratégia de testes do BookSalesOnline segue uma **pirâmide de testes** bem balanceada com forte cobertura em múltiplos níveis. Implementa **Behavior-Driven Testing** com foco em legibilidade e isolamento.

### Pirâmide de Testes Implementada

```
                    △ E2E / Acceptance
                   /|\  (Containers + Real DB)
                  / | \
                 /  |  \  Integration Tests
                /   |   \ (Repository, Mediators)
               /____|____\
              /     |     \ Unit Tests
             /______|______\ (Use Cases, Mappers, Entities)
```

---

## 1. Tipos de Testes Existentes

### 1.1 Testes Unitários (Unit Tests)

**Propósito**: Validar lógica isolada de componentes individuais

**Tecnologia**: JUnit 5 + Mockito + AssertJ

**Características**:
- Sem dependências de infraestrutura
- Mocks de todas as dependências
- Execução < 1ms por teste
- Teste de classe/método isolado

**Exemplos Encontrados**:

#### Entity Tests
```
src/test/java/adapters/repositories/entities/
├── CountryEntityTest.java
├── ImageEntityTest.java
└── PublisherEntityTest.java
```

**Padrão**: Testar instanciação e atributos
```java
@Test
public void should_create_a_country_entity_instance() {
    var expected = new CountryEntity(99, "Brazil", "Brazilian");
    assertThat(expected.getId()).isEqualTo(99);
    assertThat(expected.getName()).isEqualTo("Brazil");
    assertThat(expected.getNationality()).isEqualTo("Brazilian");
}
```

#### DTO Tests
```
src/test/java/adapters/controllers/v1/model/
├── CountryDtoTest.java
├── PublisherDtoTest.java
└── PublicationImageDtoTest.java
```

#### Mapper Tests
```
src/test/java/adapters/controllers/v1/mappers/
├── CountryDtoMapperTest.java
├── PublisherDtoMapperTest.java
├── CountryEntityMapperTest.java
└── PublicationImageDtoMapperTest.java
```

**Padrão**: Testar conversão bidirecional
```java
@Test
public void should_parse_country_dto_to_country_domain() {
    var dto = new CountryDto("name", "gentilic");
    dto.setId(1);
    
    var expected = new Country(1, "name", "gentilic");
    var actual = CountryDtoMapper.toDomain(dto);
    
    assertThat(actual)
        .usingRecursiveComparison()
        .isEqualTo(expected);
}
```

#### Use Case Tests
```
src/test/java/application/usecases/
├── CreateEntityUseCaseImplTest.java
├── GetAllEntitiesUseCaseImplTest.java
├── RemoveEntityUseCaseImplTest.java
├── UpdateEntityUseCaseImplTest.java
├── GetEntityByIdUseCaseImplTest.java
├── country/
│   └── RemoveCountryUseCaseImplTest.java
└── publisher/
    ├── CreatePublisherUseCaseImplTest.java
    ├── UpdatePublisherUseCaseImplTest.java
    └── RemovePublisherUseCaseImplTest.java
```

**Padrão**: Orquestração isolada com mocks
```java
@ExtendWith(MockitoExtension.class)
public class CreateEntityUseCaseImplTest {
    @Mock private DataCommand<Country> countryCommand;
    @Mock private RepositoryMediator mediator;
    @InjectMocks private CreateEntityUseCaseImpl createEntityUseCaseImpl;

    @Test
    public void should_create_an_entity_successfully() throws NoSuchMethodException {
        var inputEntity = new Country(0, "name", "gentilic");
        
        when(mediator.getCommand(Country.class))
            .thenReturn(countryCommand);
        when(countryCommand.save(inputEntity))
            .then(r -> {
                inputEntity.setId(1);
                return inputEntity;
            });
        
        var outputEntity = createEntityUseCaseImpl.execute(Country.class, inputEntity);
        assertThat(outputEntity).isEqualTo(inputEntity);
    }
}
```

#### Mediator Tests
```
src/test/java/application/mediators/
├── UseCaseMediatorImplTest.java
└── RepositoryMediatorImplTest.java
```

**Padrão**: Validar registro de dependências
```java
@Test
public void should_get_a_country_get_all_countries_use_case() throws NoSuchMethodException {
    var useCase = useCaseMediator.get(GetAllEntitiesUseCase.class);
    assertThat(useCase).isNotNull();
}
```

---

### 1.2 Testes de Integração (Integration Tests)

**Propósito**: Validar camada de persistência com banco de dados

**Tecnologia**: JUnit 5 + TestContainers + PostgreSQL

**Características**:
- Banco de dados real isolado (PostgreSQL container)
- Testa mapeamento Domain ↔ Entity
- Testa queries e operações de persistência
- Sem infraestrutura de rede (in-process)

**Exemplos Encontrados**:

```
src/test/java/adapters/repositories/
├── CountryRepositoryTest.java
├── PublisherRepositoryTest.java
├── PublicationRepositoryTest.java
├── LanguageRepositoryTest.java
└── ImageRepositoryTest.java
```

**Padrão**: Mock de JpaRepository, validar mapping
```java
@ExtendWith(MockitoExtension.class)
public class CountryRepositoryTest {
    @Mock private CountryData countryData;
    @InjectMocks private CountryRepository countryRepository;

    @Test
    public void should_get_all_country_entities() {
        var countryEntities = Arrays.asList(
            new CountryEntity(1, "name1", "gentilic1"),
            new CountryEntity(2, "name2", "gentilic2"),
            new CountryEntity(3, "name3", "gentilic3")
        );
        var countryEntitiesPage = new PageImpl<>(countryEntities);
        
        when(countryData.findAll(any(PageRequest.class)))
            .thenReturn(countryEntitiesPage);

        var countries = countryRepository.getAll(0, 20);

        for (int i = 0; i < 3; i++) {
            var expected = expectedCountries.get(i);
            var actual = countries.get(i);
            
            assertThat((int)actual.getId()).isEqualTo((int)expected.getId());
            assertThat(actual.getName()).isEqualTo(expected.getName());
            assertThat(actual.getNationality()).isEqualTo(expected.getNationality());
        }
    }
}
```

---

### 1.3 Testes Funcionais / E2E (Functional Tests)

**Propósito**: Validar fluxo completo ponta-a-ponta com infraestrutura real

**Tecnologia**: Spring Boot Test + TestContainers + Rest Assured + PostgreSQL + Redis

**Características**:
- Aplicação Spring completa (todos os beans)
- Banco de dados PostgreSQL real (container)
- Cache Redis real (container)
- HTTP cliente real (TestRestTemplate)
- Testa múltiplas camadas integradas

**Exemplos Encontrados**:

```
src/test/java/adapters/controllers/v1/
├── CountryControllerFunctionalTest.java
├── PublisherControllerFunctionalTest.java
├── PublicationControllerFunctionalTest.java
└── LanguageControllerFunctionalTest.java
```

**Padrão**: Setup de dados + requisições HTTP + assertions
```java
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CountryControllerFunctionalTest extends BookSalesOnlineContainerTest {
    
    @Autowired private RestClientTesting restClientTesting;
    @Autowired private CountryRepository countryRepository;
    private int createdCountryId = 0;

    @BeforeAll
    @Transactional
    public void init() {
        countryRepository.save(argentina);
        countryRepository.save(brazil);
    }

    @Test
    @Order(1)
    public void should_create_a_country_successfully() {
        var usa = new CountryDto("usa_a", "usa_a");
        var response = restClientTesting.post(CountryDto.class, "countries", usa);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        var country = response.getBody();
        assertThat(country.getName()).isEqualTo("usa_a");
        createdCountryId = (int) country.getId();
    }

    @Test
    @Order(2)
    public void should_update_created_country_successfully() {
        var usa = new CountryDto("USA", "American");
        var response = restClientTesting.put(
            CountryDto.class, 
            "countries/" + createdCountryId, 
            usa
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var country = response.getBody();
        assertThat(country.getName()).isEqualTo("USA");
    }
}
```

---

### 1.4 Testes de Arquitetura (Architecture Tests)

**Status**: ⚠️ **Configurado mas não implementado**

**Tecnologia**: ArchUnit 1.2.1

**Propósito**: Validar regras arquiteturais em tempo de teste

**Exemplo esperado**:
```java
@AnalyzeClasses(packages = "com.renan.booksalesonline")
public class ArchitectureTests {
    
    @ArchTest
    static final ArchRule layers_should_be_separated = 
        noClasses().that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..adapters..");
    
    @ArchTest
    static final ArchRule controllers_should_end_with_controller =
        classes().that().resideInAPackage("..adapters.controllers..")
            .should().haveSimpleNameEndingWith("Controller");
}
```

---

### 1.5 Testes de Qualidade de Mutação (Mutation Tests)

**Status**: ✅ **Configurado**

**Tecnologia**: PIT (Pitest) 1.16.1

**Propósito**: Verificar se testes conseguem detectar mudanças no código

**Executar**:
```bash
./mvnw pitest:mutationCoverage
open target/pit-reports/index.html
```

---

### Resumo de Cobertura de Testes

| Nível | Tipo | Padrão | Exemplo |
|-------|------|--------|---------|
| 🔴 Unit | Entity/DTO | Instanciação + atributos | `CountryEntityTest` |
| 🟡 Unit | Mapper | Conversão bidirecional | `CountryDtoMapperTest` |
| 🟡 Unit | Use Case | Mock de mediator | `CreateEntityUseCaseImplTest` |
| 🟢 Integration | Repository | Mock JpaRepository | `CountryRepositoryTest` |
| 🟢 E2E | Controller | Containers reais | `CountryControllerFunctionalTest` |
| 🟢 Architecture | (Pendente) | ArchUnit | - |

---

## 2. Estratégia de Isolamento

### 2.1 Isolamento em Testes Unitários

**Objetivo**: Cada teste verifica uma única responsabilidade (Single Responsibility)

**Técnicas**:

#### a) Mocks de Dependências
```java
@ExtendWith(MockitoExtension.class)
public class CreateEntityUseCaseImplTest {
    
    @Mock private RepositoryMediator mediator;     // ← Dependência mockada
    @InjectMocks private CreateEntityUseCaseImpl impl;  // ← SUT (System Under Test)
    
    // SUT isolado de dependências reais
}
```

#### b) Nenhuma Dependência de Infraestrutura
```java
// ✅ BOM - Sem Spring, sem banco de dados
public class CountryEntityTest {
    @Test
    public void should_create_entity() {
        var entity = new CountryEntity(1, "Brazil", "Brazilian");
        assertThat(entity.getId()).isEqualTo(1);
    }
}

// ❌ RUIM - Dependeria de banco de dados
@SpringBootTest
public class CountryEntityTest { ... }
```

#### c) Fixtures Locais
```java
@Test
public void test() {
    // Criar dados de teste localmente, não reutilizar
    var country = new Country(1, "Brazil", "Brazilian");
    // ...
}
```

---

### 2.2 Isolamento em Testes de Integração

**Objetivo**: Testar persistência com banco real, mas isolado de outros componentes

**Técnicas**:

#### a) Mocks de JpaRepository
```java
@ExtendWith(MockitoExtension.class)
public class CountryRepositoryTest {
    
    @Mock private CountryData countryData;  // ← JpaRepository mockado
    @InjectMocks private CountryRepository repo;  // ← Adapter testado
    
    // Repository isolado de banco real, mas testa mapeamento
}
```

#### b) Sem Spring Context
```java
// ✅ BOM - Sem carregar context Spring
@ExtendWith(MockitoExtension.class)
public class CountryRepositoryTest { ... }

// ❌ RUIM - Carrega context inteiro, lento
@SpringBootTest
public class CountryRepositoryTest { ... }
```

---

### 2.3 Isolamento em Testes E2E

**Objetivo**: Testar aplicação completa com infraestrutura isolada

**Técnicas**:

#### a) TestContainers para BD e Cache
```java
public abstract class BookSalesOnlineContainerTest {
    @ClassRule
    public static PostgreSQLContainer<BookSalesOnlineDatabaseContainer> 
        postgreSQLContainer = BookSalesOnlineDatabaseContainer.getInstance();
    
    @ClassRule
    public static GenericContainer<BookSalesOnlineRedisContainer>
        redisContainer = BookSalesOnlineRedisContainer.getInstance();
    // Containers isolados por classe de teste
}
```

#### b) Transações Isoladas
```java
@BeforeAll
@Transactional  // ← Dados de setup em transação
public void init() {
    countryRepository.save(argentina);
    countryRepository.save(brazil);
}

@Test
@Transactional  // ← Cada teste em transação isolada
public void should_create_country() {
    // Dados gerados não afetam outros testes
}
```

#### c) Ordem de Testes Garantida
```java
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CountryControllerFunctionalTest {
    
    @Test @Order(1) public void create() { ... }
    @Test @Order(2) public void update() { ... }
    @Test @Order(3) public void read() { ... }
    @Test @Order(4) public void delete() { ... }
    // Execução sequencial garante estado
}
```

---

### 2.4 Matriz de Isolamento por Tipo

| Aspecto | Unit | Integration | E2E |
|---------|------|-------------|-----|
| **Spring Context** | ❌ Não | ❌ Não | ✅ Sim (completo) |
| **JPA Repository** | 🔴 Mocked | 🔴 Mocked | ✅ Real |
| **Banco de Dados** | ❌ Não | ❌ Não | ✅ Real (container) |
| **Cache (Redis)** | ❌ Não | ❌ Não | ✅ Real (container) |
| **HTTP** | ❌ Não | ❌ Não | ✅ Real |
| **Velocidade** | ⚡ <1ms | ⚡ 1-10ms | 🐢 100-500ms |
| **Isolamento** | 🔒 Total | 🔒 Total | 🔒 Por transação |

---

## 3. Dobles de Teste (Mocks, Stubs, Fakes)

### 3.1 Mocks (Mockito)

**Definição**: Objetos que rastreiam chamadas e retornam valores pré-configurados

**Padrão**: `when(...).thenReturn(...)` ou `when(...).then(...)`

#### Exemplo 1: Mock Simples
```java
@Mock private DataCommand<Country> countryCommand;

@Test
public void test() {
    when(countryCommand.save(any(Country.class)))
        .thenReturn(new Country(1, "name", "gentilic"));
    
    var result = countryCommand.save(new Country(0, "name", "gentilic"));
    assertThat(result.getId()).isEqualTo(1);
}
```

#### Exemplo 2: Mock com Side Effects
```java
@Test
public void should_create_an_entity_successfully() {
    var inputEntity = new Country(0, "name", "gentilic");
    
    when(countryCommand.save(inputEntity))
        .then(invocation -> {
            inputEntity.setId(1);  // ← Side effect
            return inputEntity;
        });
    
    var result = countryCommand.save(inputEntity);
    assertThat(result.getId()).isEqualTo(1);  // ← Verifica side effect
}
```

#### Exemplo 3: Mock com ArgumentMatchers
```java
@Test
public void should_get_all_countries() {
    var entities = Arrays.asList(
        new CountryEntity(1, "name1", "gentilic1"),
        new CountryEntity(2, "name2", "gentilic2")
    );
    
    when(countryData.findAll(any(PageRequest.class)))
        .thenReturn(new PageImpl<>(entities));
    
    var countries = countryRepository.getAll(0, 20);
    assertThat(countries).hasSize(2);
}
```

**Vantagens**:
- Rastreia chamadas e parâmetros
- Implementa comportamento customizado
- Verifica interações entre objetos

**Desvantagens**:
- Acoplado à implementação (testa "como", não "o quê")

---

### 3.2 Stubs (Retornos Pré-configurados)

**Definição**: Objetos que retornam valores fixos

**Padrão em Testes**: Arrays/Collections pré-criados

#### Exemplo: Stub de Dados
```java
@Test
public void should_get_by_id() {
    // Stub de repository
    var stubCountryEntity = new CountryEntity(1, "Brazil", "Brazilian");
    
    when(countryData.findById(1))
        .thenReturn(Optional.of(stubCountryEntity));
    
    var country = countryRepository.getById(1);
    assertThat(country.getName()).isEqualTo("Brazil");
}
```

**Características**:
- Simples, sem lógica complexa
- Sempre retorna mesma coisa
- Ótimo para dados de teste

---

### 3.3 Fakes (Implementações Reais Simplificadas)

**Status**: ⚠️ **Não encontrados na aplicação atual**

**Exemplo esperado**:
```java
// Fake Repository em memória
public class CountryRepositoryFake implements DataQuery<Country> {
    private List<Country> countries = new ArrayList<>();
    
    @Override
    public List<Country> getAll(int page, int size) {
        return countries.subList(0, Math.min(size, countries.size()));
    }
    
    @Override
    public Country getById(int id) {
        return countries.stream()
            .filter(c -> c.getId() == id)
            .findFirst()
            .orElse(null);
    }
}

// Uso em testes
@Test
public void test() {
    var fakeRepository = new CountryRepositoryFake();
    var useCase = new GetEntityByIdUseCaseImpl(fakeRepository);
    
    var country = useCase.execute(Country.class, 1);
    // Testa sem mock, com implementação real simplificada
}
```

---

### 3.4 Containers (Test Fixtures para Infraestrutura)

**Definição**: Infraestrutura isolada para testes (DB, Cache)

**Implementação**: TestContainers

#### PostgreSQL Container
```java
public class BookSalesOnlineDatabaseContainer 
    extends PostgreSQLContainer<BookSalesOnlineDatabaseContainer> {
    
    private static BookSalesOnlineDatabaseContainer container;
    
    public static BookSalesOnlineDatabaseContainer getInstance() {
        if (container == null) {
            container = new BookSalesOnlineDatabaseContainer();
        }
        return container;
    }
    
    @Override
    public void start() {
        super.start();
        // Propagate container connection strings to tests
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }
}
```

**Uso**:
```java
public abstract class BookSalesOnlineContainerTest {
    @ClassRule
    public static PostgreSQLContainer<BookSalesOnlineDatabaseContainer> 
        postgreSQLContainer = BookSalesOnlineDatabaseContainer.getInstance();
    // Disponível para todas as subclasses
}
```

**Vantagens**:
- Banco real, não in-memory (testa SQL real)
- Isolado por teste
- Limpeza automática
- Compatível com CI/CD

---

### Resumo de Dobles

| Tipo | Uso | Exemplo | Vantagem | Desvantagem |
|------|-----|---------|----------|-------------|
| **Mock** | Rastrear chamadas | `when(...).thenReturn()` | Verifica interações | Acoplado à impl. |
| **Stub** | Retornar valores | `Optional.of(...)` | Simples | Sem verificações |
| **Fake** | Implementação simples | In-memory repository | Real mas rápido | Trabalho extra |
| **Container** | Infraestrutura isolada | PostgreSQL, Redis | Banco real | Lento |

---

## 4. Estratégia de Setup / Teardown

### 4.1 Setup em Testes Unitários

**Padrão**: Sem setup compartilhado, dados locais

```java
@ExtendWith(MockitoExtension.class)
public class CreateEntityUseCaseImplTest {
    
    @Mock private DataCommand<Country> countryCommand;
    @InjectMocks private CreateEntityUseCaseImpl impl;
    
    // Não há @BeforeEach - dados criados em cada teste
    
    @Test
    public void should_create() {
        // Setup local, específico do teste
        var inputEntity = new Country(0, "name", "gentilic");
        // ...
    }
}
```

**Razão**: Cada teste é independente, isolado

---

### 4.2 Setup em Testes de Integração

**Padrão**: Mock setup no construtor, dados em testes

```java
@ExtendWith(MockitoExtension.class)
public class CountryRepositoryTest {
    
    @Mock private CountryData countryData;
    @InjectMocks private CountryRepository countryRepository;
    
    // MockitoExtension initializa mocks automaticamente
    // Sem @BeforeEach necessário
    
    @Test
    public void should_get_all() {
        // Setup local de dados mockados
        var entities = Arrays.asList(
            new CountryEntity(1, "name1", "gentilic1"),
            new CountryEntity(2, "name2", "gentilic2")
        );
        when(countryData.findAll(any(PageRequest.class)))
            .thenReturn(new PageImpl<>(entities));
        
        var result = countryRepository.getAll(0, 20);
    }
}
```

---

### 4.3 Setup em Testes E2E

**Padrão**: `@BeforeAll` para dados iniciais, `@Transactional` para isolamento

```java
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CountryControllerFunctionalTest extends BookSalesOnlineContainerTest {
    
    @Autowired private CountryRepository countryRepository;
    private int createdCountryId = 0;
    
    @BeforeAll
    @Transactional
    public void init() {
        // Setup compartilhado para todos os testes
        countryRepository.save(argentina);
        countryRepository.save(brazil);
        countryRepository.save(chile);
    }
    
    @Test
    @Order(1)
    @Transactional
    public void should_create() {
        // Transação isolada por teste
    }
    
    @Test
    @Order(2)
    @Transactional
    public void should_update() {
        // Dados do @BeforeAll ainda disponíveis
        // Mas modificações não afetam próximos testes
    }
}
```

**Anotações**:

| Anotação | Escopo | Quando Executado | Uso |
|----------|--------|------------------|-----|
| `@BeforeAll` | Classe | Uma vez (antes de tudo) | Setup compartilhado |
| `@BeforeEach` | Teste | Antes de cada teste | Setup repetitivo |
| `@AfterEach` | Teste | Depois de cada teste | Limpeza |
| `@AfterAll` | Classe | Uma vez (depois de tudo) | Cleanup final |
| `@Transactional` | Método | Rollback automático | Isolamento |

**Fluxo**:
```
┌─ @BeforeAll (init)
│  └─ Salva argentina, brazil, chile
├─ @Test @Order(1) @Transactional
│  └─ Cria USA (não afeta próximos)
├─ @Test @Order(2) @Transactional
│  └─ Vê argentina, brazil, chile (argentina, brazil, chile, USA ❌)
└─ @AfterAll
```

---

### 4.4 Ciclo de Vida de Testes E2E

```
1. BookSalesOnlineContainerTest carregado
   ├─ PostgreSQL container inicia
   ├─ Redis container inicia
   └─ Spring context carregado

2. CountryControllerFunctionalTest inicia
   ├─ @BeforeAll: init() executa
   │  └─ Insere argentina, brazil, chile
   │
   ├─ @Test @Order(1)
   │  ├─ Inicia transação
   │  ├─ Testa criação de USA
   │  └─ Rollback (USA não persiste)
   │
   ├─ @Test @Order(2)
   │  ├─ Inicia transação
   │  ├─ Vê argentina, brazil, chile (+ USA de @BeforeAll)
   │  └─ Rollback
   │
   ├─ @Test @Order(3)
   │  └─ ...

3. Containers limpam automaticamente
```

---

## 5. Padrão de Assertions

### 5.1 Framework de Assertions: AssertJ

**Característica**: Assertions fluentes, legíveis

#### Pattern Básico
```java
import static org.assertj.core.api.Assertions.*;

assertThat(valor)
    .isEqualTo(esperado)
    .isNotNull()
    .isInstanceOfAny(Integer.class);
```

---

### 5.2 Assertions Comuns Encontradas

#### a) Validação de Valores
```java
@Test
public void should_create_country() {
    var country = new Country(1, "Brazil", "Brazilian");
    
    assertThat(country.getId()).isEqualTo(1);
    assertThat(country.getName()).isEqualTo("Brazil");
    assertThat(country.getNationality()).isNotBlank();
}
```

#### b) Validação de Tipos
```java
@Test
public void should_be_integer() {
    var country = new CountryDto();
    country.setId(1);
    
    assertThat(country.getId()).isInstanceOfAny(Integer.class);
}
```

#### c) Validação de Coleções
```java
@Test
public void should_have_countries() {
    var countries = countryRepository.getAll(0, 20);
    
    assertThat(countries)
        .hasSize(3)
        .isNotEmpty()
        .allMatch(c -> c.getName() != null);
}
```

#### d) Validação de Objetos Recursivos (importante!)
```java
@Test
public void should_map_dto_to_domain() {
    var dto = new CountryDto("name", "gentilic");
    dto.setId(1);
    
    var expected = new Country(1, "name", "gentilic");
    var actual = CountryDtoMapper.toDomain(dto);
    
    assertThat(actual)
        .usingRecursiveComparison()  // ← Compara todos os atributos aninhados
        .isEqualTo(expected);
}
```

#### e) Validação de HTTP Response
```java
@Test
public void should_create_successfully() {
    var response = restClientTesting.post(CountryDto.class, "countries", usa);
    
    assertThat(response.getStatusCode())
        .isEqualTo(HttpStatus.CREATED);
    
    var body = response.getBody();
    assertThat(body)
        .isNotNull()
        .hasFieldOrProperty("id")
        .hasFieldOrProperty("name");
}
```

---

### 5.3 Assertions com Exceptions

**JUnit 5**:
```java
@Test
public void should_throw_validation_exception() {
    var invalidPublisher = new Publisher(0, "", "history", null);
    
    assertThrows(ValidationException.class, () -> 
        createPublisherUseCase.execute(invalidPublisher)
    );
}
```

**Sem Exception**:
```java
@Test
public void should_not_throw() {
    assertDoesNotThrow(() -> {
        var country = new Country(1, "name", "gentilic");
        countryRepository.remove(country);
    });
}
```

---

### 5.4 Padrão AAA (Arrange-Act-Assert)

Todos os testes seguem este padrão:

```java
@Test
public void should_create_country() {
    // === ARRANGE (Setup) ===
    var usa = new CountryDto("usa_a", "usa_a");
    var expected = new CountryDto("usa_a", "usa_a");
    
    // === ACT (Executar) ===
    var response = restClientTesting.post(CountryDto.class, "countries", usa);
    var actual = response.getBody();
    
    // === ASSERT (Validar) ===
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(actual.getName()).isEqualTo(expected.getName());
}
```

**Benefícios**:
- Estrutura clara e legível
- Fácil identificar o que falha
- Facilita manutenção

---

## 6. Critérios de Qualidade de um Bom Teste

### 6.1 Características de um Bom Teste (FIRST)

#### ✅ F - Fast (Rápido)
- Unitários: < 1ms
- Integração: < 100ms
- E2E: < 500ms

**Exemplos**:
```java
// ✅ BOM - Sem I/O, sem Spring
@Test
public void test() {
    var entity = new CountryEntity(1, "Brazil", "Brazilian");
    assertThat(entity.getId()).isEqualTo(1);
}

// ❌ RUIM - Carrega Spring, acessa BD
@SpringBootTest
public class SlowTest { ... }
```

#### ✅ I - Isolated (Isolado)
- Nenhuma dependência de testes anteriores
- Cada teste é independente
- Uso de fixtures locais

**Exemplos**:
```java
// ✅ BOM - Dados locais, isolado
@Test
public void test1() {
    var country = new Country(1, "Brazil", "Brazilian");
    assertThat(country.getName()).isEqualTo("Brazil");
}

@Test
public void test2() {
    var country = new Country(1, "USA", "American");
    assertThat(country.getName()).isEqualTo("USA");
    // test2 não depende de test1
}

// ❌ RUIM - Depende de ordem
@Test @Order(1)
public void test1() {
    countryRepository.save(country);
}

@Test @Order(2)
public void test2() {
    var country = countryRepository.getById(1);  // Depende de test1!
}
```

#### ✅ R - Repeatable (Repetível)
- Resultado determinístico
- Sem dependências de sistema, hora, etc.

**Exemplos**:
```java
// ✅ BOM - Determinístico
@Test
public void test() {
    assertThat(2 + 2).isEqualTo(4);
}

// ❌ RUIM - Não determinístico
@Test
public void test() {
    var now = LocalDateTime.now();
    assertThat(now.getYear()).isEqualTo(2026);  // Falha em 2027!
}
```

#### ✅ S - Self-Checking (Auto-verificável)
- Assertions claras
- Sem output visual
- Sem verificações manuais

**Exemplos**:
```java
// ✅ BOM - Assertion clara
@Test
public void test() {
    var result = countryRepository.getById(1);
    assertThat(result.getName()).isEqualTo("Brazil");
}

// ❌ RUIM - Requer verificação manual
@Test
public void test() {
    var result = countryRepository.getById(1);
    System.out.println("Result: " + result);  // Verificar console manualmente!
}
```

#### ✅ T - Thorough (Abrangente)
- Cobre happy path + error cases
- Testa boundary conditions
- Testa comportamentos críticos

**Exemplos**:
```java
// ✅ BOM - Happy path + error case
@Test
public void should_create_country() {
    var result = createCountryUseCase.execute(validCountry);
    assertThat(result.getId()).isGreaterThan(0);
}

@Test
public void should_not_create_invalid_country() {
    assertThrows(ValidationException.class, () ->
        createCountryUseCase.execute(invalidCountry)
    );
}

// ❌ RUIM - Apenas happy path
@Test
public void test() {
    var result = createCountryUseCase.execute(validCountry);
    assertThat(result).isNotNull();
}
```

---

### 6.2 Nomenclatura de Testes (Behavior-Driven)

**Padrão**: `should_[comportamento]_[cenário]`

```java
// ✅ BOM - Claro o que é testado
@Test
public void should_create_a_country_successfully() { ... }

@Test
public void should_not_create_country_when_nationality_is_blank() { ... }

@Test
public void should_return_all_countries_paginated() { ... }

// ❌ RUIM - Vago
@Test
public void testCountry() { ... }

@Test
public void test1() { ... }
```

**Benefício**: Documentação viva do comportamento esperado

---

### 6.3 Assertions Significativas

**Regra**: Cada assertion deve validar algo importante

```java
// ✅ BOM - Assertions significativas
@Test
public void should_map_country_correctly() {
    var dto = new CountryDto("Brazil", "Brazilian");
    dto.setId(1);
    
    var domain = CountryDtoMapper.toDomain(dto);
    
    assertThat(domain.getId()).isEqualTo(1);       // Valida ID
    assertThat(domain.getName()).isEqualTo("Brazil");         // Valida nome
    assertThat(domain.getNationality()).isEqualTo("Brazilian");  // Valida nacionalidade
}

// ❌ RUIM - Assertions triviais
@Test
public void test() {
    var country = new Country(1, "Brazil", "Brazilian");
    assertThat(country).isNotNull();  // Óbvio!
    assertThat(country.getId()).isGreaterThan(0);  // Vago
}
```

---

### 6.4 Cobertura de Casos de Teste

| Tipo | Quantidade Típica | Exemplo |
|------|-------------------|---------|
| **Happy Path** | 1-2 | Criar com dados válidos |
| **Validation** | 2-3 | Dados inválidos, campos vazios |
| **Boundary** | 1-2 | ID = 0, lista vazia, max value |
| **Error Cases** | 1-2 | EntityNotFound, PermissionDenied |

**Exemplo Completo**:
```java
public class CreatePublisherUseCaseImplTest {
    
    // Happy Path
    @Test
    public void should_create_publisher_when_country_exists() {
        var country = new Country(1, "Brazil", "Brazilian");
        var publisher = new Publisher(0, "name", "history", country);
        
        when(repositoryMediator.getQuery(Country.class)).thenReturn(countryQuery);
        when(countryQuery.getById(1)).thenReturn(country);
        
        var result = createPublisherUseCase.execute(publisher);
        assertThat(result.getId()).isGreaterThan(0);
    }
    
    // Error Case
    @Test
    public void should_throw_when_country_not_found() {
        var publisher = new Publisher(0, "name", "history", 
            new Country(1, "name", "gentilic"));
        
        when(repositoryMediator.getQuery(Country.class)).thenReturn(countryQuery);
        when(countryQuery.getById(1)).thenReturn(null);
        
        assertThrows(ValidationException.class, () ->
            createPublisherUseCase.execute(publisher)
        );
    }
}
```

---

### 6.5 Métricas de Qualidade

#### a) Cobertura de Código
```bash
./mvnw clean verify
open target/site/jacoco/index.html
```

**Alvo**:
- Classes: > 80%
- Methods: > 75%
- Lines: > 70%

#### b) Mutation Score (PIT)
```bash
./mvnw pitest:mutationCoverage
open target/pit-reports/index.html
```

**Métrica**: Percentual de "mutações" detectadas por testes

**Alvo**: > 70% (testes conseguem detectar mudanças)

#### c) Teste por Feature
```
- Unitários: ~70%
- Integração: ~20%
- E2E: ~10%
```

---

### 6.6 Checklist de Qualidade

Antes de commitar um teste, validar:

- [ ] **Nome claro**: `should_[comportamento]_[cenário]`
- [ ] **Padrão AAA**: Arrange, Act, Assert separados
- [ ] **Isolado**: Sem dependência de testes anteriores
- [ ] **Rápido**: Executa em < 100ms (unit/integration)
- [ ] **Determinístico**: Resultado previsível
- [ ] **Uma responsabilidade**: Testa uma coisa
- [ ] **Sem mocks desnecessários**: Apenas o essencial
- [ ] **Assertions significativas**: Valida o importante
- [ ] **Sem código duplicado**: Reutilizar fixtures
- [ ] **Funciona em CI/CD**: Sem dependências de máquina

---

## 7. Anti-Padrões em Testes

### ❌ Testes Compartilhando Estado

```java
// ❌ RUIM
public class BadTest {
    private static Country country;  // Compartilhado entre testes!
    
    @BeforeAll
    public static void setup() {
        country = new Country(0, "Brazil", "Brazilian");
    }
    
    @Test
    public void test1() {
        country.setId(1);  // Modifica estado compartilhado
    }
    
    @Test
    public void test2() {
        assertThat(country.getId()).isEqualTo(0);  // Falha!
    }
}
```

**Solução**:
```java
// ✅ BOM
@Test
public void test1() {
    var country = new Country(0, "Brazil", "Brazilian");
    country.setId(1);
}

@Test
public void test2() {
    var country = new Country(0, "Brazil", "Brazilian");
    assertThat(country.getId()).isEqualTo(0);
}
```

---

### ❌ Testes sem Assertions (Test Doubles)

```java
// ❌ RUIM
@Test
public void test() {
    countryRepository.save(new Country(0, "Brazil", "Brazilian"));
    // Nenhuma assertion! O que é validado?
}
```

**Solução**:
```java
// ✅ BOM
@Test
public void should_save_country() {
    var saved = countryRepository.save(new Country(0, "Brazil", "Brazilian"));
    assertThat(saved.getId()).isGreaterThan(0);
}
```

---

### ❌ Testes Acoplados à Implementação

```java
// ❌ RUIM - Verifica chamadas internas
@Test
public void test() {
    var result = createCountryUseCase.execute(country);
    
    verify(mediator).getCommand(Country.class);  // Testa implementação!
    verify(countryCommand).save(any());
}
```

**Solução**:
```java
// ✅ BOM - Valida resultado
@Test
public void should_create_country() {
    var result = createCountryUseCase.execute(country);
    assertThat(result.getId()).isGreaterThan(0);
}
```

---

## 8. Sumário: O que Torna um Teste "Bom" neste Projeto

| Critério | Descrição | Exemplo |
|----------|-----------|---------|
| **Isolado** | Sem dependência de testes anteriores | Dados criados localmente |
| **Rápido** | Executa em < 100ms (unit/integration) | Sem I/O, sem Spring |
| **Legível** | Nome claro e padrão AAA | `should_create_country_successfully` |
| **Determinístico** | Sempre passa ou falha | Sem `LocalDateTime.now()` |
| **Uma responsabilidade** | Testa uma coisa | Um comportamento por teste |
| **Abrangente** | Happy path + error cases | Positivos + exceções |
| **Significativo** | Assertions validam o importante | Não `assertThat(x).isNotNull()` |
| **Isolado de implementação** | Testa "o quê", não "como" | Assertions, não verify |
| **Sem code smell** | Sem duplicação, sem fixtures compartilhadas | DRY principle |
| **Mantível** | Fácil de modificar | Sem acoplamento |

---

## 9. Próximas Melhorias

### 🔲 Curto Prazo
1. Implementar testes de Arquitetura (ArchUnit)
2. Aumentar cobertura de mutation tests
3. Adicionar testes para edge cases

### 🔲 Médio Prazo
4. Implementar Custom Assertions para domínio
5. Adicionar testes de performance
6. Implementar Data-Driven Tests (Parameterized)

### 🔲 Longo Prazo
7. Testes de contrato (Contract Testing) com clientes da API
8. Testes de carga (Load Testing)
9. Testes de segurança (OWASP)

---

## Referências

- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [AssertJ Documentation](https://assertj.github.io/assertj-core-features-highlight.html)
- [TestContainers User Guide](https://www.testcontainers.org/)
- [Behavior-Driven Development](https://en.wikipedia.org/wiki/Behavior-driven_development)
- [Test Driven Development](https://en.wikipedia.org/wiki/Test-driven_development)

---

**Data de Criação**: 2026-02-07  
**Status**: Documento de Referência  
**Mantido por**: Equipe de QA

