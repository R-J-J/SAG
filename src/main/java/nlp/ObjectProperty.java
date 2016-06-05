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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectProperty that = (ObjectProperty) o;

        if (object != null ? !object.equals(that.object) : that.object != null) return false;
        if (propertyName != null ? !propertyName.equals(that.propertyName) : that.propertyName != null) return false;
        return propertyValue != null ? propertyValue.equals(that.propertyValue) : that.propertyValue == null;

    }

    @Override
    public int hashCode() {
        int result = object != null ? object.hashCode() : 0;
        result = 31 * result + (propertyName != null ? propertyName.hashCode() : 0);
        result = 31 * result + (propertyValue != null ? propertyValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ObjectProperty{" +
                "object='" + object + '\'' +
                ", propertyName='" + propertyName + '\'' +
                ", propertyValue='" + propertyValue + '\'' +
                '}';
    }
}
