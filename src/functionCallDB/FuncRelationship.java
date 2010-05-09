package functionCallDB;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

public class FuncRelationship implements Relationship, Comparable<Relationship> {
	@Override
	public String toString() {
		return "InfoRelationship[" + rs.getId() + "]";
	}

	private Relationship rs;

	protected Relationship unwrap() {
		return rs;
	}

	private void log(String key) {
		FuncGraphDatabaseService.log(key);
	}

	public FuncRelationship(Relationship rs) {
		if (rs instanceof FuncRelationship)
			throw new Error("dont hand and inforel to an inforel constructor");
		this.rs = rs;
	}

	@Override
	public void delete() {
		log("rs.delete()");
		rs.delete();
	}

	@Override
	public Node getEndNode() {
		log("rs.getEndNode()");
		return new FuncNode(rs.getEndNode());
	}

	@Override
	public long getId() {
		log("rs.getId()");
		return rs.getId();
	}

	@Override
	public Node[] getNodes() {
		log("getNodes()");
		Node[] n = rs.getNodes();
		FuncNode[] res = new FuncNode[n.length];
		for (int i = 0; i < n.length; i++) {
			res[i] = new FuncNode(n[i]);
		}
		return res;
	}

	@Override
	public Node getOtherNode(Node arg0) {
		log("getOtherNode(arg0)");
		return new FuncNode(rs.getOtherNode(((FuncNode) arg0).unwrap()));
	}

	@Override
	public Node getStartNode() {
		log("getStartNode()");
		return new FuncNode(rs.getStartNode());
	}

	@Override
	public RelationshipType getType() {
		log("getType()");
		return rs.getType();
	}

	@Override
	public boolean isType(RelationshipType arg0) {
		log("isType(arg0)");
		return rs.isType(arg0);
	}

	@Override
	public GraphDatabaseService getGraphDatabase() {
		throw new Error("not implemented");
	}

	@Override
	public Object getProperty(String arg0) {
		log("r.getProperty(arg0)");
		return rs.getProperty(arg0);
	}

	@Override
	public Object getProperty(String arg0, Object arg1) {
		log("r.getProperty(arg0, arg1)");
		return rs.getProperty(arg0, arg1);
	}

	@Override
	public Iterable<String> getPropertyKeys() {
		log("r.getPropertyKeys()");
		return rs.getPropertyKeys();
	}

	@Override
	public Iterable<Object> getPropertyValues() {
		log("r.getPropertyValues()");
		return getPropertyValues();
	}

	@Override
	public boolean hasProperty(String arg0) {
		log("r.hasProperty(arg0)");
		return rs.hasProperty(arg0);
	}

	@Override
	public Object removeProperty(String arg0) {
		log("r.removeProperty(arg0)");
		return rs.removeProperty(arg0);
	}

	@Override
	public void setProperty(String arg0, Object arg1) {
		log("r.setProperty(arg0, arg1)");
		rs.setProperty(arg0, arg1);
	}

	@Override
	public boolean equals(Object obj) {
		log("r.equals(Object obj)");
		if (!(obj instanceof FuncRelationship)) {
			return false;
		}
		return ((FuncRelationship) obj).unwrap().getId() == rs.getId();
	}

	@Override
	public int compareTo(Relationship arg0) {
		log("r.compareTo(Node arg0)");
		long ourId = this.getId();
		long theirId = ((FuncRelationship) arg0).unwrap().getId();

		if (ourId < theirId) {
			return -1;
		} else if (ourId > theirId) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int hashCode() {
		log("r.hashCode()");
		return (int) rs.getId();
	}

}
