package mx.ejlf.demo.utils;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

public class DocumentToJSONParserUtil {
    private static final Logger LOG = Logger.getLogger(DocumentToJSONParserUtil.class.getName());

    public DocumentToJSONParserUtil() {
    }

    public static JSONObject parseDocumentToJSONObject(final Document document) {
        Set<String> keySet = document.keySet();
        Iterator<String> keyIterator = keySet.iterator();
        JSONObject jsonObject = new JSONObject(document.toJson());

        while(true) {
            while(keyIterator.hasNext()) {
                String key = (String)keyIterator.next();
                if (document.get(key) instanceof Document) {
                    jsonObject.put(key, parseDocumentToJSONObject((Document)document.get(key)));
                } else if (!(document.get(key) instanceof List)) {
                    if (document.get(key) instanceof Date) {
                        jsonObject.put(key, document.getDate(key));
                    }
                } else {
                    JSONArray array = new JSONArray();
                    List<Object> arrayWithObjects = document.getList(key, Object.class);
                    Iterator iteratorArray = arrayWithObjects.iterator();

                    while(iteratorArray.hasNext()) {
                        Object objectArray = iteratorArray.next();
                        if (objectArray instanceof Document) {
                            array.put(parseDocumentToJSONObject((Document)objectArray));
                        } else {
                            array.put(objectArray);
                        }
                    }

                    jsonObject.put(key, array);
                }
            }

            return jsonObject;
        }
    }
}

