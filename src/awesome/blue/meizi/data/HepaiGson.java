
package awesome.blue.meizi.data;

import java.lang.reflect.Field;

import awesome.blue.meizi.util.DateHelper;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HepaiGson {

    private static HepaiGson hepaiGson;
    private static Gson gson;

    private HepaiGson() {
        gson = new GsonBuilder()
                .setDateFormat(DateHelper.DATE_PATTERN)
                .setFieldNamingStrategy(new FieldNamingStrategy() {

                    @Override
                    public String translateName(Field arg0) {
                        String fieldName = arg0.getName();
                        String jsonName = fieldName;

                        // jsonName = Avatars.translateFieldName(fieldName);
                        // if (jsonName == null) {
                        // jsonName = Cover.translateFieldName(fieldName);
                        // }
                        // if (jsonName == null) {
                        // jsonName = fieldName;
                        // }

                        return jsonName;
                    }
                }).create();
    }

    public static Gson getGson() {
        if (hepaiGson == null) {
            hepaiGson = new HepaiGson();
        }

        return gson;
    }
}
