# Guia de Validação - Implementação: Criar um Assunto (POST /subjects)

## 🔍 Validações Pré-Merge

Execute estas validações antes de fazer commit/merge da feature.

---

## 1️⃣ Compilação e Build

### 1.1 Verificar Erros de Compilação
```bash
# Compile apenas (sem testes)
./mvnw clean compile -DskipTests

# Resultado esperado:
# [INFO] BUILD SUCCESS
```

### 1.2 Verificar Build Completo
```bash
./mvnw clean package -DskipTests

# Resultado esperado:
# [INFO] BUILD SUCCESS
# [INFO] bookSalesOnline-0.0.1-SNAPSHOT.jar
```

---

## 2️⃣ Testes Unitários

### 2.1 Executar Todos os Testes
```bash
./mvnw clean test

# Resultado esperado:
# [INFO] Tests run: 100+, Failures: 0, Errors: 0, Skipped: 0
```

### 2.2 Executar Apenas Testes de Subject
```bash
./mvnw test -Dtest=Subject*

# Resultado esperado:
# Tests run: 23 (8 repository + 15 controller)
# All tests PASSED
```

### 2.3 Executar Testes de SubjectRepository
```bash
./mvnw test -Dtest=SubjectRepositoryTest

# Resultado esperado:
# Tests run: 8, Failures: 0
# ✓ should_get_all_subjects_with_pagination
# ✓ should_get_subject_by_id
# ✓ should_return_null_when_subject_not_found
# ✓ should_save_new_subject
# ✓ should_update_existing_subject
# ✓ should_remove_subject
# ✓ should_map_subject_entity_to_domain_correctly
# ✓ should_save_subject_with_all_fields
```

### 2.4 Executar Testes Funcionais
```bash
./mvnw test -Dtest=SubjectControllerFunctionalTest

# Resultado esperado:
# Tests run: 15, Failures: 0
# ✓ should_create_subject_successfully (Order 1)
# ✓ should_update_created_subject_successfully (Order 2)
# ✓ should_get_by_id_created_subject_successfully (Order 3)
# ✓ should_get_by_id_created_subject_from_cache_successfully (Order 4)
# ✓ should_delete_created_subject_successfully (Order 5)
# ✓ should_get_all_subjects_successfully (Order 6)
# ... (9 testes adicionais de busca e cache)
```

---

## 3️⃣ Cobertura de Código (JaCoCo)

### 3.1 Gerar Relatório
```bash
./mvnw clean verify

# Resultado esperado:
# [INFO] BUILD SUCCESS
```

### 3.2 Verificar Cobertura
```bash
# Abrir relatório no navegador
open target/site/jacoco/index.html

# Verificar em: com.renan.booksalesonline.adapters.repositories
# SubjectRepository:
#   - Line Coverage: 90%+
#   - Branch Coverage: 85%+
#
# Verificar em: com.renan.booksalesonline.adapters.controllers.v1
# SubjectController:
#   - Line Coverage: 85%+
#   - Branch Coverage: 80%+
```

### 3.3 Validar Cobertura Total
```bash
# Cobertura total esperada: ≥ 70%
# Elementos cobertura:
#   - Domain: 100%
#   - Mappers: 100%
#   - Repository: 90%+
#   - Controller: 85%+
#   - Use Cases: 100%
```

---

## 4️⃣ Mutation Testing (PIT)

### 4.1 Executar Análise de Mutantes
```bash
./mvnw org.pitest:pitest-maven:mutationCoverage

# Resultado esperado:
# [INFO] BUILD SUCCESS
# [INFO] Mutation testing completed
```

### 4.2 Verificar Mutation Score
```bash
# Abrir relatório
open target/pit-reports/index.html

# Validar score por classe:
# SubjectRepository: > 70%
# SubjectController: > 70%
# Mappers: > 75%

# Score total esperado: > 70%
```

### 4.3 Analisar Mutantes Sobreviventes
```bash
# No relatório PIT, procurar por "SURVIVED" mutations
# Cada mutação sobrevivente indica:
# - Gap na cobertura de testes
# - Assertion insuficiente

# Exemplos de mutações comuns:
# - Removal de return statements
# - Replacement de operadores (== para !=)
# - Removal de assignment
```

---

## 5️⃣ Testes Manuais (Postman/cURL)

### 5.1 Preparação
```bash
# 1. Iniciar a aplicação
./mvnw spring-boot:run

# 2. Aguardar inicialização
# Procure por: "BookSalesOnlineApplication : Started BookSalesOnlineApplication"

# 3. Base URL: http://localhost:8080
```

### 5.2 Teste POST (CREATE)
```bash
curl -X POST http://localhost:8080/subjects \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Ficção Científica",
    "description": "Livros sobre ficção científica"
  }'

# Resultado esperado:
# HTTP 201 CREATED
# {
#   "id": 7,
#   "name": "Ficção Científica",
#   "description": "Livros sobre ficção científica"
# }
```

### 5.3 Teste GET List
```bash
curl -X GET http://localhost:8080/subjects

# Resultado esperado:
# HTTP 200 OK
# [
#   {"id": 1, "name": "Fantasia", "description": "..."},
#   {"id": 2, "name": "Aventura", "description": "..."},
#   ... (6+ assuntos)
# ]
```

### 5.4 Teste GET by ID
```bash
curl -X GET http://localhost:8080/subjects/1

# Resultado esperado:
# HTTP 200 OK
# {"id": 1, "name": "Fantasia", "description": "..."}

# 2º request (cache hit) - deve ser mais rápido
curl -X GET http://localhost:8080/subjects/1
```

### 5.5 Teste PUT (UPDATE)
```bash
curl -X PUT http://localhost:8080/subjects/7 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Ficção Científica Atualizado",
    "description": "Descrição atualizada"
  }'

# Resultado esperado:
# HTTP 200 OK
# {"id": 7, "name": "Ficção Científica Atualizado", ...}
```

### 5.6 Teste DELETE
```bash
curl -X DELETE http://localhost:8080/subjects/7

# Resultado esperado:
# HTTP 204 NO CONTENT
# (sem body)

# Validar remoção
curl -X GET http://localhost:8080/subjects/7
# Resultado: HTTP 404 NOT FOUND (se houver tratamento de erro)
```

---

## 6️⃣ Verificações de Código

### 6.1 Verificar Padrões Arquiteturais
```bash
# ✅ Verificar implementação de DataCommand em SubjectRepository
grep -n "implements DataCommand<Subject>" src/main/java/com/renan/booksalesonline/adapters/repositories/SubjectRepository.java

# ✅ Verificar metodos save() e remove()
grep -n "public Subject save\|public void remove" src/main/java/com/renan/booksalesonline/adapters/repositories/SubjectRepository.java

# ✅ Verificar mappers bidirecional
grep -n "toDomain\|fromDomain" src/main/java/com/renan/booksalesonline/adapters/controllers/v1/mappers/SubjectDtoMapper.java

# ✅ Verificar endpoints HTTP
grep -n "@PostMapping\|@PutMapping\|@DeleteMapping" src/main/java/com/renan/booksalesonline/adapters/controllers/v1/SubjectController.java

# ✅ Verificar injeção de DataCommand no mediador
grep -n "DataCommand<Subject> subjectCommand" src/main/java/com/renan/booksalesonline/application/mediators/RepositoryMediatorImpl.java
grep -n "commands.put(Subject.class" src/main/java/com/renan/booksalesonline/application/mediators/RepositoryMediatorImpl.java
```

### 6.2 Verificar Qualidade de Testes
```bash
# ✅ Verificar padrão AAA (Arrange-Act-Assert) em SubjectRepositoryTest
grep -n "// Arrange\|// Act\|// Assert" src/test/java/com/renan/booksalesonline/tests/adapters/repositories/SubjectRepositoryTest.java

# ✅ Verificar uso de mocks apropriados
grep -n "@Mock\|@InjectMocks" src/test/java/com/renan/booksalesonline/tests/adapters/repositories/SubjectRepositoryTest.java

# ✅ Verificar nomes descritivos de testes
grep -n "public void should_" src/test/java/com/renan/booksalesonline/tests/adapters/repositories/SubjectRepositoryTest.java

# ✅ Verificar assertions significativas
grep -n "assertThat" src/test/java/com/renan/booksalesonline/tests/adapters/repositories/SubjectRepositoryTest.java
```

### 6.3 Verificar Imports
```bash
# ✅ Verificar se todos os imports estão presentes
grep -n "^import" src/main/java/com/renan/booksalesonline/adapters/controllers/v1/SubjectController.java | head -15

# Resultado esperado deve incluir:
# - CreateEntityUseCase
# - UpdateEntityUseCase
# - RemoveEntityUseCase
# - RequestMapping annotations
```

---

## 7️⃣ Validação de Exemplo Completo

### 7.1 Script de Validação Rápida
```bash
#!/bin/bash
echo "🔍 Validando implementação..."

echo "1️⃣  Compilando..."
./mvnw clean compile -DskipTests || exit 1

echo "2️⃣  Testando Subject*..."
./mvnw test -Dtest=Subject* || exit 1

echo "3️⃣  Gerando cobertura JaCoCo..."
./mvnw verify || exit 1

echo "✅ Todas as validações passaram!"
echo "📊 Próximo passo: Revisar cobertura com:"
echo "   open target/site/jacoco/index.html"
echo "🚀 Pronto para commit!"
```

### 7.2 Executar Script
```bash
chmod +x validate.sh
./validate.sh
```

---

## 8️⃣ Checklist Final de Merge

Antes de fazer commit/merge, validar:

- [ ] ✅ Compilação: `./mvnw clean compile -DskipTests` sem erros
- [ ] ✅ Testes: `./mvnw test` todos passando (100+ testes)
- [ ] ✅ Subject tests: `./mvnw test -Dtest=Subject*` - 23 testes passando
- [ ] ✅ Cobertura JaCoCo: ≥ 70% (executar `./mvnw verify`)
- [ ] ✅ Mutation PIT: > 70% (executar `./mvnw org.pitest:pitest-maven:mutationCoverage`)
- [ ] ✅ Testes manuais: Postman collection funcionando
- [ ] ✅ Nenhum warning de compilação
- [ ] ✅ Padrões arquiteturais respeitados
- [ ] ✅ Commits com mensagens descritivas
- [ ] ✅ Arquivo IMPLEMENTATION_SUMMARY.md atualizado

---

## 🎯 Resultado Esperado

```
✅ BUILD SUCCESS (compilação + testes + cobertura)
✅ Tests: 100+ passed, 0 failed
✅ Subject Tests: 23 passed (8 unit + 15 E2E)
✅ JaCoCo Coverage: 70%+
✅ PIT Mutation: 70%+
✅ Manual Tests: All endpoints working
✅ Zero compilation warnings
✅ Ready for merge! 🚀
```

---

## 🆘 Troubleshooting

### Erro: "There is no repository provider"
**Causa**: `DataCommand<Subject>` não foi injetado em `RepositoryMediatorImpl`
**Solução**: Verificar se `@Autowired DataCommand<Subject> subjectCommand` está no construtor

### Erro: Test failures em E2E
**Causa**: Pode ser relacionado a ordem dos testes (@Order)
**Solução**: Executar `./mvnw clean test` para resetar estado do banco

### Cobertura baixa
**Causa**: Testes não estão sendo contados
**Solução**: Verificar `pom.xml` configuração de JaCoCo, executar `./mvnw clean verify`

### Mutation score baixo
**Causa**: Assertions insuficientes ou lógica não testada
**Solução**: Analisar mutantes sobreviventes no relatório PIT, adicionar mais assertions

---

**Data de Criação**: 2026-02-15  
**Versão**: 1.0  
**Status**: 🟢 Pronto para Validação

