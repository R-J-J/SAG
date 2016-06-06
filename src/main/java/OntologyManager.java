import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWLFacet;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class OntologyManager {

    private String base;
    private OWLOntologyManager manager;
    private IRI ontologyIRI;
    private IRI documentIRI;
    private OWLOntology ontology;
    private OWLDataFactory factory;
    private PrefixManager prefixManager;

    OntologyManager() {
        manager = OWLManager.createOWLOntologyManager();


    }

    void createNewOntology(String base, String fileName) {
        this.base = base;

        try {
            ontologyIRI = IRI.create(base);
            ontology = manager.createOntology(ontologyIRI);
            factory = manager.getOWLDataFactory();
            prefixManager = new DefaultPrefixManager(null, null, base + "#");

            // Create the document IRI for our ontology
            fileName = new File(fileName).getCanonicalPath().replaceAll("\\\\", "/");
            String prefix = "file:";

            // Windows path fixing
            if (!fileName.startsWith("/"))
                prefix += "/";

            documentIRI = IRI.create(prefix + fileName);

            // Set up a mapping, which maps the ontology to the document IRI
//            SimpleIRIMapper mapper = new SimpleIRIMapper(ontologyIRI, documentIRI);
//            manager.getIRIMappers().add(mapper);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void addClass(String newClass) {
        newClass = processName(newClass);

        // Now we use the prefix manager and just specify an abbreviated IRI;
        OWLClass owlClass = factory.getOWLClass(":" + newClass, prefixManager);

        // We can add a declaration axiom to the ontology, that essentially adds
        // the class to the signature of our ontology.
        OWLDeclarationAxiom declarationAxiom = factory.getOWLDeclarationAxiom(owlClass);
        manager.addAxiom(ontology, declarationAxiom);
    }

    // i.e. <Adult, Person>
    void addSubclass(String derived, String parent) {
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

    // i.e. <John, hasWife, Mary>
    void addObjectPropertyAssertion(String object, String property, String target) {
        object = processName(object);
        property = processName(property);
        target = processName(target);

        // Let's specify the <object, property, target>. Get hold of the necessary
        // individuals and object property.
        OWLNamedIndividual owlObject = factory.getOWLNamedIndividual(":" + object, prefixManager);
        OWLNamedIndividual owlTarget = factory.getOWLNamedIndividual(":" + target, prefixManager);
        OWLObjectProperty owlProperty = factory.getOWLObjectProperty(":" + property, prefixManager);

        // To specify that object is related to target via the property
        // we create an object property assertion and add it to the ontology.
        OWLObjectPropertyAssertionAxiom propertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(owlProperty, owlObject, owlTarget);
        manager.addAxiom(ontology, propertyAssertion);
    }

    // i.e. <John, hasNickname, Big John>
    void addDataPropertyAssertion(String object, String property, String value) {
        object = processName(object);
        property = processName(property);

        // Let's specify the <object, property, target>. Get hold of the necessary
        // individuals and object property.
        OWLNamedIndividual owlObject = factory.getOWLNamedIndividual(":" + object, prefixManager);
        OWLDataProperty owlProperty = factory.getOWLDataProperty(":" + property, prefixManager);
        OWLLiteral owlLiteral = factory.getOWLLiteral(value);

        // To specify that object is related to target via the property
        // we create an object property assertion and add it to the ontology.
        OWLDataPropertyAssertionAxiom propertyAxiom = factory.getOWLDataPropertyAssertionAxiom(owlProperty, owlObject, owlLiteral);
        manager.addAxiom(ontology, propertyAxiom);
    }

    // i.e. <John, hasAge, 21>
    void addDataPropertyAssertion(String object, String property, int value) {
        object = processName(object);
        property = processName(property);

        // Let's specify the <object, property, target>. Get hold of the necessary
        // individuals and object property.
        OWLNamedIndividual owlObject = factory.getOWLNamedIndividual(":" + object, prefixManager);
        OWLDataProperty owlProperty = factory.getOWLDataProperty(":" + property, prefixManager);
        OWLLiteral owlLiteral = factory.getOWLLiteral(value);

        // To specify that object is related to target via the property
        // we create an object property assertion and add it to the ontology.
        OWLDataPropertyAssertionAxiom propertyAxiom = factory.getOWLDataPropertyAssertionAxiom(owlProperty, owlObject, owlLiteral);
        manager.addAxiom(ontology, propertyAxiom);
    }

    // i.e. <John, Person>
    void addClassAssertion(String object, String strClass) {
        object = processName(object);
        strClass = processName(strClass);

        // Get the reference to the OWL object.
        OWLNamedIndividual owlClass = factory.getOWLNamedIndividual(":" + object, prefixManager);

        // Get the reference to the OWL class.
        OWLClass owlObject = factory.getOWLClass(":" + strClass, prefixManager);

        // Now create a ClassAssertion to specify that owlObject is an instance of owlClass.
        OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlObject, owlClass);

        // Add the class assertion
        manager.addAxiom(ontology, classAssertion);
    }

    // i.e. <hasAge, ONT_TYPE_INTEGER>
    void addPropertyDataRange(String property, String type) {
        OWLDatatype dataType;

        if (type.equals(Constants.ONT_TYPE_INTEGER))
            dataType = factory.getIntegerOWLDatatype();
        else
            return;

        // We could use this datatype in restriction.
        OWLDataProperty owlProperty = factory.getOWLDataProperty(":" + property, prefixManager);
        OWLDataPropertyRangeAxiom rangeAxiom = factory.getOWLDataPropertyRangeAxiom(owlProperty, dataType);

        // Add the range axiom to our ontology
        manager.addAxiom(ontology, rangeAxiom);
    }

    // i.e. <hasAge, 18, MIN_INCLUSIVE>
    void addPropertyDataRange(String property, int value, OWLFacet facet) {

        // For common data types there are some convenience methods of
        // OWLDataFactory.
        OWLDatatype integerDatatype = factory.getIntegerOWLDatatype();

        // Custom data ranges can be built up from these basic datatypes.
        // It is possible to restrict a datatype using facets from XML
        // Schema Datatypes.
        OWLLiteral literal = factory.getOWLLiteral(value);

        // Now create the restriction. The OWLFacet enum provides an enumeration
        // of the various facets that can be used.
        OWLDatatypeRestriction restriction = factory.getOWLDatatypeRestriction(integerDatatype, facet, literal);

        // We could use this datatype in restriction, as the range of data
        // properties etc.
        OWLDataProperty owlProperty = factory.getOWLDataProperty(":" + property, prefixManager);
        OWLDataPropertyRangeAxiom rangeAxiom = factory.getOWLDataPropertyRangeAxiom(owlProperty, restriction);

        // Add the range axiom to our ontology
        manager.addAxiom(ontology, rangeAxiom);
    }

    // i.e. <hasChild, Person>
    void addPropertyDomain(String property, String domain) {
        property = processName(property);
        domain = processName(domain);

        OWLObjectProperty owlProperty = factory.getOWLObjectProperty(":" + property, prefixManager);

        // Get the reference to the OWL domain class.
        OWLClass owlDomain = factory.getOWLClass(":" + domain, prefixManager);

        // To specify that object is related to target via the property
        // we create an object property assertion and add it to the ontology.
        OWLObjectPropertyDomainAxiom domainAssertion = factory.getOWLObjectPropertyDomainAxiom(owlProperty, owlDomain);
        manager.addAxiom(ontology, domainAssertion);

    }

    // i.e. <hasSon, hasChild>
    void addSubObjectProperty(String subProperty, String property) {
        property = processName(property);
        subProperty = processName(subProperty);

        OWLObjectProperty owlProperty = factory.getOWLObjectProperty(":" + property, prefixManager);
        OWLObjectProperty owlSubProperty = factory.getOWLObjectProperty(":" + subProperty, prefixManager);

        OWLSubObjectPropertyOfAxiom subPropertyAxiom = factory.getOWLSubObjectPropertyOfAxiom(owlSubProperty, owlProperty);

        // Add the axiom
        manager.addAxiom(ontology, subPropertyAxiom);
    }

    // i.e. <hasWife, ONT_TYPE_FUNCTIONAL | ONT_TYPE_INVERSE_FUNCTIONAL | ONT_TYPE_IRREFLEXIVE | ONT_TYPE_ASSYMETRIC>
    void setPropertyType(String property, int type) {
        if (type == 0)
            return;

        property = processName(property);
        OWLObjectProperty owlProperty = factory.getOWLObjectProperty(":" + property, prefixManager);

        Set<OWLAxiom> propertyAxioms = new HashSet<>();

        // Add all of the axioms that specify the characteristics of owlProperty
        if ((type & Constants.ONT_TYPE_FUNCTIONAL) != 0)
            propertyAxioms.add(factory.getOWLFunctionalObjectPropertyAxiom(owlProperty));
        if ((type & Constants.ONT_TYPE_INVERSE_FUNCTIONAL) != 0)
            propertyAxioms.add(factory.getOWLInverseFunctionalObjectPropertyAxiom(owlProperty));
        if ((type & Constants.ONT_TYPE_REFLEXIVE) != 0)
            propertyAxioms.add(factory.getOWLReflexiveObjectPropertyAxiom(owlProperty));
        if ((type & Constants.ONT_TYPE_IRREFLEXIVE) != 0)
            propertyAxioms.add(factory.getOWLIrreflexiveObjectPropertyAxiom(owlProperty));
        if ((type & Constants.ONT_TYPE_SYMMETRIC) != 0)
            propertyAxioms.add(factory.getOWLSymmetricObjectPropertyAxiom(owlProperty));
        if ((type & Constants.ONT_TYPE_ASYMMETRIC) != 0)
            propertyAxioms.add(factory.getOWLAsymmetricObjectPropertyAxiom(owlProperty));
        if ((type & Constants.ONT_TYPE_TRANSITIVE) != 0)
            propertyAxioms.add(factory.getOWLTransitiveObjectPropertyAxiom(owlProperty));

        // Now add them to the ontology
        manager.addAxioms(ontology, propertyAxioms);
    }

    // i.e. Bulbasaur, Charmander, Squirtle
    void setDifferentIndividuals(String... individuals) {

        Set<OWLIndividual> set = new HashSet<>();
        for (String individual : individuals) {
            set.add(factory.getOWLNamedIndividual(":" + individual, prefixManager));
        }

        OWLDifferentIndividualsAxiom differentIndividuals = factory.getOWLDifferentIndividualsAxiom(set);
        manager.addAxiom(ontology, differentIndividuals);
    }

    // i.e. Man, Woman
    void setDisjointClasses(String... classes) {

        Set<OWLClass> set = new HashSet<>();
        for (String c : classes) {
            set.add(factory.getOWLClass(":" + c, prefixManager));
        }

        OWLDisjointClassesAxiom disjointClassesAxiom = factory.getOWLDisjointClassesAxiom(set);
        manager.addAxiom(ontology, disjointClassesAxiom);
    }

    void addEquivalentClasses(String object, OWLClassExpression expression) {
        OWLClass owlClass = factory.getOWLClass(":" + object, prefixManager);

        OWLEquivalentClassesAxiom teenagerDefinition = factory.getOWLEquivalentClassesAxiom(owlClass, expression);
        manager.addAxiom(ontology, teenagerDefinition);
    }

    OWLClassExpression createClassExpression(String object, String property, int value, OWLFacet facet) {

        object = processName(object);
        property = processName(property);

        OWLClass owlObject = factory.getOWLClass(":" + object, prefixManager);
        OWLDataProperty owlProperty = factory.getOWLDataProperty(":" + property, prefixManager);
        OWLDatatype integerDatatype = factory.getIntegerOWLDatatype();

        OWLFacetRestriction owlRestriction = factory.getOWLFacetRestriction(facet, factory.getOWLLiteral(value));

        // Restrict the base type, integer (which is just an XML Schema
        // Datatype) with the facet restrictions.
        OWLDataRange range = factory.getOWLDatatypeRestriction(integerDatatype, owlRestriction);
        OWLDataSomeValuesFrom dataRestriction = factory.getOWLDataSomeValuesFrom(owlProperty, range);

        // Now make Teenager equivalent to Person and hasAge some int[>=13, <20]
        // First create the class Person and hasAge some int[>=13, <20]
        OWLClassExpression expression = factory.getOWLObjectIntersectionOf(owlObject, dataRestriction);

        return expression;
    }

    void save() {
        if (documentIRI != null) {

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
