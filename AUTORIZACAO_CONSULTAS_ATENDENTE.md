# Sistema de Autorização para Operações de Consulta

## Visão Geral

O sistema agora implementa uma regra de segurança onde **somente atendentes autorizados podem cadastrar, atualizar ou remover consultas**. Esta validação é baseada no sistema de perfis existente e nas credenciais do atendente.

## Como Funciona

### 1. Verificação de Perfil

- O sistema verifica se o funcionário que está tentando fazer a operação possui um perfil com as permissões específicas:
  - `cadastrarConsulta = true` para adicionar consultas
  - `atualizarConsulta = true` para atualizar consultas
  - `deletarConsulta = true` para remover consultas
- Esta verificação é feita através das credenciais (usuário e senha) fornecidas

### 2. Estrutura dos Requests

#### Para Cadastrar Nova Consulta (POST)

```json
{
  "atendenteUsuario": "atendente01",
  "atendenteSenha": 123456,
  "dataHorario": "2024-01-15T14:30:00",
  "sintomas": "Dor de cabeça persistente",
  "eRetorno": false,
  "estaAtiva": true,
  "pacienteId": 1,
  "medicoId": 2,
  "convenioId": 3
}
```

#### Para Atualizar Consulta Existente (PUT)

```json
{
  "atendenteUsuario": "atendente01",
  "atendenteSenha": 123456,
  "dataHorario": "2024-01-15T15:00:00",
  "sintomas": "Dor de cabeça e náusea",
  "eRetorno": false,
  "estaAtiva": true,
  "pacienteId": 1,
  "medicoId": 2,
  "convenioId": 3
}
```

### 3. Fluxo de Validação

1. **Verificação de Credenciais**: Sistema verifica se `atendenteUsuario` e `atendenteSenha` foram fornecidos
2. **Autenticação**: Busca o funcionário pelas credenciais fornecidas
3. **Autorização**: Verifica se o perfil do funcionário tem a permissão específica para a operação
4. **Execução**: Se autorizado, procede com a operação (criar/atualizar/remover)

## Endpoints e Métodos

### 1. Adicionar Consulta

**POST** `/clinica-medica-agendamento/consultas`

**Body (JSON):**

```json
{
  "atendenteUsuario": "atendente01",
  "atendenteSenha": 123456,
  "dataHorario": "2024-01-15T14:30:00",
  "sintomas": "Sintomas do paciente",
  "eRetorno": false,
  "estaAtiva": true,
  "pacienteId": 1,
  "medicoId": 2,
  "convenioId": 3
}
```

### 2. Atualizar Consulta

**PUT** `/clinica-medica-agendamento/consultas/{id}`

**Body (JSON):**

```json
{
  "atendenteUsuario": "atendente01",
  "atendenteSenha": 123456,
  "dataHorario": "2024-01-15T15:00:00",
  "sintomas": "Sintomas atualizados",
  "eRetorno": true,
  "estaAtiva": true,
  "pacienteId": 1,
  "medicoId": 2,
  "convenioId": 3
}
```

### 3. Remover Consulta

**DELETE** `/clinica-medica-agendamento/consultas/{id}?atendenteUsuario=atendente01&atendenteSenha=123456`

**Parâmetros de Query:**

- `atendenteUsuario`: Nome de usuário do atendente
- `atendenteSenha`: Senha do atendente

### 4. Listar e Buscar Consultas (Sem Autorização)

**GET** `/clinica-medica-agendamento/consultas/listar` - Lista todas as consultas

**GET** `/clinica-medica-agendamento/consultas/{id}` - Busca consulta por ID

## Respostas do Sistema

### Sucesso

#### Consulta Criada (201 - Created)

```json
{
  "id": 123,
  "dataHorario": "2024-01-15T14:30:00",
  "sintomas": "Dor de cabeça persistente",
  "eRetorno": false,
  "estaAtiva": true,
  "paciente": {
    "id": 1,
    "nome": "João Silva"
  },
  "medico": {
    "id": 2,
    "nome": "Dr. Maria Santos"
  },
  "convenio": {
    "id": 3,
    "nome": "Plano Saúde"
  }
}
```

#### Consulta Atualizada (200 - OK)

```json
{
  "id": 123,
  "dataHorario": "2024-01-15T15:00:00",
  "sintomas": "Dor de cabeça e náusea",
  ...
}
```

#### Consulta Removida (204 - No Content)

_Sem conteúdo no corpo da resposta_

### Erros

#### Credenciais não fornecidas (400 - Bad Request)

```
"Credenciais do atendente são obrigatórias para cadastrar consultas."
```

```
"Credenciais do atendente são obrigatórias para atualizar consultas."
```

```
"Credenciais do atendente são obrigatórias para remover consultas."
```

#### Sem permissão (403 - Forbidden)

```
"Acesso negado. Apenas atendentes autorizados podem cadastrar consultas."
```

```
"Acesso negado. Apenas atendentes autorizados podem atualizar consultas."
```

```
"Acesso negado. Apenas atendentes autorizados podem remover consultas."
```

## Configuração de Perfis

Para que um funcionário possa manipular consultas, seu perfil deve ter as permissões apropriadas:

- `cadastrarConsulta = true` - Para criar consultas
- `atualizarConsulta = true` - Para atualizar consultas
- `deletarConsulta = true` - Para remover consultas

**Exemplo de perfil de atendente:**

```sql
INSERT INTO perfis (nome, cadastrar_consulta, atualizar_consulta, deletar_consulta, ler_consulta, listar_consulta, ...)
VALUES ('Atendente', true, true, true, true, true, ...);
```

## Exemplos de Uso com cURL

### Criar Consulta

```bash
curl -X POST http://localhost:8080/clinica-medica-agendamento/consultas \
  -H "Content-Type: application/json" \
  -d '{
    "atendenteUsuario": "atendente01",
    "atendenteSenha": 123456,
    "dataHorario": "2024-01-15T14:30:00",
    "sintomas": "Dor de cabeça persistente",
    "eRetorno": false,
    "estaAtiva": true,
    "pacienteId": 1,
    "medicoId": 2,
    "convenioId": 3
  }'
```

### Atualizar Consulta

```bash
curl -X PUT http://localhost:8080/clinica-medica-agendamento/consultas/123 \
  -H "Content-Type: application/json" \
  -d '{
    "atendenteUsuario": "atendente01",
    "atendenteSenha": 123456,
    "dataHorario": "2024-01-15T15:00:00",
    "sintomas": "Dor de cabeça e náusea"
  }'
```

### Remover Consulta

```bash
curl -X DELETE "http://localhost:8080/clinica-medica-agendamento/consultas/123?atendenteUsuario=atendente01&atendenteSenha=123456"
```

### Listar Consultas (Sem autorização necessária)

```bash
curl -X GET http://localhost:8080/clinica-medica-agendamento/consultas/listar
```

### Buscar Consulta por ID (Sem autorização necessária)

```bash
curl -X GET http://localhost:8080/clinica-medica-agendamento/consultas/123
```

## Segurança

- As credenciais do atendente são validadas a cada requisição
- Não há armazenamento de sessão - cada operação requer autenticação
- O sistema registra logs de todas as tentativas de acesso (permitidas e negadas)
- Apenas funcionários com perfil adequado podem executar operações de escrita
- Operações de leitura (listar e buscar) não requerem autorização especial

## Diferenças das Operações

- **Operações de Escrita** (POST, PUT, DELETE): Requerem autorização de atendente
- **Operações de Leitura** (GET): Não requerem autorização especial
- **Método DELETE**: Usa parâmetros de query em vez de body para as credenciais
