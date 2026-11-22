package model;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Resumo {
    private int total,
            abertos,
            emAtendimento,
            resolvidos,
            cancelados,
            comPrazo,
            noPrazo,
            avaliados,
            somaNotas;
    private long minutosResolucao;
    private int aguardando, recusados;

    public int getAguardando() {
        return aguardando;
    }

    public int getRecusados() {
        return recusados;
    }

    public Resumo(List<Chamado> chamados) {
        total = chamados.size();
        for (Chamado c : chamados) {
            if ("Aberto".equals(c.getStatus())) abertos++;
            if ("Em atendimento".equals(c.getStatus())) emAtendimento++;
            if ("Cancelado".equals(c.getStatus())) cancelados++;
            if (c.getStatus().startsWith("Aguardando")) aguardando++;
            if ("Recusado".equals(c.getStatus())) recusados++;
            if ("Resolvido".equals(c.getStatus())) {
                resolvidos++;
                if (c.getCriadoEm() != null
                        && c.getResolvidoEm() != null
                        && !c.getResolvidoEm().before(c.getCriadoEm())) {
                    long minutos =
                            TimeUnit.MILLISECONDS.toMinutes(
                                    c.getResolvidoEm().getTime() - c.getCriadoEm().getTime());
                    minutosResolucao += minutos;
                    comPrazo++;
                    long prazo =
                            c.getPrazoSla() != null
                                    ? c.getPrazoSla().getTime()
                                    : c.getCriadoEm().getTime()
                                            + TimeUnit.HOURS.toMillis(
                                                    Chamado.horasSla(c.getPrioridade()));
                    if (c.getResolvidoEm().getTime() <= prazo) noPrazo++;
                }
                if (c.getAvaliacao() > 0) {
                    avaliados++;
                    somaNotas += c.getAvaliacao();
                }
            }
        }
    }

    public int getTotal() {
        return total;
    }

    public int getAbertos() {
        return abertos;
    }

    public int getEmAtendimento() {
        return emAtendimento;
    }

    public int getResolvidos() {
        return resolvidos;
    }

    public int getCancelados() {
        return cancelados;
    }

    public String getSla() {
        return comPrazo == 0
                ? "Sem dados"
                : String.format(
                        Locale.forLanguageTag("pt-BR"), "%.1f%%", 100.0 * noPrazo / comPrazo);
    }

    public String getTempo() {
        return comPrazo == 0
                ? "Sem dados"
                : String.format(
                        Locale.forLanguageTag("pt-BR"),
                        "%.1f h",
                        minutosResolucao / 60.0 / comPrazo);
    }

    public String getSatisfacao() {
        return avaliados == 0
                ? "Sem avaliações"
                : String.format(
                        Locale.forLanguageTag("pt-BR"), "%.1f / 5", 1.0 * somaNotas / avaliados);
    }

    public int getComPrazo() {
        return comPrazo;
    }
}
