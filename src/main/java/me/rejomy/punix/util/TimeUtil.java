package me.rejomy.punix.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    public static String getCurrentDate() {
        // Получаем текущую дату
        Date currentDate = new Date();

        // Создаем объект SimpleDateFormat с нужным форматом
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        // Преобразуем дату в строку с нужным форматом
        String formattedDate = dateFormat.format(currentDate);

        return formattedDate;
    }
    public static long getTimeToMillis(String timeObject) {
        char format = timeObject.substring(timeObject.length() - 1).charAt(0);
        int time = Integer.parseInt(timeObject.replaceAll("[^0-9]+", ""));

        // Умножаем на 1000, потому-что нам нужно его вернуть в миллисекундах.
        time *= 1000;

        if(format == 's') {
            return time;
        }

        // Мы имеем время и если у него преписка s - секунды, то возвращаем его выше.
        // Так-как приписки ниже секунд нет - значит это минуты или выше минут.
        time *= 60;

        if(format == 'm') {
            return time;
        }

        time *= 60;

        if(format == 'h') {
            return time;
        }

        time *= 24;

        if(format == 'd') {
            return time;
        }

        return -1;
    }
}
