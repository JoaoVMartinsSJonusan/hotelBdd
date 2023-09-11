package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class DBQuerys {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    PreparedStatement st;
    PreparedStatement insertHotel;
    ResultSet rs;
    Statement stmt;
    private Connection conn;
    Scanner sc = new Scanner(System.in);

    public DBQuerys() {
        conn = DB.getConnection();
    }

    // realiza o cadastro de novos hospedes
    public void cadastroHospede() {
        try {
            Date checkin = new Date();
            stmt = conn.createStatement();

            System.out.println("Insira os dados do hospede");
            System.out.print("Nome: ");
            String name = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine();
            System.out.print("Telefone: ");
            Long telefone = sc.nextLong();
            String sql = "INSERT INTO hospedes (Nome, Email, Telefone) VALUES ( '" + name + "','" + email + "' , '"
                    + telefone + "')";
            System.out.println("Selecione o quarto pelo numero:");

            listar("quartos");

            System.out.print("Digite o quarto: ");
            int idQuarto = sc.nextInt();
            System.out.println("Digite o final da estadia dd/mm/yyyy: ");
            String checkout = sc.next();
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            int idHospede = -1;

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                idHospede = rs.getInt(1);
            }

            String checkinOk = sdf.format(checkin);

            // Inserir um registro na tabela de reserva usando o idHospede como chave
            // estrangeira
            insertHotel = conn.prepareStatement(
                "INSERT INTO reserva (IdHospede, IdQuarto, CheckIn, CheckOut) VALUES (?, ?, ?, ?)"
            );
            insertHotel.setInt(1, idHospede);
            insertHotel.setInt(2, idQuarto);
            insertHotel.setString(3, checkinOk);
            insertHotel.setString(4, checkout);
            insertHotel.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listar(String tabela) {
        switch (tabela) {
            case "quartos":
                busca("select * from quartos");

                break;
            case "hospedes":
                busca("select * from hospedes");

                break;
            case "reservas":
                busca("select * from reserva");

                break;
            case "cadastro":
                busca("SELECT\n" + //
                        "    reserva.idReserva,\n" + //
                        "    hospedes.nome AS nomeHospede,\n" + //
                        "    reserva.CheckIn,\n" + //
                        "    reserva.CheckOut\n" + //
                        "FROM\n" + //
                        "    reserva\n" + //
                        "INNER JOIN\n" + //
                        "    hospedes ON reserva.idHospede = hospedes.idHospede;");
                break;
            default:
                break;
        }
    }

    public void deletarHospedes() {
        try {
            System.out.println("Deseja excluir por nome ou id: \n[1] Nome \n[2] Id");
            System.out.print("Escoha uma opção: ");
            int escolha = sc.nextInt();
            listar("hospedes");

            switch (escolha) {
                case 1:
                    System.out.println("Digite o nome: ");
                    sc.nextLine();
                    String nome = sc.nextLine();
                    String sql = "DELETE FROM hospedes WHERE Nome = '" + nome + "'";
                    st = DB.getConnection().prepareStatement(sql);
                    st.execute();

                    break;
                case 2:
                    System.out.print("Digite o id: ");
                    int id = sc.nextInt();
                    String sqlid = "DELETE FROM hospedes WHERE idHospede = '" + id + "'";
                    st = DB.getConnection().prepareStatement(sqlid);
                    st.execute();

                    break;
                default:
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizarDados() { 
        try { 
            System.out.println("Digite qual dado quer atualizar: [1] Nome\n[2] Email\n[3] Telefone "); 
            System.out.print("Escolha sua opção: "); 
            int escolha = sc.nextInt(); 

            switch (escolha) { 
                case 1: 
                    sc.nextLine(); 
                    System.out.print("Digite o nome do Hospede: "); 
                    String nome_hospede = sc.nextLine(); 
                    System.out.println("Digite o novo nome a ser atualizado: "); 
                    String nomeAtt = sc.nextLine();                     
                    String sql = "UPDATE hospedes SET Nome = '" + nomeAtt + "' WHERE (Nome = '" + nome_hospede + "')"; 
                    st = DB.getConnection().prepareStatement(sql); 
                    st.execute();
                    
                    break; 
                case 2: 
                    sc.nextLine(); 
                    System.out.print("Digite o email do Hospede: "); 
                    String emailHospede = sc.nextLine(); 
                    System.out.println("Digite o novo Email a ser atualizado: "); 
                    String emailAtt = sc.nextLine();                     
                    String sqlemail = "UPDATE hospedes SET Email = '" + emailAtt + "' WHERE (Email = '" + emailHospede + "')"; 
                    st = DB.getConnection().prepareStatement(sqlemail); 
                    st.execute(); 

                    break; 
                case 3: 
                    sc.nextLine(); 
                    System.out.print("Digite o Telefone do Hospede: "); 
                    int telefoneHospede = sc.nextInt(); 
                    System.out.println("Digite o novo Telefone a ser atualizado: "); 
                    int telefoneAtt = sc.nextInt();                     
                    String sqltelefone = "UPDATE hospedes SET Telefone = '" + telefoneAtt + "' WHERE (Telefone = " + telefoneHospede + ")"; 
                    st = DB.getConnection().prepareStatement(sqltelefone); 

                    break; 
                default: 
                    break; 
            } 
        } catch (SQLException e) { 
            e.printStackTrace(); 
        } 
    } 

    private void busca(String sql) {
        try {
            st = DB.getConnection().prepareStatement(sql);
            rs = st.executeQuery();

            while (rs.next()) {
                try{
                    if (rs.findColumn("idHospede") != 0) {
                        System.out.println("id hospede: " + rs.getInt("idHospede"));
                    }
                } catch (SQLException e) {}
                
                try {
                    if (rs.findColumn("idReserva") != 0) {
                        System.out.println("Numero reserva: " + rs.getInt("idReserva"));
                    }

                } catch (SQLException e) {}

                try {
                    if (rs.findColumn("idQuarto") != 0) {
                        System.out.println("Numero Quarto: " + rs.getInt("idQuarto"));
                    }
                } catch (SQLException e) {}

                try {
                    if (rs.findColumn("Nome") != 0) {
                        System.out.println("Nome: " + rs.getString("Nome"));
                    }
                } catch (SQLException e) {}

                try {
                    if (rs.findColumn("Email") != 0) {
                        System.out.println("Email: " + rs.getString("Email"));
                    }
                } catch (SQLException e) {}

                try {
                    if (rs.findColumn("Telefone") != 0) {
                        System.out.println("Telefone: " + rs.getLong("Telefone"));
                    }

                } catch (SQLException e) {}

                try {
                    if (rs.findColumn("nomeHospede") != 0) {
                        System.out.println("Nome: " + rs.getString("nomeHospede"));
                    }
                } catch (SQLException e) {}

                try {
                    if (rs.findColumn("TipoQuarto") != 0) {
                        System.out.println("Tipo Quarto: " + rs.getString("TipoQuarto"));
                    }
                } catch (SQLException e) {}

                try {
                    if (rs.findColumn("DiariaValor") != 0) {
                        System.out.println("Valor: " + rs.getString("DiariaValor"));
                    }
                } catch (SQLException e) {}

                try {
                    if (rs.findColumn("CheckIn") != 0) {
                        System.out.println("CheckIn: " + rs.getString("CheckIn"));
                    }
                } catch (SQLException e) {}

                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
