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

        int option = -1;

        MyIO.println("__/\\\\\\________/\\\\\\___________________________________________________/\\\\\\\\\\\\\\\\\\\\\\\\_____/\\\\\\\\\\\\\\\\\\\\\\\\\\___        ");
        MyIO.println(" _\\/\\\\\\_______\\/\\\\\\__________________________________________________\\/\\\\\\////////\\\\\\__\\/\\\\\\/////////\\\\\\_       ");
        MyIO.println("  _\\//\\\\\\______/\\\\\\___/\\\\\\_____/\\\\\\___________________________________\\/\\\\\\______\\//\\\\\\_\\/\\\\\\_______\\/\\\\\\_      ");
        MyIO.println("   __\\//\\\\\\____/\\\\\\___\\///___/\\\\\\\\\\\\\\\\\\\\\\__/\\\\\\\\\\\\\\\\\\________/\\\\\\\\\\____\\/\\\\\\_______\\/\\\\\\_\\/\\\\\\\\\\\\\\\\\\\\\\\\\\\\__     ");
        MyIO.println("    ___\\//\\\\\\__/\\\\\\_____/\\\\\\_\\////\\\\\\////__\\////////\\\\\\_____/\\\\\\///\\\\\\__\\/\\\\\\_______\\/\\\\\\_\\/\\\\\\/////////\\\\\\_    ");
        MyIO.println("     ____\\//\\\\\\/\\\\\\_____\\/\\\\\\____\\/\\\\\\________/\\\\\\\\\\\\\\\\\\\\___/\\\\\\__\\//\\\\\\_\\/\\\\\\_______\\/\\\\\\_\\/\\\\\\_______\\/\\\\\\_   ");
        MyIO.println("      _____\\//\\\\\\\\\\______\\/\\\\\\____\\/\\\\\\_/\\\\___/\\\\\\/////\\\\\\__\\//\\\\\\__/\\\\\\__\\/\\\\\\_______/\\\\\\__\\/\\\\\\_______\\/\\\\\\_  ");
        MyIO.println("       ______\\//\\\\\\_______\\/\\\\\\____\\//\\\\\\\\\\___\\//\\\\\\\\\\\\\\\\/\\\\__\\///\\\\\\\\\\/___\\/\\\\\\\\\\\\\\\\\\\\\\\\/___\\/\\\\\\\\\\\\\\\\\\\\\\\\\\/__ ");
        MyIO.println("        _______\\///________\\///______\\/////_____\\////////\\//_____\\/////_____\\////////////_____\\/////////////____");

        MyIO.println("\nVitaoDB - TP 2 v. 1.5.0 - R.A: 817958\n");
        // Campo de escolhas

        MyIO.println("Favor copie e cole o caminho ate a base de dados:");
        MyIO.println("Exemplo: \"E:\\Software\\Programming\\Github\\AEDS3\\mynewDB\"");
       
       
       
        String filepath = MyIO.readString();
        Crud myCrud = new Crud(filepath);
        Metadata meta = null;

        while (option != 0)
        {
            meta = myCrud.getData();
            MyIO.println("Registros Validos Encontrados: #" + meta.getRegNum());
            MyIO.println("Ultimo Registro No Programa: #" + meta.getLastId());
            MyIO.println("Proximo ID inserido sera:  #" + meta.getNextId());
            MyIO.println("Numero de Lapides:  #" + meta.getLapideNum());
            MyIO.println("Numero de Registros Fora De Posicao (OOP):  #" + meta.getOOPNum());
            
            MyIO.println("0 -> Sair do Programa\n" +
                                      "1 -> Recarregar primeiros 1000 registros de cada Dataset\n" +
                                      "2 -> CREATE\n" +
                                      "3 -> UPDATE\n" +
                                      "4 -> READ\n" +
                                      "5 -> DELETE\n");
            option = MyIO.readInt();

            switch (option) {
                case 0:
                    option = 0;
                    break;
                case 1:
                    MyIO.println("Favor copie e cole o caminho até os datasets Backup");
                    MyIO.println("Exemplo: \"E:\\Software\\Programming\\Github\\AEDS3\\TP1\\Database\\t\\\"");
                    String backpath = MyIO.readString();
                    myCrud.reloadDB(backpath);  
                    break;
                case 2:
                    myCrud.create();
                    break;
                case 3:
                    myCrud.update();
                    break;
                case 4:
                    myCrud.read();
                    break;
                case 5:
                    myCrud.delete();
                break;
            
                default:
                    MyIO.println("Operacao invalida.\n");
                break;
            }
            option = MyIO.readInt();
        }
    }
}


/*
 * Victor Hugo Braz - 817958
 * https://github.com/PoderosoVitao/AEDS3
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
 * 02/28/2024 22:05 UTC-3 - 0.8.0
 *      |\_> DBs  : DB tratada disponivel por meio do metodo reloadDB.
 *      Não será adicionada ao Github por questão de tamanho. Basta re-compilar.
 *      |\_> Crud.java   : Leitura foi consertada. Metodo reloadDB agora informa o úsuario do progresso a cada 1000 registros.
 *      |\_> Arquivo.java: Refatora o metodo readContinuous para parar de ler ao chegar no fim do arquivo.
 *      \_>  Model.java  : O metodo Get_Bytes não estava funcionando com caracteres Emoji, então tive que
 *            refaze-lo de acordo com as minhas necessidades. Agora parece, finalmente, estar tudo certo com o Create.
 * 03/01/2024 14:35 UTC-3 - 1.0.0
 *      |\_> MyIO.java   : Novo metodo ReadLong();
 *      |\_> Crud.java   : Todos os metodos CRUD adicionados.
 *      |\_> Arquivo.java: Novo metodo Seek.
 *      \_>  Model.java  : Refatoração do metodo printToString();
 *            Também models novos para ajudar com o CRUD.
 * 03/15/2024 14:35 UTC-3 - 1.1.0
 *      |\_> Metadata.java   : Nova classe! Essa classe escreve e gerencia os metadados do arquivo.
 *                     Agora guardamos o ultimo ID, o N# de IDs, o número de lapides, etc etc etc.
 *                       Isso será necessário no futuro para tirarmos lapides de mais automaticamente,
 *                         ou re-ordenar IDs fora de ordem.
 *      |\_> Crud.java       : Depreciação de dois metodos desnecessários agora que temos a Metadata.
 *      |\_> Arquivo.java    : Ajustes de compatibilidade para Metadata.java.
 *      |\_> Model.java      : Correção de um bug do Update
 *       \_> MyDLL.java      : Novo metodo para pegar o tamanho de todos os bytes. 
 */