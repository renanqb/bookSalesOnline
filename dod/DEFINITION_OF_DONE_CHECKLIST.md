# Definition of Done - Checklist Rápida

**Para imprimir e usar durante desenvolvimento**

---

## ✅ ANTES DE SUBMETER PR

### Executar Localmente
```bash
[ ] ./mvnw clean verify
[ ] Coverage ≥ 70%
[ ] Mutation ≥ 70%
[ ] Suite < 5 segundos
[ ] Sem warnings
```

### Testes
```
[ ] 50+ testes para feature CRUD
    [ ] 20+ unit tests
    [ ] 15+ integration tests
    [ ] 15+ E2E tests

[ ] Cada teste tem:
    [ ] Nome: should_...
    [ ] Padrão: AAA (Arrange-Act-Assert)
    [ ] 1-3 assertions significativas
    [ ] Independência (sem dependência de outros)

[ ] Coverage:
    [ ] Domain entities
    [ ] Use cases (happy + error)
    [ ] Mappers (bidirecional)
    [ ] Repositories (mock JpaRepository)
    [ ] Controllers (E2E)
```

### Qualidade
```
[ ] Legibilidade:
    [ ] Código teste legível?
    [ ] Sem loops/condicionais?
    [ ] Máximo 15 linhas por teste?

[ ] DRY:
    [ ] Sem duplicação?
    [ ] Helper methods criados?

[ ] Acoplamento:
    [ ] Testa comportamento (não implementação)?
    [ ] Sem verify() internals?

[ ] Fragilidade:
    [ ] Sem sleep()?
    [ ] Sem now()?
    [ ] Determinístico?
```

### Isolamento
```
[ ] Unit tests:
    [ ] Sem @SpringBootTest?
    [ ] Com @Mock (não @Autowired)?
    [ ] < 1ms esperado?

[ ] Integration:
    [ ] Mock JpaRepository?
    [ ] Sem BD real?
    [ ] 1-10ms esperado?

[ ] E2E:
    [ ] @SpringBootTest + TestContainers?
    [ ] CRUD completo?
    [ ] Fluxos documentados?
```

### Manutenção
```
[ ] Novo campo?
    [ ] Entity atualizada
    [ ] DTO atualizada
    [ ] Domain atualizada
    [ ] EntityMapper (2 direções)
    [ ] DtoMapper (2 direções)
    [ ] Testes de mapper

[ ] Novo endpoint?
    [ ] E2E test criado
    [ ] Fluxo documentado em critical-test-flows.md

[ ] Novo use case?
    [ ] Adicionado ao mediator
    [ ] Testes criados (mock)

[ ] Novo risco?
    [ ] Documentado em FEATURE_TEMPLATE.md
    [ ] Mitigação identificada
```

### Documentação
```
[ ] FEATURE_TEMPLATE.md?
    [ ] Objetivo preenchido
    [ ] Escopo incluído detalhado
    [ ] Out of scope explicado
    [ ] Dependências listadas
    [ ] Riscos identificados + mitigação
    [ ] Critérios de aceitação

[ ] critical-test-flows.md?
    [ ] Novo fluxo documentado
    [ ] Objetivo do fluxo
    [ ] Entradas/saídas
    [ ] Pontos sensíveis

[ ] README.md?
    [ ] Nova funcionalidade explicada
    [ ] Exemplos de uso

[ ] Código?
    [ ] Nomes descritivos
    [ ] Auto-documentado
    [ ] Sem comentários redundantes
```

---

## 🔍 CODE REVIEW - CHECKLIST

### Revisor Verificar

```
NOME DO TESTE:
  [ ] should_... pattern?
  [ ] Descreve comportamento?

ESTRUTURA:
  [ ] Arrange (setup)
  [ ] Act (executa)
  [ ] Assert (valida)

ASSERTIONS:
  [ ] Tem pelo menos 1?
  [ ] São significativas?
  [ ] Não apenas isNotNull()?
  [ ] 1-3 por teste (máximo 5)?

ISOLAMENTO:
  [ ] Independente de outros testes?
  [ ] Dados criados localmente?
  [ ] Sem variáveis static?

MOCKS:
  [ ] Apenas necessários?
  [ ] Não redundantes?
  [ ] Bem descritos?

QUALIDADE:
  [ ] Legível?
  [ ] Sem loops/if?
  [ ] < 15 linhas?
  [ ] Nomes claros?

VELOCIDADE:
  [ ] Unit < 100ms?
  [ ] Integration 1-10ms?
  [ ] E2E < 1s?

COBERTURA:
  [ ] Novo código testado?
  [ ] Coverage mantido/aumentado?

MAPPERS:
  [ ] Novo campo → 4 mappers atualizados?
  [ ] Bidirecional testado?

ENDPOINTS:
  [ ] Novo endpoint → E2E test?
  [ ] Status HTTP correto?
  [ ] Error cases testados?

CACHE:
  [ ] UPDATE → @CacheEvict?
  [ ] DELETE → @CacheEvict?

VALIDAÇÕES:
  [ ] FK validation em use case?
  [ ] Error cases com exceptions?
```

---

## 🚫 NUNCA ACEITAR

```
TESTES:
  [ ] ✗ Teste sem nome descritivo
  [ ] ✗ Teste sem assertions
  [ ] ✗ Teste com if/else ou loops
  [ ] ✗ Teste que depende de outro
  [ ] ✗ Teste com sleep() ou now()

QUALIDADE:
  [ ] ✗ Unit test com @SpringBootTest
  [ ] ✗ Integration sem mock JpaRepository
  [ ] ✗ assertThat(...).isNotNull()
  [ ] ✗ Duplicação de código
  [ ] ✗ verify() para internals

PERFORMANCE:
  [ ] ✗ Suite > 10 segundos
  [ ] ✗ Unit test > 100ms
  [ ] ✗ TestContainers em unit

COBERTURA:
  [ ] ✗ Coverage < 70%
  [ ] ✗ Mutation < 70%
  [ ] ✗ Novo código sem testes

MANUTENÇÃO:
  [ ] ✗ Novo campo sem mappers
  [ ] ✗ Novo endpoint sem E2E
  [ ] ✗ Novo fluxo sem doc
  [ ] ✗ Risco sem mitigação

BUILD:
  [ ] ✗ ./mvnw clean verify não passa
  [ ] ✗ Warnings ignorados
  [ ] ✗ Commits mal descritos
```

---

## 📊 MÉTRICAS

```
TESTES
  Unit tests:        20+ por CRUD
  Integration:       15+ por CRUD
  E2E tests:         15+ por CRUD
  Total:             50+

VELOCIDADE
  Unit:              < 1s total
  Integration:       1-2s
  E2E:               2-4s
  TOTAL SUITE:       < 5s

COBERTURA
  Code coverage:     ≥ 70%
  Mutation score:    ≥ 70%

NOME
  Padrão:            should_[comportamento]_[quando]
  Comprimento:       50-80 caracteres
  Clareza:           Novo dev entende em 5 min

ASSERTIONS
  Por teste:         1-3 (máximo 5)
  Tipo:              Significativas, não óbvias
  Exemplo:           assertThat(status).isEqualTo(CREATED)

QUALIDADE
  Duração máx:       15 linhas por teste
  Índice Leitura:    Sem lógica condicional
  Acoplamento:       Testa output, não implementação
```

---

## 🎯 DECISÃO RÁPIDA

**Este teste passa na DoD?**

```
1. Tem nome should_...?
   NÃO → FALHA
   SIM  ↓

2. Tem assertions significativas (1-3)?
   NÃO → FALHA
   SIM  ↓

3. Independente (sem dependência de outro)?
   NÃO → FALHA
   SIM  ↓

4. Rápido (< 100ms unit, < 500ms E2E)?
   NÃO → FALHA
   SIM  ↓

5. Legível (sem if/loops, < 15 linhas)?
   NÃO → FALHA
   SIM  ↓

6. Padrão AAA (Arrange-Act-Assert)?
   NÃO → FALHA
   SIM  ✅ PASSA
```

---

## 📋 TEMPLATES RÁPIDOS

### Unit Test
```java
@ExtendWith(MockitoExtension.class)
public class [Entity]UseCaseTest {
    @Mock private Dependency dep;
    @InjectMocks private [Entity]UseCase useCase;
    
    @Test
    public void should_[comportamento]_when_[condição]() {
        // ARRANGE
        var input = new Entity(...);
        when(dep.method(...)).thenReturn(...);
        
        // ACT
        var result = useCase.execute(input);
        
        // ASSERT
        assertThat(result).isEqualTo(expected);
    }
}
```

### Integration Test
```java
@ExtendWith(MockitoExtension.class)
public class [Entity]RepositoryTest {
    @Mock private JpaRepository data;
    @InjectMocks private [Entity]Repository repo;
    
    @Test
    public void should_[comportamento]() {
        // ARRANGE
        var entity = new [Entity](...);
        when(data.save(...)).thenReturn(...);
        
        // ACT
        var result = repo.save(entity);
        
        // ASSERT
        assertThat(result).isEqualTo(expected);
    }
}
```

### E2E Test
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class [Entity]ControllerTest extends BookSalesOnlineContainerTest {
    @Autowired private RestClientTesting rest;
    
    @Test
    @Order(1)
    public void should_[comportamento]() {
        // ARRANGE
        var dto = new [Entity]Dto(...);
        
        // ACT
        var response = rest.post([Entity]Dto.class, "endpoint", dto);
        
        // ASSERT
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}
```

---

**Versão**: 1.0  
**Data**: 2026-02-07  
**Imprimir e colar na parede!** 📌

