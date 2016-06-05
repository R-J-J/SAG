package nlp;

/**
 * Created by Arjan on 05.06.2016.
 */
public class ObjectSubclass {

    public final String object;
    public final String subclass;

    public ObjectSubclass(String object, String subclass) {
        this.object = object;
        this.subclass = subclass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectSubclass that = (ObjectSubclass) o;

        if (object != null ? !object.equals(that.object) : that.object != null) return false;
        return subclass != null ? subclass.equals(that.subclass) : that.subclass == null;

    }

    @Override
    public int hashCode() {
        int result = object != null ? object.hashCode() : 0;
        result = 31 * result + (subclass != null ? subclass.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ObjectSubclass{" +
                "object='" + object + '\'' +
                ", subclass='" + subclass + '\'' +
                '}';
    }
}
