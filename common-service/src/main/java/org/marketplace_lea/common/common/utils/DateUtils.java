package org.marketplace_lea.common.common.utils;

import org.marketplace_lea.common.common.exceptions.ConsoEpargneException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class DateUtils {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");


    public static Date parseDate(String dateString) {
        try {
            if (dateString == null || dateString.isEmpty()) {
                return null;
            }
            return DATE_FORMAT.parse(dateString);
        } catch (ParseException ex) {
            return new Date();
        }
    }


    public static Date parseDeliveryDate(String deliveryDate) {
        try {
            if (deliveryDate == null || deliveryDate.isEmpty()) {
                return null;
            }

            LocalDate localDate = LocalDate.parse(deliveryDate.split("T")[0]);

            if (localDate.isBefore(LocalDate.now())) {
                throw new ConsoEpargneException("Date de livraison invalide !");
            }

            return DATE_FORMAT.parse(deliveryDate);
        } catch (DateTimeParseException | ParseException ex) {
            return new Date();
        }
    }
}
