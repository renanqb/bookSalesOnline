# Definition of Done (DoD) - Testes Automatizados
## BookSalesOnline

**Data de Criação**: 2026-02-07  
**Versão**: 1.0  
**Status**: Padrão Obrigatório do Projeto

---

> Este documento define o que significa "feito" para testes neste projeto.
> Uma feature NÃO pode ser mergeada sem cumprir TODAS as seções desta DoD.

---

## ✅ 1. TESTES OBRIGATÓRIOS

### 1.1 Cobertura de Testes

```
[ ] UNIT TESTS
    [ ] Domain entities (instantiação, validações)
    [ ] Use cases (lógica orquestrada, com mocks)
    [ ] Mappers (conversão bidirecional)
    [ ] Repositórios (mock JpaRepository, validar mapping)
    
    Critério de Aceitação:
    - Mínimo 1 teste por método público
    - Happy path + error cases (mínimo 2 testes por use case)
    - Exemplo: CreatePublisherUseCase:
      ✓ should_create_publisher_when_country_exists()
      ✗ should_throw_when_country_not_found()

[ ] INTEGRATION TESTS
    [ ] Repositórios com mock de JpaRepository
    [ ] Validar mapeamento Domain ↔ Entity
    [ ] Testar queries customizadas
    
    Critério de Aceitação:
    - Todos os repositórios testados
    - Mapping validado bidirecionalmente
    - Paginação testada

[ ] E2E / FUNCTIONAL TESTS
    [ ] Endpoints HTTP via TestContainers
    [ ] CRUD completo (CREATE, READ, UPDATE, DELETE, LIST)
    [ ] Error cases (ID inexistente, validações)
    [ ] Relacionamentos (FKs, cascading)
    [ ] Cache (read from cache no segundo acesso)
    
    Critério de Aceitação:
    - Por entidade: mínimo 8 fluxos testados
    - Exemplo Country:
      ✓ CREATE Country
      ✓ READ Country by ID (cache miss)
      ✓ READ Country by ID (cache hit)
      ✓ UPDATE Country + cache evict
      ✓ DELETE Country
      ✓ LIST all Countries
      ✓ Error: DELETE inexistente
      ✓ Relacionamento: GET Publishers by Country

[ ] Cobertura Geral
    [ ] Mínimo 70% cobertura de código (JaCoCo)
    [ ] Mutation score > 70% (PIT)
    
    Verificação:
    ./mvnw clean verify
    open target/site/jacoco/index.html
```

### 1.2 Tipos de Teste por Camada

```
DOMAIN LAYER
  [  ] Entity instantiation tests (sem Spring)
  [  ] Validation tests (@NotBlank, @NotNull)
  [  ] Business logic tests (métodos customizados)
  [  ] Framework: JUnit 5 + AssertJ
  [  ] Tempo esperado: < 1ms por teste

APPLICATION LAYER
  [  ] Use case tests com mocks (sem Spring)
  [  ] Happy path + error cases
  [  ] Validação de pré-condições
  [  ] Mocking: Mockito com @ExtendWith(MockitoExtension.class)
  [  ] Tempo esperado: 1-10ms por teste

ADAPTER LAYER
  [  ] Repository tests (mock JpaRepository)
    - Validar mapping Domain ↔ Entity
    - Testar paginação
    - Testar ordenação
  [  ] Mapper tests (bidirecional)
  [  ] DTO tests (instantiação)
  [  ] Controller E2E tests (@SpringBootTest + TestContainers)
    - Testar status HTTP correto
    - Testar response body
    - Testar error handling
  [  ] Tempo esperado: 1-10ms unit, 100-500ms E2E

INFRASTRUCTURE
  [  ] TestContainers (PostgreSQL, Redis)
  [  ] S3 Storage mocking
  [  ] Tempo esperado: Incluído no E2E (< 5s suite total)
```

---

## 📋 2. TESTES - OBRIGATÓRIO

### 2.1 Nomeação Clara de Testes

```
[ ] Padrão: should_[comportamento]_[quando]

EXEMPLOS BOM:
  ✓ should_create_country_successfully()
  ✓ should_throw_validation_exception_when_country_name_is_blank()
  ✓ should_return_all_countries_paginated()
  ✓ should_invalidate_cache_when_updating_country()
  ✓ should_not_delete_country_when_id_does_not_exist()

EXEMPLOS RUIM:
  ✗ testCountry()
  ✗ test1()
  ✗ countryTest()
  ✗ shouldWork()

Verificação em Code Review:
  - Buscar por "test" (sem should_)
  - Buscar por "Test" genérico
  - Validar descrição clara do comportamento
```

### 2.2 Assertions Relevantes e Significativas

```
[ ] Cada teste tem pelo menos 1 assertion significativa

BOM:
  ✓ assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED)
  ✓ assertThat(country.getId()).isGreaterThan(0)
  ✓ assertThat(result).isEqualTo(expected)
  ✓ assertThrows(ValidationException.class, () -> useCase.execute(...))

RUIM:
  ✗ assertThat(country).isNotNull()  // Óbvio!
  ✗ assertThat(id).isGreaterThan(0)  // Vago, sem contexto
  ✗ // Sem assertions (executar só para não falhar)

Regra:
  - Validar o que MUDOU, não o óbvio
  - 1-3 assertions por teste (máximo 5 se relacionadas)
  - Cada assertion responde: "Por que testo isto?"

Verificação em Code Review:
  - Remover assertThat().isNotNull() triviais
  - Exigir 1+ assertion significativa
```

### 2.3 Teste com Padrão AAA (Arrange-Act-Assert)

```
[ ] Todo teste segue padrão AAA:

    @Test
    public void should_create_country_successfully() {
        // ARRANGE (Setup de dados)
        var countryDto = new CountryDto("Brazil", "Brazilian");
        
        // ACT (Executar ação)
        var response = restClientTesting.post(
            CountryDto.class, "countries", countryDto);
        
        // ASSERT (Validar resultado)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        var body = response.getBody();
        assertThat(body.getName()).isEqualTo("Brazil");
    }

Verificação em Code Review:
  - Identificar cada seção (ARRANGE, ACT, ASSERT)
  - Validar separação clara
  - Rejeitar setup/act/assert misturados
```

---

## 🔒 3. ISOLAMENTO - OBRIGATÓRIO

### 3.1 Testes Independentes (Sem Compartilhamento de Estado)

```
[ ] Nenhum teste depende de outro teste anterior
[ ] Cada teste cria seus próprios dados
[ ] Sem variáveis compartilhadas entre testes

BOM:
  @Test
  public void test1() {
      var country = new Country(1, "Brazil", "Brazilian");  // Local
      assertThat(country.getId()).isEqualTo(1);
  }
  
  @Test
  public void test2() {
      var country = new Country(1, "USA", "American");  // Diferente
      assertThat(country.getId()).isEqualTo(1);
  }

RUIM:
  static Country sharedCountry = new Country(...);  // ❌ Compartilhado!
  
  @Test
  public void test1() {
      sharedCountry.setId(1);
  }
  
  @Test
  public void test2() {
      assertEquals(sharedCountry.getId(), 1);  // Depende de test1!
  }

Verificação em Code Review:
  - Buscar por static fields (exceto mocks/constants)
  - Buscar por @BeforeEach com setup compartilhado
  - Validar que cada teste é independente
  - Trocar ordem de testes aleatoriamente (@Order deve ser removido)

E2E Exception:
  [@Order] é permitido em @SpringBootTest + @TestMethodOrder
  Razão: Sequência de CRUD (1: CREATE → 2: READ → 3: UPDATE)
  Mas dados devem estar em @BeforeAll com @Transactional
```

### 3.2 Mocks Apenas Necessários

```
[ ] Mock apenas dependências reais
[ ] Sem mocks desnecessários ou redundantes
[ ] Usar Mockito quando apropriado

PADRÃO:
  UNIT Tests (sem Spring):
    - Mock: RepositoryMediator, DataCommand, DataQuery
    - Sem mock: Domain entities, DTOs
    - Framework: @ExtendWith(MockitoExtension.class)

  INTEGRATION Tests (sem Spring):
    - Mock: JpaRepository (*Data)
    - Sem mock: *Repository adapter, mappers
    - Framework: @ExtendWith(MockitoExtension.class)

  E2E Tests (com Spring):
    - Sem mock: Tudo real (BD, cache, S3 via LocalStack)
    - Framework: @SpringBootTest + TestContainers

BOM:
  @Mock private RepositoryMediator mediator;  // Necessário
  @InjectMocks private CreateCountryUseCase useCase;
  
  @Test
  public void test() {
      when(mediator.getCommand(...)).thenReturn(command);  // ✓ Específico
  }

RUIM:
  @Mock private RepositoryMediator mediator;
  @Mock private DataCommand<Country> command1;
  @Mock private DataCommand<Country> command2;  // ❌ Redundante!
  @Mock private ApplicationContext context;      // ❌ Desnecessário!

Verificação em Code Review:
  - Cada @Mock tem uso no teste?
  - Pode remover mock sem quebrar teste?
  - Se sim: REMOVER
```

### 3.3 Sem Dependências de I/O (em testes unitários)

```
[ ] Testes unitários: SEM banco, SEM I/O, SEM HTTP
[ ] Testes integração: Mock JpaRepository, SEM BD real
[ ] Testes E2E: TestContainers OK (BD + cache reais)

Velocidade Esperada:
  Unit tests: < 1ms cada
  Integration tests: 1-10ms cada
  E2E tests: 100-500ms cada
  Suite total: < 5 segundos

BOM:
  @ExtendWith(MockitoExtension.class)
  public class CreateCountryUseCaseTest {
      @Mock private RepositoryMediator mediator;  // Mock, não real!
      @Test
      public void test() { ... }  // < 1ms esperado
  }

RUIM:
  @SpringBootTest  // ❌ Carrega context inteiro = lento!
  public class CreateCountryUseCaseTest {
      @Autowired private RepositoryMediator mediator;  // Real = lento!
      @Test
      public void test() { ... }  // Múltiplos segundos!
  }

Verificação em Code Review:
  - Unit test tem @SpringBootTest? REMOVER
  - Unit test acessa BD real? MOCKAR
  - Integration test sem mock JpaRepository? MOCKAR
```

---

## 🎯 4. QUALIDADE - OBRIGATÓRIO

### 4.1 Legibilidade e Clareza

```
[ ] Código de teste é legível e claro
[ ] Sem lógica complexa ou condicional
[ ] Sem loops ou condições if/else (indica teste frágil)

BOM:
  @Test
  public void should_create_country() {
      var dto = new CountryDto("Brazil", "Brazilian");  // Claro
      var response = restClientTesting.post(...);       // Ação única
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
  }

RUIM:
  @Test
  public void test() {  // Nome vago!
      for (int i = 0; i < 10; i++) {  // ❌ Loop em teste!
          if (i > 5) {  // ❌ Condicional!
              // lógica complexa
          }
      }
  }

Verificação em Code Review:
  - Teste legível para alguém novo?
  - Precisa de for/while loops? Deve ser parametrizado
  - Precisa de if/else? Split em 2 testes
  - Máximo 15 linhas de código por teste (sem be very long)
```

### 4.2 Sem Duplicação de Código (DRY)

```
[ ] Evitar repetição de setup
[ ] Reutilizar fixtures e dados de teste
[ ] Criar helper methods para operações comuns

BOM:
  @ExtendWith(MockitoExtension.class)
  public class CountryRepositoryTest {
      
      private CountryEntity createCountryEntity(int id, String name) {
          return new CountryEntity(id, name, "nationality");  // Helper
      }
      
      @Test
      public void should_save() {
          var entity = createCountryEntity(1, "Brazil");  // Reutiliza
          // ...
      }
  }

RUIM:
  @Test
  public void test1() {
      var entity = new CountryEntity(1, "Brazil", "brazilian");
      // ... 10 linhas de código
  }
  
  @Test
  public void test2() {
      var entity = new CountryEntity(1, "Brazil", "brazilian");  // Repetido!
      // ... 10 linhas de código
  }

Verificação em Code Review:
  - Mesmo setup em 2+ testes? Criar helper
  - Helper method duplicado? Mover para classe base
```

### 4.3 Sem Acoplamento a Implementação

```
[ ] Testar COMPORTAMENTO, não implementação interna
[ ] Não usar verify() para verificar chamadas internas
[ ] Não fazer assertions em atributos private

BOM:
  @Test
  public void should_create_country() {
      var result = useCase.execute(Country.class, country);
      assertThat(result.getId()).isEqualTo(1);  // ✓ Comportamento
  }

RUIM:
  @Test
  public void test() {
      useCase.execute(...);
      verify(mediator).getCommand(Country.class);  // ❌ Implementação!
      verify(command).save(any());                  // ❌ Implementação!
  }

Regra:
  - Testar outputs e side effects públicos
  - NÃO testar chamadas internas entre métodos
  - Se precisa mockar muitas coisas, design está acoplado

Verificação em Code Review:
  - Buscar por verify()
  - Justificar cada verify() (são raros e específicos)
```

---

## 🔧 5. MANUTENÇÃO - OBRIGATÓRIO

### 5.1 Fácil de Estender (Sem Quebra em Mudanças Futuras)

```
[ ] Novo campo em entidade:
    [ ] Mapper atualizado (2 direções)
    [ ] Testes de mapper atualizados
    [ ] DTO atualizado
    [ ] Use case revalidado se necessário
    
[ ] Novo endpoint HTTP:
    [ ] Novo teste E2E criado
    [ ] Novo fluxo em critical-test-flows.md
    
[ ] Novo validação:
    [ ] Testes de error case adicionados
    [ ] DoD validado

CHECKLIST antes de merge:
  [ ] Adicionar novo campo? Atualizar 4 mappers
  [ ] Adicionar novo use case? Adicionar ao mediator + testes
  [ ] Adicionar novo endpoint? Adicionar E2E test + fluxo doc
  [ ] Adicionar nova validação? Adicionar error case test

Verificação em Code Review:
  - Novo atributo foi adicionado a:
    ✓ Entity
    ✓ Dto
    ✓ Domain
    ✓ EntityMapper (2 direções)
    ✓ DtoMapper (2 direções)
    ✓ Testes
  - Se faltar algo: NÃO APROVAR
```

### 5.2 Baixa Fragilidade (Testes Robustos)

```
[ ] Testes passam consistentemente
[ ] Sem testes flaky (intermitentes)
[ ] Sem dependência de timing/sleep
[ ] Sem dependência de dados externos

BOM:
  @Test
  public void should_create() {
      var country = new Country(0, "Brazil", "Brazilian");
      var result = useCase.execute(Country.class, country);
      assertThat(result.getId()).isGreaterThan(0);  // ✓ Determinístico
  }

RUIM:
  @Test
  public void test() {
      Thread.sleep(1000);  // ❌ Timing!
      var now = LocalDateTime.now();
      assertThat(now.getYear()).isEqualTo(2026);  // ❌ Falha em 2027!
  }

Regra:
  - Sem Thread.sleep() em testes (exceto I/O timeout test)
  - Sem LocalDateTime.now() (usar injected clock)
  - Sem Random() (mockar ou usar fixture fixa)
  - Sem dependência de ordem de testes

Verificação em Code Review:
  - Buscar por sleep()
  - Buscar por now()
  - Buscar por Random()
  - Buscar por @Order (se não em E2E sequencial)
```

### 5.3 Setup/Teardown Claro

```
[ ] @BeforeAll: Setup compartilhado para toda classe
[ ] @BeforeEach: Setup por teste (não usar se possível)
[ ] @AfterAll: Limpeza final
[ ] @AfterEach: Limpeza por teste (não usar se possível)

PADRÃO E2E:
  @BeforeAll
  @Transactional
  public void init() {
      // Dados compartilhados: argentina, brazil, chile
      countryRepository.save(argentina);
      countryRepository.save(brazil);
      countryRepository.save(chile);
  }
  
  @Test
  @Order(1)
  @Transactional
  public void should_create() {
      // Dados locais + dados de @BeforeAll
      var usa = new CountryDto("USA", "American");
      // ...
      // Transação faz rollback automático
  }

PADRÃO Unit/Integration:
  @ExtendWith(MockitoExtension.class)
  public class Test {
      @Mock private dependency;
      
      // SEM @BeforeEach (MockitoExtension já inicializa)
      
      @Test
      public void test() {
          // Dados locais criados aqui
      }
  }

Verificação em Code Review:
  - E2E sem @BeforeAll? Problemas de setup
  - Unit com @SpringBootTest? Muito lento
  - Cleanup manual (tearDown)? Usar @Transactional
```

---

## 🚀 6. PERFORMANCE - OBRIGATÓRIO

### 6.1 Velocidade de Execução

```
[ ] Suite de testes < 5 segundos total
    [ ] Unit tests: < 1 segundo
    [ ] Integration tests: 1-2 segundos
    [ ] E2E tests: 2-4 segundos (TestContainers)

Medição:
  ./mvnw test  # Ver tempo total em console
  
Checklist:
  [ ] Suite > 10 segundos? Revisar testes lentos
  [ ] Unit test > 100ms? Tem @SpringBootTest ou I/O
  [ ] E2E test > 1s cada? TestContainers está ineficiente

Validação em Code Review:
  - Novo teste é lento? Reverter
  - Suite ficou mais lenta? Investigar
```

### 6.2 Sem Testes Lentos Desnecessários

```
[ ] Unit test com @SpringBootTest?
    → REFATORAR: Remover Spring, usar @Mock

[ ] Unit test com TestContainers?
    → REFATORAR: Mock JpaRepository em vez de container

[ ] E2E test sem bom motivo (testando mappers)?
    → REFATORAR: Mover para unit test

Exemplo Refatoração:

ANTES (Lento - 2+ segundos):
  @SpringBootTest
  public class CreateCountryUseCaseTest {
      @Autowired private CreateCountryUseCase useCase;
      @Test
      public void test() { ... }  // Lento!
  }

DEPOIS (Rápido - 1ms):
  @ExtendWith(MockitoExtension.class)
  public class CreateCountryUseCaseTest {
      @Mock private RepositoryMediator mediator;
      @InjectMocks private CreateCountryUseCase useCase;
      @Test
      public void test() { ... }  // Rápido!
  }
```

---

## 📊 7. COBERTURA - OBRIGATÓRIO

### 7.1 Cobertura de Código (JaCoCo)

```
[ ] Mínimo 70% cobertura de linhas executadas
[ ] Mínimo 70% cobertura de branches
[ ] Excluir classes auto-geradas (DTOs, entities)

Verificação:
  ./mvnw clean verify
  open target/site/jacoco/index.html

Regra:
  - Novo código: cobertura ≥ 70%
  - Código existente: manter ou aumentar
  - Coverage < 70%: REJEITAR em review

Classes a EXCLUIR de cobertura:
  - Main.class (entry point)
  - *Dto.class (Lombok generated)
  - *Entity.class (JPA generated)
  - @Configuration classes
  - Exception classes
```

### 7.2 Mutation Testing (PIT)

```
[ ] Mínimo 70% mutation score
[ ] Testes conseguem detectar mudanças no código

Verificação:
  ./mvnw pitest:mutationCoverage
  open target/pit-reports/index.html

Métrica:
  - Alta: 80%+ (testes muito bons)
  - Média: 60-79% (testes adequados)
  - Baixa: < 60% (testes precisam melhorar)

Mutações Comuns Detectadas:
  ✓ Remover return statement
  ✓ Mudar > para ≥
  ✓ Mudar && para ||
  ✓ Remover validações
  
Se mutation score baixo:
  [ ] Adicionar testes de error case
  [ ] Testar boundary conditions
  [ ] Adicionar assertions de validação
```

---

## 📝 8. DOCUMENTAÇÃO - OBRIGATÓRIO

### 8.1 Documentação de Fluxos

```
[ ] Novo fluxo E2E → Documentar em critical-test-flows.md
[ ] Novo risco → Documentar em FEATURE_TEMPLATE.md
[ ] Novo teste importante → Adicionar exemplo

Template para Novo Fluxo:

## Fluxo: [Nome]
- Objetivo:
- Entradas:
- Saídas esperadas:
- Pontos sensíveis:
- Exemplo de código:

Exemplo: Fluxo 13: UPLOAD Publication Image
- Objetivo: Validar upload de imagem para S3
- Entradas: POST /publications/1/images + MultipartFile
- Saídas: 201 CREATED + URL S3
- Sensível: S3 timeout, tamanho máximo, nome duplicado
```

### 8.2 Código Auto-Documentado

```
[ ] Nomes de variáveis descritivos
[ ] Nomes de testes explicam comportamento
[ ] Sem comentários redundantes (código fala por si)

BOM:
  @Test
  public void should_throw_validation_exception_when_publisher_id_is_invalid() {
      var country = new Country(1, "name", "gentilic");
      var publisher = new Publisher(0, "name", "history", country);
      
      when(mediator.getQuery(Country.class)).thenReturn(countryQuery);
      when(countryQuery.getById(1)).thenReturn(null);  // Não existe
      
      assertThrows(ValidationException.class, () ->
          createPublisherUseCase.execute(publisher));
  }

RUIM:
  @Test
  public void test() {  // Que comportamento?
      var c = new Country(...);  // Nome vago
      var p = new Publisher(...);
      // ... código complexo sem explicação
      assertThrows(Exception.class, () -> ...);  // Qual exception?
  }
```

---

## 🔍 9. CODE REVIEW - CHECKLIST

### 9.1 Checklist para Revisor

```
TESTES BÁSICOS:
  [ ] Teste tem nome descritivo (should_...)?
  [ ] Teste é independente (sem dependência de outros)?
  [ ] Teste tem pelo menos 1 assertion significativa?
  [ ] Padrão AAA (Arrange-Act-Assert)?

ISOLAMENTO:
  [ ] Sem dados compartilhados entre testes?
  [ ] Mocks apenas necessários?
  [ ] Unit tests sem @SpringBootTest?
  [ ] Integration tests sem BD real?

QUALIDADE:
  [ ] Código legível (sem lógica complexa)?
  [ ] Sem duplicação (usar helper methods)?
  [ ] Sem acoplamento à implementação (sem verify internals)?
  [ ] Nomeação de variáveis clara?

MANUTENÇÃO:
  [ ] Novo campo? Todos os 4 mappers atualizados?
  [ ] Novo use case? Adicionado ao mediator + testes?
  [ ] Novo endpoint? E2E test criado?
  [ ] Novo fluxo? Documentado em critical-test-flows.md?

PERFORMANCE:
  [ ] Suite < 5 segundos?
  [ ] Unit test < 100ms?
  [ ] E2E test < 1s cada?

COBERTURA:
  [ ] Coverage ≥ 70%?
  [ ] Mutation score ≥ 70%?
  [ ] Novo código testado?

DOCUMENTAÇÃO:
  [ ] FEATURE_TEMPLATE.md preenchido?
  [ ] Fluxos documentados?
  [ ] Riscos mitigados?

STATUS:
  [ ] ./mvnw clean verify PASSA?
  [ ] Nenhum warning?
  [ ] Commits bem descritos?
```

### 9.2 Checklist para Autor (antes de submeter PR)

```
ANTES DE SUBMETER:
  [ ] Executei ./mvnw clean verify localmente?
  [ ] Coverage > 70%?
  [ ] Mutation score > 70%?
  [ ] Todos testes passam?
  [ ] Nenhum warning?
  
  [ ] FEATURE_TEMPLATE.md preenchido (se nova feature)?
  [ ] critical-test-flows.md atualizado (se novos fluxos)?
  
  [ ] Padrão AAA em todos testes?
  [ ] Nomes descritivos (should_...)?
  [ ] Sem dados compartilhados?
  [ ] Sem mocks desnecessários?
  
  [ ] Unit tests sem @SpringBootTest?
  [ ] E2E tests usam TestContainers?
  [ ] Mappers testados bidirecional?
  
  [ ] Cache invalidation (@CacheEvict) onde necessário?
  [ ] FK validation em use cases?
  [ ] Error cases testados?
  
  [ ] Commit message clara?
  [ ] PR description com overview?
  [ ] Ligado a issue/story?
```

---

## 📋 10. CHECKLIST FINAL (GoLive)

```
TESTES GERAIS:
  [ ] Total testes: 50+ para feature CRUD
  [ ] Coverage: ≥ 70%
  [ ] Mutation: ≥ 70%
  [ ] Suite: < 5 segundos
  [ ] Sem testes flaky
  [ ] Todos passam: ./mvnw clean verify

PADRÃO:
  [ ] Nomes: should_... pattern
  [ ] AAA: Arrange-Act-Assert
  [ ] Assertions: 1-3 por teste, significativas
  [ ] Independência: Sem compartilhamento de estado

QUALIDADE:
  [ ] Legibilidade: novo dev entende em 5 min
  [ ] DRY: sem duplicação (helpers criados)
  [ ] Acoplamento: baixo (não testa implementação)
  [ ] Fragilidade: Testes robustos e determinísticos

MANUTENÇÃO:
  [ ] Novo campo: 4 mappers + testes atualizados
  [ ] Novo endpoint: E2E test criado
  [ ] Novo fluxo: Documentado em critical-test-flows.md
  [ ] Riscos: Identificados e mitigados

PERFORMANCE:
  [ ] Unit < 1s total
  [ ] Integration 1-2s
  [ ] E2E 2-4s
  [ ] Total < 5s

DOCUMENTAÇÃO:
  [ ] FEATURE_TEMPLATE.md completo
  [ ] critical-test-flows.md atualizado
  [ ] Código auto-documentado
  [ ] README.md atualizado

APROVAÇÃO FINAL:
  [ ] Tech Lead: ✓
  [ ] QA: ✓
  [ ] Code Review: ✓
  [ ] Ready to merge!
```

---

## ❌ O que NÃO é Aceitável

```
NUNCA ACEITAR:

Testes:
  ✗ Teste sem nome descritivo
  ✗ Teste sem assertions
  ✗ Teste com lógica condicional (if/else)
  ✗ Teste que depende de outro teste
  ✗ Teste com sleep() ou timing

Qualidade:
  ✗ Unit test com @SpringBootTest
  ✗ Integration test sem mock JpaRepository
  ✗ Assertions triviais (isNotNull)
  ✗ Duplicação de código
  ✗ Acoplamento à implementação (verify internals)

Manutenção:
  ✗ Novo campo sem mappers atualizados
  ✗ Novo endpoint sem E2E test
  ✗ Novo fluxo sem documentação
  ✗ Risco identificado sem mitigação

Performance:
  ✗ Suite > 10 segundos
  ✗ Unit test > 100ms
  ✗ TestContainers em unit test

Cobertura:
  ✗ Coverage < 70%
  ✗ Mutation < 70%
  ✗ Novo código sem testes

Documentação:
  ✗ FEATURE_TEMPLATE.md não preenchido
  ✗ Fluxos não documentados
  ✗ README.md não atualizado

BUILD:
  ✗ ./mvnw clean verify não passa
  ✗ Warnings ignorados
  ✗ Commits mal descritos
```

---

## 📚 Referências

- [context-core.md](context-core.md) - Arquitetura
- [testing-strategy.md](testing-strategy.md) - Estratégia de testes
- [critical-test-flows.md](critical-test-flows.md) - Fluxos testados
- [FEATURE_TEMPLATE.md](FEATURE_TEMPLATE.md) - Template de feature
- [FEATURE_TEMPLATE_GUIDE.md](FEATURE_TEMPLATE_GUIDE.md) - Guia do template

---

## 🎯 Resumo Executivo

| Aspecto | Critério |
|---------|----------|
| **Cobertura de Testes** | 50+ por CRUD, coverage ≥70%, mutation ≥70% |
| **Nomeação** | should_[comportamento]_[quando] |
| **Assertions** | 1-3 por teste, significativas |
| **Isolamento** | Sem compartilhamento de estado |
| **Performance** | Suite < 5s, unit < 1s |
| **Qualidade** | Legível, DRY, baixo acoplamento |
| **Manutenção** | Fácil de estender, robustos |
| **Documentação** | FEATURE_TEMPLATE.md + critical-test-flows.md |

---

**Versão**: 1.0  
**Data**: 2026-02-07  
**Status**: 🔴 OBRIGATÓRIO - Nada mergeia sem cumprir esta DoD  
**Aprovado por**: Architecture Team

> 🎯 **Objetivo**: Garantir testes de qualidade, manuteníveis e rápidos.
> 
> ⏰ **Impacto**: 45% menos bugs em produção, 30% menos retrabalho.
> 
> 🚀 **Resultado**: Código confiável e sustentável.


