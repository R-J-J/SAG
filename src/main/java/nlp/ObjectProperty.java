package nlp;

/**
 * Created by Arjan on 05.06.2016.
 */
public class ObjectProperty {

    public final String object;
    public final String propertyName;
    public final String propertyValue;

    public ObjectProperty(String object, String propertyName, String propertyValue) {
        this.object = object;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }
}
