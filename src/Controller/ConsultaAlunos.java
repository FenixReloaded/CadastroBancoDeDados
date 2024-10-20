package Controller;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.*;

    public class ConsultaAlunos extends JFrame {
        private JTextArea resultadoArea;

        public ConsultaAlunos() {
            setTitle("Consultas de Alunos");
            setLayout(new BorderLayout());
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(600, 400);

            // Área para mostrar resultados
            resultadoArea = new JTextArea();
            resultadoArea.setEditable(false);
            add(new JScrollPane(resultadoArea), BorderLayout.CENTER);

            // Botões de Consulta
            JPanel botoesPanel = new JPanel(new GridLayout(3, 2, 10, 10));
            addBotao("Consultar Todos Alunos", "SELECT * FROM alunos ORDER BY matriculas_id", botoesPanel);
            addBotao("Alunos em Disciplina 1",
                    "SELECT a.nome, d.nome FROM disciplinas_alunos da "
                            + "JOIN alunos a ON a.ra = da.alunos_ra "
                            + "JOIN disciplinas d ON d.disciplinas_id = da.disciplinas_id "
                            + "WHERE da.disciplinas_id = 1", botoesPanel);
            addBotao("Cursos e Total de Alunos",
                    "SELECT curso, COUNT(*) FROM alunos GROUP BY curso", botoesPanel);

            add(botoesPanel, BorderLayout.SOUTH);

            setVisible(true);
        }

        private void addBotao(String texto, String query, JPanel panel) {
            JButton botao = new JButton(texto);
            botao.addActionListener(e -> executarConsulta(query));
            panel.add(botao);
        }

        private void executarConsulta(String query) {
            try (Connection conexao = DataBase.conectar();
                 Statement stmt = conexao.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                StringBuilder sb = new StringBuilder();
                ResultSetMetaData metaData = rs.getMetaData();
                int colunas = metaData.getColumnCount();

                while (rs.next()) {
                    for (int i = 1; i <= colunas; i++) {
                        sb.append(metaData.getColumnName(i)).append(": ").append(rs.getString(i)).append("\t");
                    }
                    sb.append("\n");
                }
                resultadoArea.setText(sb.toString());
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao executar consulta.");
            }
        }


    }

