package bl;


import java.time.*;
import java.util.Date;

public class DateComponent {
    public static LocalDate utilDateToLocalDate(Date date){

        ZoneId defaultZoneId = ZoneId.systemDefault();

        Instant instant = date.toInstant();

        LocalDate localDate = instant.atZone(defaultZoneId).toLocalDate();

        return localDate;
    }
}
