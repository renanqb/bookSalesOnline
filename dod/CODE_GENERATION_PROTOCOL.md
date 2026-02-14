# Protocolo de Avaliação Arquitetural
## Para Geração de Código Novo

**Data de Criação**: 2026-02-07  
**Versão**: 1.0  
**Status**: Guia Obrigatório para Geração de Código

---

> Sempre que código novo for gerado (features, testes, utils), este protocolo DEVE ser seguido.
> Garante conformidade com regras arquiteturais e padrões do projeto.

---

## 📋 Checklist Obrigatório (4 Partes)

### Parte 1: Módulo Alvo

**O que responder:**
```
- [ ] Qual é o módulo (Domain, Application, Adapter, Infrastructure)?
- [ ] Caminho completo do pacote (com.renan.booksalesonline....)
- [ ] Nome da classe/arquivo
- [ ] Tipo de código (Entity, UseCase, Repository, etc)
```

**Exemplo:**
```
Módulo: APPLICATION
Pacote: com.renan.booksalesonline.application.usecases.publication
Classe: CreatePublicationUseCaseImpl
Tipo: Use Case Implementation
```

---

### Parte 2: Justificativa

**O que responder:**
```
- [ ] Por que este módulo é correto para este código?
- [ ] Qual responsabilidade ele tem?
- [ ] Por que não está em outro lugar?
- [ ] Referência à arquitetura (qual seção de ARCHITECTURAL_RULES.md)
```

**Exemplo:**
```
Justificativa:
✓ CreatePublicationUseCaseImpl implementa a lógica de criação de publicações
✓ Deve estar em APPLICATION (Application Layer executa orquestração)
✓ NÃO pode estar em:
  - Domain (não é entidade)
  - Controller (não recebe HTTP)
  - Repository (não persiste direto)
✓ Referência: ARCHITECTURAL_RULES.md Seção 4 (Localização de Lógica)
  "Orquestração (múltiplos steps) → Use Case"
```

---

### Parte 3: Dependências

**O que responder:**
```
- [ ] Quais classes/interfaces precisa importar?
- [ ] De qual módulo vêm?
- [ ] São permitidas pela arquitetura?
- [ ] Há ciclos potenciais?
```

**Exemplo:**
```
Dependências Necessárias:
1. RepositoryMediator (Application layer)
   └─ Permitido? ✓ SIM (será refatorado para DI futuro)
   └─ Ciclo? ✗ NÃO

2. Publication (Domain layer)
   └─ Permitido? ✓ SIM (Use Case depende de Domain)
   └─ Ciclo? ✗ NÃO

3. DataCommand<Publication> (Ports)
   └─ Permitido? ✓ SIM (Use Case usa ports, não concreto)
   └─ Ciclo? ✗ NÃO

4. ValidationException (Domain exception)
   └─ Permitido? ✓ SIM (exceção de domínio)
   └─ Ciclo? ✗ NÃO

Validação: ✅ TODAS permitidas, sem ciclos
```

---

### Parte 4: Verificação de Regras

**O que responder:**
```
- [ ] Não viola nenhuma regra de ARCHITECTURAL_RULES.md?
- [ ] Está aderente aos padrões do projeto?
- [ ] Passa nos critérios da DEFINITION_OF_DONE?
- [ ] Localização de lógica está correta?
```

**Exemplo Checklist:**
```
Verificação contra ARCHITECTURAL_RULES.md:

DEPENDÊNCIAS PERMITIDAS (Seção 2):
  [✓] Application pode depender de Domain
  [✓] Application pode depender de Ports
  [✓] Application NÃO depende de Infrastructure
  [✓] Application NÃO depende de Controllers

DEPENDÊNCIAS PROIBIDAS (Seção 3):
  [✓] NÃO há Business Logic em Repository
  [✓] NÃO há Controller acessando BD direto
  [✓] NÃO há ciclos de dependência
  [✓] NÃO há leakage de implementação

LOCALIZAÇÃO DE LÓGICA (Seção 4):
  [✓] Orquestração está em Use Case ✓
  [✓] Validação FK está em Use Case ✓
  [✓] Persistência está em Repository ✓
  [✓] Transformação DTO está em Mapper ✓

ANTI-PADRÕES (Seção 3):
  [✓] NÃO é Service Locator problemático
  [✓] NÃO é God Object
  [✓] NÃO é Tight Coupling
  [✓] NÃO é Leaky Abstraction
  [✓] NÃO tem MISSING ERROR HANDLING

TESTES (Seção 5):
  [✓] Se testes, tipo correto (Unit não E2E)
  [✓] Se testes, usar @Mock não @Autowired
  [✓] Se testes, sem @SpringBootTest
  [✓] Se testes, mínimo 50+ casos para CRUD

RESULTADO: ✅ PASSA EM TODAS AS REGRAS
```

---

## 🔍 Template Padrão de Resposta

Quando gerar código novo, responder assim:

```markdown
## Avaliação Arquitetural do Código Gerado

### 1. Módulo Alvo
- **Módulo**: [Domain/Application/Adapter/Infrastructure]
- **Pacote**: com.renan.booksalesonline.[path]
- **Classe**: [NomeDaClasse]
- **Tipo**: [Entity/UseCase/Repository/Controller/etc]

### 2. Justificativa
[Explicar por que este é o local correto, referenciar ARCHITECTURAL_RULES.md]

### 3. Dependências
| Dependência | Origem | Permitida? | Ciclo? |
|-------------|--------|-----------|--------|
| [Classe] | [Módulo] | ✓/✗ | ✓/✗ |

**Validação**: [✅ TODAS permitidas / ❌ VIOLAÇÃO]

### 4. Verificação de Regras
- [✓/✗] Não viola ARCHITECTURAL_RULES.md
- [✓/✗] Aderente à arquitetura
- [✓/✗] Passa em DEFINITION_OF_DONE
- [✓/✗] Localização de lógica correta

**Status**: [✅ APROVADO / ❌ REQUER REFATORAÇÃO]
```

---

## 📊 Exemplos de Avaliação

### Exemplo 1: ✅ APROVADO - CreatePublicationUseCase

```
## Avaliação Arquitetural

### 1. Módulo Alvo
- **Módulo**: APPLICATION
- **Pacote**: com.renan.booksalesonline.application.usecases.publication
- **Classe**: CreatePublicationUseCaseImpl
- **Tipo**: Use Case Implementation

### 2. Justificativa
CreatePublicationUseCaseImpl implementa a lógica de criação de publicações (livros).
Deve estar em APPLICATION porque:
✓ Orquestração de múltiplos steps (validar publisher, persistir, etc)
✓ Referência ARCHITECTURAL_RULES.md Seção 4: "Orquestração → Use Case"
✓ NÃO é Domain (não é entidade)
✓ NÃO é Repository (não acessa BD direto)
✓ NÃO é Controller (não recebe HTTP)

### 3. Dependências
| Dependência | Origem | Permitida? | Ciclo? |
|-------------|--------|-----------|--------|
| RepositoryMediator | APPLICATION | ✓ | ✗ |
| Publication | DOMAIN | ✓ | ✗ |
| DataCommand<Publication> | APPLICATION (Port) | ✓ | ✗ |
| ValidationException | DOMAIN | ✓ | ✗ |

**Validação**: ✅ TODAS permitidas, sem ciclos

### 4. Verificação de Regras
- [✓] Não viola ARCHITECTURAL_RULES.md Seção 2 (dependências permitidas)
- [✓] Aderente: Application depende de Domain ✓
- [✓] Passa em DEFINITION_OF_DONE: Testes unitários com mocks
- [✓] Localização correta: Orquestração em Use Case

**Status**: ✅ APROVADO - Gerar código conforme padrão
```

---

### Exemplo 2: ❌ REQUER REFATORAÇÃO - CreatePublicationController

```
## Avaliação Arquitetural

### 1. Módulo Alvo
- **Módulo**: ADAPTER (Controllers)
- **Pacote**: com.renan.booksalesonline.adapters.controllers.v1
- **Classe**: PublicationController
- **Tipo**: REST Controller

### 2. Justificativa
PublicationController é entrada HTTP para operações de publicação.
Deve estar em ADAPTER porque:
✓ Adapta requisição HTTP → Domain
✓ Adapta Domain → resposta DTO
✓ Referência ARCHITECTURAL_RULES.md Seção 4: "HTTP Response → Controller"
✓ NÃO contém business logic (vai para Use Case)
✓ NÃO acessa BD direto (passa por Use Case)

### 3. Dependências
| Dependência | Origem | Permitida? | Ciclo? |
|-------------|--------|-----------|--------|
| PublicationDtoMapper | ADAPTER | ✓ | ✗ |
| PublicationDto | ADAPTER | ✓ | ✗ |
| CreatePublicationUseCase | APPLICATION | ⚠️ VIA MEDIATOR | ✗ |

**Validação**: ✅ Permitidas, sem ciclos (mediator via APPLICATION)

### 4. Verificação de Regras
- [✓] Não viola ARCHITECTURAL_RULES.md
- [✓] Aderente: Controller depende de UseCase via Mediator
- [⚠️] ATENÇÃO: Usar mediator temporário, refatorar para DI futuro
- [✓] Localização correta: HTTP Response em Controller

**Status**: ✅ APROVADO - Gerar com anotação "REFATORAR MEDIATOR FUTURO"
```

---

### Exemplo 3: ❌ VIOLAÇÃO - Publication com JPA Annotation

```
## Avaliação Arquitetural

### 1. Módulo Alvo
- **Módulo**: DOMAIN
- **Pacote**: com.renan.booksalesonline.domain
- **Classe**: Publication
- **Tipo**: Domain Entity

### 2. Justificativa
Publication é entidade de domínio (pura, sem dependências técnicas).
Deve estar em DOMAIN com:
✓ Dados e regras de negócio
✓ Validações (@NotBlank, @NotNull)
✓ Referência ARCHITECTURAL_RULES.md Seção 2: "Domain não depende de Spring/JPA"

### 3. Dependências
| Dependência | Origem | Permitida? | Ciclo? |
|-------------|--------|-----------|--------|
| javax.persistence.Entity | JPA | ❌ NÃO | ✗ |
| javax.persistence.Column | JPA | ❌ NÃO | ✗ |

**Validação**: ❌ VIOLAÇÃO - Domain NÃO pode depender de JPA

### 4. Verificação de Regras
- [✗] VIOLA ARCHITECTURAL_RULES.md Seção 2: "Domain sem Spring/JPA"
- [✗] NÃO aderente: Domain com anotações JPA
- [✗] NÃO passa em checklist

**Status**: ❌ REJEITAR - Refatoração necessária

**Solução Correta**:
1. Publication: Domain pura (extends BaseDomain)
2. PublicationEntity: @Entity (JPA)
3. PublicationEntityMapper: traduz entre eles
4. Repository: usa mapper para conversão
```

---

## 🛠️ Como Usar Este Protocolo

### Ao Gerar Código Novo:

```
1. ANTES de escrever qualquer código:
   ✓ Identificar módulo alvo
   ✓ Justificar localização
   ✓ Listar dependências
   ✓ Verificar regras

2. DURANTE a geração:
   ✓ Incluir esta avaliação na resposta
   ✓ Ser explícito sobre decisões arquiteturais
   ✓ Listar potenciais refatorações futuras

3. DEPOIS de gerar:
   ✓ Validar contra ARCHITECTURAL_RULES.md
   ✓ Validar contra DEFINITION_OF_DONE.md
   ✓ Indicar se passa em testes automatizados
```

### Ao Revisar Código Gerado:

```
Code Reviewer deve validar:
1. Módulo está correto?
2. Justificativa faz sentido?
3. Dependências estão permitidas?
4. Nenhuma regra foi violada?

Se falhar em qualquer ponto:
→ Rejeitar e pedir refatoração
→ Referenciar ARCHITECTURAL_RULES.md
→ Explicar regra violada
```

---

## ✅ Checklist Rápida

Antes de gerar código, validar:

```
[ ] Identifiquei o módulo correto (Domain/App/Adapter/Infra)?
[ ] Justifiquei por que este é o local?
[ ] Listei todas as dependências?
[ ] Verifiquei contra ARCHITECTURAL_RULES.md?
[ ] Não há ciclos de dependência?
[ ] Localização de lógica está correta?
[ ] Não viola anti-padrões?
[ ] Testes estarão no local correto?

✅ OK para gerar!
```

---

## 🔗 Referências Obrigatórias

Sempre consultar ao gerar código:

1. **ARCHITECTURAL_RULES.md**
   - Seção 1: Estrutura de camadas
   - Seção 2: Dependências permitidas
   - Seção 3: Dependências proibidas
   - Seção 4: Localização de lógica

2. **DEFINITION_OF_DONE.md**
   - Seção de testes obrigatórios
   - Critérios de cobertura

3. **context-map-testing.md**
   - Módulos e suas responsabilidades

4. **testing-strategy.md**
   - Tipo de teste por camada

---

## 📝 Exemplo de Resposta Completa

Quando você me pedir para gerar código, responderei assim:

```markdown
# Avaliação Arquitetural: [Nome da Feature]

## 1. Módulo Alvo
- **Módulo**: APPLICATION
- **Pacote**: com.renan.booksalesonline.application.usecases.publication
- **Classe**: CreatePublicationUseCaseImpl
- **Tipo**: Use Case

## 2. Justificativa
[Explicação clara de por que aqui]

## 3. Dependências
[Tabela de dependências]

## 4. Verificação de Regras
[Checklist de validação]

**Status**: ✅ APROVADO / ❌ REQUER REFATORAÇÃO

---

[CÓDIGO GERADO]
```

---

## 🎯 Objetivo

Este protocolo garante que:
- ✅ Código gerado está no módulo correto
- ✅ Dependências são permitidas
- ✅ Nenhuma regra arquitetural é violada
- ✅ Padrões do projeto são mantidos
- ✅ Novo dev entende decisões arquiteturais
- ✅ Code review fica mais rápida
- ✅ Arquitetura se degrada zero ao tempo

---

**Versão**: 1.0  
**Data**: 2026-02-07  
**Status**: 🔴 OBRIGATÓRIO - Usar em TODA geração de código novo  
**Enforcement**: Code Review + ArchUnit validation

> Conformidade arquitetural é não-negociável.
> Este protocolo garante integridade do design. 🏗️

