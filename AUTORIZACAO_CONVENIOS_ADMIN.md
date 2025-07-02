# Sistema de Autorização para Operações de Convênio

## Visão Geral

O sistema agora implementa uma regra de segurança onde **somente administradores podem gerenciar convênios** (cadastrar, atualizar ou remover). A operação de **listar convênios está liberada para todos**. Esta validação é baseada no sistema de perfis existente e nas credenciais do administrador.

## Como Funciona

### 1. Verificação de Perfil

- O sistema verifica se o funcionário que está tentando fazer a operação possui um perfil com as permissões específicas:
  - `cadastrarConvenio = true` para adicionar convênios
  - `atualizarConvenio = true` para atualizar convênios
  - `deletarConvenio = true` para remover convênios
- Esta verificação é feita através das credenciais (usuário e senha) fornecidas

### 2. Estrutura dos Requests

#### Para Cadastrar Novo Convênio (POST)

```json
{
  "adminUsuario": "admin",
  "adminSenha": 123456,
  "nome": "Plano Gold",
  "descricao": "Plano de saúde premium com cobertura completa"
}
```

#### Para Atualizar Convênio Existente (PUT)

```json
{
  "adminUsuario": "admin",
  "adminSenha": 123456,
  "nome": "Plano Gold Plus",
  "descricao": "Plano de saúde premium com cobertura completa e internacional"
}
```

### 3. Fluxo de Validação

1. **Verificação de Credenciais**: Sistema verifica se `adminUsuario` e `adminSenha` foram fornecidos
2. **Autenticação**: Busca o funcionário pelas credenciais fornecidas
3. **Autorização**: Verifica se o perfil do funcionário tem a permissão específica para a operação
4. **Execução**: Se autorizado, procede com a operação (criar/atualizar/remover)

## Endpoints e Métodos

### 1. Adicionar Convênio

**POST** `/clinica-medica-administrativo/convenios`

**Body (JSON):**

```json
{
  "adminUsuario": "admin",
  "adminSenha": 123456,
  "nome": "Plano Saúde Basic",
  "descricao": "Plano básico com cobertura essencial"
}
```

### 2. Atualizar Convênio

**PUT** `/clinica-medica-administrativo/convenios/{id}`

**Body (JSON):**

```json
{
  "adminUsuario": "admin",
  "adminSenha": 123456,
  "nome": "Plano Saúde Basic Plus",
  "descricao": "Plano básico com cobertura essencial e consultas especializadas"
}
```

### 3. Remover Convênio

**DELETE** `/clinica-medica-administrativo/convenios/{id}?adminUsuario=admin&adminSenha=123456`

**Parâmetros de Query:**

- `adminUsuario`: Nome de usuário do administrador
- `adminSenha`: Senha do administrador

### 4. Listar e Buscar Convênios (Sem Autorização)

**GET** `/clinica-medica-administrativo/convenios/listar` - Lista todos os convênios

**GET** `/clinica-medica-administrativo/convenios/{id}` - Busca convênio por ID

## Respostas do Sistema

### Sucesso

#### Convênio Criado (201 - Created)

```json
{
  "id": 123,
  "nome": "Plano Saúde Basic",
  "descricao": "Plano básico com cobertura essencial"
}
```

#### Convênio Atualizado (200 - OK)

```json
{
  "id": 123,
  "nome": "Plano Saúde Basic Plus",
  "descricao": "Plano básico com cobertura essencial e consultas especializadas"
}
```

#### Convênio Removido (204 - No Content)

_Sem conteúdo no corpo da resposta_

### Erros

#### Credenciais não fornecidas (400 - Bad Request)

```
"Credenciais do administrador são obrigatórias para cadastrar convênios."
```

```
"Credenciais do administrador são obrigatórias para atualizar convênios."
```

```
"Credenciais do administrador são obrigatórias para remover convênios."
```

#### Sem permissão (403 - Forbidden)

```
"Acesso negado. Apenas administradores podem cadastrar convênios."
```

```
"Acesso negado. Apenas administradores podem atualizar convênios."
```

```
"Acesso negado. Apenas administradores podem remover convênios."
```

## Configuração de Perfis

Para que um funcionário possa manipular convênios, seu perfil deve ter as permissões apropriadas:

- `cadastrarConvenio = true` - Para criar convênios
- `atualizarConvenio = true` - Para atualizar convênios
- `deletarConvenio = true` - Para remover convênios

**Exemplo de perfil de administrador:**

```sql
INSERT INTO perfis (nome, cadastrar_convenio, atualizar_convenio, deletar_convenio, ler_convenio, listar_convenio, ...)
VALUES ('Administrador', true, true, true, true, true, ...);
```

## Exemplos de Uso com cURL

### Criar Convênio

```bash
curl -X POST http://localhost:8080/clinica-medica-administrativo/convenios \
  -H "Content-Type: application/json" \
  -d '{
    "adminUsuario": "admin",
    "adminSenha": 123456,
    "nome": "Plano Saúde Basic",
    "descricao": "Plano básico com cobertura essencial"
  }'
```

### Atualizar Convênio

```bash
curl -X PUT http://localhost:8080/clinica-medica-administrativo/convenios/123 \
  -H "Content-Type: application/json" \
  -d '{
    "adminUsuario": "admin",
    "adminSenha": 123456,
    "nome": "Plano Saúde Basic Plus",
    "descricao": "Plano básico atualizado com mais benefícios"
  }'
```

### Remover Convênio

```bash
curl -X DELETE "http://localhost:8080/clinica-medica-administrativo/convenios/123?adminUsuario=admin&adminSenha=123456"
```

### Listar Convênios (Sem autorização necessária)

```bash
curl -X GET http://localhost:8080/clinica-medica-administrativo/convenios/listar
```

### Buscar Convênio por ID (Sem autorização necessária)

```bash
curl -X GET http://localhost:8080/clinica-medica-administrativo/convenios/123
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

Este sistema de autorização para convênios se integra com:

- **Módulo Administrativo**: Gerenciamento completo de convênios
- **Módulo de Agendamento**: Uso de convênios nas consultas (apenas leitura)
- **Sistema de Perfis**: Controle granular de permissões por tipo de funcionário
