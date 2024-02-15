package src.code;

/*
 * Programa interface.
 * 
 * Model.java = Modelo de cada entidade do banco de dados.
 * Crud.java  = Apenas contém os metodos usados aqui.
 * Main.java  = Arquivo principal que deve ser compilado e executado.
 * 
 * Opções:
 * 0: Carrega o banco de dados.backup e formata para colocar e salvar todos os arquivos em 1.
 */

public class Main {
    public static void main (String[] args) {
        clrScreen(); // Limpa o lixo da tela.
        greetUsr(); // Dá ao usuário as opções disponiveis.
    }
    
    /*
     * Clears the Terminal Screen
     */
    public static void clrScreen ()
    {
        //TODO: Improve this method
        System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    /*
     * Greets the user and gives them options.
     */
    public static void greetUsr ()
    {
        MyIO.println("Hi!");
    }
}

/*
 * Victor Hugo Braz - 817958
 */

/* DD/MM/YYYY
 * 02/15/2024 12:00 - 0.1.0
 *      -> Seleção da Base de Dados e criação dos arquivos: Model.java, Crud.Java. 
 *      -> Importação de Arq.java e MyIO.java, de AEDS2.
 * 02/15/2024 18:42 - 0.2.0
 *      -> Implementação do Arquivo Model e criação dos metodos get e set.
/*
 * TODO:
 * -> Interface Gráfica
 * -> CRUD
 *
 */