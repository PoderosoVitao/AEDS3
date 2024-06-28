package src.code;

import java.io.File;

/*
 * Arquivo.java         = Contém metodos de manipulação de arquivos em memória secundária e primária.
 * MyIO.java            = Contém metodos de entrada e saida (I/O) de dados.
 * Model.java           = Modelo de cada entidade do dataset, e metodos para construção / tratamento.
 * Crud.java            = Classe de abstração. Age como mensageiro entre Main e as outras classes, exceto a classe FolderNavigator
 * Main.java            = Arquivo principal que deve ser compilado e executado.
 * MyDLL.java           = Lista utilizada para carregar índices da base de dados não tratada.
 * FolderNavigator.java = Classe para a GUI do programa. TODO.
 * Node.java            = Contém os nodes usados pelo MyDLL.
 * BNode.java           = Contém os nodes usadas pelo arquivo de índice MyBTree.
 * MyBTree.java         = Implementação de árvore B para guardar índices.
 * Index.java           = Par Id - ByteOffset usado como ponteiro para a classe Arquivo.
 * Metadata.java        = Classe que guarda metadados sobre a base de dados.
 * 
 */

public class Main {
    public static void main (String[] args) {

        // Metodo para facilitar testes.
        //lazyGen();
        
        // Inicia de maneira certa:
        startUp();
    }

    // Só para testes no meu computador! Regenera os arquivos baseado em informações já conhecidas.
    private static final void lazyGen()
    {
        String filepath = "E:\\Software\\Programming\\Github\\AEDS3\\mynewDB";
        String indexFilePath = "E:\\Software\\Programming\\Github\\AEDS3\\btreeindex";

        Crud myCrud = new Crud(filepath, indexFilePath);
        RSA rsa = new RSA(filepath);
        rsa.Criptografar();
        rsa.Descriptografar();
  
        int a = 24;
    
    }

    private static final void startUp()
    {
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

        MyIO.println("\nVitaoDB - TP 3 v. 3.0.0 - R.A: 817958\n");

        MyIO.println("Favor copie e cole o caminho ate o arquivo de indice:");
        MyIO.println("Exemplo: \"E:\\Software\\Programming\\Github\\AEDS3\\btreeindex\"");

        String indexFilepath = MyIO.readString();

        MyIO.println("Favor copie e cole o caminho ate a base de dados:");
        MyIO.println("Exemplo: \"E:\\Software\\Programming\\Github\\AEDS3\\mynewDB\"");
       
        String filepath = MyIO.readString();
        Crud myCrud = new Crud(filepath, indexFilepath);
        Huffman huff = new Huffman();
        LZW lampei = new LZW();


        while (option != 0)
        {
            MyIO.println("Registros Validos Encontrados: #" + myCrud.meta.getRegNum());
            MyIO.println("Ultimo Registro No Programa: #" + myCrud.meta.getLastId());
            MyIO.println("Proximo ID inserido sera:  #" + myCrud.meta.getNextId());
            MyIO.println("Numero de Lapides:  #" + myCrud.meta.getLapideNum());
            MyIO.println("Numero de Registros Fora De Posicao (OOP):  #" + myCrud.meta.getOOPNum());
            
            MyIO.println("0 -> Sair do Programa\n" +
                                      "1 -> Recarregar primeiros 1000 registros de cada Dataset\n" +
                                      "2 -> CREATE\n" +
                                      "3 -> UPDATE\n" +
                                      "4 -> READ\n" +
                                      "5 -> DELETE\n" +
                                      "6 -> COMPACTACAO\n" +
                                      "7 -> DESCOMPACTACAO\n" +
                                      "8 -> PADRAO\n" +
                                      "9 -> CRIPTOGRAFAR\n" +
                                      "10 -> DESCRIPTOGRAFAR\n");
            option = MyIO.readInt();
            RSA rsa = new RSA(filepath);
            switch (option) {
                case 0:
                    option = 0;
                    myCrud.saveBTree();
                    break;
                case 1:
                    MyIO.println("Favor copie e cole o caminho até os datasets Backup");
                    MyIO.println("Exemplo: \"E:\\Software\\Programming\\Github\\AEDS3\\Database\\t\\\"");
                    String backpath = MyIO.readString();
                    myCrud.reloadDB(backpath, 1000);  
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
                case 6:
                    try {
                        lampei.startTimer(filepath);
                        lampei.compressDB(filepath);
                        lampei.stopTimer(filepath + "LZWCompressao1");
                        MyIO.println("\n");
                        MyIO.println("Compressao LZW -- Dados: ");
                        lampei.printTimer();

                        huff.startTimer(filepath);
                        huff.compactDB(filepath);
                        huff.stopTimer(filepath);

                        MyIO.println("\n");
                        MyIO.println("Compressao Huffman -- Dados: ");
                        huff.printTimer();
                    } catch(Exception e){
                        MyIO.println("Erro compactacao opcao 6!");
                        e.printStackTrace();
                    }
                break;
                case 7:
                try {
                    lampei.startTimer(filepath);
                    lampei.decompressDB(filepath);
                    lampei.stopTimer(filepath + "LZWCompressao1");
                    MyIO.println("LZW - Tempo para descompressao:");
                    lampei.printTime();

                    huff.startTimer(filepath);
                    huff.decompressDB(filepath);
                    huff.stopTimer(filepath);
                    MyIO.println("Huffman - Tempo para descompressao:");
                    huff.printTime();

                } catch(Exception e){
                    MyIO.println("Erro descompactacao opcao 7!");
                    e.printStackTrace();
                }
                break;
                case 8:
                    MyIO.println("Escreva o padrao a ser buscado: ");
                    String padrao = MyIO.readString();
                    BoyerMoore BoyAndMore = new BoyerMoore(filepath, myCrud);
                    BoyAndMore.findPadrao(padrao);

                break;
                case 9:
                    rsa.Criptografar();
                break;
                case 10:
                    rsa.Descriptografar();
                break;
                default:
                    MyIO.println("Operacao invalida.\n");
                break;
            }
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
 *      |\_> Main.java   : Limpeza de metodos não usados. TODO: Limpeza de metodos.
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
 * 03/28/2024 14:00 UTC-3 - 1.2.0
 *      |\_> BNode.java  : Nova classe -- Agora separada do MyDLL.java! Essa classe serve como Node para a arvore B.
 *      |\_> MyBTree.java: Nova classe: Essa classe implementa uma arvore B de tamanho variável.
 *              TODO: Metodos REMOVE, UPDATE e READ. Insert já feito.
 *      |\_> Index.java  : Nova classe: Essa classe guarda o par ID e Byte_Offset de cada registro.
 *      |\_> Crud.java   : Mudança temporaria para facilitar testes. Carrega apenas 10 registros de cada dataset no ReloadDB().
 *       \_> MyDLL.java  : Remove a subclasse Node e coloca ela em um arquivo próprio.
 *
 *  04/12/2024 10:44 UTC-3 - 1.3.0
 *      Muitas mudanças com o foco em implementar e melhorar a inserção da arvore B.
 * 
 *  04/13/2024 22:44 UTC-3 - 1.4.0
 *      Adição do protótipo do metodo Remove.
 *
 *  04/14/2024 16:30 UTC-3 - 2.0.0
 *      Main -> Previsão de testes para indice B adicionada e comentada no inicio
 *      MyBTree.java -> Metodos de remoção e inserção testados e aperfeiçoados.
 *      Crud -> Crud sempre tenta buscar pelo índice, e depois linearmente, se não existir.
 *   05/20/2024 16:30 UTC-3 - 3.0.0
 *      Metodos de compressão Huffman e LZW.
 * 
 * 
 */