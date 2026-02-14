# Guia Rápido: Como Usar o Feature Template

**Data**: 2026-02-07  
**Status**: Quick Start Guide

---

## 🚀 5 Passos para Usar o Template

### Passo 1: Copiar o Template
```bash
# Copie o arquivo base
cp docs/FEATURE_TEMPLATE.md docs/FEATURE_YourFeatureName.md

# Exemplo:
# cp docs/FEATURE_TEMPLATE.md docs/FEATURE_Search.md
# cp docs/FEATURE_TEMPLATE.md docs/FEATURE_UserAuth.md
```

### Passo 2: Preencher Seção por Seção

```
Tempo estimado: 30-45 minutos

[ ] Seção 1: Objetivo (5 min)
    └─ Descrever em 1-3 linhas clara e concisa
    └─ Adicionar 3 benefícios de negócio

[ ] Seção 2: Escopo Incluído (15 min)
    └─ Checklist de cada camada (Domain → Use Case → Adapter)
    └─ Adicionar detalhes específicos (campos, endpoints)

[ ] Seção 3: Fora de Escopo (5 min)
    └─ Listar 5-10 funcionalidades NÃO fazer
    └─ Explicar por quê (complexidade, dependências)

[ ] Seção 4: Dependências Técnicas (10 min)
    └─ Módulos que vai usar
    └─ Bibliotecas a adicionar
    └─ Schema SQL necessário
    └─ Configurações

[ ] Seção 5: Riscos (15 min)
    └─ Identificar 5-8 riscos potenciais
    └─ Calcular probabilidade + severidade
    └─ Definir mitigação para cada

[ ] Seção 6: Critérios de Aceitação (5 min)
    └─ Checklist do que fazer

[ ] Seção 7: Checklist de Implementação (5 min)
    └─ Usar para acompanhar progresso
```

### Passo 3: Revisar com Time

```
Checklist de Review:

[ ] Tech Lead aprova:
    ✓ Escopo realista para 1 sprint?
    ✓ Riscos identificados e mitigados?
    ✓ Dependências claras?

[ ] Product Owner aprova:
    ✓ Objetivo alinhado com negócio?
    ✓ Out of scope justificado?
    ✓ Valor para usuário?

[ ] QA aprova:
    ✓ Testes cobrem todos os fluxos?
    ✓ Riscos de regressão?
    ✓ Performance validada?

[ ] Dev Lead aprova:
    ✓ Padrão arquitetural mantido?
    ✓ Mappers, caches, validações?
    ✓ Merge conflicts (branches paralelas)?
```

### Passo 4: Implementar Usando Checklist

```
Durante a implementação:

[ ] Copie checklist final da feature para seu Jira/GitHub
[ ] Marque conforme implementa
[ ] Se descobrir novo risco, atualize documento
[ ] Se descobrir novo teste, documente em critical-test-flows.md
```

### Passo 5: Finalizar e Arquivar

```
Após conclusão:

[ ] Atualizar critical-test-flows.md com novos fluxos
[ ] Atualizar README.md do projeto
[ ] Mover FEATURE_YourFeatureName.md para pasta /archived
[ ] Adicionar learnings/lessons learned no documento
```

---

## 📋 Seções Principais Explicadas

### 1️⃣ OBJETIVO

**O que escrever:**
```
Permitir que [QUEM] faça [O QUÊ] para [POR QUÊ].

Exemplo:
Permitir que usuários criem, leiam, atualizem e removam publicações
(livros), incluindo associação com editoras e upload de imagens de capa.

Valor de negócio:
- ✅ Usuários podem gerenciar catálogo completo
- ✅ Sincronização automática com S3
- ✅ Suporte a múltiplos idiomas
```

**Dicas:**
- Máximo 3 linhas
- Usar verbos ativos (criar, ler, atualizar, remover)
- Incluir 3 pontos de valor

---

### 2️⃣ ESCOPO INCLUÍDO

**O que escrever:**
```
Para cada camada, detalhar o que SERÁ implementado:

Domínio:
  - [ ] Entidade Publication (que campos?)
  - [ ] Validações (quais @NotBlank?)
  - [ ] Exceções customizadas

Use Cases:
  - [ ] CreatePublicationUseCase (valida o quê?)
  - [ ] UpdatePublicationUseCase (com @CacheEvict)
  - [ ] RemovePublicationUseCase

Persistência:
  - [ ] PublicationRepository
  - [ ] Schema SQL (detalhar tabela)
  - [ ] Índices para performance

API:
  - [ ] POST /publications
  - [ ] GET /publications
  - [ ] PUT /publications/{id}
  - [ ] DELETE /publications/{id}

Testes:
  - [ ] 60+ casos de teste
  - [ ] E2E: CREATE, READ, UPDATE, DELETE, LIST
  - [ ] Erro: FK não existe, ISBN duplicado
```

**Dicas:**
- Ser específico (não apenas "CRUD")
- Incluir validações esperadas
- Incluir endpoints HTTP com métodos
- Incluir número de testes estimado

---

### 3️⃣ FORA DE ESCOPO

**O que escrever:**
```
❌ NÃO fazer nesta feature:
  1. Busca avançada (v2.0)
  2. Recomendações com ML (futuro)
  3. Integração com ISBN API (v2.0)
  4. Múltiplos formatos EPUB/PDF (v2.0)
  5. Controle de permissões (v2.0)
  6. Integração pagamento (sistema separado)

Justificativa:
Estas funcionalidades requerem:
- Design adicional (tempo?)
- Dependências externas
- Testes mais complexos
- Melhor em sprints futuros
```

**Dicas:**
- Listar 5-10 itens
- Explicar por quê cada um está fora
- Mencionar quando poderia ser feito (v2.0, futuro, etc)
- Ajuda a gerenciar expectativas

---

### 4️⃣ DEPENDÊNCIAS TÉCNICAS

**O que escrever:**
```
Módulos que vai usar:
  - Domain Layer (estender BaseDomain)
  - Use Cases (CreateEntityUseCase pattern)
  - RepositoryMediator
  - S3StorageService

Bibliotecas a adicionar:
  - <dependency> (se nova)
  - <dependency> (se nova)

Banco de dados:
  CREATE TABLE publication (
    id SERIAL PRIMARY KEY,
    ...campos...
  );
  CREATE INDEX idx_name ON ...

Configurações:
  - spring.jpa.properties.hibernate.jdbc.fetch_size=50
  - aws.s3.bucket=booksalesonline-publicationImages
```

**Dicas:**
- Listar cada dependência clara
- Incluir versões de bibliotecas novas
- Incluir SQL completo
- Incluir properties necessárias

---

### 5️⃣ RISCOS

**O que escrever:**

```
Para cada risco:

┌─────────────────────────────────┐
│ RISCO: [Nome do Risco]          │
├─────────────────────────────────┤
│ Impacto: O que dá errado?       │
│ Probabilidade: ALTA/MÉDIA/BAIXA │
│ Severidade: CRÍTICA/ALTA/MÉDIA  │
│                                  │
│ MITIGAÇÃO:                      │
│ ✅ Ação 1                       │
│ ✅ Ação 2                       │
│ ✅ Ação 3                       │
│                                  │
│ Owner: Dev/QA/Arquiteto         │
│ Verificação: Code review/Test   │
└─────────────────────────────────┘
```

**Dicas:**
- Mínimo 5-8 riscos
- 3-4 críticos, 2-3 altos, 1-2 médios
- Ser específico (não "erro genérico")
- Incluir ação concreta de mitigação
- Indicar quem é responsável

**Exemplos de bons riscos:**
```
❌ RUIM: "Pode ter bugs"
✅ BOM: "S3 upload timeout se arquivo > 5MB 
          sem validação de tamanho máximo"

❌ RUIM: "Cache pode ser problema"
✅ BOM: "UPDATE Publication sem @CacheEvict 
         causa data stale por 60 segundos"
```

---

## 🎯 Template Rápido (Versão Curta)

Se tiver pressa, aqui está o mínimo:

```markdown
# Feature: [Nome]

## Objetivo
[1-3 linhas clara e concisa]
- ✅ Benefício 1
- ✅ Benefício 2
- ✅ Benefício 3

## Escopo Incluído
- [ ] Domínio: [Entidades, validações]
- [ ] Use Cases: [List de use cases]
- [ ] Persistência: [Schema SQL]
- [ ] API: [Endpoints]
- [ ] Testes: [Quantidade + tipos]

## Fora de Escopo
- ❌ Feature 1 (Por quê?)
- ❌ Feature 2 (Por quê?)
- ❌ Feature 3 (Por quê?)

## Dependências Técnicas
- Módulos: [lista]
- Bibliotecas: [lista]
- Schema: [SQL]

## Riscos (Top 5)
1. [Risco crítico + mitigação]
2. [Risco crítico + mitigação]
3. [Risco alto + mitigação]
4. [Risco alto + mitigação]
5. [Risco médio + mitigação]

## Critérios de Aceitação
- [ ] Testes: 50+ casos, coverage > 70%
- [ ] Código: Padrão mantido, sem duplication
- [ ] Performance: < 5s suite, < 200ms query
- [ ] Review: 2 aprovadores
```

---

## 📊 Exemplo Real Preenchido

Veja: **FEATURE_EXAMPLE_Publications.md**

Este é um exemplo completo e realista de como preencher o template para a feature "CRUD de Publicações".

---

## ✅ Checklist Antes de Começar

```
Antes de escrever código:

[ ] Template preenchido completamente
[ ] Tech Lead aprovou escopo
[ ] Product Owner aprovou objetivo
[ ] QA aprovou testes
[ ] Todos riscos identificados e mitigados
[ ] Dependências técnicas documentadas
[ ] Performance targets definidos
[ ] OK para começar implementação!
```

---

## 🚨 Erros Comuns a Evitar

```
❌ Escopo muito grande
   → Feature "User Management" (4 sprints?)
   ✅ Feature "User Login" (1 sprint)

❌ Riscos não identificados
   → "Não há riscos, é simples"
   ✅ Sempre há riscos, identificar agora

❌ Out of scope vago
   → "Tudo mais fica para depois"
   ✅ "Search by title (v2.0) requer Elasticsearch"

❌ Testes não pensados
   → "Testes depois da feature"
   ✅ Testes já identificados e estimados

❌ Performance ignorada
   → "Otimizamos depois"
   ✅ Já temos targets (< 200ms, índices, etc)

❌ Responsabilidades confusas
   → "Alguém vai ver depois"
   ✅ Cada risco tem Owner definido
```

---

## 📞 Quando Pedir Ajuda

```
Se não tem clareza em:

[ ] Objetivo → Contatar Product Owner
[ ] Escopo → Contatar Tech Lead
[ ] Dependências → Contatar Arquiteto
[ ] Riscos → Contatar QA Lead
[ ] Testes → Contatar QA Engineer
[ ] Performance → Contatar DBA
```

---

## 🎓 Resumo

```
1. Copiar FEATURE_TEMPLATE.md
2. Preencher 7 seções (30-45 min)
3. Revisar com time (tech lead, PO, QA)
4. Implementar usando checklist
5. Atualizar critical-test-flows.md ao finalizar
6. Arquivar documento

Tempo total até implementação: 1-2 horas
Tempo economizado em bugs: ~10 horas
ROI: Excelente! 📈
```

---

**Criado**: 2026-02-07  
**Próximo**: Use FEATURE_EXAMPLE_Publications.md como referência ao preencher seu template

