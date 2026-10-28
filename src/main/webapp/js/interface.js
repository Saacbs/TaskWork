/* Somente apresentação: as regras e validações são executadas no servidor. */
'use strict';
document.addEventListener('click', function (evento) {
    const botaoSenha = evento.target.closest('[data-mostrar-senha]');
    if (botaoSenha) {
        const campo = document.getElementById(botaoSenha.dataset.mostrarSenha);
        if (campo) {
            const mostrar = campo.type === 'password';
            campo.type = mostrar ? 'text' : 'password';
            botaoSenha.textContent = mostrar ? 'Ocultar' : 'Mostrar';
            botaoSenha.setAttribute('aria-pressed', String(mostrar));
        }
    }
    if (evento.target.closest('[data-imprimir]')) {
        window.print();
    }
});
