# Sistema de Autorização para Operações de Perfil

## Visão Geral

O sistema agora implementa uma regra de segurança onde **somente administradores podem gerenciar perfis** (cadastrar, atualizar ou remover). As operações de **listar perfis e buscar perfil por ID estão liberadas para todos**. Esta validação é baseada no sistema de perfis existente e nas credenciais do administrador.

## Como Funciona

### 1. Verificação de Perfil

- O sistema verifica se o funcionário que está tentando fazer a operação possui um perfil com as permissões específicas:
  - `cadastrarPerfil = true` para adicionar perfis
  - `atualizarPerfil = true` para atualizar perfis
  - `deletarPerfil = true` para remover perfis
- Esta verificação é feita através das credenciais (usuário e senha) fornecidas

### 2. Estrutura dos Requests

#### Para Cadastrar Novo Perfil (POST)

```json
{
  "adminUsuario": "admin",
  "adminSenha": 123456,
  "nome": "Recepcionista",
  "cadastrarFuncionario": false,
  "lerFuncionario": true,
  "atualizarFuncionario": false,
  "deletarFuncionario": false,
  "listarFuncionario": true,
  "cadastrarPaciente": true,
  "lerPaciente": true,
  "atualizarPaciente": true,
  "deletarPaciente": false,
  "listarPaciente": true,
  "cadastrarConsulta": true,
  "lerConsulta": true,
  "atualizarConsulta": true,
  "deletarConsulta": false,
  "listarConsulta": true,
  "cadastrarEspecialidade": false,
  "lerEspecialidade": true,
  "atualizarEspecialidade": false,
  "deletarEspecialidade": false,
  "listarEspecialidade": true,
  "cadastrarConvenio": false,
  "lerConvenio": true,
  "atualizarConvenio": false,
  "deletarConvenio": false,
  "listarConvenio": true,
  "cadastrarProntuario": false,
  "lerProntuario": true,
  "atualizarProntuario": false,
  "deletarProntuario": false,
  "listarProntuario": true,
  "cadastrarPerfil": false,
  "lerPerfil": true,
  "atualizarPerfil": false,
  "deletarPerfil": false,
  "listarPerfil": true
}
```

#### Para Atualizar Perfil Existente (PUT)

```json
{
  "adminUsuario": "admin",
  "adminSenha": 123456,
  "nome": "Recepcionista Senior",
  "cadastrarFuncionario": false,
  "lerFuncionario": true,
  "atualizarFuncionario": false,
  "deletarFuncionario": false,
  "listarFuncionario": true,
  "cadastrarPaciente": true,
  "lerPaciente": true,
  "atualizarPaciente": true,
  "deletarPaciente": true,
  "listarPaciente": true,
  "cadastrarConsulta": true,
  "lerConsulta": true,
  "atualizarConsulta": true,
  "deletarConsulta": true,
  "listarConsulta": true,
  "cadastrarEspecialidade": false,
  "lerEspecialidade": true,
  "atualizarEspecialidade": false,
  "deletarEspecialidade": false,
  "listarEspecialidade": true,
  "cadastrarConvenio": false,
  "lerConvenio": true,
  "atualizarConvenio": false,
  "deletarConvenio": false,
  "listarConvenio": true,
  "cadastrarProntuario": false,
  "lerProntuario": true,
  "atualizarProntuario": false,
  "deletarProntuario": false,
  "listarProntuario": true,
  "cadastrarPerfil": false,
  "lerPerfil": true,
  "atualizarPerfil": false,
  "deletarPerfil": false,
  "listarPerfil": true
}
```

### 3. Fluxo de Validação

1. **Verificação de Credenciais**: Sistema verifica se `adminUsuario` e `adminSenha` foram fornecidos
2. **Autenticação**: Busca o funcionário pelas credenciais fornecidas
3. **Autorização**: Verifica se o perfil do funcionário tem a permissão específica para a operação
4. **Execução**: Se autorizado, procede com a operação (criar/atualizar/remover)

## Endpoints e Métodos

### 1. Adicionar Perfil

**POST** `/clinica-medica-administrativo/perfis`

**Body (JSON):** Veja exemplo completo acima

### 2. Atualizar Perfil

**PUT** `/clinica-medica-administrativo/perfis/{id}`

**Body (JSON):** Veja exemplo completo acima

### 3. Remover Perfil

**DELETE** `/clinica-medica-administrativo/perfis/{id}?adminUsuario=admin&adminSenha=123456`

**Parâmetros de Query:**

- `adminUsuario`: Nome de usuário do administrador
- `adminSenha`: Senha do administrador

### 4. Listar e Buscar Perfis (Sem Autorização)

**GET** `/clinica-medica-administrativo/perfis/listar` - Lista todos os perfis

**GET** `/clinica-medica-administrativo/perfis/{id}` - Busca perfil por ID

## Respostas do Sistema

### Sucesso

#### Perfil Criado (201 - Created)

```json
{
  "id": 123,
  "nome": "Recepcionista",
  "cadastrarFuncionario": false,
  "lerFuncionario": true
}
```

#### Perfil Atualizado (200 - OK)

```json
{
  "id": 123,
  "nome": "Recepcionista Senior",
  "cadastrarFuncionario": false,
  "lerFuncionario": true
}
```

#### Perfil Removido (204 - No Content)

_Sem conteúdo no corpo da resposta_

### Erros

#### Credenciais não fornecidas (400 - Bad Request)

```
"Credenciais do administrador são obrigatórias para cadastrar perfis."
```

```
"Credenciais do administrador são obrigatórias para atualizar perfis."
```

```
"Credenciais do administrador são obrigatórias para remover perfis."
```

#### Sem permissão (403 - Forbidden)

```
"Acesso negado. Apenas administradores podem cadastrar perfis."
```

```
"Acesso negado. Apenas administradores podem atualizar perfis."
```

```
"Acesso negado. Apenas administradores podem remover perfis."
```

## Configuração de Perfis

Para que um funcionário possa manipular perfis, seu perfil deve ter as permissões apropriadas:

- `cadastrarPerfil = true` - Para criar perfis
- `atualizarPerfil = true` - Para atualizar perfis
- `deletarPerfil = true` - Para remover perfis

**Exemplo de perfil de administrador:**

```sql
INSERT INTO perfis (nome, cadastrar_perfil, atualizar_perfil, deletar_perfil, ler_perfil, listar_perfil, ...)
VALUES ('Administrador', true, true, true, true, true, ...);
```

## Exemplos de Uso com cURL

### Criar Perfil

```bash
curl -X POST http://localhost:8080/clinica-medica-administrativo/perfis \
  -H "Content-Type: application/json" \
  -d '{
    "adminUsuario": "admin",
    "adminSenha": 123456,
    "nome": "Atendente",
    "cadastrarFuncionario": false,
    "lerFuncionario": true,
    "atualizarFuncionario": false,
    "deletarFuncionario": false,
    "listarFuncionario": true,
    "cadastrarPaciente": true,
    "lerPaciente": true,
    "atualizarPaciente": true,
    "deletarPaciente": false,
    "listarPaciente": true,
    "cadastrarConsulta": true,
    "lerConsulta": true,
    "atualizarConsulta": true,
    "deletarConsulta": false,
    "listarConsulta": true,
    "cadastrarEspecialidade": false,
    "lerEspecialidade": true,
    "atualizarEspecialidade": false,
    "deletarEspecialidade": false,
    "listarEspecialidade": true,
    "cadastrarConvenio": false,
    "lerConvenio": true,
    "atualizarConvenio": false,
    "deletarConvenio": false,
    "listarConvenio": true,
    "cadastrarProntuario": false,
    "lerProntuario": true,
    "atualizarProntuario": false,
    "deletarProntuario": false,
    "listarProntuario": true,
    "cadastrarPerfil": false,
    "lerPerfil": true,
    "atualizarPerfil": false,
    "deletarPerfil": false,
    "listarPerfil": true
  }'
```

### Atualizar Perfil

```bash
curl -X PUT http://localhost:8080/clinica-medica-administrativo/perfis/123 \
  -H "Content-Type: application/json" \
  -d '{
    "adminUsuario": "admin",
    "adminSenha": 123456,
    "nome": "Atendente Senior",
    "cadastrarFuncionario": false,
    "lerFuncionario": true,
    "atualizarFuncionario": false,
    "deletarFuncionario": false,
    "listarFuncionario": true,
    "cadastrarPaciente": true,
    "lerPaciente": true,
    "atualizarPaciente": true,
    "deletarPaciente": true,
    "listarPaciente": true,
    "cadastrarConsulta": true,
    "lerConsulta": true,
    "atualizarConsulta": true,
    "deletarConsulta": true,
    "listarConsulta": true,
    "cadastrarEspecialidade": false,
    "lerEspecialidade": true,
    "atualizarEspecialidade": false,
    "deletarEspecialidade": false,
    "listarEspecialidade": true,
    "cadastrarConvenio": false,
    "lerConvenio": true,
    "atualizarConvenio": false,
    "deletarConvenio": false,
    "listarConvenio": true,
    "cadastrarProntuario": false,
    "lerProntuario": true,
    "atualizarProntuario": false,
    "deletarProntuario": false,
    "listarProntuario": true,
    "cadastrarPerfil": false,
    "lerPerfil": true,
    "atualizarPerfil": false,
    "deletarPerfil": false,
    "listarPerfil": true
  }'
```

### Remover Perfil

```bash
curl -X DELETE "http://localhost:8080/clinica-medica-administrativo/perfis/123?adminUsuario=admin&adminSenha=123456"
```

### Listar Perfis (Sem autorização necessária)

```bash
curl -X GET http://localhost:8080/clinica-medica-administrativo/perfis/listar
```

### Buscar Perfil por ID (Sem autorização necessária)

```bash
curl -X GET http://localhost:8080/clinica-medica-administrativo/perfis/123
```

## Segurança

- As credenciais do administrador são validadas a cada requisição
- Não há armazenamento de sessão - cada operação requer autenticação
- O sistema registra logs de todas as tentativas de acesso (permitidas e negadas)
- Apenas funcionários com perfil adequado podem executar operações de escrita
- Operações de leitura (listar e buscar) não requerem autorização especial

## Diferenças das Operações

- **Operações de Escrita** (POST, PUT, DELETE): Requerem autorização de administrador
- **Operações de Leitura** (GET): Não requerem autorização especial
- **Método DELETE**: Usa parâmetros de query em vez de body para as credenciais

## Integração com Outros Módulos

Este sistema de autorização para perfis se integra com:

- **Módulo Administrativo**: Gerenciamento completo de perfis
- **Sistema de Funcionários**: Cada funcionário é associado a um perfil específico
- **Sistema de Autorização**: Controle granular de permissões por tipo de funcionário

## Campos de Permissão de Perfil

O modelo Perfil agora inclui campos específicos para autorização de operações de perfil:

- `cadastrarPerfil`: Permite criar novos perfis
- `lerPerfil`: Permite visualizar detalhes de um perfil específico
- `atualizarPerfil`: Permite modificar perfis existentes
- `deletarPerfil`: Permite remover perfis
- `listarPerfil`: Permite listar todos os perfis

Estes campos devem ser configurados como `true` apenas para funcionários que devem ter permissão para gerenciar perfis do sistema.
