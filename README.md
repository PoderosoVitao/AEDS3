# AEDS3

Repositório para as atividades de AEDS 3.
3o periodo de Ciência da Computação, PUC MINAS, Praça da Liberdade

Professor Hayala Nepomuceno Curto

Aluno Victor Hugo Braz, 817958

# O que existe aqui?

* TP 1: 0.0.0 -> 1.0.0
Base de dados de acesso sequencial.
Estrutura de arquivos:

Main.java: Arquivo principal que age como interface.

FolderNavigator.java: Arquivo em progresso que deveria funcionar como interface grafica.

MyDLL.java: Arquivo que permite a utilização de uma DoubleLinkedList para operações em memória primaria.

Model.java: Arquivo que tem o modelo dos dados utilizados nos datasets. Também inclui metodos para tratamento.

Arquivo.java: Arquivo que contém metodos para a carga/descarga de arquivos CSV, assim como escrita e leitura.

Crud.java: Arquivo que chama os metodos das outras classes e permite as operações de CRUD na DB tratada.

MyIO.java: Contém metodos basicos de I/O

Database: DB Tratada
Database/t : Datasets backup/originais.

* TP 2: 1.1.0 -> 2.0.0
Base de dados com índice.
Mesma estruturda do TP1, mais:

Node.java : Contem os Nodes usados pela MyDLL.

BNode.java : Contem os BNodes usados pela MyBtree

MyBTree.java : Arvore B usada para guardar os indices.

Index.java    : Utilizado como classe Model para guardar o ID e o Byte_Offset de cada registro.

* TP 3: 2.2.0 > 3.0.0+
Base de dados com índice, compressão e descompactação.
Mesma estrutura do TP2, mais:

TimeSpace.java : Classe utilizada para medir fator de compressão, tempo decorrido, etc.

LZW.java : Classe para a compactação LZW.

Huffman.java : Classe para a compactação Huffman

DictionaryHash.java : Dicionario usado pela LZW.

MinHeap.java : Heap Mínimo utilizado pela Huffman

* TP 4: 4.0.0
Mesma coisa de antes, mais:

RSA.java
 Algoritmo de criptografia por RSA

BoyerMoore.Java
 Classe de casamento de padrões por BoyerMoore
