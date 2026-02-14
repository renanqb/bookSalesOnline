# Plano de Prompts com Templates Markdown

Este documento contém **cada prompt sugerido** no plano original, acompanhado de um **template padrão de Markdown** para salvar o resultado e reutilizá-lo como contexto em novas sessões do Copilot.

---

## 1️⃣ Refinamento antes da atividade

### Prompt 1.1 – Análise Macro do Repositório

```text
Analise este repositório como se você fosse um arquiteto de software responsável por manter consistência técnica.

Identifique:
- Tipo de aplicação
- Stack tecnológica e versões
- Padrões arquiteturais predominantes
- Convenções de nomenclatura
- Responsabilidades por camada/pasta
- Anti-padrões que NÃO devem ser repetidos

Responda de forma estruturada e objetiva.
```

#### Template Markdown – Resultado

```md
# Análise Macro do Projeto

## Tipo de Aplicação

## Stack Tecnológica
- Linguagem:
- Frameworks:
- Ferramentas de teste:

## Arquitetura Geral
- Padrão predominante:
- Organização por camadas/módulos:

## Convenções
- Nomenclatura:
- Organização de pastas:

## Responsabilidades por Camada
- Camada X:
- Camada Y:

## Anti-padrões Identificados
- ❌
- ❌
```

---

### Prompt 1.2 – Estratégia de Testes

```text
Analise especificamente a estratégia de testes desta aplicação.

Explique:
- Tipos de testes existentes
- Critérios de isolamento
- Uso de mocks, stubs ou fakes
- Estratégia de setup/teardown
- Padrão de assertions
- O que caracteriza um “bom teste” neste projeto
```

#### Template Markdown – Resultado

```md
# Estratégia de Testes

## Tipos de Testes
- Unitários:
- Integração:
- E2E:

## Isolamento

## Dobles de Teste
- Mocks:
- Stubs/Fakes:

## Setup / Teardown

## Assertions

## Critérios de Qualidade de um Bom Teste
```

---

## 2️⃣ Mapa de Contexto para Automações

### Prompt 2.1 – Context Map

```text
Crie um mapa de contexto do sistema com foco em automação de testes.

Para cada módulo/componente, descreva:
- Responsabilidade principal
- Dependências diretas
- Pontos de extensão esperados
- O que NÃO deve ser acessado diretamente
```

#### Template Markdown – Resultado

```md
# Mapa de Contexto do Sistema

## Módulo: <Nome>
- Responsabilidade:
- Dependências:
- Pontos de Extensão:
- Acessos Proibidos:

## Módulo: <Nome>
...
```

---

### Prompt 2.2 – Fluxos Críticos

```text
Liste os principais fluxos de execução testados atualmente.

Para cada fluxo:
- Objetivo do teste
- Entradas relevantes
- Saídas/assertions esperadas
- Pontos sensíveis a mudança
```

#### Template Markdown – Resultado

```md
# Fluxos Críticos de Teste

## Fluxo: <Nome>
- Objetivo:
- Entradas:
- Assertions:
- Pontos Sensíveis:
```

---

## 3️⃣ Tarefas, Features e Definition of Done

### Prompt 3.1 – Template de Feature

```text
Defina um template padrão de feature para este projeto de testes.

Inclua:
- Objetivo da feature
- Escopo incluído
- Escopo explicitamente fora
- Dependências técnicas
- Riscos conhecidos
```

#### Template Markdown – Resultado

```md
# Feature: <Nome>

## Objetivo

## Escopo Incluído

## Fora de Escopo

## Dependências Técnicas

## Riscos
```

---

### Prompt 3.2 – Definition of Done

```text
Crie um Definition of Done específico para este projeto de testes automatizados.
```

#### Template Markdown – Resultado

```md
# Definition of Done (DoD)

## Obrigatório
- [ ] Testes independentes
- [ ] Nomeação clara
- [ ] Assertions relevantes

## Qualidade
- [ ] Legibilidade
- [ ] Baixo acoplamento

## Manutenção
- [ ] Fácil extensão
- [ ] Baixa fragilidade
```

---

## 4️⃣ Arquitetura Modular

### Prompt 4.1 – Regras Arquiteturais

```text
Extraia e formalize as regras arquiteturais implícitas deste projeto.
```

#### Template Markdown – Resultado

```md
# Regras Arquiteturais

## Dependências Permitidas

## Dependências Proibidas

## Localização de Lógica de Negócio

## Restrições Específicas para Testes
```

---

### Prompt 4.2 – Geração Orientada a Módulo

```text
Ao gerar qualquer código novo:
1. Declare em qual módulo ele pertence
2. Justifique por que este é o local correto
3. Liste dependências necessárias
4. Verifique se viola alguma regra arquitetural existente
```

#### Template Markdown – Resultado

```md
# Avaliação Arquitetural do Código Gerado

## Módulo Alvo

## Justificativa

## Dependências

## Verificação de Regras
- [ ] Não viola regras
- [ ] Aderente à arquitetura
```

---

## 5️⃣ Múltiplas Personas

### Prompt 5.x – Revisões por Persona

Use o mesmo código e aplique revisões separadas.

#### Template Markdown – Resultado

```md
# Revisão por Personas

## Persona: Test Architect
- Pontos Fortes:
- Riscos:

## Persona: SRE / Performance
- Pontos Fortes:
- Riscos:

## Persona: Maintainer
- Pontos Fortes:
- Riscos:

## Persona: Gatekeeper
- Aprovado? (Sim/Não)
- Motivos:
```

---

## Uso Recomendado

* Cada seção pode virar um `.md` independente
* Cole apenas o necessário como contexto
* Atualize sempre que um erro recorrente surgir

Este conjunto forma o **Context Pack oficial do projeto**.
