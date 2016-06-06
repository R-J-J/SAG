/**
 * Created by Maciek on 27.05.2016.
 */
public interface Constants {

    //identyfikatory serwisów
    AbstractAgent.ServiceName DOWNLOADER_SERVICE = new AbstractAgent.ServiceName("web", "download");
    AbstractAgent.ServiceName CRAWLER_SERVICE = new AbstractAgent.ServiceName("web", "crawler");
    AbstractAgent.ServiceName GATEWAY_SERVICE = new AbstractAgent.ServiceName("web", "gateway");
    AbstractAgent.ServiceName ONTOLOGY_SERVICE = new AbstractAgent.ServiceName("web", "ontology");
    AbstractAgent.ServiceName STATISTICS_SERVICE = new AbstractAgent.ServiceName("general", "statistics");

    //parametry wiadomości
    String FROM_CRAWLER = "fromCrawler";
    String URL = "url";
    String PHRASES = "phrases";
    String FILE = "file";

    String URL_SEPARATOR = ";";
    String PHRASE_SEPARATOR = ";";

    // OntologyAgent message keys.
    String ONT_OPERATION = "oOperation";
    String ONT_BASE = "oBase";                  // i.e. http://www.wp.pl
    String ONT_OBJECT = "oObject";
    String ONT_RELATED_OBJECT = "oRelatedObject";
    String ONT_PROPERTY = "oProperty";
    String ONT_TYPE = "oType";
    String ONT_VALUE = "oValue";
    String ONT_VALUE_TYPE = "oValueType";

    // OntologyAgent message values;
    String ONT_NEW = "oNewOntology";
    String ONT_SAVE = "oSave";
    String ONT_ADD_CLASS = "oAddClass";
    String ONT_ADD_SUBCLASS = "oAddSubclass";
    String ONT_ADD_ASSERTION = "oAddAssertion";
    String ONT_TYPE_OBJECT_ASSERTION = "oObjectProperty";
    String ONT_TYPE_DATA_ASSERTION = "oDataProperty";
    String ONT_TYPE_CLASS_ASSERTION = "oClassAssertion";
    String ONT_TYPE_INTEGER = "oTypeInteger";
    String ONT_TYPE_STRING = "oTypeString";

    // OntologyManager properties types.
    int ONT_TYPE_FUNCTIONAL = 1;
    int ONT_TYPE_INVERSE_FUNCTIONAL = 1 << 1;
    int ONT_TYPE_REFLEXIVE = 1 << 2;
    int ONT_TYPE_IRREFLEXIVE = 1 << 4;
    int ONT_TYPE_SYMMETRIC = 1 << 4;
    int ONT_TYPE_ASYMMETRIC = 1 << 5;
    int ONT_TYPE_TRANSITIVE = 1 << 6;


    String TEST_ONTOLOGY = "<?xml version=\"1.0\"?>\n"
            + "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\" xmlns:owl=\"http://www.w3.org/2002/07/owl#\" xmlns=\"http://protege.stanford.edu/plugins/owl/owl-library/koala.owl#\" xml:base=\"http://protege.stanford.edu/plugins/owl/owl-library/koala.owl\">\n"
            + "  <owl:Ontology rdf:about=\"\"/>\n"
            + "  <owl:Class rdf:ID=\"Female\"><owl:equivalentClass><owl:Restriction><owl:onProperty><owl:FunctionalProperty rdf:about=\"#hasGender\"/></owl:onProperty><owl:hasValue><Gender rdf:ID=\"female\"/></owl:hasValue></owl:Restriction></owl:equivalentClass></owl:Class>\n"
            + "  <owl:Class rdf:ID=\"Marsupials\"><owl:disjointWith><owl:Class rdf:about=\"#Person\"/></owl:disjointWith><rdfs:subClassOf><owl:Class rdf:about=\"#Animal\"/></rdfs:subClassOf></owl:Class>\n"
            + "  <owl:Class rdf:ID=\"Student\"><owl:equivalentClass><owl:Class><owl:intersectionOf rdf:parseType=\"Collection\"><owl:Class rdf:about=\"#Person\"/><owl:Restriction><owl:onProperty><owl:FunctionalProperty rdf:about=\"#isHardWorking\"/></owl:onProperty><owl:hasValue rdf:datatype=\"http://www.w3.org/2001/XMLSchema#boolean\">true</owl:hasValue></owl:Restriction><owl:Restriction><owl:someValuesFrom><owl:Class rdf:about=\"#University\"/></owl:someValuesFrom><owl:onProperty><owl:ObjectProperty rdf:about=\"#hasHabitat\"/></owl:onProperty></owl:Restriction></owl:intersectionOf></owl:Class></owl:equivalentClass></owl:Class>\n"
            + "  <owl:Class rdf:ID=\"KoalaWithPhD\"><owl:versionInfo>1.2</owl:versionInfo><owl:equivalentClass><owl:Class><owl:intersectionOf rdf:parseType=\"Collection\"><owl:Restriction><owl:hasValue><Degree rdf:ID=\"PhD\"/></owl:hasValue><owl:onProperty><owl:ObjectProperty rdf:about=\"#hasDegree\"/></owl:onProperty></owl:Restriction><owl:Class rdf:about=\"#Koala\"/></owl:intersectionOf></owl:Class></owl:equivalentClass></owl:Class>\n"
            + "  <owl:Class rdf:ID=\"University\"><rdfs:subClassOf><owl:Class rdf:ID=\"Habitat\"/></rdfs:subClassOf></owl:Class>\n"
            + "  <owl:Class rdf:ID=\"Koala\"><rdfs:subClassOf><owl:Restriction><owl:hasValue rdf:datatype=\"http://www.w3.org/2001/XMLSchema#boolean\">false</owl:hasValue><owl:onProperty><owl:FunctionalProperty rdf:about=\"#isHardWorking\"/></owl:onProperty></owl:Restriction></rdfs:subClassOf><rdfs:subClassOf><owl:Restriction><owl:someValuesFrom><owl:Class rdf:about=\"#DryEucalyptForest\"/></owl:someValuesFrom><owl:onProperty><owl:ObjectProperty rdf:about=\"#hasHabitat\"/></owl:onProperty></owl:Restriction></rdfs:subClassOf><rdfs:subClassOf rdf:resource=\"#Marsupials\"/></owl:Class>\n"
            + "  <owl:Class rdf:ID=\"Animal\"><rdfs:seeAlso>Male</rdfs:seeAlso><rdfs:subClassOf><owl:Restriction><owl:onProperty><owl:ObjectProperty rdf:about=\"#hasHabitat\"/></owl:onProperty><owl:minCardinality rdf:datatype=\"http://www.w3.org/2001/XMLSchema#int\">1</owl:minCardinality></owl:Restriction></rdfs:subClassOf><rdfs:subClassOf><owl:Restriction><owl:cardinality rdf:datatype=\"http://www.w3.org/2001/XMLSchema#int\">1</owl:cardinality><owl:onProperty><owl:FunctionalProperty rdf:about=\"#hasGender\"/></owl:onProperty></owl:Restriction></rdfs:subClassOf><owl:versionInfo>1.1</owl:versionInfo></owl:Class>\n"
            + "  <owl:Class rdf:ID=\"Forest\"><rdfs:subClassOf rdf:resource=\"#Habitat\"/></owl:Class>\n"
            + "  <owl:Class rdf:ID=\"Rainforest\"><rdfs:subClassOf rdf:resource=\"#Forest\"/></owl:Class>\n"
            + "  <owl:Class rdf:ID=\"GraduateStudent\"><rdfs:subClassOf><owl:Restriction><owl:onProperty><owl:ObjectProperty rdf:about=\"#hasDegree\"/></owl:onProperty><owl:someValuesFrom><owl:Class><owl:oneOf rdf:parseType=\"Collection\"><Degree rdf:ID=\"BA\"/><Degree rdf:ID=\"BS\"/></owl:oneOf></owl:Class></owl:someValuesFrom></owl:Restriction></rdfs:subClassOf><rdfs:subClassOf rdf:resource=\"#Student\"/></owl:Class>\n"
            + "  <owl:Class rdf:ID=\"Parent\"><owl:equivalentClass><owl:Class><owl:intersectionOf rdf:parseType=\"Collection\"><owl:Class rdf:about=\"#Animal\"/><owl:Restriction><owl:onProperty><owl:ObjectProperty rdf:about=\"#hasChildren\"/></owl:onProperty><owl:minCardinality rdf:datatype=\"http://www.w3.org/2001/XMLSchema#int\">1</owl:minCardinality></owl:Restriction></owl:intersectionOf></owl:Class></owl:equivalentClass><rdfs:subClassOf rdf:resource=\"#Animal\"/></owl:Class>\n"
            + "  <owl:Class rdf:ID=\"DryEucalyptForest\"><rdfs:subClassOf rdf:resource=\"#Forest\"/></owl:Class>\n"
            + "  <owl:Class rdf:ID=\"Quokka\"><rdfs:subClassOf><owl:Restriction><owl:hasValue rdf:datatype=\"http://www.w3.org/2001/XMLSchema#boolean\">true</owl:hasValue><owl:onProperty><owl:FunctionalProperty rdf:about=\"#isHardWorking\"/></owl:onProperty></owl:Restriction></rdfs:subClassOf><rdfs:subClassOf rdf:resource=\"#Marsupials\"/></owl:Class>\n"
            + "  <owl:Class rdf:ID=\"TasmanianDevil\"><rdfs:subClassOf rdf:resource=\"#Marsupials\"/></owl:Class>\n"
            + "  <owl:Class rdf:ID=\"MaleStudentWith3Daughters\"><owl:equivalentClass><owl:Class><owl:intersectionOf rdf:parseType=\"Collection\"><owl:Class rdf:about=\"#Student\"/><owl:Restriction><owl:onProperty><owl:FunctionalProperty rdf:about=\"#hasGender\"/></owl:onProperty><owl:hasValue><Gender rdf:ID=\"male\"/></owl:hasValue></owl:Restriction><owl:Restriction><owl:onProperty><owl:ObjectProperty rdf:about=\"#hasChildren\"/></owl:onProperty><owl:cardinality rdf:datatype=\"http://www.w3.org/2001/XMLSchema#int\">3</owl:cardinality></owl:Restriction><owl:Restriction><owl:allValuesFrom rdf:resource=\"#Female\"/><owl:onProperty><owl:ObjectProperty rdf:about=\"#hasChildren\"/></owl:onProperty></owl:Restriction></owl:intersectionOf></owl:Class></owl:equivalentClass></owl:Class>\n"
            + "  <owl:Class rdf:ID=\"Degree\"/>\n  <owl:Class rdf:ID=\"Gender\"/>\n"
            + "  <owl:Class rdf:ID=\"Male\"><owl:equivalentClass><owl:Restriction><owl:hasValue rdf:resource=\"#male\"/><owl:onProperty><owl:FunctionalProperty rdf:about=\"#hasGender\"/></owl:onProperty></owl:Restriction></owl:equivalentClass></owl:Class>\n"
            + "  <owl:Class rdf:ID=\"Person\"><rdfs:subClassOf rdf:resource=\"#Animal\"/><owl:disjointWith rdf:resource=\"#Marsupials\"/></owl:Class>\n"
            + "  <owl:ObjectProperty rdf:ID=\"hasHabitat\"><rdfs:range rdf:resource=\"#Habitat\"/><rdfs:domain rdf:resource=\"#Animal\"/></owl:ObjectProperty>\n"
            + "  <owl:ObjectProperty rdf:ID=\"hasDegree\"><rdfs:domain rdf:resource=\"#Person\"/><rdfs:range rdf:resource=\"#Degree\"/></owl:ObjectProperty>\n"
            + "  <owl:ObjectProperty rdf:ID=\"hasChildren\"><rdfs:range rdf:resource=\"#Animal\"/><rdfs:domain rdf:resource=\"#Animal\"/></owl:ObjectProperty>\n"
            + "  <owl:FunctionalProperty rdf:ID=\"hasGender\"><rdfs:range rdf:resource=\"#Gender\"/><rdf:type rdf:resource=\"http://www.w3.org/2002/07/owl#ObjectProperty\"/><rdfs:domain rdf:resource=\"#Animal\"/></owl:FunctionalProperty>\n"
            + "  <owl:FunctionalProperty rdf:ID=\"isHardWorking\"><rdfs:range rdf:resource=\"http://www.w3.org/2001/XMLSchema#boolean\"/><rdfs:domain rdf:resource=\"#Person\"/><rdf:type rdf:resource=\"http://www.w3.org/2002/07/owl#DatatypeProperty\"/></owl:FunctionalProperty>\n"
            + "  <Degree rdf:ID=\"MA\"/>\n</rdf:RDF>";

}
