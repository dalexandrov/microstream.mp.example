package io.helidon.microstream;

import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class MicrostreamService {
    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());

    private final EmbeddedStorageManager storageManager;

    public MicrostreamService() {
        this.storageManager = EmbeddedStorage.start(new DataRoot(), Paths.get("data"));
        ((DataRoot) storageManager.root()).items().clear();
        ((DataRoot) storageManager.root()).items().add("Hello");
        ((DataRoot) storageManager.root()).items().add("World");

    }

    private DataRoot root() {
        return (DataRoot) this.storageManager.root();
    }


    public List<String> getAllItems() {
        return root().items();
    }

    public String getItem(int index) {
        return root().items().get(index);
    }

    public void addNewItem(String text) {
        root().items().add(text);
        storageManager.store(root().items());
    }

    public boolean deleteItem(int index) {
        final List<String> items = this.root().items();
        if (index < 0 || index >= items.size()) {
            return false;
        } else {
            items.remove(index);
            this.storageManager.store(items);
            return true;
        }
    }
}
