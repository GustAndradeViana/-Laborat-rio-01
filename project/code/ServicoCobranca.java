public class ServicoCobranca {
    public void notificarCobranca(Aluno aluno) {
        System.out.println("[Cobranca] Notificar cobrança para aluno: " + aluno.getNome() +
                " - disciplinas: " + aluno.getMatriculas().size());
    }
}
