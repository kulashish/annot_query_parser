package com.jabong.discovery.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

public class JSolrClient {

	private String host;
	private String port;
	private String core;
	protected SolrClient client;

	public JSolrClient(String host, String port, String core) {
		this.host = host;
		this.port = port;
		this.core = core;
		client = new HttpSolrClient(solrURLBuilder());
	}

	private String solrURLBuilder() {
		return "http://" + host + ":" + port + "/" + SolrClientConstants.SOLR + "/" + core;
	}

	public JSolrResponse query(String qString) throws SolrServerException, IOException {
		SolrQuery solrQuery = new SolrQuery(qString);		

		// System.out.println("Documents count: " + solrResponse.numResults());
		// Iterator<SolrDocument> docsIter = docs.iterator();

		// while (docsIter.hasNext()) {
		// SolrDocument solrDoc = docsIter.next();
		// Iterator<Map.Entry<String, Object>> docIter = solrDoc.iterator();
		// while (docIter.hasNext()) {
		// System.out.println(docIter.next());
		// }
		// }
		return query(solrQuery);
	}

	public JSolrResponse query(String qString, String reqHandler) throws SolrServerException, IOException {
		SolrQuery solrQuery = new SolrQuery(qString);
		solrQuery.setRequestHandler(reqHandler);
		return query(solrQuery);
	}

	public JSolrResponse query(SolrQuery q) throws SolrServerException, IOException {
		return new JSolrResponse(client.query(q));
	}

	public static void main(String[] args) {
		JSolrClient solrClient = new JSolrClient(SolrClientConstants.SOLR_LOCAL_SERVER,
				SolrClientConstants.SOLR_LOCAL_PORT, SolrClientConstants.SOLR_CORE_FASHION);
		try {
			solrClient.query("brand:wrogn");
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}

}
