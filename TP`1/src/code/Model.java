package src.code;

import java.io.RandomAccessFile;
import java.io.UTFDataFormatException;

/*
 * Arquivo que descreve o modelo das entities do dataset usado.
 */

public class Model {

    // A ordem do modelo não necessariamente representa a ordem em que o conteúdo é salvo.

    private long db_id;                       // ID dentro da base de dados.
    private int byteSize;                   // Tamanho do modelo em bytes
    private boolean lapide;                   // Registro lido é valido ou não?
    private String video_id;                  // ID do video.
    private String trending_date;             // Data em que o video ficou Trending
    private String title;                     // Titulo do video
    private String channel_title;             // Titulo do canal autor do video
    private byte category_id;                  // De 0 a 44. Detalhes estão no arquivo JSON em /database
    private String publish_time;              // Quando o video foi publicado
    private String tags;                      // Lista separada por '|' contendo as tags do video
    private long views;                       // Número de views.
    private long likes;                       // Número de likes      (Ops, esses 3 são long por que existem videos com mais de MAX_INT visualizações.)
    private long dislikes;                    // Número de dislikes
    private int comment_count;                // Quantia de comentários
    private String thumbnail_link;            // Link para ver a imagem da thumbnail
    private boolean comments_disabled;        // Comentários estão desabilitados?
    private boolean ratings_disabled;         // Likes/Dislikes estão desabilitados?
    private boolean video_error_or_removed;   // Video removido do youtube?
    private String description;               // Descrição do video
    private char[] country;                   // Pais origem do video (Sempre 2 Bytes, 2 caracteres (US/CA/BR/...))

    public int getByteSize() {
        return byteSize;
    }
    public void setByteSize(int byteSize) {
        this.byteSize = byteSize;
    }
    public void setLapide(boolean lapide) {
        this.lapide = lapide;
    }
    public void setDb_id(long db_id) {
        this.db_id = db_id;
    }
    public long getDb_id() {
        return db_id;
    }
    public int getCategory_id() {
        return category_id;
    }
    public String getChannel_title() {
        return channel_title;
    }
    public int getComment_count() {
        return comment_count;
    }
    public String getDescription() {
        return description;
    }
    public long getDislikes() {
        return dislikes;
    }
    public long getLikes() {
        return likes;
    }
    public String getPublish_time() {
        return publish_time;
    }
    public String getTags() {
        return tags;
    }
    public String getThumbnail_link() {
        return thumbnail_link;
    }
    public String getTitle() {
        return title;
    }
    public String getTrending_date() {
        return trending_date;
    }
    public String getVideo_id() {
        return video_id;
    }
    public long getViews() {
        return views;
    }
    public boolean getComments_disabled()
    {
        return comments_disabled;
    }
    public boolean getRatings_disabled()
    {
        return ratings_disabled;
    }
    public boolean getVideo_error_or_removed()
    {
        return video_error_or_removed;
    }
    public boolean getLapide()
    {
        return lapide;
    }

    public String getCountry() {
        String result = "";
        result += this.country[0];
        result += this.country[1];
        return result;
    }
    
    public void setCategory_id(byte category_id) {
        this.category_id = category_id;
    }
    public void setChannel_title(String channel_title) {
        this.channel_title = channel_title;
    }
    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }
    public void setComments_disabled(boolean comments_disabled) {
        this.comments_disabled = comments_disabled;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setDislikes(long dislikes) {
        this.dislikes = dislikes;
    }
    public void setLikes(long likes) {
        this.likes = likes;
    }
    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }
    public void setRatings_disabled(boolean ratings_disabled) {
        this.ratings_disabled = ratings_disabled;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }
    public void setThumbnail_link(String thumbnail_link) {
        this.thumbnail_link = thumbnail_link;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setTrending_date(String trending_date) {
        this.trending_date = trending_date;
    }
    public void setVideo_error_or_removed(boolean video_error_or_removed) {
        this.video_error_or_removed = video_error_or_removed;
    }
    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }
    public void setViews(long views) {
        this.views = views;
    }

    public void setCountry(String country) {
        this.country[0] = country.charAt(0);
        this.country[1] = country.charAt(1);
    }

    /*
     * Metodo de manipulação de String. Funciona de forma parecida com o .split
     */
    public static String[] stringSplit (String data, String delimitator)
    {   
        String[] retSTR = new String[2];
        retSTR[0] = "";
        retSTR[1] = "";

        int index = 0; // Checa o caractere da string Data
        int index_test = index; // Utilizado para iterar quando encontramos um caractere do delimitador, sem modificar o indice original até termos certeza de que temos um full-match.
        int index2 = 0; // Checa o caractere do Delimitador

        boolean b4Delim = true;

        if (data == null || data.length() < 1) {
            return retSTR;
        }

        // Checamos se a data começa com "
        // Se ela começar com ", nosso delimitador deve ser '",'
        // Caso contrário, será apenas ,
        if(delimitator == null)
        {
            if(data.charAt(0) == '"')
            {
                delimitator = "\",";
            }
            else
            {
                delimitator = ",";
            }
        }
        
        // Até o delimitador ser encontrado, copia em retSTR[0]
        // Mas antes, checamos se a string é um [none] para tratar de alguns erros que eu tive com tags vazias.
        if(isNone(data, 0))
        {
            retSTR[0] = "\"[none]\"";
            index = 7; // Pula o [none] e a virgula após.
        }
        else
        {
            while (b4Delim && (index < data.length())) {
                index_test = index;
                index2 = 0;
                while(data.charAt(index_test) == delimitator.charAt(index2)) // Match no primeiro caractere do delimitador
                {
                    if(index2 == delimitator.length() - 1) // Match em TODOS os caracteres do delimitador.
                    {
                        b4Delim = false;
                        if(data.charAt(index) == '"') retSTR[0] += data.charAt(index); // Garante que a string terá um " no final.
                        index = index_test + 1;
                        break;
                    }
                    index_test += 1;
                    index2 += 1;
                }
                if(b4Delim)
                {
                    retSTR[0] += data.charAt(index);
                    index++;
                }
            }
        }
        // Após encontrado o delimitador, copiar o resto da string em STR[1]
        while(index < data.length())
        {
            retSTR[1] += data.charAt(index);
            index++;
        }
        return retSTR;
    }

    private static boolean isNone(String a, int index)
    {
        if(a.charAt(index) == '[' && a.charAt(index + 5) == ']')
        {
            if(a.charAt(index + 1) == 'n' &&
               a.charAt(index + 2) == 'o' &&
               a.charAt(index + 3) == 'n' &&
               a.charAt(index + 4) == 'e')
               {
                return true;
               }
        }
        return false;
    }


    /*
     * Metodos utilizados para I/O e conversão
     */

    public void printToString()
    {
        MyIO.println("## 1 : Lapide:                ## " + this.lapide                       + "\n" +
                     "## 2 : Database ID:           ## " + this.db_id                        + "\n" +
                     "## 3 : Size in Bytes:         ## " + this.byteSize                     + "\n" +
                     "## 4 : Category ID:           ## " + this.category_id                  + "\n" +
                     "## 5 : Comment Count:         ## " + this.comment_count                + "\n" +
                     "## 6 : View:                  ## " + this.views                        + "\n" +
                     "## 7 : Likes:                 ## " + this.likes                        + "\n" +
                     "## 8 : Dislikes:              ## " + this.dislikes                     + "\n" +
                     "## 9 : Country Code:          ## " + this.country[0] + this.country[1] + "\n" +
                     "## 10: Comments Disabled:     ## " + this.comments_disabled            + "\n" +
                     "## 11: Ratings Disabled:      ## " + this.ratings_disabled             + "\n" +
                     "## 12: Video Error or Removed ## " + this.video_error_or_removed       + "\n" +
                     "## 13: Youtube Video ID:      ## " + this.video_id                     + "\n" +
                     "## 14: Trending Date:         ## " + this.trending_date                + "\n" +
                     "## 15: Title:                 ## " + this.title                        + "\n" +
                     "## 16: Author:                ## " + this.channel_title                + "\n" +
                     "## 17: Publish Time:          ## " + this.publish_time                 + "\n" +
                     "## 18: Tags:                  ## " + this.tags                         + "\n" +
                     "## 19: Thumbnail Link:        ## " + this.thumbnail_link               + "\n" +
                     "## 20: Description:           ## " + this.description                  + "\n"
        );
    }

    public String printToCSV()
    {
        return (            db_id            + ","  + byteSize               + ","
                          + lapide           + ","  + video_id               + "," 
                          + trending_date    + ","  + title                  + "," 
                          + channel_title    + ","  + category_id            + "," 
                          + publish_time     + ","  + tags                   + "," 
                          + views            + ","  + likes                  + "," 
                          + dislikes         + ","  + comment_count          + "," 
                          + thumbnail_link   + ","  + comments_disabled      + "," 
                          + ratings_disabled + ","  + video_error_or_removed + "," 
                          + getCountry()     + ","  + description);
    }


    /*
     * Construtores
     */


    // Construtor usado para registros não-tratados.
    public Model (String data, long db_id, char countryCode[])
    {
        String buffer[]; // recebe dados do stringSplit
        
        buffer = stringSplit(data, ","); // Primeiro campo é o campo video_id, que não está entre aspas, logo, ","

        /*
         * Seguiremos esse padrão neste construtor. Se um dado está entre parenteses na DB não tratada, nosso separador será
         *  ",   E, caso ele não esteja entre parenteses, nosso separador será apenas , 
         */
        
        this.video_id               =                      buffer[0] ; buffer = stringSplit(buffer[1], null);
        this.trending_date          =                      buffer[0] ; buffer = stringSplit(buffer[1], null);
        this.title                  =                      buffer[0] ; buffer = stringSplit(buffer[1], null); 
        this.channel_title          =                      buffer[0] ; buffer = stringSplit(buffer[1], null);
        this.category_id            =       Byte.parseByte(buffer[0]); buffer = stringSplit(buffer[1], null);
        this.publish_time           =                      buffer[0] ; buffer = stringSplit(buffer[1], null);
        this.tags                   =                      buffer[0] ; buffer = stringSplit(buffer[1], null);
        this.views                  =       Long.parseLong(buffer[0]); buffer = stringSplit(buffer[1], null);
        this.likes                  =       Long.parseLong(buffer[0]); buffer = stringSplit(buffer[1], null);
        this.dislikes               =       Long.parseLong(buffer[0]); buffer = stringSplit(buffer[1], null);
        this.comment_count          =     Integer.parseInt(buffer[0]); buffer = stringSplit(buffer[1], null);
        this.thumbnail_link         =                      buffer[0] ; buffer = stringSplit(buffer[1], null);
        this.comments_disabled      = Boolean.parseBoolean(buffer[0]); buffer = stringSplit(buffer[1], null);
        this.ratings_disabled       = Boolean.parseBoolean(buffer[0]); buffer = stringSplit(buffer[1], null);
        this.video_error_or_removed = Boolean.parseBoolean(buffer[0]);
        this.description            =                      buffer[1] ;
    
        // Esses dados são adicionados depois, e não estão na base de dados original.
        this.db_id = db_id;  // ID em relação ao Dataset.
        this.lapide = false; // Dados das DBs prontas nunca são laipde.
        this.country = countryCode;
        this.byteSize = this.calcSize();
    }

    public Model(Model a)
    {
        this.lapide = a.lapide; this.db_id = a.db_id; this.byteSize = a.byteSize;
        this.category_id = a.category_id; this.comment_count = a.comment_count;
        this.views = a.views; this.likes = a.likes; this.dislikes = a.dislikes;
        this.country = a.country; this.comments_disabled = a.comments_disabled;
        this.ratings_disabled = a.ratings_disabled; this.video_error_or_removed = a.video_error_or_removed;
        this.video_id = a.video_id; this.trending_date = a.trending_date;
        this.title = a.title; this.channel_title = a.channel_title;
        this.publish_time = a.publish_time; this.tags = a.tags; this.thumbnail_link = a.thumbnail_link;
        this.description = a.description;
    }

    // Construtor padrão para a base de dados tratada.
    public Model (RandomAccessFile a) throws Exception
    {
        this.lapide = a.readBoolean();
        this.db_id = a.readLong();
        this.byteSize = a.readInt();
        this.category_id = a.readByte();
        this.comment_count = a.readInt();
        this.views = a.readLong();
        this.likes = a.readLong();
        this.dislikes = a.readLong();
        this.country = new char[2];
        String ab = "";
        ab += a.readChar();
        ab += a.readChar();
        this.setCountry(ab);
        this.comments_disabled = a.readBoolean();
        this.ratings_disabled = a.readBoolean();
        this.video_error_or_removed = a.readBoolean();

        this.video_id = a.readUTF();
        this.trending_date = a.readUTF();
        this.title = a.readUTF(); 
        this.channel_title = a.readUTF();
        this.publish_time = a.readUTF();
        this.tags = a.readUTF();
        this.thumbnail_link = a.readUTF();
        this.description = a.readUTF();
    }

    @Override public Model clone()
    {
        Model returnModel = new Model(this);
        return returnModel;
    }

    // Metodo que calcula o tamanho em bits.
    private int calcSize()
    {
        int size = 0;
        
        // Tamanho fixo: 4 LONGS, 1 BYTE, 2 INT, 4 BOOL, 1 Char[2](Sempre será uma letra maiuscula, logo, 2 bytes bastam por char)
        size =              4*8 +   1*1 +   2*4 +  1*4  +  2*2;
        
        // Tamanho variavel: 8 Strings, cada char pode ter até 4 bytes em UTF-8
        try {
            size += getActualBytes(video_id);
            size += getActualBytes(trending_date);
            size += getActualBytes(title);
            size += getActualBytes(channel_title);
            size += getActualBytes(publish_time);
            size += getActualBytes(tags);
            size += getActualBytes(thumbnail_link);
            size += getActualBytes(description);
            size += (8 * 2); // Two bytes for the size of each string.
            
        } catch (Exception e) {
            MyIO.println("Error getting byte Size!");
        }

        return size;
    }

    // Apos quase coringar, descobri que a função getBytes não pega os bytes corretos de emoticons.
    // Então tive que criar uma função que funciona para pegar o tamanho correto de emoticons.
    public static int getActualBytes(String a)
    {
        int strlen = a.length();
        int utflen = strlen; // optimized for ASCII

        for (int i = 0; i < strlen; i++) {
            int c = a.charAt(i);
            if (c >= 0x80 || c == 0)
                utflen += (c >= 0x800) ? 2 : 1; // Emojis tem 2 chars por char... Se é que isso faz sentido.
        }
        return utflen;
    }

    // Retorna uma string que pode ser usada para construir um Model.
    public static String readModel()
    {
        MyIO.println("Escreva um registro novo no formato: video_id,trending_date,title,channel_title,category_id,publish_time,tags,views,likes,dislikes,comment_count,thumbnail_link,comments_disabled,ratings_disabled,video_error_or_removed,description");
        MyIO.println("Os campos comments_disabled,ratings_disabled,video_error_or_removed devem ser apenas 'true' ou 'false");
        MyIO.println("Favor preencher estes campos apenas com números: category_id, views, likes, dislikes,comment_count");
        String data = MyIO.readLine();
        return data;
    }

    // Permite que um Model seja editado.
    public Model edit()
    {
        Model a = this.clone();
        int i = 0;
        while(i == 0)
        {
            a.printToString();
            int argument = MyIO.readInt("Escreva o número atributo que deseja editar (1-20), ou digite 0 para sair. \n");
            String value = "";
            char confirm = 'b';
            switch (argument) {
                case 0:
                    i = 1;
                break;
                case 1:
                    value = MyIO.readString("Escreva o novo valor para o atributo: \n");
                    MyIO.println("## 1: Lapide = " + value);
                    confirm = MyIO.readChar("Isso parece correto? y/n: \n");
                    if(confirm == 'y' || confirm == 'Y')
                    {
                        a.lapide = Boolean.parseBoolean(value);
                    }
                    break;
                case 2:
                    value = MyIO.readString("Escreva o novo valor para o atributo: \n");
                    MyIO.println("## 2: Database ID = " + value);
                    confirm = MyIO.readChar("Isso parece correto? y/n: \n");
                    if(confirm == 'y' || confirm == 'Y')
                    {
                        a.setDb_id(Long.parseLong(value));
                    }
                    break;
                case 3:
                    value = MyIO.readString("Escreva o novo valor para o atributo: \n");
                    MyIO.println("!! Alteração manual do tamanho em Bytes não é permitido. !!");
                    confirm = MyIO.readChar("Aperte Enter para continuar.");
                    break;
                    case 4:

                    value = MyIO.readString("Escreva o novo valor para o atributo: \n");
                    MyIO.println("## 4: Category ID = " + value);
                    confirm = MyIO.readChar("Isso parece correto? y/n: \n");
                    if(confirm == 'y' || confirm == 'Y')
                    {
                        a.setCategory_id(Byte.parseByte(value));
                    }
                    break;
                case 5:
                    value = MyIO.readString("Escreva o novo valor para o atributo: \n");
                    MyIO.println("## 5: Comment Count = " + value);
                    confirm = MyIO.readChar("Isso parece correto? y/n: \n");
                    if(confirm == 'y' || confirm == 'Y')
                    {
                        a.setComment_count(Integer.parseInt(value));
                    }
                    break;
                case 6:
                    value = MyIO.readString("Escreva o novo valor para o atributo: \n");
                    MyIO.println("## 6: Views = " + value);
                    confirm = MyIO.readChar("Isso parece correto? y/n: \n");
                    if(confirm == 'y' || confirm == 'Y')
                    {
                        a.setViews(Long.parseLong(value));
                    }
                    break;
                case 7:
                    value = MyIO.readString("Escreva o novo valor para o atributo: \n");
                    MyIO.println("## 7: Likes = " + value);
                    confirm = MyIO.readChar("Isso parece correto? y/n: \n");
                    if(confirm == 'y' || confirm == 'Y')
                    {
                        a.setLikes(Long.parseLong(value));
                    }
                    break;
                case 8:
                    value = MyIO.readString("Escreva o novo valor para o atributo: \n");
                    MyIO.println("## 8: Dislikes = " + value);
                    confirm = MyIO.readChar("Isso parece correto? y/n: \n");
                    if(confirm == 'y' || confirm == 'Y')
                    {
                        a.setDislikes(Long.parseLong(value));;
                    }
                    break;
                case 9:
                    value = MyIO.readString("Escreva o novo valor para o atributo: \n");
                    MyIO.println("## 9: Country Code = " + value);
                    confirm = MyIO.readChar("Isso parece correto? y/n: \n");
                    if(confirm == 'y' || confirm == 'Y')
                    {
                        a.country[0] = value.charAt(0);
                        a.country[1] = value.charAt(1);
                    }
                    break;
                case 10:
                    value = MyIO.readString("Escreva o novo valor para o atributo: \n");
                    MyIO.println("## 10: Comments Disabled = " + value);
                    confirm = MyIO.readChar("Isso parece correto? y/n: \n");
                    if(confirm == 'y' || confirm == 'Y')
                    {
                        a.comments_disabled = Boolean.parseBoolean(value);
                    }
                    break;
                case 11:
                    value = MyIO.readString("Escreva o novo valor para o atributo: \n");
                    MyIO.println("## 11: Ratings Disabled = " + value);
                    confirm = MyIO.readChar("Isso parece correto? y/n: \n");
                    if(confirm == 'y' || confirm == 'Y')
                    {
                        a.ratings_disabled = Boolean.parseBoolean(value);
                    }
                    break;
                case 12:
                    value = MyIO.readString("Escreva o novo valor para o atributo: \n");
                    MyIO.println("## 12: Video Error or Removed = " + value);
                    confirm = MyIO.readChar("Isso parece correto? y/n: \n");
                    if(confirm == 'y' || confirm == 'Y')
                    {
                        a.video_error_or_removed = Boolean.parseBoolean(value);
                    }
                    break;
                case 13:
                    value = MyIO.readString("Escreva o novo valor para o atributo: \n");
                    MyIO.println("## 13: Youtube ID = " + value);
                    confirm = MyIO.readChar("Isso parece correto? y/n: \n");
                    if(confirm == 'y' || confirm == 'Y')
                    {
                        a.video_id = value;
                    }
                    break;
                case 14:
                    value = MyIO.readString("Escreva o novo valor para o atributo: \n");
                    MyIO.println("## 1: Trending Date = " + value);
                    confirm = MyIO.readChar("Isso parece correto? y/n: \n");
                    if(confirm == 'y' || confirm == 'Y')
                    {
                        a.trending_date = value;
                    }
                    break;
                case 15:
                    value = MyIO.readString("Escreva o novo valor para o atributo: \n");
                    MyIO.println("## 15: Titulo = " + value);
                    confirm = MyIO.readChar("Isso parece correto? y/n: \n");
                    if(confirm == 'y' || confirm == 'Y')
                    {
                        a.title = value;
                    }
                    break;
                case 16:
                    value = MyIO.readString("Escreva o novo valor para o atributo: \n");
                    MyIO.println("## 16: Author = " + value);
                    confirm = MyIO.readChar("Isso parece correto? y/n: \n");
                    if(confirm == 'y' || confirm == 'Y')
                    {
                        a.channel_title = value;
                    }
                    break;
                case 17:
                    value = MyIO.readString("Escreva o novo valor para o atributo: \n");
                    MyIO.println("## 17: Publish Time = " + value);
                    confirm = MyIO.readChar("Isso parece correto? y/n: \n");
                    if(confirm == 'y' || confirm == 'Y')
                    {
                        a.publish_time = value;
                    }
                    break;
                case 18:
                    value = MyIO.readString("Escreva o novo valor para o atributo: \n");
                    MyIO.println("## 18: Tags = " + value);
                    confirm = MyIO.readChar("Isso parece correto? y/n: \n");
                    if(confirm == 'y' || confirm == 'Y')
                    {
                        a.tags = value;
                    }
                    break;
                case 19:
                    value = MyIO.readString("Escreva o novo valor para o atributo: \n");
                    MyIO.println("## 19: Thumbnail Link = " + value);
                    confirm = MyIO.readChar("Isso parece correto? y/n: \n");
                    if(confirm == 'y' || confirm == 'Y')
                    {
                        a.thumbnail_link = value;
                    }
                    break;
                case 20:
                    value = MyIO.readString("Escreva o novo valor para o atributo: \n");
                    MyIO.println("## 20: Description = " + value);
                    confirm = MyIO.readChar("Isso parece correto? y/n: \n");
                    if(confirm == 'y' || confirm == 'Y')
                    {
                        a.description = value;
                    }
                    break;

                default:
                    MyIO.println("Operacao Invalida");
                    break;
            }

            a.byteSize = a.getByteSize();
        }
        return a;
    }
}
