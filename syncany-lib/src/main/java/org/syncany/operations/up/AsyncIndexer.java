package org.syncany.operations.up;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Queue;

import org.syncany.chunk.Deduper;
import org.syncany.config.Config;
import org.syncany.database.DatabaseVersion;
import org.syncany.util.Consumer;

/**
 * @author Tim Hegeman
 */
public class AsyncIndexer implements Runnable {

	private final Indexer indexer;
	private final List<File> files;
	private final Consumer<DatabaseVersion> databaseVersionListener;
	private boolean done;

	public AsyncIndexer(Config config, Deduper deduper, List<File> files, Queue<DatabaseVersion> queue) {
		this.files = files;
		this.databaseVersionListener = new DatabaseVersionConsumer(queue);
		this.indexer = new Indexer(config, deduper);
		this.done = false;
	}

	@Override
	public void run() {
		try {
			indexer.index(files, databaseVersionListener);
		}
		catch (IOException e) {
			// TODO: Store this exception as a "result"?
			e.printStackTrace();
		}
		// Signal end-of-stream.
		databaseVersionListener.process(null);
		this.done = true;
	}

}
