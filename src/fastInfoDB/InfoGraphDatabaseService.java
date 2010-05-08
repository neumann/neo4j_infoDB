package fastInfoDB;

import fastInfoDB.InstanceInfo.InfoKey;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.event.KernelEventHandler;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class InfoGraphDatabaseService implements GraphDatabaseService {
	private GraphDatabaseService db;
	InstanceInfo inf;
	
	private void log(InfoKey key) {
		inf.log(key);
	}

	public InstanceInfo getInstanceInfo(){
		return inf.takeSnapshot();
	}
	
	public String accessToString() {
		return inf.toString();
	}

	public InfoGraphDatabaseService(String storDir) {
		this.db = new EmbeddedGraphDatabase(storDir);
	}

	@Override
	public Transaction beginTx() {
		log(InfoKey.TRANSACTION);
		return db.beginTx();
	}

	@Override
	public Node createNode() {
		log(InfoKey.N_CREATE);
		return new InfoNode(db.createNode(),this);
	}

	@Override
	public boolean enableRemoteShell() {
		return db.enableRemoteShell();
	}

	@Override
	public boolean enableRemoteShell(Map<String, Serializable> arg0) {
		return db.enableRemoteShell(arg0);
	}

	@Override
	public Iterable<Node> getAllNodes() {
		log(InfoKey.GETALLNODES);
		return new InfoNodeIteratable(db.getAllNodes(), this);
	}

	@Override
	public Node getNodeById(long arg0) {
		log(InfoKey.NODE_BYID);
		return new InfoNode(db.getNodeById(arg0),this);
	}

	@Override
	public Node getReferenceNode() {
		return new InfoNode(db.getReferenceNode(),this);
	}

	@Override
	public Relationship getRelationshipById(long arg0) {
		log(InfoKey.REL_BYID);
		InfoRelationship infRel = new InfoRelationship(db.getRelationshipById(arg0),this);
		inf.logHop(infRel);
		return infRel;
	}

	@Override
	public Iterable<RelationshipType> getRelationshipTypes() {
		return db.getRelationshipTypes();
	}

	@Override
	public KernelEventHandler registerKernelEventHandler(KernelEventHandler arg0) {
		return db.registerKernelEventHandler(arg0);
	}

	@Override
	public <T> TransactionEventHandler<T> registerTransactionEventHandler(
			TransactionEventHandler<T> arg0) {
		return db.registerTransactionEventHandler(arg0);
	}

	@Override
	public void shutdown() {
		log(InfoKey.SHUTDOWN);
		db.shutdown();
	}

	@Override
	public KernelEventHandler unregisterKernelEventHandler(
			KernelEventHandler arg0) {
		return db.unregisterKernelEventHandler(arg0);
	}

	@Override
	public <T> TransactionEventHandler<T> unregisterTransactionEventHandler(
			TransactionEventHandler<T> arg0) {
		return db.unregisterTransactionEventHandler(arg0);
	}

	private class InfoNodeIteratable implements Iterable<Node> {
		private Iterable<Node> iter;
		private InfoGraphDatabaseService db;

		public InfoNodeIteratable(Iterable<Node> iter, InfoGraphDatabaseService db) {
			this.iter = iter;
			this.db = db;
		}

		@Override
		public Iterator<Node> iterator() {
			return new InfoNodeIterator(iter.iterator(), db);
		}

	}

	private class InfoNodeIterator implements Iterator<Node> {
		private Iterator<Node> iter;
		private InfoGraphDatabaseService db;
		
		public InfoNodeIterator(Iterator<Node> iter, InfoGraphDatabaseService db) {
			this.iter = iter;
			this.db = db;
		}

		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}

		@Override
		public Node next() {
			return new InfoNode(iter.next(),db);
		}

		@Override
		public void remove() {
			iter.remove();
		}
	}
}
