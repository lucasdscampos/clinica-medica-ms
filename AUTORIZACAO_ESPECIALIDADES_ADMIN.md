# Sistema de Autorização para Operações de Especialidade

## Visão Geral

O sistema agora implementa uma regra de segurança onde **somente administradores podem gerenciar especialidades** (cadastrar, atualizar ou remover). A operação de **listar especialidades está liberada para todos**. Esta validação é baseada no sistema de perfis existente e nas credenciais do administrador.

## Como Funciona

### 1. Verificação de Perfil

- O sistema verifica se o funcionário que está tentando fazer a operação possui um perfil com as permissões específicas:
  - `cadastrarEspecialidade = true` para adicionar especialidades
  - `atualizarEspecialidade = true` para atualizar especialidades
  - `deletarEspecialidade = true` para remover especialidades
- Esta verificação é feita através das credenciais (usuário e senha) fornecidas

### 2. Estrutura dos Requests

#### Para Cadastrar Nova Especialidade (POST)

```json
{
  "adminUsuario": "admin",
  "adminSenha": 123456,
  "nome": "Cardiologia",
  "descricao": "Especialidade médica que se dedica ao diagnóstico e tratamento das doenças que acometem o coração"
}
```

#### Para Atualizar Especialidade Existente (PUT)

```json
{
  "adminUsuario": "admin",
  "adminSenha": 123456,
  "nome": "Cardiologia Geral",
  "descricao": "Especialidade médica focada no diagnóstico e tratamento de doenças cardiovasculares"
}
```

### 3. Fluxo de Validação

1. **Verificação de Credenciais**: Sistema verifica se `adminUsuario` e `adminSenha` foram fornecidos
2. **Autenticação**: Busca o funcionário pelas credenciais fornecidas
3. **Autorização**: Verifica se o perfil do funcionário tem a permissão específica para a operação
4. **Execução**: Se autorizado, procede com a operação (criar/atualizar/remover)

## Endpoints e Métodos

### 1. Adicionar Especialidade

**POST** `/clinica-medica-administrativo/especialidades`

**Body (JSON):**

```json
{
  "adminUsuario": "admin",
  "adminSenha": 123456,
  "nome": "Dermatologia",
  "descricao": "Especialidade médica que se ocupa do diagnóstico, prevenção e tratamento de doenças e afecções relacionadas à pele"
}
```

### 2. Atualizar Especialidade

**PUT** `/clinica-medica-administrativo/especialidades/{id}`

**Body (JSON):**

```json
{
  "adminUsuario": "admin",
  "adminSenha": 123456,
  "nome": "Dermatologia Clínica",
  "descricao": "Especialidade médica focada no diagnóstico e tratamento clínico de doenças da pele, cabelos e unhas"
}
```

### 3. Remover Especialidade

**DELETE** `/clinica-medica-administrativo/especialidades/{id}?adminUsuario=admin&adminSenha=123456`

**Parâmetros de Query:**

- `adminUsuario`: Nome de usuário do administrador
- `adminSenha`: Senha do administrador

### 4. Listar e Buscar Especialidades (Sem Autorização)

**GET** `/clinica-medica-administrativo/especialidades/listar` - Lista todas as especialidades

**GET** `/clinica-medica-administrativo/especialidades/{id}` - Busca especialidade por ID

## Respostas do Sistema

### Sucesso

#### Especialidade Criada (201 - Created)

```json
{
  "id": 123,
  "nome": "Dermatologia",
  "descricao": "Especialidade médica que se ocupa do diagnóstico, prevenção e tratamento de doenças e afecções relacionadas à pele"
}
```

#### Especialidade Atualizada (200 - OK)

```json
{
  "id": 123,
  "nome": "Dermatologia Clínica",
  "descricao": "Especialidade médica focada no diagnóstico e tratamento clínico de doenças da pele, cabelos e unhas"
}
```

#### Especialidade Removida (204 - No Content)

_Sem conteúdo no corpo da resposta_

### Erros

#### Credenciais não fornecidas (400 - Bad Request)

```
"Credenciais do administrador são obrigatórias para cadastrar especialidades."
```

```
"Credenciais do administrador são obrigatórias para atualizar especialidades."
```

```
"Credenciais do administrador são obrigatórias para remover especialidades."
```

#### Sem permissão (403 - Forbidden)

```
"Acesso negado. Apenas administradores podem cadastrar especialidades."
```

```
"Acesso negado. Apenas administradores podem atualizar especialidades."
```

```
"Acesso negado. Apenas administradores podem remover especialidades."
```

## Configuração de Perfis

Para que um funcionário possa manipular especialidades, seu perfil deve ter as permissões apropriadas:

- `cadastrarEspecialidade = true` - Para criar especialidades
- `atualizarEspecialidade = true` - Para atualizar especialidades
- `deletarEspecialidade = true` - Para remover especialidades

**Exemplo de perfil de administrador:**

```sql
INSERT INTO perfis (nome, cadastrar_especialidade, atualizar_especialidade, deletar_especialidade, ler_especialidade, listar_especialidade, ...)
VALUES ('Administrador', true, true, true, true, true, ...);
```

## Exemplos de Uso com cURL

### Criar Especialidade

```bash
curl -X POST http://localhost:8080/clinica-medica-administrativo/especialidades \
  -H "Content-Type: application/json" \
  -d '{
    "adminUsuario": "admin",
    "adminSenha": 123456,
    "nome": "Neurologia",
    "descricao": "Especialidade médica que trata distúrbios do sistema nervoso"
  }'
```

### Atualizar Especialidade

```bash
curl -X PUT http://localhost:8080/clinica-medica-administrativo/especialidades/123 \
  -H "Content-Type: application/json" \
  -d '{
    "adminUsuario": "admin",
    "adminSenha": 123456,
    "nome": "Neurologia Clínica",
    "descricao": "Especialidade médica focada no diagnóstico e tratamento de distúrbios neurológicos"
  }'
```

### Remover Especialidade

```bash
curl -X DELETE "http://localhost:8080/clinica-medica-administrativo/especialidades/123?adminUsuario=admin&adminSenha=123456"
```

### Listar Especialidades (Sem autorização necessária)

```bash
curl -X GET http://localhost:8080/clinica-medica-administrativo/especialidades/listar
```

### Buscar Especialidade por ID (Sem autorização necessária)

```bash
curl -X GET http://localhost:8080/clinica-medica-administrativo/especialidades/123
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

Este sistema de autorização para especialidades se integra com:

- **Módulo Administrativo**: Gerenciamento completo de especialidades
- **Módulo de Agendamento**: Uso de especialidades nas consultas (apenas leitura)
- **Sistema de Perfis**: Controle granular de permissões por tipo de funcionário
