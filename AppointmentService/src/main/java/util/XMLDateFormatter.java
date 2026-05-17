package util;

import java.time.LocalDateTime;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author Leonardo Flores Leyva - 252390
 */
public class XMLDateFormatter {
    
    public static LocalDateTime toLocalDateTime(XMLGregorianCalendar xmlDateTime){
        LocalDateTime dateTime = LocalDateTime.of(
                xmlDateTime.getYear(),
                xmlDateTime.getMonth(), 
                xmlDateTime.getDay(), 
                xmlDateTime.getHour(), 
                xmlDateTime.getMinute(), 
                xmlDateTime.getSecond()
        );
        return dateTime;
    }
}
