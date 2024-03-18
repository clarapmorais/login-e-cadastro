import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConexaoMySQL {
    private static final String URL = "jdbc:mysql://localhost/mapa";
    private static final String USUARIO = "root";
    private static final String SENHA = "";

    public static Connection conectar() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Carrega o JDBC
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL não encontrado", e);
        } catch (SQLException e) {
            throw new SQLException("Erro ao conectar ao banco de dados", e);
        }
    }
    
    public static void desconectar(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // Lida com exceções ao fechar a conexão
                e.printStackTrace();
            }
        }
    }
    
    // Método para cadastrar usuário no banco de dados
    public static boolean cadastrarUsuario(String nome, String login, String senha, String email) {
        try (Connection conn = conectar()) {
            if (conn == null) {
                return false; // Retorna false se a conexão falhar
            }

            // Verifica se os campos obrigatórios estão preenchidos
            if (nome == null || nome.isEmpty() || login == null || login.isEmpty() ||
                senha == null || senha.isEmpty() || email == null || email.isEmpty()) {
                System.out.println("Campos obrigatórios não preenchidos.");
                return false;
            }

            // Query para inserir um novo usuário na tabela 'usuario'
            String sql = "INSERT INTO usuario (nome, login, senha, email) VALUES (?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nome);
                stmt.setString(2, login);
                stmt.setString(3, senha);
                stmt.setString(4, email);

                int linhasAfetadas = stmt.executeUpdate(); // Executa a inserção

                // Verifica se a inserção foi bem-sucedida
                return linhasAfetadas > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retorna false em caso de erro no banco de dados
        }
    }
}

