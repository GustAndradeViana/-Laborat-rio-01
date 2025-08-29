public abstract class Usuario {
    private int id;
    private String nome;
    private String login;
    private String senha;

    public abstract boolean autenticar(String login, String senha);
}