package fastInfoDB;

import fastInfoDB.InstanceInfo.InfoKey;

import java.util.Iterator;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Traverser;
import org.neo4j.graphdb.Traverser.Order;

@SuppressWarnings("deprecation")
public class InfoNode implements Node, Comparable<Node> {
	private final InfoGraphDatabaseService db;
	private InstanceInfo inf;
	private Node n;
	protected Node unwrap(){
		return n;
	}

	private void log(InfoKey key) {
		inf.log(key);
	}

	public InfoNode(Node n, InfoGraphDatabaseService db) {
		if(n instanceof InfoNode)throw new Error("dont hand and infoNode to an infoNode constructor");
		this.n = n;
		this.db = db;
		this.inf = db.inf;
	}

	@Override
	public Relationship createRelationshipTo(Node arg0, RelationshipType arg1) {
		log(InfoKey.RS_CREATE);
		InfoRelationship infRel = new InfoRelationship(n.createRelationshipTo(((InfoNode)arg0).unwrap(), arg1),db);
		inf.logHop(infRel);
		return infRel;
	}

	@Override
	public void delete() {
		log(InfoKey.N_DELETE);
		n.delete();
	}

	@Override
	public long getId() {
		log(InfoKey.N_GETID);
		return n.getId();
	}

	@Override
	public Iterable<Relationship> getRelationships() {
		log(InfoKey.GetRels);
		return new InfoRelaIteratable(n.getRelationships());
	}

	@Override
	public Iterable<Relationship> getRelationships(RelationshipType... arg0) {
		log(InfoKey.GetRels);

		return new InfoRelaIteratable(n.getRelationships(arg0));
	}

	@Override
	public Iterable<Relationship> getRelationships(Direction arg0) {
		log(InfoKey.GetRels);
		return new InfoRelaIteratable(n.getRelationships(arg0));
	}

	@Override
	public Iterable<Relationship> getRelationships(RelationshipType arg0,
			Direction arg1) {
		log(InfoKey.GetRels);
		return new InfoRelaIteratable(n.getRelationships(arg0, arg1));
	}

	@Override
	public Relationship getSingleRelationship(RelationshipType arg0,
			Direction arg1) {
		log(InfoKey.GetRels);
		InfoRelationship infRel = new InfoRelationship(n.getSingleRelationship(arg0, arg1),db);
		inf.logHop(infRel);
		return new InfoRelationship(n.getSingleRelationship(arg0, arg1),db);
	}

	@Override
	public boolean hasRelationship() {
		log(InfoKey.HasREL);
		return n.hasRelationship();
	}

	@Override
	public boolean hasRelationship(RelationshipType... arg0) {
		log(InfoKey.HasREL);
		return n.hasRelationship(arg0);
	}

	@Override
	public boolean hasRelationship(Direction arg0) {
		log(InfoKey.HasREL);
		return n.hasRelationship(arg0);
	}

	@Override
	public boolean hasRelationship(RelationshipType arg0, Direction arg1) {
		log(InfoKey.HasREL);
		return n.hasRelationship(arg0, arg1);
	}

	@Override
	public Traverser traverse(Order arg0, StopEvaluator arg1,
			ReturnableEvaluator arg2, Object... arg3) {
		return n.traverse(arg0, arg1, arg2, arg3);
	}

	@Override
	public Traverser traverse(Order arg0, StopEvaluator arg1,
			ReturnableEvaluator arg2, RelationshipType arg3, Direction arg4) {
		return n.traverse(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public Traverser traverse(Order arg0, StopEvaluator arg1,
			ReturnableEvaluator arg2, RelationshipType arg3, Direction arg4,
			RelationshipType arg5, Direction arg6) {
		return n.traverse(arg0, arg1, arg2, arg3, arg5, arg6);
	}

	@Override
	public GraphDatabaseService getGraphDatabase() {
		return db;
	}

	@Override
	public Object getProperty(String arg0) {
		log(InfoKey.N_GetProp);
		return n.getProperty(arg0);
	}

	@Override
	public Object getProperty(String arg0, Object arg1) {
		log(InfoKey.N_GetProp);
		return n.getProperty(arg0, arg1);
	}

	@Override
	public Iterable<String> getPropertyKeys() {
		return n.getPropertyKeys();
	}

	@Override
	public Iterable<Object> getPropertyValues() {
		return n.getPropertyValues();
	}

	@Override
	public boolean hasProperty(String arg0) {
		log(InfoKey.N_HasProp);
		return n.hasProperty(arg0);
	}

	@Override
	public Object removeProperty(String arg0) {
		log(InfoKey.N_RemProp);
		return n.removeProperty(arg0);
	}

	@Override
	public void setProperty(String arg0, Object arg1) {
		log(InfoKey.N_SetProp);
		n.setProperty(arg0, arg1);
	}

	private class InfoRelaIteratable implements Iterable<Relationship> {
		private Iterable<Relationship> iter;

		public InfoRelaIteratable(Iterable<Relationship> iter) {
			this.iter = iter;
		}

		@Override
		public Iterator<Relationship> iterator() {

			return new InfoRelaIterator(iter.iterator());
		}

	}

	private class InfoRelaIterator implements Iterator<Relationship> {
		private Iterator<Relationship> iter;

		public InfoRelaIterator(Iterator<Relationship> iter) {
			this.iter = iter;
		}

		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}

		@Override
		public Relationship next() {
			InfoRelationship infRel = new InfoRelationship(iter.next(),db);
			inf.logHop(infRel);
			return infRel;
		}

		@Override
		public void remove() {
			iter.remove();
		}
	}

	@Override
	public boolean equals(Object obj) {
		return ((InfoNode) obj).unwrap().getId() == n.getId();
	}
	
	@Override
	public int compareTo(Node arg0) {
		long ourId = this.getId(), theirId = n.getId();

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
