package mx.ejlf.demo.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.Iterator;

import static mx.ejlf.demo.utils.CustomTimestamp.getISODateText;

public class CustomResponse {
    /**
     * Metodo para formatear la respuesta exitosa
     *
     * @param data
     * @return
     */
    public static Response ok(JSONObject data) {
        JSONObject response = new JSONObject();
        response.put("response", "OK");
        response.put("data", toFormatDataResponse(data));

        return Response.status(200).entity(response.toString(1)).build();
    }

    /**
     * Metodo para formatear la respuesta erronea
     * @param errorCode
     * @param detail
     * @return
     */
    public static Response bad(String errorCode, String detail) {
        JSONObject response;
        JSONObject responseData;
        JSONObject responseDataException;

        responseDataException = new JSONObject();
        responseDataException.put("code", errorCode);
        responseDataException.put("detail", detail);
        responseData = new JSONObject();
        responseData.put("exception", responseDataException);

        response = new JSONObject();
        response.put("data", responseData);
        response.put("response", "BAD");


        return Response.status(200).entity(response.toString(1)).build();
    }

    /**
     * Metodo para dar formato al objeto json de respuesta
     * @param data
     * @return
     */
    private static JSONObject toFormatDataResponse(final JSONObject data) {

        if ( data.has("_id") ) {
            data.remove("_id");
        }
        Iterator<String> keyIterator = data.keys();
        while ( keyIterator.hasNext() ) {
            final String key = keyIterator.next();
            if ( data.get(key) instanceof JSONObject ) {
                data.put(key, toFormatDataResponse(data.getJSONObject(key)));
            } else if ( data.get(key) instanceof JSONArray) {
                final JSONArray array = new JSONArray();
                final JSONArray arrayWithObjects = data.getJSONArray(key);
                Iterator iteratorArray = arrayWithObjects.iterator();
                while ( iteratorArray.hasNext() ) {
                    Object objectArray = iteratorArray.next();
                    if ( objectArray instanceof JSONObject ) {
                        array.put(toFormatDataResponse((JSONObject) objectArray));
                    } else if ( objectArray instanceof Date) {
                        array.put(getISODateText(((Date) objectArray).getTime()));
                    } else {
                        array.put(objectArray);
                    }
                }
                data.put(key, array);
            }
            if ( data.get(key) instanceof Date ) {
                data.put(key, getISODateText(((Date) data.get(key)).getTime()));
            }
        }
        return data;
    }
}
