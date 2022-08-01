package com.stelmashok.logistics.util.tag;

import com.stelmashok.logistics.util.locale.ResourceBundleManager;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class TimeTagHandler  extends SimpleTagSupport {

    private static final Logger logger = LogManager.getLogger();
    private static final String SPACE = " ";

    private String itemTime;
    private String userTime;

    public void setItemTime(String itemTime) {
        this.itemTime = itemTime;
    }

    public void setUserTime(String userTime) {
        this.userTime = userTime;
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        if (userTime != null) {
            LocalDateTime dateTime = LocalDateTime.parse(userTime);
            out.println(calculateDateDifferenceForUser(dateTime, LocalDateTime.now()));
        } else if (itemTime != null) {
            LocalDateTime dateTime = LocalDateTime.parse(itemTime);
            out.println(calculateDateDifferenceForItem(dateTime, LocalDateTime.now()));
        } else {
            out.println(SPACE);
            logger.error("failed to process date difference");
        }
    }

    private String calculateDateDifferenceForUser(LocalDateTime startDate, LocalDateTime endDate) {
        String selectedLocalization = (String) getJspContext().findAttribute(LOCALIZATION);
        ResourceBundleManager resourceBundleManager = ResourceBundleManager.getInstance();
        ResourceBundle resourceBundle = resourceBundleManager.getResourceBundle(selectedLocalization);
        StringBuilder stringBuilder = new StringBuilder();
        long elapsedDays = ChronoUnit.DAYS.between(startDate, endDate);
        startDate = startDate.plusDays(elapsedDays);
        long elapsedHours = ChronoUnit.HOURS.between(startDate, endDate);
        startDate = startDate.plusHours(elapsedHours);
        long elapsedMinutes = ChronoUnit.MINUTES.between(startDate, endDate);
        startDate = startDate.plusMinutes(elapsedMinutes);
        long elapsedSeconds = ChronoUnit.SECONDS.between(startDate, endDate);
        stringBuilder.append(elapsedDays)
                .append(SPACE)
                .append(resourceBundle.getString(MESSAGE_DAYS))
                .append(SPACE)
                .append(elapsedHours)
                .append(SPACE)
                .append(resourceBundle.getString(MESSAGE_HOURS))
                .append(SPACE)
                .append(elapsedMinutes)
                .append(SPACE)
                .append(resourceBundle.getString(MESSAGE_MINUTES))
                .append(SPACE)
                .append(elapsedSeconds)
                .append(SPACE)
                .append(resourceBundle.getString(MESSAGE_SECONDS));
        return stringBuilder.toString();
    }

    private String calculateDateDifferenceForItem(LocalDateTime startDate, LocalDateTime endDate) {
        String selectedLocalization = (String) getJspContext().findAttribute(LOCALIZATION);
        ResourceBundleManager resourceBundleManager = ResourceBundleManager.getInstance();
        ResourceBundle resourceBundle = resourceBundleManager.getResourceBundle(selectedLocalization);
        StringBuilder stringBuilder = new StringBuilder();
        long elapsedDays = ChronoUnit.DAYS.between(startDate, endDate);
        startDate = startDate.plusDays(elapsedDays);
        long elapsedHours = ChronoUnit.HOURS.between(startDate, endDate);
        startDate = startDate.plusHours(elapsedHours);
        long elapsedMinutes = ChronoUnit.MINUTES.between(startDate, endDate);
        startDate = startDate.plusMinutes(elapsedMinutes);
        long elapsedSeconds = ChronoUnit.SECONDS.between(startDate, endDate);
        if (elapsedDays == 0 && elapsedHours == 0 && elapsedMinutes == 0) {
            stringBuilder.append(elapsedSeconds)
                    .append(SPACE)
                    .append(resourceBundle.getString(MESSAGE_SECONDS))
                    .append(SPACE)
                    .append(resourceBundle.getString(MESSAGE_AGO));
        } else if (elapsedDays == 0 && elapsedHours == 0) {
            stringBuilder.append(elapsedMinutes)
                    .append(SPACE)
                    .append(resourceBundle.getString(MESSAGE_MINUTES))
                    .append(SPACE)
                    .append(resourceBundle.getString(MESSAGE_AGO));
        } else if (elapsedDays == 0) {
            stringBuilder.append(elapsedHours)
                    .append(SPACE)
                    .append(resourceBundle.getString(MESSAGE_HOURS))
                    .append(SPACE)
                    .append(resourceBundle.getString(MESSAGE_AGO));
        } else {
            stringBuilder.append(elapsedDays)
                    .append(SPACE)
                    .append(resourceBundle.getString(MESSAGE_DAYS))
                    .append(SPACE)
                    .append(resourceBundle.getString(MESSAGE_AGO));
        }
        return stringBuilder.toString();
    }
}
