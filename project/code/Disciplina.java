import java.util.List;

public class Disciplina {
    private String codigo;
    private String nome;
    private int creditos;
    private int vagasMax = 60;
    private int vagasMin = 3;
    private boolean ativa;
    private List<Matricula> matriculas;

    public void abrir() {}
    public void encerrar() {}
    public void verificarStatus() {}
}
