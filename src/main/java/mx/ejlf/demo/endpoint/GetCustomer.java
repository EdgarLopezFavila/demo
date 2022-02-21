package mx.ejlf.demo.endpoint;

import mx.ejlf.demo.db.DataBaseHelper;
import mx.ejlf.demo.utils.CustomException;
import mx.ejlf.demo.utils.CustomResponse;
import mx.ejlf.demo.utils.Validator;
import org.bson.Document;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Arrays;
import java.util.logging.Logger;

import static mx.ejlf.demo.helper.RegisterConstants.*;

@Path("/get")
@OpenAPIDefinition(
        tags = {@Tag(name="CustomerGet", description="Servicio para obtener un customer")},
        info = @Info(title="CustomerGet", version="1.0", description = "Servicio para obtener un customer")
)
public class GetCustomer {
    private static final Logger log = Logger.getLogger(GetCustomer.class.getName());

    @Inject
    DataBaseHelper dbHelper;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(
            value = {
                    @APIResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", example = R_SUCCESS_RESPONSE)),
                    @APIResponse(responseCode = "200-A", description = "Empty Object", content = @Content(mediaType = "application/json", example = R_EMPTY_OBJECT_ERROR)),
            }
    )
    @Operation(summary = "Microservice used to get customer info.", description = "Microservice used to get customer info.")
    @RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.OBJECT), example = R_REQUEST))
    public Response service(String payload, @Context HttpHeaders headers) {
        try {
            //1. Validamos los datos de entrada
            this.validateData(payload);

            //2. Obtenemos los datos
            JSONObject data = new JSONObject(payload).getJSONObject("data");
            JSONObject customerData = this.getCustomer(data);

            //3. Regresamos la respuesta
            return CustomResponse.ok(customerData);
        } catch (CustomException ce) {
            return CustomResponse.bad(ce.getCode(), ce.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            log.warning(ex.getMessage());
            return CustomResponse.bad(REGISTER_CODE + R000, DR000);
        }
    }

    private void validateData(String payload) throws CustomException {
        JSONObject dataObject = null;
        try {
            dataObject = new JSONObject(payload);
        } catch (Exception ex) {
            throw new CustomException(DR000, REGISTER_CODE + R000);
        }

        if (!dataObject.has("data")) {
            throw new CustomException("data_" + DR001, REGISTER_CODE + R001);
        } else if (!(dataObject.get("data") instanceof JSONObject)) {
            throw new CustomException("data_" + DR002, REGISTER_CODE + R002);
        }

        JSONObject data = dataObject.getJSONObject("data");
        Validator.objectValidator(Arrays.asList("uuid"), data);
    }

    /**
     * Metodo para obtener un customer de bd
     * @param data
     * @return
     * @throws CustomException
     */
    private JSONObject getCustomer(JSONObject data) throws CustomException{
        Document docQuery = new Document();
        docQuery.put("uuid", data.getString("uuid"));

        JSONObject object = dbHelper.findByDocument("os_customer", docQuery);
        if (object != null) {
            return object;
        }

        throw new CustomException("data::customer_"+DR005, REGISTER_CODE + R005);
    }
}
