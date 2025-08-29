public class Aluno extends Usuario {
    private int ra;
    private Curso curso;

    public void matricular(Disciplina disciplina, String tipo) {}
    public void cancelar(Matricula matricula) {}
    
    @Override
    public boolean autenticar(String login, String senha) {
        throw new UnsupportedOperationException("Unimplemented method 'autenticar'");
    }
}
