package mx.ejlf.demo.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CustomTimestamp {
    public static String Timestamp () {
        return String.valueOf(Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTimeInMillis());
    }

    public static String utcYYYYMMDD() {
        Calendar calendar =
                Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        SimpleDateFormat sdf_YYYYMMDD =
                new SimpleDateFormat("YYYYMMdd");

        return sdf_YYYYMMDD.format(calendar.getTime());
    }

    public static String utcDDMMYYYY() {
        Calendar calendar =
                Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        SimpleDateFormat sdf_YYYYMMDD =
                new SimpleDateFormat("ddMMYYYY");

        return sdf_YYYYMMDD.format(calendar.getTime());
    }

    public static Date currentDateUTC () {
        return Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTime();
    }

    public static Date dateToLocalFormat(String dateString) throws ParseException {
        dateString = dateString.replace("Mon ", "");
        dateString = dateString.replace("Tue ", "");
        dateString = dateString.replace("Wed ", "");
        dateString = dateString.replace("Thu ", "");
        dateString = dateString.replace("Fri ", "");
        dateString = dateString.replace("Sat ", "");
        dateString = dateString.replace("Sun ", "");

        SimpleDateFormat format = new SimpleDateFormat("MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");

        //return _format.parse(dateString);

        Date d = null;

        try {
            d = format.parse(dateString);
            return d;
        }
        catch(ParseException e) {
            new ParseException("ERROR1 @ TSGenerator - dateToLocalFormat.", e.getErrorOffset());
        }

        try {
            d = format2.parse(dateString);
            return d;
        }
        catch(ParseException e) {
            new ParseException("ERROR2 @ TSGenerator - dateToLocalFormat.", e.getErrorOffset());
        }

        return d;
    }

    public static String format_yyyyMMdd(String dateString) throws ParseException {
        dateString = dateString.replace("Mon ", "");
        dateString = dateString.replace("Tue ", "");
        dateString = dateString.replace("Wed ", "");
        dateString = dateString.replace("Thu ", "");
        dateString = dateString.replace("Fri ", "");
        dateString = dateString.replace("Sat ", "");
        dateString = dateString.replace("Sun ", "");

        SimpleDateFormat _formatINCOME = new SimpleDateFormat("MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat _formatINCOME2 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat _formatOUTCOME = new SimpleDateFormat("yyyy-MM-dd");

        String _d = null;

        try {
            _d = _formatOUTCOME.format(_formatINCOME.parse(dateString.toString()));
            return _d;
        }
        catch(ParseException e) {
            new ParseException("ERROR1 @ TSGenerator - format_yyyyMMdd.", e.getErrorOffset());
        }

        try {
            _d = _formatOUTCOME.format(_formatINCOME2.parse(dateString.toString()));
            return _d;
        }
        catch(ParseException e) {
            new ParseException("ERROR2 @ TSGenerator - format_yyyyMMdd.", e.getErrorOffset());
        }

        return _d;
    }

    public static String getISODateText(long timestamp) {
        Date dateneu = new Date(timestamp);
        Locale mxLocale = new Locale("es", "MX");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", mxLocale);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(dateneu);
    }
}
