# Mapa de Contexto do Sistema - BookSalesOnline
## Foco: Automação de Testes

**Data**: 2026-02-07  
**Versão**: 1.0  
**Status**: Referência para Engenharia de Testes

---

## 📊 Visão Geral da Topologia

```
┌─────────────────────────────────────────────────────────────┐
│                   CLIENTS (HTTP)                            │
│                  (Testa via REST)                           │
└────────────────────┬────────────────────────────────────────┘
                     │
         ┌───────────▼───────────┐
         │   ADAPTERS - Controllers
         │  (v1/CountryController)
         │   🔴 Direct Test: YES
         │   Method: @SpringBootTest
         └───────────┬───────────┘
                     │
    ┌────────────────▼────────────────┐
    │  APPLICATION - Use Cases         │
    │ (CreateEntityUseCaseImpl)         │
    │ 🟡 Direct Test: UNIT ONLY        │
    │ Method: @ExtendWith(MockitoExt)  │
    └────────────────┬────────────────┘
                     │
    ┌────────────────┴─────────────────────┐
    │   APPLICATION - Mediators            │
    │   (UseCaseMediator, RepositoryMediator)
    │   🔴 Avoid: Use DI instead          │
    └────────────────┬─────────────────────┘
                     │
    ┌────────────────▼────────────────┐
    │  ADAPTERS - Repositories         │
    │ (CountryRepository)              │
    │ 🟡 Direct Test: MOCK JpaRepository
    │ Method: @Mock + @InjectMocks     │
    └────────────────┬────────────────┘
                     │
    ┌────────────────▼────────────────┐
    │  ADAPTERS - Data (JpaRepository) │
    │ (CountryData interface)          │
    │ 🔴 Direct Test: NO               │
    │ Method: MOCK in tests            │
    └────────────────┬────────────────┘
                     │
    ┌────────────────▼────────────────┐
    │  INFRASTRUCTURE                  │
    │ (PostgreSQL, Redis, AWS S3)     │
    │ ✅ Real in E2E: TestContainers  │
    └─────────────────────────────────┘
```

---

## 🟢 Módulo 1: Domain (Camada de Domínio)

### 1.1 Descrição
Núcleo da aplicação contendo entidades de negócio puras, sem dependências técnicas.

### 1.2 Componentes Principais
- **Entidades**: `Country`, `Publisher`, `Publication`, `Language`, `PublicationImage`
- **Base**: `BaseDomain` (id + name)
- **Exceções**: `ValidationException`, `RemoveException`
- **Enums**: Tipos específicos de domínio

### 1.3 Responsabilidade
```
✅ DEVE:
  - Encapsular dados de negócio
  - Validar com @NotBlank, @NotNull
  - Implementar comportamentos de negócio
  - Definir exceções de domínio
  - Ser imutável (exceto setters de Lombok)

❌ NÃO DEVE:
  - Depender de Spring
  - Acessar banco de dados
  - Fazer HTTP requests
  - Serializar/desserializar JSON diretamente
  - Conhecer infraestrutura
```

### 1.4 Dependências Diretas
- ✅ Lombok (anotações)
- ✅ javax.validation (validações)
- ❌ Nenhuma outra dependência

### 1.5 Pontos de Extensão para Testes
```java
// ✅ COMO TESTAR DOMAIN

// 1. Instantiation Tests (simples)
@Test
public void should_create_country() {
    var country = new Country(1, "Brazil", "Brazilian");
    assertThat(country.getId()).isEqualTo(1);
}

// 2. Validation Tests (regras de negócio)
@Test
public void should_validate_nationality() {
    var country = new Country(1, "Brazil", "");
    // Domain tem @NotBlank em nationality
    // Será validado pelo use case ou controller
}

// 3. Behavior Tests (métodos de negócio)
@Test
public void should_check_market_validity() {
    var country = new Country(1, "Brazil", "Brazilian");
    // Se houver método: boolean isValidForMarket(Publication pub)
    var valid = country.isValidForMarket(publication);
    assertThat(valid).isTrue();
}
```

### 1.6 Acessos Proibidos em Testes
```java
// ❌ NUNCA faça isto:
assertThat(country)
    .isNotNull()              // Óbvio
    .hasFieldOrProperty("id"); // Testando atributo privado

// ❌ NUNCA acesse reflexão
country.getClass().getDeclaredField("id");

// ❌ NUNCA use Spring Context para criar domain
@SpringBootTest
public class DomainTest {
    @Autowired Country country;  // NÃO!
}

// ✅ SEMPRE crie dados localmente
var country = new Country(1, "Brazil", "Brazilian");
```

---

## 🟡 Módulo 2: Application - Use Cases (Camada de Aplicação)

### 2.1 Descrição
Orquestra a lógica de negócio através de use cases. Coordena domain, repositories e serviços externos.

### 2.2 Componentes Principais
**Genéricos**:
- `CreateEntityUseCaseImpl` - Cria entidade genérica
- `GetAllEntitiesUseCaseImpl` - Lista paginada
- `GetEntityByIdUseCaseImpl` - Busca por ID
- `UpdateEntityUseCaseImpl` - Atualiza entidade
- `RemoveEntityUseCaseImpl` - Remove entidade

**Específicos**:
- `publisher/CreatePublisherUseCaseImpl` - Valida País antes de criar
- `country/RemoveCountryUseCase` - Lógica específica de remoção
- `publication/CreatePublicationImageUseCaseImpl` - Upload para S3

### 2.3 Responsabilidade
```
✅ DEVE:
  - Aplicar regras de negócio transversais
  - Orquestrar múltiplos repositórios
  - Validar pré-condições
  - Lançar exceções apropriadas
  - Coordenar com mediators (atual) ou DI (futuro)
  - Ser testável sem Spring

❌ NÃO DEVE:
  - Conhecer Controllers ou HTTP
  - Chamar JpaRepository diretamente
  - Acessar infraestrutura específica (S3, Redis)
  - Serializar/desserializar dados
  - Conter lógica de persistência
```

### 2.4 Dependências Diretas
- ✅ Domain entities
- ✅ Ports/Interfaces (DataQuery, DataCommand)
- ✅ RepositoryMediator (⚠️ Antipadrão, será refatorado)
- ✅ Lombok
- ✅ Spring @Service

### 2.5 Pontos de Extensão para Testes

#### a) Use Case Genérico
```java
@ExtendWith(MockitoExtension.class)
public class CreateEntityUseCaseImplTest {
    
    @Mock private RepositoryMediator mediator;
    @Mock private DataCommand<Country> countryCommand;
    @InjectMocks private CreateEntityUseCaseImpl useCase;
    
    @Test
    public void should_create_entity_successfully() throws NoSuchMethodException {
        // ARRANGE
        var input = new Country(0, "Brazil", "Brazilian");
        when(mediator.getCommand(Country.class))
            .thenReturn(countryCommand);
        when(countryCommand.save(input))
            .then(inv -> {
                input.setId(1);
                return input;
            });
        
        // ACT
        var result = useCase.execute(Country.class, input);
        
        // ASSERT
        assertThat(result.getId()).isEqualTo(1);
    }
}
```

#### b) Use Case Específico (com Validação)
```java
@ExtendWith(MockitoExtension.class)
public class CreatePublisherUseCaseImplTest {
    
    @Mock private DataQuery<Country> countryQuery;
    @Mock private RepositoryMediator mediator;
    @InjectMocks private CreatePublisherUseCaseImpl useCase;
    
    @Test
    public void should_create_when_country_exists() throws NoSuchMethodException {
        // Testa regra de negócio: país deve existir
        var country = new Country(1, "Brazil", "Brazilian");
        var publisher = new Publisher(0, "Editora", "Historia", country);
        
        when(mediator.getQuery(Country.class))
            .thenReturn(countryQuery);
        when(countryQuery.getById(1))
            .thenReturn(country);
        
        var result = useCase.execute(publisher);
        assertThat(result.getId()).isGreaterThan(0);
    }
    
    @Test
    public void should_not_create_when_country_missing() throws NoSuchMethodException {
        // Testa erro esperado
        var publisher = new Publisher(0, "Editora", "Historia", 
            new Country(999, "Inexistent", "Invalid"));
        
        when(mediator.getQuery(Country.class))
            .thenReturn(countryQuery);
        when(countryQuery.getById(999))
            .thenReturn(null);
        
        assertThrows(ValidationException.class, () ->
            useCase.execute(publisher)
        );
    }
}
```

### 2.6 Acessos Proibidos em Testes
```java
// ❌ NUNCA acesse JpaRepository diretamente
@Test
public void bad_test() {
    var result = countryData.findById(1);  // NÃO!
}

// ❌ NUNCA use mediator.get() em testes (refatorar)
@Test
public void bad_test() {
    var useCase = mediator.get(CreateEntityUseCase.class);  // Antipadrão
}

// ❌ NUNCA use @SpringBootTest em teste unitário
@SpringBootTest
public class CreateEntityUseCaseImplTest { ... }  // Muito lento!

// ✅ SEMPRE mock apenas o necessário
@Mock private RepositoryMediator mediator;
@InjectMocks private CreateEntityUseCaseImpl useCase;
```

---

## 🟡 Módulo 3: Application - Mediators (Antipadrão Atual)

### 3.1 Descrição
**AVISO**: Este é um antipadrão (Service Locator). Será refatorado para Dependency Injection.

Mediators são registries estáticos que resolvem dependências em tempo de execução.

### 3.2 Componentes Principais
- `UseCaseMediatorImpl` - Registry de use cases
- `RepositoryMediatorImpl` - Registry de repositórios

### 3.3 Responsabilidade (Atual)
```
✅ FOCA:
  - Injetar e registrar todos os use cases
  - Injetar e registrar todos os repositórios
  - Fornecer lookup por Class<T> em runtime

⚠️ PROBLEMA:
  - Reflection em runtime
  - Erros só aparecem na execução
  - Acoplamento implícito
  - IDE não consegue refatorar corretamente
  - Necessário throws NoSuchMethodException
```

### 3.4 Dependências Diretas
- ✅ Todos os use cases
- ✅ Todos os repositórios
- ✅ Spring @Component

### 3.5 Pontos de Extensão para Testes

#### Como testar Mediator
```java
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UseCaseMediatorImplTest {
    
    private UseCaseMediator mediator;
    
    @BeforeAll
    public void init() {
        // Mock todos os use cases
        mediator = new UseCaseMediatorImpl(
            mock(GetAllEntitiesUseCase.class),
            mock(GetEntityByIdUseCase.class),
            mock(CreateEntityUseCase.class),
            // ...
        );
    }
    
    @Test
    public void should_get_usecase() throws NoSuchMethodException {
        var useCase = mediator.get(GetAllEntitiesUseCase.class);
        assertThat(useCase).isNotNull();
    }
}
```

#### Como testar sem Mediator (Futuro)
```java
// Quando refatorarmos para Dependency Injection:
@ExtendWith(MockitoExtension.class)
public class ControllerTest {
    
    @Mock private CreateEntityUseCase createUseCase;
    @Mock private GetAllEntitiesUseCase getAllUseCase;
    @InjectMocks private CountryController controller;
    
    @Test
    public void should_create() {
        // Sem mediator!
        when(createUseCase.execute(...)).thenReturn(...);
        
        var result = controller.create(...);
        assertThat(result).isNotNull();
    }
}
```

### 3.6 Acessos Proibidos em Testes
```java
// ⚠️ EVITE (funciona, mas é antipadrão):
@Test
public void test() {
    var mediator = new UseCaseMediatorImpl(...);
    var useCase = mediator.get(CreateEntityUseCase.class);  // Type unsafe!
}

// ✅ MELHOR (mock direto):
@Mock private CreateEntityUseCase createUseCase;
@InjectMocks private Controller controller;
```

---

## 🔴 Módulo 4: Adapters - Controllers (Entrada HTTP)

### 4.1 Descrição
Recebe requisições HTTP, valida entrada, chama use cases, formata resposta.

### 4.2 Componentes Principais
- `CountryController` - `/countries` endpoints
- `PublisherController` - `/publishers` endpoints
- `PublicationController` - `/publications` endpoints
- `LanguageController` - `/languages` endpoints
- `ImageController` - `/images` endpoints

### 4.3 Responsabilidade
```
✅ DEVE:
  - Receber requisições HTTP
  - Validar input (@Valid, @PathVariable)
  - Mapear DTO → Domain via mappers
  - Chamar use cases apropriados
  - Mapear resultado → DTO
  - Retornar status HTTP correto (200, 201, 404, etc.)
  - Implementar cache quando apropriado

❌ NÃO DEVE:
  - Contém lógica de negócio
  - Acessar repository diretamente
  - Conhecer detalhes de persistência
  - Fazer transformações complexas
```

### 4.4 Dependências Diretas
- ✅ UseCaseMediator (será refatorado)
- ✅ DTO Mappers
- ✅ Spring @RestController, @GetMapping, etc.
- ✅ Use Cases (via mediator)

### 4.5 Pontos de Extensão para Testes

#### Teste Funcional Completo (E2E)
```java
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CountryControllerFunctionalTest extends BookSalesOnlineContainerTest {
    
    @Autowired private RestClientTesting restClientTesting;
    @Autowired private CountryRepository countryRepository;
    
    @BeforeAll
    @Transactional
    public void init() {
        // Setup dados compartilhados
        countryRepository.save(brazil);
        countryRepository.save(argentina);
    }
    
    @Test
    @Order(1)
    public void should_create_country() {
        // ARRANGE
        var dto = new CountryDto("Chile", "Chilean");
        
        // ACT
        var response = restClientTesting.post(CountryDto.class, "countries", dto);
        
        // ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        var body = response.getBody();
        assertThat(body.getName()).isEqualTo("Chile");
    }
    
    @Test
    @Order(2)
    public void should_get_all_countries() {
        // Dados do @BeforeAll + dados criados em test 1
        var response = restClientTesting.get(CountryDto[].class, "countries");
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasLengthGreaterThanOrEqualTo(2);
    }
}
```

#### Teste Unitário com Mocks (Futuro)
```java
@ExtendWith(MockitoExtension.class)
public class CountryControllerUnitTest {
    
    @Mock private CreateEntityUseCase createUseCase;
    @Mock private GetAllEntitiesUseCase getAllUseCase;
    @InjectMocks private CountryController controller;
    
    // Sem Spring, sem TestContainers, < 1ms
    
    @Test
    public void should_call_usecase() {
        var dto = new CountryDto("Brazil", "Brazilian");
        var domain = new Country(1, "Brazil", "Brazilian");
        
        when(createUseCase.execute(Country.class, any()))
            .thenReturn(domain);
        
        var result = controller.create(dto);
        assertThat(result.getId()).isEqualTo(1);
    }
}
```

### 4.6 Acessos Proibidos em Testes
```java
// ❌ NUNCA acesse repository diretamente
@Test
public void bad_test() {
    var country = countryRepository.save(new Country(...));  // Contorna controller!
}

// ❌ NUNCA skip validação de entrada
@Test
public void bad_test() {
    var dto = new CountryDto("", "");  // Inválido, mas controller não valida
    var result = controller.create(dto);
}

// ❌ NUNCA use mediator diretamente em testes
@Test
public void bad_test() {
    var useCase = controller.mediator.get(...);  // Antipadrão
}

// ✅ SEMPRE use TestRestTemplate ou Rest Assured
var response = restClientTesting.post(CountryDto.class, "countries", dto);
```

---

## 🟡 Módulo 5: Adapters - Repositories (Persistência)

### 5.1 Descrição
Implementa interfaces de ports (DataQuery, DataCommand). Coordena JpaRepository e mappers para abstrair persistência.

### 5.2 Componentes Principais
- `CountryRepository` - implements DataQuery<Country>, DataCommand<Country>
- `PublisherRepository`
- `PublicationRepository`
- `LanguageRepository`
- `ImageRepository`

### 5.3 Responsabilidade
```
✅ DEVE:
  - Implementar DataQuery + DataCommand
  - Mapear Domain ↔ JpaEntity
  - Delegar ao *Data (JpaRepository)
  - Aplicar paginação
  - Aplicar filtros específicos

❌ NÃO DEVE:
  - Conter lógica de negócio (isso é use case)
  - Acessar Controllers
  - Fazer transformações complexas
  - Conhecer HTTP ou DTOs
```

### 5.4 Dependências Diretas
- ✅ *Data (JpaRepository)
- ✅ *EntityMapper
- ✅ Domain entities
- ✅ Spring @Repository

### 5.5 Pontos de Extensão para Testes

#### Teste de Repository (Mock JpaRepository)
```java
@ExtendWith(MockitoExtension.class)
public class CountryRepositoryTest {
    
    @Mock private CountryData countryData;  // Mock JpaRepository
    @InjectMocks private CountryRepository repository;
    
    // Sem Spring, sem banco real, mas testa mapping
    
    @Test
    public void should_get_all_with_mapping() {
        // ARRANGE
        var entities = Arrays.asList(
            new CountryEntity(1, "Brazil", "Brazilian"),
            new CountryEntity(2, "USA", "American")
        );
        when(countryData.findAll(any(PageRequest.class)))
            .thenReturn(new PageImpl<>(entities));
        
        // ACT
        var result = repository.getAll(0, 20);
        
        // ASSERT - Valida mapping
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Brazil");
        assertThat(result.get(0).getNationality()).isEqualTo("Brazilian");
    }
    
    @Test
    public void should_save_entity() {
        // ARRANGE
        var input = new Country(0, "Brazil", "Brazilian");
        var savedEntity = new CountryEntity(1, "Brazil", "Brazilian");
        when(countryData.save(any(CountryEntity.class)))
            .thenReturn(savedEntity);
        
        // ACT
        var result = repository.save(input);
        
        // ASSERT
        assertThat(result.getId()).isEqualTo(1);
    }
}
```

### 5.6 Acessos Proibidos em Testes
```java
// ❌ NUNCA teste JpaRepository direto
@Test
public void bad_test() {
    var entity = countryData.findById(1);  // Testa Spring Data, não sua lógica!
}

// ❌ NUNCA acesse banco real sem TestContainers
@Test
public void bad_test() {
    // Sem @SpringBootTest, sem TestContainers
    var entity = countryData.findById(1);  // Falha!
}

// ❌ NUNCA skip validação de mapper
@Test
public void bad_test() {
    var entity = new CountryEntity(1, "Brazil", "Brazilian");
    var result = repository.getById(1);
    assertThat(result).isNotNull();  // Não valida mapping!
}

// ✅ SEMPRE valide transformação Domain ↔ Entity
assertThat(result.getId()).isEqualTo(expected.getId());
assertThat(result.getName()).isEqualTo(expected.getName());
```

---

## 🔴 Módulo 6: Adapters - Data (JpaRepository)

### 6.1 Descrição
Interface Spring Data que abstrai acesso ao banco PostgreSQL. Não deve ser testada diretamente.

### 6.2 Componentes Principais
- `CountryData extends JpaRepository<CountryEntity, Integer>`
- `PublisherData`
- `PublicationData`
- `LanguageData`
- `ImageData`

### 6.3 Responsabilidade
```
✅ DEVE:
  - Definir operações CRUD padrão
  - Fornecer métodos de query específicos (se necessário)
  - Usar anotações Spring Data

❌ NÃO DEVE:
  - Implementação customizada
  - Lógica de negócio
  - Transformações
```

### 6.4 Dependências Diretas
- ✅ JpaRepository (Spring Data)
- ✅ Entity (anotações @Entity)

### 6.5 Pontos de Extensão para Testes
```java
// ❌ NÃO teste diretamente - deixe para integração no repositório

// ✅ SE precisar testar query customizado:
public interface CountryData extends JpaRepository<CountryEntity, Integer> {
    List<CountryEntity> findByNationality(String nationality);
}

// Teste via Repository:
@Test
public void should_find_by_nationality() {
    var entities = countryData.findByNationality("Brazilian");
    // Mas isso requer BD real, use @SpringBootTest + TestContainers
}
```

### 6.6 Acessos Proibidos em Testes
```java
// ❌ NUNCA teste JpaRepository em teste unitário
@ExtendWith(MockitoExtension.class)
public class CountryDataTest {
    @InjectMocks private CountryData countryData;  // Não pode!
}

// ❌ NUNCA sem TestContainers
@Test
public void test() {
    countryData.findAll();  // Sem contexto, falha!
}

// ✅ SEMPRE teste via Repository (que mocka JpaRepository)
// OU via @SpringBootTest com TestContainers
```

---

## 🟡 Módulo 7: Adapters - Mappers (Transformação de Dados)

### 7.1 Descrição
Converte dados entre camadas sem lógica de negócio. Estático e puro.

### 7.2 Componentes Principais
**DTO Mappers**:
- `CountryDtoMapper` - DTO ↔ Domain
- `PublisherDtoMapper`
- `PublicationDtoMapper`

**Entity Mappers**:
- `CountryEntityMapper` - Entity ↔ Domain
- `PublisherEntityMapper`
- `PublicationEntityMapper`

### 7.3 Responsabilidade
```
✅ DEVE:
  - Converter Domain ↔ DTO (serialização HTTP)
  - Converter Domain ↔ Entity (persistência)
  - Funcionar com listas
  - Ser stateless e thread-safe

❌ NÃO DEVE:
  - Conter lógica de negócio
  - Acessar banco de dados
  - Fazer validações complexas
  - Conhecer Controllers ou Repositories
```

### 7.4 Dependências Diretas
- ✅ Domain entities
- ✅ DTOs ou Entities
- ✅ Java Collections

### 7.5 Pontos de Extensão para Testes

#### Teste de Mapper
```java
public class CountryDtoMapperTest {
    
    @Test
    public void should_map_dto_to_domain() {
        // ARRANGE
        var dto = new CountryDto("Brazil", "Brazilian");
        dto.setId(1);
        
        // ACT
        var domain = CountryDtoMapper.toDomain(dto);
        
        // ASSERT - Valida cada atributo
        assertThat(domain)
            .usingRecursiveComparison()
            .isEqualTo(new Country(1, "Brazil", "Brazilian"));
    }
    
    @Test
    public void should_map_domain_to_dto() {
        // ARRANGE
        var domain = new Country(1, "Brazil", "Brazilian");
        
        // ACT
        var dto = CountryDtoMapper.fromDomain(domain);
        
        // ASSERT
        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getName()).isEqualTo("Brazil");
    }
    
    @Test
    public void should_handle_null() {
        // ARRANGE & ACT
        var result = CountryDtoMapper.toDomain(null);
        
        // ASSERT
        assertThat(result).isNull();
    }
    
    @Test
    public void should_map_collections() {
        // ARRANGE
        var domains = Arrays.asList(
            new Country(1, "Brazil", "Brazilian"),
            new Country(2, "USA", "American")
        );
        
        // ACT
        var dtos = CountryDtoMapper.fromDomain(domains);
        
        // ASSERT
        assertThat(dtos).hasSize(2);
        assertThat(dtos[0].getName()).isEqualTo("Brazil");
    }
}
```

### 7.6 Acessos Proibidos em Testes
```java
// ❌ NUNCA teste mapper com Spring Context
@SpringBootTest
public class CountryDtoMapperTest { ... }

// ❌ NUNCA acesse mapper via reflexão
CountryDtoMapper.class.getDeclaredMethod("toDomain", CountryDto.class);

// ✅ SEMPRE teste método estático direto
var domain = CountryDtoMapper.toDomain(dto);
```

---

## 🟡 Módulo 8: Adapters - Entities (JPA)

### 8.1 Descrição
Modela tabelas do banco de dados. Anotações JPA para mapeamento relacional.

### 8.2 Componentes Principais
- `CountryEntity` - Tabela `country`
- `PublisherEntity` - Tabela `publisher`
- `PublicationEntity` - Tabela `publication`
- `LanguageEntity` - Tabela `language`
- `ImageEntity` - Tabela `image`

### 8.3 Responsabilidade
```
✅ DEVE:
  - Modelar estrutura relacional
  - Usar anotações JPA (@Entity, @Column)
  - Definir relacionamentos (@ManyToOne, @OneToMany)
  - Ser POJO simples com getters/setters

❌ NÃO DEVE:
  - Conter lógica de negócio
  - Validações complexas
  - Transformações
```

### 8.4 Dependências Diretas
- ✅ JPA/Hibernate (anotações)
- ✅ Lombok (opcional)

### 8.5 Pontos de Extensão para Testes

#### Teste de Entity
```java
public class CountryEntityTest {
    
    @Test
    public void should_create_entity_with_all_fields() {
        // ARRANGE & ACT
        var entity = new CountryEntity(1, "Brazil", "Brazilian");
        
        // ASSERT
        assertThat(entity.getId()).isEqualTo(1);
        assertThat(entity.getName()).isEqualTo("Brazil");
        assertThat(entity.getNationality()).isEqualTo("Brazilian");
    }
    
    @Test
    public void should_create_default_entity() {
        // ARRANGE & ACT
        var entity = new CountryEntity();
        
        // ASSERT
        assertThat(entity.getId()).isEqualTo(0);
        assertThat(entity.getName()).isNull();
    }
}
```

### 8.6 Acessos Proibidos em Testes
```java
// ❌ NUNCA teste entity com Spring Context
@SpringBootTest
public class CountryEntityTest { ... }

// ❌ NUNCA use entity em use cases (sempre use domain)
var country = new CountryEntity(...);
createCountryUseCase.execute(country);  // ERRADO!

// ✅ SEMPRE crie entity localmente para teste
var entity = new CountryEntity(1, "Brazil", "Brazilian");
```

---

## 🟢 Módulo 9: Adapters - Storage (AWS S3)

### 9.1 Descrição
Abstrai upload/download de arquivos em AWS S3 ou LocalStack.

### 9.2 Componentes Principais
- `S3StorageService` - implements StorageService
- `AmazonS3Config` - Bean de configuração
- `AwsConfigProperties` - Properties binding

### 9.3 Responsabilidade
```
✅ DEVE:
  - Fazer upload para S3
  - Gerar URLs de acesso
  - Aplicar metadados
  - Abstrair AWS SDK

❌ NÃO DEVE:
  - Validar arquivo (isso é use case)
  - Armazenar localmente
  - Conhecer lógica de negócio
```

### 9.4 Dependências Diretas
- ✅ AWS SDK (AmazonS3Client)
- ✅ Domain (PublicationImage)
- ✅ Spring @Service

### 9.5 Pontos de Extensão para Testes

#### Teste de S3StorageService
```java
@ExtendWith(MockitoExtension.class)
public class S3StorageServiceTest {
    
    @Mock private AmazonS3Client amazonS3Client;
    @InjectMocks private S3StorageService storageService;
    
    @Test
    public void should_upload_image() throws IOException, URISyntaxException {
        // ARRANGE
        var image = new PublicationImage(...);
        var mockUrl = new URL("https://bucket.s3.amazonaws.com/image.jpg");
        
        when(amazonS3Client.putObject(any(PutObjectRequest.class)))
            .thenReturn(new PutObjectResult());
        when(amazonS3Client.getUrl(anyString(), anyString()))
            .thenReturn(mockUrl);
        
        // ACT
        var url = storageService.save("bucket-name", image);
        
        // ASSERT
        assertThat(url).isEqualTo("https://bucket.s3.amazonaws.com/image.jpg");
    }
}
```

#### Teste de Configuração (Futuro)
```java
// Se precisar testar bean de configuração:
@SpringBootTest
public class AwsS3ConfigTest {
    
    @Autowired private AmazonS3Client amazonS3Client;
    
    @Test
    public void should_create_s3_client() {
        assertThat(amazonS3Client).isNotNull();
    }
}
```

### 9.6 Acessos Proibidos em Testes
```java
// ❌ NUNCA faça upload real em testes
@Test
public void bad_test() {
    var image = new PublicationImage(...);
    storageService.save("real-bucket", image);  // Realmente faz upload!
}

// ❌ NUNCA acesse AmazonS3Client direto sem mock
@Test
public void bad_test() {
    amazonS3Client.listBuckets();  // Precisa de AWS credentials
}

// ✅ SEMPRE mock AmazonS3Client
@Mock private AmazonS3Client amazonS3Client;
when(amazonS3Client.putObject(...)).thenReturn(...);
```

---

## 🟢 Módulo 10: Infrastructure - Containers (TestContainers)

### 10.1 Descrição
Fornece infraestrutura isolada para testes E2E: PostgreSQL e Redis em containers Docker.

### 10.2 Componentes Principais
- `BookSalesOnlineContainerTest` - Base class
- `BookSalesOnlineDatabaseContainer` - PostgreSQL 15.2
- `BookSalesOnlineRedisContainer` - Redis 7.0.11

### 10.3 Responsabilidade
```
✅ DEVE:
  - Iniciar PostgreSQL em container
  - Iniciar Redis em container
  - Propagar connection strings
  - Limpar automaticamente após testes
  - Suportar múltiplas classes de teste

❌ NÃO DEVE:
  - Conter dados de teste (isso é no @BeforeAll)
  - Validar lógica (deixa para testes)
  - Depender de ordem de testes
```

### 10.4 Dependências Diretas
- ✅ TestContainers
- ✅ Docker (runtime)
- ✅ junit5 ClassRule

### 10.5 Pontos de Extensão para Testes

#### Como usar TestContainers
```java
public abstract class BookSalesOnlineContainerTest {
    @ClassRule
    public static PostgreSQLContainer<BookSalesOnlineDatabaseContainer> 
        postgreSQLContainer = BookSalesOnlineDatabaseContainer.getInstance();
    
    @ClassRule
    public static GenericContainer<BookSalesOnlineRedisContainer>
        redisContainer = BookSalesOnlineRedisContainer.getInstance();
}

// Subclass automáticamente ganha containers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CountryControllerFunctionalTest extends BookSalesOnlineContainerTest {
    // Containers já estão rodando!
}
```

#### Ciclo de Vida
```
1. Classe de teste inicia
   ├─ BookSalesOnlineContainerTest carregado
   ├─ @ClassRule executa
   │  ├─ PostgreSQL container inicia
   │  ├─ Credenciais propagadas via System.setProperty()
   │  ├─ Redis container inicia
   │  └─ Portas mapeadas dinamicamente
   │
   ├─ Spring Boot Context inicia
   │  ├─ Lê System.setProperty() para BD
   │  ├─ Conecta ao PostgreSQL container
   │  └─ Conecta ao Redis container
   │
   ├─ @BeforeAll executa (setup de dados)
   │  └─ Insere fixtures
   │
   ├─ @Test executa (com @Transactional)
   │  └─ Rollback automático
   │
   ├─ Próximo @Test
   └─ ...

2. Após todos os testes
   ├─ Containers param automaticamente
   ├─ Limpeza de recursos
   └─ Docker cleanup
```

### 10.6 Acessos Proibidos em Testes
```java
// ❌ NUNCA use containers em teste unitário
@ExtendWith(MockitoExtension.class)
public class EntityTest extends BookSalesOnlineContainerTest { ... }

// ❌ NUNCA espere container estar disponível sem @SpringBootTest
public class BadTest extends BookSalesOnlineContainerTest {
    @Autowired private CountryRepository repository;  // Falha!
}

// ❌ NUNCA modifique container após inicialização
@Test
public void bad_test() {
    // Mudar porta, hostname, etc.
    postgreSQLContainer.setPortBinding(...);  // Não funciona!
}

// ✅ SEMPRE use via subclass + @SpringBootTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CountryControllerFunctionalTest extends BookSalesOnlineContainerTest {
    // Containers prontos para usar
}
```

---

## 🔵 Matriz de Teste por Módulo

```
┌─────────────────────┬──────────┬──────────────┬──────────┬────────────────┐
│ Módulo              │ Unit     │ Integration  │ E2E      │ Type           │
├─────────────────────┼──────────┼──────────────┼──────────┼────────────────┤
│ Domain              │ ✅ Sim   │ ❌ Não       │ ❌ Não   │ Instanciação   │
│ Use Cases           │ ✅ Sim   │ ❌ Não       │ ✅ Sim   │ Mock Mediator  │
│ Mediators           │ ✅ Sim   │ ❌ Não       │ ❌ Não   │ Mock use cases │
│ Controllers         │ ❌ Não   │ ❌ Não       │ ✅ Sim   │ @SpringBootTest│
│ Repositories        │ ✅ Sim   │ ✅ Sim       │ ✅ Sim   │ Mock JpaRepos  │
│ Data (JpaRepository)│ ❌ Não   │ ❌ Não       │ ✅ Sim   │ Via Repository │
│ Mappers             │ ✅ Sim   │ ❌ Não       │ ✅ Sim   │ Direto         │
│ Entities            │ ✅ Sim   │ ❌ Não       │ ✅ Sim   │ Instanciação   │
│ Storage Service     │ ✅ Sim   │ ❌ Não       │ ✅ Sim   │ Mock AWS       │
│ Containers          │ ❌ Não   │ ❌ Não       │ ✅ Sim   │ TestContainers │
└─────────────────────┴──────────┴──────────────┴──────────┴────────────────┘
```

---

## 🎯 Estratégia de Teste por Camada

### Domain
```
Tipo: UNIT
Framework: JUnit 5 + AssertJ
Velocidade: < 1ms
Isolamento: Total (sem Spring)
Exemplo: CountryEntityTest, CountryDomainTest
```

### Application (Use Cases)
```
Tipo: UNIT
Framework: JUnit 5 + Mockito + AssertJ
Velocidade: 1-10ms
Isolamento: Mock de mediator
Exemplo: CreateEntityUseCaseImplTest, CreatePublisherUseCaseImplTest
```

### Adapters (Controllers)
```
Tipo: E2E/FUNCTIONAL
Framework: Spring Boot Test + TestContainers + Rest Assured
Velocidade: 100-500ms por teste
Isolamento: TestContainers reais
Exemplo: CountryControllerFunctionalTest, PublisherControllerFunctionalTest
```

### Adapters (Repositories)
```
Tipo: UNIT (com Mock JpaRepository)
Framework: JUnit 5 + Mockito + AssertJ
Velocidade: 1-10ms
Isolamento: Mock de JpaRepository
Exemplo: CountryRepositoryTest, PublisherRepositoryTest
```

### Adapters (Mappers)
```
Tipo: UNIT
Framework: JUnit 5 + AssertJ
Velocidade: < 1ms
Isolamento: Total (sem Spring)
Exemplo: CountryDtoMapperTest, CountryEntityMapperTest
```

---

## 📋 Checklist de Teste por Módulo

### Novo Módulo? Use este checklist:

```
[ ] Domain Entity
  [ ] Unit test: instanciação
  [ ] Unit test: validações (@NotBlank, etc)
  [ ] Unit test: comportamentos

[ ] Use Case
  [ ] Unit test: com mock de mediator
  [ ] Unit test: validações de negócio
  [ ] Unit test: erro handling
  [ ] E2E test: via controller

[ ] Repository
  [ ] Unit test: mock JpaRepository
  [ ] Unit test: validar mapping Domain ↔ Entity
  [ ] E2E test: via controller (valida com BD real)

[ ] Controller
  [ ] E2E test: @SpringBootTest + TestContainers
  [ ] E2E test: requisições HTTP reais
  [ ] E2E test: status code correto
  [ ] E2E test: response body correto

[ ] Mapper
  [ ] Unit test: conversão bidirecional
  [ ] Unit test: null handling
  [ ] Unit test: coleções

[ ] Entity
  [ ] Unit test: instanciação
  [ ] Unit test: default values
```

---

## 🚫 Padrões Proibidos

| Padrão | Por quê | Solução |
|--------|---------|---------|
| Acessar `repository` direto em test | Contorna controller | Usar `restClientTesting` |
| `@SpringBootTest` em teste unitário | Muito lento | Usar `@Mock` + `@ExtendWith(MockitoExtension.class)` |
| Mock sem necessidade | Dificulta leitura | Mock apenas dependência real |
| Teste sem assertions | Não valida nada | Adicionar `assertThat(...)` |
| Compartilhar estado entre testes | Testes se influenciam | Criar dados localmente |
| Acessar `mediator.get()` | Antipadrão | Usar injeção direta |
| Banco real em teste unitário | Lento e flaky | Usar mock ou TestContainers |

---

## 🔄 Fluxo de Teste Recomendado

```
1. DESENVOLVIMENTO
   ├─ Escrever unit test (domain/use case)
   ├─ Escrever código
   ├─ Unit test passa
   └─ Refatorar

2. INTEGRAÇÃO
   ├─ Escrever repository test (mock JpaRepository)
   ├─ Escrever mapper test
   ├─ Testes passam
   └─ Validar mapping

3. FUNCIONAL
   ├─ Escrever E2E test (controller)
   ├─ Estender setup (@BeforeAll)
   ├─ Testar fluxo completo com BD real
   ├─ Testes passam
   └─ Validar com TestContainers

4. MERGE
   ├─ Rodar todo test suite
   ├─ Cobertura > 70%
   ├─ Mutation score > 70%
   └─ Green build ✅
```

---

## 📚 Referências Rápidas

**Para testar...**

- **Entity**: `EntityTest.java` - Instanciação local, sem Spring
- **Domain**: `Country.java` - Comportamentos de negócio
- **Use Case**: `CreateEntityUseCaseImplTest.java` - Mock de mediator
- **Repository**: `CountryRepositoryTest.java` - Mock JpaRepository
- **Controller**: `CountryControllerFunctionalTest.java` - @SpringBootTest
- **Mapper**: `CountryDtoMapperTest.java` - Conversão bidirecional
- **Storage**: `S3StorageServiceTest.java` - Mock AmazonS3Client

---

**Data de Criação**: 2026-02-07  
**Maintainer**: Equipe de Arquitetura e QA  
**Última Revisão**: 2026-02-07

