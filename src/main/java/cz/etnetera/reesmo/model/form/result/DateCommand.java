package cz.etnetera.reesmo.model.form.result;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class DateCommand {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
