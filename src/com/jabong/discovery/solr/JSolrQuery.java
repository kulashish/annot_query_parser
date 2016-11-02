package com.jabong.discovery.solr;

import org.apache.solr.client.solrj.SolrQuery;

public class JSolrQuery {

	private String qString;
	private String rHandler;
	private SolrQuery sQuery;

	public JSolrQuery(String q) {
		qString = q;
	}

	public JSolrQuery(String q, String rHandler) {
		this(q);
		this.rHandler = rHandler;
	}

	public void query() {
				
	}

}
