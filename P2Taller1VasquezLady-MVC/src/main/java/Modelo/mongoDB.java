package Modelo;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import org.bson.Document;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class mongoDB {
    private final String dataBaseName = "P2Lab1VasquezLady";
    private final MongoClient triang;
    private MongoDatabase mongoDB;
    public static String collectionName = "Triangulos";

    public mongoDB() {
        triang = MongoClients.create("mongodb://localhost:27017");
        mongoDB = triang.getDatabase(dataBaseName);
        if (mongoDB.getCollection(collectionName) == null) {
            mongoDB.createCollection(collectionName);
        }
    }

    public MongoDatabase getMongoDB() {
        return mongoDB;
    }

    // Connection
    public MongoDatabase createConnection() {
        try {
            mongoDB = getMongoDB();
            System.out.println("\n\n--->>>>Connection exitosa a DataBase mongoDb<<<<----\n\n");
            return mongoDB;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Create
    public boolean createDocument(Document doc) {
        try {
            MongoDatabase db = createConnection();
            if (db != null) {
                MongoCollection<Document> collection = db.getCollection(collectionName);
                collection.insertOne(doc);
                System.out.println("\n\n--->>>DATOS CARGADOS A LA DB CON EXITO<<<---\n\n");
                return true;
            }
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Read
    public List<Document> readDocument(Document doc) {
        List<Document> result = new ArrayList<>();
        try {
            MongoDatabase db = createConnection();
            MongoCollection<Document> collection = db.getCollection(collectionName);
            result = collection.find(doc).into(new ArrayList<>());
            System.out.println("\n\n--->>>DATOS LEIDOS DE LA DB CON EXITO<<<---\n\n");
            return result;
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return result;
    }

    // Delete
    public boolean deleteDocument(Document doc) {
        try {
            MongoDatabase db = createConnection();
            if (db != null) {
                MongoCollection<Document> collection = db.getCollection(collectionName);
                collection.deleteOne(doc);
                System.out.println("\n\n--->>>DATOS ELIMINADOS DE LA DB CON EXITO<<<---\n\n");
                return true;
            }
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update
    public boolean updateDocument(Document docOld, Document docNew) {
        try {
            MongoDatabase db = createConnection();
            if (db != null) {
                MongoCollection<Document> collection = db.getCollection(collectionName);
                collection.updateOne(docOld, new Document("$set", docNew));
                System.out.println("\n\n--->>>DATOS MODIFICADOS DE LA DB CON EXITO<<<---\n\n");
                return true;
            }
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Search
    public ArrayList<Document> searchDocument(Document filtro) {
        ArrayList<Document> resultados = new ArrayList<>();
        MongoDatabase db = createConnection();
        try {
            MongoCollection<Document> collection = db.getCollection(collectionName);
            for (Document doc : collection.find(filtro)) {
                resultados.add(doc);
            }
            System.out.println("\n\n--->>>DATOS ENCONTRADOS DE LA DB CON EXITO<<<---\n\n");
            return resultados;
        } catch (MongoException e) {
            e.printStackTrace();
        }
        return null;
    }

    public DefaultTableModel cargarDataTable() {
        // Obtener la colección "Triangulos"
        MongoCollection<Document> collection = mongoDB.getCollection(collectionName);

        // Crear una lista de documentos
        List<Document> documents = collection.find().into(new ArrayList<>());

        // Definir los nombres de las columnas
        String[] columnNames = {"Número A", "Número B", "Área", "Lado 1", "Lado 2", "Lado 3", "Tipo"}; // Cambia estos según tus campos en MongoDB

        // Crear un modelo de tabla
        DefaultTableModel tdm = new DefaultTableModel(columnNames, 0);

        // Iterar sobre los documentos y añadir filas al modelo de la tabla
        for (Document doc : documents) {
            Object[] row = {
                doc.get("Número A"), 
                doc.get("Número B"),
                doc.get("Área"),
                doc.get("Lado 1"),
                doc.get("Lado 2"),
                doc.get("Lado 3"),
                doc.get("Tipo")
            };
            tdm.addRow(row);
        }

        return tdm;
    }
}