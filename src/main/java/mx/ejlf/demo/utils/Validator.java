package mx.ejlf.demo.utils;

import org.json.JSONObject;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static mx.ejlf.demo.helper.RegisterConstants.*;

public class Validator {

    /**
     * Regular Expressions
     */
    private static final String EMAIL_FORMAT = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_FORMAT = "^\\+(?:[0-9] ?){6,14}[0-9]$";

    /**
     * Valida la estructura del email (correo electronico)
     * @param email
     * @return String email o còdigo formato incorrecto
     */
    public static boolean emailValidator(String email){
        Pattern pattern = Pattern.compile(EMAIL_FORMAT);
        Matcher mather = pattern.matcher(email);
        return mather.matches();
    }

    /**
     * Valida la estructura del telefono (UIT-T E.164)
     * @param phone
     * @return String phone o còdigo formato incorrecto
     */
    public static boolean phoneValidator( String phone) {
        Pattern pattern = Pattern.compile(PHONE_FORMAT);
        Matcher mather = pattern.matcher(phone);
        return mather.matches();
    }

    /**
     * Metodo para validar generico
     * @param keys
     * @param data
     * @throws CustomException
     */
    public static void objectValidator (List<String> keys, JSONObject data) throws CustomException{
        for (String key : keys) {
            if (!data.has(key)) {
                throw new CustomException("data::" + key + "_" + DR001, REGISTER_CODE + R001);
            } else if (!(data.get(key) instanceof String)) {
                throw new CustomException("data::" + key + "_" + DR002, REGISTER_CODE + R002);
            } else if (data.getString(key).isEmpty()) {
                throw new CustomException("data::" + key + "_" + DR003, REGISTER_CODE + R003);
            }
        }
    }
}
