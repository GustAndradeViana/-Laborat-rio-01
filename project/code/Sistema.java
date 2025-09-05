import java.io.*;
import java.util.*;

public class Sistema {
    private static boolean periodoAberto = false;
    private static final List<Disciplina> todasDisciplinas = new ArrayList<>();
    private static final List<Usuario> todosUsuarios = new ArrayList<>();
    private static final List<Curso> todosCursos = new ArrayList<>();
    private static final Map<Aluno, List<Disciplina>> matriculas = new HashMap<>();

    private static Usuario usuarioLogado = null;

    private static final String USUARIOS_FILE = "usuarios.csv";
    private static final String DISCIPLINAS_FILE = "disciplinas.csv";
    private static final String MATRICULAS_FILE = "matriculas.csv";
    private static final String CURSOS_FILE = "cursos.csv";

    public static void abrirPeriodo() {
        periodoAberto = true;
    }

    public static void fecharPeriodo() {
        periodoAberto = false;
    }

    public static void registrarCurso(Curso c) {
        if (!todosCursos.contains(c)) {
            todosCursos.add(c);
        }
    }

    public static List<Curso> getCursos() {
        return new ArrayList<>(todosCursos);
    }

    public static void registrarDisciplina(Disciplina d) {
        if (!todasDisciplinas.contains(d)) {
            todasDisciplinas.add(d);
        }
    }

    public static List<Disciplina> getDisciplinas() {
        return new ArrayList<>(todasDisciplinas);
    }

    public static void registrarUsuario(Usuario u) {
        if (!todosUsuarios.contains(u)) {
            todosUsuarios.add(u);
        }
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static boolean login(String nome, String senha) {
        for (Usuario u : todosUsuarios) {
            if (u.getNome().equals(nome) && u.autenticar(senha)) {
                usuarioLogado = u;
                System.out.println("Login efetuado: " + nome);
                return true;
            }
        }
        System.out.println("Usuário ou senha inválidos.");
        return false;
    }

    public static void logout() {
        if (usuarioLogado != null) {
            System.out.println("Usuário " + usuarioLogado.getNome() + " deslogado.");
            usuarioLogado = null;
        }
    }

    public static List<Usuario> getTodosUsuarios() {
        return new ArrayList<>(todosUsuarios);
    }

    public static boolean matricularAluno(Aluno a, Disciplina d, boolean obrigatoria) {
        if (!periodoAberto) {
            System.out.println("Matrícula recusada: período fechado.");
            return false;
        }
        if (!todasDisciplinas.contains(d)) {
            System.out.println("Disciplina não registrada no sistema.");
            return false;
        }
        boolean ok = a.matricular(d, obrigatoria);
        if (ok) {
            matriculas.computeIfAbsent(a, k -> new ArrayList<>()).add(d);
            System.out.println("Aluno " + a.getNome() + " matriculado em " + d.getNome());
        }
        return ok;
    }

    public static List<Disciplina> getDisciplinasAluno(Aluno a) {
        return matriculas.getOrDefault(a, Collections.emptyList());
    }

    public static void salvarDados() {
        salvarCursos();
        salvarUsuarios();
        salvarDisciplinas();
        salvarMatriculas();
    }

    public static void carregarDados() {
        todosUsuarios.clear();
        todasDisciplinas.clear();
        todosCursos.clear();
        matriculas.clear();

        carregarCursos();
        carregarUsuarios();
        carregarDisciplinas();
        carregarMatriculas();
    }

    private static void salvarCursos() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CURSOS_FILE))) {
            for (Curso c : todosCursos) {
                pw.println(c.getId() + ";" + c.getNome() + ";" + c.getCreditos());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar cursos.");
        }
    }

    private static void carregarCursos() {
        try (BufferedReader br = new BufferedReader(new FileReader(CURSOS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(";");
                int id = Integer.parseInt(p[0]);
                Curso c = new Curso(id, p[1], Integer.parseInt(p[2]));
                todosCursos.add(c);

                if (id > App.proxIdDisciplina) {
                    App.proxIdDisciplina = id;
                }
            }
        } catch (IOException e) {
        }
    }

    private static void salvarUsuarios() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USUARIOS_FILE))) {
            for (Usuario u : todosUsuarios) {
                if (u instanceof Secretaria) {
                    pw.println(u.getId() + ";SECRETARIA;" + u.getNome() + ";" + u.getSenha());
                } else if (u instanceof Professor) {
                    pw.println(u.getId() + ";PROFESSOR;" + u.getNome() + ";" + u.getSenha());
                } else if (u instanceof Aluno) {
                    Aluno a = (Aluno) u;
                    pw.println(u.getId() + ";ALUNO;" + u.getNome() + ";" + u.getSenha() + ";" + a.getCurso().getId());
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar usuários.");
        }
    }

    private static void carregarUsuarios() {
        try (BufferedReader br = new BufferedReader(new FileReader(USUARIOS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(";");
                int id = Integer.parseInt(p[0]);
                String tipo = p[1];
                String nome = p[2];
                String senha = p[3];

                if (tipo.equals("SECRETARIA")) {
                    todosUsuarios.add(new Secretaria(id, nome, senha));
                } else if (tipo.equals("PROFESSOR")) {
                    todosUsuarios.add(new Professor(id, nome, senha));
                } else if (tipo.equals("ALUNO")) {
                    int cursoId = Integer.parseInt(p[4]);
                    Curso c = todosCursos.stream().filter(x -> x.getId() == cursoId).findFirst().orElse(null);
                    if (c != null)
                        todosUsuarios.add(new Aluno(id, nome, senha, c));
                }

                if (id > App.proxIdUsuario) {
                    App.proxIdUsuario = id;
                }
            }
        } catch (IOException e) {
        }
    }

    private static void salvarDisciplinas() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DISCIPLINAS_FILE))) {
            for (Disciplina d : todasDisciplinas) {
                pw.println(d.getId() + ";" + d.getNome() + ";" + d.getCurso().getId());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar disciplinas.");
        }
    }

    private static void carregarDisciplinas() {
        try (BufferedReader br = new BufferedReader(new FileReader(DISCIPLINAS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(";");
                int id = Integer.parseInt(p[0]);
                String nome = p[1];
                int cursoId = Integer.parseInt(p[2]);
                Curso c = todosCursos.stream().filter(x -> x.getId() == cursoId).findFirst().orElse(null);
                if (c != null) {
                    todasDisciplinas.add(new Disciplina(id, nome, c));
                }

                if (id > App.proxIdDisciplina) {
                    App.proxIdDisciplina = id;
                }
            }
        } catch (IOException e) {
        }
    }

    private static void salvarMatriculas() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(MATRICULAS_FILE))) {
            for (Map.Entry<Aluno, List<Disciplina>> e : matriculas.entrySet()) {
                for (Disciplina d : e.getValue()) {
                    pw.println(e.getKey().getId() + ";" + d.getId());
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar matrículas.");
        }
    }

    private static void carregarMatriculas() {
        try (BufferedReader br = new BufferedReader(new FileReader(MATRICULAS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(";");
                int alunoId = Integer.parseInt(p[0]);
                int discId = Integer.parseInt(p[1]);

                Aluno a = (Aluno) todosUsuarios.stream()
                        .filter(u -> u instanceof Aluno && u.getId() == alunoId)
                        .findFirst().orElse(null);

                Disciplina d = todasDisciplinas.stream()
                        .filter(x -> x.getId() == discId)
                        .findFirst().orElse(null);

                if (a != null && d != null) {
                    matriculas.computeIfAbsent(a, k -> new ArrayList<>()).add(d);

                    d.matricularAluno(a);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar matrículas.");
        }
    }

}