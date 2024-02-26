package src.code;

/*
 * Arquivo.java         = Contém metodos de manipulação de arquivos em memória secundária e primária.
 * MyIO.java            = Contém metodos de entrada e saida (I/O) de dados.
 * Model.java           = Modelo de cada entidade do dataset, e metodos para construção / tratamento.
 * Crud.java            = Classe de abstração. Age como mensageiro entre Main e as outras classes, exceto a classe FolderNavigator
 * Main.java            = Arquivo principal que deve ser compilado e executado.
 * FolderNavigator.java = Classe para a GUI do programa. TODO.
 */

public class Main {
    public static void main (String[] args) {
        
        Crud myCrud = new Crud("./mynewDB");
        myCrud.reloadDB();
        myCrud.findLastID();
        
    }
}


/*
 * Victor Hugo Braz - 817958
 *
 *
 *############################################################################################ 
 *#                                                                                          #
 *#    /$$$$$$  /$$                                               /$$                        #
 *#    /$$__  $$| $$                                              | $$                       #
 *#   | $$  \__/| $$$$$$$   /$$$$$$  /$$$$$$$   /$$$$$$   /$$$$$$ | $$  /$$$$$$   /$$$$$$    #
 *#   | $$      | $$__  $$ |____  $$| $$__  $$ /$$__  $$ /$$__  $$| $$ /$$__  $$ /$$__  $$   #
 *#   | $$      | $$  \ $$  /$$$$$$$| $$  \ $$| $$  \ $$| $$$$$$$$| $$| $$  \ $$| $$  \ $$   #
 *#   | $$    $$| $$  | $$ /$$__  $$| $$  | $$| $$  | $$| $$_____/| $$| $$  | $$| $$  | $$   #
 *#   |  $$$$$$/| $$  | $$|  $$$$$$$| $$  | $$|  $$$$$$$|  $$$$$$$| $$|  $$$$$$/|  $$$$$$$   #
 *#    \______/ |__/  |__/ \_______/|__/  |__/ \____  $$ \_______/|__/ \______/  \____  $$   #
 *#                                            /$$  \ $$                         /$$  \ $$   # 
 *#                                           |  $$$$$$/                        |  $$$$$$/   #
 *#                                            \______/                          \______/    #
 *############################################################################################
 * 
 * DD/MM/YYYY
 * 
 * 02/15/2024 12:00 UTC-3 - 0.1.0
 *      -> Seleção da Base de Dados e criação dos arquivos: Model.java, Crud.Java. 
 *      -> Importação de Arq.java e MyIO.java, de AEDS2.
 * 
 * 02/15/2024 18:42 UTC-3 - 0.2.0
 *      -> Implementação do Arquivo Model e criação dos metodos get e set.
 * 
 * 02/16/2024 20:44 UTC-3 - 0.2.1
 *      -> Correção de alguns metodos faltantes. Planejamento da interface gráfica.
 * 
 * 02/19/2024 20:34 UTC-3 - 0.3.0
 *      |\_> Model.Java: Implementação do metodo printToString.
 *      |\_> Crud.java : Implementação de metodos open e close, e esboço de funções.
 *      |\_> MyDLL.java: Implementação de uma DLL para carregar o BD na memoria principal.
 *      |                  Como devemos trabalhar na memoria segundaria, isso é um placeholder.
 *      \_>  Main.java : Sem mudanças significativas
 * 
 * 02/21/2024 19:19 UTC-3 - 0.4.0
 *      |\_> Model.Java: Mais construtores e alguns atributos novos.
 *      |\_> Crud.java : Implementação do metodo ReloadDB, que recarrega a base de dados baseada na OriginalDB.csv
 *      |\_> MyDLL.java: Implementação de novos metodos que permitem a remoção e inserção em outras posições
 *      |                  
 *      |\_> Main.java : Começando a implementação da GUI
 *       \_> FolderNavigator.java: Implementação basica da classe que irá gerar o GUI.
 * 
 * 02/22/2024 13:59 UTC-3 - 0.5.0
 *      |\_> Model.Java  : Construtor não-tratado modificado. Construtor tratado deprecado.
 *      |\_> Crud.java   : Inicio da refatoração do arquivo. TODO: Metodo ReloadDB.              
 *      |\_> Main.java   : Limpeza de metodos não usados. TODO.
 *      |\_> Arq.java    : Arquivo excluido. Não utiliza RAF.
 *      |\_> Arquivo.java: Nova classe que cuida de interações entre arquivos. Utiliza exclusivamente RAF.
 *      \_> /tp1/Database/t  -> Bases de dados em outros países. Serão adicionadas à DB tratada eventualmente.
 * 
 * 02/23/2024 22:05 UTC-3 - 0.6.0
 *      |\_> Model.Java  : Muitas mudanças no construtor
 *      |\_> Crud.java   : Metodo ReloadDB feito e funcional. Atualmente carrega apenas os 25 primeiros videos de cada dataset.
 *      |\_> Main.java   : Inclusão do metodo ReloadDB para fins de teste
 *      |\_> Arquivo.java: Inclusão de alguns metodos da RAF. Considerei transformala em uma extensão da RAF, mas prefiri usar essa classe para abstrair a RAF em vez disso.
 *      \_>  MyDLL.java  : Algumas mudanças no metodo Pop. Ainda preciso fazer alguns ajustes nos outros metodos para prevenir comportamento indefinido.
 *                            Além disso, novo metodo merge que acresenta uma DLL à outra. 
 *      Observações:
 *      A leitura do dataset 'INvideos' se mostrou extremamente lenta em relação aos outros. Além disso, a leitura em geral
 *      está um pouco lenta. A minha hipotese é que pode ser melhor ler tudo de uma só vez em uma só operação de I/O, em vez de ler varias vezes
 *      com o metodo recursiveLineRead(). Irei testar isso na proxima iteração.
 * 02/25/2024 22:05 UTC-3 - 0.7.0
 *      |\_> Model.Java  : Refatoração do metodo GetBytes, levando em conta que:
 *                         -> Há um espaço de 7 bytes 'inutil' no ínicio do arquivo que deve ser levado em conta para a leitura precisa do ID
 *                         -> Cada String adiciona 2 bytes a mais ao arquivo, para marcar seu tamanho.
 *      |\_> Crud.java   : Muitas mudanças que possibilitam a criação do metodo Create, e também buscar registros por ID dentro do arquivo em bytes.
 *      |\_> Main.java   : Alteração com inclusão de metodos teste.
 *      |\_> Arquivo.java: Grandes mudanças. Passamos a usar a FileOutputStream e a DataOutputStream em vez de RAF, para podermos escrever a Bytes diretamente.
 *                          Isso reduz as dores de cabeça, cria arquivos menores e possibilita a leitura byte a byte.
 *      \_>  MyDLL.java  : Algumas mudanças no metodo Pop. Ainda preciso fazer alguns ajustes nos outros metodos para prevenir comportamento indefinido.
 *                            Além disso, novo metodo merge que acresenta uma DLL à outra. 
 *      Observações:
 *      Estava equivocado. A base de dados ruim era, na verdade, a 'JPvideos.csv'. Após remove-lá,o metodo seek do CRUD passou a funcionar, 
 *      E também aumentamos consideravelmente o tempo de leitura. Irei tentar fazer alterações para incorpora-la denovo à DB tratada.
 */