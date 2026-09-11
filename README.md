# ConsertaAI - Plataforma de Zeladoria Urbana Inteligente

Projeto Corporativo com Clean Architecture, Java 21, Spring Boot 3, Design Patterns GoF e Inteligencia Artificial.
Autora: Erica Leandra Da Silva | Versao: 1.0.0

ConsertaAI e uma plataforma de zeladoria urbana para cidades inteligentes. O cidadao relata problemas urbanos (buracos na via, iluminacao publica apagada, lixo acumulado) por texto ou voz, e o sistema utiliza Inteligencia Artificial para classificar automaticamente a categoria e calcular o nivel de urgencia/prioridade, acompanhando todo o ciclo de vida do atendimento por meio de uma maquina de estados finita.

---

## Acesso ao Projeto em Producao

O projeto esta publicado e acessivel publicamente no Railway:

- Aplicacao (interface web): https://conserta-ai-production.up.railway.app
- Documentacao interativa da API (Swagger): https://conserta-ai-production.up.railway.app/swagger-ui/index.html

Ao acessar o endereco principal, a interface web carrega automaticamente com um painel de gestao de ocorrencias, incluindo um formulario de triagem com Inteligencia Artificial e a listagem de ocorrencias cadastradas no banco de dados.

---

## Padroes de Projeto Aplicados (GoF e Spring Framework)

O projeto foi arquitetado como uma vitrine pratica dos principais Design Patterns do GoF (Gang of Four) combinados com os padroes nativos do Spring Framework:

### 1. Padroes Criacionais (Creational)
- Factory Method (GoF): Ocorrencia.registrar(...) centraliza a criacao e garante que toda ocorrencia sempre nasca no estado inicial Aberta.
- Singleton (Spring Core): Todos os @Service, @Component e @Configuration gerenciados com ciclo de vida unico pelo Spring IoC Container.

### 2. Padroes Estruturais (Structural)
- Adapter Pattern (GoF / Ports & Adapters): OcorrenciaRepositoryAdapter adapta a porta pura do dominio (RepositorioOcorrencia) para a interface SpringDataOcorrenciaRepository do JPA.
- Facade Pattern (GoF): ConsertaAiFacade unifica todos os casos de uso (registro, consulta, avanco de status e IA) em uma interface simplificada para os controllers.
- Data Mapper Pattern: OcorrenciaMapper isola a entidade de dominio da entidade JPA de persistencia.

### 3. Padroes Comportamentais (Behavioral)
- State Pattern (GoF + Java 21 Sealed Types): StatusOcorrencia (Aberta, EmAnalise, Resolvida, Reaberta) modela os estados da ocorrencia com validacoes semanticas via Pattern Matching no switch.
- Strategy Pattern (GoF): PriorizacaoStrategy (BuracoViaStrategy, IluminacaoPublicaStrategy, LixoAcumuladoStrategy, DefaultStrategy) calculam polimorficamente a prioridade conforme a gravidade.
- Template Method Pattern (GoF): AbstractNotificadorTemplate define o esqueleto do algoritmo de notificacao (formatacao, envio e auditoria).

### 4. Padroes do Spring Framework
- Dependency Injection & Inversion of Control (IoC): Injecao estrita por construtor em todas as classes.
- Repository Pattern: Abstracao de persistencia atraves do Spring Data JPA.
- Global Controller Advice: Centralizacao do tratamento de erros em formato JSON com GlobalExceptionHandler.

---

## Arquitetura em Camadas (Hexagonal / Clean Architecture)

```
src/main/java/com/ericajavaproagent/consertaai/
├── ConsertaAiApplication.java                  # Ponto de Entrada Spring Boot
│
├── domain/                                     # 100% Java Puro (Sem Frameworks)
│   ├── model/
│   │   ├── Ocorrencia.java                     # Entidade Rica (State Pattern + Factory)
│   │   ├── StatusOcorrencia.java               # Sealed Interface com Records
│   │   ├── Categoria.java                      # Enum de Categorias
│   │   └── Prioridade.java                     # Enum de Niveis de Prioridade
│   ├── strategy/
│   │   ├── PriorizacaoStrategy.java            # Interface Strategy Pattern
│   │   ├── BuracoViaStrategy.java              # Estrategia de Buracos
│   │   ├── IluminacaoPublicaStrategy.java      # Estrategia de Iluminacao
│   │   ├── LixoAcumuladoStrategy.java          # Estrategia de Lixo
│   │   └── PriorizacaoStrategyContext.java     # Contexto do Strategy Pattern
│   └── port/
│       ├── RepositorioOcorrencia.java          # Output Port de Persistencia
│       ├── NotificadorPort.java                # Output Port de Notificacao
│       └── IaClassifierPort.java               # Output Port de Inteligencia Artificial
│
├── application/                                # Regras da Aplicacao & Use Cases
│   ├── facade/
│   │   └── ConsertaAiFacade.java               # Facade Pattern
│   └── usecase/
│       ├── RegistrarOcorrenciaUseCase.java
│       ├── ConsultarOcorrenciaUseCase.java
│       ├── AvancarStatusOcorrenciaUseCase.java
│       └── TriagemComIaUseCase.java
│
└── infrastructure/                             # Adaptadores Externos & Web
    ├── adapter/
    │   ├── in/web/                             # Controllers REST e DTOs
    │   │   ├── OcorrenciaController.java
    │   │   ├── IaZeladoriaController.java
    │   │   └── dto/
    │   ├── out/persistence/                    # Adaptadores JPA e Mappers
    │   ├── out/notification/                   # Template Method de Notificacao
    │   └── out/ia/                             # Adaptador de IA / NLP
    └── exception/
        └── GlobalExceptionHandler.java
```

---

## Endpoints RESTful da API

### Ocorrencias de Zeladoria (/api/v1/ocorrencias)
| Metodo | Endpoint | Descricao |
| :--- | :--- | :--- |
| `POST` | `/api/v1/ocorrencias` | Registra nova ocorrencia com retorno de status 201 Created e Location Header |
| `GET` | `/api/v1/ocorrencias/{protocolo}` | Consulta os detalhes e o estado atual pelo protocolo |
| `GET` | `/api/v1/ocorrencias` | Lista todas as ocorrencias cadastradas |
| `PATCH` | `/api/v1/ocorrencias/{protocolo}/status` | Avanca o estado na maquina de estados (EM_ANALISE, RESOLVIDA, REABERTA) |

### Inteligencia Artificial (/api/v1/triagens-ia)
| Metodo | Endpoint | Descricao |
| :--- | :--- | :--- |
| `POST` | `/api/v1/triagens-ia` | Triagem automatica com IA sugerindo categoria e prioridade a partir do relato do cidadao |

---

## Como Utilizar (Ambiente em Producao)

1. Acesse https://conserta-ai-production.up.railway.app no navegador.
2. Na secao "Gestao de Ocorrencias Urbanas", visualize as ocorrencias ja cadastradas, com filtros por protocolo, endereco, status e categoria.
3. Para registrar uma nova ocorrencia manualmente, preencha o formulario "Registrar Ocorrencia" com categoria, descricao, endereco e coordenadas, e clique em "Salvar no Banco de Dados".
4. Para testar a triagem automatica com Inteligencia Artificial, cole um relato livre do cidadao no campo "Triagem com Inteligencia Artificial" e clique em "Executar Analise de IA".
5. Para explorar e testar os endpoints da API diretamente, acesse a documentacao Swagger em https://conserta-ai-production.up.railway.app/swagger-ui/index.html.

---

## Como Executar Localmente

1. Abra o projeto no IntelliJ IDEA.
2. Execute a classe principal ConsertaAiApplication.java.
3. Acesse a documentacao interativa no Swagger:
   http://localhost:8080/swagger-ui.html

---

## Infraestrutura de Deploy

- Hospedagem: Railway
- Build: Dockerfile multi-estagio (compilacao com Maven em Java 21, execucao em imagem enxuta com JRE 21)
- Banco de dados em producao: PostgreSQL (gerenciado pelo Railway)
- Banco de dados em desenvolvimento local: H2 em arquivo

---

## Autora

Desenvolvido por Erica Leandra Da Silva.