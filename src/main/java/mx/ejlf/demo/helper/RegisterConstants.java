package mx.ejlf.demo.helper;

public class RegisterConstants {

    public static final String REGISTER_CODE = "1000.1000.";

    /** Constantes de codigos Service 01 */
    public static final String R000 = "000"; public static final String DR000 = "service_error";
    public static final String R001 = "001"; public static final String DR001 = "object_not_found";
    public static final String R002 = "002"; public static final String DR002 = "type_object_incorrect";
    public static final String R003 = "003"; public static final String DR003 = "object_can_not_be_empty";
    public static final String R004 = "005"; public static final String DR004 = "object_format_incorrect";
    public static final String R005 = "005"; public static final String DR005 = "object_not_found_in_db";

    public static final String R_SUCCESS_RESPONSE = "{\n" +
            "  \"data\": {\n" +
            "    \"message\": \"Hello!\"\n" +
            "  },\n" +
            "  \"response\": \"OK\"\n" +
            "}";

    public static final String R_REQUEST = "{  \n" +
            "  \"data\": {\n" +
            "    \"f_name\":\"\",\n" +
            "    \"l_name\":\"\",    \n" +
            "    \"email\":\"\",\n" +
            "    \"cp\":\"\",\n" +
            "    \"street\":\"\",\n" +
            "    \"phone_number\":\"\"\n" +
            "  }\n" +
            "}";

    public static final String R_EMPTY_OBJECT_ERROR = "{\n" +
            "  \"data\": {\n" +
            "    \"exception\": {\n" +
            "      \"code\": \"1000.1000.001\",\n" +
            "      \"detail\": \"data::fname_object_not_found\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"response\": \"BAD\"\n" +
            "}";
}
