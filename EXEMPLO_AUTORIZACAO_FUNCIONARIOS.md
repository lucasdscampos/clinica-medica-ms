# Sistema de Autorização para Cadastro de Funcionários

## Visão Geral

O sistema agora implementa uma regra de segurança onde **somente administradores podem cadastrar novos funcionários**. Esta validação é baseada no sistema de perfis existente.

## Como Funciona

### 1. Verificação de Perfil

- O sistema verifica se o funcionário que está tentando cadastrar possui um perfil com a permissão `cadastrarFuncionario = true`
- Esta verificação é feita através das credenciais (usuário e senha) fornecidas

### 2. Estrutura do Request

Agora o `FuncionarioRequest` inclui campos obrigatórios para autenticação do administrador:

```json
{
  "adminUsuario": "admin_user",
  "adminSenha": 123456,
  "usuario": "novo_funcionario",
  "senha": 789123,
  "nome": "João Silva",
  "cpf": "12345678901",
  "idPerfil": 2,
  "tipoFuncionario": "ATENDENTE"
}
```

### 3. Fluxo de Validação

1. **Verificação de Credenciais**: Sistema verifica se `adminUsuario` e `adminSenha` foram fornecidos
2. **Autenticação**: Busca o funcionário pelas credenciais fornecidas
3. **Autorização**: Verifica se o perfil do funcionário tem permissão `cadastrarFuncionario`
4. **Criação**: Se autorizado, procede com a criação do novo funcionário

## Respostas do Sistema

### Sucesso (201 - Created)

```json
{
  "id": 123,
  "nome": "João Silva",
  "usuario": "novo_funcionario",
  "cpf": "12345678901",
  ...
}
```

### Erro - Credenciais não fornecidas (400 - Bad Request)

```
"Credenciais do administrador são obrigatórias para cadastrar funcionários."
```

### Erro - Sem permissão (403 - Forbidden)

```
"Acesso negado. Apenas administradores podem cadastrar funcionários."
```

### Erro - Funcionário administrador não encontrado (403 - Forbidden)

```
"Acesso negado. Apenas administradores podem cadastrar funcionários."
```

## Configuração de Perfis

Para que um funcionário possa cadastrar outros funcionários, seu perfil deve ter:

- `cadastrarFuncionario = true`

Exemplo de perfil de administrador:

```sql
INSERT INTO perfis (nome, cadastrar_funcionario, ler_funcionario, ...)
VALUES ('Administrador', true, true, ...);
```

## Exemplo de Uso com cURL

```bash
curl -X POST http://localhost:8080/clinica-medica-administrativo/funcionarios \
  -H "Content-Type: application/json" \
  -d '{
    "adminUsuario": "admin",
    "adminSenha": 123456,
    "usuario": "joao.silva",
    "senha": 789123,
    "nome": "João Silva",
    "cpf": "12345678901",
    "idPerfil": 2,
    "tipoFuncionario": "ATENDENTE"
  }'
```

## Segurança

- As credenciais do administrador são validadas a cada requisição
- Não há armazenamento de sessão - cada operação requer autenticação
- O sistema registra logs de todas as tentativas de acesso (permitidas e negadas)
