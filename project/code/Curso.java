import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Curso implements Serializable {
    private int id;
    private String nome;
    private int creditos;
    private final List<Disciplina> disciplinas = new ArrayList<>();

    public Curso(int id, String nome, int creditos) {
        this.id = id;
        this.nome = nome;
        this.creditos = creditos;
    }

    public int getId() { return id; }

    public void adicionarDisciplina(Disciplina d) {
        if (!disciplinas.contains(d)) {
            disciplinas.add(d);
        }
    }
    public String getNome() { return nome; }
    public int getCreditos() { return creditos; }
    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }
}
