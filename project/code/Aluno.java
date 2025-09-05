import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Aluno extends Usuario {
    private Curso curso;
    private final List<Disciplina> obrigatorias = new ArrayList<>();
    private final List<Disciplina> optativas = new ArrayList<>();
    public static final int MAX_OBRIGATORIAS = 4;
    public static final int MAX_OPTATIVAS = 2;

    public Aluno(int id, String nome, String senha, Curso curso) {
        super(id, nome, senha);
        this.curso = curso;
    }

    public Curso getCurso() {
        return curso;
    }

    public List<Disciplina> getMatriculas() {
        List<Disciplina> all = new ArrayList<>();
        all.addAll(obrigatorias);
        all.addAll(optativas);
        return Collections.unmodifiableList(all);
    }

    public boolean matricular(Disciplina d, boolean obrigatoria) {
        if (obrigatoria) {
            if (obrigatorias.size() >= MAX_OBRIGATORIAS)
                return false;
            if (obrigatorias.contains(d))
                return false;
            if (d.matricularAluno(this)) {
                obrigatorias.add(d);
                return true;
            }
            return false;
        } else {
            if (optativas.size() >= MAX_OPTATIVAS)
                return false;
            if (optativas.contains(d))
                return false;
            if (d.matricularAluno(this)) {
                optativas.add(d);
                return true;
            }
            return false;
        }
    }

    public boolean cancelar(Disciplina d) {
        boolean removed = false;
        if (obrigatorias.remove(d))
            removed = true;
        if (optativas.remove(d))
            removed = true;
        if (removed) {
            d.removerAluno(this);
        }
        return removed;
    }

}
