package src.code;

public class Index {
    public long id;
    public long byteOffset;

    public Index()
    {
        id = -1;
        byteOffset = -1;
    }
    public Index(long id, long byteOffset)
    {
        this.id = id;
        this.byteOffset = byteOffset;
    }
}
