//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.util.json;

import com.google.gson.JsonObject;

public interface JsonDeserializer<T> {
    T deserialize(JsonObject object);
}
