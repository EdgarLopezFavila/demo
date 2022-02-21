package mx.ejlf.demo.endpoint;

import mx.ejlf.demo.db.DataBaseHelper;
import mx.ejlf.demo.utils.*;
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
import java.util.List;
import java.util.logging.Logger;

import static mx.ejlf.demo.helper.RegisterConstants.*;

@Path("/register")
@OpenAPIDefinition(
        tags = {@Tag(name="CustomerRegister", description="Servicio para registrar un customer")},
        info = @Info(title="CustomerRegister", version="1.0", description = "Servicio para registrar un customer")
)
public class RegisterCustomer {

    private static final Logger log = Logger.getLogger(RegisterCustomer.class.getName());

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
    @Operation(summary = "Microservice used to save customer info.", description = "Microservice used to save customer info.")
    @RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.OBJECT), example = R_REQUEST))
    public Response service(String payload, @Context HttpHeaders headers) {
        try {
            //1. Validamos los datos de entrada
            this.validateData(payload);

            //2. Insertamos los datos
            JSONObject data = new JSONObject(payload).getJSONObject("data");
            this.insertCustomer(data);

            //3. Regresamos la respuesta
            return CustomResponse.ok(new JSONObject().put("result","OK"));
        } catch (CustomException ce) {
            return CustomResponse.bad(ce.getCode(), ce.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            log.warning(ex.getMessage());
            return CustomResponse.bad(REGISTER_CODE + R000, DR000);
        }
    }

    /**
     * Metodo para validar los datos de entrada
     *
     * @param payload
     * @throws CustomException
     */
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
        Validator.objectValidator(Arrays.asList("f_name", "l_name", "email","cp", "street","phone_number"), data);

        if (!Validator.emailValidator(data.getString("email"))) {
            throw new CustomException("data::email_" + DR004, REGISTER_CODE + R004);
        }

        if (!Validator.phoneValidator(data.getString("phone_number"))) {
            throw new CustomException("data::phone_number_" + DR004, REGISTER_CODE + R004);
        }

    }

    /**
     * Metodo para insertar los datos
     * @param data
     */
    private void insertCustomer (JSONObject data) {
        Document docInsert = new Document();
        Document general = new Document();
        general.put("fname", data.getString("f_name"));
        general.put("lname", data.getString("l_name"));
        general.put("email", data.getString("email"));

        Document address = new Document();
        address.put("cp", data.getString("cp"));
        address.put("street", data.getString("street"));

        Document contact = new Document();
        contact.put("phone_number", data.getString("phone_number"));

        docInsert.put("general", general);
        docInsert.put("address", address);
        docInsert.put("contact", contact);
        docInsert.put("cdate", CustomTimestamp.currentDateUTC());
        docInsert.put("uuid", CustomUuid.uuid());



        dbHelper.insert("os_customer", docInsert);
    }
}
