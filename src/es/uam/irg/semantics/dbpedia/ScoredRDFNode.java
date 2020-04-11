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

import com.hp.hpl.jena.rdf.model.RDFNode;
import java.net.URLDecoder;

/**
 * ScoredRDFNode
 * 
 * Stores a [RDF node, score] pair, where the score is a real number assigned to
 * the node in certain application, e.g., a relevance value assigned from a 
 * search engine for a given query, or a rating assigned by a user in a 
 * recommender system.
 * 
 * @author Ivan Cantador, ivan.cantador@uam.es
 * @version 1.0 - 16/03/2016
 */
public class ScoredRDFNode implements Comparable<ScoredRDFNode> {

    private RDFNode node;
    private double score;

    /**
     * Constructor of a [RDF node, score] pair.
     * 
     * @param node - the RDF node stored in the pair
     * @param score - the real number representing the score stored in the pair
     */
    public ScoredRDFNode(RDFNode node, double score) {
        this.node = node;
        this.score = score;
    }

    /**
     * Returns the node of the pair.
     * 
     * @return the RDFNode node attribute
     */
    public RDFNode getNode() {
        return this.node;
    }

    /**
     * Returns the score of the pair.
     * 
     * @return the double score attribute
     */
    public double getScore() {
        return this.score;
    }

    /**
     * Returns a unique identifier for the stored node.
     * 
     * @return the double score attribute
     */
    private String getId() {
        String id = "";
        try {
            if (this.node.isURIResource()) {
                id = URLDecoder.decode(this.node.asResource().getURI(), "UTF-8");
            } else {
                id = this.node.asLiteral().getString();
            }
        } catch (Exception e) {
        }
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ScoredRDFNode other = (ScoredRDFNode) obj;
        if (this.getId().equals(other.getId())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        String id = this.getId();
        return id.hashCode();
    }

    @Override
    public int compareTo(ScoredRDFNode node) {
        if (node != null) {
            double cmp = node.getScore() - this.getScore();
            if (cmp < 0) {
                return -1;
            } else if (cmp > 0) {
                return 1;
            }
            return 0;
        }
        return -1;
    }

    @Override
    public String toString() {
        return "ScoredRDFNode{" + "node=" + this.getNode() + ", score=" + this.getScore() + '}';
    }
}
