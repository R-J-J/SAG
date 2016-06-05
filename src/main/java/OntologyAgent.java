import jade.lang.acl.ACLMessage;
import org.semanticweb.owlapi.vocab.OWLFacet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
* Interface usage:
*   create new ontology: ONT_OPERATION = ONT_NEW, ONT_BASE = <ontology base>, ONT_OBJECT = <output file>
*   add class: ONT_OPERATION = ONT_ADD_CLASS, ONT_OBJECT = <name>
*   add subclass: ONT_OPERATION = ONT_ADD_SUBCLASS, ONT_OBJECT = <parent name>, ONT_RELATED_OBJECT = <child name>
*   add assertion: ONT_OPERATION = ONT_ADD_ASSERTION, ONT_OBJECT = <name>
        ONT_TYPE = ONT_TYPE_OBJECT_ASSERTION | ONT_TYPE_DATA_ASSERTION | ONT_TYPE_CLASS_ASSERTION
        ONT_RELATED_OBJECT = <target>       (only if object or class assertion)
        ONT_PROPERTY = <property>           (only if object or data assertion)
        ONT_VALUE_TYPE = ONT_TYPE_INTEGER | ONT_TYPE_STRING     (only if data assertion)
* */
public class OntologyAgent extends AbstractAgent {

    OntologyManager ontologyManager;

    @Override
    protected void setup() {
        super.setup();

        ontologyManager = new OntologyManager();

        test();
    }

    @Override
    protected void addBehaviours() {
        addBehaviour(new HttpGetBehaviour());
    }

    @Override
    protected List<ServiceName> servicesToRegister()
    {
        return new ArrayList<>(Arrays.asList(Constants.ONTOLOGY_SERVICE));
    }

    private class HttpGetBehaviour extends AbstractMessageProcessingBehaviour {

        @Override
        protected void processMessage(ACLMessage msg) {

            String operation = msg.getUserDefinedParameter(Constants.ONT_OPERATION);

            if (operation.equals(Constants.ONT_NEW)) {

                String base = msg.getUserDefinedParameter(Constants.ONT_BASE);
                String file = msg.getUserDefinedParameter(Constants.ONT_OBJECT);
                ontologyManager.save();
                ontologyManager.createNewOntology(base, file);
            }
            else if (operation.equals(Constants.ONT_ADD_CLASS)) {

                String newClass = msg.getUserDefinedParameter(Constants.ONT_OBJECT);
                ontologyManager.addClass(newClass);
            }
            else if (operation.equals(Constants.ONT_ADD_SUBCLASS)) {
                String parentClass = msg.getUserDefinedParameter(Constants.ONT_OBJECT);
                String derivedClass = msg.getUserDefinedParameter(Constants.ONT_RELATED_OBJECT);

                ontologyManager.addSubclass(derivedClass, parentClass);
            }
            else if (operation.equals(Constants.ONT_ADD_ASSERTION)) {
                String type = msg.getUserDefinedParameter(Constants.ONT_TYPE);
                String object = msg.getUserDefinedParameter(Constants.ONT_OBJECT);

                if (type.equals(Constants.ONT_TYPE_CLASS_ASSERTION)) {
                    String target = msg.getUserDefinedParameter(Constants.ONT_RELATED_OBJECT);

                    ontologyManager.addClassAssertion(object, target);
                }
                else if (type.equals(Constants.ONT_TYPE_OBJECT_ASSERTION)) {
                    String property = msg.getUserDefinedParameter(Constants.ONT_PROPERTY);
                    String target = msg.getUserDefinedParameter(Constants.ONT_RELATED_OBJECT);

                    ontologyManager.addObjectPropertyAssertion(object, property, target);
                }
                else if (type.equals(Constants.ONT_TYPE_DATA_ASSERTION)) {
                    String property = msg.getUserDefinedParameter(Constants.ONT_PROPERTY);
                    String value = msg.getUserDefinedParameter(Constants.ONT_PROPERTY);
                    String valueType = msg.getUserDefinedParameter(Constants.ONT_VALUE_TYPE);

                    if (valueType.equals(Constants.ONT_TYPE_STRING)) {
                        ontologyManager.addDataPropertyAssertion(object, property, value);
                    }
                    else if (valueType.equals(Constants.ONT_TYPE_INTEGER)) {
                        ontologyManager.addDataPropertyAssertion(object, property, Integer.parseInt(value));
                    }
                }
            }
        }
    }

    private void test() {
        ontologyManager.createNewOntology("http://www.panda.pl", "ontologies/panda_1.xml");
        ontologyManager.addClass("Panda");
        ontologyManager.addClass("Koala");
        ontologyManager.addClass("Panda Wielka");
        ontologyManager.addSubclass("Panda Wielka", "Panda");
        ontologyManager.addClassAssertion("Pawel", "Panda");
        ontologyManager.addClassAssertion("Hubert", "Panda Wielka");
        ontologyManager.addObjectPropertyAssertion("Pawel", "isBrotherOf", "Hubert");
        ontologyManager.addDataPropertyAssertion("Pawel", "hasAge", 29);
        ontologyManager.addDataPropertyAssertion("Pawel", "hasNickname", "Pandeiros");
        ontologyManager.addPropertyDataRange("hasAge", 20, OWLFacet.MAX_INCLUSIVE);
        ontologyManager.save();
    }
}
