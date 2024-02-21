package src.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.SwingUtilities;

/*
 * Programa interface.
 * 
 * Arq.java   = Contém metodos de manipulação de arquivos em memória secundária.
 * MyIO.java  = Contém metodos de entrada e saida (I/O) de dados.
 * Model.java = Modelo de cada entidade do banco de dados.
 * Crud.java  = Apenas contém os metodos usados aqui.
 * Main.java  = Arquivo principal que deve ser compilado e executado.
 */

// GLOBAL VAR THAT ENABLES EXTRA VERBOSE FOR DEBUGGING;

public class Main {
    public static void main (String[] args) {

        // Invoca a GUI principal.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FolderNavigator().setVisible(true);
            }
        });  
    }
}


/*
 * Victor Hugo Braz - 817958
 */

/* DD/MM/YYYY
 * 02/15/2024 12:00 UTC-3 - 0.1.0
 *      -> Seleção da Base de Dados e criação dos arquivos: Model.java, Crud.Java. 
 *      -> Importação de Arq.java e MyIO.java, de AEDS2.
 * 
 * 02/15/2024 18:42 UTC-3 - 0.2.0
 *      -> Implementação do Arquivo Model e criação dos metodos get e set.
 * 02/16/2024 20:44 UTC-3 - 0.2.1
 *      -> Correção de alguns metodos faltantes. Planejamento da interface gráfica.
 * 
 * 02/19/2024 20:34 UTC-3 - 0.3.0
 *      |\_> Model.Java: Implementação do metodo printToString.
 *      |\_> Crud.java : Implementação de metodos open e close, e esboço de funções.
 *      |\_> MyDLL.java: Implementação de uma DLL para carregar o BD na memoria principal.
 *      |                  Como devemos trabalhar na memoria segundaria, isso é um placeholder.
 *      \_>  Main.java : Sem mudanças significativas
 * 02/21/2024 19:19 UTC-3 - 0.4.0
 *      |\_> Model.Java: Mais construtores e alguns atributos novos.
 *      |\_> Crud.java : Implementação do metodo ReloadDB, que recarrega a base de dados baseada na OriginalDB.csv
 *      |\_> MyDLL.java: Implementação de novos metodos que permitem a remoção e inserção em outras posições
 *      |                  
 *      |\_> Main.java : Começando a implementação da GUI
 *       \_> FolderNavigator.java: Implementação basica da classe que irá gerar o GUI.
 * TODO:
 * -> Interface Gráfica
 * -> CRUD
 */