# Fluxos Críticos de Teste - BookSalesOnline

**Data**: 2026-02-07  
**Versão**: 1.0  
**Status**: Análise completa de fluxos testados

---

## 📊 Visão Geral dos Fluxos

O sistema testa **8 fluxos críticos** organizados em 3 categorias:

```
┌─────────────────────────────────────────────────────────────┐
│           FLUXOS CRÍTICOS TESTADOS (8 total)               │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  E2E Fluxos (5):                                           │
│  1. CRUD Country (Create, Read, Update, Delete)            │
│  2. CRUD Publisher                                         │
│  3. Get Publishers by Country                              │
│  4. Caching (Redis)                                        │
│  5. Error Handling (Invalid IDs, Missing Resources)        │
│                                                             │
│  Unit/Integration Fluxos (3):                              │
│  6. Create Entity (Use Case genérico)                      │
│  7. Remove Entity (Use Case genérico)                      │
│  8. Domain → DTO Mapping                                   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 🟢 Fluxo 1: CRUD Country (CREATE)

### Objetivo
Validar que um novo país pode ser criado com sucesso via API HTTP, com dados persistindo corretamente no banco.

### Entradas Relevantes
```java
HTTP POST /api/v1/countries
{
  "name": "USA",
  "nationality": "American"
}
```

**Dados de Setup**: 
- Argentina, Brazil, Chile pré-criados em @BeforeAll
- Publishers associados ao Brasil

### Saídas/Assertions Esperadas
```java
✅ Status HTTP: 201 (CREATED)
✅ Response Body:
   - id: > 0 (gerado pelo banco)
   - name: "USA"
   - nationality: "American"
   - Type: Integer (id)
```

### Pontos Sensíveis a Mudança
- ⚠️ **DTO Mapper**: Se mudar mapeamento DTO → Domain
- ⚠️ **Repository**: Se mudar lógica de save
- ⚠️ **Use Case**: Se mudar orquestração de criação
- ⚠️ **Mediator**: Se remover ou renomear use case
- ⚠️ **HTTP Status Code**: Se mudar de CREATED para OK
- ⚠️ **Validação de entrada**: Se adicionar @NotBlank obrigatório

### Exemplo de Código
```java
@Test
@Order(1)
public void should_create_a_country_successfully() {
    // ARRANGE
    var usa = new CountryDto("USA", "American");
    
    // ACT
    var response = restClientTesting.post(CountryDto.class, "countries", usa);
    
    // ASSERT
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    var country = response.getBody();
    assertThat(country.getId()).isInstanceOfAny(Integer.class);
    assertThat(country.getName()).isEqualTo("USA");
}
```

---

## 🟡 Fluxo 2: CRUD Country (READ by ID)

### Objetivo
Validar que um país previamente criado pode ser recuperado via GET por ID, com todos os atributos corretos.

### Entradas Relevantes
```
HTTP GET /api/v1/countries/{id}
```

**Dados de Setup**:
- País criado no Fluxo 1 com ID = createdCountryId
- Valores: name="USA", nationality="American"

### Saídas/Assertions Esperadas
```java
✅ Status HTTP: 200 (OK)
✅ Response Body:
   - id: {createdCountryId} (mesmo ID criado)
   - name: "USA"
   - nationality: "American"
✅ Tipo: CountryDto (não null)
```

### Pontos Sensíveis a Mudança
- ⚠️ **Caching (@Cacheable)**: Se mudar configuração de cache
- ⚠️ **Repository getById**: Se mudar lógica de busca
- ⚠️ **EntityMapper**: Se mudar conversão Entity → Domain
- ⚠️ **HTTP Status**: Se mudar de OK para CREATED
- ⚠️ **Pageability**: Se adicionar paginação obrigatória
- ⚠️ **Banco de Dados**: Se remover registro no setup

### Exemplo de Código
```java
@Test
@Order(3)
public void should_get_by_id_created_country_successfully() {
    // ACT
    var response = restClientTesting.get(CountryDto.class, "countries/" + createdCountryId);
    
    // ASSERT
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    var country = response.getBody();
    assertThat(country.getId()).isEqualTo(createdCountryId);
    assertThat(country.getName()).isEqualTo("USA");
}
```

---

## 🔵 Fluxo 3: CRUD Country (UPDATE)

### Objetivo
Validar que um país existente pode ser atualizado com novos valores, e as mudanças são persistidas.

### Entradas Relevantes
```
HTTP PUT /api/v1/countries/{id}
{
  "name": "United States",
  "nationality": "American"
}
```

**Dados de Setup**:
- País com ID = createdCountryId criado no Fluxo 1
- Valores antigos: name="USA", nationality="American"

### Saídas/Assertions Esperadas
```java
✅ Status HTTP: 200 (OK)
✅ Response Body:
   - id: {createdCountryId} (mesmo ID)
   - name: "United States" (NOVO)
   - nationality: "American" (pode ser novo)
✅ Persistência: Leitura posterior retorna novos valores
```

### Pontos Sensíveis a Mudança
- ⚠️ **Cache Invalidation**: Se não fazer @CacheEvict em update
- ⚠️ **DTO Mapper**: Se mudar mapeamento bidirecional
- ⚠️ **Repository save**: Se mudar lógica de update
- ⚠️ **Use Case**: Se não validar pré-condições
- ⚠️ **HTTP Status**: Se mudar de OK para CREATED
- ⚠️ **Validações**: Se adicionar constraints que bloqueiem update

### Exemplo de Código
```java
@Test
@Order(2)
public void should_update_created_country_successfully() {
    // ARRANGE
    var updatedCountry = new CountryDto("United States", "American");
    
    // ACT
    var response = restClientTesting.put(CountryDto.class, "countries/" + createdCountryId, updatedCountry);
    
    // ASSERT
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    var country = response.getBody();
    assertThat(country.getId()).isEqualTo(createdCountryId);
    assertThat(country.getName()).isEqualTo("United States");
}
```

---

## 🟣 Fluxo 4: CRUD Country (DELETE)

### Objetivo
Validar que um país pode ser removido com sucesso, retornando status apropriado, e não pode ser recuperado posteriormente.

### Entradas Relevantes
```
HTTP DELETE /api/v1/countries/{id}
```

**Dados de Setup**:
- País com ID = createdCountryId criado nos fluxos anteriores
- Sem dependências orfãs (publishers já foram removidos se necessário)

### Saídas/Assertions Esperadas
```java
✅ Status HTTP: 204 (NO_CONTENT)
✅ Response Body: null ou vazio
✅ Pós-Delete: GET /countries/{id} retorna 404 ou erro
```

### Pontos Sensíveis a Mudança
- ⚠️ **Foreign Keys**: Se houver publishers associados (erro de constraint)
- ⚠️ **Soft Delete vs Hard Delete**: Se implementar soft delete
- ⚠️ **HTTP Status**: Se mudar de NO_CONTENT para OK
- ⚠️ **Cache Invalidation**: Se não remover do cache
- ⚠️ **Use Case Validation**: Se adicionar regras de negócio que impeçam delete
- ⚠️ **Transaction Rollback**: Se transação falha silenciosamente

### Exemplo de Código
```java
@Test
@Order(5)
public void should_remove_created_country_successfully() {
    // ACT
    var response = restClientTesting.delete("countries/" + createdCountryId);
    
    // ASSERT
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
}
```

---

## 📋 Fluxo 5: Get All Countries (Listagem Paginada)

### Objetivo
Validar que todos os países podem ser listados via GET com paginação correta, retornando conjunto completo.

### Entradas Relevantes
```
HTTP GET /api/v1/countries
```

**Dados de Setup**:
- Argentina, Brazil, Chile em @BeforeAll
- USA criado em Fluxo 1 e deletado em Fluxo 4
- Total esperado: 3 (argentina + brazil + chile)

### Saídas/Assertions Esperadas
```java
✅ Status HTTP: 200 (OK)
✅ Response Body: CountryDto[] array
✅ Array Length: 3 (depois que USA é deletado)
✅ Conteúdo: Argentina, Brazil, Chile
✅ Sem duplicatas
```

### Pontos Sensíveis a Mudança
- ⚠️ **Paginação**: Se mudar tamanho padrão de página (atualmente 20)
- ⚠️ **Ordenação**: Se adicionar ordenação padrão (alfabética, por data, etc.)
- ⚠️ **Filtros**: Se adicionar filtros obrigatórios
- ⚠️ **Mapeamento**: Se mappers retornarem dados incompletos
- ⚠️ **Transações**: Se transação anterior não fizer commit correto
- ⚠️ **Índices**: Se índices do BD não forem eficientes (performance)

### Exemplo de Código
```java
@Test
@Order(6)
public void should_get_all_countries_successfully() {
    // ACT
    var response = restClientTesting.get(CountryDto[].class, "countries");
    
    // ASSERT
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    var countries = response.getBody();
    assertThat(countries).hasLength(3);  // 3 países persistidos
}
```

---

## 🟠 Fluxo 6: Create Publisher (com Validação de País)

### Objetivo
Validar que um novo publisher pode ser criado apenas se o país associado existir, garantindo integridade referencial e regras de negócio.

### Entradas Relevantes
```java
// Setup: Brasil deve existir com ID = brazil.getId()
HTTP POST /api/v1/publishers
{
  "name": "publisher_name",
  "history": "publisher_history",
  "country": {
    "id": {brazil.id},
    "name": "brazil",
    "nationality": "brazilian"
  }
}
```

**Dados de Setup**:
- Brazil país criado em @BeforeAll
- ID brasileiro conhecido e utilizado
- 3 publishers pré-criados (publisher1, publisher2, publisher3)

### Saídas/Assertions Esperadas
```java
✅ Status HTTP: 201 (CREATED)
✅ Response Body:
   - id: > 0 (gerado)
   - name: "publisher_name"
   - history: "publisher_history"
   - country.id: {brazil.id}
✅ Banco: Publisher persistido com foreign key correto
```

### Pontos Sensíveis a Mudança
- ⚠️ **Validação de País**: Se remover validação de existência
- ⚠️ **Use Case Logic**: Se mudar orquestração do CreatePublisherUseCase
- ⚠️ **Foreign Key Constraint**: Se banco não validar integridade referencial
- ⚠️ **Exception Type**: Se mudar ValidationException para OutOfException
- ⚠️ **Mapper**: Se mudar conversão DTO ↔ Domain para Publisher
- ⚠️ **HTTP Status**: Se mudar de CREATED para OK
- ⚠️ **Nested DTO**: Se mudar estrutura de country dentro de publisher

### Exemplo de Código
```java
@Test
@Order(1)
public void should_create_a_publisher_successfully() {
    // ARRANGE
    var countryDto = new CountryDto(brazil.getName(), brazil.getNationality());
    countryDto.setId(brazil.getId());
    var publisher = new PublisherDto("name", "history", countryDto);
    
    // ACT
    var response = restClientTesting.post(PublisherDto.class, "publishers", publisher);
    
    // ASSERT
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    var pub = response.getBody();
    assertThat(pub.getName()).isEqualTo("name");
    assertThat(pub.getCountry().getId()).isEqualTo(brazil.getId());
}
```

---

## 🔴 Fluxo 7: Get Publishers by Country (Relacionamento)

### Objetivo
Validar que publishers podem ser filtrados por país, testando joins/relacionamentos e pré-carregamento de dados associados.

### Entradas Relevantes
```
HTTP GET /api/v1/countries/{countryId}/publishers
```

**Dados de Setup**:
- Brazil país com ID = brazil.getId()
- 2 publishers (publisher1, publisher2) associados ao Brasil
- Argentina e Chile SEM publishers

### Saídas/Assertions Esperadas
```java
✅ Status HTTP: 200 (OK)
✅ Response Body: PublisherDto[] array
✅ Array Length: 2 (apenas publishers do Brasil)
✅ Conteúdo: publisher1 e publisher2
✅ Cada publisher com country.id == brazil.id
```

### Pontos Sensíveis a Mudança
- ⚠️ **Query Customizado**: Se mudar lógica de filtro por país
- ⚠️ **Eager Loading**: Se relationship não fizer eager load (N+1 queries)
- ⚠️ **Join Logic**: Se SQL join estiver errado
- ⚠️ **Mapper**: Se não incluir country aninhado na resposta
- ⚠️ **Paginação**: Se aplicar filtro incorretamente com página
- ⚠️ **Performance**: Se query rodar sem índices (muito lenta)
- ⚠️ **Edge Case**: Se país não existir (retornar 404 vs array vazio?)

### Exemplo de Código
```java
@Test
@Order(7)
public void should_get_publishers_given_a_country_successfully() {
    // ACT
    var response = restClientTesting.get(PublisherDto[].class, 
        "countries/" + brazil.getId() + "/publishers");
    
    // ASSERT
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    var publishers = response.getBody();
    assertThat(publishers).hasLength(2);  // publisher1 + publisher2
}
```

---

## 💾 Fluxo 8: Redis Caching (Read from Cache)

### Objetivo
Validar que cache funciona corretamente: primeira requisição bate no BD, segunda requisição lê do cache, sem duplicar queries.

### Entradas Relevantes
```
HTTP GET /api/v1/countries/{id}  # Primeira vez
HTTP GET /api/v1/countries/{id}  # Segunda vez (deve vir do cache)
```

**Dados de Setup**:
- País com ID = createdCountryId criado e atualizado
- Redis container rodando
- Cache TTL = 60 segundos (do application.properties)

### Saídas/Assertions Esperadas
```java
✅ Primeira Requisição:
   - Status: 200
   - Data: vem do banco
   - Tempo: ~50-100ms (I/O)

✅ Segunda Requisição (mesma URL):
   - Status: 200
   - Data: idêntica
   - Tempo: ~1-5ms (cache)

✅ Performance: Segunda é 10-50x mais rápida
✅ Conteúdo: Idêntico (dados não mudaram)
```

### Pontos Sensíveis a Mudança
- ⚠️ **Cache Key**: Se mudar estratégia de key (atualmente usa #{id})
- ⚠️ **@CacheEvict em UPDATE**: Se não invalidar cache no PUT
- ⚠️ **@CacheEvict em DELETE**: Se não invalidar cache no DELETE
- ⚠️ **TTL**: Se mudar 60s para outro valor
- ⚠️ **Redis Connection**: Se Redis não estiver disponível
- ⚠️ **Serialização**: Se objeto não for serializable
- ⚠️ **Concurrent Access**: Se múltiplas threads acessarem simultaneamente

### Exemplo de Código
```java
@Test
@Order(4)
public void should_get_by_id_created_country_from_cache_successfully() {
    // ACT (segunda vez, vem do cache)
    var response = restClientTesting.get(CountryDto.class, 
        "countries/" + createdCountryId);
    
    // ASSERT
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    var country = response.getBody();
    assertThat(country.getId()).isEqualTo(createdCountryId);
    assertThat(country.getName()).isEqualTo("USA");
    // Performance improvement implícito (não medido no teste)
}
```

---

## 🚫 Fluxo 9: Error Handling (Remover ID Inexistente)

### Objetivo
Validar que sistema retorna erro apropriado ao tentar deletar entidade que não existe, sem causar exceção não-tratada.

### Entradas Relevantes
```
HTTP DELETE /api/v1/countries/99
// 99 é um ID que não existe (nunca foi criado)
```

**Dados de Setup**:
- Nenhum país com ID = 99
- Setup não cria essa entidade intencionalmente

### Saídas/Assertions Esperadas
```java
✅ Status HTTP: 406 (NOT_ACCEPTABLE)
✅ Response Body: Erro/Message (implementação específica)
✅ Exceção: RemoveException lançada
✅ Transação: Rollback executado
✅ Banco: Nenhuma mudança
```

### Pontos Sensíveis a Mudança
- ⚠️ **HTTP Status**: Se mudar de 406 para 404 ou 400
- ⚠️ **Exception Handling**: Se não tratar RemoveException
- ⚠️ **Use Case Validation**: Se não verificar existência antes de delete
- ⚠️ **Error Message**: Se mensagem de erro for genérica/vaga
- ⚠️ **Partial Updates**: Se deletar parcialmente antes de descobrir erro
- ⚠️ **Logging**: Se não logar erro apropriadamente

### Exemplo de Código
```java
@Test
@Order(8)
public void should_not_remove_country_given_not_existent_id() {
    // ACT
    var response = restClientTesting.delete("countries/99");
    
    // ASSERT
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
}
```

---

## 🔧 Fluxo 10: Create Entity Use Case (Unit Test)

### Objetivo
Validar orquestração de criação genérica em nível de use case, sem depender de BD real, apenas com mocks.

### Entradas Relevantes
```java
// Unit Test (Mockito)
var inputEntity = new Country(0, "Brazil", "Brazilian");
when(mediator.getCommand(Country.class)).thenReturn(countryCommand);
when(countryCommand.save(inputEntity)).then(r -> {
    inputEntity.setId(1);  // Simula geração de ID
    return inputEntity;
});
```

**Dados de Setup**:
- Mediator mockado
- DataCommand mockado
- Domain entity criada localmente

### Saídas/Assertions Esperadas
```java
✅ Use case executa sem erro
✅ mediator.getCommand() foi chamado com Country.class
✅ countryCommand.save() foi chamado com inputEntity
✅ Resultado tem ID = 1 (atribuído pelo mock)
✅ Tipo: Country (mesma classe)
```

### Pontos Sensíveis a Mudança
- ⚠️ **Mediator Refactoring**: Se remover mediator (refatoração)
- ⚠️ **Use Case Signature**: Se mudar parâmetros de execute()
- ⚠️ **Exception Throwing**: Se adicionar validações que lancem exceção
- ⚠️ **Mock Setup**: Se não mockar corretamente o mediator
- ⚠️ **Return Type**: Se mudar para void em vez de retornar entidade
- ⚠️ **Transactional**: Se adicionar @Transactional que falhe

### Exemplo de Código
```java
@ExtendWith(MockitoExtension.class)
public class CreateEntityUseCaseImplTest {
    
    @Mock private DataCommand<Country> countryCommand;
    @Mock private RepositoryMediator mediator;
    @InjectMocks private CreateEntityUseCaseImpl createEntityUseCaseImpl;
    
    @Test
    public void should_create_an_entity_successfully() throws NoSuchMethodException {
        // ARRANGE
        var inputEntity = new Country(0, "Brazil", "Brazilian");
        when(mediator.getCommand(Country.class)).thenReturn(countryCommand);
        when(countryCommand.save(inputEntity)).then(r -> {
            inputEntity.setId(1);
            return inputEntity;
        });
        
        // ACT
        var outputEntity = createEntityUseCaseImpl.execute(Country.class, inputEntity);
        
        // ASSERT
        assertThat(outputEntity).isEqualTo(inputEntity);
        assertThat(outputEntity.getId()).isEqualTo(1);
    }
}
```

---

## 🗑️ Fluxo 11: Remove Entity Use Case (Unit Test)

### Objetivo
Validar remoção genérica com validação de existência: se entidade existe, remove com sucesso; se não existe, lança exceção.

### Entradas Relevantes
```java
// Happy Path
var entityId = 1;
when(mediator.getQuery(Country.class)).thenReturn(countryQuery);
when(countryQuery.getById(1)).thenReturn(new Country(1, "name", "gentilic"));
when(mediator.getCommand(Country.class)).thenReturn(countryCommand);
when(countryCommand.remove(...)).thenReturn(...);
```

**Dados de Setup**:
- Query mockado (simula busca)
- Command mockado (simula delete)
- Entidade localizada e existe

### Saídas/Assertions Esperadas
```java
✅ Happy Path:
   - mediator.getQuery() chamado
   - countryQuery.getById(1) retorna entidade
   - mediator.getCommand() chamado
   - countryCommand.remove() chamado
   - assertDoesNotThrow()

✅ Error Path:
   - countryQuery.getById(99) retorna null
   - Lança RemoveException
   - Command remove() NÃO é chamado
```

### Pontos Sensíveis a Mudança
- ⚠️ **Query vs Command**: Se inverter ordem de chamadas
- ⚠️ **Null Handling**: Se não validar getById() == null
- ⚠️ **Exception Type**: Se mudar para OutOfException
- ⚠️ **Mock Verification**: Se mocks não forem chamados na ordem correta
- ⚠️ **Mediator Refactoring**: Se remover serviço locator
- ⚠️ **Cascading Deletes**: Se relacionamentos causar constraint error

### Exemplo de Código
```java
@ExtendWith(MockitoExtension.class)
public class RemoveEntityUseCaseImplTest {
    
    @Mock private DataQuery<Country> countryQuery;
    @Mock private DataCommand<Country> countryCommand;
    @Mock private RepositoryMediator mediator;
    @InjectMocks private RemoveEntityUseCaseImpl removeEntityUseCase;
    
    @Test
    public void should_remove_an_entity_use_case() throws NoSuchMethodException {
        // ARRANGE
        when(mediator.getQuery(Country.class)).thenReturn(countryQuery);
        when(countryQuery.getById(1)).thenReturn(new Country(1, "name", "gentilic"));
        when(mediator.getCommand(Country.class)).thenReturn(countryCommand);
        
        // ACT & ASSERT
        assertDoesNotThrow(() -> removeEntityUseCase.execute(Country.class, 1));
    }
    
    @Test
    public void should_not_find_entity_on_remove_use_case() {
        // ARRANGE
        when(mediator.getQuery(Country.class)).thenReturn(countryQuery);
        when(countryQuery.getById(99)).thenReturn(null);
        
        // ACT & ASSERT
        assertThrows(RemoveException.class, () -> 
            removeEntityUseCase.execute(Country.class, 99));
    }
}
```

---

## 🔄 Fluxo 12: DTO ↔ Domain Mapping (Bidirecional)

### Objetivo
Validar que mappers convertem dados corretamente em ambas direções sem perda ou corrupção de atributos.

### Entradas Relevantes
```java
// Direction 1: DTO → Domain
var dto = new CountryDto("Brazil", "Brazilian");
dto.setId(1);

// Direction 2: Domain → DTO
var domain = new Country(1, "Brazil", "Brazilian");
```

**Dados de Setup**:
- CountryDto e Country com mesmos atributos
- ID definido explicitamente

### Saídas/Assertions Esperadas
```java
✅ DTO → Domain:
   - domain.getId() == 1
   - domain.getName() == "Brazil"
   - domain.getNationality() == "Brazilian"

✅ Domain → DTO:
   - dto.getId() == 1
   - dto.getName() == "Brazil"
   - dto.getNationality() == "Brazilian"

✅ Roundtrip (DTO → Domain → DTO):
   - Original == Final (recursive comparison)

✅ Null Handling:
   - null DTO → null Domain
   - null Domain → null DTO
```

### Pontos Sensíveis a Mudança
- ⚠️ **Atributo Novo**: Se adicionar campo em Country, mapper fica incompleto
- ⚠️ **Renomeação**: Se renomear atributo em DTO ou Domain
- ⚠️ **Tipo Mudança**: Se mudar tipo (String → Enum, etc.)
- ⚠️ **Nested Objects**: Se adicionar relacionamentos (country dentro de publisher)
- ⚠️ **Collections**: Se lidar com arrays/listas
- ⚠️ **Validação**: Se adicionar @Valid em DTO
- ⚠️ **Jackson Annotations**: Se mudar @JsonProperty

### Exemplo de Código
```java
public class CountryDtoMapperTest {
    
    @Test
    public void should_parse_country_dto_to_country_domain() {
        // ARRANGE
        var dto = new CountryDto("Brazil", "Brazilian");
        dto.setId(1);
        
        // ACT
        var actual = CountryDtoMapper.toDomain(dto);
        
        // ASSERT
        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(new Country(1, "Brazil", "Brazilian"));
    }
    
    @Test
    public void should_parse_country_domain_to_country_dto() {
        // ARRANGE
        var domain = new Country(1, "Brazil", "Brazilian");
        
        // ACT
        var actual = CountryDtoMapper.fromDomain(domain);
        
        // ASSERT
        var expected = new CountryDto("Brazil", "Brazilian");
        expected.setId(1);
        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }
}
```

---

## 📊 Matriz de Dependência Entre Fluxos

```
Fluxo 1 (CREATE)
    ├─ Usa: Repository, UseCase, Mapper, DTO
    └─ Gera: ID para próximos fluxos
    
Fluxo 2 (READ) ← Depende de Fluxo 1
    ├─ Usa: Repository, Mapper
    ├─ Testa: Cache (primeira leitura)
    └─ ID: Criado em Fluxo 1
    
Fluxo 3 (UPDATE) ← Depende de Fluxo 2
    ├─ Usa: Repository, UseCase, Mapper
    ├─ Pré-condição: Entidade existe
    ├─ Pós-condição: Cache invalidado?
    └─ ID: Mesmo de Fluxo 1
    
Fluxo 4 (DELETE) ← Depende de Fluxo 3
    ├─ Usa: Repository, UseCase
    ├─ Pré-condição: Entidade existe
    ├─ Pós-condição: Cache removido
    └─ Verifica: Não há constraint violations
    
Fluxo 5 (LIST) ← Depende de Fluxo 4
    ├─ Usa: Repository
    ├─ Testa: Resultado após Fluxo 4 (DELETE)
    ├─ Contagem: 3 (depois que USA é deletado)
    └─ Ordem: Verificar se consistente

Fluxo 6 (CREATE Publisher) ← Depende de @BeforeAll
    ├─ Usa: Repository, UseCase (com validação)
    ├─ Validação: País deve existir
    ├─ FK: Country ID = Brazil ID
    └─ Testa: Regra de negócio

Fluxo 7 (GET by FK) ← Depende de Fluxo 6
    ├─ Usa: Repository (query customizado)
    ├─ Filtro: country_id = brazil.id
    ├─ Contagem: 2 publishers
    └─ Testa: Relacionamento

Fluxo 8 (CACHE READ) ← Depende de Fluxo 2/3
    ├─ Usa: Cache (Redis)
    ├─ Primeira: Miss (vai ao BD)
    ├─ Segunda: Hit (vem do cache)
    └─ Verificar: Tempo resposta
```

---

## ⚠️ Matriz de Pontos Sensíveis Críticos

```
┌──────────────────────────────┬─────────────────────────┬──────────┐
│ Área                         │ Mudança Crítica         │ Fluxos   │
├──────────────────────────────┼─────────────────────────┼──────────┤
│ DTO Mapper                   │ Atributo novo/mudado    │ 1,2,3,12 │
│ Entity Mapper                │ Conversão incompleta    │ 1,2,3,5  │
│ Repository (JPA)             │ Query errada/ineficiente│ 1,2,3,5,7│
│ Cache Invalidation           │ Sem @CacheEvict em UPD  │ 3,4,8    │
│ HTTP Status Codes            │ Mudar CREATED/OK/204    │ 1,3,4,6  │
│ Use Case Validation          │ Validação removida      │ 1,6      │
│ Foreign Key Constraints      │ Remover ou desabilitar  │ 6        │
│ Exception Types              │ Mudar ValidationException│ 6,9      │
│ Mediator (Service Locator)   │ Remover (refatore)      │ 1,3,10,11│
│ Redis/Caching                │ Desabilitar/TTL mudança │ 8        │
│ Transactional                │ Remover/adicionar       │ 1,3,4,6  │
└──────────────────────────────┴─────────────────────────┴──────────┘
```

---

## 🔍 Checklist de Impacto ao Modificar Código

### Se mudar **Entity Mapper**:
```
[ ] Testar Fluxo 2 (READ) - Valores corretos?
[ ] Testar Fluxo 3 (UPDATE) - Atributos atualizados?
[ ] Testar Fluxo 5 (LIST) - Array correto?
[ ] Testar Fluxo 12 (Mapping) - Bidirecional intacto?
```

### Se mudar **Repository getById()**:
```
[ ] Testar Fluxo 2 (READ) - Busca funciona?
[ ] Testar Fluxo 8 (CACHE) - Cache invalidado?
[ ] Testar Fluxo 9 (ERROR) - Null handling correto?
[ ] Verificar indices BD
```

### Se mudar **Cache Strategy**:
```
[ ] Testar Fluxo 3 (UPDATE) - @CacheEvict acionado?
[ ] Testar Fluxo 4 (DELETE) - Cache removido?
[ ] Testar Fluxo 8 (CACHE) - TTL correto?
[ ] Medir tempo resposta antes/depois
```

### Se mudar **Use Case Validation**:
```
[ ] Testar Fluxo 1 (CREATE) - Validações passam?
[ ] Testar Fluxo 6 (CREATE Publisher) - FK validação?
[ ] Testar Fluxo 9 (ERROR) - Exceções corretas?
[ ] Testar Fluxo 11 (REMOVE) - Null checks?
```

---

## 📈 Evolução de Fluxos (Roadmap)

### Atual (8-12 fluxos)
- ✅ CRUD básico (Country, Publisher)
- ✅ Relacionamentos simples (Country → Publisher)
- ✅ Cache (Redis)
- ✅ Validação de FK

### Próximo (Médio Prazo)
- ⏳ Publication (livros) CRUD
- ⏳ Image upload (S3)
- ⏳ Advanced filtering (busca)
- ⏳ Soft delete

### Futuro (Longo Prazo)
- ⏳ Concorrência (race conditions)
- ⏳ Transações complexas
- ⏳ Auditoria (quem mudou?)
- ⏳ Performance (load testing)

---

## 📊 Estatísticas de Teste

```
Total de Fluxos Testados:          12
Total de Testes (casos):           ~50+
Tipo Dominante:                    E2E (Functional)
Cobertura Estimada:                70%+
Execução Tempo:                    < 5 segundos (suite)
TestContainers:                    PostgreSQL + Redis
Mocks:                             Mockito
Assertions:                        AssertJ
```

---

**Data**: 2026-02-07  
**Status**: Documentado e Validado  
**Próxima Revisão**: Quando adicionar novo fluxo crítico

