package mx.ejlf.demo.db;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;

import static mx.ejlf.demo.utils.DocumentToJSONParserUtil.*;

@ApplicationScoped
public class DataBaseHelper {

    protected  static MongoDatabase mongoClient = MongoConnection.Instance().document();
    public static final int ROWS_BY_PAGE = 10;

    public JSONObject findByDocument(String collection, Bson queryDoc) {
        FindIterable<Document> fi = mongoClient.getCollection(collection).find(queryDoc);
        return fi.first() != null ? parseDocumentToJSONObject(fi.first()) : null;
    }

    public void insert(String collection, Document document) {
        mongoClient.getCollection(collection).insertOne(document);
    }

    public boolean update(String collection, Bson docQuery, Document docUpdate) {
        UpdateResult result = mongoClient.getCollection(collection).updateOne(docQuery,docUpdate);
        if (result.getMatchedCount() == 1) {
            return true;
        }
        return false;
    }

    public JSONArray findManyDocument(String collection, Bson queryDoc, int pageNum) {
        JSONArray array = new JSONArray();
        FindIterable fi = null;
        if (queryDoc == null) {
            fi = mongoClient.getCollection(collection).find().skip(ROWS_BY_PAGE*(pageNum - 1));
        } else  {
            fi = mongoClient.getCollection(collection).find(queryDoc).skip(ROWS_BY_PAGE*(pageNum - 1));
        }

        if (fi != null) {
            MongoCursor cursor = fi.cursor();
            while (cursor.hasNext()) {
                array.put(parseDocumentToJSONObject((Document)cursor.next()));
            }
        }
        return null;
    }

    public Long countRows (String collection, Bson queryDoc) {
        if (queryDoc != null) {
            return mongoClient.getCollection(collection).countDocuments(queryDoc);
        }
        return mongoClient.getCollection(collection).countDocuments();
    }
}
