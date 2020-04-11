/**
 * Copyright 2016
 * Ivan Cantador
 * Information Retrieval Group at Universidad Autonoma de Madrid
 *
 * This is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * the current software. If not, see <http://www.gnu.org/licenses/>.
 */
package es.uam.irg.semantics.dbpedia;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.impl.PropertyImpl;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

// added libs

import static java.lang.Math.abs;
import static java.lang.Math.max;
import java.text.Normalizer;

/**
 * DBpedia
 *
 * Launches a number of queries to the DBpedia SPARQL endpoint.
 *
 * @author Ivan Cantador, ivan.cantador@uam.es
 * @version 1.0 - 16/03/2016
 */
public class DBpedia {

    public static final String SPARQL_ENDPOINT = "http://dbpedia.org/sparql"; // old endpoint
    // public static final String SPARQL_ENDPOINT =
    // "http://live.dbpedia.org/sparql";
    public static final Map<String, String> NAMESPACES;

    static {
        NAMESPACES = new HashMap<String, String>();
        NAMESPACES.put("a", "http://www.w3.org/2005/Atom");
        NAMESPACES.put("address", "http://schemas.talis.com/2005/address/schema#");
        NAMESPACES.put("admin", "http://webns.net/mvcb/");
        NAMESPACES.put("atom", "http://atomowl.org/ontologies/atomrdf#");
        NAMESPACES.put("aws", "http://soap.amazon.com/");
        NAMESPACES.put("b3s", "http://b3s.openlinksw.com/");
        NAMESPACES.put("batch", "http://schemas.google.com/gdata/batch");
        NAMESPACES.put("bibo", "http://purl.org/ontology/bibo/");
        NAMESPACES.put("bugzilla", "http://www.openlinksw.com/schemas/bugzilla#");
        NAMESPACES.put("c", "http://www.w3.org/2002/12/cal/icaltzd#");
        NAMESPACES.put("campsite", "http://www.openlinksw.com/campsites/schema#");
        NAMESPACES.put("cb", "http://www.crunchbase.com/");
        NAMESPACES.put("cc", "http://web.resource.org/cc/");
        NAMESPACES.put("content", "http://purl.org/rss/1.0/modules/content/");
        NAMESPACES.put("cv", "http://purl.org/captsolo/resume-rdf/0.2/cv#");
        NAMESPACES.put("cvbase", "http://purl.org/captsolo/resume-rdf/0.2/base#");
        NAMESPACES.put("dawgt", "http://www.w3.org/2001/sw/DataAccess/tests/test-dawg#");
        NAMESPACES.put("dbc", "http://dbpedia.org/resource/Category:");
        NAMESPACES.put("dbo", "http://dbpedia.org/ontology/");
        NAMESPACES.put("dbp", "http://dbpedia.org/property/");
        NAMESPACES.put("dbr", "http://dbpedia.org/resource/");
        NAMESPACES.put("dc", "http://purl.org/dc/elements/1.1/");
        NAMESPACES.put("dct", "http://purl.org/dc/terms/");
        NAMESPACES.put("digg", "http://digg.com/docs/diggrss/");
        NAMESPACES.put("dul", "http://www.ontologydesignpatterns.org/ont/dul/DUL.owl");
        NAMESPACES.put("enc", "http://purl.oclc.org/net/rss_2.0/enc#");
        NAMESPACES.put("exif", "http://www.w3.org/2003/12/exif/ns/");
        NAMESPACES.put("fb", "http://api.facebook.com/1.0/");
        NAMESPACES.put("ff", "http://api.friendfeed.com/2008/03");
        NAMESPACES.put("fn", "http://www.w3.org/2005/xpath-functions/#");
        NAMESPACES.put("foaf", "http://xmlns.com/foaf/0.1/");
        NAMESPACES.put("freebase", "http://rdf.freebase.com/ns/");
        NAMESPACES.put("g", "http://base.google.com/ns/1.0");
        NAMESPACES.put("gb", "http://www.openlinksw.com/schemas/google-base#");
        NAMESPACES.put("gd", "http://schemas.google.com/g/2005");
        NAMESPACES.put("geo", "http://www.w3.org/2003/01/geo/wgs84_pos#");
        NAMESPACES.put("geonames", "http://www.geonames.org/ontology#");
        NAMESPACES.put("georss", "http://www.georss.org/georss/");
        NAMESPACES.put("gml", "http://www.opengis.net/gml");
        NAMESPACES.put("go", "http://purl.org/obo/owl/GO#");
        NAMESPACES.put("hlisting", "http://www.openlinksw.com/schemas/hlisting/");
        NAMESPACES.put("hoovers", "http://wwww.hoovers.com/");
        NAMESPACES.put("hrev", "http://www.purl.org/stuff/hrev#");
        NAMESPACES.put("ical", "http://www.w3.org/2002/12/cal/ical#");
        NAMESPACES.put("ir", "http://web-semantics.org/ns/image-regions");
        NAMESPACES.put("itunes", "http://www.itunes.com/DTDs/Podcast-1.0.dtd");
        NAMESPACES.put("ldp", "http://www.w3.org/ns/ldp#");
        NAMESPACES.put("lgv", "http://linkedgeodata.org/vocabulary#");
        NAMESPACES.put("link", "http://www.xbrl.org/2003/linkbase");
        NAMESPACES.put("lod", "http://lod.openlinksw.com/");
        NAMESPACES.put("math", "http://www.w3.org/2000/10/swap/math#");
        NAMESPACES.put("media", "http://search.yahoo.com/mrss/");
        NAMESPACES.put("mesh", "http://purl.org/commons/record/mesh/");
        NAMESPACES.put("mf", "http://www.w3.org/2001/sw/DataAccess/tests/test-manifest#");
        NAMESPACES.put("mmd", "http://musicbrainz.org/ns/mmd-1.0#");
        NAMESPACES.put("mo", "http://purl.org/ontology/mo/");
        NAMESPACES.put("mql", "http://www.freebase.com/");
        NAMESPACES.put("nci", "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");
        NAMESPACES.put("nfo", "http://www.semanticdesktop.org/ontologies/nfo/#");
        NAMESPACES.put("ng", "http://www.openlinksw.com/schemas/ning#");
        NAMESPACES.put("nyt", "http://www.nytimes.com/");
        NAMESPACES.put("oai", "http://www.openarchives.org/OAI/2.0/");
        NAMESPACES.put("oai_dc", "http://www.openarchives.org/OAI/2.0/oai_dc/");
        NAMESPACES.put("obo", "http://www.geneontology.org/formats/oboInOwl#");
        NAMESPACES.put("ogc", "http://www.opengis.net/");
        NAMESPACES.put("ogcgml", "http://www.opengis.net/ont/gml#");
        NAMESPACES.put("ogcgs", "http://www.opengis.net/ont/geosparql#");
        NAMESPACES.put("ogcgsf", "http://www.opengis.net/def/function/geosparql/");
        NAMESPACES.put("ogcgsr", "http://www.opengis.net/def/rule/geosparql/");
        NAMESPACES.put("ogcsf", "http://www.opengis.net/ont/sf#");
        NAMESPACES.put("openSearch", "http://a9.com/-/spec/opensearchrss/1.0/");
        NAMESPACES.put("opencyc", "http://sw.opencyc.org/2008/06/10/concept/");
        NAMESPACES.put("opl", "http://www.openlinksw.com/schema/attribution#");
        NAMESPACES.put("opl-gs", "http://www.openlinksw.com/schemas/getsatisfaction/");
        NAMESPACES.put("opl-meetup", "http://www.openlinksw.com/schemas/meetup/");
        NAMESPACES.put("opl-xbrl", "http://www.openlinksw.com/schemas/xbrl/");
        NAMESPACES.put("oplweb", "http://www.openlinksw.com/schemas/oplweb#");
        NAMESPACES.put("ore", "http://www.openarchives.org/ore/terms/");
        NAMESPACES.put("owl", "http://www.w3.org/2002/07/owl#");
        NAMESPACES.put("product", "http://www.buy.com/rss/module/productV2/");
        NAMESPACES.put("protseq", "http://purl.org/science/protein/bysequence/");
        NAMESPACES.put("r", "http://backend.userland.com/rss2");
        NAMESPACES.put("radio", "http://www.radiopop.co.uk/");
        NAMESPACES.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        NAMESPACES.put("rdfa", "http://www.w3.org/ns/rdfa#");
        NAMESPACES.put("rdfdf", "http://www.openlinksw.com/virtrdf-data-formats#");
        NAMESPACES.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        NAMESPACES.put("rev", "http://purl.org/stuff/rev#");
        NAMESPACES.put("review", "http://www.purl.org/stuff/rev#");
        NAMESPACES.put("rss", "http://purl.org/rss/1.0/");
        NAMESPACES.put("sc", "http://purl.org/science/owl/sciencecommons/");
        NAMESPACES.put("scovo", "http://purl.org/NET/scovo#");
        NAMESPACES.put("sd", "http://www.w3.org/ns/sparql-service-description#");
        NAMESPACES.put("sioc", "http://rdfs.org/sioc/ns#");
        NAMESPACES.put("sioct", "http://rdfs.org/sioc/types#");
        NAMESPACES.put("skiresort", "http://www.openlinksw.com/ski_resorts/schema#");
        NAMESPACES.put("skos", "http://www.w3.org/2004/02/skos/core#");
        NAMESPACES.put("slash", "http://purl.org/rss/1.0/modules/slash/");
        NAMESPACES.put("stock", "http://xbrlontology.com/ontology/finance/stock_market#");
        NAMESPACES.put("twfy", "http://www.openlinksw.com/schemas/twfy#");
        NAMESPACES.put("umbel", "http://umbel.org/umbel#");
        NAMESPACES.put("umbel-ac", "http://umbel.org/umbel/ac/");
        NAMESPACES.put("umbel-rc", "http://umbel.org/umbel/rc/");
        NAMESPACES.put("umbel-sc", "http://umbel.org/umbel/sc/");
        NAMESPACES.put("uniprot", "http://purl.uniprot.org/");
        NAMESPACES.put("units", "http://dbpedia.org/units/");
        NAMESPACES.put("usc", "http://www.rdfabout.com/rdf/schema/uscensus/details/100pct/");
        NAMESPACES.put("v", "http://www.openlinksw.com/xsltext/");
        NAMESPACES.put("vcard", "http://www.w3.org/2001/vcard-rdf/3.0#");
        NAMESPACES.put("vcard2006", "http://www.w3.org/2006/vcard/ns#");
        NAMESPACES.put("vi", "http://www.openlinksw.com/virtuoso/xslt/");
        NAMESPACES.put("virt", "http://www.openlinksw.com/virtuoso/xslt");
        NAMESPACES.put("virtcxml", "http://www.openlinksw.com/schemas/virtcxml#");
        NAMESPACES.put("virtpivot", "http://www.openlinksw.com/schemas/virtpivot#");
        NAMESPACES.put("virtrdf", "http://www.openlinksw.com/schemas/virtrdf#");
        NAMESPACES.put("void", "http://rdfs.org/ns/void#");
        NAMESPACES.put("wb", "http://www.worldbank.org/");
        NAMESPACES.put("wdrs", "http://www.w3.org/2007/05/powder-s#");
        NAMESPACES.put("wf", "http://www.w3.org/2005/01/wf/flow#");
        NAMESPACES.put("wfw", "http://wellformedweb.org/CommentAPI/");
        NAMESPACES.put("wikicompany", "http://dbpedia.openlinksw.com/wikicompany/");
        NAMESPACES.put("wikidata", "http://www.wikidata.org/entity/");
        NAMESPACES.put("xf", "http://www.w3.org/2004/07/xpath-functions");
        NAMESPACES.put("xfn", "http://gmpg.org/xfn/11#");
        NAMESPACES.put("xhtml", "http://www.w3.org/1999/xhtml");
        NAMESPACES.put("xhv", "http://www.w3.org/1999/xhtml/vocab#");
        NAMESPACES.put("xi", "http://www.xbrl.org/2003/instance");
        NAMESPACES.put("xml", "http://www.w3.org/XML/1998/namespace");
        NAMESPACES.put("xn", "http://www.ning.com/atom/1.0");
        NAMESPACES.put("xsd", "http://www.w3.org/2001/XMLSchema#");
        NAMESPACES.put("xsl10", "http://www.w3.org/XSL/Transform/1.0");
        NAMESPACES.put("xsl1999", "http://www.w3.org/1999/XSL/Transform");
        NAMESPACES.put("xslwd", "http://www.w3.org/TR/WD-xsl");
        NAMESPACES.put("yago", "http://dbpedia.org/class/yago/");
        NAMESPACES.put("yago-res", "http://mpii.de/yago/resource/");
        NAMESPACES.put("yt", "http://gdata.youtube.com/schemas/2007");
        NAMESPACES.put("zem", "http://s.zemanta.com/ns#");
    }
    public static String NAMESPACES_SPARQL = null;

    static {
        NAMESPACES_SPARQL = "";
        for (Entry<String, String> entry : NAMESPACES.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            NAMESPACES_SPARQL += "PREFIX " + key + ": <" + value + "> \n";
        }
    }

    static {
        try {
            // Disable log messages
            org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Queries DBpedia SPARQL endpoint to retrieve the properties and corresponding
     * objects of certain subject.
     *
     * @param subject - the RDF node of the subject whose properties and objects are
     *                retrieved
     * @return a map [property, objects] with the objects associated to each
     *         property of the input subject
     */
    public static Map<RDFNode, List<RDFNode>> queryForPropertiesAndObjects(RDFNode subject) throws Exception {
        return queryForPropertiesAndObjects(subject.asResource().getURI());
    }

    /**
     * Queries DBpedia SPARQL endpoint to retrieve the properties and corresponding
     * objects of certain subject.
     *
     * @param subject - the URI of the subject whose properties and objects are
     *                retrieved
     * @return a map [property, objects] with the objects associated to each
     *         property of the input subject
     */
    public static Map<RDFNode, List<RDFNode>> queryForPropertiesAndObjects(String subjectURI) throws Exception {
        Map<RDFNode, List<RDFNode>> results = new HashMap<RDFNode, List<RDFNode>>();

        String query = NAMESPACES_SPARQL + "SELECT ?p ?o WHERE { \n" + "<" + subjectURI + ">" + " ?p ?o . \n" + "} \n";

        Query sparqlQuery = QueryFactory.create(query);

        QueryExecution qexec = QueryExecutionFactory.sparqlService(SPARQL_ENDPOINT, sparqlQuery);
        if (qexec != null) {
            ResultSet resultSet = qexec.execSelect();
            while (resultSet != null && resultSet.hasNext()) {
                QuerySolution solution = resultSet.nextSolution();
                RDFNode property = solution.get("p");
                RDFNode object = solution.get("o");
                if (!results.containsKey(property)) {
                    results.put(property, new ArrayList<RDFNode>());
                }
                results.get(property).add(object);
            }
            qexec.close();
        }

        return results;
    }

    /**
     * Queries DBpedia SPARQL endpoint to retrieve the categories and types of
     * certain subject.
     *
     * Such categories and types are obtained from the values of properties such as
     * dct:subject, rdf:type and dbp:wordnet_type.
     *
     * @param subject - the RDF node of the subject whose categories and types are
     *                retrieved
     * @return a list with the retrieved categtories and types as RDF nodes
     */
    public static List<RDFNode> queryForCategoriesAndTypesOf(RDFNode subject) throws Exception {
        return queryForCategoriesAndTypesOf(subject.asResource().getURI());
    }

    /**
     * Queries DBpedia SPARQL endpoint to retrieve the categories and types of
     * certain subject.
     *
     * Such categories and types are obtained from the values of properties such as
     * dct:subject, rdf:type and dbp:wordnet_type.
     *
     * @param subject - the URI of the subject whose categories and types are
     *                retrieved
     * @return a list with the retrieved categtories and types as RDF nodes
     */
    public static List<RDFNode> queryForCategoriesAndTypesOf(String subjectURI) throws Exception {
        List<RDFNode> results = new ArrayList<RDFNode>();

        String query = NAMESPACES_SPARQL
                + "PREFIX dbp: <http://dbpedia.org/property/> \n SELECT ?o \n FROM <http://dbpedia.org> \n WHERE { \n { <"
                + subjectURI + "> dct:subject ?o . } \n UNION \n { <" + subjectURI + "> rdf:type ?o . } \n UNION \n { <"
                + subjectURI + "> dbp:wordnet_type ?o . } \n } \n";

        Query sparqlQuery = QueryFactory.create(query);

        QueryExecution qexec = QueryExecutionFactory.sparqlService(SPARQL_ENDPOINT, sparqlQuery);
        if (qexec != null) {
            ResultSet resultSet = qexec.execSelect();
            while (resultSet != null && resultSet.hasNext()) {
                QuerySolution solution = resultSet.nextSolution();
                RDFNode object = solution.get("o");
                results.add(object);
            }
            qexec.close();
        }

        return results;
    }

    /**
     * Queries DBpedia SPARQL endpoint to retrieve the instances/individuales of
     * certain subject.
     *
     * Such instances are obtained from the values of properties such as
     * dct:subject, rdf:type and dbp:wordnet_type.
     *
     * @param subject - the RDF node of the subject whose categories and types are
     *                retrieved
     * @return a list with the retrieved instances as RDF nodes
     */
    public static List<RDFNode> queryForInstancesOf(RDFNode category) throws Exception {
        return queryForInstancesOf(category.asResource().getURI());
    }

    /**
     * Queries DBpedia SPARQL endpoint to retrieve the instances/individuales of
     * certain subject.
     *
     * Such instances are obtained from the values of properties such as
     * dct:subject, rdf:type and dbp:wordnet_type.
     *
     * @param subject - the URI of the subject whose categories and types are
     *                retrieved
     * @return a list with the retrieved instances as RDF nodes
     */
    public static List<RDFNode> queryForInstancesOf(String categoryURI) throws Exception {
        List<RDFNode> results = new ArrayList<RDFNode>();

        String query = NAMESPACES_SPARQL
                + "PREFIX dbp: <http://dbpedia.org/property/> \n PREFIX dct: <http://purl.org/dc/terms/> \n SELECT ?subject \n FROM <http://dbpedia.org> \n WHERE { \n { <"
                + categoryURI + "> ^dct:subject ?subject . } \n UNION \n { <" + categoryURI
                + "> rdf:type ?subject . } \n UNION \n { <" + categoryURI + "> dbp:wordnet_type ?subject . } \n } \n";

        Query sparqlQuery = QueryFactory.create(query);

        QueryExecution qexec = QueryExecutionFactory.sparqlService(SPARQL_ENDPOINT, sparqlQuery);
        if (qexec != null) {
            ResultSet resultSet = qexec.execSelect();
            while (resultSet != null && resultSet.hasNext()) {
                QuerySolution solution = resultSet.nextSolution();
                RDFNode object = solution.get("subject");
                results.add(object);
            }
            qexec.close();
        }

        return results;
    }

    /**
     * Queries DBpedia SPARQL endpoint to retrieve entities with labels (i.e.,
     * literal values of the rdfs:label property) that contain certain text.
     *
     * @param text - the text that appears in labels of the retrieved entities
     * @return a ranked list of the retrieved entities as RDF nodes
     */
    public static List<ScoredRDFNode> queryForEntitiesWithLabelAs(String text) throws Exception {
        return queryForEntitiesWithLabelAs(text, null);
    }

    /**
     * Queries DBpedia SPARQL endpoint to retrieve entities with labels (i.e.,
     * literal values of the rdfs:label property) that contain certain text.
     *
     * @param text - the text that appears in labels of the retrieved entities
     * @return a ranked list of the retrieved entities as RDF nodes
     */
    public static List<ScoredRDFNode> queryForEntitiesWithLabelAs(String text, List<String> forbiddenURIsPatterns)
            throws Exception {
        String string = text;
        String input = Normalizer.normalize(string, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
       
        // first things first

        List<ScoredRDFNode> results = new ArrayList<ScoredRDFNode>();

        String query = NAMESPACES_SPARQL
                + "SELECT ?s ?l \n FROM <http://dbpedia.org> \n WHERE { \n ?s  rdfs:label ?l . \n ?l <bif:contains>  '\""
                + input + "\"' . \n FILTER (!regex(?l, \"";

        // Boolean withTypes = false;
        // String label = input;
        // Integer limit = 1000;

        // String query = NAMESPACES_SPARQL;

        // query += "SELECT ?uri ?label " + (withTypes ? "?type" : "") + " WHERE { \n";
        // if (withTypes) {
        //     query += "   ?uri rdf:type ?type . \n";
        // }
        // query += "   ?uri rdfs:label ?label . \n";
        // query += "   FILTER regex(?label, \"" + label + "\", \"i\") . \n";
        // query += "} " + (limit > 0 ? "LIMIT " + limit + " " : "") + "\n";

        Iterator<String> iter = forbiddenURIsPatterns.iterator();

        while (iter.hasNext()) {
            String s = iter.next();
            if (iter.hasNext()) {
                query = query + s + "|";
            } else {
                query = query + s;
            }
        }

        query = query + "\", \"i\")) . \n FILTER (lang(?l) = 'en') \n}";

        // System.out.println(query);

        Query sparqlQuery = QueryFactory.create(query);

        QueryExecution qexec = QueryExecutionFactory.sparqlService(SPARQL_ENDPOINT, sparqlQuery);
        if (qexec != null) {
            String last = null;
            ResultSet resultSet = qexec.execSelect();
            while (resultSet != null && resultSet.hasNext()) {
                QuerySolution solution = resultSet.nextSolution();

                ScoredRDFNode objectScored;

                RDFNode object = solution.get("s");
                String uri = object.toString();
                String label = solution.get("l").toString().replace("@en","");
                if (last != null) {
                    System.err.println("\n\n");
                    System.out.println("Label: "+label+" url: "+uri);
                    System.out.println("similarity between this label (\""+label+"\") and the above text (\""+last+"\"): "+ld(label, last));
                    System.out.println("percent of similarity: "+ (float)ld(label, last)/(float)label.length());
                    System.out.println("similarity with the finded label \""+input+"\": "+ld(label, input));
                    System.out.println("percent of similarity: "+(float)ld(label, input)/(float)label.length());
                    System.out.println("label lenght: "+label.length());
                    objectScored = new ScoredRDFNode(object, ld(label, last));
                } else {
                    objectScored = new ScoredRDFNode(object, ld(label, input));
                }
                
                results.add(objectScored);
                last = label;
            }
            qexec.close();
        }

        return results;

        // throw new UnsupportedOperationException("Not implemented yet");
    }

    public static void test0() throws Exception {
        // 1. Obtaining metadata (properties and objects) of certain subject
        System.out.println("================================================================================");
        System.out.println("Test 0: Obtaining metadata (properties and objects) of certain subject...");

        String subjectURI = "http://dbpedia.org/resource/The_Matrix";
        Map<RDFNode, List<RDFNode>> metadata = DBpedia.queryForPropertiesAndObjects(subjectURI);
        System.out.println(subjectURI);
        for (RDFNode property : metadata.keySet()) {
            List<RDFNode> objects = metadata.get(property);
            for (RDFNode object : objects) {
                if (object.isURIResource()) {
                    System.out.println("\t" + property.asResource().getURI() + "\t" + object.asResource().getURI());
                } else if (object.isLiteral()) {
                    System.out.println("\t" + property.asResource().getURI() + "\t"
                            + object.asLiteral().getString().replace("\n", " "));
                }
            }
        }

        // 2. Storing RDF data into a file
        System.out.println("Saving RDF data into a file...");

        // Creating a RDF model
        Model model = ModelFactory.createDefaultModel();
        model.setNsPrefixes(NAMESPACES);

        // Creating the subject resource in the model
        Resource subject = model.createResource(subjectURI);

        // Inserting the subject metadata into the model
        for (Entry<RDFNode, List<RDFNode>> entry : metadata.entrySet()) {
            Property property = new PropertyImpl(URLDecoder.decode(entry.getKey().asResource().getURI(), "UTF-8"));
            List<RDFNode> objects = entry.getValue();

            for (RDFNode object : objects) {
                Statement statement = null;
                if (object.isLiteral()) {
                    statement = model.createStatement(subject, property,
                            object.asLiteral().getString().replace("\n", " "));
                } else if (object.isURIResource()) {
                    statement = model.createStatement(subject, property, object);
                }
                if (statement != null) {
                    model.add(statement);
                }
            }
        }

        // Saving the model as a RDF file
        String fileName = subject.getLocalName() + ".rdf";
        model.write(new FileOutputStream(new File(fileName)), "RDF/XML");
        System.out.println("Model written in " + fileName);

    }

    public static void test1() throws Exception {
        System.out.println("================================================================================");
        System.out.println("Test 1: Obtaining the categories/types of certain instance\n");
        String subject2URI = "http://dbpedia.org/resource/Autonomous_University_of_Madrid";
        List<RDFNode> types = DBpedia.queryForCategoriesAndTypesOf(subject2URI);
        System.out.println(subject2URI);
        for (RDFNode type : types) {
            if (type.isURIResource()) {
                System.out.println("\t" + type.asResource().getURI());
            } else if (type.isLiteral()) {
                System.out.println("\t" + type.asLiteral().getString());
            }
        }

        System.out.println("================================================================================");
        System.out.println("Test 1.2: Obtaining the instances of certain category/type\n");
        String categoryURI = "http://dbpedia.org/resource/Category:Universities_in_Madrid";
        List<RDFNode> instances = DBpedia.queryForInstancesOf(categoryURI);
        System.out.println(categoryURI);
        for (RDFNode instance : instances) {
            if (instance.isURIResource()) {
                System.out.println("\t" + instance.asResource().getURI());
            } else if (instance.isLiteral()) {
                System.out.println("\t" + instance.asLiteral().getString().replace("\n", " "));
            }
        }
    }

    public static void test2() throws Exception {
        System.out.println("================================================================================");
        System.out.println("Test 2: Obtaining entities with certain label\n");

        String label = "BM Atlético Madrid";
        List<String> forbiddenURIsPatterns = new ArrayList<String>();
        forbiddenURIsPatterns.add("Category");
        forbiddenURIsPatterns.add("_in_");
        forbiddenURIsPatterns.add("_B");
        forbiddenURIsPatterns.add("_C");
        forbiddenURIsPatterns.add("_c");
        forbiddenURIsPatterns.add("match");
        forbiddenURIsPatterns.add("season");
        forbiddenURIsPatterns.add("player");
        forbiddenURIsPatterns.add("footballer");
        forbiddenURIsPatterns.add("coach");
        forbiddenURIsPatterns.add("manager");
        forbiddenURIsPatterns.add("president");
        List<ScoredRDFNode> results = DBpedia.queryForEntitiesWithLabelAs(label, forbiddenURIsPatterns);
        // for (ScoredRDFNode result : results) {
        //     Resource entity = result.getNode().asResource();
        //     System.out.println(URLDecoder.decode(entity.getURI(), "UTF-8"));
        //     System.out.println("\t" + result.getScore());
        // }
    }

    // begin Levenshtein distance
    // Iterative space optimized (even bounded)
    // https://rosettacode.org/wiki/Category:Java

    public static int ld(String a, String b) {
        return distance(a, b, -1);
    }

    public static boolean ld(String a, String b, int max) {
        return distance(a, b, max) <= max;
    }

    private static int distance(String a, String b, int max) {
        if (a == b)
            return 0;
        int la = a.length();
        int lb = b.length();
        if (max >= 0 && abs(la - lb) > max)
            return max + 1;
        if (la == 0)
            return lb;
        if (lb == 0)
            return la;
        if (la < lb) {
            int tl = la;
            la = lb;
            lb = tl;
            String ts = a;
            a = b;
            b = ts;
        }

        int[] cost = new int[lb + 1];
        for (int i = 1; i <= la; i += 1) {
            cost[0] = i;
            int prv = i - 1;
            int min = prv;
            for (int j = 1; j <= lb; j += 1) {
                int act = prv + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1);
                cost[j] = min(1 + (prv = cost[j]), 1 + cost[j - 1], act);
                if (prv < min)
                    min = prv;
            }
            if (max >= 0 && min > max)
                return max + 1;
        }
        if (max >= 0 && cost[lb] > max)
            return max + 1;
        return cost[lb];
    }

    private static int min(int... a) {
        int min = Integer.MAX_VALUE;
        for (int i : a)
            if (i < min)
                min = i;
        return min;
    }

    // end Levenshtein distance

    public static void main(String[] args) {
        try {
            // DBpedia.test0();
            // DBpedia.test1();
            // DBpedia.test2();

            System.out.println((float)ld("BM Atlético Madrid", "Atlético Madrid Rugby")/(float)"BM Atlético Madrid".length());


            // Test for the implementation of Levenshtein distance algoritmn used
            // System.out.println(ld("kitten", "kitten") + " " + // 0
            //         ld("kitten", "sitten") + " " + // 1
            //         ld("kitten", "sittes") + " " + // 2
            //         ld("kitten", "sityteng") + " " + // 3
            //         ld("kitten", "sittYing") + " " + // 4
            //         ld("rosettacode", "raisethysword") + " " + // 8
            //         ld("kitten", "kittenaaaaaaaaaaaaaaaaa") + " " + // 17
            //         ld("kittenaaaaaaaaaaaaaaaaa", "kitten") // 17
            // );
            // System.out.println(ld("kitten", "kitten", 3) + " " + // true
            //         ld("kitten", "sitten", 3) + " " + // true
            //         ld("kitten", "sittes", 3) + " " + // true
            //         ld("kitten", "sityteng", 3) + " " + // true
            //         ld("kitten", "sittYing", 3) + " " + // false
            //         ld("rosettacode", "raisethysword", 3) + " " + // false
            //         ld("kitten", "kittenaaaaaaaaaaaaaaaaa", 3) + " " + // false
            //         ld("kittenaaaaaaaaaaaaaaaaa", "kitten", 3) // false
            // );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
