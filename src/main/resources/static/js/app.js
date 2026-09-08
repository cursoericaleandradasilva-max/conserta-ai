document.addEventListener("DOMContentLoaded", () => {
    carregarOcorrencias();

    const btnTriagemIa = document.getElementById("btnTriagemIa");
    const btnAplicarIa = document.getElementById("btnAplicarIa");
    const formOcorrencia = document.getElementById("formOcorrencia");
    const btnAtualizarLista = document.getElementById("btnAtualizarLista");

    let ultimaTriagem = null;

    // 1. Executar Triagem com IA
    btnTriagemIa.addEventListener("click", async () => {
        const relato = document.getElementById("relatoIa").value.trim();
        if (!relato) {
            alert("Por favor, digite um relato para a IA analisar.");
            return;
        }

        btnTriagemIa.disabled = true;
        btnTriagemIa.textContent = "Analisando com IA...";

        try {
            const response = await fetch("/api/v1/triagens-ia", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ relato })
            });

            if (!response.ok) throw new Error("Erro ao consultar a API de IA");

            const data = await response.json();
            ultimaTriagem = data;

            document.getElementById("iaCategoria").textContent = data.categoriaDetectada;
            document.getElementById("iaPrioridade").textContent = data.prioridadeSugerida;
            document.getElementById("iaJustificativa").textContent = data.justificativaIa;
            document.getElementById("resultadoIa").classList.remove("hidden");
        } catch (error) {
            alert("Falha na comunicacao com o modulo de IA: " + error.message);
        } finally {
            btnTriagemIa.disabled = false;
            btnTriagemIa.textContent = "Executar Triagem com IA";
        }
    });

    // 2. Aplicar dados da IA no formulario
    btnAplicarIa.addEventListener("click", () => {
        if (!ultimaTriagem) return;
        document.getElementById("categoria").value = ultimaTriagem.categoriaDetectada;
        document.getElementById("descricao").value = document.getElementById("relatoIa").value;
        document.getElementById("endereco").focus();
    });

    // 3. Registrar Ocorrencia via API REST
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
            alert("Ocorrencia registrada com sucesso! Protocolo: " + data.protocolo);
            formOcorrencia.reset();
            document.getElementById("resultadoIa").classList.add("hidden");
            document.getElementById("relatoIa").value = "";
            carregarOcorrencias();
        } catch (error) {
            alert("Falha no registro: " + error.message);
        }
    });

    // 4. Atualizar lista
    btnAtualizarLista.addEventListener("click", carregarOcorrencias);
});

async function carregarOcorrencias() {
    const tabela = document.getElementById("tabelaOcorrencias");
    try {
        const response = await fetch("/api/v1/ocorrencias");
        if (!response.ok) throw new Error("Erro ao buscar ocorrencias");

        const lista = await response.json();
        if (lista.length === 0) {
            tabela.innerHTML = '<tr><td colspan="6" class="text-center">Nenhuma ocorrencia registrada ate o momento.</td></tr>';
            return;
        }

        tabela.innerHTML = lista.map(item => `
            <tr>
                <td><code>${item.protocolo.substring(0, 8)}...</code></td>
                <td><strong>${item.categoria}</strong></td>
                <td><span class="badge badge-${item.prioridade}">${item.prioridade}</span></td>
                <td>
                    <span class="badge badge-${item.status}">${item.status}</span>
                    <div style="font-size: 11px; color: #757575; margin-top: 4px;">${item.detalheStatus}</div>
                </td>
                <td>${item.enderecoOuReferencia}</td>
                <td>
                    <div class="action-buttons">
                        ${gerarBotoesAcao(item.protocolo, item.status)}
                    </div>
                </td>
            </tr>
        `).join("");
    } catch (error) {
        tabela.innerHTML = `<tr><td colspan="6" class="text-center" style="color: red;">Erro ao carregar ocorrencias: ${error.message}</td></tr>`;
    }
}

function gerarBotoesAcao(protocolo, status) {
    if (status === "Aberta") {
        return `<button class="btn btn-small" onclick="avancarStatus('${protocolo}', 'EM_ANALISE', 'Equipe de Zeladoria')">Iniciar Analise</button>`;
    } else if (status === "EmAnalise") {
        return `<button class="btn btn-small" onclick="avancarStatus('${protocolo}', 'RESOLVIDA', 'Problema sanado pela equipe')">Concluir / Resolver</button>`;
    } else if (status === "Resolvida") {
        return `<button class="btn btn-small" onclick="avancarStatus('${protocolo}', 'REABERTA', 'Problema voltou a ocorrer')">Reabrir</button>`;
    } else if (status === "Reaberta") {
        return `<button class="btn btn-small" onclick="avancarStatus('${protocolo}', 'EM_ANALISE', 'Reanalise por equipe tecnica')">Reanalisar</button>`;
    }
    return "-";
}

async function avancarStatus(protocolo, novoStatusTipo, observacao) {
    const payload = {
        novoStatusTipo: novoStatusTipo,
        responsavel: novoStatusTipo === "EM_ANALISE" ? observacao : null,
        observacaoOuMotivo: novoStatusTipo !== "EM_ANALISE" ? observacao : null
    };

    try {
        const response = await fetch(`/api/v1/ocorrencias/${protocolo}/status`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const err = await response.json();
            throw new Error(err.mensagem || "Transicao invalida");
        }

        carregarOcorrencias();
    } catch (error) {
        alert("Erro na transicao de estado: " + error.message);
    }
}
