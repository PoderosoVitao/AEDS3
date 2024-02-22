package src.code;

/*
 * Arquivo que descreve o modelo das entities do dataset usado.
 */

public class Model {

    // A ordem do modelo não necessariamente representa a ordem em que o conteúdo é salvo.

    private long db_id;                       // ID dentro da base de dados.
    private short byteSize;                   // Tamanho do modelo em bytes
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
        
        // Até o delimitador ser encontrado, copia em retSTR[0]
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
                }
                index_test += 1;
                index2 += 1;
            }
            if(b4Delim)
            {
                retSTR[0] += data.charAt(index);
            }
        }
        // Após encontrado o delimitador, copiar o resto da string em STR[1]
        while(index < data.length())
        {
            retSTR[1] += data.charAt(index);
        }
        return retSTR;
    }


    /*
     * Metodos utilizados para I/O e conversão
     */

    public void printToString()
    {
        MyIO.println("(# "+ db_id + ": " + video_id + " ## " + trending_date    + " ## "
                          + title                   + " ## " + channel_title    + " ## " 
                          + category_id             + " ## " + publish_time     + " ## " 
                          + tags                    + " ## " + views            + " ## " 
                          + likes                   + " ## " + dislikes         + " ## "
                          + comment_count           + " ## " + thumbnail_link   + " ## "
                          + comments_disabled       + " ## " + ratings_disabled + " ## " 
                          + video_error_or_removed  + " ## " + getCountry()     + " ## " + description + " )");
    }

    public String printToCSV()
    {
        return (db_id + "," + video_id + "," + trending_date    + ","
                          + title                   + "," + channel_title    + "," 
                          + category_id             + "," + publish_time     + "," 
                          + tags                    + "," + views            + "," 
                          + likes                   + "," + dislikes         + ","
                          + comment_count           + "," + thumbnail_link   + ","
                          + comments_disabled       + "," + ratings_disabled + "," 
                          + video_error_or_removed  + "," + getCountry()     + "," + description);
    }


    /*
     * Construtores
     */

    // !!!!! ESSE CONSTRUTOR DEVE SER UTILIZADO APENAS PELA FUNÇÃO reloadDB !!!!!
    /*
     * Esse construtor é utilizado APENAS e ESTRITAMENTE pelo metodo reloadDB da classe CRUD.
     * Ele presume que os dados não estão tratados e não possuem os campos db_id, country, byteSize e lapide,
     * que existem apenas na base de dados tratada.
     * 
     * Não utilize esse construtor na base de dados tratada.
     */

    public Model (String data, long db_id, String country)
    {
        String buffer[]; // recebe dados do stringSplit
        
        buffer = stringSplit(data, ","); // Primeiro campo é o campo video_id, que não está entre aspas, logo, ","

        /*
         * Seguiremos esse padrão neste construtor. Se um dado está entre parenteses na DB não tratada, nosso separador será
         *  ",   E, caso ele não esteja entre parenteses, nosso separador será apenas , 
         */
        
        this.video_id               =                      buffer[0] ; buffer = stringSplit(buffer[1], ","  );
        this.trending_date          =                      buffer[0] ; buffer = stringSplit(buffer[1], "\",");
        this.title                  =                      buffer[0] ; buffer = stringSplit(buffer[1], "\","); 
        this.channel_title          =                      buffer[0] ; buffer = stringSplit(buffer[1], ","  );
        this.category_id            =       Byte.parseByte(buffer[0]); buffer = stringSplit(buffer[1], ","  );
        this.publish_time           =                      buffer[0] ; buffer = stringSplit(buffer[1], "\",");
        this.tags                   =                      buffer[0] ; buffer = stringSplit(buffer[1], ","  );
        this.views                  =       Long.parseLong(buffer[0]); buffer = stringSplit(buffer[1], ","  );
        this.likes                  =       Long.parseLong(buffer[0]); buffer = stringSplit(buffer[1], ","  );
        this.dislikes               =       Long.parseLong(buffer[0]); buffer = stringSplit(buffer[1], ","  );
        this.comment_count          =     Integer.parseInt(buffer[0]); buffer = stringSplit(buffer[1], ","  );
        this.thumbnail_link         =                      buffer[0] ; buffer = stringSplit(buffer[1], ","  );
        this.comments_disabled      = Boolean.parseBoolean(buffer[0]); buffer = stringSplit(buffer[1], ","  );
        this.ratings_disabled       = Boolean.parseBoolean(buffer[0]); buffer = stringSplit(buffer[1], ","  );
        this.video_error_or_removed = Boolean.parseBoolean(buffer[0]); buffer = stringSplit(buffer[1], "\",");
        this.description            =                      buffer[1] ;
    }

    // Construtor padrão para a base de dados tratada.
    public Model (String data)
    {
    }
}
