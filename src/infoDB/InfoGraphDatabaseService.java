package infoDB;

import java.io.Serializable;
import java.util.HashMap;
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
	public static HashMap<String, Long> accesses  = new HashMap<String, Long>();
	public static void log(String key){
		if(accesses.containsKey(key)){
			long val = accesses.get(key)+1;
			accesses.put(key, val);
		}else{
			accesses.put(key, new Long(1));
		}
	}
	
	public static String accessToString(){
		return accesses.toString();
	}
	
	public InfoGraphDatabaseService(String storDir) {
		this.db=new EmbeddedGraphDatabase(storDir);
	}
	
	@Override
	public Transaction beginTx() {
		log("beginTx()");
		return db.beginTx();
	}

	@Override
	public Node createNode() {
		log("createNode()");
		return new InfoNode(db.createNode());
	}

	@Override
	public boolean enableRemoteShell() {
		log("enableRemoteShell()");
		return db.enableRemoteShell();
	}

	@Override
	public boolean enableRemoteShell(Map<String, Serializable> arg0) {
		log("enableRemoteShell(Map<String, Serializable> arg0)");
		return db.enableRemoteShell(arg0);
	}

	// TODO wrapper
	@Override
	public Iterable<Node> getAllNodes() {
		log("getAllNodes()");
		return new infoNodeIteratable(db.getAllNodes());
	}

	@Override
	public Node getNodeById(long arg0) {
		log("getNodeById()");
		return new InfoNode(db.getNodeById(arg0));
	}

	@Override
	public Node getReferenceNode() {
		log("getReferenceNode()");
		return new InfoNode(db.getReferenceNode());
	}

	@Override
	public Relationship getRelationshipById(long arg0) {
		log("getRelationshipById(long arg0)");
		return new InfoRelationship(db.getRelationshipById(arg0));
	}

	@Override
	public Iterable<RelationshipType> getRelationshipTypes() {
		log("getRelationshipTypes()");
		return db.getRelationshipTypes();
	}

	@Override
	public KernelEventHandler registerKernelEventHandler(KernelEventHandler arg0) {
		log("registerKernelEventHandler(arg0)");
		return db.registerKernelEventHandler(arg0);
	}

	@Override
	public <T> TransactionEventHandler<T> registerTransactionEventHandler(
			TransactionEventHandler<T> arg0) {
		log("registerTransactionEventHandler(arg0)");
		return db.registerTransactionEventHandler(arg0);
	}

	@Override
	public void shutdown() {
		log("shutdown())");
		db.shutdown();
	}

	@Override
	public KernelEventHandler unregisterKernelEventHandler(
			KernelEventHandler arg0) {
		log("unregisterKernelEventHandler(arg0)");
		return db.unregisterKernelEventHandler(arg0);
	}

	@Override
	public <T> TransactionEventHandler<T> unregisterTransactionEventHandler(
			TransactionEventHandler<T> arg0) {
		log("unregisterTransactionEventHandler(arg0)");
		return db.unregisterTransactionEventHandler(arg0);
	}

	private class infoNodeIteratable implements Iterable<Node>{
		private Iterable<Node> iter;
		public infoNodeIteratable(Iterable<Node> iter) {
			this.iter = iter;
		}
		
		@Override
		public Iterator<Node> iterator() {
			
			return new infoNodeIterator(iter.iterator());
		}
		
	}
	private class infoNodeIterator implements Iterator<Node>{
		private Iterator<Node> iter;
		public infoNodeIterator(Iterator<Node> iter) {
			this.iter = iter;
		}
		
		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}

		@Override
		public Node next() {
			return new InfoNode(iter.next());
		}

		@Override
		public void remove() {
			iter.remove();	
		}
	}
}
