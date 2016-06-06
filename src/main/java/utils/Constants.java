package utils;

import agents.AbstractAgent;

/**
 * Created by Maciek on 27.05.2016.
 */
public interface Constants {

    //identyfikatory serwisów
    AbstractAgent.ServiceName DOWNLOADER_SERVICE = new AbstractAgent.ServiceName("web", "download");
    AbstractAgent.ServiceName CRAWLER_SERVICE = new AbstractAgent.ServiceName("web", "crawler");
//    agents.AbstractAgent.ServiceName GATEWAY_SERVICE = new agents.AbstractAgent.ServiceName("web", "gateway");
    AbstractAgent.ServiceName ONTOLOGY_SERVICE = new AbstractAgent.ServiceName("web", "ontology");
    AbstractAgent.ServiceName STATISTICS_SERVICE = new AbstractAgent.ServiceName("general", "statistics");

    String GATEWAY_SERVICE_TYPE = "gateway";
    String ONTOLOGY_SERVICE_TYPE = "ontology";

    //parametry wiadomości
    String FROM_CRAWLER = "fromCrawler";
    String URL = "url";
    String PHRASES = "phrases";
    String FILE = "file";
    String DOMAIN = "domain";

    String URL_SEPARATOR = ";";
    String PHRASE_SEPARATOR = ";";

    String HAS_ATTRIBUTE = "hasAttribute";
    String IS_ASSOCIATED_WITH = "isAssociatedWith";

    // agents.OntologyAgent message keys.
    String ONT_OPERATION = "oOperation";
    String ONT_BASE = "oBase";                  // i.e. http://www.wp.pl
    String ONT_OBJECT = "oObject";
    String ONT_RELATED_OBJECT = "oRelatedObject";
    String ONT_PROPERTY = "oProperty";
    String ONT_TYPE = "oType";
    String ONT_VALUE = "oValue";
    String ONT_VALUE_TYPE = "oValueType";

    // agents.OntologyAgent message values;
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

    // agents.OntologyManager properties types.
    int ONT_TYPE_FUNCTIONAL = 1;
    int ONT_TYPE_INVERSE_FUNCTIONAL = 1 << 1;
    int ONT_TYPE_REFLEXIVE = 1 << 2;
    int ONT_TYPE_IRREFLEXIVE = 1 << 4;
    int ONT_TYPE_SYMMETRIC = 1 << 4;
    int ONT_TYPE_ASYMMETRIC = 1 << 5;
    int ONT_TYPE_TRANSITIVE = 1 << 6;
}
