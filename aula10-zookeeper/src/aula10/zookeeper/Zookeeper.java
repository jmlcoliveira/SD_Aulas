package aula10.zookeeper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class Zookeeper implements Watcher {

	private static ZooKeeper _client;
	private final int TIMEOUT = 5000;

	public Zookeeper(String servers) throws Exception {
		this.connect(servers, TIMEOUT);
	}

	public synchronized ZooKeeper client() {
		if (_client == null || !_client.getState().equals(ZooKeeper.States.CONNECTED)) {
			throw new IllegalStateException("ZooKeeper is not connected.");
		}
		return _client;
	}

	private void connect(String host, int timeout) throws IOException, InterruptedException {
		var connectedSignal = new CountDownLatch(1);
		_client = new ZooKeeper(host, timeout, (e) -> {
			if (e.getState().equals(Watcher.Event.KeeperState.SyncConnected)) {
				connectedSignal.countDown();
			}
		});
		connectedSignal.await();
	}

	public String createNode(String path, byte[] data, CreateMode mode) {
		try {
			return client().create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, mode);
		} catch (KeeperException.NodeExistsException x) {
			return path;
		} catch (Exception x) {
			x.printStackTrace();
			return null;
		}
	}

	public List<String> getChildren(String path) {
		try {
			return client().getChildren(path, false);
		} catch (Exception x) {
			x.printStackTrace();
		}
		return Collections.emptyList();
	}

	public List<String> getChildren(String path, Watcher watcher) {
		try {
			return client().getChildren(path, watcher);
		} catch (Exception x) {
			x.printStackTrace();
		}
		return Collections.emptyList();
	}

	public static void main(String[] args) throws Exception {
		String host = args.length == 0 ? "localhost" : args[0];

		var zk = new Zookeeper(host);

		String root = "/feeds";

		var path = zk.createNode(root, new byte[0], CreateMode.PERSISTENT);
		System.err.println( path );
		
		zk.getChildren(root).forEach(System.out::println);
		var newpath = zk.createNode(root + "/r_", new byte[0], CreateMode.EPHEMERAL_SEQUENTIAL);
		System.err.println(	"my path: " + newpath );

		var res = zk.getChildren(root, (e) -> {
			System.out.println("Event received: ");
			zk.getChildren(root).forEach(System.out::println);
		});

		System.err.println( res );

		Thread.sleep(Integer.MAX_VALUE);
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.println("Event received: ");
		System.err.println(event);
	}
}
