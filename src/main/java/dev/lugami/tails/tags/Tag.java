package dev.lugami.tails.tags;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import dev.lugami.tails.Tails;
import dev.lugami.tails.util.CC;
import lombok.Data;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Tag {

    @Getter
    private static List<Tag> tags = new ArrayList<>();
    private String name, prefix, permission, section, serverName;

    public Tag(String name) {
        this.name = name;
        this.section = "DEFAULT";

        tags.add(this);
    }

    public static void load() {
        try (MongoCursor mongoCursor = Tails.get().getMongoDatabase().getCollection("tags").find().iterator()) {
            while (mongoCursor.hasNext()) {
                Document document = (Document) mongoCursor.next();
                if (document.containsKey("serverName") && document.getString("serverName").equals(Bukkit.getServerName().toUpperCase().replace(" ", ""))) {
                    Tag tag = new Tag(document.getString("name"));

                    if (document.containsKey("prefix")) {
                        tag.setPrefix(CC.translate(document.getString("prefix")));
                    }
                    if (document.containsKey("permission")) {
                        tag.setPermission(document.getString("permission"));
                    }
                    if (document.containsKey("section")) {
                        tag.setSection(document.getString("section"));
                    }
                    if (document.containsKey("serverName")) {
                        tag.setServerName(document.getString("serverName"));
                    }
                }
            }
        }
    }

    public void save() {
        Document document = new Document();
        document.put("name", name);
        if (prefix != null) {
            document.put("prefix", prefix);
        }
        if (permission != null) {
            document.put("permission", permission);
        }
        document.put("section", section);
        document.put("serverName", serverName.toUpperCase().replace(" ", ""));
        Tails.get().getMongoDatabase().getCollection("tags").replaceOne(Filters.eq("name", name), document, new UpdateOptions().upsert(true));
    }

    public void remove() {
        Tails.get().getMongoDatabase().getCollection("tags").deleteOne(Filters.eq("name", name));
        tags.remove(this);
    }

    public static List<Tag> getTagsBySection(String section) {
        return tags.stream().filter(tag -> tag.getSection().equalsIgnoreCase(section) && tag.getServerName().equals(Bukkit.getServerName().toUpperCase().replace(" ", ""))).collect(Collectors.toList());
    }
}