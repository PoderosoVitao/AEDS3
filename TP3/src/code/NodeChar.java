package src.code;

// Nodes de linkagem unica que guardam Chars
// Usados exclusivamente para metodos de compressão.

public class NodeChar {
    
    private char chara;
    private NodeChar right;

    public NodeChar ()
    {
        chara = '\0';
        right = null;
    }

    public NodeChar (char a)
    {
        chara = a;
        right = null;
    }

    public void setChara(char chara) {
        this.chara = chara;
    }
    public char getChara() {
       return this.chara;
    }
    
    public void setRight(NodeChar right) {
        this.right = right;
    }
    public NodeChar getRight() {
       return this.right;
    }
}
