import java.util.List;
import java.util.Scanner;

public class App {
    public static int proxIdCurso = 1;
    public static int proxIdDisciplina = 1;
    public static int proxIdUsuario = 1;

    public static void main(String[] args) {

        try {
            Sistema.carregarDados();
            Scanner sc = new Scanner(System.in);

            // Sistema.registrarUsuario(new Secretaria(1, "Secretaria", "123"));

            while (true) {
                if (Sistema.getUsuarioLogado() == null) {
                    System.out.println("\n1 - Login\n2 - Sair");
                    int op = sc.nextInt();
                    sc.nextLine();
                    if (op == 1) {
                        System.out.print("Nome: ");
                        String nome = sc.nextLine();
                        System.out.print("Senha: ");
                        String senha = sc.nextLine();
                        Sistema.login(nome, senha);
                    } else {
                        Sistema.salvarDados();
                        System.out.println("Encerrando...");
                        break;
                    }
                } else {
                    Usuario u = Sistema.getUsuarioLogado();
                    System.out.println("\nLogado como: " + u.getNome());

                    if (u instanceof Secretaria) {
                        System.out.println(
                                "1 - Criar Aluno\n2 - Criar Professor\n3 - Criar Disciplina\n4 - Criar Curso\n5 - Abrir periodo de matriculas\n6 - Fechar periodo de matriculas\n7 - Atribuir professor\n8 - Logout");
                        int op = sc.nextInt();
                        sc.nextLine();
                        if (op == 1) {
                            System.out.print("Nome aluno: ");
                            String nome = sc.nextLine();
                            System.out.print("Senha: ");
                            String senha = sc.nextLine();

                            System.out.println("Cursos disponíveis: ");
                            for (int i = 0; i < Sistema.getCursos().size(); i++) {
                                System.out.println((i + 1) + " - " + Sistema.getCursos().get(i).getNome());
                            }
                            System.out.print("Escolha curso: ");
                            int idx = sc.nextInt();
                            sc.nextLine();
                            Curso curso = Sistema.getCursos().get(idx - 1);
                            
                            proxIdUsuario++;
                            int id = proxIdUsuario;
                            Sistema.registrarUsuario(new Aluno(id, nome, senha, curso));
                        } else if (op == 2) {
                            System.out.print("Nome professor: ");
                            String nome = sc.nextLine();
                            System.out.print("Senha: ");
                            String senha = sc.nextLine();
                            Sistema.registrarUsuario(new Professor(proxIdUsuario++, nome, senha));
                        } else if (op == 3) {
                            System.out.print("Nome disciplina: ");
                            String nome = sc.nextLine();

                            System.out.println("Cursos disponíveis: ");
                            for (int i = 0; i < Sistema.getCursos().size(); i++) {
                                System.out.println((i + 1) + " - " + Sistema.getCursos().get(i).getNome());
                            }
                            System.out.print("Escolha curso: ");
                            int idx = sc.nextInt();
                            sc.nextLine();
                            Curso curso = Sistema.getCursos().get(idx - 1);
                            proxIdDisciplina++;
                            int id = proxIdDisciplina;
                            Sistema.registrarDisciplina(new Disciplina(id, nome, curso));

                        } else if (op == 4) {
                            System.out.print("Nome curso: ");
                            String nome = sc.nextLine();
                            System.out.print("Créditos: ");
                            int creditos = sc.nextInt();
                            sc.nextLine();
                            proxIdCurso++;
                            int id = proxIdCurso;
                            Curso c = new Curso(id, nome, creditos);
                            Sistema.registrarCurso(c);
                            System.out.println("Curso criado: " + nome);
                        } else if (op == 5) {
                            Sistema.abrirPeriodo();
                            System.out.println("Periodo de matriculas aberto");
                        } else if (op == 6) {
                            Sistema.fecharPeriodo();
                            System.out.println("Periodo de matriculas fechado");
                        } else if (op == 7) {
                            System.out.println("Digite nome da disciplina: ");
                            String nome = sc.nextLine();
                            Disciplina d = Sistema.getDisciplinas().stream().filter(x -> x.getNome().equals(nome))
                                    .findFirst().orElse(null);
                            System.out.println("Digite nome do professor: ");
                            String nomeProrfessor = sc.nextLine();
                            Professor p = Sistema.getTodosUsuarios().stream()
                                    .filter(x -> x instanceof Professor)
                                    .map(x -> (Professor) x)
                                    .filter(x -> x.getNome().equals(nomeProrfessor))
                                    .findFirst()
                                    .orElse(null);

                            p.adicionarDisciplina(d);
                        } else if (op == 8) {
                            Sistema.logout();
                        }
                    } else if (u instanceof Aluno) {
                        System.out
                                .println("1 - Listar disciplinas\n2 - Matricular\n3 - Minhas disciplinas\n4 - Logout");
                        int op = sc.nextInt();
                        sc.nextLine();
                        if (op == 1) {
                            Sistema.getDisciplinasAluno((Aluno) u).forEach(d -> System.out.println(d.getNome()));
                        } else if (op == 2) {
                            System.out.println("Digite nome da disciplina: ");
                            String nome = sc.nextLine();
                            Disciplina d = Sistema.getDisciplinas().stream().filter(x -> x.getNome().equals(nome))
                                    .findFirst().orElse(null);
                            if (d != null)
                                Sistema.matricularAluno((Aluno) u, d, true);
                        } else if (op == 3) {
                            Sistema.getDisciplinasAluno((Aluno) u).forEach(d -> System.out.println(d.getNome()));
                        } else
                            Sistema.logout();
                    } else if (u instanceof Professor) {
                        Professor p = (Professor) u;
                        System.out.println("1 - Minhas Disciplinas\n2 - Listar alunos em disciplina\n3 - Logout");
                        int op = sc.nextInt();
                        sc.nextLine();

                        if (op == 1) {
                            p.listarDisciplinas();
                        } else if (op == 2) {
                            System.out.print("Digite o nome da disciplina: ");
                            String nomeDisc = sc.nextLine();
                            List<Aluno> alunos = p.alunosDisciplina(nomeDisc);
                            if (alunos.isEmpty()) {
                                System.out.println("Nenhum aluno matriculado em " + nomeDisc);
                            } else {
                                System.out.println("Alunos em " + nomeDisc + ":");
                                alunos.forEach(a -> System.out.println(" - " + a.getNome()));
                            }
                        } else {
                            Sistema.logout();
                        }
                    }

                }
            }

            sc.close();
        } finally {
            Sistema.salvarDados();
        }

    }
}
