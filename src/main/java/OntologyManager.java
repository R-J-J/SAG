import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import java.io.File;

//import com.google.common.base.Optional;

/**
 * Created by milor on 5/30/2016.
 */
public class OntologyManager {

    OWLOntology load(OWLOntologyManager manager) throws OWLOntologyCreationException {
        // in this test, the ontology is loaded from a string
        return manager.loadOntologyFromOntologyDocument(new StringDocumentSource(Constants.TEST_ONTOLOGY));
    }

    public void shouldLoad() throws Exception {
        // Get hold of an ontology manager
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
//        OWLOntology ontology = load(manager);
        // We can always obtain the location where an ontology was loaded from;
        // for this test, though, since the ontology was loaded from a string,
        // this does not return a file
//        IRI documentIRI = manager.getOntologyDocumentIRI(ontology);
        // In cases where a local copy of one of more ontologies is used, an
        // ontology IRI mapper can be used to provide a redirection mechanism.
        // This means that ontologies can be loaded as if they were located on
        // the web. In this example, we simply redirect the loading from
        // an IRI to our local copy above.
        // iri and file here are used as examples
//        IRI iri = ontology.getOntologyID().getOntologyIRI().get();
//        IRI remoteOntology = IRI.create("http://remote.ontology/we/dont/want/to/load");
//        manager.getIRIMappers().add(new SimpleIRIMapper(remoteOntology, iri));
        // Load the ontology as if we were loading it from the web (from its
        // ontology IRI)

        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new StringDocumentSource(Constants.TEST_ONTOLOGY));
//        OWLOntology redirectedOntology = manager.loadOntology(remoteOntology);

        IRI destination = IRI.create(new File("ontologies/ontology_test_koala.xml"));
        manager.saveOntology(ontology, new OWLXMLDocumentFormat(), destination);
    }
}
