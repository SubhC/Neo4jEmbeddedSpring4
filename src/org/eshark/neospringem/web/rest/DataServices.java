package org.eshark.neospringem.web.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.eshark.neospringem.graph.GraphDBService;
import org.eshark.neospringem.graph.domain.TreeNode;
import org.eshark.neospringem.graph.label.Labels;
import org.eshark.neospringem.graph.relation.RelationshipTypes;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.traversal.TraversalDescription;

@Path("/DataServices")
public class DataServices {
	
	@GET
	@javax.ws.rs.Path("/findSwitch/{nsn}/")
	@Produces({ "application/json" })
	// hot to call me
	//http://localhost:8080/Neo4jEmbeddedSpring4/rest/DataServices/findSwitch/3230-01-574-9904
	public Response findNode(@PathParam("nsn") final String nsn)
			throws IOException {
		//Get the GraphDatabase Service Instance
    	//Better use Spring than this Singleton Instance
    	
    	GraphDatabaseService db =  GraphDBService.getInstance().getUnderlyingDatabase();
		System.out.println(" Attribute Name To Search: " + nsn);
		String attrubute = "NSN";
		Map<String, Object> result;
		ObjectMapper objectMapper = new ObjectMapper();
		
		try (Transaction tx = db.beginTx()) {
			final Node swtch = db.findNode(Labels.Switch, attrubute, nsn);
			if (swtch != null)
				result = swtch.getAllProperties();
			else
				result = new HashMap<String, Object>();
			tx.success();
		}

		return Response.ok().entity(objectMapper.writeValueAsString(result)).build();
	}
	
	
	
    
    @GET
	@javax.ws.rs.Path("/Hierarchy/{nsn}/")
	@Produces(MediaType.APPLICATION_JSON)
	// hot to call me
	//http://localhost:8080/Neo4jEmbeddedSpring4/rest/DataServices/Hierarchy/3230-01-574-9904    
	public Response findConnecTedTos(@PathParam("nsn") final String nsn) throws IOException {
		//Get the GraphDatabase Service Instance
    	//Better use Spring than this Singleton Instance
    	
    	GraphDatabaseService db =  GraphDBService.getInstance().getUnderlyingDatabase();
		System.out.println(" Attribute Name To Search: " + nsn);
		String attrubute = "NSN";
        ObjectMapper objectMapper = new ObjectMapper();
    	
        HashMap<Long, TreeNode> treeNodeMap = new HashMap<Long, TreeNode>();
		
		TreeNode rootNode = null;
		
		try (Transaction tx = db.beginTx()) {
			final Node swtch = db.findNode(Labels.Switch, attrubute, nsn);
			if (swtch != null) {
				TraversalDescription td = db.traversalDescription().depthFirst()
						.expand(PathExpanders.forTypeAndDirection(RelationshipTypes.CONNECTED_TO, Direction.OUTGOING));
				
				for (org.neo4j.graphdb.Path directoryPath : td.traverse(swtch)) 
				{
					Iterable<Relationship> connectedTos = directoryPath.endNode().getRelationships(Direction.OUTGOING,RelationshipTypes.CONNECTED_TO);
					
					if (connectedTos != null) 
					{
						for(Relationship connectedTo : connectedTos)
						{
							//For the Current Relationship
							//get the start node as parent
							Node parentNode = connectedTo.getStartNode();
							Long parentNodeId = parentNode.getId();
							TreeNode parentTreeNode = treeNodeMap.get(parentNodeId);
							
							if(parentTreeNode == null)
							{
								////Populate the Parent Details
								parentTreeNode = new TreeNode(parentNode.getProperty("CN", "NoName").toString());
								if(rootNode == null)
									rootNode = parentTreeNode;
								//Add to the linear HashMap for subsequent searches
								treeNodeMap.put(parentNodeId, parentTreeNode);
							}
								
							//For the Current Relationship get the end node Children
							Node childNode = connectedTo.getEndNode();
							Long childNodeId = childNode.getId();
							TreeNode childTreeNode = treeNodeMap.get(childNodeId);
							
							if(childTreeNode == null)
							{
								childTreeNode = new TreeNode(childNode.getProperty("CN", "NoName").toString());
								treeNodeMap.put(childNodeId, childTreeNode);
								parentTreeNode.setChildren(childTreeNode);
							}
						}
				}
			}
			tx.success();
		}
		}
		/*
		 System.out.println("JSON: " + objectMapper.writeValueAsString(rootNode));
		 System.out.println("LinearHashMap: " + objectMapper.writeValueAsString(treeNodeMap ));
		 */
		
		return Response.ok().entity(objectMapper.writeValueAsString(rootNode)).build();
	
	}
}
