package com.stelmashok.logistics.util.tag;

import com.stelmashok.logistics.util.locale.ResourceBundleManager;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

import java.io.IOException;
import java.util.Calendar;
import java.util.ResourceBundle;

public class GreetingTagHandler extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        out.println(getGreetings());
    }

    private String getGreetings() {
        String selectedLocalization = (String) getJspContext().findAttribute(LOCALIZATION);
        ResourceBundleManager resourceBundleManager = ResourceBundleManager.getInstance();
        ResourceBundle resourceBundle = resourceBundleManager.getResourceBundle(selectedLocalization);
        String greeting;
        int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hourOfDay >= 1 && hourOfDay <= 12) {
            greeting = resourceBundle.getString(MESSAGE_GOOD_MORNING);
        } else if (hourOfDay >= 12 && hourOfDay <= 16) {
            greeting = resourceBundle.getString(MESSAGE_GOOD_AFTERNOON);
        } else if (hourOfDay >= 16 && hourOfDay <= 21) {
            greeting = resourceBundle.getString(MESSAGE_GOOD_EVENING);
        } else if (hourOfDay >= 21) {
            greeting = resourceBundle.getString(MESSAGE_GOOD_NIGHT);
        } else {
            greeting = resourceBundle.getString(MESSAGE_HELLO);
        }
        return greeting;
    }
}
