javac -classpath "../lib/apache-jena-2.7.4/*" es/uam/irg/semantics/dbpedia/DBpedia.java es/uam/irg/semantics/dbpedia/ScoredRDFNode.java
java -classpath "/Users/cabelotaina/apps/web-mining/dbpedia/lib/apache-jena-2.7.4/*:." es.uam.irg.semantics.dbpedia.DBpedia


PREFIX dbr: <http://dbpedia.org/resource/> 
PREFIX dbp: <http://dbpedia.org/property/> 
PREFIX dbo: <http://dbpedia.org/ontology/>

SELECT ?work ?writer ?previousWork ?subsequentWork
WHERE {
?work dbo:series dbr:Star_Wars_canon .
?work dbo:author ?writer .
OPTIONAL { ?work dbp:precededBy ?previousWork } . 
OPTIONAL { ?work dbp:followedBy ?subsequentWork } .
}


1.1) complete


PREFIX dbp: <http://dbpedia.org/property/>

SELECT ?o WHERE { 
{ <http://dbpedia.org/resource/Autonomous_University_of_Madrid> dct:subject ?o . }
UNION
{ <http://dbpedia.org/resource/Autonomous_University_of_Madrid> rdf:type ?o . }
UNION 
{ <http://dbpedia.org/resource/Autonomous_University_of_Madrid> dbp:wordnet_type ?o . }
UNION
{ <http://dbpedia.org/resource/Category:Universities_in_Madrid> ?p ?o . }
}

1.2) partial

PREFIX dbp: <http://dbpedia.org/property/>
PREFIX dct: <http://purl.org/dc/terms/>

SELECT ?subject WHERE { 
{ <http://dbpedia.org/resource/Category:Universities_in_Madrid> ^dct:subject ?subject .}
UNION
{ <http://dbpedia.org/resource/Category:Universities_in_Madrid> a ?subject . }
UNION 
{ <http://dbpedia.org/resource/Category:Universities_in_Madrid> dbp:wordnet_type ?subject . }
}

2) ?

FILTER regex(str(?Concept), "atlético madrid")

 ?Concept bif:contains "'atlético madrid*'"


 select distinct ?entity where {
  [] rdfs:label ?entity .
}
