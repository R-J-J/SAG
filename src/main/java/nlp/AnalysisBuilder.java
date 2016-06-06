package nlp;

/**
 * Created by Arjan on 06.06.2016.
 */
public interface AnalysisBuilder {

    void addObjectProperty(Property property);

    void addDataProperty(Property property);

    void addSubclass(Subclass subclass);
}
