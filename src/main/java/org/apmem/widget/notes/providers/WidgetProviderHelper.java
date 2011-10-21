package org.apmem.widget.notes.providers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Artem
 * Date: 17.10.11
 * Time: 18:05
 * To change this template use File | Settings | File Templates.
 */
public class WidgetProviderHelper {
    public static List<Class> getAllProviders(){
        List<Class> result = new ArrayList<Class>();
        result.add(WidgetProvider_1_1.class);
        result.add(WidgetProvider_1_2.class);
        result.add(WidgetProvider_1_3.class);
        result.add(WidgetProvider_1_4.class);
        result.add(WidgetProvider_2_1.class);
        result.add(WidgetProvider_2_2.class);
        result.add(WidgetProvider_2_3.class);
        result.add(WidgetProvider_2_4.class);
        result.add(WidgetProvider_3_1.class);
        result.add(WidgetProvider_3_2.class);
        result.add(WidgetProvider_3_3.class);
        result.add(WidgetProvider_3_4.class);
        result.add(WidgetProvider_4_1.class);
        result.add(WidgetProvider_4_2.class);
        result.add(WidgetProvider_4_3.class);
        result.add(WidgetProvider_4_4.class);
        return result;
    }
}
