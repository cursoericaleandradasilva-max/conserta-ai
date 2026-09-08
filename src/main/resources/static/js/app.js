let todasOcorrencias = [];
let ultimaTriagem = null;

document.addEventListener("DOMContentLoaded", () => {
    carregarOcorrencias();

    // Elementos da Interface
    const btnTriagemIa = document.getElementById("btnTriagemIa");
    const btnAplicarIa = document.getElementById("btnAplicarIa");
    const formOcorrencia = document.getElementById("formOcorrencia");
    const btnAtualizarLista = document.getElementById("btnAtualizarLista");
    const filtroTexto = document.getElementById("filtroTexto");
    const filtroStatus = document.getElementById("filtroStatus");
    const filtroCategoria = document.getElementById("filtroCategoria");

    // Modal
    const btnFecharModal = document.getElementById("btnFecharModal");
    const btnCancelarModal = document.getElementById("btnCancelarModal");
    const btnConfirmarModal = document.getElementById("btnConfirmarModal");

    // 1. Executar Triagem de IA
    btnTriagemIa.addEventListener("click", async () => {
        const relato = document.getElementById("relatoIa").value.trim();
        if (!relato) {
            showToast("Digite o relato do cidadao para analise da IA.", "error");
            return;
        }

        btnTriagemIa.disabled = true;
        btnTriagemIa.textContent = "Processando NLP...";

        try {
            const response = await fetch("/api/v1/triagens-ia", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ relato })
            });

            if (!response.ok) throw new Error("Falha ao comunicar com o servico de IA");

            const data = await response.json();
            ultimaTriagem = data;

            document.getElementById("iaCategoria").textContent = data.categoriaDetectada;
            document.getElementById("iaPrioridade").textContent = data.prioridadeSugerida;
            document.getElementById("iaJustificativa").textContent = data.justificativaIa;
            document.getElementById("resultadoIa").classList.remove("hidden");
            showToast("Analise de IA concluida com sucesso!", "success");
        } catch (err) {
            showToast("Erro na triagem de IA: " + err.message, "error");
        } finally {
            btnTriagemIa.disabled = false;
            btnTriagemIa.textContent = "Executar Analise de IA";
        }
    });

    // 2. Aplicar Dados da IA no Formulario
    btnAplicarIa.addEventListener("click", () => {
        if (!ultimaTriagem) return;
        document.getElementById("categoria").value = ultimaTriagem.categoriaDetectada;
        document.getElementById("descricao").value = document.getElementById("relatoIa").value;
        document.getElementById("endereco").focus();
        showToast("Dados aplicados ao formulario com sucesso!", "success");
    });

    // 3. Salvar Ocorrencia via API RESTful
    formOcorrencia.addEventListener("submit", async (e) => {
        e.preventDefault();

        const payload = {
            categoria: document.getElementById("categoria").value,
            descricao: document.getElementById("descricao").value,
            enderecoOuReferencia: document.getElementById("endereco").value,
            latitude: parseFloat(document.getElementById("latitude").value),
            longitude: parseFloat(document.getElementById("longitude").value)
        };

        try {
            const response = await fetch("/api/v1/ocorrencias", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload)
            });

            if (!response.ok) {
                const erro = await response.json();
                throw new Error(erro.mensagem || "Erro ao registrar ocorrencia");
            }

            const data = await response.json();
            showToast("Ocorrencia salva no banco com protocolo: " + data.protocolo.substring(0, 8) + "...", "success");

            formOcorrencia.reset();
            document.getElementById("resultadoIa").classList.add("hidden");
            document.getElementById("relatoIa").value = "";
            carregarOcorrencias();
        } catch (err) {
            showToast("Falha no registro: " + err.message, "error");
        }
    });

    // 4. Filtros e Busca em Tempo Real
    filtroTexto.addEventListener("input", renderizarTabela);
    filtroStatus.addEventListener("change", renderizarTabela);
    filtroCategoria.addEventListener("change", renderizarTabela);
    btnAtualizarLista.addEventListener("click", carregarOcorrencias);

    // 5. Eventos do Modal
    btnFecharModal.addEventListener("click", fecharModal);
    btnCancelarModal.addEventListener("click", fecharModal);
    btnConfirmarModal.addEventListener("click", executarTransicaoModal);
});

// Carrega os dados da API REST
async function carregarOcorrencias() {
    try {
        const response = await fetch("/api/v1/ocorrencias");
        if (!response.ok) throw new Error("Erro na requisicao HTTP");

        todasOcorrencias = await response.json();
        atualizarMetricasKPIs(todasOcorrencias);
        renderizarTabela();
    } catch (err) {
        document.getElementById("tabelaOcorrencias").innerHTML = `
            <tr><td colspan="6" class="text-center py-4" style="color: red;">Falha ao carregar dados do banco de dados: ${err.message}</td></tr>
        `;
    }
}

// Atualiza os cards superiores de KPIs
function atualizarMetricasKPIs(lista) {
    document.getElementById("kpiTotal").textContent = lista.length;
    document.getElementById("kpiAbertas").textContent = lista.filter(o => o.status === "Aberta").length;
    document.getElementById("kpiEmAnalise").textContent = lista.filter(o => o.status === "EmAnalise").length;
    document.getElementById("kpiResolvidas").textContent = lista.filter(o => o.status === "Resolvida").length;
    document.getElementById("kpiCriticas").textContent = lista.filter(o => o.prioridade === "CRITICA").length;
}

// Renderiza as linhas da tabela com base nos filtros
function renderizarTabela() {
    const texto = document.getElementById("filtroTexto").value.toLowerCase().trim();
    const statusFiltro = document.getElementById("filtroStatus").value;
    const catFiltro = document.getElementById("filtroCategoria").value;

    const filtradas = todasOcorrencias.filter(item => {
        const bateTexto = texto === "" ||
            item.protocolo.toLowerCase().includes(texto) ||
            item.enderecoOuReferencia.toLowerCase().includes(texto) ||
            item.descricao.toLowerCase().includes(texto);

        const bateStatus = statusFiltro === "TODOS" || item.status === statusFiltro;
        const bateCat = catFiltro === "TODAS" || item.categoria === catFiltro;

        return bateTexto && bateStatus && bateCat;
    });

    const tbody = document.getElementById("tabelaOcorrencias");

    if (filtradas.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center py-4">Nenhuma ocorrencia encontrada com os filtros selecionados.</td></tr>';
        return;
    }

    tbody.innerHTML = filtradas.map(item => `
        <tr>
            <td><code>${item.protocolo.substring(0, 8)}...</code></td>
            <td><strong>${item.categoria}</strong></td>
            <td><span class="badge badge-${item.prioridade}">${item.prioridade}</span></td>
            <td>
                <span class="badge badge-${item.status}">${item.status}</span>
                <div style="font-size: 11px; color: #64748b; margin-top: 4px;">${item.detalheStatus}</div>
            </td>
            <td>
                <div style="font-weight: 500;">${item.enderecoOuReferencia}</div>
                <div style="font-size: 11px; color: #94a3b8;">${item.descricao}</div>
            </td>
            <td class="text-right">
                <div class="action-btns">
                    ${gerarBotoesAcao(item.protocolo, item.status)}
                </div>
            </td>
        </tr>
    `).join("");
}

// Botoes contextuais da Maquina de Estados
function gerarBotoesAcao(protocolo, status) {
    if (status === "Aberta") {
        return `<button class="btn btn-sm btn-outline" onclick="abrirModal('${protocolo}', 'EM_ANALISE')">Iniciar Analise</button>`;
    } else if (status === "EmAnalise") {
        return `<button class="btn btn-sm btn-primary" onclick="abrirModal('${protocolo}', 'RESOLVIDA')">Resolver / Concluir</button>`;
    } else if (status === "Resolvida") {
        return `<button class="btn btn-sm btn-outline" onclick="abrirModal('${protocolo}', 'REABERTA')">Reabrir</button>`;
    } else if (status === "Reaberta") {
        return `<button class="btn btn-sm btn-outline" onclick="abrirModal('${protocolo}', 'EM_ANALISE')">Reanalisar</button>`;
    }
    return "-";
}

// Controle do Modal
function abrirModal(protocolo, novoStatus) {
    document.getElementById("modalProtocolo").value = protocolo;
    document.getElementById("modalNovoStatus").value = novoStatus;
    
    const campoResp = document.getElementById("campoResponsavel");
    const campoObs = document.getElementById("campoObservacao");
    const titulo = document.getElementById("modalTitulo");

    if (novoStatus === "EM_ANALISE") {
        titulo.textContent = "Iniciar Analise Tecnica";
        campoResp.classList.remove("hidden");
        campoObs.classList.add("hidden");
        document.getElementById("modalResponsavel").value = "Equipe de Zeladoria Urbana";
    } else if (novoStatus === "RESOLVIDA") {
        titulo.textContent = "Concluir / Resolver Ocorrencia";
        campoResp.classList.add("hidden");
        campoObs.classList.remove("hidden");
        document.getElementById("modalObservacao").value = "Servico concluido com sucesso no local.";
    } else if (novoStatus === "REABERTA") {
        titulo.textContent = "Reabrir Ocorrencia";
        campoResp.classList.add("hidden");
        campoObs.classList.remove("hidden");
        document.getElementById("modalObservacao").value = "Problema voltou a ocorrer ou servico incompleto.";
    }

    document.getElementById("modalStatus").classList.remove("hidden");
}

function fecharModal() {
    document.getElementById("modalStatus").classList.add("hidden");
}

async function executarTransicaoModal() {
    const protocolo = document.getElementById("modalProtocolo").value;
    const novoStatus = document.getElementById("modalNovoStatus").value;
    const responsavel = document.getElementById("modalResponsavel").value.trim();
    const observacao = document.getElementById("modalObservacao").value.trim();

    const payload = {
        novoStatusTipo: novoStatus,
        responsavel: novoStatus === "EM_ANALISE" ? responsavel : null,
        observacaoOuMotivo: novoStatus !== "EM_ANALISE" ? observacao : null
    };

    try {
        const response = await fetch(`/api/v1/ocorrencias/${protocolo}/status`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const err = await response.json();
            throw new Error(err.mensagem || "Transicao de estado invalida.");
        }

        fecharModal();
        showToast("Status da ocorrencia atualizado para " + novoStatus + "!", "success");
        carregarOcorrencias();
    } catch (err) {
        showToast("Erro na transicao: " + err.message, "error");
    }
}

// Toast Notifications
function showToast(mensagem, tipo = "success") {
    const container = document.getElementById("toastContainer");
    const toast = document.createElement("div");
    toast.className = `toast toast-${tipo}`;
    toast.textContent = mensagem;

    container.appendChild(toast);

    setTimeout(() => {
        toast.remove();
    }, 4000);
}
