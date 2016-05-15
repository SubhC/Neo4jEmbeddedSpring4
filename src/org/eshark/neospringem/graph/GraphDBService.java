package org.eshark.neospringem.graph;

import java.io.File;

import org.eshark.neospringem.domin.Error;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.shell.ShellSettings;

public final class GraphDBService {

	private static final GraphDBService INSTANCE = new GraphDBService();
	private GraphDatabaseService graphDB;
	private final static File DB_DIR = new File("/Users/Subhasish/Documents/eShark/configured/Neo4j-DataBases/D3.graph.db"); 

	private GraphDBService() {
		
	}

	public static GraphDBService getInstance() {
		return INSTANCE;
	}

	public Error startDataBase()
	{
		Error error = new Error();
		try 
		{
			graphDB = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(DB_DIR).
                  setConfig(GraphDatabaseSettings.allow_store_upgrade,"true")
                  .setConfig(GraphDatabaseSettings.dump_configuration,"true")
                  .setConfig(GraphDatabaseSettings.pagecache_memory,"512M")
                  .setConfig(GraphDatabaseSettings.auth_enabled,"false")
                  .setConfig(GraphDatabaseSettings.array_block_size,"300")
                  .setConfig(ShellSettings.remote_shell_enabled, "true")
                  .setConfig(ShellSettings.remote_shell_port, "5555")
                  
                  .newGraphDatabase();
			
			registerShutdownHook(graphDB);
			
			System.out.println("  DB Detaisl: "+ graphDB.toString());
			
		} catch (Exception exception) 
		{
			System.out.println("e = " + exception.getMessage());
			exception.printStackTrace();
			error.setException(exception);
		}
		return error;
	}

	public Error stopDataBase()
	{
		Error error = new Error();
		try 
		{
			graphDB.shutdown();
			
		} catch (Exception exception) 
		{
			System.out.println("e = " + exception.getMessage());
			exception.printStackTrace();
			error.setException(exception);
		}
		return error;
	}

	public boolean isDBRunning()
	{
		return graphDB.isAvailable(30);
	}
	private void registerShutdownHook( final GraphDatabaseService graphDb )
	{
	    // Registers a shutdown hook for the Neo4j instance so that it
	    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
	    // running application).
	    Runtime.getRuntime().addShutdownHook( new Thread()
	    {
	        @Override
	        public void run()
	        {
	            graphDb.shutdown();
	        }
	    } );
	}
	
	public GraphDatabaseService getUnderlyingDatabase()
	{
		return graphDB;
	}
}
