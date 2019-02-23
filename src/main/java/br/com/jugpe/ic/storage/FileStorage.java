package br.com.jugpe.ic.storage;

import java.util.LinkedHashMap;
import java.util.Map;

public class FileStorage {

    private Map<String, String> storage = new LinkedHashMap<>();

    public void add(Map<String, String> map) {
        map.forEach(storage::put);
    }

    public byte[] get(String fileName) {
        return storage.get(fileName).getBytes();
    }

    public int size() {
        return storage.size();
    }

}