import java.util.ArrayList;
import java.util.List;

public class Secretaria extends Usuario {
    private final List<Aluno> alunos = new ArrayList<>();
    private final List<Professor> professores = new ArrayList<>();
    private final List<Curso> cursos = new ArrayList<>();
    private final List<Disciplina> disciplinas = new ArrayList<>();
    private int proxId = 1;

    public Secretaria(int id, String nome, String senha) {
        super(id, nome, senha);
    }

    public Professor criarProfessor(String nome) {
        Professor p = new Professor(proxId++, nome, "123");
        professores.add(p);
        Sistema.registrarUsuario(p);
        return p;
    }

    public Aluno criarAluno(String nome, Curso curso) {
        Aluno a = new Aluno(proxId++, nome, "123", curso);
        alunos.add(a);
        Sistema.registrarUsuario(a);
        return a;
    }

    public Curso criarCurso(String nome, int creditos) {
        Curso c = new Curso(proxId++, nome, creditos);
        cursos.add(c);
        return c;
    }

    public Disciplina criarDisciplina(String nome, Curso curso) {
        Disciplina d = new Disciplina(proxId++, nome, curso);
        disciplinas.add(d);
        curso.adicionarDisciplina(d);
        Sistema.registrarDisciplina(d);
        return d;
    }

    public List<Aluno> getAlunos() { return alunos; }
    public List<Professor> getProfessores() { return professores; }
    public List<Curso> getCursos() { return cursos; }
    public List<Disciplina> getDisciplinas() { return disciplinas; }
}
