import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Disciplina implements Serializable {
    private int id;
    private String nome;
    private final Curso curso;
    private Professor professor;
    private final List<Aluno> alunos = new ArrayList<>();
    public static final int MAX_ALUNOS = 60;
    public static final int MIN_PARA_OCORRER = 3;

    public Disciplina(int id, String nome, Curso curso) {
        this.id = id;
        this.nome = nome;
        this.curso = curso;
    }

    public int getId() { return id; }

    public synchronized boolean matricularAluno(Aluno a) {
        if (alunos.size() >= MAX_ALUNOS) return false;
        if (alunos.contains(a)) return false;
        alunos.add(a);
        return true;
    }

    public synchronized boolean removerAluno(Aluno a) {
        return alunos.remove(a);
    }

    public synchronized int numeroAlunos() {
        return alunos.size();
    }

    public List<Aluno> getAlunosMatriculados() {
        return alunos;
    }

    public String getNome() { return nome; }
    public Curso getCurso() { return curso; }

    public void setProfessor(Professor p) { this.professor = p; }
    public Professor getProfessor() { return professor; }

    public boolean isAtivaAoFinalDoPeriodo() {
        return alunos.size() >= MIN_PARA_OCORRER;
    }
}
