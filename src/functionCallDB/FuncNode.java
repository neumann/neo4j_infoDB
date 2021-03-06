package functionCallDB;

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
public class FuncNode implements Node, Comparable<Node> {
	@Override
	public String toString() {
		return "InfoNode[" + n.getId() + "]";
	}

	private Node n;

	protected Node unwrap() {
		return n;
	}

	private void log(String key) {
		FuncGraphDatabaseService.log(key);
	}

	public FuncNode(Node n) {
		if (n instanceof FuncNode)
			throw new Error("dont hand and infoNode to an infoNode constructor");
		this.n = n;
	}

	@Override
	public Relationship createRelationshipTo(Node arg0, RelationshipType arg1) {
		log("createRelationshipTo(Node arg0, RelationshipType arg1)");
		return new FuncRelationship(n.createRelationshipTo(((FuncNode) arg0)
				.unwrap(), arg1));
	}

	@Override
	public void delete() {
		log("n.delete()");
		n.delete();
	}

	@Override
	public long getId() {
		log("n.getId()");
		return n.getId();
	}

	@Override
	public Iterable<Relationship> getRelationships() {
		log("getRelationships()");
		return new InfoRelaIteratable(n.getRelationships());
	}

	@Override
	public Iterable<Relationship> getRelationships(RelationshipType... arg0) {
		log("getRelationships(RelationshipType... arg0)");

		return new InfoRelaIteratable(n.getRelationships(arg0));
	}

	@Override
	public Iterable<Relationship> getRelationships(Direction arg0) {
		log("getRelationships(Direction arg0)");
		return new InfoRelaIteratable(n.getRelationships(arg0));
	}

	@Override
	public Iterable<Relationship> getRelationships(RelationshipType arg0,
			Direction arg1) {
		log("getRelationships(RelationshipType arg0,Direction arg0)");
		return new InfoRelaIteratable(n.getRelationships(arg0, arg1));
	}

	@Override
	public Relationship getSingleRelationship(RelationshipType arg0,
			Direction arg1) {
		log("getSingleRelationship(RelationshipType arg0,Direction arg1)");
		return new FuncRelationship(n.getSingleRelationship(arg0, arg1));
	}

	@Override
	public boolean hasRelationship() {
		log("hasRelationship()");
		return n.hasRelationship();
	}

	@Override
	public boolean hasRelationship(RelationshipType... arg0) {
		log("hasRelationship(RelationshipType... arg0)");
		return n.hasRelationship(arg0);
	}

	@Override
	public boolean hasRelationship(Direction arg0) {
		log("hasRelationship(Direction arg0)");
		return n.hasRelationship(arg0);
	}

	@Override
	public boolean hasRelationship(RelationshipType arg0, Direction arg1) {
		log("hasRelationship(RelationshipType arg0, Direction arg1) ");
		return n.hasRelationship(arg0, arg1);
	}

	@Override
	public Traverser traverse(Order arg0, StopEvaluator arg1,
			ReturnableEvaluator arg2, Object... arg3) {
		log("traverse(Order arg0, StopEvaluator arg1,ReturnableEvaluator arg2, Object... arg3)");
		return n.traverse(arg0, arg1, arg2, arg3);
	}

	@Override
	public Traverser traverse(Order arg0, StopEvaluator arg1,
			ReturnableEvaluator arg2, RelationshipType arg3, Direction arg4) {

		log("traverse(Order arg0, StopEvaluator arg1, ReturnableEvaluator arg2, RelationshipType arg3, Direction arg4)");
		return n.traverse(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public Traverser traverse(Order arg0, StopEvaluator arg1,
			ReturnableEvaluator arg2, RelationshipType arg3, Direction arg4,
			RelationshipType arg5, Direction arg6) {
		log("traverse(Order arg0, StopEvaluator arg1,ReturnableEvaluator arg2, RelationshipType arg3, Direction arg4, RelationshipType arg5, Direction arg6)");

		return n.traverse(arg0, arg1, arg2, arg3, arg5, arg6);
	}

	@Override
	// TODO should not be used otherwise the counter wount work anymore
	public GraphDatabaseService getGraphDatabase() {
		throw new Error("not supportet");
	}

	@Override
	public Object getProperty(String arg0) {
		log("getProperty(String arg0)");
		return n.getProperty(arg0);
	}

	@Override
	public Object getProperty(String arg0, Object arg1) {
		log("getProperty(String arg0, Object arg1)");
		return n.getProperty(arg0, arg1);
	}

	@Override
	public Iterable<String> getPropertyKeys() {
		log("getPropertyKeys()");
		return n.getPropertyKeys();
	}

	@Override
	public Iterable<Object> getPropertyValues() {
		log("getPropertyValues()");
		return n.getPropertyValues();
	}

	@Override
	public boolean hasProperty(String arg0) {
		log("hasProperty(String arg0)");
		return n.hasProperty(arg0);
	}

	@Override
	public Object removeProperty(String arg0) {
		log("removeProperty(String arg0)");
		return n.removeProperty(arg0);
	}

	@Override
	public void setProperty(String arg0, Object arg1) {
		log("setProperty(String arg0, Object arg1)");
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
			return new FuncRelationship(iter.next());
		}

		@Override
		public void remove() {
			iter.remove();
		}
	}

	@Override
	public boolean equals(Object obj) {
		log("n.equals(Object obj)");
		if (!(obj instanceof FuncNode)) {
			return false;
		}
		return ((FuncNode) obj).unwrap().getId() == n.getId();
	}

	@Override
	public int compareTo(Node arg0) {
		log("n.compareTo(Node arg0)");
		long ourId = this.getId();
		long theirId = ((FuncNode) arg0).unwrap().getId();

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
		log("n.hashCode()");
		return (int) n.getId();
	}

}
