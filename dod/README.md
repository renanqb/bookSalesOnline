# 📚 Documentação de Contexto e Testes - BookSalesOnline

## Arquivos Criados (2026-02-07)

Esta coleção de documentos foi criada para fornecer orientação clara sobre a arquitetura do sistema e estratégia de testes automatizados.

---

## 📄 Documentos Disponíveis

### 1. **context-core.md** 
**Análise Macro da Arquitetura**

Propósito: Compreender a estrutura geral do projeto.

Contém:
- Tipo de aplicação e stack tecnológica
- Arquitetura Hexagonal + Clean Architecture
- Organização de camadas (Domain, Application, Adapters)
- Convenções de nomenclatura
- Anti-padrões identificados
- Roadmap de refatoração

**Quando ler**: Na onboarding ou para entender decisões arquiteturais.

---

### 2. **testing-strategy.md**
**Estratégia Detalhada de Automação de Testes**

Propósito: Aprender como testar cada tipo de componente.

Contém:
- 4 níveis de testes (Unit, Integration, E2E, Architecture)
- Exemplos reais de código de teste
- Padrão AAA (Arrange-Act-Assert)
- Uso de mocks, stubs e fakes (Mockito)
- Critérios FIRST (Fast, Isolated, Repeatable, Self-checking, Thorough)
- Anti-padrões e checklist de qualidade
- Próximas melhorias

**Quando ler**: Antes de escrever testes ou melhorar cobertura.

---

### 3. **context-map-testing.md** ⭐ NOVO
**Mapa de Contexto - 10 Módulos com Foco em Testes**

Propósito: Saber **o que** e **como** testar em cada módulo.

Contém:
- 10 módulos do sistema detalhados:
  1. Domain (entidades de negócio)
  2. Use Cases (orquestração)
  3. Mediators (service locator - antipadrão)
  4. Controllers (entrada HTTP)
  5. Repositories (persistência)
  6. Data (JpaRepository)
  7. Mappers (transformação)
  8. Entities (modelos JPA)
  9. Storage (AWS S3)
  10. Containers (TestContainers)

Para cada módulo:
- ✅ Responsabilidade
- ✅ Dependências diretas
- ✅ Pontos de extensão (como testar)
- ❌ Acessos proibidos
- 📋 Exemplos de código

**Quando ler**: Ao adicionar nova feature ou testar módulo específico.

---

## 🎯 Como Usar Esta Documentação

### Cenário 1: "Sou novo no projeto"
```
1. Leia: context-core.md (visão geral)
2. Leia: testing-strategy.md (como testamos)
3. Consulte: context-map-testing.md (quando implementar)
```

### Cenário 2: "Preciso testar um componente"
```
1. Vá para: context-map-testing.md
2. Procure: o módulo do seu componente
3. Use: exemplos de código e checklist
```

### Cenário 3: "Quero entender um anti-padrão"
```
1. Leia: context-core.md (seção Anti-padrões)
2. Consulte: testing-strategy.md (para refatoração)
3. Verifique: roadmap de melhorias
```

### Cenário 4: "Qual a velocidade esperada dos testes?"
```
Vá para: testing-strategy.md → Seção "Velocidades Esperadas"
Ou: context-map-testing.md → Tabela de Matriz de Teste
```

---

## 🔗 Matriz de Decisão Rápida

**Preciso testar... qual documento consulto?**

| Necessidade | Documento | Seção |
|-------------|-----------|-------|
| Entendo a arquitetura? | context-core.md | Arquitetura Geral |
| Como faço um unit test? | testing-strategy.md | Tipos de Testes |
| Como faço um E2E test? | testing-strategy.md | Testes Funcionais |
| Qual módulo testar? | context-map-testing.md | Módulos 1-10 |
| Qual padrão evitar? | context-core.md | Anti-padrões |
| O teste está bom? | testing-strategy.md | Critérios de Qualidade |
| Erro ao mockar mediator? | context-map-testing.md | Módulo 3 |
| Preciso de containers? | context-map-testing.md | Módulo 10 |

---

## 📊 Estatísticas da Documentação

```
context-core.md           ~3500 palavras
testing-strategy.md       ~5000 palavras
context-map-testing.md    ~6000 palavras
────────────────────────────────────
Total                     ~14.500 palavras
```

**Tempo de leitura**:
- Visão geral (context-core): ~15 min
- Estratégia completa (testing-strategy): ~20 min
- Mapa detalhado (context-map-testing): ~30 min
- **Total recomendado**: 45-60 min (onboarding)

---

## ✅ Checklist de Implementação

Use este checklist ao adicionar novo componente:

```
[ ] Li context-map-testing.md para meu módulo
[ ] Identifiquei:
    [ ] Responsabilidade do módulo
    [ ] Dependências diretas
    [ ] O que NÃO pode acessar
[ ] Planejei testes:
    [ ] Unit tests (se aplicável)
    [ ] Integration tests (se aplicável)
    [ ] E2E tests (se aplicável)
[ ] Implementei testes:
    [ ] Nomenclatura clara (should_...)
    [ ] Padrão AAA (Arrange-Act-Assert)
    [ ] Sem código duplicado
    [ ] Sem mocks desnecessários
[ ] Validei qualidade:
    [ ] Cobertura > 70%
    [ ] Todos os happy paths + error cases
    [ ] Execution < 100ms (unit/integration)
    [ ] Determinístico (sem dependência de hora/sistema)
```

---

## 🚀 Próximos Passos

### Curto Prazo
- [ ] Familiarizar-se com documentação
- [ ] Executar testes existentes: `./mvnw test`
- [ ] Verificar cobertura: `./mvnw clean verify`

### Médio Prazo
- [ ] Refatorar mediators para Dependency Injection (context-core.md)
- [ ] Implementar @RestControllerAdvice global (context-core.md)
- [ ] Adicionar stratégia de cache invalidation (context-core.md)
- [ ] Renomear `commom/` → `common/`

### Longo Prazo
- [ ] Implementar testes de arquitetura (ArchUnit)
- [ ] Implementar Custom Assertions para domain
- [ ] Testes de contrato (Contract Testing)
- [ ] Testes de carga (Load Testing)

---

## 📞 Referências e Links

### Documentação Técnica
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [AssertJ Documentation](https://assertj.github.io/assertj-core-features-highlight.html)
- [TestContainers User Guide](https://www.testcontainers.org/)

### Padrões e Arquitetura
- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Hexagonal Architecture - Alistair Cockburn](https://alistair.cockburn.us/hexagonal-architecture/)
- [Domain-Driven Design - Eric Evans](https://en.wikipedia.org/wiki/Domain-driven_design)
- [Behavior-Driven Development](https://en.wikipedia.org/wiki/Behavior-driven_development)

---

## 💡 Dicas Rápidas

### Ao encontrar erro em teste:
1. Verifique se está usando a ferramenta correta (mock vs stub vs fake)
2. Valide padrão AAA (Arrange-Act-Assert)
3. Procure por code smell (compartilhamento de estado, mocks desnecessários)

### Ao revisar código:
1. Verifique se teste tem nome descritivo
2. Valide isolamento (sem dependência de outros testes)
3. Confirme assertions significativas

### Ao adicionar feature:
1. Comece com unit test
2. Adicione integration test se necessário
3. Finalize com E2E test
4. Sempre rodar: `./mvnw clean verify`

---

## 📝 Histórico de Versões

| Versão | Data | Mudanças |
|--------|------|----------|
| 1.0 | 2026-02-07 | Documentação inicial criada |

---

## 👥 Mantido por

**Equipe de Arquitetura e QA**

Para dúvidas, abrir issue com tag `documentation` ou `testing`.

---

**Última Atualização**: 2026-02-07  
**Status**: Ativo e em manutenção

