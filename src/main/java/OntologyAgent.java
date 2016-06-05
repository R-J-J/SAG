import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            else if (operation.equals(Constants.ONT_ADD_PROPERTY)) {
                String parentClass = msg.getUserDefinedParameter(Constants.ONT_OBJECT);
                String derivedClass = msg.getUserDefinedParameter(Constants.ONT_RELATED_OBJECT);

                ontologyManager.addSubclass(derivedClass, parentClass);
            }
        }
    }

    private void test() {
        ontologyManager.createNewOntology("http://www.panda.pl", "ontologies/panda_1.xml");
        ontologyManager.addClass("Panda");
        ontologyManager.addSubclass("Panda Wielka", "Panda");
        ontologyManager.save();
    }
}
