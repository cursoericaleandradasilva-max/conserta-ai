# 🏙️ ConsertaAI - Zeladoria Urbana Inteligente

> **Aplicação Corporativa com Clean Architecture, Java 21, Spring Boot 3, GoF Design Patterns e Inteligência Artificial**  
> Autora: **Erica Leandra Da Silva** | Versão: **1.0.0**

O **ConsertaAI** é uma plataforma inovadora de zeladoria urbana para cidades inteligentes. O cidadão relata problemas urbanos (buracos na via, iluminação pública apagada, lixo acumulado) por texto ou voz, e o sistema utiliza **Inteligência Artificial** para classificar automaticamente a categoria e calcular o nível de urgência/prioridade, acompanhando todo o ciclo de vida do atendimento por meio de uma máquina de estados finita.

---

## 🏛️ Padrões de Projeto Aplicados (GoF & Spring Framework)

O projeto foi arquitetado como uma vitrine prática dos principais **Design Patterns do GoF (Gang of Four)** combinados com os padrões nativos do **Spring Framework**:

### 1. 🏗️ Padrões Criacionais (Creational)
- **Factory Method (GoF):** `Ocorrencia.registrar(...)` centraliza a criação e garante que toda ocorrência nasça no estado inicial `Aberta`.
- **Singleton (Spring Core):** Todos os `@Service`, `@Component` e `@Configuration` gerenciados pelo Spring IoC Container com ciclo de vida único.

### 2. 🧩 Padrões Estruturais (Structural)
- **Adapter Pattern (GoF / Ports & Adapters):** `OcorrenciaRepositoryAdapter` adapta a porta de domínio puro (`RepositorioOcorrencia`) para a interface `SpringDataOcorrenciaRepository` do JPA.
- **Facade Pattern (GoF):** `ConsertaAiFacade` unifica o subsistema complexo de casos de uso, IA, regras de negócio e notificações em uma interface simplificada para os controllers.
- **Data Mapper Pattern:** `OcorrenciaMapper` isola completamente a entidade rica de domínio da entidade JPA de persistência.

### 3. 🎭 Padrões Comportamentais (Behavioral)
- **State Pattern (GoF + Java 21 Sealed Types):** `StatusOcorrencia` (`Aberta`, `EmAnalise`, `Resolvida`, `Reaberta`) modela os estados e transições semânticas da ocorrência com validação estrita via **Pattern Matching no `switch`**.
- **Strategy Pattern (GoF):** `PriorizacaoStrategy` (`BuracoViaStrategy`, `IluminacaoPublicaStrategy`, `LixoAcumuladoStrategy`, `DefaultStrategy`) calcula algoritmos polimórficos de priorização conforme a categoria e a severidade do relato.
- **Template Method Pattern (GoF):** `AbstractNotificadorTemplate` define o esqueleto do algoritmo de notificação (formatação, envio específico e auditoria de logs), delegando a entrega para classes filhas (`ConsoleNotificadorAdapter`).

### 4. 🍃 Padrões do Spring Framework
- **Dependency Injection & Inversion of Control (IoC):** Injeção estrita por construtor garantindo baixo acoplamento e alta testabilidade.
- **Repository Pattern:** Abstração de persistência através do Spring Data JPA.
- **Global Controller Advice:** Centralização do tratamento de erros em formato JSON padronizado com `@RestControllerAdvice`.

---

## 📁 Arquitetura em Camadas (Hexagonal / Clean Architecture)

```
src/main/java/com/ericajavaproagent/consertaai/
├── ConsertaAiApplication.java                  # Ponto de Entrada Spring Boot
│
├── domain/                                     # 100% Java Puro (Sem Frameworks)
│   ├── model/
│   │   ├── Ocorrencia.java                     # Entidade Rica (State Pattern + Factory)
│   │   ├── StatusOcorrencia.java               # Sealed Interface com Records
│   │   ├── Categoria.java                      # Enum de Categorias
│   │   └── Prioridade.java                     # Enum de Níveis de Prioridade
│   ├── strategy/
│   │   ├── PriorizacaoStrategy.java            # Interface Strategy Pattern
│   │   ├── BuracoViaStrategy.java              # Estratégia de Buracos
│   │   ├── IluminacaoPublicaStrategy.java      # Estratégia de Iluminação
│   │   ├── LixoAcumuladoStrategy.java          # Estratégia de Lixo
│   │   └── PriorizacaoStrategyContext.java     # Contexto do Strategy Pattern
│   └── port/
│       ├── RepositorioOcorrencia.java          # Output Port de Persistência
│       ├── NotificadorPort.java                # Output Port de Notificação
│       └── IaClassifierPort.java               # Output Port de Inteligência Artificial
│
├── application/                                # Regras da Aplicação & Use Cases
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
    │   ├── out/notification/                   # Template Method de Notificação
    │   └── out/ia/                             # Adaptador de IA / NLP
    └── exception/
        └── GlobalExceptionHandler.java
```

---

## 🌐 Endpoints da API

### 🏙️ Ocorrências de Zeladoria (`/api/v1/ocorrencias`)
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/api/v1/ocorrencias` | Registra nova ocorrência calculando a prioridade automaticamente via Strategy |
| `GET` | `/api/v1/ocorrencias/{protocolo}` | Consulta os detalhes e o estado atual pelo protocolo |
| `GET` | `/api/v1/ocorrencias` | Lista todas as ocorrências cadastradas |
| `PATCH` | `/api/v1/ocorrencias/{protocolo}/status` | Avança o estado na máquina de estados (`EM_ANALISE`, `RESOLVIDA`, `REABERTA`) |

### 🤖 Inteligência Artificial (`/api/v1/ia`)
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/api/v1/ia/triagem` | Triagem automática com IA sugerindo categoria e prioridade a partir do relato do cidadão |

---

## 🧪 Testes Automatizados com TDD

O projeto conta com suítes completas de testes unitários:
- **`OcorrenciaTest`**: Valida a máquina de estados (State Pattern), impedindo transições inválidas (ex: pular de `Aberta` direto para `Resolvida`).
- **`RegistrarOcorrenciaUseCaseTest`**: Testa a orquestração do caso de uso isolado com Mocks (**Mockito**, **AssertJ** e **ArgumentCaptor**).

---

## 📖 Como Executar

1. Abra o projeto no IntelliJ IDEA.
2. Execute a classe `ConsertaAiApplication.java`.
3. Acesse a documentação interativa no Swagger:
   👉 **http://localhost:8080/swagger-ui/index.html**

---

## 👩‍💻 Autora

Desenvolvido por **Erica Leandra Da Silva**.
