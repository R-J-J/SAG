import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import java.io.File;

public class OntologyManager {

    private String base;
    private OWLOntologyManager manager;
    IRI ontologyIRI;
    IRI documentIRI;
    OWLOntology ontology;
    OWLDataFactory factory;

    public OntologyManager() {
        manager = OWLManager.createOWLOntologyManager();

    }

    public void createNewOntology(String base, String fileName) {
        this.base = base;

        try {
            ontologyIRI = IRI.create(base);
            ontology = manager.createOntology(ontologyIRI);
            factory = manager.getOWLDataFactory();

            // Create the document IRI for our ontology
            documentIRI = IRI.create("file:/" + new File(fileName).getCanonicalPath().replaceAll("\\\\", "/"));
            // Set up a mapping, which maps the ontology to the document IRI
            SimpleIRIMapper mapper = new SimpleIRIMapper(ontologyIRI, documentIRI);
            manager.getIRIMappers().add(mapper);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addClass(String newClass) {
        newClass = processName(newClass);

        PrefixManager pm = new DefaultPrefixManager(null, null, base + "#");
        // Now we use the prefix manager and just specify an abbreviated IRI;
        OWLClass owlClass = factory.getOWLClass(":" + newClass, pm);

        // We can add a declaration axiom to the ontology, that essentially adds
        // the class to the signature of our ontology.
        OWLDeclarationAxiom declarationAxiom = factory.getOWLDeclarationAxiom(owlClass);
        manager.addAxiom(ontology, declarationAxiom);
    }

    public void addSubclass(String derived, String parent) {
        derived = processName(derived);
        parent = processName(parent);
        
        // Get hold of references to derived and parent classes. Note that the ontology
        // does not contain them, we simply get references to
        // objects from a data factory that REPRESENT both classes.
        OWLClass classDerived = factory.getOWLClass(IRI.create(ontologyIRI + "#" + derived));
        OWLClass classParent = factory.getOWLClass(IRI.create(ontologyIRI + "#" + parent));

        // Now create the axiom
        OWLAxiom axiom = factory.getOWLSubClassOfAxiom(classDerived, classParent);

        // We now add the axiom to the ontology, so that the ontology states
        // that A is a subclass of B. To do this we create an AddAxiom change
        // object. At this stage neither classes A or B, or the axiom are
        // contained in the ontology. We have to add the axiom to the ontology.
        AddAxiom addAxiom = new AddAxiom(ontology, axiom);

        // We now use the manager to apply the change
        manager.applyChange(addAxiom);
    }

    public void save() {
        if (documentIRI != null) {
//            IRI destination = IRI.create(new File(base));

            try {
                manager.saveOntology(ontology, new OWLXMLDocumentFormat(), documentIRI);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String processName(String name) {
        return name.replaceAll(" ", "_");
    }

    public void testKoala() throws Exception {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new StringDocumentSource(Constants.TEST_ONTOLOGY));
        IRI destination = IRI.create(new File("ontologies/ontology_test_koala.xml"));
        manager.saveOntology(ontology, destination);
    }
}
