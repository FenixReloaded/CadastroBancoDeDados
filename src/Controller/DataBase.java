package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {

    public static Connection conectar() {
        Connection connection = null; // Inicializar a variável de conexão como null
        try {
            String url = "jdbc:mysql://localhost:3306/cadastroDeAlunos"; // Certifique-se de que o nome do banco de dados está correto
            String usuario = "root";
            String senha = "root";

            Class.forName("com.mysql.cj.jdbc.Driver");

            // Fazendo a conexão
            connection = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Database conectada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro estabelecendo conexão com a database: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Driver MySQL não foi encontrado: " + e.getMessage());
        }
        return connection; // Retornar a conexão
    }
}



