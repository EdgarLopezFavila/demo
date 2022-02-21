package mx.ejlf.demo.db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.eclipse.microprofile.config.ConfigProvider;

public class MongoConnection {
    //private static Map<String, MongoConnection> map = new HashMap<>();
    private static MongoConnection _instance = null;

    MongoClient documentClient = null;

    String auth;
    String user;
    String pass;
    String db;

    MongoConnection() {
        auth                = "atlas-auth";
        user                = ConfigProvider.getConfig().getValue("data.mongodb.user", String.class);;
        pass                = ConfigProvider.getConfig().getValue("data.mongodb.pass", String.class);;
        db                  = ConfigProvider.getConfig().getValue("data.mongodb.db", String.class);
    }

    /*MongoConnection(DBEnum db_enum) {
        db = db_enum.getDatabase();
        String properties_host  = "data.mongodb." + db + ".hosts";
        String properties_auth  = "data.mongodb." + db + ".auth";
        String properties_user  = "data.mongodb." + db + ".user";
        String properties_pass  = "data.mongodb." + db + ".pass";
        hosts                   = ConfigProvider.getConfig().getValue(properties_host, String.class);
        if(hosts != null && hosts.startsWith("mongodb+srv:")){
            auth                = "atlas-auth";
            user                = "";
            pass                = "";
        } else {
            auth                = ConfigProvider.getConfig().getValue(properties_auth, String.class);
            user                = ConfigProvider.getConfig().getValue(properties_user, String.class);
            pass                = ConfigProvider.getConfig().getValue(properties_pass, String.class);
        }
        this.db                 = db;
    }*/

    public static MongoConnection Instance() {
        if(_instance == null) {
            _instance = new MongoConnection();
        }

        return _instance;
    }

    /*public static MongoConnection Instance(DBEnum db_enum) {
        if (! map.containsKey(db_enum.getDatabase())){
            MongoConnection db_instance = new MongoConnection(db_enum);
            map.put(db_enum.getDatabase(),  db_instance);
        }
        return map.get(db_enum.getDatabase());
    }*/

    public MongoDatabase document() {
        if (documentClient == null) {
            documentClient = internal_createDocument();
        }
        return documentClient.getDatabase(db);
    }

    private synchronized MongoClient internal_createDocument() {
        if (documentClient == null) {
            if ("atlas-auth".equals(auth)) {
                documentClient = internal_clientAtlas();
            } else if ("no-auth".equals(auth)) {
                //documentClient = internal_clientNoAuth();
            } else if ("scram-sha1".equals(auth)) {
                //documentClient = internal_clientScramSha1();
            }
        }
        return documentClient;
    }

    private MongoClient internal_clientAtlas() {
        ConnectionString connectionString = new ConnectionString("mongodb+srv://"+user+":"+pass+"@own-dev.lgucy.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        MongoClient client = MongoClients.create(settings);
        return client;
    }

}