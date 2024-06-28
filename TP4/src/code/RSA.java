package src.code;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

public class RSA {
    String filepath;
    private BigInteger n, d, e;

    // Número de bits para a chave RSA
    private int bitlen = 1024;
    private SecureRandom secureRandom = new SecureRandom();

    public RSA(String filepath) {
        this.filepath = filepath;
        generateKeys();
    }

    private void generateKeys() {
        BigInteger p = BigInteger.probablePrime(bitlen / 2, secureRandom);
        BigInteger q = BigInteger.probablePrime(bitlen / 2, secureRandom);
        n = p.multiply(q);

        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        e = BigInteger.probablePrime(bitlen / 2, secureRandom);
        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0) {
            e = e.add(BigInteger.ONE);
        }

        d = e.modInverse(phi);
    }

    public void Criptografar() {
        try {
            // Lê o conteúdo do arquivo
            File file = new File(filepath);
            FileInputStream fis = new FileInputStream(file);
            byte[] fileData = new byte[(int) file.length()];
            fis.read(fileData);
            fis.close();

            // Criptografa o conteúdo do arquivo em blocos
            byte[] encryptedData = encryptDataInBlocks(fileData);

            // Salva o conteúdo criptografado em um novo arquivo
            File encryptedFile = new File(filepath + ".encrypted");
            FileOutputStream fos = new FileOutputStream(encryptedFile);
            fos.write(encryptedData);
            fos.close();

            System.out.println("Arquivo criptografado com sucesso: " + encryptedFile.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] encryptDataInBlocks(byte[] data) {
        int blockSize = bitlen / 8 - 11; // Tamanho do bloco para criptografia
        int numBlocks = (int) Math.ceil((double) data.length / blockSize);
        byte[] encryptedData = new byte[numBlocks * (bitlen / 8)];

        for (int i = 0; i < numBlocks; i++) {
            int start = i * blockSize;
            int length = Math.min(blockSize, data.length - start);
            byte[] block = new byte[length];
            System.arraycopy(data, start, block, 0, length);

            byte[] encryptedBlock = encrypt(block);
            System.arraycopy(encryptedBlock, 0, encryptedData, i * (bitlen / 8), encryptedBlock.length);
        }

        return encryptedData;
    }

    private byte[] encrypt(byte[] data) {
        return (new BigInteger(1, data)).modPow(e, n).toByteArray();
    }

    private byte[] decrypt(byte[] data) {
        return (new BigInteger(1, data)).modPow(d, n).toByteArray();
    }

    public void Descriptografar() {
        try {
            // Lê o conteúdo do arquivo criptografado
            File file = new File(filepath + ".encrypted");
            FileInputStream fis = new FileInputStream(file);
            byte[] fileData = new byte[(int) file.length()];
            fis.read(fileData);
            fis.close();

            // Descriptografa o conteúdo do arquivo em blocos
            byte[] decryptedData = decryptDataInBlocks(fileData);

            // Salva o conteúdo descriptografado em um novo arquivo
            File decryptedFile = new File(filepath + ".decrypted");
            FileOutputStream fos = new FileOutputStream(decryptedFile);
            
            // Padding.
            fos.write(0);
            fos.write(0);
            fos.write(0);
            fos.write(0);
            fos.write(0);
            fos.write(0);
            
            fos.write(decryptedData);
            fos.close();

            System.out.println("Arquivo descriptografado com sucesso: " + decryptedFile.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] decryptDataInBlocks(byte[] data) {
        int blockSize = bitlen / 8; // Tamanho do bloco para descriptografia
        int numBlocks = data.length / blockSize;
        byte[] decryptedData = new byte[numBlocks * (blockSize - 11)];

        for (int i = 0; i < numBlocks; i++) {
            int start = i * blockSize;
            byte[] block = new byte[blockSize];
            System.arraycopy(data, start, block, 0, blockSize);

            byte[] decryptedBlock = decrypt(block);
            System.arraycopy(decryptedBlock, 0, decryptedData, i * (blockSize - 11), decryptedBlock.length);
        }

        return decryptedData;
    }
}
