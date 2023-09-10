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
    Scanner sc = new Scanner(System.in);
    

    public DBQuerys() {
    }

    //realiza o cadastro de novos hospedes
    public void cadastroHospede(Connection conn) {
        try {
            
                Date checkin = new Date();
                stmt = conn.createStatement();
                
                System.out.println("Insira os dados do hospede");
                System.out.print("Nome: ");
                String name = sc.nextLine();
                System.out.print("Email: ");
                String emaill = sc.nextLine();
                System.out.print("Telefone: ");
                Long telefon = sc.nextLong();
                String sql = "INSERT INTO hospedes (Nome, Email, Telefone) VALUES ( '" + name + "','" + emaill + "' , '"
                        + telefon + "')";

                System.out.println("Selecione o quarto pelo numero:");
                
                listar("quartos");


                System.out.print("Digite o quarto: ");
                int idQuarto = sc.nextInt();

                
                System.out.println("Digite o final da estadia dd/mm/yyyy: ");
                String checkout = sc.next();
                
                stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

                
                
                int idHospede = -1;
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idHospede = rs.getInt(1);
                    }
                }
                
                String checkinOk = sdf.format(checkin);
                
                
                // Inserir um registro na tabela de reserva usando o idHospede como chave estrangeira
                
                insertHotel = conn.prepareStatement("INSERT INTO reserva (IdHospede, IdQuarto, CheckIn, CheckOut) VALUES (?, ?, ?, ?)");
                insertHotel.setInt(1, idHospede);
                insertHotel.setInt(2, idQuarto);
                insertHotel.setString(3, checkinOk);
                insertHotel.setString(4, checkout);
                
                insertHotel.executeUpdate();
                    


               
            }catch (SQLException e) {
                e.printStackTrace();          
            } 
    }
    
    public void listar(String tabela) {

        try {

            switch (tabela) {

                case "quartos":

                    st = DB.getConnection().prepareStatement("select * from quartos");
                    rs = st.executeQuery("select * from quartos");
                    while (rs.next()) {
                        System.out.println("Numero quarto: " + rs.getInt("idQuarto") + ", Tipo Quarto: "
                                + rs.getString("TipoQuarto") + ", Valor: " + rs.getFloat("DiariaValor"));

                    }
                    break;

                case "hospedes":
                    st = DB.getConnection().prepareStatement("select * from hospedes");
                    rs = st.executeQuery("select * from hospedes");
                    while (rs.next()) {
                        System.out.println("id: " + rs.getInt("idHospede") + ", Nome: " + rs.getString("Nome")
                                + ", email: " + rs.getString("Email") + ", Telefone: " + rs.getLong("Telefone"));
                    }
                    break;

                case "reservas":
                    st = DB.getConnection().prepareStatement("select * from reserva");
                    rs = st.executeQuery("select * from reserva");
                    while (rs.next()) {
                        System.out.println("id reserva: " + rs.getInt("idReserva") + "id hospede: "
                                + rs.getInt("idHospede") + ", id Quarto: " + rs.getInt("idQuarto") + ", CheckIn: "
                                + rs.getString("CheckIn") + ", CheckOut: " + rs.getString("CheckOut"));
                    }
                    break;
                case "listaCadastro":
                    st = DB.getConnection().prepareStatement("SELECT\n" + //
                            "    reserva.idReserva,\n" + //
                            "    hospedes.nome AS nomeHospede,\n" + //
                            "    reserva.CheckIn,\n" + //
                            "    reserva.CheckOut\n" + //
                            "FROM\n" + //
                            "    reserva\n" + //
                            "INNER JOIN\n" + //
                            "    hospedes ON reserva.idHospede = hospedes.idHospede;");

                    rs = st.executeQuery("SELECT\n" + //
                            "    reserva.idReserva,\n" + //
                            "    hospedes.nome AS nomeHospede,\n" + //
                            "    reserva.CheckIn,\n" + //
                            "    reserva.CheckOut\n" + //
                            "FROM\n" + //
                            "    reserva\n" + //
                            "INNER JOIN\n" + //
                            "    hospedes ON reserva.idHospede = hospedes.idHospede;");
                    while (rs.next()) {
                        System.out.println("Numero reserva: " + rs.getInt("idReserva") + ", Nome: " + rs.getString("nomeHospede")
                                + ", CheckIn: " + rs.getString("CheckIn") + ", CheckOut: " + rs.getString("CheckOut"));
                    }
                    break;
                default:
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    

    public void deletarHospedes() {
        
        try{

            System.out.println("Deseja excluir por nome ou id: \n[1] Nome \n[2] Id");
            System.out.print("Escoha uma opção: ");
            int escolha = sc.nextInt();
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
                    String sqlid = "DELETE FROM hospedes WHERE idHospede = '" + id +"'";
                    st = DB.getConnection().prepareStatement(sqlid);
                    st.execute();
                    break;
            
                default:
                    break;
            }
            
        } catch(SQLException e){
            e.printStackTrace();
        }


        
    }

    
    
}
