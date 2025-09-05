import java.util.ArrayList;
import java.util.List;

public class Professor extends Usuario {
    private final List<Disciplina> disciplinas = new ArrayList<>();

    public Professor(int id, String nome, String senha) {
        super(id, nome, senha);
    }

    public void adicionarDisciplina(Disciplina d) {
        if (!disciplinas.contains(d)) {
            disciplinas.add(d);
            d.setProfessor(this);
        }
    }

    public List<Aluno> alunosDisciplina(String nomeDisciplina) {
        return disciplinas.stream()
                .filter(d -> d.getNome().equalsIgnoreCase(nomeDisciplina))
                .findFirst()
                .map(Disciplina::getAlunosMatriculados)
                .orElse(new ArrayList<>());
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void listarDisciplinas() {
        if (disciplinas.isEmpty()) {
            System.out.println("Nenhuma disciplina atribuída.");
        } else {
            disciplinas.forEach(d -> System.out.println(" - " + d.getNome()));
        }
    }
}
