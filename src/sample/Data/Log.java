package sample.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    String id ="";
    Date date = new Date();
    String userName = "";
    Levels level = Levels.ERROR;
    String description = "";
    public static enum Levels{
        ERROR,
        WARN,
        INFO
    }
    public Log(Date date, String userName, Levels level, String description){
        this.date = date;
        this.userName = userName;
        this.level = level;
        this.description = description;
    }
    public String getId() {
        return id;
    }

    public String getDate() {
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return formatForDateNow.format(date);
    }

    public String getLevel() {
        return level.name();
    }

    public String getDescription() {
        return description;
    }

    public String getUserName() {
        return userName;
    }

}
