package infoDB;

import infoDB.InstanceInfo.InfoKey;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

public class InfoRelationship implements Relationship, Comparable<Relationship>{
	private Relationship rs;
	private final InfoGraphDatabaseService db;
	private InstanceInfo inf;
	
	protected Relationship unwrap() {
		return rs;
	}

	private void log(InfoKey key) {
		inf.log(key);
	}

	public InfoRelationship(Relationship rs, InfoGraphDatabaseService db) {
		if (rs instanceof InfoRelationship)
			throw new Error("dont hand and inforel to an inforel constructor");
		this.rs = rs;
		this.db = db;
		this.inf = db.inf;
	}

	@Override
	public void delete() {
		log(InfoKey.rs_delete);
		rs.delete();
	}

	@Override
	public Node getEndNode() {
		log(InfoKey.Traffic);
		return new InfoNode(rs.getEndNode(), db);
	}

	@Override
	public long getId() {
		log(InfoKey.Traffic);
		return rs.getId();
	}

	@Override
	public Node[] getNodes() {
		log(InfoKey.Traffic);
		Node[] n = rs.getNodes();
		InfoNode[] res = new InfoNode[n.length];
		for (int i = 0; i < n.length; i++) {
			res[i] = new InfoNode(n[i], db);
		}
		return res;
	}

	@Override
	public Node getOtherNode(Node arg0) {
		log(InfoKey.Traffic);
		return new InfoNode(rs.getOtherNode(((InfoNode) arg0).unwrap()), db);
	}

	@Override
	public Node getStartNode() {
		log(InfoKey.Traffic);
		return new InfoNode(rs.getStartNode(),db);
	}

	@Override
	public RelationshipType getType() {
		log(InfoKey.Traffic);
		return rs.getType();
	}

	@Override
	public boolean isType(RelationshipType arg0) {
		log(InfoKey.Traffic);
		return rs.isType(arg0);
	}

	@Override
	public GraphDatabaseService getGraphDatabase() {
		return db;
	}

	@Override
	public Object getProperty(String arg0) {
		log(InfoKey.Traffic);
		return rs.getProperty(arg0);
	}

	@Override
	public Object getProperty(String arg0, Object arg1) {
		log(InfoKey.Traffic);
		return rs.getProperty(arg0, arg1);
	}

	@Override
	public Iterable<String> getPropertyKeys() {
		return rs.getPropertyKeys();
	}

	@Override
	public Iterable<Object> getPropertyValues() {
		return getPropertyValues();
	}

	@Override
	public boolean hasProperty(String arg0) {
		log(InfoKey.Traffic);
		return rs.hasProperty(arg0);
	}

	@Override
	public Object removeProperty(String arg0) {
		log(InfoKey.Traffic);
		return rs.removeProperty(arg0);
	}

	@Override
	public void setProperty(String arg0, Object arg1) {
		log(InfoKey.Traffic);
		rs.setProperty(arg0, arg1);
	}

	@Override
	public boolean equals(Object obj) {
		return ((InfoRelationship) obj).unwrap().getId() == rs.getId();
	}
	
	@Override
	public int compareTo(Relationship r) {
		int ourId = (int) this.getId(), theirId = (int) r.getId();

        if ( ourId < theirId )
        {
            return -1;
        }
        else if ( ourId > theirId )
        {
            return 1;
        }
        else
        {
            return 0;
        }
	}
}
