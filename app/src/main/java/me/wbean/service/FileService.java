package me.wbean.service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileService {
    private File dir;

    public FileService(File dir) {
        this.dir = dir;
    }

    public boolean isSelfKeyExist() {
        File file = new File(dir, "pri.main");
        return file.exists();
    }

    public String getPublicKey(String name) throws IOException {
        return read(name + ".pub");
    }

    public String getSelfPrivateKey() throws IOException {
        return read("pri.main");
    }

    public String getSelfPublicKey() throws IOException {
        return read("pub.main");
    }

    public void writeSelfKeyPair(String name, String publicKey, String privateKey) throws IOException {
        write("pub.main", publicKey);
        write("pri.main", privateKey);
        write("name.main", name);
    }

    public void writePublicKey(String name, String publicKey) throws IOException {
        write(name + ".pub", publicKey);
    }

    private void write(String name, String content) throws IOException {
        File file = new File(dir, name);
        if (!file.exists()) {
            file.createNewFile();
        }
        // write publicKey to file
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.flush();
        writer.close();
    }

    private String read(String name) throws IOException {
        File file = new File(dir, name);
        if (!file.exists()) {
            return "";
        }
        // read content from file
        Scanner scanner = new Scanner(file);
        String content = scanner.nextLine();
        scanner.close();
        return content;
    }
}
