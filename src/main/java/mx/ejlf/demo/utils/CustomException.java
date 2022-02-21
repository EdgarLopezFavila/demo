package mx.ejlf.demo.utils;

public class CustomException extends Exception{
    private static final long serialVersion = 772995951268411L;

    private String message = null;
    private String code	  = null;

    public CustomException(String message, String code){
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage(){
        return this.message;
    }

    @Override
    public String getLocalizedMessage() {
        return "MicrodotException:: " + this.message;
    }

    public String getCode(){
        return this.code;
    }
}
