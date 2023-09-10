package src.Application;

import java.util.Scanner;
import src.DB;
import src.DBQuerys;

public class App {
    //Função para limpar a tela do terminal quando for chamada
    public static void limpartela() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

                return;
            }

            Runtime.getRuntime().exec("clear");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
            DBQuerys dbq = new DBQuerys();
            Scanner sc = new Scanner(System.in);      
            int escolha;
            limpartela();
            
            //Menu
            do {
                System.out.println("Sitema do Hotel JG");
                System.out.println("[1] Cadastrar Cliente\n[2] Opções de Listagem\n[3] Remover Clientes\n[4] Atualizar dados\n[5] Sair");
                System.out.print("Escolha uma opção: ");
                escolha = sc.nextInt();
                
                switch (escolha) {
                    case 1:
                        
                        sc.nextLine();
                        dbq.cadastroHospede();
                        

                        break;
                    case 2:
                        limpartela();
                        System.out.println("Escolha uma das opções de listagem:");
                        System.out.println("[1] Listar Hospedes\n[2] Listar Quartos\n[3] Listar Reservas\n[4] Listar Relação de Reservas");
                        System.out.println("Escolha sua opção: ");
                        int escolhaLista = sc.nextInt();
                        limpartela();

                        switch (escolhaLista) {
                            case 1:
                                dbq.listar("hospedes");

                                break;
                            case 2:
                                dbq.listar("quartos");

                                break;
                            case 3:
                                dbq.listar("reservas");

                                break;
                            case 4:
                                dbq.listar("cadastro");

                                break;
                            default:
                                break;
                        }
                        
                        break;
                    case 3:
                        limpartela();
                        dbq.deletarHospedes();
                        limpartela();

                        break;
                    case 4:
                        dbq.atualizarDados();
                        break;
                    default:
                        break;
                }
            } while (escolha != 5);

            DB.close();
            sc.close();
    }
}
